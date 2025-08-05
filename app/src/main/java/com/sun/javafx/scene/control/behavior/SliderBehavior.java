package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/SliderBehavior.class */
public class SliderBehavior extends BehaviorBase<Slider> {
    protected static final List<KeyBinding> SLIDER_BINDINGS = new ArrayList();
    private TwoLevelFocusBehavior tlFocus;

    static {
        SLIDER_BINDINGS.add(new KeyBinding(KeyCode.F4, "TraverseDebug").alt().ctrl().shift());
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.LEFT, "DecrementValue"));
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_LEFT, "DecrementValue"));
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.UP, "IncrementValue").vertical());
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_UP, "IncrementValue").vertical());
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.RIGHT, "IncrementValue"));
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_RIGHT, "IncrementValue"));
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.DOWN, "DecrementValue").vertical());
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_DOWN, "DecrementValue").vertical());
        SLIDER_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_RELEASED, "Home"));
        SLIDER_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_RELEASED, "End"));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected String matchActionForEvent(KeyEvent e2) {
        String action = super.matchActionForEvent(e2);
        if (action != null) {
            if (e2.getCode() == KeyCode.LEFT || e2.getCode() == KeyCode.KP_LEFT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    action = getControl().getOrientation() == Orientation.HORIZONTAL ? "IncrementValue" : "DecrementValue";
                }
            } else if ((e2.getCode() == KeyCode.RIGHT || e2.getCode() == KeyCode.KP_RIGHT) && getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                action = getControl().getOrientation() == Orientation.HORIZONTAL ? "DecrementValue" : "IncrementValue";
            }
        }
        return action;
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if (!"Home".equals(name)) {
            if (!"End".equals(name)) {
                if (!"IncrementValue".equals(name)) {
                    if (!"DecrementValue".equals(name)) {
                        super.callAction(name);
                        return;
                    } else {
                        decrementValue();
                        return;
                    }
                }
                incrementValue();
                return;
            }
            end();
            return;
        }
        home();
    }

    public SliderBehavior(Slider slider) {
        super(slider, SLIDER_BINDINGS);
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior(slider);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    public void trackPress(MouseEvent e2, double position) {
        Slider slider = getControl();
        if (!slider.isFocused()) {
            slider.requestFocus();
        }
        if (slider.getOrientation().equals(Orientation.HORIZONTAL)) {
            slider.adjustValue((position * (slider.getMax() - slider.getMin())) + slider.getMin());
        } else {
            slider.adjustValue(((1.0d - position) * (slider.getMax() - slider.getMin())) + slider.getMin());
        }
    }

    public void thumbPressed(MouseEvent e2, double position) {
        Slider slider = getControl();
        if (!slider.isFocused()) {
            slider.requestFocus();
        }
        slider.setValueChanging(true);
    }

    public void thumbDragged(MouseEvent e2, double position) {
        Slider slider = getControl();
        slider.setValue(com.sun.javafx.util.Utils.clamp(slider.getMin(), (position * (slider.getMax() - slider.getMin())) + slider.getMin(), slider.getMax()));
    }

    public void thumbReleased(MouseEvent e2) {
        Slider slider = getControl();
        slider.setValueChanging(false);
        slider.adjustValue(slider.getValue());
    }

    void home() {
        Slider slider = getControl();
        slider.adjustValue(slider.getMin());
    }

    void decrementValue() {
        Slider slider = getControl();
        if (slider.isSnapToTicks()) {
            slider.adjustValue(slider.getValue() - computeIncrement());
        } else {
            slider.decrement();
        }
    }

    void end() {
        Slider slider = getControl();
        slider.adjustValue(slider.getMax());
    }

    void incrementValue() {
        Slider slider = getControl();
        if (slider.isSnapToTicks()) {
            slider.adjustValue(slider.getValue() + computeIncrement());
        } else {
            slider.increment();
        }
    }

    double computeIncrement() {
        double tickSpacing;
        Slider slider = getControl();
        if (slider.getMinorTickCount() != 0) {
            tickSpacing = slider.getMajorTickUnit() / (Math.max(slider.getMinorTickCount(), 0) + 1);
        } else {
            tickSpacing = slider.getMajorTickUnit();
        }
        if (slider.getBlockIncrement() > 0.0d && slider.getBlockIncrement() < tickSpacing) {
            return tickSpacing;
        }
        return slider.getBlockIncrement();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/SliderBehavior$SliderKeyBinding.class */
    public static class SliderKeyBinding extends OrientedKeyBinding {
        public SliderKeyBinding(KeyCode code, String action) {
            super(code, action);
        }

        public SliderKeyBinding(KeyCode code, EventType<KeyEvent> type, String action) {
            super(code, type, action);
        }

        @Override // com.sun.javafx.scene.control.behavior.OrientedKeyBinding
        public boolean getVertical(Control control) {
            return ((Slider) control).getOrientation() == Orientation.VERTICAL;
        }
    }
}
