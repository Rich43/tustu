package javax.print;

import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.event.PrintServiceAttributeListener;

/* loaded from: rt.jar:javax/print/PrintService.class */
public interface PrintService {
    String getName();

    DocPrintJob createPrintJob();

    void addPrintServiceAttributeListener(PrintServiceAttributeListener printServiceAttributeListener);

    void removePrintServiceAttributeListener(PrintServiceAttributeListener printServiceAttributeListener);

    PrintServiceAttributeSet getAttributes();

    <T extends PrintServiceAttribute> T getAttribute(Class<T> cls);

    DocFlavor[] getSupportedDocFlavors();

    boolean isDocFlavorSupported(DocFlavor docFlavor);

    Class<?>[] getSupportedAttributeCategories();

    boolean isAttributeCategorySupported(Class<? extends Attribute> cls);

    Object getDefaultAttributeValue(Class<? extends Attribute> cls);

    Object getSupportedAttributeValues(Class<? extends Attribute> cls, DocFlavor docFlavor, AttributeSet attributeSet);

    boolean isAttributeValueSupported(Attribute attribute, DocFlavor docFlavor, AttributeSet attributeSet);

    AttributeSet getUnsupportedAttributes(DocFlavor docFlavor, AttributeSet attributeSet);

    ServiceUIFactory getServiceUIFactory();

    boolean equals(Object obj);

    int hashCode();
}
