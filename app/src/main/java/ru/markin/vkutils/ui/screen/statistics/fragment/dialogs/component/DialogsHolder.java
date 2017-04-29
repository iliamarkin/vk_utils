package ru.markin.vkutils.ui.screen.statistics.fragment.dialogs.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.markin.vkutils.R;

class DialogsHolder extends RecyclerView.ViewHolder {

    CircleImageView defaultPhoto;
    CircleImageView photo;
    TextView title;
    TextView date;

    DialogsHolder(View itemView) {
        super(itemView);
        defaultPhoto = (CircleImageView) itemView.findViewById(R.id.recycler_item_dialogs_default_photo);
        photo = (CircleImageView) itemView.findViewById(R.id.recycler_item_dialogs_photo);
        title = (TextView) itemView.findViewById(R.id.recycler_item_dialogs_title);
        date = (TextView) itemView.findViewById(R.id.recycler_item_dialogs_date);
    }
}
