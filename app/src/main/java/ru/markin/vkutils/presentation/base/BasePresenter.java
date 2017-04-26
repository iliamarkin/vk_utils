package ru.markin.vkutils.presentation.base;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import ru.markin.vkutils.app.App;
import ru.markin.vkutils.app.dagger.AppComponent;

public abstract class BasePresenter<View extends MvpView> extends MvpPresenter<View> {

    protected BasePresenter() {
        createComponent();
    }

    protected abstract void createComponent();

    protected AppComponent getAppComponent() {
        return App.getComponent();
    }
}
