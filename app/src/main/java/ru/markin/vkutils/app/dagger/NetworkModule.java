package ru.markin.vkutils.app.dagger;

import android.content.Context;

import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.markin.vkutils.common.network.ApiExecutor;
import ru.markin.vkutils.common.network.ApiService;
import ru.markin.vkutils.scope.PerApplication;

@SuppressWarnings("WeakerAccess,FieldCanBeLocal")
@Module
public class NetworkModule {
    private final String BASE_URL;
    private final ApiService apiService;
    private final Context context;

    public NetworkModule(String baseUrl, Context context) {
        BASE_URL = baseUrl;
        apiService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(ApiService.class);
        this.context = context;
    }

    @PerApplication
    @Provides
    public ApiExecutor provideApiExecutor() {
        return new ApiExecutor(apiService, context);
    }

    @PerApplication
    @Provides
    public ApiService provideApiService() {
        return apiService;
    }
}