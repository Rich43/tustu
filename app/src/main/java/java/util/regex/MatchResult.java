package java.util.regex;

/* loaded from: rt.jar:java/util/regex/MatchResult.class */
public interface MatchResult {
    int start();

    int start(int i2);

    int end();

    int end(int i2);

    String group();

    String group(int i2);

    int groupCount();
}
