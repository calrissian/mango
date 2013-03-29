package org.calrissian.mango.cli;

public class ExecOption {

    private boolean optional;
    private String description;
    private String shortFlag;
    private String longFLag;
    private String defaultValue;
    private String value;
    private boolean isBoolean = false;


    public ExecOption(String shortFlag, String description, boolean optional, String defaultValue)
    {

        this.shortFlag = shortFlag;
        this.optional = optional;
        this.description = description;
        this.defaultValue = defaultValue;

    }

    public ExecOption(String shortFlag, String description, boolean optional, String defaultValue, boolean isBoolean)
    {
        this.shortFlag = shortFlag;
        this.optional = optional;
        this.description = description;
        this.defaultValue = defaultValue;
        this.isBoolean = isBoolean;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortFlag() {
        return shortFlag;
    }

    public void setShortFlag(String shortFlag) {
        this.shortFlag = shortFlag;
    }

    public String getLongFLag() {
        return longFLag;
    }

    public void setLongFLag(String longFLag) {
        this.longFLag = longFLag;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean isBoolean) {
        this.isBoolean = isBoolean;
    }
}
