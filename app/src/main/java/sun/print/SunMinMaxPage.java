package sun.print;

import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:sun/print/SunMinMaxPage.class */
public final class SunMinMaxPage implements PrintRequestAttribute {
    private int page_max;
    private int page_min;

    public SunMinMaxPage(int i2, int i3) {
        this.page_min = i2;
        this.page_max = i3;
    }

    @Override // javax.print.attribute.Attribute
    public final Class getCategory() {
        return SunMinMaxPage.class;
    }

    public final int getMin() {
        return this.page_min;
    }

    public final int getMax() {
        return this.page_max;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "sun-page-minmax";
    }
}
