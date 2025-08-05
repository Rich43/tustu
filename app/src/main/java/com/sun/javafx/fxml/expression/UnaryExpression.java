package com.sun.javafx.fxml.expression;

import java.util.List;
import java.util.function.Function;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/UnaryExpression.class */
public final class UnaryExpression<U, T> extends Expression<T> {
    private final Expression<U> operand;
    private final Function<U, T> evaluator;

    public UnaryExpression(Expression<U> operand, Function<U, T> evaluator) {
        if (operand == null) {
            throw new NullPointerException();
        }
        this.operand = operand;
        this.evaluator = evaluator;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public T evaluate(Object obj) {
        return (T) this.evaluator.apply(this.operand.evaluate(obj));
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public void update(Object namespace, T value) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public boolean isDefined(Object namespace) {
        return this.operand.isDefined(namespace);
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    public boolean isLValue() {
        return false;
    }

    @Override // com.sun.javafx.fxml.expression.Expression
    protected void getArguments(List<KeyPath> arguments) {
        this.operand.getArguments(arguments);
    }
}
