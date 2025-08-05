package com.sun.javafx.image;

import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteIndexed;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.General;
import com.sun.javafx.image.impl.IntArgb;
import com.sun.javafx.image.impl.IntArgbPre;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritablePixelFormat;

/* loaded from: jfxrt.jar:com/sun/javafx/image/PixelUtils.class */
public class PixelUtils {
    private PixelUtils() {
    }

    public static int RgbToGray(int r2, int g2, int b2) {
        return (int) ((r2 * 0.3d) + (g2 * 0.59d) + (b2 * 0.11d));
    }

    public static int RgbToGray(int xrgb) {
        return RgbToGray((xrgb >> 16) & 255, (xrgb >> 8) & 255, xrgb & 255);
    }

    public static int NonPretoPre(int nonpre, int alpha) {
        if (alpha == 255) {
            return nonpre;
        }
        if (alpha == 0) {
            return 0;
        }
        return ((nonpre * alpha) + 127) / 255;
    }

    public static int PreToNonPre(int pre, int alpha) {
        if (alpha == 255 || alpha == 0) {
            return pre;
        }
        if (pre >= alpha) {
            return 255;
        }
        return ((pre * 255) + (alpha >> 1)) / alpha;
    }

    public static int NonPretoPre(int nonpre) {
        int a2 = nonpre >>> 24;
        if (a2 == 255) {
            return nonpre;
        }
        if (a2 == 0) {
            return 0;
        }
        int r2 = (nonpre >> 16) & 255;
        int g2 = (nonpre >> 8) & 255;
        int b2 = nonpre & 255;
        int r3 = ((r2 * a2) + 127) / 255;
        int g3 = ((g2 * a2) + 127) / 255;
        return (a2 << 24) | (r3 << 16) | (g3 << 8) | (((b2 * a2) + 127) / 255);
    }

    public static int PretoNonPre(int pre) {
        int a2 = pre >>> 24;
        if (a2 == 255 || a2 == 0) {
            return pre;
        }
        int r2 = (pre >> 16) & 255;
        int g2 = (pre >> 8) & 255;
        int b2 = pre & 255;
        int halfa = a2 >> 1;
        return (a2 << 24) | ((r2 >= a2 ? 255 : ((r2 * 255) + halfa) / a2) << 16) | ((g2 >= a2 ? 255 : ((g2 * 255) + halfa) / a2) << 8) | (b2 >= a2 ? 255 : ((b2 * 255) + halfa) / a2);
    }

    public static BytePixelGetter getByteGetter(PixelFormat<ByteBuffer> pf) {
        switch (pf.getType()) {
            case BYTE_BGRA:
                return ByteBgra.getter;
            case BYTE_BGRA_PRE:
                return ByteBgraPre.getter;
            case BYTE_RGB:
                return ByteRgb.getter;
            case BYTE_INDEXED:
                return ByteIndexed.createGetter(pf);
            case INT_ARGB:
            case INT_ARGB_PRE:
            default:
                return null;
        }
    }

    public static IntPixelGetter getIntGetter(PixelFormat<IntBuffer> pf) {
        switch (pf.getType()) {
            case BYTE_BGRA:
            case BYTE_BGRA_PRE:
            case BYTE_RGB:
            case BYTE_INDEXED:
            default:
                return null;
            case INT_ARGB:
                return IntArgb.getter;
            case INT_ARGB_PRE:
                return IntArgbPre.getter;
        }
    }

    public static <T extends Buffer> PixelGetter<T> getGetter(PixelFormat<T> pf) {
        switch (pf.getType()) {
            case BYTE_BGRA:
            case BYTE_BGRA_PRE:
            case BYTE_RGB:
            case BYTE_INDEXED:
                return getByteGetter(pf);
            case INT_ARGB:
            case INT_ARGB_PRE:
                return getIntGetter(pf);
            default:
                return null;
        }
    }

