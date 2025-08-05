package javax.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import javax.lang.model.SourceVersion;

/* loaded from: rt.jar:javax/tools/Tool.class */
public interface Tool {
    int run(InputStream inputStream, OutputStream outputStream, OutputStream outputStream2, String... strArr);

    Set<SourceVersion> getSourceVersions();
}
