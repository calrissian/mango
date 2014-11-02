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
package org.calrissian.mango.json.util.json;


import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * This class represents a standard way to propagate through the levels of a flattened json
 * tree so that it can be re-expanded.
 *
 * This tree structure is purposefully left package private as it is not meant to be consumed
 * or manipulated by regular users.
 */
interface JsonTreeNode {

    void visit(String[] keys, int level, Map<Integer,Integer> levelToIdx, ValueJsonNode valueJsonNode);

    JsonNode toJsonNode();
}
