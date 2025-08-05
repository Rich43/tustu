package javax.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

/* loaded from: rt.jar:javax/tools/FileObject.class */
public interface FileObject {
    URI toUri();

    String getName();

    InputStream openInputStream() throws IOException;

    OutputStream openOutputStream() throws IOException;

    Reader openReader(boolean z2) throws IOException;

    CharSequence getCharContent(boolean z2) throws IOException;

    Writer openWriter() throws IOException;

    long getLastModified();

    boolean delete();
}
