package ru.markin.vkutils.presentation.presenter.dialog;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.markin.vkutils.app.App;
import ru.markin.vkutils.presentation.base.BasePresenter;
import ru.markin.vkutils.presentation.view.dialog.DialogView;
import ru.markin.vkutils.ui.screen.dialog.DaggerDialogComponent;
import ru.markin.vkutils.ui.screen.dialog.DialogComponent;

@InjectViewState
public class DialogPresenter extends BasePresenter<DialogView> {

    private static class Information {

        public Information() {}

        int count;
        int inputCount;
        int outputCount;
        long firstDate;
        long lastDate;
    }

    @Override
    protected void createComponent() {
        DialogComponent component = DaggerDialogComponent.builder()
                .appComponent(App.getComponent())
                .build();
        component.inject(this);
    }

    public void loadData(int id) {
        Observable.just(new Information())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
