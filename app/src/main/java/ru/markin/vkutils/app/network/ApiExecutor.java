package ru.markin.vkutils.app.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.markin.vkutils.app.network.gson.DialogList;
import ru.markin.vkutils.app.network.gson.SearchList;

public class ApiExecutor {

    private ApiService apiService;

    private Context context;

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
                    }
                    else {
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


}
