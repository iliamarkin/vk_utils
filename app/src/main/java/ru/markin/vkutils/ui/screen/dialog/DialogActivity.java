package ru.markin.vkutils.ui.screen.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.markin.vkutils.R;
import ru.markin.vkutils.common.util.Util;
import ru.markin.vkutils.ui.util.Value;
import ru.markin.vkutils.presentation.presenter.dialog.DialogPresenter;
import ru.markin.vkutils.presentation.view.dialog.DialogView;
import ru.markin.vkutils.ui.base.BaseActivity;
import ru.markin.vkutils.ui.view.StatisticsView;

public class DialogActivity extends BaseActivity implements DialogView {

    @InjectPresenter
    public DialogPresenter presenter;

    private PieChart pieChart;
    private PieDataSet dataSet;
    private TextView allMessagesCountTextView;
    private TextView incomingMessagesCountTextView;
    private TextView outgoingMessagesCountTextView;
    private TextView firstDateTextView;
    private TextView lastDateTextView;
    private StatisticsView statisticsView;
    private SwipeRefreshLayout refreshLayout;
    private ViewFlipper flipper;

    @ProvidePresenter
    DialogPresenter providePresenter() {
        return new DialogPresenter(getIntent().getIntExtra(Value.INTENT_ID, 0));
    }

    @Override
    protected void initializeContentView() {
        setContentView(R.layout.activity_dialog);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void initializeToolbar(final Bundle savedInstanceState) {
        setSupportActionBar(findViewById(R.id.toolbar_dialog));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeTitle();
        initializePhoto();
    }

    @Override
    protected void initializeView(final Bundle savedInstanceState) {
        this.pieChart = findViewById(R.id.activity_dialog_pie_chart);
        this.allMessagesCountTextView = findViewById(R.id.activity_dialog_text_view_all_messages);
        this.incomingMessagesCountTextView = findViewById(R.id.activity_dialog_text_view_input_messages);
        this.outgoingMessagesCountTextView = findViewById(R.id.activity_dialog_text_view_output_messages);
        this.firstDateTextView = findViewById(R.id.activity_dialog_text_view_first_message);
        this.lastDateTextView = findViewById(R.id.activity_dialog_text_view_last_message);
        this.flipper = findViewById(R.id.activity_dialog_flipper_main);
        this.refreshLayout = findViewById(R.id.activity_dialog_swipe_layout);
        this.statisticsView = findViewById(R.id.activity_dialog_statistics_view);
        initializePieChart();
        initializeRefreshLayout();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.presenter.activityClosed();
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        this.presenter.activityClosed();
        super.onBackPressed();
    }

    @Override
    public void setInformation(final int count,
                               final int incomingCount,
                               final int outgoingCount,
                               final long firstDate,
                               final long lastDate) {
        setPieChartData(incomingCount, outgoingCount);
        setCounts(count, incomingCount, outgoingCount);
        setFirstAndLastDate(firstDate, lastDate);
        setStatistics(count, incomingCount, outgoingCount, firstDate, lastDate);
    }

    @Override
    public void doOnReady() {
        this.flipper.setDisplayedChild(1);
    }

    @Override
    public void doOnEmpty() {
        this.flipper.setDisplayedChild(2);
    }

    @Override
    public void hideRefreshLayoutProgressBar() {
        this.refreshLayout.setRefreshing(false);
    }

    private void initializeRefreshLayout() {
        this.refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.refreshLayout.setOnRefreshListener(() -> this.presenter.updateData());
    }

    private void initializeTitle() {
        final TextView title = findViewById(R.id.toolbar_dialog_title);
        title.setText(getIntent().getStringExtra(Value.INTENT_TITLE));
        title.setSelected(true);
    }

    private void initializePhoto() {
        final CircleImageView photo = findViewById(R.id.toolbar_dialog_photo);
        final CircleImageView defaultPhoto = findViewById(R.id.toolbar_dialog_default_photo);
        final Drawable icon = getResources().getDrawable(R.drawable.ic_default_user);
        final String photoUrl = getIntent().getStringExtra(Value.INTENT_PHOTO);
        final int id = getIntent().getIntExtra(Value.INTENT_ID, 0);

        Util.loadPhoto(this, photoUrl, photo, defaultPhoto, icon);
        photo.setOnClickListener(view ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/im?sel=" + id))));
    }

    private void initializePieChart() {
        final List<PieEntry> entries = new ArrayList<>();
        this.dataSet = new PieDataSet(entries, "");
        this.dataSet.setValueFormatter(new PercentFormatter());
        this.dataSet.setColors(ContextCompat.getColor(this, R.color.color_lightText),
                ContextCompat.getColor(this, R.color.colorPrimary));
        this.dataSet.setValueTextColor(Color.WHITE);

        final PieData pieData = new PieData(this.dataSet);
        pieData.setValueTextSize(10f);

        this.pieChart.setData(pieData);
        this.pieChart.setUsePercentValues(true);
        final Description description = new Description();
        description.setText("");

        this.pieChart.setCenterTextSize(10);
        this.pieChart.setDrawEntryLabels(false);
        this.pieChart.setDescription(description);

        final Legend legend = this.pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7);
        legend.setYEntrySpace(5);
    }

    private void setPieChartData(final int incomingCount,
                                 final int outgoingCount) {
        final List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(incomingCount, this.presenter.getIncomingText()));
        entries.add(new PieEntry(outgoingCount, this.presenter.getOutgoingText()));
        this.dataSet.setValues(entries);
        this.dataSet.notifyDataSetChanged();
        this.pieChart.notifyDataSetChanged();
        this.pieChart.invalidate();
    }

    @SuppressLint("SetTextI18n")
    private void setCounts(final int allCount,
                           final int incomingCount,
                           final int outgoingCount) {
        this.allMessagesCountTextView.setText(this.presenter.getAllText() + ": " + allCount);
        this.incomingMessagesCountTextView.setText(this.presenter.getIncomingText() + ": " + incomingCount);
        this.outgoingMessagesCountTextView.setText(this.presenter.getOutgoingText() + ": " + outgoingCount);
    }

    @SuppressLint("SetTextI18n")
    private void setFirstAndLastDate(final long firstDate,
                                     final long lastDate) {
        this.firstDateTextView.setText(this.presenter.getFirstDateText() + ": " + Util.getFullDateText(firstDate));
        this.lastDateTextView.setText(this.presenter.getLastDateText() + ": " + Util.getFullDateText(lastDate));
    }

    private void setStatistics(final int count,
                               final int incomingCount,
                               final int outgoingCount,
                               final long firstDate,
                               final long lastDate) {
        this.statisticsView.setMessagesCount(count, incomingCount, outgoingCount, firstDate, lastDate);
        this.statisticsView.setDuration(firstDate, lastDate);
        this.statisticsView.setLastActivityDuration(lastDate);
    }
}
