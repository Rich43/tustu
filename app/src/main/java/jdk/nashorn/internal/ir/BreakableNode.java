package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.codegen.Label;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/BreakableNode.class */
public interface BreakableNode extends LexicalContextNode, JoinPredecessor, Labels {
    Node ensureUniqueLabels(LexicalContext lexicalContext);

    boolean isBreakableWithoutLabel();

    Label getBreakLabel();
}
