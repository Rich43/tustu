package org.apache.commons.net.ftp.parser;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.net.ftp.FTPFileEntryParserImpl;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/RegexFTPFileEntryParserImpl.class */
public abstract class RegexFTPFileEntryParserImpl extends FTPFileEntryParserImpl {
    private Pattern pattern = null;
    private MatchResult result = null;
    protected Matcher _matcher_ = null;

    public RegexFTPFileEntryParserImpl(String regex) {
        compileRegex(regex, 0);
    }

    public RegexFTPFileEntryParserImpl(String regex, int flags) {
        compileRegex(regex, flags);
    }

    public boolean matches(String s2) {
        this.result = null;
        this._matcher_ = this.pattern.matcher(s2);
        if (this._matcher_.matches()) {
            this.result = this._matcher_.toMatchResult();
        }
        return null != this.result;
    }

    public int getGroupCnt() {
        if (this.result == null) {
            return 0;
        }
        return this.result.groupCount();
    }

    public String group(int matchnum) {
        if (this.result == null) {
            return null;
        }
        return this.result.group(matchnum);
    }

    public String getGroupsAsString() {
        StringBuilder b2 = new StringBuilder();
        for (int i2 = 1; i2 <= this.result.groupCount(); i2++) {
            b2.append(i2).append(") ").append(this.result.group(i2)).append(System.getProperty("line.separator"));
        }
        return b2.toString();
    }

    public boolean setRegex(String regex) {
        compileRegex(regex, 0);
        return true;
    }

    public boolean setRegex(String regex, int flags) {
        compileRegex(regex, flags);
        return true;
    }

    private void compileRegex(String regex, int flags) {
        try {
            this.pattern = Pattern.compile(regex, flags);
        } catch (PatternSyntaxException e2) {
            throw new IllegalArgumentException("Unparseable regex supplied: " + regex);
        }
    }
}
