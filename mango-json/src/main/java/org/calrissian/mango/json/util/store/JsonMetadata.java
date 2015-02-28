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
package org.calrissian.mango.json.util.store;

import com.google.common.base.Splitter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;

/**
 * A simple utility class for dealing with setting/getting of the metadata entries for flattening
 * and re-expanding nested json trees to and from {@link org.calrissian.mango.domain.TupleStore} objects.
 */
class JsonMetadata {

    private static final String ARRAY_IDX_SUFFIX = ".idx";

    private JsonMetadata() {}

    /**
     * Sets an array index on a map of metadata for a specific level of a nested
     * json tree. Since the json requires that arrays have preserved order, it's
     * imported that it is constrained from the flattened to the re-expanded
     * representation.
     * @param meta
     * @param level
     * @param index
     */
    static void setArrayIndex(Map<String,String> meta, int level, int index) {
        meta.put(level + ARRAY_IDX_SUFFIX, Integer.toString(index));
    }


    /**
     * Returns an array index for a specific level from the metadata from a flattened json tree.
     * @param meta
     * @param level
     * @return
     */
    static Integer getArrayIndex(Map<String,String> meta, int level) {
        return Integer.parseInt(meta.get(level + ARRAY_IDX_SUFFIX));
    }

    /**
     * Determines whether or not a map of metadata contains array index information at the
     * given level in a flattened json tree.
     * @param meta
     * @param level
     * @return
     */
    static boolean hasArrayIndex(Map<String,String> meta, int level) {
        return meta.containsKey(level + ARRAY_IDX_SUFFIX);
    }

    /**
     * Converts all array index information in a given map of metadata to a map that is keyed only
     * by the level and each value is the index in the array for that level. This is used to
     * optimize lookup since a string is used for the key in the metadata that should not conflict
     * with keys that other applications may want to set.
     * @param meta
     * @return
     */
    static Map<Integer, Integer> levelsToIndices(Map<String,String> meta) {
        Set<Map.Entry<String, String>> entries = meta.entrySet();
        Map<Integer, Integer> levelToIdx = new HashMap<>();
        for(Map.Entry<String,String> entry : entries)
            if(entry.getKey().endsWith(ARRAY_IDX_SUFFIX))
                levelToIdx.put(parseInt(Splitter.on('.').splitToList(entry.getKey()).get(0)), parseInt(entry.getValue()));

        return levelToIdx;
    }
}
