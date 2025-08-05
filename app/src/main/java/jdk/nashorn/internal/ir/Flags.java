package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.LexicalContextNode;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/Flags.class */
public interface Flags<T extends LexicalContextNode> {
    int getFlags();

    boolean getFlag(int i2);

    T clearFlag(LexicalContext lexicalContext, int i2);

    T setFlag(LexicalContext lexicalContext, int i2);

    T setFlags(LexicalContext lexicalContext, int i2);
}
