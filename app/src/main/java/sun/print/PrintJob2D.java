package sun.print;

import com.sun.glass.events.DndEvent;
import com.sun.glass.events.ViewEvent;
import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.SocksConsts;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Properties;
import javax.print.PrintService;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;
import org.apache.commons.net.nntp.NNTPReply;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.telnet.TelnetCommand;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/print/PrintJob2D.class */
public class PrintJob2D extends PrintJob implements Printable, Runnable {
    private static final PageAttributes.MediaType[] SIZES = {PageAttributes.MediaType.ISO_4A0, PageAttributes.MediaType.ISO_2A0, PageAttributes.MediaType.ISO_A0, PageAttributes.MediaType.ISO_A1, PageAttributes.MediaType.ISO_A2, PageAttributes.MediaType.ISO_A3, PageAttributes.MediaType.ISO_A4, PageAttributes.MediaType.ISO_A5, PageAttributes.MediaType.ISO_A6, PageAttributes.MediaType.ISO_A7, PageAttributes.MediaType.ISO_A8, PageAttributes.MediaType.ISO_A9, PageAttributes.MediaType.ISO_A10, PageAttributes.MediaType.ISO_B0, PageAttributes.MediaType.ISO_B1, PageAttributes.MediaType.ISO_B2, PageAttributes.MediaType.ISO_B3, PageAttributes.MediaType.ISO_B4, PageAttributes.MediaType.ISO_B5, PageAttributes.MediaType.ISO_B6, PageAttributes.MediaType.ISO_B7, PageAttributes.MediaType.ISO_B8, PageAttributes.MediaType.ISO_B9, PageAttributes.MediaType.ISO_B10, PageAttributes.MediaType.JIS_B0, PageAttributes.MediaType.JIS_B1, PageAttributes.MediaType.JIS_B2, PageAttributes.MediaType.JIS_B3, PageAttributes.MediaType.JIS_B4, PageAttributes.MediaType.JIS_B5, PageAttributes.MediaType.JIS_B6, PageAttributes.MediaType.JIS_B7, PageAttributes.MediaType.JIS_B8, PageAttributes.MediaType.JIS_B9, PageAttributes.MediaType.JIS_B10, PageAttributes.MediaType.ISO_C0, PageAttributes.MediaType.ISO_C1, PageAttributes.MediaType.ISO_C2, PageAttributes.MediaType.ISO_C3, PageAttributes.MediaType.ISO_C4, PageAttributes.MediaType.ISO_C5, PageAttributes.MediaType.ISO_C6, PageAttributes.MediaType.ISO_C7, PageAttributes.MediaType.ISO_C8, PageAttributes.MediaType.ISO_C9, PageAttributes.MediaType.ISO_C10, PageAttributes.MediaType.ISO_DESIGNATED_LONG, PageAttributes.MediaType.EXECUTIVE, PageAttributes.MediaType.FOLIO, PageAttributes.MediaType.INVOICE, PageAttributes.MediaType.LEDGER, PageAttributes.MediaType.NA_LETTER, PageAttributes.MediaType.NA_LEGAL, PageAttributes.MediaType.QUARTO, PageAttributes.MediaType.f12365A, PageAttributes.MediaType.f12366B, PageAttributes.MediaType.f12367C, PageAttributes.MediaType.f12368D, PageAttributes.MediaType.f12369E, PageAttributes.MediaType.NA_10X15_ENVELOPE, PageAttributes.MediaType.NA_10X14_ENVELOPE, PageAttributes.MediaType.NA_10X13_ENVELOPE, PageAttributes.MediaType.NA_9X12_ENVELOPE, PageAttributes.MediaType.NA_9X11_ENVELOPE, PageAttributes.MediaType.NA_7X9_ENVELOPE, PageAttributes.MediaType.NA_6X9_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_9_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_10_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_11_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_12_ENVELOPE, PageAttributes.MediaType.NA_NUMBER_14_ENVELOPE, PageAttributes.MediaType.INVITE_ENVELOPE, PageAttributes.MediaType.ITALY_ENVELOPE, PageAttributes.MediaType.MONARCH_ENVELOPE, PageAttributes.MediaType.PERSONAL_ENVELOPE};
    private static final MediaSizeName[] JAVAXSIZES = {null, null, MediaSizeName.ISO_A0, MediaSizeName.ISO_A1, MediaSizeName.ISO_A2, MediaSizeName.ISO_A3, MediaSizeName.ISO_A4, MediaSizeName.ISO_A5, MediaSizeName.ISO_A6, MediaSizeName.ISO_A7, MediaSizeName.ISO_A8, MediaSizeName.ISO_A9, MediaSizeName.ISO_A10, MediaSizeName.ISO_B0, MediaSizeName.ISO_B1, MediaSizeName.ISO_B2, MediaSizeName.ISO_B3, MediaSizeName.ISO_B4, MediaSizeName.ISO_B5, MediaSizeName.ISO_B6, MediaSizeName.ISO_B7, MediaSizeName.ISO_B8, MediaSizeName.ISO_B9, MediaSizeName.ISO_B10, MediaSizeName.JIS_B0, MediaSizeName.JIS_B1, MediaSizeName.JIS_B2, MediaSizeName.JIS_B3, MediaSizeName.JIS_B4, MediaSizeName.JIS_B5, MediaSizeName.JIS_B6, MediaSizeName.JIS_B7, MediaSizeName.JIS_B8, MediaSizeName.JIS_B9, MediaSizeName.JIS_B10, MediaSizeName.ISO_C0, MediaSizeName.ISO_C1, MediaSizeName.ISO_C2, MediaSizeName.ISO_C3, MediaSizeName.ISO_C4, MediaSizeName.ISO_C5, MediaSizeName.ISO_C6, null, null, null, null, MediaSizeName.ISO_DESIGNATED_LONG, MediaSizeName.EXECUTIVE, MediaSizeName.FOLIO, MediaSizeName.INVOICE, MediaSizeName.LEDGER, MediaSizeName.NA_LETTER, MediaSizeName.NA_LEGAL, MediaSizeName.QUARTO, MediaSizeName.f12792A, MediaSizeName.f12793B, MediaSizeName.f12794C, MediaSizeName.f12795D, MediaSizeName.f12796E, MediaSizeName.NA_10X15_ENVELOPE, MediaSizeName.NA_10X14_ENVELOPE, MediaSizeName.NA_10X13_ENVELOPE, MediaSizeName.NA_9X12_ENVELOPE, MediaSizeName.NA_9X11_ENVELOPE, MediaSizeName.NA_7X9_ENVELOPE, MediaSizeName.NA_6X9_ENVELOPE, MediaSizeName.NA_NUMBER_9_ENVELOPE, MediaSizeName.NA_NUMBER_10_ENVELOPE, MediaSizeName.NA_NUMBER_11_ENVELOPE, MediaSizeName.NA_NUMBER_12_ENVELOPE, MediaSizeName.NA_NUMBER_14_ENVELOPE, null, MediaSizeName.ITALY_ENVELOPE, MediaSizeName.MONARCH_ENVELOPE, MediaSizeName.PERSONAL_ENVELOPE};
    private static final int[] WIDTHS = {4768, 3370, 2384, 1684, 1191, 842, 595, NNTPReply.NO_CURRENT_ARTICLE_SELECTED, 298, 210, 147, 105, 74, 2835, Types.BLOB, 1417, 1001, 709, 499, SMTPReply.START_MAIL_INPUT, TelnetCommand.GA, 176, 125, 88, 2920, 2064, 1460, OpCodes.NODETYPE_PI, 729, 516, 363, 258, 181, 128, 91, 2599, 1837, 1298, 918, 649, 459, 323, 230, 162, 113, 79, 312, 522, DndEvent.UPDATE, 396, 792, DndEvent.UPDATE, DndEvent.UPDATE, 609, DndEvent.UPDATE, 792, 1224, 1584, 2448, 720, 720, 720, 648, 648, 504, ViewEvent.FULLSCREEN_EXIT, 279, 297, 324, 342, 360, 624, 312, 279, 261};
    private static final int[] LENGTHS = {6741, 4768, 3370, 2384, 1684, 1191, 842, 595, NNTPReply.NO_CURRENT_ARTICLE_SELECTED, 298, 210, 147, 105, 4008, 2835, Types.BLOB, 1417, 1001, 729, 499, SMTPReply.START_MAIL_INPUT, TelnetCommand.GA, 176, 125, 4127, 2920, 2064, 1460, OpCodes.NODETYPE_PI, 729, 516, 363, 258, 181, 128, 3677, 2599, 1837, 1298, 918, 649, 459, 323, 230, 162, 113, 624, 756, 936, DndEvent.UPDATE, 1224, 792, 1008, 780, 792, 1224, 1584, 2448, 3168, SocksConsts.DEFAULT_PORT, 1008, 936, 864, 792, 648, 648, 639, 684, 747, 792, 828, 624, 652, 540, 468};
    private Frame frame;
    private String docTitle;
    private JobAttributes jobAttributes;
    private PageAttributes pageAttributes;
    private PrintRequestAttributeSet attributes;
    private PrinterJob printerJob;
    private PageFormat pageFormat;
    private MessageQ graphicsToBeDrawn;
    private MessageQ graphicsDrawn;
    private Graphics2D currentGraphics;
    private int pageIndex;
    private static final String DEST_PROP = "awt.print.destination";
    private static final String PRINTER = "printer";
    private static final String FILE = "file";
    private static final String PRINTER_PROP = "awt.print.printer";
    private static final String FILENAME_PROP = "awt.print.fileName";
    private static final String NUMCOPIES_PROP = "awt.print.numCopies";
    private static final String OPTIONS_PROP = "awt.print.options";
    private static final String ORIENT_PROP = "awt.print.orientation";
    private static final String PORTRAIT = "portrait";
    private static final String LANDSCAPE = "landscape";
    private static final String PAPERSIZE_PROP = "awt.print.paperSize";
    private static final String LETTER = "letter";
    private static final String LEGAL = "legal";
    private static final String EXECUTIVE = "executive";
    private static final String A4 = "a4";
    private Properties props;
    private String options;
    private Thread printerJobThread;

