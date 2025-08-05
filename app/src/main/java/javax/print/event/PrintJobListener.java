package javax.print.event;

/* loaded from: rt.jar:javax/print/event/PrintJobListener.class */
public interface PrintJobListener {
    void printDataTransferCompleted(PrintJobEvent printJobEvent);

    void printJobCompleted(PrintJobEvent printJobEvent);

    void printJobFailed(PrintJobEvent printJobEvent);

    void printJobCanceled(PrintJobEvent printJobEvent);

    void printJobNoMoreEvents(PrintJobEvent printJobEvent);

    void printJobRequiresAttention(PrintJobEvent printJobEvent);
}
