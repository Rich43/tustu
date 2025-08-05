package java.awt.print;

import java.awt.AWTError;
import java.awt.HeadlessException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

/* loaded from: rt.jar:java/awt/print/PrinterJob.class */
public abstract class PrinterJob {
    public abstract void setPrintable(Printable printable);

    public abstract void setPrintable(Printable printable, PageFormat pageFormat);

    public abstract void setPageable(Pageable pageable) throws NullPointerException;

    public abstract boolean printDialog() throws HeadlessException;

    public abstract PageFormat pageDialog(PageFormat pageFormat) throws HeadlessException;

    public abstract PageFormat defaultPage(PageFormat pageFormat);

    public abstract PageFormat validatePage(PageFormat pageFormat);

    public abstract void print() throws PrinterException;

    public abstract void setCopies(int i2);

    public abstract int getCopies();

    public abstract String getUserName();

    public abstract void setJobName(String str);

    public abstract String getJobName();

    public abstract void cancel();

    public abstract boolean isCancelled();

    public static PrinterJob getPrinterJob() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        return (PrinterJob) AccessController.doPrivileged(new PrivilegedAction() { // from class: java.awt.print.PrinterJob.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                String property = System.getProperty("java.awt.printerjob", null);
                try {
                    return (PrinterJob) Class.forName(property).newInstance();
                } catch (ClassNotFoundException e2) {
                    throw new AWTError("PrinterJob not found: " + property);
                } catch (IllegalAccessException e3) {
                    throw new AWTError("Could not access PrinterJob: " + property);
                } catch (InstantiationException e4) {
                    throw new AWTError("Could not instantiate PrinterJob: " + property);
                }
            }
        });
    }

    public static PrintService[] lookupPrintServices() {
        return PrintServiceLookup.lookupPrintServices(DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    }

    public static StreamPrintServiceFactory[] lookupStreamPrintServices(String str) {
        return StreamPrintServiceFactory.lookupStreamPrintServiceFactories(DocFlavor.SERVICE_FORMATTED.PAGEABLE, str);
    }

    public PrintService getPrintService() {
        return null;
    }

    public void setPrintService(PrintService printService) throws PrinterException {
        throw new PrinterException("Setting a service is not supported on this class");
    }

    public boolean printDialog(PrintRequestAttributeSet printRequestAttributeSet) throws HeadlessException {
        if (printRequestAttributeSet == null) {
            throw new NullPointerException("attributes");
        }
        return printDialog();
    }

    public PageFormat pageDialog(PrintRequestAttributeSet printRequestAttributeSet) throws HeadlessException {
        if (printRequestAttributeSet == null) {
            throw new NullPointerException("attributes");
        }
        return pageDialog(defaultPage());
    }

    public PageFormat defaultPage() {
        return defaultPage(new PageFormat());
    }

    public PageFormat getPageFormat(PrintRequestAttributeSet printRequestAttributeSet) throws IllegalArgumentException {
        int i2;
        MediaSize mediaSizeForName;
        PrintService printService = getPrintService();
        PageFormat pageFormatDefaultPage = defaultPage();
        if (printService == null || printRequestAttributeSet == null) {
            return pageFormatDefaultPage;
        }
        Media media = (Media) printRequestAttributeSet.get(Media.class);
        MediaPrintableArea mediaPrintableArea = (MediaPrintableArea) printRequestAttributeSet.get(MediaPrintableArea.class);
        OrientationRequested orientationRequested = (OrientationRequested) printRequestAttributeSet.get(OrientationRequested.class);
        if (media == null && mediaPrintableArea == null && orientationRequested == null) {
            return pageFormatDefaultPage;
        }
        Paper paper = pageFormatDefaultPage.getPaper();
        if (mediaPrintableArea == null && media != null && printService.isAttributeCategorySupported(MediaPrintableArea.class)) {
            Object supportedAttributeValues = printService.getSupportedAttributeValues(MediaPrintableArea.class, null, printRequestAttributeSet);
            if ((supportedAttributeValues instanceof MediaPrintableArea[]) && ((MediaPrintableArea[]) supportedAttributeValues).length > 0) {
                mediaPrintableArea = ((MediaPrintableArea[]) supportedAttributeValues)[0];
            }
        }
        if (media != null && printService.isAttributeValueSupported(media, null, printRequestAttributeSet) && (media instanceof MediaSizeName) && (mediaSizeForName = MediaSize.getMediaSizeForName((MediaSizeName) media)) != null) {
            double x2 = mediaSizeForName.getX(25400) * 72.0d;
            double y2 = mediaSizeForName.getY(25400) * 72.0d;
            paper.setSize(x2, y2);
            if (mediaPrintableArea == null) {
                paper.setImageableArea(72.0d, 72.0d, x2 - (2.0d * 72.0d), y2 - (2.0d * 72.0d));
            }
        }
        if (mediaPrintableArea != null && printService.isAttributeValueSupported(mediaPrintableArea, null, printRequestAttributeSet)) {
            float[] printableArea = mediaPrintableArea.getPrintableArea(25400);
            for (int i3 = 0; i3 < printableArea.length; i3++) {
                printableArea[i3] = printableArea[i3] * 72.0f;
            }
            paper.setImageableArea(printableArea[0], printableArea[1], printableArea[2], printableArea[3]);
        }
        if (orientationRequested != null && printService.isAttributeValueSupported(orientationRequested, null, printRequestAttributeSet)) {
            if (orientationRequested.equals(OrientationRequested.REVERSE_LANDSCAPE)) {
                i2 = 2;
            } else if (orientationRequested.equals(OrientationRequested.LANDSCAPE)) {
                i2 = 0;
            } else {
                i2 = 1;
            }
            pageFormatDefaultPage.setOrientation(i2);
        }
        pageFormatDefaultPage.setPaper(paper);
        return validatePage(pageFormatDefaultPage);
    }

    public void print(PrintRequestAttributeSet printRequestAttributeSet) throws PrinterException {
        print();
    }
}