    public PrintJob2D(Frame frame, String str, Properties properties) {
        this.docTitle = "";
        this.graphicsToBeDrawn = new MessageQ("tobedrawn");
        this.graphicsDrawn = new MessageQ("drawn");
        this.pageIndex = -1;
        this.options = "";
        this.props = properties;
        this.jobAttributes = new JobAttributes();
        this.pageAttributes = new PageAttributes();
        translateInputProps();
        initPrintJob2D(frame, str, this.jobAttributes, this.pageAttributes);
    }

    public PrintJob2D(Frame frame, String str, JobAttributes jobAttributes, PageAttributes pageAttributes) {
        this.docTitle = "";
        this.graphicsToBeDrawn = new MessageQ("tobedrawn");
        this.graphicsDrawn = new MessageQ("drawn");
        this.pageIndex = -1;
        this.options = "";
        initPrintJob2D(frame, str, jobAttributes, pageAttributes);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v17, types: [int[], int[][]] */
    private void initPrintJob2D(Frame frame, String str, JobAttributes jobAttributes, PageAttributes pageAttributes) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        if (frame == null && (jobAttributes == null || jobAttributes.getDialog() == JobAttributes.DialogType.NATIVE)) {
            throw new NullPointerException("Frame must not be null");
        }
        this.frame = frame;
        this.docTitle = str == null ? "" : str;
        this.jobAttributes = jobAttributes != null ? jobAttributes : new JobAttributes();
        this.pageAttributes = pageAttributes != null ? pageAttributes : new PageAttributes();
        int[][] pageRanges = this.jobAttributes.getPageRanges();
        int i2 = pageRanges[0][0];
        int i3 = pageRanges[pageRanges.length - 1][1];
        this.jobAttributes.setPageRanges(new int[]{new int[]{i2, i3}});
        this.jobAttributes.setToPage(i3);
        this.jobAttributes.setFromPage(i2);
        int[] printerResolution = this.pageAttributes.getPrinterResolution();
        if (printerResolution[0] != printerResolution[1]) {
            throw new IllegalArgumentException("Differing cross feed and feed resolutions not supported.");
        }
        if (this.jobAttributes.getDestination() == JobAttributes.DestinationType.FILE) {
            throwPrintToFile();
            String fileName = jobAttributes.getFileName();
            if (fileName != null && jobAttributes.getDialog() == JobAttributes.DialogType.NONE) {
                File file = new File(fileName);
                try {
                    if (file.createNewFile()) {
                        file.delete();
                    }
                } catch (IOException e2) {
                    throw new IllegalArgumentException("Cannot write to file:" + fileName);
                } catch (SecurityException e3) {
                }
                File parentFile = file.getParentFile();
                if (!file.exists() || (file.isFile() && file.canWrite())) {
                    if (parentFile != null) {
                        if (parentFile.exists() && (!parentFile.exists() || parentFile.canWrite())) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                throw new IllegalArgumentException("Cannot write to file:" + fileName);
            }
        }
    }

    public boolean printDialog() throws HeadlessException, IllegalArgumentException {
        boolean z2;
        this.printerJob = PrinterJob.getPrinterJob();
        if (this.printerJob == null) {
            return false;
        }
        JobAttributes.DialogType dialog = this.jobAttributes.getDialog();
        PrintService printService = this.printerJob.getPrintService();
        if (printService == null && dialog == JobAttributes.DialogType.NONE) {
            return false;
        }
        copyAttributes(printService);
        JobAttributes.DefaultSelectionType defaultSelection = this.jobAttributes.getDefaultSelection();
        if (defaultSelection == JobAttributes.DefaultSelectionType.RANGE) {
            this.attributes.add(SunPageSelection.RANGE);
        } else if (defaultSelection == JobAttributes.DefaultSelectionType.SELECTION) {
            this.attributes.add(SunPageSelection.SELECTION);
        } else {
            this.attributes.add(SunPageSelection.ALL);
        }
        if (this.frame != null) {
            this.attributes.add(new DialogOwner(this.frame));
        }
        if (dialog == JobAttributes.DialogType.NONE) {
            z2 = true;
        } else {
            if (dialog == JobAttributes.DialogType.NATIVE) {
                this.attributes.add(DialogTypeSelection.NATIVE);
            } else {
                this.attributes.add(DialogTypeSelection.COMMON);
            }
            boolean zPrintDialog = this.printerJob.printDialog(this.attributes);
            z2 = zPrintDialog;
            if (zPrintDialog) {
                if (printService == null && this.printerJob.getPrintService() == null) {
                    return false;
                }
                updateAttributes();
                translateOutputProps();
            }
        }
        if (z2) {
            JobName jobName = (JobName) this.attributes.get(JobName.class);
            if (jobName != null) {
                this.printerJob.setJobName(jobName.toString());
            }
            this.pageFormat = new PageFormat();
            Media media = (Media) this.attributes.get(Media.class);
            MediaSize mediaSizeForName = null;
            if (media != null && (media instanceof MediaSizeName)) {
                mediaSizeForName = MediaSize.getMediaSizeForName((MediaSizeName) media);
            }
            Paper paper = this.pageFormat.getPaper();
            if (mediaSizeForName != null) {
                paper.setSize(mediaSizeForName.getX(25400) * 72.0d, mediaSizeForName.getY(25400) * 72.0d);
            }
            if (this.pageAttributes.getOrigin() == PageAttributes.OriginType.PRINTABLE) {
                paper.setImageableArea(18.0d, 18.0d, paper.getWidth() - 36.0d, paper.getHeight() - 36.0d);
            } else {
                paper.setImageableArea(0.0d, 0.0d, paper.getWidth(), paper.getHeight());
            }
            this.pageFormat.setPaper(paper);
            OrientationRequested orientationRequested = (OrientationRequested) this.attributes.get(OrientationRequested.class);
            if (orientationRequested != null && orientationRequested == OrientationRequested.REVERSE_LANDSCAPE) {
                this.pageFormat.setOrientation(2);
            } else if (orientationRequested == OrientationRequested.LANDSCAPE) {
                this.pageFormat.setOrientation(0);
            } else {
                this.pageFormat.setOrientation(1);
            }
            this.printerJob.setPrintable(this, this.pageFormat);
        }
        return z2;
    }

    private void updateAttributes() {
        PageAttributes.MediaType mediaTypeUnMapMedia;
        this.jobAttributes.setCopies(((Copies) this.attributes.get(Copies.class)).getValue());
        SunPageSelection sunPageSelection = (SunPageSelection) this.attributes.get(SunPageSelection.class);
        if (sunPageSelection == SunPageSelection.RANGE) {
            this.jobAttributes.setDefaultSelection(JobAttributes.DefaultSelectionType.RANGE);
        } else if (sunPageSelection == SunPageSelection.SELECTION) {
            this.jobAttributes.setDefaultSelection(JobAttributes.DefaultSelectionType.SELECTION);
        } else {
            this.jobAttributes.setDefaultSelection(JobAttributes.DefaultSelectionType.ALL);
        }
        Destination destination = (Destination) this.attributes.get(Destination.class);
        if (destination != null) {
            this.jobAttributes.setDestination(JobAttributes.DestinationType.FILE);
            this.jobAttributes.setFileName(destination.getURI().getPath());
        } else {
            this.jobAttributes.setDestination(JobAttributes.DestinationType.PRINTER);
        }
        PrintService printService = this.printerJob.getPrintService();
        if (printService != null) {
            this.jobAttributes.setPrinter(printService.getName());
        }
        this.jobAttributes.setPageRanges(((PageRanges) this.attributes.get(PageRanges.class)).getMembers());
        if (((SheetCollate) this.attributes.get(SheetCollate.class)) == SheetCollate.COLLATED) {
            this.jobAttributes.setMultipleDocumentHandling(JobAttributes.MultipleDocumentHandlingType.SEPARATE_DOCUMENTS_COLLATED_COPIES);
        } else {
            this.jobAttributes.setMultipleDocumentHandling(JobAttributes.MultipleDocumentHandlingType.SEPARATE_DOCUMENTS_UNCOLLATED_COPIES);
        }
        Sides sides = (Sides) this.attributes.get(Sides.class);
        if (sides == Sides.TWO_SIDED_LONG_EDGE) {
            this.jobAttributes.setSides(JobAttributes.SidesType.TWO_SIDED_LONG_EDGE);
        } else if (sides == Sides.TWO_SIDED_SHORT_EDGE) {
            this.jobAttributes.setSides(JobAttributes.SidesType.TWO_SIDED_SHORT_EDGE);
        } else {
            this.jobAttributes.setSides(JobAttributes.SidesType.ONE_SIDED);
        }
        if (((Chromaticity) this.attributes.get(Chromaticity.class)) == Chromaticity.COLOR) {
            this.pageAttributes.setColor(PageAttributes.ColorType.COLOR);
        } else {
            this.pageAttributes.setColor(PageAttributes.ColorType.MONOCHROME);
        }
        if (((OrientationRequested) this.attributes.get(OrientationRequested.class)) == OrientationRequested.LANDSCAPE) {
            this.pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE);
        } else {
            this.pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT);
        }
        PrintQuality printQuality = (PrintQuality) this.attributes.get(PrintQuality.class);
        if (printQuality == PrintQuality.DRAFT) {
            this.pageAttributes.setPrintQuality(PageAttributes.PrintQualityType.DRAFT);
        } else if (printQuality == PrintQuality.HIGH) {
            this.pageAttributes.setPrintQuality(PageAttributes.PrintQualityType.HIGH);
        } else {
            this.pageAttributes.setPrintQuality(PageAttributes.PrintQualityType.NORMAL);
        }
        Media media = (Media) this.attributes.get(Media.class);
        if (media != null && (media instanceof MediaSizeName) && (mediaTypeUnMapMedia = unMapMedia((MediaSizeName) media)) != null) {
            this.pageAttributes.setMedia(mediaTypeUnMapMedia);
        }
        debugPrintAttributes(false, false);
    }

