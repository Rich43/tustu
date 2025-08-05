package javax.swing.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import javax.swing.Action;
import javax.swing.JEditorPane;

/* loaded from: rt.jar:javax/swing/text/EditorKit.class */
public abstract class EditorKit implements Cloneable, Serializable {
    public abstract String getContentType();

    public abstract ViewFactory getViewFactory();

    public abstract Action[] getActions();

    public abstract Caret createCaret();

    public abstract Document createDefaultDocument();

    public abstract void read(InputStream inputStream, Document document, int i2) throws IOException, BadLocationException;

    public abstract void write(OutputStream outputStream, Document document, int i2, int i3) throws IOException, BadLocationException;

    public abstract void read(Reader reader, Document document, int i2) throws IOException, BadLocationException;

    public abstract void write(Writer writer, Document document, int i2, int i3) throws IOException, BadLocationException;

    public Object clone() {
        Object objClone;
        try {
            objClone = super.clone();
        } catch (CloneNotSupportedException e2) {
            objClone = null;
        }
        return objClone;
    }

    public void install(JEditorPane jEditorPane) {
    }

    public void deinstall(JEditorPane jEditorPane) {
    }
}
