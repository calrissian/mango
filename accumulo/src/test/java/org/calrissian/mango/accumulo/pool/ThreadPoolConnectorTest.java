package org.calrissian.mango.accumulo.pool;

import org.apache.accumulo.core.client.BatchScanner;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.mock.MockInstance;
import org.apache.accumulo.core.security.Authorizations;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class ThreadPoolConnectorTest {

    @Test
    public void testNumThreadsPoolMaxHit() throws Exception {
        Connector connector = new MockInstance().getConnector("", "".getBytes());
        ThreadPoolConnector poolConnector = new ThreadPoolConnector(connector, 10);

        //keep asking for 3
        BatchScanner bs1 = poolConnector.createBatchScanner("", new Authorizations(), 3);
        assertEquals(7, poolConnector.getNumAvailable());

        BatchScanner bs2 = poolConnector.createBatchScanner("", new Authorizations(), 3);
        assertEquals(4, poolConnector.getNumAvailable());

        BatchScanner bs3 = poolConnector.createBatchScanner("", new Authorizations(), 3);

        //only should be one left in the pool
        assertEquals(1, poolConnector.getNumAvailable());

        //ask for 3, should only return 1
        BatchScanner bs4 = poolConnector.createBatchScanner("", new Authorizations(), 3);
        assertEquals(0, poolConnector.getNumAvailable());
        assertEquals(1, ((ThreadedResource)bs4).getNumThreads());

        //ask again, should return a non threaded impl
        BatchScanner bs5 = poolConnector.createBatchScanner("", new Authorizations(), 3);
        assertFalse(bs5 instanceof ThreadedResource);

        //close one and ask again, should be a threaded resource
        bs1.close();
        assertEquals(3, poolConnector.getNumAvailable());
        BatchScanner bs6 = poolConnector.createBatchScanner("", new Authorizations(), 3);
        assertTrue(bs6 instanceof ThreadedResource);
    }
}
