package ru.markin.vkutils.common.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java8.util.Optional;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import retrofit2.Response;
import ru.markin.vkutils.common.network.gson.AccessToken;
import ru.markin.vkutils.common.network.gson.CheckToken;
import ru.markin.vkutils.common.network.gson.DialogInfo;
import ru.markin.vkutils.common.network.gson.DialogList;
import ru.markin.vkutils.common.network.gson.History;
import ru.markin.vkutils.common.network.gson.SearchList;
import ru.markin.vkutils.common.network.util.DataObject;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static java.util.Collections.emptyList;

public class ApiService {

    private static final String V5_62 = "5.62";
    private static final String V5_63 = "5.63";

    private final RestService restService;
    private final Context context;

    public ApiService(final RestService restService, final Context context) {
        this.restService = restService;
        this.context = context;
    }

    /**
     * Check that token is valid
     *
     * @param token     current token
     * @param secretKey secret key
     * @param appId     application id
     * @return observable of token checking
     */
    public Observable<Boolean> checkTokenObservable(@NonNull final String token,
                                                    @NonNull final String secretKey,
                                                    final int appId) {
        if (token.isEmpty()) {
            Observable.just(false);
        }

        return this.restService.getAccessToken(appId, secretKey, V5_62, "client_credentials")
                .subscribeOn(Schedulers.io())
                .filter(Response::isSuccessful)
                .map(Response::body)
                .map(AccessToken::getAccessToken)
                .flatMap(accessToken -> this.restService.checkToken(token, secretKey, accessToken, V5_62))
                .filter(Response::isSuccessful)
                .map(Response::body)
                .map(CheckToken::getResponse)
                .map(CheckToken.Response::getSuccess)
                .map(success -> success == 1)
                .retry(3);
    }

    /**
     * Get dialogs response observable
     *
     * @param token  current user's token
     * @param offset offset
     * @return dialogs observable
     */
    public Observable<Response<DialogList>> dialogsResponseObservable(@NonNull final String token,
                                                                      final int offset) {
        return this.restService
                .getDialogs(offset, token, V5_62)
                .subscribeOn(Schedulers.io());
    }

    /**
     * Check network connection
     *
     * @return network availability
     */
    public boolean isOnline() {
        return Optional.of(CONNECTIVITY_SERVICE)
                .map(this.context::getSystemService)
                .map(ConnectivityManager.class::cast)
                .map(ConnectivityManager::getActiveNetworkInfo)
                .map(NetworkInfo::isConnectedOrConnecting)
                .orElse(false);
    }

    /**
     * Get dialogs observable
     *
     * @param token  current user's token
     * @param offset offset to load
     * @return dialogs
     */
    public Observable<DialogList> getDialogsListObservable(@NonNull final String token,
                                                           final int offset) {
        return this.restService.getDialogs(offset, token, V5_62)
                .subscribeOn(Schedulers.io())
                .flatMap(dialogListResponse -> {
                    if (dialogListResponse.isSuccessful()) {
                        return Observable.just(dialogListResponse.body());
                    } else {
                        throw new RuntimeException("'method/execute.getDialogs' is not successful!");
                    }
                });
    }

    /**
     * Get observable to search dialog
     *
     * @param token current user's token
     * @param query search text
     * @return items observable
     */
    public Observable<SearchList.Item> getSearchObservable(@NonNull final String token,
                                                           @NonNull final String query) {
        return this.restService.searchDialogs(query, token, V5_63)
                .subscribeOn(Schedulers.io())
                .filter(Response::isSuccessful)
                .map(Response::body)
                .map(SearchList::getItems)
                .flatMap(Observable::fromIterable)
                .retry(3);
    }

    /**
     * Get the first date of message in dialog
     *
     * @param token current user's token
     * @param id    companion's id
     * @return first date of message
     */
    public long getFirstDate(final String token, final int id) {
        return getDate(token, id, Direction.FIRST);
    }

    /**
     * Get the last date of message in dialog
     *
     * @param token current user's token
     * @param id    companion's id
     * @return last date of message
     */
    public long getLastDate(final String token, final int id) {
        return getDate(token, id, Direction.LAST);
    }

