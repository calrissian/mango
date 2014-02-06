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


import org.calrissian.mango.types.TypeRegistry;
import org.calrissian.mango.types.canonicalizer.domain.CanonicalDef;
import org.calrissian.mango.types.canonicalizer.validator.Validator;
import org.calrissian.mango.types.exception.TypeDecodingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import static org.calrissian.mango.types.GenericTypeEncoders.DEFAULT_TYPES;

public class CanonicalizerContext {

    private final TypeRegistry<String> typeContext;
    private Map<String, Class<? extends Validator>> validatorClasses;

    private Map<String, String> matchers;
    private Map<String, String> types;
    private Map<String, Validator> validators;

    public CanonicalizerContext() throws IOException {
        this(DEFAULT_TYPES);
    }

    public CanonicalizerContext(TypeRegistry<String> typeContext) throws IOException {
        this.typeContext = typeContext;
        loadCanonicalDefs();
    }

    @SuppressWarnings("unchecked")
    private void loadCanonicalDefs() throws IOException {

        validatorClasses = new HashMap<String, Class<? extends Validator>>();
        types = new HashMap<String, String>();
        matchers = new HashMap<String, String>();
        validators = new HashMap<String, Validator>();

        InputStream is = getClass().getClassLoader().getResourceAsStream("validators.properties");
        Properties props = new Properties();
        props.load(is);
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String theKey = (String) entry.getKey();
            String[] info = theKey.split("\\.");
            if (info.length == 2) {
                if ("validator".equals(info[0])) {
                    try {
                        validatorClasses.put(info[1],
                                (Class<? extends Validator>) getClass().getClassLoader().loadClass((String) entry.getValue()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        is = getClass().getClassLoader().getResourceAsStream("canonicalDefs.properties");
        props = new Properties();
        props.load(is);

        for (Map.Entry<Object, Object> entry : props.entrySet()) {

            String theKey = (String) entry.getKey();
            String[] info = theKey.split("\\.");

            if (info.length == 2) {

                if (info[1].equals("type")) {
                    types.put(info[0], (String) entry.getValue());
                } else if (info[1].equals("matcher")) {
                    matchers.put(info[0], (String) entry.getValue());
                }

            } else if (info.length == 3) {
                if ("validator".equals(info[2])) {
                    //check validator
                    Class<? extends Validator> vclass = validatorClasses.get(info[1]);
                    if (vclass == null) throw new IllegalArgumentException("Validator not found for type: " + info[1]);
                    try {
                        Validator validator = vclass.newInstance();
                        validator.configure((String) entry.getValue());
                        validators.put(info[0], validator);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }
    }

    public Object canonicalizeValueFromString(String typeName, String value) {

        String datatype = types.get(typeName);
        if (datatype == null)
            throw new IllegalArgumentException(typeName + " is not a recognized type.");

        String matcher = matchers.get(typeName);
        Validator validator = matcher != null ? validators.get(matcher) : null;

        if (validator != null && !validator.validate(value))
            throw new IllegalArgumentException(value + " did not validate with validator: " + validator.getClass());

        try {

            return typeContext.decode(datatype, value.trim());

        } catch (RuntimeException re) {
            throw re;
        } catch (TypeDecodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<CanonicalDef> getCanonicalDefs() {

        List<CanonicalDef> canonicalDefs = new ArrayList<CanonicalDef>();
        for (Map.Entry<String, String> entry : types.entrySet()) {

            String type = entry.getKey();
            String dataType = entry.getValue();

            canonicalDefs.add(new CanonicalDef(type, dataType));
        }

        sort(canonicalDefs);
        return unmodifiableList(canonicalDefs);
    }
}
