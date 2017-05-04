package ru.markin.vkutils.presentation.presenter.statistics;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lombok.Getter;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.common.network.ApiExecutor;
import ru.markin.vkutils.common.network.gson.DialogList;
import ru.markin.vkutils.common.Dialog;
import ru.markin.vkutils.common.Util;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.statistics.DialogsFragmentView;
import ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.DaggerDialogsFragmentComponent;
import ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.DialogsFragmentComponent;
import ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.DialogsFragmentModule;

@InjectViewState
public class DialogsFragmentPresenter extends BasePresenter<DialogsFragmentView> {

    @Inject
    @Named(DialogsFragmentModule.TODAY)
    String todayText;
    @Inject
    @Named(DialogsFragmentModule.YESTERDAY)
    String yesterdayText;
    @Inject
    int appId;
    @Inject
    String secretKey;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ApiExecutor apiExecutor;

    @Getter
    private volatile boolean loading = true;
    private boolean isLoaded = false;
    private final String token;
    private final List<Dialog> dialogs;

    private int dialogsCount = 0;

    public DialogsFragmentPresenter() {
        token = sharedPreferences.getString(App.TOKEN_KEY, "");
        dialogs = new ArrayList<>();
        loadDialogs();
    }

    @Override
    protected void createComponent() {
        DialogsFragmentComponent component = DaggerDialogsFragmentComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }

    public List<Dialog> getDialogs() {
        return new ArrayList<>(dialogs);
    }

    public void loadMoreDialogs() {
        if (apiExecutor.isOnline()) {
            getDialogs(dialogs.size()).subscribe((dialogsResult, e) -> {
                if (checkResult(dialogsResult)) {
                    getViewState().doOnLoadMore(dialogsResult, dialogsCount);
                }
            });
        }
    }

    private void loadDialogs() {
        if (apiExecutor.isOnline()) {
            getDialogs(0).subscribe(dialogsResult -> {
                if (checkResult(dialogsResult)) {
                    isLoaded = true;
                    getViewState().doOnLoaded(dialogsResult, dialogsCount);
                }
            }, e -> checkToken());
        } else {
            //load from DB
        }
    }

    public void updateDialogs() {
        if (isLoaded) {
            if (apiExecutor.isOnline()) {
                getDialogs(0).subscribe(dialogsResult -> {
                    dialogs.clear();
                    if (checkResult(dialogsResult)) {
                        getViewState().doOnUpdate(dialogsResult, dialogsCount);
                        getViewState().hideRefreshLayoutProgressBar();
                    }
                }, e -> checkToken());
            }
        } else {
            getViewState().hideRefreshLayoutProgressBar();
        }
    }

    private boolean checkResult(List<Dialog> dialogs) {
        if (dialogsCount != 0) {
            this.dialogs.addAll(dialogs);
            return true;
        } else {
            getViewState().doOnEmptyDialogs();
            return false;
        }
    }

    private void checkToken() {
        apiExecutor.checkTokenObservable(token, secretKey, appId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        getViewState().doOnBadToken();
                    }
                });
    }

    private Single<List<Dialog>> getDialogs(int offset) {
        return apiExecutor.getDialogsListObservable(token, offset)
                .retry(3)
                .flatMap(dialogList -> {
                    dialogsCount = dialogList.getData().getCount();
                    return Observable.fromIterable(dialogList.getData().getItems());
                })
                .map(this::createDialog)
                .toList()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Dialog createDialog(DialogList.Item item) {
        long date = item.getDate() * 1000;
        String dateText = Util.getAdvancedDateText(date, todayText, yesterdayText);
        return new Dialog(item.getId(), item.getTitle(), item.getPhoto(), date, dateText);
    }
}
