package ru.markin.vkutils.ui.screen.statistics;

import android.content.res.Resources;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.markin.vkutils.R;
import ru.markin.vkutils.scope.PerScreen;

@Module
public class StatisticsModule {

    @PerScreen
    @Provides
    @Named("messages")
    String provideMessage(Resources resources) {
        return resources.getString(R.string.activity_stats_tab_layout_messages);
    }

    @PerScreen
    @Provides
    @Named("statistics")
    String provideStatistics(Resources resources) {
        return resources.getString(R.string.activity_stats_tab_layout_statistics);
    }
}
