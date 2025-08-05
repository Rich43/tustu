package javax.swing.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;

/* loaded from: rt.jar:javax/swing/text/MaskFormatter.class */
public class MaskFormatter extends DefaultFormatter {
    private static final char DIGIT_KEY = '#';
    private static final char LITERAL_KEY = '\'';
    private static final char UPPERCASE_KEY = 'U';
    private static final char LOWERCASE_KEY = 'L';
    private static final char ALPHA_NUMERIC_KEY = 'A';
    private static final char CHARACTER_KEY = '?';
    private static final char ANYTHING_KEY = '*';
    private static final char HEX_KEY = 'H';
    private static final MaskCharacter[] EmptyMaskChars = new MaskCharacter[0];
    private String mask;
    private transient MaskCharacter[] maskChars;
    private String validCharacters;
    private String invalidCharacters;
    private String placeholderString;
    private char placeholder;
    private boolean containsLiteralChars;

    public MaskFormatter() {
        setAllowsInvalid(false);
        this.containsLiteralChars = true;
        this.maskChars = EmptyMaskChars;
        this.placeholder = ' ';
    }

    public MaskFormatter(String str) throws ParseException {
        this();
        setMask(str);
    }

    public void setMask(String str) throws ParseException {
        this.mask = str;
        updateInternalMask();
    }

    public String getMask() {
        return this.mask;
    }

    public void setValidCharacters(String str) {
        this.validCharacters = str;
    }

    public String getValidCharacters() {
        return this.validCharacters;
    }

    public void setInvalidCharacters(String str) {
        this.invalidCharacters = str;
    }

    public String getInvalidCharacters() {
        return this.invalidCharacters;
    }

    public void setPlaceholder(String str) {
        this.placeholderString = str;
    }

    public String getPlaceholder() {
        return this.placeholderString;
    }

    public void setPlaceholderCharacter(char c2) {
        this.placeholder = c2;
    }

    public char getPlaceholderCharacter() {
        return this.placeholder;
    }

    public void setValueContainsLiteralCharacters(boolean z2) {
        this.containsLiteralChars = z2;
    }

    public boolean getValueContainsLiteralCharacters() {
        return this.containsLiteralChars;
    }

    @Override // javax.swing.text.DefaultFormatter, javax.swing.JFormattedTextField.AbstractFormatter
    public Object stringToValue(String str) throws ParseException {
        return stringToValue(str, true);
    }

    @Override // javax.swing.text.DefaultFormatter, javax.swing.JFormattedTextField.AbstractFormatter
    public String valueToString(Object obj) throws ParseException {
        String string = obj == null ? "" : obj.toString();
        StringBuilder sb = new StringBuilder();
        append(sb, string, new int[]{0}, getPlaceholder(), this.maskChars);
        return sb.toString();
    }

    @Override // javax.swing.text.DefaultFormatter, javax.swing.JFormattedTextField.AbstractFormatter
    public void install(JFormattedTextField jFormattedTextField) {
        super.install(jFormattedTextField);
        if (jFormattedTextField != null) {
            try {
                stringToValue(valueToString(jFormattedTextField.getValue()));
            } catch (ParseException e2) {
                setEditValid(false);
            }
        }
    }

    private Object stringToValue(String str, boolean z2) throws ParseException {
        int invalidOffset = getInvalidOffset(str, z2);
        if (invalidOffset == -1) {
            if (!getValueContainsLiteralCharacters()) {
                str = stripLiteralChars(str);
            }
            return super.stringToValue(str);
        }
        throw new ParseException("stringToValue passed invalid value", invalidOffset);
    }

    private int getInvalidOffset(String str, boolean z2) {
        int length = str.length();
        if (length != getMaxLength()) {
            return length;
        }
        int length2 = str.length();
        for (int i2 = 0; i2 < length2; i2++) {
            char cCharAt = str.charAt(i2);
            if (!isValidCharacter(i2, cCharAt) && (z2 || !isPlaceholder(i2, cCharAt))) {
                return i2;
            }
        }
        return -1;
    }

