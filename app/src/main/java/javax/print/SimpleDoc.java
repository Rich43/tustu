package javax.print;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.DocAttributeSet;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/print/SimpleDoc.class */
public final class SimpleDoc implements Doc {
    private DocFlavor flavor;
    private DocAttributeSet attributes;
    private Object printData;
    private Reader reader;
    private InputStream inStream;

    public SimpleDoc(Object obj, DocFlavor docFlavor, DocAttributeSet docAttributeSet) {
        if (docFlavor == null || obj == null) {
            throw new IllegalArgumentException("null argument(s)");
        }
        try {
            String representationClassName = docFlavor.getRepresentationClassName();
            ReflectUtil.checkPackageAccess(representationClassName);
            if (!Class.forName(representationClassName, false, Thread.currentThread().getContextClassLoader()).isInstance(obj)) {
                throw new IllegalArgumentException("data is not of declared type");
            }
            this.flavor = docFlavor;
            if (docAttributeSet != null) {
                this.attributes = AttributeSetUtilities.unmodifiableView(docAttributeSet);
            }
            this.printData = obj;
        } catch (Throwable th) {
            throw new IllegalArgumentException("unknown representation class");
        }
    }

    @Override // javax.print.Doc
    public DocFlavor getDocFlavor() {
        return this.flavor;
    }

    @Override // javax.print.Doc
    public DocAttributeSet getAttributes() {
        return this.attributes;
    }

    @Override // javax.print.Doc
    public Object getPrintData() throws IOException {
        return this.printData;
    }

    @Override // javax.print.Doc
    public Reader getReaderForText() throws IOException {
        if (this.printData instanceof Reader) {
            return (Reader) this.printData;
        }
        synchronized (this) {
            if (this.reader != null) {
                return this.reader;
            }
            if (this.printData instanceof char[]) {
                this.reader = new CharArrayReader((char[]) this.printData);
            } else if (this.printData instanceof String) {
                this.reader = new StringReader((String) this.printData);
            }
            return this.reader;
        }
    }

    @Override // javax.print.Doc
    public InputStream getStreamForBytes() throws IOException {
        if (this.printData instanceof InputStream) {
            return (InputStream) this.printData;
        }
        synchronized (this) {
            if (this.inStream != null) {
                return this.inStream;
            }
            if (this.printData instanceof byte[]) {
                this.inStream = new ByteArrayInputStream((byte[]) this.printData);
            }
            return this.inStream;
        }
    }
}
