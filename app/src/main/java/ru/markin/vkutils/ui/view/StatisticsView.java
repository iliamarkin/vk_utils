package ru.markin.vkutils.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.markin.vkutils.R;
import ru.markin.vkutils.common.Util;

public class StatisticsView extends FrameLayout {

    private TextView duration;
    private TextView lastActivityDuration;
    private TextView allMessagesPerWeek;
    private TextView inputMessagesPerWeek;
    private TextView outputMessagesPerWeek;
    private TextView allMessagesPerDay;
    private TextView inputMessagesPerDay;
    private TextView outputMessagesPerDay;
    private TextView allMessagesPerHour;
    private TextView inputMessagesPerHour;
    private TextView outputMessagesPerHour;

    private String allText;
    private String inputText;
    private String outputText;

    public StatisticsView(Context context) {
        super(context);
        initialize(context);
    }

    public StatisticsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View rootView = initialize(context);
        initializeAttributes(rootView, attrs, context);
    }

    private View initialize(Context context) {
        View rootView = inflate(context, R.layout.statistics, this);
        initializeView(rootView);
        getStrings(context);
        return rootView;
    }

    private void initializeAttributes(View rootView, AttributeSet attrs, Context context) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatisticsView, 0, 0);
        int mainTextColor = typedArray.getColor(R.styleable.StatisticsView_mainTextColor, Color.BLACK);
        int secondaryTextColor = typedArray.getColor(R.styleable.StatisticsView_secondaryTextColor, Color.GRAY);
        typedArray.recycle();
        setMainTextColor(rootView, mainTextColor);
        setSecondaryTextColor(secondaryTextColor);
    }

    private void setMainTextColor(View rootView, int color) {
        TextView duration = (TextView) rootView.findViewById(R.id.statistics_duration);
        TextView lastActivity = (TextView) rootView.findViewById(R.id.statistics_last_activity);
        TextView countPerWeek = (TextView) rootView.findViewById(R.id.statistics_count_per_week);
        TextView countPerDay = (TextView) rootView.findViewById(R.id.statistics_count_per_day);
        TextView countPerHour = (TextView) rootView.findViewById(R.id.statistics_count_per_hour);
        duration.setTextColor(color);
        lastActivity.setTextColor(color);
        countPerWeek.setTextColor(color);
        countPerDay.setTextColor(color);
        countPerHour.setTextColor(color);

    }

    private void setSecondaryTextColor(int color) {
        duration.setTextColor(color);
        lastActivityDuration.setTextColor(color);
        allMessagesPerWeek.setTextColor(color);
        inputMessagesPerWeek.setTextColor(color);
        outputMessagesPerWeek.setTextColor(color);
        allMessagesPerDay.setTextColor(color);
        inputMessagesPerDay.setTextColor(color);
        outputMessagesPerDay.setTextColor(color);
        allMessagesPerHour.setTextColor(color);
        inputMessagesPerHour.setTextColor(color);
        outputMessagesPerHour.setTextColor(color);
    }

    private void getStrings(Context context) {
        allText = context.getString(R.string.statistics_all_messages) + " ";
        inputText = context.getString(R.string.statistics_input_messages) + " ";
        outputText = context.getString(R.string.statistics_output_messages) + " ";
    }

    private void initializeView(View rootView) {
        duration = (TextView) rootView.findViewById(R.id.statistics_text_view_duration);
        lastActivityDuration = (TextView) rootView.findViewById(R.id.statistics_text_view_last_activity);
        allMessagesPerWeek = (TextView) rootView.findViewById(R.id.statistics_text_view_all_messages_week);
        inputMessagesPerWeek = (TextView) rootView.findViewById(R.id.statistics_text_view_input_messages_week);
        outputMessagesPerWeek = (TextView) rootView.findViewById(R.id.statistics_text_view_output_messages_week);
        allMessagesPerDay = (TextView) rootView.findViewById(R.id.statistics_text_view_all_messages_day);
        inputMessagesPerDay = (TextView) rootView.findViewById(R.id.statistics_text_view_input_messages_day);
        outputMessagesPerDay = (TextView) rootView.findViewById(R.id.statistics_text_view_output_messages_day);
        allMessagesPerHour = (TextView) rootView.findViewById(R.id.statistics_text_view_all_messages_hour);
        inputMessagesPerHour = (TextView) rootView.findViewById(R.id.statistics_text_view_input_messages_hour);
        outputMessagesPerHour = (TextView) rootView.findViewById(R.id.statistics_text_view_output_messages_hour);
    }

    public void setMessagesPerWeek(int all, int input, int output) {
        String allData = allText + all;
        String inputData = inputText + input;
        String outputData = outputText + output;
        allMessagesPerWeek.setText(allData);
        inputMessagesPerWeek.setText(inputData);
        outputMessagesPerWeek.setText(outputData);
    }

    public void setMessagesPerDay(int all, int input, int output) {
        String allData = allText + all;
        String inputData = inputText + input;
        String outputData = outputText + output;
        allMessagesPerDay.setText(allData);
        inputMessagesPerDay.setText(inputData);
        outputMessagesPerDay.setText(outputData);
    }

    public void setMessagesPerHour(int all, int input, int output) {
        String allData = allText + all;
        String inputData = inputText + input;
        String outputData = outputText + output;
        allMessagesPerHour.setText(allData);
        inputMessagesPerHour.setText(inputData);
        outputMessagesPerHour.setText(outputData);
    }

    public void setDuration(long firstDate, long lastDate) {
        duration.setText(Util.getPeriod(getResources(), firstDate, lastDate));
    }

    public void setLastActivityDuration(long lastDate) {
        lastActivityDuration.setText(Util.getPeriod(getResources(), lastDate, System.currentTimeMillis()));
    }
}
