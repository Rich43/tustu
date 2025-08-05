package sun.print;

import javax.print.attribute.Attribute;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:sun/print/DialogOnTop.class */
public class DialogOnTop implements PrintRequestAttribute {
    private static final long serialVersionUID = -1901909867156076547L;
    long id;

    public DialogOnTop() {
    }

    public DialogOnTop(long j2) {
        this.id = j2;
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return DialogOnTop.class;
    }

    public long getID() {
        return this.id;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "dialog-on-top";
    }

    public String toString() {
        return "dialog-on-top";
    }
}
