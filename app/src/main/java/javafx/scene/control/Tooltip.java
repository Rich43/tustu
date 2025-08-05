package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.DurationConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.skin.TooltipSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;
import javafx.util.Duration;

@IDProperty("id")
/* loaded from: jfxrt.jar:javafx/scene/control/Tooltip.class */
public class Tooltip extends PopupControl {
    private final StringProperty text;
    private final ObjectProperty<TextAlignment> textAlignment;
    private final ObjectProperty<OverrunStyle> textOverrun;
    private final BooleanProperty wrapText;
    private final ObjectProperty<Font> font;
    private final ObjectProperty<Duration> showDelayProperty;
    private final ObjectProperty<Duration> showDurationProperty;
    private final ObjectProperty<Duration> hideDelayProperty;
    private final ObjectProperty<Node> graphic;
    private StyleableStringProperty imageUrl;
    private final ObjectProperty<ContentDisplay> contentDisplay;
    private final DoubleProperty graphicTextGap;
    private final ReadOnlyBooleanWrapper activated;
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
    private static String TOOLTIP_PROP_KEY = "javafx.scene.control.Tooltip";
    private static int TOOLTIP_XOFFSET = 10;
    private static int TOOLTIP_YOFFSET = 7;
    private static TooltipBehavior BEHAVIOR = new TooltipBehavior(false);
    private static final CssMetaData<CSSBridge, Font> FONT = new FontCssMetaData<CSSBridge>("-fx-font", Font.getDefault()) { // from class: javafx.scene.control.Tooltip.5
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.fontProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Font> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.fontProperty();
        }
    };
    private static final CssMetaData<CSSBridge, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<CSSBridge, TextAlignment>("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) { // from class: javafx.scene.control.Tooltip.6
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.textAlignmentProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<TextAlignment> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.textAlignmentProperty();
        }
    };
    private static final CssMetaData<CSSBridge, OverrunStyle> TEXT_OVERRUN = new CssMetaData<CSSBridge, OverrunStyle>("-fx-text-overrun", new EnumConverter(OverrunStyle.class), OverrunStyle.ELLIPSIS) { // from class: javafx.scene.control.Tooltip.7
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.textOverrunProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<OverrunStyle> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.textOverrunProperty();
        }
    };
    private static final CssMetaData<CSSBridge, Boolean> WRAP_TEXT = new CssMetaData<CSSBridge, Boolean>("-fx-wrap-text", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.control.Tooltip.8
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.wrapTextProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Boolean> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.wrapTextProperty();
        }
    };
    private static final CssMetaData<CSSBridge, String> GRAPHIC = new CssMetaData<CSSBridge, String>("-fx-graphic", StringConverter.getInstance()) { // from class: javafx.scene.control.Tooltip.9
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.graphicProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<String> getStyleableProperty(CSSBridge cssBridge) {
            return cssBridge.tooltip.imageUrlProperty();
        }
    };
    private static final CssMetaData<CSSBridge, ContentDisplay> CONTENT_DISPLAY = new CssMetaData<CSSBridge, ContentDisplay>("-fx-content-display", new EnumConverter(ContentDisplay.class), ContentDisplay.LEFT) { // from class: javafx.scene.control.Tooltip.10
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.contentDisplayProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<ContentDisplay> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.contentDisplayProperty();
        }
    };
    private static final CssMetaData<CSSBridge, Number> GRAPHIC_TEXT_GAP = new CssMetaData<CSSBridge, Number>("-fx-graphic-text-gap", SizeConverter.getInstance(), Double.valueOf(4.0d)) { // from class: javafx.scene.control.Tooltip.11
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.graphicTextGapProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Number> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.graphicTextGapProperty();
        }
    };
    private static final CssMetaData<CSSBridge, Duration> SHOW_DELAY = new CssMetaData<CSSBridge, Duration>("-fx-show-delay", DurationConverter.getInstance(), new Duration(1000.0d)) { // from class: javafx.scene.control.Tooltip.12
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.showDelayProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Duration> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.showDelayProperty();
        }
    };
    private static final CssMetaData<CSSBridge, Duration> SHOW_DURATION = new CssMetaData<CSSBridge, Duration>("-fx-show-duration", DurationConverter.getInstance(), new Duration(5000.0d)) { // from class: javafx.scene.control.Tooltip.13
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.showDurationProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Duration> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.showDurationProperty();
        }
    };
    private static final CssMetaData<CSSBridge, Duration> HIDE_DELAY = new CssMetaData<CSSBridge, Duration>("-fx-hide-delay", DurationConverter.getInstance(), new Duration(200.0d)) { // from class: javafx.scene.control.Tooltip.14
        @Override // javafx.css.CssMetaData
        public boolean isSettable(CSSBridge cssBridge) {
            return !cssBridge.tooltip.hideDelayProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Duration> getStyleableProperty(CSSBridge cssBridge) {
            return (StyleableProperty) cssBridge.tooltip.hideDelayProperty();
        }
    };

    static {
        List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(PopupControl.getClassCssMetaData());
        styleables.add(FONT);
        styleables.add(TEXT_ALIGNMENT);
        styleables.add(TEXT_OVERRUN);
        styleables.add(WRAP_TEXT);
        styleables.add(GRAPHIC);
        styleables.add(CONTENT_DISPLAY);
        styleables.add(GRAPHIC_TEXT_GAP);
        styleables.add(SHOW_DELAY);
        styleables.add(SHOW_DURATION);
        styleables.add(HIDE_DELAY);
        STYLEABLES = Collections.unmodifiableList(styleables);
    }

    public static void install(Node node, Tooltip t2) {
        BEHAVIOR.install(node, t2);
    }

    public static void uninstall(Node node, Tooltip t2) {
        BEHAVIOR.uninstall(node);
    }

    public Tooltip() {
        this(null);
    }

    public Tooltip(String text) {
        this.text = new SimpleStringProperty(this, "text", "") { // from class: javafx.scene.control.Tooltip.1
            @Override // javafx.beans.property.StringPropertyBase
            protected void invalidated() {
                super.invalidated();
                String value = get();
                if (!Tooltip.this.isShowing() || value == null || value.equals(Tooltip.this.getText())) {
                    return;
                }
                Tooltip.this.setAnchorX(Tooltip.BEHAVIOR.lastMouseX);
                Tooltip.this.setAnchorY(Tooltip.BEHAVIOR.lastMouseY);
            }
        };
        this.textAlignment = new SimpleStyleableObjectProperty(TEXT_ALIGNMENT, this, "textAlignment", TextAlignment.LEFT);
        this.textOverrun = new SimpleStyleableObjectProperty(TEXT_OVERRUN, this, "textOverrun", OverrunStyle.ELLIPSIS);
        this.wrapText = new SimpleStyleableBooleanProperty(WRAP_TEXT, this, "wrapText", false);
        this.font = new StyleableObjectProperty<Font>(Font.getDefault()) { // from class: javafx.scene.control.Tooltip.2
            private boolean fontSetByCss = false;

            @Override // javafx.css.StyleableObjectProperty, javafx.css.StyleableProperty
            public void applyStyle(StyleOrigin newOrigin, Font value) {
                try {
                    try {
                        this.fontSetByCss = true;
                        super.applyStyle(newOrigin, (StyleOrigin) value);
                        this.fontSetByCss = false;
                    } catch (Exception e2) {
                        throw e2;
                    }
                } catch (Throwable th) {
                    this.fontSetByCss = false;
                    throw th;
                }
            }

            @Override // javafx.css.StyleableObjectProperty, javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
            public void set(Font value) {
                Font oldValue = get();
                StyleOrigin origin = ((StyleableObjectProperty) Tooltip.this.font).getStyleOrigin();
                if (origin != null) {
                    if (value != null) {
                        if (value.equals(oldValue)) {
                            return;
                        }
                    } else if (oldValue == null) {
                        return;
                    }
                }
                super.set((AnonymousClass2) value);
            }

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (!this.fontSetByCss) {
                    Tooltip.this.bridge.impl_reapplyCSS();
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<CSSBridge, Font> getCssMetaData() {
                return Tooltip.FONT;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Tooltip.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "font";
            }
        };
        this.showDelayProperty = new SimpleStyleableObjectProperty(SHOW_DELAY, this, "showDelay", new Duration(1000.0d));
        this.showDurationProperty = new SimpleStyleableObjectProperty(SHOW_DURATION, this, "showDuration", new Duration(5000.0d));
        this.hideDelayProperty = new SimpleStyleableObjectProperty(HIDE_DELAY, this, "hideDelay", new Duration(200.0d));
        this.graphic = new StyleableObjectProperty<Node>() { // from class: javafx.scene.control.Tooltip.3
            @Override // javafx.css.StyleableProperty
            public CssMetaData getCssMetaData() {
                return Tooltip.GRAPHIC;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Tooltip.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "graphic";
            }
        };
        this.imageUrl = null;
        this.contentDisplay = new SimpleStyleableObjectProperty(CONTENT_DISPLAY, this, "contentDisplay", ContentDisplay.LEFT);
        this.graphicTextGap = new SimpleStyleableDoubleProperty(GRAPHIC_TEXT_GAP, this, "graphicTextGap", Double.valueOf(4.0d));
        this.activated = new ReadOnlyBooleanWrapper(this, "activated");
        if (text != null) {
            setText(text);
        }
        this.bridge = new CSSBridge();
        getContent().setAll(this.bridge);
        getStyleClass().setAll("tooltip");
    }

    public final StringProperty textProperty() {
        return this.text;
    }

    public final void setText(String value) {
        textProperty().setValue(value);
    }

    public final String getText() {
        return this.text.getValue2() == null ? "" : this.text.getValue2();
    }

    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
        return this.textAlignment;
    }

    public final void setTextAlignment(TextAlignment value) {
        textAlignmentProperty().setValue(value);
    }

    public final TextAlignment getTextAlignment() {
        return textAlignmentProperty().getValue2();
    }

    public final ObjectProperty<OverrunStyle> textOverrunProperty() {
        return this.textOverrun;
    }

    public final void setTextOverrun(OverrunStyle value) {
        textOverrunProperty().setValue(value);
    }

    public final OverrunStyle getTextOverrun() {
        return textOverrunProperty().getValue2();
    }

    public final BooleanProperty wrapTextProperty() {
        return this.wrapText;
    }

    public final void setWrapText(boolean value) {
        wrapTextProperty().setValue(Boolean.valueOf(value));
    }

    public final boolean isWrapText() {
        return wrapTextProperty().getValue2().booleanValue();
    }

    public final ObjectProperty<Font> fontProperty() {
        return this.font;
    }

    public final void setFont(Font value) {
        fontProperty().setValue(value);
    }

    public final Font getFont() {
        return fontProperty().getValue2();
    }

    public final ObjectProperty<Duration> showDelayProperty() {
        return this.showDelayProperty;
    }

    public final void setShowDelay(Duration showDelay) {
        this.showDelayProperty.set(showDelay);
    }

    public final Duration getShowDelay() {
        return this.showDelayProperty.get();
    }

    public final ObjectProperty<Duration> showDurationProperty() {
        return this.showDurationProperty;
    }

    public final void setShowDuration(Duration showDuration) {
        this.showDurationProperty.set(showDuration);
    }

    public final Duration getShowDuration() {
        return this.showDurationProperty.get();
    }

    public final ObjectProperty<Duration> hideDelayProperty() {
        return this.hideDelayProperty;
    }

    public final void setHideDelay(Duration hideDelay) {
        this.hideDelayProperty.set(hideDelay);
    }

    public final Duration getHideDelay() {
        return this.hideDelayProperty.get();
    }

    public final ObjectProperty<Node> graphicProperty() {
        return this.graphic;
    }

    public final void setGraphic(Node value) {
        graphicProperty().setValue(value);
    }

    public final Node getGraphic() {
        return graphicProperty().getValue2();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleableStringProperty imageUrlProperty() {
        if (this.imageUrl == null) {
            this.imageUrl = new StyleableStringProperty() { // from class: javafx.scene.control.Tooltip.4
                StyleOrigin origin = StyleOrigin.USER;

                @Override // javafx.css.StyleableStringProperty, javafx.css.StyleableProperty
                public void applyStyle(StyleOrigin origin, String v2) {
                    this.origin = origin;
                    if (Tooltip.this.graphic == null || !Tooltip.this.graphic.isBound()) {
                        super.applyStyle(origin, v2);
                    }
                    this.origin = StyleOrigin.USER;
                }

                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    String url = super.get();
                    if (url == null) {
                        ((StyleableProperty) Tooltip.this.graphicProperty()).applyStyle(this.origin, null);
                        return;
                    }
                    Node graphicNode = Tooltip.this.getGraphic();
                    if (graphicNode instanceof ImageView) {
                        ImageView imageView = (ImageView) graphicNode;
                        Image image = imageView.getImage();
                        if (image != null) {
                            String imageViewUrl = image.impl_getUrl();
                            if (url.equals(imageViewUrl)) {
                                return;
                            }
                        }
                    }
                    Image img = StyleManager.getInstance().getCachedImage(url);
                    if (img != null) {
                        ((StyleableProperty) Tooltip.this.graphicProperty()).applyStyle(this.origin, new ImageView(img));
                    }
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.beans.property.StringPropertyBase, javafx.beans.value.ObservableObjectValue
                public String get() {
                    Image image;
                    Node graphic = Tooltip.this.getGraphic();
                    if ((graphic instanceof ImageView) && (image = ((ImageView) graphic).getImage()) != null) {
                        return image.impl_getUrl();
                    }
                    return null;
                }

                @Override // javafx.css.StyleableStringProperty, javafx.css.StyleableProperty
                public StyleOrigin getStyleOrigin() {
                    if (Tooltip.this.graphic != null) {
                        return ((StyleableProperty) Tooltip.this.graphic).getStyleOrigin();
                    }
                    return null;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Tooltip.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "imageUrl";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, String> getCssMetaData() {
                    return Tooltip.GRAPHIC;
                }
            };
        }
        return this.imageUrl;
    }

    public final ObjectProperty<ContentDisplay> contentDisplayProperty() {
        return this.contentDisplay;
    }

    public final void setContentDisplay(ContentDisplay value) {
        contentDisplayProperty().setValue(value);
    }

    public final ContentDisplay getContentDisplay() {
        return contentDisplayProperty().getValue2();
    }

    public final DoubleProperty graphicTextGapProperty() {
        return this.graphicTextGap;
    }

    public final void setGraphicTextGap(double value) {
        graphicTextGapProperty().setValue((Number) Double.valueOf(value));
    }

    public final double getGraphicTextGap() {
        return graphicTextGapProperty().getValue2().doubleValue();
    }

    final void setActivated(boolean value) {
        this.activated.set(value);
    }

    public final boolean isActivated() {
        return this.activated.get();
    }

    public final ReadOnlyBooleanProperty activatedProperty() {
        return this.activated.getReadOnlyProperty();
    }

    @Override // javafx.scene.control.PopupControl
    protected Skin<?> createDefaultSkin() {
        return new TooltipSkin(this);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override // javafx.scene.control.PopupControl, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.PopupControl, javafx.css.Styleable
    public Styleable getStyleableParent() {
        if (BEHAVIOR.hoveredNode == null) {
            return super.getStyleableParent();
        }
        return BEHAVIOR.hoveredNode;
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/Tooltip$CSSBridge.class */
    private final class CSSBridge extends PopupControl.CSSBridge {
        private Tooltip tooltip;

        CSSBridge() {
            super();
            this.tooltip = Tooltip.this;
            setAccessibleRole(AccessibleRole.TOOLTIP);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/Tooltip$TooltipBehavior.class */
    private static class TooltipBehavior {
        private Node hoveredNode;
        private Tooltip activatedTooltip;
        private Tooltip visibleTooltip;
        private double lastMouseX;
        private double lastMouseY;
        private boolean hideOnExit;
        static final /* synthetic */ boolean $assertionsDisabled;
        private Timeline activationTimer = new Timeline();
        private Timeline hideTimer = new Timeline();
        private Timeline leftTimer = new Timeline();
        private boolean cssForced = false;
        private EventHandler<MouseEvent> MOVE_HANDLER = event -> {
            this.lastMouseX = event.getScreenX();
            this.lastMouseY = event.getScreenY();
            if (this.hideTimer.getStatus() == Animation.Status.RUNNING) {
                return;
            }
            this.hoveredNode = (Node) event.getSource();
            Tooltip t2 = (Tooltip) this.hoveredNode.getProperties().get(Tooltip.TOOLTIP_PROP_KEY);
            if (t2 != null) {
                Window owner = getWindow(this.hoveredNode);
                boolean treeVisible = isWindowHierarchyVisible(this.hoveredNode);
                if (owner != null && treeVisible) {
                    if (this.leftTimer.getStatus() == Animation.Status.RUNNING) {
                        if (this.visibleTooltip != null) {
                            this.visibleTooltip.hide();
                        }
                        this.visibleTooltip = t2;
                        t2.show(owner, event.getScreenX() + Tooltip.TOOLTIP_XOFFSET, event.getScreenY() + Tooltip.TOOLTIP_YOFFSET);
                        this.leftTimer.stop();
                        if (t2.getShowDuration() != null) {
                            this.hideTimer.getKeyFrames().setAll(new KeyFrame(t2.getShowDuration(), new KeyValue[0]));
                        }
                        this.hideTimer.playFromStart();
                        return;
                    }
                    if (!this.cssForced) {
                        double opacity = t2.getOpacity();
                        t2.setOpacity(0.0d);
                        t2.show(owner);
                        t2.hide();
                        t2.setOpacity(opacity);
                        this.cssForced = true;
                    }
                    t2.setActivated(true);
                    this.activatedTooltip = t2;
                    this.activationTimer.stop();
                    if (t2.getShowDelay() != null) {
                        this.activationTimer.getKeyFrames().setAll(new KeyFrame(t2.getShowDelay(), new KeyValue[0]));
                    }
                    this.activationTimer.playFromStart();
                }
            }
        };
        private EventHandler<MouseEvent> LEAVING_HANDLER = event -> {
            if (this.activationTimer.getStatus() == Animation.Status.RUNNING) {
                this.activationTimer.stop();
            } else if (this.hideTimer.getStatus() == Animation.Status.RUNNING) {
                if (!$assertionsDisabled && this.visibleTooltip == null) {
                    throw new AssertionError();
                }
                this.hideTimer.stop();
                if (this.hideOnExit) {
                    this.visibleTooltip.hide();
                }
                Node source = (Node) event.getSource();
                Tooltip t2 = (Tooltip) source.getProperties().get(Tooltip.TOOLTIP_PROP_KEY);
                if (t2 != null) {
                    if (t2.getHideDelay() != null) {
                        this.leftTimer.getKeyFrames().setAll(new KeyFrame(t2.getHideDelay(), new KeyValue[0]));
                    }
                    this.leftTimer.playFromStart();
                }
            }
            this.hoveredNode = null;
            this.activatedTooltip = null;
            if (this.hideOnExit) {
                this.visibleTooltip = null;
            }
        };
        private EventHandler<MouseEvent> KILL_HANDLER = event -> {
            this.activationTimer.stop();
            this.hideTimer.stop();
            this.leftTimer.stop();
            if (this.visibleTooltip != null) {
                this.visibleTooltip.hide();
            }
            this.hoveredNode = null;
            this.activatedTooltip = null;
            this.visibleTooltip = null;
        };

        static {
            $assertionsDisabled = !Tooltip.class.desiredAssertionStatus();
        }

        TooltipBehavior(boolean hideOnExit) {
            this.hideOnExit = hideOnExit;
            this.activationTimer.setOnFinished(event -> {
                if (!$assertionsDisabled && this.activatedTooltip == null) {
                    throw new AssertionError();
                }
                Window owner = getWindow(this.hoveredNode);
                boolean treeVisible = isWindowHierarchyVisible(this.hoveredNode);
                if (owner != null && owner.isShowing() && treeVisible) {
                    double x2 = this.lastMouseX;
                    double y2 = this.lastMouseY;
                    NodeOrientation nodeOrientation = this.hoveredNode.getEffectiveNodeOrientation();
                    this.activatedTooltip.getScene().setNodeOrientation(nodeOrientation);
                    if (nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
                        x2 -= this.activatedTooltip.getWidth();
                    }
                    this.activatedTooltip.show(owner, x2 + Tooltip.TOOLTIP_XOFFSET, y2 + Tooltip.TOOLTIP_YOFFSET);
                    if (y2 + Tooltip.TOOLTIP_YOFFSET > this.activatedTooltip.getAnchorY()) {
                        this.activatedTooltip.hide();
                        this.activatedTooltip.show(owner, x2 + Tooltip.TOOLTIP_XOFFSET, y2 - this.activatedTooltip.getHeight());
                    }
                    this.visibleTooltip = this.activatedTooltip;
                    this.hoveredNode = null;
                    if (this.activatedTooltip.getShowDuration() != null) {
                        this.hideTimer.getKeyFrames().setAll(new KeyFrame(this.activatedTooltip.getShowDuration(), new KeyValue[0]));
                    }
                    this.hideTimer.playFromStart();
                }
                this.activatedTooltip.setActivated(false);
                this.activatedTooltip = null;
            });
            this.hideTimer.setOnFinished(event2 -> {
                if (!$assertionsDisabled && this.visibleTooltip == null) {
                    throw new AssertionError();
                }
                this.visibleTooltip.hide();
                this.visibleTooltip = null;
                this.hoveredNode = null;
            });
            this.leftTimer.setOnFinished(event3 -> {
                if (!hideOnExit) {
                    if (!$assertionsDisabled && this.visibleTooltip == null) {
                        throw new AssertionError();
                    }
                    this.visibleTooltip.hide();
                    this.visibleTooltip = null;
                    this.hoveredNode = null;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void install(Node node, Tooltip t2) {
            if (node == null) {
                return;
            }
            node.addEventHandler(MouseEvent.MOUSE_MOVED, this.MOVE_HANDLER);
            node.addEventHandler(MouseEvent.MOUSE_EXITED, this.LEAVING_HANDLER);
            node.addEventHandler(MouseEvent.MOUSE_PRESSED, this.KILL_HANDLER);
            node.getProperties().put(Tooltip.TOOLTIP_PROP_KEY, t2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void uninstall(Node node) {
            if (node == null) {
                return;
            }
            node.removeEventHandler(MouseEvent.MOUSE_MOVED, this.MOVE_HANDLER);
            node.removeEventHandler(MouseEvent.MOUSE_EXITED, this.LEAVING_HANDLER);
            node.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.KILL_HANDLER);
            Tooltip t2 = (Tooltip) node.getProperties().get(Tooltip.TOOLTIP_PROP_KEY);
            if (t2 != null) {
                node.getProperties().remove(Tooltip.TOOLTIP_PROP_KEY);
                if (t2.equals(this.visibleTooltip) || t2.equals(this.activatedTooltip)) {
                    this.KILL_HANDLER.handle(null);
                }
            }
        }

        private Window getWindow(Node node) {
            Scene scene = node == null ? null : node.getScene();
            if (scene == null) {
                return null;
            }
            return scene.getWindow();
        }

        private boolean isWindowHierarchyVisible(Node node) {
            boolean treeVisible = node != null;
            Parent parent = node == null ? null : node.getParent();
            while (true) {
                Parent parent2 = parent;
                if (parent2 == null || !treeVisible) {
                    break;
                }
                treeVisible = parent2.isVisible();
                parent = parent2.getParent();
            }
            return treeVisible;
        }
    }
}
