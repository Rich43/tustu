package sun.awt.im;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.lang.ref.WeakReference;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/* loaded from: rt.jar:sun/awt/im/CompositionAreaHandler.class */
class CompositionAreaHandler implements InputMethodListener, InputMethodRequests {
    private static CompositionArea compositionArea;
    private static CompositionAreaHandler compositionAreaOwner;
    private AttributedCharacterIterator composedText;
    private TextHitInfo caret = null;
    private WeakReference<Component> clientComponent = new WeakReference<>(null);
    private InputMethodContext inputMethodContext;
    private static Object compositionAreaLock = new Object();
    private static final AttributedCharacterIterator.Attribute[] IM_ATTRIBUTES = {TextAttribute.INPUT_METHOD_HIGHLIGHT};
    private static final AttributedCharacterIterator EMPTY_TEXT = new AttributedString("").getIterator();

    CompositionAreaHandler(InputMethodContext inputMethodContext) {
        this.inputMethodContext = inputMethodContext;
    }

    private void createCompositionArea() {
        synchronized (compositionAreaLock) {
            compositionArea = new CompositionArea();
            if (compositionAreaOwner != null) {
                compositionArea.setHandlerInfo(compositionAreaOwner, this.inputMethodContext);
            }
            Component component = this.clientComponent.get();
            if (component != null && component.getInputMethodRequests() != null && this.inputMethodContext.useBelowTheSpotInput()) {
                setCompositionAreaUndecorated(true);
            }
        }
    }

    void setClientComponent(Component component) {
        this.clientComponent = new WeakReference<>(component);
    }

    void grabCompositionArea(boolean z2) {
        synchronized (compositionAreaLock) {
            if (compositionAreaOwner != this) {
                compositionAreaOwner = this;
                if (compositionArea != null) {
                    compositionArea.setHandlerInfo(this, this.inputMethodContext);
                }
                if (z2) {
                    if (this.composedText != null && compositionArea == null) {
                        createCompositionArea();
                    }
                    if (compositionArea != null) {
                        compositionArea.setText(this.composedText, this.caret);
                    }
                }
            }
        }
    }

    void releaseCompositionArea() {
        synchronized (compositionAreaLock) {
            if (compositionAreaOwner == this) {
                compositionAreaOwner = null;
                if (compositionArea != null) {
                    compositionArea.setHandlerInfo(null, null);
                    compositionArea.setText(null, null);
                }
            }
        }
    }

    static void closeCompositionArea() {
        if (compositionArea != null) {
            synchronized (compositionAreaLock) {
                compositionAreaOwner = null;
                compositionArea.setHandlerInfo(null, null);
                compositionArea.setText(null, null);
            }
        }
    }

    boolean isCompositionAreaVisible() {
        if (compositionArea != null) {
            return compositionArea.isCompositionAreaVisible();
        }
        return false;
    }

    void setCompositionAreaVisible(boolean z2) {
        if (compositionArea != null) {
            compositionArea.setCompositionAreaVisible(z2);
        }
    }

    void processInputMethodEvent(InputMethodEvent inputMethodEvent) {
        if (inputMethodEvent.getID() == 1100) {
            inputMethodTextChanged(inputMethodEvent);
        } else {
            caretPositionChanged(inputMethodEvent);
        }
    }

    void setCompositionAreaUndecorated(boolean z2) {
        if (compositionArea != null) {
            compositionArea.setCompositionAreaUndecorated(z2);
        }
    }

