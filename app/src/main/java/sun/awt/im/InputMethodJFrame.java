package sun.awt.im;

import javax.swing.JFrame;

/* loaded from: rt.jar:sun/awt/im/InputMethodJFrame.class */
public class InputMethodJFrame extends JFrame implements InputMethodWindow {
    InputContext inputContext;
    private static final long serialVersionUID = -4705856747771842549L;

    public InputMethodJFrame(String str, InputContext inputContext) {
        super(str);
        this.inputContext = null;
        if (JFrame.isDefaultLookAndFeelDecorated()) {
            setUndecorated(true);
            getRootPane().setWindowDecorationStyle(0);
        }
        if (inputContext != null) {
            this.inputContext = inputContext;
        }
        setFocusableWindowState(false);
    }

    @Override // sun.awt.im.InputMethodWindow
    public void setInputContext(InputContext inputContext) {
        this.inputContext = inputContext;
    }

    @Override // java.awt.Window, java.awt.Component
    public java.awt.im.InputContext getInputContext() {
        if (this.inputContext != null) {
            return this.inputContext;
        }
        return super.getInputContext();
    }
}
