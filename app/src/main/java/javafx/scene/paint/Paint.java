package javafx.scene.paint;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.tk.Toolkit;

/* loaded from: jfxrt.jar:javafx/scene/paint/Paint.class */
public abstract class Paint {
    abstract Object acc_getPlatformPaint();

    public abstract boolean isOpaque();

    static {
        Toolkit.setPaintAccessor(new Toolkit.PaintAccessor() { // from class: javafx.scene.paint.Paint.1
            @Override // com.sun.javafx.tk.Toolkit.PaintAccessor
            public boolean isMutable(Paint paint) {
                return paint.acc_isMutable();
            }

            @Override // com.sun.javafx.tk.Toolkit.PaintAccessor
            public Object getPlatformPaint(Paint paint) {
                return paint.acc_getPlatformPaint();
            }

            @Override // com.sun.javafx.tk.Toolkit.PaintAccessor
            public void addListener(Paint paint, AbstractNotifyListener platformChangeListener) {
                paint.acc_addListener(platformChangeListener);
            }

            @Override // com.sun.javafx.tk.Toolkit.PaintAccessor
            public void removeListener(Paint paint, AbstractNotifyListener platformChangeListener) {
                paint.acc_removeListener(platformChangeListener);
            }
        });
    }

    Paint() {
    }

    boolean acc_isMutable() {
        return false;
    }

    void acc_addListener(AbstractNotifyListener platformChangeListener) {
        throw new UnsupportedOperationException("Not Supported.");
    }

    void acc_removeListener(AbstractNotifyListener platformChangeListener) {
        throw new UnsupportedOperationException("Not Supported.");
    }

    public static Paint valueOf(String value) {
        if (value == null) {
            throw new NullPointerException("paint must be specified");
        }
        if (value.startsWith("linear-gradient(")) {
            return LinearGradient.valueOf(value);
        }
        if (value.startsWith("radial-gradient(")) {
            return RadialGradient.valueOf(value);
        }
        return Color.valueOf(value);
    }
}
