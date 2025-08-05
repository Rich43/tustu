package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.skin.TextInputControlSkin;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.beans.InvalidationListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TextInputControlBehavior.class */
public abstract class TextInputControlBehavior<T extends TextInputControl> extends BehaviorBase<T> {
    protected static final List<KeyBinding> TEXT_INPUT_BINDINGS = new ArrayList();
    T textInputControl;
    private KeyEvent lastEvent;
    private InvalidationListener textListener;
    private Bidi bidi;
    private Boolean mixed;
    private Boolean rtlText;
    private boolean editing;

    protected abstract void deleteChar(boolean z2);

    protected abstract void replaceText(int i2, int i3, String str);

    protected abstract void setCaretAnimating(boolean z2);

    protected abstract void deleteFromLineStart();

    static {
        TEXT_INPUT_BINDINGS.addAll(TextInputControlBindings.BINDINGS);
        for (KeyCode key : KeyCode.values()) {
            if (key.isFunctionKey()) {
                TEXT_INPUT_BINDINGS.add(new KeyBinding(key, KeyEvent.KEY_PRESSED, null));
            }
        }
        TEXT_INPUT_BINDINGS.add(new KeyBinding(null, KeyEvent.KEY_PRESSED, "Consume"));
    }

    public TextInputControlBehavior(T textInputControl, List<KeyBinding> bindings) {
        super(textInputControl, bindings);
        this.textListener = observable -> {
            invalidateBidi();
        };
        this.bidi = null;
        this.mixed = null;
        this.rtlText = null;
        this.editing = false;
        this.textInputControl = textInputControl;
        textInputControl.textProperty().addListener(this.textListener);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        this.textInputControl.textProperty().removeListener(this.textListener);
        super.dispose();
    }

