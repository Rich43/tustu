package sun.text.normalizer;

import java.text.ParsePosition;
import java.util.Iterator;
import java.util.TreeSet;

/* loaded from: rt.jar:sun/text/normalizer/UnicodeSet.class */
public class UnicodeSet implements UnicodeMatcher {
    private static final int LOW = 0;
    private static final int HIGH = 1114112;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 1114111;
    private int len;
    private int[] list;
    private int[] rangeList;
    private int[] buffer;
    TreeSet<String> strings;
    private String pat;
    private static final int START_EXTRA = 16;
    private static final int GROW_EXTRA = 16;
    private static UnicodeSet[] INCLUSIONS = null;
    static final VersionInfo NO_VERSION = VersionInfo.getInstance(0, 0, 0, 0);
    public static final int IGNORE_SPACE = 1;

    /* loaded from: rt.jar:sun/text/normalizer/UnicodeSet$Filter.class */
    private interface Filter {
        boolean contains(int i2);
    }

    public UnicodeSet() {
        this.strings = new TreeSet<>();
        this.pat = null;
        this.list = new int[17];
        int[] iArr = this.list;
        int i2 = this.len;
        this.len = i2 + 1;
        iArr[i2] = HIGH;
    }

    public UnicodeSet(int i2, int i3) {
        this();
        complement(i2, i3);
    }

    public UnicodeSet(String str) {
        this();
        applyPattern(str, (ParsePosition) null, (SymbolTable) null, 1);
    }

    public UnicodeSet set(UnicodeSet unicodeSet) {
        this.list = (int[]) unicodeSet.list.clone();
        this.len = unicodeSet.len;
        this.pat = unicodeSet.pat;
        this.strings = (TreeSet) unicodeSet.strings.clone();
        return this;
    }

    public final UnicodeSet applyPattern(String str) {
        return applyPattern(str, (ParsePosition) null, (SymbolTable) null, 1);
    }

    private static void _appendToPat(StringBuffer stringBuffer, String str, boolean z2) {
        int charCount = 0;
        while (true) {
            int i2 = charCount;
            if (i2 < str.length()) {
                _appendToPat(stringBuffer, UTF16.charAt(str, i2), z2);
                charCount = i2 + UTF16.getCharCount(i2);
            } else {
                return;
            }
        }
    }

    private static void _appendToPat(StringBuffer stringBuffer, int i2, boolean z2) {
        if (z2 && Utility.isUnprintable(i2) && Utility.escapeUnprintable(stringBuffer, i2)) {
            return;
        }
        switch (i2) {
            case 36:
            case 38:
            case 45:
            case 58:
            case 91:
            case 92:
            case 93:
            case 94:
            case 123:
            case 125:
                stringBuffer.append('\\');
                break;
            default:
                if (UCharacterProperty.isRuleWhiteSpace(i2)) {
                    stringBuffer.append('\\');
                    break;
                }
                break;
        }
        UTF16.append(stringBuffer, i2);
    }

    private StringBuffer _toPattern(StringBuffer stringBuffer, boolean z2) {
        if (this.pat != null) {
            int i2 = 0;
            int charCount = 0;
            while (charCount < this.pat.length()) {
                int iCharAt = UTF16.charAt(this.pat, charCount);
                charCount += UTF16.getCharCount(iCharAt);
                if (z2 && Utility.isUnprintable(iCharAt)) {
                    if (i2 % 2 == 1) {
                        stringBuffer.setLength(stringBuffer.length() - 1);
                    }
                    Utility.escapeUnprintable(stringBuffer, iCharAt);
                    i2 = 0;
                } else {
                    UTF16.append(stringBuffer, iCharAt);
                    if (iCharAt == 92) {
                        i2++;
                    } else {
                        i2 = 0;
                    }
                }
            }
            return stringBuffer;
        }
        return _generatePattern(stringBuffer, z2, true);
    }

