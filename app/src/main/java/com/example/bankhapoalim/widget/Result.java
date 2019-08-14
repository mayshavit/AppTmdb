package com.example.bankhapoalim.widget;

public class Result<T> {
    private boolean _isSuccessful;
    private T _value;

    private Result(boolean isSuccessful) {
        _isSuccessful = isSuccessful;
    }

    public static Result successful() {
        return new Result(true);
    }

    public static Result failure() {
        return new Result(false);
    }

    public boolean isSuccessful() {
        return _isSuccessful;
    }

    public T getValue() {
        return _value;
    }

    public Result setValue(T value) {
        _value = value;
        return this;
    }
}
