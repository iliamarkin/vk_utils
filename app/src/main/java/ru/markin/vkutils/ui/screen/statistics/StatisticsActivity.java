package ru.markin.vkutils.ui.screen.statistics;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.markin.vkutils.R;
import ru.markin.vkutils.presentation.presenter.statistics.StatisticsPresenter;
import ru.markin.vkutils.presentation.view.statistics.StatisticsView;
import ru.markin.vkutils.ui.base.BaseActivity;
import ru.markin.vkutils.ui.screen.statistics.component.StatisticsPagerAdapter;

public class StatisticsActivity extends BaseActivity implements StatisticsView {

    @InjectPresenter
    public StatisticsPresenter presenter;

    @Override
    protected void initializeContentView() {
        setContentView(R.layout.activity_statistics);
    }

    @Override
    protected void initializeToolbar(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_statistics);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void initializeView(Bundle savedInstanceState) {
        ViewPager pager = getViewPager();
        initTabLayout(pager);
    }

    private ViewPager getViewPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.activity_stats_view_pager);
        FragmentStatePagerAdapter adapter = new StatisticsPagerAdapter(getSupportFragmentManager(),
                presenter.getMessages(), presenter.getStatistics());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return pager;
    }

    private void initTabLayout(ViewPager pager) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_stats_tab_layout);
        tabLayout.setupWithViewPager(pager);
    }
}
