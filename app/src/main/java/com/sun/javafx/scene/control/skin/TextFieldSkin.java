package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.PasswordFieldBehavior;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.text.HitInfo;
import java.util.List;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TextFieldSkin.class */
public class TextFieldSkin extends TextInputControlSkin<TextField, TextFieldBehavior> {
    private Pane textGroup;
    private Group handleGroup;
    private Rectangle clip;
    private Text textNode;
    private Text promptNode;
    private Path selectionHighlightPath;
    private Path characterBoundingPath;
    private ObservableBooleanValue usePromptText;
    private DoubleProperty textTranslateX;
    private double caretWidth;
    protected ObservableDoubleValue textRight;
    private double pressX;
    private double pressY;
    public static final char BULLET = 9679;

    protected int translateCaretPosition(int cp) {
        return cp;
    }

    protected Point2D translateCaretPosition(Point2D p2) {
        return p2;
    }

    public TextFieldSkin(TextField textField) {
        this(textField, textField instanceof PasswordField ? new PasswordFieldBehavior((PasswordField) textField) : new TextFieldBehavior(textField));
    }

    public TextFieldSkin(final TextField textField, TextFieldBehavior behavior) {
        super(textField, behavior);
        this.textGroup = new Pane();
        this.clip = new Rectangle();
        this.textNode = new Text();
        this.selectionHighlightPath = new Path();
        this.characterBoundingPath = new Path();
        this.textTranslateX = new SimpleDoubleProperty(this, "textTranslateX");
        behavior.setTextFieldSkin(this);
        textField.caretPositionProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getWidth() > 0.0d) {
                updateTextNodeCaretPos(textField.getCaretPosition());
                if (!isForwardBias()) {
                    setForwardBias(true);
                }
                updateCaretOff();
            }
        });
        forwardBiasProperty().addListener(observable2 -> {
            if (textField.getWidth() > 0.0d) {
                updateTextNodeCaretPos(textField.getCaretPosition());
                updateCaretOff();
            }
        });
        this.textRight = new DoubleBinding() { // from class: com.sun.javafx.scene.control.skin.TextFieldSkin.1
            {
                bind(TextFieldSkin.this.textGroup.widthProperty());
            }

            @Override // javafx.beans.binding.DoubleBinding
            protected double computeValue() {
                return TextFieldSkin.this.textGroup.getWidth();
            }
        };
        this.clip.setSmooth(false);
        this.clip.setX(0.0d);
        this.clip.widthProperty().bind(this.textGroup.widthProperty());
        this.clip.heightProperty().bind(this.textGroup.heightProperty());
        this.textGroup.setClip(this.clip);
        this.textGroup.getChildren().addAll(this.selectionHighlightPath, this.textNode, new Group(this.caretPath));
        getChildren().add(this.textGroup);
        if (SHOW_HANDLES) {
            this.handleGroup = new Group();
            this.handleGroup.setManaged(false);
            this.handleGroup.getChildren().addAll(this.caretHandle, this.selectionHandle1, this.selectionHandle2);
            getChildren().add(this.handleGroup);
        }
        this.textNode.setManaged(false);
        this.textNode.getStyleClass().add("text");
        this.textNode.fontProperty().bind(textField.fontProperty());
        this.textNode.layoutXProperty().bind(this.textTranslateX);
        this.textNode.textProperty().bind(new StringBinding() { // from class: com.sun.javafx.scene.control.skin.TextFieldSkin.2
            {
                bind(textField.textProperty());
            }

            @Override // javafx.beans.binding.StringBinding
            protected String computeValue() {
                return TextFieldSkin.this.maskText(textField.textProperty().getValueSafe());
            }
        });
        this.textNode.fillProperty().bind(this.textFill);
        this.textNode.impl_selectionFillProperty().bind(new ObjectBinding<Paint>() { // from class: com.sun.javafx.scene.control.skin.TextFieldSkin.3
            {
                bind(TextFieldSkin.this.highlightTextFill, TextFieldSkin.this.textFill, textField.focusedProperty());
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.beans.binding.ObjectBinding
            public Paint computeValue() {
                return textField.isFocused() ? TextFieldSkin.this.highlightTextFill.get() : TextFieldSkin.this.textFill.get();
            }
        });
        updateTextNodeCaretPos(textField.getCaretPosition());
        textField.selectionProperty().addListener(observable3 -> {
            updateSelection();
        });
        this.selectionHighlightPath.setManaged(false);
        this.selectionHighlightPath.setStroke(null);
        this.selectionHighlightPath.layoutXProperty().bind(this.textTranslateX);
        this.selectionHighlightPath.visibleProperty().bind(textField.anchorProperty().isNotEqualTo(textField.caretPositionProperty()).and(textField.focusedProperty()));
        this.selectionHighlightPath.fillProperty().bind(this.highlightFill);
        this.textNode.impl_selectionShapeProperty().addListener(observable4 -> {
            updateSelection();
        });
        this.caretPath.setManaged(false);
        this.caretPath.setStrokeWidth(1.0d);
        this.caretPath.fillProperty().bind(this.textFill);
        this.caretPath.strokeProperty().bind(this.textFill);
        this.caretPath.opacityProperty().bind(new DoubleBinding() { // from class: com.sun.javafx.scene.control.skin.TextFieldSkin.4
            {
                bind(TextFieldSkin.this.caretVisible);
            }

            @Override // javafx.beans.binding.DoubleBinding
            protected double computeValue() {
                return TextFieldSkin.this.caretVisible.get() ? 1.0d : 0.0d;
            }
        });
        this.caretPath.layoutXProperty().bind(this.textTranslateX);
        this.textNode.impl_caretShapeProperty().addListener(observable5 -> {
            this.caretPath.getElements().setAll(this.textNode.impl_caretShapeProperty().get());
            if (this.caretPath.getElements().size() == 0) {
                updateTextNodeCaretPos(textField.getCaretPosition());
            } else if (this.caretPath.getElements().size() != 4) {
                this.caretWidth = Math.round(this.caretPath.getLayoutBounds().getWidth());
            }
        });
        textField.fontProperty().addListener(observable6 -> {
            textField.requestLayout();
            ((TextField) getSkinnable()).requestLayout();
        });
        registerChangeListener(textField.prefColumnCountProperty(), "prefColumnCount");
        if (textField.isFocused()) {
            setCaretAnimating(true);
        }
        textField.alignmentProperty().addListener(observable7 -> {
            if (textField.getWidth() > 0.0d) {
                updateTextPos();
                updateCaretOff();
                textField.requestLayout();
            }
        });
        this.usePromptText = new BooleanBinding() { // from class: com.sun.javafx.scene.control.skin.TextFieldSkin.5
            {
                bind(textField.textProperty(), textField.promptTextProperty(), TextFieldSkin.this.promptTextFill);
            }

            @Override // javafx.beans.binding.BooleanBinding
            protected boolean computeValue() {
                String txt = textField.getText();
                String promptTxt = textField.getPromptText();
                return ((txt != null && !txt.isEmpty()) || promptTxt == null || promptTxt.isEmpty() || TextFieldSkin.this.promptTextFill.get().equals(Color.TRANSPARENT)) ? false : true;
            }
        };
        this.promptTextFill.addListener(observable8 -> {
            updateTextPos();
        });
        textField.textProperty().addListener(observable9 -> {
            if (!((TextFieldBehavior) getBehavior()).isEditing()) {
                updateTextPos();
            }
        });
        if (this.usePromptText.get()) {
            createPromptNode();
        }
        this.usePromptText.addListener(observable10 -> {
            createPromptNode();
            textField.requestLayout();
        });
        if (SHOW_HANDLES) {
            this.selectionHandle1.setRotate(180.0d);
            EventHandler<MouseEvent> handlePressHandler = e2 -> {
                this.pressX = e2.getX();
                this.pressY = e2.getY();
                e2.consume();
            };
            this.caretHandle.setOnMousePressed(handlePressHandler);
            this.selectionHandle1.setOnMousePressed(handlePressHandler);
            this.selectionHandle2.setOnMousePressed(handlePressHandler);
            this.caretHandle.setOnMouseDragged(e3 -> {
                Point2D p2 = new Point2D(((this.caretHandle.getLayoutX() + e3.getX()) + this.pressX) - this.textNode.getLayoutX(), ((this.caretHandle.getLayoutY() + e3.getY()) - this.pressY) - 6.0d);
                HitInfo hit = this.textNode.impl_hitTestChar(translateCaretPosition(p2));
                positionCaret(hit, false);
                e3.consume();
            });
            this.selectionHandle1.setOnMouseDragged(new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.TextFieldSkin.6
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.event.EventHandler
                public void handle(MouseEvent e4) {
                    TextField textField2 = (TextField) TextFieldSkin.this.getSkinnable();
                    Point2D tp = TextFieldSkin.this.textNode.localToScene(0.0d, 0.0d);
                    Point2D p2 = new Point2D((((e4.getSceneX() - tp.getX()) + 10.0d) - TextFieldSkin.this.pressX) + (TextFieldSkin.this.selectionHandle1.getWidth() / 2.0d), ((e4.getSceneY() - tp.getY()) - TextFieldSkin.this.pressY) - 6.0d);
                    HitInfo hit = TextFieldSkin.this.textNode.impl_hitTestChar(TextFieldSkin.this.translateCaretPosition(p2));
                    int pos = hit.getCharIndex();
                    if (textField2.getAnchor() < textField2.getCaretPosition()) {
                        textField2.selectRange(textField2.getCaretPosition(), textField2.getAnchor());
                    }
                    if (pos >= 0) {
                        if (pos >= textField2.getAnchor() - 1) {
                            hit.setCharIndex(Math.max(0, textField2.getAnchor() - 1));
                        }
                        TextFieldSkin.this.positionCaret(hit, true);
                    }
                    e4.consume();
                }
            });
            this.selectionHandle2.setOnMouseDragged(new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.TextFieldSkin.7
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.event.EventHandler
                public void handle(MouseEvent e4) {
                    TextField textField2 = (TextField) TextFieldSkin.this.getSkinnable();
                    Point2D tp = TextFieldSkin.this.textNode.localToScene(0.0d, 0.0d);
                    Point2D p2 = new Point2D((((e4.getSceneX() - tp.getX()) + 10.0d) - TextFieldSkin.this.pressX) + (TextFieldSkin.this.selectionHandle2.getWidth() / 2.0d), ((e4.getSceneY() - tp.getY()) - TextFieldSkin.this.pressY) - 6.0d);
                    HitInfo hit = TextFieldSkin.this.textNode.impl_hitTestChar(TextFieldSkin.this.translateCaretPosition(p2));
                    int pos = hit.getCharIndex();
                    if (textField2.getAnchor() > textField2.getCaretPosition()) {
                        textField2.selectRange(textField2.getCaretPosition(), textField2.getAnchor());
                    }
                    if (pos > 0) {
                        if (pos <= textField2.getAnchor()) {
                            hit.setCharIndex(Math.min(textField2.getAnchor() + 1, textField2.getLength()));
                        }
                        TextFieldSkin.this.positionCaret(hit, true);
                    }
                    e4.consume();
                }
            });
        }
    }

    private void updateTextNodeCaretPos(int pos) {
        if (pos == 0 || isForwardBias()) {
            this.textNode.setImpl_caretPosition(pos);
        } else {
            this.textNode.setImpl_caretPosition(pos - 1);
        }
        this.textNode.impl_caretBiasProperty().set(isForwardBias());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void createPromptNode() {
        if (this.promptNode == null && this.usePromptText.get()) {
            this.promptNode = new Text();
            this.textGroup.getChildren().add(0, this.promptNode);
            this.promptNode.setManaged(false);
            this.promptNode.getStyleClass().add("text");
            this.promptNode.visibleProperty().bind(this.usePromptText);
            this.promptNode.fontProperty().bind(((TextField) getSkinnable()).fontProperty());
            this.promptNode.textProperty().bind(((TextField) getSkinnable()).promptTextProperty());
            this.promptNode.fillProperty().bind(this.promptTextFill);
            updateSelection();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateSelection() {
        TextField textField = (TextField) getSkinnable();
        IndexRange newValue = textField.getSelection();
        if (newValue == null || newValue.getLength() == 0) {
            this.textNode.impl_selectionStartProperty().set(-1);
            this.textNode.impl_selectionEndProperty().set(-1);
        } else {
            this.textNode.impl_selectionStartProperty().set(newValue.getStart());
            this.textNode.impl_selectionEndProperty().set(newValue.getStart());
            this.textNode.impl_selectionEndProperty().set(newValue.getEnd());
        }
        PathElement[] elements = this.textNode.impl_selectionShapeProperty().get();
        if (elements == null) {
            this.selectionHighlightPath.getElements().clear();
        } else {
            this.selectionHighlightPath.getElements().setAll(elements);
        }
        if (SHOW_HANDLES && newValue != null && newValue.getLength() > 0) {
            int caretPos = textField.getCaretPosition();
            int anchorPos = textField.getAnchor();
            updateTextNodeCaretPos(anchorPos);
            Bounds b2 = this.caretPath.getBoundsInParent();
            if (caretPos < anchorPos) {
                this.selectionHandle2.setLayoutX(b2.getMinX() - (this.selectionHandle2.getWidth() / 2.0d));
            } else {
                this.selectionHandle1.setLayoutX(b2.getMinX() - (this.selectionHandle1.getWidth() / 2.0d));
            }
            updateTextNodeCaretPos(caretPos);
            Bounds b3 = this.caretPath.getBoundsInParent();
            if (caretPos < anchorPos) {
                this.selectionHandle1.setLayoutX(b3.getMinX() - (this.selectionHandle1.getWidth() / 2.0d));
            } else {
                this.selectionHandle2.setLayoutX(b3.getMinX() - (this.selectionHandle2.getWidth() / 2.0d));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String propertyReference) {
        if ("prefColumnCount".equals(propertyReference)) {
            ((TextField) getSkinnable()).requestLayout();
        } else {
            super.handleControlPropertyChanged(propertyReference);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        TextField textField = (TextField) getSkinnable();
        double characterWidth = this.fontMetrics.get().computeStringWidth(PdfOps.W_TOKEN);
        int columnCount = textField.getPrefColumnCount();
        return (columnCount * characterWidth) + leftInset + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + this.textNode.getLayoutBounds().getHeight() + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((TextField) getSkinnable()).prefHeight(width);
    }

    @Override // javafx.scene.control.SkinBase
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset + this.textNode.getBaselineOffset();
    }

    private void updateTextPos() {
        double newX;
        double oldX = this.textTranslateX.get();
        double textNodeWidth = this.textNode.getLayoutBounds().getWidth();
        switch (getHAlignment()) {
            case CENTER:
                double midPoint = this.textRight.get() / 2.0d;
                if (this.usePromptText.get()) {
                    newX = midPoint - (this.promptNode.getLayoutBounds().getWidth() / 2.0d);
                    this.promptNode.setLayoutX(newX);
                } else {
                    newX = midPoint - (textNodeWidth / 2.0d);
                }
                if (newX + textNodeWidth <= this.textRight.get()) {
                    this.textTranslateX.set(newX);
                    break;
                }
                break;
            case RIGHT:
                double newX2 = (this.textRight.get() - textNodeWidth) - (this.caretWidth / 2.0d);
                if (newX2 > oldX || newX2 > 0.0d) {
                    this.textTranslateX.set(newX2);
                }
                if (this.usePromptText.get()) {
                    this.promptNode.setLayoutX((this.textRight.get() - this.promptNode.getLayoutBounds().getWidth()) - (this.caretWidth / 2.0d));
                    break;
                }
                break;
            case LEFT:
            default:
                double newX3 = this.caretWidth / 2.0d;
                if (newX3 < oldX || newX3 + textNodeWidth <= this.textRight.get()) {
                    this.textTranslateX.set(newX3);
                }
                if (this.usePromptText.get()) {
                    this.promptNode.layoutXProperty().set(newX3);
                    break;
                }
                break;
        }
    }

    protected void updateCaretOff() {
        double delta = 0.0d;
        double caretX = this.caretPath.getLayoutBounds().getMinX() + this.textTranslateX.get();
        if (caretX < 0.0d) {
            delta = caretX;
        } else if (caretX > this.textRight.get() - this.caretWidth) {
            delta = caretX - (this.textRight.get() - this.caretWidth);
        }
        switch (getHAlignment()) {
            case CENTER:
                this.textTranslateX.set(this.textTranslateX.get() - delta);
                break;
            case RIGHT:
                this.textTranslateX.set(Math.max(this.textTranslateX.get() - delta, (this.textRight.get() - this.textNode.getLayoutBounds().getWidth()) - (this.caretWidth / 2.0d)));
                break;
            case LEFT:
            default:
                this.textTranslateX.set(Math.min(this.textTranslateX.get() - delta, this.caretWidth / 2.0d));
                break;
        }
        if (SHOW_HANDLES) {
            this.caretHandle.setLayoutX((caretX - (this.caretHandle.getWidth() / 2.0d)) + 1.0d);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void replaceText(int start, int end, String txt) {
        double textMaxXOld = this.textNode.getBoundsInParent().getMaxX();
        double caretMaxXOld = this.caretPath.getLayoutBounds().getMaxX() + this.textTranslateX.get();
        ((TextField) getSkinnable()).replaceText(start, end, txt);
        scrollAfterDelete(textMaxXOld, caretMaxXOld);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void deleteChar(boolean previous) {
        boolean z2;
        double textMaxXOld = this.textNode.getBoundsInParent().getMaxX();
        double caretMaxXOld = this.caretPath.getLayoutBounds().getMaxX() + this.textTranslateX.get();
        if (previous) {
            z2 = !((TextField) getSkinnable()).deletePreviousChar();
        } else {
            z2 = !((TextField) getSkinnable()).deleteNextChar();
        }
        boolean shouldBeep = z2;
        if (!shouldBeep) {
            scrollAfterDelete(textMaxXOld, caretMaxXOld);
        }
    }

    public void scrollAfterDelete(double textMaxXOld, double caretMaxXOld) {
        Bounds textLayoutBounds = this.textNode.getLayoutBounds();
        Bounds textBounds = this.textNode.localToParent(textLayoutBounds);
        Bounds clipBounds = this.clip.getBoundsInParent();
        Bounds caretBounds = this.caretPath.getLayoutBounds();
        switch (getHAlignment()) {
            case CENTER:
            case LEFT:
            default:
                if (textBounds.getMinX() < clipBounds.getMinX() + (this.caretWidth / 2.0d) && textBounds.getMaxX() <= clipBounds.getMaxX()) {
                    double delta = (caretMaxXOld - caretBounds.getMaxX()) - this.textTranslateX.get();
                    if (textBounds.getMaxX() + delta < clipBounds.getMaxX()) {
                        if (textMaxXOld <= clipBounds.getMaxX()) {
                            delta = textMaxXOld - textBounds.getMaxX();
                        } else {
                            delta = clipBounds.getMaxX() - textBounds.getMaxX();
                        }
                    }
                    this.textTranslateX.set(this.textTranslateX.get() + delta);
                    break;
                }
                break;
            case RIGHT:
                if (textBounds.getMaxX() > clipBounds.getMaxX()) {
                    double delta2 = (caretMaxXOld - caretBounds.getMaxX()) - this.textTranslateX.get();
                    if (textBounds.getMaxX() + delta2 < clipBounds.getMaxX()) {
                        if (textMaxXOld <= clipBounds.getMaxX()) {
                            delta2 = textMaxXOld - textBounds.getMaxX();
                        } else {
                            delta2 = clipBounds.getMaxX() - textBounds.getMaxX();
                        }
                    }
                    this.textTranslateX.set(this.textTranslateX.get() + delta2);
                    break;
                } else {
                    updateTextPos();
                    break;
                }
        }
        updateCaretOff();
    }

    public HitInfo getIndex(double x2, double y2) {
        Point2D p2 = new Point2D((x2 - this.textTranslateX.get()) - snappedLeftInset(), y2 - snappedTopInset());
        return this.textNode.impl_hitTestChar(translateCaretPosition(p2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void positionCaret(HitInfo hit, boolean select) {
        TextField textField = (TextField) getSkinnable();
        int pos = Utils.getHitInsertionIndex(hit, textField.textProperty().getValueSafe());
        if (select) {
            textField.selectPositionCaret(pos);
        } else {
            textField.positionCaret(pos);
        }
        setForwardBias(hit.isLeading());
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public Rectangle2D getCharacterBounds(int index) {
        double x2;
        double y2;
        double width;
        double height;
        if (index == this.textNode.getText().length()) {
            Bounds textNodeBounds = this.textNode.getBoundsInLocal();
            x2 = textNodeBounds.getMaxX();
            y2 = 0.0d;
            width = 0.0d;
            height = textNodeBounds.getMaxY();
        } else {
            this.characterBoundingPath.getElements().clear();
            this.characterBoundingPath.getElements().addAll(this.textNode.impl_getRangeShape(index, index + 1));
            this.characterBoundingPath.setLayoutX(this.textNode.getLayoutX());
            this.characterBoundingPath.setLayoutY(this.textNode.getLayoutY());
            Bounds bounds = this.characterBoundingPath.getBoundsInLocal();
            x2 = bounds.getMinX();
            y2 = bounds.getMinY();
            width = bounds.isEmpty() ? 0.0d : bounds.getWidth();
            height = bounds.isEmpty() ? 0.0d : bounds.getHeight();
        }
        Bounds textBounds = this.textGroup.getBoundsInParent();
        return new Rectangle2D(x2 + textBounds.getMinX() + this.textTranslateX.get(), y2 + textBounds.getMinY(), width, height);
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected PathElement[] getUnderlineShape(int start, int end) {
        return this.textNode.impl_getUnderlineShape(start, end);
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected PathElement[] getRangeShape(int start, int end) {
        return this.textNode.impl_getRangeShape(start, end);
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected void addHighlight(List<? extends Node> nodes, int start) {
        this.textGroup.getChildren().addAll(nodes);
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected void removeHighlight(List<? extends Node> nodes) {
        this.textGroup.getChildren().removeAll(nodes);
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public void nextCharacterVisually(boolean moveRight) {
        if (isRTL()) {
            moveRight = !moveRight;
        }
        Bounds caretBounds = this.caretPath.getLayoutBounds();
        if (this.caretPath.getElements().size() == 4) {
            caretBounds = new Path(this.caretPath.getElements().get(0), this.caretPath.getElements().get(1)).getLayoutBounds();
        }
        double hitX = moveRight ? caretBounds.getMaxX() : caretBounds.getMinX();
        double hitY = (caretBounds.getMinY() + caretBounds.getMaxY()) / 2.0d;
        HitInfo hit = this.textNode.impl_hitTestChar(translateCaretPosition(new Point2D(hitX, hitY)));
        Path charShape = new Path(this.textNode.impl_getRangeShape(hit.getCharIndex(), hit.getCharIndex() + 1));
        if ((moveRight && charShape.getLayoutBounds().getMaxX() > caretBounds.getMaxX()) || (!moveRight && charShape.getLayoutBounds().getMinX() < caretBounds.getMinX())) {
            hit.setLeading(!hit.isLeading());
        }
        positionCaret(hit, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double textY;
        super.layoutChildren(x2, y2, w2, h2);
        if (this.textNode != null) {
            Bounds textNodeBounds = this.textNode.getLayoutBounds();
            double ascent = this.textNode.getBaselineOffset();
            double descent = textNodeBounds.getHeight() - ascent;
            switch (((TextField) getSkinnable()).getAlignment().getVpos()) {
                case TOP:
                    textY = ascent;
                    break;
                case CENTER:
                    textY = ((ascent + this.textGroup.getHeight()) - descent) / 2.0d;
                    break;
                case BOTTOM:
                default:
                    textY = this.textGroup.getHeight() - descent;
                    break;
            }
            this.textNode.setY(textY);
            if (this.promptNode != null) {
                this.promptNode.setY(textY);
            }
            if (((TextField) getSkinnable()).getWidth() > 0.0d) {
                updateTextPos();
                updateCaretOff();
            }
        }
        if (SHOW_HANDLES) {
            this.handleGroup.setLayoutX(x2 + this.textTranslateX.get());
            this.handleGroup.setLayoutY(y2);
            this.selectionHandle1.resize(this.selectionHandle1.prefWidth(-1.0d), this.selectionHandle1.prefHeight(-1.0d));
            this.selectionHandle2.resize(this.selectionHandle2.prefWidth(-1.0d), this.selectionHandle2.prefHeight(-1.0d));
            this.caretHandle.resize(this.caretHandle.prefWidth(-1.0d), this.caretHandle.prefHeight(-1.0d));
            Bounds b2 = this.caretPath.getBoundsInParent();
            this.caretHandle.setLayoutY(b2.getMaxY() - 1.0d);
            this.selectionHandle1.setLayoutY((b2.getMinY() - this.selectionHandle1.getHeight()) + 1.0d);
            this.selectionHandle2.setLayoutY(b2.getMaxY() - 1.0d);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected HPos getHAlignment() {
        HPos hPos = ((TextField) getSkinnable()).getAlignment().getHpos();
        return hPos;
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public Point2D getMenuPosition() {
        Point2D p2 = super.getMenuPosition();
        if (p2 != null) {
            p2 = new Point2D(Math.max(0.0d, ((p2.getX() - this.textNode.getLayoutX()) - snappedLeftInset()) + this.textTranslateX.get()), Math.max(0.0d, (p2.getY() - this.textNode.getLayoutY()) - snappedTopInset()));
        }
        return p2;
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField) {
            int n2 = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n2);
            for (int i2 = 0; i2 < n2; i2++) {
                passwordBuilder.append((char) 9679);
            }
            return passwordBuilder.toString();
        }
        return txt;
    }

    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case BOUNDS_FOR_RANGE:
            case OFFSET_AT_POINT:
                return this.textNode.queryAccessibleAttribute(attribute, parameters);
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
