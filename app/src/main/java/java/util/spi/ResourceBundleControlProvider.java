package java.util.spi;

import java.util.ResourceBundle;

/* loaded from: rt.jar:java/util/spi/ResourceBundleControlProvider.class */
public interface ResourceBundleControlProvider {
    ResourceBundle.Control getControl(String str);
}
