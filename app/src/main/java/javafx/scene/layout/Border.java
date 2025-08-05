package javafx.scene.layout;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.css.SubCssMetaData;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/layout/Border.class */
public final class Border {
    static final CssMetaData<Node, Paint[]> BORDER_COLOR = new SubCssMetaData("-fx-border-color", LayeredBorderPaintConverter.getInstance());
    static final CssMetaData<Node, BorderStrokeStyle[][]> BORDER_STYLE = new SubCssMetaData("-fx-border-style", LayeredBorderStyleConverter.getInstance());
    static final CssMetaData<Node, Margins[]> BORDER_WIDTH = new SubCssMetaData("-fx-border-width", Margins.SequenceConverter.getInstance());
    static final CssMetaData<Node, CornerRadii[]> BORDER_RADIUS = new SubCssMetaData("-fx-border-radius", CornerRadiiConverter.getInstance());
    static final CssMetaData<Node, Insets[]> BORDER_INSETS = new SubCssMetaData("-fx-border-insets", InsetsConverter.SequenceConverter.getInstance());
    static final CssMetaData<Node, String[]> BORDER_IMAGE_SOURCE = new SubCssMetaData("-fx-border-image-source", URLConverter.SequenceConverter.getInstance());
    static final CssMetaData<Node, RepeatStruct[]> BORDER_IMAGE_REPEAT = new SubCssMetaData("-fx-border-image-repeat", RepeatStructConverter.getInstance(), new RepeatStruct[]{new RepeatStruct(BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT)});
    static final CssMetaData<Node, BorderImageSlices[]> BORDER_IMAGE_SLICE = new SubCssMetaData("-fx-border-image-slice", SliceSequenceConverter.getInstance(), new BorderImageSlices[]{BorderImageSlices.DEFAULT});
    static final CssMetaData<Node, BorderWidths[]> BORDER_IMAGE_WIDTH = new SubCssMetaData("-fx-border-image-width", BorderImageWidthConverter.getInstance(), new BorderWidths[]{BorderWidths.DEFAULT});
    static final CssMetaData<Node, Insets[]> BORDER_IMAGE_INSETS = new SubCssMetaData("-fx-border-image-insets", InsetsConverter.SequenceConverter.getInstance(), new Insets[]{Insets.EMPTY});
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES = Collections.unmodifiableList(Arrays.asList(BORDER_COLOR, BORDER_STYLE, BORDER_WIDTH, BORDER_RADIUS, BORDER_INSETS, BORDER_IMAGE_SOURCE, BORDER_IMAGE_REPEAT, BORDER_IMAGE_SLICE, BORDER_IMAGE_WIDTH, BORDER_IMAGE_INSETS));
    public static final Border EMPTY = new Border((BorderStroke[]) null, (BorderImage[]) null);
    final List<BorderStroke> strokes;
    final List<BorderImage> images;
    final Insets outsets;
    final Insets insets;
    private final int hash;

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    public final List<BorderStroke> getStrokes() {
        return this.strokes;
    }

    public final List<BorderImage> getImages() {
        return this.images;
    }

    public final Insets getOutsets() {
        return this.outsets;
    }

    public final Insets getInsets() {
        return this.insets;
    }

    public final boolean isEmpty() {
        return this.strokes.isEmpty() && this.images.isEmpty();
    }

    public Border(@NamedArg("strokes") BorderStroke... strokes) {
        this(strokes, (BorderImage[]) null);
    }

    public Border(@NamedArg("images") BorderImage... images) {
        this((BorderStroke[]) null, images);
    }

    public Border(@NamedArg("strokes") List<BorderStroke> strokes, @NamedArg("images") List<BorderImage> images) {
        this(strokes == null ? null : (BorderStroke[]) strokes.toArray(new BorderStroke[strokes.size()]), images == null ? null : (BorderImage[]) images.toArray(new BorderImage[images.size()]));
    }

