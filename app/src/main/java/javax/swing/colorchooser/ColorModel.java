package javax.swing.colorchooser;

import java.awt.Component;
import javax.swing.UIManager;
import org.icepdf.core.pobjects.graphics.SoftMask;

/* loaded from: rt.jar:javax/swing/colorchooser/ColorModel.class */
class ColorModel {
    private final String prefix;
    private final String[] labels;

    ColorModel(String str, String... strArr) {
        this.prefix = "ColorChooser." + str;
        this.labels = strArr;
    }

    ColorModel() {
        this("rgb", "Red", "Green", "Blue", SoftMask.SOFT_MASK_TYPE_ALPHA);
    }

    void setColor(int i2, float[] fArr) {
        fArr[0] = normalize(i2 >> 16);
        fArr[1] = normalize(i2 >> 8);
        fArr[2] = normalize(i2);
        fArr[3] = normalize(i2 >> 24);
    }

    int getColor(float[] fArr) {
        return to8bit(fArr[2]) | (to8bit(fArr[1]) << 8) | (to8bit(fArr[0]) << 16) | (to8bit(fArr[3]) << 24);
    }

    int getCount() {
        return this.labels.length;
    }

    int getMinimum(int i2) {
        return 0;
    }

    int getMaximum(int i2) {
        return 255;
    }

    float getDefault(int i2) {
        return 0.0f;
    }

    final String getLabel(Component component, int i2) {
        return getText(component, this.labels[i2]);
    }

    private static float normalize(int i2) {
        return (i2 & 255) / 255.0f;
    }

    private static int to8bit(float f2) {
        return (int) (255.0f * f2);
    }

    final String getText(Component component, String str) {
        return UIManager.getString(this.prefix + str + "Text", component.getLocale());
    }

    final int getInteger(Component component, String str) {
        Object obj = UIManager.get(this.prefix + str, component.getLocale());
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e2) {
                return -1;
            }
        }
        return -1;
    }
}
