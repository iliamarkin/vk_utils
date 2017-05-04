package ru.markin.vkutils.ui.screen.dialog;

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
import ru.markin.vkutils.common.util.Value;
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
        incomingMessagesCountTextView = (TextView) findViewById(R.id.activity_dialog_text_view_input_messages);
        outgoingMessagesCountTextView = (TextView) findViewById(R.id.activity_dialog_text_view_output_messages);
        firstDateTextView = (TextView) findViewById(R.id.activity_dialog_text_view_first_message);
        lastDateTextView = (TextView) findViewById(R.id.activity_dialog_text_view_last_message);
        flipper = (ViewFlipper) findViewById(R.id.activity_dialog_flipper_main);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_dialog_swipe_layout);
        statisticsView = (StatisticsView) findViewById(R.id.activity_dialog_statistics_view);
        initializePieChart();
        initializeRefreshLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.activityClosed();
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        presenter.activityClosed();
        super.onBackPressed();
    }

    @Override
    public void setInformation(int count, int incomingCount, int outgoingCount, long firstDate, long lastDate) {
        setPieChartData(incomingCount, outgoingCount);
        setCounts(count, incomingCount, outgoingCount);
        setFirstAndLastDate(firstDate, lastDate);
        setStatistics(count, incomingCount, outgoingCount, firstDate, lastDate);
    }

    @Override
    public void doOnReady() {
        flipper.setDisplayedChild(1);
    }

    @Override
    public void doOnEmpty() {
        flipper.setDisplayedChild(2);
    }

    @Override
    public void hideRefreshLayoutProgressBar() {
        refreshLayout.setRefreshing(false);
    }

    private void initializeRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(() -> presenter.updateData());
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

    private void initializePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        dataSet = new PieDataSet(entries, "");
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setColors(ContextCompat.getColor(this, R.color.color_lightText), ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setValueTextColor(Color.WHITE);
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDescription(description);
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7);
        legend.setYEntrySpace(5);
    }

    private void setPieChartData(int incomingCount, int outgoingCount) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(incomingCount, presenter.getIncomingText()));
        entries.add(new PieEntry(outgoingCount, presenter.getOutgoingText()));
        dataSet.setValues(entries);
        dataSet.notifyDataSetChanged();
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    private void setCounts(int allCount, int incomingCount, int outgoingCount) {
        allMessagesCountTextView.setText(presenter.getAllText() + ": " + allCount);
        incomingMessagesCountTextView.setText(presenter.getIncomingText() + ": " + incomingCount);
        outgoingMessagesCountTextView.setText(presenter.getOutgoingText() + ": " + outgoingCount);
    }

    private void setFirstAndLastDate(long firstDate, long lastDate) {
        firstDateTextView.setText(presenter.getFirstDateText() + ": " + Util.getFullDateText(firstDate));
        lastDateTextView.setText(presenter.getLastDateText() + ": " + Util.getFullDateText(lastDate));
    }

    private void setStatistics(int count, int incomingCount, int outgoingCount, long firstDate, long lastDate) {
        statisticsView.setMessagesCount(count, incomingCount, outgoingCount, firstDate, lastDate);
        statisticsView.setDuration(firstDate, lastDate);
        statisticsView.setLastActivityDuration(lastDate);
    }
}