    @Override // java.awt.event.InputMethodListener
    public void inputMethodTextChanged(InputMethodEvent inputMethodEvent) {
        AttributedCharacterIterator text = inputMethodEvent.getText();
        int committedCharacterCount = inputMethodEvent.getCommittedCharacterCount();
        this.composedText = null;
        this.caret = null;
        if (text != null && committedCharacterCount < text.getEndIndex() - text.getBeginIndex()) {
            if (compositionArea == null) {
                createCompositionArea();
            }
            AttributedString attributedString = new AttributedString(text, text.getBeginIndex() + committedCharacterCount, text.getEndIndex(), IM_ATTRIBUTES);
            attributedString.addAttribute(TextAttribute.FONT, compositionArea.getFont());
            this.composedText = attributedString.getIterator();
            this.caret = inputMethodEvent.getCaret();
        }
        if (compositionArea != null) {
            compositionArea.setText(this.composedText, this.caret);
        }
        if (committedCharacterCount > 0) {
            this.inputMethodContext.dispatchCommittedText((Component) inputMethodEvent.getSource(), text, committedCharacterCount);
            if (isCompositionAreaVisible()) {
                compositionArea.updateWindowLocation();
            }
        }
        inputMethodEvent.consume();
    }

    @Override // java.awt.event.InputMethodListener
    public void caretPositionChanged(InputMethodEvent inputMethodEvent) {
        if (compositionArea != null) {
            compositionArea.setCaret(inputMethodEvent.getCaret());
        }
        inputMethodEvent.consume();
    }

    InputMethodRequests getClientInputMethodRequests() {
        Component component = this.clientComponent.get();
        if (component != null) {
            return component.getInputMethodRequests();
        }
        return null;
    }

    @Override // java.awt.im.InputMethodRequests
    public Rectangle getTextLocation(TextHitInfo textHitInfo) {
        synchronized (compositionAreaLock) {
            if (compositionAreaOwner == this && isCompositionAreaVisible()) {
                return compositionArea.getTextLocation(textHitInfo);
            }
            if (this.composedText != null) {
                return new Rectangle(0, 0, 0, 10);
            }
            InputMethodRequests clientInputMethodRequests = getClientInputMethodRequests();
            if (clientInputMethodRequests != null) {
                return clientInputMethodRequests.getTextLocation(textHitInfo);
            }
            return new Rectangle(0, 0, 0, 10);
        }
    }

    @Override // java.awt.im.InputMethodRequests
    public TextHitInfo getLocationOffset(int i2, int i3) {
        synchronized (compositionAreaLock) {
            if (compositionAreaOwner == this && isCompositionAreaVisible()) {
                return compositionArea.getLocationOffset(i2, i3);
            }
            return null;
        }
    }

    @Override // java.awt.im.InputMethodRequests
    public int getInsertPositionOffset() {
        InputMethodRequests clientInputMethodRequests = getClientInputMethodRequests();
        if (clientInputMethodRequests != null) {
            return clientInputMethodRequests.getInsertPositionOffset();
        }
        return 0;
    }

    @Override // java.awt.im.InputMethodRequests
    public AttributedCharacterIterator getCommittedText(int i2, int i3, AttributedCharacterIterator.Attribute[] attributeArr) {
        InputMethodRequests clientInputMethodRequests = getClientInputMethodRequests();
        if (clientInputMethodRequests != null) {
            return clientInputMethodRequests.getCommittedText(i2, i3, attributeArr);
        }
        return EMPTY_TEXT;
    }

    @Override // java.awt.im.InputMethodRequests
    public int getCommittedTextLength() {
        InputMethodRequests clientInputMethodRequests = getClientInputMethodRequests();
        if (clientInputMethodRequests != null) {
            return clientInputMethodRequests.getCommittedTextLength();
        }
        return 0;
    }

    @Override // java.awt.im.InputMethodRequests
    public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributeArr) {
        InputMethodRequests clientInputMethodRequests = getClientInputMethodRequests();
        if (clientInputMethodRequests != null) {
            return clientInputMethodRequests.cancelLatestCommittedText(attributeArr);
        }
        return null;
    }

    @Override // java.awt.im.InputMethodRequests
    public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributeArr) {
        InputMethodRequests clientInputMethodRequests = getClientInputMethodRequests();
        if (clientInputMethodRequests != null) {
            return clientInputMethodRequests.getSelectedText(attributeArr);
        }
        return EMPTY_TEXT;
    }
}
