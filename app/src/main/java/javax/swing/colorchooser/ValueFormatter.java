package javax.swing.colorchooser;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.DocumentFilter;

/* loaded from: rt.jar:javax/swing/colorchooser/ValueFormatter.class */
final class ValueFormatter extends JFormattedTextField.AbstractFormatter implements FocusListener, Runnable {
    private final DocumentFilter filter = new DocumentFilter() { // from class: javax.swing.colorchooser.ValueFormatter.1
        @Override // javax.swing.text.DocumentFilter
        public void remove(DocumentFilter.FilterBypass filterBypass, int i2, int i3) throws BadLocationException {
            if (ValueFormatter.this.isValid(filterBypass.getDocument().getLength() - i3)) {
                filterBypass.remove(i2, i3);
            }
        }

        @Override // javax.swing.text.DocumentFilter
        public void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws BadLocationException {
            if (ValueFormatter.this.isValid((filterBypass.getDocument().getLength() + str.length()) - i3) && ValueFormatter.this.isValid(str)) {
                filterBypass.replace(i2, i3, str.toUpperCase(Locale.ENGLISH), attributeSet);
            }
        }

        @Override // javax.swing.text.DocumentFilter
        public void insertString(DocumentFilter.FilterBypass filterBypass, int i2, String str, AttributeSet attributeSet) throws BadLocationException {
            if (ValueFormatter.this.isValid(filterBypass.getDocument().getLength() + str.length()) && ValueFormatter.this.isValid(str)) {
                filterBypass.insertString(i2, str.toUpperCase(Locale.ENGLISH), attributeSet);
            }
        }
    };
    private final int length;
    private final int radix;
    private JFormattedTextField text;

    static void init(int i2, boolean z2, JFormattedTextField jFormattedTextField) {
        ValueFormatter valueFormatter = new ValueFormatter(i2, z2);
        jFormattedTextField.setColumns(i2);
        jFormattedTextField.setFormatterFactory(new DefaultFormatterFactory(valueFormatter));
        jFormattedTextField.setHorizontalAlignment(4);
        jFormattedTextField.setMinimumSize(jFormattedTextField.getPreferredSize());
        jFormattedTextField.addFocusListener(valueFormatter);
    }

    ValueFormatter(int i2, boolean z2) {
        this.length = i2;
        this.radix = z2 ? 16 : 10;
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    public Object stringToValue(String str) throws ParseException {
        try {
            return Integer.valueOf(str, this.radix);
        } catch (NumberFormatException e2) {
            ParseException parseException = new ParseException("illegal format", 0);
            parseException.initCause(e2);
            throw parseException;
        }
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    public String valueToString(Object obj) throws ParseException {
        if (obj instanceof Integer) {
            if (this.radix == 10) {
                return obj.toString();
            }
            int iIntValue = ((Integer) obj).intValue();
            int i2 = this.length;
            char[] cArr = new char[i2];
            while (true) {
                int i3 = i2;
                i2--;
                if (0 < i3) {
                    cArr[i2] = Character.forDigit(iIntValue & 15, this.radix);
                    iIntValue >>= 4;
                } else {
                    return new String(cArr).toUpperCase(Locale.ENGLISH);
                }
            }
        } else {
            throw new ParseException("illegal object", 0);
        }
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    protected DocumentFilter getDocumentFilter() {
        return this.filter;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        Object source = focusEvent.getSource();
        if (source instanceof JFormattedTextField) {
            this.text = (JFormattedTextField) source;
            SwingUtilities.invokeLater(this);
        }
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.text != null) {
            this.text.selectAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValid(int i2) {
        return 0 <= i2 && i2 <= this.length;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValid(String str) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            if (Character.digit(str.charAt(i2), this.radix) < 0) {
                return false;
            }
        }
        return true;
    }
}
