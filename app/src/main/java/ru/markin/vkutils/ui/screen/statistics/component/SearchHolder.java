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

    SearchHolder(View itemView) {
        super(itemView);
        defaultPhoto = (CircleImageView) itemView.findViewById(R.id.recycler_item_search_default_photo);
        photo = (CircleImageView) itemView.findViewById(R.id.recycler_item_search_photo);
        title = (TextView) itemView.findViewById(R.id.recycler_item_search_title);
    }
}
