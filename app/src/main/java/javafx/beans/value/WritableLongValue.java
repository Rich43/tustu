package javafx.beans.value;

/* loaded from: jfxrt.jar:javafx/beans/value/WritableLongValue.class */
public interface WritableLongValue extends WritableNumberValue {
    long get();

    void set(long j2);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    void setValue(Number number);
}
