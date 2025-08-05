package javax.print;

import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobAttributeListener;
import javax.print.event.PrintJobListener;

/* loaded from: rt.jar:javax/print/DocPrintJob.class */
public interface DocPrintJob {
    PrintService getPrintService();

    PrintJobAttributeSet getAttributes();

    void addPrintJobListener(PrintJobListener printJobListener);

    void removePrintJobListener(PrintJobListener printJobListener);

    void addPrintJobAttributeListener(PrintJobAttributeListener printJobAttributeListener, PrintJobAttributeSet printJobAttributeSet);

    void removePrintJobAttributeListener(PrintJobAttributeListener printJobAttributeListener);

    void print(Doc doc, PrintRequestAttributeSet printRequestAttributeSet) throws PrintException;
}
