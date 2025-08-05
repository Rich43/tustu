package org.icepdf.ri.common;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Window;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.CancelablePrintJob;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.swing.SwingUtilities;
import org.icepdf.core.pobjects.PDimension;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/PrintHelper.class */
public class PrintHelper implements Printable {
    private static final Logger logger = Logger.getLogger(PrintHelper.class.toString());
    private PageTree pageTree;
    private Container container;
    private float userRotation;
    private boolean printFitToMargin;
    private int printingCurrentPage;
    private int totalPagesToPrint;
    private boolean paintAnnotation;
    private boolean paintSearchHighlight;
    private static PrintService[] services;
    private PrintService printService;
    private HashDocAttributeSet docAttributeSet;
    private HashPrintRequestAttributeSet printRequestAttributeSet;

    public PrintHelper(Container container, PageTree pageTree, int rotation) {
        this(container, pageTree, rotation, MediaSizeName.NA_LETTER, PrintQuality.DRAFT);
    }

    public PrintHelper(Container container, PageTree pageTree, float rotation, MediaSizeName paperSizeName, PrintQuality printQuality) {
        this.paintAnnotation = true;
        this.paintSearchHighlight = true;
        this.container = container;
        this.pageTree = pageTree;
        this.userRotation = rotation;
        services = lookForPrintServices();
        this.printRequestAttributeSet = new HashPrintRequestAttributeSet();
        this.docAttributeSet = new HashDocAttributeSet();
        this.printRequestAttributeSet.add(printQuality);
        this.printRequestAttributeSet.add(paperSizeName);
        this.docAttributeSet.add(paperSizeName);
        MediaSize mediaSize = MediaSize.getMediaSizeForName(paperSizeName);
        float[] size = mediaSize.getSize(25400);
        this.printRequestAttributeSet.add(new MediaPrintableArea(0.0f, 0.0f, size[0], size[1], 25400));
        this.docAttributeSet.add(new MediaPrintableArea(0.0f, 0.0f, size[0], size[1], 25400));
        setupPrintService(0, this.pageTree.getNumberOfPages(), 1, true, false);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Paper Size: " + paperSizeName.getName() + " " + size[0] + " x " + size[1]);
        }
    }

    public PrintHelper(Container container, PageTree pageTree, float userRotation, HashDocAttributeSet docAttributeSet, HashPrintRequestAttributeSet printRequestAttributeSet) {
        this.paintAnnotation = true;
        this.paintSearchHighlight = true;
        this.container = container;
        this.pageTree = pageTree;
        this.userRotation = userRotation;
        this.docAttributeSet = docAttributeSet;
        this.printRequestAttributeSet = printRequestAttributeSet;
        services = lookForPrintServices();
        setupPrintService(0, this.pageTree.getNumberOfPages(), 1, true, false);
    }

    public boolean setupPrintService(int startPage, int endPage, int copies, boolean shrinkToPrintableArea, boolean showPrintDialog) {
        this.printFitToMargin = shrinkToPrintableArea;
        this.printRequestAttributeSet.add(new PageRanges(startPage + 1, endPage + 1));
        this.printRequestAttributeSet.add(new Copies(copies));
        if (showPrintDialog) {
            this.printService = getSetupDialog();
            return this.printService != null;
        }
        return true;
    }

    public void setupPrintService(PrintService printService, int startPage, int endPage, int copies, boolean shrinkToPrintableArea) {
        this.printFitToMargin = shrinkToPrintableArea;
        this.printRequestAttributeSet.add(new PageRanges(startPage + 1, endPage + 1));
        this.printRequestAttributeSet.add(new Copies(copies));
        this.printService = printService;
    }

    public void setupPrintService(PrintService printService, HashPrintRequestAttributeSet printRequestAttributeSet, boolean shrinkToPrintableArea) {
        this.printFitToMargin = shrinkToPrintableArea;
        this.printRequestAttributeSet = printRequestAttributeSet;
        this.printService = printService;
    }

    public void showPrintSetupDialog() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (this.printService == null && services != null && services.length > 0 && services[0] != null) {
            this.printService = services[0];
        }
        try {
            pj.setPrintService(this.printService);
            pj.pageDialog(this.printRequestAttributeSet);
        } catch (Throwable e2) {
            logger.log(Level.FINE, "Error creating page setup dialog.", e2);
        }
    }

    public int getCurrentPage() {
        return this.printingCurrentPage;
    }

    public int getNumberOfPages() {
        return this.totalPagesToPrint;
    }

    public boolean isPrintFitToMargin() {
        return this.printFitToMargin;
    }

    public float getUserRotation() {
        return this.userRotation;
    }

    public HashDocAttributeSet getDocAttributeSet() {
        return this.docAttributeSet;
    }

    public HashPrintRequestAttributeSet getPrintRequestAttributeSet() {
        return this.printRequestAttributeSet;
    }

    public PrintService getPrintService() {
        return this.printService;
    }

    @Override // java.awt.print.Printable
    public int print(Graphics printGraphics, PageFormat pageFormat, int pageIndex) {
        Dimension imageablePrintSize;
        if (this.printingCurrentPage != pageIndex) {
            this.printingCurrentPage = pageIndex + 1;
        }
        if (pageIndex < 0 || pageIndex >= this.pageTree.getNumberOfPages()) {
            return 1;
        }
        Page currentPage = this.pageTree.getPage(pageIndex);
        currentPage.init();
        PDimension pageDim = currentPage.getSize(this.userRotation);
        float pageWidth = pageDim.getWidth();
        float pageHeight = pageDim.getHeight();
        float zoomFactor = 1.0f;
        Point imageablePrintLocation = new Point();
        float rotation = this.userRotation;
        boolean isDefaultRotation = true;
        if (pageWidth > pageHeight && pageFormat.getOrientation() == 1) {
            isDefaultRotation = false;
            rotation -= 90.0f;
        }
        if (this.printFitToMargin) {
            if (isDefaultRotation) {
                imageablePrintSize = new Dimension((int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight());
            } else {
                imageablePrintSize = new Dimension((int) pageFormat.getImageableHeight(), (int) pageFormat.getImageableWidth());
            }
            float zw = imageablePrintSize.width / pageWidth;
            float zh = imageablePrintSize.height / pageHeight;
            zoomFactor = Math.min(zw, zh);
            imageablePrintLocation.f12370x = (int) pageFormat.getImageableX();
            imageablePrintLocation.f12371y = (int) pageFormat.getImageableY();
        }
        printGraphics.translate(imageablePrintLocation.f12370x, imageablePrintLocation.f12371y);
        currentPage.paint(printGraphics, 2, 2, rotation, zoomFactor, this.paintAnnotation, this.paintSearchHighlight);
        return 0;
    }

    public void print() throws PrintException {
        if (this.printService == null && services != null && services.length > 0 && services[0] != null) {
            this.printService = services[0];
        }
        if (this.printService != null) {
            calculateTotalPagesToPrint();
            this.printService.createPrintJob().print(new SimpleDoc(this, DocFlavor.SERVICE_FORMATTED.PRINTABLE, null), this.printRequestAttributeSet);
        } else {
            logger.fine("No print could be found to print to.");
        }
    }

    public CancelablePrintJob cancelablePrint() throws PrintException {
        if (this.printService == null && services != null && services.length > 0 && services[0] != null) {
            this.printService = services[0];
        }
        if (this.printService != null) {
            calculateTotalPagesToPrint();
            DocPrintJob printerJob = this.printService.createPrintJob();
            printerJob.print(new SimpleDoc(this, DocFlavor.SERVICE_FORMATTED.PRINTABLE, null), this.printRequestAttributeSet);
            return (CancelablePrintJob) printerJob;
        }
        return null;
    }

    public void print(PrintJobWatcher printJobWatcher) throws PrintException {
        if (this.printService == null && services != null && services.length > 0 && services[0] != null) {
            this.printService = services[0];
        }
        if (this.printService != null) {
            calculateTotalPagesToPrint();
            DocPrintJob printerJob = this.printService.createPrintJob();
            printJobWatcher.setPrintJob(printerJob);
            printerJob.print(new SimpleDoc(this, DocFlavor.SERVICE_FORMATTED.PRINTABLE, null), this.printRequestAttributeSet);
            printJobWatcher.waitForDone();
            return;
        }
        logger.fine("No print could be found to print to.");
    }

    private PrintService getSetupDialog() {
        Window window = SwingUtilities.getWindowAncestor(this.container);
        GraphicsConfiguration graphicsConfiguration = window == null ? null : window.getGraphicsConfiguration();
        return ServiceUI.printDialog(graphicsConfiguration, this.container.getX() + 50, this.container.getY() + 50, services, services[0], DocFlavor.SERVICE_FORMATTED.PRINTABLE, this.printRequestAttributeSet);
    }

    private void calculateTotalPagesToPrint() {
        PageRanges pageRanges = (PageRanges) this.printRequestAttributeSet.get(PageRanges.class);
        this.totalPagesToPrint = 0;
        int[][] arr$ = pageRanges.getMembers();
        for (int[] ranges : arr$) {
            int start = ranges[0];
            int end = ranges[1];
            if (start < 1) {
                start = 1;
            }
            if (end > this.pageTree.getNumberOfPages()) {
                end = this.pageTree.getNumberOfPages();
            }
            this.totalPagesToPrint += (end - start) + 1;
        }
    }

    private PrintService[] lookForPrintServices() {
        PrintService[] services2 = PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        if (defaultService != null && services2.length > 1) {
            int i2 = 1;
            int max = services2.length;
            while (true) {
                if (i2 >= max) {
                    break;
                }
                PrintService printService = services2[i2];
                if (!printService.equals(defaultService)) {
                    i2++;
                } else {
                    PrintService tmp = services2[0];
                    services2[0] = defaultService;
                    services2[i2] = tmp;
                    break;
                }
            }
        }
        return services2;
    }

    public boolean isPaintAnnotation() {
        return this.paintAnnotation;
    }

    public void setPaintAnnotation(boolean paintAnnotation) {
        this.paintAnnotation = paintAnnotation;
    }

    public boolean isPaintSearchHighlight() {
        return this.paintSearchHighlight;
    }

    public void setPaintSearchHighlight(boolean paintSearchHighlight) {
        this.paintSearchHighlight = paintSearchHighlight;
    }
}
