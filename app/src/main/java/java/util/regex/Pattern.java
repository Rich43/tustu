package java.util.regex;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.Character;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;
import org.icepdf.core.pobjects.graphics.SoftMask;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/util/regex/Pattern.class */
public final class Pattern implements Serializable {
    public static final int UNIX_LINES = 1;
    public static final int CASE_INSENSITIVE = 2;
    public static final int COMMENTS = 4;
    public static final int MULTILINE = 8;
    public static final int LITERAL = 16;
    public static final int DOTALL = 32;
    public static final int UNICODE_CASE = 64;
    public static final int CANON_EQ = 128;
    public static final int UNICODE_CHARACTER_CLASS = 256;
    private static final long serialVersionUID = 5073258162644648461L;
    private String pattern;
    private int flags;
    private volatile transient boolean compiled = false;
    private transient String normalizedPattern;
    transient Node root;
    transient Node matchRoot;
    transient int[] buffer;
    volatile transient Map<String, Integer> namedGroups;
    transient GroupHead[] groupNodes;
    private transient int[] temp;
    transient int capturingGroupCount;
    transient int localCount;
    private transient int cursor;
    private transient int patternLength;
    private transient boolean hasSupplementary;
    static final int MAX_REPS = Integer.MAX_VALUE;
    static final int GREEDY = 0;
    static final int LAZY = 1;
    static final int POSSESSIVE = 2;
    static final int INDEPENDENT = 3;
    static Node lookbehindEnd;
    static Node accept;
    static Node lastAccept;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Pattern.class.desiredAssertionStatus();
        lookbehindEnd = new Node() { // from class: java.util.regex.Pattern.4
            @Override // java.util.regex.Pattern.Node
            boolean match(Matcher matcher, int i2, CharSequence charSequence) {
                return i2 == matcher.lookbehindTo;
            }
        };
        accept = new Node();
        lastAccept = new LastNode();
    }

    public static Pattern compile(String str) {
        return new Pattern(str, 0);
    }

    public static Pattern compile(String str, int i2) {
        return new Pattern(str, i2);
    }

    public String pattern() {
        return this.pattern;
    }

    public String toString() {
        return this.pattern;
    }

    public Matcher matcher(CharSequence charSequence) {
        if (!this.compiled) {
            synchronized (this) {
                if (!this.compiled) {
                    compile();
                }
            }
        }
        return new Matcher(this, charSequence);
    }

    public int flags() {
        return this.flags;
    }

    public static boolean matches(String str, CharSequence charSequence) {
        return compile(str).matcher(charSequence).matches();
    }

    public String[] split(CharSequence charSequence, int i2) {
        int iEnd = 0;
        boolean z2 = i2 > 0;
        ArrayList arrayList = new ArrayList();
        Matcher matcher = matcher(charSequence);
        while (matcher.find()) {
            if (!z2 || arrayList.size() < i2 - 1) {
                if (iEnd != 0 || iEnd != matcher.start() || matcher.start() != matcher.end()) {
                    arrayList.add(charSequence.subSequence(iEnd, matcher.start()).toString());
                    iEnd = matcher.end();
                }
            } else if (arrayList.size() == i2 - 1) {
                arrayList.add(charSequence.subSequence(iEnd, charSequence.length()).toString());
                iEnd = matcher.end();
            }
        }
        if (iEnd == 0) {
            return new String[]{charSequence.toString()};
        }
        if (!z2 || arrayList.size() < i2) {
            arrayList.add(charSequence.subSequence(iEnd, charSequence.length()).toString());
        }
        int size = arrayList.size();
        if (i2 == 0) {
            while (size > 0 && ((String) arrayList.get(size - 1)).equals("")) {
                size--;
            }
        }
        return (String[]) arrayList.subList(0, size).toArray(new String[size]);
    }

    public String[] split(CharSequence charSequence) {
        return split(charSequence, 0);
    }

    public static String quote(String str) {
        if (str.indexOf("\\E") == -1) {
            return "\\Q" + str + "\\E";
        }
        StringBuilder sb = new StringBuilder(str.length() * 2);
        sb.append("\\Q");
        int i2 = 0;
        while (true) {
            int iIndexOf = str.indexOf("\\E", i2);
            if (iIndexOf != -1) {
                sb.append(str.substring(i2, iIndexOf));
                i2 = iIndexOf + 2;
                sb.append("\\E\\\\E\\Q");
            } else {
                sb.append(str.substring(i2, str.length()));
                sb.append("\\E");
                return sb.toString();
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.capturingGroupCount = 1;
        this.localCount = 0;
        this.compiled = false;
        if (this.pattern.length() == 0) {
            this.root = new Start(lastAccept);
            this.matchRoot = lastAccept;
            this.compiled = true;
        }
    }

    private Pattern(String str, int i2) {
        this.pattern = str;
        this.flags = i2;
        if ((this.flags & 256) != 0) {
            this.flags |= 64;
        }
        this.capturingGroupCount = 1;
        this.localCount = 0;
        if (this.pattern.length() > 0) {
            try {
                compile();
            } catch (StackOverflowError e2) {
                throw error("Stack overflow during pattern compilation");
            }
        } else {
            this.root = new Start(lastAccept);
            this.matchRoot = lastAccept;
        }
    }

    private void normalize() {
        int i2 = -1;
        this.normalizedPattern = Normalizer.normalize(this.pattern, Normalizer.Form.NFD);
        this.patternLength = this.normalizedPattern.length();
        StringBuilder sb = new StringBuilder(this.patternLength);
        int iCharCount = 0;
        while (true) {
            int iNormalizeCharClass = iCharCount;
            if (iNormalizeCharClass < this.patternLength) {
                int iCodePointAt = this.normalizedPattern.codePointAt(iNormalizeCharClass);
                if (Character.getType(iCodePointAt) == 6 && i2 != -1) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.appendCodePoint(i2);
                    sb2.appendCodePoint(iCodePointAt);
                    while (Character.getType(iCodePointAt) == 6) {
                        iNormalizeCharClass += Character.charCount(iCodePointAt);
                        if (iNormalizeCharClass >= this.patternLength) {
                            break;
                        }
                        iCodePointAt = this.normalizedPattern.codePointAt(iNormalizeCharClass);
                        sb2.appendCodePoint(iCodePointAt);
                    }
                    String strProduceEquivalentAlternation = produceEquivalentAlternation(sb2.toString());
                    sb.setLength(sb.length() - Character.charCount(i2));
                    sb.append("(?:").append(strProduceEquivalentAlternation).append(")");
                } else if (iCodePointAt == 91 && i2 != 92) {
                    iNormalizeCharClass = normalizeCharClass(sb, iNormalizeCharClass);
                } else {
                    sb.appendCodePoint(iCodePointAt);
                }
                i2 = iCodePointAt;
                iCharCount = iNormalizeCharClass + Character.charCount(iCodePointAt);
            } else {
                this.normalizedPattern = sb.toString();
                return;
            }
        }
    }

    private int normalizeCharClass(StringBuilder sb, int i2) {
        String string;
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = null;
        int i3 = -1;
        int iCharCount = i2 + 1;
        if (iCharCount == this.normalizedPattern.length()) {
            throw error("Unclosed character class");
        }
        sb2.append("[");
        while (true) {
            int iCodePointAt = this.normalizedPattern.codePointAt(iCharCount);
            if (iCodePointAt == 93 && i3 != 92) {
                sb2.append((char) iCodePointAt);
                if (sb3 != null) {
                    string = "(?:" + sb2.toString() + sb3.toString() + ")";
                } else {
                    string = sb2.toString();
                }
                sb.append(string);
                return iCharCount;
            }
            if (Character.getType(iCodePointAt) == 6) {
                StringBuilder sb4 = new StringBuilder();
                sb4.appendCodePoint(i3);
                while (Character.getType(iCodePointAt) == 6) {
                    sb4.appendCodePoint(iCodePointAt);
                    iCharCount += Character.charCount(iCodePointAt);
                    if (iCharCount >= this.normalizedPattern.length()) {
                        break;
                    }
                    iCodePointAt = this.normalizedPattern.codePointAt(iCharCount);
                }
                String strProduceEquivalentAlternation = produceEquivalentAlternation(sb4.toString());
                sb2.setLength(sb2.length() - Character.charCount(i3));
                if (sb3 == null) {
                    sb3 = new StringBuilder();
                }
                sb3.append('|');
                sb3.append(strProduceEquivalentAlternation);
            } else {
                sb2.appendCodePoint(iCodePointAt);
                iCharCount++;
            }
            if (iCharCount == this.normalizedPattern.length()) {
                throw error("Unclosed character class");
            }
            i3 = iCodePointAt;
        }
    }

    private String produceEquivalentAlternation(String str) {
        int iCountChars = countChars(str, 0, 1);
        if (str.length() == iCountChars) {
            return str;
        }
        String strSubstring = str.substring(0, iCountChars);
        String[] strArrProducePermutations = producePermutations(str.substring(iCountChars));
        StringBuilder sb = new StringBuilder(str);
        for (int i2 = 0; i2 < strArrProducePermutations.length; i2++) {
            String str2 = strSubstring + strArrProducePermutations[i2];
            if (i2 > 0) {
                sb.append(CallSiteDescriptor.OPERATOR_DELIMITER + str2);
            }
            String strComposeOneStep = composeOneStep(str2);
            if (strComposeOneStep != null) {
                sb.append(CallSiteDescriptor.OPERATOR_DELIMITER + produceEquivalentAlternation(strComposeOneStep));
            }
        }
        return sb.toString();
    }

    private String[] producePermutations(String str) {
        if (str.length() == countChars(str, 0, 1)) {
            return new String[]{str};
        }
        if (str.length() == countChars(str, 0, 2)) {
            int iCodePointAt = Character.codePointAt(str, 0);
            int iCodePointAt2 = Character.codePointAt(str, Character.charCount(iCodePointAt));
            if (getClass(iCodePointAt2) == getClass(iCodePointAt)) {
                return new String[]{str};
            }
            StringBuilder sb = new StringBuilder(2);
            sb.appendCodePoint(iCodePointAt2);
            sb.appendCodePoint(iCodePointAt);
            return new String[]{str, sb.toString()};
        }
        int i2 = 1;
        int iCountCodePoints = countCodePoints(str);
        for (int i3 = 1; i3 < iCountCodePoints; i3++) {
            i2 *= i3 + 1;
        }
        String[] strArr = new String[i2];
        int[] iArr = new int[iCountCodePoints];
        int iCharCount = 0;
        for (int i4 = 0; i4 < iCountCodePoints; i4++) {
            int iCodePointAt3 = Character.codePointAt(str, iCharCount);
            iArr[i4] = getClass(iCodePointAt3);
            iCharCount += Character.charCount(iCodePointAt3);
        }
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (true) {
            int i8 = i7;
            if (i6 >= iCountCodePoints) {
                break;
            }
            int iCountChars = countChars(str, i8, 1);
            int i9 = i6 - 1;
            while (true) {
                if (i9 >= 0) {
                    if (iArr[i9] == iArr[i6]) {
                        break;
                    }
                    i9--;
                } else {
                    String[] strArrProducePermutations = producePermutations(new StringBuilder(str).delete(i8, i8 + iCountChars).toString());
                    String strSubstring = str.substring(i8, i8 + iCountChars);
                    for (String str2 : strArrProducePermutations) {
                        int i10 = i5;
                        i5++;
                        strArr[i10] = strSubstring + str2;
                    }
                }
            }
            i6++;
            i7 = i8 + iCountChars;
        }
        String[] strArr2 = new String[i5];
        for (int i11 = 0; i11 < i5; i11++) {
            strArr2[i11] = strArr[i11];
        }
        return strArr2;
    }

    private int getClass(int i2) {
        return sun.text.Normalizer.getCombiningClass(i2);
    }

    private String composeOneStep(String str) {
        int iCountChars = countChars(str, 0, 2);
        String strSubstring = str.substring(0, iCountChars);
        String strNormalize = Normalizer.normalize(strSubstring, Normalizer.Form.NFC);
        if (strNormalize.equals(strSubstring)) {
            return null;
        }
        return strNormalize + str.substring(iCountChars);
    }

    private void RemoveQEQuoting() {
        int i2 = this.patternLength;
        int i3 = 0;
        while (i3 < i2 - 1) {
            if (this.temp[i3] != 92) {
                i3++;
            } else if (this.temp[i3 + 1] == 81) {
                break;
            } else {
                i3 += 2;
            }
        }
        if (i3 >= i2 - 1) {
            return;
        }
        int i4 = i3;
        int i5 = i3 + 2;
        int[] iArr = new int[i4 + (3 * (i2 - i5)) + 2];
        System.arraycopy(this.temp, 0, iArr, 0, i4);
        boolean z2 = true;
        boolean z3 = true;
        while (true) {
            boolean z4 = z3;
            if (i5 < i2) {
                int i6 = i5;
                i5++;
                int i7 = this.temp[i6];
                if (!ASCII.isAscii(i7) || ASCII.isAlpha(i7)) {
                    int i8 = i4;
                    i4++;
                    iArr[i8] = i7;
                    z3 = false;
                } else {
                    if (ASCII.isDigit(i7)) {
                        if (z4) {
                            int i9 = i4;
                            int i10 = i4 + 1;
                            iArr[i9] = 92;
                            int i11 = i10 + 1;
                            iArr[i10] = 120;
                            i4 = i11 + 1;
                            iArr[i11] = 51;
                        }
                        int i12 = i4;
                        i4++;
                        iArr[i12] = i7;
                    } else if (i7 != 92) {
                        if (z2) {
                            int i13 = i4;
                            i4++;
                            iArr[i13] = 92;
                        }
                        int i14 = i4;
                        i4++;
                        iArr[i14] = i7;
                    } else if (z2) {
                        if (this.temp[i5] == 69) {
                            i5++;
                            z2 = false;
                        } else {
                            int i15 = i4;
                            int i16 = i4 + 1;
                            iArr[i15] = 92;
                            i4 = i16 + 1;
                            iArr[i16] = 92;
                        }
                    } else if (this.temp[i5] == 81) {
                        i5++;
                        z2 = true;
                        z3 = true;
                    } else {
                        int i17 = i4;
                        i4++;
                        iArr[i17] = i7;
                        if (i5 != i2) {
                            i4++;
                            i5++;
                            iArr[i4] = this.temp[i5];
                        }
                    }
                    z3 = false;
                }
            } else {
                this.patternLength = i4;
                this.temp = Arrays.copyOf(iArr, i4 + 2);
                return;
            }
        }
    }

    private void compile() {
        if (has(128) && !has(16)) {
            normalize();
        } else {
            this.normalizedPattern = this.pattern;
        }
        this.patternLength = this.normalizedPattern.length();
        this.temp = new int[this.patternLength + 2];
        this.hasSupplementary = false;
        int i2 = 0;
        int iCharCount = 0;
        while (true) {
            int i3 = iCharCount;
            if (i3 >= this.patternLength) {
                break;
            }
            int iCodePointAt = this.normalizedPattern.codePointAt(i3);
            if (isSupplementary(iCodePointAt)) {
                this.hasSupplementary = true;
            }
            int i4 = i2;
            i2++;
            this.temp[i4] = iCodePointAt;
            iCharCount = i3 + Character.charCount(iCodePointAt);
        }
        this.patternLength = i2;
        if (!has(16)) {
            RemoveQEQuoting();
        }
        this.buffer = new int[32];
        this.groupNodes = new GroupHead[10];
        this.namedGroups = null;
        if (has(16)) {
            this.matchRoot = newSlice(this.temp, this.patternLength, this.hasSupplementary);
            this.matchRoot.next = lastAccept;
        } else {
            this.matchRoot = expr(lastAccept);
            if (this.patternLength != this.cursor) {
                if (peek() == 41) {
                    throw error("Unmatched closing ')'");
                }
                throw error("Unexpected internal error");
            }
        }
        if (this.matchRoot instanceof Slice) {
            this.root = BnM.optimize(this.matchRoot);
            if (this.root == this.matchRoot) {
                this.root = this.hasSupplementary ? new StartS(this.matchRoot) : new Start(this.matchRoot);
            }
        } else if ((this.matchRoot instanceof Begin) || (this.matchRoot instanceof First)) {
            this.root = this.matchRoot;
        } else {
            this.root = this.hasSupplementary ? new StartS(this.matchRoot) : new Start(this.matchRoot);
        }
        this.temp = null;
        this.buffer = null;
        this.groupNodes = null;
        this.patternLength = 0;
        this.compiled = true;
    }

    Map<String, Integer> namedGroups() {
        if (this.namedGroups == null) {
            this.namedGroups = new HashMap(2);
        }
        return this.namedGroups;
    }

    private static void printObjectTree(Node node) {
        while (node != null) {
            if (node instanceof Prolog) {
                System.out.println(node);
                printObjectTree(((Prolog) node).loop);
                System.out.println("**** end contents prolog loop");
            } else if (node instanceof Loop) {
                System.out.println(node);
                printObjectTree(((Loop) node).body);
                System.out.println("**** end contents Loop body");
            } else if (node instanceof Curly) {
                System.out.println(node);
                printObjectTree(((Curly) node).atom);
                System.out.println("**** end contents Curly body");
            } else if (node instanceof GroupCurly) {
                System.out.println(node);
                printObjectTree(((GroupCurly) node).atom);
                System.out.println("**** end contents GroupCurly body");
            } else {
                if (node instanceof GroupTail) {
                    System.out.println(node);
                    System.out.println("Tail next is " + ((Object) node.next));
                    return;
                }
                System.out.println(node);
            }
            node = node.next;
            if (node != null) {
                System.out.println("->next:");
            }
            if (node == accept) {
                System.out.println("Accept Node");
                node = null;
            }
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$TreeInfo.class */
    static final class TreeInfo {
        int minLength;
        int maxLength;
        boolean maxValid;
        boolean deterministic;

        TreeInfo() {
            reset();
        }

        void reset() {
            this.minLength = 0;
            this.maxLength = 0;
            this.maxValid = true;
            this.deterministic = true;
        }
    }

    private boolean has(int i2) {
        return (this.flags & i2) != 0;
    }

    private void accept(int i2, String str) {
        int[] iArr = this.temp;
        int i3 = this.cursor;
        this.cursor = i3 + 1;
        int pastWhitespace = iArr[i3];
        if (has(4)) {
            pastWhitespace = parsePastWhitespace(pastWhitespace);
        }
        if (i2 != pastWhitespace) {
            throw error(str);
        }
    }

    private void mark(int i2) {
        this.temp[this.patternLength] = i2;
    }

    private int peek() {
        int iPeekPastWhitespace = this.temp[this.cursor];
        if (has(4)) {
            iPeekPastWhitespace = peekPastWhitespace(iPeekPastWhitespace);
        }
        return iPeekPastWhitespace;
    }

    private int read() {
        int[] iArr = this.temp;
        int i2 = this.cursor;
        this.cursor = i2 + 1;
        int pastWhitespace = iArr[i2];
        if (has(4)) {
            pastWhitespace = parsePastWhitespace(pastWhitespace);
        }
        return pastWhitespace;
    }

    private int readEscaped() {
        int[] iArr = this.temp;
        int i2 = this.cursor;
        this.cursor = i2 + 1;
        return iArr[i2];
    }

    private int next() {
        int[] iArr = this.temp;
        int i2 = this.cursor + 1;
        this.cursor = i2;
        int iPeekPastWhitespace = iArr[i2];
        if (has(4)) {
            iPeekPastWhitespace = peekPastWhitespace(iPeekPastWhitespace);
        }
        return iPeekPastWhitespace;
    }

    private int nextEscaped() {
        int[] iArr = this.temp;
        int i2 = this.cursor + 1;
        this.cursor = i2;
        return iArr[i2];
    }

    private int peekPastWhitespace(int i2) {
        while (true) {
            if (ASCII.isSpace(i2) || i2 == 35) {
                while (ASCII.isSpace(i2)) {
                    int[] iArr = this.temp;
                    int i3 = this.cursor + 1;
                    this.cursor = i3;
                    i2 = iArr[i3];
                }
                if (i2 == 35) {
                    i2 = peekPastLine();
                }
            } else {
                return i2;
            }
        }
    }

    private int parsePastWhitespace(int i2) {
        while (true) {
            if (ASCII.isSpace(i2) || i2 == 35) {
                while (ASCII.isSpace(i2)) {
                    int[] iArr = this.temp;
                    int i3 = this.cursor;
                    this.cursor = i3 + 1;
                    i2 = iArr[i3];
                }
                if (i2 == 35) {
                    i2 = parsePastLine();
                }
            } else {
                return i2;
            }
        }
    }

    private int parsePastLine() {
        int i2;
        int[] iArr = this.temp;
        int i3 = this.cursor;
        this.cursor = i3 + 1;
        int i4 = iArr[i3];
        while (true) {
            i2 = i4;
            if (i2 == 0 || isLineSeparator(i2)) {
                break;
            }
            int[] iArr2 = this.temp;
            int i5 = this.cursor;
            this.cursor = i5 + 1;
            i4 = iArr2[i5];
        }
        if (i2 == 0 && this.cursor > this.patternLength) {
            this.cursor = this.patternLength;
            int[] iArr3 = this.temp;
            int i6 = this.cursor;
            this.cursor = i6 + 1;
            i2 = iArr3[i6];
        }
        return i2;
    }

    private int peekPastLine() {
        int i2;
        int[] iArr = this.temp;
        int i3 = this.cursor + 1;
        this.cursor = i3;
        int i4 = iArr[i3];
        while (true) {
            i2 = i4;
            if (i2 == 0 || isLineSeparator(i2)) {
                break;
            }
            int[] iArr2 = this.temp;
            int i5 = this.cursor + 1;
            this.cursor = i5;
            i4 = iArr2[i5];
        }
        if (i2 == 0 && this.cursor > this.patternLength) {
            this.cursor = this.patternLength;
            i2 = this.temp[this.cursor];
        }
        return i2;
    }

    private boolean isLineSeparator(int i2) {
        return has(1) ? i2 == 10 : i2 == 10 || i2 == 13 || (i2 | 1) == 8233 || i2 == 133;
    }

    private int skip() {
        int i2 = this.cursor;
        int i3 = this.temp[i2 + 1];
        this.cursor = i2 + 2;
        return i3;
    }

    private void unread() {
        this.cursor--;
    }

    private PatternSyntaxException error(String str) {
        return new PatternSyntaxException(str, this.normalizedPattern, this.cursor - 1);
    }

    private boolean findSupplementary(int i2, int i3) {
        for (int i4 = i2; i4 < i3; i4++) {
            if (isSupplementary(this.temp[i4])) {
                return true;
            }
        }
        return false;
    }

    private static final boolean isSupplementary(int i2) {
        return i2 >= 65536 || Character.isSurrogate((char) i2);
    }

    private Node expr(Node node) {
        Node node2 = null;
        Node node3 = null;
        Branch branch = null;
        BranchConn branchConn = null;
        while (true) {
            Node nodeSequence = sequence(node);
            Node node4 = this.root;
            if (node2 == null) {
                node2 = nodeSequence;
                node3 = node4;
            } else {
                if (branchConn == null) {
                    branchConn = new BranchConn();
                    branchConn.next = node;
                }
                if (nodeSequence == node) {
                    nodeSequence = null;
                } else {
                    node4.next = branchConn;
                }
                if (node2 == branch) {
                    branch.add(nodeSequence);
                } else {
                    if (node2 == node) {
                        node2 = null;
                    } else {
                        node3.next = branchConn;
                    }
                    Branch branch2 = new Branch(node2, nodeSequence, branchConn);
                    branch = branch2;
                    node2 = branch2;
                }
            }
            if (peek() != 124) {
                return node2;
            }
            next();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:78:0x020b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0203 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.regex.Pattern.Node sequence(java.util.regex.Pattern.Node r6) {
        /*
            Method dump skipped, instructions count: 553
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.regex.Pattern.sequence(java.util.regex.Pattern$Node):java.util.regex.Pattern$Node");
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x00a8, code lost:
    
        if (r6 <= 0) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x00ab, code lost:
    
        unread();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00b6, code lost:
    
        if (r0 != 80) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00b9, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00bd, code lost:
    
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00be, code lost:
    
        r10 = r0;
        r11 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00cd, code lost:
    
        if (next() == 123) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00d0, code lost:
    
        unread();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00d7, code lost:
    
        r11 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00e2, code lost:
    
        return family(r11, r10);
     */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0155  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.regex.Pattern.Node atom() {
        /*
            Method dump skipped, instructions count: 379
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.regex.Pattern.atom():java.util.regex.Pattern$Node");
    }

    private void append(int i2, int i3) {
        if (i3 >= this.buffer.length) {
            int[] iArr = new int[i3 + i3];
            System.arraycopy(this.buffer, 0, iArr, 0, i3);
            this.buffer = iArr;
        }
        this.buffer[i3] = i2;
    }

    private Node ref(int i2) {
        boolean z2 = false;
        while (!z2) {
            int iPeek = peek();
            switch (iPeek) {
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                    int i3 = (i2 * 10) + (iPeek - 48);
                    if (this.capturingGroupCount - 1 < i3) {
                        z2 = true;
                        break;
                    } else {
                        i2 = i3;
                        read();
                        break;
                    }
                default:
                    z2 = true;
                    break;
            }
        }
        if (has(2)) {
            return new CIBackRef(i2, has(64));
        }
        return new BackRef(i2);
    }

    private int escape(boolean z2, boolean z3, boolean z4) {
        CharProperty charPropertyComplement;
        CharProperty charPropertyComplement2;
        CharProperty charPropertyComplement3;
        int iSkip = skip();
        switch (iSkip) {
            case 48:
                return o();
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                if (!z2) {
                    if (z3) {
                        this.root = ref(iSkip - 48);
                        return -1;
                    }
                    return -1;
                }
                break;
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            default:
                return iSkip;
            case 65:
                if (!z2) {
                    if (z3) {
                        this.root = new Begin();
                        return -1;
                    }
                    return -1;
                }
                break;
            case 66:
                if (!z2) {
                    if (z3) {
                        this.root = new Bound(Bound.NONE, has(256));
                        return -1;
                    }
                    return -1;
                }
                break;
            case 67:
            case 69:
            case 70:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 84:
            case 85:
            case 88:
            case 89:
            case 103:
            case 105:
            case 106:
            case 108:
            case 109:
            case 111:
            case 112:
            case 113:
            case 121:
                break;
            case 68:
                if (z3) {
                    if (has(256)) {
                        charPropertyComplement3 = new Utype(UnicodeProp.DIGIT).complement();
                    } else {
                        charPropertyComplement3 = new Ctype(1024).complement();
                    }
                    this.root = charPropertyComplement3;
                    return -1;
                }
                return -1;
            case 71:
                if (!z2) {
                    if (z3) {
                        this.root = new LastMatch();
                        return -1;
                    }
                    return -1;
                }
                break;
            case 72:
                if (z3) {
                    this.root = new HorizWS().complement();
                    return -1;
                }
                return -1;
            case 82:
                if (!z2) {
                    if (z3) {
                        this.root = new LineEnding();
                        return -1;
                    }
                    return -1;
                }
                break;
            case 83:
                if (z3) {
                    if (has(256)) {
                        charPropertyComplement2 = new Utype(UnicodeProp.WHITE_SPACE).complement();
                    } else {
                        charPropertyComplement2 = new Ctype(2048).complement();
                    }
                    this.root = charPropertyComplement2;
                    return -1;
                }
                return -1;
            case 86:
                if (z3) {
                    this.root = new VertWS().complement();
                    return -1;
                }
                return -1;
            case 87:
                if (z3) {
                    if (has(256)) {
                        charPropertyComplement = new Utype(UnicodeProp.WORD).complement();
                    } else {
                        charPropertyComplement = new Ctype(67328).complement();
                    }
                    this.root = charPropertyComplement;
                    return -1;
                }
                return -1;
            case 90:
                if (!z2) {
                    if (z3) {
                        if (has(1)) {
                            this.root = new UnixDollar(false);
                            return -1;
                        }
                        this.root = new Dollar(false);
                        return -1;
                    }
                    return -1;
                }
                break;
            case 97:
                return 7;
            case 98:
                if (!z2) {
                    if (z3) {
                        this.root = new Bound(Bound.BOTH, has(256));
                        return -1;
                    }
                    return -1;
                }
                break;
            case 99:
                return c();
            case 100:
                if (z3) {
                    this.root = has(256) ? new Utype(UnicodeProp.DIGIT) : new Ctype(1024);
                    return -1;
                }
                return -1;
            case 101:
                return 27;
            case 102:
                return 12;
            case 104:
                if (z3) {
                    this.root = new HorizWS();
                    return -1;
                }
                return -1;
            case 107:
                if (!z2) {
                    if (read() != 60) {
                        throw error("\\k is not followed by '<' for named capturing group");
                    }
                    String strGroupname = groupname(read());
                    if (!namedGroups().containsKey(strGroupname)) {
                        throw error("(named capturing group <" + strGroupname + "> does not exit");
                    }
                    if (z3) {
                        if (has(2)) {
                            this.root = new CIBackRef(namedGroups().get(strGroupname).intValue(), has(64));
                            return -1;
                        }
                        this.root = new BackRef(namedGroups().get(strGroupname).intValue());
                        return -1;
                    }
                    return -1;
                }
                break;
            case 110:
                return 10;
            case 114:
                return 13;
            case 115:
                if (z3) {
                    this.root = has(256) ? new Utype(UnicodeProp.WHITE_SPACE) : new Ctype(2048);
                    return -1;
                }
                return -1;
            case 116:
                return 9;
            case 117:
                return u();
            case 118:
                if (z4) {
                    return 11;
                }
                if (z3) {
                    this.root = new VertWS();
                    return -1;
                }
                return -1;
            case 119:
                if (z3) {
                    this.root = has(256) ? new Utype(UnicodeProp.WORD) : new Ctype(67328);
                    return -1;
                }
                return -1;
            case 120:
                return x();
            case 122:
                if (!z2) {
                    if (z3) {
                        this.root = new End();
                        return -1;
                    }
                    return -1;
                }
                break;
        }
        throw error("Illegal/unsupported escape sequence");
    }

    private CharProperty clazz(boolean z2) {
        CharProperty charPropertyClazz;
        CharProperty charPropertyUnion;
        CharProperty difference = null;
        CharProperty charPropertyRange = null;
        BitClass bitClass = new BitClass();
        boolean z3 = true;
        boolean z4 = true;
        int next = next();
        while (true) {
            switch (next) {
                case 0:
                    z4 = false;
                    if (this.cursor >= this.patternLength) {
                        throw error("Unclosed character class");
                    }
                    break;
                case 38:
                    z4 = false;
                    if (next() == 38) {
                        next = next();
                        CharProperty charProperty = null;
                        while (next != 93 && next != 38) {
                            if (next == 91) {
                                if (charProperty == null) {
                                    charPropertyClazz = clazz(true);
                                } else {
                                    charPropertyClazz = union(charProperty, clazz(true));
                                }
                            } else {
                                unread();
                                charPropertyClazz = clazz(false);
                            }
                            charProperty = charPropertyClazz;
                            next = peek();
                        }
                        if (charProperty != null) {
                            charPropertyRange = charProperty;
                        }
                        if (difference == null) {
                            if (charProperty == null) {
                                throw error("Bad class syntax");
                            }
                            difference = charProperty;
                        } else {
                            difference = intersection(difference, charPropertyRange);
                        }
                    } else {
                        unread();
                        break;
                    }
                case 91:
                    z4 = false;
                    charPropertyRange = clazz(true);
                    if (difference == null) {
                        charPropertyUnion = charPropertyRange;
                    } else {
                        charPropertyUnion = union(difference, charPropertyRange);
                    }
                    difference = charPropertyUnion;
                    next = peek();
                    continue;
                case 93:
                    z4 = false;
                    if (difference != null) {
                        if (z2) {
                            next();
                        }
                        return difference;
                    }
                    break;
                case 94:
                    if (!z4 || this.temp[this.cursor - 1] != 91) {
                        break;
                    } else {
                        next = next();
                        z3 = !z3;
                    }
                    break;
                default:
                    z4 = false;
                    break;
            }
            charPropertyRange = range(bitClass);
            if (z3) {
                if (difference == null) {
                    difference = charPropertyRange;
                } else if (difference != charPropertyRange) {
                    difference = union(difference, charPropertyRange);
                }
            } else if (difference == null) {
                difference = charPropertyRange.complement();
            } else if (difference != charPropertyRange) {
                difference = setDifference(difference, charPropertyRange);
            }
            next = peek();
        }
    }

    private CharProperty bitsOrSingle(BitClass bitClass, int i2) {
        if (i2 < 256 && (!has(2) || !has(64) || (i2 != 255 && i2 != 181 && i2 != 73 && i2 != 105 && i2 != 83 && i2 != 115 && i2 != 75 && i2 != 107 && i2 != 197 && i2 != 229))) {
            return bitClass.add(i2, flags());
        }
        return newSingle(i2);
    }

    private CharProperty range(BitClass bitClass) {
        int iPeek = peek();
        if (iPeek == 92) {
            int iNextEscaped = nextEscaped();
            if (iNextEscaped == 112 || iNextEscaped == 80) {
                boolean z2 = iNextEscaped == 80;
                boolean z3 = true;
                if (next() != 123) {
                    unread();
                } else {
                    z3 = false;
                }
                return family(z3, z2);
            }
            boolean z4 = this.temp[this.cursor + 1] == 45;
            unread();
            iPeek = escape(true, true, z4);
            if (iPeek == -1) {
                return (CharProperty) this.root;
            }
        } else {
            next();
        }
        if (iPeek >= 0) {
            if (peek() == 45) {
                int i2 = this.temp[this.cursor + 1];
                if (i2 == 91) {
                    return bitsOrSingle(bitClass, iPeek);
                }
                if (i2 != 93) {
                    next();
                    int iPeek2 = peek();
                    if (iPeek2 == 92) {
                        iPeek2 = escape(true, false, true);
                    } else {
                        next();
                    }
                    if (iPeek2 < iPeek) {
                        throw error("Illegal character range");
                    }
                    if (has(2)) {
                        return caseInsensitiveRangeFor(iPeek, iPeek2);
                    }
                    return rangeFor(iPeek, iPeek2);
                }
            }
            return bitsOrSingle(bitClass, iPeek);
        }
        throw error("Unexpected character '" + ((char) iPeek) + PdfOps.SINGLE_QUOTE_TOKEN);
    }

    private CharProperty family(boolean z2, boolean z3) {
        String str;
        UnicodeProp unicodePropForPOSIXName;
        next();
        CharProperty charPropertyCharPropertyNodeFor = null;
        if (z2) {
            int i2 = this.temp[this.cursor];
            if (!Character.isSupplementaryCodePoint(i2)) {
                str = String.valueOf((char) i2);
            } else {
                str = new String(this.temp, this.cursor, 1);
            }
            read();
        } else {
            int i3 = this.cursor;
            mark(125);
            while (read() != 125) {
            }
            mark(0);
            int i4 = this.cursor;
            if (i4 > this.patternLength) {
                throw error("Unclosed character family");
            }
            if (i3 + 1 >= i4) {
                throw error("Empty character family");
            }
            str = new String(this.temp, i3, (i4 - i3) - 1);
        }
        int iIndexOf = str.indexOf(61);
        if (iIndexOf != -1) {
            String strSubstring = str.substring(iIndexOf + 1);
            String lowerCase = str.substring(0, iIndexOf).toLowerCase(Locale.ENGLISH);
            if (PdfOps.sc_TOKEN.equals(lowerCase) || "script".equals(lowerCase)) {
                charPropertyCharPropertyNodeFor = unicodeScriptPropertyFor(strSubstring);
            } else if ("blk".equals(lowerCase) || "block".equals(lowerCase)) {
                charPropertyCharPropertyNodeFor = unicodeBlockPropertyFor(strSubstring);
            } else if ("gc".equals(lowerCase) || "general_category".equals(lowerCase)) {
                charPropertyCharPropertyNodeFor = charPropertyNodeFor(strSubstring);
            } else {
                throw error("Unknown Unicode property {name=<" + lowerCase + ">, value=<" + strSubstring + ">}");
            }
        } else if (str.startsWith("In")) {
            charPropertyCharPropertyNodeFor = unicodeBlockPropertyFor(str.substring(2));
        } else if (str.startsWith("Is")) {
            String strSubstring2 = str.substring(2);
            UnicodeProp unicodePropForName = UnicodeProp.forName(strSubstring2);
            if (unicodePropForName != null) {
                charPropertyCharPropertyNodeFor = new Utype(unicodePropForName);
            }
            if (charPropertyCharPropertyNodeFor == null) {
                charPropertyCharPropertyNodeFor = CharPropertyNames.charPropertyFor(strSubstring2);
            }
            if (charPropertyCharPropertyNodeFor == null) {
                charPropertyCharPropertyNodeFor = unicodeScriptPropertyFor(strSubstring2);
            }
        } else {
            if (has(256) && (unicodePropForPOSIXName = UnicodeProp.forPOSIXName(str)) != null) {
                charPropertyCharPropertyNodeFor = new Utype(unicodePropForPOSIXName);
            }
            if (charPropertyCharPropertyNodeFor == null) {
                charPropertyCharPropertyNodeFor = charPropertyNodeFor(str);
            }
        }
        if (z3) {
            if ((charPropertyCharPropertyNodeFor instanceof Category) || (charPropertyCharPropertyNodeFor instanceof Block)) {
                this.hasSupplementary = true;
            }
            charPropertyCharPropertyNodeFor = charPropertyCharPropertyNodeFor.complement();
        }
        return charPropertyCharPropertyNodeFor;
    }

    private CharProperty unicodeScriptPropertyFor(String str) {
        try {
            return new Script(Character.UnicodeScript.forName(str));
        } catch (IllegalArgumentException e2) {
            throw error("Unknown character script name {" + str + "}");
        }
    }

    private CharProperty unicodeBlockPropertyFor(String str) {
        try {
            return new Block(Character.UnicodeBlock.forName(str));
        } catch (IllegalArgumentException e2) {
            throw error("Unknown character block name {" + str + "}");
        }
    }

    private CharProperty charPropertyNodeFor(String str) {
        CharProperty charPropertyCharPropertyFor = CharPropertyNames.charPropertyFor(str);
        if (charPropertyCharPropertyFor == null) {
            throw error("Unknown character property name {" + str + "}");
        }
        return charPropertyCharPropertyFor;
    }

    private String groupname(int i2) {
        int i3;
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toChars(i2));
        while (true) {
            i3 = read();
            if (!ASCII.isLower(i3) && !ASCII.isUpper(i3) && !ASCII.isDigit(i3)) {
                break;
            }
            sb.append(Character.toChars(i3));
        }
        if (sb.length() == 0) {
            throw error("named capturing group has 0 length name");
        }
        if (i3 != 62) {
            throw error("named capturing group is missing trailing '>'");
        }
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v48, types: [java.util.regex.Pattern$Loop] */
    private Node group0() {
        Node nodeCreateGroup;
        Node node;
        LazyLoop lazyLoop;
        Branch branch;
        boolean z2 = false;
        int i2 = this.flags;
        this.root = null;
        if (next() == 63) {
            int iSkip = skip();
            switch (iSkip) {
                case 33:
                case 61:
                    Node nodeCreateGroup2 = createGroup(true);
                    nodeCreateGroup2.next = expr(this.root);
                    if (iSkip == 61) {
                        Pos pos = new Pos(nodeCreateGroup2);
                        node = pos;
                        nodeCreateGroup = pos;
                        break;
                    } else {
                        Neg neg = new Neg(nodeCreateGroup2);
                        node = neg;
                        nodeCreateGroup = neg;
                        break;
                    }
                case 36:
                case 64:
                    throw error("Unknown group type");
                case 58:
                    nodeCreateGroup = createGroup(true);
                    node = this.root;
                    nodeCreateGroup.next = expr(node);
                    break;
                case 60:
                    int i3 = read();
                    if (ASCII.isLower(i3) || ASCII.isUpper(i3)) {
                        String strGroupname = groupname(i3);
                        if (namedGroups().containsKey(strGroupname)) {
                            throw error("Named capturing group <" + strGroupname + "> is already defined");
                        }
                        z2 = true;
                        nodeCreateGroup = createGroup(false);
                        node = this.root;
                        namedGroups().put(strGroupname, Integer.valueOf(this.capturingGroupCount - 1));
                        nodeCreateGroup.next = expr(node);
                        break;
                    } else {
                        int i4 = this.cursor;
                        Node nodeCreateGroup3 = createGroup(true);
                        Node node2 = this.root;
                        nodeCreateGroup3.next = expr(node2);
                        node2.next = lookbehindEnd;
                        TreeInfo treeInfo = new TreeInfo();
                        nodeCreateGroup3.study(treeInfo);
                        if (!treeInfo.maxValid) {
                            throw error("Look-behind group does not have an obvious maximum length");
                        }
                        boolean zFindSupplementary = findSupplementary(i4, this.patternLength);
                        if (i3 == 61) {
                            Node behindS = zFindSupplementary ? new BehindS(nodeCreateGroup3, treeInfo.maxLength, treeInfo.minLength) : new Behind(nodeCreateGroup3, treeInfo.maxLength, treeInfo.minLength);
                            node = behindS;
                            nodeCreateGroup = behindS;
                            break;
                        } else if (i3 == 33) {
                            Node notBehindS = zFindSupplementary ? new NotBehindS(nodeCreateGroup3, treeInfo.maxLength, treeInfo.minLength) : new NotBehind(nodeCreateGroup3, treeInfo.maxLength, treeInfo.minLength);
                            node = notBehindS;
                            nodeCreateGroup = notBehindS;
                            break;
                        } else {
                            throw error("Unknown look-behind group");
                        }
                    }
                    break;
                case 62:
                    Node nodeCreateGroup4 = createGroup(true);
                    nodeCreateGroup4.next = expr(this.root);
                    Ques ques = new Ques(nodeCreateGroup4, 3);
                    node = ques;
                    nodeCreateGroup = ques;
                    break;
                default:
                    unread();
                    addFlag();
                    int i5 = read();
                    if (i5 == 41) {
                        return null;
                    }
                    if (i5 != 58) {
                        throw error("Unknown inline modifier");
                    }
                    nodeCreateGroup = createGroup(true);
                    node = this.root;
                    nodeCreateGroup.next = expr(node);
                    break;
            }
        } else {
            z2 = true;
            nodeCreateGroup = createGroup(false);
            node = this.root;
            nodeCreateGroup.next = expr(node);
        }
        accept(41, "Unclosed group");
        this.flags = i2;
        Node nodeClosure = closure(nodeCreateGroup);
        if (nodeClosure == nodeCreateGroup) {
            this.root = node;
            return nodeClosure;
        }
        if (nodeCreateGroup == node) {
            this.root = nodeClosure;
            return nodeClosure;
        }
        if (nodeClosure instanceof Ques) {
            Ques ques2 = (Ques) nodeClosure;
            if (ques2.type == 2) {
                this.root = nodeClosure;
                return nodeClosure;
            }
            node.next = new BranchConn();
            Node node3 = node.next;
            if (ques2.type == 0) {
                branch = new Branch(nodeCreateGroup, null, node3);
            } else {
                branch = new Branch(null, nodeCreateGroup, node3);
            }
            this.root = node3;
            return branch;
        }
        if (nodeClosure instanceof Curly) {
            Curly curly = (Curly) nodeClosure;
            if (curly.type == 2) {
                this.root = nodeClosure;
                return nodeClosure;
            }
            if (nodeCreateGroup.study(new TreeInfo())) {
                GroupCurly groupCurly = new GroupCurly(nodeCreateGroup.next, curly.cmin, curly.cmax, curly.type, ((GroupTail) node).localIndex, ((GroupTail) node).groupIndex, z2);
                this.root = groupCurly;
                return groupCurly;
            }
            int i6 = ((GroupHead) nodeCreateGroup).localIndex;
            if (curly.type == 0) {
                lazyLoop = new Loop(this.localCount, i6);
            } else {
                lazyLoop = new LazyLoop(this.localCount, i6);
            }
            Prolog prolog = new Prolog(lazyLoop);
            this.localCount++;
            lazyLoop.cmin = curly.cmin;
            lazyLoop.cmax = curly.cmax;
            lazyLoop.body = nodeCreateGroup;
            node.next = lazyLoop;
            this.root = lazyLoop;
            return prolog;
        }
        throw error("Internal logic error");
    }

    private Node createGroup(boolean z2) {
        int i2 = this.localCount;
        this.localCount = i2 + 1;
        int i3 = 0;
        if (!z2) {
            int i4 = this.capturingGroupCount;
            this.capturingGroupCount = i4 + 1;
            i3 = i4;
        }
        GroupHead groupHead = new GroupHead(i2);
        this.root = new GroupTail(i2, i3);
        if (!z2 && i3 < 10) {
            this.groupNodes[i3] = groupHead;
        }
        return groupHead;
    }

    private void addFlag() {
        int iPeek = peek();
        while (true) {
            switch (iPeek) {
                case 45:
                    next();
                    subFlag();
                    return;
                case 85:
                    this.flags |= 320;
                    break;
                case 99:
                    this.flags |= 128;
                    break;
                case 100:
                    this.flags |= 1;
                    break;
                case 105:
                    this.flags |= 2;
                    break;
                case 109:
                    this.flags |= 8;
                    break;
                case 115:
                    this.flags |= 32;
                    break;
                case 117:
                    this.flags |= 64;
                    break;
                case 120:
                    this.flags |= 4;
                    break;
                default:
                    return;
            }
            iPeek = next();
        }
    }

    private void subFlag() {
        int iPeek = peek();
        while (true) {
            switch (iPeek) {
                case 85:
                    this.flags &= -321;
                    return;
                case 99:
                    this.flags &= -129;
                    break;
                case 100:
                    this.flags &= -2;
                    break;
                case 105:
                    this.flags &= -3;
                    break;
                case 109:
                    this.flags &= -9;
                    break;
                case 115:
                    this.flags &= -33;
                    break;
                case 117:
                    this.flags &= -65;
                    break;
                case 120:
                    this.flags &= -5;
                    break;
                default:
                    return;
            }
            iPeek = next();
        }
    }

    private Node closure(Node node) {
        int i2;
        Curly curly;
        switch (peek()) {
            case 42:
                int next = next();
                if (next == 63) {
                    next();
                    return new Curly(node, 0, Integer.MAX_VALUE, 1);
                }
                if (next == 43) {
                    next();
                    return new Curly(node, 0, Integer.MAX_VALUE, 2);
                }
                return new Curly(node, 0, Integer.MAX_VALUE, 0);
            case 43:
                int next2 = next();
                if (next2 == 63) {
                    next();
                    return new Curly(node, 1, Integer.MAX_VALUE, 1);
                }
                if (next2 == 43) {
                    next();
                    return new Curly(node, 1, Integer.MAX_VALUE, 2);
                }
                return new Curly(node, 1, Integer.MAX_VALUE, 0);
            case 63:
                int next3 = next();
                if (next3 == 63) {
                    next();
                    return new Ques(node, 1);
                }
                if (next3 == 43) {
                    next();
                    return new Ques(node, 2);
                }
                return new Ques(node, 0);
            case 123:
                int i3 = this.temp[this.cursor + 1];
                if (ASCII.isDigit(i3)) {
                    skip();
                    int i4 = 0;
                    do {
                        i4 = (i4 * 10) + (i3 - 48);
                        i2 = read();
                        i3 = i2;
                    } while (ASCII.isDigit(i2));
                    int i5 = i4;
                    if (i3 == 44) {
                        i3 = read();
                        i5 = Integer.MAX_VALUE;
                        if (i3 != 125) {
                            i5 = 0;
                            while (ASCII.isDigit(i3)) {
                                i5 = (i5 * 10) + (i3 - 48);
                                i3 = read();
                            }
                        }
                    }
                    if (i3 != 125) {
                        throw error("Unclosed counted closure");
                    }
                    if ((i4 | i5 | (i5 - i4)) < 0) {
                        throw error("Illegal repetition range");
                    }
                    int iPeek = peek();
                    if (iPeek == 63) {
                        next();
                        curly = new Curly(node, i4, i5, 1);
                    } else if (iPeek == 43) {
                        next();
                        curly = new Curly(node, i4, i5, 2);
                    } else {
                        curly = new Curly(node, i4, i5, 0);
                    }
                    return curly;
                }
                throw error("Illegal repetition");
            default:
                return node;
        }
    }

    private int c() {
        if (this.cursor < this.patternLength) {
            return read() ^ 64;
        }
        throw error("Illegal control escape sequence");
    }

    private int o() {
        int i2 = read();
        if (((i2 - 48) | (55 - i2)) >= 0) {
            int i3 = read();
            if (((i3 - 48) | (55 - i3)) >= 0) {
                int i4 = read();
                if (((i4 - 48) | (55 - i4)) >= 0 && ((i2 - 48) | (51 - i2)) >= 0) {
                    return ((i2 - 48) * 64) + ((i3 - 48) * 8) + (i4 - 48);
                }
                unread();
                return ((i2 - 48) * 8) + (i3 - 48);
            }
            unread();
            return i2 - 48;
        }
        throw error("Illegal octal escape sequence");
    }

    private int x() {
        int i2 = read();
        if (ASCII.isHexDigit(i2)) {
            int i3 = read();
            if (ASCII.isHexDigit(i3)) {
                return (ASCII.toDigit(i2) * 16) + ASCII.toDigit(i3);
            }
        } else if (i2 == 123 && ASCII.isHexDigit(peek())) {
            int digit = 0;
            do {
                int i4 = read();
                if (ASCII.isHexDigit(i4)) {
                    digit = (digit << 4) + ASCII.toDigit(i4);
                } else {
                    if (i4 != 125) {
                        throw error("Unclosed hexadecimal escape sequence");
                    }
                    return digit;
                }
            } while (digit <= 1114111);
            throw error("Hexadecimal codepoint is too big");
        }
        throw error("Illegal hexadecimal escape sequence");
    }

    private int cursor() {
        return this.cursor;
    }

    private void setcursor(int i2) {
        this.cursor = i2;
    }

    private int uxxxx() {
        int digit = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            int i3 = read();
            if (!ASCII.isHexDigit(i3)) {
                throw error("Illegal Unicode escape sequence");
            }
            digit = (digit * 16) + ASCII.toDigit(i3);
        }
        return digit;
    }

    private int u() {
        int iUxxxx = uxxxx();
        if (Character.isHighSurrogate((char) iUxxxx)) {
            int iCursor = cursor();
            if (read() == 92 && read() == 117) {
                int iUxxxx2 = uxxxx();
                if (Character.isLowSurrogate((char) iUxxxx2)) {
                    return Character.toCodePoint((char) iUxxxx, (char) iUxxxx2);
                }
            }
            setcursor(iCursor);
        }
        return iUxxxx;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int countChars(CharSequence charSequence, int i2, int i3) {
        if (i3 == 1 && i2 >= 0 && i2 < charSequence.length() && !Character.isHighSurrogate(charSequence.charAt(i2))) {
            return 1;
        }
        int length = charSequence.length();
        int i4 = i2;
        if (i3 >= 0) {
            if (!$assertionsDisabled && ((length != 0 || i2 != 0) && (i2 < 0 || i2 >= length))) {
                throw new AssertionError();
            }
            for (int i5 = 0; i4 < length && i5 < i3; i5++) {
                int i6 = i4;
                i4++;
                if (Character.isHighSurrogate(charSequence.charAt(i6)) && i4 < length && Character.isLowSurrogate(charSequence.charAt(i4))) {
                    i4++;
                }
            }
            return i4 - i2;
        }
        if (!$assertionsDisabled && (i2 < 0 || i2 > length)) {
            throw new AssertionError();
        }
        if (i2 == 0) {
            return 0;
        }
        int i7 = -i3;
        for (int i8 = 0; i4 > 0 && i8 < i7; i8++) {
            i4--;
            if (Character.isLowSurrogate(charSequence.charAt(i4)) && i4 > 0 && Character.isHighSurrogate(charSequence.charAt(i4 - 1))) {
                i4--;
            }
        }
        return i2 - i4;
    }

    private static final int countCodePoints(CharSequence charSequence) {
        int length = charSequence.length();
        int i2 = 0;
        int i3 = 0;
        while (i3 < length) {
            i2++;
            int i4 = i3;
            i3++;
            if (Character.isHighSurrogate(charSequence.charAt(i4)) && i3 < length && Character.isLowSurrogate(charSequence.charAt(i3))) {
                i3++;
            }
        }
        return i2;
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$BitClass.class */
    private static final class BitClass extends BmpCharProperty {
        final boolean[] bits;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Pattern.class.desiredAssertionStatus();
        }

        BitClass() {
            super();
            this.bits = new boolean[256];
        }

        private BitClass(boolean[] zArr) {
            super();
            this.bits = zArr;
        }

        BitClass add(int i2, int i3) {
            if (!$assertionsDisabled && (i2 < 0 || i2 > 255)) {
                throw new AssertionError();
            }
            if ((i3 & 2) != 0) {
                if (ASCII.isAscii(i2)) {
                    this.bits[ASCII.toUpper(i2)] = true;
                    this.bits[ASCII.toLower(i2)] = true;
                } else if ((i3 & 64) != 0) {
                    this.bits[Character.toLowerCase(i2)] = true;
                    this.bits[Character.toUpperCase(i2)] = true;
                }
            }
            this.bits[i2] = true;
            return this;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return i2 < 256 && this.bits[i2];
        }
    }

    private CharProperty newSingle(int i2) {
        int lower;
        int upper;
        if (has(2)) {
            if (has(64)) {
                int upperCase = Character.toUpperCase(i2);
                int lowerCase = Character.toLowerCase(upperCase);
                if (upperCase != lowerCase) {
                    return new SingleU(lowerCase);
                }
            } else if (ASCII.isAscii(i2) && (lower = ASCII.toLower(i2)) != (upper = ASCII.toUpper(i2))) {
                return new SingleI(lower, upper);
            }
        }
        if (isSupplementary(i2)) {
            return new SingleS(i2);
        }
        return new Single(i2);
    }

    private Node newSlice(int[] iArr, int i2, boolean z2) {
        int[] iArr2 = new int[i2];
        if (has(2)) {
            if (has(64)) {
                for (int i3 = 0; i3 < i2; i3++) {
                    iArr2[i3] = Character.toLowerCase(Character.toUpperCase(iArr[i3]));
                }
                return z2 ? new SliceUS(iArr2) : new SliceU(iArr2);
            }
            for (int i4 = 0; i4 < i2; i4++) {
                iArr2[i4] = ASCII.toLower(iArr[i4]);
            }
            return z2 ? new SliceIS(iArr2) : new SliceI(iArr2);
        }
        for (int i5 = 0; i5 < i2; i5++) {
            iArr2[i5] = iArr[i5];
        }
        return z2 ? new SliceS(iArr2) : new Slice(iArr2);
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Node.class */
    static class Node {
        Node next = Pattern.accept;

        Node() {
        }

        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            matcher.last = i2;
            matcher.groups[0] = matcher.first;
            matcher.groups[1] = matcher.last;
            return true;
        }

        boolean study(TreeInfo treeInfo) {
            if (this.next != null) {
                return this.next.study(treeInfo);
            }
            return treeInfo.deterministic;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$LastNode.class */
    static class LastNode extends Node {
        LastNode() {
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (matcher.acceptMode == 1 && i2 != matcher.to) {
                return false;
            }
            matcher.last = i2;
            matcher.groups[0] = matcher.first;
            matcher.groups[1] = matcher.last;
            return true;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Start.class */
    static class Start extends Node {
        int minLength;

        Start(Node node) {
            this.next = node;
            TreeInfo treeInfo = new TreeInfo();
            this.next.study(treeInfo);
            this.minLength = treeInfo.minLength;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 > matcher.to - this.minLength) {
                matcher.hitEnd = true;
                return false;
            }
            int i3 = matcher.to - this.minLength;
            while (i2 <= i3) {
                if (!this.next.match(matcher, i2, charSequence)) {
                    i2++;
                } else {
                    matcher.first = i2;
                    matcher.groups[0] = matcher.first;
                    matcher.groups[1] = matcher.last;
                    return true;
                }
            }
            matcher.hitEnd = true;
            return false;
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            this.next.study(treeInfo);
            treeInfo.maxValid = false;
            treeInfo.deterministic = false;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$StartS.class */
    static final class StartS extends Start {
        StartS(Node node) {
            super(node);
        }

        @Override // java.util.regex.Pattern.Start, java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 > matcher.to - this.minLength) {
                matcher.hitEnd = true;
                return false;
            }
            int i3 = matcher.to - this.minLength;
            while (i2 <= i3) {
                if (this.next.match(matcher, i2, charSequence)) {
                    matcher.first = i2;
                    matcher.groups[0] = matcher.first;
                    matcher.groups[1] = matcher.last;
                    return true;
                }
                if (i2 == i3) {
                    break;
                }
                int i4 = i2;
                i2++;
                if (Character.isHighSurrogate(charSequence.charAt(i4)) && i2 < charSequence.length() && Character.isLowSurrogate(charSequence.charAt(i2))) {
                    i2++;
                }
            }
            matcher.hitEnd = true;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Begin.class */
    static final class Begin extends Node {
        Begin() {
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 == (matcher.anchoringBounds ? matcher.from : 0) && this.next.match(matcher, i2, charSequence)) {
                matcher.first = i2;
                matcher.groups[0] = i2;
                matcher.groups[1] = matcher.last;
                return true;
            }
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$End.class */
    static final class End extends Node {
        End() {
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 == (matcher.anchoringBounds ? matcher.to : matcher.getTextLength())) {
                matcher.hitEnd = true;
                return this.next.match(matcher, i2, charSequence);
            }
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Caret.class */
    static final class Caret extends Node {
        Caret() {
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.from;
            int textLength = matcher.to;
            if (!matcher.anchoringBounds) {
                i3 = 0;
                textLength = matcher.getTextLength();
            }
            if (i2 == textLength) {
                matcher.hitEnd = true;
                return false;
            }
            if (i2 > i3) {
                char cCharAt = charSequence.charAt(i2 - 1);
                if (cCharAt != '\n' && cCharAt != '\r' && (cCharAt | 1) != 8233 && cCharAt != 133) {
                    return false;
                }
                if (cCharAt == '\r' && charSequence.charAt(i2) == '\n') {
                    return false;
                }
            }
            return this.next.match(matcher, i2, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$UnixCaret.class */
    static final class UnixCaret extends Node {
        UnixCaret() {
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.from;
            int textLength = matcher.to;
            if (!matcher.anchoringBounds) {
                i3 = 0;
                textLength = matcher.getTextLength();
            }
            if (i2 == textLength) {
                matcher.hitEnd = true;
                return false;
            }
            if (i2 > i3 && charSequence.charAt(i2 - 1) != '\n') {
                return false;
            }
            return this.next.match(matcher, i2, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$LastMatch.class */
    static final class LastMatch extends Node {
        LastMatch() {
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 != matcher.oldLast) {
                return false;
            }
            return this.next.match(matcher, i2, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Dollar.class */
    static final class Dollar extends Node {
        boolean multiline;

        Dollar(boolean z2) {
            this.multiline = z2;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int textLength = matcher.anchoringBounds ? matcher.to : matcher.getTextLength();
            if (!this.multiline) {
                if (i2 < textLength - 2) {
                    return false;
                }
                if (i2 == textLength - 2 && (charSequence.charAt(i2) != '\r' || charSequence.charAt(i2 + 1) != '\n')) {
                    return false;
                }
            }
            if (i2 < textLength) {
                char cCharAt = charSequence.charAt(i2);
                if (cCharAt == '\n') {
                    if (i2 > 0 && charSequence.charAt(i2 - 1) == '\r') {
                        return false;
                    }
                    if (this.multiline) {
                        return this.next.match(matcher, i2, charSequence);
                    }
                } else if (cCharAt == '\r' || cCharAt == 133 || (cCharAt | 1) == 8233) {
                    if (this.multiline) {
                        return this.next.match(matcher, i2, charSequence);
                    }
                } else {
                    return false;
                }
            }
            matcher.hitEnd = true;
            matcher.requireEnd = true;
            return this.next.match(matcher, i2, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            this.next.study(treeInfo);
            return treeInfo.deterministic;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$UnixDollar.class */
    static final class UnixDollar extends Node {
        boolean multiline;

        UnixDollar(boolean z2) {
            this.multiline = z2;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int textLength = matcher.anchoringBounds ? matcher.to : matcher.getTextLength();
            if (i2 < textLength) {
                if (charSequence.charAt(i2) == '\n') {
                    if (!this.multiline && i2 != textLength - 1) {
                        return false;
                    }
                    if (this.multiline) {
                        return this.next.match(matcher, i2, charSequence);
                    }
                } else {
                    return false;
                }
            }
            matcher.hitEnd = true;
            matcher.requireEnd = true;
            return this.next.match(matcher, i2, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            this.next.study(treeInfo);
            return treeInfo.deterministic;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$LineEnding.class */
    static final class LineEnding extends Node {
        LineEnding() {
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 < matcher.to) {
                char cCharAt = charSequence.charAt(i2);
                if (cCharAt == '\n' || cCharAt == 11 || cCharAt == '\f' || cCharAt == 133 || cCharAt == 8232 || cCharAt == 8233) {
                    return this.next.match(matcher, i2 + 1, charSequence);
                }
                if (cCharAt == '\r') {
                    int i3 = i2 + 1;
                    if (i3 < matcher.to && charSequence.charAt(i3) == '\n') {
                        i3++;
                    }
                    return this.next.match(matcher, i3, charSequence);
                }
                return false;
            }
            matcher.hitEnd = true;
            return false;
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.minLength++;
            treeInfo.maxLength += 2;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$CharProperty.class */
    private static abstract class CharProperty extends Node {
        abstract boolean isSatisfiedBy(int i2);

        private CharProperty() {
        }

        CharProperty complement() {
            return new CharProperty() { // from class: java.util.regex.Pattern.CharProperty.1
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return !CharProperty.this.isSatisfiedBy(i2);
                }
            };
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 < matcher.to) {
                int iCodePointAt = Character.codePointAt(charSequence, i2);
                return isSatisfiedBy(iCodePointAt) && this.next.match(matcher, i2 + Character.charCount(iCodePointAt), charSequence);
            }
            matcher.hitEnd = true;
            return false;
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.minLength++;
            treeInfo.maxLength++;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$BmpCharProperty.class */
    private static abstract class BmpCharProperty extends CharProperty {
        private BmpCharProperty() {
            super();
        }

        @Override // java.util.regex.Pattern.CharProperty, java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 < matcher.to) {
                return isSatisfiedBy(charSequence.charAt(i2)) && this.next.match(matcher, i2 + 1, charSequence);
            }
            matcher.hitEnd = true;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SingleS.class */
    static final class SingleS extends CharProperty {

        /* renamed from: c, reason: collision with root package name */
        final int f12597c;

        SingleS(int i2) {
            super();
            this.f12597c = i2;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return i2 == this.f12597c;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Single.class */
    static final class Single extends BmpCharProperty {

        /* renamed from: c, reason: collision with root package name */
        final int f12596c;

        Single(int i2) {
            super();
            this.f12596c = i2;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return i2 == this.f12596c;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SingleI.class */
    static final class SingleI extends BmpCharProperty {
        final int lower;
        final int upper;

        SingleI(int i2, int i3) {
            super();
            this.lower = i2;
            this.upper = i3;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return i2 == this.lower || i2 == this.upper;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SingleU.class */
    static final class SingleU extends CharProperty {
        final int lower;

        SingleU(int i2) {
            super();
            this.lower = i2;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return this.lower == i2 || this.lower == Character.toLowerCase(Character.toUpperCase(i2));
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Block.class */
    static final class Block extends CharProperty {
        final Character.UnicodeBlock block;

        Block(Character.UnicodeBlock unicodeBlock) {
            super();
            this.block = unicodeBlock;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return this.block == Character.UnicodeBlock.of(i2);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Script.class */
    static final class Script extends CharProperty {
        final Character.UnicodeScript script;

        Script(Character.UnicodeScript unicodeScript) {
            super();
            this.script = unicodeScript;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return this.script == Character.UnicodeScript.of(i2);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Category.class */
    static final class Category extends CharProperty {
        final int typeMask;

        Category(int i2) {
            super();
            this.typeMask = i2;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return (this.typeMask & (1 << Character.getType(i2))) != 0;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Utype.class */
    static final class Utype extends CharProperty {
        final UnicodeProp uprop;

        Utype(UnicodeProp unicodeProp) {
            super();
            this.uprop = unicodeProp;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return this.uprop.is(i2);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Ctype.class */
    static final class Ctype extends BmpCharProperty {
        final int ctype;

        Ctype(int i2) {
            super();
            this.ctype = i2;
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return i2 < 128 && ASCII.isType(i2, this.ctype);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$VertWS.class */
    static final class VertWS extends BmpCharProperty {
        VertWS() {
            super();
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return (i2 >= 10 && i2 <= 13) || i2 == 133 || i2 == 8232 || i2 == 8233;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$HorizWS.class */
    static final class HorizWS extends BmpCharProperty {
        HorizWS() {
            super();
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return i2 == 9 || i2 == 32 || i2 == 160 || i2 == 5760 || i2 == 6158 || (i2 >= 8192 && i2 <= 8202) || i2 == 8239 || i2 == 8287 || i2 == 12288;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SliceNode.class */
    static class SliceNode extends Node {
        int[] buffer;

        SliceNode(int[] iArr) {
            this.buffer = iArr;
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.minLength += this.buffer.length;
            treeInfo.maxLength += this.buffer.length;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Slice.class */
    static final class Slice extends SliceNode {
        Slice(int[] iArr) {
            super(iArr);
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int[] iArr = this.buffer;
            int length = iArr.length;
            for (int i3 = 0; i3 < length; i3++) {
                if (i2 + i3 >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                if (iArr[i3] != charSequence.charAt(i2 + i3)) {
                    return false;
                }
            }
            return this.next.match(matcher, i2 + length, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SliceI.class */
    static class SliceI extends SliceNode {
        SliceI(int[] iArr) {
            super(iArr);
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int[] iArr = this.buffer;
            int length = iArr.length;
            for (int i3 = 0; i3 < length; i3++) {
                if (i2 + i3 >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                char cCharAt = charSequence.charAt(i2 + i3);
                if (iArr[i3] != cCharAt && iArr[i3] != ASCII.toLower(cCharAt)) {
                    return false;
                }
            }
            return this.next.match(matcher, i2 + length, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SliceU.class */
    static final class SliceU extends SliceNode {
        SliceU(int[] iArr) {
            super(iArr);
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int[] iArr = this.buffer;
            int length = iArr.length;
            for (int i3 = 0; i3 < length; i3++) {
                if (i2 + i3 >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                char cCharAt = charSequence.charAt(i2 + i3);
                if (iArr[i3] != cCharAt && iArr[i3] != Character.toLowerCase(Character.toUpperCase((int) cCharAt))) {
                    return false;
                }
            }
            return this.next.match(matcher, i2 + length, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SliceS.class */
    static final class SliceS extends SliceNode {
        SliceS(int[] iArr) {
            super(iArr);
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int iCharCount = i2;
            for (int i3 : this.buffer) {
                if (iCharCount >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                int iCodePointAt = Character.codePointAt(charSequence, iCharCount);
                if (i3 != iCodePointAt) {
                    return false;
                }
                iCharCount += Character.charCount(iCodePointAt);
                if (iCharCount > matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
            }
            return this.next.match(matcher, iCharCount, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SliceIS.class */
    static class SliceIS extends SliceNode {
        SliceIS(int[] iArr) {
            super(iArr);
        }

        int toLower(int i2) {
            return ASCII.toLower(i2);
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int[] iArr = this.buffer;
            int iCharCount = i2;
            for (int i3 = 0; i3 < iArr.length; i3++) {
                if (iCharCount >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                int iCodePointAt = Character.codePointAt(charSequence, iCharCount);
                if (iArr[i3] != iCodePointAt && iArr[i3] != toLower(iCodePointAt)) {
                    return false;
                }
                iCharCount += Character.charCount(iCodePointAt);
                if (iCharCount > matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
            }
            return this.next.match(matcher, iCharCount, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$SliceUS.class */
    static final class SliceUS extends SliceIS {
        SliceUS(int[] iArr) {
            super(iArr);
        }

        @Override // java.util.regex.Pattern.SliceIS
        int toLower(int i2) {
            return Character.toLowerCase(Character.toUpperCase(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean inRange(int i2, int i3, int i4) {
        return i2 <= i3 && i3 <= i4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static CharProperty rangeFor(final int i2, final int i3) {
        return new CharProperty() { // from class: java.util.regex.Pattern.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // java.util.regex.Pattern.CharProperty
            boolean isSatisfiedBy(int i4) {
                return Pattern.inRange(i2, i4, i3);
            }
        };
    }

    private CharProperty caseInsensitiveRangeFor(final int i2, final int i3) {
        if (has(64)) {
            return new CharProperty() { // from class: java.util.regex.Pattern.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i4) {
                    if (Pattern.inRange(i2, i4, i3)) {
                        return true;
                    }
                    int upperCase = Character.toUpperCase(i4);
                    return Pattern.inRange(i2, upperCase, i3) || Pattern.inRange(i2, Character.toLowerCase(upperCase), i3);
                }
            };
        }
        return new CharProperty() { // from class: java.util.regex.Pattern.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // java.util.regex.Pattern.CharProperty
            boolean isSatisfiedBy(int i4) {
                return Pattern.inRange(i2, i4, i3) || (ASCII.isAscii(i4) && (Pattern.inRange(i2, ASCII.toUpper(i4), i3) || Pattern.inRange(i2, ASCII.toLower(i4), i3)));
            }
        };
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$All.class */
    static final class All extends CharProperty {
        All() {
            super();
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return true;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Dot.class */
    static final class Dot extends CharProperty {
        Dot() {
            super();
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return (i2 == 10 || i2 == 13 || (i2 | 1) == 8233 || i2 == 133) ? false : true;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$UnixDot.class */
    static final class UnixDot extends CharProperty {
        UnixDot() {
            super();
        }

        @Override // java.util.regex.Pattern.CharProperty
        boolean isSatisfiedBy(int i2) {
            return i2 != 10;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Ques.class */
    static final class Ques extends Node {
        Node atom;
        int type;

        Ques(Node node, int i2) {
            this.atom = node;
            this.type = i2;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            switch (this.type) {
                case 0:
                    return (this.atom.match(matcher, i2, charSequence) && this.next.match(matcher, matcher.last, charSequence)) || this.next.match(matcher, i2, charSequence);
                case 1:
                    return this.next.match(matcher, i2, charSequence) || (this.atom.match(matcher, i2, charSequence) && this.next.match(matcher, matcher.last, charSequence));
                case 2:
                    if (this.atom.match(matcher, i2, charSequence)) {
                        i2 = matcher.last;
                    }
                    return this.next.match(matcher, i2, charSequence);
                default:
                    return this.atom.match(matcher, i2, charSequence) && this.next.match(matcher, matcher.last, charSequence);
            }
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            if (this.type != 3) {
                int i2 = treeInfo.minLength;
                this.atom.study(treeInfo);
                treeInfo.minLength = i2;
                treeInfo.deterministic = false;
                return this.next.study(treeInfo);
            }
            this.atom.study(treeInfo);
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Curly.class */
    static final class Curly extends Node {
        Node atom;
        int type;
        int cmin;
        int cmax;

        Curly(Node node, int i2, int i3, int i4) {
            this.atom = node;
            this.type = i4;
            this.cmin = i2;
            this.cmax = i3;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = 0;
            while (i3 < this.cmin) {
                if (this.atom.match(matcher, i2, charSequence)) {
                    i2 = matcher.last;
                    i3++;
                } else {
                    return false;
                }
            }
            if (this.type == 0) {
                return match0(matcher, i2, i3, charSequence);
            }
            if (this.type == 1) {
                return match1(matcher, i2, i3, charSequence);
            }
            return match2(matcher, i2, i3, charSequence);
        }

        boolean match0(Matcher matcher, int i2, int i3, CharSequence charSequence) {
            int i4;
            int i5;
            if (i3 >= this.cmax) {
                return this.next.match(matcher, i2, charSequence);
            }
            if (this.atom.match(matcher, i2, charSequence) && (i4 = matcher.last - i2) != 0) {
                int i6 = matcher.last;
                while (true) {
                    i5 = i6;
                    i3++;
                    if (i3 >= this.cmax || !this.atom.match(matcher, i5, charSequence)) {
                        break;
                    }
                    if (i5 + i4 != matcher.last) {
                        if (match0(matcher, matcher.last, i3 + 1, charSequence)) {
                            return true;
                        }
                    } else {
                        i6 = i5 + i4;
                    }
                }
                while (i3 >= i3) {
                    if (this.next.match(matcher, i5, charSequence)) {
                        return true;
                    }
                    i5 -= i4;
                    i3--;
                }
                return false;
            }
            return this.next.match(matcher, i2, charSequence);
        }

        boolean match1(Matcher matcher, int i2, int i3, CharSequence charSequence) {
            while (!this.next.match(matcher, i2, charSequence)) {
                if (i3 >= this.cmax || !this.atom.match(matcher, i2, charSequence) || i2 == matcher.last) {
                    return false;
                }
                i2 = matcher.last;
                i3++;
            }
            return true;
        }

        boolean match2(Matcher matcher, int i2, int i3, CharSequence charSequence) {
            while (i3 < this.cmax && this.atom.match(matcher, i2, charSequence) && i2 != matcher.last) {
                i2 = matcher.last;
                i3++;
            }
            return this.next.match(matcher, i2, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            int i2 = treeInfo.minLength;
            int i3 = treeInfo.maxLength;
            boolean z2 = treeInfo.maxValid;
            boolean z3 = treeInfo.deterministic;
            treeInfo.reset();
            this.atom.study(treeInfo);
            int i4 = (treeInfo.minLength * this.cmin) + i2;
            if (i4 < i2) {
                i4 = 268435455;
            }
            treeInfo.minLength = i4;
            if (z2 & treeInfo.maxValid) {
                int i5 = (treeInfo.maxLength * this.cmax) + i3;
                treeInfo.maxLength = i5;
                if (i5 < i3) {
                    treeInfo.maxValid = false;
                }
            } else {
                treeInfo.maxValid = false;
            }
            if (treeInfo.deterministic && this.cmin == this.cmax) {
                treeInfo.deterministic = z3;
            } else {
                treeInfo.deterministic = false;
            }
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$GroupCurly.class */
    static final class GroupCurly extends Node {
        Node atom;
        int type;
        int cmin;
        int cmax;
        int localIndex;
        int groupIndex;
        boolean capture;

        GroupCurly(Node node, int i2, int i3, int i4, int i5, int i6, boolean z2) {
            this.atom = node;
            this.type = i4;
            this.cmin = i2;
            this.cmax = i3;
            this.localIndex = i5;
            this.groupIndex = i6;
            this.capture = z2;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int[] iArr = matcher.groups;
            int[] iArr2 = matcher.locals;
            int i3 = iArr2[this.localIndex];
            int i4 = 0;
            int i5 = 0;
            if (this.capture) {
                i4 = iArr[this.groupIndex];
                i5 = iArr[this.groupIndex + 1];
            }
            iArr2[this.localIndex] = -1;
            boolean zMatch2 = true;
            int i6 = 0;
            while (true) {
                if (i6 < this.cmin) {
                    if (this.atom.match(matcher, i2, charSequence)) {
                        if (this.capture) {
                            iArr[this.groupIndex] = i2;
                            iArr[this.groupIndex + 1] = matcher.last;
                        }
                        i2 = matcher.last;
                        i6++;
                    } else {
                        zMatch2 = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (zMatch2) {
                if (this.type == 0) {
                    zMatch2 = match0(matcher, i2, this.cmin, charSequence);
                } else if (this.type == 1) {
                    zMatch2 = match1(matcher, i2, this.cmin, charSequence);
                } else {
                    zMatch2 = match2(matcher, i2, this.cmin, charSequence);
                }
            }
            if (!zMatch2) {
                iArr2[this.localIndex] = i3;
                if (this.capture) {
                    iArr[this.groupIndex] = i4;
                    iArr[this.groupIndex + 1] = i5;
                }
            }
            return zMatch2;
        }

        boolean match0(Matcher matcher, int i2, int i3, CharSequence charSequence) {
            int[] iArr = matcher.groups;
            int i4 = 0;
            int i5 = 0;
            if (this.capture) {
                i4 = iArr[this.groupIndex];
                i5 = iArr[this.groupIndex + 1];
            }
            if (i3 < this.cmax && this.atom.match(matcher, i2, charSequence)) {
                int i6 = matcher.last - i2;
                if (i6 > 0) {
                    while (true) {
                        if (this.capture) {
                            iArr[this.groupIndex] = i2;
                            iArr[this.groupIndex + 1] = i2 + i6;
                        }
                        i2 += i6;
                        i3++;
                        if (i3 >= this.cmax || !this.atom.match(matcher, i2, charSequence)) {
                            break;
                        }
                        if (i2 + i6 != matcher.last) {
                            if (match0(matcher, i2, i3, charSequence)) {
                                return true;
                            }
                        }
                    }
                    while (i3 > i3) {
                        if (this.next.match(matcher, i2, charSequence)) {
                            if (this.capture) {
                                iArr[this.groupIndex + 1] = i2;
                                iArr[this.groupIndex] = i2 - i6;
                                return true;
                            }
                            return true;
                        }
                        i2 -= i6;
                        if (this.capture) {
                            iArr[this.groupIndex + 1] = i2;
                            iArr[this.groupIndex] = i2 - i6;
                        }
                        i3--;
                    }
                } else {
                    if (this.capture) {
                        iArr[this.groupIndex] = i2;
                        iArr[this.groupIndex + 1] = i2 + i6;
                    }
                    i2 += i6;
                }
            }
            if (this.capture) {
                iArr[this.groupIndex] = i4;
                iArr[this.groupIndex + 1] = i5;
            }
            return this.next.match(matcher, i2, charSequence);
        }

        boolean match1(Matcher matcher, int i2, int i3, CharSequence charSequence) {
            while (!this.next.match(matcher, i2, charSequence)) {
                if (i3 >= this.cmax || !this.atom.match(matcher, i2, charSequence) || i2 == matcher.last) {
                    return false;
                }
                if (this.capture) {
                    matcher.groups[this.groupIndex] = i2;
                    matcher.groups[this.groupIndex + 1] = matcher.last;
                }
                i2 = matcher.last;
                i3++;
            }
            return true;
        }

        boolean match2(Matcher matcher, int i2, int i3, CharSequence charSequence) {
            while (i3 < this.cmax && this.atom.match(matcher, i2, charSequence)) {
                if (this.capture) {
                    matcher.groups[this.groupIndex] = i2;
                    matcher.groups[this.groupIndex + 1] = matcher.last;
                }
                if (i2 == matcher.last) {
                    break;
                }
                i2 = matcher.last;
                i3++;
            }
            return this.next.match(matcher, i2, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            int i2 = treeInfo.minLength;
            int i3 = treeInfo.maxLength;
            boolean z2 = treeInfo.maxValid;
            boolean z3 = treeInfo.deterministic;
            treeInfo.reset();
            this.atom.study(treeInfo);
            int i4 = (treeInfo.minLength * this.cmin) + i2;
            if (i4 < i2) {
                i4 = 268435455;
            }
            treeInfo.minLength = i4;
            if (z2 & treeInfo.maxValid) {
                int i5 = (treeInfo.maxLength * this.cmax) + i3;
                treeInfo.maxLength = i5;
                if (i5 < i3) {
                    treeInfo.maxValid = false;
                }
            } else {
                treeInfo.maxValid = false;
            }
            if (treeInfo.deterministic && this.cmin == this.cmax) {
                treeInfo.deterministic = z3;
            } else {
                treeInfo.deterministic = false;
            }
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$BranchConn.class */
    static final class BranchConn extends Node {
        BranchConn() {
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            return this.next.match(matcher, i2, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            return treeInfo.deterministic;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Branch.class */
    static final class Branch extends Node {
        Node[] atoms = new Node[2];
        int size = 2;
        Node conn;

        Branch(Node node, Node node2, Node node3) {
            this.conn = node3;
            this.atoms[0] = node;
            this.atoms[1] = node2;
        }

        void add(Node node) {
            if (this.size >= this.atoms.length) {
                Node[] nodeArr = new Node[this.atoms.length * 2];
                System.arraycopy(this.atoms, 0, nodeArr, 0, this.atoms.length);
                this.atoms = nodeArr;
            }
            Node[] nodeArr2 = this.atoms;
            int i2 = this.size;
            this.size = i2 + 1;
            nodeArr2[i2] = node;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            for (int i3 = 0; i3 < this.size; i3++) {
                if (this.atoms[i3] == null) {
                    if (this.conn.next.match(matcher, i2, charSequence)) {
                        return true;
                    }
                } else if (this.atoms[i3].match(matcher, i2, charSequence)) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            int i2 = treeInfo.minLength;
            int i3 = treeInfo.maxLength;
            boolean z2 = treeInfo.maxValid;
            int iMin = Integer.MAX_VALUE;
            int iMax = -1;
            for (int i4 = 0; i4 < this.size; i4++) {
                treeInfo.reset();
                if (this.atoms[i4] != null) {
                    this.atoms[i4].study(treeInfo);
                }
                iMin = Math.min(iMin, treeInfo.minLength);
                iMax = Math.max(iMax, treeInfo.maxLength);
                z2 &= treeInfo.maxValid;
            }
            treeInfo.reset();
            this.conn.next.study(treeInfo);
            treeInfo.minLength += i2 + iMin;
            treeInfo.maxLength += i3 + iMax;
            treeInfo.maxValid &= z2;
            treeInfo.deterministic = false;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$GroupHead.class */
    static final class GroupHead extends Node {
        int localIndex;

        GroupHead(int i2) {
            this.localIndex = i2;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.locals[this.localIndex];
            matcher.locals[this.localIndex] = i2;
            boolean zMatch = this.next.match(matcher, i2, charSequence);
            matcher.locals[this.localIndex] = i3;
            return zMatch;
        }

        boolean matchRef(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.locals[this.localIndex];
            matcher.locals[this.localIndex] = i2 ^ (-1);
            boolean zMatch = this.next.match(matcher, i2, charSequence);
            matcher.locals[this.localIndex] = i3;
            return zMatch;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$GroupRef.class */
    static final class GroupRef extends Node {
        GroupHead head;

        GroupRef(GroupHead groupHead) {
            this.head = groupHead;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            return this.head.matchRef(matcher, i2, charSequence) && this.next.match(matcher, matcher.last, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.maxValid = false;
            treeInfo.deterministic = false;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$GroupTail.class */
    static final class GroupTail extends Node {
        int localIndex;
        int groupIndex;

        GroupTail(int i2, int i3) {
            this.localIndex = i2;
            this.groupIndex = i3 + i3;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.locals[this.localIndex];
            if (i3 >= 0) {
                int i4 = matcher.groups[this.groupIndex];
                int i5 = matcher.groups[this.groupIndex + 1];
                matcher.groups[this.groupIndex] = i3;
                matcher.groups[this.groupIndex + 1] = i2;
                if (this.next.match(matcher, i2, charSequence)) {
                    return true;
                }
                matcher.groups[this.groupIndex] = i4;
                matcher.groups[this.groupIndex + 1] = i5;
                return false;
            }
            matcher.last = i2;
            return true;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Prolog.class */
    static final class Prolog extends Node {
        Loop loop;

        Prolog(Loop loop) {
            this.loop = loop;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            return this.loop.matchInit(matcher, i2, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            return this.loop.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Loop.class */
    static class Loop extends Node {
        Node body;
        int countIndex;
        int beginIndex;
        int cmin;
        int cmax;

        Loop(int i2, int i3) {
            this.countIndex = i2;
            this.beginIndex = i3;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 > matcher.locals[this.beginIndex]) {
                int i3 = matcher.locals[this.countIndex];
                if (i3 < this.cmin) {
                    matcher.locals[this.countIndex] = i3 + 1;
                    boolean zMatch = this.body.match(matcher, i2, charSequence);
                    if (!zMatch) {
                        matcher.locals[this.countIndex] = i3;
                    }
                    return zMatch;
                }
                if (i3 < this.cmax) {
                    matcher.locals[this.countIndex] = i3 + 1;
                    if (!this.body.match(matcher, i2, charSequence)) {
                        matcher.locals[this.countIndex] = i3;
                    } else {
                        return true;
                    }
                }
            }
            return this.next.match(matcher, i2, charSequence);
        }

        boolean matchInit(Matcher matcher, int i2, CharSequence charSequence) {
            boolean zMatch;
            int i3 = matcher.locals[this.countIndex];
            if (0 < this.cmin) {
                matcher.locals[this.countIndex] = 1;
                zMatch = this.body.match(matcher, i2, charSequence);
            } else if (0 < this.cmax) {
                matcher.locals[this.countIndex] = 1;
                zMatch = this.body.match(matcher, i2, charSequence);
                if (!zMatch) {
                    zMatch = this.next.match(matcher, i2, charSequence);
                }
            } else {
                zMatch = this.next.match(matcher, i2, charSequence);
            }
            matcher.locals[this.countIndex] = i3;
            return zMatch;
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.maxValid = false;
            treeInfo.deterministic = false;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$LazyLoop.class */
    static final class LazyLoop extends Loop {
        LazyLoop(int i2, int i3) {
            super(i2, i3);
        }

        @Override // java.util.regex.Pattern.Loop, java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (i2 > matcher.locals[this.beginIndex]) {
                int i3 = matcher.locals[this.countIndex];
                if (i3 < this.cmin) {
                    matcher.locals[this.countIndex] = i3 + 1;
                    boolean zMatch = this.body.match(matcher, i2, charSequence);
                    if (!zMatch) {
                        matcher.locals[this.countIndex] = i3;
                    }
                    return zMatch;
                }
                if (this.next.match(matcher, i2, charSequence)) {
                    return true;
                }
                if (i3 < this.cmax) {
                    matcher.locals[this.countIndex] = i3 + 1;
                    boolean zMatch2 = this.body.match(matcher, i2, charSequence);
                    if (!zMatch2) {
                        matcher.locals[this.countIndex] = i3;
                    }
                    return zMatch2;
                }
                return false;
            }
            return this.next.match(matcher, i2, charSequence);
        }

        @Override // java.util.regex.Pattern.Loop
        boolean matchInit(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.locals[this.countIndex];
            boolean zMatch = false;
            if (0 < this.cmin) {
                matcher.locals[this.countIndex] = 1;
                zMatch = this.body.match(matcher, i2, charSequence);
            } else if (this.next.match(matcher, i2, charSequence)) {
                zMatch = true;
            } else if (0 < this.cmax) {
                matcher.locals[this.countIndex] = 1;
                zMatch = this.body.match(matcher, i2, charSequence);
            }
            matcher.locals[this.countIndex] = i3;
            return zMatch;
        }

        @Override // java.util.regex.Pattern.Loop, java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.maxValid = false;
            treeInfo.deterministic = false;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$BackRef.class */
    static class BackRef extends Node {
        int groupIndex;

        BackRef(int i2) {
            this.groupIndex = i2 + i2;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.groups[this.groupIndex];
            int i4 = matcher.groups[this.groupIndex + 1] - i3;
            if (i3 < 0) {
                return false;
            }
            if (i2 + i4 > matcher.to) {
                matcher.hitEnd = true;
                return false;
            }
            for (int i5 = 0; i5 < i4; i5++) {
                if (charSequence.charAt(i2 + i5) != charSequence.charAt(i3 + i5)) {
                    return false;
                }
            }
            return this.next.match(matcher, i2 + i4, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.maxValid = false;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$CIBackRef.class */
    static class CIBackRef extends Node {
        int groupIndex;
        boolean doUnicodeCase;

        CIBackRef(int i2, boolean z2) {
            this.groupIndex = i2 + i2;
            this.doUnicodeCase = z2;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int iCharCount = matcher.groups[this.groupIndex];
            int i3 = matcher.groups[this.groupIndex + 1] - iCharCount;
            if (iCharCount < 0) {
                return false;
            }
            if (i2 + i3 > matcher.to) {
                matcher.hitEnd = true;
                return false;
            }
            int iCharCount2 = i2;
            for (int i4 = 0; i4 < i3; i4++) {
                int iCodePointAt = Character.codePointAt(charSequence, iCharCount2);
                int iCodePointAt2 = Character.codePointAt(charSequence, iCharCount);
                if (iCodePointAt != iCodePointAt2) {
                    if (this.doUnicodeCase) {
                        int upperCase = Character.toUpperCase(iCodePointAt);
                        int upperCase2 = Character.toUpperCase(iCodePointAt2);
                        if (upperCase != upperCase2 && Character.toLowerCase(upperCase) != Character.toLowerCase(upperCase2)) {
                            return false;
                        }
                    } else if (ASCII.toLower(iCodePointAt) != ASCII.toLower(iCodePointAt2)) {
                        return false;
                    }
                }
                iCharCount2 += Character.charCount(iCodePointAt);
                iCharCount += Character.charCount(iCodePointAt2);
            }
            return this.next.match(matcher, i2 + i3, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.maxValid = false;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$First.class */
    static final class First extends Node {
        Node atom;

        First(Node node) {
            this.atom = BnM.optimize(node);
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (this.atom instanceof BnM) {
                return this.atom.match(matcher, i2, charSequence) && this.next.match(matcher, matcher.last, charSequence);
            }
            while (i2 <= matcher.to) {
                if (!this.atom.match(matcher, i2, charSequence)) {
                    i2 += Pattern.countChars(charSequence, i2, 1);
                    matcher.first++;
                } else {
                    return this.next.match(matcher, matcher.last, charSequence);
                }
            }
            matcher.hitEnd = true;
            return false;
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            this.atom.study(treeInfo);
            treeInfo.maxValid = false;
            treeInfo.deterministic = false;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Conditional.class */
    static final class Conditional extends Node {
        Node cond;
        Node yes;
        Node not;

        Conditional(Node node, Node node2, Node node3) {
            this.cond = node;
            this.yes = node2;
            this.not = node3;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            if (this.cond.match(matcher, i2, charSequence)) {
                return this.yes.match(matcher, i2, charSequence);
            }
            return this.not.match(matcher, i2, charSequence);
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            int i2 = treeInfo.minLength;
            int i3 = treeInfo.maxLength;
            boolean z2 = treeInfo.maxValid;
            treeInfo.reset();
            this.yes.study(treeInfo);
            int i4 = treeInfo.minLength;
            int i5 = treeInfo.maxLength;
            boolean z3 = treeInfo.maxValid;
            treeInfo.reset();
            this.not.study(treeInfo);
            treeInfo.minLength = i2 + Math.min(i4, treeInfo.minLength);
            treeInfo.maxLength = i3 + Math.max(i5, treeInfo.maxLength);
            treeInfo.maxValid = z2 & z3 & treeInfo.maxValid;
            treeInfo.deterministic = false;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Pos.class */
    static final class Pos extends Node {
        Node cond;

        Pos(Node node) {
            this.cond = node;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.to;
            if (matcher.transparentBounds) {
                matcher.to = matcher.getTextLength();
            }
            try {
                boolean zMatch = this.cond.match(matcher, i2, charSequence);
                matcher.to = i3;
                return zMatch && this.next.match(matcher, i2, charSequence);
            } catch (Throwable th) {
                matcher.to = i3;
                throw th;
            }
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Neg.class */
    static final class Neg extends Node {
        Node cond;

        Neg(Node node) {
            this.cond = node;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            boolean z2;
            int i3 = matcher.to;
            if (matcher.transparentBounds) {
                matcher.to = matcher.getTextLength();
            }
            try {
                if (i2 < matcher.to) {
                    z2 = !this.cond.match(matcher, i2, charSequence);
                } else {
                    matcher.requireEnd = true;
                    z2 = !this.cond.match(matcher, i2, charSequence);
                }
                return z2 && this.next.match(matcher, i2, charSequence);
            } finally {
                matcher.to = i3;
            }
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Behind.class */
    static class Behind extends Node {
        Node cond;
        int rmax;
        int rmin;

        Behind(Node node, int i2, int i3) {
            this.cond = node;
            this.rmax = i2;
            this.rmin = i3;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.from;
            boolean zMatch = false;
            int iMax = Math.max(i2 - this.rmax, !matcher.transparentBounds ? matcher.from : 0);
            int i4 = matcher.lookbehindTo;
            matcher.lookbehindTo = i2;
            if (matcher.transparentBounds) {
                matcher.from = 0;
            }
            for (int i5 = i2 - this.rmin; !zMatch && i5 >= iMax; i5--) {
                zMatch = this.cond.match(matcher, i5, charSequence);
            }
            matcher.from = i3;
            matcher.lookbehindTo = i4;
            return zMatch && this.next.match(matcher, i2, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$BehindS.class */
    static final class BehindS extends Behind {
        BehindS(Node node, int i2, int i3) {
            super(node, i2, i3);
        }

        @Override // java.util.regex.Pattern.Behind, java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int iCountChars = Pattern.countChars(charSequence, i2, -this.rmax);
            int iCountChars2 = Pattern.countChars(charSequence, i2, -this.rmin);
            int i3 = matcher.from;
            boolean zMatch = false;
            int iMax = Math.max(i2 - iCountChars, !matcher.transparentBounds ? matcher.from : 0);
            int i4 = matcher.lookbehindTo;
            matcher.lookbehindTo = i2;
            if (matcher.transparentBounds) {
                matcher.from = 0;
            }
            int i5 = i2;
            int iCountChars3 = iCountChars2;
            while (true) {
                int i6 = i5 - iCountChars3;
                if (zMatch || i6 < iMax) {
                    break;
                }
                zMatch = this.cond.match(matcher, i6, charSequence);
                i5 = i6;
                iCountChars3 = i6 > iMax ? Pattern.countChars(charSequence, i6, -1) : 1;
            }
            matcher.from = i3;
            matcher.lookbehindTo = i4;
            return zMatch && this.next.match(matcher, i2, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$NotBehind.class */
    static class NotBehind extends Node {
        Node cond;
        int rmax;
        int rmin;

        NotBehind(Node node, int i2, int i3) {
            this.cond = node;
            this.rmax = i2;
            this.rmin = i3;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int i3 = matcher.lookbehindTo;
            int i4 = matcher.from;
            boolean zMatch = false;
            int iMax = Math.max(i2 - this.rmax, !matcher.transparentBounds ? matcher.from : 0);
            matcher.lookbehindTo = i2;
            if (matcher.transparentBounds) {
                matcher.from = 0;
            }
            for (int i5 = i2 - this.rmin; !zMatch && i5 >= iMax; i5--) {
                zMatch = this.cond.match(matcher, i5, charSequence);
            }
            matcher.from = i4;
            matcher.lookbehindTo = i3;
            return !zMatch && this.next.match(matcher, i2, charSequence);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$NotBehindS.class */
    static final class NotBehindS extends NotBehind {
        NotBehindS(Node node, int i2, int i3) {
            super(node, i2, i3);
        }

        @Override // java.util.regex.Pattern.NotBehind, java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int iCountChars = Pattern.countChars(charSequence, i2, -this.rmax);
            int iCountChars2 = Pattern.countChars(charSequence, i2, -this.rmin);
            int i3 = matcher.from;
            int i4 = matcher.lookbehindTo;
            boolean zMatch = false;
            int iMax = Math.max(i2 - iCountChars, !matcher.transparentBounds ? matcher.from : 0);
            matcher.lookbehindTo = i2;
            if (matcher.transparentBounds) {
                matcher.from = 0;
            }
            int i5 = i2;
            int iCountChars3 = iCountChars2;
            while (true) {
                int i6 = i5 - iCountChars3;
                if (zMatch || i6 < iMax) {
                    break;
                }
                zMatch = this.cond.match(matcher, i6, charSequence);
                i5 = i6;
                iCountChars3 = i6 > iMax ? Pattern.countChars(charSequence, i6, -1) : 1;
            }
            matcher.from = i3;
            matcher.lookbehindTo = i4;
            return !zMatch && this.next.match(matcher, i2, charSequence);
        }
    }

    private static CharProperty union(final CharProperty charProperty, final CharProperty charProperty2) {
        return new CharProperty() { // from class: java.util.regex.Pattern.5
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // java.util.regex.Pattern.CharProperty
            boolean isSatisfiedBy(int i2) {
                return charProperty.isSatisfiedBy(i2) || charProperty2.isSatisfiedBy(i2);
            }
        };
    }

    private static CharProperty intersection(final CharProperty charProperty, final CharProperty charProperty2) {
        return new CharProperty() { // from class: java.util.regex.Pattern.6
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // java.util.regex.Pattern.CharProperty
            boolean isSatisfiedBy(int i2) {
                return charProperty.isSatisfiedBy(i2) && charProperty2.isSatisfiedBy(i2);
            }
        };
    }

    private static CharProperty setDifference(final CharProperty charProperty, final CharProperty charProperty2) {
        return new CharProperty() { // from class: java.util.regex.Pattern.7
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // java.util.regex.Pattern.CharProperty
            boolean isSatisfiedBy(int i2) {
                return !charProperty2.isSatisfiedBy(i2) && charProperty.isSatisfiedBy(i2);
            }
        };
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$Bound.class */
    static final class Bound extends Node {
        static int LEFT = 1;
        static int RIGHT = 2;
        static int BOTH = 3;
        static int NONE = 4;
        int type;
        boolean useUWORD;

        Bound(int i2, boolean z2) {
            this.type = i2;
            this.useUWORD = z2;
        }

        boolean isWord(int i2) {
            return this.useUWORD ? UnicodeProp.WORD.is(i2) : i2 == 95 || Character.isLetterOrDigit(i2);
        }

        int check(Matcher matcher, int i2, CharSequence charSequence) {
            boolean z2 = false;
            int i3 = matcher.from;
            int textLength = matcher.to;
            if (matcher.transparentBounds) {
                i3 = 0;
                textLength = matcher.getTextLength();
            }
            if (i2 > i3) {
                int iCodePointBefore = Character.codePointBefore(charSequence, i2);
                z2 = isWord(iCodePointBefore) || (Character.getType(iCodePointBefore) == 6 && Pattern.hasBaseCharacter(matcher, i2 - 1, charSequence));
            }
            boolean z3 = false;
            if (i2 < textLength) {
                int iCodePointAt = Character.codePointAt(charSequence, i2);
                z3 = isWord(iCodePointAt) || (Character.getType(iCodePointAt) == 6 && Pattern.hasBaseCharacter(matcher, i2, charSequence));
            } else {
                matcher.hitEnd = true;
                matcher.requireEnd = true;
            }
            return z2 ^ z3 ? z3 ? LEFT : RIGHT : NONE;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            return (check(matcher, i2, charSequence) & this.type) > 0 && this.next.match(matcher, i2, charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasBaseCharacter(Matcher matcher, int i2, CharSequence charSequence) {
        int i3 = !matcher.transparentBounds ? matcher.from : 0;
        for (int i4 = i2; i4 >= i3; i4--) {
            int iCodePointAt = Character.codePointAt(charSequence, i4);
            if (Character.isLetterOrDigit(iCodePointAt)) {
                return true;
            }
            if (Character.getType(iCodePointAt) != 6) {
                return false;
            }
        }
        return false;
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$BnM.class */
    static class BnM extends Node {
        int[] buffer;
        int[] lastOcc;
        int[] optoSft;

        static Node optimize(Node node) {
            if (!(node instanceof Slice)) {
                return node;
            }
            int[] iArr = ((Slice) node).buffer;
            int length = iArr.length;
            if (length < 4) {
                return node;
            }
            int[] iArr2 = new int[128];
            int[] iArr3 = new int[length];
            for (int i2 = 0; i2 < length; i2++) {
                iArr2[iArr[i2] & 127] = i2 + 1;
            }
            for (int i3 = length; i3 > 0; i3--) {
                int i4 = length - 1;
                while (true) {
                    if (i4 >= i3) {
                        if (iArr[i4] == iArr[i4 - i3]) {
                            iArr3[i4 - 1] = i3;
                            i4--;
                        }
                    } else {
                        while (i4 > 0) {
                            i4--;
                            iArr3[i4] = i3;
                        }
                    }
                }
            }
            iArr3[length - 1] = 1;
            if (node instanceof SliceS) {
                return new BnMS(iArr, iArr2, iArr3, node.next);
            }
            return new BnM(iArr, iArr2, iArr3, node.next);
        }

        BnM(int[] iArr, int[] iArr2, int[] iArr3, Node node) {
            this.buffer = iArr;
            this.lastOcc = iArr2;
            this.optoSft = iArr3;
            this.next = node;
        }

        @Override // java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int[] iArr = this.buffer;
            int length = iArr.length;
            int i3 = matcher.to - length;
            while (i2 <= i3) {
                int i4 = length - 1;
                while (true) {
                    if (i4 >= 0) {
                        char cCharAt = charSequence.charAt(i2 + i4);
                        if (cCharAt == iArr[i4]) {
                            i4--;
                        } else {
                            i2 += Math.max((i4 + 1) - this.lastOcc[cCharAt & 127], this.optoSft[i4]);
                            break;
                        }
                    } else {
                        matcher.first = i2;
                        if (this.next.match(matcher, i2 + length, charSequence)) {
                            matcher.first = i2;
                            matcher.groups[0] = matcher.first;
                            matcher.groups[1] = matcher.last;
                            return true;
                        }
                        i2++;
                    }
                }
            }
            matcher.hitEnd = true;
            return false;
        }

        @Override // java.util.regex.Pattern.Node
        boolean study(TreeInfo treeInfo) {
            treeInfo.minLength += this.buffer.length;
            treeInfo.maxValid = false;
            return this.next.study(treeInfo);
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$BnMS.class */
    static final class BnMS extends BnM {
        int lengthInChars;

        BnMS(int[] iArr, int[] iArr2, int[] iArr3, Node node) {
            super(iArr, iArr2, iArr3, node);
            for (int i2 = 0; i2 < this.buffer.length; i2++) {
                this.lengthInChars += Character.charCount(this.buffer[i2]);
            }
        }

        @Override // java.util.regex.Pattern.BnM, java.util.regex.Pattern.Node
        boolean match(Matcher matcher, int i2, CharSequence charSequence) {
            int[] iArr = this.buffer;
            int length = iArr.length;
            int i3 = matcher.to - this.lengthInChars;
            while (i2 <= i3) {
                int iCountChars = Pattern.countChars(charSequence, i2, length);
                int i4 = length - 1;
                while (true) {
                    if (iCountChars > 0) {
                        int iCodePointBefore = Character.codePointBefore(charSequence, i2 + iCountChars);
                        if (iCodePointBefore == iArr[i4]) {
                            iCountChars -= Character.charCount(iCodePointBefore);
                            i4--;
                        } else {
                            i2 += Pattern.countChars(charSequence, i2, Math.max((i4 + 1) - this.lastOcc[iCodePointBefore & 127], this.optoSft[i4]));
                            break;
                        }
                    } else {
                        matcher.first = i2;
                        if (!this.next.match(matcher, i2 + this.lengthInChars, charSequence)) {
                            i2 += Pattern.countChars(charSequence, i2, 1);
                        } else {
                            matcher.first = i2;
                            matcher.groups[0] = matcher.first;
                            matcher.groups[1] = matcher.last;
                            return true;
                        }
                    }
                }
            }
            matcher.hitEnd = true;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/regex/Pattern$CharPropertyNames.class */
    private static class CharPropertyNames {
        private static final HashMap<String, CharPropertyFactory> map = new HashMap<>();

        private CharPropertyNames() {
        }

        static CharProperty charPropertyFor(String str) {
            CharPropertyFactory charPropertyFactory = map.get(str);
            if (charPropertyFactory == null) {
                return null;
            }
            return charPropertyFactory.make();
        }

        /* loaded from: rt.jar:java/util/regex/Pattern$CharPropertyNames$CharPropertyFactory.class */
        private static abstract class CharPropertyFactory {
            abstract CharProperty make();

            private CharPropertyFactory() {
            }
        }

        private static void defCategory(String str, final int i2) {
            map.put(str, new CharPropertyFactory() { // from class: java.util.regex.Pattern.CharPropertyNames.1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // java.util.regex.Pattern.CharPropertyNames.CharPropertyFactory
                CharProperty make() {
                    return new Category(i2);
                }
            });
        }

        private static void defRange(String str, final int i2, final int i3) {
            map.put(str, new CharPropertyFactory() { // from class: java.util.regex.Pattern.CharPropertyNames.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // java.util.regex.Pattern.CharPropertyNames.CharPropertyFactory
                CharProperty make() {
                    return Pattern.rangeFor(i2, i3);
                }
            });
        }

        private static void defCtype(String str, final int i2) {
            map.put(str, new CharPropertyFactory() { // from class: java.util.regex.Pattern.CharPropertyNames.3
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // java.util.regex.Pattern.CharPropertyNames.CharPropertyFactory
                CharProperty make() {
                    return new Ctype(i2);
                }
            });
        }

        /* loaded from: rt.jar:java/util/regex/Pattern$CharPropertyNames$CloneableProperty.class */
        private static abstract class CloneableProperty extends CharProperty implements Cloneable {
            private CloneableProperty() {
                super();
            }

            /* renamed from: clone, reason: merged with bridge method [inline-methods] */
            public CloneableProperty m3523clone() {
                try {
                    return (CloneableProperty) super.clone();
                } catch (CloneNotSupportedException e2) {
                    throw new AssertionError(e2);
                }
            }
        }

        private static void defClone(String str, final CloneableProperty cloneableProperty) {
            map.put(str, new CharPropertyFactory() { // from class: java.util.regex.Pattern.CharPropertyNames.4
                {
                    super();
                }

                @Override // java.util.regex.Pattern.CharPropertyNames.CharPropertyFactory
                CharProperty make() {
                    return cloneableProperty.m3523clone();
                }
            });
        }

        static {
            defCategory("Cn", 1);
            defCategory("Lu", 2);
            defCategory("Ll", 4);
            defCategory("Lt", 8);
            defCategory("Lm", 16);
            defCategory("Lo", 32);
            defCategory("Mn", 64);
            defCategory("Me", 128);
            defCategory("Mc", 256);
            defCategory("Nd", 512);
            defCategory("Nl", 1024);
            defCategory("No", 2048);
            defCategory("Zs", 4096);
            defCategory("Zl", 8192);
            defCategory("Zp", 16384);
            defCategory("Cc", 32768);
            defCategory("Cf", 65536);
            defCategory("Co", 262144);
            defCategory("Cs", 524288);
            defCategory("Pd", 1048576);
            defCategory("Ps", 2097152);
            defCategory("Pe", 4194304);
            defCategory("Pc", 8388608);
            defCategory("Po", 16777216);
            defCategory("Sm", 33554432);
            defCategory("Sc", 67108864);
            defCategory("Sk", 134217728);
            defCategory("So", 268435456);
            defCategory("Pi", 536870912);
            defCategory("Pf", 1073741824);
            defCategory("L", 62);
            defCategory(PdfOps.M_TOKEN, 448);
            defCategory("N", 3584);
            defCategory(Constants.HASIDCALL_INDEX_SIG, CharacterType.SPACE_MASK);
            defCategory("C", CharacterType.CNTRL_MASK);
            defCategory(com.sun.org.apache.xml.internal.security.utils.Constants._TAG_P, CharacterType.PUNCT_MASK);
            defCategory(PdfOps.S_TOKEN, 503316480);
            defCategory("LC", 14);
            defCategory("LD", 574);
            defRange("L1", 0, 255);
            map.put("all", new CharPropertyFactory() { // from class: java.util.regex.Pattern.CharPropertyNames.5
                @Override // java.util.regex.Pattern.CharPropertyNames.CharPropertyFactory
                CharProperty make() {
                    return new All();
                }
            });
            defRange("ASCII", 0, 127);
            defCtype("Alnum", 1792);
            defCtype(SoftMask.SOFT_MASK_TYPE_ALPHA, 768);
            defCtype("Blank", 16384);
            defCtype("Cntrl", 8192);
            defRange("Digit", 48, 57);
            defCtype("Graph", 5888);
            defRange("Lower", 97, 122);
            defRange("Print", 32, 126);
            defCtype("Punct", 4096);
            defCtype("Space", 2048);
            defRange("Upper", 65, 90);
            defCtype("XDigit", 32768);
            defClone("javaLowerCase", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.6
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isLowerCase(i2);
                }
            });
            defClone("javaUpperCase", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.7
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isUpperCase(i2);
                }
            });
            defClone("javaAlphabetic", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.8
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isAlphabetic(i2);
                }
            });
            defClone("javaIdeographic", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.9
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isIdeographic(i2);
                }
            });
            defClone("javaTitleCase", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.10
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isTitleCase(i2);
                }
            });
            defClone("javaDigit", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.11
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isDigit(i2);
                }
            });
            defClone("javaDefined", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.12
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isDefined(i2);
                }
            });
            defClone("javaLetter", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.13
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isLetter(i2);
                }
            });
            defClone("javaLetterOrDigit", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.14
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isLetterOrDigit(i2);
                }
            });
            defClone("javaJavaIdentifierStart", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.15
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isJavaIdentifierStart(i2);
                }
            });
            defClone("javaJavaIdentifierPart", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.16
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isJavaIdentifierPart(i2);
                }
            });
            defClone("javaUnicodeIdentifierStart", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.17
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isUnicodeIdentifierStart(i2);
                }
            });
            defClone("javaUnicodeIdentifierPart", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.18
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isUnicodeIdentifierPart(i2);
                }
            });
            defClone("javaIdentifierIgnorable", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.19
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isIdentifierIgnorable(i2);
                }
            });
            defClone("javaSpaceChar", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.20
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isSpaceChar(i2);
                }
            });
            defClone("javaWhitespace", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.21
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isWhitespace(i2);
                }
            });
            defClone("javaISOControl", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.22
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isISOControl(i2);
                }
            });
            defClone("javaMirrored", new CloneableProperty() { // from class: java.util.regex.Pattern.CharPropertyNames.23
                @Override // java.util.regex.Pattern.CharProperty
                boolean isSatisfiedBy(int i2) {
                    return Character.isMirrored(i2);
                }
            });
        }
    }

    public Predicate<String> asPredicate() {
        return str -> {
            return matcher(str).find();
        };
    }

    public Stream<String> splitAsStream(final CharSequence charSequence) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<String>() { // from class: java.util.regex.Pattern.1MatcherIterator
            private final Matcher matcher;
            private int current;
            private String nextElement;
            private int emptyElementCount;

            {
                this.matcher = Pattern.this.matcher(charSequence);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (this.emptyElementCount == 0) {
                    String str = this.nextElement;
                    this.nextElement = null;
                    return str;
                }
                this.emptyElementCount--;
                return "";
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.nextElement != null || this.emptyElementCount > 0) {
                    return true;
                }
                if (this.current == charSequence.length()) {
                    return false;
                }
                while (this.matcher.find()) {
                    this.nextElement = charSequence.subSequence(this.current, this.matcher.start()).toString();
                    this.current = this.matcher.end();
                    if (!this.nextElement.isEmpty()) {
                        return true;
                    }
                    if (this.current > 0) {
                        this.emptyElementCount++;
                    }
                }
                this.nextElement = charSequence.subSequence(this.current, charSequence.length()).toString();
                this.current = charSequence.length();
                if (!this.nextElement.isEmpty()) {
                    return true;
                }
                this.emptyElementCount = 0;
                this.nextElement = null;
                return false;
            }
        }, 272), false);
    }
}
