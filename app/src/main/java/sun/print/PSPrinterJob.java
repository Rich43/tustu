package sun.print;

import com.sun.glass.ui.Platform;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterIOException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderMalfunctionError;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import javax.print.PrintService;
import javax.print.StreamPrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.PrinterName;
import javax.print.attribute.standard.Sides;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;
import sun.awt.CharsetString;
import sun.awt.FontConfiguration;
import sun.awt.PlatformFont;
import sun.awt.SunToolkit;
import sun.font.FontUtilities;

/* loaded from: rt.jar:sun/print/PSPrinterJob.class */
public class PSPrinterJob extends RasterPrinterJob {
    protected static final int FILL_EVEN_ODD = 1;
    protected static final int FILL_WINDING = 2;
    private static final int MAX_PSSTR = 65535;
    private static final int RED_MASK = 16711680;
    private static final int GREEN_MASK = 65280;
    private static final int BLUE_MASK = 255;
    private static final int RED_SHIFT = 16;
    private static final int GREEN_SHIFT = 8;
    private static final int BLUE_SHIFT = 0;
    private static final int LOWNIBBLE_MASK = 15;
    private static final int HINIBBLE_MASK = 240;
    private static final int HINIBBLE_SHIFT = 4;
    private static final int PS_XRES = 300;
    private static final int PS_YRES = 300;
    private static final String ADOBE_PS_STR = "%!PS-Adobe-3.0";
    private static final String EOF_COMMENT = "%%EOF";
    private static final String PAGE_COMMENT = "%%Page: ";
    private static final String READIMAGEPROC = "/imStr 0 def /imageSrc {currentfile /ASCII85Decode filter /RunLengthDecode filter  imStr readstring pop } def";
    private static final String COPIES = "/#copies exch def";
    private static final String PAGE_SAVE = "/pgSave save def";
    private static final String PAGE_RESTORE = "pgSave restore";
    private static final String SHOWPAGE = "showpage";
    private static final String IMAGE_SAVE = "/imSave save def";
    private static final String IMAGE_STR = " string /imStr exch def";
    private static final String IMAGE_RESTORE = "imSave restore";
    private static final String COORD_PREP = " 0 exch translate 1 -1 scale[72 300 div 0 0 72 300 div 0 0]concat";
    private static final String SetFontName = "F";
    private static final String DrawStringName = "S";
    private static final String EVEN_ODD_FILL_STR = "EF";
    private static final String WINDING_FILL_STR = "WF";
    private static final String EVEN_ODD_CLIP_STR = "EC";
    private static final String WINDING_CLIP_STR = "WC";
    private static final String MOVETO_STR = " M";
    private static final String LINETO_STR = " L";
    private static final String CURVETO_STR = " C";
    private static final String GRESTORE_STR = "R";
    private static final String GSAVE_STR = "G";
    private static final String NEWPATH_STR = "N";
    private static final String CLOSEPATH_STR = "P";
    private static final String SETRGBCOLOR_STR = " SC";
    private static final String SETGRAY_STR = " SG";
    private int mDestType;
    private String mOptions;
    private Font mLastFont;
    private Color mLastColor;
    private Shape mLastClip;
    private AffineTransform mLastTransform;
    FontMetrics mCurMetrics;
    PrintStream mPSStream;
    File spoolFile;
    private float mPenX;
    private float mPenY;
    private float mStartPathX;
    private float mStartPathY;
    private static boolean isMac;
    private static final byte[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
    private static Properties mFontProps = null;
    private String mDestination = "lp";
    private boolean mNoJobSheet = false;
    private EPSPrinter epsPrinter = null;
    private String mFillOpStr = WINDING_FILL_STR;
    private String mClipOpStr = WINDING_CLIP_STR;
    ArrayList mGStateStack = new ArrayList();

    static {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.PSPrinterJob.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                Properties unused = PSPrinterJob.mFontProps = PSPrinterJob.initProps();
                boolean unused2 = PSPrinterJob.isMac = System.getProperty("os.name").startsWith(Platform.MAC);
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Properties initProps() {
        String property = System.getProperty("java.home");
        if (property != null) {
            String language = SunToolkit.getStartupLocale().getLanguage();
            try {
                File file = new File(property + File.separator + "lib" + File.separator + "psfontj2d.properties." + language);
                if (!file.canRead()) {
                    file = new File(property + File.separator + "lib" + File.separator + "psfont.properties." + language);
                    if (!file.canRead()) {
                        file = new File(property + File.separator + "lib" + File.separator + "psfontj2d.properties");
                        if (!file.canRead()) {
                            file = new File(property + File.separator + "lib" + File.separator + "psfont.properties");
                            if (!file.canRead()) {
                                return (Properties) null;
                            }
                        }
                    }
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file.getPath()));
                Properties properties = new Properties();
                properties.load(bufferedInputStream);
                bufferedInputStream.close();
                return properties;
            } catch (Exception e2) {
                return (Properties) null;
            }
        }
        return (Properties) null;
    }

    @Override // sun.print.RasterPrinterJob, java.awt.print.PrinterJob
    public boolean printDialog() throws HeadlessException {
        boolean zPrintDialog;
        PrintServiceAttributeSet attributes;
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (this.attributes == null) {
            this.attributes = new HashPrintRequestAttributeSet();
        }
        this.attributes.add(new Copies(getCopies()));
        this.attributes.add(new JobName(getJobName(), null));
        if (((DialogTypeSelection) this.attributes.get(DialogTypeSelection.class)) == DialogTypeSelection.NATIVE) {
            this.attributes.remove(DialogTypeSelection.class);
            zPrintDialog = printDialog(this.attributes);
            this.attributes.add(DialogTypeSelection.NATIVE);
        } else {
            zPrintDialog = printDialog(this.attributes);
        }
        if (zPrintDialog) {
            JobName jobName = (JobName) this.attributes.get(JobName.class);
            if (jobName != null) {
                setJobName(jobName.getValue());
            }
            Copies copies = (Copies) this.attributes.get(Copies.class);
            if (copies != null) {
                setCopies(copies.getValue());
            }
            Destination destination = (Destination) this.attributes.get(Destination.class);
            if (destination != null) {
                try {
                    this.mDestType = 1;
                    this.mDestination = new File(destination.getURI()).getPath();
                } catch (Exception e2) {
                    this.mDestination = "out.ps";
                }
            } else {
                this.mDestType = 0;
                PrintService printService = getPrintService();
                if (printService != null) {
                    this.mDestination = printService.getName();
                    if (isMac && (attributes = printService.getAttributes()) != null) {
                        this.mDestination = attributes.get(PrinterName.class).toString();
                    }
                }
            }
        }
        return zPrintDialog;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v163, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r0v178, types: [java.io.OutputStream] */
    @Override // sun.print.RasterPrinterJob
    protected void startDoc() throws IndexOutOfBoundsException, PrinterException, NumberFormatException {
        FileOutputStream fileOutputStream;
        if (this.epsPrinter == null) {
            if (getPrintService() instanceof PSStreamPrintService) {
                StreamPrintService streamPrintService = (StreamPrintService) getPrintService();
                this.mDestType = 2;
                if (streamPrintService.isDisposed()) {
                    throw new PrinterException("service is disposed");
                }
                fileOutputStream = streamPrintService.getOutputStream();
                if (fileOutputStream == null) {
                    throw new PrinterException("Null output stream");
                }
            } else {
                this.mNoJobSheet = this.noJobSheet;
                if (this.destinationAttr != null) {
                    this.mDestType = 1;
                    this.mDestination = this.destinationAttr;
                }
                if (this.mDestType == 1) {
                    try {
                        this.spoolFile = new File(this.mDestination);
                        fileOutputStream = new FileOutputStream(this.spoolFile);
                    } catch (IOException e2) {
                        throw new PrinterIOException(e2);
                    }
                } else {
                    PrinterOpener printerOpener = new PrinterOpener();
                    AccessController.doPrivileged(printerOpener);
                    if (printerOpener.pex != null) {
                        throw printerOpener.pex;
                    }
                    fileOutputStream = printerOpener.result;
                }
            }
            this.mPSStream = new PrintStream(new BufferedOutputStream(fileOutputStream));
            this.mPSStream.println(ADOBE_PS_STR);
        }
        this.mPSStream.println("%%BeginProlog");
        this.mPSStream.println(READIMAGEPROC);
        this.mPSStream.println("/BD {bind def} bind def");
        this.mPSStream.println("/D {def} BD");
        this.mPSStream.println("/C {curveto} BD");
        this.mPSStream.println("/L {lineto} BD");
        this.mPSStream.println("/M {moveto} BD");
        this.mPSStream.println("/R {grestore} BD");
        this.mPSStream.println("/G {gsave} BD");
        this.mPSStream.println("/N {newpath} BD");
        this.mPSStream.println("/P {closepath} BD");
        this.mPSStream.println("/EC {eoclip} BD");
        this.mPSStream.println("/WC {clip} BD");
        this.mPSStream.println("/EF {eofill} BD");
        this.mPSStream.println("/WF {fill} BD");
        this.mPSStream.println("/SG {setgray} BD");
        this.mPSStream.println("/SC {setrgbcolor} BD");
        this.mPSStream.println("/ISOF {");
        this.mPSStream.println("     dup findfont dup length 1 add dict begin {");
        this.mPSStream.println("             1 index /FID eq {pop pop} {D} ifelse");
        this.mPSStream.println("     } forall /Encoding ISOLatin1Encoding D");
        this.mPSStream.println("     currentdict end definefont");
        this.mPSStream.println("} BD");
        this.mPSStream.println("/NZ {dup 1 lt {pop 1} if} BD");
        this.mPSStream.println("/S {");
        this.mPSStream.println("     moveto 1 index stringwidth pop NZ sub");
        this.mPSStream.println("     1 index length 1 sub NZ div 0");
        this.mPSStream.println("     3 2 roll ashow newpath} BD");
        this.mPSStream.println("/FL [");
        if (mFontProps == null) {
            this.mPSStream.println(" /Helvetica ISOF");
            this.mPSStream.println(" /Helvetica-Bold ISOF");
            this.mPSStream.println(" /Helvetica-Oblique ISOF");
            this.mPSStream.println(" /Helvetica-BoldOblique ISOF");
            this.mPSStream.println(" /Times-Roman ISOF");
            this.mPSStream.println(" /Times-Bold ISOF");
            this.mPSStream.println(" /Times-Italic ISOF");
            this.mPSStream.println(" /Times-BoldItalic ISOF");
            this.mPSStream.println(" /Courier ISOF");
            this.mPSStream.println(" /Courier-Bold ISOF");
            this.mPSStream.println(" /Courier-Oblique ISOF");
            this.mPSStream.println(" /Courier-BoldOblique ISOF");
        } else {
            int i2 = Integer.parseInt(mFontProps.getProperty("font.num", "9"));
            for (int i3 = 0; i3 < i2; i3++) {
                this.mPSStream.println("    /" + mFontProps.getProperty("font." + String.valueOf(i3), "Courier ISOF"));
            }
        }
        this.mPSStream.println("] D");
        this.mPSStream.println("/F {");
        this.mPSStream.println("     FL exch get exch scalefont");
        this.mPSStream.println("     [1 0 0 -1 0 0] makefont setfont} BD");
        this.mPSStream.println("%%EndProlog");
        this.mPSStream.println("%%BeginSetup");
        if (this.epsPrinter == null) {
            PageFormat pageFormat = getPageable().getPageFormat(0);
            this.mPSStream.print("<< /PageSize [" + pageFormat.getPaper().getWidth() + " " + pageFormat.getPaper().getHeight() + "]");
            final PrintService printService = getPrintService();
            if (((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.PSPrinterJob.2
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    try {
                        Class<?> cls = Class.forName("sun.print.IPPPrintService");
                        if (cls.isInstance(printService)) {
                            return (Boolean) cls.getMethod("isPostscript", (Class[]) null).invoke(printService, (Object[]) null);
                        }
                    } catch (Throwable th) {
                    }
                    return Boolean.TRUE;
                }
            })).booleanValue()) {
                this.mPSStream.print(" /DeferredMediaSelection true");
            }
            this.mPSStream.print(" /ImagingBBox null /ManualFeed false");
            this.mPSStream.print(isCollated() ? " /Collate true" : "");
            this.mPSStream.print(" /NumCopies " + getCopiesInt());
            if (this.sidesAttr != Sides.ONE_SIDED) {
                if (this.sidesAttr == Sides.TWO_SIDED_LONG_EDGE) {
                    this.mPSStream.print(" /Duplex true ");
                } else if (this.sidesAttr == Sides.TWO_SIDED_SHORT_EDGE) {
                    this.mPSStream.print(" /Duplex true /Tumble true ");
                }
            }
            this.mPSStream.println(" >> setpagedevice ");
        }
        this.mPSStream.println("%%EndSetup");
    }

