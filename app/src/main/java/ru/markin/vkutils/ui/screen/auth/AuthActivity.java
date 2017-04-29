package ru.markin.vkutils.ui.screen.auth;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import ru.markin.vkutils.R;
import ru.markin.vkutils.presentation.view.auth.AuthView;
import ru.markin.vkutils.ui.base.BaseActivity;
import ru.markin.vkutils.ui.screen.auth.dialog.AuthDialogFragment;

public class AuthActivity extends BaseActivity implements AuthView {

    @Override
    protected void initializeContentView() {
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected void initializeToolbar(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_auth);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void initializeView(Bundle savedInstanceState) {
        Button exitButton = (Button) findViewById(R.id.activity_auth_button_exit);
        exitButton.setOnClickListener(view -> finish());

        Button authButton = (Button) findViewById(R.id.activity_auth_button_auth);
        DialogFragment fragment = new AuthDialogFragment();
        authButton.setOnClickListener(view -> fragment.show(getSupportFragmentManager(), "Auth"));
    }
}
