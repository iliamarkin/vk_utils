package ru.markin.vkutils.app;

import android.app.Application;

import lombok.Getter;
import ru.markin.vkutils.app.dagger.AppComponent;
import ru.markin.vkutils.app.dagger.AppModule;
import ru.markin.vkutils.app.dagger.DaggerAppComponent;
import ru.markin.vkutils.app.dagger.NetworkModule;

public class App extends Application {

    public static final String APP_PREFERENCE = "profile_info";
    public static final String TOKEN_KEY = "token_key";

    @Getter
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
    }

    private void initInjector() {
        component = DaggerAppComponent.builder()
                .networkModule(new NetworkModule("https://api.vk.com/", getApplicationContext()))
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }
}