    public static BytePixelSetter getByteSetter(WritablePixelFormat<ByteBuffer> pf) {
        switch (pf.getType()) {
            case BYTE_BGRA:
                return ByteBgra.setter;
            case BYTE_BGRA_PRE:
                return ByteBgraPre.setter;
            case BYTE_RGB:
            case BYTE_INDEXED:
            case INT_ARGB:
            case INT_ARGB_PRE:
            default:
                return null;
        }
    }

    public static IntPixelSetter getIntSetter(WritablePixelFormat<IntBuffer> pf) {
        switch (pf.getType()) {
            case BYTE_BGRA:
            case BYTE_BGRA_PRE:
            case BYTE_RGB:
            case BYTE_INDEXED:
            default:
                return null;
            case INT_ARGB:
                return IntArgb.setter;
            case INT_ARGB_PRE:
                return IntArgbPre.setter;
        }
    }

    public static <T extends Buffer> PixelSetter<T> getSetter(WritablePixelFormat<T> pf) {
        switch (pf.getType()) {
            case BYTE_BGRA:
            case BYTE_BGRA_PRE:
                return getByteSetter(pf);
            case BYTE_RGB:
            case BYTE_INDEXED:
            default:
                return null;
            case INT_ARGB:
            case INT_ARGB_PRE:
                return getIntSetter(pf);
        }
    }

    public static <T extends Buffer, U extends Buffer> PixelConverter<T, U> getConverter(PixelGetter<T> src, PixelSetter<U> dst) {
        if (src instanceof BytePixelGetter) {
            if (dst instanceof BytePixelSetter) {
                return getB2BConverter((BytePixelGetter) src, (BytePixelSetter) dst);
            }
            return getB2IConverter((BytePixelGetter) src, (IntPixelSetter) dst);
        }
        if (dst instanceof BytePixelSetter) {
            return getI2BConverter((IntPixelGetter) src, (BytePixelSetter) dst);
        }
        return getI2IConverter((IntPixelGetter) src, (IntPixelSetter) dst);
    }

    public static ByteToBytePixelConverter getB2BConverter(PixelGetter<ByteBuffer> src, PixelSetter<ByteBuffer> dst) {
        if (src == ByteBgra.getter) {
            if (dst == ByteBgra.setter) {
                return ByteBgra.ToByteBgraConverter();
            }
            if (dst == ByteBgraPre.setter) {
                return ByteBgra.ToByteBgraPreConverter();
            }
        } else if (src == ByteBgraPre.getter) {
            if (dst == ByteBgra.setter) {
                return ByteBgraPre.ToByteBgraConverter();
            }
            if (dst == ByteBgraPre.setter) {
                return ByteBgraPre.ToByteBgraPreConverter();
            }
        } else if (src == ByteRgb.getter) {
            if (dst == ByteBgra.setter) {
                return ByteRgb.ToByteBgraConverter();
            }
            if (dst == ByteBgraPre.setter) {
                return ByteRgb.ToByteBgraPreConverter();
            }
            if (dst == ByteBgr.setter) {
                return ByteRgb.ToByteBgrConverter();
            }
        } else if (src == ByteBgr.getter) {
            if (dst == ByteBgr.setter) {
                return ByteBgr.ToByteBgrConverter();
            }
            if (dst == ByteBgra.setter) {
                return ByteBgr.ToByteBgraConverter();
            }
            if (dst == ByteBgraPre.setter) {
                return ByteBgr.ToByteBgraPreConverter();
            }
        } else if (src == ByteGray.getter) {
            if (dst == ByteGray.setter) {
                return ByteGray.ToByteGrayConverter();
            }
            if (dst == ByteBgr.setter) {
                return ByteGray.ToByteBgrConverter();
            }
            if (dst == ByteBgra.setter) {
                return ByteGray.ToByteBgraConverter();
            }
            if (dst == ByteBgraPre.setter) {
                return ByteGray.ToByteBgraPreConverter();
            }
        } else if ((src instanceof ByteIndexed.Getter) && (dst == ByteBgra.setter || dst == ByteBgraPre.setter)) {
            return ByteIndexed.createToByteBgraAny((BytePixelGetter) src, (BytePixelSetter) dst);
        }
        if (dst == ByteGray.setter) {
            return null;
        }
        if (src.getAlphaType() != AlphaType.OPAQUE && dst.getAlphaType() == AlphaType.OPAQUE) {
            return null;
        }
        return General.create((BytePixelGetter) src, (BytePixelSetter) dst);
    }

