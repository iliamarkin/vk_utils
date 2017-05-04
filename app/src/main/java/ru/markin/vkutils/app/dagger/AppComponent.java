package ru.markin.vkutils.app.dagger;

import android.content.SharedPreferences;
import android.content.res.Resources;

import dagger.Component;
import ru.markin.vkutils.common.network.ApiExecutor;
import ru.markin.vkutils.common.network.ApiService;
import ru.markin.vkutils.scope.PerApplication;

@PerApplication
@Component(modules = {NetworkModule.class, AppModule.class})
public interface AppComponent {

    ApiExecutor apiExecutor();

    ApiService apiService();

    String secretKey();

    Integer appId();

    SharedPreferences sharedPreference();

    Resources resources();
}
