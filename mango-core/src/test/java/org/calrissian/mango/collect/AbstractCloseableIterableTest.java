/*
 * Copyright (C) 2019 The Calrissian Authors
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


import org.calrissian.mango.collect.mock.MockIterable;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertTrue;

public class AbstractCloseableIterableTest {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void closeTest() throws Exception {
        MockIterable iterable = new MockIterable(emptyList());
        iterable.close();
        assertTrue(iterable.isClosed());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test(expected = IllegalStateException.class)
    public void errorAfterCloseTest() throws Exception {
        MockIterable iterable = new MockIterable(emptyList());
        iterable.close();
        iterable.iterator();
    }
}
