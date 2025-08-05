package java.awt;

import java.beans.ConstructorProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import sun.awt.AWTAccessor;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;
import sun.security.action.GetPropertyAction;
import sun.util.logging.PlatformLogger;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    */
/* loaded from: rt.jar:java/awt/Cursor.class */
public class Cursor implements Serializable {
    public static final int DEFAULT_CURSOR = 0;
    public static final int CROSSHAIR_CURSOR = 1;
    public static final int TEXT_CURSOR = 2;
    public static final int WAIT_CURSOR = 3;
    public static final int SW_RESIZE_CURSOR = 4;
    public static final int SE_RESIZE_CURSOR = 5;
    public static final int NW_RESIZE_CURSOR = 6;
    public static final int NE_RESIZE_CURSOR = 7;
    public static final int N_RESIZE_CURSOR = 8;
    public static final int S_RESIZE_CURSOR = 9;
    public static final int W_RESIZE_CURSOR = 10;
    public static final int E_RESIZE_CURSOR = 11;
    public static final int HAND_CURSOR = 12;
    public static final int MOVE_CURSOR = 13;
    int type;
    public static final int CUSTOM_CURSOR = -1;
    private static final String CursorDotPrefix = "Cursor.";
    private static final String DotFileSuffix = ".File";
    private static final String DotHotspotSuffix = ".HotSpot";
    private static final String DotNameSuffix = ".Name";
    private static final long serialVersionUID = 8028237497568985504L;
    private transient long pData;
    private transient Object anchor = new Object();
    transient CursorDisposer disposer;
    protected String name;

    @Deprecated
    protected static Cursor[] predefined = new Cursor[14];
    private static final Cursor[] predefinedPrivate = new Cursor[14];
    static final String[][] cursorProperties = {new String[]{"AWT.DefaultCursor", "Default Cursor"}, new String[]{"AWT.CrosshairCursor", "Crosshair Cursor"}, new String[]{"AWT.TextCursor", "Text Cursor"}, new String[]{"AWT.WaitCursor", "Wait Cursor"}, new String[]{"AWT.SWResizeCursor", "Southwest Resize Cursor"}, new String[]{"AWT.SEResizeCursor", "Southeast Resize Cursor"}, new String[]{"AWT.NWResizeCursor", "Northwest Resize Cursor"}, new String[]{"AWT.NEResizeCursor", "Northeast Resize Cursor"}, new String[]{"AWT.NResizeCursor", "North Resize Cursor"}, new String[]{"AWT.SResizeCursor", "South Resize Cursor"}, new String[]{"AWT.WResizeCursor", "West Resize Cursor"}, new String[]{"AWT.EResizeCursor", "East Resize Cursor"}, new String[]{"AWT.HandCursor", "Hand Cursor"}, new String[]{"AWT.MoveCursor", "Move Cursor"}};
    private static final Hashtable<String, Cursor> systemCustomCursors = new Hashtable<>(1);
    private static final String systemCustomCursorDirPrefix = initCursorDir();
    private static final String systemCustomCursorPropertiesFile = systemCustomCursorDirPrefix + "cursors.properties";
    private static Properties systemCustomCursorProperties = null;
    private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.Cursor");

