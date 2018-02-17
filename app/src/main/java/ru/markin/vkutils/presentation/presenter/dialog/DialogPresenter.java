package ru.markin.vkutils.presentation.presenter.dialog;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.NonNull;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.common.network.ApiService;
import ru.markin.vkutils.common.network.util.DataObject;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.dialog.DialogView;
import ru.markin.vkutils.ui.screen.dialog.DaggerDialogComponent;
import ru.markin.vkutils.ui.screen.dialog.DialogComponent;
import ru.markin.vkutils.ui.screen.dialog.DialogModule;

@SuppressWarnings("FieldCanBeLocal")
@InjectViewState
public class DialogPresenter extends BasePresenter<DialogView> {

    private final int COUNT_OF_MESSAGES = 5000;

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
    ApiService apiService;

    @Inject
    SharedPreferences sharedPreferences;

    private final AtomicBoolean stop = new AtomicBoolean(false);
    private final AtomicBoolean isLoaded = new AtomicBoolean(false);
    private final String token;
    private final int id;

    /**
     * @param id companion id
     */
    public DialogPresenter(final int id) {
        this.id = id;
        this.token = this.sharedPreferences.getString(App.TOKEN_KEY, "");
        loadData();
    }

    @Override
    protected void createComponent() {
        final DialogComponent component = DaggerDialogComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }

    /**
     * Stop all processes if activity is closed
     */
    public void activityClosed() {
        this.stop.set(true);
    }

    /**
     * Load all necessary data
     */
    private void loadData() {
        load().subscribe(inf -> {
            if (inf.count > 0) {
                getViewState().setInformation(
                        inf.count,
                        inf.incomingCount,
                        inf.outgoingCount,
                        inf.firstDate,
                        inf.lastDate);
                getViewState().doOnReady();
                this.isLoaded.set(true);
            } else {
                getViewState().doOnEmpty();
            }
        });
    }

    /**
     * Update all activity data
     */
    public void updateData() {
        if (this.isLoaded.get()) {
            load().subscribe(inf -> {
                if (inf.count > 0) {
                    getViewState().setInformation(
                            inf.count,
                            inf.incomingCount,
                            inf.outgoingCount,
                            inf.firstDate,
                            inf.lastDate);
                } else {
                    getViewState().doOnEmpty();
                }
                getViewState().hideRefreshLayoutProgressBar();
            });
        } else {
            getViewState().hideRefreshLayoutProgressBar();
        }
    }

    /**
     * Load all necessary data for the dialog
     *
     * @return observable of information
     */
    private Observable<Information> load() {
        return Observable.just(new Information())
                .observeOn(Schedulers.io())
                .flatMap(information -> getAllCountAndDate(this.id, information))
                .flatMap(information -> getIncomingAndOutGoingCount(this.id, information))
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get all messages count from dialog and last date of message
     *
     * @param id          companion id
     * @param information information
     * @return observable of information
     */
    private Observable<Information> getAllCountAndDate(final int id,
                                                       @NonNull final Information information) {
        final DataObject<Long, Integer> firstDate = this.apiService.getFirstDateWithCount(this.token, id);
        final long lastDate = this.apiService.getLastDate(this.token, id);

        information.firstDate = firstDate.getObj1();
        information.count = firstDate.getObj2();
        information.lastDate = lastDate;

        if (information.firstDate != -1
                && information.lastDate != -1
                && information.count != -1) {
            return Observable.just(information);
        } else {
            throw new RuntimeException("Loading failure!");
        }
    }

    /**
     * Calculate incoming and outgoing count
     *
     * @param id          companion id
     * @param information information
     * @return observable of information
     */
    private Observable<Information> getIncomingAndOutGoingCount(final int id,
                                                                @NonNull final Information information) {
        information.incomingCount = 0;
        information.outgoingCount = 0;
        int i = 0;
        while (i < information.count && !this.stop.get() && i >= 0) {
            final DataObject<Long, Long> count = this.apiService.getIncomingAndOutgoingCount(this.token, id, i);
            if (count != null) {
                information.incomingCount += count.getObj1();
                information.outgoingCount += count.getObj2();
            } else {
                i -= this.COUNT_OF_MESSAGES;
            }
            i += this.COUNT_OF_MESSAGES;
        }
        return Observable.just(information);
    }

    /**
     * Data holder
     */
    private static class Information {
        int count;
        int incomingCount;
        int outgoingCount;
        long firstDate;
        long lastDate;
    }
}
