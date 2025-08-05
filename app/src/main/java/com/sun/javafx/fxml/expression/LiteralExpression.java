package com.sun.javafx.fxml.expression;

import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/LiteralExpression.class */
public class LiteralExpression<T> extends Expression<T> {
    private T value;

    public LiteralExpression(T value) {
        this.value = value;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public T evaluate(Object namespace) {
        return this.value;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public void update(Object namespace, T value) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public boolean isDefined(Object namespace) {
        return true;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public boolean isLValue() {
        return false;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    protected void getArguments(List<KeyPath> arguments) {
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
