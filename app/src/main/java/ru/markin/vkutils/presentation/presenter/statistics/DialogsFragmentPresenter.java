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
import ru.markin.vkutils.app.network.ApiExecutor;
import ru.markin.vkutils.app.network.gson.DialogList;
import ru.markin.vkutils.common.Dialog;
import ru.markin.vkutils.common.Util;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.statistics.DialogsFragmentView;
import ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.DaggerDialogsFragmentComponent;
import ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.DialogsFragmentComponent;

@InjectViewState
public class DialogsFragmentPresenter extends BasePresenter<DialogsFragmentView> {

    private final String token;
    private final List<Dialog> dialogs;
    @Inject
    @Named("today")
    String todayText;
    @Inject
    @Named("yesterday")
    String yesterdayText;
    @Inject
    int appId;
    @Inject
    String secretKey;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ApiExecutor apiExecutor;

    @Getter private volatile boolean loading = true;

    private int dialogsCount = 0;

    public DialogsFragmentPresenter() {
        token = getTokenFromSharedPreference();
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
            getDialogs(dialogs.size())
                    .subscribe((dialogsResult, e) -> {
                        if (checkResult(dialogsResult)) {
                            getViewState().doOnLoadMore(dialogsResult, dialogsCount);
                        }
                    });
        }
    }

    private void loadDialogs() {
        if (apiExecutor.isOnline()) {
                    getDialogs(0)
                    .subscribe(dialogsResult -> {
                        if (checkResult(dialogsResult)) {
                            getViewState().doOnLoad(dialogsResult, dialogsCount);
                        }
                    }, e -> checkToken());
        } else {
            //load from DB
        }
    }

    public void updateDialogs() {
        if (apiExecutor.isOnline()) {
            getDialogs(0)
                    .subscribe(dialogsResult -> {
                        dialogs.clear();
                        if (checkResult(dialogsResult)) {
                            getViewState().doOnUpdate(dialogsResult, dialogsCount);
                        }
                    }, e -> checkToken());
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

    private String getTokenFromSharedPreference() {
        return sharedPreferences.getString(App.TOKEN_KEY, "");
    }
}