    /* loaded from: rt.jar:sun/print/PSPrinterJob$PrinterOpener.class */
    private class PrinterOpener implements PrivilegedAction {
        PrinterException pex;
        OutputStream result;

        private PrinterOpener() {
        }

        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Object run2() {
            try {
                PSPrinterJob.this.spoolFile = Files.createTempFile("javaprint", ".ps", new FileAttribute[0]).toFile();
                PSPrinterJob.this.spoolFile.deleteOnExit();
                this.result = new FileOutputStream(PSPrinterJob.this.spoolFile);
                return this.result;
            } catch (IOException e2) {
                this.pex = new PrinterIOException(e2);
                return null;
            }
        }
    }

    /* loaded from: rt.jar:sun/print/PSPrinterJob$PrinterSpooler.class */
    private class PrinterSpooler implements PrivilegedAction {
        PrinterException pex;

        private PrinterSpooler() {
        }

        /* JADX WARN: Failed to calculate best type for var: r10v0 ??
        java.lang.NullPointerException
         */
        /* JADX WARN: Failed to calculate best type for var: r11v0 ??
        java.lang.NullPointerException
         */
        /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
         */
        /* JADX WARN: Not initialized variable reg: 10, insn: 0x01dd: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r10 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:91:0x01dd */
        /* JADX WARN: Not initialized variable reg: 11, insn: 0x01e2: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r11 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:93:0x01e2 */
        /* JADX WARN: Type inference failed for: r0v27, types: [int, java.lang.Throwable] */
        /* JADX WARN: Type inference failed for: r0v45, types: [java.io.InputStreamReader] */
        /* JADX WARN: Type inference failed for: r0v46, types: [java.io.InputStreamReader] */
        /* JADX WARN: Type inference failed for: r0v87, types: [java.lang.CharSequence, java.lang.Throwable] */
        /* JADX WARN: Type inference failed for: r0v89, types: [java.io.PrintWriter] */
        /* JADX WARN: Type inference failed for: r10v0, types: [java.io.PrintWriter] */
        /* JADX WARN: Type inference failed for: r11v0, types: [java.lang.Throwable] */
        private void handleProcessFailure(Process process, String[] strArr, int i2) throws IOException {
            ?? r10;
            ?? r11;
            String string;
            ?? r0;
            StringWriter stringWriter = new StringWriter();
            Throwable th = null;
            try {
                try {
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    printWriter.append((CharSequence) "error=").append((CharSequence) Integer.toString(i2));
                    printWriter.append((CharSequence) " running:");
                    ?? length = strArr.length;
                    int i3 = 0;
                    while (i3 < length) {
                        r0 = strArr[i3];
                        printWriter.append((CharSequence) " '").append(r0).append((CharSequence) PdfOps.SINGLE_QUOTE_TOKEN);
                        i3++;
                    }
                    try {
                        try {
                            InputStream errorStream = process.getErrorStream();
                            Throwable th2 = null;
                            try {
                                InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
                                Throwable th3 = null;
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                Throwable th4 = null;
                                while (bufferedReader.ready()) {
                                    try {
                                        try {
                                            printWriter.println();
                                            printWriter.append((CharSequence) "\t\t").append((CharSequence) bufferedReader.readLine());
                                        } catch (Throwable th5) {
                                            if (bufferedReader != null) {
                                                if (th4 != null) {
                                                    try {
                                                        bufferedReader.close();
                                                    } catch (Throwable th6) {
                                                        th4.addSuppressed(th6);
                                                    }
                                                } else {
                                                    bufferedReader.close();
                                                }
                                            }
                                            throw th5;
                                        }
                                    } finally {
                                    }
                                }
                                if (bufferedReader != null) {
                                    if (0 != 0) {
                                        try {
                                            bufferedReader.close();
                                        } catch (Throwable th7) {
                                            th4.addSuppressed(th7);
                                        }
                                    } else {
                                        bufferedReader.close();
                                    }
                                }
                                if (inputStreamReader != null) {
                                    if (0 != 0) {
                                        try {
                                            inputStreamReader.close();
                                        } catch (Throwable th8) {
                                            th3.addSuppressed(th8);
                                        }
                                    } else {
                                        inputStreamReader.close();
                                    }
                                }
                                if (errorStream != null) {
                                    if (0 != 0) {
                                        try {
                                            errorStream.close();
                                        } catch (Throwable th9) {
                                            th2.addSuppressed(th9);
                                        }
                                    } else {
                                        errorStream.close();
                                    }
                                }
                                throw new IOException(string);
                            } catch (Throwable th10) {
                                if (i3 != 0) {
                                    if (r0 != 0) {
                                        try {
                                            i3.close();
                                        } catch (Throwable th11) {
                                            r0.addSuppressed(th11);
                                        }
                                    } else {
                                        i3.close();
                                    }
                                }
                                throw th10;
                            }
                        } finally {
                            printWriter.flush();
                            IOException iOException = new IOException(stringWriter.toString());
                        }
                    } catch (Throwable th12) {
                        if (strArr != 0) {
                            if (length != 0) {
                                try {
                                    strArr.close();
                                } catch (Throwable th13) {
                                    length.addSuppressed(th13);
                                }
                            } else {
                                strArr.close();
                            }
                        }
                        throw th12;
                    }
                } catch (Throwable th14) {
                    if (r10 != 0) {
                        if (r11 != 0) {
                            try {
                                r10.close();
                            } catch (Throwable th15) {
                                r11.addSuppressed(th15);
                            }
                        } else {
                            r10.close();
                        }
                    }
                    throw th14;
                }
            } catch (Throwable th16) {
                if (stringWriter != null) {
                    if (0 != 0) {
                        try {
                            stringWriter.close();
                        } catch (Throwable th17) {
                            th.addSuppressed(th17);
                        }
                    } else {
                        stringWriter.close();
                    }
                }
                throw th16;
            }
        }

        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Object run2() {
            if (PSPrinterJob.this.spoolFile != null) {
                try {
                    if (PSPrinterJob.this.spoolFile.exists()) {
                        try {
                            String[] strArrPrintExecCmd = PSPrinterJob.this.printExecCmd(PSPrinterJob.this.mDestination, PSPrinterJob.this.mOptions, PSPrinterJob.this.mNoJobSheet, PSPrinterJob.this.getJobNameInt(), 1, PSPrinterJob.this.spoolFile.getAbsolutePath());
                            Process processExec = Runtime.getRuntime().exec(strArrPrintExecCmd);
                            processExec.waitFor();
                            int iExitValue = processExec.exitValue();
                            if (0 != iExitValue) {
                                handleProcessFailure(processExec, strArrPrintExecCmd, iExitValue);
                            }
                            PSPrinterJob.this.spoolFile.delete();
                            return null;
                        } catch (IOException e2) {
                            this.pex = new PrinterIOException(e2);
                            PSPrinterJob.this.spoolFile.delete();
                            return null;
                        } catch (InterruptedException e3) {
                            this.pex = new PrinterException(e3.toString());
                            PSPrinterJob.this.spoolFile.delete();
                            return null;
                        }
                    }
                } catch (Throwable th) {
                    PSPrinterJob.this.spoolFile.delete();
                    throw th;
                }
            }
            this.pex = new PrinterException("No spool file");
            return null;
        }
    }

