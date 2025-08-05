package sun.awt.im;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.awt.im.spi.InputMethod;
import java.security.AccessController;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.JFrame;
import sun.awt.InputMethodSupport;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/awt/im/InputMethodContext.class */
public class InputMethodContext extends InputContext implements java.awt.im.spi.InputMethodContext {
    private boolean dispatchingCommittedText;
    private CompositionAreaHandler compositionAreaHandler;
    private Object compositionAreaHandlerLock = new Object();
    private static boolean belowTheSpotInputRequested;
    private boolean inputMethodSupportsBelowTheSpot;

    static {
        String property = (String) AccessController.doPrivileged(new GetPropertyAction("java.awt.im.style", null));
        if (property == null) {
            property = Toolkit.getProperty("java.awt.im.style", null);
        }
        belowTheSpotInputRequested = "below-the-spot".equals(property);
    }

    void setInputMethodSupportsBelowTheSpot(boolean z2) {
        this.inputMethodSupportsBelowTheSpot = z2;
    }

    boolean useBelowTheSpotInput() {
        return belowTheSpotInputRequested && this.inputMethodSupportsBelowTheSpot;
    }

    private boolean haveActiveClient() {
        Component clientComponent = getClientComponent();
        return (clientComponent == null || clientComponent.getInputMethodRequests() == null) ? false : true;
    }

    @Override // java.awt.im.spi.InputMethodContext
    public void dispatchInputMethodEvent(int i2, AttributedCharacterIterator attributedCharacterIterator, int i3, TextHitInfo textHitInfo, TextHitInfo textHitInfo2) {
        Component clientComponent = getClientComponent();
        if (clientComponent != null) {
            InputMethodEvent inputMethodEvent = new InputMethodEvent(clientComponent, i2, attributedCharacterIterator, i3, textHitInfo, textHitInfo2);
            if (haveActiveClient() && !useBelowTheSpotInput()) {
                clientComponent.dispatchEvent(inputMethodEvent);
            } else {
                getCompositionAreaHandler(true).processInputMethodEvent(inputMethodEvent);
            }
        }
    }

    synchronized void dispatchCommittedText(Component component, AttributedCharacterIterator attributedCharacterIterator, int i2) {
        if (i2 == 0 || attributedCharacterIterator.getEndIndex() <= attributedCharacterIterator.getBeginIndex()) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.dispatchingCommittedText = true;
        try {
            if (component.getInputMethodRequests() != null) {
                int beginIndex = attributedCharacterIterator.getBeginIndex();
                component.dispatchEvent(new InputMethodEvent(component, 1100, new AttributedString(attributedCharacterIterator, beginIndex, beginIndex + i2).getIterator(), i2, null, null));
            } else {
                char cFirst = attributedCharacterIterator.first();
                while (true) {
                    int i3 = i2;
                    i2--;
                    if (i3 <= 0 || cFirst == 65535) {
                        break;
                    }
                    component.dispatchEvent(new KeyEvent(component, 400, jCurrentTimeMillis, 0, 0, cFirst));
                    cFirst = attributedCharacterIterator.next();
                }
            }
        } finally {
            this.dispatchingCommittedText = false;
        }
    }

