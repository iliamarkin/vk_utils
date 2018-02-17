package ru.markin.vkutils.app.dagger;

import android.content.Context;

import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.markin.vkutils.common.network.ApiService;
import ru.markin.vkutils.common.network.RestService;
import ru.markin.vkutils.scope.PerApplication;

@SuppressWarnings("WeakerAccess,FieldCanBeLocal")
@Module
public class NetworkModule {
    private final RestService restService;
    private final Context context;

    public NetworkModule(final String baseUrl, final Context context) {
        this.restService = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(RestService.class);
        this.context = context;
    }

    @PerApplication
    @Provides
    public ApiService provideApiExecutor() {
        return new ApiService(this.restService, this.context);
    }

    @PerApplication
    @Provides
    public RestService provideApiService() {
        return this.restService;
    }
}