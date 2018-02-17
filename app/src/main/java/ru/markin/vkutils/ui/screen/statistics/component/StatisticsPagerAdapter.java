package ru.markin.vkutils.ui.screen.statistics.component;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.DialogsFragment;
import ru.markin.vkutils.ui.screen.statistics.fragment.statistics.StatisticsFragment;

public class StatisticsPagerAdapter extends FragmentStatePagerAdapter {

    private final String firstPageTitle;
    private final String secondPageTitle;

    public StatisticsPagerAdapter(final FragmentManager fm,
                                  final String firstPageTitle,
                                  final String secondPageTitle) {
        super(fm);
        this.firstPageTitle = firstPageTitle;
        this.secondPageTitle = secondPageTitle;
    }

    @Nullable
    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                return new DialogsFragment();
            case 1:
                return new StatisticsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return this.firstPageTitle;
            case 1:
                return this.secondPageTitle;
            default:
                return null;
        }
    }
}