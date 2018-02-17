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

import java8.util.Objects;
import ru.markin.vkutils.R;
import ru.markin.vkutils.common.util.Dialog;
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
    protected void initializeToolbar(final Bundle savedInstanceState) {
        final Toolbar toolbar = findViewById(R.id.toolbar_statistics);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void initializeView(final Bundle savedInstanceState) {
        final ViewPager pager = getViewPager();
        initTabLayout(pager);
        initializeSearchItems();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        initializeMenuView(menu);
        return true;
    }

    @Override
    public void showSearchResult(final List<Dialog> dialogs) {
        this.adapter.updateAll(dialogs);
        this.searchFlipper.setDisplayedChild(1);
    }

    @Override
    public void hideSearch() {
        this.mainFlipper.setDisplayedChild(0);
    }

    @Override
    public void showSearch() {
        this.mainFlipper.setDisplayedChild(1);
        this.searchFlipper.setDisplayedChild(0);
        this.adapter.clear();
    }

    private void initializeSearchItems() {
        this.adapter = initializeSearchRecyclerView();
        this.mainFlipper = findViewById(R.id.activity_stats_flipper_main);
        this.searchFlipper = findViewById(R.id.activity_stats_flipper_search);
    }

    private ViewPager getViewPager() {
        final ViewPager pager = findViewById(R.id.activity_stats_view_pager);
        final FragmentStatePagerAdapter adapter = new StatisticsPagerAdapter(getSupportFragmentManager(),
                this.presenter.getMessages(), this.presenter.getStatistics());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position,
                                       final float positionOffset,
                                       final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        return pager;
    }

    private void initTabLayout(final ViewPager pager) {
        final TabLayout tabLayout = findViewById(R.id.activity_stats_tab_layout);
        tabLayout.setupWithViewPager(pager);
    }

    private void initializeMenuView(final Menu menu) {
        initializeSearch(menu);
    }

    private SearchAdapter initializeSearchRecyclerView() {
        final RecyclerView searchRecycler = findViewById(R.id.activity_stats_recycler_view_search);
        final SearchAdapter adapter = new SearchAdapter(this);
        searchRecycler.setAdapter(adapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchRecycler.setItemAnimator(new DefaultItemAnimator());
        return adapter;
    }

    private void initializeSearch(final Menu menu) {
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if (searchManager == null) {
            return;
        }
        final MenuItem searchMenuItem = menu.findItem(R.id.menu_stats_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        initializeSearchViewTextChangesListener(searchView);
    }

    private void initializeSearchViewTextChangesListener(final SearchView searchView) {
        RxSearchView.queryTextChanges(searchView)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribe(this.presenter::search);
    }
}
