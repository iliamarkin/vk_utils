package ru.markin.vkutils.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java8.util.function.BiFunction;
import ru.markin.vkutils.R;
import ru.markin.vkutils.common.util.Util;

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

    public StatisticsView(final Context context) {
        super(context);
        initialize(context);
    }

    public StatisticsView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final View rootView = initialize(context);
        initializeAttributes(rootView, attrs, context);
    }

    private View initialize(final Context context) {
        final View rootView = inflate(context, R.layout.statistics, this);

        initializeView(rootView);
        getStrings(context);
        return rootView;
    }

    private void initializeAttributes(final View rootView,
                                      final AttributeSet attrs,
                                      final Context context) {
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatisticsView, 0, 0);
        final int mainTextColor = typedArray.getColor(R.styleable.StatisticsView_mainTextColor, Color.BLACK);
        final int secondaryTextColor = typedArray.getColor(R.styleable.StatisticsView_secondaryTextColor, Color.GRAY);

        typedArray.recycle();
        setMainTextColor(rootView, mainTextColor);
        setSecondaryTextColor(secondaryTextColor);
    }

    private void setMainTextColor(final View rootView, final int color) {
        final TextView duration = rootView.findViewById(R.id.statistics_duration);
        final TextView lastActivity = rootView.findViewById(R.id.statistics_last_activity);
        final TextView countPerWeek = rootView.findViewById(R.id.statistics_count_per_week);
        final TextView countPerDay = rootView.findViewById(R.id.statistics_count_per_day);
        final TextView countPerHour = rootView.findViewById(R.id.statistics_count_per_hour);

        duration.setTextColor(color);
        lastActivity.setTextColor(color);
        countPerWeek.setTextColor(color);
        countPerDay.setTextColor(color);
        countPerHour.setTextColor(color);

    }

    private void setSecondaryTextColor(final int color) {
        this.duration.setTextColor(color);
        this.lastActivityDuration.setTextColor(color);
        this.allMessagesPerWeek.setTextColor(color);
        this.incomingMessagesPerWeek.setTextColor(color);
        this.outgoingMessagesPerWeek.setTextColor(color);
        this.allMessagesPerDay.setTextColor(color);
        this.incomingMessagesPerDay.setTextColor(color);
        this.outgoingMessagesPerDay.setTextColor(color);
        this.allMessagesPerHour.setTextColor(color);
        this.incomingMessagesPerHour.setTextColor(color);
        this.outgoingMessagesPerHour.setTextColor(color);
    }

    private void getStrings(Context context) {
        this.allText = context.getString(R.string.statistics_all_messages) + ": ";
        this.incomingText = context.getString(R.string.statistics_input_messages) + ": ";
        this.outgoingText = context.getString(R.string.statistics_output_messages) + ": ";
    }

    private void initializeView(final View rootView) {
        this.duration = rootView.findViewById(R.id.statistics_text_view_duration);
        this.lastActivityDuration = rootView.findViewById(R.id.statistics_text_view_last_activity);
        this.allMessagesPerWeek = rootView.findViewById(R.id.statistics_text_view_all_messages_week);
        this.incomingMessagesPerWeek = rootView.findViewById(R.id.statistics_text_view_input_messages_week);
        this.outgoingMessagesPerWeek = rootView.findViewById(R.id.statistics_text_view_output_messages_week);
        this.allMessagesPerDay = rootView.findViewById(R.id.statistics_text_view_all_messages_day);
        this.incomingMessagesPerDay = rootView.findViewById(R.id.statistics_text_view_input_messages_day);
        this.outgoingMessagesPerDay = rootView.findViewById(R.id.statistics_text_view_output_messages_day);
        this.allMessagesPerHour = rootView.findViewById(R.id.statistics_text_view_all_messages_hour);
        this.incomingMessagesPerHour = rootView.findViewById(R.id.statistics_text_view_input_messages_hour);
        this.outgoingMessagesPerHour = rootView.findViewById(R.id.statistics_text_view_output_messages_hour);
    }

    public void setMessagesCount(final int all,
                                 final int incoming,
                                 final int outgoing,
                                 final long firstDate,
                                 final long lastDate) {
        long difference = (lastDate - firstDate) / 1000;
        difference = (difference < 3600) ? 1 : difference / 3600;
        setMessagesPerHour(Math.round(all / difference), Math.round(incoming / difference), Math.round(outgoing / difference));

        difference = (difference < 24) ? 1 : difference / 24;
        setMessagesPerDay(Math.round(all / difference), Math.round(incoming / difference), Math.round(outgoing / difference));

        difference = (difference < 7) ? 1 : difference / 7;
        setMessagesPerWeek(Math.round(all / difference), Math.round(incoming / difference), Math.round(outgoing / difference));
    }

    private void setMessagesPerWeek(final int all, final int incoming, final int outgoing) {
        final String allData = allText + all;
        final String inputData = incomingText + incoming;
        final String outputData = outgoingText + outgoing;

        this.allMessagesPerWeek.setText(allData);
        this.incomingMessagesPerWeek.setText(inputData);
        this.outgoingMessagesPerWeek.setText(outputData);
    }

    private void setMessagesPerDay(final int all, final int incoming, final int outgoing) {
        final String allData = this.allText + all;
        final String inputData = this.incomingText + incoming;
        final String outputData = this.outgoingText + outgoing;

        this.allMessagesPerDay.setText(allData);
        this.incomingMessagesPerDay.setText(inputData);
        this.outgoingMessagesPerDay.setText(outputData);
    }

    private void setMessagesPerHour(final int all, final int incoming, final int outgoing) {
        final String allData = this.allText + all;
        final String inputData = this.incomingText + incoming;
        final String outputData = this.outgoingText + outgoing;

        this.allMessagesPerHour.setText(allData);
        this.incomingMessagesPerHour.setText(inputData);
        this.outgoingMessagesPerHour.setText(outputData);
    }

    public void setDuration(final long firstDate, final long lastDate) {
        this.duration.setText(Util.getPeriod(createTextProducer(), firstDate, lastDate));
    }

    public void setLastActivityDuration(final long lastDate) {
        this.lastActivityDuration.setText(Util.getPeriod(createTextProducer(), lastDate,
                System.currentTimeMillis()));
    }

    private BiFunction<Util.TimePeriod, Integer, String> createTextProducer() {
        return (timePeriod, count) -> {
            if (count == 0) {
                return "";
            }
            switch (timePeriod) {
                case YEAR:
                    return getResources()
                            .getQuantityString(R.plurals.years, count, count) + " ";
                case MONTH:
                    return getResources()
                            .getQuantityString(R.plurals.months, count, count) + " ";
                case DAY:
                    return getResources()
                            .getQuantityString(R.plurals.days, count, count) + " ";
                case HOUR:
                    return getResources()
                            .getQuantityString(R.plurals.hours, count, count) + " ";
                case MINUTE:
                    return getResources()
                            .getQuantityString(R.plurals.minutes, count, count);
                default:
                    throw new IllegalArgumentException("Unknown enum: " + timePeriod.name());
            }
        };
    }
}
