package javafx.scene.web;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.web.NGWebView;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.webkit.InputMethodClientImpl;
import com.sun.javafx.webkit.KeyCodeMap;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.webkit.WebPage;
import com.sun.webkit.event.WCFocusEvent;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.event.WCKeyEvent;
import com.sun.webkit.event.WCMouseEvent;
import com.sun.webkit.event.WCMouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.Stage;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:javafx/scene/web/WebView.class */
public final class WebView extends Parent {
    private static final boolean DEFAULT_CONTEXT_MENU_ENABLED = true;
    private static final double DEFAULT_ZOOM = 1.0d;
    private static final double DEFAULT_FONT_SCALE = 1.0d;
    private static final double DEFAULT_MIN_WIDTH = 0.0d;
    private static final double DEFAULT_MIN_HEIGHT = 0.0d;
    private static final double DEFAULT_PREF_WIDTH = 800.0d;
    private static final double DEFAULT_PREF_HEIGHT = 600.0d;
    private static final double DEFAULT_MAX_WIDTH = Double.MAX_VALUE;
    private static final double DEFAULT_MAX_HEIGHT = Double.MAX_VALUE;
    private final WebPage page;
    private final WebEngine engine;
    private volatile InputMethodClientImpl imClient;
    private final TKPulseListener stagePulseListener;
    private final ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper(this, MetadataParser.WIDTH_TAG_NAME);
    private final ReadOnlyDoubleWrapper height = new ReadOnlyDoubleWrapper(this, MetadataParser.HEIGHT_TAG_NAME);
    private DoubleProperty zoom;
    private DoubleProperty fontScale;
    private DoubleProperty minWidth;
    private DoubleProperty minHeight;
    private DoubleProperty prefWidth;
    private DoubleProperty prefHeight;
    private DoubleProperty maxWidth;
    private DoubleProperty maxHeight;
    private ObjectProperty<FontSmoothingType> fontSmoothingType;
    private BooleanProperty contextMenuEnabled;
    private static final int WK_DND_ACTION_NONE = 0;
    private static final int WK_DND_ACTION_COPY = 1;
    private static final int WK_DND_ACTION_MOVE = 2;
    private static final int WK_DND_ACTION_LINK = 1073741824;
    private static final Map<Object, Integer> idMap = new HashMap();
    private static final FontSmoothingType DEFAULT_FONT_SMOOTHING_TYPE = FontSmoothingType.LCD;

    static {
        idMap.put(MouseButton.NONE, 0);
        idMap.put(MouseButton.PRIMARY, 1);
        idMap.put(MouseButton.MIDDLE, 2);
        idMap.put(MouseButton.SECONDARY, 4);
        idMap.put(MouseEvent.MOUSE_PRESSED, 0);
        idMap.put(MouseEvent.MOUSE_RELEASED, 1);
        idMap.put(MouseEvent.MOUSE_MOVED, 2);
        idMap.put(MouseEvent.MOUSE_DRAGGED, 3);
        idMap.put(KeyEvent.KEY_PRESSED, 1);
        idMap.put(KeyEvent.KEY_RELEASED, 2);
        idMap.put(KeyEvent.KEY_TYPED, 0);
    }

    public final WebEngine getEngine() {
        return this.engine;
    }

    public final double getWidth() {
        return this.width.get();
    }

    public ReadOnlyDoubleProperty widthProperty() {
        return this.width.getReadOnlyProperty();
    }

    public final double getHeight() {
        return this.height.get();
    }

    public ReadOnlyDoubleProperty heightProperty() {
        return this.height.getReadOnlyProperty();
    }

    public final void setZoom(double value) {
        WebEngine.checkThread();
        zoomProperty().set(value);
    }

    public final double getZoom() {
        if (this.zoom != null) {
            return this.zoom.get();
        }
        return 1.0d;
    }

