package jdk.nashorn.internal.parser;

import java.util.HashMap;
import java.util.Locale;

/* loaded from: nashorn.jar:jdk/nashorn/internal/parser/DateParser.class */
public class DateParser {
    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int HOUR = 3;
    public static final int MINUTE = 4;
    public static final int SECOND = 5;
    public static final int MILLISECOND = 6;
    public static final int TIMEZONE = 7;
    private final String string;
    private final int length;
    private Token token;
    private int tokenLength;
    private Name nameValue;
    private int numValue;
    private static final HashMap<String, Name> names = new HashMap<>();
    private int pos = 0;
    private int currentField = 0;
    private int yearSign = 0;
    private boolean namedMonth = false;
    private final Integer[] fields = new Integer[8];

    /* loaded from: nashorn.jar:jdk/nashorn/internal/parser/DateParser$Token.class */
    private enum Token {
        UNKNOWN,
        NUMBER,
        SEPARATOR,
        PARENTHESIS,
        NAME,
        SIGN,
        END
    }

    static {
        addName("monday", -1, 0);
        addName("tuesday", -1, 0);
        addName("wednesday", -1, 0);
        addName("thursday", -1, 0);
        addName("friday", -1, 0);
        addName("saturday", -1, 0);
        addName("sunday", -1, 0);
        addName("january", 0, 1);
        addName("february", 0, 2);
        addName("march", 0, 3);
        addName("april", 0, 4);
        addName("may", 0, 5);
        addName("june", 0, 6);
        addName("july", 0, 7);
        addName("august", 0, 8);
        addName("september", 0, 9);
        addName("october", 0, 10);
        addName("november", 0, 11);
        addName("december", 0, 12);
        addName("am", 1, 0);
        addName("pm", 1, 12);
        addName("z", 2, 0);
        addName("gmt", 2, 0);
        addName("ut", 2, 0);
        addName("utc", 2, 0);
        addName("est", 2, -300);
        addName("edt", 2, -240);
        addName("cst", 2, -360);
        addName("cdt", 2, -300);
        addName("mst", 2, -420);
        addName("mdt", 2, -360);
        addName("pst", 2, -480);
        addName("pdt", 2, -420);
        addName("t", 3, 0);
    }

    public DateParser(String string) {
        this.string = string;
        this.length = string.length();
    }

    public boolean parse() {
        return parseEcmaDate() || parseLegacyDate();
    }

