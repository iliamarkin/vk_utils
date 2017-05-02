package ru.markin.vkutils.presentation.view.dialog;

import com.arellomobile.mvp.MvpView;

public interface DialogView extends MvpView {

    void setInformation(int count, int incomingCount, int outgoingCount, long firstDate, long lastDate);

    void doOnReady();

    void doOnEmpty();

    void hideRefreshLayoutProgressBar();
}
