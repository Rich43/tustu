package jdk.nashorn.internal.ir;

import jdk.nashorn.internal.ir.visitor.NodeVisitor;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LexicalContextNode.class */
public interface LexicalContextNode {
    Node accept(LexicalContext lexicalContext, NodeVisitor<? extends LexicalContext> nodeVisitor);

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/LexicalContextNode$Acceptor.class */
    public static class Acceptor {
        static Node accept(LexicalContextNode node, NodeVisitor<? extends LexicalContext> visitor) {
            LexicalContext lc = visitor.getLexicalContext();
            lc.push(node);
            Node newNode = node.accept(lc, visitor);
            return lc.pop(newNode);
        }
    }
}
