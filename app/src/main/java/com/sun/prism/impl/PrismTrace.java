package com.sun.prism.impl;

import java.util.HashMap;
import java.util.Map;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/prism/impl/PrismTrace.class */
public class PrismTrace {
    private static final boolean enabled = PrismSettings.printAllocs;
    private static Map<Long, Long> texData;
    private static long texBytes;
    private static Map<Long, Long> rttData;
    private static long rttBytes;

    /* loaded from: jfxrt.jar:com/sun/prism/impl/PrismTrace$SummaryType.class */
    private enum SummaryType {
        TYPE_TEX,
        TYPE_RTT,
        TYPE_ALL
    }

    static {
        if (enabled) {
            texData = new HashMap();
            rttData = new HashMap();
            Runtime.getRuntime().addShutdownHook(new Thread("RTT printAlloc shutdown hook") { // from class: com.sun.prism.impl.PrismTrace.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    System.out.println("Final Texture resources:" + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
                }
            });
        }
    }

    private static String summary(long count, long size, String label) {
        return String.format("%s=%d@%,dKB", label, Long.valueOf(count), Long.valueOf(size >> 10));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String summary(SummaryType type) {
        switch (type) {
            case TYPE_TEX:
                return summary(texData.size(), texBytes, " tex");
            case TYPE_RTT:
                return summary(rttData.size(), rttBytes, " rtt");
            case TYPE_ALL:
                return summary(texData.size() + rttData.size(), texBytes + rttBytes, " combined");
            default:
                return "ERROR";
        }
    }

    private static long computeSize(int w2, int h2, int bpp) {
        long size = w2;
        return size * h2 * bpp;
    }

    public static void textureCreated(long id, int w2, int h2, long size) {
        if (enabled) {
            texData.put(Long.valueOf(id), Long.valueOf(size));
            texBytes += size;
            System.out.println("Created Texture: id=" + id + ", " + w2 + LanguageTag.PRIVATEUSE + h2 + " pixels, " + size + " bytes," + summary(SummaryType.TYPE_TEX) + summary(SummaryType.TYPE_ALL));
        }
    }

    public static void textureCreated(long id, int w2, int h2, int bytesPerPixel) {
        if (enabled) {
            long size = computeSize(w2, h2, bytesPerPixel);
            texData.put(Long.valueOf(id), Long.valueOf(size));
            texBytes += size;
            System.out.println("Created Texture: id=" + id + ", " + w2 + LanguageTag.PRIVATEUSE + h2 + " pixels, " + size + " bytes," + summary(SummaryType.TYPE_TEX) + summary(SummaryType.TYPE_ALL));
        }
    }

    public static void textureDisposed(long id) {
        if (enabled) {
            Long size = texData.remove(Long.valueOf(id));
            if (size == null) {
                throw new InternalError("Disposing unknown Texture " + id);
            }
            texBytes -= size.longValue();
            System.out.println("Disposed Texture: id=" + id + ", " + ((Object) size) + " bytes" + summary(SummaryType.TYPE_TEX) + summary(SummaryType.TYPE_ALL));
        }
    }

    public static void rttCreated(long id, int w2, int h2, long size) {
        if (enabled) {
            rttData.put(Long.valueOf(id), Long.valueOf(size));
            rttBytes += size;
            System.out.println("Created RTTexture: id=" + id + ", " + w2 + LanguageTag.PRIVATEUSE + h2 + " pixels, " + size + " bytes," + summary(SummaryType.TYPE_RTT) + summary(SummaryType.TYPE_ALL));
        }
    }

    public static void rttCreated(long id, int w2, int h2, int bytesPerPixel) {
        if (enabled) {
            long size = computeSize(w2, h2, bytesPerPixel);
            rttData.put(Long.valueOf(id), Long.valueOf(size));
            rttBytes += size;
            System.out.println("Created RTTexture: id=" + id + ", " + w2 + LanguageTag.PRIVATEUSE + h2 + " pixels, " + size + " bytes," + summary(SummaryType.TYPE_RTT) + summary(SummaryType.TYPE_ALL));
        }
    }

    public static void rttDisposed(long id) {
        if (enabled) {
            Long size = rttData.remove(Long.valueOf(id));
            if (size == null) {
                throw new InternalError("Disposing unknown RTTexture " + id);
            }
            rttBytes -= size.longValue();
            System.out.println("Disposed RTTexture: id=" + id + ", " + ((Object) size) + " bytes" + summary(SummaryType.TYPE_RTT) + summary(SummaryType.TYPE_ALL));
        }
    }

    private PrismTrace() {
    }
}
