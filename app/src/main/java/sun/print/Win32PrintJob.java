package sun.print;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.Vector;
import javax.print.CancelablePrintJob;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.DocumentName;
import javax.print.attribute.standard.Fidelity;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.JobOriginatingUserName;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;
import javax.print.attribute.standard.PrinterStateReasons;
import javax.print.attribute.standard.RequestingUserName;
import javax.print.event.PrintJobAttributeListener;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import sun.awt.windows.WPrinterJob;

/* loaded from: rt.jar:sun/print/Win32PrintJob.class */
public class Win32PrintJob implements CancelablePrintJob {
    private transient Vector jobListeners;
    private transient Vector attrListeners;
    private transient Vector listenedAttributeSets;
    private Win32PrintService service;
    private boolean fidelity;
    private PrinterJob job;
    private Doc doc;
    private long hPrintJob;
    private static final int PRINTBUFFERLEN = 8192;
    private boolean printing = false;
    private boolean printReturned = false;
    private PrintRequestAttributeSet reqAttrSet = null;
    private PrintJobAttributeSet jobAttrSet = null;
    private String mDestination = null;
    private InputStream instream = null;
    private Reader reader = null;
    private String jobName = "Java Printing";
    private int copies = 0;
    private MediaSizeName mediaName = null;
    private MediaSize mediaSize = null;
    private OrientationRequested orient = null;

    private native boolean startPrintRawData(String str, String str2);

    private native boolean printRawData(byte[] bArr, int i2);

    private native boolean endPrintRawData();

    Win32PrintJob(Win32PrintService win32PrintService) {
        this.service = win32PrintService;
    }

    @Override // javax.print.DocPrintJob
    public PrintService getPrintService() {
        return this.service;
    }

    @Override // javax.print.DocPrintJob
    public PrintJobAttributeSet getAttributes() {
        synchronized (this) {
            if (this.jobAttrSet == null) {
                return AttributeSetUtilities.unmodifiableView((PrintJobAttributeSet) new HashPrintJobAttributeSet());
            }
            return this.jobAttrSet;
        }
    }

    @Override // javax.print.DocPrintJob
    public void addPrintJobListener(PrintJobListener printJobListener) {
        synchronized (this) {
            if (printJobListener == null) {
                return;
            }
            if (this.jobListeners == null) {
                this.jobListeners = new Vector();
            }
            this.jobListeners.add(printJobListener);
        }
    }

    @Override // javax.print.DocPrintJob
    public void removePrintJobListener(PrintJobListener printJobListener) {
        synchronized (this) {
            if (printJobListener != null) {
                if (this.jobListeners != null) {
                    this.jobListeners.remove(printJobListener);
                    if (this.jobListeners.isEmpty()) {
                        this.jobListeners = null;
                    }
                }
            }
        }
    }

    private void closeDataStreams() {
        if (this.doc == null) {
            return;
        }
        try {
            Object printData = this.doc.getPrintData();
            if (this.instream != null) {
                try {
                    this.instream.close();
                    return;
                } catch (IOException e2) {
                    return;
                } finally {
                    this.instream = null;
                }
            }
            if (this.reader != null) {
                try {
                    this.reader.close();
                    this.reader = null;
                    return;
                } catch (IOException e3) {
                    this.reader = null;
                    return;
                } catch (Throwable th) {
                    this.reader = null;
                    throw th;
                }
            }
            if (printData instanceof InputStream) {
                try {
                    ((InputStream) printData).close();
                } catch (IOException e4) {
                }
            } else if (printData instanceof Reader) {
                try {
                    ((Reader) printData).close();
                } catch (IOException e5) {
                }
            }
        } catch (IOException e6) {
        }
    }

