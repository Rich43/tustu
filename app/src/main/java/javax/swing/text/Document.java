package javax.swing.text;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;

/* loaded from: rt.jar:javax/swing/text/Document.class */
public interface Document {
    public static final String StreamDescriptionProperty = "stream";
    public static final String TitleProperty = "title";

    int getLength();

    void addDocumentListener(DocumentListener documentListener);

    void removeDocumentListener(DocumentListener documentListener);

    void addUndoableEditListener(UndoableEditListener undoableEditListener);

    void removeUndoableEditListener(UndoableEditListener undoableEditListener);

    Object getProperty(Object obj);

    void putProperty(Object obj, Object obj2);

    void remove(int i2, int i3) throws BadLocationException;

    void insertString(int i2, String str, AttributeSet attributeSet) throws BadLocationException;

    String getText(int i2, int i3) throws BadLocationException;

    void getText(int i2, int i3, Segment segment) throws BadLocationException;

    Position getStartPosition();

    Position getEndPosition();

    Position createPosition(int i2) throws BadLocationException;

    Element[] getRootElements();

    Element getDefaultRootElement();

    void render(Runnable runnable);
}
