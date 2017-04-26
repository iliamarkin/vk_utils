package ru.markin.vkutils.ui.screen.launch;

import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.markin.vkutils.presentation.presenter.launch.SplashPresenter;
import ru.markin.vkutils.presentation.view.launch.SplashView;
import ru.markin.vkutils.ui.base.BaseActivity;
import ru.markin.vkutils.ui.screen.auth.AuthActivity;
import ru.markin.vkutils.ui.screen.statistics.StatisticsActivity;

public class SplashActivity extends BaseActivity implements SplashView {

    @InjectPresenter
    public SplashPresenter presenter;

    @Override
    protected void initializeContentView() {
        //empty method
    }

    @Override
    protected void initializeToolbar(Bundle savedInstanceState) {
        //empty method
    }

    @Override
    protected void initializeView(Bundle savedInstanceState) {
        //empty method
    }

    @Override
    public void startScreenOnBadToken() {
        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
        finish();
    }

    @Override
    public void startScreenOnCorrectToken() {
        startActivity(new Intent(SplashActivity.this, StatisticsActivity.class));
        finish();
    }

    @Override
    public void startScreenOnError() {
        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
        finish();
    }
}
