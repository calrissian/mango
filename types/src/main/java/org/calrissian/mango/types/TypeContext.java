package org.calrissian.mango.types;

import org.calrissian.commons.domain.Tuple;
import org.calrissian.commons.serialization.ObjectMapperContext;
import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.calrissian.mango.types.normalizers.*;
import org.calrissian.mango.types.serialization.TypedTupleMixin;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TypeContext {

    private Map<String, TypeNormalizer> typeToNormalizer = new HashMap<String, TypeNormalizer>();
    private Map<Class, TypeNormalizer> classToNormalizer = new HashMap<Class, TypeNormalizer>();

    private static TypeContext instance;

    static {

        ObjectMapper objectMapper = ObjectMapperContext.getInstance().getObjectMapper();

        objectMapper.getSerializationConfig().addMixInAnnotations(Tuple.class, TypedTupleMixin.class);
        objectMapper.getDeserializationConfig().addMixInAnnotations(Tuple.class, TypedTupleMixin.class);
    }

    public static synchronized TypeContext getInstance() {

        if(instance == null) {
            instance = new TypeContext();
        }

        return instance;
    }

    private TypeContext() {
        //load default mappings
        loadNormalizers(new BooleanNormalizer(), new DateNormalizer(), new DoubleNormalizer(),
                new IntegerNormalizer(), new LongNormalizer(), new IPv4Normalizer(), new StringNormalizer(),
                new URINormalizer());
    }

    /**
     * Gets a normalization alias for a given java types.
     * @param obj
     * @return
     */
    public String getAliasForType(Object obj) {
        TypeNormalizer typeNormalizer = classToNormalizer.get(obj.getClass());
        if(typeNormalizer != null) {
            return typeNormalizer.getAlias();
        }

        return null;
    }

    public Object fromString(String str, String objType) throws TypeNormalizationException {

        TypeNormalizer typeNormalizer = typeToNormalizer.get(objType);
        if(typeNormalizer != null) {
            return typeNormalizer.fromString(str);
        }

        throw new TypeNormalizationException("An unknown type [" + objType + "] was encountered");
    }

    public String asString(Object obj) throws TypeNormalizationException {
        TypeNormalizer typeNormalizer = classToNormalizer.get(obj.getClass());
        if(typeNormalizer != null) {
            return typeNormalizer.asString(obj);
        }

        throw new TypeNormalizationException("An unknown type [" + obj.getClass() + "] was encountered");
    }

    /**
     * Normalizes an object into its lexicographically sorted form
     * @param obj
     * @return
     */
    public String normalize(Object obj) throws TypeNormalizationException {
        TypeNormalizer typeNormalizer = classToNormalizer.get(obj.getClass());
        if(typeNormalizer != null) {
            return typeNormalizer.normalize(obj);
        }

        throw new TypeNormalizationException("An unknown type [" + obj.getClass() + "] was encountered");
    }

    /**
     * Denormalizes a normalized form into a java form.
     * @param str
     * @param objType
     * @return
     */
    public Object denormalize(String str, String objType) throws TypeNormalizationException {
        TypeNormalizer typeNormalizer = typeToNormalizer.get(objType);
        if(typeNormalizer != null) {
            return typeNormalizer.denormalize(str);
        }

        throw new TypeNormalizationException("An unknown type [" + objType + "] was encountered");
    }

    /**
     * Loads a set of normalizers so their corresponding types can be supported
     * @param mapping
     */
    public void loadNormalizers(TypeNormalizer... mapping)  {
        for(TypeNormalizer resolver: mapping) {
            typeToNormalizer.put(resolver.getAlias(), resolver);
            classToNormalizer.put(resolver.resolves(), resolver);
        }
    }

    public Collection<TypeNormalizer> getAllNormalizers() {

        return classToNormalizer.values();
    }

}
