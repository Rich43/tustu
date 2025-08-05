package com.sun.javafx.binding;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/StringConstant.class */
public class StringConstant extends StringExpression {
    private final String value;

    private StringConstant(String value) {
        this.value = value;
    }

    public static StringConstant valueOf(String value) {
        return new StringConstant(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.ObservableObjectValue
    public String get() {
        return this.value;
    }

    @Override // javafx.beans.binding.StringExpression, javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public String getValue2() {
        return this.value;
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super String> observer) {
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener observer) {
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super String> observer) {
    }
}
