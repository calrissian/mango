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
package org.calrissian.mango.types.canonicalizer;

import org.calrissian.mango.domain.ip.IPv4;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CanonicalizerContextTest {

    @Test
    public void testFromStringRegularType() throws Exception {

        CanonicalizerContext context = new CanonicalizerContext();
        assertEquals(IPv4.fromString("1.2.3.4"), context.canonicalizeValueFromString("destinationIp", "1.2.3.4"));
        assertEquals(8080, context.canonicalizeValueFromString("sourcePort", "8080"));
    }

    @Test
    public void testRange() throws Exception {
        CanonicalizerContext context = new CanonicalizerContext();
        assertNotNull(context.canonicalizeValueFromString("sourcePort", "8080"));
        try {
            context.canonicalizeValueFromString("sourcePort", "hello");
            fail();
        } catch (Exception ignored) {
        }
        try {
            context.canonicalizeValueFromString("sourcePort", "111111");
            fail();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testRegex() throws Exception {
        CanonicalizerContext context = new CanonicalizerContext();
        assertNotNull(context.canonicalizeValueFromString("sourceIp", "1.2.3.4"));
        try {
            context.canonicalizeValueFromString("sourceIp", "hello");
            fail();
        } catch (Exception ignored) {
        }
        try {
            context.canonicalizeValueFromString("sourceIp", "111111");
            fail();
        } catch (Exception ignored) {
        }
    }
}
