package ru.markin.vkutils.presentation.presenter.statistics;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.app.network.ApiExecutor;
import ru.markin.vkutils.app.network.gson.SearchList;
import ru.markin.vkutils.common.Dialog;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.statistics.StatisticsView;
import ru.markin.vkutils.ui.screen.statistics.DaggerStatisticsComponent;
import ru.markin.vkutils.ui.screen.statistics.StatisticsComponent;
import ru.markin.vkutils.ui.screen.statistics.StatisticsModule;

@InjectViewState
public class StatisticsPresenter extends BasePresenter<StatisticsView> {

    private final String token;
    @Inject
    @Named(StatisticsModule.MESSAGES)
    @Getter
    String messages;
    @Inject
    @Named(StatisticsModule.STATISTICS)
    @Getter
    String statistics;
    @Inject
    ApiExecutor apiExecutor;
    @Inject
    SharedPreferences sharedPreferences;

    public StatisticsPresenter() {
        token = sharedPreferences.getString(App.TOKEN_KEY, "");
    }

    @Override
    protected void createComponent() {
        StatisticsComponent component = DaggerStatisticsComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }

    public void search(CharSequence query) {
        Observable.just(query)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::selectScreen)
                .filter(q -> q.length() > 0 && apiExecutor.isOnline())
                .flatMap(q -> apiExecutor.getSearchObservable(token, q.toString()))
                .observeOn(Schedulers.io())
                .map(this::createDialog)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((dialogs, throwable) -> getViewState().showSearchResult(dialogs));
    }

    private void selectScreen(CharSequence query) {
        if (query.length() == 0) {
            getViewState().hideSearch();
        } else {
            getViewState().showSearch();
        }
    }

    private Dialog createDialog(SearchList.Item item) {
        return new Dialog(item.getId(), item.getTitle(), item.getPhoto());
    }
}
