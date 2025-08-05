package sun.awt.windows;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.peer.ComponentPeer;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FilePermission;
import java.net.URI;
import java.net.URISyntaxException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;
import sun.awt.Win32FontManager;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;
import sun.java2d.DisposerTarget;
import sun.print.PeekGraphics;
import sun.print.PeekMetrics;
import sun.print.PrintServiceLookupProvider;
import sun.print.RasterPrinterJob;
import sun.print.SunAlternateMedia;
import sun.print.SunPageSelection;
import sun.print.Win32MediaTray;
import sun.print.Win32PrintService;
import sun.security.util.SecurityConstants;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:sun/awt/windows/WPrinterJob.class */
public final class WPrinterJob extends RasterPrinterJob implements DisposerTarget {
    protected static final long PS_ENDCAP_ROUND = 0;
    protected static final long PS_ENDCAP_SQUARE = 256;
    protected static final long PS_ENDCAP_FLAT = 512;
    protected static final long PS_JOIN_ROUND = 0;
    protected static final long PS_JOIN_BEVEL = 4096;
    protected static final long PS_JOIN_MITER = 8192;
    protected static final int POLYFILL_ALTERNATE = 1;
    protected static final int POLYFILL_WINDING = 2;
    private static final int MAX_WCOLOR = 255;
    private static final int SET_DUP_VERTICAL = 16;
    private static final int SET_DUP_HORIZONTAL = 32;
    private static final int SET_RES_HIGH = 64;
    private static final int SET_RES_LOW = 128;
    private static final int SET_COLOR = 512;
    private static final int SET_ORIENTATION = 16384;
    private static final int SET_COLLATED = 32768;
    private static final int PD_COLLATE = 16;
    private static final int PD_PRINTTOFILE = 32;
    private static final int DM_ORIENTATION = 1;
    private static final int DM_PAPERSIZE = 2;
    private static final int DM_COPIES = 256;
    private static final int DM_DEFAULTSOURCE = 512;
    private static final int DM_PRINTQUALITY = 1024;
    private static final int DM_COLOR = 2048;
    private static final int DM_DUPLEX = 4096;
    private static final int DM_YRESOLUTION = 8192;
    private static final int DM_COLLATE = 32768;
    private static final short DMCOLLATE_FALSE = 0;
    private static final short DMCOLLATE_TRUE = 1;
    private static final short DMORIENT_PORTRAIT = 1;
    private static final short DMORIENT_LANDSCAPE = 2;
    private static final short DMCOLOR_MONOCHROME = 1;
    private static final short DMCOLOR_COLOR = 2;
    private static final short DMRES_DRAFT = -1;
    private static final short DMRES_LOW = -2;
    private static final short DMRES_MEDIUM = -3;
    private static final short DMRES_HIGH = -4;
    private static final short DMDUP_SIMPLEX = 1;
    private static final short DMDUP_VERTICAL = 2;
    private static final short DMDUP_HORIZONTAL = 3;
    private static final int MAX_UNKNOWN_PAGES = 9999;
    private HandleRecord handleRecord;
    private int mPrintPaperSize;
    private int mPrintXRes;
    private int mPrintYRes;
    private int mPrintPhysX;
    private int mPrintPhysY;
    private int mPrintWidth;
    private int mPrintHeight;
    private int mPageWidth;
    private int mPageHeight;
    private int mAttSides;
    private int mAttChromaticity;
    private int mAttXRes;
    private int mAttYRes;
    private int mAttQuality;
    private int mAttCollate;
    private int mAttCopies;
    private int mAttMediaSizeName;
    private int mAttMediaTray;
    private Color mLastColor;
    private Color mLastTextColor;
    private String mLastFontFamily;
    private float mLastFontSize;
    private int mLastFontStyle;
    private int mLastRotation;
    private float mLastAwScale;
    private PrinterJob pjob;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean driverDoesMultipleCopies = false;
    private boolean driverDoesCollation = false;
    private boolean userRequestedCollation = false;
    private boolean noDefaultPrinter = false;
    private String mDestination = null;
    private ComponentPeer dialogOwnerPeer = null;
    private Object disposerReferent = new Object();
    private String lastNativeService = null;
    private boolean defaultCopies = true;

    private native void setNativePrintService(String str) throws PrinterException;

    private native String getNativePrintService();

    private native void getDefaultPage(PageFormat pageFormat);

    @Override // sun.print.RasterPrinterJob
    protected native void validatePaper(Paper paper, Paper paper2);

    private native void setNativeCopies(int i2);

    private native boolean jobSetup(Pageable pageable, boolean z2);

    @Override // sun.print.RasterPrinterJob
    protected native void initPrinter();

    private native boolean _startDoc(String str, String str2) throws PrinterException;

    @Override // sun.print.RasterPrinterJob
    protected native void endDoc();

    @Override // sun.print.RasterPrinterJob
    protected native void abortDoc();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void deleteDC(long j2, long j3, long j4);

    protected native void deviceStartPage(PageFormat pageFormat, Printable printable, int i2, boolean z2);

    protected native void deviceEndPage(PageFormat pageFormat, Printable printable, int i2);

    @Override // sun.print.RasterPrinterJob
    protected native void printBand(byte[] bArr, int i2, int i3, int i4, int i5);

    protected native void beginPath(long j2);

    protected native void endPath(long j2);

    protected native void closeFigure(long j2);

    protected native void fillPath(long j2);

    protected native void moveTo(long j2, float f2, float f3);

    protected native void lineTo(long j2, float f2, float f3);

