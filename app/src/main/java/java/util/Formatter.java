package java.util;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import org.slf4j.Marker;
import sun.misc.FormattedFloatingDecimal;

/* loaded from: rt.jar:java/util/Formatter.class */
public final class Formatter implements Closeable, Flushable {

    /* renamed from: a, reason: collision with root package name */
    private Appendable f12552a;

    /* renamed from: l, reason: collision with root package name */
    private final Locale f12553l;
    private IOException lastException;
    private final char zero;
    private static double scaleUp;
    private static final int MAX_FD_CHARS = 30;
    private static final String formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    private static Pattern fsPattern = Pattern.compile(formatSpecifier);

    /* loaded from: rt.jar:java/util/Formatter$BigDecimalLayoutForm.class */
    public enum BigDecimalLayoutForm {
        SCIENTIFIC,
        DECIMAL_FLOAT
    }

    /* loaded from: rt.jar:java/util/Formatter$FormatString.class */
    private interface FormatString {
        int index();

        void print(Object obj, Locale locale) throws IOException;

        String toString();
    }

    private static Charset toCharset(String str) throws UnsupportedEncodingException {
        Objects.requireNonNull(str, "charsetName");
        try {
            return Charset.forName(str);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e2) {
            throw new UnsupportedEncodingException(str);
        }
    }

    private static final Appendable nonNullAppendable(Appendable appendable) {
        if (appendable == null) {
            return new StringBuilder();
        }
        return appendable;
    }

    private Formatter(Locale locale, Appendable appendable) {
        this.f12552a = appendable;
        this.f12553l = locale;
        this.zero = getZero(locale);
    }