    private void debugPrintAttributes(boolean z2, boolean z3) {
        if (z2) {
            System.out.println("new Attributes\ncopies = " + this.jobAttributes.getCopies() + "\nselection = " + ((Object) this.jobAttributes.getDefaultSelection()) + "\ndest " + ((Object) this.jobAttributes.getDestination()) + "\nfile " + this.jobAttributes.getFileName() + "\nfromPage " + this.jobAttributes.getFromPage() + "\ntoPage " + this.jobAttributes.getToPage() + "\ncollation " + ((Object) this.jobAttributes.getMultipleDocumentHandling()) + "\nPrinter " + this.jobAttributes.getPrinter() + "\nSides2 " + ((Object) this.jobAttributes.getSides()));
        }
        if (z3) {
            System.out.println("new Attributes\ncolor = " + ((Object) this.pageAttributes.getColor()) + "\norientation = " + ((Object) this.pageAttributes.getOrientationRequested()) + "\nquality " + ((Object) this.pageAttributes.getPrintQuality()) + "\nMedia2 " + ((Object) this.pageAttributes.getMedia()));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0063, code lost:
    
        r6.printerJob.setPrintService(r0[r11]);
        r8 = r0[r11];
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void copyAttributes(javax.print.PrintService r7) throws java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instructions count: 755
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.print.PrintJob2D.copyAttributes(javax.print.PrintService):void");
    }

    @Override // java.awt.PrintJob
    public Graphics getGraphics() {
        ProxyPrintGraphics proxyPrintGraphics = null;
        synchronized (this) {
            this.pageIndex++;
            if (this.pageIndex == 0 && !this.graphicsToBeDrawn.isClosed()) {
                startPrinterJobThread();
            }
            notify();
        }
        if (this.currentGraphics != null) {
            this.graphicsDrawn.append(this.currentGraphics);
            this.currentGraphics = null;
        }
        this.currentGraphics = this.graphicsToBeDrawn.pop();
        if (this.currentGraphics instanceof PeekGraphics) {
            ((PeekGraphics) this.currentGraphics).setAWTDrawingOnly();
            this.graphicsDrawn.append(this.currentGraphics);
            this.currentGraphics = this.graphicsToBeDrawn.pop();
        }
        if (this.currentGraphics != null) {
            this.currentGraphics.translate(this.pageFormat.getImageableX(), this.pageFormat.getImageableY());
            double pageResolutionInternal = 72.0d / getPageResolutionInternal();
            this.currentGraphics.scale(pageResolutionInternal, pageResolutionInternal);
            proxyPrintGraphics = new ProxyPrintGraphics(this.currentGraphics.create(), this);
        }
        return proxyPrintGraphics;
    }

    @Override // java.awt.PrintJob
    public Dimension getPageDimension() {
        double width;
        double height;
        if (this.pageAttributes != null && this.pageAttributes.getOrigin() == PageAttributes.OriginType.PRINTABLE) {
            width = this.pageFormat.getImageableWidth();
            height = this.pageFormat.getImageableHeight();
        } else {
            width = this.pageFormat.getWidth();
            height = this.pageFormat.getHeight();
        }
        double pageResolutionInternal = getPageResolutionInternal() / 72.0d;
        return new Dimension((int) (width * pageResolutionInternal), (int) (height * pageResolutionInternal));
    }

    private double getPageResolutionInternal() {
        if (this.pageAttributes != null) {
            if (this.pageAttributes.getPrinterResolution()[2] == 3) {
                return r0[0];
            }
            return r0[0] * 2.54d;
        }
        return 72.0d;
    }

    @Override // java.awt.PrintJob
    public int getPageResolution() {
        return (int) getPageResolutionInternal();
    }

    @Override // java.awt.PrintJob
    public boolean lastPageFirst() {
        return false;
    }

    @Override // java.awt.PrintJob
    public synchronized void end() {
        this.graphicsToBeDrawn.close();
        if (this.currentGraphics != null) {
            this.graphicsDrawn.append(this.currentGraphics);
        }
        this.graphicsDrawn.closeWhenEmpty();
        if (this.printerJobThread != null && this.printerJobThread.isAlive()) {
            try {
                this.printerJobThread.join();
            } catch (InterruptedException e2) {
            }
        }
    }

    @Override // java.awt.PrintJob
    public void finalize() {
        end();
    }

    @Override // java.awt.print.Printable
    public int print(Graphics graphics, PageFormat pageFormat, int i2) throws PrinterException {
        int i3;
        this.graphicsToBeDrawn.append((Graphics2D) graphics);
        if (this.graphicsDrawn.pop() != null) {
            i3 = 0;
        } else {
            i3 = 1;
        }
        return i3;
    }

    private void startPrinterJobThread() {
        this.printerJobThread = new Thread(this, "printerJobThread");
        this.printerJobThread.start();
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.printerJob.print(this.attributes);
        } catch (PrinterException e2) {
        }
        this.graphicsToBeDrawn.closeWhenEmpty();
        this.graphicsDrawn.close();
    }

