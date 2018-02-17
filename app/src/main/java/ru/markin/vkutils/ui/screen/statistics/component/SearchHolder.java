package ru.markin.vkutils.ui.screen.statistics.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.markin.vkutils.R;

class SearchHolder extends RecyclerView.ViewHolder {

    CircleImageView defaultPhoto;
    CircleImageView photo;
    TextView title;

    SearchHolder(final View itemView) {
        super(itemView);
        this.defaultPhoto = itemView.findViewById(R.id.recycler_item_search_default_photo);
        this.photo = itemView.findViewById(R.id.recycler_item_search_photo);
        this.title = itemView.findViewById(R.id.recycler_item_search_title);
    }
}
