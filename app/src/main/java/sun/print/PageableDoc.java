package sun.print;

import java.awt.print.Pageable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;

/* loaded from: rt.jar:sun/print/PageableDoc.class */
public class PageableDoc implements Doc {
    private Pageable pageable;

    public PageableDoc(Pageable pageable) {
        this.pageable = pageable;
    }

    @Override // javax.print.Doc
    public DocFlavor getDocFlavor() {
        return DocFlavor.SERVICE_FORMATTED.PAGEABLE;
    }

    @Override // javax.print.Doc
    public DocAttributeSet getAttributes() {
        return new HashDocAttributeSet();
    }

    @Override // javax.print.Doc
    public Object getPrintData() throws IOException {
        return this.pageable;
    }

    @Override // javax.print.Doc
    public Reader getReaderForText() throws IOException {
        return null;
    }

    @Override // javax.print.Doc
    public InputStream getStreamForBytes() throws IOException {
        return null;
    }
}
