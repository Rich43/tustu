package javax.swing.plaf;

import java.awt.Color;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:javax/swing/plaf/ColorUIResource.class */
public class ColorUIResource extends Color implements UIResource {
    @ConstructorProperties({"red", "green", "blue"})
    public ColorUIResource(int i2, int i3, int i4) {
        super(i2, i3, i4);
    }

    public ColorUIResource(int i2) {
        super(i2);
    }

    public ColorUIResource(float f2, float f3, float f4) {
        super(f2, f3, f4);
    }

    public ColorUIResource(Color color) {
        super(color.getRGB(), (color.getRGB() & (-16777216)) != -16777216);
    }
}
