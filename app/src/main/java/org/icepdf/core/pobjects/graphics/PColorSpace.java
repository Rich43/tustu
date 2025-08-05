package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/PColorSpace.class */
public abstract class PColorSpace extends Dictionary {
    private static final Logger logger = Logger.getLogger(PColorSpace.class.toString());

    public abstract int getNumComponents();

    public abstract Color getColor(float[] fArr, boolean z2);

    public String getDescription() {
        String name = getClass().getName();
        int index = name.lastIndexOf(46);
        return name.substring(index + 1);
    }

    PColorSpace(Library l2, HashMap h2) {
        super(l2, h2);
    }

    public static PColorSpace getColorSpace(Library library, Object o2) {
        if (o2 != null) {
            PColorSpace colorSpace = null;
            Reference ref = null;
            if (o2 instanceof Reference) {
                ref = (Reference) o2;
                o2 = library.getObject(ref);
            }
            if (o2 instanceof PColorSpace) {
                colorSpace = (PColorSpace) o2;
            } else if (o2 instanceof Name) {
                if (o2.equals(DeviceGray.DEVICEGRAY_KEY) || o2.equals(DeviceGray.G_KEY)) {
                    colorSpace = new DeviceGray(library, null);
                } else if (o2.equals(DeviceRGB.DEVICERGB_KEY) || o2.equals(DeviceRGB.RGB_KEY)) {
                    colorSpace = new DeviceRGB(library, null);
                } else if (o2.equals(DeviceCMYK.DEVICECMYK_KEY) || o2.equals(DeviceCMYK.CMYK_KEY)) {
                    colorSpace = new DeviceCMYK(library, null);
                } else if (o2.equals(PatternColor.PATTERN_KEY)) {
                    colorSpace = new PatternColor(library, null);
                }
            } else if (o2 instanceof List) {
                List v2 = (List) o2;
                Name colorant = (Name) v2.get(0);
                if (colorant.equals(Indexed.INDEXED_KEY) || colorant.equals(Indexed.I_KEY)) {
                    colorSpace = new Indexed(library, null, v2);
                } else if (colorant.equals(CalRGB.CALRGB_KEY)) {
                    colorSpace = new CalRGB(library, getHashMap(library, v2.get(1)));
                } else if (colorant.equals(CalGray.CAL_GRAY_KEY)) {
                    colorSpace = new CalGray(library, getHashMap(library, v2.get(1)));
                } else if (colorant.equals(Lab.LAB_KEY)) {
                    colorSpace = new Lab(library, getHashMap(library, v2.get(1)));
                } else if (colorant.equals(Separation.SEPARATION_KEY)) {
                    colorSpace = new Separation(library, null, v2.get(1), v2.get(2), v2.get(3));
                } else if (colorant.equals(DeviceN.DEVICEN_KEY)) {
                    colorSpace = new DeviceN(library, null, v2.get(1), v2.get(2), v2.get(3), v2.size() > 4 ? v2.get(4) : null);
                } else if (colorant.equals(ICCBased.ICCBASED_KEY)) {
                    colorSpace = library.getICCBased((Reference) v2.get(1));
                } else if (colorant.equals(DeviceRGB.DEVICERGB_KEY)) {
                    colorSpace = new DeviceRGB(library, null);
                } else if (colorant.equals(DeviceCMYK.DEVICECMYK_KEY)) {
                    colorSpace = new DeviceCMYK(library, null);
                } else if (colorant.equals(DeviceGray.DEVICEGRAY_KEY)) {
                    colorSpace = new DeviceGray(library, null);
                } else if (colorant.equals(PatternColor.PATTERN_KEY)) {
                    PatternColor patternColour = new PatternColor(library, null);
                    if (v2.size() > 1) {
                        if (v2.get(1) instanceof Reference) {
                            Object tmp = library.getObject((Reference) v2.get(1));
                            if (tmp instanceof PColorSpace) {
                                patternColour.setPColorSpace((PColorSpace) tmp);
                            } else if (tmp instanceof HashMap) {
                                patternColour.setPColorSpace(getColorSpace(library, tmp));
                            }
                        } else {
                            patternColour.setPColorSpace(getColorSpace(library, getHashMap(library, v2.get(1))));
                        }
                    }
                    colorSpace = patternColour;
                }
            } else if (o2 instanceof HashMap) {
                colorSpace = new PatternColor(library, (HashMap) o2);
            }
            if (colorSpace == null && logger.isLoggable(Level.FINE)) {
                logger.fine("Unsupported ColorSpace: " + o2);
            }
            if (ref != null && colorSpace != null) {
                library.addObject(colorSpace, ref);
            }
            if (colorSpace != null) {
                return colorSpace;
            }
        }
        return new DeviceGray(library, null);
    }

    private static HashMap getHashMap(Library library, Object obj) {
        HashMap entries = null;
        if (obj instanceof HashMap) {
            entries = (HashMap) obj;
        } else if (obj instanceof Reference) {
            Object obj2 = library.getObject((Reference) obj);
            if (obj2 instanceof HashMap) {
                entries = (HashMap) obj2;
            }
        }
        return entries;
    }

    public static synchronized PColorSpace getColorSpace(Library library, float n2) {
        if (n2 == 3.0f) {
            return new DeviceRGB(library, null);
        }
        if (n2 == 4.0f) {
            return new DeviceCMYK(library, null);
        }
        return new DeviceGray(library, null);
    }

    public Color getColor(float[] components) {
        return getColor(components, false);
    }

    public void normaliseComponentsToFloats(int[] in, float[] out, float maxval) {
        int count = getNumComponents();
        for (int i2 = 0; i2 < count; i2++) {
            out[i2] = in[i2] / maxval;
        }
    }

    public static float[] reverse(float[] f2) {
        float[] n2 = new float[f2.length];
        for (int i2 = 0; i2 < f2.length; i2++) {
            n2[i2] = f2[(f2.length - i2) - 1];
        }
        return n2;
    }

    public static void reverseInPlace(float[] f2) {
        int num = f2.length / 2;
        for (int i2 = 0; i2 < num; i2++) {
            float tmp = f2[i2];
            f2[i2] = f2[(f2.length - 1) - i2];
            f2[(f2.length - 1) - i2] = tmp;
        }
    }

    public static void reverseInPlace(int[] f2) {
        int num = f2.length / 2;
        for (int i2 = 0; i2 < num; i2++) {
            int tmp = f2[i2];
            f2[i2] = f2[(f2.length - 1) - i2];
            f2[(f2.length - 1) - i2] = tmp;
        }
    }
}
