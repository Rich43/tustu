package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ComboBoxPopupControl.class */
public abstract class ComboBoxPopupControl<T> extends ComboBoxBaseSkin<T> {
    protected PopupControl popup;
    public static final String COMBO_BOX_STYLE_CLASS = "combo-box-popup";
    private boolean popupNeedsReconfiguring;
    private final ComboBoxBase<T> comboBoxBase;
    private TextField textField;
    private EventHandler<MouseEvent> textFieldMouseEventHandler;
    private EventHandler<DragEvent> textFieldDragEventHandler;
    private String initialTextFieldValue;

    protected abstract Node getPopupContent();

    protected abstract TextField getEditor();

    protected abstract StringConverter<T> getConverter();

    public ComboBoxPopupControl(ComboBoxBase<T> comboBoxBase, ComboBoxBaseBehavior<T> behavior) {
        super(comboBoxBase, behavior);
        this.popupNeedsReconfiguring = true;
        this.textFieldMouseEventHandler = event -> {
            ComboBoxBase<T> comboBoxBase2 = (ComboBoxBase) getSkinnable();
            if (!event.getTarget().equals(comboBoxBase2)) {
                comboBoxBase2.fireEvent(event.copyFor((Object) comboBoxBase2, (EventTarget) comboBoxBase2));
                event.consume();
            }
        };
        this.textFieldDragEventHandler = event2 -> {
            ComboBoxBase<T> comboBoxBase2 = (ComboBoxBase) getSkinnable();
            if (!event2.getTarget().equals(comboBoxBase2)) {
                comboBoxBase2.fireEvent(event2.copyFor((Object) comboBoxBase2, (EventTarget) comboBoxBase2));
                event2.consume();
            }
        };
        this.initialTextFieldValue = null;
        this.comboBoxBase = comboBoxBase;
        this.textField = getEditor() != null ? getEditableInputNode() : null;
        if (this.textField != null) {
            getChildren().add(this.textField);
        }
        comboBoxBase.focusedProperty().addListener((ov, t2, hasFocus) -> {
            if (getEditor() != null) {
                ((FakeFocusTextField) this.textField).setFakeFocus(hasFocus.booleanValue());
                if (!hasFocus.booleanValue()) {
                    setTextFromTextFieldIntoComboBoxValue();
                }
            }
        });
        comboBoxBase.addEventFilter(KeyEvent.ANY, ke -> {
            if (this.textField == null || getEditor() == null) {
                handleKeyEvent(ke, false);
                return;
            }
            if (ke.getTarget().equals(this.textField)) {
            }
            switch (ke.getCode()) {
                case ESCAPE:
                case F10:
                    break;
                case ENTER:
                    handleKeyEvent(ke, true);
                    break;
                default:
                    this.textField.fireEvent(ke.copyFor((Object) this.textField, (EventTarget) this.textField));
                    ke.consume();
                    break;
            }
        });
        if (comboBoxBase.getOnInputMethodTextChanged() == null) {
            comboBoxBase.setOnInputMethodTextChanged(event3 -> {
                if (this.textField != null && getEditor() != null && comboBoxBase.getScene().getFocusOwner() == comboBoxBase && this.textField.getOnInputMethodTextChanged() != null) {
                    this.textField.getOnInputMethodTextChanged().handle(event3);
                }
            });
        }
        comboBoxBase.setImpl_traversalEngine(new ParentTraversalEngine(comboBoxBase, new Algorithm() { // from class: com.sun.javafx.scene.control.skin.ComboBoxPopupControl.1
            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node select(Node owner, Direction dir, TraversalContext context) {
                return null;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectFirst(TraversalContext context) {
                return null;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectLast(TraversalContext context) {
                return null;
            }
        }));
        updateEditable();
    }

    protected PopupControl getPopup() {
        if (this.popup == null) {
            createPopup();
        }
        return this.popup;
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    public void show() {
        if (getSkinnable() == 0) {
            throw new IllegalStateException("ComboBox is null");
        }
        Node content = getPopupContent();
        if (content == null) {
            throw new IllegalStateException("Popup node is null");
        }
        if (getPopup().isShowing()) {
            return;
        }
        positionAndShowPopup();
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    public void hide() {
        if (this.popup != null && this.popup.isShowing()) {
            this.popup.hide();
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [javafx.scene.Node, javafx.scene.control.Control] */
    private Point2D getPrefPopupPosition() {
        return com.sun.javafx.util.Utils.pointRelativeTo((Node) getSkinnable(), getPopupContent(), HPos.CENTER, VPos.BOTTOM, 0.0d, 0.0d, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void positionAndShowPopup() {
        PopupControl _popup = getPopup();
        _popup.getScene().setNodeOrientation(((ComboBoxBase) getSkinnable()).getEffectiveNodeOrientation());
        Node popupContent = getPopupContent();
        sizePopup();
        Point2D p2 = getPrefPopupPosition();
        this.popupNeedsReconfiguring = true;
        reconfigurePopup();
        ComboBoxBase<T> comboBoxBase = (ComboBoxBase) getSkinnable();
        _popup.show(comboBoxBase.getScene().getWindow(), snapPosition(p2.getX()), snapPosition(p2.getY()));
        popupContent.requestFocus();
        sizePopup();
    }

    private void sizePopup() {
        Node popupContent = getPopupContent();
        if (popupContent instanceof Region) {
            Region r2 = (Region) popupContent;
            double prefHeight = snapSize(r2.prefHeight(0.0d));
            double minHeight = snapSize(r2.minHeight(0.0d));
            double maxHeight = snapSize(r2.maxHeight(0.0d));
            double h2 = snapSize(Math.min(Math.max(prefHeight, minHeight), Math.max(minHeight, maxHeight)));
            double prefWidth = snapSize(r2.prefWidth(h2));
            double minWidth = snapSize(r2.minWidth(h2));
            double maxWidth = snapSize(r2.maxWidth(h2));
            double w2 = snapSize(Math.min(Math.max(prefWidth, minWidth), Math.max(minWidth, maxWidth)));
            popupContent.resize(w2, h2);
            return;
        }
        popupContent.autosize();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void createPopup() {
        this.popup = new PopupControl() { // from class: com.sun.javafx.scene.control.skin.ComboBoxPopupControl.2
            {
                setSkin(new Skin<Skinnable>() { // from class: com.sun.javafx.scene.control.skin.ComboBoxPopupControl.2.1
                    /* JADX WARN: Type inference failed for: r0v3, types: [javafx.scene.control.Control, javafx.scene.control.Skinnable] */
                    @Override // javafx.scene.control.Skin
                    public Skinnable getSkinnable() {
                        return ComboBoxPopupControl.this.getSkinnable();
                    }

                    @Override // javafx.scene.control.Skin
                    public Node getNode() {
                        return ComboBoxPopupControl.this.getPopupContent();
                    }

                    @Override // javafx.scene.control.Skin
                    public void dispose() {
                    }
                });
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [javafx.css.Styleable, javafx.scene.control.Control] */
            @Override // javafx.scene.control.PopupControl, javafx.css.Styleable
            public Styleable getStyleableParent() {
                return ComboBoxPopupControl.this.getSkinnable();
            }
        };
        this.popup.getStyleClass().add(COMBO_BOX_STYLE_CLASS);
        this.popup.setConsumeAutoHidingEvents(false);
        this.popup.setAutoHide(true);
        this.popup.setAutoFix(true);
        this.popup.setHideOnEscape(true);
        this.popup.setOnAutoHide(e2 -> {
            getBehavior().onAutoHide();
        });
        this.popup.addEventHandler(MouseEvent.MOUSE_CLICKED, t2 -> {
            getBehavior().onAutoHide();
        });
        this.popup.addEventHandler(WindowEvent.WINDOW_HIDDEN, t3 -> {
            ((ComboBoxBase) getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE);
        });
        InvalidationListener layoutPosListener = o2 -> {
            this.popupNeedsReconfiguring = true;
            reconfigurePopup();
        };
        ((ComboBoxBase) getSkinnable()).layoutXProperty().addListener(layoutPosListener);
        ((ComboBoxBase) getSkinnable()).layoutYProperty().addListener(layoutPosListener);
        ((ComboBoxBase) getSkinnable()).widthProperty().addListener(layoutPosListener);
        ((ComboBoxBase) getSkinnable()).heightProperty().addListener(layoutPosListener);
        ((ComboBoxBase) getSkinnable()).sceneProperty().addListener(o3 -> {
            if (((ObservableValue) o3).getValue2() == null) {
                hide();
            }
        });
    }

    void reconfigurePopup() {
        if (this.popup == null) {
            return;
        }
        boolean isShowing = this.popup.isShowing();
        if (isShowing && this.popupNeedsReconfiguring) {
            this.popupNeedsReconfiguring = false;
            Point2D p2 = getPrefPopupPosition();
            Node popupContent = getPopupContent();
            double minWidth = popupContent.prefWidth(-1.0d);
            double minHeight = popupContent.prefHeight(-1.0d);
            if (p2.getX() > -1.0d) {
                this.popup.setAnchorX(p2.getX());
            }
            if (p2.getY() > -1.0d) {
                this.popup.setAnchorY(p2.getY());
            }
            if (minWidth > -1.0d) {
                this.popup.setMinWidth(minWidth);
            }
            if (minHeight > -1.0d) {
                this.popup.setMinHeight(minHeight);
            }
            Bounds b2 = popupContent.getLayoutBounds();
            double currentWidth = b2.getWidth();
            double currentHeight = b2.getHeight();
            double newWidth = currentWidth < minWidth ? minWidth : currentWidth;
            double newHeight = currentHeight < minHeight ? minHeight : currentHeight;
            if (newWidth != currentWidth || newHeight != currentHeight) {
                popupContent.resize(newWidth, newHeight);
                if (popupContent instanceof Region) {
                    ((Region) popupContent).setMinSize(newWidth, newHeight);
                    ((Region) popupContent).setPrefSize(newWidth, newHeight);
                }
            }
        }
    }

    protected TextField getEditableInputNode() {
        if (this.textField == null && getEditor() != null) {
            this.textField = getEditor();
            this.textField.setFocusTraversable(false);
            this.textField.promptTextProperty().bind(this.comboBoxBase.promptTextProperty());
            this.textField.tooltipProperty().bind(this.comboBoxBase.tooltipProperty());
            this.initialTextFieldValue = this.textField.getText();
        }
        return this.textField;
    }

    protected void setTextFromTextFieldIntoComboBoxValue() {
        StringConverter<T> c2;
        if (getEditor() != null && (c2 = getConverter()) != null) {
            T oldValue = this.comboBoxBase.getValue();
            T value = oldValue;
            String text = this.textField.getText();
            if (oldValue == null && (text == null || text.isEmpty())) {
                value = null;
            } else {
                try {
                    value = c2.fromString(text);
                } catch (Exception e2) {
                }
            }
            if ((value != null || oldValue != null) && (value == null || !value.equals(oldValue))) {
                this.comboBoxBase.setValue(value);
            }
            updateDisplayNode();
        }
    }

    protected void updateDisplayNode() {
        if (this.textField != null && getEditor() != null) {
            T value = this.comboBoxBase.getValue();
            StringConverter<T> c2 = getConverter();
            if (this.initialTextFieldValue != null && !this.initialTextFieldValue.isEmpty()) {
                this.textField.setText(this.initialTextFieldValue);
                this.initialTextFieldValue = null;
                return;
            }
            String stringValue = c2.toString(value);
            if (value == null || stringValue == null) {
                this.textField.setText("");
            } else if (!stringValue.equals(this.textField.getText())) {
                this.textField.setText(stringValue);
            }
        }
    }

    private void handleKeyEvent(KeyEvent ke, boolean doConsume) {
        if (ke.getCode() == KeyCode.ENTER) {
            setTextFromTextFieldIntoComboBoxValue();
            if (doConsume && this.comboBoxBase.getOnAction() != null) {
                ke.consume();
                return;
            } else {
                forwardToParent(ke);
                return;
            }
        }
        if (ke.getCode() == KeyCode.F4) {
            if (ke.getEventType() == KeyEvent.KEY_RELEASED) {
                if (this.comboBoxBase.isShowing()) {
                    this.comboBoxBase.hide();
                } else {
                    this.comboBoxBase.show();
                }
            }
            ke.consume();
        }
    }

    private void forwardToParent(KeyEvent event) {
        if (this.comboBoxBase.getParent() != null) {
            this.comboBoxBase.getParent().fireEvent(event);
        }
    }

    protected void updateEditable() {
        final TextField newTextField = getEditor();
        if (getEditor() == null) {
            if (this.textField != null) {
                this.textField.removeEventFilter(MouseEvent.DRAG_DETECTED, this.textFieldMouseEventHandler);
                this.textField.removeEventFilter(DragEvent.ANY, this.textFieldDragEventHandler);
                this.comboBoxBase.setInputMethodRequests(null);
            }
        } else if (newTextField != null) {
            newTextField.addEventFilter(MouseEvent.DRAG_DETECTED, this.textFieldMouseEventHandler);
            newTextField.addEventFilter(DragEvent.ANY, this.textFieldDragEventHandler);
            this.comboBoxBase.setInputMethodRequests(new ExtendedInputMethodRequests() { // from class: com.sun.javafx.scene.control.skin.ComboBoxPopupControl.3
                @Override // javafx.scene.input.InputMethodRequests
                public Point2D getTextLocation(int offset) {
                    return newTextField.getInputMethodRequests().getTextLocation(offset);
                }

                @Override // javafx.scene.input.InputMethodRequests
                public int getLocationOffset(int x2, int y2) {
                    return newTextField.getInputMethodRequests().getLocationOffset(x2, y2);
                }

                @Override // javafx.scene.input.InputMethodRequests
                public void cancelLatestCommittedText() {
                    newTextField.getInputMethodRequests().cancelLatestCommittedText();
                }

                @Override // javafx.scene.input.InputMethodRequests
                public String getSelectedText() {
                    return newTextField.getInputMethodRequests().getSelectedText();
                }

                @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
                public int getInsertPositionOffset() {
                    return ((ExtendedInputMethodRequests) newTextField.getInputMethodRequests()).getInsertPositionOffset();
                }

                @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
                public String getCommittedText(int begin, int end) {
                    return ((ExtendedInputMethodRequests) newTextField.getInputMethodRequests()).getCommittedText(begin, end);
                }

                @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
                public int getCommittedTextLength() {
                    return ((ExtendedInputMethodRequests) newTextField.getInputMethodRequests()).getCommittedTextLength();
                }
            });
        }
        this.textField = newTextField;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ComboBoxPopupControl$FakeFocusTextField.class */
    public static final class FakeFocusTextField extends TextField {
        @Override // javafx.scene.Node
        public void requestFocus() {
            if (getParent() != null) {
                getParent().requestFocus();
            }
        }

        public void setFakeFocus(boolean b2) {
            setFocused(b2);
        }

        @Override // javafx.scene.control.TextInputControl, javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            switch (attribute) {
                case FOCUS_ITEM:
                    return getParent();
                default:
                    return super.queryAccessibleAttribute(attribute, parameters);
            }
        }
    }
}
