package com.customviews.library.model;

/**
 * Used as a container class of a variable that is used in inner class.
 *
 * @author Md. Rashadul Alam
 */
public class MutableVariable<T> {

    private T value;

    public MutableVariable(T value) {
        setValue(value);
    }

    public T getValue() {
        return value;
    }

    public MutableVariable setValue(T value) {
        this.value = value;
        return this;
    }
}
