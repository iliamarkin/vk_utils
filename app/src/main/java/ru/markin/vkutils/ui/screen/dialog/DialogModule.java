package ru.markin.vkutils.ui.screen.dialog;

import android.content.res.Resources;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.markin.vkutils.R;
import ru.markin.vkutils.scope.PerScreen;

@Module
public class DialogModule {

    public static final String ALL = "all";
    public static final String INCOMING = "incoming";
    public static final String OUTGOING = "outgoing";
    public static final String FIRST_DATE = "firstDate";
    public static final String LAST_DATE = "lastDate";

    @PerScreen
    @Provides
    @Named(ALL)
    String provideAllText(Resources resources) {
        return resources.getString(R.string.statistics_all_messages);
    }

    @PerScreen
    @Provides
    @Named(INCOMING)
    String provideIncomingText(Resources resources) {
        return resources.getString(R.string.statistics_input_messages);
    }

    @PerScreen
    @Provides
    @Named(OUTGOING)
    String provideOutgoingText(Resources resources) {
        return resources.getString(R.string.statistics_output_messages);
    }

    @PerScreen
    @Provides
    @Named(FIRST_DATE)
    String provideFirstDateText(Resources resources) {
        return resources.getString(R.string.first_message);
    }

    @PerScreen
    @Provides
    @Named(LAST_DATE)
    String provideLastDateText(Resources resources) {
        return resources.getString(R.string.last_message);
    }
}
