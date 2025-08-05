package javax.print.event;

import javax.print.PrintService;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.PrintServiceAttributeSet;

/* loaded from: rt.jar:javax/print/event/PrintServiceAttributeEvent.class */
public class PrintServiceAttributeEvent extends PrintEvent {
    private static final long serialVersionUID = -7565987018140326600L;
    private PrintServiceAttributeSet attributes;

    public PrintServiceAttributeEvent(PrintService printService, PrintServiceAttributeSet printServiceAttributeSet) {
        super(printService);
        this.attributes = AttributeSetUtilities.unmodifiableView(printServiceAttributeSet);
    }

    public PrintService getPrintService() {
        return (PrintService) getSource();
    }

    public PrintServiceAttributeSet getAttributes() {
        return this.attributes;
    }
}
