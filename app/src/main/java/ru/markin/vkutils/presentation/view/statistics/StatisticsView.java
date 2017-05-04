package ru.markin.vkutils.presentation.view.statistics;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.markin.vkutils.common.util.Dialog;

public interface StatisticsView extends MvpView {

    void showSearchResult(List<Dialog> dialogs);

    void hideSearch();

    void showSearch();
}
