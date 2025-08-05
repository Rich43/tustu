package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ICCBased.class */
public class ICCBased extends PColorSpace {
    private static final Logger logger = Logger.getLogger(ICCBased.class.toString());
    public static final Name ICCBASED_KEY = new Name("ICCBased");
    public static final Name N_KEY = new Name("N");
    int numcomp;
    PColorSpace alternate;
    Stream stream;
    ColorSpace colorSpace;
    private static ConcurrentHashMap<Integer, Color> iccColorCache3B;
    private static ConcurrentHashMap<Integer, Color> iccColorCache4B;
    private boolean failed;

    public ICCBased(Library l2, Stream h2) {
        super(l2, h2.getEntries());
        iccColorCache3B = new ConcurrentHashMap<>();
        iccColorCache4B = new ConcurrentHashMap<>();
        this.numcomp = h2.getInt(N_KEY);
        switch (this.numcomp) {
            case 1:
                this.alternate = new DeviceGray(l2, null);
                break;
            case 3:
                this.alternate = new DeviceRGB(l2, null);
                break;
            case 4:
                this.alternate = new DeviceCMYK(l2, null);
                break;
        }
        this.stream = h2;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        if (this.inited) {
            return;
        }
        try {
            this.stream.init();
            byte[] in = this.stream.getDecodedStreamBytes(0);
            if (logger.isLoggable(Level.FINEST)) {
                String content = Utils.convertByteArrayToByteString(in);
                logger.finest("Content = " + content);
            }
            if (in != null) {
                ICC_Profile profile = ICC_Profile.getInstance(in);
                this.colorSpace = new ICC_ColorSpace(profile);
            }
        } catch (Exception e2) {
            logger.log(Level.FINE, "Error Processing ICCBased Colour Profile", (Throwable) e2);
        }
        this.inited = true;
    }

    public PColorSpace getAlternate() {
        return this.alternate;
    }

    private static int generateKey(float[] f2) {
        int key = ((((int) (f2[0] * 255.0f)) & 255) << 16) | ((((int) (f2[1] * 255.0f)) & 255) << 8) | (((int) (f2[2] * 255.0f)) & 255 & 255);
        if (f2.length == 4) {
            key |= (((int) (f2[3] * 255.0f)) & 255) << 24;
        }
        return key;
    }

    private static Color addColorToCache(ConcurrentHashMap<Integer, Color> iccColorCache, int key, ColorSpace colorSpace, float[] f2) {
        Color color = iccColorCache.get(Integer.valueOf(key));
        if (color != null) {
            return color;
        }
        Color color2 = new Color(calculateColor(f2, colorSpace));
        iccColorCache.put(Integer.valueOf(key), color2);
        return color2;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public Color getColor(float[] f2, boolean fillAndStroke) {
        init();
        if (this.colorSpace != null && !this.failed) {
            try {
                int key = generateKey(f2);
                if (f2.length <= 3) {
                    return addColorToCache(iccColorCache3B, key, this.colorSpace, f2);
                }
                return addColorToCache(iccColorCache4B, key, this.colorSpace, f2);
            } catch (Exception e2) {
                logger.log(Level.FINE, "Error getting ICCBased colour", (Throwable) e2);
                this.failed = true;
            }
        }
        return this.alternate.getColor(f2);
    }

    private static int calculateColor(float[] f2, ColorSpace colorSpace) {
        int n2 = colorSpace.getNumComponents();
        float[] fvalue = new float[n2];
        int toCopy = n2;
        int fLength = f2.length;
        if (fLength < toCopy) {
            toCopy = fLength;
        }
        for (int i2 = 0; i2 < toCopy; i2++) {
            float curr = f2[(fLength - 1) - i2];
            if (curr < 0.0f) {
                curr = 0.0f;
            } else if (curr > 1.0f) {
                curr = 1.0f;
            }
            fvalue[i2] = curr;
        }
        float[] frgbvalue = colorSpace.toRGB(fvalue);
        return (-16777216) | ((((int) (frgbvalue[0] * 255.0f)) & 255) << 16) | ((((int) (frgbvalue[1] * 255.0f)) & 255) << 8) | (((int) (frgbvalue[2] * 255.0f)) & 255);
    }

    public ColorSpace getColorSpace() {
        return this.colorSpace;
    }

    @Override // org.icepdf.core.pobjects.graphics.PColorSpace
    public int getNumComponents() {
        return this.numcomp;
    }
}
