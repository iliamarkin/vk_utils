package ru.markin.vkutils.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.markin.vkutils.R;
import ru.markin.vkutils.common.Util;

public class StatisticsView extends LinearLayout {

    private TextView duration;
    private TextView lastActivityDuration;
    private TextView allMessagesPerWeek;
    private TextView incomingMessagesPerWeek;
    private TextView outgoingMessagesPerWeek;
    private TextView allMessagesPerDay;
    private TextView incomingMessagesPerDay;
    private TextView outgoingMessagesPerDay;
    private TextView allMessagesPerHour;
    private TextView incomingMessagesPerHour;
    private TextView outgoingMessagesPerHour;

    private String allText;
    private String incomingText;
    private String outgoingText;

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
        incomingMessagesPerWeek.setTextColor(color);
        outgoingMessagesPerWeek.setTextColor(color);
        allMessagesPerDay.setTextColor(color);
        incomingMessagesPerDay.setTextColor(color);
        outgoingMessagesPerDay.setTextColor(color);
        allMessagesPerHour.setTextColor(color);
        incomingMessagesPerHour.setTextColor(color);
        outgoingMessagesPerHour.setTextColor(color);
    }

    private void getStrings(Context context) {
        allText = context.getString(R.string.statistics_all_messages) + ": ";
        incomingText = context.getString(R.string.statistics_input_messages) + ": ";
        outgoingText = context.getString(R.string.statistics_output_messages) + ": ";
    }

    private void initializeView(View rootView) {
        duration = (TextView) rootView.findViewById(R.id.statistics_text_view_duration);
        lastActivityDuration = (TextView) rootView.findViewById(R.id.statistics_text_view_last_activity);
        allMessagesPerWeek = (TextView) rootView.findViewById(R.id.statistics_text_view_all_messages_week);
        incomingMessagesPerWeek = (TextView) rootView.findViewById(R.id.statistics_text_view_input_messages_week);
        outgoingMessagesPerWeek = (TextView) rootView.findViewById(R.id.statistics_text_view_output_messages_week);
        allMessagesPerDay = (TextView) rootView.findViewById(R.id.statistics_text_view_all_messages_day);
        incomingMessagesPerDay = (TextView) rootView.findViewById(R.id.statistics_text_view_input_messages_day);
        outgoingMessagesPerDay = (TextView) rootView.findViewById(R.id.statistics_text_view_output_messages_day);
        allMessagesPerHour = (TextView) rootView.findViewById(R.id.statistics_text_view_all_messages_hour);
        incomingMessagesPerHour = (TextView) rootView.findViewById(R.id.statistics_text_view_input_messages_hour);
        outgoingMessagesPerHour = (TextView) rootView.findViewById(R.id.statistics_text_view_output_messages_hour);
    }

    public void setMessagesCount(int all, int incoming, int outgoing, long firstDate, long lastDate) {
        long difference = (lastDate - firstDate) / 1000;
        difference = (difference < 3600) ? 1 : difference / 3600;
        setMessagesPerHour(Math.round(all / difference), Math.round(incoming / difference), Math.round(outgoing / difference));
        difference = (difference < 24) ? 1 : difference / 24;
        setMessagesPerDay(Math.round(all / difference), Math.round(incoming / difference), Math.round(outgoing / difference));
        difference = (difference < 7) ? 1 : difference / 7;
        setMessagesPerWeek(Math.round(all / difference), Math.round(incoming / difference), Math.round(outgoing / difference));
    }

    private void setMessagesPerWeek(int all, int incoming, int outgoing) {
        String allData = allText + all;
        String inputData = incomingText + incoming;
        String outputData = outgoingText + outgoing;
        allMessagesPerWeek.setText(allData);
        incomingMessagesPerWeek.setText(inputData);
        outgoingMessagesPerWeek.setText(outputData);
    }

    private void setMessagesPerDay(int all, int incoming, int outgoing) {
        String allData = allText + all;
        String inputData = incomingText + incoming;
        String outputData = outgoingText + outgoing;
        allMessagesPerDay.setText(allData);
        incomingMessagesPerDay.setText(inputData);
        outgoingMessagesPerDay.setText(outputData);
    }

    private void setMessagesPerHour(int all, int incoming, int outgoing) {
        String allData = allText + all;
        String inputData = incomingText + incoming;
        String outputData = outgoingText + outgoing;
        allMessagesPerHour.setText(allData);
        incomingMessagesPerHour.setText(inputData);
        outgoingMessagesPerHour.setText(outputData);
    }

    public void setDuration(long firstDate, long lastDate) {
        duration.setText(Util.getPeriod(getResources(), firstDate, lastDate));
    }

    public void setLastActivityDuration(long lastDate) {
        lastActivityDuration.setText(Util.getPeriod(getResources(), lastDate, System.currentTimeMillis()));
    }
}
