package java.text;

/* loaded from: rt.jar:java/text/CharacterIterator.class */
public interface CharacterIterator extends Cloneable {
    public static final char DONE = 65535;

    char first();

    char last();

    char current();

    char next();

    char previous();

    char setIndex(int i2);

    int getBeginIndex();

    int getEndIndex();

    int getIndex();

    Object clone();
}