    private void notifyEvent(int i2) {
        switch (i2) {
            case 101:
            case 102:
            case 103:
            case 105:
            case 106:
                closeDataStreams();
                break;
        }
        synchronized (this) {
            if (this.jobListeners != null) {
                PrintJobEvent printJobEvent = new PrintJobEvent(this, i2);
                for (int i3 = 0; i3 < this.jobListeners.size(); i3++) {
                    PrintJobListener printJobListener = (PrintJobListener) this.jobListeners.elementAt(i3);
                    switch (i2) {
                        case 101:
                            printJobListener.printJobCanceled(printJobEvent);
                            break;
                        case 102:
                            printJobListener.printJobCompleted(printJobEvent);
                            break;
                        case 103:
                            printJobListener.printJobFailed(printJobEvent);
                            break;
                        case 105:
                            printJobListener.printJobNoMoreEvents(printJobEvent);
                            break;
                        case 106:
                            printJobListener.printDataTransferCompleted(printJobEvent);
                            break;
                    }
                }
            }
        }
    }

    @Override // javax.print.DocPrintJob
    public void addPrintJobAttributeListener(PrintJobAttributeListener printJobAttributeListener, PrintJobAttributeSet printJobAttributeSet) {
        synchronized (this) {
            if (printJobAttributeListener == null) {
                return;
            }
            if (this.attrListeners == null) {
                this.attrListeners = new Vector();
                this.listenedAttributeSets = new Vector();
            }
            this.attrListeners.add(printJobAttributeListener);
            if (printJobAttributeSet == null) {
                printJobAttributeSet = new HashPrintJobAttributeSet();
            }
            this.listenedAttributeSets.add(printJobAttributeSet);
        }
    }

    @Override // javax.print.DocPrintJob
    public void removePrintJobAttributeListener(PrintJobAttributeListener printJobAttributeListener) {
        synchronized (this) {
            if (printJobAttributeListener != null) {
                if (this.attrListeners != null) {
                    int iIndexOf = this.attrListeners.indexOf(printJobAttributeListener);
                    if (iIndexOf == -1) {
                        return;
                    }
                    this.attrListeners.remove(iIndexOf);
                    this.listenedAttributeSets.remove(iIndexOf);
                    if (this.attrListeners.isEmpty()) {
                        this.attrListeners = null;
                        this.listenedAttributeSets = null;
                    }
                }
            }
        }
    }

