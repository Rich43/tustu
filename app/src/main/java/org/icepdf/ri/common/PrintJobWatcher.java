package org.icepdf.ri.common;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/PrintJobWatcher.class */
public class PrintJobWatcher {
    private boolean done = false;

    public PrintJobWatcher() {
    }

    public PrintJobWatcher(DocPrintJob job) {
        setPrintJob(job);
    }

    public void setPrintJob(DocPrintJob job) {
        job.addPrintJobListener(new PrintJobAdapter() { // from class: org.icepdf.ri.common.PrintJobWatcher.1
            @Override // javax.print.event.PrintJobAdapter, javax.print.event.PrintJobListener
            public void printJobCanceled(PrintJobEvent printJobEvent) {
                allDone();
            }

            @Override // javax.print.event.PrintJobAdapter, javax.print.event.PrintJobListener
            public void printJobCompleted(PrintJobEvent printJobEvent) {
                allDone();
            }

            @Override // javax.print.event.PrintJobAdapter, javax.print.event.PrintJobListener
            public void printJobFailed(PrintJobEvent printJobEvent) {
                allDone();
            }

            @Override // javax.print.event.PrintJobAdapter, javax.print.event.PrintJobListener
            public void printJobNoMoreEvents(PrintJobEvent printJobEvent) {
                allDone();
            }

            void allDone() {
                synchronized (PrintJobWatcher.this) {
                    PrintJobWatcher.this.done = true;
                    PrintJobWatcher.this.notify();
                }
            }
        });
    }

    public synchronized void waitForDone() {
        while (!this.done) {
            try {
                wait();
            } catch (InterruptedException e2) {
                return;
            }
        }
    }
}
