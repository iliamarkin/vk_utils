package ru.markin.vkutils.ui.screen.statistics.fragment.dialogs;

import android.content.res.Resources;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.markin.vkutils.R;
import ru.markin.vkutils.scope.PerFragment;

@Module
public class DialogsFragmentModule {

    @PerFragment
    @Named("today")
    @Provides
    String provideTodayText(Resources resources) {
        return resources.getString(R.string.today_text);
    }

    @PerFragment
    @Named("yesterday")
    @Provides
    String provideYesterdayText(Resources resources) {
        return resources.getString(R.string.yesterday_text);
    }
}
