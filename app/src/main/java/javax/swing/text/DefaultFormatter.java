package javax.swing.text;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;
import sun.reflect.misc.ReflectUtil;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/DefaultFormatter.class */
public class DefaultFormatter extends JFormattedTextField.AbstractFormatter implements Cloneable, Serializable {
    private boolean commitOnEdit;
    private Class<?> valueClass;
    private NavigationFilter navigationFilter;
    private DocumentFilter documentFilter;
    transient ReplaceHolder replaceHolder;
    private boolean overwriteMode = true;
    private boolean allowsInvalid = true;

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    public void install(JFormattedTextField jFormattedTextField) {
        super.install(jFormattedTextField);
        positionCursorAtInitialLocation();
    }

    public void setCommitsOnValidEdit(boolean z2) {
        this.commitOnEdit = z2;
    }

    public boolean getCommitsOnValidEdit() {
        return this.commitOnEdit;
    }

    public void setOverwriteMode(boolean z2) {
        this.overwriteMode = z2;
    }

    public boolean getOverwriteMode() {
        return this.overwriteMode;
    }

    public void setAllowsInvalid(boolean z2) {
        this.allowsInvalid = z2;
    }

    public boolean getAllowsInvalid() {
        return this.allowsInvalid;
    }

    public void setValueClass(Class<?> cls) {
        this.valueClass = cls;
    }