    public boolean parseEcmaDate() {
        if (this.token == null) {
            this.token = next();
        }
        while (this.token != Token.END) {
            switch (this.token) {
                case NUMBER:
                    if (this.currentField == 0 && this.yearSign != 0) {
                        if (this.tokenLength != 6) {
                            return false;
                        }
                        this.numValue *= this.yearSign;
                    } else if (!checkEcmaField(this.currentField, this.numValue)) {
                        return false;
                    }
                    if (!skipEcmaDelimiter()) {
                        return false;
                    }
                    if (this.currentField >= 7) {
                        break;
                    } else {
                        int i2 = this.currentField;
                        this.currentField = i2 + 1;
                        set(i2, this.numValue);
                        break;
                    }
                    break;
                case NAME:
                    if (this.nameValue == null) {
                        return false;
                    }
                    switch (this.nameValue.type) {
                        case 2:
                            if (!this.nameValue.key.equals("z") || !setTimezone(this.nameValue.value, false)) {
                                return false;
                            }
                            break;
                        case 3:
                            if (this.currentField == 0 || this.currentField > 3) {
                                return false;
                            }
                            this.currentField = 3;
                            break;
                            break;
                        default:
                            return false;
                    }
                case SIGN:
                    if (peek() == -1) {
                        return false;
                    }
                    if (this.currentField == 0) {
                        this.yearSign = this.numValue;
                        break;
                    } else if (this.currentField >= 5 && setTimezone(readTimeZoneOffset(), true)) {
                        break;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
            this.token = next();
        }
        return patchResult(true);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:61:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x015a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean parseLegacyDate() {
        /*
            Method dump skipped, instructions count: 363
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.parser.DateParser.parseLegacyDate():boolean");
    }

    public Integer[] getDateFields() {
        return this.fields;
    }

    private boolean isSet(int field) {
        return this.fields[field] != null;
    }

    private Integer get(int field) {
        return this.fields[field];
    }

    private void set(int field, int value) {
        this.fields[field] = Integer.valueOf(value);
    }

    private int peek() {
        if (this.pos < this.length) {
            return this.string.charAt(this.pos);
        }
        return -1;
    }

    private boolean skipNumberDelimiter(char c2) {
        if (this.pos < this.length - 1 && this.string.charAt(this.pos) == c2 && Character.getType(this.string.charAt(this.pos + 1)) == 9) {
            this.token = null;
            this.pos++;
            return true;
        }
        return false;
    }

    private boolean skipDelimiter(char c2) {
        if (this.pos < this.length && this.string.charAt(this.pos) == c2) {
            this.token = null;
            this.pos++;
            return true;
        }
        return false;
    }

    private Token next() {
        if (this.pos >= this.length) {
            this.tokenLength = 0;
            return Token.END;
        }
        char c2 = this.string.charAt(this.pos);
        if (c2 > 128) {
            this.tokenLength = 1;
            this.pos++;
            return Token.UNKNOWN;
        }
        int type = Character.getType(c2);
        switch (type) {
            case 1:
            case 2:
                this.nameValue = readName();
                return Token.NAME;
            case 9:
                this.numValue = readNumber(6);
                return Token.NUMBER;
            case 12:
            case 24:
                this.tokenLength = 1;
                this.pos++;
                return Token.SEPARATOR;
            default:
                this.tokenLength = 1;
                this.pos++;
                switch (c2) {
                    case '(':
                        return Token.PARENTHESIS;
                    case '+':
                    case '-':
                        this.numValue = c2 == '-' ? -1 : 1;
                        return Token.SIGN;
                    default:
                        return Token.UNKNOWN;
                }
        }
    }

    private static boolean checkLegacyField(int field, int value) {
        switch (field) {
            case 3:
                return isHour(value);
            case 4:
            case 5:
                return isMinuteOrSecond(value);
            case 6:
                return isMillisecond(value);
            default:
                return true;
        }
    }

    private boolean checkEcmaField(int field, int value) {
        switch (field) {
            case 0:
                return this.tokenLength == 4;
            case 1:
                return this.tokenLength == 2 && isMonth(value);
            case 2:
                return this.tokenLength == 2 && isDay(value);
            case 3:
                return this.tokenLength == 2 && isHour(value);
            case 4:
            case 5:
                return this.tokenLength == 2 && isMinuteOrSecond(value);
            case 6:
                return this.tokenLength < 4 && isMillisecond(value);
            default:
                return true;
        }
    }

    private boolean skipEcmaDelimiter() {
        switch (this.currentField) {
            case 0:
            case 1:
                return skipNumberDelimiter('-') || peek() == 84 || peek() == -1;
            case 2:
                return peek() == 84 || peek() == -1;
            case 3:
            case 4:
                return skipNumberDelimiter(':') || endOfTime();
            case 5:
                return skipNumberDelimiter('.') || endOfTime();
            default:
                return true;
        }
    }

    private boolean endOfTime() {
        int c2 = peek();
        return c2 == -1 || c2 == 90 || c2 == 45 || c2 == 43 || c2 == 32;
    }

    private static boolean isAsciiLetter(char ch) {
        return ('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z');
    }

    private static boolean isAsciiDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }

    private int readNumber(int maxDigits) {
        int start = this.pos;
        int n2 = 0;
        int max = Math.min(this.length, this.pos + maxDigits);
        while (this.pos < max && isAsciiDigit(this.string.charAt(this.pos))) {
            String str = this.string;
            int i2 = this.pos;
            this.pos = i2 + 1;
            n2 = ((n2 * 10) + str.charAt(i2)) - 48;
        }
        this.tokenLength = this.pos - start;
        return n2;
    }

    private Name readName() {
        int start = this.pos;
        int limit = Math.min(this.pos + 3, this.length);
        while (this.pos < limit && isAsciiLetter(this.string.charAt(this.pos))) {
            this.pos++;
        }
        String key = this.string.substring(start, this.pos).toLowerCase(Locale.ENGLISH);
        Name name = names.get(key);
        while (this.pos < this.length && isAsciiLetter(this.string.charAt(this.pos))) {
            this.pos++;
        }
        this.tokenLength = this.pos - start;
        if (name != null && name.matches(this.string, start, this.tokenLength)) {
            return name;
        }
        return null;
    }

    private int readTimeZoneOffset() {
        int sign = this.string.charAt(this.pos - 1) == '+' ? 1 : -1;
        int offset = readNumber(2);
        skipDelimiter(':');
        return sign * ((offset * 60) + readNumber(2));
    }

    private boolean skipParentheses() {
        int parenCount = 1;
        while (this.pos < this.length && parenCount != 0) {
            String str = this.string;
            int i2 = this.pos;
            this.pos = i2 + 1;
            char c2 = str.charAt(i2);
            if (c2 == '(') {
                parenCount++;
            } else if (c2 == ')') {
                parenCount--;
            }
        }
        return true;
    }

    private static int getDefaultValue(int field) {
        switch (field) {
            case 1:
            case 2:
                return 1;
            default:
                return 0;
        }
    }

    private static boolean isDay(int n2) {
        return 1 <= n2 && n2 <= 31;
    }

    private static boolean isMonth(int n2) {
        return 1 <= n2 && n2 <= 12;
    }

    private static boolean isHour(int n2) {
        return 0 <= n2 && n2 <= 24;
    }

    private static boolean isMinuteOrSecond(int n2) {
        return 0 <= n2 && n2 < 60;
    }

    private static boolean isMillisecond(int n2) {
        return 0 <= n2 && n2 < 1000;
    }

    private boolean setMonth(int m2) {
        if (!isSet(1)) {
            this.namedMonth = true;
            set(1, m2);
            return true;
        }
        return false;
    }

    private boolean setDateField(int n2) {
        for (int field = 0; field != 3; field++) {
            if (!isSet(field)) {
                set(field, n2);
                return true;
            }
        }
        return false;
    }

    private boolean setTimeField(int n2) {
        for (int field = 3; field != 7; field++) {
            if (!isSet(field)) {
                if (checkLegacyField(field, n2)) {
                    set(field, n2);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean setTimezone(int offset, boolean asNumericOffset) {
        if (!isSet(7) || (asNumericOffset && get(7).intValue() == 0)) {
            set(7, offset);
            return true;
        }
        return false;
    }

    private boolean setAmPm(int offset) {
        if (!isSet(3)) {
            return false;
        }
        int hour = get(3).intValue();
        if (hour >= 0 && hour <= 12) {
            set(3, hour + offset);
            return true;
        }
        return true;
    }

    private boolean patchResult(boolean strict) {
        if (!isSet(0) && !isSet(3)) {
            return false;
        }
        if (isSet(3) && !isSet(4)) {
            return false;
        }
        for (int field = 0; field <= 7; field++) {
            if (get(field) == null && (field != 7 || strict)) {
                int value = getDefaultValue(field);
                set(field, value);
            }
        }
        if (!strict) {
            if (isDay(get(0).intValue())) {
                int d2 = get(0).intValue();
                set(0, get(2).intValue());
                if (this.namedMonth) {
                    set(2, d2);
                } else {
                    int d22 = get(1).intValue();
                    set(1, d2);
                    set(2, d22);
                }
            }
            if (!isMonth(get(1).intValue()) || !isDay(get(2).intValue())) {
                return false;
            }
            int year = get(0).intValue();
            if (year >= 0 && year < 100) {
                set(0, year >= 50 ? 1900 + year : 2000 + year);
            }
        } else if (get(3).intValue() == 24 && (get(4).intValue() != 0 || get(5).intValue() != 0 || get(6).intValue() != 0)) {
            return false;
        }
        set(1, get(1).intValue() - 1);
        return true;
    }

    private static void addName(String str, int type, int value) {
        Name name = new Name(str, type, value);
        names.put(name.key, name);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/parser/DateParser$Name.class */
    private static class Name {
        final String name;
        final String key;
        final int value;
        final int type;
        static final int DAY_OF_WEEK = -1;
        static final int MONTH_NAME = 0;
        static final int AM_PM = 1;
        static final int TIMEZONE_ID = 2;
        static final int TIME_SEPARATOR = 3;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DateParser.class.desiredAssertionStatus();
        }

        Name(String name, int type, int value) {
            if (!$assertionsDisabled && name == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !name.equals(name.toLowerCase(Locale.ENGLISH))) {
                throw new AssertionError();
            }
            this.name = name;
            this.key = name.substring(0, Math.min(3, name.length()));
            this.type = type;
            this.value = value;
        }

        public boolean matches(String str, int offset, int len) {
            return this.name.regionMatches(true, 0, str, offset, len);
        }

        public String toString() {
            return this.name;
        }
    }
}
