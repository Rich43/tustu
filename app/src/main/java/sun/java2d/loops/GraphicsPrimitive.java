package sun.java2d.loops;

import java.awt.AlphaComposite;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import jdk.jfr.Enabled;
import org.icepdf.core.util.PdfOps;
import sun.awt.image.BufImgSurfaceData;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/java2d/loops/GraphicsPrimitive.class */
public abstract class GraphicsPrimitive {
    private String methodSignature;
    private int uniqueID;
    private static int unusedPrimID = 1;
    private SurfaceType sourceType;
    private CompositeType compositeType;
    private SurfaceType destType;
    private long pNativePrim;
    static HashMap traceMap;
    public static int traceflags;
    public static String tracefile;
    public static PrintStream traceout;
    public static final int TRACELOG = 1;
    public static final int TRACETIMESTAMP = 2;
    public static final int TRACECOUNTS = 4;
    private String cachedname;

    /* loaded from: rt.jar:sun/java2d/loops/GraphicsPrimitive$GeneralBinaryOp.class */
    protected interface GeneralBinaryOp {
        void setPrimitives(Blit blit, Blit blit2, GraphicsPrimitive graphicsPrimitive, Blit blit3);

        SurfaceType getSourceType();

        CompositeType getCompositeType();

        SurfaceType getDestType();

        String getSignature();

        int getPrimTypeID();
    }

    /* loaded from: rt.jar:sun/java2d/loops/GraphicsPrimitive$GeneralUnaryOp.class */
    protected interface GeneralUnaryOp {
        void setPrimitives(Blit blit, GraphicsPrimitive graphicsPrimitive, Blit blit2);

        CompositeType getCompositeType();

        SurfaceType getDestType();

        String getSignature();

        int getPrimTypeID();
    }

