package org.icepdf.ri.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.CancelablePrintJob;
import javax.print.PrintException;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/PrinterTask.class */
public class PrinterTask implements Runnable {
    private static final Logger logger = Logger.getLogger(PrinterTask.class.toString());
    private PrintHelper printHelper;
    private CancelablePrintJob cancelablePrintJob;

    public PrinterTask(PrintHelper printHelper) {
        this.printHelper = printHelper;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.printHelper != null) {
                this.cancelablePrintJob = this.printHelper.cancelablePrint();
            }
        } catch (PrintException ex) {
            logger.log(Level.FINE, "Error during printing.", (Throwable) ex);
        }
    }

    public void cancel() {
        try {
            if (this.cancelablePrintJob != null) {
                this.cancelablePrintJob.cancel();
            }
        } catch (PrintException ex) {
            logger.log(Level.FINE, "Error during printing, " + ex.getMessage());
        }
    }
}
