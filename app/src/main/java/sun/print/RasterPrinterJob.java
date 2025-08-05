package sun.print;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Locale;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.Fidelity;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.JobSheets;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;
import javax.print.attribute.standard.PrinterStateReasons;
import javax.print.attribute.standard.RequestingUserName;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;
import sun.awt.image.ByteInterleavedRaster;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/print/RasterPrinterJob.class */
public abstract class RasterPrinterJob extends PrinterJob {
    protected static final int PRINTER = 0;
    protected static final int FILE = 1;
    protected static final int STREAM = 2;
    protected static final int MAX_UNKNOWN_PAGES = 9999;
    protected static final int PD_ALLPAGES = 0;
    protected static final int PD_SELECTION = 1;
    protected static final int PD_PAGENUMS = 2;
    protected static final int PD_NOSELECTION = 4;
    private static final int MAX_BAND_SIZE = 4194304;
    private static final float DPI = 72.0f;
    private static final String FORCE_PIPE_PROP = "sun.java2d.print.pipeline";
    private static final String FORCE_RASTER = "raster";
    private static final String FORCE_PDL = "pdl";
    private static final String SHAPE_TEXT_PROP = "sun.java2d.print.shapetext";
    public static boolean forcePDL;
    public static boolean forceRaster;
    public static boolean shapeTextProp;
    private Paper previousPaper;
    private FilePermission printToFilePermission;
    private int copiesAttr;
    private String jobNameAttr;
    private String userNameAttr;
    private PageRanges pageRangesAttr;
    protected Sides sidesAttr;
    protected String destinationAttr;
    protected PrintService myService;
    public static boolean debugPrint;
    private int deviceWidth;
    private int deviceHeight;
    private AffineTransform defaultDeviceTransform;
    private PrinterGraphicsConfig pgConfig;
    private int cachedBandWidth = 0;
    private int cachedBandHeight = 0;
    private BufferedImage cachedBand = null;
    private int mNumCopies = 1;
    private boolean mCollate = false;
    private int mFirstPage = -1;
    private int mLastPage = -1;
    protected Pageable mDocument = new Book();
    private String mDocName = "Java Printing";
    protected boolean performingPrinting = false;
    protected boolean userCancelled = false;
    private ArrayList redrawList = new ArrayList();
    protected boolean noJobSheet = false;
    protected int mDestType = 1;
    protected String mDestination = "";
    protected boolean collateAttReq = false;
    protected boolean landscapeRotates270 = false;
    protected PrintRequestAttributeSet attributes = null;
    private DialogOnTop onTop = null;
    private long parentWindowID = 0;

    protected abstract double getXRes();

    protected abstract double getYRes();

    protected abstract double getPhysicalPrintableX(Paper paper);

    protected abstract double getPhysicalPrintableY(Paper paper);

    protected abstract double getPhysicalPrintableWidth(Paper paper);

    protected abstract double getPhysicalPrintableHeight(Paper paper);

    protected abstract double getPhysicalPageWidth(Paper paper);

    protected abstract double getPhysicalPageHeight(Paper paper);

    protected abstract void startPage(PageFormat pageFormat, Printable printable, int i2, boolean z2) throws PrinterException;

    protected abstract void endPage(PageFormat pageFormat, Printable printable, int i2) throws PrinterException;

    protected abstract void printBand(byte[] bArr, int i2, int i3, int i4, int i5) throws PrinterException;

    protected abstract void startDoc() throws PrinterException;

    protected abstract void endDoc() throws PrinterException;

    protected abstract void abortDoc();

