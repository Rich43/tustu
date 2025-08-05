package sun.nio.fs;

import java.io.IOException;
import java.util.Map;

/* loaded from: rt.jar:sun/nio/fs/DynamicFileAttributeView.class */
interface DynamicFileAttributeView {
    void setAttribute(String str, Object obj) throws IOException;

    Map<String, Object> readAttributes(String[] strArr) throws IOException;
}
