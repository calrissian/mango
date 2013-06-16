package org.calrissian.mango.types.canonicalizer.validator;

/**
 * Date: 9/7/12
 * Time: 3:32 PM
 */
public interface Validator {
    public boolean validate(String str);

    public void configure(String config);
}
