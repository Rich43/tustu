package org.icepdf.core.pobjects.functions.postscript;

import java.util.Iterator;
import java.util.Stack;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/postscript/Procedure.class */
public class Procedure extends Operator {
    private Stack<Object> stack;
    private Procedure previousProcedure;

    public Procedure(Procedure previousProcedure) {
        super(45);
        this.stack = new Stack<>();
        if (previousProcedure != null) {
            previousProcedure.getProc().push(this);
        }
        this.previousProcedure = previousProcedure;
    }

    public Procedure getPrevious() {
        return this.previousProcedure;
    }

    public Stack getProc() {
        return this.stack;
    }

    @Override // org.icepdf.core.pobjects.functions.postscript.Operator
    public void eval(Stack stack) {
        Iterator i$ = this.stack.iterator();
        while (i$.hasNext()) {
            Object tmp = i$.next();
            if ((tmp instanceof Operator) && !(tmp instanceof Procedure)) {
                ((Operator) tmp).eval(stack);
            } else {
                stack.push(tmp);
            }
        }
    }
}
