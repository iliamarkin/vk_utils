package ru.markin.vkutils.presentation.presenter.launch;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.common.network.ApiExecutor;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.launch.SplashView;
import ru.markin.vkutils.ui.screen.launch.DaggerSplashComponent;
import ru.markin.vkutils.ui.screen.launch.SplashComponent;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    @Inject
    ApiExecutor apiExecutor;

    @Inject
    int appId;

    @Inject
    String secretKey;

    @Inject
    SharedPreferences sharedPreferences;

    public SplashPresenter() {
        showScreen();
    }

    @Override
    protected void createComponent() {
        SplashComponent component = DaggerSplashComponent.builder()
                .appComponent(getAppComponent())
                .build();
        component.inject(this);
    }

    private void showScreen() {
        checkTokenObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(aBoolean -> Observable.just(apiExecutor.isOnline() && aBoolean
                        || !apiExecutor.isOnline() && aBoolean))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        getViewState().startScreenOnCorrectToken();
                    } else {
                        getViewState().startScreenOnBadToken();
                    }
                }, e -> getViewState().startScreenOnError());
    }

    private Observable<Boolean> checkTokenObservable() {
        String token = sharedPreferences.getString(App.TOKEN_KEY, "");
        return apiExecutor.checkTokenObservable(token, secretKey, appId);
    }
}
