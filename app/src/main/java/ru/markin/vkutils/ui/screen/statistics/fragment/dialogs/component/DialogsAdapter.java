package ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.component;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.markin.vkutils.R;
import ru.markin.vkutils.common.Dialog;
import ru.markin.vkutils.common.Util;
import ru.markin.vkutils.ui.screen.dialog.DialogActivity;

public class DialogsAdapter extends RecyclerView.Adapter {

    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;

    @Getter @Setter private int dialogsCount = 0;

    private final Context context;
    private final List<Dialog> dialogs;
    private final Drawable icon;

    @SuppressWarnings("deprecation")
    public DialogsAdapter(Context context, List<Dialog> dialogs) {
        this.context = context;
        this.dialogs = dialogs;
        icon = context.getResources().getDrawable(R.drawable.ic_default_user);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            return new DialogsHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_dialogs, parent, false));
        } else {
            return new ProgressBarHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_progress_bar, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DialogsHolder) {
            DialogsHolder dialogsHolder = (DialogsHolder) holder;
            Dialog dialog = dialogs.get(position);
            Util.loadPhoto(context, dialog.getPhoto(), dialogsHolder.photo, dialogsHolder.defaultPhoto, icon);
            dialogsHolder.title.setText(dialog.getName());
            dialogsHolder.date.setText(dialog.getDateText());
            initItemClickListener(holder.itemView, dialog);
        } else {
            ProgressBarHolder progressBarHolder = (ProgressBarHolder) holder;
            progressBarHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dialogs.size() - 1 != position || position == dialogsCount - 1 ? VIEW_ITEM : VIEW_PROG;
    }

    private void initItemClickListener(View itemView, Dialog dialog) {
        itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DialogActivity.class);
            intent.putExtra("id", dialog.getId());
            intent.putExtra("title", dialog.getName());
            intent.putExtra("photo", dialog.getPhoto());
            context.startActivity(intent);
        });
    }

    public void addAll(List<Dialog> dialogs) {
        this.dialogs.addAll(dialogs);
        this.notifyDataSetChanged();
    }

    public void addAllToEnd(List<Dialog> dialogs) {
        this.dialogs.addAll(dialogs);
        notifyItemRangeInserted(this.dialogs.size() - dialogs.size() + 1, this.dialogs.size() - 1);
    }

    public void updateAllItems(List<Dialog> dialogs) {
        this.dialogs.clear();
        this.dialogs.addAll(dialogs);
        notifyDataSetChanged();
    }
}
