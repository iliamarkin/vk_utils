package ru.markin.vkutils.presentation.presenter.auth;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.markin.vkutils.app.App;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.auth.AuthDialogFragmentView;
import ru.markin.vkutils.ui.screen.auth.dialog.AuthDialogFragmentComponent;
import ru.markin.vkutils.ui.screen.auth.dialog.DaggerAuthDialogFragmentComponent;

@SuppressWarnings("FieldCanBeLocal,WeakerAccess")
@InjectViewState
public class AuthDialogFragmentPresenter extends BasePresenter<AuthDialogFragmentView> {

    private final String FIRST_PART_URL = "https://oauth.vk.com/authorize?client_id=";
    private final String SECOND_PART_URL = "&scope=friends,photos,audio,video,docs,notes,pages,status,offers,questions" +
            ",wall,groups,messages,email,notifications,stats,ads,offline" +
            ",docs,pages,stats,notifications&response_type=token";

    @Inject int appId;

    @Inject SharedPreferences sharedPreferences;

    @Override
    protected void createComponent() {
        AuthDialogFragmentComponent component = DaggerAuthDialogFragmentComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }

    public String url() {
        return FIRST_PART_URL + appId + SECOND_PART_URL;
    }

    public void getAccessToken(String url) {
        if (url.contains("error")) {
            getViewState().doOnError();
        } else if (url.contains("access_token=")){
            String token = findTokenInUrl(url);
            saveTokenToSharedPreference(token);
            getViewState().doOnSuccess();
        }
    }

    private String findTokenInUrl(String url) {
        int i = url.indexOf("access_token=") + 13;
        StringBuilder stringBuilder = new StringBuilder();
        while (Character.isDigit(url.charAt(i)) || Character.isLetter(url.charAt(i))) {
            stringBuilder.append(url.charAt(i));
            i++;
        }
        return stringBuilder.toString();
    }

    private void saveTokenToSharedPreference(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(App.TOKEN_KEY, token);
        editor.apply();
    }
}
