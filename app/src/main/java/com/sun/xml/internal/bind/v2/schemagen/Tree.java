package com.sun.xml.internal.bind.v2.schemagen;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ContentModelContainer;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Occurs;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeDefParticle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/Tree.class */
abstract class Tree {
    abstract boolean isNullable();

    protected abstract void write(ContentModelContainer contentModelContainer, boolean z2, boolean z3);

    Tree() {
    }

    Tree makeOptional(boolean really) {
        return really ? new Optional() : this;
    }

    Tree makeRepeated(boolean really) {
        return really ? new Repeated() : this;
    }

    static Tree makeGroup(GroupKind kind, List<Tree> children) {
        if (children.size() == 1) {
            return children.get(0);
        }
        List<Tree> normalizedChildren = new ArrayList<>(children.size());
        for (Tree t2 : children) {
            if (t2 instanceof Group) {
                Group g2 = (Group) t2;
                if (g2.kind == kind) {
                    normalizedChildren.addAll(Arrays.asList(g2.children));
                }
            }
            normalizedChildren.add(t2);
        }
        return new Group(kind, (Tree[]) normalizedChildren.toArray(new Tree[normalizedChildren.size()]));
    }

    boolean canBeTopLevel() {
        return false;
    }

    protected void write(TypeDefParticle ct) {
        if (canBeTopLevel()) {
            write((ContentModelContainer) ct._cast(ContentModelContainer.class), false, false);
        } else {
            new Group(GroupKind.SEQUENCE, new Tree[]{this}).write(ct);
        }
    }

    protected final void writeOccurs(Occurs o2, boolean isOptional, boolean repeated) {
        if (isOptional) {
            o2.minOccurs(0);
        }
        if (repeated) {
            o2.maxOccurs(SchemaSymbols.ATTVAL_UNBOUNDED);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/Tree$Term.class */
    static abstract class Term extends Tree {
        Term() {
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        boolean isNullable() {
            return false;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/Tree$Optional.class */
    private static final class Optional extends Tree {
        private final Tree body;

        private Optional(Tree body) {
            this.body = body;
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        boolean isNullable() {
            return true;
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        Tree makeOptional(boolean really) {
            return this;
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
            this.body.write(parent, true, repeated);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/Tree$Repeated.class */
    private static final class Repeated extends Tree {
        private final Tree body;

        private Repeated(Tree body) {
            this.body = body;
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        boolean isNullable() {
            return this.body.isNullable();
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        Tree makeRepeated(boolean really) {
            return this;
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
            this.body.write(parent, isOptional, true);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/Tree$Group.class */
    private static final class Group extends Tree {
        private final GroupKind kind;
        private final Tree[] children;

        private Group(GroupKind kind, Tree... children) {
            this.kind = kind;
            this.children = children;
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        boolean canBeTopLevel() {
            return true;
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        boolean isNullable() {
            if (this.kind == GroupKind.CHOICE) {
                for (Tree t2 : this.children) {
                    if (t2.isNullable()) {
                        return true;
                    }
                }
                return false;
            }
            for (Tree t3 : this.children) {
                if (!t3.isNullable()) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.sun.xml.internal.bind.v2.schemagen.Tree
        protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
            Particle c2 = this.kind.write(parent);
            writeOccurs(c2, isOptional, repeated);
            for (Tree child : this.children) {
                child.write(c2, false, false);
            }
        }
    }
}
