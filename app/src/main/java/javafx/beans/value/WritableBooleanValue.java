package javafx.beans.value;

/* loaded from: jfxrt.jar:javafx/beans/value/WritableBooleanValue.class */
public interface WritableBooleanValue extends WritableValue<Boolean> {
    boolean get();

    void set(boolean z2);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // 
    void setValue(Boolean bool);
}
