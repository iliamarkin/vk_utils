package ru.markin.vkutils.ui.screen.statistics.component;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.DialogsFragment;
import ru.markin.vkutils.ui.screen.statistics.fragment.statistics.StatisticsFragment;

public class StatisticsPagerAdapter extends FragmentStatePagerAdapter {

    private String firstPageTitle;
    private String secondPageTitle;

    public StatisticsPagerAdapter(FragmentManager fm, String firstPageTitle, String secondPageTitle) {
        super(fm);
        this.firstPageTitle = firstPageTitle;
        this.secondPageTitle = secondPageTitle;
    }

    @Override
    public Fragment getItem(int position) {
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
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return firstPageTitle;
            case 1:
                return secondPageTitle;
            default:
                return null;
        }
    }
}