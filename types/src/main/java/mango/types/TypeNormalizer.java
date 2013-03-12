package mango.types;


import mango.types.exception.TypeNormalizationException;

public interface TypeNormalizer<T> {

    /**
     * Normalizes a types into a form that can be sorted lexicographically & scanned in a range
     * @param obj
     * @return
     */
    public String normalize(T obj) throws TypeNormalizationException;

    /**
     * Denormalizes a normalized form back into a native java form.
     * @param str
     * @return
     */
    public T denormalize(String str) throws TypeNormalizationException;

    /**
     * Returns the "alias" of the types so that the normalized form can be denormalized
     * @return
     */
    public String getAlias();

    /**
     * The java class that the normalized form denormalizes to
     * @return
     */
    public Class resolves();


    /**
     * Creates the java type from a user-readable string
     * @param str
     * @return
     * @throws TypeNormalizationException
     */
    T fromString(String str) throws TypeNormalizationException;

    /**
     * Serializes the java type into a user-readable string
     * (this should be parseable by the fromString() method)
     * @param obj
     * @return
     * @throws TypeNormalizationException
     */
    String asString(T obj) throws TypeNormalizationException;
}
