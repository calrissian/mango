/*
 * Copyright (C) 2016 The Calrissian Authors
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


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.Pair;

import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.sort;
import static org.calrissian.mango.json.util.store.JsonMetadata.*;
import static org.calrissian.mango.json.util.store.JsonUtil.nodeToObject;

/**
 * Utility methods for
 * 1) Flattening a raw nested json string into a {@link org.calrissian.mango.domain.AttributeStore} object, and
 * 2) Re-expanding a flattened json object from a {@link org.calrissian.mango.domain.AttributeStore} back into a raw nested
 * json string.
 *
 * <b>NOTE:</b> It's important that the flatting/re-expansion only be done through these methods.
 * Metadata is added to various attributes in the flattened representation which acts as a guide for
 * re-expansion. If this metadata is missing or tampered with externally, this class will yield
 * unexpected results.
 *
 */
public class JsonAttributeStore {

    private static final String NESTING_DELIM = "_$";
    private static final Pair<Integer,Integer> DEFAULT_PAIR = new Pair<>(-1, -1);
    private static final Splitter SPLITTER = Splitter.on(NESTING_DELIM);

    private JsonAttributeStore() {}


    /**
     * Flattens a raw nested json string representation into a collection of attributes that
     * can be used to construct a {@link org.calrissian.mango.domain.AttributeStore} implementation.
     * @param object
     * @return
     * @throws IOException
     */
    public static Collection<Attribute> fromJson(ObjectNode object) throws IOException {
        Collection<Attribute> attributes = new HashSet<>();
        convertJsonObject(attributes, object, "", 0, new HashMap<String, String>());

        return attributes;
    }

    /**
     * Flattens a raw nested json string representation into a collection of attributes that can be used to construct a
     * {@link org.calrissian.mango.domain.AttributeStore} implementation. This method has the same effect as
     * {@link #fromJson(com.fasterxml.jackson.databind.node.ObjectNode)}, except it takes a json as a string
     * and a configured object mapper instance.
     * @param json
     * @param objectMapper
     * @return
     * @throws java.io.IOException
     */
    public static Collection<Attribute> fromJson(String json, ObjectMapper objectMapper) throws IOException {
        checkNotNull(json);
        JsonNode object = objectMapper.readTree(json);
        if(object.isObject())
            return fromJson((ObjectNode)object);
        else
            throw new IllegalArgumentException("Error parsing json or input was not json object");
    }


    /**
     * Flattens a Map<String,Object> into a collection of attributes that can be used to construct a
     * {@link org.calrissian.mango.domain.AttributeStore} implementation. This allows objects which have
     * already been parsed from json strings and processed to be turned into attribute stores as well.
     * @param map
     * @return
     */
    public static Collection<Attribute> fromMap(Map<String,Object> map) {
        checkNotNull(map);
        Collection<Attribute> attributes = new HashSet<>();
        convertObject(attributes, map, "", 0, new HashMap<String, String>());
        return attributes;
    }


    /**
     * Re-expands a flattened json representation from a {@link org.calrissian.mango.domain.AttributeStore} back into a raw
     * nested json string.
     * @param attributeCollection
     * @param objectMapper
     * @return
     */
    public static String toJsonString(Collection<Attribute> attributeCollection, ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(toObject(attributeCollection));
    }

    /**
     * Re-expands a nested attribute representation from a {@link org.calrissian.mango.domain.AttributeStore} back into a nested
     * java object representation (objects become Map<String,Object>, arrays become Lists, and non-containers stay
     * the same).
     *
     * NOTE: It is possible that this method could fail in the case where attributes were added to a attribute collection
     * which changed from a primitive type to a container type, from one container type to another, or vice versa.
     * @param attributeCollection
     * @return
     */
    public static Map<String,Object> toObject(Collection<Attribute> attributeCollection) {

        checkNotNull(attributeCollection);

        List<Attribute> attributes = new ArrayList<>(attributeCollection);

        sort(attributes, new FlattenedLevelsComparator());

        ObjectJsonNode root = new ObjectJsonNode();
        for(Attribute attribute : attributes) {
            List<String> keys = SPLITTER.splitToList(attribute.getKey());

            Map<Integer, Integer> levelsIndices = levelsToIndices(attribute.getMetadata());
            String[] adjustedKeys = new String[keys.size() + levelsIndices.size()];

            int count = 0;
            for(int i = 0; i < adjustedKeys.length; i++) {

                if(levelsIndices.containsKey(i))
                    adjustedKeys[i] = "[]";
                else {
                    adjustedKeys[i] = keys.get(count);
                    count++;
                }
            }

            root.visit(adjustedKeys, 0, levelsIndices, new ValueJsonNode(attribute.getValue()));
        }

        return root.toObject();
    }

    /**
     * This comparator is used to sort a list of attributes that represent flattened json so that raw json can
     * be reconstructed with array sort order already in tact. This helps optimize the creation of the
     * tree so that array objects can be added naturally, rather than performing excess copying of the array
     * each time an item needs to be inserted before another item in the array.
     */
    public static class FlattenedLevelsComparator implements Comparator<Attribute> {

