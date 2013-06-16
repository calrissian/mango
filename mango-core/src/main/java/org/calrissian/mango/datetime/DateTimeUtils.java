package org.calrissian.mango.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 */
public class DateTimeUtils {
    public static final long INDEXED_DATE_SORT_VAL = 999999999999999999L; // 18 char long, same length as date format pattern below
    public static final String INDEXED_DATE_FORMAT = "yyyyMMddHHmmsssSSS";

    public static String getIndexedDate(Date date) {
        checkNotNull(date);
        String formattedDateString = new SimpleDateFormat(INDEXED_DATE_FORMAT).format(date);
        long diff = INDEXED_DATE_SORT_VAL - Long.valueOf(formattedDateString);

        return Long.toString(diff);
    }
}
