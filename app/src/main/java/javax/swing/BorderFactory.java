package javax.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.StrokeBorder;
import javax.swing.border.TitledBorder;

/* loaded from: rt.jar:javax/swing/BorderFactory.class */
public class BorderFactory {
    private static Border sharedSoftRaisedBevel;
    private static Border sharedSoftLoweredBevel;
    private static Border sharedRaisedEtchedBorder;
    private static Border sharedDashedBorder;
    static final Border sharedRaisedBevel = new BevelBorder(0);
    static final Border sharedLoweredBevel = new BevelBorder(1);
    static final Border sharedEtchedBorder = new EtchedBorder();
    static final Border emptyBorder = new EmptyBorder(0, 0, 0, 0);

    private BorderFactory() {
    }

    public static Border createLineBorder(Color color) {
        return new LineBorder(color, 1);
    }

    public static Border createLineBorder(Color color, int i2) {
        return new LineBorder(color, i2);
    }

    public static Border createLineBorder(Color color, int i2, boolean z2) {
        return new LineBorder(color, i2, z2);
    }

    public static Border createRaisedBevelBorder() {
        return createSharedBevel(0);
    }

    public static Border createLoweredBevelBorder() {
        return createSharedBevel(1);
    }

    public static Border createBevelBorder(int i2) {
        return createSharedBevel(i2);
    }

    public static Border createBevelBorder(int i2, Color color, Color color2) {
        return new BevelBorder(i2, color, color2);
    }

    public static Border createBevelBorder(int i2, Color color, Color color2, Color color3, Color color4) {
        return new BevelBorder(i2, color, color2, color3, color4);
    }

    static Border createSharedBevel(int i2) {
        if (i2 == 0) {
            return sharedRaisedBevel;
        }
        if (i2 == 1) {
            return sharedLoweredBevel;
        }
        return null;
    }

    public static Border createRaisedSoftBevelBorder() {
        if (sharedSoftRaisedBevel == null) {
            sharedSoftRaisedBevel = new SoftBevelBorder(0);
        }
        return sharedSoftRaisedBevel;
    }

    public static Border createLoweredSoftBevelBorder() {
        if (sharedSoftLoweredBevel == null) {
            sharedSoftLoweredBevel = new SoftBevelBorder(1);
        }
        return sharedSoftLoweredBevel;
    }

    public static Border createSoftBevelBorder(int i2) {
        if (i2 == 0) {
            return createRaisedSoftBevelBorder();
        }
        if (i2 == 1) {
            return createLoweredSoftBevelBorder();
        }
        return null;
    }

    public static Border createSoftBevelBorder(int i2, Color color, Color color2) {
        return new SoftBevelBorder(i2, color, color2);
    }

    public static Border createSoftBevelBorder(int i2, Color color, Color color2, Color color3, Color color4) {
        return new SoftBevelBorder(i2, color, color2, color3, color4);
    }

    public static Border createEtchedBorder() {
        return sharedEtchedBorder;
    }

    public static Border createEtchedBorder(Color color, Color color2) {
        return new EtchedBorder(color, color2);
    }

    public static Border createEtchedBorder(int i2) {
        switch (i2) {
            case 0:
                if (sharedRaisedEtchedBorder == null) {
                    sharedRaisedEtchedBorder = new EtchedBorder(0);
                }
                return sharedRaisedEtchedBorder;
            case 1:
                return sharedEtchedBorder;
            default:
                throw new IllegalArgumentException("type must be one of EtchedBorder.RAISED or EtchedBorder.LOWERED");
        }
    }

    public static Border createEtchedBorder(int i2, Color color, Color color2) {
        return new EtchedBorder(i2, color, color2);
    }

    public static TitledBorder createTitledBorder(String str) {
        return new TitledBorder(str);
    }

    public static TitledBorder createTitledBorder(Border border) {
        return new TitledBorder(border);
    }

    public static TitledBorder createTitledBorder(Border border, String str) {
        return new TitledBorder(border, str);
    }

    public static TitledBorder createTitledBorder(Border border, String str, int i2, int i3) {
        return new TitledBorder(border, str, i2, i3);
    }

    public static TitledBorder createTitledBorder(Border border, String str, int i2, int i3, Font font) {
        return new TitledBorder(border, str, i2, i3, font);
    }

    public static TitledBorder createTitledBorder(Border border, String str, int i2, int i3, Font font, Color color) {
        return new TitledBorder(border, str, i2, i3, font, color);
    }

    public static Border createEmptyBorder() {
        return emptyBorder;
    }

    public static Border createEmptyBorder(int i2, int i3, int i4, int i5) {
        return new EmptyBorder(i2, i3, i4, i5);
    }

    public static CompoundBorder createCompoundBorder() {
        return new CompoundBorder();
    }

    public static CompoundBorder createCompoundBorder(Border border, Border border2) {
        return new CompoundBorder(border, border2);
    }

    public static MatteBorder createMatteBorder(int i2, int i3, int i4, int i5, Color color) {
        return new MatteBorder(i2, i3, i4, i5, color);
    }

    public static MatteBorder createMatteBorder(int i2, int i3, int i4, int i5, Icon icon) {
        return new MatteBorder(i2, i3, i4, i5, icon);
    }

    public static Border createStrokeBorder(BasicStroke basicStroke) {
        return new StrokeBorder(basicStroke);
    }

    public static Border createStrokeBorder(BasicStroke basicStroke, Paint paint) {
        return new StrokeBorder(basicStroke, paint);
    }

    public static Border createDashedBorder(Paint paint) {
        return createDashedBorder(paint, 1.0f, 1.0f, 1.0f, false);
    }

    public static Border createDashedBorder(Paint paint, float f2, float f3) {
        return createDashedBorder(paint, 1.0f, f2, f3, false);
    }

    public static Border createDashedBorder(Paint paint, float f2, float f3, float f4, boolean z2) {
        boolean z3 = !z2 && paint == null && f2 == 1.0f && f3 == 1.0f && f4 == 1.0f;
        if (z3 && sharedDashedBorder != null) {
            return sharedDashedBorder;
        }
        if (f2 < 1.0f) {
            throw new IllegalArgumentException("thickness is less than 1");
        }
        if (f3 < 1.0f) {
            throw new IllegalArgumentException("length is less than 1");
        }
        if (f4 < 0.0f) {
            throw new IllegalArgumentException("spacing is less than 0");
        }
        Border borderCreateStrokeBorder = createStrokeBorder(new BasicStroke(f2, z2 ? 1 : 2, z2 ? 1 : 0, f2 * 2.0f, new float[]{f2 * (f3 - 1.0f), f2 * (f4 + 1.0f)}, 0.0f), paint);
        if (z3) {
            sharedDashedBorder = borderCreateStrokeBorder;
        }
        return borderCreateStrokeBorder;
    }
}
