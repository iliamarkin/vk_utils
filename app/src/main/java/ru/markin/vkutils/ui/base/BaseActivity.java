package ru.markin.vkutils.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;

import ru.markin.vkutils.app.App;
import ru.markin.vkutils.app.dagger.AppComponent;

public abstract class BaseActivity extends MvpAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeContentView();
        initializeToolbar(savedInstanceState);
        initializeView(savedInstanceState);
    }

    protected abstract void initializeContentView();

    protected abstract void initializeToolbar(Bundle savedInstanceState);

    protected abstract void initializeView(Bundle savedInstanceState);
}
