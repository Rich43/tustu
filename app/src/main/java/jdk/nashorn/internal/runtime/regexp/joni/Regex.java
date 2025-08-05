package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm;
import jdk.nashorn.internal.runtime.regexp.joni.constants.RegexState;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/Regex.class */
public final class Regex implements RegexState {
    int[] code;
    int codeLength;
    boolean stackNeeded;
    Object[] operands;
    int operandLength;
    int numMem;
    int numRepeat;
    int numNullCheck;
    int captureHistory;
    int btMemStart;
    int btMemEnd;
    int stackPopLevel;
    int[] repeatRangeLo;
    int[] repeatRangeHi;
    WarnCallback warnings;
    MatcherFactory factory;
    protected Analyser analyser;
    int options;
    final int caseFoldFlag;
    SearchAlgorithm searchAlgorithm;
    int thresholdLength;
    int anchor;
    int anchorDmin;
    int anchorDmax;
    int subAnchor;
    char[] exact;
    int exactP;
    int exactEnd;
    byte[] map;
    int[] intMap;
    int[] intMapBackward;
    int dMin;
    int dMax;
    char[][] templates;
    int templateNum;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Regex.class.desiredAssertionStatus();
    }

    public Regex(CharSequence cs) {
        this(cs.toString());
    }

    public Regex(String str) {
        this(str.toCharArray(), 0, str.length(), 0);
    }

    public Regex(char[] chars) {
        this(chars, 0, chars.length, 0);
    }

    public Regex(char[] chars, int p2, int end) {
        this(chars, p2, end, 0);
    }

    public Regex(char[] chars, int p2, int end, int option) {
        this(chars, p2, end, option, Syntax.RUBY, WarnCallback.DEFAULT);
    }

    public Regex(char[] chars, int p2, int end, int option, Syntax syntax) {
        this(chars, p2, end, option, 1073741824, syntax, WarnCallback.DEFAULT);
    }

    public Regex(char[] chars, int p2, int end, int option, WarnCallback warnings) {
        this(chars, p2, end, option, Syntax.RUBY, warnings);
    }

    public Regex(char[] chars, int p2, int end, int option, Syntax syntax, WarnCallback warnings) {
        this(chars, p2, end, option, 1073741824, syntax, warnings);
    }

    public Regex(char[] chars, int p2, int end, int optionp, int caseFoldFlag, Syntax syntax, WarnCallback warnings) {
        int option;
        if ((optionp & 384) == 384) {
            throw new ValueException(ErrorMessages.ERR_INVALID_COMBINATION_OF_OPTIONS);
        }
        if ((optionp & 64) != 0) {
            int option2 = optionp | syntax.options;
            option = option2 & (-9);
        } else {
            option = optionp | syntax.options;
        }
        this.options = option;
        this.caseFoldFlag = caseFoldFlag;
        this.warnings = warnings;
        this.analyser = new Analyser(new ScanEnvironment(this, syntax), chars, p2, end);
        this.analyser.compile();
        this.warnings = null;
    }

    public synchronized MatcherFactory compile() {
        if (this.factory == null && this.analyser != null) {
            new ArrayCompiler(this.analyser).compile();
            this.analyser = null;
        }
        if ($assertionsDisabled || this.factory != null) {
            return this.factory;
        }
        throw new AssertionError();
    }

    public Matcher matcher(char[] chars) {
        return matcher(chars, 0, chars.length);
    }

    public Matcher matcher(char[] chars, int p2, int end) {
        MatcherFactory matcherFactory = this.factory;
        if (matcherFactory == null) {
            matcherFactory = compile();
        }
        return matcherFactory.create(this, chars, p2, end);
    }

    public WarnCallback getWarnings() {
        return this.warnings;
    }

    public int numberOfCaptures() {
        return this.numMem;
    }

    void setupBMSkipMap() {
        char[] chars = this.exact;
        int p2 = this.exactP;
        int end = this.exactEnd;
        int len = end - p2;
        if (len < 256) {
            if (this.map == null) {
                this.map = new byte[256];
            }
            for (int i2 = 0; i2 < 256; i2++) {
                this.map[i2] = (byte) len;
            }
            for (int i3 = 0; i3 < len - 1; i3++) {
                this.map[chars[p2 + i3] & 255] = (byte) ((len - 1) - i3);
            }
            return;
        }
        if (this.intMap == null) {
            this.intMap = new int[256];
        }
        for (int i4 = 0; i4 < len - 1; i4++) {
            this.intMap[chars[p2 + i4] & 255] = (len - 1) - i4;
        }
    }

    void setExactInfo(OptExactInfo e2) {
        if (e2.length == 0) {
            return;
        }
        this.exact = e2.chars;
        this.exactP = 0;
        this.exactEnd = e2.length;
        if (e2.ignoreCase) {
            this.searchAlgorithm = new SearchAlgorithm.SLOW_IC(this);
        } else if (e2.length >= 2) {
            setupBMSkipMap();
            this.searchAlgorithm = SearchAlgorithm.BM;
        } else {
            this.searchAlgorithm = SearchAlgorithm.SLOW;
        }
        this.dMin = e2.mmd.min;
        this.dMax = e2.mmd.max;
        if (this.dMin != Integer.MAX_VALUE) {
            this.thresholdLength = this.dMin + (this.exactEnd - this.exactP);
        }
    }

    void setOptimizeMapInfo(OptMapInfo m2) {
        this.map = m2.map;
        this.searchAlgorithm = SearchAlgorithm.MAP;
        this.dMin = m2.mmd.min;
        this.dMax = m2.mmd.max;
        if (this.dMin != Integer.MAX_VALUE) {
            this.thresholdLength = this.dMin + 1;
        }
    }

    void setSubAnchor(OptAnchorInfo anc) {
        this.subAnchor |= anc.leftAnchor & 2;
        this.subAnchor |= anc.rightAnchor & 32;
    }

    void clearOptimizeInfo() {
        this.searchAlgorithm = SearchAlgorithm.NONE;
        this.anchor = 0;
        this.anchorDmax = 0;
        this.anchorDmin = 0;
        this.subAnchor = 0;
        this.exact = null;
        this.exactEnd = 0;
        this.exactP = 0;
    }

    public String optimizeInfoToString() {
        StringBuilder s2 = new StringBuilder();
        s2.append("optimize: ").append(this.searchAlgorithm.getName()).append("\n");
        s2.append("  anchor:     ").append(OptAnchorInfo.anchorToString(this.anchor));
        if ((this.anchor & 24) != 0) {
            s2.append(MinMaxLen.distanceRangeToString(this.anchorDmin, this.anchorDmax));
        }
        s2.append("\n");
        if (this.searchAlgorithm != SearchAlgorithm.NONE) {
            s2.append("  sub anchor: ").append(OptAnchorInfo.anchorToString(this.subAnchor)).append("\n");
        }
        s2.append("dmin: ").append(this.dMin).append(" dmax: ").append(this.dMax).append("\n");
        s2.append("threshold length: ").append(this.thresholdLength).append("\n");
        if (this.exact != null) {
            s2.append("exact: [").append(this.exact, this.exactP, this.exactEnd - this.exactP).append("]: length: ").append(this.exactEnd - this.exactP).append("\n");
        } else if (this.searchAlgorithm == SearchAlgorithm.MAP) {
            int n2 = 0;
            for (int i2 = 0; i2 < 256; i2++) {
                if (this.map[i2] != 0) {
                    n2++;
                }
            }
            s2.append("map: n = ").append(n2).append("\n");
            if (n2 > 0) {
                int c2 = 0;
                s2.append("[");
                for (int i3 = 0; i3 < 256; i3++) {
                    if (this.map[i3] != 0) {
                        if (c2 > 0) {
                            s2.append(", ");
                        }
                        c2++;
                        s2.append((char) i3);
                    }
                }
                s2.append("]\n");
            }
        }
        return s2.toString();
    }

    public int getOptions() {
        return this.options;
    }

    public String dumpTree() {
        if (this.analyser == null) {
            return null;
        }
        return this.analyser.root.toString();
    }

    public String dumpByteCode() {
        compile();
        return new ByteCodePrinter(this).byteCodeListToString();
    }
}
