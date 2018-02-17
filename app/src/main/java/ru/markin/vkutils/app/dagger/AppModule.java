package ru.markin.vkutils.app.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import dagger.Module;
import dagger.Provides;
import ru.markin.vkutils.R;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.scope.PerApplication;

@Module
public class AppModule {

    private final Context context;

    public AppModule(final Context context) {
        this.context = context;
    }

    @PerApplication
    @Provides
    int provideApplicationId() {
        return this.context.getResources().getInteger(R.integer.application_id);
    }

    @PerApplication
    @Provides
    String provideSecretKey() {
        return this.context.getString(R.string.secret_key);
    }

    @PerApplication
    @Provides
    SharedPreferences provideSharedPreference() {
        return this.context.getSharedPreferences(App.APP_PREFERENCE, Context.MODE_PRIVATE);
    }

    @PerApplication
    @Provides
    Resources provideResources() {
        return this.context.getResources();
    }
}
