package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableFloatValue;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/FloatConstant.class */
public final class FloatConstant implements ObservableFloatValue {
    private final float value;

    private FloatConstant(float value) {
        this.value = value;
    }

    public static FloatConstant valueOf(float value) {
        return new FloatConstant(value);
    }

    @Override // javafx.beans.value.ObservableFloatValue
    public float get() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue, reason: merged with bridge method [inline-methods] */
    public Number getValue2() {
        return Float.valueOf(this.value);
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super Number> listener) {
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super Number> listener) {
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public int intValue() {
        return (int) this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public long longValue() {
        return (long) this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public float floatValue() {
        return this.value;
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public double doubleValue() {
        return this.value;
    }
}