    /* loaded from: rt.jar:sun/print/PrintJob2D$MessageQ.class */
    private class MessageQ {
        private String qid;
        private ArrayList queue = new ArrayList();

        MessageQ(String str) {
            this.qid = "noname";
            this.qid = str;
        }

        synchronized void closeWhenEmpty() {
            while (this.queue != null && this.queue.size() > 0) {
                try {
                    wait(1000L);
                } catch (InterruptedException e2) {
                }
            }
            this.queue = null;
            notifyAll();
        }

        synchronized void close() {
            this.queue = null;
            notifyAll();
        }

        synchronized boolean append(Graphics2D graphics2D) {
            boolean z2 = false;
            if (this.queue != null) {
                this.queue.add(graphics2D);
                z2 = true;
                notify();
            }
            return z2;
        }

        synchronized Graphics2D pop() {
            Graphics2D graphics2D = null;
            while (graphics2D == null && this.queue != null) {
                if (this.queue.size() > 0) {
                    graphics2D = (Graphics2D) this.queue.remove(0);
                    notify();
                } else {
                    try {
                        wait(2000L);
                    } catch (InterruptedException e2) {
                    }
                }
            }
            return graphics2D;
        }

        synchronized boolean isClosed() {
            return this.queue == null;
        }
    }

    private static int[] getSize(PageAttributes.MediaType mediaType) {
        int[] iArr = {DndEvent.UPDATE, 792};
        int i2 = 0;
        while (true) {
            if (i2 >= SIZES.length) {
                break;
            }
            if (SIZES[i2] != mediaType) {
                i2++;
            } else {
                iArr[0] = WIDTHS[i2];
                iArr[1] = LENGTHS[i2];
                break;
            }
        }
        return iArr;
    }

