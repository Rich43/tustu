package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.Expression;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/Assignment.class */
public interface Assignment<D extends Expression> {
    D getAssignmentDest();

    Expression getAssignmentSource();

    Node setAssignmentDest(D d2);
}
