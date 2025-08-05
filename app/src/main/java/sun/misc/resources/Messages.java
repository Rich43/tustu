package sun.misc.resources;

import java.util.ListResourceBundle;

/* loaded from: rt.jar:sun/misc/resources/Messages.class */
public final class Messages extends ListResourceBundle {
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[], java.lang.Object[][]] */
    @Override // java.util.ListResourceBundle
    protected Object[][] getContents() {
        return new Object[]{new Object[]{"optpkg.versionerror", "ERROR: Invalid version format used in {0} JAR file. Check the documentation for the supported version format."}, new Object[]{"optpkg.attributeerror", "ERROR: The required {0} JAR manifest attribute is not set in {1} JAR file."}, new Object[]{"optpkg.attributeserror", "ERROR: Some required JAR manifest attributes are not set in {0} JAR file."}};
    }
}
