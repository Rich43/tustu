package javax.accessibility;

import java.awt.datatransfer.DataFlavor;
import java.io.InputStream;

/* loaded from: rt.jar:javax/accessibility/AccessibleStreamable.class */
public interface AccessibleStreamable {
    DataFlavor[] getMimeTypes();

    InputStream getStream(DataFlavor dataFlavor);
}
