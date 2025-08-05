package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.text.HitInfo;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextArea;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TextAreaBehavior.class */
public class TextAreaBehavior extends TextInputControlBehavior<TextArea> {
    protected static final List<KeyBinding> TEXT_AREA_BINDINGS = new ArrayList();
    private TextAreaSkin skin;
    private ContextMenu contextMenu;
    private TwoLevelFocusBehavior tlFocus;
    private boolean focusGainedByMouseClick;
    private boolean shiftDown;
    private boolean deferClick;

    static {
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_PRESSED, "LineStart"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_PRESSED, "LineEnd"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "PreviousLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "PreviousLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "NextLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "NextLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, KeyEvent.KEY_PRESSED, "PreviousPage"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, KeyEvent.KEY_PRESSED, "NextPage"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "InsertNewLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.TAB, KeyEvent.KEY_PRESSED, "TraverseOrInsertTab"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_PRESSED, "SelectLineStart").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_PRESSED, "SelectLineEnd").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectPreviousLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectPreviousLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectNextLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectNextLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, KeyEvent.KEY_PRESSED, "SelectPreviousPage").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, KeyEvent.KEY_PRESSED, "SelectNextPage").shift());
        if (PlatformUtil.isMac()) {
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.LEFT, KeyEvent.KEY_PRESSED, "LineStart").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, KeyEvent.KEY_PRESSED, "LineStart").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, KeyEvent.KEY_PRESSED, "LineEnd").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, KeyEvent.KEY_PRESSED, "LineEnd").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "Home").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "Home").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "End").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "End").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.LEFT, KeyEvent.KEY_PRESSED, "SelectLineStartExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, KeyEvent.KEY_PRESSED, "SelectLineStartExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, KeyEvent.KEY_PRESSED, "SelectLineEndExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, KeyEvent.KEY_PRESSED, "SelectLineEndExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectHomeExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectHomeExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectEndExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectEndExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "ParagraphStart").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "ParagraphStart").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd").alt().shift());
        } else {
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "ParagraphStart").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "ParagraphStart").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "ParagraphEnd").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, KeyEvent.KEY_PRESSED, "SelectParagraphStart").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, KeyEvent.KEY_PRESSED, "SelectParagraphEnd").ctrl().shift());
        }
        TEXT_AREA_BINDINGS.addAll(TextInputControlBindings.BINDINGS);
        TEXT_AREA_BINDINGS.add(new KeyBinding(null, KeyEvent.KEY_PRESSED, "Consume"));
    }

    public TextAreaBehavior(TextArea textArea) {
        super(textArea, TEXT_AREA_BINDINGS);
        this.focusGainedByMouseClick = false;
        this.shiftDown = false;
        this.deferClick = false;
        this.contextMenu = new ContextMenu();
        if (IS_TOUCH_SUPPORTED) {
            this.contextMenu.getStyleClass().add("text-input-context-menu");
        }
        textArea.focusedProperty().addListener(new ChangeListener<Boolean>() { // from class: com.sun.javafx.scene.control.behavior.TextAreaBehavior.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                TextArea textArea2 = (TextArea) TextAreaBehavior.this.getControl();
                if (textArea2.isFocused()) {
                    if (PlatformUtil.isIOS()) {
                        Bounds bounds = textArea2.getBoundsInParent();
                        double w2 = bounds.getWidth();
                        double h2 = bounds.getHeight();
                        Affine3D trans = TextFieldBehavior.calculateNodeToSceneTransform(textArea2);
                        String text = textArea2.textProperty().getValueSafe();
                        textArea2.getScene().getWindow().impl_getPeer().requestInput(text, TextFieldBehavior.TextInputTypes.TEXT_AREA.ordinal(), w2, h2, trans.getMxx(), trans.getMxy(), trans.getMxz(), trans.getMxt(), trans.getMyx(), trans.getMyy(), trans.getMyz(), trans.getMyt(), trans.getMzx(), trans.getMzy(), trans.getMzz(), trans.getMzt());
                    }
                    if (!TextAreaBehavior.this.focusGainedByMouseClick) {
                        TextAreaBehavior.this.setCaretAnimating(true);
                        return;
                    }
                    return;
                }
                if (PlatformUtil.isIOS() && textArea2.getScene() != null) {
                    textArea2.getScene().getWindow().impl_getPeer().releaseInput();
                }
                TextAreaBehavior.this.focusGainedByMouseClick = false;
                TextAreaBehavior.this.setCaretAnimating(false);
            }
        });
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior(textArea);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior, com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    public void setTextAreaSkin(TextAreaSkin skin) {
        this.skin = skin;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior, com.sun.javafx.scene.control.behavior.BehaviorBase
    public void callAction(String name) {
        TextArea textInputControl = (TextArea) getControl();
        boolean done = false;
        if (textInputControl.isEditable()) {
            setEditing(true);
            done = true;
            if ("InsertNewLine".equals(name)) {
                insertNewLine();
            } else if ("TraverseOrInsertTab".equals(name)) {
                insertTab();
            } else {
                done = false;
            }
            setEditing(false);
        }
        if (!done) {
            done = true;
            if ("LineStart".equals(name)) {
                lineStart(false, false);
            } else if ("LineEnd".equals(name)) {
                lineEnd(false, false);
            } else if ("SelectLineStart".equals(name)) {
                lineStart(true, false);
            } else if ("SelectLineStartExtend".equals(name)) {
                lineStart(true, true);
            } else if ("SelectLineEnd".equals(name)) {
                lineEnd(true, false);
            } else if ("SelectLineEndExtend".equals(name)) {
                lineEnd(true, true);
            } else if ("PreviousLine".equals(name)) {
                this.skin.previousLine(false);
            } else if ("NextLine".equals(name)) {
                this.skin.nextLine(false);
            } else if ("SelectPreviousLine".equals(name)) {
                this.skin.previousLine(true);
            } else if ("SelectNextLine".equals(name)) {
                this.skin.nextLine(true);
            } else if ("ParagraphStart".equals(name)) {
                this.skin.paragraphStart(true, false);
            } else if ("ParagraphEnd".equals(name)) {
                this.skin.paragraphEnd(true, PlatformUtil.isWindows(), false);
            } else if ("SelectParagraphStart".equals(name)) {
                this.skin.paragraphStart(true, true);
            } else if ("SelectParagraphEnd".equals(name)) {
                this.skin.paragraphEnd(true, PlatformUtil.isWindows(), true);
            } else if ("PreviousPage".equals(name)) {
                this.skin.previousPage(false);
            } else if ("NextPage".equals(name)) {
                this.skin.nextPage(false);
            } else if ("SelectPreviousPage".equals(name)) {
                this.skin.previousPage(true);
            } else if ("SelectNextPage".equals(name)) {
                this.skin.nextPage(true);
            } else if ("TraverseOrInsertTab".equals(name)) {
                name = "TraverseNext";
                done = false;
            } else {
                done = false;
            }
        }
        if (!done) {
            super.callAction(name);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void insertNewLine() {
        TextArea textArea = (TextArea) getControl();
        textArea.replaceSelection("\n");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void insertTab() {
        TextArea textArea = (TextArea) getControl();
        textArea.replaceSelection("\t");
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void deleteChar(boolean previous) {
        this.skin.deleteChar(previous);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void deleteFromLineStart() {
        TextArea textArea = (TextArea) getControl();
        int end = textArea.getCaretPosition();
        if (end > 0) {
            lineStart(false, false);
            int start = textArea.getCaretPosition();
            if (end > start) {
                replaceText(start, end, "");
            }
        }
    }

    private void lineStart(boolean select, boolean extendSelection) {
        this.skin.lineStart(select, extendSelection);
    }

    private void lineEnd(boolean select, boolean extendSelection) {
        this.skin.lineEnd(select, extendSelection);
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void scrollCharacterToVisible(int index) {
        this.skin.scrollCharacterToVisible(index);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void replaceText(int start, int end, String txt) {
        ((TextArea) getControl()).replaceText(start, end, txt);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        TextArea textArea = (TextArea) getControl();
        super.mousePressed(e2);
        if (!textArea.isDisabled()) {
            if (!textArea.isFocused()) {
                this.focusGainedByMouseClick = true;
                textArea.requestFocus();
            }
            setCaretAnimating(false);
            if (e2.getButton() == MouseButton.PRIMARY && !e2.isMiddleButtonDown() && !e2.isSecondaryButtonDown()) {
                HitInfo hit = this.skin.getIndex(e2.getX(), e2.getY());
                int i2 = Utils.getHitInsertionIndex(hit, textArea.textProperty().getValueSafe());
                int anchor = textArea.getAnchor();
                int caretPosition = textArea.getCaretPosition();
                if (e2.getClickCount() < 2 && (e2.isSynthesized() || (anchor != caretPosition && ((i2 > anchor && i2 < caretPosition) || (i2 < anchor && i2 > caretPosition))))) {
                    this.deferClick = true;
                } else if (!e2.isControlDown() && !e2.isAltDown() && !e2.isShiftDown() && !e2.isMetaDown() && !e2.isShortcutDown()) {
                    switch (e2.getClickCount()) {
                        case 1:
                            this.skin.positionCaret(hit, false, false);
                            break;
                        case 2:
                            mouseDoubleClick(hit);
                            break;
                        case 3:
                            mouseTripleClick(hit);
                            break;
                    }
                } else if (e2.isShiftDown() && !e2.isControlDown() && !e2.isAltDown() && !e2.isMetaDown() && !e2.isShortcutDown() && e2.getClickCount() == 1) {
                    this.shiftDown = true;
                    if (PlatformUtil.isMac()) {
                        textArea.extendSelection(i2);
                    } else {
                        this.skin.positionCaret(hit, true, false);
                    }
                }
            }
            if (this.contextMenu.isShowing()) {
                this.contextMenu.hide();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseDragged(MouseEvent e2) {
        TextArea textArea = (TextArea) getControl();
        if (!textArea.isDisabled() && !e2.isSynthesized() && e2.getButton() == MouseButton.PRIMARY && !e2.isMiddleButtonDown() && !e2.isSecondaryButtonDown() && !e2.isControlDown() && !e2.isAltDown() && !e2.isShiftDown() && !e2.isMetaDown()) {
            this.skin.positionCaret(this.skin.getIndex(e2.getX(), e2.getY()), true, false);
        }
        this.deferClick = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseReleased(MouseEvent e2) {
        TextArea textArea = (TextArea) getControl();
        super.mouseReleased(e2);
        if (!textArea.isDisabled()) {
            setCaretAnimating(false);
            if (this.deferClick) {
                this.deferClick = false;
                this.skin.positionCaret(this.skin.getIndex(e2.getX(), e2.getY()), this.shiftDown, false);
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
        TextArea textArea = (TextArea) getControl();
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        } else if (textArea.getContextMenu() == null) {
            double screenX = e2.getScreenX();
            double screenY = e2.getScreenY();
            double sceneX = e2.getSceneX();
            if (IS_TOUCH_SUPPORTED) {
                if (textArea.getSelection().getLength() == 0) {
                    this.skin.positionCaret(this.skin.getIndex(e2.getX(), e2.getY()), false, false);
                    menuPos = this.skin.getMenuPosition();
                } else {
                    menuPos = this.skin.getMenuPosition();
                    if (menuPos != null && (menuPos.getX() <= 0.0d || menuPos.getY() <= 0.0d)) {
                        this.skin.positionCaret(this.skin.getIndex(e2.getX(), e2.getY()), false, false);
                        menuPos = this.skin.getMenuPosition();
                    }
                }
                if (menuPos != null) {
                    Point2D p2 = ((TextArea) getControl()).localToScene(menuPos);
                    Scene scene = ((TextArea) getControl()).getScene();
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
                ((TextArea) getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", Double.valueOf(screenX));
                ((TextArea) getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", Double.valueOf(sceneX));
                this.contextMenu.show((Node) getControl(), bounds.getMinX(), screenY);
            } else if (screenX + menuWidth > bounds.getMaxX()) {
                double leftOver = menuWidth - (bounds.getMaxX() - screenX);
                ((TextArea) getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", Double.valueOf(screenX));
                ((TextArea) getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", Double.valueOf(sceneX));
                this.contextMenu.show((Node) getControl(), screenX - leftOver, screenY);
            } else {
                ((TextArea) getControl()).getProperties().put("CONTEXT_MENU_SCREEN_X", 0);
                ((TextArea) getControl()).getProperties().put("CONTEXT_MENU_SCENE_X", 0);
                this.contextMenu.show((Node) getControl(), menuX, screenY);
            }
        }
        e2.consume();
    }

    @Override // com.sun.javafx.scene.control.behavior.TextInputControlBehavior
    protected void setCaretAnimating(boolean play) {
        this.skin.setCaretAnimating(play);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void mouseDoubleClick(HitInfo hit) {
        TextArea textArea = (TextArea) getControl();
        textArea.previousWord();
        if (PlatformUtil.isWindows()) {
            textArea.selectNextWord();
        } else {
            textArea.selectEndOfNextWord();
        }
    }

    protected void mouseTripleClick(HitInfo hit) {
        this.skin.paragraphStart(false, false);
        this.skin.paragraphEnd(false, PlatformUtil.isWindows(), true);
    }
}