    public static MediaSizeName mapMedia(PageAttributes.MediaType mediaType) {
        MediaSizeName customMediaSizeName = null;
        int iMin = Math.min(SIZES.length, JAVAXSIZES.length);
        int i2 = 0;
        while (true) {
            if (i2 >= iMin) {
                break;
            }
            if (SIZES[i2] != mediaType) {
                i2++;
            } else if (JAVAXSIZES[i2] != null && MediaSize.getMediaSizeForName(JAVAXSIZES[i2]) != null) {
                customMediaSizeName = JAVAXSIZES[i2];
            } else {
                customMediaSizeName = new CustomMediaSizeName(SIZES[i2].toString());
                float fRint = (float) Math.rint(WIDTHS[i2] / 72.0d);
                float fRint2 = (float) Math.rint(LENGTHS[i2] / 72.0d);
                if (fRint > 0.0d && fRint2 > 0.0d) {
                    new MediaSize(fRint, fRint2, 25400, customMediaSizeName);
                }
            }
        }
        return customMediaSizeName;
    }

    public static PageAttributes.MediaType unMapMedia(MediaSizeName mediaSizeName) {
        PageAttributes.MediaType mediaType = null;
        int iMin = Math.min(SIZES.length, JAVAXSIZES.length);
        int i2 = 0;
        while (true) {
            if (i2 < iMin) {
                if (JAVAXSIZES[i2] != mediaSizeName || SIZES[i2] == null) {
                    i2++;
                } else {
                    mediaType = SIZES[i2];
                    break;
                }
            } else {
                break;
            }
        }
        return mediaType;
    }

