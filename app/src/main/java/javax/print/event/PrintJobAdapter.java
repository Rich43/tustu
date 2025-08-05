package javax.print.event;

/* loaded from: rt.jar:javax/print/event/PrintJobAdapter.class */
public abstract class PrintJobAdapter implements PrintJobListener {
    @Override // javax.print.event.PrintJobListener
    public void printDataTransferCompleted(PrintJobEvent printJobEvent) {
    }

    @Override // javax.print.event.PrintJobListener
    public void printJobCompleted(PrintJobEvent printJobEvent) {
    }

    @Override // javax.print.event.PrintJobListener
    public void printJobFailed(PrintJobEvent printJobEvent) {
    }

    @Override // javax.print.event.PrintJobListener
    public void printJobCanceled(PrintJobEvent printJobEvent) {
    }

    @Override // javax.print.event.PrintJobListener
    public void printJobNoMoreEvents(PrintJobEvent printJobEvent) {
    }

    @Override // javax.print.event.PrintJobListener
    public void printJobRequiresAttention(PrintJobEvent printJobEvent) {
    }
}
