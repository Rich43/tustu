package java.util.regex;

import java.util.Objects;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;

/* loaded from: rt.jar:java/util/regex/Matcher.class */
public final class Matcher implements MatchResult {
    Pattern parentPattern;
    int[] groups;
    int from;
    int to;
    int lookbehindTo;
    CharSequence text;
    static final int ENDANCHOR = 1;
    static final int NOANCHOR = 0;
    int[] locals;
    boolean hitEnd;
    boolean requireEnd;
    int acceptMode = 0;
    int first = -1;
    int last = 0;
    int oldLast = -1;
    int lastAppendPosition = 0;
    boolean transparentBounds = false;
    boolean anchoringBounds = true;

    Matcher() {
    }

    Matcher(Pattern pattern, CharSequence charSequence) {
        this.parentPattern = pattern;
        this.text = charSequence;
        this.groups = new int[Math.max(pattern.capturingGroupCount, 10) * 2];
        this.locals = new int[pattern.localCount];
        reset();
    }

    public Pattern pattern() {
        return this.parentPattern;
    }

    public MatchResult toMatchResult() {
        Matcher matcher = new Matcher(this.parentPattern, this.text.toString());
        matcher.first = this.first;
        matcher.last = this.last;
        matcher.groups = (int[]) this.groups.clone();
        return matcher;
    }

    public Matcher usePattern(Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        this.parentPattern = pattern;
        this.groups = new int[Math.max(pattern.capturingGroupCount, 10) * 2];
        this.locals = new int[pattern.localCount];
        for (int i2 = 0; i2 < this.groups.length; i2++) {
            this.groups[i2] = -1;
        }
        for (int i3 = 0; i3 < this.locals.length; i3++) {
            this.locals[i3] = -1;
        }
        return this;
    }

    public Matcher reset() {
        this.first = -1;
        this.last = 0;
        this.oldLast = -1;
        for (int i2 = 0; i2 < this.groups.length; i2++) {
            this.groups[i2] = -1;
        }
        for (int i3 = 0; i3 < this.locals.length; i3++) {
            this.locals[i3] = -1;
        }
        this.lastAppendPosition = 0;
        this.from = 0;
        this.to = getTextLength();
        return this;
    }

    public Matcher reset(CharSequence charSequence) {
        this.text = charSequence;
        return reset();
    }

    @Override // java.util.regex.MatchResult
    public int start() {
        if (this.first < 0) {
            throw new IllegalStateException("No match available");
        }
        return this.first;
    }

