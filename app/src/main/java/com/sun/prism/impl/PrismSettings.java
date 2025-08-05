package com.sun.prism.impl;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.util.Utils;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/prism/impl/PrismSettings.class */
public final class PrismSettings {
    public static final boolean verbose;
    public static final boolean debug;
    public static final boolean trace;
    public static final boolean printAllocs;
    public static final boolean isVsyncEnabled;
    public static final boolean dirtyOptsEnabled;
    public static final boolean occlusionCullingEnabled;
    public static final boolean scrollCacheOpt;
    public static final boolean threadCheck;
    public static final boolean cacheSimpleShapes;
    public static final boolean cacheComplexShapes;
    public static final boolean useNewImageLoader;
    public static final List<String> tryOrder;
    public static final int prismStatFrequency;
    public static final boolean doNativePisces;
    public static final String refType;
    public static final boolean forceRepaint;
    public static final boolean noFallback;
    public static final boolean showDirtyRegions;
    public static final boolean showOverdraw;
    public static final boolean printRenderGraph;
    public static final int minRTTSize;
    public static final int dirtyRegionCount;
    public static final boolean disableBadDriverWarning;
    public static final boolean forceGPU;
    public static final int maxTextureSize;
    public static final int primTextureSize;
    public static final boolean disableRegionCaching;
    public static final boolean forcePow2;
    public static final boolean noClampToZero;
    public static final boolean disableD3D9Ex;
    public static final boolean allowHiDPIScaling;
    public static final long maxVram;
    public static final long targetVram;
    public static final boolean poolStats;
    public static final boolean poolDebug;
    public static final boolean disableEffects;
    public static final int glyphCacheWidth;
    public static final int glyphCacheHeight;
    public static final String perfLog;
    public static final boolean perfLogExitFlush;
    public static final boolean perfLogFirstPaintFlush;
    public static final boolean perfLogFirstPaintExit;
    public static final boolean superShader;
    public static final boolean skipMeshNormalComputation;
    public static final boolean forceUploadingPainter;
    public static final boolean forceAlphaTestShader;
    public static final boolean forceNonAntialiasedShape;

    private PrismSettings() {
    }

    private static void printBooleanOption(boolean opt, String trueStr) {
        if (opt) {
            System.out.println(trueStr);
            return;
        }
        System.out.print("Not ");
        System.out.print(Character.toLowerCase(trueStr.charAt(0)));
        System.out.println(trueStr.substring(1));
    }

