package sun.awt.im;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.im.spi.InputMethod;

/* loaded from: rt.jar:sun/awt/im/InputMethodAdapter.class */
public abstract class InputMethodAdapter implements InputMethod {
    private Component clientComponent;

    public abstract void disableInputMethod();

    public abstract String getNativeInputMethodInfo();

    void setClientComponent(Component component) {
        this.clientComponent = component;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Component getClientComponent() {
        return this.clientComponent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean haveActiveClient() {
        return (this.clientComponent == null || this.clientComponent.getInputMethodRequests() == null) ? false : true;
    }

    protected void setAWTFocussedComponent(Component component) {
    }

    protected boolean supportsBelowTheSpot() {
        return false;
    }

    protected void stopListening() {
    }

    @Override // java.awt.im.spi.InputMethod
    public void notifyClientWindowChange(Rectangle rectangle) {
    }

    @Override // java.awt.im.spi.InputMethod
    public void reconvert() {
        throw new UnsupportedOperationException();
    }
}
