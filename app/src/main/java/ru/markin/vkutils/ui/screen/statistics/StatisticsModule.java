package ru.markin.vkutils.ui.screen.statistics;

import android.content.res.Resources;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.markin.vkutils.R;
import ru.markin.vkutils.scope.PerScreen;

@Module
public class StatisticsModule {

    public static final String MESSAGES = "messages";
    public static final String STATISTICS = "statistics";

    @PerScreen
    @Provides
    @Named(MESSAGES)
    String provideMessage(final Resources resources) {
        return resources.getString(R.string.activity_stats_tab_layout_messages);
    }

    @PerScreen
    @Provides
    @Named(STATISTICS)
    String provideStatistics(final Resources resources) {
        return resources.getString(R.string.activity_stats_tab_layout_statistics);
    }
}