    private void append(StringBuilder sb, String str, int[] iArr, String str2, MaskCharacter[] maskCharacterArr) throws ParseException {
        for (MaskCharacter maskCharacter : maskCharacterArr) {
            maskCharacter.append(sb, str, iArr, str2);
        }
    }

    private void updateInternalMask() throws ParseException {
        String mask = getMask();
        ArrayList arrayList = new ArrayList();
        if (mask != null) {
            int i2 = 0;
            int length = mask.length();
            while (i2 < length) {
                char cCharAt = mask.charAt(i2);
                switch (cCharAt) {
                    case '#':
                        arrayList.add(new DigitMaskCharacter());
                        break;
                    case '\'':
                        i2++;
                        if (i2 >= length) {
                            break;
                        } else {
                            arrayList.add(new LiteralCharacter(mask.charAt(i2)));
                            break;
                        }
                    case '*':
                        arrayList.add(new MaskCharacter());
                        break;
                    case '?':
                        arrayList.add(new CharCharacter());
                        break;
                    case 'A':
                        arrayList.add(new AlphaNumericCharacter());
                        break;
                    case 'H':
                        arrayList.add(new HexCharacter());
                        break;
                    case 'L':
                        arrayList.add(new LowerCaseCharacter());
                        break;
                    case 'U':
                        arrayList.add(new UpperCaseCharacter());
                        break;
                    default:
                        arrayList.add(new LiteralCharacter(cCharAt));
                        break;
                }
                i2++;
            }
        }
        if (arrayList.size() == 0) {
            this.maskChars = EmptyMaskChars;
        } else {
            this.maskChars = new MaskCharacter[arrayList.size()];
            arrayList.toArray(this.maskChars);
        }
    }

    private MaskCharacter getMaskCharacter(int i2) {
        if (i2 >= this.maskChars.length) {
            return null;
        }
        return this.maskChars[i2];
    }

    private boolean isPlaceholder(int i2, char c2) {
        return getPlaceholderCharacter() == c2;
    }

    private boolean isValidCharacter(int i2, char c2) {
        return getMaskCharacter(i2).isValidCharacter(c2);
    }

    private boolean isLiteral(int i2) {
        return getMaskCharacter(i2).isLiteral();
    }

    private int getMaxLength() {
        return this.maskChars.length;
    }

    private char getLiteral(int i2) {
        return getMaskCharacter(i2).getChar((char) 0);
    }

    private char getCharacter(int i2, char c2) {
        return getMaskCharacter(i2).getChar(c2);
    }