    public StringBuffer _generatePattern(StringBuffer stringBuffer, boolean z2, boolean z3) {
        stringBuffer.append('[');
        int rangeCount = getRangeCount();
        if (rangeCount > 1 && getRangeStart(0) == 0 && getRangeEnd(rangeCount - 1) == 1114111) {
            stringBuffer.append('^');
            for (int i2 = 1; i2 < rangeCount; i2++) {
                int rangeEnd = getRangeEnd(i2 - 1) + 1;
                int rangeStart = getRangeStart(i2) - 1;
                _appendToPat(stringBuffer, rangeEnd, z2);
                if (rangeEnd != rangeStart) {
                    if (rangeEnd + 1 != rangeStart) {
                        stringBuffer.append('-');
                    }
                    _appendToPat(stringBuffer, rangeStart, z2);
                }
            }
        } else {
            for (int i3 = 0; i3 < rangeCount; i3++) {
                int rangeStart2 = getRangeStart(i3);
                int rangeEnd2 = getRangeEnd(i3);
                _appendToPat(stringBuffer, rangeStart2, z2);
                if (rangeStart2 != rangeEnd2) {
                    if (rangeStart2 + 1 != rangeEnd2) {
                        stringBuffer.append('-');
                    }
                    _appendToPat(stringBuffer, rangeEnd2, z2);
                }
            }
        }
        if (z3 && this.strings.size() > 0) {
            Iterator<String> it = this.strings.iterator();
            while (it.hasNext()) {
                stringBuffer.append('{');
                _appendToPat(stringBuffer, it.next(), z2);
                stringBuffer.append('}');
            }
        }
        return stringBuffer.append(']');
    }

