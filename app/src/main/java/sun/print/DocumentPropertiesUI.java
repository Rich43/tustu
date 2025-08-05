package sun.print;

import java.awt.Window;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import javax.print.attribute.PrintRequestAttributeSet;

/* loaded from: rt.jar:sun/print/DocumentPropertiesUI.class */
public abstract class DocumentPropertiesUI {
    public static final int DOCUMENTPROPERTIES_ROLE = 199;
    public static final String DOCPROPERTIESCLASSNAME = DocumentPropertiesUI.class.getName();

    public abstract PrintRequestAttributeSet showDocumentProperties(PrinterJob printerJob, Window window, PrintService printService, PrintRequestAttributeSet printRequestAttributeSet);
}
