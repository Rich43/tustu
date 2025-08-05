package com.sun.javafx.fxml.expression;

import java.util.List;
import java.util.function.BiFunction;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/BinaryExpression.class */
public final class BinaryExpression<U, T> extends Expression<T> {
    private final BiFunction<U, U, T> evaluator;
    private final Expression<U> left;
    private final Expression<U> right;

    public BinaryExpression(Expression<U> left, Expression<U> right, BiFunction<U, U, T> evaluator) {
        if (left == null) {
            throw new NullPointerException();
        }
        if (right == null) {
            throw new NullPointerException();
        }
        this.left = left;
        this.right = right;
        this.evaluator = evaluator;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public T evaluate(Object obj) {
        return (T) this.evaluator.apply(this.left.evaluate(obj), this.right.evaluate(obj));
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public void update(Object namespace, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public boolean isDefined(Object namespace) {
        return this.left.isDefined(namespace) && this.right.isDefined(namespace);
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public boolean isLValue() {
        return false;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    protected void getArguments(List<KeyPath> arguments) {
        this.left.getArguments(arguments);
        this.right.getArguments(arguments);
    }
}
