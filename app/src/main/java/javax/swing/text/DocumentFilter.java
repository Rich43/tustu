package javax.swing.text;

/* loaded from: rt.jar:javax/swing/text/DocumentFilter.class */
public class DocumentFilter {

    /* loaded from: rt.jar:javax/swing/text/DocumentFilter$FilterBypass.class */
    public static abstract class FilterBypass {
        public abstract Document getDocument();

        public abstract void remove(int i2, int i3) throws BadLocationException;

        public abstract void insertString(int i2, String str, AttributeSet attributeSet) throws BadLocationException;

        public abstract void replace(int i2, int i3, String str, AttributeSet attributeSet) throws BadLocationException;
    }

    public void remove(FilterBypass filterBypass, int i2, int i3) throws BadLocationException {
        filterBypass.remove(i2, i3);
    }

    public void insertString(FilterBypass filterBypass, int i2, String str, AttributeSet attributeSet) throws BadLocationException {
        filterBypass.insertString(i2, str, attributeSet);
    }

    public void replace(FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws BadLocationException {
        filterBypass.replace(i2, i3, str, attributeSet);
    }
}
