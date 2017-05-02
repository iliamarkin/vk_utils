package ru.markin.vkutils.ui.screen.statistics.fragment.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewFlipper;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.markin.vkutils.R;
import ru.markin.vkutils.common.Dialog;
import ru.markin.vkutils.presentation.presenter.statistics.DialogsFragmentPresenter;
import ru.markin.vkutils.presentation.view.statistics.DialogsFragmentView;
import ru.markin.vkutils.ui.base.BaseFragment;
import ru.markin.vkutils.ui.screen.auth.AuthActivity;
import ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.component.DialogsAdapter;

public class DialogsFragment extends BaseFragment implements DialogsFragmentView {

    @InjectPresenter
    public DialogsFragmentPresenter presenter;

    private DialogsAdapter adapter;
    private ViewFlipper flipper;
    private SwipeRefreshLayout refreshLayout;

    public DialogsFragment() {}

    @Override
    protected void initializeView(View rootView, Bundle savedInstanceState) {
        flipper = (ViewFlipper) rootView.findViewById(R.id.fragment_dialogs_flipper_main);
        initializeRecyclerView(rootView);
        initSwipeRefreshLayout(rootView);
    }

    @Override
    public int getFragmentId() {
        return R.layout.fragment_dialogs;
    }

    @Override
    public boolean getAttachToRoot() {
        return false;
    }

    @Override
    public void doOnBadToken() {
        Activity activity = getActivity();
        activity.startActivity(new Intent(activity, AuthActivity.class));
        activity.finish();
    }

    @Override
    public void doOnLoaded(List<Dialog> dialogs, int dialogsCount) {
        adapter.setDialogsCount(dialogsCount);
        adapter.addAll(dialogs);
    }

    @Override
    public void doOnLoadMore(List<Dialog> dialogs, int dialogsCount) {
        adapter.setDialogsCount(dialogsCount);
        adapter.addAllToEnd(dialogs);
    }

    @Override
    public void doOnUpdate(List<Dialog> dialogs, int dialogsCount) {
        adapter.setDialogsCount(dialogsCount);
        adapter.updateAllItems(dialogs);
    }

    @Override
    public void hideProgressBar() {
        flipper.setDisplayedChild(1);
    }

    @Override
    public void hideRefreshLayoutProgressBar() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void doOnEmptyDialogs() {
        flipper.setDisplayedChild(2);
    }

    private void initializeRecyclerView(View rootView) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_dialogs_recycler_view_dialogs);
        adapter = new DialogsAdapter(getContext(), presenter.getDialogs());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), manager.getOrientation()));
        initializeRecyclerViewScrollListener(recyclerView, manager);
    }

    private void initializeRecyclerViewScrollListener(RecyclerView recyclerView, LinearLayoutManager manager) {
        RxRecyclerView.scrollEvents(recyclerView)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(r -> r.dy() > 0 && (manager.getChildCount() + manager.findFirstVisibleItemPosition()) >= manager.getItemCount()
                        && adapter.getDialogsCount() != adapter.getItemCount())
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(recyclerViewScrollEvent -> presenter.loadMoreDialogs());
    }

    private void initSwipeRefreshLayout(View rootView) {
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_dialogs_swipe_layout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(() -> presenter.updateDialogs());
    }
}
