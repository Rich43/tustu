package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.Clipboard;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;
import javafx.scene.shape.VLineTo;
import javafx.stage.Window;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TextInputControlSkin.class */
public abstract class TextInputControlSkin<T extends TextInputControl, B extends TextInputControlBehavior<T>> extends BehaviorSkinBase<T, B> {
    static boolean preload = false;
    protected static final boolean SHOW_HANDLES;
    protected final ObservableObjectValue<FontMetrics> fontMetrics;
    protected final ObjectProperty<Paint> textFill;
    protected final ObjectProperty<Paint> promptTextFill;
    protected final ObjectProperty<Paint> highlightFill;
    protected final ObjectProperty<Paint> highlightTextFill;
    protected final BooleanProperty displayCaret;
    private BooleanProperty forwardBias;
    private BooleanProperty blink;
    protected ObservableBooleanValue caretVisible;
    private CaretBlinking caretBlinking;
    protected final Path caretPath;
    protected StackPane caretHandle;
    protected StackPane selectionHandle1;
    protected StackPane selectionHandle2;
    private static final boolean IS_FXVK_SUPPORTED;
    private static boolean USE_FXVK;
    static int vkType;
    private int imstart;
    private int imlength;
    private List<Shape> imattrs;
    final MenuItem undoMI;
    final MenuItem redoMI;
    final MenuItem cutMI;
    final MenuItem copyMI;
    final MenuItem pasteMI;
    final MenuItem deleteMI;
    final MenuItem selectWordMI;
    final MenuItem selectAllMI;
    final MenuItem separatorMI;

    protected abstract PathElement[] getUnderlineShape(int i2, int i3);

    protected abstract PathElement[] getRangeShape(int i2, int i3);

    protected abstract void addHighlight(List<? extends Node> list, int i2);

    protected abstract void removeHighlight(List<? extends Node> list);

    public abstract void nextCharacterVisually(boolean z2);

