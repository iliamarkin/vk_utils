package ru.markin.vkutils.ui.screen.dialog;

import dagger.Component;
import ru.markin.vkutils.app.dagger.AppComponent;
import ru.markin.vkutils.presentation.presenter.dialog.DialogPresenter;
import ru.markin.vkutils.scope.PerScreen;

@PerScreen
@Component(dependencies = {AppComponent.class})
public interface DialogComponent {

    void inject(DialogPresenter presenter);
}
