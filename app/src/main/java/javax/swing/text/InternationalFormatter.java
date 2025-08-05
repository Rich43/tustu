package javax.swing.text;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.AttributedCharacterIterator;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DocumentFilter;

/* loaded from: rt.jar:javax/swing/text/InternationalFormatter.class */
public class InternationalFormatter extends DefaultFormatter {
    private static final Format.Field[] EMPTY_FIELD_ARRAY = new Format.Field[0];
    private Format format;
    private Comparable max;
    private Comparable min;
    private transient BitSet literalMask;
    private transient AttributedCharacterIterator iterator;
    private transient boolean validMask;
    private transient String string;
    private transient boolean ignoreDocumentMutate;

    public InternationalFormatter() {
        setOverwriteMode(false);
    }

    public InternationalFormatter(Format format) {
        this();
        setFormat(format);
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return this.format;
    }

    public void setMinimum(Comparable comparable) {
        if (getValueClass() == null && comparable != null) {
            setValueClass(comparable.getClass());
        }
        this.min = comparable;
    }

    public Comparable getMinimum() {
        return this.min;
    }

    public void setMaximum(Comparable comparable) {
        if (getValueClass() == null && comparable != null) {
            setValueClass(comparable.getClass());
        }
        this.max = comparable;
    }

    public Comparable getMaximum() {
        return this.max;
    }

    @Override // javax.swing.text.DefaultFormatter, javax.swing.JFormattedTextField.AbstractFormatter
    public void install(JFormattedTextField jFormattedTextField) throws SecurityException {
        super.install(jFormattedTextField);
        updateMaskIfNecessary();
        positionCursorAtInitialLocation();
    }

    @Override // javax.swing.text.DefaultFormatter, javax.swing.JFormattedTextField.AbstractFormatter
    public String valueToString(Object obj) throws ParseException {
        if (obj == null) {
            return "";
        }
        Format format = getFormat();
        if (format == null) {
            return obj.toString();
        }
        return format.format(obj);
    }

    @Override // javax.swing.text.DefaultFormatter, javax.swing.JFormattedTextField.AbstractFormatter
    public Object stringToValue(String str) throws SecurityException, ParseException {
        Object objStringToValue = stringToValue(str, getFormat());
        if (objStringToValue != null && getValueClass() != null && !getValueClass().isInstance(objStringToValue)) {
            objStringToValue = super.stringToValue(objStringToValue.toString());
        }
        try {
            if (!isValidValue(objStringToValue, true)) {
                throw new ParseException("Value not within min/max range", 0);
            }
            return objStringToValue;
        } catch (ClassCastException e2) {
            throw new ParseException("Class cast exception comparing values: " + ((Object) e2), 0);
        }
    }

