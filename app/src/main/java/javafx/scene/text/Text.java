package javafx.scene.text;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.TransformedShape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.scene.text.TextLine;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NGText;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

@DefaultProperty("text")
/* loaded from: jfxrt.jar:javafx/scene/text/Text.class */
public class Text extends Shape {
    private TextLayout layout;
    private boolean isSpan;
    private TextSpan textSpan;
    private GlyphList[] textRuns;
    private BaseBounds spanBounds;
    private boolean spanBoundsInvalid;
    private StringProperty text;

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12743x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12744y;
    private ObjectProperty<Font> font;
    private ObjectProperty<TextBoundsType> boundsType;
    private DoubleProperty wrappingWidth;
    private ObjectProperty<FontSmoothingType> fontSmoothingType;
    private TextAttribute attributes;
    private static final boolean DEFAULT_UNDERLINE = false;
    private static final boolean DEFAULT_STRIKETHROUGH = false;
    private static final double DEFAULT_LINE_SPACING = 0.0d;
    private static final int DEFAULT_CARET_POSITION = -1;
    private static final int DEFAULT_SELECTION_START = -1;
    private static final int DEFAULT_SELECTION_END = -1;
    private static final boolean DEFAULT_CARET_BIAS = true;
    private static final PathElement[] EMPTY_PATH_ELEMENT_ARRAY = new PathElement[0];
    private static final VPos DEFAULT_TEXT_ORIGIN = VPos.BASELINE;
    private static final TextBoundsType DEFAULT_BOUNDS_TYPE = TextBoundsType.LOGICAL;
    private static final TextAlignment DEFAULT_TEXT_ALIGNMENT = TextAlignment.LEFT;
    private static final Color DEFAULT_SELECTION_FILL = Color.WHITE;

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected final NGNode impl_createPeer() {
        return new NGText();
    }

    public Text() {
        this.textRuns = null;
        this.spanBounds = new RectBounds();
        this.spanBoundsInvalid = true;
        setAccessibleRole(AccessibleRole.TEXT);
        InvalidationListener listener = observable -> {
            checkSpan();
        };
        parentProperty().addListener(listener);
        managedProperty().addListener(listener);
        effectiveNodeOrientationProperty().addListener(observable2 -> {
            checkOrientation();
        });
        setPickOnBounds(true);
    }

    public Text(String text) {
        this();
        setText(text);
    }

