package jdk.nashorn.internal.runtime.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import jdk.nashorn.internal.runtime.ParserException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/JdkRegExp.class */
public class JdkRegExp extends RegExp {
    private Pattern pattern;

    public JdkRegExp(String source, String flags) throws ParserException {
        super(source, flags);
        int intFlags = 0;
        intFlags = isIgnoreCase() ? 0 | 66 : intFlags;
        intFlags = isMultiline() ? intFlags | 8 : intFlags;
        try {
            try {
                RegExpScanner parsed = RegExpScanner.scan(source);
                if (parsed != null) {
                    this.pattern = Pattern.compile(parsed.getJavaPattern(), intFlags);
                    this.groupsInNegativeLookahead = parsed.getGroupsInNegativeLookahead();
                }
            } catch (StackOverflowError e3) {
                throw new RuntimeException(e3);
            } catch (PatternSyntaxException e2) {
                throwParserException("syntax", e2.getMessage());
            }
        } catch (PatternSyntaxException e4) {
            Pattern.compile(source, intFlags);
            throw e4;
        }
    }

    @Override // jdk.nashorn.internal.runtime.regexp.RegExp
    public RegExpMatcher match(String str) {
        if (this.pattern == null) {
            return null;
        }
        return new DefaultMatcher(str);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/JdkRegExp$DefaultMatcher.class */
    class DefaultMatcher implements RegExpMatcher {
        final String input;
        final Matcher defaultMatcher;

        DefaultMatcher(String input) {
            this.input = input;
            this.defaultMatcher = JdkRegExp.this.pattern.matcher(input);
        }

        @Override // jdk.nashorn.internal.runtime.regexp.RegExpMatcher
        public boolean search(int start) {
            return this.defaultMatcher.find(start);
        }

        @Override // jdk.nashorn.internal.runtime.regexp.RegExpMatcher
        public String getInput() {
            return this.input;
        }

        @Override // java.util.regex.MatchResult
        public int start() {
            return this.defaultMatcher.start();
        }

        @Override // java.util.regex.MatchResult
        public int start(int group) {
            return this.defaultMatcher.start(group);
        }

        @Override // java.util.regex.MatchResult
        public int end() {
            return this.defaultMatcher.end();
        }

        @Override // java.util.regex.MatchResult
        public int end(int group) {
            return this.defaultMatcher.end(group);
        }

        @Override // java.util.regex.MatchResult
        public String group() {
            return this.defaultMatcher.group();
        }

        @Override // java.util.regex.MatchResult
        public String group(int group) {
            return this.defaultMatcher.group(group);
        }

        @Override // java.util.regex.MatchResult
        public int groupCount() {
            return this.defaultMatcher.groupCount();
        }
    }
}
