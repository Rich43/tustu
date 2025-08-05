package javafx.collections;

/* loaded from: jfxrt.jar:javafx/collections/ObservableFloatArray.class */
public interface ObservableFloatArray extends ObservableArray<ObservableFloatArray> {
    void copyTo(int i2, float[] fArr, int i3, int i4);

    void copyTo(int i2, ObservableFloatArray observableFloatArray, int i3, int i4);

    float get(int i2);

    void addAll(float... fArr);

    void addAll(ObservableFloatArray observableFloatArray);

    void addAll(float[] fArr, int i2, int i3);

    void addAll(ObservableFloatArray observableFloatArray, int i2, int i3);

    void setAll(float... fArr);

    void setAll(float[] fArr, int i2, int i3);

    void setAll(ObservableFloatArray observableFloatArray);

    void setAll(ObservableFloatArray observableFloatArray, int i2, int i3);

    void set(int i2, float[] fArr, int i3, int i4);

    void set(int i2, ObservableFloatArray observableFloatArray, int i3, int i4);

    void set(int i2, float f2);

    float[] toArray(float[] fArr);

    float[] toArray(int i2, float[] fArr, int i3);
}