    /**
     * Get date of the last/first message in dialog
     *
     * @param token     current user's token
     * @param id        companion's id
     * @param direction direction
     * @return date in long
     */
    private long getDate(@NonNull final String token,
                         final int id,
                         @NonNull final Direction direction) {
        try {
            final int rev = calculateRev(direction);
            final Response<History> response = this.restService
                    .getHistory(1, id, rev, token, V5_63)
                    .execute();

            return Optional.of(response)
                    .filter(Response::isSuccessful)
                    .map(Response::body)
                    .map(History::getResponse)
                    .map(History.Response::getItems)
                    .filter(items -> items.size() > 0)
                    .map(items -> items.get(0))
                    .map(History.Item::getDate)
                    .map(date -> date * 1000)
                    .orElse(-1L);
        } catch (final IOException ignore) {
        }
        return -1L;
    }

    /**
     * Get first date of message and count of message from dialog
     * by companion's id
     *
     * @param token current user's token
     * @param id    companion's id
     * @return data object with first date and count
     */
    public DataObject<Long, Integer> getFirstDateWithCount(@NonNull final String token,
                                                           final int id) {
        return getDateWithCount(token, id, Direction.FIRST);
    }

    /**
     * Get last date of message and count of message from dialog
     * by companion's id
     *
     * @param token current user's token
     * @param id    companion's id
     * @return data object with last date and count
     */
    public DataObject<Long, Integer> getLastDateWithCount(@NonNull final String token,
                                                          final int id) {
        return getDateWithCount(token, id, Direction.LAST);
    }

    /**
     * Get date of message and count of message from dialog
     * by companion's id
     *
     * @param token     current user's token
     * @param id        companion's id
     * @param direction kind of message
     * @return data object with date and count
     */
    private DataObject<Long, Integer> getDateWithCount(@NonNull final String token,
                                                       final int id,
                                                       @NonNull final Direction direction) {
        try {
            final int rev = calculateRev(direction);
            final Response<History> response = this.restService
                    .getHistory(1, id, rev, token, V5_63)
                    .execute();

            if (response.isSuccessful()) {
                //last date from response
                final Long date = Optional.of(response)
                        .map(Response::body)
                        .map(History::getResponse)
                        .map(History.Response::getItems)
                        .filter(items -> items.size() > 0)
                        .map(items -> items.get(0))
                        .map(History.Item::getDate)
                        .map(time -> time * 1000)
                        .orElse(-1L);

                //count from response
                final Integer count = Optional.of(response)
                        .map(Response::body)
                        .map(History::getResponse)
                        .map(History.Response::getCount)
                        .orElse(-1);

                return DataObject.of(date, count);
            }
        } catch (final IOException ignore) {
        }
        return DataObject.of(-1L, -1);
    }

    /**
     * Get reverse value by direction
     *
     * @param direction direction
     * @return reverse value
     */
    private int calculateRev(@NonNull final Direction direction) {
        switch (direction) {
            case LAST:
                return 0;
            case FIRST:
                return 1;
            default:
                throw new IllegalArgumentException("Unknown enum: " + direction.name());
        }
    }

    /**
     * Get information about dialog
     *
     * @param token  current user's token
     * @param id     companion's id
     * @param offset offset to load
     * @return dialog info
     */
    @Nullable
    private DialogInfo getDialogInfo(@NonNull final String token, final int id,
                                     final int offset) {
        try {
            final Response<DialogInfo> response = this.restService
                    .getDialogInfo(offset, id, token, V5_63)
                    .execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (final IOException ignore) {
        }
        return null;
    }

    /**
     * Get data object where the first value is incoming count
     * and the second value is outgoing count
     *
     * @param token  current user's token
     * @param id     companion's id
     * @param offset offset to load
     * @return data object with counts
     */
    @Nullable
    public DataObject<Long, Long> getIncomingAndOutgoingCount(@NonNull final String token,
                                                              final int id,
                                                              final int offset) {
        //count messages
        final Map<Integer, Long> counts = Optional
                .ofNullable(getDialogInfo(token, id, offset))
                .map(DialogInfo::getOutputs)
                .map(StreamSupport::stream)
                .orElse(StreamSupport.stream(emptyList()))
                .flatMap(StreamSupport::stream)
                .parallel()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));

        //request is broken
        if (counts.isEmpty() || counts.size() > 2) {
            return null;
        }

        final Long incomingCount = Optional.ofNullable(counts.get(0)).orElse(0L);
        final Long outgoingCount = Optional.ofNullable(counts.get(1)).orElse(0L);

        return DataObject.of(incomingCount, outgoingCount);
    }

    private enum Direction {
        FIRST,
        LAST
    }
}
