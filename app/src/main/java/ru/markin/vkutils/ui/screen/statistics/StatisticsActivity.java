package ru.markin.vkutils.ui.screen.statistics;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewFlipper;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.markin.vkutils.R;
import ru.markin.vkutils.common.Dialog;
import ru.markin.vkutils.presentation.presenter.statistics.StatisticsPresenter;
import ru.markin.vkutils.presentation.view.statistics.StatisticsView;
import ru.markin.vkutils.ui.base.BaseActivity;
import ru.markin.vkutils.ui.screen.statistics.component.SearchAdapter;
import ru.markin.vkutils.ui.screen.statistics.component.StatisticsPagerAdapter;

public class StatisticsActivity extends BaseActivity implements StatisticsView {

    @InjectPresenter
    public StatisticsPresenter presenter;

    private SearchAdapter adapter;
    private ViewFlipper mainFlipper;
    private ViewFlipper searchFlipper;

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
        initializeSearchItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        initializeMenuView(menu);
        return true;
    }

    @Override
    public void showSearchResult(List<Dialog> dialogs) {
        adapter.updateAll(dialogs);
        searchFlipper.setDisplayedChild(1);
    }

    @Override
    public void hideSearch() {
        mainFlipper.setDisplayedChild(0);
    }

    @Override
    public void showSearch() {
        mainFlipper.setDisplayedChild(1);
        searchFlipper.setDisplayedChild(0);
        adapter.clear();
    }

    private void initializeSearchItems() {
        adapter = initializeSearchRecyclerView();
        mainFlipper = (ViewFlipper) findViewById(R.id.activity_stats_flipper_main);
        searchFlipper = (ViewFlipper) findViewById(R.id.activity_stats_flipper_search);
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

    private void initializeMenuView(Menu menu) {
        initializeSearch(menu);
    }

    private SearchAdapter initializeSearchRecyclerView() {
        RecyclerView searchRecycler = (RecyclerView) findViewById(R.id.activity_stats_recycler_view_search);
        SearchAdapter adapter = new SearchAdapter(this);
        searchRecycler.setAdapter(adapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setItemAnimator(new DefaultItemAnimator());
        return adapter;
    }

    private void initializeSearch(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.menu_stats_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        initializeSearchViewTextChangesListener(searchView);
    }

    private void initializeSearchViewTextChangesListener(SearchView searchView) {
        RxSearchView.queryTextChanges(searchView)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(presenter::search);

    }
}
