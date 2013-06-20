package org.calrissian.mango.types.canonicalizer.validator;

/**
 * Date: 9/7/12
 * Time: 3:32 PM
 */
public class RangeValidator implements Validator {

    private double start;
    private double stop;

    public RangeValidator() {
    }

    public RangeValidator(double start, double stop) {
        this.start = start;
        this.stop = stop;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getStop() {
        return stop;
    }

    public void setStop(double stop) {
        this.stop = stop;
    }

    @Override
    public boolean validate(String str) {
        try {
            if(str == null) return false;
            double v = Double.parseDouble(str);
            return v >= start && v <= stop;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void configure(String config) {
        String[] split = config.split(",");
        if(split.length == 2) {
            start = Double.parseDouble(split[0]);
            stop = Double.parseDouble(split[1]);
        }
    }
}
