package sun.swing;

import java.awt.Color;
import javax.swing.plaf.ColorUIResource;

/* loaded from: rt.jar:sun/swing/PrintColorUIResource.class */
public class PrintColorUIResource extends ColorUIResource {
    private Color printColor;

    public PrintColorUIResource(int i2, Color color) {
        super(i2);
        this.printColor = color;
    }

    public Color getPrintColor() {
        return this.printColor != null ? this.printColor : this;
    }

    private Object writeReplace() {
        return new ColorUIResource(this);
    }
}
