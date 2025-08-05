package javax.swing;

/* loaded from: rt.jar:javax/swing/InputVerifier.class */
public abstract class InputVerifier {
    public abstract boolean verify(JComponent jComponent);

    public boolean shouldYieldFocus(JComponent jComponent) {
        return verify(jComponent);
    }
}