    static {
        String[] tryOrderArr;
        Properties systemProperties = (Properties) AccessController.doPrivileged(() -> {
            return System.getProperties();
        });
        isVsyncEnabled = getBoolean(systemProperties, "prism.vsync", true) && !getBoolean(systemProperties, "javafx.animation.fullspeed", false);
        dirtyOptsEnabled = getBoolean(systemProperties, "prism.dirtyopts", true);
        occlusionCullingEnabled = dirtyOptsEnabled && getBoolean(systemProperties, "prism.occlusion.culling", true);
        dirtyRegionCount = Utils.clamp(0, getInt(systemProperties, "prism.dirtyregioncount", 6, null), 15);
        scrollCacheOpt = getBoolean(systemProperties, "prism.scrollcacheopt", false);
        threadCheck = getBoolean(systemProperties, "prism.threadcheck", false);
        showDirtyRegions = getBoolean(systemProperties, "prism.showdirty", false);
        showOverdraw = getBoolean(systemProperties, "prism.showoverdraw", false);
        printRenderGraph = getBoolean(systemProperties, "prism.printrendergraph", false);
        forceRepaint = getBoolean(systemProperties, "prism.forcerepaint", false);
        noFallback = getBoolean(systemProperties, "prism.noFallback", false);
        String cache = systemProperties.getProperty("prism.cacheshapes", "complex");
        if ("all".equals(cache) || "true".equals(cache)) {
            cacheSimpleShapes = true;
            cacheComplexShapes = true;
        } else if ("complex".equals(cache)) {
            cacheSimpleShapes = false;
            cacheComplexShapes = true;
        } else {
            cacheSimpleShapes = false;
            cacheComplexShapes = false;
        }
        useNewImageLoader = getBoolean(systemProperties, "prism.newiio", true);
        verbose = getBoolean(systemProperties, "prism.verbose", false);
        prismStatFrequency = getInt(systemProperties, "prism.printStats", 0, 1, "Try -Dprism.printStats=<true or number>");
        debug = getBoolean(systemProperties, "prism.debug", false);
        trace = getBoolean(systemProperties, "prism.trace", false);
        printAllocs = getBoolean(systemProperties, "prism.printallocs", false);
        disableBadDriverWarning = getBoolean(systemProperties, "prism.disableBadDriverWarning", false);
        forceGPU = getBoolean(systemProperties, "prism.forceGPU", false);
        skipMeshNormalComputation = getBoolean(systemProperties, "prism.experimental.skipMeshNormalComputation", false);
        String order = systemProperties.getProperty("prism.order");
        if (order != null) {
            tryOrderArr = split(order, ",");
        } else if (PlatformUtil.isWindows()) {
            tryOrderArr = new String[]{"d3d", "sw"};
        } else if (PlatformUtil.isMac()) {
            tryOrderArr = new String[]{"es2", "sw"};
        } else if (PlatformUtil.isIOS() || PlatformUtil.isAndroid()) {
            tryOrderArr = new String[]{"es2"};
        } else if (PlatformUtil.isLinux()) {
            tryOrderArr = new String[]{"es2", "sw"};
        } else {
            tryOrderArr = new String[]{"sw"};
        }
        tryOrder = Collections.unmodifiableList(Arrays.asList(tryOrderArr));
        String npprop = systemProperties.getProperty("prism.nativepisces");
        if (npprop == null) {
            doNativePisces = PlatformUtil.isEmbedded() || !PlatformUtil.isLinux();
        } else {
            doNativePisces = Boolean.parseBoolean(npprop);
        }
        String primtex = systemProperties.getProperty("prism.primtextures");
        if (primtex == null) {
            primTextureSize = PlatformUtil.isEmbedded() ? -1 : 0;
        } else if (primtex.equals("true")) {
            primTextureSize = -1;
        } else if (primtex.equals("false")) {
            primTextureSize = 0;
        } else {
            primTextureSize = parseInt(primtex, 0, "Try -Dprism.primtextures=[true|false|<number>]");
        }
        refType = systemProperties.getProperty("prism.reftype");
        forcePow2 = getBoolean(systemProperties, "prism.forcepowerof2", false);
        noClampToZero = getBoolean(systemProperties, "prism.noclamptozero", false);
        allowHiDPIScaling = getBoolean(systemProperties, "prism.allowhidpi", true);
        maxVram = getLong(systemProperties, "prism.maxvram", 536870912L, "Try -Dprism.maxvram=<long>[kKmMgG]");
        targetVram = getLong(systemProperties, "prism.targetvram", maxVram / 8, maxVram, "Try -Dprism.targetvram=<long>[kKmMgG]|<double(0,100)>%");
        poolStats = getBoolean(systemProperties, "prism.poolstats", false);
        poolDebug = getBoolean(systemProperties, "prism.pooldebug", false);
        if (verbose) {
            System.out.print("Prism pipeline init order: ");
            for (String s2 : tryOrder) {
                System.out.print(s2 + " ");
            }
            System.out.println("");
            String piscestype = doNativePisces ? "native" : "java";
            System.out.println("Using " + piscestype + "-based Pisces rasterizer");
            printBooleanOption(dirtyOptsEnabled, "Using dirty region optimizations");
            if (primTextureSize == 0) {
                System.out.println("Not using texture mask for primitives");
            } else if (primTextureSize < 0) {
                System.out.println("Using system sized mask for primitives");
            } else {
                System.out.println("Using " + primTextureSize + " sized mask for primitives");
            }
            printBooleanOption(forcePow2, "Forcing power of 2 sizes for textures");
            printBooleanOption(!noClampToZero, "Using hardware CLAMP_TO_ZERO mode");
            printBooleanOption(allowHiDPIScaling, "Opting in for HiDPI pixel scaling");
        }
        int size = getInt(systemProperties, "prism.maxTextureSize", 4096, "Try -Dprism.maxTextureSize=<number>");
        if (size <= 0) {
            size = Integer.MAX_VALUE;
        }
        maxTextureSize = size;
        minRTTSize = getInt(systemProperties, "prism.minrttsize", PlatformUtil.isEmbedded() ? 16 : 0, "Try -Dprism.minrttsize=<number>");
        disableRegionCaching = getBoolean(systemProperties, "prism.disableRegionCaching", false);
        disableD3D9Ex = getBoolean(systemProperties, "prism.disableD3D9Ex", false);
        disableEffects = getBoolean(systemProperties, "prism.disableEffects", false);
        glyphCacheWidth = getInt(systemProperties, "prism.glyphCacheWidth", 1024, "Try -Dprism.glyphCacheWidth=<number>");
        glyphCacheHeight = getInt(systemProperties, "prism.glyphCacheHeight", 1024, "Try -Dprism.glyphCacheHeight=<number>");
        perfLog = systemProperties.getProperty("sun.perflog");
        perfLogExitFlush = getBoolean(systemProperties, "sun.perflog.fx.exitflush", false, true);
        perfLogFirstPaintFlush = getBoolean(systemProperties, "sun.perflog.fx.firstpaintflush", false, true);
        perfLogFirstPaintExit = getBoolean(systemProperties, "sun.perflog.fx.firstpaintexit", false, true);
        superShader = getBoolean(systemProperties, "prism.supershader", true);
        forceUploadingPainter = getBoolean(systemProperties, "prism.forceUploadingPainter", false);
        forceAlphaTestShader = getBoolean(systemProperties, "prism.forceAlphaTestShader", false);
        forceNonAntialiasedShape = getBoolean(systemProperties, "prism.forceNonAntialiasedShape", false);
    }