    protected native void polyBezierTo(long j2, float f2, float f3, float f4, float f5, float f6, float f7);

    protected native void setPolyFillMode(long j2, int i2);

    protected native void selectSolidBrush(long j2, int i2, int i3, int i4);

    protected native int getPenX(long j2);

    protected native int getPenY(long j2);

    protected native void selectClipPath(long j2);

    protected native void frameRect(long j2, float f2, float f3, float f4, float f5);

    protected native void fillRect(long j2, float f2, float f3, float f4, float f5, int i2, int i3, int i4);

    protected native void selectPen(long j2, float f2, int i2, int i3, int i4);

    protected native boolean selectStylePen(long j2, long j3, long j4, float f2, int i2, int i3, int i4);

    protected native boolean setFont(long j2, String str, float f2, boolean z2, boolean z3, int i2, float f3);

    protected native void setTextColor(long j2, int i2, int i3, int i4);

    protected native void textOut(long j2, String str, int i2, boolean z2, float f2, float f3, float[] fArr);

    private native int getGDIAdvance(long j2, String str);

    private native void drawDIBImage(long j2, byte[] bArr, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int i2, byte[] bArr2);

    private native boolean showDocProperties(long j2, PrintRequestAttributeSet printRequestAttributeSet, int i2, short s2, short s3, short s4, short s5, short s6, short s7, short s8, short s9, short s10);

    private static native void initIDs();

    static {
        $assertionsDisabled = !WPrinterJob.class.desiredAssertionStatus();
        Toolkit.getDefaultToolkit();
        initIDs();
        Win32FontManager.registerJREFontsForPrinting();
    }

    /* loaded from: rt.jar:sun/awt/windows/WPrinterJob$HandleRecord.class */
    static class HandleRecord implements DisposerRecord {
        private long mPrintDC;
        private long mPrintHDevMode;
        private long mPrintHDevNames;

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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$102(sun.awt.windows.WPrinterJob.HandleRecord r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.mPrintDC = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.awt.windows.WPrinterJob.HandleRecord.access$102(sun.awt.windows.WPrinterJob$HandleRecord, long):long");
        }

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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$202(sun.awt.windows.WPrinterJob.HandleRecord r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.mPrintHDevMode = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.awt.windows.WPrinterJob.HandleRecord.access$202(sun.awt.windows.WPrinterJob$HandleRecord, long):long");
        }

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
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$302(sun.awt.windows.WPrinterJob.HandleRecord r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.mPrintHDevNames = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.awt.windows.WPrinterJob.HandleRecord.access$302(sun.awt.windows.WPrinterJob$HandleRecord, long):long");
        }

        HandleRecord() {
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            WPrinterJob.deleteDC(this.mPrintDC, this.mPrintHDevMode, this.mPrintHDevNames);
        }
    }

    public WPrinterJob() {
        this.handleRecord = new HandleRecord();
        Object obj = this.disposerReferent;
        HandleRecord handleRecord = new HandleRecord();
        this.handleRecord = handleRecord;
        Disposer.addRecord(obj, handleRecord);
        initAttributeMembers();
    }

    @Override // sun.java2d.DisposerTarget
    public Object getDisposerReferent() {
        return this.disposerReferent;
    }

