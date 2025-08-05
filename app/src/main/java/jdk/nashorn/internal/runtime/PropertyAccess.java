package jdk.nashorn.internal.runtime;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/PropertyAccess.class */
public interface PropertyAccess {
    int getInt(Object obj, int i2);

    int getInt(double d2, int i2);

    int getInt(int i2, int i3);

    double getDouble(Object obj, int i2);

    double getDouble(double d2, int i2);

    double getDouble(int i2, int i3);

    Object get(Object obj);

    Object get(double d2);

    Object get(int i2);

    void set(Object obj, int i2, int i3);

    void set(Object obj, double d2, int i2);

    void set(Object obj, Object obj2, int i2);

    void set(double d2, int i2, int i3);

    void set(double d2, double d3, int i2);

    void set(double d2, Object obj, int i2);

    void set(int i2, int i3, int i4);

    void set(int i2, double d2, int i3);

    void set(int i2, Object obj, int i3);

    boolean has(Object obj);

    boolean has(int i2);

    boolean has(double d2);

    boolean hasOwnProperty(Object obj);

    boolean hasOwnProperty(int i2);

    boolean hasOwnProperty(double d2);

    boolean delete(int i2, boolean z2);

    boolean delete(double d2, boolean z2);

    boolean delete(Object obj, boolean z2);
}
