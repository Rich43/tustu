package javax.tools;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.nio.CharBuffer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/* loaded from: rt.jar:javax/tools/SimpleJavaFileObject.class */
public class SimpleJavaFileObject implements JavaFileObject {
    protected final URI uri;
    protected final JavaFileObject.Kind kind;

    protected SimpleJavaFileObject(URI uri, JavaFileObject.Kind kind) {
        uri.getClass();
        kind.getClass();
        if (uri.getPath() == null) {
            throw new IllegalArgumentException("URI must have a path: " + ((Object) uri));
        }
        this.uri = uri;
        this.kind = kind;
    }

    @Override // javax.tools.FileObject
    public URI toUri() {
        return this.uri;
    }

    @Override // javax.tools.FileObject
    public String getName() {
        return toUri().getPath();
    }

    @Override // javax.tools.FileObject
    public InputStream openInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.tools.FileObject
    public OutputStream openOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.tools.FileObject
    public Reader openReader(boolean z2) throws IOException {
        CharSequence charContent = getCharContent(z2);
        if (charContent == null) {
            throw new UnsupportedOperationException();
        }
        if (charContent instanceof CharBuffer) {
            CharBuffer charBuffer = (CharBuffer) charContent;
            if (charBuffer.hasArray()) {
                return new CharArrayReader(charBuffer.array());
            }
        }
        return new StringReader(charContent.toString());
    }

    @Override // javax.tools.FileObject
    public CharSequence getCharContent(boolean z2) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // javax.tools.FileObject
    public Writer openWriter() throws IOException {
        return new OutputStreamWriter(openOutputStream());
    }

    @Override // javax.tools.FileObject
    public long getLastModified() {
        return 0L;
    }

    @Override // javax.tools.FileObject
    public boolean delete() {
        return false;
    }

    @Override // javax.tools.JavaFileObject
    public JavaFileObject.Kind getKind() {
        return this.kind;
    }

    @Override // javax.tools.JavaFileObject
    public boolean isNameCompatible(String str, JavaFileObject.Kind kind) {
        String str2 = str + kind.extension;
        return kind.equals(getKind()) && (str2.equals(toUri().getPath()) || toUri().getPath().endsWith(new StringBuilder().append("/").append(str2).toString()));
    }

    @Override // javax.tools.JavaFileObject
    public NestingKind getNestingKind() {
        return null;
    }

    @Override // javax.tools.JavaFileObject
    public Modifier getAccessLevel() {
        return null;
    }

    public String toString() {
        return getClass().getName() + "[" + ((Object) toUri()) + "]";
    }
}
