package sun.print;

import java.awt.print.PrinterJob;
import javax.print.attribute.PrintRequestAttribute;

/* loaded from: rt.jar:sun/print/PrinterJobWrapper.class */
public class PrinterJobWrapper implements PrintRequestAttribute {
    private static final long serialVersionUID = -8792124426995707237L;
    private PrinterJob job;

    public PrinterJobWrapper(PrinterJob printerJob) {
        this.job = printerJob;
    }

    public PrinterJob getPrinterJob() {
        return this.job;
    }

    @Override // javax.print.attribute.Attribute
    public final Class getCategory() {
        return PrinterJobWrapper.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printerjob-wrapper";
    }

    public String toString() {
        return "printerjob-wrapper: " + this.job.toString();
    }

    public int hashCode() {
        return this.job.hashCode();
    }
}
