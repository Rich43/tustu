package jdk.nashorn.internal.runtime.regexp.joni;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/MatcherFactory.class */
public abstract class MatcherFactory {
    static final MatcherFactory DEFAULT = new MatcherFactory() { // from class: jdk.nashorn.internal.runtime.regexp.joni.MatcherFactory.1
        @Override // jdk.nashorn.internal.runtime.regexp.joni.MatcherFactory
        public Matcher create(Regex regex, char[] chars, int p2, int end) {
            return new ByteCodeMachine(regex, chars, p2, end);
        }
    };

    public abstract Matcher create(Regex regex, char[] cArr, int i2, int i3);
}
