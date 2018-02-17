package ru.markin.vkutils.ui.screen.auth.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.markin.vkutils.R;
import ru.markin.vkutils.presentation.presenter.auth.AuthDialogFragmentPresenter;
import ru.markin.vkutils.presentation.view.auth.AuthDialogFragmentView;
import ru.markin.vkutils.ui.base.BaseDialogFragment;
import ru.markin.vkutils.ui.screen.statistics.StatisticsActivity;

public class AuthDialogFragment extends BaseDialogFragment implements AuthDialogFragmentView {

    @InjectPresenter
    public AuthDialogFragmentPresenter presenter;

    @Override
    protected void initializeView(final View rootView, final Bundle savedInstanceState) {
        disableTitleWindow();
        initWebView(rootView);
    }

    @Override
    public int getFragmentId() {
        return R.layout.dialog_fragment_auth;
    }

    @Override
    public boolean getAttachToRoot() {
        return false;
    }

    @Override
    public void doOnSuccess() {
        startActivity(new Intent(getActivity(), StatisticsActivity.class));
        dismiss();
    }

    @Override
    public void doOnError() {
        dismiss();
        Toast.makeText(getActivity(), getString(R.string.get_token_error), Toast.LENGTH_SHORT).show();
    }

    private void disableTitleWindow() {
        final Window window = getDialog().getWindow();
        if (window != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
    }

    private void initWebView(final View rootView) {
        final WebView authView = rootView.findViewById(R.id.dialog_fragment_auth_web_view);
        authView.loadUrl(this.presenter.url());


        authView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                AuthDialogFragment.this.presenter.getAccessToken(url);
                view.loadUrl(url);
                return true;
            }
        });
    }
}
