package ru.markin.vkutils.ui.screen.statistics.component;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.markin.vkutils.R;
import ru.markin.vkutils.common.util.Dialog;
import ru.markin.vkutils.common.util.Util;
import ru.markin.vkutils.ui.util.Value;
import ru.markin.vkutils.ui.screen.dialog.DialogActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

    private final Context context;
    private final List<Dialog> dialogs;
    private final Drawable icon;

    @SuppressWarnings("deprecation")
    public SearchAdapter(final Context context) {
        this.context = context;
        this.dialogs = new ArrayList<>();
        this.icon = context.getResources().getDrawable(R.drawable.ic_default_user);
    }

    @Override
    public SearchHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_search, parent,false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchHolder holder, final int position) {
        final Dialog dialog = this.dialogs.get(position);
        Util.loadPhoto(this.context, dialog.getPhoto(), holder.photo, holder.defaultPhoto, this.icon);
        holder.title.setText(dialog.getName());
        initItemClickListener(holder.itemView, dialog);
    }

    @Override
    public int getItemCount() {
        return this.dialogs.size();
    }

    private void initItemClickListener(final View itemView, final Dialog dialog) {
        itemView.setOnClickListener(view -> {
            final Intent intent = new Intent(this.context, DialogActivity.class);
            intent.putExtra(Value.INTENT_ID, dialog.getId());
            intent.putExtra(Value.INTENT_TITLE, dialog.getName());
            intent.putExtra(Value.INTENT_PHOTO, dialog.getPhoto());
            this.context.startActivity(intent);
        });
    }

    public void updateAll(final List<Dialog> dialogs) {
        this.dialogs.clear();
        this.dialogs.addAll(dialogs);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dialogs.clear();
        notifyDataSetChanged();
    }
}
