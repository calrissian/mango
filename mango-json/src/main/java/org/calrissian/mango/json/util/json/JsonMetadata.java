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

import org.calrissian.mango.domain.TupleStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang.StringUtils.splitPreserveAllTokens;

/**
 * A simple utility class for dealing with setting/getting of the metadata entries for flattening
 * and re-expanding nested json trees to and from {@link TupleStore} objects.
 */
class JsonMetadata {

    private static final String ARRAY_IDX_SUFFIX = ".idx";
    private static final String JSON_PROP = "json";

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
    static final void setArrayIndex(Map<String,Object> meta, int level, int index) {
        meta.put(level + ARRAY_IDX_SUFFIX, index);
    }

    /**
     * Sets whether or not the current metadata has been created by flattening an
     * arbitrarily nested set of json.
     * @param meta
     */
    static final void setFlattenedJsonProp(Map<String,Object> meta) {
        meta.put(JSON_PROP, true);
    }

    /**
     * Sets whether or not the current metadata has been created by flattening an
     * arbitrarily nested set of json.
     * @param meta
     */
    static final boolean isFlattenedJson(Map<String,Object> meta) {
        return meta.containsKey(JSON_PROP) && meta.get(JSON_PROP).equals(true);
    }


    /**
     * Returns an array index for a specific level from the metadata from a flattened json tree.
     * @param meta
     * @param level
     * @return
     */
    static final Integer getArrayIndex(Map<String,Object> meta, int level) {
        return (Integer)meta.get(level + ARRAY_IDX_SUFFIX);
    }

    /**
     * Determines whether or not a map of metadata contains array index information at the
     * given level in a flattened json tree.
     * @param meta
     * @param level
     * @return
     */
    static final boolean hasArrayIndex(Map<String,Object> meta, int level) {
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
    static final Map<Integer, Integer> levelsToIndices(Map<String,Object> meta) {
        Set<Map.Entry<String, Object>> entries = meta.entrySet();
        Map<Integer, Integer> levelToIdx = new HashMap<Integer, Integer>();
        for(Map.Entry<String,Object> entry : entries)
            if(entry.getKey().endsWith(ARRAY_IDX_SUFFIX))
                levelToIdx.put(parseInt(splitPreserveAllTokens(entry.getKey(), "\\.")[0]), (Integer)entry.getValue());

        return levelToIdx;
    }

}
