package ru.markin.vkutils.ui.screen.statistics.fragment.dialogs;

import dagger.Component;
import ru.markin.vkutils.app.dagger.AppComponent;
import ru.markin.vkutils.presentation.presenter.statistics.DialogsFragmentPresenter;
import ru.markin.vkutils.scope.PerFragment;

@PerFragment
@Component(dependencies = {AppComponent.class}, modules = {DialogsFragmentModule.class})
public interface DialogsFragmentComponent {

    void inject(DialogsFragmentPresenter presenter);
}