    @Override // sun.print.RasterPrinterJob, java.awt.print.PrinterJob
    public PageFormat pageDialog(PageFormat pageFormat) throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (!(getPrintService() instanceof Win32PrintService)) {
            return super.pageDialog(pageFormat);
        }
        PageFormat pageFormat2 = (PageFormat) pageFormat.clone();
        WPageDialog wPageDialog = new WPageDialog((Frame) null, this, pageFormat2, (Printable) null);
        wPageDialog.setRetVal(false);
        wPageDialog.setVisible(true);
        boolean retVal = wPageDialog.getRetVal();
        wPageDialog.dispose();
        if (retVal && this.myService != null) {
            String nativePrintService = getNativePrintService();
            if (!this.myService.getName().equals(nativePrintService)) {
                try {
                    setPrintService(PrintServiceLookupProvider.getWin32PrintLUS().getPrintServiceByName(nativePrintService));
                } catch (PrinterException e2) {
                }
            }
            updatePageAttributes(this.myService, pageFormat2);
            return pageFormat2;
        }
        return pageFormat;
    }

    /* JADX WARN: Code restructure failed: missing block: B:54:0x01cd, code lost:
    
        r0.dispose();
        r8.attributes.add(new javax.print.attribute.standard.Destination(r22.toURI()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01e9, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean displayNativeDialog() {
        /*
            Method dump skipped, instructions count: 490
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.awt.windows.WPrinterJob.displayNativeDialog():boolean");
    }

    @Override // sun.print.RasterPrinterJob, java.awt.print.PrinterJob
    public boolean printDialog() throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (this.attributes == null) {
            this.attributes = new HashPrintRequestAttributeSet();
        }
        if (!(getPrintService() instanceof Win32PrintService)) {
            return super.printDialog(this.attributes);
        }
        if (this.noDefaultPrinter) {
            return false;
        }
        return displayNativeDialog();
    }

    @Override // sun.print.RasterPrinterJob, java.awt.print.PrinterJob
    public void setPrintService(PrintService printService) throws PrinterException {
        super.setPrintService(printService);
        if (!(printService instanceof Win32PrintService)) {
            return;
        }
        this.driverDoesMultipleCopies = false;
        this.driverDoesCollation = false;
        setNativePrintServiceIfNeeded(printService.getName());
    }

    private void setNativePrintServiceIfNeeded(String str) throws PrinterException {
        if (str != null && !str.equals(this.lastNativeService)) {
            setNativePrintService(str);
            this.lastNativeService = str;
        }
    }

    @Override // sun.print.RasterPrinterJob, java.awt.print.PrinterJob
    public PrintService getPrintService() {
        if (this.myService == null) {
            String nativePrintService = getNativePrintService();
            if (nativePrintService != null) {
                this.myService = PrintServiceLookupProvider.getWin32PrintLUS().getPrintServiceByName(nativePrintService);
                if (this.myService != null) {
                    return this.myService;
                }
            }
            this.myService = PrintServiceLookup.lookupDefaultPrintService();
            if (this.myService instanceof Win32PrintService) {
                try {
                    setNativePrintServiceIfNeeded(this.myService.getName());
                } catch (Exception e2) {
                    this.myService = null;
                }
            }
        }
        return this.myService;
    }

    private void initAttributeMembers() {
        this.mAttSides = 0;
        this.mAttChromaticity = 0;
        this.mAttXRes = 0;
        this.mAttYRes = 0;
        this.mAttQuality = 0;
        this.mAttCollate = -1;
        this.mAttCopies = 0;
        this.mAttMediaTray = 0;
        this.mAttMediaSizeName = 0;
        this.mDestination = null;
    }

    @Override // sun.print.RasterPrinterJob
    protected void setAttributes(PrintRequestAttributeSet printRequestAttributeSet) throws PrinterException {
        Media media;
        initAttributeMembers();
        super.setAttributes(printRequestAttributeSet);
        this.mAttCopies = getCopiesInt();
        this.mDestination = this.destinationAttr;
        if (printRequestAttributeSet == null) {
            return;
        }
        Attribute[] array = printRequestAttributeSet.toArray();
        for (int i2 = 0; i2 < array.length; i2++) {
            Attribute media2 = array[i2];
            try {
                if (media2.getCategory() == Sides.class) {
                    setSidesAttrib(media2);
                } else if (media2.getCategory() == Chromaticity.class) {
                    setColorAttrib(media2);
                } else if (media2.getCategory() == PrinterResolution.class) {
                    setResolutionAttrib(media2);
                } else if (media2.getCategory() == PrintQuality.class) {
                    setQualityAttrib(media2);
                } else if (media2.getCategory() == SheetCollate.class) {
                    setCollateAttrib(media2);
                } else if (media2.getCategory() == Media.class || media2.getCategory() == SunAlternateMedia.class) {
                    if (media2.getCategory() == SunAlternateMedia.class && ((media = (Media) printRequestAttributeSet.get(Media.class)) == null || !(media instanceof MediaTray))) {
                        media2 = ((SunAlternateMedia) media2).getMedia();
                    }
                    if (media2 instanceof MediaSizeName) {
                        setWin32MediaAttrib(media2);
                    }
                    if (media2 instanceof MediaTray) {
                        setMediaTrayAttrib(media2);
                    }
                }
            } catch (ClassCastException e2) {
            }
        }
    }

    @Override // sun.print.RasterPrinterJob, java.awt.print.PrinterJob
    public PageFormat defaultPage(PageFormat pageFormat) {
        PageFormat pageFormat2 = (PageFormat) pageFormat.clone();
        getDefaultPage(pageFormat2);
        return pageFormat2;
    }

    @Override // sun.print.RasterPrinterJob
    protected Graphics2D createPathGraphics(PeekGraphics peekGraphics, PrinterJob printerJob, Printable printable, PageFormat pageFormat, int i2) {
        WPathGraphics wPathGraphics;
        PeekMetrics metrics = peekGraphics.getMetrics();
        if (!forcePDL && (forceRaster || metrics.hasNonSolidColors() || metrics.hasCompositing())) {
            wPathGraphics = null;
        } else {
            wPathGraphics = new WPathGraphics(new BufferedImage(8, 8, 1).createGraphics(), printerJob, printable, pageFormat, i2, !peekGraphics.getAWTDrawingOnly());
        }
        return wPathGraphics;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getXRes() {
        if (this.mAttXRes != 0) {
            return this.mAttXRes;
        }
        return this.mPrintXRes;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getYRes() {
        if (this.mAttYRes != 0) {
            return this.mAttYRes;
        }
        return this.mPrintYRes;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPrintableX(Paper paper) {
        return this.mPrintPhysX;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPrintableY(Paper paper) {
        return this.mPrintPhysY;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPrintableWidth(Paper paper) {
        return this.mPrintWidth;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPrintableHeight(Paper paper) {
        return this.mPrintHeight;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPageWidth(Paper paper) {
        return this.mPageWidth;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPageHeight(Paper paper) {
        return this.mPageHeight;
    }

    @Override // sun.print.RasterPrinterJob
    protected boolean isCollated() {
        return this.userRequestedCollation;
    }

    @Override // sun.print.RasterPrinterJob
    protected int getCollatedCopies() {
        debug_println("driverDoesMultipleCopies=" + this.driverDoesMultipleCopies + " driverDoesCollation=" + this.driverDoesCollation);
        if (super.isCollated() && !this.driverDoesCollation) {
            this.mAttCollate = 0;
            this.mAttCopies = 1;
            return getCopies();
        }
        return 1;
    }

    @Override // sun.print.RasterPrinterJob
    protected int getNoncollatedCopies() {
        if (this.driverDoesMultipleCopies || super.isCollated()) {
            return 1;
        }
        return getCopies();
    }

    private long getPrintDC() {
        return this.handleRecord.mPrintDC;
    }

    /* JADX WARN: Failed to check method for inline after forced processsun.awt.windows.WPrinterJob.HandleRecord.access$102(sun.awt.windows.WPrinterJob$HandleRecord, long):long */
    private void setPrintDC(long j2) {
        HandleRecord.access$102(this.handleRecord, j2);
    }

    private long getDevMode() {
        return this.handleRecord.mPrintHDevMode;
    }

    /* JADX WARN: Failed to check method for inline after forced processsun.awt.windows.WPrinterJob.HandleRecord.access$202(sun.awt.windows.WPrinterJob$HandleRecord, long):long */
    private void setDevMode(long j2) {
        HandleRecord.access$202(this.handleRecord, j2);
    }

    private long getDevNames() {
        return this.handleRecord.mPrintHDevNames;
    }

    /* JADX WARN: Failed to check method for inline after forced processsun.awt.windows.WPrinterJob.HandleRecord.access$302(sun.awt.windows.WPrinterJob$HandleRecord, long):long */
    private void setDevNames(long j2) {
        HandleRecord.access$302(this.handleRecord, j2);
    }

    protected void beginPath() {
        beginPath(getPrintDC());
    }

    protected void endPath() {
        endPath(getPrintDC());
    }

    protected void closeFigure() {
        closeFigure(getPrintDC());
    }

    protected void fillPath() {
        fillPath(getPrintDC());
    }

    protected void moveTo(float f2, float f3) {
        moveTo(getPrintDC(), f2, f3);
    }

    protected void lineTo(float f2, float f3) {
        lineTo(getPrintDC(), f2, f3);
    }

    protected void polyBezierTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        polyBezierTo(getPrintDC(), f2, f3, f4, f5, f6, f7);
    }

    protected void setPolyFillMode(int i2) {
        setPolyFillMode(getPrintDC(), i2);
    }

    protected void selectSolidBrush(Color color) {
        if (!color.equals(this.mLastColor)) {
            this.mLastColor = color;
            float[] rGBColorComponents = color.getRGBColorComponents(null);
            selectSolidBrush(getPrintDC(), (int) (rGBColorComponents[0] * 255.0f), (int) (rGBColorComponents[1] * 255.0f), (int) (rGBColorComponents[2] * 255.0f));
        }
    }

    protected int getPenX() {
        return getPenX(getPrintDC());
    }

    protected int getPenY() {
        return getPenY(getPrintDC());
    }

    protected void selectClipPath() {
        selectClipPath(getPrintDC());
    }

    protected void frameRect(float f2, float f3, float f4, float f5) {
        frameRect(getPrintDC(), f2, f3, f4, f5);
    }

    protected void fillRect(float f2, float f3, float f4, float f5, Color color) {
        float[] rGBColorComponents = color.getRGBColorComponents(null);
        fillRect(getPrintDC(), f2, f3, f4, f5, (int) (rGBColorComponents[0] * 255.0f), (int) (rGBColorComponents[1] * 255.0f), (int) (rGBColorComponents[2] * 255.0f));
    }

    protected void selectPen(float f2, Color color) {
        float[] rGBColorComponents = color.getRGBColorComponents(null);
        selectPen(getPrintDC(), f2, (int) (rGBColorComponents[0] * 255.0f), (int) (rGBColorComponents[1] * 255.0f), (int) (rGBColorComponents[2] * 255.0f));
    }

    protected boolean selectStylePen(int i2, int i3, float f2, Color color) {
        long j2;
        long j3;
        float[] rGBColorComponents = color.getRGBColorComponents(null);
        switch (i2) {
            case 0:
                j2 = 512;
                break;
            case 1:
                j2 = 0;
                break;
            case 2:
            default:
                j2 = 256;
                break;
        }
        switch (i3) {
            case 0:
            default:
                j3 = 8192;
                break;
            case 1:
                j3 = 0;
                break;
            case 2:
                j3 = 4096;
                break;
        }
        return selectStylePen(getPrintDC(), j2, j3, f2, (int) (rGBColorComponents[0] * 255.0f), (int) (rGBColorComponents[1] * 255.0f), (int) (rGBColorComponents[2] * 255.0f));
    }

    protected boolean setFont(String str, float f2, int i2, int i3, float f3) {
        boolean font = true;
        if (!str.equals(this.mLastFontFamily) || f2 != this.mLastFontSize || i2 != this.mLastFontStyle || i3 != this.mLastRotation || f3 != this.mLastAwScale) {
            font = setFont(getPrintDC(), str, f2, (i2 & 1) != 0, (i2 & 2) != 0, i3, f3);
            if (font) {
                this.mLastFontFamily = str;
                this.mLastFontSize = f2;
                this.mLastFontStyle = i2;
                this.mLastRotation = i3;
                this.mLastAwScale = f3;
            }
        }
        return font;
    }

    protected void setTextColor(Color color) {
        if (!color.equals(this.mLastTextColor)) {
            this.mLastTextColor = color;
            float[] rGBColorComponents = color.getRGBColorComponents(null);
            setTextColor(getPrintDC(), (int) (rGBColorComponents[0] * 255.0f), (int) (rGBColorComponents[1] * 255.0f), (int) (rGBColorComponents[2] * 255.0f));
        }
    }

    @Override // sun.print.RasterPrinterJob
    protected String removeControlChars(String str) {
        return super.removeControlChars(str);
    }

    protected void textOut(String str, float f2, float f3, float[] fArr) {
        String strRemoveControlChars = removeControlChars(str);
        if (!$assertionsDisabled && fArr != null && strRemoveControlChars.length() != str.length()) {
            throw new AssertionError();
        }
        if (strRemoveControlChars.length() == 0) {
            return;
        }
        textOut(getPrintDC(), strRemoveControlChars, strRemoveControlChars.length(), false, f2, f3, fArr);
    }

    protected void glyphsOut(int[] iArr, float f2, float f3, float[] fArr) {
        char[] cArr = new char[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            cArr[i2] = (char) (iArr[i2] & 65535);
        }
        textOut(getPrintDC(), new String(cArr), iArr.length, true, f2, f3, fArr);
    }

    protected int getGDIAdvance(String str) {
        String strRemoveControlChars = removeControlChars(str);
        if (strRemoveControlChars.length() == 0) {
            return 0;
        }
        return getGDIAdvance(getPrintDC(), strRemoveControlChars);
    }

    protected void drawImage3ByteBGR(byte[] bArr, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        drawDIBImage(getPrintDC(), bArr, f2, f3, f4, f5, f6, f7, f8, f9, 24, null);
    }

    protected void drawDIBImage(byte[] bArr, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int i2, IndexColorModel indexColorModel) {
        int i3 = 24;
        byte[] bArr2 = null;
        if (indexColorModel != null) {
            i3 = i2;
            bArr2 = new byte[(1 << indexColorModel.getPixelSize()) * 4];
            for (int i4 = 0; i4 < indexColorModel.getMapSize(); i4++) {
                bArr2[(i4 * 4) + 0] = (byte) (indexColorModel.getBlue(i4) & 255);
                bArr2[(i4 * 4) + 1] = (byte) (indexColorModel.getGreen(i4) & 255);
                bArr2[(i4 * 4) + 2] = (byte) (indexColorModel.getRed(i4) & 255);
            }
        }
        drawDIBImage(getPrintDC(), bArr, f2, f3, f4, f5, f6, f7, f8, f9, i3, bArr2);
    }

    @Override // sun.print.RasterPrinterJob
    protected void startPage(PageFormat pageFormat, Printable printable, int i2, boolean z2) {
        invalidateCachedState();
        deviceStartPage(pageFormat, printable, i2, z2);
    }

    @Override // sun.print.RasterPrinterJob
    protected void endPage(PageFormat pageFormat, Printable printable, int i2) {
        deviceEndPage(pageFormat, printable, i2);
    }

    private void invalidateCachedState() {
        this.mLastColor = null;
        this.mLastTextColor = null;
        this.mLastFontFamily = null;
    }

    @Override // sun.print.RasterPrinterJob, java.awt.print.PrinterJob
    public void setCopies(int i2) {
        super.setCopies(i2);
        this.defaultCopies = false;
        this.mAttCopies = i2;
        setNativeCopies(i2);
    }

    @Override // sun.print.RasterPrinterJob
    protected void startDoc() throws PrinterException {
        if (!_startDoc(this.mDestination, getJobName())) {
            cancel();
        }
    }

    private final String getPrinterAttrib() {
        PrintService printService = getPrintService();
        return printService != null ? printService.getName() : null;
    }

    private final int getCollateAttrib() {
        return this.mAttCollate;
    }

    private void setCollateAttrib(Attribute attribute) {
        if (attribute == SheetCollate.COLLATED) {
            this.mAttCollate = 1;
        } else {
            this.mAttCollate = 0;
        }
    }

    private void setCollateAttrib(Attribute attribute, PrintRequestAttributeSet printRequestAttributeSet) {
        setCollateAttrib(attribute);
        printRequestAttributeSet.add(attribute);
    }

    private final int getOrientAttrib() {
        int i2 = 1;
        OrientationRequested orientationRequested = this.attributes == null ? null : (OrientationRequested) this.attributes.get(OrientationRequested.class);
        if (orientationRequested == null) {
            orientationRequested = (OrientationRequested) this.myService.getDefaultAttributeValue(OrientationRequested.class);
        }
        if (orientationRequested != null) {
            if (orientationRequested == OrientationRequested.REVERSE_LANDSCAPE) {
                i2 = 2;
            } else if (orientationRequested == OrientationRequested.LANDSCAPE) {
                i2 = 0;
            }
        }
        return i2;
    }

    private void setOrientAttrib(Attribute attribute, PrintRequestAttributeSet printRequestAttributeSet) {
        if (printRequestAttributeSet != null) {
            printRequestAttributeSet.add(attribute);
        }
    }

    private final int getCopiesAttrib() {
        if (this.defaultCopies) {
            return 0;
        }
        return getCopiesInt();
    }

    private final void setRangeCopiesAttribute(int i2, int i3, boolean z2, int i4) {
        if (this.attributes != null) {
            if (z2) {
                this.attributes.add(new PageRanges(i2, i3));
                setPageRange(i2, i3);
            }
            this.defaultCopies = false;
            this.attributes.add(new Copies(i4));
            super.setCopies(i4);
            this.mAttCopies = i4;
        }
    }

    private final boolean getDestAttrib() {
        return this.mDestination != null;
    }

    private final int getQualityAttrib() {
        return this.mAttQuality;
    }

    private void setQualityAttrib(Attribute attribute) {
        if (attribute == PrintQuality.HIGH) {
            this.mAttQuality = -4;
        } else if (attribute == PrintQuality.NORMAL) {
            this.mAttQuality = -3;
        } else {
            this.mAttQuality = -2;
        }
    }

    private void setQualityAttrib(Attribute attribute, PrintRequestAttributeSet printRequestAttributeSet) {
        setQualityAttrib(attribute);
        printRequestAttributeSet.add(attribute);
    }

    private final int getColorAttrib() {
        return this.mAttChromaticity;
    }

    private void setColorAttrib(Attribute attribute) {
        if (attribute == Chromaticity.COLOR) {
            this.mAttChromaticity = 2;
        } else {
            this.mAttChromaticity = 1;
        }
    }

    private void setColorAttrib(Attribute attribute, PrintRequestAttributeSet printRequestAttributeSet) {
        setColorAttrib(attribute);
        printRequestAttributeSet.add(attribute);
    }

    private final int getSidesAttrib() {
        return this.mAttSides;
    }

    private void setSidesAttrib(Attribute attribute) {
        if (attribute == Sides.TWO_SIDED_LONG_EDGE) {
            this.mAttSides = 2;
        } else if (attribute == Sides.TWO_SIDED_SHORT_EDGE) {
            this.mAttSides = 3;
        } else {
            this.mAttSides = 1;
        }
    }

    private void setSidesAttrib(Attribute attribute, PrintRequestAttributeSet printRequestAttributeSet) {
        setSidesAttrib(attribute);
        printRequestAttributeSet.add(attribute);
    }

    private final int[] getWin32MediaAttrib() {
        MediaSize mediaSizeForName;
        int[] iArr = {0, 0};
        if (this.attributes != null) {
            Media media = (Media) this.attributes.get(Media.class);
            if ((media instanceof MediaSizeName) && (mediaSizeForName = MediaSize.getMediaSizeForName((MediaSizeName) media)) != null) {
                iArr[0] = (int) (mediaSizeForName.getX(25400) * 72.0d);
                iArr[1] = (int) (mediaSizeForName.getY(25400) * 72.0d);
            }
        }
        return iArr;
    }

    private void setWin32MediaAttrib(Attribute attribute) {
        if (!(attribute instanceof MediaSizeName)) {
            return;
        }
        this.mAttMediaSizeName = ((Win32PrintService) this.myService).findPaperID((MediaSizeName) attribute);
    }

    private void addPaperSize(PrintRequestAttributeSet printRequestAttributeSet, int i2, int i3, int i4) {
        if (printRequestAttributeSet == null) {
            return;
        }
        MediaSizeName mediaSizeNameFindWin32Media = ((Win32PrintService) this.myService).findWin32Media(i2);
        if (mediaSizeNameFindWin32Media == null) {
            mediaSizeNameFindWin32Media = ((Win32PrintService) this.myService).findMatchingMediaSizeNameMM(i3, i4);
        }
        if (mediaSizeNameFindWin32Media != null) {
            printRequestAttributeSet.add(mediaSizeNameFindWin32Media);
        }
    }

    private void setWin32MediaAttrib(int i2, int i3, int i4) {
        addPaperSize(this.attributes, i2, i3, i4);
        this.mAttMediaSizeName = i2;
    }

    private void setMediaTrayAttrib(Attribute attribute) {
        if (attribute == MediaTray.BOTTOM) {
            this.mAttMediaTray = 2;
            return;
        }
        if (attribute == MediaTray.ENVELOPE) {
            this.mAttMediaTray = 5;
            return;
        }
        if (attribute == MediaTray.LARGE_CAPACITY) {
            this.mAttMediaTray = 11;
            return;
        }
        if (attribute == MediaTray.MAIN) {
            this.mAttMediaTray = 1;
            return;
        }
        if (attribute == MediaTray.MANUAL) {
            this.mAttMediaTray = 4;
            return;
        }
        if (attribute == MediaTray.MIDDLE) {
            this.mAttMediaTray = 3;
            return;
        }
        if (attribute == MediaTray.SIDE) {
            this.mAttMediaTray = 7;
            return;
        }
        if (attribute == MediaTray.TOP) {
            this.mAttMediaTray = 1;
        } else if (attribute instanceof Win32MediaTray) {
            this.mAttMediaTray = ((Win32MediaTray) attribute).winID;
        } else {
            this.mAttMediaTray = 1;
        }
    }

    private void setMediaTrayAttrib(int i2) {
        this.mAttMediaTray = i2;
        ((Win32PrintService) this.myService).findMediaTray(i2);
    }

    private int getMediaTrayAttrib() {
        return this.mAttMediaTray;
    }

    private final boolean getPrintToFileEnabled() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission(new FilePermission("<<ALL FILES>>", SecurityConstants.PROPERTY_RW_ACTION));
                return true;
            } catch (SecurityException e2) {
                return false;
            }
        }
        return true;
    }

    private final void setNativeAttributes(int i2, int i3, int i4) {
        Sides sides;
        PrintQuality printQuality;
        if (this.attributes == null) {
            return;
        }
        if ((i2 & 32) != 0) {
            if (((Destination) this.attributes.get(Destination.class)) == null) {
                try {
                    this.attributes.add(new Destination(new File("./out.prn").toURI()));
                } catch (SecurityException e2) {
                    try {
                        this.attributes.add(new Destination(new URI("file:out.prn")));
                    } catch (URISyntaxException e3) {
                    }
                }
            }
        } else {
            this.attributes.remove(Destination.class);
        }
        if ((i2 & 16) != 0) {
            setCollateAttrib(SheetCollate.COLLATED, this.attributes);
        } else {
            setCollateAttrib(SheetCollate.UNCOLLATED, this.attributes);
        }
        if ((i2 & 2) != 0) {
            this.attributes.add(SunPageSelection.RANGE);
        } else if ((i2 & 1) != 0) {
            this.attributes.add(SunPageSelection.SELECTION);
        } else {
            this.attributes.add(SunPageSelection.ALL);
        }
        if ((i3 & 1) != 0) {
            if ((i4 & 16384) != 0) {
                setOrientAttrib(OrientationRequested.LANDSCAPE, this.attributes);
            } else {
                setOrientAttrib(OrientationRequested.PORTRAIT, this.attributes);
            }
        }
        if ((i3 & 2048) != 0) {
            if ((i4 & 512) != 0) {
                setColorAttrib(Chromaticity.COLOR, this.attributes);
            } else {
                setColorAttrib(Chromaticity.MONOCHROME, this.attributes);
            }
        }
        if ((i3 & 1024) != 0) {
            if ((i4 & 128) != 0) {
                printQuality = PrintQuality.DRAFT;
            } else if ((i3 & 64) != 0) {
                printQuality = PrintQuality.HIGH;
            } else {
                printQuality = PrintQuality.NORMAL;
            }
            setQualityAttrib(printQuality, this.attributes);
        }
        if ((i3 & 4096) != 0) {
            if ((i4 & 16) != 0) {
                sides = Sides.TWO_SIDED_LONG_EDGE;
            } else if ((i4 & 32) != 0) {
                sides = Sides.TWO_SIDED_SHORT_EDGE;
            } else {
                sides = Sides.ONE_SIDED;
            }
            setSidesAttrib(sides, this.attributes);
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/WPrinterJob$DevModeValues.class */
    private static final class DevModeValues {
        int dmFields;
        short copies;
        short collate;
        short color;
        short duplex;
        short orient;
        short paper;
        short bin;
        short xres_quality;
        short yres;

        private DevModeValues() {
        }
    }

    private void getDevModeValues(PrintRequestAttributeSet printRequestAttributeSet, DevModeValues devModeValues) {
        SunAlternateMedia sunAlternateMedia;
        Copies copies = (Copies) printRequestAttributeSet.get(Copies.class);
        if (copies != null) {
            devModeValues.dmFields |= 256;
            devModeValues.copies = (short) copies.getValue();
        }
        SheetCollate sheetCollate = (SheetCollate) printRequestAttributeSet.get(SheetCollate.class);
        if (sheetCollate != null) {
            devModeValues.dmFields |= 32768;
            devModeValues.collate = sheetCollate == SheetCollate.COLLATED ? (short) 1 : (short) 0;
        }
        Chromaticity chromaticity = (Chromaticity) printRequestAttributeSet.get(Chromaticity.class);
        if (chromaticity != null) {
            devModeValues.dmFields |= 2048;
            if (chromaticity == Chromaticity.COLOR) {
                devModeValues.color = (short) 2;
            } else {
                devModeValues.color = (short) 1;
            }
        }
        Sides sides = (Sides) printRequestAttributeSet.get(Sides.class);
        if (sides != null) {
            devModeValues.dmFields |= 4096;
            if (sides == Sides.TWO_SIDED_LONG_EDGE) {
                devModeValues.duplex = (short) 2;
            } else if (sides == Sides.TWO_SIDED_SHORT_EDGE) {
                devModeValues.duplex = (short) 3;
            } else {
                devModeValues.duplex = (short) 1;
            }
        }
        OrientationRequested orientationRequested = (OrientationRequested) printRequestAttributeSet.get(OrientationRequested.class);
        if (orientationRequested != null) {
            devModeValues.dmFields |= 1;
            devModeValues.orient = orientationRequested == OrientationRequested.LANDSCAPE ? (short) 2 : (short) 1;
        }
        Media media = (Media) printRequestAttributeSet.get(Media.class);
        if (media instanceof MediaSizeName) {
            devModeValues.dmFields |= 2;
            devModeValues.paper = (short) ((Win32PrintService) this.myService).findPaperID((MediaSizeName) media);
        }
        MediaTray mediaTray = null;
        if (media instanceof MediaTray) {
            mediaTray = (MediaTray) media;
        }
        if (mediaTray == null && (sunAlternateMedia = (SunAlternateMedia) printRequestAttributeSet.get(SunAlternateMedia.class)) != null && (sunAlternateMedia.getMedia() instanceof MediaTray)) {
            mediaTray = (MediaTray) sunAlternateMedia.getMedia();
        }
        if (mediaTray != null) {
            devModeValues.dmFields |= 512;
            devModeValues.bin = (short) ((Win32PrintService) this.myService).findTrayID(mediaTray);
        }
        PrintQuality printQuality = (PrintQuality) printRequestAttributeSet.get(PrintQuality.class);
        if (printQuality != null) {
            devModeValues.dmFields |= 1024;
            if (printQuality == PrintQuality.DRAFT) {
                devModeValues.xres_quality = (short) -1;
            } else if (printQuality == PrintQuality.HIGH) {
                devModeValues.xres_quality = (short) -4;
            } else {
                devModeValues.xres_quality = (short) -3;
            }
        }
        PrinterResolution printerResolution = (PrinterResolution) printRequestAttributeSet.get(PrinterResolution.class);
        if (printerResolution != null) {
            devModeValues.dmFields |= 9216;
            devModeValues.xres_quality = (short) printerResolution.getCrossFeedResolution(100);
            devModeValues.yres = (short) printerResolution.getFeedResolution(100);
        }
    }

    private final void setJobAttributes(PrintRequestAttributeSet printRequestAttributeSet, int i2, int i3, short s2, short s3, short s4, short s5, short s6, short s7, short s8) {
        Sides sides;
        PrintQuality printQuality;
        if (printRequestAttributeSet == null) {
            return;
        }
        if ((i2 & 256) != 0) {
            printRequestAttributeSet.add(new Copies(s2));
        }
        if ((i2 & 32768) != 0) {
            if ((i3 & 32768) != 0) {
                printRequestAttributeSet.add(SheetCollate.COLLATED);
            } else {
                printRequestAttributeSet.add(SheetCollate.UNCOLLATED);
            }
        }
        if ((i2 & 1) != 0) {
            if ((i3 & 16384) != 0) {
                printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
            } else {
                printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
            }
        }
        if ((i2 & 2048) != 0) {
            if ((i3 & 512) != 0) {
                printRequestAttributeSet.add(Chromaticity.COLOR);
            } else {
                printRequestAttributeSet.add(Chromaticity.MONOCHROME);
            }
        }
        if ((i2 & 1024) != 0) {
            if (s7 < 0) {
                if ((i3 & 128) != 0) {
                    printQuality = PrintQuality.DRAFT;
                } else if ((i2 & 64) != 0) {
                    printQuality = PrintQuality.HIGH;
                } else {
                    printQuality = PrintQuality.NORMAL;
                }
                printRequestAttributeSet.add(printQuality);
            } else if (s7 > 0 && s8 > 0) {
                printRequestAttributeSet.add(new PrinterResolution(s7, s8, 100));
            }
        }
        if ((i2 & 4096) != 0) {
            if ((i3 & 16) != 0) {
                sides = Sides.TWO_SIDED_LONG_EDGE;
            } else if ((i3 & 32) != 0) {
                sides = Sides.TWO_SIDED_SHORT_EDGE;
            } else {
                sides = Sides.ONE_SIDED;
            }
            printRequestAttributeSet.add(sides);
        }
        if ((i2 & 2) != 0) {
            addPaperSize(printRequestAttributeSet, s3, s4, s5);
        }
        if ((i2 & 512) != 0) {
            printRequestAttributeSet.add(new SunAlternateMedia(((Win32PrintService) this.myService).findMediaTray(s6)));
        }
    }

    public PrintRequestAttributeSet showDocumentProperties(Window window, PrintService printService, PrintRequestAttributeSet printRequestAttributeSet) {
        try {
            setNativePrintServiceIfNeeded(printService.getName());
        } catch (PrinterException e2) {
        }
        long hWnd = ((WWindowPeer) window.getPeer()).getHWnd();
        DevModeValues devModeValues = new DevModeValues();
        getDevModeValues(printRequestAttributeSet, devModeValues);
        if (showDocProperties(hWnd, printRequestAttributeSet, devModeValues.dmFields, devModeValues.copies, devModeValues.collate, devModeValues.color, devModeValues.duplex, devModeValues.orient, devModeValues.paper, devModeValues.bin, devModeValues.xres_quality, devModeValues.yres)) {
            return printRequestAttributeSet;
        }
        return null;
    }

    private final void setResolutionDPI(int i2, int i3) {
        if (this.attributes != null) {
            this.attributes.add(new PrinterResolution(i2, i3, 100));
        }
        this.mAttXRes = i2;
        this.mAttYRes = i3;
    }

    private void setResolutionAttrib(Attribute attribute) {
        PrinterResolution printerResolution = (PrinterResolution) attribute;
        this.mAttXRes = printerResolution.getCrossFeedResolution(100);
        this.mAttYRes = printerResolution.getFeedResolution(100);
    }

    private void setPrinterNameAttrib(String str) {
        PrintService printService = getPrintService();
        if (str == null) {
            return;
        }
        if (printService != null && str.equals(printService.getName())) {
            return;
        }
        PrintService[] printServiceArrLookupPrintServices = PrinterJob.lookupPrintServices();
        for (int i2 = 0; i2 < printServiceArrLookupPrintServices.length; i2++) {
            if (str.equals(printServiceArrLookupPrintServices[i2].getName())) {
                try {
                    setPrintService(printServiceArrLookupPrintServices[i2]);
                    return;
                } catch (PrinterException e2) {
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/WPrinterJob$PrintToFileErrorDialog.class */
    class PrintToFileErrorDialog extends Dialog implements ActionListener {
        final /* synthetic */ WPrinterJob this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PrintToFileErrorDialog(WPrinterJob wPrinterJob, Frame frame, String str, String str2, String str3) {
            super(frame, str, true);
            this.this$0 = wPrinterJob;
            init(frame, str, str2, str3);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PrintToFileErrorDialog(WPrinterJob wPrinterJob, Dialog dialog, String str, String str2, String str3) {
            super(dialog, str, true);
            this.this$0 = wPrinterJob;
            init(dialog, str, str2, str3);
        }

        private void init(Component component, String str, String str2, String str3) {
            Panel panel = new Panel();
            add(BorderLayout.CENTER, new Label(str2));
            Button button = new Button(str3);
            button.addActionListener(this);
            panel.add(button);
            add("South", panel);
            pack();
            Dimension size = getSize();
            if (component != null) {
                Rectangle bounds = component.getBounds();
                setLocation(bounds.f12372x + ((bounds.width - size.width) / 2), bounds.f12373y + ((bounds.height - size.height) / 2));
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            dispose();
        }
    }
}