    public Format.Field[] getFields(int i2) throws SecurityException {
        if (getAllowsInvalid()) {
            updateMask();
        }
        Map<AttributedCharacterIterator.Attribute, Object> attributes = getAttributes(i2);
        if (attributes != null && attributes.size() > 0) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(attributes.keySet());
            return (Format.Field[]) arrayList.toArray(EMPTY_FIELD_ARRAY);
        }
        return EMPTY_FIELD_ARRAY;
    }

    @Override // javax.swing.text.DefaultFormatter, javax.swing.JFormattedTextField.AbstractFormatter
    public Object clone() throws CloneNotSupportedException {
        InternationalFormatter internationalFormatter = (InternationalFormatter) super.clone();
        internationalFormatter.literalMask = null;
        internationalFormatter.iterator = null;
        internationalFormatter.validMask = false;
        internationalFormatter.string = null;
        return internationalFormatter;
    }

    @Override // javax.swing.JFormattedTextField.AbstractFormatter
    protected Action[] getActions() {
        if (getSupportsIncrement()) {
            return new Action[]{new IncrementAction("increment", 1), new IncrementAction("decrement", -1)};
        }
        return null;
    }

    Object stringToValue(String str, Format format) throws ParseException {
        if (format == null) {
            return str;
        }
        return format.parseObject(str);
    }

    boolean isValidValue(Object obj, boolean z2) {
        Comparable minimum = getMinimum();
        if (minimum != null) {
            try {
                if (minimum.compareTo(obj) > 0) {
                    return false;
                }
            } catch (ClassCastException e2) {
                if (z2) {
                    throw e2;
                }
                return false;
            }
        }
        Comparable maximum = getMaximum();
        if (maximum != null) {
            try {
                if (maximum.compareTo(obj) < 0) {
                    return false;
                }
                return true;
            } catch (ClassCastException e3) {
                if (z2) {
                    throw e3;
                }
                return false;
            }
        }
        return true;
    }

    Map<AttributedCharacterIterator.Attribute, Object> getAttributes(int i2) {
        if (isValidMask()) {
            AttributedCharacterIterator iterator = getIterator();
            if (i2 >= 0 && i2 <= iterator.getEndIndex()) {
                iterator.setIndex(i2);
                return iterator.getAttributes();
            }
            return null;
        }
        return null;
    }

    int getAttributeStart(AttributedCharacterIterator.Attribute attribute) {
        if (isValidMask()) {
            AttributedCharacterIterator iterator = getIterator();
            iterator.first();
            while (iterator.current() != 65535) {
                if (iterator.getAttribute(attribute) != null) {
                    return iterator.getIndex();
                }
                iterator.next();
            }
            return -1;
        }
        return -1;
    }

    AttributedCharacterIterator getIterator() {
        return this.iterator;
    }

    void updateMaskIfNecessary() throws SecurityException {
        if (!getAllowsInvalid() && getFormat() != null) {
            if (!isValidMask()) {
                updateMask();
            } else if (!getFormattedTextField().getText().equals(this.string)) {
                updateMask();
            }
        }
    }

    void updateMask() throws SecurityException {
        if (getFormat() != null) {
            Document document = getFormattedTextField().getDocument();
            this.validMask = false;
            if (document != null) {
                try {
                    this.string = document.getText(0, document.getLength());
                } catch (BadLocationException e2) {
                    this.string = null;
                }
                if (this.string != null) {
                    try {
                        updateMask(getFormat().formatToCharacterIterator(stringToValue(this.string)));
                    } catch (IllegalArgumentException e3) {
                    } catch (NullPointerException e4) {
                    } catch (ParseException e5) {
                    }
                }
            }
        }
    }

    int getLiteralCountTo(int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            if (isLiteral(i4)) {
                i3++;
            }
        }
        return i3;
    }

    boolean isLiteral(int i2) {
        if (isValidMask() && i2 < this.string.length()) {
            return this.literalMask.get(i2);
        }
        return false;
    }

    char getLiteral(int i2) {
        if (isValidMask() && this.string != null && i2 < this.string.length()) {
            return this.string.charAt(i2);
        }
        return (char) 0;
    }

    @Override // javax.swing.text.DefaultFormatter
    boolean isNavigatable(int i2) {
        return !isLiteral(i2);
    }

    @Override // javax.swing.text.DefaultFormatter
    void updateValue(Object obj) throws SecurityException {
        super.updateValue(obj);
        updateMaskIfNecessary();
    }

    @Override // javax.swing.text.DefaultFormatter
    void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws SecurityException, BadLocationException {
        if (this.ignoreDocumentMutate) {
            filterBypass.replace(i2, i3, str, attributeSet);
        } else {
            super.replace(filterBypass, i2, i3, str, attributeSet);
        }
    }

    private int getNextNonliteralIndex(int i2, int i3) {
        int length = getFormattedTextField().getDocument().getLength();
        while (i2 >= 0 && i2 < length) {
            if (isLiteral(i2)) {
                i2 += i3;
            } else {
                return i2;
            }
        }
        if (i3 == -1) {
            return 0;
        }
        return length;
    }

    @Override // javax.swing.text.DefaultFormatter
    boolean canReplace(DefaultFormatter.ReplaceHolder replaceHolder) {
        if (!getAllowsInvalid()) {
            String str = replaceHolder.text;
            int length = str != null ? str.length() : 0;
            JFormattedTextField formattedTextField = getFormattedTextField();
            if (length == 0 && replaceHolder.length == 1 && formattedTextField.getSelectionStart() != replaceHolder.offset) {
                replaceHolder.offset = getNextNonliteralIndex(replaceHolder.offset, -1);
            } else if (getOverwriteMode()) {
                int i2 = replaceHolder.offset;
                int i3 = i2;
                boolean z2 = false;
                int i4 = 0;
                while (true) {
                    if (i4 >= replaceHolder.length) {
                        break;
                    }
                    while (isLiteral(i2)) {
                        i2++;
                    }
                    if (i2 >= this.string.length()) {
                        i2 = i3;
                        z2 = true;
                        break;
                    }
                    i2++;
                    i3 = i2;
                    i4++;
                }
                if (z2 || formattedTextField.getSelectedText() == null) {
                    replaceHolder.length = i2 - replaceHolder.offset;
                }
            } else if (length > 0) {
                replaceHolder.offset = getNextNonliteralIndex(replaceHolder.offset, 1);
            } else {
                replaceHolder.offset = getNextNonliteralIndex(replaceHolder.offset, -1);
            }
            ((ExtendedReplaceHolder) replaceHolder).endOffset = replaceHolder.offset;
            ((ExtendedReplaceHolder) replaceHolder).endTextLength = replaceHolder.text != null ? replaceHolder.text.length() : 0;
        } else {
            ((ExtendedReplaceHolder) replaceHolder).endOffset = replaceHolder.offset;
            ((ExtendedReplaceHolder) replaceHolder).endTextLength = replaceHolder.text != null ? replaceHolder.text.length() : 0;
        }
        boolean zCanReplace = super.canReplace(replaceHolder);
        if (zCanReplace && !getAllowsInvalid()) {
            ((ExtendedReplaceHolder) replaceHolder).resetFromValue(this);
        }
        return zCanReplace;
    }

    @Override // javax.swing.text.DefaultFormatter
    boolean replace(DefaultFormatter.ReplaceHolder replaceHolder) throws BadLocationException {
        int selectionStart = -1;
        int i2 = 1;
        int literalCountTo = -1;
        if (replaceHolder.length > 0 && ((replaceHolder.text == null || replaceHolder.text.length() == 0) && (getFormattedTextField().getSelectionStart() != replaceHolder.offset || replaceHolder.length > 1))) {
            i2 = -1;
        }
        if (!getAllowsInvalid()) {
            if ((replaceHolder.text == null || replaceHolder.text.length() == 0) && replaceHolder.length > 0) {
                selectionStart = getFormattedTextField().getSelectionStart();
            } else {
                selectionStart = replaceHolder.offset;
            }
            literalCountTo = getLiteralCountTo(selectionStart);
        }
        if (super.replace(replaceHolder)) {
            if (selectionStart != -1) {
                repositionCursor(literalCountTo, ((ExtendedReplaceHolder) replaceHolder).endOffset + ((ExtendedReplaceHolder) replaceHolder).endTextLength, i2);
                return true;
            }
            int i3 = ((ExtendedReplaceHolder) replaceHolder).endOffset;
            if (i2 == 1) {
                i3 += ((ExtendedReplaceHolder) replaceHolder).endTextLength;
            }
            repositionCursor(i3, i2);
            return true;
        }
        return false;
    }

    private void repositionCursor(int i2, int i3, int i4) {
        if (getLiteralCountTo(i3) != i3) {
            i3 -= i2;
            for (int i5 = 0; i5 < i3; i5++) {
                if (isLiteral(i5)) {
                    i3++;
                }
            }
        }
        repositionCursor(i3, 1);
    }

    char getBufferedChar(int i2) {
        if (isValidMask() && this.string != null && i2 < this.string.length()) {
            return this.string.charAt(i2);
        }
        return (char) 0;
    }

    boolean isValidMask() {
        return this.validMask;
    }

    boolean isLiteral(Map map) {
        return map == null || map.size() == 0;
    }

    private void updateMask(AttributedCharacterIterator attributedCharacterIterator) {
        if (attributedCharacterIterator != null) {
            this.validMask = true;
            this.iterator = attributedCharacterIterator;
            if (this.literalMask == null) {
                this.literalMask = new BitSet();
            } else {
                for (int length = this.literalMask.length() - 1; length >= 0; length--) {
                    this.literalMask.clear(length);
                }
            }
            attributedCharacterIterator.first();
            while (attributedCharacterIterator.current() != 65535) {
                boolean zIsLiteral = isLiteral(attributedCharacterIterator.getAttributes());
                int index = attributedCharacterIterator.getIndex();
                int runLimit = attributedCharacterIterator.getRunLimit();
                while (index < runLimit) {
                    if (zIsLiteral) {
                        this.literalMask.set(index);
                    } else {
                        this.literalMask.clear(index);
                    }
                    index++;
                }
                attributedCharacterIterator.setIndex(index);
            }
        }
    }

    boolean canIncrement(Object obj, int i2) {
        return obj != null;
    }

    void selectField(Object obj, int i2) {
        AttributedCharacterIterator iterator = getIterator();
        if (iterator != null && (obj instanceof AttributedCharacterIterator.Attribute)) {
            AttributedCharacterIterator.Attribute attribute = (AttributedCharacterIterator.Attribute) obj;
            iterator.first();
            while (iterator.current() != 65535) {
                while (iterator.getAttribute(attribute) == null && iterator.next() != 65535) {
                }
                if (iterator.current() != 65535) {
                    int runLimit = iterator.getRunLimit(attribute);
                    i2--;
                    if (i2 <= 0) {
                        getFormattedTextField().select(iterator.getIndex(), runLimit);
                        return;
                    } else {
                        iterator.setIndex(runLimit);
                        iterator.next();
                    }
                }
            }
        }
    }

    Object getAdjustField(int i2, Map map) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getFieldTypeCountTo(Object obj, int i2) {
        AttributedCharacterIterator iterator = getIterator();
        int i3 = 0;
        if (iterator != null && (obj instanceof AttributedCharacterIterator.Attribute)) {
            AttributedCharacterIterator.Attribute attribute = (AttributedCharacterIterator.Attribute) obj;
            iterator.first();
            while (iterator.getIndex() < i2) {
                while (iterator.getAttribute(attribute) == null && iterator.next() != 65535) {
                }
                if (iterator.current() == 65535) {
                    break;
                }
                iterator.setIndex(iterator.getRunLimit(attribute));
                iterator.next();
                i3++;
            }
        }
        return i3;
    }

    Object adjustValue(Object obj, Map map, Object obj2, int i2) throws BadLocationException, ParseException {
        return null;
    }

    boolean getSupportsIncrement() {
        return false;
    }

    void resetValue(Object obj) throws SecurityException, BadLocationException, ParseException {
        Document document = getFormattedTextField().getDocument();
        String strValueToString = valueToString(obj);
        try {
            this.ignoreDocumentMutate = true;
            document.remove(0, document.getLength());
            document.insertString(0, strValueToString, null);
            this.ignoreDocumentMutate = false;
            updateValue(obj);
        } catch (Throwable th) {
            this.ignoreDocumentMutate = false;
            throw th;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, SecurityException {
        objectInputStream.defaultReadObject();
        updateMaskIfNecessary();
    }

    @Override // javax.swing.text.DefaultFormatter
    DefaultFormatter.ReplaceHolder getReplaceHolder(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) {
        if (this.replaceHolder == null) {
            this.replaceHolder = new ExtendedReplaceHolder();
        }
        return super.getReplaceHolder(filterBypass, i2, i3, str, attributeSet);
    }

    /* loaded from: rt.jar:javax/swing/text/InternationalFormatter$ExtendedReplaceHolder.class */
    static class ExtendedReplaceHolder extends DefaultFormatter.ReplaceHolder {
        int endOffset;
        int endTextLength;

        ExtendedReplaceHolder() {
        }

        void resetFromValue(InternationalFormatter internationalFormatter) {
            this.offset = 0;
            try {
                this.text = internationalFormatter.valueToString(this.value);
            } catch (ParseException e2) {
                this.text = "";
            }
            this.length = this.fb.getDocument().getLength();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/InternationalFormatter$IncrementAction.class */
    private class IncrementAction extends AbstractAction {
        private int direction;

        IncrementAction(String str, int i2) {
            super(str);
            this.direction = i2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws SecurityException {
            int selectionStart;
            if (InternationalFormatter.this.getFormattedTextField().isEditable()) {
                if (InternationalFormatter.this.getAllowsInvalid()) {
                    InternationalFormatter.this.updateMask();
                }
                boolean z2 = false;
                if (InternationalFormatter.this.isValidMask() && (selectionStart = InternationalFormatter.this.getFormattedTextField().getSelectionStart()) != -1) {
                    AttributedCharacterIterator iterator = InternationalFormatter.this.getIterator();
                    iterator.setIndex(selectionStart);
                    Map<AttributedCharacterIterator.Attribute, Object> attributes = iterator.getAttributes();
                    Object adjustField = InternationalFormatter.this.getAdjustField(selectionStart, attributes);
                    if (InternationalFormatter.this.canIncrement(adjustField, selectionStart)) {
                        try {
                            Object objStringToValue = InternationalFormatter.this.stringToValue(InternationalFormatter.this.getFormattedTextField().getText());
                            int fieldTypeCountTo = InternationalFormatter.this.getFieldTypeCountTo(adjustField, selectionStart);
                            Object objAdjustValue = InternationalFormatter.this.adjustValue(objStringToValue, attributes, adjustField, this.direction);
                            if (objAdjustValue != null && InternationalFormatter.this.isValidValue(objAdjustValue, false)) {
                                InternationalFormatter.this.resetValue(objAdjustValue);
                                InternationalFormatter.this.updateMask();
                                if (InternationalFormatter.this.isValidMask()) {
                                    InternationalFormatter.this.selectField(adjustField, fieldTypeCountTo);
                                }
                                z2 = true;
                            }
                        } catch (ParseException e2) {
                        } catch (BadLocationException e3) {
                        }
                    }
                }
                if (!z2) {
                    InternationalFormatter.this.invalidEdit();
                }
            }
        }
    }
}