    public static ByteToIntPixelConverter getB2IConverter(PixelGetter<ByteBuffer> src, PixelSetter<IntBuffer> dst) {
        if (src == ByteBgra.getter) {
            if (dst == IntArgb.setter) {
                return ByteBgra.ToIntArgbConverter();
            }
            if (dst == IntArgbPre.setter) {
                return ByteBgra.ToIntArgbPreConverter();
            }
        } else if (src == ByteBgraPre.getter) {
            if (dst == IntArgb.setter) {
                return ByteBgraPre.ToIntArgbConverter();
            }
            if (dst == IntArgbPre.setter) {
                return ByteBgraPre.ToIntArgbPreConverter();
            }
        } else if (src == ByteRgb.getter) {
            if (dst == IntArgb.setter) {
                return ByteRgb.ToIntArgbConverter();
            }
            if (dst == IntArgbPre.setter) {
                return ByteRgb.ToIntArgbPreConverter();
            }
        } else if (src == ByteBgr.getter) {
            if (dst == IntArgb.setter) {
                return ByteBgr.ToIntArgbConverter();
            }
            if (dst == IntArgbPre.setter) {
                return ByteBgr.ToIntArgbPreConverter();
            }
        } else if (src == ByteGray.getter) {
            if (dst == IntArgbPre.setter) {
                return ByteGray.ToIntArgbPreConverter();
            }
            if (dst == IntArgb.setter) {
                return ByteGray.ToIntArgbConverter();
            }
        } else if ((src instanceof ByteIndexed.Getter) && (dst == IntArgb.setter || dst == IntArgbPre.setter)) {
            return ByteIndexed.createToIntArgbAny((BytePixelGetter) src, (IntPixelSetter) dst);
        }
        if (src.getAlphaType() != AlphaType.OPAQUE && dst.getAlphaType() == AlphaType.OPAQUE) {
            return null;
        }
        return General.create((BytePixelGetter) src, (IntPixelSetter) dst);
    }

    public static IntToBytePixelConverter getI2BConverter(PixelGetter<IntBuffer> src, PixelSetter<ByteBuffer> dst) {
        if (src == IntArgb.getter) {
            if (dst == ByteBgra.setter) {
                return IntArgb.ToByteBgraConverter();
            }
            if (dst == ByteBgraPre.setter) {
                return IntArgb.ToByteBgraPreConverter();
            }
        } else if (src == IntArgbPre.getter) {
            if (dst == ByteBgra.setter) {
                return IntArgbPre.ToByteBgraConverter();
            }
            if (dst == ByteBgraPre.setter) {
                return IntArgbPre.ToByteBgraPreConverter();
            }
        }
        if (dst == ByteGray.setter) {
            return null;
        }
        if (src.getAlphaType() != AlphaType.OPAQUE && dst.getAlphaType() == AlphaType.OPAQUE) {
            return null;
        }
        return General.create((IntPixelGetter) src, (BytePixelSetter) dst);
    }

    public static IntToIntPixelConverter getI2IConverter(PixelGetter<IntBuffer> src, PixelSetter<IntBuffer> dst) {
        if (src == IntArgb.getter) {
            if (dst == IntArgb.setter) {
                return IntArgb.ToIntArgbConverter();
            }
            if (dst == IntArgbPre.setter) {
                return IntArgb.ToIntArgbPreConverter();
            }
        } else if (src == IntArgbPre.getter) {
            if (dst == IntArgb.setter) {
                return IntArgbPre.ToIntArgbConverter();
            }
            if (dst == IntArgbPre.setter) {
                return IntArgbPre.ToIntArgbPreConverter();
            }
        }
        if (src.getAlphaType() != AlphaType.OPAQUE && dst.getAlphaType() == AlphaType.OPAQUE) {
            return null;
        }
        return General.create((IntPixelGetter) src, (IntPixelSetter) dst);
    }
}
