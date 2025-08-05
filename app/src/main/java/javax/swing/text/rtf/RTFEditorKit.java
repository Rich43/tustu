package javax.swing.text.rtf;

import com.sun.glass.ui.Clipboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

/* loaded from: rt.jar:javax/swing/text/rtf/RTFEditorKit.class */
public class RTFEditorKit extends StyledEditorKit {
    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public String getContentType() {
        return Clipboard.RTF_TYPE;
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public void read(InputStream inputStream, Document document, int i2) throws IOException, BadLocationException {
        if (document instanceof StyledDocument) {
            RTFReader rTFReader = new RTFReader((StyledDocument) document);
            rTFReader.readFromStream(inputStream);
            rTFReader.close();
            return;
        }
        super.read(inputStream, document, i2);
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public void write(OutputStream outputStream, Document document, int i2, int i3) throws IOException, BadLocationException {
        RTFGenerator.writeDocument(document, outputStream);
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public void read(Reader reader, Document document, int i2) throws IOException, BadLocationException {
        if (document instanceof StyledDocument) {
            RTFReader rTFReader = new RTFReader((StyledDocument) document);
            rTFReader.readFromReader(reader);
            rTFReader.close();
            return;
        }
        super.read(reader, document, i2);
    }

    @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
    public void write(Writer writer, Document document, int i2, int i3) throws IOException, BadLocationException {
        throw new IOException("RTF is an 8-bit format");
    }
}
