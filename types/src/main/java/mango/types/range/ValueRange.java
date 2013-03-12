package mango.types.range;

public class ValueRange<T> {

    private T start;
    private T stop;

    public ValueRange(T start, T stop) {
        this.start = start;
        this.stop = stop;
    }

    public ValueRange() {
        start = null;
        stop = null;
    }

    public T getStart() {
        return start;
    }

    public T getStop() {
        return stop;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public void setStop(T stop) {
        this.stop = stop;
    }
}
