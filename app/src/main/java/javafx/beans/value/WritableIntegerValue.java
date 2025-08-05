package javafx.beans.value;

/* loaded from: jfxrt.jar:javafx/beans/value/WritableIntegerValue.class */
public interface WritableIntegerValue extends WritableNumberValue {
    int get();

    void set(int i2);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    void setValue(Number number);
}
