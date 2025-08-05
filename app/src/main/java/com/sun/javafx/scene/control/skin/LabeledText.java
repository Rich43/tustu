package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/LabeledText.class */
public class LabeledText extends Text {
    private final Labeled labeled;
    private StyleablePropertyMirror<Font> fontMirror = null;
    private StyleablePropertyMirror<Paint> fillMirror;
    private StyleablePropertyMirror<TextAlignment> textAlignmentMirror;
    private StyleablePropertyMirror<Boolean> underlineMirror;
    private StyleablePropertyMirror<Number> lineSpacingMirror;
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
    private static final CssMetaData<LabeledText, Font> FONT = new FontCssMetaData<LabeledText>("-fx-font", Font.getDefault()) { // from class: com.sun.javafx.scene.control.skin.LabeledText.1
        @Override // javafx.css.CssMetaData
        public boolean isSettable(LabeledText node) {
            return node.labeled == null || !node.labeled.fontProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Font> getStyleableProperty(LabeledText node) {
            return node.fontMirror();
        }
    };
    private static final CssMetaData<LabeledText, Paint> FILL = new CssMetaData<LabeledText, Paint>("-fx-fill", PaintConverter.getInstance(), Color.BLACK) { // from class: com.sun.javafx.scene.control.skin.LabeledText.2
        @Override // javafx.css.CssMetaData
        public boolean isSettable(LabeledText node) {
            return !node.labeled.textFillProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Paint> getStyleableProperty(LabeledText node) {
            return node.fillMirror();
        }
    };
    private static final CssMetaData<LabeledText, TextAlignment> TEXT_ALIGNMENT = new CssMetaData<LabeledText, TextAlignment>("-fx-text-alignment", new EnumConverter(TextAlignment.class), TextAlignment.LEFT) { // from class: com.sun.javafx.scene.control.skin.LabeledText.3
        @Override // javafx.css.CssMetaData
        public boolean isSettable(LabeledText node) {
            return !node.labeled.textAlignmentProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<TextAlignment> getStyleableProperty(LabeledText node) {
            return node.textAlignmentMirror();
        }
    };
    private static final CssMetaData<LabeledText, Boolean> UNDERLINE = new CssMetaData<LabeledText, Boolean>("-fx-underline", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: com.sun.javafx.scene.control.skin.LabeledText.4
        @Override // javafx.css.CssMetaData
        public boolean isSettable(LabeledText node) {
            return !node.labeled.underlineProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Boolean> getStyleableProperty(LabeledText node) {
            return node.underlineMirror();
        }
    };
    private static final CssMetaData<LabeledText, Number> LINE_SPACING = new CssMetaData<LabeledText, Number>("-fx-line-spacing", SizeConverter.getInstance(), 0) { // from class: com.sun.javafx.scene.control.skin.LabeledText.5
        @Override // javafx.css.CssMetaData
        public boolean isSettable(LabeledText node) {
            return !node.labeled.lineSpacingProperty().isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Number> getStyleableProperty(LabeledText node) {
            return node.lineSpacingMirror();
        }
    };

    public LabeledText(Labeled labeled) {
        if (labeled == null) {
            throw new IllegalArgumentException("labeled cannot be null");
        }
        this.labeled = labeled;
        setFill(this.labeled.getTextFill());
        setFont(this.labeled.getFont());
        setTextAlignment(this.labeled.getTextAlignment());
        setUnderline(this.labeled.isUnderline());
        setLineSpacing(this.labeled.getLineSpacing());
        fillProperty().bind(this.labeled.textFillProperty());
        fontProperty().bind(this.labeled.fontProperty());
        textAlignmentProperty().bind(this.labeled.textAlignmentProperty());
        underlineProperty().bind(this.labeled.underlineProperty());
        lineSpacingProperty().bind(this.labeled.lineSpacingProperty());
        getStyleClass().addAll("text");
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override // javafx.scene.text.Text, javafx.scene.shape.Shape, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleableProperty<Font> fontMirror() {
        if (this.fontMirror == null) {
            this.fontMirror = new StyleablePropertyMirror<>(FONT, "fontMirror", Font.getDefault(), (StyleableProperty) this.labeled.fontProperty());
            fontProperty().addListener(this.fontMirror);
        }
        return this.fontMirror;
    }

    static {
        List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Text.getClassCssMetaData());
        int nMax = styleables.size();
        for (int n2 = 0; n2 < nMax; n2++) {
            String prop = styleables.get(n2).getProperty();
            if ("-fx-fill".equals(prop)) {
                styleables.set(n2, FILL);
            } else if ("-fx-font".equals(prop)) {
                styleables.set(n2, FONT);
            } else if ("-fx-text-alignment".equals(prop)) {
                styleables.set(n2, TEXT_ALIGNMENT);
            } else if ("-fx-underline".equals(prop)) {
                styleables.set(n2, UNDERLINE);
            } else if ("-fx-line-spacing".equals(prop)) {
                styleables.set(n2, LINE_SPACING);
            }
        }
        STYLEABLES = Collections.unmodifiableList(styleables);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleableProperty<Paint> fillMirror() {
        if (this.fillMirror == null) {
            this.fillMirror = new StyleablePropertyMirror<>(FILL, "fillMirror", Color.BLACK, (StyleableProperty) this.labeled.textFillProperty());
            fillProperty().addListener(this.fillMirror);
        }
        return this.fillMirror;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleableProperty<TextAlignment> textAlignmentMirror() {
        if (this.textAlignmentMirror == null) {
            this.textAlignmentMirror = new StyleablePropertyMirror<>(TEXT_ALIGNMENT, "textAlignmentMirror", TextAlignment.LEFT, (StyleableProperty) this.labeled.textAlignmentProperty());
            textAlignmentProperty().addListener(this.textAlignmentMirror);
        }
        return this.textAlignmentMirror;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleableProperty<Boolean> underlineMirror() {
        if (this.underlineMirror == null) {
            this.underlineMirror = new StyleablePropertyMirror<>(UNDERLINE, "underLineMirror", Boolean.FALSE, (StyleableProperty) this.labeled.underlineProperty());
            underlineProperty().addListener(this.underlineMirror);
        }
        return this.underlineMirror;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleableProperty<Number> lineSpacingMirror() {
        if (this.lineSpacingMirror == null) {
            this.lineSpacingMirror = new StyleablePropertyMirror<>(LINE_SPACING, "lineSpacingMirror", Double.valueOf(0.0d), (StyleableProperty) this.labeled.lineSpacingProperty());
            lineSpacingProperty().addListener(this.lineSpacingMirror);
        }
        return this.lineSpacingMirror;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/LabeledText$StyleablePropertyMirror.class */
    private class StyleablePropertyMirror<T> extends SimpleStyleableObjectProperty<T> implements InvalidationListener {
        boolean applying;
        private final StyleableProperty<T> property;

        private StyleablePropertyMirror(CssMetaData<LabeledText, T> cssMetaData, String name, T initialValue, StyleableProperty<T> property) {
            super(cssMetaData, LabeledText.this, name, initialValue);
            this.property = property;
            this.applying = false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            if (!this.applying) {
                super.applyStyle(null, ((ObservableValue) observable).getValue2());
            }
        }

        @Override // javafx.css.StyleableObjectProperty, javafx.css.StyleableProperty
        public void applyStyle(StyleOrigin newOrigin, T value) {
            this.applying = true;
            StyleOrigin propOrigin = this.property.getStyleOrigin();
            if (propOrigin == null || (newOrigin == null ? propOrigin != StyleOrigin.USER : propOrigin.compareTo(newOrigin) <= 0)) {
                super.applyStyle(newOrigin, value);
                this.property.applyStyle(newOrigin, value);
            }
            this.applying = false;
        }

        @Override // javafx.css.StyleableObjectProperty, javafx.css.StyleableProperty
        public StyleOrigin getStyleOrigin() {
            return this.property.getStyleOrigin();
        }
    }
}