    private void translateInputProps() {
        if (this.props == null) {
            return;
        }
        String property = this.props.getProperty(DEST_PROP);
        if (property != null) {
            if (property.equals(PRINTER)) {
                this.jobAttributes.setDestination(JobAttributes.DestinationType.PRINTER);
            } else if (property.equals("file")) {
                this.jobAttributes.setDestination(JobAttributes.DestinationType.FILE);
            }
        }
        String property2 = this.props.getProperty(PRINTER_PROP);
        if (property2 != null) {
            this.jobAttributes.setPrinter(property2);
        }
        String property3 = this.props.getProperty(FILENAME_PROP);
        if (property3 != null) {
            this.jobAttributes.setFileName(property3);
        }
        String property4 = this.props.getProperty(NUMCOPIES_PROP);
        if (property4 != null) {
            this.jobAttributes.setCopies(Integer.parseInt(property4));
        }
        this.options = this.props.getProperty(OPTIONS_PROP, "");
        String property5 = this.props.getProperty(ORIENT_PROP);
        if (property5 != null) {
            if (property5.equals(PORTRAIT)) {
                this.pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT);
            } else if (property5.equals(LANDSCAPE)) {
                this.pageAttributes.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE);
            }
        }
        String property6 = this.props.getProperty(PAPERSIZE_PROP);
        if (property6 != null) {
            if (property6.equals(LETTER)) {
                this.pageAttributes.setMedia(SIZES[PageAttributes.MediaType.LETTER.hashCode()]);
                return;
            }
            if (property6.equals(LEGAL)) {
                this.pageAttributes.setMedia(SIZES[PageAttributes.MediaType.LEGAL.hashCode()]);
            } else if (property6.equals(EXECUTIVE)) {
                this.pageAttributes.setMedia(SIZES[PageAttributes.MediaType.EXECUTIVE.hashCode()]);
            } else if (property6.equals(A4)) {
                this.pageAttributes.setMedia(SIZES[PageAttributes.MediaType.A4.hashCode()]);
            }
        }
    }

    private void translateOutputProps() {
        String string;
        if (this.props == null) {
            return;
        }
        this.props.setProperty(DEST_PROP, this.jobAttributes.getDestination() == JobAttributes.DestinationType.PRINTER ? PRINTER : "file");
        String printer = this.jobAttributes.getPrinter();
        if (printer != null && !printer.equals("")) {
            this.props.setProperty(PRINTER_PROP, printer);
        }
        String fileName = this.jobAttributes.getFileName();
        if (fileName != null && !fileName.equals("")) {
            this.props.setProperty(FILENAME_PROP, fileName);
        }
        int copies = this.jobAttributes.getCopies();
        if (copies > 0) {
            this.props.setProperty(NUMCOPIES_PROP, "" + copies);
        }
        String str = this.options;
        if (str != null && !str.equals("")) {
            this.props.setProperty(OPTIONS_PROP, str);
        }
        this.props.setProperty(ORIENT_PROP, this.pageAttributes.getOrientationRequested() == PageAttributes.OrientationRequestedType.PORTRAIT ? PORTRAIT : LANDSCAPE);
        PageAttributes.MediaType mediaType = SIZES[this.pageAttributes.getMedia().hashCode()];
        if (mediaType == PageAttributes.MediaType.LETTER) {
            string = LETTER;
        } else if (mediaType == PageAttributes.MediaType.LEGAL) {
            string = LEGAL;
        } else if (mediaType == PageAttributes.MediaType.EXECUTIVE) {
            string = EXECUTIVE;
        } else if (mediaType == PageAttributes.MediaType.A4) {
            string = A4;
        } else {
            string = mediaType.toString();
        }
        this.props.setProperty(PAPERSIZE_PROP, string);
    }

    private void throwPrintToFile() {
        SecurityManager securityManager = System.getSecurityManager();
        FilePermission filePermission = null;
        if (securityManager != null) {
            if (0 == 0) {
                filePermission = new FilePermission("<<ALL FILES>>", SecurityConstants.PROPERTY_RW_ACTION);
            }
            securityManager.checkPermission(filePermission);
        }
    }
}
