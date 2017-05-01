package ru.markin.vkutils.presentation.view.dialog;

import com.arellomobile.mvp.MvpView;

public interface DialogView extends MvpView {

    void setInformation(int count, int inputCount, int outputCount, long firstDate, long lastDate);
}
