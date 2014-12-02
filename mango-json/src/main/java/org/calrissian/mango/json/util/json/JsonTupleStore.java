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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ComparisonChain;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.Pair;

import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.sort;
import static org.apache.commons.lang.StringUtils.splitPreserveAllTokens;
import static org.calrissian.mango.json.util.json.JsonMetadata.*;
import static org.calrissian.mango.json.util.json.JsonUtil.nodeToObject;

/**
 * Utility methods for
 * 1) Flattening a raw nested json string into a {@link org.calrissian.mango.domain.AttributeStore} object, and
 * 2) Re-expanding a flattened json object from a {@link org.calrissian.mango.domain.AttributeStore} back into a raw nested
 * json string.
 *
 * <b>NOTE:</b> It's important that the flatting/re-expansion only be done through these methods.
 * Metadata is added to various tuples in the flattened representation which acts as a guide for
 * re-expansion. If this metadata is missing or tampered with externally, this class will yield
 * unexpected results. Trying to re-expand a {@link org.calrissian.mango.domain.AttributeStore} which has not been previously
 * flattened using these methods will throw an {@link IllegalStateException}.
 *
 */
public class JsonTupleStore {

    private static final String JSON_DELIM = "$";
    private static final Pair<Integer,Integer> DEFAULT_PAIR = new Pair<Integer, Integer>(-1, -1);

    private static final HashFunction hf = Hashing.murmur3_32();


    private JsonTupleStore() {}


    /**
     * Flattens a raw nested json string representation into a collection of tuples that
     * can be used to construct a {@link org.calrissian.mango.domain.AttributeStore} implementation.
     * @param object
     * @return
     * @throws IOException
     */
    public static final Collection<Attribute> fromJson(ObjectNode object) throws IOException {
        Collection<Attribute> keyValues = new HashSet<Attribute>();
        convertJsonObject(keyValues, object, "", 0, new HashMap<String, Object>());

        System.out.println(keyValues);
        return keyValues;
    }

    /**
     * Flattens a raw nested json string representation into a collection of tuples that
     * can be used to construct a {@link org.calrissian.mango.domain.AttributeStore} implementation. This method has the
     * same effect as {@link #fromJson(ObjectNode)}, except it takes a json as a string
     * and a configured object mapper instance.
     * @param json
     * @param objectMapper
     * @return
     * @throws IOException
     */
    public static final Collection<Attribute> fromJson(String json, ObjectMapper objectMapper) throws IOException {
        checkNotNull(json);
        JsonNode object = objectMapper.readTree(json);
        if(object.isObject())
            return fromJson((ObjectNode)object);
        else
            throw new IllegalArgumentException("Error parsing json or input was not json object");
    }

    /**
     * Re-expands a flattened json representatin from a {@link org.calrissian.mango.domain.AttributeStore} back into a raw nested json
     * representation. This method has the same effect as {@link #toJson(Collection, ObjectMapper)},
     * except it returns a string instead of a {@link JsonNode}.
     * @param keyValueCollection
     * @param objectMapper
     * @return
     */
    public static final String toJsonString(Collection<Attribute> keyValueCollection, ObjectMapper objectMapper) {
        return toJson(keyValueCollection, objectMapper).toString();
    }

    /**
     * Re-expands a flattened json representation from a {@link org.calrissian.mango.domain.AttributeStore} back into a raw nested json
     * representation. It's important that the metadata placed in the tuples by the
     * {@link JsonTupleStore#fromJson(ObjectNode)} method have not been removed or tampered with
     * as it could yield unexpected results.
     * @param keyValueCollection
     * @return
     */
    public static final ObjectNode toJson(Collection<Attribute> keyValueCollection, ObjectMapper objectMapper) {

        checkNotNull(keyValueCollection);
        checkNotNull(objectMapper);

        List<Attribute> keyValues = new ArrayList<Attribute>(keyValueCollection);

        sort(keyValues, new FlattenedLevelsComparator());

        System.out.println(keyValues);

        JsonTreeNode root = new ObjectJsonNode(objectMapper);
        for(Attribute keyValue : keyValues) {
            if(!JsonMetadata.isFlattenedJson(keyValue.getMetadata()))
                throw new IllegalStateException("Trying to re-expand json from keyValues which were not previously flattened.");
            else {
                String[] keys = splitPreserveAllTokens(keyValue.getKey(), JSON_DELIM);

                Map<Integer, Integer> levelsIndices = levelsToIndices(keyValue.getMetadata());
                String[] adjustedKeys = new String[keys.length + levelsIndices.size()];

                int count = 0;
                for(int i = 0; i < adjustedKeys.length; i++) {

                    if(levelsIndices.containsKey(i))
                        adjustedKeys[i] = "[]";
                    else {
                        adjustedKeys[i] = keys[count];
                        count++;
                    }
                }

                root.visit(adjustedKeys, 0, levelsIndices, new ValueJsonNode(keyValue.getValue()));
            }
        }

        return (ObjectNode)root.toJsonNode();
    }


