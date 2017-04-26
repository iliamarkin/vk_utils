package ru.markin.vkutils.presentation.view.launch;

import android.content.SharedPreferences;

import com.arellomobile.mvp.MvpView;

public interface SplashView extends MvpView {

    void startScreenOnBadToken();

    void startScreenOnCorrectToken();

    void startScreenOnError();
}
