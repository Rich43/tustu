package javax.print;

/* loaded from: rt.jar:javax/print/CancelablePrintJob.class */
public interface CancelablePrintJob extends DocPrintJob {
    void cancel() throws PrintException;
}
