package ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import ru.markin.vkutils.R;

class ProgressBarHolder extends RecyclerView.ViewHolder {

    ProgressBar progressBar;

    ProgressBarHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.recycler_item_progress_bar);
    }
}
