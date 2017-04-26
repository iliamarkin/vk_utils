package ru.markin.vkutils.ui.screen.auth.dialog;

import dagger.Component;
import ru.markin.vkutils.app.dagger.AppComponent;
import ru.markin.vkutils.presentation.presenter.auth.AuthDialogFragmentPresenter;
import ru.markin.vkutils.scope.PerFragment;

@PerFragment
@Component(dependencies = {AppComponent.class})
public interface AuthDialogFragmentComponent {

    void inject(AuthDialogFragmentPresenter presenter);
}