    protected void scrollCharacterToVisible(int index) {
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callActionForEvent(KeyEvent e2) {
        this.lastEvent = e2;
        super.callActionForEvent(e2);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void callAction(String name) {
        TextInputControl textInputControl = getControl();
        boolean done = false;
        setCaretAnimating(false);
        if (textInputControl.isEditable()) {
            setEditing(true);
            done = true;
            if ("InputCharacter".equals(name)) {
                defaultKeyTyped(this.lastEvent);
            } else if ("Cut".equals(name)) {
                cut();
            } else if ("Paste".equals(name)) {
                paste();
            } else if ("DeleteFromLineStart".equals(name)) {
                deleteFromLineStart();
            } else if ("DeletePreviousChar".equals(name)) {
                deletePreviousChar();
            } else if ("DeleteNextChar".equals(name)) {
                deleteNextChar();
            } else if ("DeletePreviousWord".equals(name)) {
                deletePreviousWord();
            } else if ("DeleteNextWord".equals(name)) {
                deleteNextWord();
            } else if ("DeleteSelection".equals(name)) {
                deleteSelection();
            } else if ("Undo".equals(name)) {
                textInputControl.undo();
            } else if ("Redo".equals(name)) {
                textInputControl.redo();
            } else {
                done = false;
            }
            setEditing(false);
        }
        if (!done) {
            done = true;
            if ("Copy".equals(name)) {
                textInputControl.copy();
            } else if ("SelectBackward".equals(name)) {
                textInputControl.selectBackward();
            } else if ("SelectForward".equals(name)) {
                textInputControl.selectForward();
            } else if ("SelectLeft".equals(name)) {
                selectLeft();
            } else if ("SelectRight".equals(name)) {
                selectRight();
            } else if ("PreviousWord".equals(name)) {
                previousWord();
            } else if ("NextWord".equals(name)) {
                nextWord();
            } else if ("LeftWord".equals(name)) {
                leftWord();
            } else if ("RightWord".equals(name)) {
                rightWord();
            } else if ("SelectPreviousWord".equals(name)) {
                selectPreviousWord();
            } else if ("SelectNextWord".equals(name)) {
                selectNextWord();
            } else if ("SelectLeftWord".equals(name)) {
                selectLeftWord();
            } else if ("SelectRightWord".equals(name)) {
                selectRightWord();
            } else if ("SelectWord".equals(name)) {
                selectWord();
            } else if ("SelectAll".equals(name)) {
                textInputControl.selectAll();
            } else if ("Home".equals(name)) {
                textInputControl.home();
            } else if ("End".equals(name)) {
                textInputControl.end();
            } else if ("Forward".equals(name)) {
                textInputControl.forward();
            } else if ("Backward".equals(name)) {
                textInputControl.backward();
            } else if ("Right".equals(name)) {
                nextCharacterVisually(true);
            } else if ("Left".equals(name)) {
                nextCharacterVisually(false);
            } else if ("Fire".equals(name)) {
                fire(this.lastEvent);
            } else if ("Cancel".equals(name)) {
                cancelEdit(this.lastEvent);
            } else if ("Unselect".equals(name)) {
                textInputControl.deselect();
            } else if ("SelectHome".equals(name)) {
                selectHome();
            } else if ("SelectEnd".equals(name)) {
                selectEnd();
            } else if ("SelectHomeExtend".equals(name)) {
                selectHomeExtend();
            } else if ("SelectEndExtend".equals(name)) {
                selectEndExtend();
            } else if ("ToParent".equals(name)) {
                forwardToParent(this.lastEvent);
            } else if ("UseVK".equals(name) && PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                ((TextInputControlSkin) textInputControl.getSkin()).toggleUseVK();
            } else {
                done = false;
            }
        }
        setCaretAnimating(true);
        if (!done) {
            if (!"TraverseNext".equals(name)) {
                if (!"TraversePrevious".equals(name)) {
                    super.callAction(name);
                    return;
                } else {
                    traversePrevious();
                    return;
                }
            }
            traverseNext();
        }
    }

    private void defaultKeyTyped(KeyEvent event) {
        TextInputControl textInput = getControl();
        if (!textInput.isEditable() || textInput.isDisabled()) {
            return;
        }
        String character = event.getCharacter();
        if (character.length() == 0) {
            return;
        }
        if (((!event.isControlDown() && !event.isAltDown() && (!PlatformUtil.isMac() || !event.isMetaDown())) || ((event.isControlDown() || PlatformUtil.isMac()) && event.isAltDown())) && character.charAt(0) > 31 && character.charAt(0) != 127 && !event.isMetaDown()) {
            IndexRange selection = textInput.getSelection();
            int start = selection.getStart();
            int end = selection.getEnd();
            replaceText(start, end, character);
            scrollCharacterToVisible(start);
        }
    }

    private void invalidateBidi() {
        this.bidi = null;
        this.mixed = null;
        this.rtlText = null;
    }

    private Bidi getBidi() {
        if (this.bidi == null) {
            this.bidi = new Bidi(this.textInputControl.textProperty().getValueSafe(), this.textInputControl.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? 1 : 0);
        }
        return this.bidi;
    }

    protected boolean isMixed() {
        if (this.mixed == null) {
            this.mixed = Boolean.valueOf(getBidi().isMixed());
        }
        return this.mixed.booleanValue();
    }

    protected boolean isRTLText() {
        if (this.rtlText == null) {
            Bidi bidi = getBidi();
            this.rtlText = Boolean.valueOf(bidi.isRightToLeft() || (isMixed() && this.textInputControl.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT));
        }
        return this.rtlText.booleanValue();
    }

    private void nextCharacterVisually(boolean moveRight) {
        if (isMixed()) {
            TextInputControlSkin<?, ?> skin = (TextInputControlSkin) this.textInputControl.getSkin();
            skin.nextCharacterVisually(moveRight);
        } else if (moveRight != isRTLText()) {
            this.textInputControl.forward();
        } else {
            this.textInputControl.backward();
        }
    }

    private void selectLeft() {
        if (isRTLText()) {
            this.textInputControl.selectForward();
        } else {
            this.textInputControl.selectBackward();
        }
    }

    private void selectRight() {
        if (isRTLText()) {
            this.textInputControl.selectBackward();
        } else {
            this.textInputControl.selectForward();
        }
    }

    private void deletePreviousChar() {
        deleteChar(true);
    }

    private void deleteNextChar() {
        deleteChar(false);
    }

    protected void deletePreviousWord() {
        TextInputControl textInputControl = getControl();
        int end = textInputControl.getCaretPosition();
        if (end > 0) {
            textInputControl.previousWord();
            int start = textInputControl.getCaretPosition();
            replaceText(start, end, "");
        }
    }

    protected void deleteNextWord() {
        TextInputControl textInputControl = getControl();
        int start = textInputControl.getCaretPosition();
        if (start < textInputControl.getLength()) {
            nextWord();
            int end = textInputControl.getCaretPosition();
            replaceText(start, end, "");
        }
    }

    private void deleteSelection() {
        TextInputControl textInputControl = getControl();
        IndexRange selection = textInputControl.getSelection();
        if (selection.getLength() > 0) {
            deleteChar(false);
        }
    }

    private void cut() {
        TextInputControl textInputControl = getControl();
        textInputControl.cut();
    }

    private void paste() {
        TextInputControl textInputControl = getControl();
        textInputControl.paste();
    }

    protected void selectPreviousWord() {
        getControl().selectPreviousWord();
    }

    protected void selectNextWord() {
        TextInputControl textInputControl = getControl();
        if (PlatformUtil.isMac() || PlatformUtil.isLinux()) {
            textInputControl.selectEndOfNextWord();
        } else {
            textInputControl.selectNextWord();
        }
    }

    private void selectLeftWord() {
        if (isRTLText()) {
            selectNextWord();
        } else {
            selectPreviousWord();
        }
    }

    private void selectRightWord() {
        if (isRTLText()) {
            selectPreviousWord();
        } else {
            selectNextWord();
        }
    }

    protected void selectWord() {
        TextInputControl textInputControl = getControl();
        textInputControl.previousWord();
        if (PlatformUtil.isWindows()) {
            textInputControl.selectNextWord();
        } else {
            textInputControl.selectEndOfNextWord();
        }
    }

    protected void previousWord() {
        getControl().previousWord();
    }

    protected void nextWord() {
        TextInputControl textInputControl = getControl();
        if (PlatformUtil.isMac() || PlatformUtil.isLinux()) {
            textInputControl.endOfNextWord();
        } else {
            textInputControl.nextWord();
        }
    }

    private void leftWord() {
        if (isRTLText()) {
            nextWord();
        } else {
            previousWord();
        }
    }

    private void rightWord() {
        if (isRTLText()) {
            previousWord();
        } else {
            nextWord();
        }
    }

    protected void fire(KeyEvent event) {
    }

    protected void cancelEdit(KeyEvent event) {
        forwardToParent(event);
    }

    protected void forwardToParent(KeyEvent event) {
        if (getControl().getParent() != null) {
            getControl().getParent().fireEvent(event);
        }
    }

    private void selectHome() {
        getControl().selectHome();
    }

    private void selectEnd() {
        getControl().selectEnd();
    }

    private void selectHomeExtend() {
        getControl().extendSelection(0);
    }

    private void selectEndExtend() {
        TextInputControl textInputControl = getControl();
        textInputControl.extendSelection(textInputControl.getLength());
    }

    protected void setEditing(boolean b2) {
        this.editing = b2;
    }

    public boolean isEditing() {
        return this.editing;
    }
}
