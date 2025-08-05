package java.text;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javafx.fxml.FXMLLoader;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:java/text/MessageFormat.class */
public class MessageFormat extends Format {
    private static final long serialVersionUID = 6479157306784022952L;
    private Locale locale;
    private String pattern;
    private static final int INITIAL_FORMATS = 10;
    private Format[] formats;
    private int[] offsets;
    private int[] argumentNumbers;
    private int maxOffset;
    private static final int SEG_RAW = 0;
    private static final int SEG_INDEX = 1;
    private static final int SEG_TYPE = 2;
    private static final int SEG_MODIFIER = 3;
    private static final int TYPE_NULL = 0;
    private static final int TYPE_NUMBER = 1;
    private static final int TYPE_DATE = 2;
    private static final int TYPE_TIME = 3;
    private static final int TYPE_CHOICE = 4;
    private static final int MODIFIER_DEFAULT = 0;
    private static final int MODIFIER_CURRENCY = 1;
    private static final int MODIFIER_PERCENT = 2;
    private static final int MODIFIER_INTEGER = 3;
    private static final int MODIFIER_SHORT = 1;
    private static final int MODIFIER_MEDIUM = 2;
    private static final int MODIFIER_LONG = 3;
    private static final int MODIFIER_FULL = 4;
    private static final String[] TYPE_KEYWORDS = {"", "number", "date", SchemaSymbols.ATTVAL_TIME, "choice"};
    private static final String[] NUMBER_MODIFIER_KEYWORDS = {"", "currency", Constants.ATTRNAME_PERCENT, SchemaSymbols.ATTVAL_INTEGER};
    private static final String[] DATE_TIME_MODIFIER_KEYWORDS = {"", SchemaSymbols.ATTVAL_SHORT, "medium", SchemaSymbols.ATTVAL_LONG, "full"};
    private static final int[] DATE_TIME_MODIFIERS = {2, 3, 2, 1, 0};

    public MessageFormat(String str) {
        this.pattern = "";
        this.formats = new Format[10];
        this.offsets = new int[10];
        this.argumentNumbers = new int[10];
        this.maxOffset = -1;
        this.locale = Locale.getDefault(Locale.Category.FORMAT);
        applyPattern(str);
    }

