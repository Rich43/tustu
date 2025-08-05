package java.lang.reflect;

/* loaded from: rt.jar:java/lang/reflect/Member.class */
public interface Member {
    public static final int PUBLIC = 0;
    public static final int DECLARED = 1;

    Class<?> getDeclaringClass();

    String getName();

    int getModifiers();

    boolean isSynthetic();
}
