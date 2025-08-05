package com.sun.javafx.print;

import javafx.print.PageLayout;
import javafx.scene.Node;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/print/PrinterJobImpl.class */
public interface PrinterJobImpl {
    PrinterImpl getPrinterImpl();

    void setPrinterImpl(PrinterImpl printerImpl);

    boolean showPrintDialog(Window window);

    boolean showPageDialog(Window window);

    PageLayout validatePageLayout(PageLayout pageLayout);

    boolean print(PageLayout pageLayout, Node node);

    boolean endJob();

    void cancelJob();
}
