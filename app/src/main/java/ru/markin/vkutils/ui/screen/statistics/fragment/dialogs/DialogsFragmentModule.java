package ru.markin.vkutils.ui.screen.statistics.fragment.dialogs;

import android.content.res.Resources;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.markin.vkutils.R;
import ru.markin.vkutils.scope.PerFragment;

@Module
public class DialogsFragmentModule {

    public static final String TODAY = "today";
    public static final String YESTERDAY = "yesterday";

    @PerFragment
    @Named(TODAY)
    @Provides
    String provideTodayText(Resources resources) {
        return resources.getString(R.string.today_text);
    }

    @PerFragment
    @Named(YESTERDAY)
    @Provides
    String provideYesterdayText(Resources resources) {
        return resources.getString(R.string.yesterday_text);
    }
}