    @Override // java.util.regex.MatchResult
    public int start(int i2) {
        if (this.first < 0) {
            throw new IllegalStateException("No match available");
        }
        if (i2 < 0 || i2 > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + i2);
        }
        return this.groups[i2 * 2];
    }

    public int start(String str) {
        return this.groups[getMatchedGroupIndex(str) * 2];
    }

    @Override // java.util.regex.MatchResult
    public int end() {
        if (this.first < 0) {
            throw new IllegalStateException("No match available");
        }
        return this.last;
    }

    @Override // java.util.regex.MatchResult
    public int end(int i2) {
        if (this.first < 0) {
            throw new IllegalStateException("No match available");
        }
        if (i2 < 0 || i2 > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + i2);
        }
        return this.groups[(i2 * 2) + 1];
    }

    public int end(String str) {
        return this.groups[(getMatchedGroupIndex(str) * 2) + 1];
    }

    @Override // java.util.regex.MatchResult
    public String group() {
        return group(0);
    }

    @Override // java.util.regex.MatchResult
    public String group(int i2) {
        if (this.first < 0) {
            throw new IllegalStateException("No match found");
        }
        if (i2 < 0 || i2 > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + i2);
        }
        if (this.groups[i2 * 2] == -1 || this.groups[(i2 * 2) + 1] == -1) {
            return null;
        }
        return getSubSequence(this.groups[i2 * 2], this.groups[(i2 * 2) + 1]).toString();
    }

    public String group(String str) {
        int matchedGroupIndex = getMatchedGroupIndex(str);
        if (this.groups[matchedGroupIndex * 2] == -1 || this.groups[(matchedGroupIndex * 2) + 1] == -1) {
            return null;
        }
        return getSubSequence(this.groups[matchedGroupIndex * 2], this.groups[(matchedGroupIndex * 2) + 1]).toString();
    }

    @Override // java.util.regex.MatchResult
    public int groupCount() {
        return this.parentPattern.capturingGroupCount - 1;
    }

    public boolean matches() {
        return match(this.from, 1);
    }

    public boolean find() {
        int i2 = this.last;
        if (i2 == this.first) {
            i2++;
        }
        if (i2 < this.from) {
            i2 = this.from;
        }
        if (i2 > this.to) {
            for (int i3 = 0; i3 < this.groups.length; i3++) {
                this.groups[i3] = -1;
            }
            return false;
        }
        return search(i2);
    }

    public boolean find(int i2) {
        int textLength = getTextLength();
        if (i2 < 0 || i2 > textLength) {
            throw new IndexOutOfBoundsException("Illegal start index");
        }
        reset();
        return search(i2);
    }

    public boolean lookingAt() {
        return match(this.from, 0);
    }

    public static String quoteReplacement(String str) {
        if (str.indexOf(92) == -1 && str.indexOf(36) == -1) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\\' || cCharAt == '$') {
                sb.append('\\');
            }
            sb.append(cCharAt);
        }
        return sb.toString();
    }

    public Matcher appendReplacement(StringBuffer stringBuffer, String str) {
        int iIntValue;
        int iCharAt;
        if (this.first < 0) {
            throw new IllegalStateException("No match available");
        }
        int i2 = 0;
        StringBuilder sb = new StringBuilder();
        while (i2 < str.length()) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\\') {
                int i3 = i2 + 1;
                if (i3 == str.length()) {
                    throw new IllegalArgumentException("character to be escaped is missing");
                }
                sb.append(str.charAt(i3));
                i2 = i3 + 1;
            } else if (cCharAt == '$') {
                int i4 = i2 + 1;
                if (i4 == str.length()) {
                    throw new IllegalArgumentException("Illegal group reference: group index is missing");
                }
                char cCharAt2 = str.charAt(i4);
                if (cCharAt2 == '{') {
                    int i5 = i4 + 1;
                    StringBuilder sb2 = new StringBuilder();
                    while (i5 < str.length()) {
                        cCharAt2 = str.charAt(i5);
                        if (!ASCII.isLower(cCharAt2) && !ASCII.isUpper(cCharAt2) && !ASCII.isDigit(cCharAt2)) {
                            break;
                        }
                        sb2.append(cCharAt2);
                        i5++;
                    }
                    if (sb2.length() == 0) {
                        throw new IllegalArgumentException("named capturing group has 0 length name");
                    }
                    if (cCharAt2 != '}') {
                        throw new IllegalArgumentException("named capturing group is missing trailing '}'");
                    }
                    String string = sb2.toString();
                    if (ASCII.isDigit(string.charAt(0))) {
                        throw new IllegalArgumentException("capturing group name {" + string + "} starts with digit character");
                    }
                    if (!this.parentPattern.namedGroups().containsKey(string)) {
                        throw new IllegalArgumentException("No group with name {" + string + "}");
                    }
                    iIntValue = this.parentPattern.namedGroups().get(string).intValue();
                    i2 = i5 + 1;
                } else {
                    iIntValue = cCharAt2 - '0';
                    if (iIntValue < 0 || iIntValue > 9) {
                        throw new IllegalArgumentException("Illegal group reference");
                    }
                    i2 = i4 + 1;
                    boolean z2 = false;
                    while (!z2 && i2 < str.length() && (iCharAt = str.charAt(i2) - '0') >= 0 && iCharAt <= 9) {
                        int i6 = (iIntValue * 10) + iCharAt;
                        if (groupCount() < i6) {
                            z2 = true;
                        } else {
                            iIntValue = i6;
                            i2++;
                        }
                    }
                }
                if (start(iIntValue) != -1 && end(iIntValue) != -1) {
                    sb.append(this.text, start(iIntValue), end(iIntValue));
                }
            } else {
                sb.append(cCharAt);
                i2++;
            }
        }
        stringBuffer.append(this.text, this.lastAppendPosition, this.first);
        stringBuffer.append((CharSequence) sb);
        this.lastAppendPosition = this.last;
        return this;
    }

    public StringBuffer appendTail(StringBuffer stringBuffer) {
        stringBuffer.append(this.text, this.lastAppendPosition, getTextLength());
        return stringBuffer;
    }

    public String replaceAll(String str) {
        reset();
        if (find()) {
            StringBuffer stringBuffer = new StringBuffer();
            do {
                appendReplacement(stringBuffer, str);
            } while (find());
            appendTail(stringBuffer);
            return stringBuffer.toString();
        }
        return this.text.toString();
    }

    public String replaceFirst(String str) {
        if (str == null) {
            throw new NullPointerException("replacement");
        }
        reset();
        if (!find()) {
            return this.text.toString();
        }
        StringBuffer stringBuffer = new StringBuffer();
        appendReplacement(stringBuffer, str);
        appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public Matcher region(int i2, int i3) {
        if (i2 < 0 || i2 > getTextLength()) {
            throw new IndexOutOfBoundsException("start");
        }
        if (i3 < 0 || i3 > getTextLength()) {
            throw new IndexOutOfBoundsException(AsmConstants.END);
        }
        if (i2 > i3) {
            throw new IndexOutOfBoundsException("start > end");
        }
        reset();
        this.from = i2;
        this.to = i3;
        return this;
    }

    public int regionStart() {
        return this.from;
    }

    public int regionEnd() {
        return this.to;
    }

    public boolean hasTransparentBounds() {
        return this.transparentBounds;
    }

    public Matcher useTransparentBounds(boolean z2) {
        this.transparentBounds = z2;
        return this;
    }

    public boolean hasAnchoringBounds() {
        return this.anchoringBounds;
    }

    public Matcher useAnchoringBounds(boolean z2) {
        this.anchoringBounds = z2;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("java.util.regex.Matcher");
        sb.append("[pattern=" + ((Object) pattern()));
        sb.append(" region=");
        sb.append(regionStart() + "," + regionEnd());
        sb.append(" lastmatch=");
        if (this.first >= 0 && group() != null) {
            sb.append(group());
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean hitEnd() {
        return this.hitEnd;
    }

    public boolean requireEnd() {
        return this.requireEnd;
    }

    boolean search(int i2) {
        this.hitEnd = false;
        this.requireEnd = false;
        int i3 = i2 < 0 ? 0 : i2;
        this.first = i3;
        this.oldLast = this.oldLast < 0 ? i3 : this.oldLast;
        for (int i4 = 0; i4 < this.groups.length; i4++) {
            this.groups[i4] = -1;
        }
        this.acceptMode = 0;
        boolean zMatch = this.parentPattern.root.match(this, i3, this.text);
        if (!zMatch) {
            this.first = -1;
        }
        this.oldLast = this.last;
        return zMatch;
    }

    boolean match(int i2, int i3) {
        this.hitEnd = false;
        this.requireEnd = false;
        int i4 = i2 < 0 ? 0 : i2;
        this.first = i4;
        this.oldLast = this.oldLast < 0 ? i4 : this.oldLast;
        for (int i5 = 0; i5 < this.groups.length; i5++) {
            this.groups[i5] = -1;
        }
        this.acceptMode = i3;
        boolean zMatch = this.parentPattern.matchRoot.match(this, i4, this.text);
        if (!zMatch) {
            this.first = -1;
        }
        this.oldLast = this.last;
        return zMatch;
    }

    int getTextLength() {
        return this.text.length();
    }

    CharSequence getSubSequence(int i2, int i3) {
        return this.text.subSequence(i2, i3);
    }

    char charAt(int i2) {
        return this.text.charAt(i2);
    }

    int getMatchedGroupIndex(String str) {
        Objects.requireNonNull(str, "Group name");
        if (this.first < 0) {
            throw new IllegalStateException("No match found");
        }
        if (!this.parentPattern.namedGroups().containsKey(str)) {
            throw new IllegalArgumentException("No group with name <" + str + ">");
        }
        return this.parentPattern.namedGroups().get(str).intValue();
    }
}
