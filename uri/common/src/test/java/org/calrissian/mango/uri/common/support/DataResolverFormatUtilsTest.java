package org.calrissian.mango.uri.common.support;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class DataResolverFormatUtilsTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testUrlBuilder_basic() throws URISyntaxException, MalformedURLException {

        URI baseURI = new URI("service://hello?param1=value1");
        URI uri = DataResolverFormatUtils.buildRequestURI(baseURI, new String[]{"A", "B", "C"});

        assertEquals("service://hello?param1=value1\u0000A,B,C", uri.toString());
    }

    @Test
    public void testExtractAuthsFromUrl() throws MalformedURLException, URISyntaxException {

        URI baseURI = new URI("service://hello?param1=value1\u0000A,B,C");

        assertEquals(new String[] {"A", "B" ,"C"}, DataResolverFormatUtils.extractAuthsFromUri(baseURI));

    }
}
