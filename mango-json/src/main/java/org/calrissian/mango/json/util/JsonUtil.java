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
package org.calrissian.mango.json.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

public class JsonUtil {

    private JsonUtil() {}

    static JsonNode objectToNode(Object obj) {

        if(obj instanceof Boolean)
            return BooleanNode.valueOf((Boolean) obj);
        else if(obj instanceof Integer)
            return new IntNode((Integer)obj);
        else if(obj instanceof Long)
            return new LongNode((Long)obj);
        else if(obj instanceof Double)
            return new DoubleNode((Double)obj);
        else if (obj instanceof Float)
            return new DoubleNode(((Float) obj).doubleValue());
        else if(obj instanceof String)
            return new TextNode((String)obj);
        else return null;
    }

    static Object nodeToObject(JsonNode jsonNode) {
        if(jsonNode.isBoolean())
            return jsonNode.asBoolean();
        else if(jsonNode.isDouble())
            return jsonNode.asDouble();
        else if(jsonNode.isLong())
            return jsonNode.asLong();
        else if(jsonNode.isInt())
            return jsonNode.asInt();
        else if(jsonNode.isTextual())
            return jsonNode.asText();
        else
            return null;
    }
}
