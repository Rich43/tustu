package sun.print;

import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:sun/print/SunPageSelection.class */
public final class SunPageSelection implements PrintRequestAttribute {
    public static final SunPageSelection ALL = new SunPageSelection(0);
    public static final SunPageSelection RANGE = new SunPageSelection(1);
    public static final SunPageSelection SELECTION = new SunPageSelection(2);
    private int pages;

    public SunPageSelection(int i2) {
        this.pages = i2;
    }

    @Override // javax.print.attribute.Attribute
    public final Class getCategory() {
        return SunPageSelection.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "sun-page-selection";
    }

    public String toString() {
        return "page-selection: " + this.pages;
    }
}
