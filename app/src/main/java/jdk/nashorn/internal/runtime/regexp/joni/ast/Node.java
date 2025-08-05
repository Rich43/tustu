package jdk.nashorn.internal.runtime.regexp.joni.ast;

import java.util.Set;
import jdk.nashorn.internal.runtime.regexp.joni.WarnCallback;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/ast/Node.class */
public abstract class Node implements NodeType {
    public Node parent;

    public abstract int getType();

    public abstract String getName();

    protected abstract String toString(int i2);

    public final int getType2Bit() {
        return 1 << getType();
    }

    protected void setChild(Node tgt) {
    }

    protected Node getChild() {
        return null;
    }

    public void swap(Node with) {
        if (this.parent != null) {
            this.parent.setChild(with);
        }
        if (with.parent != null) {
            with.parent.setChild(this);
        }
        Node tmp = this.parent;
        this.parent = with.parent;
        with.parent = tmp;
    }

    public void verifyTree(Set<Node> set, WarnCallback warnings) {
        if (!set.contains(this) && getChild() != null) {
            set.add(this);
            if (getChild().parent != this) {
                warnings.warn("broken link to child: " + getAddressName() + " -> " + getChild().getAddressName());
            }
            getChild().verifyTree(set, warnings);
        }
    }

    public String getAddressName() {
        return getName() + ":0x" + Integer.toHexString(System.identityHashCode(this));
    }

    public final String toString() {
        StringBuilder s2 = new StringBuilder();
        s2.append("<" + getAddressName() + " (" + (this.parent == null ? "NULL" : this.parent.getAddressName()) + ")>");
        return ((Object) s2) + toString(0);
    }

    protected static String pad(Object value, int level) {
        if (value == null) {
            return "NULL";
        }
        StringBuilder sb = new StringBuilder(Constants.INDENT);
        for (int i2 = 0; i2 < level; i2++) {
            sb.append((CharSequence) sb);
        }
        return value.toString().replace("\n", "\n" + ((Object) sb));
    }

    public final boolean isInvalidQuantifier() {
        return false;
    }

    public final boolean isAllowedInLookBehind() {
        return (getType2Bit() & NodeType.ALLOWED_IN_LB) != 0;
    }

    public final boolean isSimple() {
        return (getType2Bit() & 31) != 0;
    }
}
