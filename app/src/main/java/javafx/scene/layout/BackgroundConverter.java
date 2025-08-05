package javafx.scene.layout;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import java.util.Map;
import javafx.css.CssMetaData;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/layout/BackgroundConverter.class */
class BackgroundConverter extends StyleConverterImpl<ParsedValue[], Background> {
    static final StyleConverter<ParsedValue[], Background> INSTANCE = new BackgroundConverter();

    BackgroundConverter() {
    }

    @Override // com.sun.javafx.css.StyleConverterImpl
    public /* bridge */ /* synthetic */ Background convert(Map map) {
        return convert((Map<CssMetaData<? extends Styleable, ?>, Object>) map);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.javafx.css.StyleConverterImpl
    public Background convert(Map<CssMetaData<? extends Styleable, ?>, Object> convertedValues) {
        Image image;
        RepeatStruct repeatStruct;
        BackgroundPosition backgroundPosition;
        BackgroundSize backgroundSize;
        Insets insets;
        CornerRadii cornerRadii;
        Paint[] fills = (Paint[]) convertedValues.get(Background.BACKGROUND_COLOR);
        String[] imageUrls = (String[]) convertedValues.get(Background.BACKGROUND_IMAGE);
        boolean hasFills = fills != null && fills.length > 0;
        boolean hasImages = imageUrls != null && imageUrls.length > 0;
        if (!hasFills && !hasImages) {
            return null;
        }
        BackgroundFill[] backgroundFills = null;
        if (hasFills) {
            backgroundFills = new BackgroundFill[fills.length];
            Object tmp = convertedValues.get(Background.BACKGROUND_INSETS);
            Insets[] insets2 = tmp == null ? new Insets[0] : (Insets[]) tmp;
            Object tmp2 = convertedValues.get(Background.BACKGROUND_RADIUS);
            CornerRadii[] radii = tmp2 == null ? new CornerRadii[0] : (CornerRadii[]) tmp2;
            int lastInsetsIndex = insets2.length - 1;
            int lastRadiiIndex = radii.length - 1;
            int i2 = 0;
            while (i2 < fills.length) {
                if (insets2.length > 0) {
                    insets = insets2[i2 <= lastInsetsIndex ? i2 : lastInsetsIndex];
                } else {
                    insets = Insets.EMPTY;
                }
                Insets in = insets;
                if (radii.length > 0) {
                    cornerRadii = radii[i2 <= lastRadiiIndex ? i2 : lastRadiiIndex];
                } else {
                    cornerRadii = CornerRadii.EMPTY;
                }
                CornerRadii ra = cornerRadii;
                backgroundFills[i2] = new BackgroundFill(fills[i2], ra, in);
                i2++;
            }
        }
        BackgroundImage[] backgroundImages = null;
        if (hasImages) {
            backgroundImages = new BackgroundImage[imageUrls.length];
            Object tmp3 = convertedValues.get(Background.BACKGROUND_REPEAT);
            RepeatStruct[] repeats = tmp3 == null ? new RepeatStruct[0] : (RepeatStruct[]) tmp3;
            Object tmp4 = convertedValues.get(Background.BACKGROUND_POSITION);
            BackgroundPosition[] positions = tmp4 == null ? new BackgroundPosition[0] : (BackgroundPosition[]) tmp4;
            Object tmp5 = convertedValues.get(Background.BACKGROUND_SIZE);
            BackgroundSize[] sizes = tmp5 == null ? new BackgroundSize[0] : (BackgroundSize[]) tmp5;
            int lastRepeatIndex = repeats.length - 1;
            int lastPositionIndex = positions.length - 1;
            int lastSizeIndex = sizes.length - 1;
            int i3 = 0;
            while (i3 < imageUrls.length) {
                if (imageUrls[i3] != null && (image = StyleManager.getInstance().getCachedImage(imageUrls[i3])) != null) {
                    if (repeats.length > 0) {
                        repeatStruct = repeats[i3 <= lastRepeatIndex ? i3 : lastRepeatIndex];
                    } else {
                        repeatStruct = null;
                    }
                    RepeatStruct repeat = repeatStruct;
                    if (positions.length > 0) {
                        backgroundPosition = positions[i3 <= lastPositionIndex ? i3 : lastPositionIndex];
                    } else {
                        backgroundPosition = null;
                    }
                    BackgroundPosition position = backgroundPosition;
                    if (sizes.length > 0) {
                        backgroundSize = sizes[i3 <= lastSizeIndex ? i3 : lastSizeIndex];
                    } else {
                        backgroundSize = null;
                    }
                    BackgroundSize size = backgroundSize;
                    backgroundImages[i3] = new BackgroundImage(image, repeat == null ? null : repeat.repeatX, repeat == null ? null : repeat.repeatY, position, size);
                }
                i3++;
            }
        }
        return new Background(backgroundFills, backgroundImages);
    }
}
