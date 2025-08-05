package javax.print;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.Fidelity;
import sun.print.ServiceDialog;
import sun.print.SunAlternateMedia;

/* loaded from: rt.jar:javax/print/ServiceUI.class */
public class ServiceUI {
    public static PrintService printDialog(GraphicsConfiguration graphicsConfiguration, int i2, int i3, PrintService[] printServiceArr, PrintService printService, DocFlavor docFlavor, PrintRequestAttributeSet printRequestAttributeSet) throws HeadlessException {
        ServiceDialog serviceDialog;
        int i4 = -1;
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (printServiceArr == null || printServiceArr.length == 0) {
            throw new IllegalArgumentException("services must be non-null and non-empty");
        }
        if (printRequestAttributeSet == null) {
            throw new IllegalArgumentException("attributes must be non-null");
        }
        if (printService != null) {
            int i5 = 0;
            while (true) {
                if (i5 >= printServiceArr.length) {
                    break;
                }
                if (!printServiceArr[i5].equals(printService)) {
                    i5++;
                } else {
                    i4 = i5;
                    break;
                }
            }
            if (i4 < 0) {
                throw new IllegalArgumentException("services must contain defaultService");
            }
        } else {
            i4 = 0;
        }
        Object obj = null;
        Rectangle bounds = graphicsConfiguration == null ? GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds() : graphicsConfiguration.getBounds();
        if (obj instanceof Frame) {
            serviceDialog = new ServiceDialog(graphicsConfiguration, i2 + bounds.f12372x, i3 + bounds.f12373y, printServiceArr, i4, docFlavor, printRequestAttributeSet, (Frame) null);
        } else {
            serviceDialog = new ServiceDialog(graphicsConfiguration, i2 + bounds.f12372x, i3 + bounds.f12373y, printServiceArr, i4, docFlavor, printRequestAttributeSet, (Dialog) null);
        }
        Rectangle bounds2 = serviceDialog.getBounds();
        for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            bounds = bounds.union(graphicsDevice.getDefaultConfiguration().getBounds());
        }
        if (!bounds.contains(bounds2)) {
            serviceDialog.setLocationRelativeTo(null);
        }
        serviceDialog.show();
        if (serviceDialog.getStatus() == 1) {
            PrintRequestAttributeSet attributes = serviceDialog.getAttributes();
            if (printRequestAttributeSet.containsKey(Destination.class) && !attributes.containsKey(Destination.class)) {
                printRequestAttributeSet.remove(Destination.class);
            }
            if (printRequestAttributeSet.containsKey(SunAlternateMedia.class) && !attributes.containsKey(SunAlternateMedia.class)) {
                printRequestAttributeSet.remove(SunAlternateMedia.class);
            }
            printRequestAttributeSet.addAll(attributes);
            Fidelity fidelity = (Fidelity) printRequestAttributeSet.get(Fidelity.class);
            if (fidelity != null && fidelity == Fidelity.FIDELITY_TRUE) {
                removeUnsupportedAttributes(serviceDialog.getPrintService(), docFlavor, printRequestAttributeSet);
            }
        }
        return serviceDialog.getPrintService();
    }

    private static void removeUnsupportedAttributes(PrintService printService, DocFlavor docFlavor, AttributeSet attributeSet) {
        AttributeSet unsupportedAttributes = printService.getUnsupportedAttributes(docFlavor, attributeSet);
        if (unsupportedAttributes != null) {
            for (Attribute attribute : unsupportedAttributes.toArray()) {
                Class<? extends Attribute> category = attribute.getCategory();
                if (printService.isAttributeCategorySupported(category)) {
                    Attribute attribute2 = (Attribute) printService.getDefaultAttributeValue(category);
                    if (attribute2 != null) {
                        attributeSet.add(attribute2);
                    } else {
                        attributeSet.remove(category);
                    }
                } else {
                    attributeSet.remove(category);
                }
            }
        }
    }
}
