package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.behavior.ColorPickerBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ColorPickerSkin.class */
public class ColorPickerSkin extends ComboBoxPopupControl<Color> {
    private Label displayNode;
    private StackPane pickerColorBox;
    private Rectangle colorRect;
    private ColorPalette popupContent;
    BooleanProperty colorLabelVisible;
    private final StyleableStringProperty imageUrl;
    private final StyleableDoubleProperty colorRectWidth;
    private final StyleableDoubleProperty colorRectHeight;
    private final StyleableDoubleProperty colorRectX;
    private final StyleableDoubleProperty colorRectY;
    private static final Map<Color, String> colorNameMap = new HashMap(24);
    private static final Map<Color, String> cssNameMap = new HashMap(139);

    public StringProperty imageUrlProperty() {
        return this.imageUrl;
    }

    public ColorPickerSkin(ColorPicker colorPicker) {
        super(colorPicker, new ColorPickerBehavior(colorPicker));
        this.colorLabelVisible = new StyleableBooleanProperty(true) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.beans.property.BooleanPropertyBase
            public void invalidated() {
                if (ColorPickerSkin.this.displayNode != null) {
                    if (ColorPickerSkin.this.colorLabelVisible.get()) {
                        ColorPickerSkin.this.displayNode.setText(ColorPickerSkin.colorDisplayName(((ColorPicker) ColorPickerSkin.this.getSkinnable()).getValue()));
                    } else {
                        ColorPickerSkin.this.displayNode.setText("");
                    }
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ColorPickerSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "colorLabelVisible";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.COLOR_LABEL_VISIBLE;
            }
        };
        this.imageUrl = new StyleableStringProperty() { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.2
            @Override // javafx.css.StyleableStringProperty, javafx.css.StyleableProperty
            public void applyStyle(StyleOrigin origin, String v2) {
                super.applyStyle(origin, v2);
                if (v2 == null) {
                    if (ColorPickerSkin.this.pickerColorBox.getChildren().size() == 2) {
                        ColorPickerSkin.this.pickerColorBox.getChildren().remove(1);
                    }
                } else if (ColorPickerSkin.this.pickerColorBox.getChildren().size() == 2) {
                    ImageView imageView = (ImageView) ColorPickerSkin.this.pickerColorBox.getChildren().get(1);
                    imageView.setImage(StyleManager.getInstance().getCachedImage(v2));
                } else {
                    ColorPickerSkin.this.pickerColorBox.getChildren().add(new ImageView(StyleManager.getInstance().getCachedImage(v2)));
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ColorPickerSkin.this;
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
        this.colorRectWidth = new StyleableDoubleProperty(12.0d) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.3
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (ColorPickerSkin.this.pickerColorBox != null) {
                    ColorPickerSkin.this.pickerColorBox.requestLayout();
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.COLOR_RECT_WIDTH;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ColorPickerSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "colorRectWidth";
            }
        };
        this.colorRectHeight = new StyleableDoubleProperty(12.0d) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.4
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (ColorPickerSkin.this.pickerColorBox != null) {
                    ColorPickerSkin.this.pickerColorBox.requestLayout();
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.COLOR_RECT_HEIGHT;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ColorPickerSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "colorRectHeight";
            }
        };
        this.colorRectX = new StyleableDoubleProperty(0.0d) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.5
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (ColorPickerSkin.this.pickerColorBox != null) {
                    ColorPickerSkin.this.pickerColorBox.requestLayout();
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.COLOR_RECT_X;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ColorPickerSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "colorRectX";
            }
        };
        this.colorRectY = new StyleableDoubleProperty(0.0d) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.6
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                if (ColorPickerSkin.this.pickerColorBox != null) {
                    ColorPickerSkin.this.pickerColorBox.requestLayout();
                }
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.COLOR_RECT_Y;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ColorPickerSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "colorRectY";
            }
        };
        updateComboBoxMode();
        registerChangeListener(colorPicker.valueProperty(), "VALUE");
        this.displayNode = new Label();
        this.displayNode.getStyleClass().add("color-picker-label");
        this.displayNode.setManaged(false);
        this.pickerColorBox = new PickerColorBox();
        this.pickerColorBox.getStyleClass().add("picker-color");
        this.colorRect = new Rectangle(12.0d, 12.0d);
        this.colorRect.getStyleClass().add("picker-color-rect");
        updateColor();
        this.pickerColorBox.getChildren().add(this.colorRect);
        this.displayNode.setGraphic(this.pickerColorBox);
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (!this.colorLabelVisible.get()) {
            return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        }
        String displayNodeText = this.displayNode.getText();
        double width = 0.0d;
        for (String name : colorNameMap.values()) {
            this.displayNode.setText(name);
            width = Math.max(width, super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset));
        }
        this.displayNode.setText(formatHexString(Color.BLACK));
        double width2 = Math.max(width, super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset));
        this.displayNode.setText(displayNodeText);
        return width2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateComboBoxMode() {
        List<String> styleClass = ((ComboBoxBase) getSkinnable()).getStyleClass();
        if (styleClass.contains(ColorPicker.STYLE_CLASS_BUTTON)) {
            setMode(ComboBoxMode.BUTTON);
        } else if (styleClass.contains(ColorPicker.STYLE_CLASS_SPLIT_BUTTON)) {
            setMode(ComboBoxMode.SPLITBUTTON);
        }
    }

    static {
        colorNameMap.put(Color.TRANSPARENT, getString("colorName.transparent"));
        colorNameMap.put(Color.BLACK, getString("colorName.black"));
        colorNameMap.put(Color.BLUE, getString("colorName.blue"));
        colorNameMap.put(Color.CYAN, getString("colorName.cyan"));
        colorNameMap.put(Color.DARKBLUE, getString("colorName.darkblue"));
        colorNameMap.put(Color.DARKCYAN, getString("colorName.darkcyan"));
        colorNameMap.put(Color.DARKGRAY, getString("colorName.darkgray"));
        colorNameMap.put(Color.DARKGREEN, getString("colorName.darkgreen"));
        colorNameMap.put(Color.DARKMAGENTA, getString("colorName.darkmagenta"));
        colorNameMap.put(Color.DARKRED, getString("colorName.darkred"));
        colorNameMap.put(Color.GRAY, getString("colorName.gray"));
        colorNameMap.put(Color.GREEN, getString("colorName.green"));
        colorNameMap.put(Color.LIGHTBLUE, getString("colorName.lightblue"));
        colorNameMap.put(Color.LIGHTCYAN, getString("colorName.lightcyan"));
        colorNameMap.put(Color.LIGHTGRAY, getString("colorName.lightgray"));
        colorNameMap.put(Color.LIGHTGREEN, getString("colorName.lightgreen"));
        colorNameMap.put(Color.LIGHTYELLOW, getString("colorName.lightyellow"));
        colorNameMap.put(Color.MAGENTA, getString("colorName.magenta"));
        colorNameMap.put(Color.MEDIUMBLUE, getString("colorName.mediumblue"));
        colorNameMap.put(Color.ORANGE, getString("colorName.orange"));
        colorNameMap.put(Color.PINK, getString("colorName.pink"));
        colorNameMap.put(Color.RED, getString("colorName.red"));
        colorNameMap.put(Color.WHITE, getString("colorName.white"));
        colorNameMap.put(Color.YELLOW, getString("colorName.yellow"));
        cssNameMap.put(Color.ALICEBLUE, "aliceblue");
        cssNameMap.put(Color.ANTIQUEWHITE, "antiquewhite");
        cssNameMap.put(Color.AQUAMARINE, "aquamarine");
        cssNameMap.put(Color.AZURE, "azure");
        cssNameMap.put(Color.BEIGE, "beige");
        cssNameMap.put(Color.BISQUE, "bisque");
        cssNameMap.put(Color.BLACK, "black");
        cssNameMap.put(Color.BLANCHEDALMOND, "blanchedalmond");
        cssNameMap.put(Color.BLUE, "blue");
        cssNameMap.put(Color.BLUEVIOLET, "blueviolet");
        cssNameMap.put(Color.BROWN, "brown");
        cssNameMap.put(Color.BURLYWOOD, "burlywood");
        cssNameMap.put(Color.CADETBLUE, "cadetblue");
        cssNameMap.put(Color.CHARTREUSE, "chartreuse");
        cssNameMap.put(Color.CHOCOLATE, "chocolate");
        cssNameMap.put(Color.CORAL, "coral");
        cssNameMap.put(Color.CORNFLOWERBLUE, "cornflowerblue");
        cssNameMap.put(Color.CORNSILK, "cornsilk");
        cssNameMap.put(Color.CRIMSON, "crimson");
        cssNameMap.put(Color.CYAN, "cyan");
        cssNameMap.put(Color.DARKBLUE, "darkblue");
        cssNameMap.put(Color.DARKCYAN, "darkcyan");
        cssNameMap.put(Color.DARKGOLDENROD, "darkgoldenrod");
        cssNameMap.put(Color.DARKGRAY, "darkgray");
        cssNameMap.put(Color.DARKGREEN, "darkgreen");
        cssNameMap.put(Color.DARKKHAKI, "darkkhaki");
        cssNameMap.put(Color.DARKMAGENTA, "darkmagenta");
        cssNameMap.put(Color.DARKOLIVEGREEN, "darkolivegreen");
        cssNameMap.put(Color.DARKORANGE, "darkorange");
        cssNameMap.put(Color.DARKORCHID, "darkorchid");
        cssNameMap.put(Color.DARKRED, "darkred");
        cssNameMap.put(Color.DARKSALMON, "darksalmon");
        cssNameMap.put(Color.DARKSEAGREEN, "darkseagreen");
        cssNameMap.put(Color.DARKSLATEBLUE, "darkslateblue");
        cssNameMap.put(Color.DARKSLATEGRAY, "darkslategray");
        cssNameMap.put(Color.DARKTURQUOISE, "darkturquoise");
        cssNameMap.put(Color.DARKVIOLET, "darkviolet");
        cssNameMap.put(Color.DEEPPINK, "deeppink");
        cssNameMap.put(Color.DEEPSKYBLUE, "deepskyblue");
        cssNameMap.put(Color.DIMGRAY, "dimgray");
        cssNameMap.put(Color.DODGERBLUE, "dodgerblue");
        cssNameMap.put(Color.FIREBRICK, "firebrick");
        cssNameMap.put(Color.FLORALWHITE, "floralwhite");
        cssNameMap.put(Color.FORESTGREEN, "forestgreen");
        cssNameMap.put(Color.GAINSBORO, "gainsboro");
        cssNameMap.put(Color.GHOSTWHITE, "ghostwhite");
        cssNameMap.put(Color.GOLD, "gold");
        cssNameMap.put(Color.GOLDENROD, "goldenrod");
        cssNameMap.put(Color.GRAY, "gray");
        cssNameMap.put(Color.GREEN, "green");
        cssNameMap.put(Color.GREENYELLOW, "greenyellow");
        cssNameMap.put(Color.HONEYDEW, "honeydew");
        cssNameMap.put(Color.HOTPINK, "hotpink");
        cssNameMap.put(Color.INDIANRED, "indianred");
        cssNameMap.put(Color.INDIGO, "indigo");
        cssNameMap.put(Color.IVORY, "ivory");
        cssNameMap.put(Color.KHAKI, "khaki");
        cssNameMap.put(Color.LAVENDER, "lavender");
        cssNameMap.put(Color.LAVENDERBLUSH, "lavenderblush");
        cssNameMap.put(Color.LAWNGREEN, "lawngreen");
        cssNameMap.put(Color.LEMONCHIFFON, "lemonchiffon");
        cssNameMap.put(Color.LIGHTBLUE, "lightblue");
        cssNameMap.put(Color.LIGHTCORAL, "lightcoral");
        cssNameMap.put(Color.LIGHTCYAN, "lightcyan");
        cssNameMap.put(Color.LIGHTGOLDENRODYELLOW, "lightgoldenrodyellow");
        cssNameMap.put(Color.LIGHTGRAY, "lightgray");
        cssNameMap.put(Color.LIGHTGREEN, "lightgreen");
        cssNameMap.put(Color.LIGHTPINK, "lightpink");
        cssNameMap.put(Color.LIGHTSALMON, "lightsalmon");
        cssNameMap.put(Color.LIGHTSEAGREEN, "lightseagreen");
        cssNameMap.put(Color.LIGHTSKYBLUE, "lightskyblue");
        cssNameMap.put(Color.LIGHTSLATEGRAY, "lightslategray");
        cssNameMap.put(Color.LIGHTSTEELBLUE, "lightsteelblue");
        cssNameMap.put(Color.LIGHTYELLOW, "lightyellow");
        cssNameMap.put(Color.LIME, "lime");
        cssNameMap.put(Color.LIMEGREEN, "limegreen");
        cssNameMap.put(Color.LINEN, "linen");
        cssNameMap.put(Color.MAGENTA, "magenta");
        cssNameMap.put(Color.MAROON, "maroon");
        cssNameMap.put(Color.MEDIUMAQUAMARINE, "mediumaquamarine");
        cssNameMap.put(Color.MEDIUMBLUE, "mediumblue");
        cssNameMap.put(Color.MEDIUMORCHID, "mediumorchid");
        cssNameMap.put(Color.MEDIUMPURPLE, "mediumpurple");
        cssNameMap.put(Color.MEDIUMSEAGREEN, "mediumseagreen");
        cssNameMap.put(Color.MEDIUMSLATEBLUE, "mediumslateblue");
        cssNameMap.put(Color.MEDIUMSPRINGGREEN, "mediumspringgreen");
        cssNameMap.put(Color.MEDIUMTURQUOISE, "mediumturquoise");
        cssNameMap.put(Color.MEDIUMVIOLETRED, "mediumvioletred");
        cssNameMap.put(Color.MIDNIGHTBLUE, "midnightblue");
        cssNameMap.put(Color.MINTCREAM, "mintcream");
        cssNameMap.put(Color.MISTYROSE, "mistyrose");
        cssNameMap.put(Color.MOCCASIN, "moccasin");
        cssNameMap.put(Color.NAVAJOWHITE, "navajowhite");
        cssNameMap.put(Color.NAVY, "navy");
        cssNameMap.put(Color.OLDLACE, "oldlace");
        cssNameMap.put(Color.OLIVE, "olive");
        cssNameMap.put(Color.OLIVEDRAB, "olivedrab");
        cssNameMap.put(Color.ORANGE, "orange");
        cssNameMap.put(Color.ORANGERED, "orangered");
        cssNameMap.put(Color.ORCHID, "orchid");
        cssNameMap.put(Color.PALEGOLDENROD, "palegoldenrod");
        cssNameMap.put(Color.PALEGREEN, "palegreen");
        cssNameMap.put(Color.PALETURQUOISE, "paleturquoise");
        cssNameMap.put(Color.PALEVIOLETRED, "palevioletred");
        cssNameMap.put(Color.PAPAYAWHIP, "papayawhip");
        cssNameMap.put(Color.PEACHPUFF, "peachpuff");
        cssNameMap.put(Color.PERU, "peru");
        cssNameMap.put(Color.PINK, "pink");
        cssNameMap.put(Color.PLUM, "plum");
        cssNameMap.put(Color.POWDERBLUE, "powderblue");
        cssNameMap.put(Color.PURPLE, "purple");
        cssNameMap.put(Color.RED, "red");
        cssNameMap.put(Color.ROSYBROWN, "rosybrown");
        cssNameMap.put(Color.ROYALBLUE, "royalblue");
        cssNameMap.put(Color.SADDLEBROWN, "saddlebrown");
        cssNameMap.put(Color.SALMON, "salmon");
        cssNameMap.put(Color.SANDYBROWN, "sandybrown");
        cssNameMap.put(Color.SEAGREEN, "seagreen");
        cssNameMap.put(Color.SEASHELL, "seashell");
        cssNameMap.put(Color.SIENNA, "sienna");
        cssNameMap.put(Color.SILVER, "silver");
        cssNameMap.put(Color.SKYBLUE, "skyblue");
        cssNameMap.put(Color.SLATEBLUE, "slateblue");
        cssNameMap.put(Color.SLATEGRAY, "slategray");
        cssNameMap.put(Color.SNOW, "snow");
        cssNameMap.put(Color.SPRINGGREEN, "springgreen");
        cssNameMap.put(Color.STEELBLUE, "steelblue");
        cssNameMap.put(Color.TAN, "tan");
        cssNameMap.put(Color.TEAL, "teal");
        cssNameMap.put(Color.THISTLE, "thistle");
        cssNameMap.put(Color.TOMATO, "tomato");
        cssNameMap.put(Color.TRANSPARENT, "transparent");
        cssNameMap.put(Color.TURQUOISE, "turquoise");
        cssNameMap.put(Color.VIOLET, "violet");
        cssNameMap.put(Color.WHEAT, "wheat");
        cssNameMap.put(Color.WHITE, "white");
        cssNameMap.put(Color.WHITESMOKE, "whitesmoke");
        cssNameMap.put(Color.YELLOW, "yellow");
        cssNameMap.put(Color.YELLOWGREEN, "yellowgreen");
    }

    static String colorDisplayName(Color c2) {
        if (c2 != null) {
            String displayName = colorNameMap.get(c2);
            if (displayName == null) {
                displayName = formatHexString(c2);
            }
            return displayName;
        }
        return null;
    }

    static String tooltipString(Color c2) {
        if (c2 != null) {
            String tooltipStr = "";
            String displayName = colorNameMap.get(c2);
            if (displayName != null) {
                tooltipStr = tooltipStr + displayName + " ";
            }
            String tooltipStr2 = tooltipStr + formatHexString(c2);
            String cssName = cssNameMap.get(c2);
            if (cssName != null) {
                tooltipStr2 = tooltipStr2 + " (css: " + cssName + ")";
            }
            return tooltipStr2;
        }
        return null;
    }

    static String formatHexString(Color c2) {
        if (c2 != null) {
            return String.format((Locale) null, "#%02x%02x%02x", Long.valueOf(Math.round(c2.getRed() * 255.0d)), Long.valueOf(Math.round(c2.getGreen() * 255.0d)), Long.valueOf(Math.round(c2.getBlue() * 255.0d)));
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    protected Node getPopupContent() {
        if (this.popupContent == null) {
            this.popupContent = new ColorPalette((ColorPicker) getSkinnable());
            this.popupContent.setPopupControl(getPopup());
        }
        return this.popupContent;
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    protected void focusLost() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl, com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    public void show() {
        super.show();
        ColorPicker colorPicker = (ColorPicker) getSkinnable();
        this.popupContent.updateSelection(colorPicker.getValue());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("SHOWING".equals(p2)) {
            if (((ComboBoxBase) getSkinnable()).isShowing()) {
                show();
                return;
            } else {
                if (!this.popupContent.isCustomColorDialogShowing()) {
                    hide();
                    return;
                }
                return;
            }
        }
        if ("VALUE".equals(p2)) {
            updateColor();
            if (this.popupContent != null) {
            }
        }
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    public Node getDisplayNode() {
        return this.displayNode;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateColor() {
        ColorPicker colorPicker = (ColorPicker) getSkinnable();
        this.colorRect.setFill(colorPicker.getValue());
        if (this.colorLabelVisible.get()) {
            this.displayNode.setText(colorDisplayName(colorPicker.getValue()));
        } else {
            this.displayNode.setText("");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void syncWithAutoUpdate() {
        if (!getPopup().isShowing() && ((ComboBoxBase) getSkinnable()).isShowing()) {
            ((ComboBoxBase) getSkinnable()).hide();
        }
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        updateComboBoxMode();
        super.layoutChildren(x2, y2, w2, h2);
    }

    static String getString(String key) {
        return ControlResources.getString("ColorPicker." + key);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ColorPickerSkin$PickerColorBox.class */
    private class PickerColorBox extends StackPane {
        private PickerColorBox() {
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double top = snappedTopInset();
            double left = snappedLeftInset();
            double width = getWidth();
            double height = getHeight();
            double right = snappedRightInset();
            double bottom = snappedBottomInset();
            ColorPickerSkin.this.colorRect.setX(snapPosition(ColorPickerSkin.this.colorRectX.get()));
            ColorPickerSkin.this.colorRect.setY(snapPosition(ColorPickerSkin.this.colorRectY.get()));
            ColorPickerSkin.this.colorRect.setWidth(snapSize(ColorPickerSkin.this.colorRectWidth.get()));
            ColorPickerSkin.this.colorRect.setHeight(snapSize(ColorPickerSkin.this.colorRectHeight.get()));
            if (getChildren().size() != 2) {
                Pos childAlignment = StackPane.getAlignment(ColorPickerSkin.this.colorRect);
                layoutInArea(ColorPickerSkin.this.colorRect, left, top, (width - left) - right, (height - top) - bottom, 0.0d, getMargin(ColorPickerSkin.this.colorRect), childAlignment != null ? childAlignment.getHpos() : getAlignment().getHpos(), childAlignment != null ? childAlignment.getVpos() : getAlignment().getVpos());
                return;
            }
            ImageView icon = (ImageView) getChildren().get(1);
            Pos childAlignment2 = StackPane.getAlignment(icon);
            layoutInArea(icon, left, top, (width - left) - right, (height - top) - bottom, 0.0d, getMargin(icon), childAlignment2 != null ? childAlignment2.getHpos() : getAlignment().getHpos(), childAlignment2 != null ? childAlignment2.getVpos() : getAlignment().getVpos());
            ColorPickerSkin.this.colorRect.setLayoutX(icon.getLayoutX());
            ColorPickerSkin.this.colorRect.setLayoutY(icon.getLayoutY());
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ColorPickerSkin$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ColorPicker, Boolean> COLOR_LABEL_VISIBLE = new CssMetaData<ColorPicker, Boolean>("-fx-color-label-visible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return skin.colorLabelVisible == null || !skin.colorLabelVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return (StyleableProperty) skin.colorLabelVisible;
            }
        };
        private static final CssMetaData<ColorPicker, Number> COLOR_RECT_WIDTH = new CssMetaData<ColorPicker, Number>("-fx-color-rect-width", SizeConverter.getInstance(), Double.valueOf(12.0d)) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return !skin.colorRectWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return skin.colorRectWidth;
            }
        };
        private static final CssMetaData<ColorPicker, Number> COLOR_RECT_HEIGHT = new CssMetaData<ColorPicker, Number>("-fx-color-rect-height", SizeConverter.getInstance(), Double.valueOf(12.0d)) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return !skin.colorRectHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return skin.colorRectHeight;
            }
        };
        private static final CssMetaData<ColorPicker, Number> COLOR_RECT_X = new CssMetaData<ColorPicker, Number>("-fx-color-rect-x", SizeConverter.getInstance(), 0) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return !skin.colorRectX.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return skin.colorRectX;
            }
        };
        private static final CssMetaData<ColorPicker, Number> COLOR_RECT_Y = new CssMetaData<ColorPicker, Number>("-fx-color-rect-y", SizeConverter.getInstance(), 0) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return !skin.colorRectY.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return skin.colorRectY;
            }
        };
        private static final CssMetaData<ColorPicker, String> GRAPHIC = new CssMetaData<ColorPicker, String>("-fx-graphic", StringConverter.getInstance()) { // from class: com.sun.javafx.scene.control.skin.ColorPickerSkin.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return !skin.imageUrl.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<String> getStyleableProperty(ColorPicker n2) {
                ColorPickerSkin skin = (ColorPickerSkin) n2.getSkin();
                return skin.imageUrl;
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(ComboBoxBaseSkin.getClassCssMetaData());
            styleables.add(COLOR_LABEL_VISIBLE);
            styleables.add(COLOR_RECT_WIDTH);
            styleables.add(COLOR_RECT_HEIGHT);
            styleables.add(COLOR_RECT_X);
            styleables.add(COLOR_RECT_Y);
            styleables.add(GRAPHIC);
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

    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    protected javafx.util.StringConverter<Color> getConverter() {
        return null;
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    protected TextField getEditor() {
        return null;
    }
}
