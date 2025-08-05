package com.sun.javafx.tk;

import com.sun.javafx.print.PrinterJobImpl;
import java.lang.reflect.Method;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.print.PrinterJob;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/PrintPipeline.class */
public abstract class PrintPipeline {
    private static PrintPipeline ppl = null;

    public abstract Printer getDefaultPrinter();

    public abstract ObservableSet<Printer> getAllPrinters();

    public abstract PrinterJobImpl createPrinterJob(PrinterJob printerJob);

    public static PrintPipeline getPrintPipeline() {
        if (ppl != null) {
            return ppl;
        }
        try {
            Class klass = Class.forName("com.sun.prism.j2d.PrismPrintPipeline");
            Method m2 = klass.getMethod("getInstance", (Class[]) null);
            ppl = (PrintPipeline) m2.invoke(null, (Object[]) null);
            return ppl;
        } catch (Throwable t2) {
            throw new RuntimeException(t2);
        }
    }
}
