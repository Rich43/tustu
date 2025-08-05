package javax.swing.plaf.nimbus;

import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SliderArrowShapeState.class */
class SliderArrowShapeState extends State {
    SliderArrowShapeState() {
        super("ArrowShape");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return jComponent.getClientProperty("Slider.paintThumbArrowShape") == Boolean.TRUE;
    }
}
