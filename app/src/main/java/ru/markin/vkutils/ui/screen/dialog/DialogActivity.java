package ru.markin.vkutils.ui.screen.dialog;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.mikephil.charting.charts.PieChart;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.markin.vkutils.R;
import ru.markin.vkutils.common.Util;
import ru.markin.vkutils.common.Value;
import ru.markin.vkutils.presentation.presenter.dialog.DialogPresenter;
import ru.markin.vkutils.presentation.view.dialog.DialogView;
import ru.markin.vkutils.ui.base.BaseActivity;
import ru.markin.vkutils.ui.view.StatisticsView;

public class DialogActivity extends BaseActivity implements DialogView {

    @InjectPresenter
    public DialogPresenter presenter;

    private PieChart pieChart;
    private TextView allMessagesCountTextView;
    private TextView inputMessagesCountTextView;
    private TextView outputMessagesCountTextView;
    private TextView firstDateTextView;
    private TextView lastDateTextView;
    private StatisticsView statisticsView;
    private SwipeRefreshLayout refreshLayout;
    private ViewFlipper flipper;

    @Override
    protected void initializeContentView() {
        setContentView(R.layout.activity_dialog);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void initializeToolbar(Bundle savedInstanceState) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_dialog));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeTitle();
        initializePhoto();
    }

    @Override
    protected void initializeView(Bundle savedInstanceState) {
        pieChart = (PieChart) findViewById(R.id.activity_dialog_pie_chart);
        allMessagesCountTextView = (TextView) findViewById(R.id.activity_dialog_text_view_all_messages);
        inputMessagesCountTextView = (TextView) findViewById(R.id.activity_dialog_text_view_input_messages);
        outputMessagesCountTextView = (TextView) findViewById(R.id.activity_dialog_text_view_output_messages);
        firstDateTextView = (TextView) findViewById(R.id.activity_dialog_text_view_first_message);
        lastDateTextView = (TextView) findViewById(R.id.activity_dialog_text_view_last_message);
        flipper = (ViewFlipper) findViewById(R.id.activity_dialog_flipper_main);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_dialog_swipe_layout);
        statisticsView = (StatisticsView) findViewById(R.id.activity_dialog_statistics_view);
        flipper.setDisplayedChild(1);
    }

    private void initializeTitle() {
        TextView title = (TextView) findViewById(R.id.toolbar_dialog_title);
        title.setText(getIntent().getStringExtra(Value.INTENT_TITLE));
        title.setSelected(true);
    }

    @SuppressWarnings("deprecation")
    private void initializePhoto() {
        CircleImageView photo = (CircleImageView) findViewById(R.id.toolbar_dialog_photo);
        CircleImageView defaultPhoto = (CircleImageView) findViewById(R.id.toolbar_dialog_default_photo);
        Drawable icon = getResources().getDrawable(R.drawable.ic_default_user);
        String photoUrl = getIntent().getStringExtra(Value.INTENT_PHOTO);
        Util.loadPhoto(this, photoUrl, photo, defaultPhoto, icon);
        int id = getIntent().getIntExtra(Value.INTENT_ID, 0);
        photo.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/im?sel=" + id))));
    }

    @Override
    public void setInformation(int count, int inputCount, int outputCount, long firstDate, long lastDate) {

    }
}
