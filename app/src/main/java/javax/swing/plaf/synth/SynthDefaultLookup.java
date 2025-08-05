package javax.swing.plaf.synth;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthDefaultLookup.class */
class SynthDefaultLookup extends DefaultLookup {
    SynthDefaultLookup() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.swing.DefaultLookup
    public Object getDefault(JComponent jComponent, ComponentUI componentUI, String str) {
        if (!(componentUI instanceof SynthUI)) {
            return super.getDefault(jComponent, componentUI, str);
        }
        SynthContext context = ((SynthUI) componentUI).getContext(jComponent);
        Object obj = context.getStyle().get(context, str);
        context.dispose();
        return obj;
    }
}