    @Override // sun.print.RasterPrinterJob
    protected void abortDoc() {
        if (this.mPSStream != null && this.mDestType != 2) {
            this.mPSStream.close();
        }
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.PSPrinterJob.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                if (PSPrinterJob.this.spoolFile != null && PSPrinterJob.this.spoolFile.exists()) {
                    PSPrinterJob.this.spoolFile.delete();
                    return null;
                }
                return null;
            }
        });
    }

    @Override // sun.print.RasterPrinterJob
    protected void endDoc() throws PrinterException {
        PrintServiceAttributeSet attributes;
        if (this.mPSStream != null) {
            this.mPSStream.println(EOF_COMMENT);
            this.mPSStream.flush();
            if (this.mDestType != 2) {
                this.mPSStream.close();
            }
        }
        if (this.mDestType == 0) {
            PrintService printService = getPrintService();
            if (printService != null) {
                this.mDestination = printService.getName();
                if (isMac && (attributes = printService.getAttributes()) != null) {
                    this.mDestination = attributes.get(PrinterName.class).toString();
                }
            }
            PrinterSpooler printerSpooler = new PrinterSpooler();
            AccessController.doPrivileged(printerSpooler);
            if (printerSpooler.pex != null) {
                throw printerSpooler.pex;
            }
        }
    }

    @Override // sun.print.RasterPrinterJob
    protected void startPage(PageFormat pageFormat, Printable printable, int i2, boolean z2) throws PrinterException {
        double height = pageFormat.getPaper().getHeight();
        double width = pageFormat.getPaper().getWidth();
        int i3 = i2 + 1;
        this.mGStateStack = new ArrayList();
        this.mGStateStack.add(new GState());
        this.mPSStream.println(PAGE_COMMENT + i3 + " " + i3);
        if (i2 > 0 && z2) {
            this.mPSStream.print("<< /PageSize [" + width + " " + height + "]");
            final PrintService printService = getPrintService();
            if (((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.PSPrinterJob.4
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    try {
                        Class<?> cls = Class.forName("sun.print.IPPPrintService");
                        if (cls.isInstance(printService)) {
                            return (Boolean) cls.getMethod("isPostscript", (Class[]) null).invoke(printService, (Object[]) null);
                        }
                    } catch (Throwable th) {
                    }
                    return Boolean.TRUE;
                }
            })).booleanValue()) {
                this.mPSStream.print(" /DeferredMediaSelection true");
            }
            this.mPSStream.println(" >> setpagedevice");
        }
        this.mPSStream.println(PAGE_SAVE);
        this.mPSStream.println(height + COORD_PREP);
    }

    @Override // sun.print.RasterPrinterJob
    protected void endPage(PageFormat pageFormat, Printable printable, int i2) throws PrinterException {
        this.mPSStream.println(PAGE_RESTORE);
        this.mPSStream.println(SHOWPAGE);
    }

    protected void drawImageBGR(byte[] bArr, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int i2, int i3) {
        int i4;
        setTransform(new AffineTransform());
        prepDrawing();
        int i5 = (int) f8;
        int i6 = (int) f9;
        this.mPSStream.println(IMAGE_SAVE);
        int i7 = 3 * i5;
        while (true) {
            i4 = i7;
            if (i4 <= 65535) {
                break;
            } else {
                i7 = i4 / 2;
            }
        }
        this.mPSStream.println(i4 + IMAGE_STR);
        this.mPSStream.println("[" + f4 + " 0 0 " + f5 + " " + f2 + " " + f3 + "]concat");
        this.mPSStream.println(i5 + " " + i6 + " 8[" + i5 + " 0 0 " + i6 + " 0 0]/imageSrc load false 3 colorimage");
        byte[] bArr2 = new byte[i5 * 3];
        try {
            int iSwapBGRtoRGB = ((int) f7) * i2;
            for (int i8 = 0; i8 < i6; i8++) {
                iSwapBGRtoRGB = swapBGRtoRGB(bArr, iSwapBGRtoRGB + ((int) f6), bArr2);
                this.mPSStream.write(ascii85Encode(rlEncode(bArr2)));
                this.mPSStream.println("");
            }
        } catch (IOException e2) {
        }
        this.mPSStream.println(IMAGE_RESTORE);
    }

    @Override // sun.print.RasterPrinterJob
    protected void printBand(byte[] bArr, int i2, int i3, int i4, int i5) throws PrinterException {
        int i6;
        this.mPSStream.println(IMAGE_SAVE);
        int i7 = 3 * i4;
        while (true) {
            i6 = i7;
            if (i6 <= 65535) {
                break;
            } else {
                i7 = i6 / 2;
            }
        }
        this.mPSStream.println(i6 + IMAGE_STR);
        this.mPSStream.println("[" + i4 + " 0 0 " + i5 + " " + i2 + " " + i3 + "]concat");
        this.mPSStream.println(i4 + " " + i5 + " 8[" + i4 + " 0 0 " + (-i5) + " 0 " + i5 + "]/imageSrc load false 3 colorimage");
        int iSwapBGRtoRGB = 0;
        byte[] bArr2 = new byte[i4 * 3];
        for (int i8 = 0; i8 < i5; i8++) {
            try {
                iSwapBGRtoRGB = swapBGRtoRGB(bArr, iSwapBGRtoRGB, bArr2);
                this.mPSStream.write(ascii85Encode(rlEncode(bArr2)));
                this.mPSStream.println("");
            } catch (IOException e2) {
                throw new PrinterIOException(e2);
            }
        }
        this.mPSStream.println(IMAGE_RESTORE);
    }

    @Override // sun.print.RasterPrinterJob
    protected Graphics2D createPathGraphics(PeekGraphics peekGraphics, PrinterJob printerJob, Printable printable, PageFormat pageFormat, int i2) {
        PSPathGraphics pSPathGraphics;
        PeekMetrics metrics = peekGraphics.getMetrics();
        if (!forcePDL && (forceRaster || metrics.hasNonSolidColors() || metrics.hasCompositing())) {
            pSPathGraphics = null;
        } else {
            pSPathGraphics = new PSPathGraphics(new BufferedImage(8, 8, 1).createGraphics(), printerJob, printable, pageFormat, i2, !peekGraphics.getAWTDrawingOnly());
        }
        return pSPathGraphics;
    }

    protected void selectClipPath() {
        this.mPSStream.println(this.mClipOpStr);
    }

    protected void setClip(Shape shape) {
        this.mLastClip = shape;
    }

    protected void setTransform(AffineTransform affineTransform) {
        this.mLastTransform = affineTransform;
    }

    protected boolean setFont(Font font) {
        this.mLastFont = font;
        return true;
    }

    private int[] getPSFontIndexArray(Font font, CharsetString[] charsetStringArr) {
        String strMakeCharsetName;
        int[] iArr = null;
        if (mFontProps != null) {
            iArr = new int[charsetStringArr.length];
        }
        for (int i2 = 0; i2 < charsetStringArr.length && iArr != null; i2++) {
            CharsetString charsetString = charsetStringArr[i2];
            CharsetEncoder charsetEncoder = charsetString.fontDescriptor.encoder;
            String fontCharsetName = charsetString.fontDescriptor.getFontCharsetName();
            if ("Symbol".equals(fontCharsetName)) {
                strMakeCharsetName = "symbol";
            } else if ("WingDings".equals(fontCharsetName) || "X11Dingbats".equals(fontCharsetName)) {
                strMakeCharsetName = "dingbats";
            } else {
                strMakeCharsetName = makeCharsetName(fontCharsetName, charsetString.charsetChars);
            }
            String property = mFontProps.getProperty(mFontProps.getProperty(font.getFamily().toLowerCase(Locale.ENGLISH).replace(' ', '_'), "") + "." + strMakeCharsetName + "." + FontConfiguration.getStyleString(font.getStyle() | FontUtilities.getFont2D(font).getStyle()), null);
            if (property != null) {
                try {
                    iArr[i2] = Integer.parseInt(mFontProps.getProperty(property));
                } catch (NumberFormatException e2) {
                    iArr = null;
                }
            } else {
                iArr = null;
            }
        }
        return iArr;
    }

    private static String escapeParens(String str) {
        if (str.indexOf(40) == -1 && str.indexOf(41) == -1) {
            return str;
        }
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int iIndexOf = str.indexOf(40, i3);
            if (iIndexOf == -1) {
                break;
            }
            i2++;
            i3 = iIndexOf + 1;
        }
        int i4 = 0;
        while (true) {
            int iIndexOf2 = str.indexOf(41, i4);
            if (iIndexOf2 == -1) {
                break;
            }
            i2++;
            i4 = iIndexOf2 + 1;
        }
        char[] charArray = str.toCharArray();
        char[] cArr = new char[charArray.length + i2];
        int i5 = 0;
        for (int i6 = 0; i6 < charArray.length; i6++) {
            if (charArray[i6] == '(' || charArray[i6] == ')') {
                int i7 = i5;
                i5++;
                cArr[i7] = '\\';
            }
            int i8 = i5;
            i5++;
            cArr[i8] = charArray[i6];
        }
        return new String(cArr);
    }

    protected int platformFontCount(Font font, String str) {
        CharsetString[] charsetStringArrMakeMultiCharsetString;
        int[] pSFontIndexArray;
        if (mFontProps == null || (charsetStringArrMakeMultiCharsetString = ((PlatformFont) font.getPeer()).makeMultiCharsetString(str, false)) == null || (pSFontIndexArray = getPSFontIndexArray(font, charsetStringArrMakeMultiCharsetString)) == null) {
            return 0;
        }
        return pSFontIndexArray.length;
    }

    protected boolean textOut(Graphics graphics, String str, float f2, float f3, Font font, FontRenderContext fontRenderContext, float f4) {
        int iLimit;
        float width;
        boolean z2 = true;
        if (mFontProps == null) {
            return false;
        }
        prepDrawing();
        String strRemoveControlChars = removeControlChars(str);
        if (strRemoveControlChars.length() == 0) {
            return true;
        }
        CharsetString[] charsetStringArrMakeMultiCharsetString = ((PlatformFont) font.getPeer()).makeMultiCharsetString(strRemoveControlChars, false);
        if (charsetStringArrMakeMultiCharsetString == null) {
            return false;
        }
        int[] pSFontIndexArray = getPSFontIndexArray(font, charsetStringArrMakeMultiCharsetString);
        if (pSFontIndexArray != null) {
            for (int i2 = 0; i2 < charsetStringArrMakeMultiCharsetString.length; i2++) {
                CharsetString charsetString = charsetStringArrMakeMultiCharsetString[i2];
                CharsetEncoder charsetEncoder = charsetString.fontDescriptor.encoder;
                StringBuffer stringBuffer = new StringBuffer();
                byte[] bArr = new byte[charsetString.length * 2];
                try {
                    ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
                    charsetEncoder.encode(CharBuffer.wrap(charsetString.charsetChars, charsetString.offset, charsetString.length), byteBufferWrap, true);
                    byteBufferWrap.flip();
                    iLimit = byteBufferWrap.limit();
                    if (charsetStringArrMakeMultiCharsetString.length == 1 && f4 != 0.0f) {
                        width = f4;
                    } else {
                        width = (float) font.getStringBounds(charsetString.charsetChars, charsetString.offset, charsetString.offset + charsetString.length, fontRenderContext).getWidth();
                    }
                } catch (IllegalStateException e2) {
                } catch (CoderMalfunctionError e3) {
                }
                if (width == 0.0f) {
                    return true;
                }
                stringBuffer.append('<');
                for (int i3 = 0; i3 < iLimit; i3++) {
                    String hexString = Integer.toHexString(bArr[i3]);
                    int length = hexString.length();
                    if (length > 2) {
                        hexString = hexString.substring(length - 2, length);
                    } else if (length == 1) {
                        hexString = "0" + hexString;
                    } else if (length == 0) {
                        hexString = "00";
                    }
                    stringBuffer.append(hexString);
                }
                stringBuffer.append('>');
                getGState().emitPSFont(pSFontIndexArray[i2], font.getSize2D());
                this.mPSStream.println(stringBuffer.toString() + " " + width + " " + f2 + " " + f3 + " S");
                f2 += width;
            }
        } else {
            z2 = false;
        }
        return z2;
    }

    protected void setFillMode(int i2) {
        switch (i2) {
            case 1:
                this.mFillOpStr = EVEN_ODD_FILL_STR;
                this.mClipOpStr = EVEN_ODD_CLIP_STR;
                return;
            case 2:
                this.mFillOpStr = WINDING_FILL_STR;
                this.mClipOpStr = WINDING_CLIP_STR;
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    protected void setColor(Color color) {
        this.mLastColor = color;
    }

    protected void fillPath() {
        this.mPSStream.println(this.mFillOpStr);
    }

    protected void beginPath() {
        prepDrawing();
        this.mPSStream.println(NEWPATH_STR);
        this.mPenX = 0.0f;
        this.mPenY = 0.0f;
    }

    protected void closeSubpath() {
        this.mPSStream.println("P");
        this.mPenX = this.mStartPathX;
        this.mPenY = this.mStartPathY;
    }

    protected void moveTo(float f2, float f3) {
        this.mPSStream.println(trunc(f2) + " " + trunc(f3) + MOVETO_STR);
        this.mStartPathX = f2;
        this.mStartPathY = f3;
        this.mPenX = f2;
        this.mPenY = f3;
    }

    protected void lineTo(float f2, float f3) {
        this.mPSStream.println(trunc(f2) + " " + trunc(f3) + LINETO_STR);
        this.mPenX = f2;
        this.mPenY = f3;
    }

    protected void bezierTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.mPSStream.println(trunc(f2) + " " + trunc(f3) + " " + trunc(f4) + " " + trunc(f5) + " " + trunc(f6) + " " + trunc(f7) + CURVETO_STR);
        this.mPenX = f6;
        this.mPenY = f7;
    }

    String trunc(float f2) {
        float fAbs = Math.abs(f2);
        if (fAbs >= 1.0f && fAbs <= 1000.0f) {
            f2 = Math.round(f2 * 1000.0f) / 1000.0f;
        }
        return Float.toString(f2);
    }

    protected float getPenX() {
        return this.mPenX;
    }

    protected float getPenY() {
        return this.mPenY;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getXRes() {
        return 300.0d;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getYRes() {
        return 300.0d;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPrintableX(Paper paper) {
        return 0.0d;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPrintableY(Paper paper) {
        return 0.0d;
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPrintableWidth(Paper paper) {
        return paper.getImageableWidth();
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPrintableHeight(Paper paper) {
        return paper.getImageableHeight();
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPageWidth(Paper paper) {
        return paper.getWidth();
    }

    @Override // sun.print.RasterPrinterJob
    protected double getPhysicalPageHeight(Paper paper) {
        return paper.getHeight();
    }

    @Override // sun.print.RasterPrinterJob
    protected int getNoncollatedCopies() {
        return 1;
    }

    @Override // sun.print.RasterPrinterJob
    protected int getCollatedCopies() {
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] printExecCmd(String str, String str2, boolean z2, String str3, int i2, String str4) {
        String[] strArr;
        int i3;
        boolean z3 = false;
        int i4 = 2;
        boolean z4 = z3;
        if (str != null) {
            z4 = z3;
            if (!str.equals("")) {
                z4 = z3;
                if (!str.equals("lp")) {
                    z4 = false | true;
                    i4 = 2 + 1;
                }
            }
        }
        boolean z5 = z4;
        if (str2 != null) {
            z5 = z4;
            if (!str2.equals("")) {
                z5 = ((z4 ? 1 : 0) | 2) == true ? 1 : 0;
                i4++;
            }
        }
        boolean z6 = z5;
        if (str3 != null) {
            z6 = z5;
            if (!str3.equals("")) {
                z6 = ((z5 ? 1 : 0) | 4) == true ? 1 : 0;
                i4++;
            }
        }
        boolean z7 = z6;
        if (i2 > 1) {
            z7 = ((z6 ? 1 : 0) | 8) == true ? 1 : 0;
            i4++;
        }
        boolean z8 = z7;
        if (z2) {
            z8 = ((z7 ? 1 : 0) | 16) == true ? 1 : 0;
            i4++;
        }
        String property = System.getProperty("os.name");
        if (property.equals("Linux") || property.contains("OS X")) {
            strArr = new String[i4];
            i3 = 0 + 1;
            strArr[0] = "/usr/bin/lpr";
            if (z8 & true) {
                i3++;
                strArr[i3] = "-P" + str;
            }
            if (((z8 ? 1 : 0) & 4) != 0) {
                int i5 = i3;
                i3++;
                strArr[i5] = "-J" + str3;
            }
            if (((z8 ? 1 : 0) & 8) != 0) {
                int i6 = i3;
                i3++;
                strArr[i6] = "-#" + i2;
            }
            if (((z8 ? 1 : 0) & 16) != 0) {
                int i7 = i3;
                i3++;
                strArr[i7] = "-h";
            }
            if (((z8 ? 1 : 0) & 2) != 0) {
                int i8 = i3;
                i3++;
                strArr[i8] = new String(str2);
            }
        } else {
            strArr = new String[i4 + 1];
            int i9 = 0 + 1;
            strArr[0] = "/usr/bin/lp";
            i3 = i9 + 1;
            strArr[i9] = "-c";
            if (z8 & true) {
                i3++;
                strArr[i3] = "-d" + str;
            }
            if (((z8 ? 1 : 0) & 4) != 0) {
                int i10 = i3;
                i3++;
                strArr[i10] = "-t" + str3;
            }
            if (((z8 ? 1 : 0) & 8) != 0) {
                int i11 = i3;
                i3++;
                strArr[i11] = "-n" + i2;
            }
            if (((z8 ? 1 : 0) & 16) != 0) {
                int i12 = i3;
                i3++;
                strArr[i12] = "-o nobanner";
            }
            if (((z8 ? 1 : 0) & 2) != 0) {
                int i13 = i3;
                i3++;
                strArr[i13] = "-o" + str2;
            }
        }
        int i14 = i3;
        int i15 = i3 + 1;
        strArr[i14] = str4;
        return strArr;
    }

    private static int swapBGRtoRGB(byte[] bArr, int i2, byte[] bArr2) {
        int i3 = 0;
        while (i2 < bArr.length - 2 && i3 < bArr2.length - 2) {
            int i4 = i3;
            int i5 = i3 + 1;
            bArr2[i4] = bArr[i2 + 2];
            int i6 = i5 + 1;
            bArr2[i5] = bArr[i2 + 1];
            i3 = i6 + 1;
            bArr2[i6] = bArr[i2 + 0];
            i2 += 3;
        }
        return i2;
    }

    private String makeCharsetName(String str, char[] cArr) {
        if (str.equals("Cp1252") || str.equals("ISO8859_1")) {
            return "latin1";
        }
        if (str.equals(InternalZipConstants.CHARSET_UTF8)) {
            for (char c2 : cArr) {
                if (c2 > 255) {
                    return str.toLowerCase();
                }
            }
            return "latin1";
        }
        if (str.startsWith("ISO8859")) {
            for (char c3 : cArr) {
                if (c3 > 127) {
                    return str.toLowerCase();
                }
            }
            return "latin1";
        }
        return str.toLowerCase();
    }

    private void prepDrawing() {
        while (!isOuterGState() && (!getGState().canSetClip(this.mLastClip) || !getGState().mTransform.equals(this.mLastTransform))) {
            grestore();
        }
        getGState().emitPSColor(this.mLastColor);
        if (isOuterGState()) {
            gsave();
            getGState().emitTransform(this.mLastTransform);
            getGState().emitPSClip(this.mLastClip);
        }
    }

    private GState getGState() {
        return (GState) this.mGStateStack.get(this.mGStateStack.size() - 1);
    }

    private void gsave() {
        this.mGStateStack.add(new GState(getGState()));
        this.mPSStream.println("G");
    }

    private void grestore() {
        this.mGStateStack.remove(this.mGStateStack.size() - 1);
        this.mPSStream.println(GRESTORE_STR);
    }

    private boolean isOuterGState() {
        return this.mGStateStack.size() == 1;
    }

    /* loaded from: rt.jar:sun/print/PSPrinterJob$GState.class */
    private class GState {
        Color mColor;
        Shape mClip;
        Font mFont;
        AffineTransform mTransform;

        GState() {
            this.mColor = Color.black;
            this.mClip = null;
            this.mFont = null;
            this.mTransform = new AffineTransform();
        }

        GState(GState gState) {
            this.mColor = gState.mColor;
            this.mClip = gState.mClip;
            this.mFont = gState.mFont;
            this.mTransform = gState.mTransform;
        }

        boolean canSetClip(Shape shape) {
            return this.mClip == null || this.mClip.equals(shape);
        }

        void emitPSClip(Shape shape) {
            if (shape != null) {
                if (this.mClip == null || !this.mClip.equals(shape)) {
                    String str = PSPrinterJob.this.mFillOpStr;
                    String str2 = PSPrinterJob.this.mClipOpStr;
                    PSPrinterJob.this.convertToPSPath(shape.getPathIterator(new AffineTransform()));
                    PSPrinterJob.this.selectClipPath();
                    this.mClip = shape;
                    PSPrinterJob.this.mClipOpStr = str;
                    PSPrinterJob.this.mFillOpStr = str;
                }
            }
        }

        void emitTransform(AffineTransform affineTransform) {
            if (affineTransform != null && !affineTransform.equals(this.mTransform)) {
                double[] dArr = new double[6];
                affineTransform.getMatrix(dArr);
                PSPrinterJob.this.mPSStream.println("[" + ((float) dArr[0]) + " " + ((float) dArr[1]) + " " + ((float) dArr[2]) + " " + ((float) dArr[3]) + " " + ((float) dArr[4]) + " " + ((float) dArr[5]) + "] concat");
                this.mTransform = affineTransform;
            }
        }

        void emitPSColor(Color color) {
            if (color != null && !color.equals(this.mColor)) {
                float[] rGBColorComponents = color.getRGBColorComponents(null);
                if (rGBColorComponents[0] == rGBColorComponents[1] && rGBColorComponents[1] == rGBColorComponents[2]) {
                    PSPrinterJob.this.mPSStream.println(rGBColorComponents[0] + PSPrinterJob.SETGRAY_STR);
                } else {
                    PSPrinterJob.this.mPSStream.println(rGBColorComponents[0] + " " + rGBColorComponents[1] + " " + rGBColorComponents[2] + " " + PSPrinterJob.SETRGBCOLOR_STR);
                }
                this.mColor = color;
            }
        }

        void emitPSFont(int i2, float f2) {
            PSPrinterJob.this.mPSStream.println(f2 + " " + i2 + " F");
        }
    }

    void convertToPSPath(PathIterator pathIterator) {
        int i2;
        float[] fArr = new float[6];
        if (pathIterator.getWindingRule() == 0) {
            i2 = 1;
        } else {
            i2 = 2;
        }
        beginPath();
        setFillMode(i2);
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(fArr)) {
                case 0:
                    moveTo(fArr[0], fArr[1]);
                    break;
                case 1:
                    lineTo(fArr[0], fArr[1]);
                    break;
                case 2:
                    float penX = getPenX();
                    float penY = getPenY();
                    bezierTo(penX + (((fArr[0] - penX) * 2.0f) / 3.0f), penY + (((fArr[1] - penY) * 2.0f) / 3.0f), fArr[2] - (((fArr[2] - fArr[0]) * 2.0f) / 3.0f), fArr[3] - (((fArr[3] - fArr[1]) * 2.0f) / 3.0f), fArr[2], fArr[3]);
                    break;
                case 3:
                    bezierTo(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
                    break;
                case 4:
                    closeSubpath();
                    break;
            }
            pathIterator.next();
        }
    }

    protected void deviceFill(PathIterator pathIterator, Color color, AffineTransform affineTransform, Shape shape) {
        setTransform(affineTransform);
        setClip(shape);
        setColor(color);
        convertToPSPath(pathIterator);
        this.mPSStream.println("G");
        selectClipPath();
        fillPath();
        this.mPSStream.println("R N");
    }

    private byte[] rlEncode(byte[] bArr) {
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        byte[] bArr2 = new byte[(bArr.length * 2) + 2];
        while (i2 < bArr.length) {
            if (i5 == 0) {
                int i6 = i2;
                i2++;
                i4 = i6;
                i5 = 1;
            }
            while (i5 < 128 && i2 < bArr.length && bArr[i2] == bArr[i4]) {
                i5++;
                i2++;
            }
            if (i5 > 1) {
                int i7 = i3;
                int i8 = i3 + 1;
                bArr2[i7] = (byte) (257 - i5);
                i3 = i8 + 1;
                bArr2[i8] = bArr[i4];
                i5 = 0;
            } else {
                while (i5 < 128 && i2 < bArr.length && bArr[i2] != bArr[i2 - 1]) {
                    i5++;
                    i2++;
                }
                int i9 = i3;
                i3++;
                bArr2[i9] = (byte) (i5 - 1);
                for (int i10 = i4; i10 < i4 + i5; i10++) {
                    int i11 = i3;
                    i3++;
                    bArr2[i11] = bArr[i10];
                }
                i5 = 0;
            }
        }
        int i12 = i3;
        int i13 = i3 + 1;
        bArr2[i12] = Byte.MIN_VALUE;
        byte[] bArr3 = new byte[i13];
        System.arraycopy(bArr2, 0, bArr3, 0, i13);
        return bArr3;
    }

    private byte[] ascii85Encode(byte[] bArr) {
        long j2;
        byte[] bArr2 = new byte[(((bArr.length + 4) * 5) / 4) + 2];
        long j3 = 85 * 85;
        long j4 = 85 * j3;
        long j5 = 85 * j4;
        int i2 = 0;
        int i3 = 0;
        while (i2 + 3 < bArr.length) {
            int i4 = i2;
            long j6 = ((bArr[i4] & 255) << 24) + ((bArr[r19] & 255) << 16);
            long j7 = j6 + ((bArr[r19] & 255) << 8);
            i2 = i2 + 1 + 1 + 1 + 1;
            long j8 = j7 + (bArr[r19] & 255);
            if (j8 == 0) {
                int i5 = i3;
                i3++;
                bArr2[i5] = 122;
            } else {
                int i6 = i3;
                int i7 = i3 + 1;
                bArr2[i6] = (byte) ((j8 / j5) + 33);
                int i8 = i7 + 1;
                bArr2[i7] = (byte) ((r0 / j4) + 33);
                int i9 = i8 + 1;
                bArr2[i8] = (byte) ((r0 / j3) + 33);
                long j9 = ((j8 % j5) % j4) % j3;
                int i10 = i9 + 1;
                bArr2[i9] = (byte) ((j9 / 85) + 33);
                i3 = i10 + 1;
                bArr2[i10] = (byte) ((j9 % 85) + 33);
            }
        }
        if (i2 < bArr.length) {
            int length = bArr.length - i2;
            long j10 = 0;
            while (true) {
                j2 = j10;
                if (i2 >= bArr.length) {
                    break;
                }
                int i11 = i2;
                i2++;
                j10 = (j2 << 8) + (bArr[i11] & 255);
            }
            int i12 = 4 - length;
            while (true) {
                int i13 = i12;
                i12--;
                if (i13 <= 0) {
                    break;
                }
                j2 <<= 8;
            }
            long j11 = ((j2 % j5) % j4) % j3;
            byte[] bArr3 = {(byte) ((r0 / j5) + 33), (byte) ((r0 / j4) + 33), (byte) ((r0 / j3) + 33), (byte) ((j11 / 85) + 33), (byte) ((j11 % 85) + 33)};
            for (int i14 = 0; i14 < length + 1; i14++) {
                int i15 = i3;
                i3++;
                bArr2[i15] = bArr3[i14];
            }
        }
        int i16 = i3;
        int i17 = i3 + 1;
        bArr2[i16] = 126;
        int i18 = i17 + 1;
        bArr2[i17] = 62;
        byte[] bArr4 = new byte[i18];
        System.arraycopy(bArr2, 0, bArr4, 0, i18);
        return bArr4;
    }

    /* loaded from: rt.jar:sun/print/PSPrinterJob$PluginPrinter.class */
    public static class PluginPrinter implements Printable {
        private EPSPrinter epsPrinter;
        private Component applet;
        private PrintStream stream;
        private String epsTitle = "Java Plugin Applet";

        /* renamed from: bx, reason: collision with root package name */
        private int f13596bx;

        /* renamed from: by, reason: collision with root package name */
        private int f13597by;

        /* renamed from: bw, reason: collision with root package name */
        private int f13598bw;

        /* renamed from: bh, reason: collision with root package name */
        private int f13599bh;
        private int width;
        private int height;

        public PluginPrinter(Component component, PrintStream printStream, int i2, int i3, int i4, int i5) {
            this.applet = component;
            this.stream = printStream;
            this.f13596bx = i2;
            this.f13597by = i3;
            this.f13598bw = i4;
            this.f13599bh = i5;
            this.width = component.size().width;
            this.height = component.size().height;
            this.epsPrinter = new EPSPrinter(this, this.epsTitle, printStream, 0, 0, this.width, this.height);
        }

        public void printPluginPSHeader() {
            this.stream.println("%%BeginDocument: JavaPluginApplet");
        }

        public void printPluginApplet() throws IndexOutOfBoundsException, NumberFormatException {
            try {
                this.epsPrinter.print();
            } catch (PrinterException e2) {
            }
        }

        public void printPluginPSTrailer() {
            this.stream.println("%%EndDocument: JavaPluginApplet");
            this.stream.flush();
        }

        public void printAll() throws IndexOutOfBoundsException, NumberFormatException {
            printPluginPSHeader();
            printPluginApplet();
            printPluginPSTrailer();
        }

        @Override // java.awt.print.Printable
        public int print(Graphics graphics, PageFormat pageFormat, int i2) {
            if (i2 > 0) {
                return 1;
            }
            this.applet.printAll(graphics);
            return 0;
        }
    }

    /* loaded from: rt.jar:sun/print/PSPrinterJob$EPSPrinter.class */
    public static class EPSPrinter implements Pageable {
        private PageFormat pf;
        private PSPrinterJob job;
        private int llx;
        private int lly;
        private int urx;
        private int ury;
        private Printable printable;
        private PrintStream stream;
        private String epsTitle;

        public EPSPrinter(Printable printable, String str, PrintStream printStream, int i2, int i3, int i4, int i5) {
            this.printable = printable;
            this.epsTitle = str;
            this.stream = printStream;
            this.llx = i2;
            this.lly = i3;
            this.urx = this.llx + i4;
            this.ury = this.lly + i5;
            Paper paper = new Paper();
            paper.setSize(i4, i5);
            paper.setImageableArea(0.0d, 0.0d, i4, i5);
            this.pf = new PageFormat();
            this.pf.setPaper(paper);
        }

        public void print() throws IndexOutOfBoundsException, PrinterException, NumberFormatException {
            this.stream.println("%!PS-Adobe-3.0 EPSF-3.0");
            this.stream.println("%%BoundingBox: " + this.llx + " " + this.lly + " " + this.urx + " " + this.ury);
            this.stream.println("%%Title: " + this.epsTitle);
            this.stream.println("%%Creator: Java Printing");
            this.stream.println("%%CreationDate: " + ((Object) new Date()));
            this.stream.println("%%EndComments");
            this.stream.println("/pluginSave save def");
            this.stream.println("mark");
            this.job = new PSPrinterJob();
            this.job.epsPrinter = this;
            this.job.mPSStream = this.stream;
            this.job.mDestType = 2;
            this.job.startDoc();
            try {
                try {
                    this.job.printPage(this, 0);
                    this.stream.println("cleartomark");
                    this.stream.println("pluginSave restore");
                    this.job.endDoc();
                    this.stream.flush();
                } catch (Throwable th) {
                    if (th instanceof PrinterException) {
                        throw ((PrinterException) th);
                    }
                    throw new PrinterException(th.toString());
                }
            } catch (Throwable th2) {
                this.stream.println("cleartomark");
                this.stream.println("pluginSave restore");
                this.job.endDoc();
                throw th2;
            }
        }

        @Override // java.awt.print.Pageable
        public int getNumberOfPages() {
            return 1;
        }

        @Override // java.awt.print.Pageable
        public PageFormat getPageFormat(int i2) {
            if (i2 > 0) {
                throw new IndexOutOfBoundsException("pgIndex");
            }
            return this.pf;
        }

        @Override // java.awt.print.Pageable
        public Printable getPrintable(int i2) {
            if (i2 > 0) {
                throw new IndexOutOfBoundsException("pgIndex");
            }
            return this.printable;
        }
    }
}
