package javafx.scene.layout;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import java.util.Map;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/layout/BorderConverter.class */
class BorderConverter extends StyleConverterImpl<ParsedValue[], Border> {
    private static final BorderConverter BORDER_IMAGE_CONVERTER = new BorderConverter();

    @Override // com.sun.javafx.css.StyleConverterImpl
    public /* bridge */ /* synthetic */ Border convert(Map map) {
        return convert((Map<CssMetaData<? extends Styleable, ?>, Object>) map);
    }

    public static BorderConverter getInstance() {
        return BORDER_IMAGE_CONVERTER;
    }

    private BorderConverter() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.javafx.css.StyleConverterImpl
    public Border convert(Map<CssMetaData<? extends Styleable, ?>, Object> convertedValues) {
        BorderImageSlices borderImageSlices;
        Insets insets;
        BorderWidths borderWidths;
        BorderStrokeStyle[] styles;
        Paint[] strokes;
        Margins margins;
        CornerRadii cornerRadii;
        Insets insets2;
        Paint[][] strokeFills = (Paint[][]) convertedValues.get(Border.BORDER_COLOR);
        BorderStrokeStyle[][] strokeStyles = (BorderStrokeStyle[][]) convertedValues.get(Border.BORDER_STYLE);
        String[] imageUrls = (String[]) convertedValues.get(Border.BORDER_IMAGE_SOURCE);
        boolean hasStrokes = (strokeFills != null && strokeFills.length > 0) || (strokeStyles != null && strokeStyles.length > 0);
        boolean hasImages = imageUrls != null && imageUrls.length > 0;
        if (!hasStrokes && !hasImages) {
            return null;
        }
        BorderStroke[] borderStrokes = null;
        if (hasStrokes) {
            int lastStrokeFill = strokeFills != null ? strokeFills.length - 1 : -1;
            int lastStrokeStyle = strokeStyles != null ? strokeStyles.length - 1 : -1;
            int nLayers = (lastStrokeFill >= lastStrokeStyle ? lastStrokeFill : lastStrokeStyle) + 1;
            Object tmp = convertedValues.get(Border.BORDER_WIDTH);
            Margins[] borderWidths2 = tmp == null ? new Margins[0] : (Margins[]) tmp;
            int lastMarginIndex = borderWidths2.length - 1;
            Object tmp2 = convertedValues.get(Border.BORDER_RADIUS);
            CornerRadii[] borderRadii = tmp2 == null ? new CornerRadii[0] : (CornerRadii[]) tmp2;
            int lastRadiusIndex = borderRadii.length - 1;
            Object tmp3 = convertedValues.get(Border.BORDER_INSETS);
            Insets[] borderInsets = tmp3 == null ? new Insets[0] : (Insets[]) tmp3;
            int lastInsetsIndex = borderInsets.length - 1;
            int i2 = 0;
            while (i2 < nLayers) {
                if (lastStrokeStyle < 0) {
                    BorderStrokeStyle borderStrokeStyle = BorderStrokeStyle.SOLID;
                    styles = new BorderStrokeStyle[]{borderStrokeStyle, borderStrokeStyle, borderStrokeStyle, borderStrokeStyle};
                } else {
                    styles = strokeStyles[i2 <= lastStrokeStyle ? i2 : lastStrokeStyle];
                }
                if (styles[0] != BorderStrokeStyle.NONE || styles[1] != BorderStrokeStyle.NONE || styles[2] != BorderStrokeStyle.NONE || styles[3] != BorderStrokeStyle.NONE) {
                    if (lastStrokeFill < 0) {
                        Color color = Color.BLACK;
                        strokes = new Paint[]{color, color, color, color};
                    } else {
                        strokes = strokeFills[i2 <= lastStrokeFill ? i2 : lastStrokeFill];
                    }
                    if (borderStrokes == null) {
                        borderStrokes = new BorderStroke[nLayers];
                    }
                    if (borderWidths2.length == 0) {
                        margins = null;
                    } else {
                        margins = borderWidths2[i2 <= lastMarginIndex ? i2 : lastMarginIndex];
                    }
                    Margins margins2 = margins;
                    if (borderRadii.length == 0) {
                        cornerRadii = CornerRadii.EMPTY;
                    } else {
                        cornerRadii = borderRadii[i2 <= lastRadiusIndex ? i2 : lastRadiusIndex];
                    }
                    CornerRadii radii = cornerRadii;
                    if (borderInsets.length == 0) {
                        insets2 = null;
                    } else {
                        insets2 = borderInsets[i2 <= lastInsetsIndex ? i2 : lastInsetsIndex];
                    }
                    borderStrokes[i2] = new BorderStroke(strokes[0], strokes[1], strokes[2], strokes[3], styles[0], styles[1], styles[2], styles[3], radii, margins2 == null ? BorderStroke.DEFAULT_WIDTHS : new BorderWidths(margins2.getTop(), margins2.getRight(), margins2.getBottom(), margins2.getLeft()), insets2);
                }
                i2++;
            }
        }
        BorderImage[] borderImages = null;
        if (hasImages) {
            borderImages = new BorderImage[imageUrls.length];
            Object tmp4 = convertedValues.get(Border.BORDER_IMAGE_REPEAT);
            RepeatStruct[] repeats = tmp4 == null ? new RepeatStruct[0] : (RepeatStruct[]) tmp4;
            int lastRepeatIndex = repeats.length - 1;
            Object tmp5 = convertedValues.get(Border.BORDER_IMAGE_SLICE);
            BorderImageSlices[] slices = tmp5 == null ? new BorderImageSlices[0] : (BorderImageSlices[]) tmp5;
            int lastSlicesIndex = slices.length - 1;
            Object tmp6 = convertedValues.get(Border.BORDER_IMAGE_WIDTH);
            BorderWidths[] widths = tmp6 == null ? new BorderWidths[0] : (BorderWidths[]) tmp6;
            int lastWidthsIndex = widths.length - 1;
            Object tmp7 = convertedValues.get(Border.BORDER_IMAGE_INSETS);
            Insets[] insets3 = tmp7 == null ? new Insets[0] : (Insets[]) tmp7;
            int lastInsetsIndex2 = insets3.length - 1;
            int i3 = 0;
            while (i3 < imageUrls.length) {
                if (imageUrls[i3] != null) {
                    BorderRepeat repeatX = BorderRepeat.STRETCH;
                    BorderRepeat repeatY = BorderRepeat.STRETCH;
                    if (repeats.length > 0) {
                        RepeatStruct repeat = repeats[i3 <= lastRepeatIndex ? i3 : lastRepeatIndex];
                        switch (repeat.repeatX) {
                            case SPACE:
                                repeatX = BorderRepeat.SPACE;
                                break;
                            case ROUND:
                                repeatX = BorderRepeat.ROUND;
                                break;
                            case REPEAT:
                                repeatX = BorderRepeat.REPEAT;
                                break;
                            case NO_REPEAT:
                                repeatX = BorderRepeat.STRETCH;
                                break;
                        }
                        switch (repeat.repeatY) {
                            case SPACE:
                                repeatY = BorderRepeat.SPACE;
                                break;
                            case ROUND:
                                repeatY = BorderRepeat.ROUND;
                                break;
                            case REPEAT:
                                repeatY = BorderRepeat.REPEAT;
                                break;
                            case NO_REPEAT:
                                repeatY = BorderRepeat.STRETCH;
                                break;
                        }
                    }
                    if (slices.length > 0) {
                        borderImageSlices = slices[i3 <= lastSlicesIndex ? i3 : lastSlicesIndex];
                    } else {
                        borderImageSlices = BorderImageSlices.DEFAULT;
                    }
                    BorderImageSlices slice = borderImageSlices;
                    if (insets3.length > 0) {
                        insets = insets3[i3 <= lastInsetsIndex2 ? i3 : lastInsetsIndex2];
                    } else {
                        insets = Insets.EMPTY;
                    }
                    Insets inset = insets;
                    if (widths.length > 0) {
                        borderWidths = widths[i3 <= lastWidthsIndex ? i3 : lastWidthsIndex];
                    } else {
                        borderWidths = BorderWidths.DEFAULT;
                    }
                    BorderWidths width = borderWidths;
                    Image img = StyleManager.getInstance().getCachedImage(imageUrls[i3]);
                    borderImages[i3] = new BorderImage(img, width, inset, slice.widths, slice.filled, repeatX, repeatY);
                }
                i3++;
            }
        }
        if (borderStrokes == null && borderImages == null) {
            return null;
        }
        return new Border(borderStrokes, borderImages);
    }

    public String toString() {
        return "BorderConverter";
    }
}
