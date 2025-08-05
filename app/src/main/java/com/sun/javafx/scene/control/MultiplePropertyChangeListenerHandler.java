package com.sun.javafx.scene.control;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/MultiplePropertyChangeListenerHandler.class */
public final class MultiplePropertyChangeListenerHandler {
    private final Callback<String, Void> propertyChangedHandler;
    private Map<ObservableValue<?>, String> propertyReferenceMap = new HashMap();
    private final ChangeListener<Object> propertyChangedListener = new ChangeListener<Object>() { // from class: com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler.1
        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends Object> observableValue, Object oldValue, Object newValue) {
            MultiplePropertyChangeListenerHandler.this.propertyChangedHandler.call(MultiplePropertyChangeListenerHandler.this.propertyReferenceMap.get(observableValue));
        }
    };
    private final WeakChangeListener<Object> weakPropertyChangedListener = new WeakChangeListener<>(this.propertyChangedListener);

    public MultiplePropertyChangeListenerHandler(Callback<String, Void> propertyChangedHandler) {
        this.propertyChangedHandler = propertyChangedHandler;
    }

    public final void registerChangeListener(ObservableValue<?> property, String reference) {
        if (!this.propertyReferenceMap.containsKey(property)) {
            this.propertyReferenceMap.put(property, reference);
            property.addListener(this.weakPropertyChangedListener);
        }
    }

    public final void unregisterChangeListener(ObservableValue<?> property) {
        if (this.propertyReferenceMap.containsKey(property)) {
            this.propertyReferenceMap.remove(property);
            property.removeListener(this.weakPropertyChangedListener);
        }
    }

    public void dispose() {
        for (ObservableValue<?> value : this.propertyReferenceMap.keySet()) {
            value.removeListener(this.weakPropertyChangedListener);
        }
        this.propertyReferenceMap.clear();
    }
}