        private Map<Attribute, Integer> levelsCache = new HashMap<>();

        /**
         * Calculating the occurrence of a string within another can be costly so .'s and ['s will be
         * cached so they only need to be done once.
         * @param attribute
         * @return
         */
        private int getLevels(Attribute attribute) {

            Integer levels = levelsCache.get(attribute);

            if(levels == null) {
                levels = SPLITTER.splitToList(attribute.getKey()).size();
                levels += JsonMetadata.levelsToIndices(attribute.getMetadata()).size();
                levelsCache.put(attribute, levels);
            }
            return levels;
        }

        @Override
        public int compare(Attribute attribute, Attribute attribute2) {

            int levels1 = getLevels(attribute);
            int levels2 = getLevels(attribute2);

            ComparisonChain comparisonChain = ComparisonChain.start();

            for(int i = 0; i < Math.max(levels1, levels2); i++) {

                Pair<Integer, Integer> pair1 = hasArrayIndex(attribute.getMetadata(), i)?
                        new Pair<>(i, getArrayIndex(attribute.getMetadata(), i)) : DEFAULT_PAIR;

                Pair<Integer, Integer> pair2 = hasArrayIndex(attribute2.getMetadata(), i) ?
                        new Pair<>(i, getArrayIndex(attribute2.getMetadata(), i)) : DEFAULT_PAIR;

                comparisonChain = comparisonChain.compare(pair1.getOne(), pair2.getOne());
                comparisonChain = comparisonChain.compare(pair1.getTwo(), pair2.getTwo());
            }

            comparisonChain = comparisonChain.compare(levels1, levels2)
                    .compare(attribute.getKey(), attribute2.getKey());



            return comparisonChain.result();
        }
    }

    private static void convertJsonObject(Collection<Attribute> attributes, JsonNode object,  String intialKey, int nestedLevel, Map<String, String> metadata) {

        Iterator<Map.Entry<String,JsonNode>> fields = object.fields();
        while(fields.hasNext()) {
            Map.Entry<String,JsonNode> entry = fields.next();

            String key = intialKey.equals("") ? entry.getKey() : intialKey + NESTING_DELIM + entry.getKey();

            if(!entry.getValue().isNull()) {
                if(entry.getValue().isObject())
                    convertJsonObject(attributes, entry.getValue(), key, nestedLevel + 1, metadata);
                else if(entry.getValue().isArray())
                    convertJsonArray(attributes, entry.getValue(), key, nestedLevel+1, metadata);
                else
                    attributes.add(new Attribute(key, nodeToObject(entry.getValue()), metadata));
            }

        }
    }

    private static void convertJsonArray(Collection<Attribute> attributes, JsonNode jsonArray, String intialKey, int nestedLevel, Map<String, String> metadata) {

        Map<String,String> map = new HashMap<>(metadata);
        for(int i = 0; i < jsonArray.size(); i++) {

            JsonNode obj = jsonArray.get(i);
            setArrayIndex(map, nestedLevel, i);

            String key = intialKey.equals("") ? "" : intialKey;

            if(!obj.isNull()) {
                if(obj.isObject())
                    convertJsonObject(attributes, obj, key, nestedLevel + 1, map);
                else if(obj.isArray())
                    convertJsonArray(attributes, obj, key, nestedLevel+1, map);
                else
                    attributes.add(new Attribute(key, nodeToObject(obj), map));
            }
        }
    }


    private static void convertObject(Collection<Attribute> attributes, Map<String, Object> object, String intialKey, int nestedLevel, Map<String, String> metadata) {

        Iterable<Map.Entry<String,Object>> fields = object.entrySet();
        for(Map.Entry<String, Object> entry : fields) {

            String key = intialKey.equals("") ? entry.getKey() : intialKey + NESTING_DELIM + entry.getKey();

            if(entry.getValue() != null) {
                if(entry.getValue() instanceof Map)
                    convertObject(attributes, (Map<String, Object>) entry.getValue(), key, nestedLevel + 1, metadata);
                else if(entry.getValue() instanceof Iterable)
                    convertIterable(attributes, (Iterable<Object>) entry.getValue(), key, nestedLevel + 1, metadata);
                else
                    attributes.add(new Attribute(key, entry.getValue(), metadata));
            }

        }
    }

    private static void convertIterable(Collection<Attribute> attributes, Iterable<Object> jsonArray, String intialKey, int nestedLevel, Map<String, String> metadata) {

        Map<String,String> map = new HashMap<>(metadata);
        int count = 0;
        for(Object obj : jsonArray) {

            setArrayIndex(map, nestedLevel, count);
            String key = intialKey.equals("") ? "" : intialKey;

            if(obj != null) {
                if(obj instanceof Map)
                    convertObject(attributes, (Map<String, Object>) obj, key, nestedLevel + 1, map);
                else if(obj instanceof Iterable)
                    convertIterable(attributes, (Iterable<Object>) obj, key, nestedLevel + 1, map);
                else
                    attributes.add(new Attribute(key, obj, map));
            }

            count++;
        }
    }

}
