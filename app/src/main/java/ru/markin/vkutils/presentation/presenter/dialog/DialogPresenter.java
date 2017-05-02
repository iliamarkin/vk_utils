package ru.markin.vkutils.presentation.presenter.dialog;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.app.network.ApiExecutor;
import ru.markin.vkutils.app.network.gson.DialogInfo;
import ru.markin.vkutils.app.network.util.LongAndInt;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.dialog.DialogView;
import ru.markin.vkutils.ui.screen.dialog.DaggerDialogComponent;
import ru.markin.vkutils.ui.screen.dialog.DialogComponent;
import ru.markin.vkutils.ui.screen.dialog.DialogModule;

@InjectViewState
public class DialogPresenter extends BasePresenter<DialogView> {

    private final String token;
    private final int id;
    @Inject
    @Named(DialogModule.ALL)
    @Getter
    String allText;
    @Inject
    @Named(DialogModule.INCOMING)
    @Getter
    String incomingText;
    @Inject
    @Named(DialogModule.OUTGOING)
    @Getter
    String outgoingText;
    @Inject
    @Named(DialogModule.FIRST_DATE)
    @Getter
    String firstDateText;
    @Inject
    @Named(DialogModule.LAST_DATE)
    @Getter
    String lastDateText;
    @Inject
    ApiExecutor apiExecutor;
    @Inject
    SharedPreferences sharedPreferences;
    private boolean stop = false;


    public DialogPresenter(int id) {
        this.id = id;
        token = sharedPreferences.getString(App.TOKEN_KEY, "");
        loadData();
    }

    @Override
    protected void createComponent() {
        DialogComponent component = DaggerDialogComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }

    public void activityClosed() {
        stop = true;
    }

    private void loadData() {
        load().subscribe(inf -> {
            if (inf.count > 0) {
                getViewState().setInformation(inf.count, inf.incomingCount, inf.outgoingCount, inf.firstDate, inf.lastDate);
                getViewState().doOnReady();
            } else {
                getViewState().doOnEmpty();
            }
        });
    }

    public void updateData() {
        load().subscribe(inf -> {
            if (inf.count > 0) {
                getViewState().setInformation(inf.count, inf.incomingCount, inf.outgoingCount, inf.firstDate, inf.lastDate);
                getViewState().doOnUpdated();
            } else {
                getViewState().doOnEmpty();
            }
        });
    }

    private Observable<Information> load() {
        return Observable.just(new Information())
                .observeOn(Schedulers.io())
                .flatMap(information -> getAllCountAndDate(id, information))
                .flatMap(information -> getIncomingAndOutGoingCount(id, information))
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Information> getAllCountAndDate(int id, Information information) {
        LongAndInt firstDate = apiExecutor.getFirstDateWithCount(token, id);
        long lastDate = apiExecutor.getLastDate(token, id);
        information.firstDate = firstDate.getLongValue();
        information.count = firstDate.getIntValue();
        information.lastDate = lastDate;
        if (information.firstDate != -1 && information.lastDate != -1 && information.count != -1) {
            return Observable.just(information);
        } else {
            throw new RuntimeException();
        }
    }

    private Observable<Information> getIncomingAndOutGoingCount(int id, Information information) {
        information.incomingCount = 0;
        information.outgoingCount = 0;
        int i = 0;
        while (i < information.count && !stop) {
            DialogInfo dialogInfo = apiExecutor.getDialogInfo(token, id, i);
            if (dialogInfo != null) {
                List<List<Integer>> dialogInfoList = dialogInfo.getOutputs();
                for (List<Integer> list : dialogInfoList) {
                    for (Integer item : list) {
                        if (item == 0) {
                            information.incomingCount++;
                        } else {
                            information.outgoingCount++;
                        }
                    }
                }
            } else {
                i -= 5000;
            }
            i += 5000;
        }
        return Observable.just(information);
    }

    private static class Information {

        int count;
        int incomingCount;
        int outgoingCount;
        long firstDate;
        long lastDate;

        Information() {
        }
    }
}
