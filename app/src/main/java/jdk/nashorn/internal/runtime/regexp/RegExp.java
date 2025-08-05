package jdk.nashorn.internal.runtime.regexp;

import jdk.nashorn.internal.runtime.BitVector;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ParserException;
import org.icepdf.core.util.PdfOps;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/RegExp.class */
public abstract class RegExp {
    private final String source;
    private boolean global;
    private boolean ignoreCase;
    private boolean multiline;
    protected BitVector groupsInNegativeLookahead;

    public abstract RegExpMatcher match(String str);

    protected RegExp(String source, String flags) throws ParserException {
        this.source = source.length() == 0 ? "(?:)" : source;
        for (int i2 = 0; i2 < flags.length(); i2++) {
            char ch = flags.charAt(i2);
            switch (ch) {
                case 'g':
                    if (this.global) {
                        throwParserException("repeated.flag", PdfOps.g_TOKEN);
                    }
                    this.global = true;
                    break;
                case 'i':
                    if (this.ignoreCase) {
                        throwParserException("repeated.flag", PdfOps.i_TOKEN);
                    }
                    this.ignoreCase = true;
                    break;
                case 'm':
                    if (this.multiline) {
                        throwParserException("repeated.flag", PdfOps.m_TOKEN);
                    }
                    this.multiline = true;
                    break;
                default:
                    throwParserException("unsupported.flag", Character.toString(ch));
                    break;
            }
        }
    }

    public String getSource() {
        return this.source;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return this.global;
    }

    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    public boolean isMultiline() {
        return this.multiline;
    }

    public BitVector getGroupsInNegativeLookahead() {
        return this.groupsInNegativeLookahead;
    }

    protected static void throwParserException(String key, String str) throws ParserException {
        throw new ParserException(ECMAErrors.getMessage("parser.error.regex." + key, str));
    }
}
