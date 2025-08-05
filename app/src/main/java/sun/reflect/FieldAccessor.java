package sun.reflect;

/* loaded from: rt.jar:sun/reflect/FieldAccessor.class */
public interface FieldAccessor {
    Object get(Object obj) throws IllegalArgumentException;

    boolean getBoolean(Object obj) throws IllegalArgumentException;

    byte getByte(Object obj) throws IllegalArgumentException;

    char getChar(Object obj) throws IllegalArgumentException;

    short getShort(Object obj) throws IllegalArgumentException;

    int getInt(Object obj) throws IllegalArgumentException;

    long getLong(Object obj) throws IllegalArgumentException;

    float getFloat(Object obj) throws IllegalArgumentException;

    double getDouble(Object obj) throws IllegalArgumentException;

    void set(Object obj, Object obj2) throws IllegalAccessException, IllegalArgumentException;

    void setBoolean(Object obj, boolean z2) throws IllegalAccessException, IllegalArgumentException;

    void setByte(Object obj, byte b2) throws IllegalAccessException, IllegalArgumentException;

    void setChar(Object obj, char c2) throws IllegalAccessException, IllegalArgumentException;

    void setShort(Object obj, short s2) throws IllegalAccessException, IllegalArgumentException;

    void setInt(Object obj, int i2) throws IllegalAccessException, IllegalArgumentException;

    void setLong(Object obj, long j2) throws IllegalAccessException, IllegalArgumentException;

    void setFloat(Object obj, float f2) throws IllegalAccessException, IllegalArgumentException;

    void setDouble(Object obj, double d2) throws IllegalAccessException, IllegalArgumentException;
}
