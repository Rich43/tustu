package javafx.scene.control;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.NodeHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.icepdf.core.util.PdfOps;

@DefaultProperty("text")
/* loaded from: jfxrt.jar:javafx/scene/control/Labeled.class */
public abstract class Labeled extends Control {
    private static final String DEFAULT_ELLIPSIS_STRING = "...";
    private StringProperty text;
    private ObjectProperty<Pos> alignment;
    private ObjectProperty<TextAlignment> textAlignment;
    private ObjectProperty<OverrunStyle> textOverrun;
    private StringProperty ellipsisString;
    private BooleanProperty wrapText;
    private ObjectProperty<Font> font;
    private ObjectProperty<Node> graphic;
    private StyleableStringProperty imageUrl = null;
    private BooleanProperty underline;
    private DoubleProperty lineSpacing;
    private ObjectProperty<ContentDisplay> contentDisplay;
    private ObjectProperty<Insets> labelPadding;
    private DoubleProperty graphicTextGap;
    private ObjectProperty<Paint> textFill;
    private BooleanProperty mnemonicParsing;

    public Labeled() {
    }

    public Labeled(String text) {
        setText(text);
    }

    public Labeled(String text, Node graphic) {
        setText(text);
        ((StyleableProperty) graphicProperty()).applyStyle(null, graphic);
    }

    public final StringProperty textProperty() {
        if (this.text == null) {
            this.text = new SimpleStringProperty(this, "text", "");
        }
        return this.text;
    }

    public final void setText(String value) {
        textProperty().setValue(value);
    }

