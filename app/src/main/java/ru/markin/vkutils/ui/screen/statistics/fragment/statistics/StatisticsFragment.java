package ru.markin.vkutils.ui.screen.statistics.fragment.statistics;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.markin.vkutils.R;
import ru.markin.vkutils.presentation.view.statistics.StatisticsFragmentView;
import ru.markin.vkutils.ui.base.BaseFragment;

public class StatisticsFragment extends BaseFragment implements StatisticsFragmentView {


    public StatisticsFragment() {}

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {

    }

    @Override
    public int getFragmentId() {
        return R.layout.fragment_statistics;
    }

    @Override
    public boolean getAttachToRoot() {
        return false;
    }
}
