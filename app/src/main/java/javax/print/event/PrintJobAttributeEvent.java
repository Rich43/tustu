package javax.print.event;

import javax.print.DocPrintJob;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.PrintJobAttributeSet;

/* loaded from: rt.jar:javax/print/event/PrintJobAttributeEvent.class */
public class PrintJobAttributeEvent extends PrintEvent {
    private static final long serialVersionUID = -6534469883874742101L;
    private PrintJobAttributeSet attributes;

    public PrintJobAttributeEvent(DocPrintJob docPrintJob, PrintJobAttributeSet printJobAttributeSet) {
        super(docPrintJob);
        this.attributes = AttributeSetUtilities.unmodifiableView(printJobAttributeSet);
    }

    public DocPrintJob getPrintJob() {
        return (DocPrintJob) getSource();
    }

    public PrintJobAttributeSet getAttributes() {
        return this.attributes;
    }
}
