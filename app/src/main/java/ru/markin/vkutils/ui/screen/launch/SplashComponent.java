package ru.markin.vkutils.ui.screen.launch;

import dagger.Component;
import ru.markin.vkutils.app.dagger.AppComponent;
import ru.markin.vkutils.presentation.presenter.launch.SplashPresenter;
import ru.markin.vkutils.scope.PerScreen;

@PerScreen
@Component(dependencies = {AppComponent.class})
public interface SplashComponent {

    void inject(SplashPresenter presenter);
}