    @Override // sun.awt.im.InputContext, java.awt.im.InputContext
    public void dispatchEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof InputMethodEvent) {
            if (((Component) aWTEvent.getSource()).getInputMethodRequests() == null || (useBelowTheSpotInput() && !this.dispatchingCommittedText)) {
                getCompositionAreaHandler(true).processInputMethodEvent((InputMethodEvent) aWTEvent);
                return;
            }
            return;
        }
        if (!this.dispatchingCommittedText) {
            super.dispatchEvent(aWTEvent);
        }
    }

    private CompositionAreaHandler getCompositionAreaHandler(boolean z2) {
        CompositionAreaHandler compositionAreaHandler;
        synchronized (this.compositionAreaHandlerLock) {
            if (this.compositionAreaHandler == null) {
                this.compositionAreaHandler = new CompositionAreaHandler(this);
            }
            this.compositionAreaHandler.setClientComponent(getClientComponent());
            if (z2) {
                this.compositionAreaHandler.grabCompositionArea(false);
            }
            compositionAreaHandler = this.compositionAreaHandler;
        }
        return compositionAreaHandler;
    }

    void grabCompositionArea(boolean z2) {
        synchronized (this.compositionAreaHandlerLock) {
            if (this.compositionAreaHandler != null) {
                this.compositionAreaHandler.grabCompositionArea(z2);
            } else {
                CompositionAreaHandler.closeCompositionArea();
            }
        }
    }

    void releaseCompositionArea() {
        synchronized (this.compositionAreaHandlerLock) {
            if (this.compositionAreaHandler != null) {
                this.compositionAreaHandler.releaseCompositionArea();
            }
        }
    }

    boolean isCompositionAreaVisible() {
        if (this.compositionAreaHandler != null) {
            return this.compositionAreaHandler.isCompositionAreaVisible();
        }
        return false;
    }

    void setCompositionAreaVisible(boolean z2) {
        if (this.compositionAreaHandler != null) {
            this.compositionAreaHandler.setCompositionAreaVisible(z2);
        }
    }

    @Override // java.awt.im.InputMethodRequests
    public Rectangle getTextLocation(TextHitInfo textHitInfo) {
        return getReq().getTextLocation(textHitInfo);
    }

    @Override // java.awt.im.InputMethodRequests
    public TextHitInfo getLocationOffset(int i2, int i3) {
        return getReq().getLocationOffset(i2, i3);
    }

    @Override // java.awt.im.InputMethodRequests
    public int getInsertPositionOffset() {
        return getReq().getInsertPositionOffset();
    }

    @Override // java.awt.im.InputMethodRequests
    public AttributedCharacterIterator getCommittedText(int i2, int i3, AttributedCharacterIterator.Attribute[] attributeArr) {
        return getReq().getCommittedText(i2, i3, attributeArr);
    }

    @Override // java.awt.im.InputMethodRequests
    public int getCommittedTextLength() {
        return getReq().getCommittedTextLength();
    }

    @Override // java.awt.im.InputMethodRequests
    public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributeArr) {
        return getReq().cancelLatestCommittedText(attributeArr);
    }

    @Override // java.awt.im.InputMethodRequests
    public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributeArr) {
        return getReq().getSelectedText(attributeArr);
    }

    private InputMethodRequests getReq() {
        if (haveActiveClient() && !useBelowTheSpotInput()) {
            return getClientComponent().getInputMethodRequests();
        }
        return getCompositionAreaHandler(false);
    }

    @Override // java.awt.im.spi.InputMethodContext
    public Window createInputMethodWindow(String str, boolean z2) {
        return createInputMethodWindow(str, z2 ? this : null, false);
    }

    @Override // java.awt.im.spi.InputMethodContext
    public JFrame createInputMethodJFrame(String str, boolean z2) {
        return (JFrame) createInputMethodWindow(str, z2 ? this : null, true);
    }

    static Window createInputMethodWindow(String str, InputContext inputContext, boolean z2) {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (z2) {
            return new InputMethodJFrame(str, inputContext);
        }
        Object defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof InputMethodSupport) {
            return ((InputMethodSupport) defaultToolkit).createInputMethodWindow(str, inputContext);
        }
        throw new InternalError("Input methods must be supported");
    }

    @Override // sun.awt.im.InputContext, java.awt.im.spi.InputMethodContext
    public void enableClientWindowNotification(InputMethod inputMethod, boolean z2) {
        super.enableClientWindowNotification(inputMethod, z2);
    }

    void setCompositionAreaUndecorated(boolean z2) {
        if (this.compositionAreaHandler != null) {
            this.compositionAreaHandler.setCompositionAreaUndecorated(z2);
        }
    }
}
