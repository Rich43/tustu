package sun.print;

import java.awt.Frame;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:sun/print/DialogOwner.class */
public final class DialogOwner implements PrintRequestAttribute {
    private Frame dlgOwner;

    public DialogOwner(Frame frame) {
        this.dlgOwner = frame;
    }

    public Frame getOwner() {
        return this.dlgOwner;
    }

    @Override // javax.print.attribute.Attribute
    public final Class getCategory() {
        return DialogOwner.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "dialog-owner";
    }
}
