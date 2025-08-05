package javafx.collections;

/* loaded from: jfxrt.jar:javafx/collections/ObservableIntegerArray.class */
public interface ObservableIntegerArray extends ObservableArray<ObservableIntegerArray> {
    void copyTo(int i2, int[] iArr, int i3, int i4);

    void copyTo(int i2, ObservableIntegerArray observableIntegerArray, int i3, int i4);

    int get(int i2);

    void addAll(int... iArr);

    void addAll(ObservableIntegerArray observableIntegerArray);

    void addAll(int[] iArr, int i2, int i3);

    void addAll(ObservableIntegerArray observableIntegerArray, int i2, int i3);

    void setAll(int... iArr);

    void setAll(int[] iArr, int i2, int i3);

    void setAll(ObservableIntegerArray observableIntegerArray);

    void setAll(ObservableIntegerArray observableIntegerArray, int i2, int i3);

    void set(int i2, int[] iArr, int i3, int i4);

    void set(int i2, ObservableIntegerArray observableIntegerArray, int i3, int i4);

    void set(int i2, int i3);

    int[] toArray(int[] iArr);

    int[] toArray(int i2, int[] iArr, int i3);
}
