package com.sun.javafx.print;

import java.util.Set;
import javafx.geometry.Rectangle2D;
import javafx.print.Collation;
import javafx.print.JobSettings;
import javafx.print.PageOrientation;
import javafx.print.PageRange;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintColor;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.PrintSides;
import javafx.print.Printer;

/* loaded from: jfxrt.jar:com/sun/javafx/print/PrinterImpl.class */
public interface PrinterImpl {
    void setPrinter(Printer printer);

    String getName();

    JobSettings getDefaultJobSettings();

    Rectangle2D printableArea(Paper paper);

    int defaultCopies();

    int maxCopies();

    Collation defaultCollation();

    Set<Collation> supportedCollations();

    PrintSides defaultSides();

    Set<PrintSides> supportedSides();

    PageRange defaultPageRange();

    boolean supportsPageRanges();

    PrintResolution defaultPrintResolution();

    Set<PrintResolution> supportedPrintResolution();

    PrintColor defaultPrintColor();

    Set<PrintColor> supportedPrintColor();

    PrintQuality defaultPrintQuality();

    Set<PrintQuality> supportedPrintQuality();

    PageOrientation defaultOrientation();

    Set<PageOrientation> supportedOrientation();

    Paper defaultPaper();

    Set<Paper> supportedPapers();

    PaperSource defaultPaperSource();

    Set<PaperSource> supportedPaperSources();
}