    private Formatter(Charset charset, Locale locale, File file) throws FileNotFoundException {
        this(locale, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset)));
    }

    public Formatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), new StringBuilder());
    }

    public Formatter(Appendable appendable) {
        this(Locale.getDefault(Locale.Category.FORMAT), nonNullAppendable(appendable));
    }

    public Formatter(Locale locale) {
        this(locale, new StringBuilder());
    }

    public Formatter(Appendable appendable, Locale locale) {
        this(locale, nonNullAppendable(appendable));
    }

    public Formatter(String str) throws FileNotFoundException {
        this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str))));
    }

    public Formatter(String str, String str2) throws UnsupportedEncodingException, FileNotFoundException {
        this(str, str2, Locale.getDefault(Locale.Category.FORMAT));
    }

    public Formatter(String str, String str2, Locale locale) throws UnsupportedEncodingException, FileNotFoundException {
        this(toCharset(str2), locale, new File(str));
    }

    public Formatter(File file) throws FileNotFoundException {
        this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))));
    }

    public Formatter(File file, String str) throws UnsupportedEncodingException, FileNotFoundException {
        this(file, str, Locale.getDefault(Locale.Category.FORMAT));
    }

    public Formatter(File file, String str, Locale locale) throws UnsupportedEncodingException, FileNotFoundException {
        this(toCharset(str), locale, file);
    }

    public Formatter(PrintStream printStream) {
        this(Locale.getDefault(Locale.Category.FORMAT), (Appendable) Objects.requireNonNull(printStream));
    }

    public Formatter(OutputStream outputStream) {
        this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(new OutputStreamWriter(outputStream)));
    }

    public Formatter(OutputStream outputStream, String str) throws UnsupportedEncodingException {
        this(outputStream, str, Locale.getDefault(Locale.Category.FORMAT));
    }

    public Formatter(OutputStream outputStream, String str, Locale locale) throws UnsupportedEncodingException {
        this(locale, new BufferedWriter(new OutputStreamWriter(outputStream, str)));
    }

    private static char getZero(Locale locale) {
        if (locale != null && !locale.equals(Locale.US)) {
            return DecimalFormatSymbols.getInstance(locale).getZeroDigit();
        }
        return '0';
    }

    public Locale locale() {
        ensureOpen();
        return this.f12553l;
    }

    public Appendable out() {
        ensureOpen();
        return this.f12552a;
    }

    public String toString() {
        ensureOpen();
        return this.f12552a.toString();
    }

    @Override // java.io.Flushable
    public void flush() {
        ensureOpen();
        if (this.f12552a instanceof Flushable) {
            try {
                ((Flushable) this.f12552a).flush();
            } catch (IOException e2) {
                this.lastException = e2;
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            if (this.f12552a == null) {
                return;
            }
            if (this.f12552a instanceof Closeable) {
                ((Closeable) this.f12552a).close();
            }
        } catch (IOException e2) {
            this.lastException = e2;
        } finally {
            this.f12552a = null;
        }
    }

    private void ensureOpen() {
        if (this.f12552a == null) {
            throw new FormatterClosedException();
        }
    }

    public IOException ioException() {
        return this.lastException;
    }

    public Formatter format(String str, Object... objArr) {
        return format(this.f12553l, str, objArr);
    }

    public Formatter format(Locale locale, String str, Object... objArr) {
        ensureOpen();
        int i2 = -1;
        int i3 = -1;
        for (FormatString formatString : parse(str)) {
            int iIndex = formatString.index();
            try {
                switch (iIndex) {
                    case -2:
                        formatString.print(null, locale);
                        break;
                    case -1:
                        if (i2 < 0 || (objArr != null && i2 > objArr.length - 1)) {
                            throw new MissingFormatArgumentException(formatString.toString());
                        }
                        formatString.print(objArr == null ? null : objArr[i2], locale);
                        break;
                    case 0:
                        i3++;
                        i2 = i3;
                        if (objArr != null && i3 > objArr.length - 1) {
                            throw new MissingFormatArgumentException(formatString.toString());
                        }
                        formatString.print(objArr == null ? null : objArr[i3], locale);
                        break;
                    default:
                        i2 = iIndex - 1;
                        if (objArr != null && i2 > objArr.length - 1) {
                            throw new MissingFormatArgumentException(formatString.toString());
                        }
                        formatString.print(objArr == null ? null : objArr[i2], locale);
                        break;
                        break;
                }
            } catch (IOException e2) {
                this.lastException = e2;
            }
        }
        return this;
    }

    private FormatString[] parse(String str) {
        ArrayList arrayList = new ArrayList();
        Matcher matcher = fsPattern.matcher(str);
        int iEnd = 0;
        int length = str.length();
        while (true) {
            if (iEnd < length) {
                if (matcher.find(iEnd)) {
                    if (matcher.start() != iEnd) {
                        checkText(str, iEnd, matcher.start());
                        arrayList.add(new FixedString(str.substring(iEnd, matcher.start())));
                    }
                    arrayList.add(new FormatSpecifier(matcher));
                    iEnd = matcher.end();
                } else {
                    checkText(str, iEnd, length);
                    arrayList.add(new FixedString(str.substring(iEnd)));
                    break;
                }
            } else {
                break;
            }
        }
        return (FormatString[]) arrayList.toArray(new FormatString[arrayList.size()]);
    }

    private static void checkText(String str, int i2, int i3) {
        int i4 = i2;
        while (i4 < i3) {
            if (str.charAt(i4) != '%') {
                i4++;
            } else {
                throw new UnknownFormatConversionException(String.valueOf(i4 == i3 - 1 ? '%' : str.charAt(i4 + 1)));
            }
        }
    }

    /* loaded from: rt.jar:java/util/Formatter$FixedString.class */
    private class FixedString implements FormatString {

        /* renamed from: s, reason: collision with root package name */
        private String f12554s;

        FixedString(String str) {
            this.f12554s = str;
        }

        @Override // java.util.Formatter.FormatString
        public int index() {
            return -2;
        }

        @Override // java.util.Formatter.FormatString
        public void print(Object obj, Locale locale) throws IOException {
            Formatter.this.f12552a.append(this.f12554s);
        }

        @Override // java.util.Formatter.FormatString
        public String toString() {
            return this.f12554s;
        }
    }

    /* loaded from: rt.jar:java/util/Formatter$FormatSpecifier.class */
    private class FormatSpecifier implements FormatString {
        private int index = -1;

        /* renamed from: f, reason: collision with root package name */
        private Flags f12555f = Flags.NONE;
        private int width;
        private int precision;
        private boolean dt;

        /* renamed from: c, reason: collision with root package name */
        private char f12556c;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Formatter.class.desiredAssertionStatus();
        }

        private int index(String str) {
            if (str != null) {
                try {
                    this.index = Integer.parseInt(str.substring(0, str.length() - 1));
                } catch (NumberFormatException e2) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                }
            } else {
                this.index = 0;
            }
            return this.index;
        }

        @Override // java.util.Formatter.FormatString
        public int index() {
            return this.index;
        }

        private Flags flags(String str) {
            this.f12555f = Flags.parse(str);
            if (this.f12555f.contains(Flags.PREVIOUS)) {
                this.index = -1;
            }
            return this.f12555f;
        }

        Flags flags() {
            return this.f12555f;
        }

        private int width(String str) {
            this.width = -1;
            if (str != null) {
                try {
                    this.width = Integer.parseInt(str);
                    if (this.width < 0) {
                        throw new IllegalFormatWidthException(this.width);
                    }
                } catch (NumberFormatException e2) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                }
            }
            return this.width;
        }

        int width() {
            return this.width;
        }

        private int precision(String str) {
            this.precision = -1;
            if (str != null) {
                try {
                    this.precision = Integer.parseInt(str.substring(1));
                    if (this.precision < 0) {
                        throw new IllegalFormatPrecisionException(this.precision);
                    }
                } catch (NumberFormatException e2) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                }
            }
            return this.precision;
        }

        int precision() {
            return this.precision;
        }

        private char conversion(String str) {
            this.f12556c = str.charAt(0);
            if (!this.dt) {
                if (!Conversion.isValid(this.f12556c)) {
                    throw new UnknownFormatConversionException(String.valueOf(this.f12556c));
                }
                if (Character.isUpperCase(this.f12556c)) {
                    this.f12555f.add(Flags.UPPERCASE);
                }
                this.f12556c = Character.toLowerCase(this.f12556c);
                if (Conversion.isText(this.f12556c)) {
                    this.index = -2;
                }
            }
            return this.f12556c;
        }

        private char conversion() {
            return this.f12556c;
        }

        FormatSpecifier(Matcher matcher) {
            this.dt = false;
            int i2 = 1 + 1;
            index(matcher.group(1));
            int i3 = i2 + 1;
            flags(matcher.group(i2));
            int i4 = i3 + 1;
            width(matcher.group(i3));
            int i5 = i4 + 1;
            precision(matcher.group(i4));
            int i6 = i5 + 1;
            String strGroup = matcher.group(i5);
            if (strGroup != null) {
                this.dt = true;
                if (strGroup.equals("T")) {
                    this.f12555f.add(Flags.UPPERCASE);
                }
            }
            conversion(matcher.group(i6));
            if (this.dt) {
                checkDateTime();
                return;
            }
            if (Conversion.isGeneral(this.f12556c)) {
                checkGeneral();
                return;
            }
            if (Conversion.isCharacter(this.f12556c)) {
                checkCharacter();
                return;
            }
            if (Conversion.isInteger(this.f12556c)) {
                checkInteger();
            } else if (Conversion.isFloat(this.f12556c)) {
                checkFloat();
            } else {
                if (Conversion.isText(this.f12556c)) {
                    checkText();
                    return;
                }
                throw new UnknownFormatConversionException(String.valueOf(this.f12556c));
            }
        }

        @Override // java.util.Formatter.FormatString
        public void print(Object obj, Locale locale) throws IOException {
            if (!this.dt) {
                switch (this.f12556c) {
                    case '%':
                        Formatter.this.f12552a.append('%');
                        return;
                    case 'C':
                    case 'c':
                        printCharacter(obj);
                        return;
                    case 'a':
                    case 'e':
                    case 'f':
                    case 'g':
                        printFloat(obj, locale);
                        return;
                    case 'b':
                        printBoolean(obj);
                        return;
                    case 'd':
                    case 'o':
                    case 'x':
                        printInteger(obj, locale);
                        return;
                    case 'h':
                        printHashCode(obj);
                        return;
                    case 'n':
                        Formatter.this.f12552a.append(System.lineSeparator());
                        return;
                    case 's':
                        printString(obj, locale);
                        return;
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        return;
                }
            }
            printDateTime(obj, locale);
        }

        private void printInteger(Object obj, Locale locale) throws IOException {
            if (obj == null) {
                print(FXMLLoader.NULL_KEYWORD);
                return;
            }
            if (obj instanceof Byte) {
                print(((Byte) obj).byteValue(), locale);
                return;
            }
            if (obj instanceof Short) {
                print(((Short) obj).shortValue(), locale);
                return;
            }
            if (obj instanceof Integer) {
                print(((Integer) obj).intValue(), locale);
                return;
            }
            if (obj instanceof Long) {
                print(((Long) obj).longValue(), locale);
            } else if (obj instanceof BigInteger) {
                print((BigInteger) obj, locale);
            } else {
                failConversion(this.f12556c, obj);
            }
        }

        private void printFloat(Object obj, Locale locale) throws IOException {
            if (obj == null) {
                print(FXMLLoader.NULL_KEYWORD);
                return;
            }
            if (obj instanceof Float) {
                print(((Float) obj).floatValue(), locale);
                return;
            }
            if (obj instanceof Double) {
                print(((Double) obj).doubleValue(), locale);
            } else if (obj instanceof BigDecimal) {
                print((BigDecimal) obj, locale);
            } else {
                failConversion(this.f12556c, obj);
            }
        }

        private void printDateTime(Object obj, Locale locale) throws IOException {
            if (obj == null) {
                print(FXMLLoader.NULL_KEYWORD);
                return;
            }
            Calendar calendar = null;
            if (obj instanceof Long) {
                calendar = Calendar.getInstance(locale == null ? Locale.US : locale);
                calendar.setTimeInMillis(((Long) obj).longValue());
            } else if (obj instanceof Date) {
                calendar = Calendar.getInstance(locale == null ? Locale.US : locale);
                calendar.setTime((Date) obj);
            } else if (obj instanceof Calendar) {
                calendar = (Calendar) ((Calendar) obj).clone();
                calendar.setLenient(true);
            } else {
                if (obj instanceof TemporalAccessor) {
                    print((TemporalAccessor) obj, this.f12556c, locale);
                    return;
                }
                failConversion(this.f12556c, obj);
            }
            print(calendar, this.f12556c, locale);
        }

        private void printCharacter(Object obj) throws IOException {
            if (obj == null) {
                print(FXMLLoader.NULL_KEYWORD);
                return;
            }
            String str = null;
            if (obj instanceof Character) {
                str = ((Character) obj).toString();
            } else if (obj instanceof Byte) {
                byte bByteValue = ((Byte) obj).byteValue();
                if (Character.isValidCodePoint(bByteValue)) {
                    str = new String(Character.toChars(bByteValue));
                } else {
                    throw new IllegalFormatCodePointException(bByteValue);
                }
            } else if (obj instanceof Short) {
                short sShortValue = ((Short) obj).shortValue();
                if (Character.isValidCodePoint(sShortValue)) {
                    str = new String(Character.toChars(sShortValue));
                } else {
                    throw new IllegalFormatCodePointException(sShortValue);
                }
            } else if (obj instanceof Integer) {
                int iIntValue = ((Integer) obj).intValue();
                if (Character.isValidCodePoint(iIntValue)) {
                    str = new String(Character.toChars(iIntValue));
                } else {
                    throw new IllegalFormatCodePointException(iIntValue);
                }
            } else {
                failConversion(this.f12556c, obj);
            }
            print(str);
        }

        private void printString(Object obj, Locale locale) throws IOException {
            if (obj instanceof Formattable) {
                Formatter formatter = Formatter.this;
                if (formatter.locale() != locale) {
                    formatter = new Formatter(formatter.out(), locale);
                }
                ((Formattable) obj).formatTo(formatter, this.f12555f.valueOf(), this.width, this.precision);
                return;
            }
            if (this.f12555f.contains(Flags.ALTERNATE)) {
                failMismatch(Flags.ALTERNATE, 's');
            }
            if (obj == null) {
                print(FXMLLoader.NULL_KEYWORD);
            } else {
                print(obj.toString());
            }
        }

        private void printBoolean(Object obj) throws IOException {
            String string;
            String string2;
            if (obj != null) {
                if (obj instanceof Boolean) {
                    string2 = ((Boolean) obj).toString();
                } else {
                    string2 = Boolean.toString(true);
                }
                string = string2;
            } else {
                string = Boolean.toString(false);
            }
            print(string);
        }

        private void printHashCode(Object obj) throws IOException {
            print(obj == null ? FXMLLoader.NULL_KEYWORD : Integer.toHexString(obj.hashCode()));
        }

        private void print(String str) throws IOException {
            if (this.precision != -1 && this.precision < str.length()) {
                str = str.substring(0, this.precision);
            }
            if (this.f12555f.contains(Flags.UPPERCASE)) {
                str = str.toUpperCase();
            }
            Formatter.this.f12552a.append(justify(str));
        }

        private String justify(String str) {
            if (this.width == -1) {
                return str;
            }
            StringBuilder sb = new StringBuilder();
            boolean zContains = this.f12555f.contains(Flags.LEFT_JUSTIFY);
            int length = this.width - str.length();
            if (!zContains) {
                for (int i2 = 0; i2 < length; i2++) {
                    sb.append(' ');
                }
            }
            sb.append(str);
            if (zContains) {
                for (int i3 = 0; i3 < length; i3++) {
                    sb.append(' ');
                }
            }
            return sb.toString();
        }

        @Override // java.util.Formatter.FormatString
        public String toString() {
            StringBuilder sb = new StringBuilder(FXMLLoader.RESOURCE_KEY_PREFIX);
            sb.append(this.f12555f.dup().remove(Flags.UPPERCASE).toString());
            if (this.index > 0) {
                sb.append(this.index).append('$');
            }
            if (this.width != -1) {
                sb.append(this.width);
            }
            if (this.precision != -1) {
                sb.append('.').append(this.precision);
            }
            if (this.dt) {
                sb.append(this.f12555f.contains(Flags.UPPERCASE) ? 'T' : 't');
            }
            sb.append(this.f12555f.contains(Flags.UPPERCASE) ? Character.toUpperCase(this.f12556c) : this.f12556c);
            return sb.toString();
        }

        private void checkGeneral() {
            if ((this.f12556c == 'b' || this.f12556c == 'h') && this.f12555f.contains(Flags.ALTERNATE)) {
                failMismatch(Flags.ALTERNATE, this.f12556c);
            }
            if (this.width == -1 && this.f12555f.contains(Flags.LEFT_JUSTIFY)) {
                throw new MissingFormatWidthException(toString());
            }
            checkBadFlags(Flags.PLUS, Flags.LEADING_SPACE, Flags.ZERO_PAD, Flags.GROUP, Flags.PARENTHESES);
        }

        private void checkDateTime() {
            if (this.precision != -1) {
                throw new IllegalFormatPrecisionException(this.precision);
            }
            if (!DateTime.isValid(this.f12556c)) {
                throw new UnknownFormatConversionException("t" + this.f12556c);
            }
            checkBadFlags(Flags.ALTERNATE, Flags.PLUS, Flags.LEADING_SPACE, Flags.ZERO_PAD, Flags.GROUP, Flags.PARENTHESES);
            if (this.width == -1 && this.f12555f.contains(Flags.LEFT_JUSTIFY)) {
                throw new MissingFormatWidthException(toString());
            }
        }

        private void checkCharacter() {
            if (this.precision != -1) {
                throw new IllegalFormatPrecisionException(this.precision);
            }
            checkBadFlags(Flags.ALTERNATE, Flags.PLUS, Flags.LEADING_SPACE, Flags.ZERO_PAD, Flags.GROUP, Flags.PARENTHESES);
            if (this.width == -1 && this.f12555f.contains(Flags.LEFT_JUSTIFY)) {
                throw new MissingFormatWidthException(toString());
            }
        }

        private void checkInteger() {
            checkNumeric();
            if (this.precision != -1) {
                throw new IllegalFormatPrecisionException(this.precision);
            }
            if (this.f12556c == 'd') {
                checkBadFlags(Flags.ALTERNATE);
            } else if (this.f12556c == 'o') {
                checkBadFlags(Flags.GROUP);
            } else {
                checkBadFlags(Flags.GROUP);
            }
        }

        private void checkBadFlags(Flags... flagsArr) {
            for (int i2 = 0; i2 < flagsArr.length; i2++) {
                if (this.f12555f.contains(flagsArr[i2])) {
                    failMismatch(flagsArr[i2], this.f12556c);
                }
            }
        }

        private void checkFloat() {
            checkNumeric();
            if (this.f12556c != 'f') {
                if (this.f12556c == 'a') {
                    checkBadFlags(Flags.PARENTHESES, Flags.GROUP);
                } else if (this.f12556c == 'e') {
                    checkBadFlags(Flags.GROUP);
                } else if (this.f12556c == 'g') {
                    checkBadFlags(Flags.ALTERNATE);
                }
            }
        }

        private void checkNumeric() {
            if (this.width != -1 && this.width < 0) {
                throw new IllegalFormatWidthException(this.width);
            }
            if (this.precision != -1 && this.precision < 0) {
                throw new IllegalFormatPrecisionException(this.precision);
            }
            if (this.width == -1 && (this.f12555f.contains(Flags.LEFT_JUSTIFY) || this.f12555f.contains(Flags.ZERO_PAD))) {
                throw new MissingFormatWidthException(toString());
            }
            if ((this.f12555f.contains(Flags.PLUS) && this.f12555f.contains(Flags.LEADING_SPACE)) || (this.f12555f.contains(Flags.LEFT_JUSTIFY) && this.f12555f.contains(Flags.ZERO_PAD))) {
                throw new IllegalFormatFlagsException(this.f12555f.toString());
            }
        }

        private void checkText() {
            if (this.precision != -1) {
                throw new IllegalFormatPrecisionException(this.precision);
            }
            switch (this.f12556c) {
                case '%':
                    if (this.f12555f.valueOf() != Flags.LEFT_JUSTIFY.valueOf() && this.f12555f.valueOf() != Flags.NONE.valueOf()) {
                        throw new IllegalFormatFlagsException(this.f12555f.toString());
                    }
                    if (this.width == -1 && this.f12555f.contains(Flags.LEFT_JUSTIFY)) {
                        throw new MissingFormatWidthException(toString());
                    }
                    return;
                case 'n':
                    if (this.width != -1) {
                        throw new IllegalFormatWidthException(this.width);
                    }
                    if (this.f12555f.valueOf() != Flags.NONE.valueOf()) {
                        throw new IllegalFormatFlagsException(this.f12555f.toString());
                    }
                    return;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    return;
            }
        }

        private void print(byte b2, Locale locale) throws IOException {
            long j2 = b2;
            if (b2 < 0 && (this.f12556c == 'o' || this.f12556c == 'x')) {
                j2 += 256;
                if (!$assertionsDisabled && j2 < 0) {
                    throw new AssertionError(j2);
                }
            }
            print(j2, locale);
        }

        private void print(short s2, Locale locale) throws IOException {
            long j2 = s2;
            if (s2 < 0 && (this.f12556c == 'o' || this.f12556c == 'x')) {
                j2 += 65536;
                if (!$assertionsDisabled && j2 < 0) {
                    throw new AssertionError(j2);
                }
            }
            print(j2, locale);
        }

        private void print(int i2, Locale locale) throws IOException {
            long j2 = i2;
            if (i2 < 0 && (this.f12556c == 'o' || this.f12556c == 'x')) {
                j2 += EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH;
                if (!$assertionsDisabled && j2 < 0) {
                    throw new AssertionError(j2);
                }
            }
            print(j2, locale);
        }

        private void print(long j2, Locale locale) throws IOException {
            int length;
            int length2;
            char[] charArray;
            StringBuilder sb = new StringBuilder();
            if (this.f12556c == 'd') {
                boolean z2 = j2 < 0;
                if (j2 < 0) {
                    charArray = Long.toString(j2, 10).substring(1).toCharArray();
                } else {
                    charArray = Long.toString(j2, 10).toCharArray();
                }
                leadingSign(sb, z2);
                localizedMagnitude(sb, charArray, this.f12555f, adjustWidth(this.width, this.f12555f, z2), locale);
                trailingSign(sb, z2);
            } else if (this.f12556c == 'o') {
                checkBadFlags(Flags.PARENTHESES, Flags.LEADING_SPACE, Flags.PLUS);
                String octalString = Long.toOctalString(j2);
                if (this.f12555f.contains(Flags.ALTERNATE)) {
                    length2 = octalString.length() + 1;
                } else {
                    length2 = octalString.length();
                }
                int i2 = length2;
                if (this.f12555f.contains(Flags.ALTERNATE)) {
                    sb.append('0');
                }
                if (this.f12555f.contains(Flags.ZERO_PAD)) {
                    for (int i3 = 0; i3 < this.width - i2; i3++) {
                        sb.append('0');
                    }
                }
                sb.append(octalString);
            } else if (this.f12556c == 'x') {
                checkBadFlags(Flags.PARENTHESES, Flags.LEADING_SPACE, Flags.PLUS);
                String hexString = Long.toHexString(j2);
                if (this.f12555f.contains(Flags.ALTERNATE)) {
                    length = hexString.length() + 2;
                } else {
                    length = hexString.length();
                }
                int i4 = length;
                if (this.f12555f.contains(Flags.ALTERNATE)) {
                    sb.append(this.f12555f.contains(Flags.UPPERCASE) ? "0X" : "0x");
                }
                if (this.f12555f.contains(Flags.ZERO_PAD)) {
                    for (int i5 = 0; i5 < this.width - i4; i5++) {
                        sb.append('0');
                    }
                }
                if (this.f12555f.contains(Flags.UPPERCASE)) {
                    hexString = hexString.toUpperCase();
                }
                sb.append(hexString);
            }
            Formatter.this.f12552a.append(justify(sb.toString()));
        }

        private StringBuilder leadingSign(StringBuilder sb, boolean z2) {
            if (!z2) {
                if (this.f12555f.contains(Flags.PLUS)) {
                    sb.append('+');
                } else if (this.f12555f.contains(Flags.LEADING_SPACE)) {
                    sb.append(' ');
                }
            } else if (this.f12555f.contains(Flags.PARENTHESES)) {
                sb.append('(');
            } else {
                sb.append('-');
            }
            return sb;
        }

        private StringBuilder trailingSign(StringBuilder sb, boolean z2) {
            if (z2 && this.f12555f.contains(Flags.PARENTHESES)) {
                sb.append(')');
            }
            return sb;
        }

        private void print(BigInteger bigInteger, Locale locale) throws IOException {
            StringBuilder sb = new StringBuilder();
            boolean z2 = bigInteger.signum() == -1;
            BigInteger bigIntegerAbs = bigInteger.abs();
            leadingSign(sb, z2);
            if (this.f12556c == 'd') {
                localizedMagnitude(sb, bigIntegerAbs.toString().toCharArray(), this.f12555f, adjustWidth(this.width, this.f12555f, z2), locale);
            } else if (this.f12556c == 'o') {
                String string = bigIntegerAbs.toString(8);
                int length = string.length() + sb.length();
                if (z2 && this.f12555f.contains(Flags.PARENTHESES)) {
                    length++;
                }
                if (this.f12555f.contains(Flags.ALTERNATE)) {
                    length++;
                    sb.append('0');
                }
                if (this.f12555f.contains(Flags.ZERO_PAD)) {
                    for (int i2 = 0; i2 < this.width - length; i2++) {
                        sb.append('0');
                    }
                }
                sb.append(string);
            } else if (this.f12556c == 'x') {
                String string2 = bigIntegerAbs.toString(16);
                int length2 = string2.length() + sb.length();
                if (z2 && this.f12555f.contains(Flags.PARENTHESES)) {
                    length2++;
                }
                if (this.f12555f.contains(Flags.ALTERNATE)) {
                    length2 += 2;
                    sb.append(this.f12555f.contains(Flags.UPPERCASE) ? "0X" : "0x");
                }
                if (this.f12555f.contains(Flags.ZERO_PAD)) {
                    for (int i3 = 0; i3 < this.width - length2; i3++) {
                        sb.append('0');
                    }
                }
                if (this.f12555f.contains(Flags.UPPERCASE)) {
                    string2 = string2.toUpperCase();
                }
                sb.append(string2);
            }
            trailingSign(sb, bigInteger.signum() == -1);
            Formatter.this.f12552a.append(justify(sb.toString()));
        }

        private void print(float f2, Locale locale) throws IOException {
            print(f2, locale);
        }

        private void print(double d2, Locale locale) throws IOException {
            StringBuilder sb = new StringBuilder();
            boolean z2 = Double.compare(d2, 0.0d) == -1;
            if (!Double.isNaN(d2)) {
                double dAbs = Math.abs(d2);
                leadingSign(sb, z2);
                if (!Double.isInfinite(dAbs)) {
                    print(sb, dAbs, locale, this.f12555f, this.f12556c, this.precision, z2);
                } else {
                    sb.append(this.f12555f.contains(Flags.UPPERCASE) ? "INFINITY" : Constants.ATTRVAL_INFINITY);
                }
                trailingSign(sb, z2);
            } else {
                sb.append(this.f12555f.contains(Flags.UPPERCASE) ? "NAN" : "NaN");
            }
            Formatter.this.f12552a.append(justify(sb.toString()));
        }

        private void print(StringBuilder sb, double d2, Locale locale, Flags flags, char c2, int i2, boolean z2) throws IOException {
            char[] exponent;
            char[] mantissa;
            int exponentRounded;
            if (c2 == 'e') {
                int i3 = i2 == -1 ? 6 : i2;
                FormattedFloatingDecimal formattedFloatingDecimalValueOf = FormattedFloatingDecimal.valueOf(d2, i3, FormattedFloatingDecimal.Form.SCIENTIFIC);
                char[] cArrAddZeros = addZeros(formattedFloatingDecimalValueOf.getMantissa(), i3);
                if (flags.contains(Flags.ALTERNATE) && i3 == 0) {
                    cArrAddZeros = addDot(cArrAddZeros);
                }
                char[] exponent2 = d2 == 0.0d ? new char[]{'+', '0', '0'} : formattedFloatingDecimalValueOf.getExponent();
                int iAdjustWidth = this.width;
                if (this.width != -1) {
                    iAdjustWidth = adjustWidth((this.width - exponent2.length) - 1, flags, z2);
                }
                localizedMagnitude(sb, cArrAddZeros, flags, iAdjustWidth, locale);
                sb.append(flags.contains(Flags.UPPERCASE) ? 'E' : 'e');
                Flags flagsRemove = flags.dup().remove(Flags.GROUP);
                char c3 = exponent2[0];
                if (!$assertionsDisabled && c3 != '+' && c3 != '-') {
                    throw new AssertionError();
                }
                sb.append(c3);
                char[] cArr = new char[exponent2.length - 1];
                System.arraycopy(exponent2, 1, cArr, 0, exponent2.length - 1);
                sb.append((CharSequence) localizedMagnitude((StringBuilder) null, cArr, flagsRemove, -1, locale));
                return;
            }
            if (c2 == 'f') {
                int i4 = i2 == -1 ? 6 : i2;
                char[] cArrAddZeros2 = addZeros(FormattedFloatingDecimal.valueOf(d2, i4, FormattedFloatingDecimal.Form.DECIMAL_FLOAT).getMantissa(), i4);
                if (flags.contains(Flags.ALTERNATE) && i4 == 0) {
                    cArrAddZeros2 = addDot(cArrAddZeros2);
                }
                int iAdjustWidth2 = this.width;
                if (this.width != -1) {
                    iAdjustWidth2 = adjustWidth(this.width, flags, z2);
                }
                localizedMagnitude(sb, cArrAddZeros2, flags, iAdjustWidth2, locale);
                return;
            }
            if (c2 != 'g') {
                if (c2 == 'a') {
                    int i5 = i2;
                    if (i2 == -1) {
                        i5 = 0;
                    } else if (i2 == 0) {
                        i5 = 1;
                    }
                    String strHexDouble = hexDouble(d2, i5);
                    boolean zContains = flags.contains(Flags.UPPERCASE);
                    sb.append(zContains ? "0X" : "0x");
                    if (flags.contains(Flags.ZERO_PAD)) {
                        for (int i6 = 0; i6 < (this.width - strHexDouble.length()) - 2; i6++) {
                            sb.append('0');
                        }
                    }
                    int iIndexOf = strHexDouble.indexOf(112);
                    char[] charArray = strHexDouble.substring(0, iIndexOf).toCharArray();
                    if (zContains) {
                        charArray = new String(charArray).toUpperCase(Locale.US).toCharArray();
                    }
                    sb.append(i5 != 0 ? addZeros(charArray, i5) : charArray);
                    sb.append(zContains ? 'P' : 'p');
                    sb.append(strHexDouble.substring(iIndexOf + 1));
                    return;
                }
                return;
            }
            int i7 = i2;
            if (i2 == -1) {
                i7 = 6;
            } else if (i2 == 0) {
                i7 = 1;
            }
            if (d2 == 0.0d) {
                exponent = null;
                mantissa = new char[]{'0'};
                exponentRounded = 0;
            } else {
                FormattedFloatingDecimal formattedFloatingDecimalValueOf2 = FormattedFloatingDecimal.valueOf(d2, i7, FormattedFloatingDecimal.Form.GENERAL);
                exponent = formattedFloatingDecimalValueOf2.getExponent();
                mantissa = formattedFloatingDecimalValueOf2.getMantissa();
                exponentRounded = formattedFloatingDecimalValueOf2.getExponentRounded();
            }
            int i8 = exponent != null ? i7 - 1 : i7 - (exponentRounded + 1);
            char[] cArrAddZeros3 = addZeros(mantissa, i8);
            if (flags.contains(Flags.ALTERNATE) && i8 == 0) {
                cArrAddZeros3 = addDot(cArrAddZeros3);
            }
            int iAdjustWidth3 = this.width;
            if (this.width != -1) {
                if (exponent != null) {
                    iAdjustWidth3 = adjustWidth((this.width - exponent.length) - 1, flags, z2);
                } else {
                    iAdjustWidth3 = adjustWidth(this.width, flags, z2);
                }
            }
            localizedMagnitude(sb, cArrAddZeros3, flags, iAdjustWidth3, locale);
            if (exponent != null) {
                sb.append(flags.contains(Flags.UPPERCASE) ? 'E' : 'e');
                Flags flagsRemove2 = flags.dup().remove(Flags.GROUP);
                char c4 = exponent[0];
                if (!$assertionsDisabled && c4 != '+' && c4 != '-') {
                    throw new AssertionError();
                }
                sb.append(c4);
                char[] cArr2 = new char[exponent.length - 1];
                System.arraycopy(exponent, 1, cArr2, 0, exponent.length - 1);
                sb.append((CharSequence) localizedMagnitude((StringBuilder) null, cArr2, flagsRemove2, -1, locale));
            }
        }

        private char[] addZeros(char[] cArr, int i2) {
            int i3 = 0;
            while (i3 < cArr.length && cArr[i3] != '.') {
                i3++;
            }
            boolean z2 = false;
            if (i3 == cArr.length) {
                z2 = true;
            }
            int length = (cArr.length - i3) - (z2 ? 0 : 1);
            if (!$assertionsDisabled && length > i2) {
                throw new AssertionError();
            }
            if (length == i2) {
                return cArr;
            }
            char[] cArr2 = new char[((cArr.length + i2) - length) + (z2 ? 1 : 0)];
            System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
            int length2 = cArr.length;
            if (z2) {
                cArr2[cArr.length] = '.';
                length2++;
            }
            for (int i4 = length2; i4 < cArr2.length; i4++) {
                cArr2[i4] = '0';
            }
            return cArr2;
        }

        private String hexDouble(double d2, int i2) {
            if (!Double.isFinite(d2) || d2 == 0.0d || i2 == 0 || i2 >= 13) {
                return Double.toHexString(d2).substring(2);
            }
            if (!$assertionsDisabled && (i2 < 1 || i2 > 12)) {
                throw new AssertionError();
            }
            boolean z2 = Math.getExponent(d2) == -1023;
            if (z2) {
                double unused = Formatter.scaleUp = Math.scalb(1.0d, 54);
                d2 *= Formatter.scaleUp;
                int exponent = Math.getExponent(d2);
                if (!$assertionsDisabled && (exponent < -1022 || exponent > 1023)) {
                    throw new AssertionError(exponent);
                }
            }
            int i3 = 53 - (1 + (i2 * 4));
            if (!$assertionsDisabled && (i3 < 1 || i3 >= 53)) {
                throw new AssertionError();
            }
            long jDoubleToLongBits = Double.doubleToLongBits(d2);
            long j2 = (jDoubleToLongBits & Long.MAX_VALUE) >> i3;
            long j3 = jDoubleToLongBits & (((-1) << i3) ^ (-1));
            boolean z3 = (j2 & 1) == 0;
            boolean z4 = ((1 << (i3 - 1)) & j3) != 0;
            boolean z5 = i3 > 1 && (((1 << (i3 - 1)) ^ (-1)) & j3) != 0;
            if ((z3 && z4 && z5) || (!z3 && z4)) {
                j2++;
            }
            double dLongBitsToDouble = Double.longBitsToDouble((jDoubleToLongBits & Long.MIN_VALUE) | (j2 << i3));
            if (Double.isInfinite(dLongBitsToDouble)) {
                return "1.0p1024";
            }
            String strSubstring = Double.toHexString(dLongBitsToDouble).substring(2);
            if (!z2) {
                return strSubstring;
            }
            int iIndexOf = strSubstring.indexOf(112);
            if (iIndexOf == -1) {
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError();
            }
            return strSubstring.substring(0, iIndexOf) + "p" + Integer.toString(Integer.parseInt(strSubstring.substring(iIndexOf + 1)) - 54);
        }

        private void print(BigDecimal bigDecimal, Locale locale) throws IOException {
            if (this.f12556c == 'a') {
                failConversion(this.f12556c, bigDecimal);
            }
            StringBuilder sb = new StringBuilder();
            boolean z2 = bigDecimal.signum() == -1;
            BigDecimal bigDecimalAbs = bigDecimal.abs();
            leadingSign(sb, z2);
            print(sb, bigDecimalAbs, locale, this.f12555f, this.f12556c, this.precision, z2);
            trailingSign(sb, z2);
            Formatter.this.f12552a.append(justify(sb.toString()));
        }

        private void print(StringBuilder sb, BigDecimal bigDecimal, Locale locale, Flags flags, char c2, int i2, boolean z2) throws IOException {
            int i3;
            if (c2 == 'e') {
                int i4 = i2 == -1 ? 6 : i2;
                int iScale = bigDecimal.scale();
                int iPrecision = bigDecimal.precision();
                int i5 = 0;
                if (i4 > iPrecision - 1) {
                    i3 = iPrecision;
                    i5 = i4 - (iPrecision - 1);
                } else {
                    i3 = i4 + 1;
                }
                BigDecimal bigDecimal2 = new BigDecimal(bigDecimal.unscaledValue(), iScale, new MathContext(i3));
                BigDecimalLayout bigDecimalLayout = new BigDecimalLayout(bigDecimal2.unscaledValue(), bigDecimal2.scale(), BigDecimalLayoutForm.SCIENTIFIC);
                char[] cArrMantissa = bigDecimalLayout.mantissa();
                if ((iPrecision == 1 || !bigDecimalLayout.hasDot()) && (i5 > 0 || flags.contains(Flags.ALTERNATE))) {
                    cArrMantissa = addDot(cArrMantissa);
                }
                char[] cArrTrailingZeros = trailingZeros(cArrMantissa, i5);
                char[] cArrExponent = bigDecimalLayout.exponent();
                int iAdjustWidth = this.width;
                if (this.width != -1) {
                    iAdjustWidth = adjustWidth((this.width - cArrExponent.length) - 1, flags, z2);
                }
                localizedMagnitude(sb, cArrTrailingZeros, flags, iAdjustWidth, locale);
                sb.append(flags.contains(Flags.UPPERCASE) ? 'E' : 'e');
                Flags flagsRemove = flags.dup().remove(Flags.GROUP);
                char c3 = cArrExponent[0];
                if (!$assertionsDisabled && c3 != '+' && c3 != '-') {
                    throw new AssertionError();
                }
                sb.append(cArrExponent[0]);
                char[] cArr = new char[cArrExponent.length - 1];
                System.arraycopy(cArrExponent, 1, cArr, 0, cArrExponent.length - 1);
                sb.append((CharSequence) localizedMagnitude((StringBuilder) null, cArr, flagsRemove, -1, locale));
                return;
            }
            if (c2 == 'f') {
                int i6 = i2 == -1 ? 6 : i2;
                int iScale2 = bigDecimal.scale();
                if (iScale2 > i6) {
                    int iPrecision2 = bigDecimal.precision();
                    if (iPrecision2 <= iScale2) {
                        bigDecimal = bigDecimal.setScale(i6, RoundingMode.HALF_UP);
                    } else {
                        bigDecimal = new BigDecimal(bigDecimal.unscaledValue(), iScale2, new MathContext(iPrecision2 - (iScale2 - i6)));
                    }
                }
                BigDecimalLayout bigDecimalLayout2 = new BigDecimalLayout(bigDecimal.unscaledValue(), bigDecimal.scale(), BigDecimalLayoutForm.DECIMAL_FLOAT);
                char[] cArrMantissa2 = bigDecimalLayout2.mantissa();
                int iScale3 = bigDecimalLayout2.scale() < i6 ? i6 - bigDecimalLayout2.scale() : 0;
                if (bigDecimalLayout2.scale() == 0 && (flags.contains(Flags.ALTERNATE) || iScale3 > 0)) {
                    cArrMantissa2 = addDot(bigDecimalLayout2.mantissa());
                }
                localizedMagnitude(sb, trailingZeros(cArrMantissa2, iScale3), flags, adjustWidth(this.width, flags, z2), locale);
                return;
            }
            if (c2 == 'g') {
                int i7 = i2;
                if (i2 == -1) {
                    i7 = 6;
                } else if (i2 == 0) {
                    i7 = 1;
                }
                BigDecimal bigDecimalValueOf = BigDecimal.valueOf(1L, 4);
                BigDecimal bigDecimalValueOf2 = BigDecimal.valueOf(1L, -i7);
                if (bigDecimal.equals(BigDecimal.ZERO) || (bigDecimal.compareTo(bigDecimalValueOf) != -1 && bigDecimal.compareTo(bigDecimalValueOf2) == -1)) {
                    print(sb, bigDecimal, locale, flags, 'f', (i7 - ((-bigDecimal.scale()) + (bigDecimal.unscaledValue().toString().length() - 1))) - 1, z2);
                    return;
                } else {
                    print(sb, bigDecimal, locale, flags, 'e', i7 - 1, z2);
                    return;
                }
            }
            if (c2 == 'a' && !$assertionsDisabled) {
                throw new AssertionError();
            }
        }

        /* loaded from: rt.jar:java/util/Formatter$FormatSpecifier$BigDecimalLayout.class */
        private class BigDecimalLayout {
            private StringBuilder mant;
            private StringBuilder exp;
            private boolean dot = false;
            private int scale;

            public BigDecimalLayout(BigInteger bigInteger, int i2, BigDecimalLayoutForm bigDecimalLayoutForm) {
                layout(bigInteger, i2, bigDecimalLayoutForm);
            }

            public boolean hasDot() {
                return this.dot;
            }

            public int scale() {
                return this.scale;
            }

            public char[] layoutChars() {
                StringBuilder sb = new StringBuilder(this.mant);
                if (this.exp != null) {
                    sb.append('E');
                    sb.append((CharSequence) this.exp);
                }
                return toCharArray(sb);
            }

            public char[] mantissa() {
                return toCharArray(this.mant);
            }

            public char[] exponent() {
                return toCharArray(this.exp);
            }

            private char[] toCharArray(StringBuilder sb) {
                if (sb == null) {
                    return null;
                }
                char[] cArr = new char[sb.length()];
                sb.getChars(0, cArr.length, cArr, 0);
                return cArr;
            }

            private void layout(BigInteger bigInteger, int i2, BigDecimalLayoutForm bigDecimalLayoutForm) {
                char[] charArray = bigInteger.toString().toCharArray();
                this.scale = i2;
                this.mant = new StringBuilder(charArray.length + 14);
                if (i2 == 0) {
                    int length = charArray.length;
                    if (length > 1) {
                        this.mant.append(charArray[0]);
                        if (bigDecimalLayoutForm == BigDecimalLayoutForm.SCIENTIFIC) {
                            this.mant.append('.');
                            this.dot = true;
                            this.mant.append(charArray, 1, length - 1);
                            this.exp = new StringBuilder(Marker.ANY_NON_NULL_MARKER);
                            if (length < 10) {
                                this.exp.append("0").append(length - 1);
                                return;
                            } else {
                                this.exp.append(length - 1);
                                return;
                            }
                        }
                        this.mant.append(charArray, 1, length - 1);
                        return;
                    }
                    this.mant.append(charArray);
                    if (bigDecimalLayoutForm == BigDecimalLayoutForm.SCIENTIFIC) {
                        this.exp = new StringBuilder("+00");
                        return;
                    }
                    return;
                }
                long length2 = (-i2) + (charArray.length - 1);
                if (bigDecimalLayoutForm == BigDecimalLayoutForm.DECIMAL_FLOAT) {
                    int length3 = i2 - charArray.length;
                    if (length3 >= 0) {
                        this.mant.append("0.");
                        this.dot = true;
                        while (length3 > 0) {
                            this.mant.append('0');
                            length3--;
                        }
                        this.mant.append(charArray);
                        return;
                    }
                    if ((-length3) < charArray.length) {
                        this.mant.append(charArray, 0, -length3);
                        this.mant.append('.');
                        this.dot = true;
                        this.mant.append(charArray, -length3, i2);
                        return;
                    }
                    this.mant.append(charArray, 0, charArray.length);
                    for (int i3 = 0; i3 < (-i2); i3++) {
                        this.mant.append('0');
                    }
                    this.scale = 0;
                    return;
                }
                this.mant.append(charArray[0]);
                if (charArray.length > 1) {
                    this.mant.append('.');
                    this.dot = true;
                    this.mant.append(charArray, 1, charArray.length - 1);
                }
                this.exp = new StringBuilder();
                if (length2 != 0) {
                    long jAbs = Math.abs(length2);
                    this.exp.append(length2 < 0 ? '-' : '+');
                    if (jAbs < 10) {
                        this.exp.append('0');
                    }
                    this.exp.append(jAbs);
                    return;
                }
                this.exp.append("+00");
            }
        }

        private int adjustWidth(int i2, Flags flags, boolean z2) {
            int i3 = i2;
            if (i3 != -1 && z2 && flags.contains(Flags.PARENTHESES)) {
                i3--;
            }
            return i3;
        }

        private char[] addDot(char[] cArr) {
            char[] cArr2 = new char[cArr.length + 1];
            System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
            cArr2[cArr2.length - 1] = '.';
            return cArr2;
        }

        private char[] trailingZeros(char[] cArr, int i2) {
            char[] cArr2 = cArr;
            if (i2 > 0) {
                cArr2 = new char[cArr.length + i2];
                System.arraycopy(cArr, 0, cArr2, 0, cArr.length);
                for (int length = cArr.length; length < cArr2.length; length++) {
                    cArr2[length] = '0';
                }
            }
            return cArr2;
        }

        private void print(Calendar calendar, char c2, Locale locale) throws IOException {
            StringBuilder sb = new StringBuilder();
            print(sb, calendar, c2, locale);
            String strJustify = justify(sb.toString());
            if (this.f12555f.contains(Flags.UPPERCASE)) {
                strJustify = strJustify.toUpperCase();
            }
            Formatter.this.f12552a.append(strJustify);
        }

        private Appendable print(StringBuilder sb, Calendar calendar, char c2, Locale locale) throws IOException {
            if (sb == null) {
                sb = new StringBuilder();
            }
            switch (c2) {
                case 'A':
                case 'a':
                    int i2 = calendar.get(7);
                    DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale == null ? Locale.US : locale);
                    if (c2 == 'A') {
                        sb.append(dateFormatSymbols.getWeekdays()[i2]);
                        break;
                    } else {
                        sb.append(dateFormatSymbols.getShortWeekdays()[i2]);
                        break;
                    }
                case 'B':
                case 'b':
                case 'h':
                    int i3 = calendar.get(2);
                    DateFormatSymbols dateFormatSymbols2 = DateFormatSymbols.getInstance(locale == null ? Locale.US : locale);
                    if (c2 == 'B') {
                        sb.append(dateFormatSymbols2.getMonths()[i3]);
                        break;
                    } else {
                        sb.append(dateFormatSymbols2.getShortMonths()[i3]);
                        break;
                    }
                case 'C':
                case 'Y':
                case 'y':
                    int i4 = calendar.get(1);
                    int i5 = 2;
                    switch (c2) {
                        case 'C':
                            i4 /= 100;
                            break;
                        case 'Y':
                            i5 = 4;
                            break;
                        case 'y':
                            i4 %= 100;
                            break;
                    }
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, i4, Flags.ZERO_PAD, i5, locale));
                    break;
                case 'D':
                    print(sb, calendar, 'm', locale).append('/');
                    print(sb, calendar, 'd', locale).append('/');
                    print(sb, calendar, 'y', locale);
                    break;
                case 'E':
                case 'G':
                case 'J':
                case 'K':
                case 'O':
                case 'P':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case '[':
                case '\\':
                case ']':
                case '^':
                case '_':
                case '`':
                case 'f':
                case 'g':
                case 'i':
                case 'n':
                case 'o':
                case 'q':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
                case 'F':
                    print(sb, calendar, 'Y', locale).append('-');
                    print(sb, calendar, 'm', locale).append('-');
                    print(sb, calendar, 'd', locale);
                    break;
                case 'H':
                case 'I':
                case 'k':
                case 'l':
                    int i6 = calendar.get(11);
                    if (c2 == 'I' || c2 == 'l') {
                        i6 = (i6 == 0 || i6 == 12) ? 12 : i6 % 12;
                    }
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, i6, (c2 == 'H' || c2 == 'I') ? Flags.ZERO_PAD : Flags.NONE, 2, locale));
                    break;
                case 'L':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.get(14), Flags.ZERO_PAD, 3, locale));
                    break;
                case 'M':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.get(12), Flags.ZERO_PAD, 2, locale));
                    break;
                case 'N':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.get(14) * 1000000, Flags.ZERO_PAD, 9, locale));
                    break;
                case 'Q':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.getTimeInMillis(), Flags.NONE, this.width, locale));
                    break;
                case 'R':
                case 'T':
                    print(sb, calendar, 'H', locale).append(':');
                    print(sb, calendar, 'M', locale);
                    if (c2 == 'T') {
                        sb.append(':');
                        print(sb, calendar, 'S', locale);
                        break;
                    }
                    break;
                case 'S':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.get(13), Flags.ZERO_PAD, 2, locale));
                    break;
                case 'Z':
                    sb.append(calendar.getTimeZone().getDisplayName(calendar.get(16) != 0, 0, locale == null ? Locale.US : locale));
                    break;
                case 'c':
                    print(sb, calendar, 'a', locale).append(' ');
                    print(sb, calendar, 'b', locale).append(' ');
                    print(sb, calendar, 'd', locale).append(' ');
                    print(sb, calendar, 'T', locale).append(' ');
                    print(sb, calendar, 'Z', locale).append(' ');
                    print(sb, calendar, 'Y', locale);
                    break;
                case 'd':
                case 'e':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.get(5), c2 == 'd' ? Flags.ZERO_PAD : Flags.NONE, 2, locale));
                    break;
                case 'j':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.get(6), Flags.ZERO_PAD, 3, locale));
                    break;
                case 'm':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.get(2) + 1, Flags.ZERO_PAD, 2, locale));
                    break;
                case 'p':
                    String[] amPmStrings = {"AM", "PM"};
                    if (locale != null && locale != Locale.US) {
                        amPmStrings = DateFormatSymbols.getInstance(locale).getAmPmStrings();
                    }
                    sb.append(amPmStrings[calendar.get(9)].toLowerCase(locale != null ? locale : Locale.US));
                    break;
                case 'r':
                    print(sb, calendar, 'I', locale).append(':');
                    print(sb, calendar, 'M', locale).append(':');
                    print(sb, calendar, 'S', locale).append(' ');
                    StringBuilder sb2 = new StringBuilder();
                    print(sb2, calendar, 'p', locale);
                    sb.append(sb2.toString().toUpperCase(locale != null ? locale : Locale.US));
                    break;
                case 's':
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, calendar.getTimeInMillis() / 1000, Flags.NONE, this.width, locale));
                    break;
                case 'z':
                    int i7 = calendar.get(15) + calendar.get(16);
                    boolean z2 = i7 < 0;
                    sb.append(z2 ? '-' : '+');
                    if (z2) {
                        i7 = -i7;
                    }
                    int i8 = i7 / 60000;
                    sb.append((CharSequence) localizedMagnitude((StringBuilder) null, ((i8 / 60) * 100) + (i8 % 60), Flags.ZERO_PAD, 4, locale));
                    break;
            }
            return sb;
        }

        private void print(TemporalAccessor temporalAccessor, char c2, Locale locale) throws IOException {
            StringBuilder sb = new StringBuilder();
            print(sb, temporalAccessor, c2, locale);
            String strJustify = justify(sb.toString());
            if (this.f12555f.contains(Flags.UPPERCASE)) {
                strJustify = strJustify.toUpperCase();
            }
            Formatter.this.f12552a.append(strJustify);
        }

        private Appendable print(StringBuilder sb, TemporalAccessor temporalAccessor, char c2, Locale locale) throws IOException {
            if (sb == null) {
                sb = new StringBuilder();
            }
            try {
                switch (c2) {
                    case 'A':
                    case 'a':
                        int i2 = (temporalAccessor.get(ChronoField.DAY_OF_WEEK) % 7) + 1;
                        DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale == null ? Locale.US : locale);
                        if (c2 == 'A') {
                            sb.append(dateFormatSymbols.getWeekdays()[i2]);
                            break;
                        } else {
                            sb.append(dateFormatSymbols.getShortWeekdays()[i2]);
                            break;
                        }
                    case 'B':
                    case 'b':
                    case 'h':
                        int i3 = temporalAccessor.get(ChronoField.MONTH_OF_YEAR) - 1;
                        DateFormatSymbols dateFormatSymbols2 = DateFormatSymbols.getInstance(locale == null ? Locale.US : locale);
                        if (c2 == 'B') {
                            sb.append(dateFormatSymbols2.getMonths()[i3]);
                            break;
                        } else {
                            sb.append(dateFormatSymbols2.getShortMonths()[i3]);
                            break;
                        }
                    case 'C':
                    case 'Y':
                    case 'y':
                        int i4 = temporalAccessor.get(ChronoField.YEAR_OF_ERA);
                        int i5 = 2;
                        switch (c2) {
                            case 'C':
                                i4 /= 100;
                                break;
                            case 'Y':
                                i5 = 4;
                                break;
                            case 'y':
                                i4 %= 100;
                                break;
                        }
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, i4, Flags.ZERO_PAD, i5, locale));
                        break;
                    case 'D':
                        print(sb, temporalAccessor, 'm', locale).append('/');
                        print(sb, temporalAccessor, 'd', locale).append('/');
                        print(sb, temporalAccessor, 'y', locale);
                        break;
                    case 'E':
                    case 'G':
                    case 'J':
                    case 'K':
                    case 'O':
                    case 'P':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case '[':
                    case '\\':
                    case ']':
                    case '^':
                    case '_':
                    case '`':
                    case 'f':
                    case 'g':
                    case 'i':
                    case 'n':
                    case 'o':
                    case 'q':
                    case 't':
                    case 'u':
                    case 'v':
                    case 'w':
                    case 'x':
                    default:
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        break;
                    case 'F':
                        print(sb, temporalAccessor, 'Y', locale).append('-');
                        print(sb, temporalAccessor, 'm', locale).append('-');
                        print(sb, temporalAccessor, 'd', locale);
                        break;
                    case 'H':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.HOUR_OF_DAY), Flags.ZERO_PAD, 2, locale));
                        break;
                    case 'I':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.CLOCK_HOUR_OF_AMPM), Flags.ZERO_PAD, 2, locale));
                        break;
                    case 'L':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.MILLI_OF_SECOND), Flags.ZERO_PAD, 3, locale));
                        break;
                    case 'M':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.MINUTE_OF_HOUR), Flags.ZERO_PAD, 2, locale));
                        break;
                    case 'N':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.MILLI_OF_SECOND) * 1000000, Flags.ZERO_PAD, 9, locale));
                        break;
                    case 'Q':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, (temporalAccessor.getLong(ChronoField.INSTANT_SECONDS) * 1000) + temporalAccessor.getLong(ChronoField.MILLI_OF_SECOND), Flags.NONE, this.width, locale));
                        break;
                    case 'R':
                    case 'T':
                        print(sb, temporalAccessor, 'H', locale).append(':');
                        print(sb, temporalAccessor, 'M', locale);
                        if (c2 == 'T') {
                            sb.append(':');
                            print(sb, temporalAccessor, 'S', locale);
                            break;
                        }
                        break;
                    case 'S':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.SECOND_OF_MINUTE), Flags.ZERO_PAD, 2, locale));
                        break;
                    case 'Z':
                        ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.zone());
                        if (zoneId == null) {
                            throw new IllegalFormatConversionException(c2, temporalAccessor.getClass());
                        }
                        if (!(zoneId instanceof ZoneOffset) && temporalAccessor.isSupported(ChronoField.INSTANT_SECONDS)) {
                            sb.append(TimeZone.getTimeZone(zoneId.getId()).getDisplayName(zoneId.getRules().isDaylightSavings(Instant.from(temporalAccessor)), 0, locale == null ? Locale.US : locale));
                            break;
                        } else {
                            sb.append(zoneId.getId());
                            break;
                        }
                        break;
                    case 'c':
                        print(sb, temporalAccessor, 'a', locale).append(' ');
                        print(sb, temporalAccessor, 'b', locale).append(' ');
                        print(sb, temporalAccessor, 'd', locale).append(' ');
                        print(sb, temporalAccessor, 'T', locale).append(' ');
                        print(sb, temporalAccessor, 'Z', locale).append(' ');
                        print(sb, temporalAccessor, 'Y', locale);
                        break;
                    case 'd':
                    case 'e':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.DAY_OF_MONTH), c2 == 'd' ? Flags.ZERO_PAD : Flags.NONE, 2, locale));
                        break;
                    case 'j':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.DAY_OF_YEAR), Flags.ZERO_PAD, 3, locale));
                        break;
                    case 'k':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.HOUR_OF_DAY), Flags.NONE, 2, locale));
                        break;
                    case 'l':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.CLOCK_HOUR_OF_AMPM), Flags.NONE, 2, locale));
                        break;
                    case 'm':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.get(ChronoField.MONTH_OF_YEAR), Flags.ZERO_PAD, 2, locale));
                        break;
                    case 'p':
                        String[] amPmStrings = {"AM", "PM"};
                        if (locale != null && locale != Locale.US) {
                            amPmStrings = DateFormatSymbols.getInstance(locale).getAmPmStrings();
                        }
                        sb.append(amPmStrings[temporalAccessor.get(ChronoField.AMPM_OF_DAY)].toLowerCase(locale != null ? locale : Locale.US));
                        break;
                    case 'r':
                        print(sb, temporalAccessor, 'I', locale).append(':');
                        print(sb, temporalAccessor, 'M', locale).append(':');
                        print(sb, temporalAccessor, 'S', locale).append(' ');
                        StringBuilder sb2 = new StringBuilder();
                        print(sb2, temporalAccessor, 'p', locale);
                        sb.append(sb2.toString().toUpperCase(locale != null ? locale : Locale.US));
                        break;
                    case 's':
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, temporalAccessor.getLong(ChronoField.INSTANT_SECONDS), Flags.NONE, this.width, locale));
                        break;
                    case 'z':
                        int i6 = temporalAccessor.get(ChronoField.OFFSET_SECONDS);
                        boolean z2 = i6 < 0;
                        sb.append(z2 ? '-' : '+');
                        if (z2) {
                            i6 = -i6;
                        }
                        int i7 = i6 / 60;
                        sb.append((CharSequence) localizedMagnitude((StringBuilder) null, ((i7 / 60) * 100) + (i7 % 60), Flags.ZERO_PAD, 4, locale));
                        break;
                }
                return sb;
            } catch (DateTimeException e2) {
                throw new IllegalFormatConversionException(c2, temporalAccessor.getClass());
            }
        }

        private void failMismatch(Flags flags, char c2) {
            throw new FormatFlagsConversionMismatchException(flags.toString(), c2);
        }

        private void failConversion(char c2, Object obj) {
            throw new IllegalFormatConversionException(c2, obj.getClass());
        }

        private char getZero(Locale locale) {
            if (locale == null || locale.equals(Formatter.this.locale())) {
                return Formatter.this.zero;
            }
            return DecimalFormatSymbols.getInstance(locale).getZeroDigit();
        }

        private StringBuilder localizedMagnitude(StringBuilder sb, long j2, Flags flags, int i2, Locale locale) {
            return localizedMagnitude(sb, Long.toString(j2, 10).toCharArray(), flags, i2, locale);
        }

        private StringBuilder localizedMagnitude(StringBuilder sb, char[] cArr, Flags flags, int i2, Locale locale) {
            if (sb == null) {
                sb = new StringBuilder();
            }
            int length = sb.length();
            char zero = getZero(locale);
            char groupingSeparator = 0;
            int groupingSize = -1;
            char decimalSeparator = 0;
            int length2 = cArr.length;
            int i3 = length2;
            int i4 = 0;
            while (true) {
                if (i4 >= length2) {
                    break;
                }
                if (cArr[i4] != '.') {
                    i4++;
                } else {
                    i3 = i4;
                    break;
                }
            }
            if (i3 < length2) {
                if (locale == null || locale.equals(Locale.US)) {
                    decimalSeparator = '.';
                } else {
                    decimalSeparator = DecimalFormatSymbols.getInstance(locale).getDecimalSeparator();
                }
            }
            if (flags.contains(Flags.GROUP)) {
                if (locale == null || locale.equals(Locale.US)) {
                    groupingSeparator = ',';
                    groupingSize = 3;
                } else {
                    groupingSeparator = DecimalFormatSymbols.getInstance(locale).getGroupingSeparator();
                    groupingSize = ((DecimalFormat) NumberFormat.getIntegerInstance(locale)).getGroupingSize();
                }
            }
            for (int i5 = 0; i5 < length2; i5++) {
                if (i5 == i3) {
                    sb.append(decimalSeparator);
                    groupingSeparator = 0;
                } else {
                    sb.append((char) ((cArr[i5] - '0') + zero));
                    if (groupingSeparator != 0 && i5 != i3 - 1 && (i3 - i5) % groupingSize == 1) {
                        sb.append(groupingSeparator);
                    }
                }
            }
            int length3 = sb.length();
            if (i2 != -1 && flags.contains(Flags.ZERO_PAD)) {
                for (int i6 = 0; i6 < i2 - length3; i6++) {
                    sb.insert(length, zero);
                }
            }
            return sb;
        }
    }

    /* loaded from: rt.jar:java/util/Formatter$Flags.class */
    private static class Flags {
        private int flags;
        static final Flags NONE = new Flags(0);
        static final Flags LEFT_JUSTIFY = new Flags(1);
        static final Flags UPPERCASE = new Flags(2);
        static final Flags ALTERNATE = new Flags(4);
        static final Flags PLUS = new Flags(8);
        static final Flags LEADING_SPACE = new Flags(16);
        static final Flags ZERO_PAD = new Flags(32);
        static final Flags GROUP = new Flags(64);
        static final Flags PARENTHESES = new Flags(128);
        static final Flags PREVIOUS = new Flags(256);

        private Flags(int i2) {
            this.flags = i2;
        }

        public int valueOf() {
            return this.flags;
        }

        public boolean contains(Flags flags) {
            return (this.flags & flags.valueOf()) == flags.valueOf();
        }

        public Flags dup() {
            return new Flags(this.flags);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Flags add(Flags flags) {
            this.flags |= flags.valueOf();
            return this;
        }

        public Flags remove(Flags flags) {
            this.flags &= flags.valueOf() ^ (-1);
            return this;
        }

        public static Flags parse(String str) {
            char[] charArray = str.toCharArray();
            Flags flags = new Flags(0);
            for (char c2 : charArray) {
                Flags flags2 = parse(c2);
                if (flags.contains(flags2)) {
                    throw new DuplicateFormatFlagsException(flags2.toString());
                }
                flags.add(flags2);
            }
            return flags;
        }

        private static Flags parse(char c2) {
            switch (c2) {
                case ' ':
                    return LEADING_SPACE;
                case '!':
                case '\"':
                case '$':
                case '%':
                case '&':
                case '\'':
                case ')':
                case '*':
                case '.':
                case '/':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case ':':
                case ';':
                default:
                    throw new UnknownFormatFlagsException(String.valueOf(c2));
                case '#':
                    return ALTERNATE;
                case '(':
                    return PARENTHESES;
                case '+':
                    return PLUS;
                case ',':
                    return GROUP;
                case '-':
                    return LEFT_JUSTIFY;
                case '0':
                    return ZERO_PAD;
                case '<':
                    return PREVIOUS;
            }
        }

        public static String toString(Flags flags) {
            return flags.toString();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (contains(LEFT_JUSTIFY)) {
                sb.append('-');
            }
            if (contains(UPPERCASE)) {
                sb.append('^');
            }
            if (contains(ALTERNATE)) {
                sb.append('#');
            }
            if (contains(PLUS)) {
                sb.append('+');
            }
            if (contains(LEADING_SPACE)) {
                sb.append(' ');
            }
            if (contains(ZERO_PAD)) {
                sb.append('0');
            }
            if (contains(GROUP)) {
                sb.append(',');
            }
            if (contains(PARENTHESES)) {
                sb.append('(');
            }
            if (contains(PREVIOUS)) {
                sb.append('<');
            }
            return sb.toString();
        }
    }

    /* loaded from: rt.jar:java/util/Formatter$Conversion.class */
    private static class Conversion {
        static final char DECIMAL_INTEGER = 'd';
        static final char OCTAL_INTEGER = 'o';
        static final char HEXADECIMAL_INTEGER = 'x';
        static final char HEXADECIMAL_INTEGER_UPPER = 'X';
        static final char SCIENTIFIC = 'e';
        static final char SCIENTIFIC_UPPER = 'E';
        static final char GENERAL = 'g';
        static final char GENERAL_UPPER = 'G';
        static final char DECIMAL_FLOAT = 'f';
        static final char HEXADECIMAL_FLOAT = 'a';
        static final char HEXADECIMAL_FLOAT_UPPER = 'A';
        static final char CHARACTER = 'c';
        static final char CHARACTER_UPPER = 'C';
        static final char DATE_TIME = 't';
        static final char DATE_TIME_UPPER = 'T';
        static final char BOOLEAN = 'b';
        static final char BOOLEAN_UPPER = 'B';
        static final char STRING = 's';
        static final char STRING_UPPER = 'S';
        static final char HASHCODE = 'h';
        static final char HASHCODE_UPPER = 'H';
        static final char LINE_SEPARATOR = 'n';
        static final char PERCENT_SIGN = '%';

        private Conversion() {
        }

        static boolean isValid(char c2) {
            return isGeneral(c2) || isInteger(c2) || isFloat(c2) || isText(c2) || c2 == 't' || isCharacter(c2);
        }

        static boolean isGeneral(char c2) {
            switch (c2) {
                case 'B':
                case 'H':
                case 'S':
                case 'b':
                case 'h':
                case 's':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isCharacter(char c2) {
            switch (c2) {
                case 'C':
                case 'c':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isInteger(char c2) {
            switch (c2) {
                case 'X':
                case 'd':
                case 'o':
                case 'x':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isFloat(char c2) {
            switch (c2) {
                case 'A':
                case 'E':
                case 'G':
                case 'a':
                case 'e':
                case 'f':
                case 'g':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isText(char c2) {
            switch (c2) {
                case '%':
                case 'n':
                    return true;
                default:
                    return false;
            }
        }
    }

    /* loaded from: rt.jar:java/util/Formatter$DateTime.class */
    private static class DateTime {
        static final char HOUR_OF_DAY_0 = 'H';
        static final char HOUR_0 = 'I';
        static final char HOUR_OF_DAY = 'k';
        static final char HOUR = 'l';
        static final char MINUTE = 'M';
        static final char NANOSECOND = 'N';
        static final char MILLISECOND = 'L';
        static final char MILLISECOND_SINCE_EPOCH = 'Q';
        static final char AM_PM = 'p';
        static final char SECONDS_SINCE_EPOCH = 's';
        static final char SECOND = 'S';
        static final char TIME = 'T';
        static final char ZONE_NUMERIC = 'z';
        static final char ZONE = 'Z';
        static final char NAME_OF_DAY_ABBREV = 'a';
        static final char NAME_OF_DAY = 'A';
        static final char NAME_OF_MONTH_ABBREV = 'b';
        static final char NAME_OF_MONTH = 'B';
        static final char CENTURY = 'C';
        static final char DAY_OF_MONTH_0 = 'd';
        static final char DAY_OF_MONTH = 'e';
        static final char NAME_OF_MONTH_ABBREV_X = 'h';
        static final char DAY_OF_YEAR = 'j';
        static final char MONTH = 'm';
        static final char YEAR_2 = 'y';
        static final char YEAR_4 = 'Y';
        static final char TIME_12_HOUR = 'r';
        static final char TIME_24_HOUR = 'R';
        static final char DATE_TIME = 'c';
        static final char DATE = 'D';
        static final char ISO_STANDARD_DATE = 'F';

        private DateTime() {
        }

        static boolean isValid(char c2) {
            switch (c2) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'H':
                case 'I':
                case 'L':
                case 'M':
                case 'N':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'h':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'p':
                case 'r':
                case 's':
                case 'y':
                case 'z':
                    return true;
                case 'E':
                case 'G':
                case 'J':
                case 'K':
                case 'O':
                case 'P':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case '[':
                case '\\':
                case ']':
                case '^':
                case '_':
                case '`':
                case 'f':
                case 'g':
                case 'i':
                case 'n':
                case 'o':
                case 'q':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                default:
                    return false;
            }
        }
    }
}
