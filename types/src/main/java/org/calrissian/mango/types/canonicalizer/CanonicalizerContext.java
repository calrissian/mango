package org.calrissian.mango.types.canonicalizer;


import org.calrissian.mango.types.TypeContext;
import org.calrissian.mango.types.canonicalizer.domain.CanonicalDef;
import org.calrissian.mango.types.canonicalizer.validator.Validator;
import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CanonicalizerContext {

    public static Logger logger = LoggerFactory.getLogger(CanonicalizerContext.class);

    private static CanonicalizerContext instance;
    private Map<String, Class<? extends Validator>> validatorClasses;

    public static CanonicalizerContext getInstance() {

        if (instance == null) {
            instance = new CanonicalizerContext();
        }

        return instance;
    }

    private Map<String, String> matchers;
    private Map<String, String> types;
    private Map<String, Validator> validators;

    public CanonicalizerContext() {
        try {
            loadCanonicalDefs();
        } catch (IOException e) {
            logger.error("There was an error loading the canonical definitions from the canonicalDefs.properties file.");
        }
    }

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

    public Object canonicalizeValueFromString(String typeName, String value)
            throws IllegalArgumentException {

        String datatype = types.get(typeName);
        if (datatype == null) {

            throw new IllegalArgumentException(typeName + " is not a recognized type.");
        }

        String matcher = matchers.get(typeName);
        Validator validator = matcher != null ? validators.get(matcher) : null;

        if (validator != null) {
            if (!validator.validate(value)) {

                throw new IllegalArgumentException(value + " did not validate with validator: " + validator.getClass());
            }
        }

        try {

            Object val = TypeContext.getInstance().fromString(value.trim(), datatype);
            return val;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e);
        } catch (TypeNormalizationException e) {
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

        Collections.sort(canonicalDefs);
        return canonicalDefs;
    }
}