    static {
        forcePDL = false;
        forceRaster = false;
        shapeTextProp = false;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(FORCE_PIPE_PROP));
        if (str != null) {
            if (str.equalsIgnoreCase(FORCE_PDL)) {
                forcePDL = true;
            } else if (str.equalsIgnoreCase(FORCE_RASTER)) {
                forceRaster = true;
            }
        }
        if (((String) AccessController.doPrivileged(new GetPropertyAction(SHAPE_TEXT_PROP))) != null) {
            shapeTextProp = true;
        }
        debugPrint = false;
    }

    /* loaded from: rt.jar:sun/print/RasterPrinterJob$GraphicsState.class */
    private class GraphicsState {
        Rectangle2D region;
        Shape theClip;
        AffineTransform theTransform;
        double sx;
        double sy;

        private GraphicsState() {
        }
    }

    public void saveState(AffineTransform affineTransform, Shape shape, Rectangle2D rectangle2D, double d2, double d3) {
        GraphicsState graphicsState = new GraphicsState();
        graphicsState.theTransform = affineTransform;
        graphicsState.theClip = shape;
        graphicsState.region = rectangle2D;
        graphicsState.sx = d2;
        graphicsState.sy = d3;
        this.redrawList.add(graphicsState);
    }

    protected static PrintService lookupDefaultPrintService() {
        PrintService printServiceLookupDefaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
        if (printServiceLookupDefaultPrintService != null && printServiceLookupDefaultPrintService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PAGEABLE) && printServiceLookupDefaultPrintService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
            return printServiceLookupDefaultPrintService;
        }
        PrintService[] printServiceArrLookupPrintServices = PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
        if (printServiceArrLookupPrintServices.length > 0) {
            return printServiceArrLookupPrintServices[0];
        }
        return null;
    }

    @Override // java.awt.print.PrinterJob
    public PrintService getPrintService() {
        if (this.myService == null) {
            PrintService printServiceLookupDefaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
            if (printServiceLookupDefaultPrintService != null && printServiceLookupDefaultPrintService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PAGEABLE)) {
                try {
                    setPrintService(printServiceLookupDefaultPrintService);
                    this.myService = printServiceLookupDefaultPrintService;
                } catch (PrinterException e2) {
                }
            }
            if (this.myService == null) {
                PrintService[] printServiceArrLookupPrintServices = PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
                if (printServiceArrLookupPrintServices.length > 0) {
                    try {
                        setPrintService(printServiceArrLookupPrintServices[0]);
                        this.myService = printServiceArrLookupPrintServices[0];
                    } catch (PrinterException e3) {
                    }
                }
            }
        }
        return this.myService;
    }

    @Override // java.awt.print.PrinterJob
    public void setPrintService(PrintService printService) throws PrinterException {
        PrinterStateReasons printerStateReasons;
        if (printService == null) {
            throw new PrinterException("Service cannot be null");
        }
        if (!(printService instanceof StreamPrintService) && printService.getName() == null) {
            throw new PrinterException("Null PrintService name.");
        }
        if (((PrinterState) printService.getAttribute(PrinterState.class)) == PrinterState.STOPPED && (printerStateReasons = (PrinterStateReasons) printService.getAttribute(PrinterStateReasons.class)) != null && printerStateReasons.containsKey(PrinterStateReason.SHUTDOWN)) {
            throw new PrinterException("PrintService is no longer available.");
        }
        if (printService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PAGEABLE) && printService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PRINTABLE)) {
            this.myService = printService;
            return;
        }
        throw new PrinterException("Not a 2D print service: " + ((Object) printService));
    }

    private PageFormat attributeToPageFormat(PrintService printService, PrintRequestAttributeSet printRequestAttributeSet) throws IllegalArgumentException {
        PageFormat pageFormatDefaultPage = defaultPage();
        if (printService == null) {
            return pageFormatDefaultPage;
        }
        OrientationRequested orientationRequested = (OrientationRequested) printRequestAttributeSet.get(OrientationRequested.class);
        if (orientationRequested == null) {
            orientationRequested = (OrientationRequested) printService.getDefaultAttributeValue(OrientationRequested.class);
        }
        if (orientationRequested == OrientationRequested.REVERSE_LANDSCAPE) {
            pageFormatDefaultPage.setOrientation(2);
        } else if (orientationRequested == OrientationRequested.LANDSCAPE) {
            pageFormatDefaultPage.setOrientation(0);
        } else {
            pageFormatDefaultPage.setOrientation(1);
        }
        MediaSize mediaSize = getMediaSize((Media) printRequestAttributeSet.get(Media.class), printService, pageFormatDefaultPage);
        Paper paper = new Paper();
        float[] size = mediaSize.getSize(1);
        double dRint = Math.rint((size[0] * 72.0d) / 25400.0d);
        double dRint2 = Math.rint((size[1] * 72.0d) / 25400.0d);
        paper.setSize(dRint, dRint2);
        MediaPrintableArea defaultPrintableArea = (MediaPrintableArea) printRequestAttributeSet.get(MediaPrintableArea.class);
        if (defaultPrintableArea == null) {
            defaultPrintableArea = getDefaultPrintableArea(pageFormatDefaultPage, dRint, dRint2);
        }
        paper.setImageableArea(Math.rint(defaultPrintableArea.getX(25400) * DPI), Math.rint(defaultPrintableArea.getY(25400) * DPI), Math.rint(defaultPrintableArea.getWidth(25400) * DPI), Math.rint(defaultPrintableArea.getHeight(25400) * DPI));
        pageFormatDefaultPage.setPaper(paper);
        return pageFormatDefaultPage;
    }

    protected MediaSize getMediaSize(Media media, PrintService printService, PageFormat pageFormat) {
        if (media == null) {
            media = (Media) printService.getDefaultAttributeValue(Media.class);
        }
        if (!(media instanceof MediaSizeName)) {
            media = MediaSizeName.NA_LETTER;
        }
        MediaSize mediaSizeForName = MediaSize.getMediaSizeForName((MediaSizeName) media);
        return mediaSizeForName != null ? mediaSizeForName : MediaSize.NA.LETTER;
    }

    protected MediaPrintableArea getDefaultPrintableArea(PageFormat pageFormat, double d2, double d3) {
        double d4;
        double d5;
        double d6;
        double d7;
        if (d2 >= 432.0d) {
            d4 = 72.0d;
            d5 = d2 - 144.0d;
        } else {
            d4 = d2 / 6.0d;
            d5 = d2 * 0.75d;
        }
        if (d3 >= 432.0d) {
            d6 = 72.0d;
            d7 = d3 - 144.0d;
        } else {
            d6 = d3 / 6.0d;
            d7 = d3 * 0.75d;
        }
        return new MediaPrintableArea((float) (d4 / 72.0d), (float) (d6 / 72.0d), (float) (d5 / 72.0d), (float) (d7 / 72.0d), 25400);
    }

    protected void updatePageAttributes(PrintService printService, PageFormat pageFormat) {
        if (this.attributes == null) {
            this.attributes = new HashPrintRequestAttributeSet();
        }
        updateAttributesWithPageFormat(printService, pageFormat, this.attributes);
    }

    protected void updateAttributesWithPageFormat(PrintService printService, PageFormat pageFormat, PrintRequestAttributeSet printRequestAttributeSet) {
        OrientationRequested orientationRequested;
        if (printService == null || pageFormat == null || printRequestAttributeSet == null) {
            return;
        }
        Media mediaFindMedia = null;
        try {
            mediaFindMedia = CustomMediaSizeName.findMedia((Media[]) printService.getSupportedAttributeValues(Media.class, null, null), ((float) Math.rint((pageFormat.getPaper().getWidth() * 25400.0d) / 72.0d)) / 25400.0f, ((float) Math.rint((pageFormat.getPaper().getHeight() * 25400.0d) / 72.0d)) / 25400.0f, 25400);
        } catch (IllegalArgumentException e2) {
        }
        if (mediaFindMedia == null || !printService.isAttributeValueSupported(mediaFindMedia, null, null)) {
            mediaFindMedia = (Media) printService.getDefaultAttributeValue(Media.class);
        }
        switch (pageFormat.getOrientation()) {
            case 0:
                orientationRequested = OrientationRequested.LANDSCAPE;
                break;
            case 2:
                orientationRequested = OrientationRequested.REVERSE_LANDSCAPE;
                break;
            default:
                orientationRequested = OrientationRequested.PORTRAIT;
                break;
        }
        if (mediaFindMedia != null) {
            printRequestAttributeSet.add(mediaFindMedia);
        }
        printRequestAttributeSet.add(orientationRequested);
        float imageableX = (float) (pageFormat.getPaper().getImageableX() / 72.0d);
        float imageableWidth = (float) (pageFormat.getPaper().getImageableWidth() / 72.0d);
        float imageableY = (float) (pageFormat.getPaper().getImageableY() / 72.0d);
        float imageableHeight = (float) (pageFormat.getPaper().getImageableHeight() / 72.0d);
        if (imageableX < 0.0f) {
            imageableX = 0.0f;
        }
        if (imageableY < 0.0f) {
            imageableY = 0.0f;
        }
        try {
            printRequestAttributeSet.add(new MediaPrintableArea(imageableX, imageableY, imageableWidth, imageableHeight, 25400));
        } catch (IllegalArgumentException e3) {
        }
    }

    @Override // java.awt.print.PrinterJob
    public PageFormat pageDialog(PageFormat pageFormat) throws HeadlessException, IllegalArgumentException {
        PageFormat pageFormatPageDialog;
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        final GraphicsConfiguration defaultConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        PrintService printService = (PrintService) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.RasterPrinterJob.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() throws HeadlessException {
                PrintService printService2 = RasterPrinterJob.this.getPrintService();
                if (printService2 == null) {
                    ServiceDialog.showNoPrintService(defaultConfiguration);
                    return null;
                }
                return printService2;
            }
        });
        if (printService == null) {
            return pageFormat;
        }
        updatePageAttributes(printService, pageFormat);
        if (((DialogTypeSelection) this.attributes.get(DialogTypeSelection.class)) == DialogTypeSelection.NATIVE) {
            this.attributes.remove(DialogTypeSelection.class);
            pageFormatPageDialog = pageDialog(this.attributes);
            this.attributes.add(DialogTypeSelection.NATIVE);
        } else {
            pageFormatPageDialog = pageDialog(this.attributes);
        }
        if (pageFormatPageDialog == null) {
            return pageFormat;
        }
        return pageFormatPageDialog;
    }

    @Override // java.awt.print.PrinterJob
    public PageFormat pageDialog(PrintRequestAttributeSet printRequestAttributeSet) throws HeadlessException, IllegalArgumentException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (((DialogTypeSelection) printRequestAttributeSet.get(DialogTypeSelection.class)) == DialogTypeSelection.NATIVE) {
            PrintService printService = getPrintService();
            PageFormat pageFormatAttributeToPageFormat = attributeToPageFormat(printService, printRequestAttributeSet);
            setParentWindowID(printRequestAttributeSet);
            PageFormat pageFormatPageDialog = pageDialog(pageFormatAttributeToPageFormat);
            clearParentWindowID();
            if (pageFormatPageDialog == pageFormatAttributeToPageFormat) {
                return null;
            }
            updateAttributesWithPageFormat(printService, pageFormatPageDialog, printRequestAttributeSet);
            return pageFormatPageDialog;
        }
        final GraphicsConfiguration defaultConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle bounds = defaultConfiguration.getBounds();
        int i2 = bounds.f12372x + (bounds.width / 3);
        int i3 = bounds.f12373y + (bounds.height / 3);
        PrintService printService2 = (PrintService) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.RasterPrinterJob.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() throws HeadlessException {
                PrintService printService3 = RasterPrinterJob.this.getPrintService();
                if (printService3 == null) {
                    ServiceDialog.showNoPrintService(defaultConfiguration);
                    return null;
                }
                return printService3;
            }
        });
        if (printService2 == null) {
            return null;
        }
        if (this.onTop != null) {
            printRequestAttributeSet.add(this.onTop);
        }
        ServiceDialog serviceDialog = new ServiceDialog(defaultConfiguration, i2, i3, printService2, DocFlavor.SERVICE_FORMATTED.PAGEABLE, printRequestAttributeSet, (Frame) null);
        serviceDialog.show();
        if (serviceDialog.getStatus() == 1) {
            PrintRequestAttributeSet attributes = serviceDialog.getAttributes();
            if (printRequestAttributeSet.containsKey(SunAlternateMedia.class) && !attributes.containsKey(SunAlternateMedia.class)) {
                printRequestAttributeSet.remove(SunAlternateMedia.class);
            }
            printRequestAttributeSet.addAll(attributes);
            return attributeToPageFormat(printService2, printRequestAttributeSet);
        }
        return null;
    }

    protected PageFormat getPageFormatFromAttributes() throws IndexOutOfBoundsException, IllegalArgumentException {
        if (this.attributes != null && !this.attributes.isEmpty()) {
            Pageable pageable = getPageable();
            if (!(pageable instanceof OpenBook)) {
                return null;
            }
            PageFormat pageFormatAttributeToPageFormat = attributeToPageFormat(getPrintService(), this.attributes);
            PageFormat pageFormat = pageable.getPageFormat(0);
            if (pageFormat != null) {
                if (this.attributes.get(OrientationRequested.class) == null) {
                    pageFormatAttributeToPageFormat.setOrientation(pageFormat.getOrientation());
                }
                Paper paper = pageFormatAttributeToPageFormat.getPaper();
                Paper paper2 = pageFormat.getPaper();
                boolean z2 = false;
                if (this.attributes.get(MediaSizeName.class) == null) {
                    paper.setSize(paper2.getWidth(), paper2.getHeight());
                    z2 = true;
                }
                if (this.attributes.get(MediaPrintableArea.class) == null) {
                    paper.setImageableArea(paper2.getImageableX(), paper2.getImageableY(), paper2.getImageableWidth(), paper2.getImageableHeight());
                    z2 = true;
                }
                if (z2) {
                    pageFormatAttributeToPageFormat.setPaper(paper);
                }
            }
            return pageFormatAttributeToPageFormat;
        }
        return null;
    }

    @Override // java.awt.print.PrinterJob
    public boolean printDialog(PrintRequestAttributeSet printRequestAttributeSet) throws HeadlessException {
        PrintService[] printServiceArr;
        PrintService printServicePrintDialog;
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (((DialogTypeSelection) printRequestAttributeSet.get(DialogTypeSelection.class)) == DialogTypeSelection.NATIVE) {
            this.attributes = printRequestAttributeSet;
            try {
                debug_println("calling setAttributes in printDialog");
                setAttributes(printRequestAttributeSet);
            } catch (PrinterException e2) {
            }
            setParentWindowID(printRequestAttributeSet);
            boolean zPrintDialog = printDialog();
            clearParentWindowID();
            this.attributes = printRequestAttributeSet;
            return zPrintDialog;
        }
        final GraphicsConfiguration defaultConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        PrintService printService = (PrintService) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.RasterPrinterJob.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() throws HeadlessException {
                PrintService printService2 = RasterPrinterJob.this.getPrintService();
                if (printService2 == null) {
                    ServiceDialog.showNoPrintService(defaultConfiguration);
                    return null;
                }
                return printService2;
            }
        });
        if (printService == null) {
            return false;
        }
        if (printService instanceof StreamPrintService) {
            StreamPrintServiceFactory[] streamPrintServiceFactoryArrLookupStreamPrintServices = lookupStreamPrintServices(null);
            printServiceArr = new StreamPrintService[streamPrintServiceFactoryArrLookupStreamPrintServices.length];
            for (int i2 = 0; i2 < streamPrintServiceFactoryArrLookupStreamPrintServices.length; i2++) {
                printServiceArr[i2] = streamPrintServiceFactoryArrLookupStreamPrintServices[i2].getPrintService(null);
            }
        } else {
            printServiceArr = (PrintService[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.RasterPrinterJob.4
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    return PrinterJob.lookupPrintServices();
                }
            });
            if (printServiceArr == null || printServiceArr.length == 0) {
                printServiceArr = new PrintService[]{printService};
            }
        }
        Rectangle bounds = defaultConfiguration.getBounds();
        int i3 = bounds.f12372x + (bounds.width / 3);
        int i4 = bounds.f12373y + (bounds.height / 3);
        printRequestAttributeSet.add(new PrinterJobWrapper(this));
        try {
            printServicePrintDialog = ServiceUI.printDialog(defaultConfiguration, i3, i4, printServiceArr, printService, DocFlavor.SERVICE_FORMATTED.PAGEABLE, printRequestAttributeSet);
        } catch (IllegalArgumentException e3) {
            printServicePrintDialog = ServiceUI.printDialog(defaultConfiguration, i3, i4, printServiceArr, printServiceArr[0], DocFlavor.SERVICE_FORMATTED.PAGEABLE, printRequestAttributeSet);
        }
        printRequestAttributeSet.remove(PrinterJobWrapper.class);
        if (printServicePrintDialog == null) {
            return false;
        }
        if (!printService.equals(printServicePrintDialog)) {
            try {
                setPrintService(printServicePrintDialog);
                return true;
            } catch (PrinterException e4) {
                this.myService = printServicePrintDialog;
                return true;
            }
        }
        return true;
    }

    @Override // java.awt.print.PrinterJob
    public boolean printDialog() throws HeadlessException {
        Destination destination;
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        HashPrintRequestAttributeSet hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
        hashPrintRequestAttributeSet.add(new Copies(getCopies()));
        hashPrintRequestAttributeSet.add(new JobName(getJobName(), null));
        boolean zPrintDialog = printDialog(hashPrintRequestAttributeSet);
        if (zPrintDialog) {
            JobName jobName = (JobName) hashPrintRequestAttributeSet.get(JobName.class);
            if (jobName != null) {
                setJobName(jobName.getValue());
            }
            Copies copies = (Copies) hashPrintRequestAttributeSet.get(Copies.class);
            if (copies != null) {
                setCopies(copies.getValue());
            }
            Destination destination2 = (Destination) hashPrintRequestAttributeSet.get(Destination.class);
            if (destination2 != null) {
                try {
                    this.mDestType = 1;
                    this.mDestination = new File(destination2.getURI()).getPath();
                } catch (Exception e2) {
                    this.mDestination = "out.prn";
                    PrintService printService = getPrintService();
                    if (printService != null && (destination = (Destination) printService.getDefaultAttributeValue(Destination.class)) != null) {
                        this.mDestination = new File(destination.getURI()).getPath();
                    }
                }
            } else {
                this.mDestType = 0;
                PrintService printService2 = getPrintService();
                if (printService2 != null) {
                    this.mDestination = printService2.getName();
                }
            }
        }
        return zPrintDialog;
    }

    @Override // java.awt.print.PrinterJob
    public void setPrintable(Printable printable) throws NullPointerException {
        setPageable(new OpenBook(defaultPage(new PageFormat()), printable));
    }

    @Override // java.awt.print.PrinterJob
    public void setPrintable(Printable printable, PageFormat pageFormat) throws NullPointerException {
        setPageable(new OpenBook(pageFormat, printable));
        updatePageAttributes(getPrintService(), pageFormat);
    }

    @Override // java.awt.print.PrinterJob
    public void setPageable(Pageable pageable) throws NullPointerException {
        if (pageable != null) {
            this.mDocument = pageable;
            return;
        }
        throw new NullPointerException();
    }

    protected void initPrinter() {
    }

    protected boolean isSupportedValue(Attribute attribute, PrintRequestAttributeSet printRequestAttributeSet) {
        PrintService printService = getPrintService();
        return (attribute == null || printService == null || !printService.isAttributeValueSupported(attribute, DocFlavor.SERVICE_FORMATTED.PAGEABLE, printRequestAttributeSet)) ? false : true;
    }

    protected void setAttributes(PrintRequestAttributeSet printRequestAttributeSet) throws PrinterException {
        int i2;
        MediaSize mediaSizeForName;
        setCollated(false);
        this.sidesAttr = null;
        this.pageRangesAttr = null;
        this.copiesAttr = 0;
        this.jobNameAttr = null;
        this.userNameAttr = null;
        this.destinationAttr = null;
        this.collateAttReq = false;
        PrintService printService = getPrintService();
        if (printRequestAttributeSet == null || printService == null) {
            return;
        }
        boolean z2 = false;
        Fidelity fidelity = (Fidelity) printRequestAttributeSet.get(Fidelity.class);
        if (fidelity != null && fidelity == Fidelity.FIDELITY_TRUE) {
            z2 = true;
        }
        if (z2 && printService.getUnsupportedAttributes(DocFlavor.SERVICE_FORMATTED.PAGEABLE, printRequestAttributeSet) != null) {
            throw new PrinterException("Fidelity cannot be satisfied");
        }
        Attribute attribute = (SheetCollate) printRequestAttributeSet.get(SheetCollate.class);
        if (isSupportedValue(attribute, printRequestAttributeSet)) {
            setCollated(attribute == SheetCollate.COLLATED);
        }
        this.sidesAttr = (Sides) printRequestAttributeSet.get(Sides.class);
        if (!isSupportedValue(this.sidesAttr, printRequestAttributeSet)) {
            this.sidesAttr = Sides.ONE_SIDED;
        }
        this.pageRangesAttr = (PageRanges) printRequestAttributeSet.get(PageRanges.class);
        if (!isSupportedValue(this.pageRangesAttr, printRequestAttributeSet)) {
            this.pageRangesAttr = null;
        } else if (((SunPageSelection) printRequestAttributeSet.get(SunPageSelection.class)) == SunPageSelection.RANGE) {
            int[][] members = this.pageRangesAttr.getMembers();
            setPageRange(members[0][0] - 1, members[0][1] - 1);
        } else {
            setPageRange(-1, -1);
        }
        Copies copies = (Copies) printRequestAttributeSet.get(Copies.class);
        if (isSupportedValue(copies, printRequestAttributeSet) || (!z2 && copies != null)) {
            this.copiesAttr = copies.getValue();
            setCopies(this.copiesAttr);
        } else {
            this.copiesAttr = getCopies();
        }
        Destination destination = (Destination) printRequestAttributeSet.get(Destination.class);
        if (isSupportedValue(destination, printRequestAttributeSet)) {
            try {
                this.destinationAttr = "" + ((Object) new File(destination.getURI().getSchemeSpecificPart()));
            } catch (Exception e2) {
                Destination destination2 = (Destination) printService.getDefaultAttributeValue(Destination.class);
                if (destination2 != null) {
                    this.destinationAttr = "" + ((Object) new File(destination2.getURI().getSchemeSpecificPart()));
                }
            }
        }
        JobSheets jobSheets = (JobSheets) printRequestAttributeSet.get(JobSheets.class);
        if (jobSheets != null) {
            this.noJobSheet = jobSheets == JobSheets.NONE;
        }
        JobName jobName = (JobName) printRequestAttributeSet.get(JobName.class);
        if (isSupportedValue(jobName, printRequestAttributeSet) || (!z2 && jobName != null)) {
            this.jobNameAttr = jobName.getValue();
            setJobName(this.jobNameAttr);
        } else {
            this.jobNameAttr = getJobName();
        }
        RequestingUserName requestingUserName = (RequestingUserName) printRequestAttributeSet.get(RequestingUserName.class);
        if (isSupportedValue(requestingUserName, printRequestAttributeSet) || (!z2 && requestingUserName != null)) {
            this.userNameAttr = requestingUserName.getValue();
        } else {
            try {
                this.userNameAttr = getUserName();
            } catch (SecurityException e3) {
                this.userNameAttr = "";
            }
        }
        Attribute attribute2 = (Media) printRequestAttributeSet.get(Media.class);
        Attribute attribute3 = (OrientationRequested) printRequestAttributeSet.get(OrientationRequested.class);
        MediaPrintableArea mediaPrintableArea = (MediaPrintableArea) printRequestAttributeSet.get(MediaPrintableArea.class);
        if ((attribute3 != null || attribute2 != null || mediaPrintableArea != null) && (getPageable() instanceof OpenBook)) {
            Pageable pageable = getPageable();
            Printable printable = pageable.getPrintable(0);
            PageFormat pageFormat = (PageFormat) pageable.getPageFormat(0).clone();
            Paper paper = pageFormat.getPaper();
            if (mediaPrintableArea == null && attribute2 != null && printService.isAttributeCategorySupported(MediaPrintableArea.class)) {
                Object supportedAttributeValues = printService.getSupportedAttributeValues(MediaPrintableArea.class, null, printRequestAttributeSet);
                if ((supportedAttributeValues instanceof MediaPrintableArea[]) && ((MediaPrintableArea[]) supportedAttributeValues).length > 0) {
                    mediaPrintableArea = ((MediaPrintableArea[]) supportedAttributeValues)[0];
                }
            }
            if (isSupportedValue(attribute3, printRequestAttributeSet) || (!z2 && attribute3 != null)) {
                if (attribute3.equals(OrientationRequested.REVERSE_LANDSCAPE)) {
                    i2 = 2;
                } else if (attribute3.equals(OrientationRequested.LANDSCAPE)) {
                    i2 = 0;
                } else {
                    i2 = 1;
                }
                pageFormat.setOrientation(i2);
            }
            if ((isSupportedValue(attribute2, printRequestAttributeSet) || (!z2 && attribute2 != null)) && (attribute2 instanceof MediaSizeName) && (mediaSizeForName = MediaSize.getMediaSizeForName((MediaSizeName) attribute2)) != null) {
                float x2 = mediaSizeForName.getX(25400) * DPI;
                float y2 = mediaSizeForName.getY(25400) * DPI;
                paper.setSize(x2, y2);
                if (mediaPrintableArea == null) {
                    paper.setImageableArea(72.0d, 72.0d, x2 - 144.0d, y2 - 144.0d);
                }
            }
            if (isSupportedValue(mediaPrintableArea, printRequestAttributeSet) || (!z2 && mediaPrintableArea != null)) {
                float[] printableArea = mediaPrintableArea.getPrintableArea(25400);
                for (int i3 = 0; i3 < printableArea.length; i3++) {
                    printableArea[i3] = printableArea[i3] * DPI;
                }
                paper.setImageableArea(printableArea[0], printableArea[1], printableArea[2], printableArea[3]);
            }
            pageFormat.setPaper(paper);
            setPrintable(printable, validatePage(pageFormat));
            return;
        }
        this.attributes = printRequestAttributeSet;
    }

    protected void spoolToService(PrintService printService, PrintRequestAttributeSet printRequestAttributeSet) throws PrinterException {
        if (printService == null) {
            throw new PrinterException("No print service found.");
        }
        DocPrintJob docPrintJobCreatePrintJob = printService.createPrintJob();
        PageableDoc pageableDoc = new PageableDoc(getPageable());
        if (printRequestAttributeSet == null) {
            printRequestAttributeSet = new HashPrintRequestAttributeSet();
        }
        try {
            docPrintJobCreatePrintJob.print(pageableDoc, printRequestAttributeSet);
        } catch (PrintException e2) {
            throw new PrinterException(e2.toString());
        }
    }

    @Override // java.awt.print.PrinterJob
    public void print() throws PrinterException {
        print(this.attributes);
    }

    protected void debug_println(String str) {
        if (debugPrint) {
            System.out.println("RasterPrinterJob " + str + " " + ((Object) this));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:75:0x01ef  */
    @Override // java.awt.print.PrinterJob
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void print(javax.print.attribute.PrintRequestAttributeSet r5) throws java.awt.print.PrinterException {
        /*
            Method dump skipped, instructions count: 684
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.print.RasterPrinterJob.print(javax.print.attribute.PrintRequestAttributeSet):void");
    }

    protected void validateDestination(String str) throws PrinterException {
        if (str == null) {
            return;
        }
        File file = new File(str);
        try {
            if (file.createNewFile()) {
                file.delete();
            }
        } catch (IOException e2) {
            throw new PrinterException("Cannot write to file:" + str);
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
        throw new PrinterException("Cannot write to file:" + str);
    }

    protected void validatePaper(Paper paper, Paper paper2) {
        if (paper == null || paper2 == null) {
            return;
        }
        double width = paper.getWidth();
        double height = paper.getHeight();
        double imageableX = paper.getImageableX();
        double imageableY = paper.getImageableY();
        double imageableWidth = paper.getImageableWidth();
        double imageableHeight = paper.getImageableHeight();
        Paper paper3 = new Paper();
        double width2 = width > 0.0d ? width : paper3.getWidth();
        double height2 = height > 0.0d ? height : paper3.getHeight();
        double imageableX2 = imageableX > 0.0d ? imageableX : paper3.getImageableX();
        double imageableY2 = imageableY > 0.0d ? imageableY : paper3.getImageableY();
        double imageableWidth2 = imageableWidth > 0.0d ? imageableWidth : paper3.getImageableWidth();
        double imageableHeight2 = imageableHeight > 0.0d ? imageableHeight : paper3.getImageableHeight();
        if (imageableWidth2 > width2) {
            imageableWidth2 = width2;
        }
        if (imageableHeight2 > height2) {
            imageableHeight2 = height2;
        }
        if (imageableX2 + imageableWidth2 > width2) {
            imageableX2 = width2 - imageableWidth2;
        }
        if (imageableY2 + imageableHeight2 > height2) {
            imageableY2 = height2 - imageableHeight2;
        }
        paper2.setSize(width2, height2);
        paper2.setImageableArea(imageableX2, imageableY2, imageableWidth2, imageableHeight2);
    }

    @Override // java.awt.print.PrinterJob
    public PageFormat defaultPage(PageFormat pageFormat) throws IllegalArgumentException {
        MediaSize mediaSizeForName;
        PageFormat pageFormat2 = (PageFormat) pageFormat.clone();
        pageFormat2.setOrientation(1);
        Paper paper = new Paper();
        PrintService printService = getPrintService();
        if (printService != null) {
            Media media = (Media) printService.getDefaultAttributeValue(Media.class);
            if ((media instanceof MediaSizeName) && (mediaSizeForName = MediaSize.getMediaSizeForName((MediaSizeName) media)) != null) {
                double x2 = mediaSizeForName.getX(25400) * 72.0d;
                double y2 = mediaSizeForName.getY(25400) * 72.0d;
                paper.setSize(x2, y2);
                paper.setImageableArea(72.0d, 72.0d, x2 - (2.0d * 72.0d), y2 - (2.0d * 72.0d));
                pageFormat2.setPaper(paper);
                return pageFormat2;
            }
        }
        String country = Locale.getDefault().getCountry();
        if (!Locale.getDefault().equals(Locale.ENGLISH) && country != null && !country.equals(Locale.US.getCountry()) && !country.equals(Locale.CANADA.getCountry())) {
            double dRint = Math.rint((210.0d * 72.0d) / 25.4d);
            double dRint2 = Math.rint((297.0d * 72.0d) / 25.4d);
            paper.setSize(dRint, dRint2);
            paper.setImageableArea(72.0d, 72.0d, dRint - (2.0d * 72.0d), dRint2 - (2.0d * 72.0d));
        }
        pageFormat2.setPaper(paper);
        return pageFormat2;
    }

    @Override // java.awt.print.PrinterJob
    public PageFormat validatePage(PageFormat pageFormat) {
        PageFormat pageFormat2 = (PageFormat) pageFormat.clone();
        Paper paper = new Paper();
        validatePaper(pageFormat2.getPaper(), paper);
        pageFormat2.setPaper(paper);
        return pageFormat2;
    }

    @Override // java.awt.print.PrinterJob
    public void setCopies(int i2) {
        this.mNumCopies = i2;
    }

    @Override // java.awt.print.PrinterJob
    public int getCopies() {
        return this.mNumCopies;
    }

    protected int getCopiesInt() {
        return this.copiesAttr > 0 ? this.copiesAttr : getCopies();
    }

    @Override // java.awt.print.PrinterJob
    public String getUserName() {
        return System.getProperty("user.name");
    }

    protected String getUserNameInt() {
        if (this.userNameAttr != null) {
            return this.userNameAttr;
        }
        try {
            return getUserName();
        } catch (SecurityException e2) {
            return "";
        }
    }

    @Override // java.awt.print.PrinterJob
    public void setJobName(String str) {
        if (str != null) {
            this.mDocName = str;
            return;
        }
        throw new NullPointerException();
    }

    @Override // java.awt.print.PrinterJob
    public String getJobName() {
        return this.mDocName;
    }

    protected String getJobNameInt() {
        return this.jobNameAttr != null ? this.jobNameAttr : getJobName();
    }

    protected void setPageRange(int i2, int i3) {
        if (i2 >= 0 && i3 >= 0) {
            this.mFirstPage = i2;
            this.mLastPage = i3;
            if (this.mLastPage < this.mFirstPage) {
                this.mLastPage = this.mFirstPage;
                return;
            }
            return;
        }
        this.mFirstPage = -1;
        this.mLastPage = -1;
    }

    protected int getFirstPage() {
        if (this.mFirstPage == -1) {
            return 0;
        }
        return this.mFirstPage;
    }

    protected int getLastPage() {
        return this.mLastPage;
    }

    protected void setCollated(boolean z2) {
        this.mCollate = z2;
        this.collateAttReq = true;
    }

    protected boolean isCollated() {
        return this.mCollate;
    }

    protected final int getSelectAttrib() {
        if (this.attributes != null) {
            SunPageSelection sunPageSelection = (SunPageSelection) this.attributes.get(SunPageSelection.class);
            if (sunPageSelection == SunPageSelection.RANGE) {
                return 2;
            }
            if (sunPageSelection == SunPageSelection.SELECTION) {
                return 1;
            }
            if (sunPageSelection == SunPageSelection.ALL) {
                return 0;
            }
            return 4;
        }
        return 4;
    }

    protected final int getFromPageAttrib() {
        PageRanges pageRanges;
        if (this.attributes != null && (pageRanges = (PageRanges) this.attributes.get(PageRanges.class)) != null) {
            return pageRanges.getMembers()[0][0];
        }
        return getMinPageAttrib();
    }

    protected final int getToPageAttrib() {
        PageRanges pageRanges;
        if (this.attributes != null && (pageRanges = (PageRanges) this.attributes.get(PageRanges.class)) != null) {
            int[][] members = pageRanges.getMembers();
            return members[members.length - 1][1];
        }
        return getMaxPageAttrib();
    }

    protected final int getMinPageAttrib() {
        SunMinMaxPage sunMinMaxPage;
        if (this.attributes != null && (sunMinMaxPage = (SunMinMaxPage) this.attributes.get(SunMinMaxPage.class)) != null) {
            return sunMinMaxPage.getMin();
        }
        return 1;
    }

    protected final int getMaxPageAttrib() {
        SunMinMaxPage sunMinMaxPage;
        if (this.attributes != null && (sunMinMaxPage = (SunMinMaxPage) this.attributes.get(SunMinMaxPage.class)) != null) {
            return sunMinMaxPage.getMax();
        }
        Pageable pageable = getPageable();
        if (pageable != null) {
            int numberOfPages = pageable.getNumberOfPages();
            if (numberOfPages <= -1) {
                numberOfPages = MAX_UNKNOWN_PAGES;
            }
            if (numberOfPages == 0) {
                return 1;
            }
            return numberOfPages;
        }
        return Integer.MAX_VALUE;
    }

    protected void cancelDoc() throws PrinterAbortException {
        abortDoc();
        synchronized (this) {
            this.userCancelled = false;
            this.performingPrinting = false;
            notify();
        }
        throw new PrinterAbortException();
    }

    protected int getCollatedCopies() {
        if (isCollated()) {
            return getCopiesInt();
        }
        return 1;
    }

    protected int getNoncollatedCopies() {
        if (isCollated()) {
            return 1;
        }
        return getCopiesInt();
    }

    synchronized void setGraphicsConfigInfo(AffineTransform affineTransform, double d2, double d3) {
        Point2D.Double r0 = new Point2D.Double(d2, d3);
        affineTransform.transform(r0, r0);
        if (this.pgConfig == null || this.defaultDeviceTransform == null || !affineTransform.equals(this.defaultDeviceTransform) || this.deviceWidth != ((int) r0.getX()) || this.deviceHeight != ((int) r0.getY())) {
            this.deviceWidth = (int) r0.getX();
            this.deviceHeight = (int) r0.getY();
            this.defaultDeviceTransform = affineTransform;
            this.pgConfig = null;
        }
    }

    synchronized PrinterGraphicsConfig getPrinterGraphicsConfig() {
        if (this.pgConfig != null) {
            return this.pgConfig;
        }
        String string = "Printer Device";
        PrintService printService = getPrintService();
        if (printService != null) {
            string = printService.toString();
        }
        this.pgConfig = new PrinterGraphicsConfig(string, this.defaultDeviceTransform, this.deviceWidth, this.deviceHeight);
        return this.pgConfig;
    }

    protected int printPage(Pageable pageable, int i2) throws PrinterException, IllegalArgumentException {
        try {
            PageFormat pageFormat = pageable.getPageFormat(i2);
            PageFormat pageFormat2 = (PageFormat) pageFormat.clone();
            Printable printable = pageable.getPrintable(i2);
            Paper paper = pageFormat2.getPaper();
            if (pageFormat2.getOrientation() != 1 && this.landscapeRotates270) {
                double imageableX = paper.getImageableX();
                double imageableY = paper.getImageableY();
                double imageableWidth = paper.getImageableWidth();
                double imageableHeight = paper.getImageableHeight();
                paper.setImageableArea((paper.getWidth() - imageableX) - imageableWidth, (paper.getHeight() - imageableY) - imageableHeight, imageableWidth, imageableHeight);
                pageFormat2.setPaper(paper);
                if (pageFormat2.getOrientation() == 0) {
                    pageFormat2.setOrientation(2);
                } else {
                    pageFormat2.setOrientation(0);
                }
            }
            double xRes = getXRes() / 72.0d;
            double yRes = getYRes() / 72.0d;
            Rectangle2D.Double r0 = new Rectangle2D.Double(paper.getImageableX() * xRes, paper.getImageableY() * yRes, paper.getImageableWidth() * xRes, paper.getImageableHeight() * yRes);
            AffineTransform affineTransform = new AffineTransform();
            AffineTransform affineTransform2 = new AffineTransform();
            affineTransform2.scale(xRes, yRes);
            int width = (int) r0.getWidth();
            if (width % 4 != 0) {
                width += 4 - (width % 4);
            }
            if (width <= 0) {
                throw new PrinterException("Paper's imageable width is too small.");
            }
            int height = (int) r0.getHeight();
            if (height <= 0) {
                throw new PrinterException("Paper's imageable height is too small.");
            }
            int i3 = (4194304 / width) / 3;
            int iRint = (int) Math.rint(paper.getImageableX() * xRes);
            int iRint2 = (int) Math.rint(paper.getImageableY() * yRes);
            AffineTransform affineTransform3 = new AffineTransform();
            affineTransform3.translate(-iRint, iRint2);
            affineTransform3.translate(0.0d, i3);
            affineTransform3.scale(1.0d, -1.0d);
            PeekGraphics peekGraphicsCreatePeekGraphics = createPeekGraphics(new BufferedImage(1, 1, 5).createGraphics(), this);
            Rectangle2D rectangle2D = new Rectangle2D.Double(pageFormat2.getImageableX(), pageFormat2.getImageableY(), pageFormat2.getImageableWidth(), pageFormat2.getImageableHeight());
            peekGraphicsCreatePeekGraphics.transform(affineTransform2);
            peekGraphicsCreatePeekGraphics.translate((-getPhysicalPrintableX(paper)) / xRes, (-getPhysicalPrintableY(paper)) / yRes);
            peekGraphicsCreatePeekGraphics.transform(new AffineTransform(pageFormat2.getMatrix()));
            initPrinterGraphics(peekGraphicsCreatePeekGraphics, rectangle2D);
            AffineTransform transform = peekGraphicsCreatePeekGraphics.getTransform();
            setGraphicsConfigInfo(affineTransform2, paper.getWidth(), paper.getHeight());
            int iPrint = printable.print(peekGraphicsCreatePeekGraphics, pageFormat, i2);
            debug_println("pageResult " + iPrint);
            if (iPrint == 0) {
                debug_println("startPage " + i2);
                Paper paper2 = pageFormat2.getPaper();
                boolean z2 = (this.previousPaper != null && paper2.getWidth() == this.previousPaper.getWidth() && paper2.getHeight() == this.previousPaper.getHeight()) ? false : true;
                this.previousPaper = paper2;
                startPage(pageFormat2, printable, i2, z2);
                Graphics2D graphics2DCreatePathGraphics = createPathGraphics(peekGraphicsCreatePeekGraphics, this, printable, pageFormat2, i2);
                if (graphics2DCreatePathGraphics != null) {
                    graphics2DCreatePathGraphics.transform(affineTransform2);
                    graphics2DCreatePathGraphics.translate((-getPhysicalPrintableX(paper)) / xRes, (-getPhysicalPrintableY(paper)) / yRes);
                    graphics2DCreatePathGraphics.transform(new AffineTransform(pageFormat2.getMatrix()));
                    initPrinterGraphics(graphics2DCreatePathGraphics, rectangle2D);
                    this.redrawList.clear();
                    AffineTransform transform2 = graphics2DCreatePathGraphics.getTransform();
                    printable.print(graphics2DCreatePathGraphics, pageFormat, i2);
                    for (int i4 = 0; i4 < this.redrawList.size(); i4++) {
                        GraphicsState graphicsState = (GraphicsState) this.redrawList.get(i4);
                        graphics2DCreatePathGraphics.setTransform(transform2);
                        ((PathGraphics) graphics2DCreatePathGraphics).redrawRegion(graphicsState.region, graphicsState.sx, graphicsState.sy, graphicsState.theClip, graphicsState.theTransform);
                    }
                } else {
                    BufferedImage bufferedImage = this.cachedBand;
                    if (this.cachedBand == null || width != this.cachedBandWidth || i3 != this.cachedBandHeight) {
                        bufferedImage = new BufferedImage(width, i3, 5);
                        this.cachedBand = bufferedImage;
                        this.cachedBandWidth = width;
                        this.cachedBandHeight = i3;
                    }
                    Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
                    initPrinterGraphics(graphics2DCreateGraphics, new Rectangle2D.Double(0.0d, 0.0d, width, i3));
                    ProxyGraphics2D proxyGraphics2D = new ProxyGraphics2D(graphics2DCreateGraphics, this);
                    Graphics2D graphics2DCreateGraphics2 = bufferedImage.createGraphics();
                    graphics2DCreateGraphics2.setColor(Color.white);
                    byte[] dataStorage = ((ByteInterleavedRaster) bufferedImage.getRaster()).getDataStorage();
                    int i5 = iRint2 + height;
                    int physicalPrintableX = (int) getPhysicalPrintableX(paper);
                    int physicalPrintableY = (int) getPhysicalPrintableY(paper);
                    int i6 = 0;
                    while (true) {
                        int i7 = i6;
                        if (i7 > height) {
                            break;
                        }
                        graphics2DCreateGraphics2.fillRect(0, 0, width, i3);
                        graphics2DCreateGraphics.setTransform(affineTransform);
                        graphics2DCreateGraphics.transform(affineTransform3);
                        affineTransform3.translate(0.0d, -i3);
                        graphics2DCreateGraphics.transform(affineTransform2);
                        graphics2DCreateGraphics.transform(new AffineTransform(pageFormat2.getMatrix()));
                        Rectangle bounds = transform.createTransformedShape(graphics2DCreateGraphics.getClipBounds()).getBounds();
                        if (bounds == null || (peekGraphicsCreatePeekGraphics.hitsDrawingArea(bounds) && width > 0 && i3 > 0)) {
                            int i8 = iRint - physicalPrintableX;
                            if (i8 < 0) {
                                graphics2DCreateGraphics.translate(i8 / xRes, 0.0d);
                                i8 = 0;
                            }
                            int i9 = (iRint2 + i7) - physicalPrintableY;
                            if (i9 < 0) {
                                graphics2DCreateGraphics.translate(0.0d, i9 / yRes);
                                i9 = 0;
                            }
                            proxyGraphics2D.setDelegate((Graphics2D) graphics2DCreateGraphics.create());
                            printable.print(proxyGraphics2D, pageFormat, i2);
                            proxyGraphics2D.dispose();
                            printBand(dataStorage, i8, i9, width, i3);
                        }
                        i6 = i7 + i3;
                    }
                    graphics2DCreateGraphics2.dispose();
                    graphics2DCreateGraphics.dispose();
                }
                debug_println("calling endPage " + i2);
                endPage(pageFormat2, printable, i2);
            }
            return iPrint;
        } catch (Exception e2) {
            PrinterException printerException = new PrinterException("Error getting page or printable.[ " + ((Object) e2) + " ]");
            printerException.initCause(e2);
            throw printerException;
        }
    }

    @Override // java.awt.print.PrinterJob
    public void cancel() {
        synchronized (this) {
            if (this.performingPrinting) {
                this.userCancelled = true;
            }
            notify();
        }
    }

    @Override // java.awt.print.PrinterJob
    public boolean isCancelled() {
        boolean z2;
        synchronized (this) {
            z2 = this.performingPrinting && this.userCancelled;
            notify();
        }
        return z2;
    }

    protected Pageable getPageable() {
        return this.mDocument;
    }

    protected Graphics2D createPathGraphics(PeekGraphics peekGraphics, PrinterJob printerJob, Printable printable, PageFormat pageFormat, int i2) {
        return null;
    }

    protected PeekGraphics createPeekGraphics(Graphics2D graphics2D, PrinterJob printerJob) {
        return new PeekGraphics(graphics2D, printerJob);
    }

    protected void initPrinterGraphics(Graphics2D graphics2D, Rectangle2D rectangle2D) {
        graphics2D.setClip(rectangle2D);
        graphics2D.setPaint(Color.black);
    }

    public boolean checkAllowedToPrintToFile() {
        try {
            throwPrintToFile();
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    private void throwPrintToFile() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (this.printToFilePermission == null) {
                this.printToFilePermission = new FilePermission("<<ALL FILES>>", SecurityConstants.PROPERTY_RW_ACTION);
            }
            securityManager.checkPermission(this.printToFilePermission);
        }
    }

    protected String removeControlChars(String str) {
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        char[] cArr = new char[length];
        int i2 = 0;
        for (char c2 : charArray) {
            if (c2 > '\r' || c2 < '\t' || c2 == 11 || c2 == '\f') {
                int i3 = i2;
                i2++;
                cArr[i3] = c2;
            }
        }
        if (i2 == length) {
            return str;
        }
        return new String(cArr, 0, i2);
    }

    private long getParentWindowID() {
        return this.parentWindowID;
    }

    private void clearParentWindowID() {
        this.parentWindowID = 0L;
        this.onTop = null;
    }

    private void setParentWindowID(PrintRequestAttributeSet printRequestAttributeSet) {
        this.parentWindowID = 0L;
        this.onTop = (DialogOnTop) printRequestAttributeSet.get(DialogOnTop.class);
        if (this.onTop != null) {
            this.parentWindowID = this.onTop.getID();
        }
    }
}
