package ru.markin.vkutils.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.markin.vkutils.R;

public class Util {

    public static void loadPhoto(Context context, String photoUrl, CircleImageView photo, CircleImageView defaultPhoto, Drawable icon) {
        photoUrl = photoUrl.trim();
        if (photoUrl.contains(" ")) {
            photo.setImageDrawable(icon);
            loadConversationPhotoObservable(context, photoUrl)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(photo::setImageBitmap, e -> Log.d("DialogsAdapterError", e.toString()));
        } else {
            Glide.with(context)
                    .load(photoUrl)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            defaultPhoto.setVisibility(View.VISIBLE);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            defaultPhoto.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(photo);
        }
    }

    private static Observable<Bitmap> loadConversationPhotoObservable(Context context, String photoUrl) {
        return Observable.just(photoUrl)
                .subscribeOn(Schedulers.io())
                .flatMap(s -> {
                    String[] urls = s.split(" ");
                    switch (urls.length) {
                        case 2:
                            return Observable.just(Util.mergeBitmaps(context, urls[0],
                                    urls[1], 200));
                        case 3:
                            return Observable.just(Util.mergeBitmaps(context, urls[0],
                                    urls[1], urls[2], 200));
                        default:
                            return Observable.just(Util.mergeBitmaps(context, urls[0],
                                    urls[1], urls[2], urls[3], 200));
                    }
                });
    }

    @SuppressWarnings("all")
    public static Bitmap mergeBitmaps(Context context, String url1, String url2, int width)
            throws ExecutionException, InterruptedException {
        Bitmap firstImage = Glide.with(context)
                .load(url1)
                .asBitmap()
                .into(width, width)
                .get();
        firstImage = Bitmap.createScaledBitmap(firstImage, width, width, false);
        firstImage = Bitmap.createBitmap(firstImage, width / 4, 0,
                width / 2, width);

        Bitmap secondImage = Glide.with(context)
                .load(url2)
                .asBitmap()
                .into(width, width)
                .get();
        secondImage = Bitmap.createScaledBitmap(secondImage, width, width, false);
        secondImage = Bitmap.createBitmap(secondImage, width / 4, 0,
                width / 2, width);

        Bitmap result = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        canvas.drawBitmap(firstImage, 0, 0, paint);
        canvas.drawBitmap(secondImage, width / 2, 0, paint);

        return result;
    }

    @SuppressWarnings("all")
    public static Bitmap mergeBitmaps(Context context, String url1, String url2, String url3, int width)
            throws ExecutionException, InterruptedException {
        Bitmap bigImage = Glide.with(context)
                .load(url1)
                .asBitmap()
                .into(width, width)
                .get();

        bigImage = Bitmap.createScaledBitmap(bigImage, width, width, false);
        bigImage = Bitmap.createBitmap(bigImage, width / 4, 0, width / 2, width);

        Bitmap firstSmallImage = Glide.with(context)
                .load(url2)
                .asBitmap()
                .into(width / 2, width / 2)
                .get();

        Bitmap secondSmallImage = Glide.with(context)
                .load(url3)
                .asBitmap()
                .into(width / 2, width / 2)
                .get();

        Bitmap result = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        canvas.drawBitmap(bigImage, 0, 0, paint);
        canvas.drawBitmap(firstSmallImage, width / 2, 0, paint);
        canvas.drawBitmap(secondSmallImage, width / 2, width / 2, paint);

        return result;
    }

    @SuppressWarnings("all")
    public static Bitmap mergeBitmaps(Context context, String url1, String url2, String url3, String url4, int width)
            throws ExecutionException, InterruptedException {
        String[] urls = new String[] {url1, url2, url3, url4};
        Bitmap[] bitmaps = new Bitmap[4];

        for (int i = 0; i < 4; i++) {
            bitmaps[i] = Glide.with(context)
                    .load(urls[i])
                    .asBitmap()
                    .into(width / 2, width / 2)
                    .get();
        }

        Bitmap result = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        for (int i = 0; i < 4; i++) {
            canvas.drawBitmap(bitmaps[i], bitmaps[i].getWidth() * (i % 2), bitmaps[i].getHeight() * (i / 2), paint);
        }

        return result;
    }

    public static String getDateText(long date){
        DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return df1.format(new Date(date));
    }

    public static String getTimeText(long date){
        DateFormat df2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return df2.format(new Date(date));
    }

    public static String getFullDateText(long date){
        DateFormat df2 = new SimpleDateFormat("dd.MM.yy: HH:mm", Locale.getDefault());
        return df2.format(new Date(date));
    }

    public static String getAdvancedDateText(long date, String today, String yesterday) {
        String dateText = "";
        DateTime tempDate = new DateTime();
        String current = getDateText(date);
        String todayDate = getDateText(tempDate.getMillis());
        String yesterdayDate = getDateText(tempDate.minusDays(1).getMillis());
        if (current.compareTo(todayDate) == 0){
            dateText = today + ": " + getTimeText(date);
        } else if (current.compareTo(yesterdayDate) == 0){
            dateText = yesterday + ": " + getTimeText(date);
        } else {
            dateText = getDateText(date);
        }
        return dateText;
    }

    public static String getPeriod(Resources resources, long firstDate, long lastDate) {
        StringBuilder stringBuilder = new StringBuilder();
        Period period = new Period(firstDate, lastDate, PeriodType.yearMonthDayTime());
        int year = period.getYears();
        int month = period.getMonths();
        int day = period.getDays();
        int hour = period.getHours();
        int minute = period.getMinutes();
        stringBuilder.append(getYearsText(resources, year))
                .append(getMonthsText(resources, month))
                .append(getDaysText(resources, day))
                .append(getHoursText(resources, hour))
                .append(getMinutesText(resources, minute));
        return stringBuilder.toString();
    }

    private static String getYearsText(Resources resources, int year) {
        if (year != 0) {
            return resources.getQuantityString(R.plurals.years, year, year) + " ";
        }
        return "";
    }

    private static String getMonthsText(Resources resources, int month) {
        if (month != 0) {
            return resources.getQuantityString(R.plurals.months, month, month) + " ";
        }
        return "";
    }

    private static String getDaysText(Resources resources, int day) {
        if (day != 0) {
            return resources.getQuantityString(R.plurals.days, day, day) + " ";
        }
        return "";
    }

    private static String getHoursText(Resources resources, int hour) {
        if (hour != 0) {
            return resources.getQuantityString(R.plurals.hours, hour, hour) + " ";
        }
        return "";
    }

    private static String getMinutesText(Resources resources, int minute) {
        if (minute != 0) {
            return resources.getQuantityString(R.plurals.minutes, minute, minute);
        }
        return "";
    }
}
