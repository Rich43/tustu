package javax.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import javax.tools.FileObject;

/* loaded from: rt.jar:javax/tools/ForwardingFileObject.class */
public class ForwardingFileObject<F extends FileObject> implements FileObject {
    protected final F fileObject;

    protected ForwardingFileObject(F f2) {
        f2.getClass();
        this.fileObject = f2;
    }

    @Override // javax.tools.FileObject
    public URI toUri() {
        return this.fileObject.toUri();
    }

    @Override // javax.tools.FileObject
    public String getName() {
        return this.fileObject.getName();
    }

    @Override // javax.tools.FileObject
    public InputStream openInputStream() throws IOException {
        return this.fileObject.openInputStream();
    }

    @Override // javax.tools.FileObject
    public OutputStream openOutputStream() throws IOException {
        return this.fileObject.openOutputStream();
    }

    @Override // javax.tools.FileObject
    public Reader openReader(boolean z2) throws IOException {
        return this.fileObject.openReader(z2);
    }

    @Override // javax.tools.FileObject
    public CharSequence getCharContent(boolean z2) throws IOException {
        return this.fileObject.getCharContent(z2);
    }

    @Override // javax.tools.FileObject
    public Writer openWriter() throws IOException {
        return this.fileObject.openWriter();
    }

    @Override // javax.tools.FileObject
    public long getLastModified() {
        return this.fileObject.getLastModified();
    }

    @Override // javax.tools.FileObject
    public boolean delete() {
        return this.fileObject.delete();
    }
}