    public MessageFormat(String str, Locale locale) {
        this.pattern = "";
        this.formats = new Format[10];
        this.offsets = new int[10];
        this.argumentNumbers = new int[10];
        this.maxOffset = -1;
        this.locale = locale;
        applyPattern(str);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void applyPattern(String str) {
        StringBuilder[] sbArr = new StringBuilder[4];
        sbArr[0] = new StringBuilder();
        int i2 = 0;
        int i3 = 0;
        boolean z2 = false;
        int i4 = 0;
        this.maxOffset = -1;
        int i5 = 0;
        while (i5 < str.length()) {
            char cCharAt = str.charAt(i5);
            if (i2 == 0) {
                if (cCharAt == '\'') {
                    if (i5 + 1 < str.length() && str.charAt(i5 + 1) == '\'') {
                        sbArr[i2].append(cCharAt);
                        i5++;
                    } else {
                        z2 = !z2;
                    }
                } else if (cCharAt == '{' && !z2) {
                    i2 = 1;
                    if (sbArr[1] == null) {
                        sbArr[1] = new StringBuilder();
                    }
                } else {
                    sbArr[i2].append(cCharAt);
                }
            } else if (z2) {
                sbArr[i2].append(cCharAt);
                if (cCharAt == '\'') {
                    z2 = false;
                }
            } else {
                switch (cCharAt) {
                    case ' ':
                        if (i2 != 2 || sbArr[2].length() > 0) {
                            sbArr[i2].append(cCharAt);
                            break;
                        } else {
                            continue;
                        }
                    case '\'':
                        z2 = true;
                        break;
                    case ',':
                        if (i2 < 3) {
                            i2++;
                            if (sbArr[i2] == null) {
                                sbArr[i2] = new StringBuilder();
                                break;
                            } else {
                                continue;
                            }
                        } else {
                            sbArr[i2].append(cCharAt);
                            break;
                        }
                    case '{':
                        i4++;
                        sbArr[i2].append(cCharAt);
                        continue;
                    case '}':
                        if (i4 == 0) {
                            i2 = 0;
                            makeFormat(i5, i3, sbArr);
                            i3++;
                            sbArr[1] = null;
                            sbArr[2] = null;
                            sbArr[3] = null;
                            continue;
                        } else {
                            i4--;
                            sbArr[i2].append(cCharAt);
                            break;
                        }
                }
                sbArr[i2].append(cCharAt);
            }
            i5++;
        }
        if (i4 == 0 && i2 != 0) {
            this.maxOffset = -1;
            throw new IllegalArgumentException("Unmatched braces in the pattern.");
        }
        this.pattern = sbArr[0].toString();
    }

    public String toPattern() {
        int i2 = 0;
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 <= this.maxOffset; i3++) {
            copyAndFixQuotes(this.pattern, i2, this.offsets[i3], sb);
            i2 = this.offsets[i3];
            sb.append('{').append(this.argumentNumbers[i3]);
            Format format = this.formats[i3];
            if (format != null) {
                if (format instanceof NumberFormat) {
                    if (format.equals(NumberFormat.getInstance(this.locale))) {
                        sb.append(",number");
                    } else if (format.equals(NumberFormat.getCurrencyInstance(this.locale))) {
                        sb.append(",number,currency");
                    } else if (format.equals(NumberFormat.getPercentInstance(this.locale))) {
                        sb.append(",number,percent");
                    } else if (format.equals(NumberFormat.getIntegerInstance(this.locale))) {
                        sb.append(",number,integer");
                    } else if (format instanceof DecimalFormat) {
                        sb.append(",number,").append(((DecimalFormat) format).toPattern());
                    } else if (format instanceof ChoiceFormat) {
                        sb.append(",choice,").append(((ChoiceFormat) format).toPattern());
                    }
                } else if (format instanceof DateFormat) {
                    int i4 = 0;
                    while (true) {
                        if (i4 >= DATE_TIME_MODIFIERS.length) {
                            break;
                        }
                        if (format.equals(DateFormat.getDateInstance(DATE_TIME_MODIFIERS[i4], this.locale))) {
                            sb.append(",date");
                            break;
                        }
                        if (!format.equals(DateFormat.getTimeInstance(DATE_TIME_MODIFIERS[i4], this.locale))) {
                            i4++;
                        } else {
                            sb.append(",time");
                            break;
                        }
                    }
                    if (i4 >= DATE_TIME_MODIFIERS.length) {
                        if (format instanceof SimpleDateFormat) {
                            sb.append(",date,").append(((SimpleDateFormat) format).toPattern());
                        }
                    } else if (i4 != 0) {
                        sb.append(',').append(DATE_TIME_MODIFIER_KEYWORDS[i4]);
                    }
                }
            }
            sb.append('}');
        }
        copyAndFixQuotes(this.pattern, i2, this.pattern.length(), sb);
        return sb.toString();
    }

    public void setFormatsByArgumentIndex(Format[] formatArr) {
        for (int i2 = 0; i2 <= this.maxOffset; i2++) {
            int i3 = this.argumentNumbers[i2];
            if (i3 < formatArr.length) {
                this.formats[i2] = formatArr[i3];
            }
        }
    }

    public void setFormats(Format[] formatArr) {
        int length = formatArr.length;
        if (length > this.maxOffset + 1) {
            length = this.maxOffset + 1;
        }
        for (int i2 = 0; i2 < length; i2++) {
            this.formats[i2] = formatArr[i2];
        }
    }

    public void setFormatByArgumentIndex(int i2, Format format) {
        for (int i3 = 0; i3 <= this.maxOffset; i3++) {
            if (this.argumentNumbers[i3] == i2) {
                this.formats[i3] = format;
            }
        }
    }

    public void setFormat(int i2, Format format) {
        this.formats[i2] = format;
    }

    public Format[] getFormatsByArgumentIndex() {
        int i2 = -1;
        for (int i3 = 0; i3 <= this.maxOffset; i3++) {
            if (this.argumentNumbers[i3] > i2) {
                i2 = this.argumentNumbers[i3];
            }
        }
        Format[] formatArr = new Format[i2 + 1];
        for (int i4 = 0; i4 <= this.maxOffset; i4++) {
            formatArr[this.argumentNumbers[i4]] = this.formats[i4];
        }
        return formatArr;
    }

    public Format[] getFormats() {
        Format[] formatArr = new Format[this.maxOffset + 1];
        System.arraycopy(this.formats, 0, formatArr, 0, this.maxOffset + 1);
        return formatArr;
    }

    public final StringBuffer format(Object[] objArr, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        return subformat(objArr, stringBuffer, fieldPosition, null);
    }

    public static String format(String str, Object... objArr) {
        return new MessageFormat(str).format(objArr);
    }

