package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.text.HitInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TextFieldBehavior.class */
public class TextFieldBehavior extends TextInputControlBehavior<TextField> {
    private TextFieldSkin skin;
    private ContextMenu contextMenu;
    private TwoLevelFocusBehavior tlFocus;
    private ChangeListener<Scene> sceneListener;
    private ChangeListener<Node> focusOwnerListener;
    private boolean focusGainedByMouseClick;
    private boolean shiftDown;
    private boolean deferClick;

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TextFieldBehavior$TextInputTypes.class */
    enum TextInputTypes {
        TEXT_FIELD,
        PASSWORD_FIELD,
        EDITABLE_COMBO,
        TEXT_AREA
    }

    public TextFieldBehavior(TextField textField) {
        super(textField, TEXT_INPUT_BINDINGS);
        this.focusGainedByMouseClick = false;
        this.shiftDown = false;
        this.deferClick = false;
        this.contextMenu = new ContextMenu();
        if (IS_TOUCH_SUPPORTED) {
            this.contextMenu.getStyleClass().add("text-input-context-menu");
        }
        handleFocusChange();
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            handleFocusChange();
        });
        this.focusOwnerListener = (observable2, oldValue2, newValue2) -> {
            if (newValue2 == textField) {
                if (!this.focusGainedByMouseClick) {
                    textField.selectRange(textField.getLength(), 0);
                    return;
                }
                return;
            }
            textField.selectRange(0, 0);
        };
        WeakChangeListener<Node> weakFocusOwnerListener = new WeakChangeListener<>(this.focusOwnerListener);
        this.sceneListener = (observable3, oldValue3, newValue3) -> {
            if (oldValue3 != null) {
                oldValue3.focusOwnerProperty().removeListener(weakFocusOwnerListener);
            }
            if (newValue3 != null) {
                newValue3.focusOwnerProperty().addListener(weakFocusOwnerListener);
            }
        };
        textField.sceneProperty().addListener(new WeakChangeListener(this.sceneListener));
        if (textField.getScene() != null) {
            textField.getScene().focusOwnerProperty().addListener(weakFocusOwnerListener);
        }
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior(textField);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior, com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void handleFocusChange() {
        TextField textField = (TextField) getControl();
        if (textField.isFocused()) {
            if (PlatformUtil.isIOS()) {
                TextInputTypes type = TextInputTypes.TEXT_FIELD;
                if (textField.getClass().equals(PasswordField.class)) {
                    type = TextInputTypes.PASSWORD_FIELD;
                } else if (textField.getParent().getClass().equals(ComboBox.class)) {
                    type = TextInputTypes.EDITABLE_COMBO;
                }
                Bounds bounds = textField.getBoundsInParent();
                double w2 = bounds.getWidth();
                double h2 = bounds.getHeight();
                Affine3D trans = calculateNodeToSceneTransform(textField);
                String text = textField.getText();
                textField.getScene().getWindow().impl_getPeer().requestInput(text, type.ordinal(), w2, h2, trans.getMxx(), trans.getMxy(), trans.getMxz(), trans.getMxt(), trans.getMyx(), trans.getMyy(), trans.getMyz(), trans.getMyt(), trans.getMzx(), trans.getMzy(), trans.getMzz(), trans.getMzt());
            }
            if (!this.focusGainedByMouseClick) {
                setCaretAnimating(true);
                return;
            }
            return;
        }
        if (PlatformUtil.isIOS() && textField.getScene() != null) {
            textField.getScene().getWindow().impl_getPeer().releaseInput();
        }
        this.focusGainedByMouseClick = false;
        setCaretAnimating(false);
    }

    static Affine3D calculateNodeToSceneTransform(Node node) {
        Affine3D transform = new Affine3D();
        do {
            transform.preConcatenate(node.impl_getLeafTransform());
            node = node.getParent();
        } while (node != null);
        return transform;
    }

    public void setTextFieldSkin(TextFieldSkin skin) {
        this.skin = skin;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void fire(KeyEvent event) {
        TextField textField = (TextField) getControl();
        EventHandler<ActionEvent> onAction = textField.getOnAction();
        ActionEvent actionEvent = new ActionEvent(textField, null);
        textField.fireEvent(actionEvent);
        textField.commitValue();
        if (onAction == null && !actionEvent.isConsumed()) {
            forwardToParent(event);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void cancelEdit(KeyEvent event) {
        TextField textField = (TextField) getControl();
        if (textField.getTextFormatter() != null) {
            textField.cancelEdit();
        } else {
            forwardToParent(event);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void deleteChar(boolean previous) {
        this.skin.deleteChar(previous);
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void replaceText(int start, int end, String txt) {
        this.skin.replaceText(start, end, txt);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void deleteFromLineStart() {
        TextField textField = (TextField) getControl();
        int end = textField.getCaretPosition();
        if (end > 0) {
            replaceText(0, end, "");
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void setCaretAnimating(boolean play) {
        if (this.skin != null) {
            this.skin.setCaretAnimating(play);
        }
    }

    private void beep() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        TextField textField = (TextField) getControl();
        super.mousePressed(e2);
        if (!textField.isDisabled()) {
            if (!textField.isFocused()) {
                this.focusGainedByMouseClick = true;
                textField.requestFocus();
            }
            setCaretAnimating(false);
            if (e2.isPrimaryButtonDown() && !e2.isMiddleButtonDown() && !e2.isSecondaryButtonDown()) {
                HitInfo hit = this.skin.getIndex(e2.getX(), e2.getY());
                String text = textField.textProperty().getValueSafe();
                int i2 = Utils.getHitInsertionIndex(hit, text);
                int anchor = textField.getAnchor();
                int caretPosition = textField.getCaretPosition();
                if (e2.getClickCount() < 2 && (IS_TOUCH_SUPPORTED || (anchor != caretPosition && ((i2 > anchor && i2 < caretPosition) || (i2 < anchor && i2 > caretPosition))))) {
                    this.deferClick = true;
                } else if (!e2.isControlDown() && !e2.isAltDown() && !e2.isShiftDown() && !e2.isMetaDown()) {
                    switch (e2.getClickCount()) {
                        case 1:
                            mouseSingleClick(hit);
                            break;
                        case 2:
                            mouseDoubleClick(hit);
                            break;
                        case 3:
                            mouseTripleClick(hit);
                            break;
                    }
                } else if (e2.isShiftDown() && !e2.isControlDown() && !e2.isAltDown() && !e2.isMetaDown() && e2.getClickCount() == 1) {
                    this.shiftDown = true;
                    if (PlatformUtil.isMac()) {
                        textField.extendSelection(i2);
                    } else {
                        this.skin.positionCaret(hit, true);
                    }
                }
                this.skin.setForwardBias(hit.isLeading());
            }
        }
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseDragged(MouseEvent e2) {
        TextField textField = (TextField) getControl();
        if (!textField.isDisabled() && !this.deferClick && e2.isPrimaryButtonDown() && !e2.isMiddleButtonDown() && !e2.isSecondaryButtonDown() && !e2.isControlDown() && !e2.isAltDown() && !e2.isShiftDown() && !e2.isMetaDown()) {
            this.skin.positionCaret(this.skin.getIndex(e2.getX(), e2.getY()), true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseReleased(MouseEvent e2) {
        TextField textField = (TextField) getControl();
        super.mouseReleased(e2);
        if (!textField.isDisabled()) {
            setCaretAnimating(false);
            if (this.deferClick) {
                this.deferClick = false;
                this.skin.positionCaret(this.skin.getIndex(e2.getX(), e2.getY()), this.shiftDown);
                this.shiftDown = false;
            }
            setCaretAnimating(true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v15, types: [javafx.scene.Node, javafx.scene.control.Control] */
    /* JADX WARN: Type inference failed for: r1v22, types: [javafx.scene.Node, javafx.scene.control.Control] */
    /* JADX WARN: Type inference failed for: r1v26, types: [javafx.scene.Node, javafx.scene.control.Control] */
    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void contextMenuRequested(ContextMenuEvent e2) {
        Point2D menuPos;
        TextField textField = (TextField) getControl();
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        } else if (textField.getContextMenu() == null) {
            double screenX = e2.getScreenX();
            double screenY = e2.getScreenY();
            double sceneX = e2.getSceneX();
            if (IS_TOUCH_SUPPORTED) {
                if (textField.getSelection().getLength() == 0) {
                    this.skin.positionCaret(this.skin.getIndex(e2.getX(), e2.getY()), false);
                    menuPos = this.skin.getMenuPosition();
                } else {
                    menuPos = this.skin.getMenuPosition();
                    if (menuPos != null && (menuPos.getX() <= 0.0d || menuPos.getY() <= 0.0d)) {
                        this.skin.positionCaret(this.skin.getIndex(e2.getX(), e2.getY()), false);
                        menuPos = this.skin.getMenuPosition();
                    }
                }
                if (menuPos != null) {
                    Point2D p2 = ((TextField) getControl()).localToScene(menuPos);
                    Scene scene = ((TextField) getControl()).getScene();
                    Window window = scene.getWindow();
                    Point2D location = new Point2D(window.getX() + scene.getX() + p2.getX(), window.getY() + scene.getY() + p2.getY());
                    screenX = location.getX();
                    sceneX = p2.getX();
                    screenY = location.getY();
                }
            }
            this.skin.populateContextMenu(this.contextMenu);
            double menuWidth = this.contextMenu.prefWidth(-1.0d);
            double menuX = screenX - (IS_TOUCH_SUPPORTED ? menuWidth / 2.0d : 0.0d);
            Screen currentScreen = com.sun.javafx.util.Utils.getScreenForPoint(screenX, 0.0d);
            Rectangle2D bounds = currentScreen.getBounds();
            if (menuX < bounds.getMinX()) {
                ((TextField) getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", Double.valueOf(screenX));
                ((TextField) getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", Double.valueOf(sceneX));
                this.contextMenu.show((Node) getControl(), bounds.getMinX(), screenY);
            } else if (screenX + menuWidth > bounds.getMaxX()) {
                double leftOver = menuWidth - (bounds.getMaxX() - screenX);
                ((TextField) getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", Double.valueOf(screenX));
                ((TextField) getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", Double.valueOf(sceneX));
                this.contextMenu.show((Node) getControl(), screenX - leftOver, screenY);
            } else {
                ((TextField) getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", 0);
                ((TextField) getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", 0);
                this.contextMenu.show((Node) getControl(), menuX, screenY);
            }
        }
        e2.consume();
    }

    protected void mouseSingleClick(HitInfo hit) {
        this.skin.positionCaret(hit, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void mouseDoubleClick(HitInfo hit) {
        TextField textField = (TextField) getControl();
        textField.previousWord();
        if (PlatformUtil.isWindows()) {
            textField.selectNextWord();
        } else {
            textField.selectEndOfNextWord();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void mouseTripleClick(HitInfo hit) {
        ((TextField) getControl()).selectAll();
    }
}
