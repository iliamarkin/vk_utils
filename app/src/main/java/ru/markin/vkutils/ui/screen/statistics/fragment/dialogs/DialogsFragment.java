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
import ru.markin.vkutils.common.util.Dialog;
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

    public DialogsFragment() {
    }

    @Override
    protected void initializeView(final View rootView, final Bundle savedInstanceState) {
        this.flipper = rootView.findViewById(R.id.fragment_dialogs_flipper_main);
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
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.startActivity(new Intent(activity, AuthActivity.class));
        activity.finish();
    }

    @Override
    public void doOnLoaded(final List<Dialog> dialogs, final int dialogsCount) {
        this.adapter.setDialogsCount(dialogsCount);
        this.adapter.addAll(dialogs);
        this.flipper.setDisplayedChild(1);
    }

    @Override
    public void doOnLoadMore(final List<Dialog> dialogs, final int dialogsCount) {
        this.adapter.setDialogsCount(dialogsCount);
        this.adapter.addAllToEnd(dialogs);
    }

    @Override
    public void doOnUpdate(final List<Dialog> dialogs, final int dialogsCount) {
        this.adapter.setDialogsCount(dialogsCount);
        this.adapter.updateAllItems(dialogs);
    }

    @Override
    public void hideRefreshLayoutProgressBar() {
        this.refreshLayout.setRefreshing(false);
    }

    @Override
    public void doOnEmptyDialogs() {
        this.flipper.setDisplayedChild(2);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeRecyclerView(final View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        final RecyclerView recyclerView = rootView.findViewById(R.id.fragment_dialogs_recycler_view_dialogs);
        this.adapter = new DialogsAdapter(getContext(), this.presenter.getDialogs());
        recyclerView.setAdapter(this.adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), manager.getOrientation()));
        initializeRecyclerViewScrollListener(recyclerView, manager);
    }

    private void initializeRecyclerViewScrollListener(final RecyclerView recyclerView,
                                                      final LinearLayoutManager manager) {
        RxRecyclerView.scrollEvents(recyclerView)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(r -> r.dy() > 0)
                .filter(r -> manager.getChildCount() + manager.findFirstVisibleItemPosition() >= manager.getItemCount())
                .filter(r -> this.adapter.getDialogsCount() != this.adapter.getItemCount())
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(recyclerViewScrollEvent -> this.presenter.loadMoreDialogs());
    }

    private void initSwipeRefreshLayout(final View rootView) {
        this.refreshLayout = rootView.findViewById(R.id.fragment_dialogs_swipe_layout);
        this.refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.refreshLayout.setOnRefreshListener(() -> this.presenter.updateDialogs());
    }
}