    private UnicodeSet add_unchecked(int i2, int i3) {
        if (i2 < 0 || i2 > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(i2, 6));
        }
        if (i3 < 0 || i3 > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(i3, 6));
        }
        if (i2 < i3) {
            add(range(i2, i3), 2, 0);
        } else if (i2 == i3) {
            add(i2);
        }
        return this;
    }

    public final UnicodeSet add(int i2) {
        return add_unchecked(i2);
    }

    private final UnicodeSet add_unchecked(int i2) {
        if (i2 < 0 || i2 > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(i2, 6));
        }
        int iFindCodePoint = findCodePoint(i2);
        if ((iFindCodePoint & 1) != 0) {
            return this;
        }
        if (i2 == this.list[iFindCodePoint] - 1) {
            this.list[iFindCodePoint] = i2;
            if (i2 == 1114111) {
                ensureCapacity(this.len + 1);
                int[] iArr = this.list;
                int i3 = this.len;
                this.len = i3 + 1;
                iArr[i3] = HIGH;
            }
            if (iFindCodePoint > 0 && i2 == this.list[iFindCodePoint - 1]) {
                System.arraycopy(this.list, iFindCodePoint + 1, this.list, iFindCodePoint - 1, (this.len - iFindCodePoint) - 1);
                this.len -= 2;
            }
        } else if (iFindCodePoint > 0 && i2 == this.list[iFindCodePoint - 1]) {
            int[] iArr2 = this.list;
            int i4 = iFindCodePoint - 1;
            iArr2[i4] = iArr2[i4] + 1;
        } else {
            if (this.len + 2 > this.list.length) {
                int[] iArr3 = new int[this.len + 2 + 16];
                if (iFindCodePoint != 0) {
                    System.arraycopy(this.list, 0, iArr3, 0, iFindCodePoint);
                }
                System.arraycopy(this.list, iFindCodePoint, iArr3, iFindCodePoint + 2, this.len - iFindCodePoint);
                this.list = iArr3;
            } else {
                System.arraycopy(this.list, iFindCodePoint, this.list, iFindCodePoint + 2, this.len - iFindCodePoint);
            }
            this.list[iFindCodePoint] = i2;
            this.list[iFindCodePoint + 1] = i2 + 1;
            this.len += 2;
        }
        this.pat = null;
        return this;
    }

    public final UnicodeSet add(String str) {
        int singleCP = getSingleCP(str);
        if (singleCP < 0) {
            this.strings.add(str);
            this.pat = null;
        } else {
            add_unchecked(singleCP, singleCP);
        }
        return this;
    }

    private static int getSingleCP(String str) {
        if (str.length() < 1) {
            throw new IllegalArgumentException("Can't use zero-length strings in UnicodeSet");
        }
        if (str.length() > 2) {
            return -1;
        }
        if (str.length() == 1) {
            return str.charAt(0);
        }
        int iCharAt = UTF16.charAt(str, 0);
        if (iCharAt > 65535) {
            return iCharAt;
        }
        return -1;
    }

    public UnicodeSet complement(int i2, int i3) {
        if (i2 < 0 || i2 > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(i2, 6));
        }
        if (i3 < 0 || i3 > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(i3, 6));
        }
        if (i2 <= i3) {
            xor(range(i2, i3), 2, 0);
        }
        this.pat = null;
        return this;
    }

    public UnicodeSet complement() {
        if (this.list[0] == 0) {
            System.arraycopy(this.list, 1, this.list, 0, this.len - 1);
            this.len--;
        } else {
            ensureCapacity(this.len + 1);
            System.arraycopy(this.list, 0, this.list, 1, this.len);
            this.list[0] = 0;
            this.len++;
        }
        this.pat = null;
        return this;
    }

    public boolean contains(int i2) {
        if (i2 < 0 || i2 > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(i2, 6));
        }
        return (findCodePoint(i2) & 1) != 0;
    }

    private final int findCodePoint(int i2) {
        if (i2 < this.list[0]) {
            return 0;
        }
        if (this.len >= 2 && i2 >= this.list[this.len - 2]) {
            return this.len - 1;
        }
        int i3 = 0;
        int i4 = this.len - 1;
        while (true) {
            int i5 = (i3 + i4) >>> 1;
            if (i5 == i3) {
                return i4;
            }
            if (i2 < this.list[i5]) {
                i4 = i5;
            } else {
                i3 = i5;
            }
        }
    }

    public UnicodeSet addAll(UnicodeSet unicodeSet) {
        add(unicodeSet.list, unicodeSet.len, 0);
        this.strings.addAll(unicodeSet.strings);
        return this;
    }

    public UnicodeSet retainAll(UnicodeSet unicodeSet) {
        retain(unicodeSet.list, unicodeSet.len, 0);
        this.strings.retainAll(unicodeSet.strings);
        return this;
    }

    public UnicodeSet removeAll(UnicodeSet unicodeSet) {
        retain(unicodeSet.list, unicodeSet.len, 2);
        this.strings.removeAll(unicodeSet.strings);
        return this;
    }

    public UnicodeSet clear() {
        this.list[0] = HIGH;
        this.len = 1;
        this.pat = null;
        this.strings.clear();
        return this;
    }

    public int getRangeCount() {
        return this.len / 2;
    }

    public int getRangeStart(int i2) {
        return this.list[i2 * 2];
    }

    public int getRangeEnd(int i2) {
        return this.list[(i2 * 2) + 1] - 1;
    }

    UnicodeSet applyPattern(String str, ParsePosition parsePosition, SymbolTable symbolTable, int i2) {
        boolean z2 = parsePosition == null;
        if (z2) {
            parsePosition = new ParsePosition(0);
        }
        StringBuffer stringBuffer = new StringBuffer();
        RuleCharacterIterator ruleCharacterIterator = new RuleCharacterIterator(str, symbolTable, parsePosition);
        applyPattern(ruleCharacterIterator, symbolTable, stringBuffer, i2);
        if (ruleCharacterIterator.inVariable()) {
            syntaxError(ruleCharacterIterator, "Extra chars in variable value");
        }
        this.pat = stringBuffer.toString();
        if (z2) {
            int index = parsePosition.getIndex();
            if ((i2 & 1) != 0) {
                index = Utility.skipWhitespace(str, index);
            }
            if (index != str.length()) {
                throw new IllegalArgumentException("Parse of \"" + str + "\" failed at " + index);
            }
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    void applyPattern(RuleCharacterIterator ruleCharacterIterator, SymbolTable symbolTable, StringBuffer stringBuffer, int i2) {
        UnicodeMatcher unicodeMatcherLookupMatcher;
        int i3 = 3;
        if ((i2 & 1) != 0) {
            i3 = 3 | 4;
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = null;
        boolean z2 = false;
        UnicodeSet unicodeSet = null;
        Object pos = null;
        boolean z3 = false;
        int i4 = 0;
        boolean z4 = false;
        char c2 = 0;
        boolean z5 = false;
        clear();
        while (true) {
            if (z4 != 2 && !ruleCharacterIterator.atEnd()) {
                int next = 0;
                boolean zIsEscaped = false;
                UnicodeSet unicodeSet2 = null;
                boolean z6 = false;
                if (resemblesPropertyPattern(ruleCharacterIterator, i3)) {
                    z6 = 2;
                } else {
                    pos = ruleCharacterIterator.getPos(pos);
                    next = ruleCharacterIterator.next(i3);
                    zIsEscaped = ruleCharacterIterator.isEscaped();
                    if (next == 91 && !zIsEscaped) {
                        if (z4) {
                            ruleCharacterIterator.setPos(pos);
                            z6 = true;
                        } else {
                            z4 = true;
                            stringBuffer2.append('[');
                            pos = ruleCharacterIterator.getPos(pos);
                            next = ruleCharacterIterator.next(i3);
                            boolean zIsEscaped2 = ruleCharacterIterator.isEscaped();
                            if (next == 94 && !zIsEscaped2) {
                                z5 = true;
                                stringBuffer2.append('^');
                                pos = ruleCharacterIterator.getPos(pos);
                                next = ruleCharacterIterator.next(i3);
                                ruleCharacterIterator.isEscaped();
                            }
                            if (next == 45) {
                                zIsEscaped = true;
                            } else {
                                ruleCharacterIterator.setPos(pos);
                            }
                        }
                    } else if (symbolTable != null && (unicodeMatcherLookupMatcher = symbolTable.lookupMatcher(next)) != null) {
                        try {
                            unicodeSet2 = (UnicodeSet) unicodeMatcherLookupMatcher;
                            z6 = 3;
                        } catch (ClassCastException e2) {
                            syntaxError(ruleCharacterIterator, "Syntax error");
                        }
                    }
                }
                if (z6) {
                    if (z3) {
                        if (c2 != 0) {
                            syntaxError(ruleCharacterIterator, "Char expected after operator");
                        }
                        add_unchecked(i4, i4);
                        _appendToPat(stringBuffer2, i4, false);
                        c2 = 0;
                    }
                    if (c2 == '-' || c2 == '&') {
                        stringBuffer2.append(c2);
                    }
                    if (unicodeSet2 == null) {
                        if (unicodeSet == null) {
                            unicodeSet = new UnicodeSet();
                        }
                        unicodeSet2 = unicodeSet;
                    }
                    switch (z6) {
                        case true:
                            unicodeSet2.applyPattern(ruleCharacterIterator, symbolTable, stringBuffer2, i2);
                            break;
                        case true:
                            ruleCharacterIterator.skipIgnored(i3);
                            unicodeSet2.applyPropertyPattern(ruleCharacterIterator, stringBuffer2, symbolTable);
                            break;
                        case true:
                            unicodeSet2._toPattern(stringBuffer2, false);
                            break;
                    }
                    z2 = true;
                    if (!z4) {
                        set(unicodeSet2);
                        z4 = 2;
                    } else {
                        switch (c2) {
                            case 0:
                                addAll(unicodeSet2);
                                break;
                            case '&':
                                retainAll(unicodeSet2);
                                break;
                            case '-':
                                removeAll(unicodeSet2);
                                break;
                        }
                        c2 = 0;
                        z3 = 2;
                    }
                } else {
                    if (!z4) {
                        syntaxError(ruleCharacterIterator, "Missing '['");
                    }
                    if (!zIsEscaped) {
                        switch (next) {
                            case 36:
                                pos = ruleCharacterIterator.getPos(pos);
                                next = ruleCharacterIterator.next(i3);
                                boolean z7 = next == 93 && !ruleCharacterIterator.isEscaped();
                                if (symbolTable == null && !z7) {
                                    next = 36;
                                    ruleCharacterIterator.setPos(pos);
                                    break;
                                } else if (z7 && c2 == 0) {
                                    if (z3) {
                                        add_unchecked(i4, i4);
                                        _appendToPat(stringBuffer2, i4, false);
                                    }
                                    add_unchecked(65535);
                                    z2 = true;
                                    stringBuffer2.append('$').append(']');
                                    z4 = 2;
                                    break;
                                } else {
                                    syntaxError(ruleCharacterIterator, "Unquoted '$'");
                                    break;
                                }
                                break;
                            case 38:
                                if (z3 == 2 && c2 == 0) {
                                    c2 = (char) next;
                                    break;
                                } else {
                                    syntaxError(ruleCharacterIterator, "'&' not after set");
                                    break;
                                }
                                break;
                            case 45:
                                if (c2 == 0) {
                                    if (z3) {
                                        c2 = (char) next;
                                        break;
                                    } else {
                                        add_unchecked(next, next);
                                        next = ruleCharacterIterator.next(i3);
                                        boolean zIsEscaped3 = ruleCharacterIterator.isEscaped();
                                        if (next == 93 && !zIsEscaped3) {
                                            stringBuffer2.append("-]");
                                            z4 = 2;
                                            break;
                                        }
                                    }
                                }
                                syntaxError(ruleCharacterIterator, "'-' not after char or set");
                                break;
                            case 93:
                                if (z3) {
                                    add_unchecked(i4, i4);
                                    _appendToPat(stringBuffer2, i4, false);
                                }
                                if (c2 == '-') {
                                    add_unchecked(c2, c2);
                                    stringBuffer2.append(c2);
                                } else if (c2 == '&') {
                                    syntaxError(ruleCharacterIterator, "Trailing '&'");
                                }
                                stringBuffer2.append(']');
                                z4 = 2;
                                continue;
                            case 94:
                                syntaxError(ruleCharacterIterator, "'^' not after '['");
                                break;
                            case 123:
                                if (c2 != 0) {
                                    syntaxError(ruleCharacterIterator, "Missing operand after operator");
                                }
                                if (z3) {
                                    add_unchecked(i4, i4);
                                    _appendToPat(stringBuffer2, i4, false);
                                }
                                z3 = false;
                                if (stringBuffer3 == null) {
                                    stringBuffer3 = new StringBuffer();
                                } else {
                                    stringBuffer3.setLength(0);
                                }
                                boolean z8 = false;
                                while (true) {
                                    if (!ruleCharacterIterator.atEnd()) {
                                        int next2 = ruleCharacterIterator.next(i3);
                                        boolean zIsEscaped4 = ruleCharacterIterator.isEscaped();
                                        if (next2 == 125 && !zIsEscaped4) {
                                            z8 = true;
                                        } else {
                                            UTF16.append(stringBuffer3, next2);
                                        }
                                    }
                                }
                                if (stringBuffer3.length() < 1 || !z8) {
                                    syntaxError(ruleCharacterIterator, "Invalid multicharacter string");
                                }
                                add(stringBuffer3.toString());
                                stringBuffer2.append('{');
                                _appendToPat(stringBuffer2, stringBuffer3.toString(), false);
                                stringBuffer2.append('}');
                                continue;
                        }
                    }
                    switch (z3) {
                        case false:
                            z3 = true;
                            i4 = next;
                            break;
                        case true:
                            if (c2 == '-') {
                                if (i4 >= next) {
                                    syntaxError(ruleCharacterIterator, "Invalid range");
                                }
                                add_unchecked(i4, next);
                                _appendToPat(stringBuffer2, i4, false);
                                stringBuffer2.append(c2);
                                _appendToPat(stringBuffer2, next, false);
                                c2 = 0;
                                z3 = false;
                                break;
                            } else {
                                add_unchecked(i4, i4);
                                _appendToPat(stringBuffer2, i4, false);
                                i4 = next;
                                break;
                            }
                        case true:
                            if (c2 != 0) {
                                syntaxError(ruleCharacterIterator, "Set expected after operator");
                            }
                            i4 = next;
                            z3 = true;
                            break;
                    }
                }
            }
        }
        if (z4 != 2) {
            syntaxError(ruleCharacterIterator, "Missing ']'");
        }
        ruleCharacterIterator.skipIgnored(i3);
        if (z5) {
            complement();
        }
        if (z2) {
            stringBuffer.append(stringBuffer2.toString());
        } else {
            _generatePattern(stringBuffer, false, true);
        }
    }

    private static void syntaxError(RuleCharacterIterator ruleCharacterIterator, String str) {
        throw new IllegalArgumentException("Error: " + str + " at \"" + Utility.escape(ruleCharacterIterator.toString()) + '\"');
    }

    private void ensureCapacity(int i2) {
        if (i2 <= this.list.length) {
            return;
        }
        int[] iArr = new int[i2 + 16];
        System.arraycopy(this.list, 0, iArr, 0, this.len);
        this.list = iArr;
    }

    private void ensureBufferCapacity(int i2) {
        if (this.buffer == null || i2 > this.buffer.length) {
            this.buffer = new int[i2 + 16];
        }
    }

    private int[] range(int i2, int i3) {
        if (this.rangeList == null) {
            this.rangeList = new int[]{i2, i3 + 1, HIGH};
        } else {
            this.rangeList[0] = i2;
            this.rangeList[1] = i3 + 1;
        }
        return this.rangeList;
    }

    private UnicodeSet xor(int[] iArr, int i2, int i3) {
        int i4;
        ensureBufferCapacity(this.len + i2);
        int i5 = 0;
        int i6 = 0;
        int i7 = 0 + 1;
        int i8 = this.list[0];
        if (i3 == 1 || i3 == 2) {
            i4 = 0;
            if (iArr[0] == 0) {
                i5 = 0 + 1;
                i4 = iArr[i5];
            }
        } else {
            i5 = 0 + 1;
            i4 = iArr[0];
        }
        while (true) {
            if (i8 < i4) {
                int i9 = i6;
                i6++;
                this.buffer[i9] = i8;
                int i10 = i7;
                i7++;
                i8 = this.list[i10];
            } else if (i4 < i8) {
                int i11 = i6;
                i6++;
                this.buffer[i11] = i4;
                int i12 = i5;
                i5++;
                i4 = iArr[i12];
            } else if (i8 != HIGH) {
                int i13 = i7;
                i7++;
                i8 = this.list[i13];
                int i14 = i5;
                i5++;
                i4 = iArr[i14];
            } else {
                this.buffer[i6] = HIGH;
                this.len = i6 + 1;
                int[] iArr2 = this.list;
                this.list = this.buffer;
                this.buffer = iArr2;
                this.pat = null;
                return this;
            }
        }
    }

    private UnicodeSet add(int[] iArr, int i2, int i3) {
        int iMax;
        int iMax2;
        int iMax3;
        ensureBufferCapacity(this.len + i2);
        int i4 = 0;
        int i5 = 0 + 1;
        int i6 = this.list[0];
        int i7 = 0 + 1;
        int i8 = iArr[0];
        while (true) {
            switch (i3) {
                case 0:
                    if (i6 < i8) {
                        if (i4 > 0 && i6 <= this.buffer[i4 - 1]) {
                            i4--;
                            iMax = max(this.list[i5], this.buffer[i4]);
                        } else {
                            int i9 = i4;
                            i4++;
                            this.buffer[i9] = i6;
                            iMax = this.list[i5];
                        }
                        i6 = iMax;
                        i5++;
                        i3 ^= 1;
                        break;
                    } else if (i8 < i6) {
                        if (i4 > 0 && i8 <= this.buffer[i4 - 1]) {
                            i4--;
                            iMax2 = max(iArr[i7], this.buffer[i4]);
                        } else {
                            int i10 = i4;
                            i4++;
                            this.buffer[i10] = i8;
                            iMax2 = iArr[i7];
                        }
                        i8 = iMax2;
                        i7++;
                        i3 ^= 2;
                        break;
                    } else if (i6 != HIGH) {
                        if (i4 > 0 && i6 <= this.buffer[i4 - 1]) {
                            i4--;
                            iMax3 = max(this.list[i5], this.buffer[i4]);
                        } else {
                            int i11 = i4;
                            i4++;
                            this.buffer[i11] = i6;
                            iMax3 = this.list[i5];
                        }
                        i6 = iMax3;
                        i5++;
                        int i12 = i7;
                        i7++;
                        i8 = iArr[i12];
                        i3 = (i3 ^ 1) ^ 2;
                        break;
                    } else {
                        break;
                    }
                    break;
                case 1:
                    if (i6 < i8) {
                        int i13 = i4;
                        i4++;
                        this.buffer[i13] = i6;
                        int i14 = i5;
                        i5++;
                        i6 = this.list[i14];
                        i3 ^= 1;
                        break;
                    } else if (i8 < i6) {
                        int i15 = i7;
                        i7++;
                        i8 = iArr[i15];
                        i3 ^= 2;
                        break;
                    } else if (i6 != HIGH) {
                        int i16 = i5;
                        i5++;
                        i6 = this.list[i16];
                        int i17 = i7;
                        i7++;
                        i8 = iArr[i17];
                        i3 = (i3 ^ 1) ^ 2;
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (i8 < i6) {
                        int i18 = i4;
                        i4++;
                        this.buffer[i18] = i8;
                        int i19 = i7;
                        i7++;
                        i8 = iArr[i19];
                        i3 ^= 2;
                        break;
                    } else if (i6 < i8) {
                        int i20 = i5;
                        i5++;
                        i6 = this.list[i20];
                        i3 ^= 1;
                        break;
                    } else if (i6 != HIGH) {
                        int i21 = i5;
                        i5++;
                        i6 = this.list[i21];
                        int i22 = i7;
                        i7++;
                        i8 = iArr[i22];
                        i3 = (i3 ^ 1) ^ 2;
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (i8 <= i6) {
                        if (i6 != HIGH) {
                            int i23 = i4;
                            i4++;
                            this.buffer[i23] = i6;
                            int i24 = i5;
                            i5++;
                            i6 = this.list[i24];
                            int i25 = i7;
                            i7++;
                            i8 = iArr[i25];
                            i3 = (i3 ^ 1) ^ 2;
                            break;
                        } else {
                            break;
                        }
                    } else if (i8 == HIGH) {
                        break;
                    } else {
                        int i26 = i4;
                        i4++;
                        this.buffer[i26] = i8;
                        int i242 = i5;
                        i5++;
                        i6 = this.list[i242];
                        int i252 = i7;
                        i7++;
                        i8 = iArr[i252];
                        i3 = (i3 ^ 1) ^ 2;
                    }
            }
        }
        this.buffer[i4] = HIGH;
        this.len = i4 + 1;
        int[] iArr2 = this.list;
        this.list = this.buffer;
        this.buffer = iArr2;
        this.pat = null;
        return this;
    }

    private UnicodeSet retain(int[] iArr, int i2, int i3) {
        ensureBufferCapacity(this.len + i2);
        int i4 = 0;
        int i5 = 0 + 1;
        int i6 = this.list[0];
        int i7 = 0 + 1;
        int i8 = iArr[0];
        while (true) {
            switch (i3) {
                case 0:
                    if (i6 < i8) {
                        int i9 = i5;
                        i5++;
                        i6 = this.list[i9];
                        i3 ^= 1;
                        break;
                    } else if (i8 < i6) {
                        int i10 = i7;
                        i7++;
                        i8 = iArr[i10];
                        i3 ^= 2;
                        break;
                    } else if (i6 != HIGH) {
                        int i11 = i4;
                        i4++;
                        this.buffer[i11] = i6;
                        int i12 = i5;
                        i5++;
                        i6 = this.list[i12];
                        int i13 = i7;
                        i7++;
                        i8 = iArr[i13];
                        i3 = (i3 ^ 1) ^ 2;
                        break;
                    } else {
                        break;
                    }
                case 1:
                    if (i6 < i8) {
                        int i14 = i5;
                        i5++;
                        i6 = this.list[i14];
                        i3 ^= 1;
                        break;
                    } else if (i8 < i6) {
                        int i15 = i4;
                        i4++;
                        this.buffer[i15] = i8;
                        int i16 = i7;
                        i7++;
                        i8 = iArr[i16];
                        i3 ^= 2;
                        break;
                    } else if (i6 != HIGH) {
                        int i17 = i5;
                        i5++;
                        i6 = this.list[i17];
                        int i18 = i7;
                        i7++;
                        i8 = iArr[i18];
                        i3 = (i3 ^ 1) ^ 2;
                        break;
                    } else {
                        break;
                    }
                case 2:
                    if (i8 < i6) {
                        int i19 = i7;
                        i7++;
                        i8 = iArr[i19];
                        i3 ^= 2;
                        break;
                    } else if (i6 < i8) {
                        int i20 = i4;
                        i4++;
                        this.buffer[i20] = i6;
                        int i21 = i5;
                        i5++;
                        i6 = this.list[i21];
                        i3 ^= 1;
                        break;
                    } else if (i6 != HIGH) {
                        int i22 = i5;
                        i5++;
                        i6 = this.list[i22];
                        int i23 = i7;
                        i7++;
                        i8 = iArr[i23];
                        i3 = (i3 ^ 1) ^ 2;
                        break;
                    } else {
                        break;
                    }
                case 3:
                    if (i6 < i8) {
                        int i24 = i4;
                        i4++;
                        this.buffer[i24] = i6;
                        int i25 = i5;
                        i5++;
                        i6 = this.list[i25];
                        i3 ^= 1;
                        break;
                    } else if (i8 < i6) {
                        int i26 = i4;
                        i4++;
                        this.buffer[i26] = i8;
                        int i27 = i7;
                        i7++;
                        i8 = iArr[i27];
                        i3 ^= 2;
                        break;
                    } else if (i6 != HIGH) {
                        int i28 = i4;
                        i4++;
                        this.buffer[i28] = i6;
                        int i29 = i5;
                        i5++;
                        i6 = this.list[i29];
                        int i30 = i7;
                        i7++;
                        i8 = iArr[i30];
                        i3 = (i3 ^ 1) ^ 2;
                        break;
                    } else {
                        break;
                    }
            }
        }
        this.buffer[i4] = HIGH;
        this.len = i4 + 1;
        int[] iArr2 = this.list;
        this.list = this.buffer;
        this.buffer = iArr2;
        this.pat = null;
        return this;
    }

    private static final int max(int i2, int i3) {
        return i2 > i3 ? i2 : i3;
    }

    /* loaded from: rt.jar:sun/text/normalizer/UnicodeSet$VersionFilter.class */
    private static class VersionFilter implements Filter {
        VersionInfo version;

        VersionFilter(VersionInfo versionInfo) {
            this.version = versionInfo;
        }

        @Override // sun.text.normalizer.UnicodeSet.Filter
        public boolean contains(int i2) {
            VersionInfo age = UCharacter.getAge(i2);
            return age != UnicodeSet.NO_VERSION && age.compareTo(this.version) <= 0;
        }
    }

    private static synchronized UnicodeSet getInclusions(int i2) {
        if (INCLUSIONS == null) {
            INCLUSIONS = new UnicodeSet[9];
        }
        if (INCLUSIONS[i2] == null) {
            UnicodeSet unicodeSet = new UnicodeSet();
            switch (i2) {
                case 2:
                    UCharacterProperty.getInstance().upropsvec_addPropertyStarts(unicodeSet);
                    INCLUSIONS[i2] = unicodeSet;
                    break;
                default:
                    throw new IllegalStateException("UnicodeSet.getInclusions(unknown src " + i2 + ")");
            }
        }
        return INCLUSIONS[i2];
    }

    private UnicodeSet applyFilter(Filter filter, int i2) {
        clear();
        int i3 = -1;
        UnicodeSet inclusions = getInclusions(i2);
        int rangeCount = inclusions.getRangeCount();
        for (int i4 = 0; i4 < rangeCount; i4++) {
            int rangeStart = inclusions.getRangeStart(i4);
            int rangeEnd = inclusions.getRangeEnd(i4);
            for (int i5 = rangeStart; i5 <= rangeEnd; i5++) {
                if (filter.contains(i5)) {
                    if (i3 < 0) {
                        i3 = i5;
                    }
                } else if (i3 >= 0) {
                    add_unchecked(i3, i5 - 1);
                    i3 = -1;
                }
            }
        }
        if (i3 >= 0) {
            add_unchecked(i3, 1114111);
        }
        return this;
    }

    private static String mungeCharName(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        int charCount = 0;
        while (charCount < str.length()) {
            int iCharAt = UTF16.charAt(str, charCount);
            charCount += UTF16.getCharCount(iCharAt);
            if (UCharacterProperty.isRuleWhiteSpace(iCharAt)) {
                if (stringBuffer.length() != 0 && stringBuffer.charAt(stringBuffer.length() - 1) != ' ') {
                    iCharAt = 32;
                }
            }
            UTF16.append(stringBuffer, iCharAt);
        }
        if (stringBuffer.length() != 0 && stringBuffer.charAt(stringBuffer.length() - 1) == ' ') {
            stringBuffer.setLength(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public UnicodeSet applyPropertyAlias(String str, String str2, SymbolTable symbolTable) {
        if (str2.length() > 0 && str.equals("Age")) {
            applyFilter(new VersionFilter(VersionInfo.getInstance(mungeCharName(str2))), 2);
            return this;
        }
        throw new IllegalArgumentException("Unsupported property: " + str);
    }

    private static boolean resemblesPropertyPattern(RuleCharacterIterator ruleCharacterIterator, int i2) {
        boolean z2 = false;
        int i3 = i2 & (-3);
        Object pos = ruleCharacterIterator.getPos(null);
        int next = ruleCharacterIterator.next(i3);
        if (next == 91 || next == 92) {
            int next2 = ruleCharacterIterator.next(i3 & (-5));
            z2 = next == 91 ? next2 == 58 : next2 == 78 || next2 == 112 || next2 == 80;
        }
        ruleCharacterIterator.setPos(pos);
        return z2;
    }

    private UnicodeSet applyPropertyPattern(String str, ParsePosition parsePosition, SymbolTable symbolTable) {
        int iSkipWhitespace;
        String strSubstring;
        String strSubstring2;
        int index = parsePosition.getIndex();
        if (index + 5 > str.length()) {
            return null;
        }
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        if (str.regionMatches(index, "[:", 0, 2)) {
            z2 = true;
            iSkipWhitespace = Utility.skipWhitespace(str, index + 2);
            if (iSkipWhitespace < str.length() && str.charAt(iSkipWhitespace) == '^') {
                iSkipWhitespace++;
                z4 = true;
            }
        } else if (str.regionMatches(true, index, "\\p", 0, 2) || str.regionMatches(index, "\\N", 0, 2)) {
            char cCharAt = str.charAt(index + 1);
            z4 = cCharAt == 'P';
            z3 = cCharAt == 'N';
            int iSkipWhitespace2 = Utility.skipWhitespace(str, index + 2);
            if (iSkipWhitespace2 == str.length()) {
                return null;
            }
            iSkipWhitespace = iSkipWhitespace2 + 1;
            if (str.charAt(iSkipWhitespace2) != '{') {
                return null;
            }
        } else {
            return null;
        }
        int iIndexOf = str.indexOf(z2 ? ":]" : "}", iSkipWhitespace);
        if (iIndexOf < 0) {
            return null;
        }
        int iIndexOf2 = str.indexOf(61, iSkipWhitespace);
        if (iIndexOf2 >= 0 && iIndexOf2 < iIndexOf && !z3) {
            strSubstring = str.substring(iSkipWhitespace, iIndexOf2);
            strSubstring2 = str.substring(iIndexOf2 + 1, iIndexOf);
        } else {
            strSubstring = str.substring(iSkipWhitespace, iIndexOf);
            strSubstring2 = "";
            if (z3) {
                strSubstring2 = strSubstring;
                strSubstring = "na";
            }
        }
        applyPropertyAlias(strSubstring, strSubstring2, symbolTable);
        if (z4) {
            complement();
        }
        parsePosition.setIndex(iIndexOf + (z2 ? 2 : 1));
        return this;
    }

    private void applyPropertyPattern(RuleCharacterIterator ruleCharacterIterator, StringBuffer stringBuffer, SymbolTable symbolTable) {
        String strLookahead = ruleCharacterIterator.lookahead();
        ParsePosition parsePosition = new ParsePosition(0);
        applyPropertyPattern(strLookahead, parsePosition, symbolTable);
        if (parsePosition.getIndex() == 0) {
            syntaxError(ruleCharacterIterator, "Invalid property pattern");
        }
        ruleCharacterIterator.jumpahead(parsePosition.getIndex());
        stringBuffer.append(strLookahead.substring(0, parsePosition.getIndex()));
    }
}