    public Class<?> getValueClass() {
        return this.valueClass;
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    public Object stringToValue(String str) throws SecurityException, ParseException {
        Constructor<?> constructor;
        Object value;
        Class<?> valueClass = getValueClass();
        JFormattedTextField formattedTextField = getFormattedTextField();
        if (valueClass == null && formattedTextField != null && (value = formattedTextField.getValue()) != null) {
            valueClass = value.getClass();
        }
        if (valueClass != null) {
            try {
                ReflectUtil.checkPackageAccess(valueClass);
                SwingUtilities2.checkAccess(valueClass.getModifiers());
                constructor = valueClass.getConstructor(String.class);
            } catch (NoSuchMethodException e2) {
                constructor = null;
            }
            if (constructor != null) {
                try {
                    SwingUtilities2.checkAccess(constructor.getModifiers());
                    return constructor.newInstance(str);
                } catch (Throwable th) {
                    throw new ParseException("Error creating instance", 0);
                }
            }
        }
        return str;
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    public String valueToString(Object obj) throws ParseException {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    protected DocumentFilter getDocumentFilter() {
        if (this.documentFilter == null) {
            this.documentFilter = new DefaultDocumentFilter();
        }
        return this.documentFilter;
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    protected NavigationFilter getNavigationFilter() {
        if (this.navigationFilter == null) {
            this.navigationFilter = new DefaultNavigationFilter();
        }
        return this.navigationFilter;
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    public Object clone() throws CloneNotSupportedException {
        DefaultFormatter defaultFormatter = (DefaultFormatter) super.clone();
        defaultFormatter.navigationFilter = null;
        defaultFormatter.documentFilter = null;
        defaultFormatter.replaceHolder = null;
        return defaultFormatter;
    }

    void positionCursorAtInitialLocation() {
        JFormattedTextField formattedTextField = getFormattedTextField();
        if (formattedTextField != null) {
            formattedTextField.setCaretPosition(getInitialVisualPosition());
        }
    }

    int getInitialVisualPosition() {
        return getNextNavigatableChar(0, 1);
    }

    boolean isNavigatable(int i2) {
        return true;
    }

    boolean isLegalInsertText(String str) {
        return true;
    }

    private int getNextNavigatableChar(int i2, int i3) {
        int length = getFormattedTextField().getDocument().getLength();
        while (i2 >= 0 && i2 < length) {
            if (isNavigatable(i2)) {
                return i2;
            }
            i2 += i3;
        }
        return i2;
    }

    String getReplaceString(int i2, int i3, String str) {
        String text = getFormattedTextField().getText();
        String strSubstring = text.substring(0, i2);
        if (str != null) {
            strSubstring = strSubstring + str;
        }
        if (i2 + i3 < text.length()) {
            strSubstring = strSubstring + text.substring(i2 + i3);
        }
        return strSubstring;
    }

    boolean isValidEdit(ReplaceHolder replaceHolder) {
        if (!getAllowsInvalid()) {
            try {
                replaceHolder.value = stringToValue(getReplaceString(replaceHolder.offset, replaceHolder.length, replaceHolder.text));
                return true;
            } catch (ParseException e2) {
                return false;
            }
        }
        return true;
    }

    void commitEdit() throws ParseException {
        JFormattedTextField formattedTextField = getFormattedTextField();
        if (formattedTextField != null) {
            formattedTextField.commitEdit();
        }
    }

    void updateValue() throws SecurityException {
        updateValue(null);
    }

    void updateValue(Object obj) throws SecurityException {
        if (obj == null) {
            try {
                stringToValue(getFormattedTextField().getText());
            } catch (ParseException e2) {
                setEditValid(false);
                return;
            }
        }
        if (getCommitsOnValidEdit()) {
            commitEdit();
        }
        setEditValid(true);
    }

    int getNextCursorPosition(int i2, int i3) {
        int nextNavigatableChar = getNextNavigatableChar(i2, i3);
        int length = getFormattedTextField().getDocument().getLength();
        if (!getAllowsInvalid()) {
            if (i3 == -1 && i2 == nextNavigatableChar) {
                nextNavigatableChar = getNextNavigatableChar(nextNavigatableChar, 1);
                if (nextNavigatableChar >= length) {
                    nextNavigatableChar = i2;
                }
            } else if (i3 == 1 && nextNavigatableChar >= length) {
                nextNavigatableChar = getNextNavigatableChar(length - 1, -1);
                if (nextNavigatableChar < length) {
                    nextNavigatableChar++;
                }
            }
        }
        return nextNavigatableChar;
    }

    void repositionCursor(int i2, int i3) {
        getFormattedTextField().getCaret().setDot(getNextCursorPosition(i2, i3));
    }

    int getNextVisualPositionFrom(JTextComponent jTextComponent, int i2, Position.Bias bias, int i3, Position.Bias[] biasArr) throws BadLocationException {
        int nextVisualPositionFrom = jTextComponent.getUI().getNextVisualPositionFrom(jTextComponent, i2, bias, i3, biasArr);
        if (nextVisualPositionFrom == -1) {
            return -1;
        }
        if (!getAllowsInvalid() && (i3 == 3 || i3 == 7)) {
            int i4 = -1;
            while (!isNavigatable(nextVisualPositionFrom) && nextVisualPositionFrom != i4) {
                i4 = nextVisualPositionFrom;
                nextVisualPositionFrom = jTextComponent.getUI().getNextVisualPositionFrom(jTextComponent, nextVisualPositionFrom, bias, i3, biasArr);
            }
            int length = getFormattedTextField().getDocument().getLength();
            if (i4 == nextVisualPositionFrom || nextVisualPositionFrom == length) {
                if (nextVisualPositionFrom == 0) {
                    biasArr[0] = Position.Bias.Forward;
                    nextVisualPositionFrom = getInitialVisualPosition();
                }
                if (nextVisualPositionFrom >= length && length > 0) {
                    biasArr[0] = Position.Bias.Forward;
                    nextVisualPositionFrom = getNextNavigatableChar(length - 1, -1) + 1;
                }
            }
        }
        return nextVisualPositionFrom;
    }

    boolean canReplace(ReplaceHolder replaceHolder) {
        return isValidEdit(replaceHolder);
    }

    void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws SecurityException, BadLocationException {
        replace(getReplaceHolder(filterBypass, i2, i3, str, attributeSet));
    }

    boolean replace(ReplaceHolder replaceHolder) throws SecurityException, BadLocationException {
        boolean z2 = true;
        int i2 = 1;
        if (replaceHolder.length > 0 && ((replaceHolder.text == null || replaceHolder.text.length() == 0) && (getFormattedTextField().getSelectionStart() != replaceHolder.offset || replaceHolder.length > 1))) {
            i2 = -1;
        }
        if (getOverwriteMode() && replaceHolder.text != null && getFormattedTextField().getSelectedText() == null) {
            replaceHolder.length = Math.min(Math.max(replaceHolder.length, replaceHolder.text.length()), replaceHolder.fb.getDocument().getLength() - replaceHolder.offset);
        }
        if ((replaceHolder.text != null && !isLegalInsertText(replaceHolder.text)) || !canReplace(replaceHolder) || (replaceHolder.length == 0 && (replaceHolder.text == null || replaceHolder.text.length() == 0))) {
            z2 = false;
        }
        if (z2) {
            int length = replaceHolder.cursorPosition;
            replaceHolder.fb.replace(replaceHolder.offset, replaceHolder.length, replaceHolder.text, replaceHolder.attrs);
            if (length == -1) {
                length = replaceHolder.offset;
                if (i2 == 1 && replaceHolder.text != null) {
                    length = replaceHolder.offset + replaceHolder.text.length();
                }
            }
            updateValue(replaceHolder.value);
            repositionCursor(length, i2);
            return true;
        }
        invalidEdit();
        return false;
    }

    void setDot(NavigationFilter.FilterBypass filterBypass, int i2, Position.Bias bias) {
        filterBypass.setDot(i2, bias);
    }

    void moveDot(NavigationFilter.FilterBypass filterBypass, int i2, Position.Bias bias) {
        filterBypass.moveDot(i2, bias);
    }

    ReplaceHolder getReplaceHolder(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) {
        if (this.replaceHolder == null) {
            this.replaceHolder = new ReplaceHolder();
        }
        this.replaceHolder.reset(filterBypass, i2, i3, str, attributeSet);
        return this.replaceHolder;
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultFormatter$ReplaceHolder.class */
    static class ReplaceHolder {
        DocumentFilter.FilterBypass fb;
        int offset;
        int length;
        String text;
        AttributeSet attrs;
        Object value;
        int cursorPosition;

        ReplaceHolder() {
        }

        void reset(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) {
            this.fb = filterBypass;
            this.offset = i2;
            this.length = i3;
            this.text = str;
            this.attrs = attributeSet;
            this.value = null;
            this.cursorPosition = -1;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultFormatter$DefaultNavigationFilter.class */
    private class DefaultNavigationFilter extends NavigationFilter implements Serializable {
        private DefaultNavigationFilter() {
        }

        @Override // javax.swing.text.NavigationFilter
        public void setDot(NavigationFilter.FilterBypass filterBypass, int i2, Position.Bias bias) {
            if (DefaultFormatter.this.getFormattedTextField().composedTextExists()) {
                filterBypass.setDot(i2, bias);
            } else {
                DefaultFormatter.this.setDot(filterBypass, i2, bias);
            }
        }

        @Override // javax.swing.text.NavigationFilter
        public void moveDot(NavigationFilter.FilterBypass filterBypass, int i2, Position.Bias bias) {
            if (DefaultFormatter.this.getFormattedTextField().composedTextExists()) {
                filterBypass.moveDot(i2, bias);
            } else {
                DefaultFormatter.this.moveDot(filterBypass, i2, bias);
            }
        }

        @Override // javax.swing.text.NavigationFilter
        public int getNextVisualPositionFrom(JTextComponent jTextComponent, int i2, Position.Bias bias, int i3, Position.Bias[] biasArr) throws BadLocationException {
            if (jTextComponent.composedTextExists()) {
                return jTextComponent.getUI().getNextVisualPositionFrom(jTextComponent, i2, bias, i3, biasArr);
            }
            return DefaultFormatter.this.getNextVisualPositionFrom(jTextComponent, i2, bias, i3, biasArr);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultFormatter$DefaultDocumentFilter.class */
    private class DefaultDocumentFilter extends DocumentFilter implements Serializable {
        private DefaultDocumentFilter() {
        }

        @Override // javax.swing.text.DocumentFilter
        public void remove(DocumentFilter.FilterBypass filterBypass, int i2, int i3) throws SecurityException, BadLocationException {
            if (DefaultFormatter.this.getFormattedTextField().composedTextExists()) {
                filterBypass.remove(i2, i3);
            } else {
                DefaultFormatter.this.replace(filterBypass, i2, i3, null, null);
            }
        }

        @Override // javax.swing.text.DocumentFilter
        public void insertString(DocumentFilter.FilterBypass filterBypass, int i2, String str, AttributeSet attributeSet) throws SecurityException, BadLocationException {
            if (DefaultFormatter.this.getFormattedTextField().composedTextExists() || Utilities.isComposedTextAttributeDefined(attributeSet)) {
                filterBypass.insertString(i2, str, attributeSet);
            } else {
                DefaultFormatter.this.replace(filterBypass, i2, 0, str, attributeSet);
            }
        }

        @Override // javax.swing.text.DocumentFilter
        public void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws SecurityException, BadLocationException {
            if (DefaultFormatter.this.getFormattedTextField().composedTextExists() || Utilities.isComposedTextAttributeDefined(attributeSet)) {
                filterBypass.replace(i2, i3, str, attributeSet);
            } else {
                DefaultFormatter.this.replace(filterBypass, i2, i3, str, attributeSet);
            }
        }
    }
}
