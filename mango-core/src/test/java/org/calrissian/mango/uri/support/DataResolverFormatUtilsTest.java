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
package org.calrissian.mango.uri.support;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DataResolverFormatUtilsTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testUrlBuilder_basic() throws URISyntaxException, MalformedURLException {

        URI baseURI = new URI("service://hello?param1=value1");
        URI uri = DataResolverFormatUtils.buildRequestURI(baseURI, new String[]{"A", "B", "C"});

        assertEquals("service://hello?param1=value1#A,B,C", uri.toString());
    }

    @Test
    public void testExtractAuthsFromUrl() throws MalformedURLException, URISyntaxException {

        URI baseURI = new URI("service://hello?param1=value1#A,B,C");

        assertArrayEquals(new String[]{"A", "B", "C"}, DataResolverFormatUtils.extractAuthsFromUri(baseURI));

    }
}
