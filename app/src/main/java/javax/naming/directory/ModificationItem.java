package javax.naming.directory;

import java.io.Serializable;

/* loaded from: rt.jar:javax/naming/directory/ModificationItem.class */
public class ModificationItem implements Serializable {
    private int mod_op;
    private Attribute attr;
    private static final long serialVersionUID = 7573258562534746850L;

    public ModificationItem(int i2, Attribute attribute) {
        switch (i2) {
            case 1:
            case 2:
            case 3:
                if (attribute == null) {
                    throw new IllegalArgumentException("Must specify non-null attribute for modification");
                }
                this.mod_op = i2;
                this.attr = attribute;
                return;
            default:
                throw new IllegalArgumentException("Invalid modification code " + i2);
        }
    }

    public int getModificationOp() {
        return this.mod_op;
    }

    public Attribute getAttribute() {
        return this.attr;
    }

    public String toString() {
        switch (this.mod_op) {
            case 1:
                return "Add attribute: " + this.attr.toString();
            case 2:
                return "Replace attribute: " + this.attr.toString();
            case 3:
                return "Remove attribute: " + this.attr.toString();
            default:
                return "";
        }
    }
}