    @Override // javax.print.DocPrintJob
    public void print(Doc doc, PrintRequestAttributeSet printRequestAttributeSet) throws PrintException {
        int i2;
        PrinterStateReasons printerStateReasons;
        synchronized (this) {
            if (this.printing) {
                throw new PrintException("already printing");
            }
            this.printing = true;
        }
        if (((PrinterState) this.service.getAttribute(PrinterState.class)) == PrinterState.STOPPED && (printerStateReasons = (PrinterStateReasons) this.service.getAttribute(PrinterStateReasons.class)) != null && printerStateReasons.containsKey(PrinterStateReason.SHUTDOWN)) {
            throw new PrintException("PrintService is no longer available.");
        }
        if (((PrinterIsAcceptingJobs) this.service.getAttribute(PrinterIsAcceptingJobs.class)) == PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS) {
            throw new PrintException("Printer is not accepting job.");
        }
        this.doc = doc;
        DocFlavor docFlavor = doc.getDocFlavor();
        try {
            Object printData = doc.getPrintData();
            if (printData == null) {
                throw new PrintException("Null print data.");
            }
            if (docFlavor == null || !this.service.isDocFlavorSupported(docFlavor)) {
                notifyEvent(103);
                throw new PrintJobFlavorException("invalid flavor", docFlavor);
            }
            initializeAttributeSets(doc, printRequestAttributeSet);
            getAttributeValues(docFlavor);
            String representationClassName = docFlavor.getRepresentationClassName();
            if (docFlavor.equals(DocFlavor.INPUT_STREAM.GIF) || docFlavor.equals(DocFlavor.INPUT_STREAM.JPEG) || docFlavor.equals(DocFlavor.INPUT_STREAM.PNG) || docFlavor.equals(DocFlavor.BYTE_ARRAY.GIF) || docFlavor.equals(DocFlavor.BYTE_ARRAY.JPEG) || docFlavor.equals(DocFlavor.BYTE_ARRAY.PNG)) {
                try {
                    this.instream = doc.getStreamForBytes();
                    if (this.instream == null) {
                        notifyEvent(103);
                        throw new PrintException("No stream for data");
                    }
                    printableJob(new ImagePrinter(this.instream));
                    this.service.wakeNotifier();
                    return;
                } catch (IOException e2) {
                    notifyEvent(103);
                    throw new PrintException(e2);
                } catch (ClassCastException e3) {
                    notifyEvent(103);
                    throw new PrintException(e3);
                }
            }
            if (docFlavor.equals(DocFlavor.URL.GIF) || docFlavor.equals(DocFlavor.URL.JPEG) || docFlavor.equals(DocFlavor.URL.PNG)) {
                try {
                    printableJob(new ImagePrinter((URL) printData));
                    this.service.wakeNotifier();
                    return;
                } catch (ClassCastException e4) {
                    notifyEvent(103);
                    throw new PrintException(e4);
                }
            }
            if (representationClassName.equals("java.awt.print.Pageable")) {
                try {
                    pageableJob((Pageable) doc.getPrintData());
                    this.service.wakeNotifier();
                    return;
                } catch (IOException e5) {
                    notifyEvent(103);
                    throw new PrintException(e5);
                } catch (ClassCastException e6) {
                    notifyEvent(103);
                    throw new PrintException(e6);
                }
            }
            if (representationClassName.equals("java.awt.print.Printable")) {
                try {
                    printableJob((Printable) doc.getPrintData());
                    this.service.wakeNotifier();
                    return;
                } catch (IOException e7) {
                    notifyEvent(103);
                    throw new PrintException(e7);
                } catch (ClassCastException e8) {
                    notifyEvent(103);
                    throw new PrintException(e8);
                }
            }
            if (representationClassName.equals("[B") || representationClassName.equals("java.io.InputStream") || representationClassName.equals("java.net.URL")) {
                if (representationClassName.equals("java.net.URL")) {
                    try {
                        this.instream = ((URL) printData).openStream();
                    } catch (IOException e9) {
                        notifyEvent(103);
                        throw new PrintException(e9.toString());
                    }
                } else {
                    try {
                        this.instream = doc.getStreamForBytes();
                    } catch (IOException e10) {
                        notifyEvent(103);
                        throw new PrintException(e10.toString());
                    }
                }
                if (this.instream == null) {
                    notifyEvent(103);
                    throw new PrintException("No stream for data");
                }
                if (this.mDestination != null) {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(this.mDestination);
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int i3 = this.instream.read(bArr, 0, bArr.length);
                            if (i3 >= 0) {
                                fileOutputStream.write(bArr, 0, i3);
                            } else {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                notifyEvent(106);
                                notifyEvent(102);
                                this.service.wakeNotifier();
                                return;
                            }
                        }
                    } catch (FileNotFoundException e11) {
                        notifyEvent(103);
                        throw new PrintException(e11.toString());
                    } catch (IOException e12) {
                        notifyEvent(103);
                        throw new PrintException(e12.toString());
                    }
                } else {
                    if (!startPrintRawData(this.service.getName(), this.jobName)) {
                        notifyEvent(103);
                        throw new PrintException("Print job failed to start.");
                    }
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(this.instream);
                    try {
                        try {
                            byte[] bArr2 = new byte[8192];
                            do {
                                i2 = bufferedInputStream.read(bArr2, 0, 8192);
                                if (i2 < 0) {
                                    bufferedInputStream.close();
                                    if (!endPrintRawData()) {
                                        notifyEvent(103);
                                        throw new PrintException("Print job failed to close properly.");
                                    }
                                    notifyEvent(106);
                                    notifyEvent(105);
                                    this.service.wakeNotifier();
                                    return;
                                }
                            } while (printRawData(bArr2, i2));
                            bufferedInputStream.close();
                            notifyEvent(103);
                            throw new PrintException("Problem while spooling data");
                        } catch (IOException e13) {
                            notifyEvent(103);
                            throw new PrintException(e13.toString());
                        }
                    } catch (Throwable th) {
                        notifyEvent(105);
                        throw th;
                    }
                }
            } else {
                notifyEvent(103);
                throw new PrintException("unrecognized class: " + representationClassName);
            }
        } catch (IOException e14) {
            notifyEvent(103);
            throw new PrintException("can't get print data: " + e14.toString());
        }
    }

    public void printableJob(Printable printable) throws PrintException {
        try {
            try {
                synchronized (this) {
                    if (this.job != null) {
                        throw new PrintException("already printing");
                    }
                    this.job = new WPrinterJob();
                }
                PrintService printService = getPrintService();
                this.job.setPrintService(printService);
                if (this.copies == 0) {
                    this.copies = ((Copies) printService.getDefaultAttributeValue(Copies.class)).getValue();
                }
                if (this.mediaName == null) {
                    Object defaultAttributeValue = printService.getDefaultAttributeValue(Media.class);
                    if (defaultAttributeValue instanceof MediaSizeName) {
                        this.mediaName = (MediaSizeName) defaultAttributeValue;
                        this.mediaSize = MediaSize.getMediaSizeForName(this.mediaName);
                    }
                }
                if (this.orient == null) {
                    this.orient = (OrientationRequested) printService.getDefaultAttributeValue(OrientationRequested.class);
                }
                this.job.setCopies(this.copies);
                this.job.setJobName(this.jobName);
                PageFormat pageFormat = new PageFormat();
                if (this.mediaSize != null) {
                    Paper paper = new Paper();
                    paper.setSize(this.mediaSize.getX(25400) * 72.0d, this.mediaSize.getY(25400) * 72.0d);
                    paper.setImageableArea(72.0d, 72.0d, paper.getWidth() - 144.0d, paper.getHeight() - 144.0d);
                    pageFormat.setPaper(paper);
                }
                if (this.orient == OrientationRequested.REVERSE_LANDSCAPE) {
                    pageFormat.setOrientation(2);
                } else if (this.orient == OrientationRequested.LANDSCAPE) {
                    pageFormat.setOrientation(0);
                }
                this.job.setPrintable(printable, pageFormat);
                this.job.print(this.reqAttrSet);
                notifyEvent(106);
                this.printReturned = true;
                notifyEvent(105);
            } catch (PrinterException e2) {
                notifyEvent(103);
                throw new PrintException(e2);
            }
        } catch (Throwable th) {
            this.printReturned = true;
            notifyEvent(105);
            throw th;
        }
    }

    public void pageableJob(Pageable pageable) throws PrintException {
        try {
            try {
                synchronized (this) {
                    if (this.job != null) {
                        throw new PrintException("already printing");
                    }
                    this.job = new WPrinterJob();
                }
                PrintService printService = getPrintService();
                this.job.setPrintService(printService);
                if (this.copies == 0) {
                    this.copies = ((Copies) printService.getDefaultAttributeValue(Copies.class)).getValue();
                }
                this.job.setCopies(this.copies);
                this.job.setJobName(this.jobName);
                this.job.setPageable(pageable);
                this.job.print(this.reqAttrSet);
                notifyEvent(106);
                this.printReturned = true;
                notifyEvent(105);
            } catch (PrinterException e2) {
                notifyEvent(103);
                throw new PrintException(e2);
            }
        } catch (Throwable th) {
            this.printReturned = true;
            notifyEvent(105);
            throw th;
        }
    }

    private synchronized void initializeAttributeSets(Doc doc, PrintRequestAttributeSet printRequestAttributeSet) {
        this.reqAttrSet = new HashPrintRequestAttributeSet();
        this.jobAttrSet = new HashPrintJobAttributeSet();
        if (printRequestAttributeSet != null) {
            this.reqAttrSet.addAll(printRequestAttributeSet);
            Attribute[] array = printRequestAttributeSet.toArray();
            for (int i2 = 0; i2 < array.length; i2++) {
                if (array[i2] instanceof PrintJobAttribute) {
                    this.jobAttrSet.add(array[i2]);
                }
            }
        }
        DocAttributeSet attributes = doc.getAttributes();
        if (attributes != null) {
            Attribute[] array2 = attributes.toArray();
            for (int i3 = 0; i3 < array2.length; i3++) {
                if (array2[i3] instanceof PrintRequestAttribute) {
                    this.reqAttrSet.add(array2[i3]);
                }
                if (array2[i3] instanceof PrintJobAttribute) {
                    this.jobAttrSet.add(array2[i3]);
                }
            }
        }
        String property = "";
        try {
            property = System.getProperty("user.name");
        } catch (SecurityException e2) {
        }
        if (property == null || property.equals("")) {
            RequestingUserName requestingUserName = (RequestingUserName) printRequestAttributeSet.get(RequestingUserName.class);
            if (requestingUserName != null) {
                this.jobAttrSet.add(new JobOriginatingUserName(requestingUserName.getValue(), requestingUserName.getLocale()));
            } else {
                this.jobAttrSet.add(new JobOriginatingUserName("", null));
            }
        } else {
            this.jobAttrSet.add(new JobOriginatingUserName(property, null));
        }
        if (this.jobAttrSet.get(JobName.class) == null) {
            if (attributes != null && attributes.get(DocumentName.class) != null) {
                DocumentName documentName = (DocumentName) attributes.get(DocumentName.class);
                this.jobAttrSet.add(new JobName(documentName.getValue(), documentName.getLocale()));
            } else {
                String string = "JPS Job:" + ((Object) doc);
                try {
                    if (doc.getPrintData() instanceof URL) {
                        string = ((URL) doc.getPrintData()).toString();
                    }
                } catch (IOException e3) {
                }
                this.jobAttrSet.add(new JobName(string, null));
            }
        }
        this.jobAttrSet = AttributeSetUtilities.unmodifiableView(this.jobAttrSet);
    }

    private void getAttributeValues(DocFlavor docFlavor) throws PrintException {
        if (this.reqAttrSet.get(Fidelity.class) == Fidelity.FIDELITY_TRUE) {
            this.fidelity = true;
        } else {
            this.fidelity = false;
        }
        for (Attribute attribute : this.reqAttrSet.toArray()) {
            Class<? extends Attribute> category = attribute.getCategory();
            if (this.fidelity) {
                if (!this.service.isAttributeCategorySupported(category)) {
                    notifyEvent(103);
                    throw new PrintJobAttributeException("unsupported category: " + ((Object) category), category, null);
                }
                if (!this.service.isAttributeValueSupported(attribute, docFlavor, null)) {
                    notifyEvent(103);
                    throw new PrintJobAttributeException("unsupported attribute: " + ((Object) attribute), null, attribute);
                }
            }
            if (category == Destination.class) {
                URI uri = ((Destination) attribute).getURI();
                if (!DeploymentDescriptorParser.ATTR_FILE.equals(uri.getScheme())) {
                    notifyEvent(103);
                    throw new PrintException("Not a file: URI");
                }
                try {
                    this.mDestination = new File(uri).getPath();
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkWrite(this.mDestination);
                        } catch (SecurityException e2) {
                            notifyEvent(103);
                            throw new PrintException(e2);
                        }
                    } else {
                        continue;
                    }
                } catch (Exception e3) {
                    throw new PrintException(e3);
                }
            } else if (category == JobName.class) {
                this.jobName = ((JobName) attribute).getValue();
            } else if (category == Copies.class) {
                this.copies = ((Copies) attribute).getValue();
            } else if (category == Media.class) {
                if (attribute instanceof MediaSizeName) {
                    this.mediaName = (MediaSizeName) attribute;
                    if (!this.service.isAttributeValueSupported(attribute, null, null)) {
                        this.mediaSize = MediaSize.getMediaSizeForName(this.mediaName);
                    }
                }
            } else if (category == OrientationRequested.class) {
                this.orient = (OrientationRequested) attribute;
            }
        }
    }

    @Override // javax.print.CancelablePrintJob
    public void cancel() throws PrintException {
        synchronized (this) {
            if (!this.printing) {
                throw new PrintException("Job is not yet submitted.");
            }
            if (this.job != null && !this.printReturned) {
                this.job.cancel();
                notifyEvent(101);
            } else {
                throw new PrintException("Job could not be cancelled.");
            }
        }
    }
}
