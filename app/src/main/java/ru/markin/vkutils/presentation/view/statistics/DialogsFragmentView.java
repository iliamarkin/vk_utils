package ru.markin.vkutils.presentation.view.statistics;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.markin.vkutils.common.Dialog;

public interface DialogsFragmentView extends MvpView {

    void doOnLoaded(List<Dialog> dialogs, int dialogsCount);

    void doOnLoadMore(List<Dialog> dialogs, int dialogsCount);

    void doOnUpdate(List<Dialog> dialogs, int dialogsCount);

    void hideProgressBar();

    void hideRefreshLayoutProgressBar();

    void doOnEmptyDialogs();

    void doOnBadToken();
}