    static {
        AccessController.doPrivileged(() -> {
            String s2 = System.getProperty("com.sun.javafx.virtualKeyboard.preload");
            if (s2 != null && s2.equalsIgnoreCase("PRERENDER")) {
                preload = true;
                return null;
            }
            return null;
        });
        SHOW_HANDLES = IS_TOUCH_SUPPORTED && !PlatformUtil.isIOS();
        IS_FXVK_SUPPORTED = Platform.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD);
        USE_FXVK = IS_FXVK_SUPPORTED;
        vkType = -1;
    }

    public BooleanProperty forwardBiasProperty() {
        return this.forwardBias;
    }

    public void setForwardBias(boolean isLeading) {
        this.forwardBias.set(isLeading);
    }

    public boolean isForwardBias() {
        return this.forwardBias.get();
    }

    public Point2D getMenuPosition() {
        if (SHOW_HANDLES) {
            if (this.caretHandle.isVisible()) {
                return new Point2D(this.caretHandle.getLayoutX() + (this.caretHandle.getWidth() / 2.0d), this.caretHandle.getLayoutY());
            }
            if (this.selectionHandle1.isVisible() && this.selectionHandle2.isVisible()) {
                return new Point2D((((this.selectionHandle1.getLayoutX() + (this.selectionHandle1.getWidth() / 2.0d)) + this.selectionHandle2.getLayoutX()) + (this.selectionHandle2.getWidth() / 2.0d)) / 2.0d, this.selectionHandle2.getLayoutY() + (this.selectionHandle2.getHeight() / 2.0d));
            }
            return null;
        }
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [javafx.scene.Node, javafx.scene.control.Control] */
    public void toggleUseVK() {
        vkType++;
        if (vkType < 4) {
            USE_FXVK = true;
            ((TextInputControl) getSkinnable()).getProperties().put(FXVK.VK_TYPE_PROP_KEY, FXVK.VK_TYPE_NAMES[vkType]);
            FXVK.attach(getSkinnable());
        } else {
            FXVK.detach();
            vkType = -1;
            USE_FXVK = false;
        }
    }

    public TextInputControlSkin(final T textInput, B behavior) {
        Scene scene;
        super(textInput, behavior);
        this.textFill = new StyleableObjectProperty<Paint>(Color.BLACK) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.1
            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextInputControlSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "textFill";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<TextInputControl, Paint> getCssMetaData() {
                return StyleableProperties.TEXT_FILL;
            }
        };
        this.promptTextFill = new StyleableObjectProperty<Paint>(Color.GRAY) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.2
            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextInputControlSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "promptTextFill";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<TextInputControl, Paint> getCssMetaData() {
                return StyleableProperties.PROMPT_TEXT_FILL;
            }
        };
        this.highlightFill = new StyleableObjectProperty<Paint>(Color.DODGERBLUE) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.3
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                TextInputControlSkin.this.updateHighlightFill();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextInputControlSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "highlightFill";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<TextInputControl, Paint> getCssMetaData() {
                return StyleableProperties.HIGHLIGHT_FILL;
            }
        };
        this.highlightTextFill = new StyleableObjectProperty<Paint>(Color.WHITE) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.4
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                TextInputControlSkin.this.updateHighlightTextFill();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextInputControlSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "highlightTextFill";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<TextInputControl, Paint> getCssMetaData() {
                return StyleableProperties.HIGHLIGHT_TEXT_FILL;
            }
        };
        this.displayCaret = new StyleableBooleanProperty(true) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.5
            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextInputControlSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "displayCaret";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.DISPLAY_CARET;
            }
        };
        this.forwardBias = new SimpleBooleanProperty(this, "forwardBias", true);
        this.blink = new SimpleBooleanProperty(this, "blink", true);
        this.caretBlinking = new CaretBlinking(this.blink);
        this.caretPath = new Path();
        this.caretHandle = null;
        this.selectionHandle1 = null;
        this.selectionHandle2 = null;
        this.imattrs = new ArrayList();
        this.undoMI = new ContextMenuItem("Undo");
        this.redoMI = new ContextMenuItem("Redo");
        this.cutMI = new ContextMenuItem("Cut");
        this.copyMI = new ContextMenuItem("Copy");
        this.pasteMI = new ContextMenuItem("Paste");
        this.deleteMI = new ContextMenuItem("DeleteSelection");
        this.selectWordMI = new ContextMenuItem("SelectWord");
        this.selectAllMI = new ContextMenuItem("SelectAll");
        this.separatorMI = new SeparatorMenuItem();
        this.fontMetrics = new ObjectBinding<FontMetrics>() { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.6
            {
                bind(textInput.fontProperty());
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.beans.binding.ObjectBinding
            public FontMetrics computeValue() {
                TextInputControlSkin.this.invalidateMetrics();
                return Toolkit.getToolkit().getFontLoader().getFontMetrics(textInput.getFont());
            }
        };
        this.caretVisible = new BooleanBinding() { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.7
            {
                bind(textInput.focusedProperty(), textInput.anchorProperty(), textInput.caretPositionProperty(), textInput.disabledProperty(), textInput.editableProperty(), TextInputControlSkin.this.displayCaret, TextInputControlSkin.this.blink);
            }

            @Override // javafx.beans.binding.BooleanBinding
            protected boolean computeValue() {
                return !TextInputControlSkin.this.blink.get() && TextInputControlSkin.this.displayCaret.get() && textInput.isFocused() && (PlatformUtil.isWindows() || textInput.getCaretPosition() == textInput.getAnchor()) && !textInput.isDisabled() && textInput.isEditable();
            }
        };
        if (SHOW_HANDLES) {
            this.caretHandle = new StackPane();
            this.selectionHandle1 = new StackPane();
            this.selectionHandle2 = new StackPane();
            this.caretHandle.setManaged(false);
            this.selectionHandle1.setManaged(false);
            this.selectionHandle2.setManaged(false);
            this.caretHandle.visibleProperty().bind(new BooleanBinding() { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.8
                {
                    bind(textInput.focusedProperty(), textInput.anchorProperty(), textInput.caretPositionProperty(), textInput.disabledProperty(), textInput.editableProperty(), textInput.lengthProperty(), TextInputControlSkin.this.displayCaret);
                }

                @Override // javafx.beans.binding.BooleanBinding
                protected boolean computeValue() {
                    return TextInputControlSkin.this.displayCaret.get() && textInput.isFocused() && textInput.getCaretPosition() == textInput.getAnchor() && !textInput.isDisabled() && textInput.isEditable() && textInput.getLength() > 0;
                }
            });
            this.selectionHandle1.visibleProperty().bind(new BooleanBinding() { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.9
                {
                    bind(textInput.focusedProperty(), textInput.anchorProperty(), textInput.caretPositionProperty(), textInput.disabledProperty(), TextInputControlSkin.this.displayCaret);
                }

                @Override // javafx.beans.binding.BooleanBinding
                protected boolean computeValue() {
                    return TextInputControlSkin.this.displayCaret.get() && textInput.isFocused() && textInput.getCaretPosition() != textInput.getAnchor() && !textInput.isDisabled();
                }
            });
            this.selectionHandle2.visibleProperty().bind(new BooleanBinding() { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.10
                {
                    bind(textInput.focusedProperty(), textInput.anchorProperty(), textInput.caretPositionProperty(), textInput.disabledProperty(), TextInputControlSkin.this.displayCaret);
                }

                @Override // javafx.beans.binding.BooleanBinding
                protected boolean computeValue() {
                    return TextInputControlSkin.this.displayCaret.get() && textInput.isFocused() && textInput.getCaretPosition() != textInput.getAnchor() && !textInput.isDisabled();
                }
            });
            this.caretHandle.getStyleClass().setAll("caret-handle");
            this.selectionHandle1.getStyleClass().setAll("selection-handle");
            this.selectionHandle2.getStyleClass().setAll("selection-handle");
            this.selectionHandle1.setId("selection-handle-1");
            this.selectionHandle2.setId("selection-handle-2");
        }
        if (IS_FXVK_SUPPORTED) {
            if (preload && (scene = textInput.getScene()) != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    FXVK.init(textInput);
                }
            }
            textInput.focusedProperty().addListener(observable -> {
                if (USE_FXVK) {
                    Scene scene2 = ((TextInputControl) getSkinnable()).getScene();
                    if (textInput.isEditable() && textInput.isFocused()) {
                        FXVK.attach(textInput);
                        return;
                    }
                    if (scene2 == null || scene2.getWindow() == null || !scene2.getWindow().isFocused() || !(scene2.getFocusOwner() instanceof TextInputControl) || !((TextInputControl) scene2.getFocusOwner()).isEditable()) {
                        FXVK.detach();
                    }
                }
            });
        }
        if (textInput.getOnInputMethodTextChanged() == null) {
            textInput.setOnInputMethodTextChanged(event -> {
                handleInputMethodEvent(event);
            });
        }
        textInput.setInputMethodRequests(new ExtendedInputMethodRequests() { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.11
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.input.InputMethodRequests
            public Point2D getTextLocation(int offset) {
                Scene scene2 = ((TextInputControl) TextInputControlSkin.this.getSkinnable()).getScene();
                Window window2 = scene2.getWindow();
                Rectangle2D characterBounds = TextInputControlSkin.this.getCharacterBounds(textInput.getSelection().getStart() + offset);
                Point2D p2 = ((TextInputControl) TextInputControlSkin.this.getSkinnable()).localToScene(characterBounds.getMinX(), characterBounds.getMaxY());
                Point2D location = new Point2D(window2.getX() + scene2.getX() + p2.getX(), window2.getY() + scene2.getY() + p2.getY());
                return location;
            }

            @Override // javafx.scene.input.InputMethodRequests
            public int getLocationOffset(int x2, int y2) {
                return TextInputControlSkin.this.getInsertionPoint(x2, y2);
            }

            @Override // javafx.scene.input.InputMethodRequests
            public void cancelLatestCommittedText() {
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.scene.input.InputMethodRequests
            public String getSelectedText() {
                TextInputControl textInput2 = (TextInputControl) TextInputControlSkin.this.getSkinnable();
                IndexRange selection = textInput2.getSelection();
                return textInput2.getText(selection.getStart(), selection.getEnd());
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
            public int getInsertPositionOffset() {
                int caretPosition = ((TextInputControl) TextInputControlSkin.this.getSkinnable()).getCaretPosition();
                if (caretPosition < TextInputControlSkin.this.imstart) {
                    return caretPosition;
                }
                if (caretPosition < TextInputControlSkin.this.imstart + TextInputControlSkin.this.imlength) {
                    return TextInputControlSkin.this.imstart;
                }
                return caretPosition - TextInputControlSkin.this.imlength;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
            public String getCommittedText(int begin, int end) {
                TextInputControl textInput2 = (TextInputControl) TextInputControlSkin.this.getSkinnable();
                if (begin < TextInputControlSkin.this.imstart) {
                    if (end > TextInputControlSkin.this.imstart) {
                        return textInput2.getText(begin, TextInputControlSkin.this.imstart) + textInput2.getText(TextInputControlSkin.this.imstart + TextInputControlSkin.this.imlength, end + TextInputControlSkin.this.imlength);
                    }
                    return textInput2.getText(begin, end);
                }
                return textInput2.getText(begin + TextInputControlSkin.this.imlength, end + TextInputControlSkin.this.imlength);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
            public int getCommittedTextLength() {
                return ((TextInputControl) TextInputControlSkin.this.getSkinnable()).getText().length() - TextInputControlSkin.this.imlength;
            }
        });
    }

    protected String maskText(String txt) {
        return txt;
    }

    public char getCharacter(int index) {
        return (char) 0;
    }

    public int getInsertionPoint(double x2, double y2) {
        return 0;
    }

    public Rectangle2D getCharacterBounds(int index) {
        return null;
    }

    public void scrollCharacterToVisible(int index) {
    }

    protected void invalidateMetrics() {
    }

    protected void updateTextFill() {
    }

    protected void updateHighlightFill() {
    }

    protected void updateHighlightTextFill() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void handleInputMethodEvent(InputMethodEvent event) {
        TextInputControl textInput = (TextInputControl) getSkinnable();
        if (textInput.isEditable() && !textInput.textProperty().isBound() && !textInput.isDisabled()) {
            if (PlatformUtil.isIOS()) {
                textInput.setText(event.getCommitted());
                return;
            }
            if (this.imlength != 0) {
                removeHighlight(this.imattrs);
                this.imattrs.clear();
                textInput.selectRange(this.imstart, this.imstart + this.imlength);
            }
            if (event.getCommitted().length() != 0) {
                String committed = event.getCommitted();
                textInput.replaceText(textInput.getSelection(), committed);
            }
            this.imstart = textInput.getSelection().getStart();
            StringBuilder composed = new StringBuilder();
            Iterator<InputMethodTextRun> it = event.getComposed().iterator();
            while (it.hasNext()) {
                composed.append(it.next().getText());
            }
            textInput.replaceText(textInput.getSelection(), composed.toString());
            this.imlength = composed.length();
            if (this.imlength != 0) {
                int pos = this.imstart;
                for (InputMethodTextRun run : event.getComposed()) {
                    int endPos = pos + run.getText().length();
                    createInputMethodAttributes(run.getHighlight(), pos, endPos);
                    pos = endPos;
                }
                addHighlight(this.imattrs, this.imstart);
                int caretPos = event.getCaretPosition();
                if (caretPos >= 0 && caretPos < this.imlength) {
                    textInput.selectRange(this.imstart + caretPos, this.imstart + caretPos);
                }
            }
        }
    }

    private void createInputMethodAttributes(InputMethodHighlight highlight, int start, int end) {
        double minX = 0.0d;
        double maxX = 0.0d;
        double minY = 0.0d;
        double maxY = 0.0d;
        PathElement[] elements = getUnderlineShape(start, end);
        for (int i2 = 0; i2 < elements.length; i2++) {
            PathElement pe = elements[i2];
            if (pe instanceof MoveTo) {
                double x2 = ((MoveTo) pe).getX();
                maxX = x2;
                minX = x2;
                double y2 = ((MoveTo) pe).getY();
                maxY = y2;
                minY = y2;
            } else if (pe instanceof LineTo) {
                minX = minX < ((LineTo) pe).getX() ? minX : ((LineTo) pe).getX();
                maxX = maxX > ((LineTo) pe).getX() ? maxX : ((LineTo) pe).getX();
                minY = minY < ((LineTo) pe).getY() ? minY : ((LineTo) pe).getY();
                maxY = maxY > ((LineTo) pe).getY() ? maxY : ((LineTo) pe).getY();
            } else if (pe instanceof HLineTo) {
                minX = minX < ((HLineTo) pe).getX() ? minX : ((HLineTo) pe).getX();
                maxX = maxX > ((HLineTo) pe).getX() ? maxX : ((HLineTo) pe).getX();
            } else if (pe instanceof VLineTo) {
                minY = minY < ((VLineTo) pe).getY() ? minY : ((VLineTo) pe).getY();
                maxY = maxY > ((VLineTo) pe).getY() ? maxY : ((VLineTo) pe).getY();
            }
            if ((pe instanceof ClosePath) || i2 == elements.length - 1 || (i2 < elements.length - 1 && (elements[i2 + 1] instanceof MoveTo))) {
                Shape attr = null;
                if (highlight == InputMethodHighlight.SELECTED_RAW) {
                    attr = new Path();
                    ((Path) attr).getElements().addAll(getRangeShape(start, end));
                    attr.setFill(Color.BLUE);
                    attr.setOpacity(0.30000001192092896d);
                } else if (highlight == InputMethodHighlight.UNSELECTED_RAW) {
                    attr = new Line(minX + 2.0d, maxY + 1.0d, maxX - 2.0d, maxY + 1.0d);
                    attr.setStroke(this.textFill.get());
                    attr.setStrokeWidth(maxY - minY);
                    ObservableList<Double> dashArray = attr.getStrokeDashArray();
                    dashArray.add(Double.valueOf(2.0d));
                    dashArray.add(Double.valueOf(2.0d));
                } else if (highlight == InputMethodHighlight.SELECTED_CONVERTED) {
                    attr = new Line(minX + 2.0d, maxY + 1.0d, maxX - 2.0d, maxY + 1.0d);
                    attr.setStroke(this.textFill.get());
                    attr.setStrokeWidth((maxY - minY) * 3.0d);
                } else if (highlight == InputMethodHighlight.UNSELECTED_CONVERTED) {
                    attr = new Line(minX + 2.0d, maxY + 1.0d, maxX - 2.0d, maxY + 1.0d);
                    attr.setStroke(this.textFill.get());
                    attr.setStrokeWidth(maxY - minY);
                }
                if (attr != null) {
                    attr.setManaged(false);
                    this.imattrs.add(attr);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected boolean isRTL() {
        return ((TextInputControl) getSkinnable()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
    }

    public void setCaretAnimating(boolean value) {
        if (value) {
            this.caretBlinking.start();
        } else {
            this.caretBlinking.stop();
            this.blink.set(true);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TextInputControlSkin$CaretBlinking.class */
    private static final class CaretBlinking {
        private final Timeline caretTimeline = new Timeline();
        private final WeakReference<BooleanProperty> blinkPropertyRef;

        public CaretBlinking(BooleanProperty blinkProperty) {
            this.blinkPropertyRef = new WeakReference<>(blinkProperty);
            this.caretTimeline.setCycleCount(-1);
            this.caretTimeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, (EventHandler<ActionEvent>) event -> {
                setBlink(false);
            }, new KeyValue[0]), new KeyFrame(Duration.seconds(0.5d), (EventHandler<ActionEvent>) event2 -> {
                setBlink(true);
            }, new KeyValue[0]), new KeyFrame(Duration.seconds(1.0d), new KeyValue[0]));
        }

        public void start() {
            this.caretTimeline.play();
        }

        public void stop() {
            this.caretTimeline.stop();
        }

        private void setBlink(boolean value) {
            BooleanProperty blinkProperty = this.blinkPropertyRef.get();
            if (blinkProperty == null) {
                this.caretTimeline.stop();
            } else {
                blinkProperty.set(value);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TextInputControlSkin$ContextMenuItem.class */
    class ContextMenuItem extends MenuItem {
        ContextMenuItem(String action) {
            super(ControlResources.getString("TextInputControl.menu." + action));
            setOnAction(e2 -> {
                ((TextInputControlBehavior) TextInputControlSkin.this.getBehavior()).callAction(action);
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void populateContextMenu(ContextMenu contextMenu) {
        TextInputControl textInputControl = (TextInputControl) getSkinnable();
        boolean editable = textInputControl.isEditable();
        boolean hasText = textInputControl.getLength() > 0;
        boolean hasSelection = textInputControl.getSelection().getLength() > 0;
        boolean maskText = maskText("A") != "A";
        ObservableList<MenuItem> items = contextMenu.getItems();
        if (SHOW_HANDLES) {
            items.clear();
            if (!maskText && hasSelection) {
                if (editable) {
                    items.add(this.cutMI);
                }
                items.add(this.copyMI);
            }
            if (editable && Clipboard.getSystemClipboard().hasString()) {
                items.add(this.pasteMI);
            }
            if (hasText) {
                if (!hasSelection) {
                    items.add(this.selectWordMI);
                }
                items.add(this.selectAllMI);
            }
            this.selectWordMI.getProperties().put("refreshMenu", Boolean.TRUE);
            this.selectAllMI.getProperties().put("refreshMenu", Boolean.TRUE);
            return;
        }
        if (editable) {
            items.setAll(this.undoMI, this.redoMI, this.cutMI, this.copyMI, this.pasteMI, this.deleteMI, this.separatorMI, this.selectAllMI);
        } else {
            items.setAll(this.copyMI, this.separatorMI, this.selectAllMI);
        }
        this.undoMI.setDisable(!((TextInputControl) getSkinnable()).isUndoable());
        this.redoMI.setDisable(!((TextInputControl) getSkinnable()).isRedoable());
        this.cutMI.setDisable(maskText || !hasSelection);
        this.copyMI.setDisable(maskText || !hasSelection);
        this.pasteMI.setDisable(!Clipboard.getSystemClipboard().hasString());
        this.deleteMI.setDisable(!hasSelection);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TextInputControlSkin$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TextInputControl, Paint> TEXT_FILL = new CssMetaData<TextInputControl, Paint>("-fx-text-fill", PaintConverter.getInstance(), Color.BLACK) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return skin.textFill == null || !skin.textFill.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return (StyleableProperty) skin.textFill;
            }
        };
        private static final CssMetaData<TextInputControl, Paint> PROMPT_TEXT_FILL = new CssMetaData<TextInputControl, Paint>("-fx-prompt-text-fill", PaintConverter.getInstance(), Color.GRAY) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return skin.promptTextFill == null || !skin.promptTextFill.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return (StyleableProperty) skin.promptTextFill;
            }
        };
        private static final CssMetaData<TextInputControl, Paint> HIGHLIGHT_FILL = new CssMetaData<TextInputControl, Paint>("-fx-highlight-fill", PaintConverter.getInstance(), Color.DODGERBLUE) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return skin.highlightFill == null || !skin.highlightFill.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return (StyleableProperty) skin.highlightFill;
            }
        };
        private static final CssMetaData<TextInputControl, Paint> HIGHLIGHT_TEXT_FILL = new CssMetaData<TextInputControl, Paint>("-fx-highlight-text-fill", PaintConverter.getInstance(), Color.WHITE) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return skin.highlightTextFill == null || !skin.highlightTextFill.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return (StyleableProperty) skin.highlightTextFill;
            }
        };
        private static final CssMetaData<TextInputControl, Boolean> DISPLAY_CARET = new CssMetaData<TextInputControl, Boolean>("-fx-display-caret", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: com.sun.javafx.scene.control.skin.TextInputControlSkin.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return skin.displayCaret == null || !skin.displayCaret.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(TextInputControl n2) {
                TextInputControlSkin<?, ?> skin = (TextInputControlSkin) n2.getSkin();
                return (StyleableProperty) skin.displayCaret;
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(SkinBase.getClassCssMetaData());
            styleables.add(TEXT_FILL);
            styleables.add(PROMPT_TEXT_FILL);
            styleables.add(HIGHLIGHT_FILL);
            styleables.add(HIGHLIGHT_TEXT_FILL);
            styleables.add(DISPLAY_CARET);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.SkinBase
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.SkinBase
    protected void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case SHOW_TEXT_RANGE:
                Integer start = (Integer) parameters[0];
                Integer end = (Integer) parameters[1];
                if (start != null && end != null) {
                    scrollCharacterToVisible(end.intValue());
                    scrollCharacterToVisible(start.intValue());
                    scrollCharacterToVisible(end.intValue());
                    break;
                }
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                break;
        }
    }
}
