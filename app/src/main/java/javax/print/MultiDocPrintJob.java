package javax.print;

import javax.print.attribute.PrintRequestAttributeSet;

/* loaded from: rt.jar:javax/print/MultiDocPrintJob.class */
public interface MultiDocPrintJob extends DocPrintJob {
    void print(MultiDoc multiDoc, PrintRequestAttributeSet printRequestAttributeSet) throws PrintException;
}
