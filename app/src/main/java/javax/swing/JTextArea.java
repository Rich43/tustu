package javax.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/* loaded from: rt.jar:javax/swing/JTextArea.class */
public class JTextArea extends JTextComponent {
    private static final String uiClassID = "TextAreaUI";
    private int rows;
    private int columns;
    private int columnWidth;
    private int rowHeight;
    private boolean wrap;
    private boolean word;

    public JTextArea() {
        this(null, null, 0, 0);
    }

    public JTextArea(String str) {
        this(null, str, 0, 0);
    }

    public JTextArea(int i2, int i3) {
        this(null, null, i2, i3);
    }

    public JTextArea(String str, int i2, int i3) {
        this(null, str, i2, i3);
    }

    public JTextArea(Document document) {
        this(document, null, 0, 0);
    }

    public JTextArea(Document document, String str, int i2, int i3) {
        this.rows = i2;
        this.columns = i3;
        setDocument(document == null ? createDefaultModel() : document);
        if (str != null) {
            setText(str);
            select(0, 0);
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("rows: " + i2);
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("columns: " + i3);
        }
        LookAndFeel.installProperty(this, "focusTraversalKeysForward", JComponent.getManagingFocusForwardTraversalKeys());
        LookAndFeel.installProperty(this, "focusTraversalKeysBackward", JComponent.getManagingFocusBackwardTraversalKeys());
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    protected Document createDefaultModel() {
        return new PlainDocument();
    }

    public void setTabSize(int i2) {
        Document document = getDocument();
        if (document != null) {
            int tabSize = getTabSize();
            document.putProperty(PlainDocument.tabSizeAttribute, Integer.valueOf(i2));
            firePropertyChange(PlainDocument.tabSizeAttribute, tabSize, i2);
        }
    }

    public int getTabSize() {
        Integer num;
        int iIntValue = 8;
        Document document = getDocument();
        if (document != null && (num = (Integer) document.getProperty(PlainDocument.tabSizeAttribute)) != null) {
            iIntValue = num.intValue();
        }
        return iIntValue;
    }

    public void setLineWrap(boolean z2) {
        boolean z3 = this.wrap;
        this.wrap = z2;
        firePropertyChange("lineWrap", z3, z2);
    }

    public boolean getLineWrap() {
        return this.wrap;
    }

    public void setWrapStyleWord(boolean z2) {
        boolean z3 = this.word;
        this.word = z2;
        firePropertyChange("wrapStyleWord", z3, z2);
    }

    public boolean getWrapStyleWord() {
        return this.word;
    }

    public int getLineOfOffset(int i2) throws BadLocationException {
        Document document = getDocument();
        if (i2 < 0) {
            throw new BadLocationException("Can't translate offset to line", -1);
        }
        if (i2 > document.getLength()) {
            throw new BadLocationException("Can't translate offset to line", document.getLength() + 1);
        }
        return getDocument().getDefaultRootElement().getElementIndex(i2);
    }

    public int getLineCount() {
        return getDocument().getDefaultRootElement().getElementCount();
    }

    public int getLineStartOffset(int i2) throws BadLocationException {
        int lineCount = getLineCount();
        if (i2 < 0) {
            throw new BadLocationException("Negative line", -1);
        }
        if (i2 >= lineCount) {
            throw new BadLocationException("No such line", getDocument().getLength() + 1);
        }
        return getDocument().getDefaultRootElement().getElement(i2).getStartOffset();
    }

    public int getLineEndOffset(int i2) throws BadLocationException {
        int lineCount = getLineCount();
        if (i2 < 0) {
            throw new BadLocationException("Negative line", -1);
        }
        if (i2 >= lineCount) {
            throw new BadLocationException("No such line", getDocument().getLength() + 1);
        }
        int endOffset = getDocument().getDefaultRootElement().getElement(i2).getEndOffset();
        return i2 == lineCount - 1 ? endOffset - 1 : endOffset;
    }

    public void insert(String str, int i2) {
        Document document = getDocument();
        if (document != null) {
            try {
                document.insertString(i2, str, null);
            } catch (BadLocationException e2) {
                throw new IllegalArgumentException(e2.getMessage());
            }
        }
    }

    public void append(String str) {
        Document document = getDocument();
        if (document != null) {
            try {
                document.insertString(document.getLength(), str, null);
            } catch (BadLocationException e2) {
            }
        }
    }

    public void replaceRange(String str, int i2, int i3) {
        if (i3 < i2) {
            throw new IllegalArgumentException("end before start");
        }
        Document document = getDocument();
        if (document != null) {
            try {
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).replace(i2, i3 - i2, str, null);
                } else {
                    document.remove(i2, i3 - i2);
                    document.insertString(i2, str, null);
                }
            } catch (BadLocationException e2) {
                throw new IllegalArgumentException(e2.getMessage());
            }
        }
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int i2) {
        int i3 = this.rows;
        if (i2 < 0) {
            throw new IllegalArgumentException("rows less than zero.");
        }
        if (i2 != i3) {
            this.rows = i2;
            invalidate();
        }
    }

    protected int getRowHeight() {
        if (this.rowHeight == 0) {
            this.rowHeight = getFontMetrics(getFont()).getHeight();
        }
        return this.rowHeight;
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int i2) {
        int i3 = this.columns;
        if (i2 < 0) {
            throw new IllegalArgumentException("columns less than zero.");
        }
        if (i2 != i3) {
            this.columns = i2;
            invalidate();
        }
    }

    protected int getColumnWidth() {
        if (this.columnWidth == 0) {
            this.columnWidth = getFontMetrics(getFont()).charWidth('m');
        }
        return this.columnWidth;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        Dimension dimension = preferredSize == null ? new Dimension(400, 400) : preferredSize;
        Insets insets = getInsets();
        if (this.columns != 0) {
            dimension.width = Math.max(dimension.width, (this.columns * getColumnWidth()) + insets.left + insets.right);
        }
        if (this.rows != 0) {
            dimension.height = Math.max(dimension.height, (this.rows * getRowHeight()) + insets.top + insets.bottom);
        }
        return dimension;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        this.rowHeight = 0;
        this.columnWidth = 0;
    }

    @Override // javax.swing.text.JTextComponent, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",colums=" + this.columns + ",columWidth=" + this.columnWidth + ",rows=" + this.rows + ",rowHeight=" + this.rowHeight + ",word=" + (this.word ? "true" : "false") + ",wrap=" + (this.wrap ? "true" : "false");
    }

    @Override // javax.swing.text.JTextComponent, javax.swing.Scrollable
    public boolean getScrollableTracksViewportWidth() {
        if (this.wrap) {
            return true;
        }
        return super.getScrollableTracksViewportWidth();
    }

    @Override // javax.swing.text.JTextComponent, javax.swing.Scrollable
    public Dimension getPreferredScrollableViewportSize() {
        Dimension preferredScrollableViewportSize = super.getPreferredScrollableViewportSize();
        Dimension dimension = preferredScrollableViewportSize == null ? new Dimension(400, 400) : preferredScrollableViewportSize;
        Insets insets = getInsets();
        dimension.width = this.columns == 0 ? dimension.width : (this.columns * getColumnWidth()) + insets.left + insets.right;
        dimension.height = this.rows == 0 ? dimension.height : (this.rows * getRowHeight()) + insets.top + insets.bottom;
        return dimension;
    }

    @Override // javax.swing.text.JTextComponent, javax.swing.Scrollable
    public int getScrollableUnitIncrement(Rectangle rectangle, int i2, int i3) {
        switch (i2) {
            case 0:
                return getColumnWidth();
            case 1:
                return getRowHeight();
            default:
                throw new IllegalArgumentException("Invalid orientation: " + i2);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.text.JTextComponent, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJTextArea();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JTextArea$AccessibleJTextArea.class */
    protected class AccessibleJTextArea extends JTextComponent.AccessibleJTextComponent {
        protected AccessibleJTextArea() {
            super();
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            accessibleStateSet.add(AccessibleState.MULTI_LINE);
            return accessibleStateSet;
        }
    }
}