    public final DoubleProperty zoomProperty() {
        if (this.zoom == null) {
            this.zoom = new StyleableDoubleProperty(1.0d) { // from class: javafx.scene.web.WebView.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Toolkit.getToolkit().checkFxUserThread();
                    WebView.this.page.setZoomFactor((float) get(), false);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.ZOOM;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "zoom";
                }
            };
        }
        return this.zoom;
    }

    public final void setFontScale(double value) {
        WebEngine.checkThread();
        fontScaleProperty().set(value);
    }

    public final double getFontScale() {
        if (this.fontScale != null) {
            return this.fontScale.get();
        }
        return 1.0d;
    }

    public DoubleProperty fontScaleProperty() {
        if (this.fontScale == null) {
            this.fontScale = new StyleableDoubleProperty(1.0d) { // from class: javafx.scene.web.WebView.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Toolkit.getToolkit().checkFxUserThread();
                    WebView.this.page.setZoomFactor((float) get(), true);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.FONT_SCALE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fontScale";
                }
            };
        }
        return this.fontScale;
    }

    public WebView() {
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        getStyleClass().add("web-view");
        this.engine = new WebEngine();
        this.engine.setView(this);
        this.page = this.engine.getPage();
        this.page.setFontSmoothingType(DEFAULT_FONT_SMOOTHING_TYPE.ordinal());
        registerEventHandlers();
        this.stagePulseListener = () -> {
            handleStagePulse();
        };
        focusedProperty().addListener((ov, t2, t1) -> {
            if (this.page != null) {
                WCFocusEvent focusEvent = new WCFocusEvent(isFocused() ? 2 : 3, -1);
                this.page.dispatchFocusEvent(focusEvent);
            }
        });
        setFocusTraversable(true);
        Toolkit.getToolkit().addStageTkPulseListener(this.stagePulseListener);
    }

    @Override // javafx.scene.Node
    public boolean isResizable() {
        return true;
    }

    @Override // javafx.scene.Node
    public void resize(double width, double height) {
        if (width != this.width.get() || height != this.height.get()) {
            this.width.set(width);
            this.height.set(height);
            impl_markDirty(DirtyBits.NODE_GEOMETRY);
            impl_geomChanged();
        }
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double minWidth(double height) {
        double result = getMinWidth();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double minHeight(double width) {
        double result = getMinHeight();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double prefWidth(double height) {
        double result = getPrefWidth();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public final double prefHeight(double width) {
        double result = getPrefHeight();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Node
    public final double maxWidth(double height) {
        double result = getMaxWidth();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Node
    public final double maxHeight(double width) {
        double result = getMaxHeight();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    public DoubleProperty minWidthProperty() {
        if (this.minWidth == null) {
            this.minWidth = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.web.WebView.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (WebView.this.getParent() != null) {
                        WebView.this.getParent().requestLayout();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MIN_WIDTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minWidth";
                }
            };
        }
        return this.minWidth;
    }

    public final void setMinWidth(double value) {
        minWidthProperty().set(value);
    }

    public final double getMinWidth() {
        if (this.minWidth != null) {
            return this.minWidth.get();
        }
        return 0.0d;
    }

    public DoubleProperty minHeightProperty() {
        if (this.minHeight == null) {
            this.minHeight = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.web.WebView.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (WebView.this.getParent() != null) {
                        WebView.this.getParent().requestLayout();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MIN_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "minHeight";
                }
            };
        }
        return this.minHeight;
    }

    public final void setMinHeight(double value) {
        minHeightProperty().set(value);
    }

    public final double getMinHeight() {
        if (this.minHeight != null) {
            return this.minHeight.get();
        }
        return 0.0d;
    }

    public void setMinSize(double minWidth, double minHeight) {
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }

    public DoubleProperty prefWidthProperty() {
        if (this.prefWidth == null) {
            this.prefWidth = new StyleableDoubleProperty(DEFAULT_PREF_WIDTH) { // from class: javafx.scene.web.WebView.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (WebView.this.getParent() != null) {
                        WebView.this.getParent().requestLayout();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.PREF_WIDTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefWidth";
                }
            };
        }
        return this.prefWidth;
    }

    public final void setPrefWidth(double value) {
        prefWidthProperty().set(value);
    }

    public final double getPrefWidth() {
        return this.prefWidth != null ? this.prefWidth.get() : DEFAULT_PREF_WIDTH;
    }

    public DoubleProperty prefHeightProperty() {
        if (this.prefHeight == null) {
            this.prefHeight = new StyleableDoubleProperty(DEFAULT_PREF_HEIGHT) { // from class: javafx.scene.web.WebView.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (WebView.this.getParent() != null) {
                        WebView.this.getParent().requestLayout();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.PREF_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefHeight";
                }
            };
        }
        return this.prefHeight;
    }

    public final void setPrefHeight(double value) {
        prefHeightProperty().set(value);
    }

    public final double getPrefHeight() {
        return this.prefHeight != null ? this.prefHeight.get() : DEFAULT_PREF_HEIGHT;
    }

    public void setPrefSize(double prefWidth, double prefHeight) {
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
    }

    public DoubleProperty maxWidthProperty() {
        if (this.maxWidth == null) {
            this.maxWidth = new StyleableDoubleProperty(Double.MAX_VALUE) { // from class: javafx.scene.web.WebView.7
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (WebView.this.getParent() != null) {
                        WebView.this.getParent().requestLayout();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MAX_WIDTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxWidth";
                }
            };
        }
        return this.maxWidth;
    }

    public final void setMaxWidth(double value) {
        maxWidthProperty().set(value);
    }

    public final double getMaxWidth() {
        if (this.maxWidth != null) {
            return this.maxWidth.get();
        }
        return Double.MAX_VALUE;
    }

    public DoubleProperty maxHeightProperty() {
        if (this.maxHeight == null) {
            this.maxHeight = new StyleableDoubleProperty(Double.MAX_VALUE) { // from class: javafx.scene.web.WebView.8
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (WebView.this.getParent() != null) {
                        WebView.this.getParent().requestLayout();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MAX_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "maxHeight";
                }
            };
        }
        return this.maxHeight;
    }

    public final void setMaxHeight(double value) {
        maxHeightProperty().set(value);
    }

    public final double getMaxHeight() {
        if (this.maxHeight != null) {
            return this.maxHeight.get();
        }
        return Double.MAX_VALUE;
    }

    public void setMaxSize(double maxWidth, double maxHeight) {
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
    }

    public final void setFontSmoothingType(FontSmoothingType value) {
        fontSmoothingTypeProperty().set(value);
    }

    public final FontSmoothingType getFontSmoothingType() {
        return this.fontSmoothingType != null ? this.fontSmoothingType.get() : DEFAULT_FONT_SMOOTHING_TYPE;
    }

    public final ObjectProperty<FontSmoothingType> fontSmoothingTypeProperty() {
        if (this.fontSmoothingType == null) {
            this.fontSmoothingType = new StyleableObjectProperty<FontSmoothingType>(DEFAULT_FONT_SMOOTHING_TYPE) { // from class: javafx.scene.web.WebView.9
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Toolkit.getToolkit().checkFxUserThread();
                    WebView.this.page.setFontSmoothingType(get().ordinal());
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<WebView, FontSmoothingType> getCssMetaData() {
                    return StyleableProperties.FONT_SMOOTHING_TYPE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fontSmoothingType";
                }
            };
        }
        return this.fontSmoothingType;
    }

    public final void setContextMenuEnabled(boolean value) {
        contextMenuEnabledProperty().set(value);
    }

    public final boolean isContextMenuEnabled() {
        if (this.contextMenuEnabled == null) {
            return true;
        }
        return this.contextMenuEnabled.get();
    }

    public final BooleanProperty contextMenuEnabledProperty() {
        if (this.contextMenuEnabled == null) {
            this.contextMenuEnabled = new StyleableBooleanProperty(true) { // from class: javafx.scene.web.WebView.10
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    Toolkit.getToolkit().checkFxUserThread();
                    WebView.this.page.setContextMenuEnabled(get());
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.CONTEXT_MENU_ENABLED;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "contextMenuEnabled";
                }
            };
        }
        return this.contextMenuEnabled;
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebView$StyleableProperties.class */
    private static final class StyleableProperties {
        private static final CssMetaData<WebView, Boolean> CONTEXT_MENU_ENABLED = new CssMetaData<WebView, Boolean>("-fx-context-menu-enabled", BooleanConverter.getInstance(), true) { // from class: javafx.scene.web.WebView.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.contextMenuEnabled == null || !view.contextMenuEnabled.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.contextMenuEnabledProperty();
            }
        };
        private static final CssMetaData<WebView, FontSmoothingType> FONT_SMOOTHING_TYPE = new CssMetaData<WebView, FontSmoothingType>("-fx-font-smoothing-type", new EnumConverter(FontSmoothingType.class), WebView.DEFAULT_FONT_SMOOTHING_TYPE) { // from class: javafx.scene.web.WebView.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.fontSmoothingType == null || !view.fontSmoothingType.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<FontSmoothingType> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.fontSmoothingTypeProperty();
            }
        };
        private static final CssMetaData<WebView, Number> ZOOM = new CssMetaData<WebView, Number>("-fx-zoom", SizeConverter.getInstance(), Double.valueOf(1.0d)) { // from class: javafx.scene.web.WebView.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.zoom == null || !view.zoom.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.zoomProperty();
            }
        };
        private static final CssMetaData<WebView, Number> FONT_SCALE = new CssMetaData<WebView, Number>("-fx-font-scale", SizeConverter.getInstance(), Double.valueOf(1.0d)) { // from class: javafx.scene.web.WebView.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.fontScale == null || !view.fontScale.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.fontScaleProperty();
            }
        };
        private static final CssMetaData<WebView, Number> MIN_WIDTH = new CssMetaData<WebView, Number>("-fx-min-width", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.web.WebView.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.minWidth == null || !view.minWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.minWidthProperty();
            }
        };
        private static final CssMetaData<WebView, Number> MIN_HEIGHT = new CssMetaData<WebView, Number>("-fx-min-height", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.web.WebView.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.minHeight == null || !view.minHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.minHeightProperty();
            }
        };
        private static final CssMetaData<WebView, Number> MAX_WIDTH = new CssMetaData<WebView, Number>("-fx-max-width", SizeConverter.getInstance(), Double.valueOf(Double.MAX_VALUE)) { // from class: javafx.scene.web.WebView.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.maxWidth == null || !view.maxWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.maxWidthProperty();
            }
        };
        private static final CssMetaData<WebView, Number> MAX_HEIGHT = new CssMetaData<WebView, Number>("-fx-max-height", SizeConverter.getInstance(), Double.valueOf(Double.MAX_VALUE)) { // from class: javafx.scene.web.WebView.StyleableProperties.8
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.maxHeight == null || !view.maxHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.maxHeightProperty();
            }
        };
        private static final CssMetaData<WebView, Number> PREF_WIDTH = new CssMetaData<WebView, Number>("-fx-pref-width", SizeConverter.getInstance(), Double.valueOf(WebView.DEFAULT_PREF_WIDTH)) { // from class: javafx.scene.web.WebView.StyleableProperties.9
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.prefWidth == null || !view.prefWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.prefWidthProperty();
            }
        };
        private static final CssMetaData<WebView, Number> PREF_HEIGHT = new CssMetaData<WebView, Number>("-fx-pref-height", SizeConverter.getInstance(), Double.valueOf(WebView.DEFAULT_PREF_HEIGHT)) { // from class: javafx.scene.web.WebView.StyleableProperties.10
            @Override // javafx.css.CssMetaData
            public boolean isSettable(WebView view) {
                return view.prefHeight == null || !view.prefHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(WebView view) {
                return (StyleableProperty) view.prefHeightProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Parent.getClassCssMetaData());
            styleables.add(CONTEXT_MENU_ENABLED);
            styleables.add(FONT_SMOOTHING_TYPE);
            styleables.add(ZOOM);
            styleables.add(FONT_SCALE);
            styleables.add(MIN_WIDTH);
            styleables.add(PREF_WIDTH);
            styleables.add(MAX_WIDTH);
            styleables.add(MIN_HEIGHT);
            styleables.add(PREF_HEIGHT);
            styleables.add(MAX_HEIGHT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    private boolean isTreeReallyVisible() {
        Window window;
        if (getScene() == null || (window = getScene().getWindow()) == null) {
            return false;
        }
        boolean iconified = window instanceof Stage ? ((Stage) window).isIconified() : false;
        return impl_isTreeVisible() && window.isShowing() && window.getWidth() > 0.0d && window.getHeight() > 0.0d && !iconified;
    }

    private void handleStagePulse() {
        if (this.page == null) {
            return;
        }
        boolean reallyVisible = isTreeReallyVisible();
        if (reallyVisible) {
            if (this.page.isDirty()) {
                Scene.impl_setAllowPGAccess(true);
                NGWebView peer = (NGWebView) impl_getPeer();
                peer.update();
                if (this.page.isRepaintPending()) {
                    impl_markDirty(DirtyBits.WEBVIEW_VIEW);
                }
                Scene.impl_setAllowPGAccess(false);
                return;
            }
            return;
        }
        this.page.dropRenderFrames();
    }

    private void processMouseEvent(MouseEvent ev) {
        if (this.page == null) {
            return;
        }
        EventType<? extends MouseEvent> type = ev.getEventType();
        double x2 = ev.getX();
        double y2 = ev.getY();
        double screenX = ev.getScreenX();
        double screenY = ev.getScreenY();
        if (type == MouseEvent.MOUSE_EXITED) {
            type = MouseEvent.MOUSE_MOVED;
            x2 = -32768.0d;
            y2 = -32768.0d;
            Point2D screenPoint = localToScreen(-32768.0d, -32768.0d);
            if (screenPoint == null) {
                return;
            }
            screenX = screenPoint.getX();
            screenY = screenPoint.getY();
        }
        Integer id = idMap.get(type);
        if (id == null) {
            return;
        }
        int buttonMask = (ev.isPrimaryButtonDown() ? 1 : 0) | (ev.isMiddleButtonDown() ? 2 : 0) | (ev.isSecondaryButtonDown() ? 4 : 0);
        WCMouseEvent mouseEvent = new WCMouseEvent(id.intValue(), idMap.get(ev.getButton()).intValue(), ev.getClickCount(), (int) x2, (int) y2, (int) screenX, (int) screenY, System.currentTimeMillis(), ev.isShiftDown(), ev.isControlDown(), ev.isAltDown(), ev.isMetaDown(), ev.isPopupTrigger(), buttonMask);
        this.page.dispatchMouseEvent(mouseEvent);
        ev.consume();
    }

    private void processScrollEvent(ScrollEvent ev) {
        if (this.page == null) {
            return;
        }
        double dx = (-ev.getDeltaX()) * getFontScale() * getScaleX();
        double dy = (-ev.getDeltaY()) * getFontScale() * getScaleY();
        WCMouseWheelEvent wheelEvent = new WCMouseWheelEvent((int) ev.getX(), (int) ev.getY(), (int) ev.getScreenX(), (int) ev.getScreenY(), System.currentTimeMillis(), ev.isShiftDown(), ev.isControlDown(), ev.isAltDown(), ev.isMetaDown(), (float) dx, (float) dy);
        this.page.dispatchMouseWheelEvent(wheelEvent);
        ev.consume();
    }

    private void processKeyEvent(KeyEvent ev) {
        if (this.page == null) {
            return;
        }
        String text = null;
        String keyIdentifier = null;
        int windowsVirtualKeyCode = 0;
        if (ev.getEventType() == KeyEvent.KEY_TYPED) {
            text = ev.getCharacter();
        } else {
            KeyCodeMap.Entry keyCodeEntry = KeyCodeMap.lookup(ev.getCode());
            keyIdentifier = keyCodeEntry.getKeyIdentifier();
            windowsVirtualKeyCode = keyCodeEntry.getWindowsVirtualKeyCode();
        }
        WCKeyEvent keyEvent = new WCKeyEvent(idMap.get(ev.getEventType()).intValue(), text, keyIdentifier, windowsVirtualKeyCode, ev.isShiftDown(), ev.isControlDown(), ev.isAltDown(), ev.isMetaDown(), System.currentTimeMillis());
        if (this.page.dispatchKeyEvent(keyEvent)) {
            ev.consume();
        }
    }

    private InputMethodClientImpl getInputMethodClient() {
        if (this.imClient == null) {
            synchronized (this) {
                if (this.imClient == null) {
                    this.imClient = new InputMethodClientImpl(this, this.page);
                }
            }
        }
        return this.imClient;
    }

    private void processInputMethodEvent(InputMethodEvent ie) {
        if (this.page == null) {
            return;
        }
        if (!getInputMethodClient().getInputMethodState()) {
            ie.consume();
            return;
        }
        WCInputMethodEvent imEvent = InputMethodClientImpl.convertToWCInputMethodEvent(ie);
        if (this.page.dispatchInputMethodEvent(imEvent)) {
            ie.consume();
        }
    }

    private static int getWKDndEventType(EventType et) {
        int commandId = 0;
        if (et == DragEvent.DRAG_ENTERED) {
            commandId = 0;
        } else if (et == DragEvent.DRAG_EXITED) {
            commandId = 3;
        } else if (et == DragEvent.DRAG_OVER) {
            commandId = 1;
        } else if (et == DragEvent.DRAG_DROPPED) {
            commandId = 4;
        }
        return commandId;
    }

    private static int getWKDndAction(TransferMode... tms) {
        int dndActionId = 0;
        for (TransferMode tm : tms) {
            if (tm == TransferMode.COPY) {
                dndActionId |= 1;
            } else if (tm == TransferMode.MOVE) {
                dndActionId |= 2;
            } else if (tm == TransferMode.LINK) {
                dndActionId |= 1073741824;
            }
        }
        return dndActionId;
    }

    private static TransferMode[] getFXDndAction(int wkDndAction) {
        LinkedList<TransferMode> tms = new LinkedList<>();
        if ((wkDndAction & 1) != 0) {
            tms.add(TransferMode.COPY);
        }
        if ((wkDndAction & 2) != 0) {
            tms.add(TransferMode.MOVE);
        }
        if ((wkDndAction & 1073741824) != 0) {
            tms.add(TransferMode.LINK);
        }
        return (TransferMode[]) tms.toArray(new TransferMode[0]);
    }

    private void registerEventHandlers() {
        addEventHandler(KeyEvent.ANY, event -> {
            processKeyEvent(event);
        });
        addEventHandler(MouseEvent.ANY, event2 -> {
            processMouseEvent(event2);
            if (event2.isDragDetect() && !this.page.isDragConfirmed()) {
                event2.setDragDetect(false);
            }
        });
        addEventHandler(ScrollEvent.SCROLL, event3 -> {
            processScrollEvent(event3);
        });
        setOnInputMethodTextChanged(event4 -> {
            processInputMethodEvent(event4);
        });
        EventHandler<DragEvent> destHandler = event5 -> {
            try {
                Dragboard db = event5.getDragboard();
                LinkedList<String> mimes = new LinkedList<>();
                LinkedList<String> values = new LinkedList<>();
                for (DataFormat df : db.getContentTypes()) {
                    Object content = db.getContent(df);
                    if (content != null) {
                        for (String mime : df.getIdentifiers()) {
                            mimes.add(mime);
                            values.add(content.toString());
                        }
                    }
                }
                if (!mimes.isEmpty()) {
                    int wkDndEventType = getWKDndEventType(event5.getEventType());
                    int wkDndAction = this.page.dispatchDragOperation(wkDndEventType, (String[]) mimes.toArray(new String[0]), (String[]) values.toArray(new String[0]), (int) event5.getX(), (int) event5.getY(), (int) event5.getScreenX(), (int) event5.getScreenY(), getWKDndAction((TransferMode[]) db.getTransferModes().toArray(new TransferMode[0])));
                    if (wkDndEventType != 4 || wkDndAction != 0) {
                        event5.acceptTransferModes(getFXDndAction(wkDndAction));
                    }
                    event5.consume();
                }
            } catch (SecurityException e2) {
            }
        };
        setOnDragEntered(destHandler);
        setOnDragExited(destHandler);
        setOnDragOver(destHandler);
        setOnDragDropped(destHandler);
        setOnDragDetected(event6 -> {
            if (this.page.isDragConfirmed()) {
                this.page.confirmStartDrag();
                event6.consume();
            }
        });
        setOnDragDone(event7 -> {
            this.page.dispatchDragOperation(104, null, null, (int) event7.getX(), (int) event7.getY(), (int) event7.getScreenX(), (int) event7.getScreenY(), getWKDndAction(event7.getAcceptedTransferMode()));
            event7.consume();
        });
        setInputMethodRequests(getInputMethodClient());
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    protected void impl_pickNodeLocal(PickRay pickRay, PickResultChooser result) {
        impl_intersects(pickRay, result);
    }

    @Override // javafx.scene.Parent
    protected ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGWebView();
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        bounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, (float) getWidth(), (float) getHeight(), 0.0f);
        tx.transform(bounds, bounds);
        return bounds;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        return true;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        NGWebView peer = (NGWebView) impl_getPeer();
        if (impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            peer.setPage(this.page);
        }
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            peer.resize((float) getWidth(), (float) getHeight());
        }
        if (impl_isDirty(DirtyBits.WEBVIEW_VIEW)) {
            peer.requestRender();
        }
    }
}