    private static native void initIDs();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void finalizeImpl(long j2);

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$002(java.awt.Cursor r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.pData = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.Cursor.access$002(java.awt.Cursor, long):long");
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.String[], java.lang.String[][]] */
    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setCursorAccessor(new AWTAccessor.CursorAccessor() { // from class: java.awt.Cursor.1
            @Override // sun.awt.AWTAccessor.CursorAccessor
            public long getPData(Cursor cursor) {
                return cursor.pData;
            }

            /* JADX WARN: Failed to check method for inline after forced processjava.awt.Cursor.access$002(java.awt.Cursor, long):long */
            @Override // sun.awt.AWTAccessor.CursorAccessor
            public void setPData(Cursor cursor, long j2) {
                Cursor.access$002(cursor, j2);
            }

            @Override // sun.awt.AWTAccessor.CursorAccessor
            public int getType(Cursor cursor) {
                return cursor.type;
            }
        });
    }

    private static String initCursorDir() {
        return ((String) AccessController.doPrivileged(new GetPropertyAction("java.home"))) + File.separator + "lib" + File.separator + "images" + File.separator + "cursors" + File.separator;
    }

    /* loaded from: rt.jar:java/awt/Cursor$CursorDisposer.class */
    static class CursorDisposer implements DisposerRecord {
        volatile long pData;

        public CursorDisposer(long j2) {
            this.pData = j2;
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            if (this.pData != 0) {
                Cursor.finalizeImpl(this.pData);
            }
        }
    }

    private void setPData(long j2) {
        this.pData = j2;
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        if (this.disposer == null) {
            this.disposer = new CursorDisposer(j2);
            if (this.anchor == null) {
                this.anchor = new Object();
            }
            Disposer.addRecord(this.anchor, this.disposer);
            return;
        }
        this.disposer.pData = j2;
    }

    public static Cursor getPredefinedCursor(int i2) {
        if (i2 < 0 || i2 > 13) {
            throw new IllegalArgumentException("illegal cursor type");
        }
        Cursor cursor = predefinedPrivate[i2];
        if (cursor == null) {
            Cursor[] cursorArr = predefinedPrivate;
            Cursor cursor2 = new Cursor(i2);
            cursor = cursor2;
            cursorArr[i2] = cursor2;
        }
        if (predefined[i2] == null) {
            predefined[i2] = cursor;
        }
        return cursor;
    }

    public static Cursor getSystemCustomCursor(String str) throws AWTException, HeadlessException {
        GraphicsEnvironment.checkHeadless();
        Cursor cursor = systemCustomCursors.get(str);
        if (cursor == null) {
            synchronized (systemCustomCursors) {
                if (systemCustomCursorProperties == null) {
                    loadSystemCustomCursorProperties();
                }
            }
            String str2 = CursorDotPrefix + str;
            String str3 = str2 + DotFileSuffix;
            if (!systemCustomCursorProperties.containsKey(str3)) {
                if (log.isLoggable(PlatformLogger.Level.FINER)) {
                    log.finer("Cursor.getSystemCustomCursor(" + str + ") returned null");
                    return null;
                }
                return null;
            }
            final String property = systemCustomCursorProperties.getProperty(str3);
            String property2 = systemCustomCursorProperties.getProperty(str2 + DotNameSuffix);
            if (property2 == null) {
                property2 = str;
            }
            String property3 = systemCustomCursorProperties.getProperty(str2 + DotHotspotSuffix);
            if (property3 == null) {
                throw new AWTException("no hotspot property defined for cursor: " + str);
            }
            StringTokenizer stringTokenizer = new StringTokenizer(property3, ",");
            if (stringTokenizer.countTokens() != 2) {
                throw new AWTException("failed to parse hotspot property for cursor: " + str);
            }
            try {
                final int i2 = Integer.parseInt(stringTokenizer.nextToken());
                final int i3 = Integer.parseInt(stringTokenizer.nextToken());
                try {
                    final String str4 = property2;
                    cursor = (Cursor) AccessController.doPrivileged(new PrivilegedExceptionAction<Cursor>() { // from class: java.awt.Cursor.2
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Cursor run() throws Exception {
                            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                            return defaultToolkit.createCustomCursor(defaultToolkit.getImage(Cursor.systemCustomCursorDirPrefix + property), new Point(i2, i3), str4);
                        }
                    });
                    if (cursor == null) {
                        if (log.isLoggable(PlatformLogger.Level.FINER)) {
                            log.finer("Cursor.getSystemCustomCursor(" + str + ") returned null");
                        }
                    } else {
                        systemCustomCursors.put(str, cursor);
                    }
                } catch (Exception e2) {
                    throw new AWTException("Exception: " + ((Object) e2.getClass()) + " " + e2.getMessage() + " occurred while creating cursor " + str);
                }
            } catch (NumberFormatException e3) {
                throw new AWTException("failed to parse hotspot property for cursor: " + str);
            }
        }
        return cursor;
    }

    public static Cursor getDefaultCursor() {
        return getPredefinedCursor(0);
    }

    @ConstructorProperties({"type"})
    public Cursor(int i2) {
        this.type = 0;
        if (i2 < 0 || i2 > 13) {
            throw new IllegalArgumentException("illegal cursor type");
        }
        this.type = i2;
        this.name = Toolkit.getProperty(cursorProperties[i2][0], cursorProperties[i2][1]);
    }

    protected Cursor(String str) {
        this.type = 0;
        this.type = -1;
        this.name = str;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getClass().getName() + "[" + getName() + "]";
    }

    private static void loadSystemCustomCursorProperties() throws AWTException {
        synchronized (systemCustomCursors) {
            systemCustomCursorProperties = new Properties();
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: java.awt.Cursor.3
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() throws Exception {
                        FileInputStream fileInputStream = null;
                        try {
                            fileInputStream = new FileInputStream(Cursor.systemCustomCursorPropertiesFile);
                            Cursor.systemCustomCursorProperties.load(fileInputStream);
                            if (fileInputStream != null) {
                                fileInputStream.close();
                                return null;
                            }
                            return null;
                        } catch (Throwable th) {
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                            throw th;
                        }
                    }
                });
            } catch (Exception e2) {
                systemCustomCursorProperties = null;
                throw new AWTException("Exception: " + ((Object) e2.getClass()) + " " + e2.getMessage() + " occurred while loading: " + systemCustomCursorPropertiesFile);
            }
        }
    }
}