    public Border(@NamedArg("strokes") BorderStroke[] strokes, @NamedArg("images") BorderImage[] images) {
        double innerTop = 0.0d;
        double innerRight = 0.0d;
        double innerBottom = 0.0d;
        double innerLeft = 0.0d;
        double outerTop = 0.0d;
        double outerRight = 0.0d;
        double outerBottom = 0.0d;
        double outerLeft = 0.0d;
        if (strokes == null || strokes.length == 0) {
            this.strokes = Collections.emptyList();
        } else {
            BorderStroke[] noNulls = new BorderStroke[strokes.length];
            int size = 0;
            for (BorderStroke stroke : strokes) {
                if (stroke != null) {
                    int i2 = size;
                    size++;
                    noNulls[i2] = stroke;
                    double strokeInnerTop = stroke.innerEdge.getTop();
                    double strokeInnerRight = stroke.innerEdge.getRight();
                    double strokeInnerBottom = stroke.innerEdge.getBottom();
                    double strokeInnerLeft = stroke.innerEdge.getLeft();
                    innerTop = innerTop >= strokeInnerTop ? innerTop : strokeInnerTop;
                    innerRight = innerRight >= strokeInnerRight ? innerRight : strokeInnerRight;
                    innerBottom = innerBottom >= strokeInnerBottom ? innerBottom : strokeInnerBottom;
                    innerLeft = innerLeft >= strokeInnerLeft ? innerLeft : strokeInnerLeft;
                    double strokeOuterTop = stroke.outerEdge.getTop();
                    double strokeOuterRight = stroke.outerEdge.getRight();
                    double strokeOuterBottom = stroke.outerEdge.getBottom();
                    double strokeOuterLeft = stroke.outerEdge.getLeft();
                    outerTop = outerTop >= strokeOuterTop ? outerTop : strokeOuterTop;
                    outerRight = outerRight >= strokeOuterRight ? outerRight : strokeOuterRight;
                    outerBottom = outerBottom >= strokeOuterBottom ? outerBottom : strokeOuterBottom;
                    outerLeft = outerLeft >= strokeOuterLeft ? outerLeft : strokeOuterLeft;
                }
            }
            this.strokes = new UnmodifiableArrayList(noNulls, size);
        }
        if (images == null || images.length == 0) {
            this.images = Collections.emptyList();
        } else {
            BorderImage[] noNulls2 = new BorderImage[images.length];
            int size2 = 0;
            for (BorderImage image : images) {
                if (image != null) {
                    int i3 = size2;
                    size2++;
                    noNulls2[i3] = image;
                    double imageInnerTop = image.innerEdge.getTop();
                    double imageInnerRight = image.innerEdge.getRight();
                    double imageInnerBottom = image.innerEdge.getBottom();
                    double imageInnerLeft = image.innerEdge.getLeft();
                    innerTop = innerTop >= imageInnerTop ? innerTop : imageInnerTop;
                    innerRight = innerRight >= imageInnerRight ? innerRight : imageInnerRight;
                    innerBottom = innerBottom >= imageInnerBottom ? innerBottom : imageInnerBottom;
                    innerLeft = innerLeft >= imageInnerLeft ? innerLeft : imageInnerLeft;
                    double imageOuterTop = image.outerEdge.getTop();
                    double imageOuterRight = image.outerEdge.getRight();
                    double imageOuterBottom = image.outerEdge.getBottom();
                    double imageOuterLeft = image.outerEdge.getLeft();
                    outerTop = outerTop >= imageOuterTop ? outerTop : imageOuterTop;
                    outerRight = outerRight >= imageOuterRight ? outerRight : imageOuterRight;
                    outerBottom = outerBottom >= imageOuterBottom ? outerBottom : imageOuterBottom;
                    outerLeft = outerLeft >= imageOuterLeft ? outerLeft : imageOuterLeft;
                }
            }
            this.images = new UnmodifiableArrayList(noNulls2, size2);
        }
        this.outsets = new Insets(outerTop, outerRight, outerBottom, outerLeft);
        this.insets = new Insets(innerTop, innerRight, innerBottom, innerLeft);
        int result = this.strokes.hashCode();
        this.hash = (31 * result) + this.images.hashCode();
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        Border border = (Border) o2;
        return this.hash == border.hash && this.images.equals(border.images) && this.strokes.equals(border.strokes);
    }

    public int hashCode() {
        return this.hash;
    }
}
