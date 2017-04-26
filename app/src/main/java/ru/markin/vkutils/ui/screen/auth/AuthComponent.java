package ru.markin.vkutils.ui.screen.auth;

import dagger.Component;
import ru.markin.vkutils.app.dagger.AppComponent;
import ru.markin.vkutils.presentation.presenter.auth.AuthDialogFragmentPresenter;
import ru.markin.vkutils.presentation.presenter.auth.AuthPresenter;
import ru.markin.vkutils.scope.PerScreen;

@PerScreen
@Component(dependencies = {AppComponent.class})
public interface AuthComponent {

    void inject(AuthPresenter presenter);
}