    private String stripLiteralChars(String str) {
        StringBuilder sb = null;
        int i2 = 0;
        int length = str.length();
        for (int i3 = 0; i3 < length; i3++) {
            if (isLiteral(i3)) {
                if (sb == null) {
                    sb = new StringBuilder();
                    if (i3 > 0) {
                        sb.append(str.substring(0, i3));
                    }
                    int i4 = i3 + 1;
                } else if (i2 != i3) {
                    sb.append(str.substring(i2, i3));
                }
                i2 = i3 + 1;
            }
        }
        if (sb == null) {
            return str;
        }
        if (i2 != str.length()) {
            if (sb == null) {
                return str.substring(i2);
            }
            sb.append(str.substring(i2));
        }
        return sb.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            updateInternalMask();
        } catch (ParseException e2) {
        }
    }

    @Override // javax.swing.text.DefaultFormatter
    boolean isNavigatable(int i2) {
        if (getAllowsInvalid()) {
            return true;
        }
        return i2 < getMaxLength() && !isLiteral(i2);
    }

    @Override // javax.swing.text.DefaultFormatter
    boolean isValidEdit(DefaultFormatter.ReplaceHolder replaceHolder) {
        if (!getAllowsInvalid()) {
            try {
                replaceHolder.value = stringToValue(getReplaceString(replaceHolder.offset, replaceHolder.length, replaceHolder.text), false);
                return true;
            } catch (ParseException e2) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.swing.text.DefaultFormatter
    boolean canReplace(DefaultFormatter.ReplaceHolder replaceHolder) {
        if (!getAllowsInvalid()) {
            StringBuilder sb = null;
            String str = replaceHolder.text;
            int length = str != null ? str.length() : 0;
            if (length == 0 && replaceHolder.length == 1 && getFormattedTextField().getSelectionStart() != replaceHolder.offset) {
                while (replaceHolder.offset > 0 && isLiteral(replaceHolder.offset)) {
                    replaceHolder.offset--;
                }
            }
            int iMin = Math.min(getMaxLength() - replaceHolder.offset, Math.max(length, replaceHolder.length));
            int i2 = 0;
            int i3 = 0;
            while (i2 < iMin) {
                if (i3 < length && isValidCharacter(replaceHolder.offset + i2, str.charAt(i3))) {
                    char cCharAt = str.charAt(i3);
                    if (cCharAt != getCharacter(replaceHolder.offset + i2, cCharAt) && sb == null) {
                        sb = new StringBuilder();
                        if (i3 > 0) {
                            sb.append(str.substring(0, i3));
                        }
                    }
                    if (sb != null) {
                        sb.append(getCharacter(replaceHolder.offset + i2, cCharAt));
                    }
                    i3++;
                } else if (isLiteral(replaceHolder.offset + i2)) {
                    if (sb != null) {
                        sb.append(getLiteral(replaceHolder.offset + i2));
                        if (i3 < length) {
                            iMin = Math.min(iMin + 1, getMaxLength() - replaceHolder.offset);
                        }
                    } else if (i3 > 0) {
                        sb = new StringBuilder(iMin);
                        sb.append(str.substring(0, i3));
                        sb.append(getLiteral(replaceHolder.offset + i2));
                        if (i3 < length) {
                            iMin = Math.min(iMin + 1, getMaxLength() - replaceHolder.offset);
                        } else if (replaceHolder.cursorPosition == -1) {
                            replaceHolder.cursorPosition = replaceHolder.offset + i2;
                        }
                    } else {
                        replaceHolder.offset++;
                        replaceHolder.length--;
                        i2--;
                        iMin--;
                    }
                } else if (i3 >= length) {
                    if (sb == null) {
                        sb = new StringBuilder();
                        if (str != null) {
                            sb.append(str);
                        }
                    }
                    sb.append(getPlaceholderCharacter());
                    if (length > 0 && replaceHolder.cursorPosition == -1) {
                        replaceHolder.cursorPosition = replaceHolder.offset + i2;
                    }
                } else {
                    return false;
                }
                i2++;
            }
            if (sb != null) {
                replaceHolder.text = sb.toString();
            } else if (str != null && replaceHolder.offset + length > getMaxLength()) {
                replaceHolder.text = str.substring(0, getMaxLength() - replaceHolder.offset);
            }
            if (getOverwriteMode() && replaceHolder.text != null) {
                replaceHolder.length = replaceHolder.text.length();
            }
        }
        return super.canReplace(replaceHolder);
    }

    /* loaded from: rt.jar:javax/swing/text/MaskFormatter$MaskCharacter.class */
    private class MaskCharacter {
        private MaskCharacter() {
        }

        public boolean isLiteral() {
            return false;
        }

        public boolean isValidCharacter(char c2) {
            if (isLiteral()) {
                return getChar(c2) == c2;
            }
            char c3 = getChar(c2);
            String validCharacters = MaskFormatter.this.getValidCharacters();
            if (validCharacters != null && validCharacters.indexOf(c3) == -1) {
                return false;
            }
            String invalidCharacters = MaskFormatter.this.getInvalidCharacters();
            if (invalidCharacters != null && invalidCharacters.indexOf(c3) != -1) {
                return false;
            }
            return true;
        }

        public char getChar(char c2) {
            return c2;
        }

        public void append(StringBuilder sb, String str, int[] iArr, String str2) throws ParseException {
            boolean z2 = iArr[0] < str.length();
            char cCharAt = z2 ? str.charAt(iArr[0]) : (char) 0;
            if (isLiteral()) {
                sb.append(getChar(cCharAt));
                if (MaskFormatter.this.getValueContainsLiteralCharacters()) {
                    if (z2 && cCharAt != getChar(cCharAt)) {
                        throw new ParseException("Invalid character: " + cCharAt, iArr[0]);
                    }
                    iArr[0] = iArr[0] + 1;
                    return;
                }
                return;
            }
            if (iArr[0] >= str.length()) {
                if (str2 != null && iArr[0] < str2.length()) {
                    sb.append(str2.charAt(iArr[0]));
                } else {
                    sb.append(MaskFormatter.this.getPlaceholderCharacter());
                }
                iArr[0] = iArr[0] + 1;
                return;
            }
            if (isValidCharacter(cCharAt)) {
                sb.append(getChar(cCharAt));
                iArr[0] = iArr[0] + 1;
                return;
            }
            throw new ParseException("Invalid character: " + cCharAt, iArr[0]);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/MaskFormatter$LiteralCharacter.class */
    private class LiteralCharacter extends MaskCharacter {
        private char fixedChar;

        public LiteralCharacter(char c2) {
            super();
            this.fixedChar = c2;
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public boolean isLiteral() {
            return true;
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public char getChar(char c2) {
            return this.fixedChar;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/MaskFormatter$DigitMaskCharacter.class */
    private class DigitMaskCharacter extends MaskCharacter {
        private DigitMaskCharacter() {
            super();
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public boolean isValidCharacter(char c2) {
            return Character.isDigit(c2) && super.isValidCharacter(c2);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/MaskFormatter$UpperCaseCharacter.class */
    private class UpperCaseCharacter extends MaskCharacter {
        private UpperCaseCharacter() {
            super();
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public boolean isValidCharacter(char c2) {
            return Character.isLetter(c2) && super.isValidCharacter(c2);
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public char getChar(char c2) {
            return Character.toUpperCase(c2);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/MaskFormatter$LowerCaseCharacter.class */
    private class LowerCaseCharacter extends MaskCharacter {
        private LowerCaseCharacter() {
            super();
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public boolean isValidCharacter(char c2) {
            return Character.isLetter(c2) && super.isValidCharacter(c2);
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public char getChar(char c2) {
            return Character.toLowerCase(c2);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/MaskFormatter$AlphaNumericCharacter.class */
    private class AlphaNumericCharacter extends MaskCharacter {
        private AlphaNumericCharacter() {
            super();
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public boolean isValidCharacter(char c2) {
            return Character.isLetterOrDigit(c2) && super.isValidCharacter(c2);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/MaskFormatter$CharCharacter.class */
    private class CharCharacter extends MaskCharacter {
        private CharCharacter() {
            super();
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public boolean isValidCharacter(char c2) {
            return Character.isLetter(c2) && super.isValidCharacter(c2);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/MaskFormatter$HexCharacter.class */
    private class HexCharacter extends MaskCharacter {
        private HexCharacter() {
            super();
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public boolean isValidCharacter(char c2) {
            return (c2 == '0' || c2 == '1' || c2 == '2' || c2 == '3' || c2 == '4' || c2 == '5' || c2 == '6' || c2 == '7' || c2 == '8' || c2 == '9' || c2 == 'a' || c2 == 'A' || c2 == 'b' || c2 == 'B' || c2 == 'c' || c2 == 'C' || c2 == 'd' || c2 == 'D' || c2 == 'e' || c2 == 'E' || c2 == 'f' || c2 == 'F') && super.isValidCharacter(c2);
        }

        @Override // javax.swing.text.MaskFormatter.MaskCharacter
        public char getChar(char c2) {
            if (Character.isDigit(c2)) {
                return c2;
            }
            return Character.toUpperCase(c2);
        }
    }
}
