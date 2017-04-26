package ru.markin.vkutils.presentation.presenter.statistics;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.statistics.StatisticsView;
import ru.markin.vkutils.ui.screen.statistics.DaggerStatisticsComponent;
import ru.markin.vkutils.ui.screen.statistics.StatisticsComponent;

@InjectViewState
public class StatisticsPresenter extends BasePresenter<StatisticsView> {

    @Inject
    @Named("messages")
    @Getter String messages;

    @Inject
    @Named("statistics")
    @Getter String statistics;

    @Override
    protected void createComponent() {
        StatisticsComponent component = DaggerStatisticsComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }
}
