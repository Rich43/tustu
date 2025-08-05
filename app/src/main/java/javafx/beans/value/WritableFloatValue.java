package javafx.beans.value;

/* loaded from: jfxrt.jar:javafx/beans/value/WritableFloatValue.class */
public interface WritableFloatValue extends WritableNumberValue {
    float get();

    void set(float f2);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    void setValue(Number number);
}
