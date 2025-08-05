package javafx.scene.effect;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/scene/effect/EffectChangeListener.class */
abstract class EffectChangeListener extends AbstractNotifyListener {
    protected ObservableValue registredOn;

    EffectChangeListener() {
    }

    public void register(ObservableValue value) {
        if (this.registredOn == value) {
            return;
        }
        if (this.registredOn != null) {
            this.registredOn.removeListener(getWeakListener());
        }
        this.registredOn = value;
        if (this.registredOn != null) {
            this.registredOn.addListener(getWeakListener());
        }
    }
}
