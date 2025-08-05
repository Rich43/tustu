package javax.swing.plaf.nimbus;

import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SliderTrackArrowShapeState.class */
class SliderTrackArrowShapeState extends State {
    SliderTrackArrowShapeState() {
        super("ArrowShape");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return jComponent.getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE;
    }
}
