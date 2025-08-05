package java.awt.color;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.Serializable;
import org.icepdf.core.util.PdfOps;
import sun.java2d.cmm.CMSManager;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/awt/color/ColorSpace.class */
public abstract class ColorSpace implements Serializable {
    static final long serialVersionUID = -409452704308689724L;
    private int type;
    private int numComponents;
    private transient String[] compName = null;
    private static ColorSpace sRGBspace;
    private static ColorSpace XYZspace;
    private static ColorSpace PYCCspace;
    private static ColorSpace GRAYspace;
    private static ColorSpace LINEAR_RGBspace;
    public static final int TYPE_XYZ = 0;
    public static final int TYPE_Lab = 1;
    public static final int TYPE_Luv = 2;
    public static final int TYPE_YCbCr = 3;
    public static final int TYPE_Yxy = 4;
    public static final int TYPE_RGB = 5;
    public static final int TYPE_GRAY = 6;
    public static final int TYPE_HSV = 7;
    public static final int TYPE_HLS = 8;
    public static final int TYPE_CMYK = 9;
    public static final int TYPE_CMY = 11;
    public static final int TYPE_2CLR = 12;
    public static final int TYPE_3CLR = 13;
    public static final int TYPE_4CLR = 14;
    public static final int TYPE_5CLR = 15;
    public static final int TYPE_6CLR = 16;
    public static final int TYPE_7CLR = 17;
    public static final int TYPE_8CLR = 18;
    public static final int TYPE_9CLR = 19;
    public static final int TYPE_ACLR = 20;
    public static final int TYPE_BCLR = 21;
    public static final int TYPE_CCLR = 22;
    public static final int TYPE_DCLR = 23;
    public static final int TYPE_ECLR = 24;
    public static final int TYPE_FCLR = 25;
    public static final int CS_sRGB = 1000;
    public static final int CS_LINEAR_RGB = 1004;
    public static final int CS_CIEXYZ = 1001;
    public static final int CS_PYCC = 1002;
    public static final int CS_GRAY = 1003;

    public abstract float[] toRGB(float[] fArr);

    public abstract float[] fromRGB(float[] fArr);

    public abstract float[] toCIEXYZ(float[] fArr);

    public abstract float[] fromCIEXYZ(float[] fArr);

    protected ColorSpace(int i2, int i3) {
        this.type = i2;
        this.numComponents = i3;
    }

    public static ColorSpace getInstance(int i2) {
        ColorSpace colorSpace;
        switch (i2) {
            case 1000:
                synchronized (ColorSpace.class) {
                    if (sRGBspace == null) {
                        sRGBspace = new ICC_ColorSpace(ICC_Profile.getInstance(1000));
                    }
                    colorSpace = sRGBspace;
                }
                break;
            case 1001:
                synchronized (ColorSpace.class) {
                    if (XYZspace == null) {
                        XYZspace = new ICC_ColorSpace(ICC_Profile.getInstance(1001));
                    }
                    colorSpace = XYZspace;
                }
                break;
            case 1002:
                synchronized (ColorSpace.class) {
                    if (PYCCspace == null) {
                        PYCCspace = new ICC_ColorSpace(ICC_Profile.getInstance(1002));
                    }
                    colorSpace = PYCCspace;
                }
                break;
            case 1003:
                synchronized (ColorSpace.class) {
                    if (GRAYspace == null) {
                        GRAYspace = new ICC_ColorSpace(ICC_Profile.getInstance(1003));
                        CMSManager.GRAYspace = GRAYspace;
                    }
                    colorSpace = GRAYspace;
                }
                break;
            case 1004:
                synchronized (ColorSpace.class) {
                    if (LINEAR_RGBspace == null) {
                        LINEAR_RGBspace = new ICC_ColorSpace(ICC_Profile.getInstance(1004));
                        CMSManager.LINEAR_RGBspace = LINEAR_RGBspace;
                    }
                    colorSpace = LINEAR_RGBspace;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown color space");
        }
        return colorSpace;
    }

    public boolean isCS_sRGB() {
        return this == sRGBspace;
    }

    public int getType() {
        return this.type;
    }

    public int getNumComponents() {
        return this.numComponents;
    }

    public String getName(int i2) {
        if (i2 < 0 || i2 > this.numComponents - 1) {
            throw new IllegalArgumentException("Component index out of range: " + i2);
        }
        if (this.compName == null) {
            switch (this.type) {
                case 0:
                    this.compName = new String[]{"X", Constants._TAG_Y, com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG};
                    break;
                case 1:
                    this.compName = new String[]{"L", "a", PdfOps.b_TOKEN};
                    break;
                case 2:
                    this.compName = new String[]{"L", "u", PdfOps.v_TOKEN};
                    break;
                case 3:
                    this.compName = new String[]{Constants._TAG_Y, "Cb", "Cr"};
                    break;
                case 4:
                    this.compName = new String[]{Constants._TAG_Y, LanguageTag.PRIVATEUSE, PdfOps.y_TOKEN};
                    break;
                case 5:
                    this.compName = new String[]{"Red", "Green", "Blue"};
                    break;
                case 6:
                    this.compName = new String[]{"Gray"};
                    break;
                case 7:
                    this.compName = new String[]{"Hue", "Saturation", "Value"};
                    break;
                case 8:
                    this.compName = new String[]{"Hue", "Lightness", "Saturation"};
                    break;
                case 9:
                    this.compName = new String[]{"Cyan", "Magenta", "Yellow", "Black"};
                    break;
                case 10:
                default:
                    String[] strArr = new String[this.numComponents];
                    for (int i3 = 0; i3 < strArr.length; i3++) {
                        strArr[i3] = "Unnamed color component(" + i3 + ")";
                    }
                    this.compName = strArr;
                    break;
                case 11:
                    this.compName = new String[]{"Cyan", "Magenta", "Yellow"};
                    break;
            }
        }
        return this.compName[i2];
    }

    public float getMinValue(int i2) {
        if (i2 < 0 || i2 > this.numComponents - 1) {
            throw new IllegalArgumentException("Component index out of range: " + i2);
        }
        return 0.0f;
    }

    public float getMaxValue(int i2) {
        if (i2 < 0 || i2 > this.numComponents - 1) {
            throw new IllegalArgumentException("Component index out of range: " + i2);
        }
        return 1.0f;
    }

    static boolean isCS_CIEXYZ(ColorSpace colorSpace) {
        return colorSpace == XYZspace;
    }
}
