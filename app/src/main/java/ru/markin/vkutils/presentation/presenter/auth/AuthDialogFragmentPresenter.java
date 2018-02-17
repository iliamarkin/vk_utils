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

    @Inject
    int appId;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void createComponent() {
        final AuthDialogFragmentComponent component = DaggerAuthDialogFragmentComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }

    public String url() {
        return this.FIRST_PART_URL + this.appId + this.SECOND_PART_URL;
    }

    public void getAccessToken(final String url) {
        if (url.contains("error")) {
            getViewState().doOnError();
        } else if (url.contains("access_token=")) {
            final String token = findTokenInUrl(url);
            saveTokenToSharedPreference(token);
            getViewState().doOnSuccess();
        }
    }

    private String findTokenInUrl(final String url) {
        final String accessToken = "access_token=";
        int i = url.indexOf(accessToken) + accessToken.length();
        final StringBuilder stringBuilder = new StringBuilder();
        while (url.charAt(i) != '&') {
            stringBuilder.append(url.charAt(i));
            i++;
        }
        return stringBuilder.toString();
    }

    private void saveTokenToSharedPreference(final String token) {
        final SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(App.TOKEN_KEY, token);
        editor.apply();
    }
}
