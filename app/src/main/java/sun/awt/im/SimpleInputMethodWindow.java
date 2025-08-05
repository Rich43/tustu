package sun.awt.im;

import java.awt.Frame;

/* loaded from: rt.jar:sun/awt/im/SimpleInputMethodWindow.class */
public class SimpleInputMethodWindow extends Frame implements InputMethodWindow {
    InputContext inputContext;
    private static final long serialVersionUID = 5093376647036461555L;

    public SimpleInputMethodWindow(String str, InputContext inputContext) {
        super(str);
        this.inputContext = null;
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
