package com.sun.prism.j2d;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.PrinterImpl;
import com.sun.javafx.print.PrinterJobImpl;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.PrintPipeline;
import com.sun.prism.j2d.print.J2DPrinter;
import com.sun.prism.j2d.print.J2DPrinterJob;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/PrismPrintPipeline.class */
public final class PrismPrintPipeline extends PrintPipeline {
    private static Printer defaultPrinter = null;
    private static final NameComparator nameComparator = new NameComparator();
    private static ObservableSet<Printer> printerSet = null;

    public static PrintPipeline getInstance() {
        return new PrismPrintPipeline();
    }

    public boolean printNode(NGNode ngNode, int w2, int h2, Graphics g2) {
        PrismPrintGraphics ppg = new PrismPrintGraphics((Graphics2D) g2, w2, h2);
        ngNode.render(ppg);
        return true;
    }

    @Override // com.sun.javafx.tk.PrintPipeline
    public PrinterJobImpl createPrinterJob(PrinterJob job) {
        return new J2DPrinterJob(job);
    }

    @Override // com.sun.javafx.tk.PrintPipeline
    public synchronized Printer getDefaultPrinter() {
        if (defaultPrinter == null) {
            PrintService defPrt = PrintServiceLookup.lookupDefaultPrintService();
            if (defPrt == null) {
                defaultPrinter = null;
            } else if (printerSet == null) {
                PrinterImpl impl = new J2DPrinter(defPrt);
                defaultPrinter = PrintHelper.createPrinter(impl);
            } else {
                Iterator<Printer> it = printerSet.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Printer p2 = it.next();
                    PrinterImpl impl2 = PrintHelper.getPrinterImpl(p2);
                    J2DPrinter j2dp = (J2DPrinter) impl2;
                    if (j2dp.getService().equals(defPrt)) {
                        defaultPrinter = p2;
                        break;
                    }
                }
            }
        }
        return defaultPrinter;
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/PrismPrintPipeline$NameComparator.class */
    static class NameComparator implements Comparator<Printer> {
        NameComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Printer p1, Printer p2) {
            return p1.getName().compareTo(p2.getName());
        }
    }

    @Override // com.sun.javafx.tk.PrintPipeline
    public synchronized ObservableSet<Printer> getAllPrinters() {
        if (printerSet == null) {
            Set printers = new TreeSet(nameComparator);
            Printer defPrinter = getDefaultPrinter();
            PrintService defService = null;
            if (defPrinter != null) {
                J2DPrinter def2D = (J2DPrinter) PrintHelper.getPrinterImpl(defPrinter);
                defService = def2D.getService();
            }
            PrintService[] allServices = PrintServiceLookup.lookupPrintServices(null, null);
            for (int i2 = 0; i2 < allServices.length; i2++) {
                if (defService != null && defService.equals(allServices[i2])) {
                    printers.add(defPrinter);
                } else {
                    PrinterImpl impl = new J2DPrinter(allServices[i2]);
                    Printer printer = PrintHelper.createPrinter(impl);
                    impl.setPrinter(printer);
                    printers.add(printer);
                }
            }
            printerSet = FXCollections.unmodifiableObservableSet(FXCollections.observableSet(printers));
        }
        return printerSet;
    }
}
