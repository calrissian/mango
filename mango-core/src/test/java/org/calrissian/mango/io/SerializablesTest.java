/*
 * Copyright (C) 2014 The Calrissian Authors
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
package org.calrissian.mango.io;


import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.Serializable;

import static org.calrissian.mango.io.Serializables.*;
import static org.junit.Assert.assertEquals;

public class SerializablesTest {

    private static final Serializable testObject = Lists.newArrayList(1,2,3,4,5);

    @Test
    public void testSimpleSerialization() throws Exception {
        byte [] serialized = serialize(testObject);
        assertEquals(testObject, deserialize(serialized));
    }

    @Test
    public void testCompressedSerialization() throws Exception {
        byte [] serialized = serialize(testObject, true);
        assertEquals(testObject, deserialize(serialized, true));
    }

    @Test
    public void testBase64Encoding() throws Exception {
        byte[] base64 = toBase64(testObject);
        assertEquals(testObject, fromBase64(base64));
    }
}
