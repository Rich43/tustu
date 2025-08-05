package com.sun.javafx.fxml.expression;

import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/VariableExpression.class */
public class VariableExpression extends Expression<Object> {
    private KeyPath keyPath;

    public VariableExpression(KeyPath keyPath) {
        if (keyPath == null) {
            throw new NullPointerException();
        }
        this.keyPath = keyPath;
    }

    public KeyPath getKeyPath() {
        return this.keyPath;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public Object evaluate(Object namespace) {
        return get(namespace, this.keyPath);
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public void update(Object namespace, Object value) {
        set(namespace, this.keyPath, value);
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public boolean isDefined(Object namespace) {
        return isDefined(namespace, this.keyPath);
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public boolean isLValue() {
        return true;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    protected void getArguments(List<KeyPath> arguments) {
        arguments.add(this.keyPath);
    }

    public String toString() {
        return this.keyPath.toString();
    }
}