    public abstract GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2);

    public abstract GraphicsPrimitive traceWrap();

    static {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.trace"));
        if (str != null) {
            boolean z2 = false;
            int i2 = 0;
            StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                if (strNextToken.equalsIgnoreCase("count")) {
                    i2 |= 4;
                } else if (strNextToken.equalsIgnoreCase("log")) {
                    i2 |= 1;
                } else if (strNextToken.equalsIgnoreCase("timestamp")) {
                    i2 |= 2;
                } else if (strNextToken.equalsIgnoreCase("verbose")) {
                    z2 = true;
                } else if (strNextToken.regionMatches(true, 0, "out:", 0, 4)) {
                    tracefile = strNextToken.substring(4);
                } else {
                    if (!strNextToken.equalsIgnoreCase("help")) {
                        System.err.println("unrecognized token: " + strNextToken);
                    }
                    System.err.println("usage: -Dsun.java2d.trace=[log[,timestamp]],[count],[out:<filename>],[help],[verbose]");
                }
            }
            if (z2) {
                System.err.print("GraphicsPrimitive logging ");
                if ((i2 & 1) != 0) {
                    System.err.println(Enabled.NAME);
                    System.err.print("GraphicsPrimitive timetamps ");
                    if ((i2 & 2) != 0) {
                        System.err.println(Enabled.NAME);
                    } else {
                        System.err.println("disabled");
                    }
                } else {
                    System.err.println("[and timestamps] disabled");
                }
                System.err.print("GraphicsPrimitive invocation counts ");
                if ((i2 & 4) != 0) {
                    System.err.println(Enabled.NAME);
                } else {
                    System.err.println("disabled");
                }
                System.err.print("GraphicsPrimitive trace output to ");
                if (tracefile == null) {
                    System.err.println("System.err");
                } else {
                    System.err.println("file '" + tracefile + PdfOps.SINGLE_QUOTE_TOKEN);
                }
            }
            traceflags = i2;
        }
    }

    public static final synchronized int makePrimTypeID() {
        if (unusedPrimID > 255) {
            throw new InternalError("primitive id overflow");
        }
        int i2 = unusedPrimID;
        unusedPrimID = i2 + 1;
        return i2;
    }

    public static final synchronized int makeUniqueID(int i2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (i2 << 24) | (surfaceType2.getUniqueID() << 16) | (compositeType.getUniqueID() << 8) | surfaceType.getUniqueID();
    }

    protected GraphicsPrimitive(String str, int i2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        this.methodSignature = str;
        this.sourceType = surfaceType;
        this.compositeType = compositeType;
        this.destType = surfaceType2;
        if (surfaceType == null || compositeType == null || surfaceType2 == null) {
            this.uniqueID = i2 << 24;
        } else {
            this.uniqueID = makeUniqueID(i2, surfaceType, compositeType, surfaceType2);
        }
    }

    protected GraphicsPrimitive(long j2, String str, int i2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        this.pNativePrim = j2;
        this.methodSignature = str;
        this.sourceType = surfaceType;
        this.compositeType = compositeType;
        this.destType = surfaceType2;
        if (surfaceType == null || compositeType == null || surfaceType2 == null) {
            this.uniqueID = i2 << 24;
        } else {
            this.uniqueID = makeUniqueID(i2, surfaceType, compositeType, surfaceType2);
        }
    }

    public final int getUniqueID() {
        return this.uniqueID;
    }

    public final String getSignature() {
        return this.methodSignature;
    }

    public final int getPrimTypeID() {
        return this.uniqueID >>> 24;
    }

    public final long getNativePrim() {
        return this.pNativePrim;
    }

    public final SurfaceType getSourceType() {
        return this.sourceType;
    }

    public final CompositeType getCompositeType() {
        return this.compositeType;
    }

    public final SurfaceType getDestType() {
        return this.destType;
    }

    public final boolean satisfies(String str, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        if (str != this.methodSignature) {
            return false;
        }
        while (surfaceType != null) {
            if (!surfaceType.equals(this.sourceType)) {
                surfaceType = surfaceType.getSuperType();
            } else {
                while (compositeType != null) {
                    if (!compositeType.equals(this.compositeType)) {
                        compositeType = compositeType.getSuperType();
                    } else {
                        while (surfaceType2 != null) {
                            if (!surfaceType2.equals(this.destType)) {
                                surfaceType2 = surfaceType2.getSuperType();
                            } else {
                                return true;
                            }
                        }
                        return false;
                    }
                }
                return false;
            }
        }
        return false;
    }

    final boolean satisfiesSameAs(GraphicsPrimitive graphicsPrimitive) {
        return this.methodSignature == graphicsPrimitive.methodSignature && this.sourceType.equals(graphicsPrimitive.sourceType) && this.compositeType.equals(graphicsPrimitive.compositeType) && this.destType.equals(graphicsPrimitive.destType);
    }

    public static boolean tracingEnabled() {
        return traceflags != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PrintStream getTraceOutputFile() {
        FileOutputStream fileOutputStream;
        if (traceout == null) {
            if (tracefile != null && (fileOutputStream = (FileOutputStream) AccessController.doPrivileged(new PrivilegedAction<FileOutputStream>() { // from class: sun.java2d.loops.GraphicsPrimitive.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public FileOutputStream run2() {
                    try {
                        return new FileOutputStream(GraphicsPrimitive.tracefile);
                    } catch (FileNotFoundException e2) {
                        return null;
                    }
                }
            })) != null) {
                traceout = new PrintStream(fileOutputStream);
            } else {
                traceout = System.err;
            }
        }
        return traceout;
    }

    /* loaded from: rt.jar:sun/java2d/loops/GraphicsPrimitive$TraceReporter.class */
    public static class TraceReporter extends Thread {
        public static void setShutdownHook() {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.java2d.loops.GraphicsPrimitive.TraceReporter.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    TraceReporter traceReporter = new TraceReporter();
                    traceReporter.setContextClassLoader(null);
                    Runtime.getRuntime().addShutdownHook(traceReporter);
                    return null;
                }
            });
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            PrintStream traceOutputFile = GraphicsPrimitive.getTraceOutputFile();
            long j2 = 0;
            int i2 = 0;
            for (Map.Entry entry : GraphicsPrimitive.traceMap.entrySet()) {
                Object key = entry.getKey();
                int[] iArr = (int[]) entry.getValue();
                if (iArr[0] == 1) {
                    traceOutputFile.print("1 call to ");
                } else {
                    traceOutputFile.print(iArr[0] + " calls to ");
                }
                traceOutputFile.println(key);
                i2++;
                j2 += iArr[0];
            }
            if (i2 == 0) {
                traceOutputFile.println("No graphics primitives executed");
            } else if (i2 > 1) {
                traceOutputFile.println(j2 + " total calls to " + i2 + " different primitives");
            }
        }
    }

    public static synchronized void tracePrimitive(Object obj) {
        if ((traceflags & 4) != 0) {
            if (traceMap == null) {
                traceMap = new HashMap();
                TraceReporter.setShutdownHook();
            }
            Object obj2 = traceMap.get(obj);
            if (obj2 == null) {
                obj2 = new int[1];
                traceMap.put(obj, obj2);
            }
            int[] iArr = (int[]) obj2;
            iArr[0] = iArr[0] + 1;
        }
        if ((traceflags & 1) != 0) {
            PrintStream traceOutputFile = getTraceOutputFile();
            if ((traceflags & 2) != 0) {
                traceOutputFile.print(System.currentTimeMillis() + ": ");
            }
            traceOutputFile.println(obj);
        }
    }

    protected void setupGeneralBinaryOp(GeneralBinaryOp generalBinaryOp) {
        Blit blitCreateConverter;
        Blit blitCreateConverter2;
        int primTypeID = generalBinaryOp.getPrimTypeID();
        String signature = generalBinaryOp.getSignature();
        SurfaceType sourceType = generalBinaryOp.getSourceType();
        CompositeType compositeType = generalBinaryOp.getCompositeType();
        SurfaceType destType = generalBinaryOp.getDestType();
        Blit blitCreateConverter3 = createConverter(sourceType, SurfaceType.IntArgb);
        GraphicsPrimitive graphicsPrimitiveLocatePrim = GraphicsPrimitiveMgr.locatePrim(primTypeID, SurfaceType.IntArgb, compositeType, destType);
        if (graphicsPrimitiveLocatePrim != null) {
            blitCreateConverter = null;
            blitCreateConverter2 = null;
        } else {
            graphicsPrimitiveLocatePrim = getGeneralOp(primTypeID, compositeType);
            if (graphicsPrimitiveLocatePrim == null) {
                throw new InternalError("Cannot construct general op for " + signature + " " + ((Object) compositeType));
            }
            blitCreateConverter = createConverter(destType, SurfaceType.IntArgb);
            blitCreateConverter2 = createConverter(SurfaceType.IntArgb, destType);
        }
        generalBinaryOp.setPrimitives(blitCreateConverter3, blitCreateConverter, graphicsPrimitiveLocatePrim, blitCreateConverter2);
    }

    protected void setupGeneralUnaryOp(GeneralUnaryOp generalUnaryOp) {
        int primTypeID = generalUnaryOp.getPrimTypeID();
        generalUnaryOp.getSignature();
        CompositeType compositeType = generalUnaryOp.getCompositeType();
        SurfaceType destType = generalUnaryOp.getDestType();
        Blit blitCreateConverter = createConverter(destType, SurfaceType.IntArgb);
        GraphicsPrimitive generalOp = getGeneralOp(primTypeID, compositeType);
        Blit blitCreateConverter2 = createConverter(SurfaceType.IntArgb, destType);
        if (blitCreateConverter == null || generalOp == null || blitCreateConverter2 == null) {
            throw new InternalError("Cannot construct binary op for " + ((Object) compositeType) + " " + ((Object) destType));
        }
        generalUnaryOp.setPrimitives(blitCreateConverter, generalOp, blitCreateConverter2);
    }

    protected static Blit createConverter(SurfaceType surfaceType, SurfaceType surfaceType2) {
        if (surfaceType.equals(surfaceType2)) {
            return null;
        }
        Blit fromCache = Blit.getFromCache(surfaceType, CompositeType.SrcNoEa, surfaceType2);
        if (fromCache == null) {
            throw new InternalError("Cannot construct converter for " + ((Object) surfaceType) + "=>" + ((Object) surfaceType2));
        }
        return fromCache;
    }

    protected static SurfaceData convertFrom(Blit blit, SurfaceData surfaceData, int i2, int i3, int i4, int i5, SurfaceData surfaceData2) {
        return convertFrom(blit, surfaceData, i2, i3, i4, i5, surfaceData2, 2);
    }

    protected static SurfaceData convertFrom(Blit blit, SurfaceData surfaceData, int i2, int i3, int i4, int i5, SurfaceData surfaceData2, int i6) {
        if (surfaceData2 != null) {
            Rectangle bounds = surfaceData2.getBounds();
            if (i4 > bounds.width || i5 > bounds.height) {
                surfaceData2 = null;
            }
        }
        if (surfaceData2 == null) {
            surfaceData2 = BufImgSurfaceData.createData(new BufferedImage(i4, i5, i6));
        }
        blit.Blit(surfaceData, surfaceData2, AlphaComposite.Src, null, i2, i3, 0, 0, i4, i5);
        return surfaceData2;
    }

    protected static void convertTo(Blit blit, SurfaceData surfaceData, SurfaceData surfaceData2, Region region, int i2, int i3, int i4, int i5) {
        if (blit != null) {
            blit.Blit(surfaceData, surfaceData2, AlphaComposite.Src, region, 0, 0, i2, i3, i4, i5);
        }
    }

    protected static GraphicsPrimitive getGeneralOp(int i2, CompositeType compositeType) {
        return GraphicsPrimitiveMgr.locatePrim(i2, SurfaceType.IntArgb, compositeType, SurfaceType.IntArgb);
    }

    public static String simplename(Field[] fieldArr, Object obj) {
        for (Field field : fieldArr) {
            if (obj != field.get(null)) {
                continue;
            } else {
                return field.getName();
            }
        }
        return PdfOps.DOUBLE_QUOTE__TOKEN + obj.toString() + PdfOps.DOUBLE_QUOTE__TOKEN;
    }

    public static String simplename(SurfaceType surfaceType) {
        return simplename(SurfaceType.class.getDeclaredFields(), surfaceType);
    }

    public static String simplename(CompositeType compositeType) {
        return simplename(CompositeType.class.getDeclaredFields(), compositeType);
    }

    public String toString() {
        if (this.cachedname == null) {
            String strSubstring = this.methodSignature;
            int iIndexOf = strSubstring.indexOf(40);
            if (iIndexOf >= 0) {
                strSubstring = strSubstring.substring(0, iIndexOf);
            }
            this.cachedname = getClass().getName() + "::" + strSubstring + "(" + simplename(this.sourceType) + ", " + simplename(this.compositeType) + ", " + simplename(this.destType) + ")";
        }
        return this.cachedname;
    }
}
