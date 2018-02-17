package ru.markin.vkutils.presentation.presenter.launch;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.common.network.ApiService;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.launch.SplashView;
import ru.markin.vkutils.ui.screen.launch.DaggerSplashComponent;
import ru.markin.vkutils.ui.screen.launch.SplashComponent;

@InjectViewState
public class SplashPresenter extends BasePresenter<SplashView> {

    @Inject
    ApiService apiService;

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
        final SplashComponent component = DaggerSplashComponent.builder()
                .appComponent(getAppComponent())
                .build();
        component.inject(this);
    }

    private void showScreen() {
        checkTokenObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(aBoolean -> Observable.just(this.apiService.isOnline() && aBoolean
                        || !this.apiService.isOnline() && aBoolean))
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        getViewState().startScreenOnCorrectToken();
                    } else {
                        getViewState().startScreenOnBadToken();
                    }
                }, e -> getViewState().startScreenOnError());
    }

    private Observable<Boolean> checkTokenObservable() {
        final String token = this.sharedPreferences.getString(App.TOKEN_KEY, "");
        return this.apiService.checkTokenObservable(token, this.secretKey, this.appId);
    }
}
