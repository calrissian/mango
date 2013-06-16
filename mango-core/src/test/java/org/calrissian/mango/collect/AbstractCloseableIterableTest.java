package org.calrissian.mango.collect;


import org.calrissian.mango.collect.mock.MockIterable;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertTrue;

public class AbstractCloseableIterableTest {

    @Test
    public void closeTest() throws Exception {
        MockIterable iterable = new MockIterable(emptyList());
        iterable.close();
        assertTrue(iterable.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void errorAfterCloseTest() throws Exception {
        MockIterable iterable = new MockIterable(emptyList());
        iterable.close();
        iterable.iterator();
    }
}
