package com.sun.media.jfxmedia.control;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/control/VideoFormat.class */
public enum VideoFormat {
    ARGB(1),
    BGRA_PRE(2),
    YCbCr_420p(100),
    YCbCr_422(101);

    private int nativeType;
    private static final Map<Integer, VideoFormat> lookupMap = new HashMap();

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/control/VideoFormat$FormatTypes.class */
    public static class FormatTypes {
        public static final int FORMAT_TYPE_ARGB = 1;
        public static final int FORMAT_TYPE_BGRA_PRE = 2;
        public static final int FORMAT_TYPE_YCBCR_420P = 100;
        public static final int FORMAT_TYPE_YCBCR_422 = 101;
    }

    static {
        Iterator<E> it = EnumSet.allOf(VideoFormat.class).iterator();
        while (it.hasNext()) {
            VideoFormat fmt = (VideoFormat) it.next();
            lookupMap.put(Integer.valueOf(fmt.getNativeType()), fmt);
        }
    }

    VideoFormat(int ntype) {
        this.nativeType = ntype;
    }

    public int getNativeType() {
        return this.nativeType;
    }

    public boolean isRGB() {
        return this == ARGB || this == BGRA_PRE;
    }

    public boolean isEqualTo(int ntype) {
        return this.nativeType == ntype;
    }

    public static VideoFormat formatForType(int ntype) {
        return lookupMap.get(Integer.valueOf(ntype));
    }
}