    public Text(double x2, double y2, String text) {
        this(text);
        setX(x2);
        setY(y2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSpan() {
        return this.isSpan;
    }

    private void checkSpan() {
        this.isSpan = isManaged() && (getParent() instanceof TextFlow);
        if (isSpan() && !pickOnBoundsProperty().isBound()) {
            setPickOnBounds(false);
        }
    }

    private void checkOrientation() {
        if (!isSpan()) {
            NodeOrientation orientation = getEffectiveNodeOrientation();
            boolean rtl = orientation == NodeOrientation.RIGHT_TO_LEFT;
            int dir = rtl ? 2048 : 1024;
            TextLayout layout = getTextLayout();
            if (layout.setDirection(dir)) {
                needsTextLayout();
            }
        }
    }

    @Override // javafx.scene.Node
    public boolean usesMirroring() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needsFullTextLayout() {
        if (isSpan()) {
            this.textSpan = null;
        } else {
            TextLayout layout = getTextLayout();
            String string = getTextInternal();
            Object font = getFontInternal();
            layout.setContent(string, font);
        }
        needsTextLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needsTextLayout() {
        this.textRuns = null;
        impl_geomChanged();
        impl_markDirty(DirtyBits.NODE_CONTENTS);
    }

    TextSpan getTextSpan() {
        if (this.textSpan == null) {
            this.textSpan = new TextSpan() { // from class: javafx.scene.text.Text.1
                @Override // com.sun.javafx.scene.text.TextSpan
                public String getText() {
                    return Text.this.getTextInternal();
                }

                @Override // com.sun.javafx.scene.text.TextSpan
                public Object getFont() {
                    return Text.this.getFontInternal();
                }

                @Override // com.sun.javafx.scene.text.TextSpan
                public RectBounds getBounds() {
                    return null;
                }
            };
        }
        return this.textSpan;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TextLayout getTextLayout() {
        if (isSpan()) {
            this.layout = null;
            TextFlow parent = (TextFlow) getParent();
            return parent.getTextLayout();
        }
        if (this.layout == null) {
            TextLayoutFactory factory = Toolkit.getToolkit().getTextLayoutFactory();
            this.layout = factory.createLayout();
            String string = getTextInternal();
            Object font = getFontInternal();
            TextAlignment alignment = getTextAlignment();
            if (alignment == null) {
                alignment = DEFAULT_TEXT_ALIGNMENT;
            }
            this.layout.setContent(string, font);
            this.layout.setAlignment(alignment.ordinal());
            this.layout.setLineSpacing((float) getLineSpacing());
            this.layout.setWrapWidth((float) getWrappingWidth());
            if (getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                this.layout.setDirection(2048);
            } else {
                this.layout.setDirection(1024);
            }
        }
        return this.layout;
    }

    void layoutSpan(GlyphList[] runs) {
        TextSpan span = getTextSpan();
        int count = 0;
        for (GlyphList glyphList : runs) {
            if (glyphList.getTextSpan() == span) {
                count++;
            }
        }
        this.textRuns = new GlyphList[count];
        int count2 = 0;
        for (GlyphList run : runs) {
            if (run.getTextSpan() == span) {
                int i2 = count2;
                count2++;
                this.textRuns[i2] = run;
            }
        }
        this.spanBoundsInvalid = true;
        impl_geomChanged();
        impl_markDirty(DirtyBits.NODE_CONTENTS);
    }

    BaseBounds getSpanBounds() {
        if (this.spanBoundsInvalid) {
            GlyphList[] runs = getRuns();
            if (runs.length != 0) {
                float left = Float.POSITIVE_INFINITY;
                float top = Float.POSITIVE_INFINITY;
                float right = 0.0f;
                float bottom = 0.0f;
                for (GlyphList run : runs) {
                    Point2D location = run.getLocation();
                    float width = run.getWidth();
                    float height = run.getLineBounds().getHeight();
                    left = Math.min(location.f11907x, left);
                    top = Math.min(location.f11908y, top);
                    right = Math.max(location.f11907x + width, right);
                    bottom = Math.max(location.f11908y + height, bottom);
                }
                this.spanBounds = this.spanBounds.deriveWithNewBounds(left, top, 0.0f, right, bottom, 0.0f);
            } else {
                this.spanBounds = this.spanBounds.makeEmpty();
            }
            this.spanBoundsInvalid = false;
        }
        return this.spanBounds;
    }

    private GlyphList[] getRuns() {
        if (this.textRuns != null) {
            return this.textRuns;
        }
        if (isSpan()) {
            getParent().layout();
        } else {
            TextLayout layout = getTextLayout();
            this.textRuns = layout.getRuns();
        }
        return this.textRuns;
    }

    private com.sun.javafx.geom.Shape getShape() {
        int type;
        TextLayout layout = getTextLayout();
        int type2 = 1;
        if (isStrikethrough()) {
            type2 = 1 | 4;
        }
        if (isUnderline()) {
            type2 |= 2;
        }
        TextSpan filter = null;
        if (isSpan()) {
            type = type2 | 16;
            filter = getTextSpan();
        } else {
            type = type2 | 8;
        }
        return layout.getShape(type, filter);
    }

    private BaseBounds getVisualBounds() {
        if (this.impl_mode == NGShape.Mode.FILL || getStrokeType() == StrokeType.INSIDE) {
            int type = 1;
            if (isStrikethrough()) {
                type = 1 | 4;
            }
            if (isUnderline()) {
                type |= 2;
            }
            return getTextLayout().getVisualBounds(type);
        }
        return getShape().getBounds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BaseBounds getLogicalBounds() {
        TextLayout layout = getTextLayout();
        return layout.getBounds();
    }

    public final void setText(String value) {
        if (value == null) {
            value = "";
        }
        textProperty().set(value);
    }

    public final String getText() {
        return this.text == null ? "" : this.text.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getTextInternal() {
        String localText = getText();
        return localText == null ? "" : localText;
    }

    public final StringProperty textProperty() {
        if (this.text == null) {
            this.text = new StringPropertyBase("") { // from class: javafx.scene.text.Text.2
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Text.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "text";
                }

                @Override // javafx.beans.property.StringPropertyBase
                public void invalidated() {
                    Text.this.needsFullTextLayout();
                    Text.this.setImpl_selectionStart(-1);
                    Text.this.setImpl_selectionEnd(-1);
                    Text.this.setImpl_caretPosition(-1);
                    Text.this.setImpl_caretBias(true);
                    String value = get();
                    if (value == null && !isBound()) {
                        set("");
                    }
                    Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
                }
            };
        }
        return this.text;
    }

    public final void setX(double value) {
        xProperty().set(value);
    }

    public final double getX() {
        if (this.f12743x == null) {
            return 0.0d;
        }
        return this.f12743x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12743x == null) {
            this.f12743x = new DoublePropertyBase() { // from class: javafx.scene.text.Text.3
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Text.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }

                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Text.this.impl_geomChanged();
                }
            };
        }
        return this.f12743x;
    }

    public final void setY(double value) {
        yProperty().set(value);
    }

    public final double getY() {
        if (this.f12744y == null) {
            return 0.0d;
        }
        return this.f12744y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12744y == null) {
            this.f12744y = new DoublePropertyBase() { // from class: javafx.scene.text.Text.4
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Text.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }

                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Text.this.impl_geomChanged();
                }
            };
        }
        return this.f12744y;
    }

    public final void setFont(Font value) {
        fontProperty().set(value);
    }

    public final Font getFont() {
        return this.font == null ? Font.getDefault() : this.font.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object getFontInternal() {
        Font font = getFont();
        if (font == null) {
            font = Font.getDefault();
        }
        return font.impl_getNativeFont();
    }

    public final ObjectProperty<Font> fontProperty() {
        if (this.font == null) {
            this.font = new StyleableObjectProperty<Font>(Font.getDefault()) { // from class: javafx.scene.text.Text.5
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Text.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "font";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Text, Font> getCssMetaData() {
                    return StyleableProperties.FONT;
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Text.this.needsFullTextLayout();
                    Text.this.impl_markDirty(DirtyBits.TEXT_FONT);
                }
            };
        }
        return this.font;
    }

    public final void setTextOrigin(VPos value) {
        textOriginProperty().set(value);
    }

    public final VPos getTextOrigin() {
        if (this.attributes == null || this.attributes.textOrigin == null) {
            return DEFAULT_TEXT_ORIGIN;
        }
        return this.attributes.getTextOrigin();
    }

    public final ObjectProperty<VPos> textOriginProperty() {
        return getTextAttribute().textOriginProperty();
    }

    public final void setBoundsType(TextBoundsType value) {
        boundsTypeProperty().set(value);
    }

    public final TextBoundsType getBoundsType() {
        return this.boundsType == null ? DEFAULT_BOUNDS_TYPE : boundsTypeProperty().get();
    }

    public final ObjectProperty<TextBoundsType> boundsTypeProperty() {
        if (this.boundsType == null) {
            this.boundsType = new StyleableObjectProperty<TextBoundsType>(DEFAULT_BOUNDS_TYPE) { // from class: javafx.scene.text.Text.6
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Text.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "boundsType";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Text, TextBoundsType> getCssMetaData() {
                    return StyleableProperties.BOUNDS_TYPE;
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    TextLayout layout = Text.this.getTextLayout();
                    int type = 0;
                    if (Text.this.boundsType.get() == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
                        type = 0 | 16384;
                    }
                    if (layout.setBoundsType(type)) {
                        Text.this.needsTextLayout();
                    } else {
                        Text.this.impl_geomChanged();
                    }
                }
            };
        }
        return this.boundsType;
    }

    public final void setWrappingWidth(double value) {
        wrappingWidthProperty().set(value);
    }

    public final double getWrappingWidth() {
        if (this.wrappingWidth == null) {
            return 0.0d;
        }
        return this.wrappingWidth.get();
    }

    public final DoubleProperty wrappingWidthProperty() {
        if (this.wrappingWidth == null) {
            this.wrappingWidth = new DoublePropertyBase() { // from class: javafx.scene.text.Text.7
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Text.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "wrappingWidth";
                }

                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    if (!Text.this.isSpan()) {
                        TextLayout layout = Text.this.getTextLayout();
                        if (layout.setWrapWidth((float) get())) {
                            Text.this.needsTextLayout();
                        } else {
                            Text.this.impl_geomChanged();
                        }
                    }
                }
            };
        }
        return this.wrappingWidth;
    }

    public final void setUnderline(boolean value) {
        underlineProperty().set(value);
    }

    public final boolean isUnderline() {
        if (this.attributes == null || this.attributes.underline == null) {
            return false;
        }
        return this.attributes.isUnderline();
    }

    public final BooleanProperty underlineProperty() {
        return getTextAttribute().underlineProperty();
    }

    public final void setStrikethrough(boolean value) {
        strikethroughProperty().set(value);
    }

    public final boolean isStrikethrough() {
        if (this.attributes == null || this.attributes.strikethrough == null) {
            return false;
        }
        return this.attributes.isStrikethrough();
    }

    public final BooleanProperty strikethroughProperty() {
        return getTextAttribute().strikethroughProperty();
    }

    public final void setTextAlignment(TextAlignment value) {
        textAlignmentProperty().set(value);
    }

    public final TextAlignment getTextAlignment() {
        if (this.attributes == null || this.attributes.textAlignment == null) {
            return DEFAULT_TEXT_ALIGNMENT;
        }
        return this.attributes.getTextAlignment();
    }

    public final ObjectProperty<TextAlignment> textAlignmentProperty() {
        return getTextAttribute().textAlignmentProperty();
    }

    public final void setLineSpacing(double spacing) {
        lineSpacingProperty().set(spacing);
    }

    public final double getLineSpacing() {
        if (this.attributes == null || this.attributes.lineSpacing == null) {
            return 0.0d;
        }
        return this.attributes.getLineSpacing();
    }

    public final DoubleProperty lineSpacingProperty() {
        return getTextAttribute().lineSpacingProperty();
    }

    @Override // javafx.scene.Node
    public final double getBaselineOffset() {
        return baselineOffsetProperty().get();
    }

    public final ReadOnlyDoubleProperty baselineOffsetProperty() {
        return getTextAttribute().baselineOffsetProperty();
    }

    public final void setFontSmoothingType(FontSmoothingType value) {
        fontSmoothingTypeProperty().set(value);
    }

    public final FontSmoothingType getFontSmoothingType() {
        return this.fontSmoothingType == null ? FontSmoothingType.GRAY : this.fontSmoothingType.get();
    }

    public final ObjectProperty<FontSmoothingType> fontSmoothingTypeProperty() {
        if (this.fontSmoothingType == null) {
            this.fontSmoothingType = new StyleableObjectProperty<FontSmoothingType>(FontSmoothingType.GRAY) { // from class: javafx.scene.text.Text.8
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Text.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fontSmoothingType";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Text, FontSmoothingType> getCssMetaData() {
                    return StyleableProperties.FONT_SMOOTHING_TYPE;
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
                    Text.this.impl_geomChanged();
                }
            };
        }
        return this.fontSmoothingType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javafx.scene.Node
    @Deprecated
    public final void impl_geomChanged() {
        super.impl_geomChanged();
        if (this.attributes != null) {
            if (this.attributes.impl_caretBinding != null) {
                this.attributes.impl_caretBinding.invalidate();
            }
            if (this.attributes.impl_selectionBinding != null) {
                this.attributes.impl_selectionBinding.invalidate();
            }
        }
        impl_markDirty(DirtyBits.NODE_GEOMETRY);
    }

    @Deprecated
    public final PathElement[] getImpl_selectionShape() {
        return impl_selectionShapeProperty().get();
    }

    @Deprecated
    public final ReadOnlyObjectProperty<PathElement[]> impl_selectionShapeProperty() {
        return getTextAttribute().impl_selectionShapeProperty();
    }

    @Deprecated
    public final void setImpl_selectionStart(int value) {
        if (value == -1 && (this.attributes == null || this.attributes.impl_selectionStart == null)) {
            return;
        }
        impl_selectionStartProperty().set(value);
    }

    @Deprecated
    public final int getImpl_selectionStart() {
        if (this.attributes == null || this.attributes.impl_selectionStart == null) {
            return -1;
        }
        return this.attributes.getImpl_selectionStart();
    }

    @Deprecated
    public final IntegerProperty impl_selectionStartProperty() {
        return getTextAttribute().impl_selectionStartProperty();
    }

    @Deprecated
    public final void setImpl_selectionEnd(int value) {
        if (value == -1 && (this.attributes == null || this.attributes.impl_selectionEnd == null)) {
            return;
        }
        impl_selectionEndProperty().set(value);
    }

    @Deprecated
    public final int getImpl_selectionEnd() {
        if (this.attributes == null || this.attributes.impl_selectionEnd == null) {
            return -1;
        }
        return this.attributes.getImpl_selectionEnd();
    }

    @Deprecated
    public final IntegerProperty impl_selectionEndProperty() {
        return getTextAttribute().impl_selectionEndProperty();
    }

    @Deprecated
    public final ObjectProperty<Paint> impl_selectionFillProperty() {
        return getTextAttribute().impl_selectionFillProperty();
    }

    @Deprecated
    public final PathElement[] getImpl_caretShape() {
        return impl_caretShapeProperty().get();
    }

    @Deprecated
    public final ReadOnlyObjectProperty<PathElement[]> impl_caretShapeProperty() {
        return getTextAttribute().impl_caretShapeProperty();
    }

    @Deprecated
    public final void setImpl_caretPosition(int value) {
        if (value == -1 && (this.attributes == null || this.attributes.impl_caretPosition == null)) {
            return;
        }
        impl_caretPositionProperty().set(value);
    }

    @Deprecated
    public final int getImpl_caretPosition() {
        if (this.attributes == null || this.attributes.impl_caretPosition == null) {
            return -1;
        }
        return this.attributes.getImpl_caretPosition();
    }

    @Deprecated
    public final IntegerProperty impl_caretPositionProperty() {
        return getTextAttribute().impl_caretPositionProperty();
    }

    @Deprecated
    public final void setImpl_caretBias(boolean value) {
        if (value && (this.attributes == null || this.attributes.impl_caretBias == null)) {
            return;
        }
        impl_caretBiasProperty().set(value);
    }

    @Deprecated
    public final boolean isImpl_caretBias() {
        if (this.attributes == null || this.attributes.impl_caretBias == null) {
            return true;
        }
        return getTextAttribute().isImpl_caretBias();
    }

    @Deprecated
    public final BooleanProperty impl_caretBiasProperty() {
        return getTextAttribute().impl_caretBiasProperty();
    }

    @Deprecated
    public final HitInfo impl_hitTestChar(javafx.geometry.Point2D point) {
        if (point == null) {
            return null;
        }
        TextLayout layout = getTextLayout();
        double x2 = point.getX() - getX();
        double y2 = (point.getY() - getY()) + getYRendering();
        return layout.getHitInfo((float) x2, (float) y2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PathElement[] getRange(int start, int end, int type) {
        int length = getTextInternal().length();
        if (0 <= start && start < end && end <= length) {
            TextLayout layout = getTextLayout();
            float x2 = (float) getX();
            float y2 = ((float) getY()) - getYRendering();
            return layout.getRange(start, end, type, x2, y2);
        }
        return EMPTY_PATH_ELEMENT_ARRAY;
    }

    @Deprecated
    public final PathElement[] impl_getRangeShape(int start, int end) {
        return getRange(start, end, 1);
    }

    @Deprecated
    public final PathElement[] impl_getUnderlineShape(int start, int end) {
        return getRange(start, end, 2);
    }

    @Deprecated
    public final void impl_displaySoftwareKeyboard(boolean display) {
    }

    private float getYAdjustment(BaseBounds bounds) {
        VPos origin = getTextOrigin();
        if (origin == null) {
            origin = DEFAULT_TEXT_ORIGIN;
        }
        switch (origin) {
            case TOP:
                return -bounds.getMinY();
            case BASELINE:
                return 0.0f;
            case CENTER:
                return (-bounds.getMinY()) - (bounds.getHeight() / 2.0f);
            case BOTTOM:
                return (-bounds.getMinY()) - bounds.getHeight();
            default:
                return 0.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getYRendering() {
        if (isSpan()) {
            return 0.0f;
        }
        BaseBounds bounds = getLogicalBounds();
        VPos origin = getTextOrigin();
        if (origin == null) {
            origin = DEFAULT_TEXT_ORIGIN;
        }
        if (getBoundsType() == TextBoundsType.VISUAL) {
            BaseBounds vBounds = getVisualBounds();
            float delta = vBounds.getMinY() - bounds.getMinY();
            switch (origin) {
                case TOP:
                    return delta;
                case BASELINE:
                    return (-vBounds.getMinY()) + delta;
                case CENTER:
                    return (vBounds.getHeight() / 2.0f) + delta;
                case BOTTOM:
                    return vBounds.getHeight() + delta;
                default:
                    return 0.0f;
            }
        }
        switch (origin) {
            case TOP:
                return 0.0f;
            case BASELINE:
                return -bounds.getMinY();
            case CENTER:
                return bounds.getHeight() / 2.0f;
            case BOTTOM:
                return bounds.getHeight();
            default:
                return 0.0f;
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected final Bounds impl_computeLayoutBounds() {
        if (isSpan()) {
            BaseBounds bounds = getSpanBounds();
            double width = bounds.getWidth();
            double height = bounds.getHeight();
            return new BoundingBox(0.0d, 0.0d, width, height);
        }
        if (getBoundsType() == TextBoundsType.VISUAL) {
            return super.impl_computeLayoutBounds();
        }
        BaseBounds bounds2 = getLogicalBounds();
        double x2 = bounds2.getMinX() + getX();
        double y2 = bounds2.getMinY() + getY() + getYAdjustment(bounds2);
        double width2 = bounds2.getWidth();
        double height2 = bounds2.getHeight();
        double wrappingWidth = getWrappingWidth();
        if (wrappingWidth != 0.0d) {
            width2 = wrappingWidth;
        }
        return new BoundingBox(x2, y2, width2, height2);
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public final BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        BaseBounds bounds2;
        if (isSpan()) {
            if (this.impl_mode != NGShape.Mode.FILL && getStrokeType() != StrokeType.INSIDE) {
                return super.impl_computeGeomBounds(bounds, tx);
            }
            TextLayout layout = getTextLayout();
            BaseBounds bounds3 = layout.getBounds(getTextSpan(), bounds);
            BaseBounds spanBounds = getSpanBounds();
            float minX = bounds3.getMinX() - spanBounds.getMinX();
            float minY = bounds3.getMinY() - spanBounds.getMinY();
            float maxX = minX + bounds3.getWidth();
            float maxY = minY + bounds3.getHeight();
            BaseBounds bounds4 = bounds3.deriveWithNewBounds(minX, minY, 0.0f, maxX, maxY, 0.0f);
            return tx.transform(bounds4, bounds4);
        }
        if (getBoundsType() == TextBoundsType.VISUAL) {
            if (getTextInternal().length() == 0 || this.impl_mode == NGShape.Mode.EMPTY) {
                return bounds.makeEmpty();
            }
            if (this.impl_mode == NGShape.Mode.FILL || getStrokeType() == StrokeType.INSIDE) {
                BaseBounds visualBounds = getVisualBounds();
                float x2 = visualBounds.getMinX() + ((float) getX());
                float yadj = getYAdjustment(visualBounds);
                float y2 = visualBounds.getMinY() + yadj + ((float) getY());
                bounds.deriveWithNewBounds(x2, y2, 0.0f, x2 + visualBounds.getWidth(), y2 + visualBounds.getHeight(), 0.0f);
                return tx.transform(bounds, bounds);
            }
            return super.impl_computeGeomBounds(bounds, tx);
        }
        BaseBounds textBounds = getLogicalBounds();
        float x3 = textBounds.getMinX() + ((float) getX());
        float yadj2 = getYAdjustment(textBounds);
        float y3 = textBounds.getMinY() + yadj2 + ((float) getY());
        float width = textBounds.getWidth();
        float height = textBounds.getHeight();
        float wrappingWidth = (float) getWrappingWidth();
        if (wrappingWidth > width) {
            width = wrappingWidth;
        } else if (wrappingWidth > 0.0f) {
            NodeOrientation orientation = getEffectiveNodeOrientation();
            if (orientation == NodeOrientation.RIGHT_TO_LEFT) {
                x3 -= width - wrappingWidth;
            }
        }
        BaseBounds textBounds2 = new RectBounds(x3, y3, x3 + width, y3 + height);
        if (this.impl_mode != NGShape.Mode.FILL && getStrokeType() != StrokeType.INSIDE) {
            bounds2 = super.impl_computeGeomBounds(bounds, BaseTransform.IDENTITY_TRANSFORM);
        } else {
            TextLayout layout2 = getTextLayout();
            BaseBounds bounds5 = layout2.getBounds(null, bounds);
            float x4 = bounds5.getMinX() + ((float) getX());
            bounds2 = bounds5.deriveWithNewBounds(x4, y3, 0.0f, x4 + bounds5.getWidth(), y3 + height, 0.0f);
        }
        BaseBounds bounds6 = bounds2.deriveWithUnion(textBounds2);
        return tx.transform(bounds6, bounds6);
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected final boolean impl_computeContains(double localX, double localY) {
        double x2 = localX + getSpanBounds().getMinX();
        double y2 = localY + getSpanBounds().getMinY();
        GlyphList[] runs = getRuns();
        if (runs.length != 0) {
            for (GlyphList run : runs) {
                Point2D location = run.getLocation();
                float width = run.getWidth();
                RectBounds lineBounds = run.getLineBounds();
                float height = lineBounds.getHeight();
                if (location.f11907x <= x2 && x2 < location.f11907x + width && location.f11908y <= y2 && y2 < location.f11908y + height) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public final com.sun.javafx.geom.Shape impl_configShape() {
        float x2;
        float y2;
        if (this.impl_mode == NGShape.Mode.EMPTY || getTextInternal().length() == 0) {
            return new Path2D();
        }
        com.sun.javafx.geom.Shape shape = getShape();
        if (isSpan()) {
            BaseBounds bounds = getSpanBounds();
            x2 = -bounds.getMinX();
            y2 = -bounds.getMinY();
        } else {
            x2 = (float) getX();
            y2 = getYAdjustment(getVisualBounds()) + ((float) getY());
        }
        return TransformedShape.translatedShape(shape, x2, y2);
    }

    /* loaded from: jfxrt.jar:javafx/scene/text/Text$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Text, Font> FONT = new FontCssMetaData<Text>("-fx-font", Font.getDefault()) { // from class: javafx.scene.text.Text.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Text node) {
                return node.font == null || !node.font.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Font> getStyleableProperty(Text node) {
                return (StyleableProperty) node.fontProperty();
            }
        };
        private static final CssMetaData<Text, Boolean> UNDERLINE = new CssMetaData<Text, Boolean>("-fx-underline", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.text.Text.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Text node) {
                return node.attributes == null || node.attributes.underline == null || !node.attributes.underline.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Text node) {
                return (StyleableProperty) node.underlineProperty();
            }
        };
        private static final CssMetaData<Text, Boolean> STRIKETHROUGH = new CssMetaData<Text, Boolean>("-fx-strikethrough", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.text.Text.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Text node) {
                return node.attributes == null || node.attributes.strikethrough == null || !node.attributes.strikethrough.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(Text node) {
                return (StyleableProperty) node.strikethroughProperty();
            }
        };
        private static final CssMetaData<Text, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<Text, TextAlignment>("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) { // from class: javafx.scene.text.Text.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Text node) {
                return node.attributes == null || node.attributes.textAlignment == null || !node.attributes.textAlignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<TextAlignment> getStyleableProperty(Text node) {
                return (StyleableProperty) node.textAlignmentProperty();
            }
        };
        private static final CssMetaData<Text, VPos> TEXT_ORIGIN = new CssMetaData<Text, VPos>("-fx-text-origin", new EnumConverter(VPos.class), VPos.BASELINE) { // from class: javafx.scene.text.Text.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Text node) {
                return node.attributes == null || node.attributes.textOrigin == null || !node.attributes.textOrigin.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<VPos> getStyleableProperty(Text node) {
                return (StyleableProperty) node.textOriginProperty();
            }
        };
        private static final CssMetaData<Text, FontSmoothingType> FONT_SMOOTHING_TYPE = new CssMetaData<Text, FontSmoothingType>("-fx-font-smoothing-type", new EnumConverter(FontSmoothingType.class), FontSmoothingType.GRAY) { // from class: javafx.scene.text.Text.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Text node) {
                return node.fontSmoothingType == null || !node.fontSmoothingType.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<FontSmoothingType> getStyleableProperty(Text node) {
                return (StyleableProperty) node.fontSmoothingTypeProperty();
            }
        };
        private static final CssMetaData<Text, Number> LINE_SPACING = new CssMetaData<Text, Number>("-fx-line-spacing", SizeConverter.getInstance(), 0) { // from class: javafx.scene.text.Text.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Text node) {
                return node.attributes == null || node.attributes.lineSpacing == null || !node.attributes.lineSpacing.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Text node) {
                return (StyleableProperty) node.lineSpacingProperty();
            }
        };
        private static final CssMetaData<Text, TextBoundsType> BOUNDS_TYPE = new CssMetaData<Text, TextBoundsType>("-fx-bounds-type", new EnumConverter(TextBoundsType.class), Text.DEFAULT_BOUNDS_TYPE) { // from class: javafx.scene.text.Text.StyleableProperties.8
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Text node) {
                return node.boundsType == null || !node.boundsType.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<TextBoundsType> getStyleableProperty(Text node) {
                return (StyleableProperty) node.boundsTypeProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Shape.getClassCssMetaData());
            styleables.add(FONT);
            styleables.add(UNDERLINE);
            styleables.add(STRIKETHROUGH);
            styleables.add(TEXT_ALIGNMENT);
            styleables.add(TEXT_ORIGIN);
            styleables.add(FONT_SMOOTHING_TYPE);
            styleables.add(LINE_SPACING);
            styleables.add(BOUNDS_TYPE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    private void updatePGText() {
        NGText peer = (NGText) impl_getPeer();
        if (impl_isDirty(DirtyBits.TEXT_ATTRS)) {
            peer.setUnderline(isUnderline());
            peer.setStrikethrough(isStrikethrough());
            FontSmoothingType smoothing = getFontSmoothingType();
            if (smoothing == null) {
                smoothing = FontSmoothingType.GRAY;
            }
            peer.setFontSmoothingType(smoothing.ordinal());
        }
        if (impl_isDirty(DirtyBits.TEXT_FONT)) {
            peer.setFont(getFontInternal());
        }
        if (impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            peer.setGlyphs(getRuns());
        }
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            if (isSpan()) {
                BaseBounds spanBounds = getSpanBounds();
                peer.setLayoutLocation(spanBounds.getMinX(), spanBounds.getMinY());
            } else {
                float x2 = (float) getX();
                float y2 = (float) getY();
                float yadj = getYRendering();
                peer.setLayoutLocation(-x2, yadj - y2);
            }
        }
        if (impl_isDirty(DirtyBits.TEXT_SELECTION)) {
            Object fillObj = null;
            int start = getImpl_selectionStart();
            int end = getImpl_selectionEnd();
            int length = getTextInternal().length();
            if (0 <= start && start < end && end <= length) {
                Paint fill = impl_selectionFillProperty().get();
                fillObj = fill != null ? Toolkit.getPaintAccessor().getPlatformPaint(fill) : null;
            }
            peer.setSelection(start, end, fillObj);
        }
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public final void impl_updatePeer() {
        super.impl_updatePeer();
        updatePGText();
    }

    private TextAttribute getTextAttribute() {
        if (this.attributes == null) {
            this.attributes = new TextAttribute();
        }
        return this.attributes;
    }

    /* loaded from: jfxrt.jar:javafx/scene/text/Text$TextAttribute.class */
    private final class TextAttribute {
        private ObjectProperty<VPos> textOrigin;
        private BooleanProperty underline;
        private BooleanProperty strikethrough;
        private ObjectProperty<TextAlignment> textAlignment;
        private DoubleProperty lineSpacing;
        private ReadOnlyDoubleWrapper baselineOffset;

        @Deprecated
        private ObjectProperty<PathElement[]> impl_selectionShape;
        private ObjectBinding<PathElement[]> impl_selectionBinding;
        private ObjectProperty<Paint> selectionFill;

        @Deprecated
        private IntegerProperty impl_selectionStart;

        @Deprecated
        private IntegerProperty impl_selectionEnd;

        @Deprecated
        private ObjectProperty<PathElement[]> impl_caretShape;
        private ObjectBinding<PathElement[]> impl_caretBinding;

        @Deprecated
        private IntegerProperty impl_caretPosition;

        @Deprecated
        private BooleanProperty impl_caretBias;

        private TextAttribute() {
        }

        public final VPos getTextOrigin() {
            return this.textOrigin == null ? Text.DEFAULT_TEXT_ORIGIN : this.textOrigin.get();
        }

        public final ObjectProperty<VPos> textOriginProperty() {
            if (this.textOrigin == null) {
                this.textOrigin = new StyleableObjectProperty<VPos>(Text.DEFAULT_TEXT_ORIGIN) { // from class: javafx.scene.text.Text.TextAttribute.1
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "textOrigin";
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.TEXT_ORIGIN;
                    }

                    @Override // javafx.beans.property.ObjectPropertyBase
                    public void invalidated() {
                        Text.this.impl_geomChanged();
                    }
                };
            }
            return this.textOrigin;
        }

        public final boolean isUnderline() {
            if (this.underline == null) {
                return false;
            }
            return this.underline.get();
        }

        public final BooleanProperty underlineProperty() {
            if (this.underline == null) {
                this.underline = new StyleableBooleanProperty() { // from class: javafx.scene.text.Text.TextAttribute.2
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "underline";
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                        return StyleableProperties.UNDERLINE;
                    }

                    @Override // javafx.beans.property.BooleanPropertyBase
                    public void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
                        if (Text.this.getBoundsType() == TextBoundsType.VISUAL) {
                            Text.this.impl_geomChanged();
                        }
                    }
                };
            }
            return this.underline;
        }

        public final boolean isStrikethrough() {
            if (this.strikethrough == null) {
                return false;
            }
            return this.strikethrough.get();
        }

        public final BooleanProperty strikethroughProperty() {
            if (this.strikethrough == null) {
                this.strikethrough = new StyleableBooleanProperty() { // from class: javafx.scene.text.Text.TextAttribute.3
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "strikethrough";
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                        return StyleableProperties.STRIKETHROUGH;
                    }

                    @Override // javafx.beans.property.BooleanPropertyBase
                    public void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_ATTRS);
                        if (Text.this.getBoundsType() == TextBoundsType.VISUAL) {
                            Text.this.impl_geomChanged();
                        }
                    }
                };
            }
            return this.strikethrough;
        }

        public final TextAlignment getTextAlignment() {
            return this.textAlignment == null ? Text.DEFAULT_TEXT_ALIGNMENT : this.textAlignment.get();
        }

        public final ObjectProperty<TextAlignment> textAlignmentProperty() {
            if (this.textAlignment == null) {
                this.textAlignment = new StyleableObjectProperty<TextAlignment>(Text.DEFAULT_TEXT_ALIGNMENT) { // from class: javafx.scene.text.Text.TextAttribute.4
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "textAlignment";
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData getCssMetaData() {
                        return StyleableProperties.TEXT_ALIGNMENT;
                    }

                    @Override // javafx.beans.property.ObjectPropertyBase
                    public void invalidated() {
                        if (!Text.this.isSpan()) {
                            TextAlignment alignment = get();
                            if (alignment == null) {
                                alignment = Text.DEFAULT_TEXT_ALIGNMENT;
                            }
                            TextLayout layout = Text.this.getTextLayout();
                            if (layout.setAlignment(alignment.ordinal())) {
                                Text.this.needsTextLayout();
                            }
                        }
                    }
                };
            }
            return this.textAlignment;
        }

        public final double getLineSpacing() {
            if (this.lineSpacing == null) {
                return 0.0d;
            }
            return this.lineSpacing.get();
        }

        public final DoubleProperty lineSpacingProperty() {
            if (this.lineSpacing == null) {
                this.lineSpacing = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.text.Text.TextAttribute.5
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "lineSpacing";
                    }

                    @Override // javafx.css.StyleableProperty
                    public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                        return StyleableProperties.LINE_SPACING;
                    }

                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        if (!Text.this.isSpan()) {
                            TextLayout layout = Text.this.getTextLayout();
                            if (layout.setLineSpacing((float) get())) {
                                Text.this.needsTextLayout();
                            }
                        }
                    }
                };
            }
            return this.lineSpacing;
        }

        public final ReadOnlyDoubleProperty baselineOffsetProperty() {
            if (this.baselineOffset == null) {
                this.baselineOffset = new ReadOnlyDoubleWrapper(Text.this, "baselineOffset") { // from class: javafx.scene.text.Text.TextAttribute.6
                    {
                        bind(new DoubleBinding() { // from class: javafx.scene.text.Text.TextAttribute.6.1
                            {
                                bind(Text.this.fontProperty());
                            }

                            @Override // javafx.beans.binding.DoubleBinding
                            protected double computeValue() {
                                BaseBounds bounds = Text.this.getLogicalBounds();
                                return -bounds.getMinY();
                            }
                        });
                    }
                };
            }
            return this.baselineOffset.getReadOnlyProperty();
        }

        @Deprecated
        public final ReadOnlyObjectProperty<PathElement[]> impl_selectionShapeProperty() {
            if (this.impl_selectionShape == null) {
                this.impl_selectionBinding = new ObjectBinding<PathElement[]>() { // from class: javafx.scene.text.Text.TextAttribute.7
                    {
                        bind(TextAttribute.this.impl_selectionStartProperty(), TextAttribute.this.impl_selectionEndProperty());
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // javafx.beans.binding.ObjectBinding
                    public PathElement[] computeValue() {
                        int start = TextAttribute.this.getImpl_selectionStart();
                        int end = TextAttribute.this.getImpl_selectionEnd();
                        return Text.this.getRange(start, end, 1);
                    }
                };
                this.impl_selectionShape = new SimpleObjectProperty(Text.this, "impl_selectionShape");
                this.impl_selectionShape.bind(this.impl_selectionBinding);
            }
            return this.impl_selectionShape;
        }

        @Deprecated
        public final ObjectProperty<Paint> impl_selectionFillProperty() {
            if (this.selectionFill == null) {
                this.selectionFill = new ObjectPropertyBase<Paint>(Text.DEFAULT_SELECTION_FILL) { // from class: javafx.scene.text.Text.TextAttribute.8
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "impl_selectionFill";
                    }

                    @Override // javafx.beans.property.ObjectPropertyBase
                    protected void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
                    }
                };
            }
            return this.selectionFill;
        }

        @Deprecated
        public final int getImpl_selectionStart() {
            if (this.impl_selectionStart == null) {
                return -1;
            }
            return this.impl_selectionStart.get();
        }

        @Deprecated
        public final IntegerProperty impl_selectionStartProperty() {
            if (this.impl_selectionStart == null) {
                this.impl_selectionStart = new IntegerPropertyBase(-1) { // from class: javafx.scene.text.Text.TextAttribute.9
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "impl_selectionStart";
                    }

                    @Override // javafx.beans.property.IntegerPropertyBase
                    protected void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
                        Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_START);
                    }
                };
            }
            return this.impl_selectionStart;
        }

        @Deprecated
        public final int getImpl_selectionEnd() {
            if (this.impl_selectionEnd == null) {
                return -1;
            }
            return this.impl_selectionEnd.get();
        }

        @Deprecated
        public final IntegerProperty impl_selectionEndProperty() {
            if (this.impl_selectionEnd == null) {
                this.impl_selectionEnd = new IntegerPropertyBase(-1) { // from class: javafx.scene.text.Text.TextAttribute.10
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "impl_selectionEnd";
                    }

                    @Override // javafx.beans.property.IntegerPropertyBase
                    protected void invalidated() {
                        Text.this.impl_markDirty(DirtyBits.TEXT_SELECTION);
                        Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_END);
                    }
                };
            }
            return this.impl_selectionEnd;
        }

        @Deprecated
        public final ReadOnlyObjectProperty<PathElement[]> impl_caretShapeProperty() {
            if (this.impl_caretShape == null) {
                this.impl_caretBinding = new ObjectBinding<PathElement[]>() { // from class: javafx.scene.text.Text.TextAttribute.11
                    {
                        bind(TextAttribute.this.impl_caretPositionProperty(), TextAttribute.this.impl_caretBiasProperty());
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // javafx.beans.binding.ObjectBinding
                    public PathElement[] computeValue() {
                        int pos = TextAttribute.this.getImpl_caretPosition();
                        int length = Text.this.getTextInternal().length();
                        if (0 > pos || pos > length) {
                            return Text.EMPTY_PATH_ELEMENT_ARRAY;
                        }
                        boolean bias = TextAttribute.this.isImpl_caretBias();
                        float x2 = (float) Text.this.getX();
                        float y2 = ((float) Text.this.getY()) - Text.this.getYRendering();
                        TextLayout layout = Text.this.getTextLayout();
                        return layout.getCaretShape(pos, bias, x2, y2);
                    }
                };
                this.impl_caretShape = new SimpleObjectProperty(Text.this, "impl_caretShape");
                this.impl_caretShape.bind(this.impl_caretBinding);
            }
            return this.impl_caretShape;
        }

        @Deprecated
        public final int getImpl_caretPosition() {
            if (this.impl_caretPosition == null) {
                return -1;
            }
            return this.impl_caretPosition.get();
        }

        @Deprecated
        public final IntegerProperty impl_caretPositionProperty() {
            if (this.impl_caretPosition == null) {
                this.impl_caretPosition = new IntegerPropertyBase(-1) { // from class: javafx.scene.text.Text.TextAttribute.12
                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Text.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "impl_caretPosition";
                    }

                    @Override // javafx.beans.property.IntegerPropertyBase
                    protected void invalidated() {
                        Text.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_END);
                    }
                };
            }
            return this.impl_caretPosition;
        }

        @Deprecated
        public final boolean isImpl_caretBias() {
            if (this.impl_caretBias == null) {
                return true;
            }
            return this.impl_caretBias.get();
        }

        @Deprecated
        public final BooleanProperty impl_caretBiasProperty() {
            if (this.impl_caretBias == null) {
                this.impl_caretBias = new SimpleBooleanProperty(Text.this, "impl_caretBias", true);
            }
            return this.impl_caretBias;
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("Text[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("text=\"").append(getText()).append(PdfOps.DOUBLE_QUOTE__TOKEN);
        sb.append(", x=").append(getX());
        sb.append(", y=").append(getY());
        sb.append(", alignment=").append((Object) getTextAlignment());
        sb.append(", origin=").append((Object) getTextOrigin());
        sb.append(", boundsType=").append((Object) getBoundsType());
        double spacing = getLineSpacing();
        if (spacing != 0.0d) {
            sb.append(", lineSpacing=").append(spacing);
        }
        double wrap = getWrappingWidth();
        if (wrap != 0.0d) {
            sb.append(", wrappingWidth=").append(wrap);
        }
        sb.append(", font=").append((Object) getFont());
        sb.append(", fontSmoothingType=").append((Object) getFontSmoothingType());
        if (isStrikethrough()) {
            sb.append(", strikethrough");
        }
        if (isUnderline()) {
            sb.append(", underline");
        }
        sb.append(", fill=").append((Object) getFill());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }

    @Override // javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT:
                String accText = getAccessibleText();
                return (accText == null || accText.isEmpty()) ? getText() : accText;
            case FONT:
                return getFont();
            case CARET_OFFSET:
                int sel = getImpl_caretPosition();
                return sel >= 0 ? Integer.valueOf(sel) : Integer.valueOf(getText().length());
            case SELECTION_START:
                int sel2 = getImpl_selectionStart();
                if (sel2 >= 0) {
                    return Integer.valueOf(sel2);
                }
                int sel3 = getImpl_caretPosition();
                return sel3 >= 0 ? Integer.valueOf(sel3) : Integer.valueOf(getText().length());
            case SELECTION_END:
                int sel4 = getImpl_selectionEnd();
                if (sel4 >= 0) {
                    return Integer.valueOf(sel4);
                }
                int sel5 = getImpl_caretPosition();
                return sel5 >= 0 ? Integer.valueOf(sel5) : Integer.valueOf(getText().length());
            case LINE_FOR_OFFSET:
                int offset = ((Integer) parameters[0]).intValue();
                if (offset > getTextInternal().length()) {
                    return null;
                }
                TextLine[] lines = getTextLayout().getLines();
                int lineIndex = 0;
                for (int i2 = 1; i2 < lines.length && lines[i2].getStart() <= offset; i2++) {
                    lineIndex++;
                }
                return Integer.valueOf(lineIndex);
            case LINE_START:
                int lineIndex2 = ((Integer) parameters[0]).intValue();
                TextLine[] lines2 = getTextLayout().getLines();
                if (0 <= lineIndex2 && lineIndex2 < lines2.length) {
                    return Integer.valueOf(lines2[lineIndex2].getStart());
                }
                return null;
            case LINE_END:
                int lineIndex3 = ((Integer) parameters[0]).intValue();
                TextLine[] lines3 = getTextLayout().getLines();
                if (0 <= lineIndex3 && lineIndex3 < lines3.length) {
                    TextLine line = lines3[lineIndex3];
                    return Integer.valueOf(line.getStart() + line.getLength());
                }
                return null;
            case OFFSET_AT_POINT:
                javafx.geometry.Point2D point = (javafx.geometry.Point2D) parameters[0];
                return Integer.valueOf(impl_hitTestChar(screenToLocal(point)).getCharIndex());
            case BOUNDS_FOR_RANGE:
                int start = ((Integer) parameters[0]).intValue();
                int end = ((Integer) parameters[1]).intValue();
                PathElement[] elements = impl_getRangeShape(start, end + 1);
                Bounds[] bounds = new Bounds[elements.length / 5];
                int index = 0;
                for (int i3 = 0; i3 < bounds.length; i3++) {
                    MoveTo topLeft = (MoveTo) elements[index];
                    LineTo topRight = (LineTo) elements[index + 1];
                    LineTo bottomRight = (LineTo) elements[index + 2];
                    BoundingBox b2 = new BoundingBox(topLeft.getX(), topLeft.getY(), topRight.getX() - topLeft.getX(), bottomRight.getY() - topRight.getY());
                    bounds[i3] = localToScreen(b2);
                    index += 5;
                }
                return bounds;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
