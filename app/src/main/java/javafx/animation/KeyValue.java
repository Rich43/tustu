package javafx.animation;

import javafx.beans.NamedArg;
import javafx.beans.value.WritableBooleanValue;
import javafx.beans.value.WritableDoubleValue;
import javafx.beans.value.WritableFloatValue;
import javafx.beans.value.WritableIntegerValue;
import javafx.beans.value.WritableLongValue;
import javafx.beans.value.WritableNumberValue;
import javafx.beans.value.WritableValue;

/* loaded from: jfxrt.jar:javafx/animation/KeyValue.class */
public final class KeyValue {
    private static final Interpolator DEFAULT_INTERPOLATOR;
    private final Type type;
    private final WritableValue<?> target;
    private final Object endValue;
    private final Interpolator interpolator;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Deprecated
    /* loaded from: jfxrt.jar:javafx/animation/KeyValue$Type.class */
    public enum Type {
        BOOLEAN,
        DOUBLE,
        FLOAT,
        INTEGER,
        LONG,
        OBJECT
    }

    static {
        $assertionsDisabled = !KeyValue.class.desiredAssertionStatus();
        DEFAULT_INTERPOLATOR = Interpolator.LINEAR;
    }

    @Deprecated
    public Type getType() {
        return this.type;
    }

    public WritableValue<?> getTarget() {
        return this.target;
    }

    public Object getEndValue() {
        return this.endValue;
    }

    public Interpolator getInterpolator() {
        return this.interpolator;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> KeyValue(@NamedArg("target") WritableValue<T> writableValue, @NamedArg("endValue") T endValue, @NamedArg("interpolator") Interpolator interpolator) {
        if (writableValue == 0) {
            throw new NullPointerException("Target needs to be specified");
        }
        if (interpolator == null) {
            throw new NullPointerException("Interpolator needs to be specified");
        }
        this.target = writableValue;
        this.endValue = endValue;
        this.interpolator = interpolator;
        this.type = writableValue instanceof WritableNumberValue ? writableValue instanceof WritableDoubleValue ? Type.DOUBLE : writableValue instanceof WritableIntegerValue ? Type.INTEGER : writableValue instanceof WritableFloatValue ? Type.FLOAT : writableValue instanceof WritableLongValue ? Type.LONG : Type.OBJECT : writableValue instanceof WritableBooleanValue ? Type.BOOLEAN : Type.OBJECT;
    }

    public <T> KeyValue(@NamedArg("target") WritableValue<T> target, @NamedArg("endValue") T endValue) {
        this(target, endValue, DEFAULT_INTERPOLATOR);
    }

    public String toString() {
        return "KeyValue [target=" + ((Object) this.target) + ", endValue=" + this.endValue + ", interpolator=" + ((Object) this.interpolator) + "]";
    }

    public int hashCode() {
        if (!$assertionsDisabled && (this.target == null || this.interpolator == null)) {
            throw new AssertionError();
        }
        int result = (31 * 1) + this.target.hashCode();
        return (31 * ((31 * result) + (this.endValue == null ? 0 : this.endValue.hashCode()))) + this.interpolator.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof KeyValue) {
            KeyValue keyValue = (KeyValue) obj;
            if ($assertionsDisabled || !(this.target == null || this.interpolator == null || keyValue.target == null || keyValue.interpolator == null)) {
                return this.target.equals(keyValue.target) && (this.endValue != null ? this.endValue.equals(keyValue.endValue) : keyValue.endValue == null) && this.interpolator.equals(keyValue.interpolator);
            }
            throw new AssertionError();
        }
        return false;
    }
}
