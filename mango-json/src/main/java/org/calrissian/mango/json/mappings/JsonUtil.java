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
package org.calrissian.mango.json.mappings;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

class JsonUtil {

    private JsonUtil() {}
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