    @Override // java.text.Format
    public final StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        return subformat((Object[]) obj, stringBuffer, fieldPosition, null);
    }

    @Override // java.text.Format
    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList arrayList = new ArrayList();
        if (obj == null) {
            throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
        }
        subformat((Object[]) obj, stringBuffer, null, arrayList);
        if (arrayList.size() == 0) {
            return createAttributedCharacterIterator("");
        }
        return createAttributedCharacterIterator((AttributedCharacterIterator[]) arrayList.toArray(new AttributedCharacterIterator[arrayList.size()]));
    }

    public Object[] parse(String str, ParsePosition parsePosition) {
        int i2;
        int iIndexOf;
        if (str == null) {
            return new Object[0];
        }
        int i3 = -1;
        for (int i4 = 0; i4 <= this.maxOffset; i4++) {
            if (this.argumentNumbers[i4] > i3) {
                i3 = this.argumentNumbers[i4];
            }
        }
        Object[] objArr = new Object[i3 + 1];
        int i5 = 0;
        int i6 = parsePosition.index;
        ParsePosition parsePosition2 = new ParsePosition(0);
        int i7 = 0;
        while (i7 <= this.maxOffset) {
            int i8 = this.offsets[i7] - i5;
            if (i8 == 0 || this.pattern.regionMatches(i5, str, i6, i8)) {
                int i9 = i6 + i8;
                i5 += i8;
                if (this.formats[i7] == null) {
                    int length = i7 != this.maxOffset ? this.offsets[i7 + 1] : this.pattern.length();
                    if (i5 >= length) {
                        iIndexOf = str.length();
                    } else {
                        iIndexOf = str.indexOf(this.pattern.substring(i5, length), i9);
                    }
                    if (iIndexOf < 0) {
                        parsePosition.errorIndex = i9;
                        return null;
                    }
                    if (!str.substring(i9, iIndexOf).equals(VectorFormat.DEFAULT_PREFIX + this.argumentNumbers[i7] + "}")) {
                        objArr[this.argumentNumbers[i7]] = str.substring(i9, iIndexOf);
                    }
                    i2 = iIndexOf;
                } else {
                    parsePosition2.index = i9;
                    objArr[this.argumentNumbers[i7]] = this.formats[i7].parseObject(str, parsePosition2);
                    if (parsePosition2.index == i9) {
                        parsePosition.errorIndex = i9;
                        return null;
                    }
                    i2 = parsePosition2.index;
                }
                i6 = i2;
                i7++;
            } else {
                parsePosition.errorIndex = i6;
                return null;
            }
        }
        int length2 = this.pattern.length() - i5;
        if (length2 == 0 || this.pattern.regionMatches(i5, str, i6, length2)) {
            parsePosition.index = i6 + length2;
            return objArr;
        }
        parsePosition.errorIndex = i6;
        return null;
    }

    public Object[] parse(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Object[] objArr = parse(str, parsePosition);
        if (parsePosition.index == 0) {
            throw new ParseException("MessageFormat parse error!", parsePosition.errorIndex);
        }
        return objArr;
    }

    @Override // java.text.Format
    public Object parseObject(String str, ParsePosition parsePosition) {
        return parse(str, parsePosition);
    }

    @Override // java.text.Format
    public Object clone() {
        MessageFormat messageFormat = (MessageFormat) super.clone();
        messageFormat.formats = (Format[]) this.formats.clone();
        for (int i2 = 0; i2 < this.formats.length; i2++) {
            if (this.formats[i2] != null) {
                messageFormat.formats[i2] = (Format) this.formats[i2].clone();
            }
        }
        messageFormat.offsets = (int[]) this.offsets.clone();
        messageFormat.argumentNumbers = (int[]) this.argumentNumbers.clone();
        return messageFormat;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MessageFormat messageFormat = (MessageFormat) obj;
        return this.maxOffset == messageFormat.maxOffset && this.pattern.equals(messageFormat.pattern) && ((this.locale != null && this.locale.equals(messageFormat.locale)) || (this.locale == null && messageFormat.locale == null)) && Arrays.equals(this.offsets, messageFormat.offsets) && Arrays.equals(this.argumentNumbers, messageFormat.argumentNumbers) && Arrays.equals(this.formats, messageFormat.formats);
    }

    public int hashCode() {
        return this.pattern.hashCode();
    }

    /* loaded from: rt.jar:java/text/MessageFormat$Field.class */
    public static class Field extends Format.Field {
        private static final long serialVersionUID = 7899943957617360810L;
        public static final Field ARGUMENT = new Field("message argument field");

        protected Field(String str) {
            super(str);
        }

        @Override // java.text.AttributedCharacterIterator.Attribute
        protected Object readResolve() throws InvalidObjectException {
            if (getClass() != Field.class) {
                throw new InvalidObjectException("subclass didn't correctly implement readResolve");
            }
            return ARGUMENT;
        }
    }

    private StringBuffer subformat(Object[] objArr, StringBuffer stringBuffer, FieldPosition fieldPosition, List<AttributedCharacterIterator> list) {
        int i2 = 0;
        int length = stringBuffer.length();
        for (int i3 = 0; i3 <= this.maxOffset; i3++) {
            stringBuffer.append(this.pattern.substring(i2, this.offsets[i3]));
            i2 = this.offsets[i3];
            int i4 = this.argumentNumbers[i3];
            if (objArr == null || i4 >= objArr.length) {
                stringBuffer.append('{').append(i4).append('}');
            } else {
                Object obj = objArr[i4];
                String string = null;
                Format dateTimeInstance = null;
                if (obj == null) {
                    string = FXMLLoader.NULL_KEYWORD;
                } else if (this.formats[i3] != null) {
                    dateTimeInstance = this.formats[i3];
                    if (dateTimeInstance instanceof ChoiceFormat) {
                        string = this.formats[i3].format(obj);
                        if (string.indexOf(123) >= 0) {
                            dateTimeInstance = new MessageFormat(string, this.locale);
                            obj = objArr;
                            string = null;
                        }
                    }
                } else if (obj instanceof Number) {
                    dateTimeInstance = NumberFormat.getInstance(this.locale);
                } else if (obj instanceof Date) {
                    dateTimeInstance = DateFormat.getDateTimeInstance(3, 3, this.locale);
                } else if (obj instanceof String) {
                    string = (String) obj;
                } else {
                    string = obj.toString();
                    if (string == null) {
                        string = FXMLLoader.NULL_KEYWORD;
                    }
                }
                if (list != null) {
                    if (length != stringBuffer.length()) {
                        list.add(createAttributedCharacterIterator(stringBuffer.substring(length)));
                        length = stringBuffer.length();
                    }
                    if (dateTimeInstance != null) {
                        AttributedCharacterIterator toCharacterIterator = dateTimeInstance.formatToCharacterIterator(obj);
                        append(stringBuffer, toCharacterIterator);
                        if (length != stringBuffer.length()) {
                            list.add(createAttributedCharacterIterator(toCharacterIterator, Field.ARGUMENT, Integer.valueOf(i4)));
                            length = stringBuffer.length();
                        }
                        string = null;
                    }
                    if (string != null && string.length() > 0) {
                        stringBuffer.append(string);
                        list.add(createAttributedCharacterIterator(string, Field.ARGUMENT, Integer.valueOf(i4)));
                        length = stringBuffer.length();
                    }
                } else {
                    if (dateTimeInstance != null) {
                        string = dateTimeInstance.format(obj);
                    }
                    int length2 = stringBuffer.length();
                    stringBuffer.append(string);
                    if (i3 == 0 && fieldPosition != null && Field.ARGUMENT.equals(fieldPosition.getFieldAttribute())) {
                        fieldPosition.setBeginIndex(length2);
                        fieldPosition.setEndIndex(stringBuffer.length());
                    }
                    length = stringBuffer.length();
                }
            }
        }
        stringBuffer.append(this.pattern.substring(i2, this.pattern.length()));
        if (list != null && length != stringBuffer.length()) {
            list.add(createAttributedCharacterIterator(stringBuffer.substring(length)));
        }
        return stringBuffer;
    }

    private void append(StringBuffer stringBuffer, CharacterIterator characterIterator) {
        if (characterIterator.first() != 65535) {
            stringBuffer.append(characterIterator.first());
            while (true) {
                char next = characterIterator.next();
                if (next != 65535) {
                    stringBuffer.append(next);
                } else {
                    return;
                }
            }
        }
    }

    private void makeFormat(int i2, int i3, StringBuilder[] sbArr) {
        String[] strArr = new String[sbArr.length];
        for (int i4 = 0; i4 < sbArr.length; i4++) {
            StringBuilder sb = sbArr[i4];
            strArr[i4] = sb != null ? sb.toString() : "";
        }
        try {
            int i5 = Integer.parseInt(strArr[1]);
            if (i5 < 0) {
                throw new IllegalArgumentException("negative argument number: " + i5);
            }
            if (i3 >= this.formats.length) {
                int length = this.formats.length * 2;
                Format[] formatArr = new Format[length];
                int[] iArr = new int[length];
                int[] iArr2 = new int[length];
                System.arraycopy(this.formats, 0, formatArr, 0, this.maxOffset + 1);
                System.arraycopy(this.offsets, 0, iArr, 0, this.maxOffset + 1);
                System.arraycopy(this.argumentNumbers, 0, iArr2, 0, this.maxOffset + 1);
                this.formats = formatArr;
                this.offsets = iArr;
                this.argumentNumbers = iArr2;
            }
            int i6 = this.maxOffset;
            this.maxOffset = i3;
            this.offsets[i3] = strArr[0].length();
            this.argumentNumbers[i3] = i5;
            Format choiceFormat = null;
            if (strArr[2].length() != 0) {
                int iFindKeyword = findKeyword(strArr[2], TYPE_KEYWORDS);
                switch (iFindKeyword) {
                    case 0:
                        break;
                    case 1:
                        switch (findKeyword(strArr[3], NUMBER_MODIFIER_KEYWORDS)) {
                            case 0:
                                choiceFormat = NumberFormat.getInstance(this.locale);
                                break;
                            case 1:
                                choiceFormat = NumberFormat.getCurrencyInstance(this.locale);
                                break;
                            case 2:
                                choiceFormat = NumberFormat.getPercentInstance(this.locale);
                                break;
                            case 3:
                                choiceFormat = NumberFormat.getIntegerInstance(this.locale);
                                break;
                            default:
                                try {
                                    choiceFormat = new DecimalFormat(strArr[3], DecimalFormatSymbols.getInstance(this.locale));
                                    break;
                                } catch (IllegalArgumentException e2) {
                                    this.maxOffset = i6;
                                    throw e2;
                                }
                        }
                    case 2:
                    case 3:
                        int iFindKeyword2 = findKeyword(strArr[3], DATE_TIME_MODIFIER_KEYWORDS);
                        if (iFindKeyword2 >= 0 && iFindKeyword2 < DATE_TIME_MODIFIER_KEYWORDS.length) {
                            if (iFindKeyword == 2) {
                                choiceFormat = DateFormat.getDateInstance(DATE_TIME_MODIFIERS[iFindKeyword2], this.locale);
                                break;
                            } else {
                                choiceFormat = DateFormat.getTimeInstance(DATE_TIME_MODIFIERS[iFindKeyword2], this.locale);
                                break;
                            }
                        } else {
                            try {
                                choiceFormat = new SimpleDateFormat(strArr[3], this.locale);
                                break;
                            } catch (IllegalArgumentException e3) {
                                this.maxOffset = i6;
                                throw e3;
                            }
                        }
                        break;
                    case 4:
                        try {
                            choiceFormat = new ChoiceFormat(strArr[3]);
                            break;
                        } catch (Exception e4) {
                            this.maxOffset = i6;
                            throw new IllegalArgumentException("Choice Pattern incorrect: " + strArr[3], e4);
                        }
                    default:
                        this.maxOffset = i6;
                        throw new IllegalArgumentException("unknown format type: " + strArr[2]);
                }
            }
            this.formats[i3] = choiceFormat;
        } catch (NumberFormatException e5) {
            throw new IllegalArgumentException("can't parse argument number: " + strArr[1], e5);
        }
    }

    private static final int findKeyword(String str, String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (str.equals(strArr[i2])) {
                return i2;
            }
        }
        String lowerCase = str.trim().toLowerCase(Locale.ROOT);
        if (lowerCase != str) {
            for (int i3 = 0; i3 < strArr.length; i3++) {
                if (lowerCase.equals(strArr[i3])) {
                    return i3;
                }
            }
            return -1;
        }
        return -1;
    }

    private static final void copyAndFixQuotes(String str, int i2, int i3, StringBuilder sb) {
        boolean z2 = false;
        for (int i4 = i2; i4 < i3; i4++) {
            char cCharAt = str.charAt(i4);
            if (cCharAt == '{') {
                if (!z2) {
                    sb.append('\'');
                    z2 = true;
                }
                sb.append(cCharAt);
            } else if (cCharAt == '\'') {
                sb.append("''");
            } else {
                if (z2) {
                    sb.append('\'');
                    z2 = false;
                }
                sb.append(cCharAt);
            }
        }
        if (z2) {
            sb.append('\'');
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        boolean z2 = this.maxOffset >= -1 && this.formats.length > this.maxOffset && this.offsets.length > this.maxOffset && this.argumentNumbers.length > this.maxOffset;
        if (z2) {
            int length = this.pattern.length() + 1;
            for (int i2 = this.maxOffset; i2 >= 0; i2--) {
                if (this.offsets[i2] < 0 || this.offsets[i2] > length) {
                    z2 = false;
                    break;
                }
                length = this.offsets[i2];
            }
        }
        if (!z2) {
            throw new InvalidObjectException("Could not reconstruct MessageFormat from corrupt stream.");
        }
    }
}