    public final String getText() {
        return this.text == null ? "" : this.text.getValue2();
    }

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.CENTER_LEFT) { // from class: javafx.scene.control.Labeled.1
                @Override // javafx.css.StyleableProperty
                public CssMetaData<Labeled, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "alignment";
                }
            };
        }
        return this.alignment;
    }

    public final void setAlignment(Pos value) {
        alignmentProperty().set(value);
    }

    public final Pos getAlignment() {
        return this.alignment == null ? Pos.CENTER_LEFT : this.alignment.get();
    }

    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
        if (this.textAlignment == null) {
            this.textAlignment = new StyleableObjectProperty<TextAlignment>(TextAlignment.LEFT) { // from class: javafx.scene.control.Labeled.2
                @Override // javafx.css.StyleableProperty
                public CssMetaData<Labeled, TextAlignment> getCssMetaData() {
                    return StyleableProperties.TEXT_ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "textAlignment";
                }
            };
        }
        return this.textAlignment;
    }

    public final void setTextAlignment(TextAlignment value) {
        textAlignmentProperty().setValue(value);
    }

    public final TextAlignment getTextAlignment() {
        return this.textAlignment == null ? TextAlignment.LEFT : this.textAlignment.getValue2();
    }

    public final ObjectProperty<OverrunStyle> textOverrunProperty() {
        if (this.textOverrun == null) {
            this.textOverrun = new StyleableObjectProperty<OverrunStyle>(OverrunStyle.ELLIPSIS) { // from class: javafx.scene.control.Labeled.3
                @Override // javafx.css.StyleableProperty
                public CssMetaData<Labeled, OverrunStyle> getCssMetaData() {
                    return StyleableProperties.TEXT_OVERRUN;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "textOverrun";
                }
            };
        }
        return this.textOverrun;
    }

    public final void setTextOverrun(OverrunStyle value) {
        textOverrunProperty().setValue(value);
    }

    public final OverrunStyle getTextOverrun() {
        return this.textOverrun == null ? OverrunStyle.ELLIPSIS : this.textOverrun.getValue2();
    }

    public final StringProperty ellipsisStringProperty() {
        if (this.ellipsisString == null) {
            this.ellipsisString = new StyleableStringProperty(DEFAULT_ELLIPSIS_STRING) { // from class: javafx.scene.control.Labeled.4
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "ellipsisString";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, String> getCssMetaData() {
                    return StyleableProperties.ELLIPSIS_STRING;
                }
            };
        }
        return this.ellipsisString;
    }

    public final void setEllipsisString(String value) {
        ellipsisStringProperty().set(value == null ? "" : value);
    }

    public final String getEllipsisString() {
        return this.ellipsisString == null ? DEFAULT_ELLIPSIS_STRING : this.ellipsisString.get();
    }

    public final BooleanProperty wrapTextProperty() {
        if (this.wrapText == null) {
            this.wrapText = new StyleableBooleanProperty() { // from class: javafx.scene.control.Labeled.5
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.WRAP_TEXT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "wrapText";
                }
            };
        }
        return this.wrapText;
    }

    public final void setWrapText(boolean value) {
        wrapTextProperty().setValue(Boolean.valueOf(value));
    }

    public final boolean isWrapText() {
        if (this.wrapText == null) {
            return false;
        }
        return this.wrapText.getValue2().booleanValue();
    }

    @Override // javafx.scene.Node
    public Orientation getContentBias() {
        if (isWrapText()) {
            return Orientation.HORIZONTAL;
        }
        return null;
    }

    public final ObjectProperty<Font> fontProperty() {
        if (this.font == null) {
            this.font = new StyleableObjectProperty<Font>(Font.getDefault()) { // from class: javafx.scene.control.Labeled.6
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
                    if (value != null) {
                        if (value.equals(oldValue)) {
                            return;
                        }
                    } else if (oldValue == null) {
                        return;
                    }
                    super.set((AnonymousClass6) value);
                    NodeHelper.recalculateRelativeSizeProperties(Labeled.this, value);
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (!this.fontSetByCss) {
                        Labeled.this.impl_reapplyCSS();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Labeled, Font> getCssMetaData() {
                    return StyleableProperties.FONT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "font";
                }
            };
        }
        return this.font;
    }

    public final void setFont(Font value) {
        fontProperty().setValue(value);
    }

    public final Font getFont() {
        return this.font == null ? Font.getDefault() : this.font.getValue2();
    }

    public final ObjectProperty<Node> graphicProperty() {
        if (this.graphic == null) {
            this.graphic = new StyleableObjectProperty<Node>() { // from class: javafx.scene.control.Labeled.7
                @Override // javafx.css.StyleableProperty
                public CssMetaData getCssMetaData() {
                    return StyleableProperties.GRAPHIC;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "graphic";
                }
            };
        }
        return this.graphic;
    }

    public final void setGraphic(Node value) {
        graphicProperty().setValue(value);
    }

    public final Node getGraphic() {
        if (this.graphic == null) {
            return null;
        }
        return this.graphic.getValue2();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleableStringProperty imageUrlProperty() {
        if (this.imageUrl == null) {
            this.imageUrl = new StyleableStringProperty() { // from class: javafx.scene.control.Labeled.8
                StyleOrigin origin = StyleOrigin.USER;

                @Override // javafx.css.StyleableStringProperty, javafx.css.StyleableProperty
                public void applyStyle(StyleOrigin origin, String v2) {
                    this.origin = origin;
                    if (Labeled.this.graphic == null || !Labeled.this.graphic.isBound()) {
                        super.applyStyle(origin, v2);
                    }
                    this.origin = StyleOrigin.USER;
                }

                @Override // javafx.beans.property.StringPropertyBase
                protected void invalidated() {
                    String url = super.get();
                    if (url == null) {
                        ((StyleableProperty) Labeled.this.graphicProperty()).applyStyle(this.origin, null);
                        return;
                    }
                    Node graphicNode = Labeled.this.getGraphic();
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
                        ((StyleableProperty) Labeled.this.graphicProperty()).applyStyle(this.origin, new ImageView(img));
                    }
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // javafx.beans.property.StringPropertyBase, javafx.beans.value.ObservableObjectValue
                public String get() {
                    Image image;
                    Node graphic = Labeled.this.getGraphic();
                    if ((graphic instanceof ImageView) && (image = ((ImageView) graphic).getImage()) != null) {
                        return image.impl_getUrl();
                    }
                    return null;
                }

                @Override // javafx.css.StyleableStringProperty, javafx.css.StyleableProperty
                public StyleOrigin getStyleOrigin() {
                    if (Labeled.this.graphic != null) {
                        return ((StyleableProperty) Labeled.this.graphic).getStyleOrigin();
                    }
                    return null;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "imageUrl";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, String> getCssMetaData() {
                    return StyleableProperties.GRAPHIC;
                }
            };
        }
        return this.imageUrl;
    }

    public final BooleanProperty underlineProperty() {
        if (this.underline == null) {
            this.underline = new StyleableBooleanProperty(false) { // from class: javafx.scene.control.Labeled.9
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.UNDERLINE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "underline";
                }
            };
        }
        return this.underline;
    }

    public final void setUnderline(boolean value) {
        underlineProperty().setValue(Boolean.valueOf(value));
    }

    public final boolean isUnderline() {
        if (this.underline == null) {
            return false;
        }
        return this.underline.getValue2().booleanValue();
    }

    public final DoubleProperty lineSpacingProperty() {
        if (this.lineSpacing == null) {
            this.lineSpacing = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.control.Labeled.10
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.LINE_SPACING;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "lineSpacing";
                }
            };
        }
        return this.lineSpacing;
    }

    public final void setLineSpacing(double value) {
        lineSpacingProperty().setValue((Number) Double.valueOf(value));
    }

    public final double getLineSpacing() {
        if (this.lineSpacing == null) {
            return 0.0d;
        }
        return this.lineSpacing.getValue2().doubleValue();
    }

    public final ObjectProperty<ContentDisplay> contentDisplayProperty() {
        if (this.contentDisplay == null) {
            this.contentDisplay = new StyleableObjectProperty<ContentDisplay>(ContentDisplay.LEFT) { // from class: javafx.scene.control.Labeled.11
                @Override // javafx.css.StyleableProperty
                public CssMetaData<Labeled, ContentDisplay> getCssMetaData() {
                    return StyleableProperties.CONTENT_DISPLAY;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "contentDisplay";
                }
            };
        }
        return this.contentDisplay;
    }

    public final void setContentDisplay(ContentDisplay value) {
        contentDisplayProperty().setValue(value);
    }

    public final ContentDisplay getContentDisplay() {
        return this.contentDisplay == null ? ContentDisplay.LEFT : this.contentDisplay.getValue2();
    }

    public final ReadOnlyObjectProperty<Insets> labelPaddingProperty() {
        return labelPaddingPropertyImpl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ObjectProperty<Insets> labelPaddingPropertyImpl() {
        if (this.labelPadding == null) {
            this.labelPadding = new StyleableObjectProperty<Insets>(Insets.EMPTY) { // from class: javafx.scene.control.Labeled.12
                private Insets lastValidValue = Insets.EMPTY;

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Insets newValue = get();
                    if (newValue == null) {
                        set(this.lastValidValue);
                        throw new NullPointerException("cannot set labelPadding to null");
                    }
                    this.lastValidValue = newValue;
                    Labeled.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Labeled, Insets> getCssMetaData() {
                    return StyleableProperties.LABEL_PADDING;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "labelPadding";
                }
            };
        }
        return this.labelPadding;
    }

    private void setLabelPadding(Insets value) {
        labelPaddingPropertyImpl().set(value);
    }

    public final Insets getLabelPadding() {
        return this.labelPadding == null ? Insets.EMPTY : this.labelPadding.get();
    }

    public final DoubleProperty graphicTextGapProperty() {
        if (this.graphicTextGap == null) {
            this.graphicTextGap = new StyleableDoubleProperty(4.0d) { // from class: javafx.scene.control.Labeled.13
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.GRAPHIC_TEXT_GAP;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "graphicTextGap";
                }
            };
        }
        return this.graphicTextGap;
    }

    public final void setGraphicTextGap(double value) {
        graphicTextGapProperty().setValue((Number) Double.valueOf(value));
    }

    public final double getGraphicTextGap() {
        if (this.graphicTextGap == null) {
            return 4.0d;
        }
        return this.graphicTextGap.getValue2().doubleValue();
    }

    public final void setTextFill(Paint value) {
        textFillProperty().set(value);
    }

    public final Paint getTextFill() {
        return this.textFill == null ? Color.BLACK : this.textFill.get();
    }

    public final ObjectProperty<Paint> textFillProperty() {
        if (this.textFill == null) {
            this.textFill = new StyleableObjectProperty<Paint>(Color.BLACK) { // from class: javafx.scene.control.Labeled.14
                @Override // javafx.css.StyleableProperty
                public CssMetaData<Labeled, Paint> getCssMetaData() {
                    return StyleableProperties.TEXT_FILL;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Labeled.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "textFill";
                }
            };
        }
        return this.textFill;
    }

    public final void setMnemonicParsing(boolean value) {
        mnemonicParsingProperty().set(value);
    }

    public final boolean isMnemonicParsing() {
        if (this.mnemonicParsing == null) {
            return false;
        }
        return this.mnemonicParsing.get();
    }

    public final BooleanProperty mnemonicParsingProperty() {
        if (this.mnemonicParsing == null) {
            this.mnemonicParsing = new SimpleBooleanProperty(this, "mnemonicParsing");
        }
        return this.mnemonicParsing;
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString()).append(PdfOps.SINGLE_QUOTE_TOKEN).append(getText()).append(PdfOps.SINGLE_QUOTE_TOKEN);
        return builder.toString();
    }

    @Deprecated
    protected Pos impl_cssGetAlignmentInitialValue() {
        return Pos.CENTER_LEFT;
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/Labeled$StyleableProperties.class */
    private static class StyleableProperties {
        private static final FontCssMetaData<Labeled> FONT = new FontCssMetaData<Labeled>("-fx-font", Font.getDefault()) { // from class: javafx.scene.control.Labeled.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.font == null || !n2.font.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Font> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.fontProperty();
            }
        };
        private static final CssMetaData<Labeled, Pos> ALIGNMENT = new CssMetaData<Labeled, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.CENTER_LEFT) { // from class: javafx.scene.control.Labeled.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.alignment == null || !n2.alignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.alignmentProperty();
            }

            @Override // javafx.css.CssMetaData
            public Pos getInitialValue(Labeled n2) {
                return n2.impl_cssGetAlignmentInitialValue();
            }
        };
        private static final CssMetaData<Labeled, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<Labeled, TextAlignment>("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) { // from class: javafx.scene.control.Labeled.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.textAlignment == null || !n2.textAlignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<TextAlignment> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.textAlignmentProperty();
            }
        };
        private static final CssMetaData<Labeled, Paint> TEXT_FILL = new CssMetaData<Labeled, Paint>("-fx-text-fill", PaintConverter.getInstance(), Color.BLACK) { // from class: javafx.scene.control.Labeled.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.textFill == null || !n2.textFill.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Paint> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.textFillProperty();
            }
        };
        private static final CssMetaData<Labeled, OverrunStyle> TEXT_OVERRUN = new CssMetaData<Labeled, OverrunStyle>("-fx-text-overrun", new EnumConverter(OverrunStyle.class), OverrunStyle.ELLIPSIS) { // from class: javafx.scene.control.Labeled.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.textOverrun == null || !n2.textOverrun.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<OverrunStyle> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.textOverrunProperty();
            }
        };
        private static final CssMetaData<Labeled, String> ELLIPSIS_STRING = new CssMetaData<Labeled, String>("-fx-ellipsis-string", StringConverter.getInstance(), Labeled.DEFAULT_ELLIPSIS_STRING) { // from class: javafx.scene.control.Labeled.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.ellipsisString == null || !n2.ellipsisString.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<String> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.ellipsisStringProperty();
            }
        };
        private static final CssMetaData<Labeled, Boolean> WRAP_TEXT = new CssMetaData<Labeled, Boolean>("-fx-wrap-text", BooleanConverter.getInstance(), false) { // from class: javafx.scene.control.Labeled.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.wrapText == null || !n2.wrapText.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.wrapTextProperty();
            }
        };
        private static final CssMetaData<Labeled, String> GRAPHIC = new CssMetaData<Labeled, String>("-fx-graphic", StringConverter.getInstance()) { // from class: javafx.scene.control.Labeled.StyleableProperties.8
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.graphic == null || !n2.graphic.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<String> getStyleableProperty(Labeled n2) {
                return n2.imageUrlProperty();
            }
        };
        private static final CssMetaData<Labeled, Boolean> UNDERLINE = new CssMetaData<Labeled, Boolean>("-fx-underline", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.control.Labeled.StyleableProperties.9
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.underline == null || !n2.underline.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.underlineProperty();
            }
        };
        private static final CssMetaData<Labeled, Number> LINE_SPACING = new CssMetaData<Labeled, Number>("-fx-line-spacing", SizeConverter.getInstance(), 0) { // from class: javafx.scene.control.Labeled.StyleableProperties.10
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.lineSpacing == null || !n2.lineSpacing.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.lineSpacingProperty();
            }
        };
        private static final CssMetaData<Labeled, ContentDisplay> CONTENT_DISPLAY = new CssMetaData<Labeled, ContentDisplay>("-fx-content-display", new EnumConverter(ContentDisplay.class), ContentDisplay.LEFT) { // from class: javafx.scene.control.Labeled.StyleableProperties.11
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.contentDisplay == null || !n2.contentDisplay.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<ContentDisplay> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.contentDisplayProperty();
            }
        };
        private static final CssMetaData<Labeled, Insets> LABEL_PADDING = new CssMetaData<Labeled, Insets>("-fx-label-padding", InsetsConverter.getInstance(), Insets.EMPTY) { // from class: javafx.scene.control.Labeled.StyleableProperties.12
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.labelPadding == null || !n2.labelPadding.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Insets> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.labelPaddingPropertyImpl();
            }
        };
        private static final CssMetaData<Labeled, Number> GRAPHIC_TEXT_GAP = new CssMetaData<Labeled, Number>("-fx-graphic-text-gap", SizeConverter.getInstance(), Double.valueOf(4.0d)) { // from class: javafx.scene.control.Labeled.StyleableProperties.13
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Labeled n2) {
                return n2.graphicTextGap == null || !n2.graphicTextGap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Labeled n2) {
                return (StyleableProperty) n2.graphicTextGapProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, FONT, ALIGNMENT, TEXT_ALIGNMENT, TEXT_FILL, TEXT_OVERRUN, ELLIPSIS_STRING, WRAP_TEXT, GRAPHIC, UNDERLINE, LINE_SPACING, CONTENT_DISPLAY, LABEL_PADDING, GRAPHIC_TEXT_GAP);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
