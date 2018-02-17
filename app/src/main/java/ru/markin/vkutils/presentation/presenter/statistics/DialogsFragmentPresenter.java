package ru.markin.vkutils.presentation.presenter.statistics;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import lombok.Getter;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.common.network.ApiService;
import ru.markin.vkutils.common.network.gson.DialogList;
import ru.markin.vkutils.common.util.Dialog;
import ru.markin.vkutils.common.util.Util;
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
    ApiService apiService;

    private final String token;
    private final List<Dialog> dialogs;
    private final AtomicBoolean isLoaded = new AtomicBoolean(false);

    private int dialogsCount = 0;

    public DialogsFragmentPresenter() {
        this.token = this.sharedPreferences.getString(App.TOKEN_KEY, "");
        this.dialogs = new ArrayList<>();
        loadDialogs();
    }

    @Override
    protected void createComponent() {
        final DialogsFragmentComponent component = DaggerDialogsFragmentComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }

    public List<Dialog> getDialogs() {
        return new ArrayList<>(this.dialogs);
    }

    public void loadMoreDialogs() {
        if (this.apiService.isOnline()) {
            getDialogs(this.dialogs.size()).subscribe((dialogsResult, e) -> {
                if (checkResult(dialogsResult)) {
                    getViewState().doOnLoadMore(dialogsResult, this.dialogsCount);
                }
            });
        }
    }

    private void loadDialogs() {
        if (this.apiService.isOnline()) {
            getDialogs(0).subscribe(dialogsResult -> {
                if (checkResult(dialogsResult)) {
                    this.isLoaded.set(true);
                    getViewState().doOnLoaded(dialogsResult, this.dialogsCount);
                }
            }, e -> checkToken());
        } else {
            //load from DB
        }
    }

    public void updateDialogs() {
        if (this.isLoaded.get()) {
            if (this.apiService.isOnline()) {
                getDialogs(0).subscribe(dialogsResult -> {
                    this.dialogs.clear();
                    if (checkResult(dialogsResult)) {
                        getViewState().doOnUpdate(dialogsResult, this.dialogsCount);
                        getViewState().hideRefreshLayoutProgressBar();
                    }
                }, e -> checkToken());
            }
        } else {
            getViewState().hideRefreshLayoutProgressBar();
        }
    }

    private boolean checkResult(final List<Dialog> dialogs) {
        if (this.dialogsCount != 0) {
            this.dialogs.addAll(dialogs);
            return true;
        } else {
            getViewState().doOnEmptyDialogs();
            return false;
        }
    }

    private void checkToken() {
        this.apiService.checkTokenObservable(this.token, this.secretKey, this.appId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        getViewState().doOnBadToken();
                    }
                });
    }

    private Single<List<Dialog>> getDialogs(final int offset) {
        return this.apiService.getDialogsListObservable(this.token, offset)
                .retry(3)
                .flatMap(dialogList -> {
                    this.dialogsCount = dialogList.getData().getCount();
                    return Observable.fromIterable(dialogList.getData().getItems());
                })
                .map(this::createDialog)
                .toList()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Dialog createDialog(final DialogList.Item item) {
        final long date = item.getDate() * 1000;
        final String dateText = Util.getAdvancedDateText(date, this.todayText, this.yesterdayText);
        return new Dialog(item.getId(), item.getTitle(), item.getPhoto(), date, dateText);
    }
}
