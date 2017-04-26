package ru.markin.vkutils.presentation.view.auth;

import com.arellomobile.mvp.MvpView;

public interface AuthDialogFragmentView extends MvpView {

    void doOnSuccess();

    void doOnError();
}
