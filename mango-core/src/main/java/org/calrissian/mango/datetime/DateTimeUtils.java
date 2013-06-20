/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