    private static int parseInt(String s2, int dflt, int trueDflt, String errMsg) {
        return "true".equalsIgnoreCase(s2) ? trueDflt : parseInt(s2, dflt, errMsg);
    }

    private static int parseInt(String s2, int dflt, String errMsg) {
        if (s2 != null) {
            try {
                return Integer.parseInt(s2);
            } catch (Exception e2) {
                if (errMsg != null) {
                    System.err.println(errMsg);
                }
            }
        }
        return dflt;
    }

    private static long parseLong(String s2, long dflt, long rel, String errMsg) {
        if (s2 != null && s2.length() > 0) {
            long mult = 1;
            if (s2.endsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                if (rel > 0) {
                    try {
                        double percent = Double.parseDouble(s2.substring(0, s2.length() - 1));
                        if (percent >= 0.0d && percent <= 100.0d) {
                            return Math.round((rel * percent) / 100.0d);
                        }
                    } catch (Exception e2) {
                    }
                }
                if (errMsg != null) {
                    System.err.println(errMsg);
                }
                return dflt;
            }
            if (s2.endsWith(PdfOps.k_TOKEN) || s2.endsWith(PdfOps.K_TOKEN)) {
                mult = 1024;
            } else if (s2.endsWith(PdfOps.m_TOKEN) || s2.endsWith(PdfOps.M_TOKEN)) {
                mult = 1048576;
            } else if (s2.endsWith(PdfOps.g_TOKEN) || s2.endsWith("G")) {
                mult = 1073741824;
            }
            if (mult > 1) {
                s2 = s2.substring(0, s2.length() - 1);
            }
            try {
                return Long.parseLong(s2) * mult;
            } catch (Exception e3) {
                if (errMsg != null) {
                    System.err.println(errMsg);
                }
            }
        }
        return dflt;
    }

    private static String[] split(String str, String delim) {
        StringTokenizer st = new StringTokenizer(str, delim);
        String[] ret = new String[st.countTokens()];
        int i2 = 0;
        while (st.hasMoreTokens()) {
            int i3 = i2;
            i2++;
            ret[i3] = st.nextToken();
        }
        return ret;
    }

    private static boolean getBoolean(Properties properties, String key, boolean dflt) {
        String strval = properties.getProperty(key);
        return strval != null ? Boolean.parseBoolean(strval) : dflt;
    }

    private static boolean getBoolean(Properties properties, String key, boolean dflt, boolean dfltIfDefined) {
        String strval = properties.getProperty(key);
        return (strval == null || strval.length() != 0) ? strval != null ? Boolean.parseBoolean(strval) : dflt : dfltIfDefined;
    }

    private static int getInt(Properties properties, String key, int dflt, int trueDflt, String errMsg) {
        return parseInt(properties.getProperty(key), dflt, trueDflt, errMsg);
    }

    private static int getInt(Properties properties, String key, int dflt, String errMsg) {
        return parseInt(properties.getProperty(key), dflt, errMsg);
    }

    private static long getLong(Properties properties, String key, long dflt, String errMsg) {
        return parseLong(properties.getProperty(key), dflt, 0L, errMsg);
    }

    private static long getLong(Properties properties, String key, long dflt, long rel, String errMsg) {
        return parseLong(properties.getProperty(key), dflt, rel, errMsg);
    }
}
