package ru.markin.vkutils.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java8.util.Optional;
import java8.util.function.BiFunction;
import java8.util.stream.RefStreams;

public class Util {

    public static final SimpleDateFormat DATE_FORMAT;
    public static final SimpleDateFormat TIME_FORMAT;
    public static final SimpleDateFormat FULL_DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
        FULL_DATE_FORMAT = new SimpleDateFormat("dd.MM.yy: HH:mm", Locale.getDefault());
    }

    /**
     * Put photo from url into CircleImageView
     *
     * @param context      context
     * @param photoUrl     one or multiple url with photos
     * @param photo        target view
     * @param defaultPhoto default view with photo
     * @param icon         default icon
     */
    public static void loadPhoto(@NonNull final Context context,
                                 @NonNull final String photoUrl,
                                 @NonNull final CircleImageView photo,
                                 @NonNull final CircleImageView defaultPhoto,
                                 @NonNull final Drawable icon) {
        final String url = photoUrl.trim();
        if (url.contains(" ")) {
            photo.setImageDrawable(icon);
            loadConversationPhotoObservable(context, url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(photo::setImageBitmap, e -> Log.e("DialogsAdapterError", e.toString()));
        } else {
            Glide.with(context)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable final GlideException e,
                                                    final Object model,
                                                    final Target<Drawable> target,
                                                    final boolean isFirstResource) {
                            defaultPhoto.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(final Drawable resource,
                                                       final Object model,
                                                       final Target<Drawable> target,
                                                       final DataSource dataSource,
                                                       final boolean isFirstResource) {
                            defaultPhoto.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(photo);
        }
    }

    /**
     * Get bitmap observable with conversation photo
     *
     * @param context  context
     * @param photoUrl conversations url
     * @return observable from bitmap
     */
    private static Observable<Bitmap> loadConversationPhotoObservable(@NonNull final Context context,
                                                                      @NonNull final String photoUrl) {
        return Observable.just(photoUrl)
                .subscribeOn(Schedulers.io())
                .map(s -> s.split(" "))
                .flatMap(urls -> {
                    switch (urls.length) {
                        case 1:
                            throw new IllegalArgumentException("The url is not from conversation!");
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

    /**
     * Get combined photo
     *
     * @param context context
     * @param url1    url of the first photo
     * @param url2    url of the second photo
     * @param size    size of result image
     * @return combined photo
     */
    public static Bitmap mergeBitmaps(@NonNull final Context context,
                                      @NonNull final String url1,
                                      @NonNull final String url2,
                                      final int size) {
        //loading of the first image
        final Bitmap firstImage = loadImage(context, url1, size)
                .map(bitmap -> Bitmap.createScaledBitmap(bitmap, size, size, false))
                .map(bitmap -> Bitmap.createBitmap(bitmap, size / 4, 0, size / 2, size))
                .orElse(null);

        //loading of the second image
        final Bitmap secondImage = loadImage(context, url2, size)
                .map(bitmap -> Bitmap.createScaledBitmap(bitmap, size, size, false))
                .map(bitmap -> Bitmap.createBitmap(bitmap, size / 4, 0, size / 2, size))
                .orElse(null);

        final Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(result);
        final Paint paint = new Paint();

        canvas.drawBitmap(firstImage, 0, 0, paint);
        canvas.drawBitmap(secondImage, size / 2, 0, paint);

        return result;
    }

    /**
     * Get combined photo
     *
     * @param context context
     * @param url1    url of the first photo
     * @param url2    url of the second photo
     * @param url3    url of the third photo
     * @param size    size of result image
     * @return combined photo
     */
    public static Bitmap mergeBitmaps(@NonNull final Context context,
                                      @NonNull final String url1,
                                      @NonNull final String url2,
                                      @NonNull final String url3,
                                      final int size) {
        final Bitmap bigImage = loadImage(context, url1, size)
                .map(bitmap -> Bitmap.createScaledBitmap(bitmap, size, size, false))
                .map(bitmap -> Bitmap.createBitmap(bitmap, size / 4, 0, size / 2, size))
                .orElse(null);

        final Bitmap firstSmallImage = loadImage(context, url2, size / 2).orElse(null);
        final Bitmap secondSmallImage = loadImage(context, url3, size / 2).orElse(null);

        final Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(result);
        final Paint paint = new Paint();

        canvas.drawBitmap(bigImage, 0, 0, paint);
        canvas.drawBitmap(firstSmallImage, size / 2, 0, paint);
        canvas.drawBitmap(secondSmallImage, size / 2, size / 2, paint);

        return result;
    }

    /**
     * Get combined photo
     *
     * @param context context
     * @param url1    url of the first photo
     * @param url2    url of the second photo
     * @param url3    url of the third photo
     * @param url4    url of the fourth photo
     * @param size    size of result image
     * @return combined photo
     */
    public static Bitmap mergeBitmaps(@NonNull final Context context,
                                      @NonNull final String url1,
                                      @NonNull final String url2,
                                      @NonNull final String url3,
                                      @NonNull final String url4,
                                      final int size) {
        final Bitmap[] bitmaps = RefStreams.of(url1, url2, url3, url4)
                .map(url -> loadImage(context, url, size / 2))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(Bitmap[]::new);

        final Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(result);
        final Paint paint = new Paint();

        for (int i = 0; i < 4; i++) {
            canvas.drawBitmap(bitmaps[i],
                    bitmaps[i].getWidth() * (i % 2),
                    bitmaps[i].getHeight() * (i / 2),
                    paint);
        }

        return result;
    }

    @NonNull
    private static Optional<Bitmap> loadImage(@NonNull final Context context,
                                              @NonNull final String url,
                                              final int size) {
        try {
            return Optional.of(Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .submit(size, size)
                    .get());
        } catch (final InterruptedException e) {
            Log.e("Load image", e.getMessage());
            return Optional.empty();
        } catch (final ExecutionException e) {
            Log.e("Load image", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get text format of date from long value
     *
     * @param date date in millis
     * @return date text
     */
    public static String getDateText(final long date) {
        return DATE_FORMAT.format(new Date(date));
    }

    /**
     * Get text format of time from long value
     *
     * @param date date in millis
     * @return time text
     */
    public static String getTimeText(final long date) {
        return TIME_FORMAT.format(new Date(date));
    }

    /**
     * Get text format of date and time from long value
     *
     * @param date date in millis
     * @return date and time text
     */
    public static String getFullDateText(final long date) {
        return FULL_DATE_FORMAT.format(new Date(date));
    }

    /**
     * Get advanced text of date
     *
     * @param date      date in millis
     * @param today     today text
     * @param yesterday yesterday text
     * @return advanced text of date
     */
    public static String getAdvancedDateText(final long date,
                                             @NonNull final String today,
                                             @NonNull final String yesterday) {
        final DateTime tempDate = new DateTime();
        final LocalDate current = new DateTime(date).toLocalDate();
        final LocalDate todayDate = new DateTime(tempDate.getMillis()).toLocalDate();
        final LocalDate yesterdayDate = new DateTime(tempDate.minusDays(1).getMillis())
                .toLocalDate();

        if (current.compareTo(todayDate) == 0) {
            return today + ": " + getTimeText(date);
        } else if (current.compareTo(yesterdayDate) == 0) {
            return yesterday + ": " + getTimeText(date);
        }
        return getDateText(date);
    }

    /**
     * Get text of period
     *
     * @param textProducer producer on the necessary text
     * @param firstDate    the first interval date
     * @param lastDate     the second interval date
     * @return text of period
     */
    public static String getPeriod(@NonNull final BiFunction<TimePeriod, Integer, String> textProducer,
                                   final long firstDate,
                                   final long lastDate) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Period period = new Period(firstDate, lastDate, PeriodType.yearMonthDayTime());

        final int year = period.getYears();
        final int month = period.getMonths();
        final int day = period.getDays();
        final int hour = period.getHours();
        final int minute = period.getMinutes();

        stringBuilder.append(textProducer.apply(TimePeriod.YEAR, year))
                .append(textProducer.apply(TimePeriod.MONTH, month))
                .append(textProducer.apply(TimePeriod.DAY, day))
                .append(textProducer.apply(TimePeriod.HOUR, hour))
                .append(textProducer.apply(TimePeriod.MINUTE, minute));
        return stringBuilder.toString();
    }

    public enum TimePeriod {
        YEAR,
        MONTH,
        DAY,
        HOUR,
        MINUTE
    }
}
