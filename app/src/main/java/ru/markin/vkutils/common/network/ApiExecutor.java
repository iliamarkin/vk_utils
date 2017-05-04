package ru.markin.vkutils.common.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.markin.vkutils.common.network.gson.DialogInfo;
import ru.markin.vkutils.common.network.gson.DialogList;
import ru.markin.vkutils.common.network.gson.History;
import ru.markin.vkutils.common.network.gson.SearchList;
import ru.markin.vkutils.common.network.util.IntAndInt;
import ru.markin.vkutils.common.network.util.LongAndInt;

public class ApiExecutor {

    private final ApiService apiService;
    private final Context context;

    @SuppressLint("UseSparseArrays")
    public ApiExecutor(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public Observable<Boolean> checkTokenObservable(@NonNull String token, @NonNull String secretKey, int appId) {
        return !token.isEmpty() ? apiService.getAccessToken(appId, secretKey, "5.62", "client_credentials")
                .subscribeOn(Schedulers.io())
                .filter(Response::isSuccessful)
                .map(accessToken -> accessToken.body().getAccessToken())
                .flatMap(accessToken -> apiService.checkToken(token, secretKey, accessToken, "5.62"))
                .filter(Response::isSuccessful)
                .flatMap(checkToken -> Observable.just(checkToken.body().getResponse().getSuccess() == 1))
                .retry(3) : Observable.just(false);
    }

    public Observable<Response<DialogList>> dialogsResponseObservable(String token, int offset) {
        return apiService.getDialogs(offset, token, "5.62")
                .subscribeOn(Schedulers.io());
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public Observable<DialogList> getDialogsListObservable(String token, int offset) {
        return apiService.getDialogs(offset, token, "5.62")
                .subscribeOn(Schedulers.io())
                .flatMap(dialogListResponse -> {
                    if (dialogListResponse.isSuccessful()) {
                        return Observable.just(dialogListResponse.body());
                    } else {
                        throw new RuntimeException();
                    }
                });
    }

    public Observable<SearchList.Item> getSearchObservable(String token, String query) {
        return apiService.searchDialogs(query, token, "5.63")
                .subscribeOn(Schedulers.io())
                .filter(Response::isSuccessful)
                .flatMap(response -> Observable.fromIterable(response.body().getItems()))
                .retry(3);
    }

    public long getFirstDate(String token, int id) {
        try {
            Response<History> response = apiService.getHistory(1, id, 1, token, "5.63").execute();
            return response.isSuccessful() ? response.body().getResponse().getItems().get(0).getDate() * 1000 : -1;
        } catch (IOException ignore) {
        }
        return -1;
    }

    public long getLastDate(String token, int id) {
        try {
            Response<History> response = apiService.getHistory(1, id, 0, token, "5.63").execute();
            return response.isSuccessful() ? response.body().getResponse().getItems().get(0).getDate() * 1000 : -1;
        } catch (IOException ignore) {
        }
        return -1;
    }

    public LongAndInt getFirstDateWithCount(String token, int id) {
        try {
            Response<History> response = apiService.getHistory(1, id, 1, token, "5.63").execute();
            if (response.isSuccessful()) {
                long date = response.body().getResponse().getItems().get(0).getDate() * 1000;
                int count = response.body().getResponse().getCount();
                return new LongAndInt(date, count);
            }
        } catch (IOException ignore) {
        }
        return new LongAndInt(-1, -1);
    }

    public LongAndInt getLastDateWithCount(String token, int id) {
        try {
            Response<History> response = apiService.getHistory(1, id, 0, token, "5.63").execute();
            if (response.isSuccessful()) {
                long date = response.body().getResponse().getItems().get(0).getDate() * 1000;
                int count = response.body().getResponse().getCount();
                return new LongAndInt(date, count);
            }
        } catch (IOException ignore) {
        }
        return new LongAndInt(-1, -1);
    }

    public DialogInfo getDialogInfo(String token, int id, int offset) {
        try {
            Response<DialogInfo> response = apiService.getDialogInfo(offset, id, token, "5.63").execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException ignore) {
        }
        return null;
    }

    public IntAndInt getIncomingAndOutgoingCount(String token, int id, int offset) {
        int incomingCount = 0;
        int outgoingCount = 0;
        DialogInfo dialogInfo = getDialogInfo(token, id, offset);
        if (dialogInfo != null) {
            List<List<Integer>> dialogInfoList = dialogInfo.getOutputs();
            for (List<Integer> list : dialogInfoList) {
                for (Integer item : list) {
                    if (item == 0) {
                        incomingCount++;
                    } else {
                        outgoingCount++;
                    }
                }
            }
            return new IntAndInt(incomingCount, outgoingCount);
        }
        return null;
    }
}
