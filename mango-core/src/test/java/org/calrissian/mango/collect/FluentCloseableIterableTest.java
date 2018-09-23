/*
 * Copyright (C) 2017 The Calrissian Authors
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
package org.calrissian.mango.collect;

import com.google.common.collect.Iterables;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.calrissian.mango.collect.CloseableIterables.fromIterable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 */
public class FluentCloseableIterableTest {

    @Test
    public void testFluent() {
        CloseableIterable<Integer> closeableIterable = fromIterable(asList(1, 2, 3, 4, 5));

        FluentCloseableIterable<Integer> filter = FluentCloseableIterable.
                from(closeableIterable).
                transform(input -> input + 1).
                filter(input -> {
                    return (input % 2) == 0;      //even only
                });

        Iterables.elementsEqual(asList(2, 4, 6), filter);

        filter.close();

        //make sure closed
        try {
            filter.iterator();
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testLimit() {
        CloseableIterable<Integer> closeableIterable = fromIterable(asList(1, 2, 3, 4, 5));

        FluentCloseableIterable<Integer> limit = FluentCloseableIterable.
                from(closeableIterable).limit(3);

        assertEquals(3, Iterables.size(limit));
        limit.close();
    }
}
