package jdk.nashorn.internal.ir.visitor;

import jdk.nashorn.internal.ir.LexicalContext;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/visitor/SimpleNodeVisitor.class */
public abstract class SimpleNodeVisitor extends NodeVisitor<LexicalContext> {
    public SimpleNodeVisitor() {
        super(new LexicalContext());
    }
}
