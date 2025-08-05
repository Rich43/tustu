package javafx.util;

/* loaded from: jfxrt.jar:javafx/util/StringConverter.class */
public abstract class StringConverter<T> {
    public abstract String toString(T t2);

    public abstract T fromString(String str);
}
