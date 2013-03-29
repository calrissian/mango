package org.calrissian.mango.types.canonicalizer.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 9/7/12
 * Time: 3:32 PM
 */
public class RegexValidator implements Validator {

    private String regex;

    public RegexValidator() {
    }

    public RegexValidator(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean validate(String str) {
        if(str == null) return false;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @Override
    public void configure(String config) {
        this.setRegex(config);
    }
}
