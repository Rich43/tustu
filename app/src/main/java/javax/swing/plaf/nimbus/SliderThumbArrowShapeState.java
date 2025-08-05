package javax.swing.plaf.nimbus;

import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SliderThumbArrowShapeState.class */
class SliderThumbArrowShapeState extends State {
    SliderThumbArrowShapeState() {
        super("ArrowShape");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return jComponent.getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE;
    }
}
