package com.sun.javafx.print;

import javafx.print.JobSettings;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintResolution;
import javafx.print.Printer;

/* loaded from: jfxrt.jar:com/sun/javafx/print/PrintHelper.class */
public class PrintHelper {
    private static PrintAccessor printAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/print/PrintHelper$PrintAccessor.class */
    public interface PrintAccessor {
        PrintResolution createPrintResolution(int i2, int i3);

        Paper createPaper(String str, double d2, double d3, Units units);

        PaperSource createPaperSource(String str);

        JobSettings createJobSettings(Printer printer);

        Printer createPrinter(PrinterImpl printerImpl);

        PrinterImpl getPrinterImpl(Printer printer);
    }

    static {
        forceInit(Printer.class);
    }

    private PrintHelper() {
    }

    public static PrintResolution createPrintResolution(int fr, int cfr) {
        return printAccessor.createPrintResolution(fr, cfr);
    }

    public static Paper createPaper(String paperName, double paperWidth, double paperHeight, Units units) {
        return printAccessor.createPaper(paperName, paperWidth, paperHeight, units);
    }

    public static PaperSource createPaperSource(String name) {
        return printAccessor.createPaperSource(name);
    }

    public static JobSettings createJobSettings(Printer printer) {
        return printAccessor.createJobSettings(printer);
    }

    public static Printer createPrinter(PrinterImpl impl) {
        return printAccessor.createPrinter(impl);
    }

    public static PrinterImpl getPrinterImpl(Printer printer) {
        return printAccessor.getPrinterImpl(printer);
    }

    public static void setPrintAccessor(PrintAccessor newAccessor) {
        if (printAccessor != null) {
            throw new IllegalStateException();
        }
        printAccessor = newAccessor;
    }

    private static void forceInit(Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true, classToInit.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }
}