    /**
     * This comparator is used to sort a list of tuples that represent flattened json so that raw json can
     * be reconstructed with array sort order already in tact. This helps optimize the creation of the
     * tree so that array objects can be added naturally, rather than performing excess copying of the array
     * each time an item needs to be inserted before another item in the array.
     */
    private static class FlattenedLevelsComparator implements Comparator<Attribute> {

        private Map<Attribute, Integer> levelsCache = new HashMap<Attribute, Integer>();

        /**
         * Calculating the occurrence of a string within another can be costly so .'s and ['s will be
         * cached so they only need to be done once.
         * @param keyValue
         * @return
         */
        private int getLevels(Attribute keyValue) {

            Integer levels = levelsCache.get(keyValue);

            if(levels == null) {
                levels = splitPreserveAllTokens(keyValue.getKey(), JSON_DELIM).length;
                levels += JsonMetadata.levelsToIndices(keyValue.getMetadata()).size();
                levelsCache.put(keyValue, levels);
            }
            return levels;
        }

        @Override
        public int compare(Attribute keyValue, Attribute keyValue2) {

            int levels1 = getLevels(keyValue);
            int levels2 = getLevels(keyValue2);

            ComparisonChain comparisonChain = ComparisonChain.start();

            for(int i = 0; i < Math.max(levels1, levels2); i++) {

                Pair<Integer, Integer> pair1 = hasArrayIndex(keyValue.getMetadata(), i)?
                        new Pair<Integer, Integer>(i, getArrayIndex(keyValue.getMetadata(), i)) : DEFAULT_PAIR;

                Pair<Integer, Integer> pair2 = hasArrayIndex(keyValue2.getMetadata(), i) ?
                        new Pair<Integer, Integer>(i, getArrayIndex(keyValue2.getMetadata(), i)) : DEFAULT_PAIR;

                comparisonChain = comparisonChain.compare(pair1.getOne(), pair2.getOne());
                comparisonChain = comparisonChain.compare(pair1.getTwo(), pair2.getTwo());
            }

            comparisonChain = comparisonChain.compare(levels1, levels2)
                 .compare(keyValue.getKey(), keyValue2.getKey());



            return comparisonChain.result();
        }
    };

    private static final void convertJsonObject(Collection<Attribute> keyValues, JsonNode object,  String intialKey, int nestedLevel, Map<String, Object> metadata) {

        Iterator<Map.Entry<String,JsonNode>> fields = object.fields();
        while(fields.hasNext()) {
            Map.Entry<String,JsonNode> entry = fields.next();

            if(!isFlattenedJson(metadata))
                setFlattenedJsonProp(metadata);

            String key = intialKey.equals("") ? entry.getKey() : intialKey + JSON_DELIM + entry.getKey();

            if(!entry.getValue().isNull()) {
                if(entry.getValue().isObject())
                    convertJsonObject(keyValues, entry.getValue(), key, nestedLevel+1, metadata);
                else if(entry.getValue().isArray())
                    convertJsonArray(keyValues, entry.getValue(), key, nestedLevel+1, metadata);
                else
                    keyValues.add(new Attribute(key, nodeToObject(entry.getValue()), metadata));
            }

        }
    }



    private static final void convertJsonArray(Collection<Attribute> keyValues, JsonNode jsonArray, String intialKey, int nestedLevel, Map<String, Object> metadata) {

        Map<String,Object> map = new HashMap<String, Object>(metadata);
        for(int i = 0; i < jsonArray.size(); i++) {

            JsonNode obj = jsonArray.get(i);
            setArrayIndex(map, nestedLevel, i);

            String key = intialKey.equals("") ? "" : intialKey;

            if(!obj.isNull()) {
                if(obj.isObject())
                    convertJsonObject(keyValues, obj, key, nestedLevel+1, map);
                else if(obj.isArray())
                    convertJsonArray(keyValues, obj, key, nestedLevel+1, map);
                else
                    keyValues.add(new Attribute(key, nodeToObject(obj), map));
            }
        }
    }
}
