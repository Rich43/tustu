package org.icepdf.core.pobjects.functions.postscript;

import java.util.Stack;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/postscript/Operator.class */
public abstract class Operator {
    protected int type;

    public abstract void eval(Stack stack);

    protected Operator(int type) {
        this.type = type;
    }

    public boolean equals(Object op) {
        return (op instanceof Operator) && ((Operator) op).type == this.type;
    }

    public int getType() {
        return this.type;
    }
}
