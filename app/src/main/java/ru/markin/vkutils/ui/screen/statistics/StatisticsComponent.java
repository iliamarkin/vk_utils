package ru.markin.vkutils.ui.screen.statistics;

import dagger.Component;
import ru.markin.vkutils.app.dagger.AppComponent;
import ru.markin.vkutils.presentation.presenter.statistics.StatisticsPresenter;
import ru.markin.vkutils.scope.PerScreen;

@PerScreen
@Component(dependencies = {AppComponent.class}, modules = {StatisticsModule.class})
public interface StatisticsComponent {

    void inject(StatisticsPresenter presenter);
}
