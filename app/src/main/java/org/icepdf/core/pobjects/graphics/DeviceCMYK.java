package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/DeviceCMYK.class */
public class DeviceCMYK extends PColorSpace {
    private static final Logger logger = Logger.getLogger(DeviceCMYK.class.toString());
    public static final Name DEVICECMYK_KEY = new Name("DeviceCMYK");
    public static final Name CMYK_KEY = new Name("CMYK");
    private static final DeviceGray DEVICE_GRAY = new DeviceGray(null, null);
    private static float blackRatio = (float) Defs.doubleProperty("org.icepdf.core.cmyk.colorant.black", 1.0d);
    private static boolean disableICCCmykColorSpace = Defs.booleanProperty("org.icepdf.core.cmyk.disableICCProfile", false);
    private static ConcurrentHashMap<Integer, Color> iccCmykColorCache = new ConcurrentHashMap<>();
    private static ICC_ColorSpace iccCmykColorSpace = getIccCmykColorSpace();

    public DeviceCMYK(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return 4;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        return alternative2(f2);
    }

    private static Color alternative1(float[] f2) {
        float c2 = f2[3];
        float m2 = f2[2];
        float y2 = f2[1];
        float k2 = f2[0];
        float r2 = 1.0f - Math.min(1.0f, c2 + k2);
        float g2 = 1.0f - Math.min(1.0f, m2 + k2);
        float b2 = 1.0f - Math.min(1.0f, y2 + k2);
        return new Color(r2, g2, b2);
    }

    private static Color alternative3(float[] f2) {
        float c2 = f2[3];
        float m2 = f2[2];
        float y2 = f2[1];
        float k2 = f2[0];
        float r2 = 1.0f - Math.min(1.0f, (c2 * (1.0f - k2)) + k2);
        float g2 = 1.0f - Math.min(1.0f, (m2 * (1.0f - k2)) + k2);
        float b2 = 1.0f - Math.min(1.0f, (y2 * (1.0f - k2)) + k2);
        return new Color(r2, g2, b2);
    }

    private static Color getAutoCadColor(float[] f2) {
        float c2 = f2[3];
        float m2 = f2[2];
        float y2 = f2[1];
        float k2 = f2[0];
        int red = Math.round((1.0f - c2) * (1.0f - k2) * 255.0f);
        int blue = Math.round((1.0f - y2) * (1.0f - k2) * 255.0f);
        int green = Math.round((1.0f - m2) * (1.0f - k2) * 255.0f);
        return new Color(red, green, blue);
    }

    private static Color getGhostColor(float[] f2) {
        int cyan = (int) (f2[3] * 255.0f);
        int magenta = (int) (f2[2] * 255.0f);
        int yellow = (int) (f2[1] * 255.0f);
        int black = (int) (f2[0] * 255.0f);
        float colors = 255 - black;
        float[] rgb = {(colors * (255 - cyan)) / 255.0f, (colors * (255 - magenta)) / 255.0f, (colors * (255 - yellow)) / 255.0f};
        return new Color((int) rgb[0], (int) rgb[1], (int) rgb[2]);
    }

    private static Color alternative2(float[] f2) {
        float inCyan = f2[3];
        float inMagenta = f2[2];
        float inYellow = f2[1];
        float inBlack = f2[0];
        if (!disableICCCmykColorSpace && iccCmykColorSpace != null) {
            int key = ((((int) (f2[0] * 255.0f)) & 255) << 24) | ((((int) (f2[1] * 255.0f)) & 255) << 16) | ((((int) (f2[2] * 255.0f)) & 255) << 8) | (((int) (f2[3] * 255.0f)) & 255 & 255);
            Color color = iccCmykColorCache.get(Integer.valueOf(key));
            if (color != null) {
                return color;
            }
            try {
                f2 = iccCmykColorSpace.toRGB(reverse(f2));
                Color color2 = new Color(f2[0], f2[1], f2[2]);
                iccCmykColorCache.put(Integer.valueOf(key), color2);
                return color2;
            } catch (Throwable th) {
                logger.warning("Error using iccCmykColorSpace in DeviceCMYK.");
            }
        }
        if (inCyan != 0.0f || inMagenta != 0.0f || inYellow != 0.0f) {
            float inBlack2 = inBlack * blackRatio;
            double c2 = clip(0.0d, 1.0d, inCyan + inBlack2);
            double m2 = clip(0.0d, 1.0d, inMagenta + inBlack2);
            double y2 = clip(0.0d, 1.0d, inYellow + inBlack2);
            double aw2 = (1.0d - c2) * (1.0d - m2) * (1.0d - y2);
            double ac2 = c2 * (1.0d - m2) * (1.0d - y2);
            double am2 = (1.0d - c2) * m2 * (1.0d - y2);
            double ay2 = (1.0d - c2) * (1.0d - m2) * y2;
            double ar2 = (1.0d - c2) * m2 * y2;
            double ag2 = c2 * (1.0d - m2) * y2;
            double ab2 = c2 * m2 * (1.0d - y2);
            float outRed = (float) clip(0.0d, 1.0d, aw2 + (0.9137d * am2) + (0.9961d * ay2) + (0.9882d * ar2));
            float outGreen = (float) clip(0.0d, 1.0d, aw2 + (0.6196d * ac2) + ay2 + (0.5176d * ag2));
            float outBlue = (float) clip(0.0d, 1.0d, aw2 + (0.7804d * ac2) + (0.5412d * am2) + (0.0667d * ar2) + (0.2118d * ag2) + (0.4863d * ab2));
            return new Color(outRed, outGreen, outBlue);
        }
        f2[0] = 1.0f - f2[0];
        return DEVICE_GRAY.getColor(f2);
    }

    private static double clip(double floor, double ceiling, double value) {
        if (value < floor) {
            value = floor;
        }
        if (value > ceiling) {
            value = ceiling;
        }
        return value;
    }

    public static ICC_ColorSpace getIccCmykColorSpace() {
        Object profileStream;
        String customCMYKProfilePath = null;
        try {
            customCMYKProfilePath = Defs.sysProperty("org.icepdf.core.pobjects.graphics.cmyk");
            if (customCMYKProfilePath == null) {
                customCMYKProfilePath = "/org/icepdf/core/pobjects/graphics/res/CoatedFOGRA27.icc";
                profileStream = DeviceCMYK.class.getResourceAsStream(customCMYKProfilePath);
            } else {
                profileStream = new FileInputStream(customCMYKProfilePath);
            }
            ICC_Profile icc_profile = ICC_Profile.getInstance((InputStream) profileStream);
            return new ICC_ColorSpace(icc_profile);
        } catch (Exception e2) {
            logger.warning("Error loading ICC color profile: " + customCMYKProfilePath);
            return null;
        }
    }

    public static boolean isDisableICCCmykColorSpace() {
        return disableICCCmykColorSpace;
    }

    public static void setDisableICCCmykColorSpace(boolean disableICCCmykColorSpace2) {
        disableICCCmykColorSpace = disableICCCmykColorSpace2;
    }
}
