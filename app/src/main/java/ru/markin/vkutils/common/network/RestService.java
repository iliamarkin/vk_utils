package ru.markin.vkutils.common.network;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.markin.vkutils.common.network.gson.AccessToken;
import ru.markin.vkutils.common.network.gson.CheckToken;
import ru.markin.vkutils.common.network.gson.DialogInfo;
import ru.markin.vkutils.common.network.gson.DialogList;
import ru.markin.vkutils.common.network.gson.History;
import ru.markin.vkutils.common.network.gson.SearchList;

public interface RestService {

    @GET("oauth/access_token")
    Observable<Response<AccessToken>> getAccessToken(@Query("client_id") int clientId,
                                                     @Query("client_secret") String clientSecret,
                                                     @Query("v") String version,
                                                     @Query("grant_type") String grantType);

    @GET("method/secure.checkToken")
    Observable<Response<CheckToken>> checkToken(@Query("token") String token,
                                                @Query("client_secret") String clientSecret,
                                                @Query("access_token") String accessToken,
                                                @Query("v") String version);

    @GET("method/execute.getDialogs")
    Observable<Response<DialogList>> getDialogs(@Query("offset") int offset,
                                                @Query("access_token") String token,
                                                @Query("v") String version);

    @GET("method/execute.searchDialogs")
    Observable<Response<SearchList>> searchDialogs(@Query("q") String q,
                                                   @Query("access_token") String token,
                                                   @Query("v") String version);

    @GET("method/execute.getDialogInfo")
    Call<DialogInfo> getDialogInfo(@Query("offset") int offset,
                                   @Query("user_id") int id,
                                   @Query("access_token") String accessToken,
                                   @Query("v") String version);

    @GET("method/messages.getHistory")
    Call<History> getHistory(@Query("count") int count,
                             @Query("user_id") int user_id,
                             @Query("rev") int rev,
                             @Query("access_token") String token,
                             @Query("v") String version);
}
