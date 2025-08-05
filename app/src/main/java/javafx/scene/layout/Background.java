package javafx.scene.layout;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.css.SubCssMetaData;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/layout/Background.class */
public final class Background {
    static final CssMetaData<Node, Paint[]> BACKGROUND_COLOR = new SubCssMetaData("-fx-background-color", PaintConverter.SequenceConverter.getInstance(), new Paint[]{Color.TRANSPARENT});
    static final CssMetaData<Node, CornerRadii[]> BACKGROUND_RADIUS = new SubCssMetaData("-fx-background-radius", CornerRadiiConverter.getInstance(), new CornerRadii[]{CornerRadii.EMPTY});
    static final CssMetaData<Node, Insets[]> BACKGROUND_INSETS = new SubCssMetaData("-fx-background-insets", InsetsConverter.SequenceConverter.getInstance(), new Insets[]{Insets.EMPTY});
    static final CssMetaData<Node, Image[]> BACKGROUND_IMAGE = new SubCssMetaData("-fx-background-image", URLConverter.SequenceConverter.getInstance());
    static final CssMetaData<Node, RepeatStruct[]> BACKGROUND_REPEAT = new SubCssMetaData("-fx-background-repeat", RepeatStructConverter.getInstance(), new RepeatStruct[]{new RepeatStruct(BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT)});
    static final CssMetaData<Node, BackgroundPosition[]> BACKGROUND_POSITION = new SubCssMetaData("-fx-background-position", LayeredBackgroundPositionConverter.getInstance(), new BackgroundPosition[]{BackgroundPosition.DEFAULT});
    static final CssMetaData<Node, BackgroundSize[]> BACKGROUND_SIZE = new SubCssMetaData("-fx-background-size", LayeredBackgroundSizeConverter.getInstance(), new BackgroundSize[]{BackgroundSize.DEFAULT});
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES = Collections.unmodifiableList(Arrays.asList(BACKGROUND_COLOR, BACKGROUND_INSETS, BACKGROUND_RADIUS, BACKGROUND_IMAGE, BACKGROUND_REPEAT, BACKGROUND_POSITION, BACKGROUND_SIZE));
    public static final Background EMPTY = new Background((BackgroundFill[]) null, (BackgroundImage[]) null);
    final List<BackgroundFill> fills;
    final List<BackgroundImage> images;
    final Insets outsets;
    private final boolean hasOpaqueFill;
    private final double opaqueFillTop;
    private final double opaqueFillRight;
    private final double opaqueFillBottom;
    private final double opaqueFillLeft;
    final boolean hasPercentageBasedOpaqueFills;
    final boolean hasPercentageBasedFills;
    private final int hash;

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    public final List<BackgroundFill> getFills() {
        return this.fills;
    }

    public final List<BackgroundImage> getImages() {
        return this.images;
    }

    public final Insets getOutsets() {
        return this.outsets;
    }

    public final boolean isEmpty() {
        return this.fills.isEmpty() && this.images.isEmpty();
    }

    public Background(@NamedArg("fills") BackgroundFill... fills) {
        this(fills, (BackgroundImage[]) null);
    }

    public Background(@NamedArg("images") BackgroundImage... images) {
        this((BackgroundFill[]) null, images);
    }

    public Background(@NamedArg("fills") List<BackgroundFill> fills, @NamedArg("images") List<BackgroundImage> images) {
        this(fills == null ? null : (BackgroundFill[]) fills.toArray(new BackgroundFill[fills.size()]), images == null ? null : (BackgroundImage[]) images.toArray(new BackgroundImage[images.size()]));
    }

    public Background(@NamedArg("fills") BackgroundFill[] fills, @NamedArg("images") BackgroundImage[] images) {
        double outerTop = 0.0d;
        double outerRight = 0.0d;
        double outerBottom = 0.0d;
        double outerLeft = 0.0d;
        boolean hasPercentOpaqueInsets = false;
        boolean hasPercentFillRadii = false;
        boolean opaqueFill = false;
        if (fills == null || fills.length == 0) {
            this.fills = Collections.emptyList();
        } else {
            BackgroundFill[] noNulls = new BackgroundFill[fills.length];
            int size = 0;
            for (BackgroundFill fill : fills) {
                if (fill != null) {
                    int i2 = size;
                    size++;
                    noNulls[i2] = fill;
                    Insets fillInsets = fill.getInsets();
                    double fillTop = fillInsets.getTop();
                    double fillRight = fillInsets.getRight();
                    double fillBottom = fillInsets.getBottom();
                    double fillLeft = fillInsets.getLeft();
                    outerTop = outerTop <= fillTop ? outerTop : fillTop;
                    outerRight = outerRight <= fillRight ? outerRight : fillRight;
                    outerBottom = outerBottom <= fillBottom ? outerBottom : fillBottom;
                    outerLeft = outerLeft <= fillLeft ? outerLeft : fillLeft;
                    boolean b2 = fill.getRadii().hasPercentBasedRadii;
                    hasPercentFillRadii |= b2;
                    if (fill.fill.isOpaque()) {
                        opaqueFill = true;
                        if (b2) {
                            hasPercentOpaqueInsets = true;
                        }
                    }
                }
            }
            this.fills = new UnmodifiableArrayList(noNulls, size);
        }
        this.hasPercentageBasedFills = hasPercentFillRadii;
        this.outsets = new Insets(Math.max(0.0d, -outerTop), Math.max(0.0d, -outerRight), Math.max(0.0d, -outerBottom), Math.max(0.0d, -outerLeft));
        if (images == null || images.length == 0) {
            this.images = Collections.emptyList();
        } else {
            BackgroundImage[] noNulls2 = new BackgroundImage[images.length];
            int size2 = 0;
            for (BackgroundImage image : images) {
                if (image != null) {
                    int i3 = size2;
                    size2++;
                    noNulls2[i3] = image;
                }
            }
            this.images = new UnmodifiableArrayList(noNulls2, size2);
        }
        this.hasOpaqueFill = opaqueFill;
        if (hasPercentOpaqueInsets) {
            this.opaqueFillTop = Double.NaN;
            this.opaqueFillRight = Double.NaN;
            this.opaqueFillBottom = Double.NaN;
            this.opaqueFillLeft = Double.NaN;
        } else {
            double[] trbl = new double[4];
            computeOpaqueInsets(1.0d, 1.0d, true, trbl);
            this.opaqueFillTop = trbl[0];
            this.opaqueFillRight = trbl[1];
            this.opaqueFillBottom = trbl[2];
            this.opaqueFillLeft = trbl[3];
        }
        this.hasPercentageBasedOpaqueFills = hasPercentOpaqueInsets;
        int result = this.fills.hashCode();
        this.hash = (31 * result) + this.images.hashCode();
    }

    public boolean isFillPercentageBased() {
        return this.hasPercentageBasedFills;
    }

    void computeOpaqueInsets(double width, double height, double[] trbl) {
        computeOpaqueInsets(width, height, false, trbl);
    }

    private void computeOpaqueInsets(double width, double height, boolean firstTime, double[] trbl) {
        double tileWidth;
        double tileHeight;
        double opaqueRegionTop = Double.NaN;
        double opaqueRegionRight = Double.NaN;
        double opaqueRegionBottom = Double.NaN;
        double opaqueRegionLeft = Double.NaN;
        if (this.hasOpaqueFill) {
            if (!firstTime && !this.hasPercentageBasedOpaqueFills) {
                opaqueRegionTop = this.opaqueFillTop;
                opaqueRegionRight = this.opaqueFillRight;
                opaqueRegionBottom = this.opaqueFillBottom;
                opaqueRegionLeft = this.opaqueFillLeft;
            } else {
                int max = this.fills.size();
                for (int i2 = 0; i2 < max; i2++) {
                    BackgroundFill fill = this.fills.get(i2);
                    Insets fillInsets = fill.getInsets();
                    double fillTop = fillInsets.getTop();
                    double fillRight = fillInsets.getRight();
                    double fillBottom = fillInsets.getBottom();
                    double fillLeft = fillInsets.getLeft();
                    if (fill.fill.isOpaque()) {
                        CornerRadii radii = fill.getRadii();
                        double topLeftHorizontalRadius = radii.isTopLeftHorizontalRadiusAsPercentage() ? width * radii.getTopLeftHorizontalRadius() : radii.getTopLeftHorizontalRadius();
                        double topLeftVerticalRadius = radii.isTopLeftVerticalRadiusAsPercentage() ? height * radii.getTopLeftVerticalRadius() : radii.getTopLeftVerticalRadius();
                        double topRightVerticalRadius = radii.isTopRightVerticalRadiusAsPercentage() ? height * radii.getTopRightVerticalRadius() : radii.getTopRightVerticalRadius();
                        double topRightHorizontalRadius = radii.isTopRightHorizontalRadiusAsPercentage() ? width * radii.getTopRightHorizontalRadius() : radii.getTopRightHorizontalRadius();
                        double bottomRightHorizontalRadius = radii.isBottomRightHorizontalRadiusAsPercentage() ? width * radii.getBottomRightHorizontalRadius() : radii.getBottomRightHorizontalRadius();
                        double bottomRightVerticalRadius = radii.isBottomRightVerticalRadiusAsPercentage() ? height * radii.getBottomRightVerticalRadius() : radii.getBottomRightVerticalRadius();
                        double bottomLeftVerticalRadius = radii.isBottomLeftVerticalRadiusAsPercentage() ? height * radii.getBottomLeftVerticalRadius() : radii.getBottomLeftVerticalRadius();
                        double bottomLeftHorizontalRadius = radii.isBottomLeftHorizontalRadiusAsPercentage() ? width * radii.getBottomLeftHorizontalRadius() : radii.getBottomLeftHorizontalRadius();
                        double t2 = fillTop + (Math.max(topLeftVerticalRadius, topRightVerticalRadius) / 2.0d);
                        double r2 = fillRight + (Math.max(topRightHorizontalRadius, bottomRightHorizontalRadius) / 2.0d);
                        double b2 = fillBottom + (Math.max(bottomLeftVerticalRadius, bottomRightVerticalRadius) / 2.0d);
                        double l2 = fillLeft + (Math.max(topLeftHorizontalRadius, bottomLeftHorizontalRadius) / 2.0d);
                        if (Double.isNaN(opaqueRegionTop)) {
                            opaqueRegionTop = t2;
                            opaqueRegionRight = r2;
                            opaqueRegionBottom = b2;
                            opaqueRegionLeft = l2;
                        } else {
                            boolean largerTop = t2 >= opaqueRegionTop;
                            boolean largerRight = r2 >= opaqueRegionRight;
                            boolean largerBottom = b2 >= opaqueRegionBottom;
                            boolean largerLeft = l2 >= opaqueRegionLeft;
                            if (!largerTop || !largerRight || !largerBottom || !largerLeft) {
                                if (!largerTop && !largerRight && !largerBottom && !largerLeft) {
                                    opaqueRegionTop = fillTop;
                                    opaqueRegionRight = fillRight;
                                    opaqueRegionBottom = fillBottom;
                                    opaqueRegionLeft = fillLeft;
                                } else if (l2 == opaqueRegionLeft && r2 == opaqueRegionRight) {
                                    opaqueRegionTop = Math.min(t2, opaqueRegionTop);
                                    opaqueRegionBottom = Math.min(b2, opaqueRegionBottom);
                                } else if (t2 == opaqueRegionTop && b2 == opaqueRegionBottom) {
                                    opaqueRegionLeft = Math.min(l2, opaqueRegionLeft);
                                    opaqueRegionRight = Math.min(r2, opaqueRegionRight);
                                }
                            }
                        }
                    }
                }
            }
        }
        Toolkit.ImageAccessor acc = Toolkit.getImageAccessor();
        Iterator<BackgroundImage> it = this.images.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            BackgroundImage bi2 = it.next();
            if (bi2.opaque == null) {
                PlatformImage platformImage = acc.getImageProperty(bi2.image).get();
                if (platformImage != null && (platformImage instanceof com.sun.prism.Image)) {
                    bi2.opaque = Boolean.valueOf(((com.sun.prism.Image) platformImage).isOpaque());
                }
            }
            if (!bi2.opaque.booleanValue()) {
                continue;
            } else {
                if (bi2.size.cover || (bi2.size.height == -1.0d && bi2.size.width == -1.0d && bi2.size.widthAsPercentage && bi2.size.heightAsPercentage)) {
                    break;
                }
                if (bi2.repeatX == BackgroundRepeat.SPACE || bi2.repeatY == BackgroundRepeat.SPACE) {
                    bi2.opaque = false;
                } else {
                    boolean filledX = bi2.repeatX == BackgroundRepeat.REPEAT || bi2.repeatX == BackgroundRepeat.ROUND;
                    boolean filledY = bi2.repeatY == BackgroundRepeat.REPEAT || bi2.repeatY == BackgroundRepeat.ROUND;
                    if (filledX && filledY) {
                        opaqueRegionTop = Double.isNaN(opaqueRegionTop) ? 0.0d : Math.min(0.0d, opaqueRegionTop);
                        opaqueRegionRight = Double.isNaN(opaqueRegionRight) ? 0.0d : Math.min(0.0d, opaqueRegionRight);
                        opaqueRegionBottom = Double.isNaN(opaqueRegionBottom) ? 0.0d : Math.min(0.0d, opaqueRegionBottom);
                        opaqueRegionLeft = Double.isNaN(opaqueRegionLeft) ? 0.0d : Math.min(0.0d, opaqueRegionLeft);
                    } else {
                        double w2 = bi2.size.widthAsPercentage ? bi2.size.width * width : bi2.size.width;
                        double h2 = bi2.size.heightAsPercentage ? bi2.size.height * height : bi2.size.height;
                        double imgUnscaledWidth = bi2.image.getWidth();
                        double imgUnscaledHeight = bi2.image.getHeight();
                        if (bi2.size.contain) {
                            double scaleX = width / imgUnscaledWidth;
                            double scaleY = height / imgUnscaledHeight;
                            double scale = Math.min(scaleX, scaleY);
                            tileWidth = Math.ceil(scale * imgUnscaledWidth);
                            tileHeight = Math.ceil(scale * imgUnscaledHeight);
                        } else if (bi2.size.width >= 0.0d && bi2.size.height >= 0.0d) {
                            tileWidth = w2;
                            tileHeight = h2;
                        } else if (w2 >= 0.0d) {
                            tileWidth = w2;
                            double scale2 = tileWidth / imgUnscaledWidth;
                            tileHeight = imgUnscaledHeight * scale2;
                        } else if (h2 >= 0.0d) {
                            tileHeight = h2;
                            double scale3 = tileHeight / imgUnscaledHeight;
                            tileWidth = imgUnscaledWidth * scale3;
                        } else {
                            tileWidth = imgUnscaledWidth;
                            tileHeight = imgUnscaledHeight;
                        }
                        opaqueRegionTop = Double.isNaN(opaqueRegionTop) ? 0.0d : Math.min(0.0d, opaqueRegionTop);
                        opaqueRegionRight = Double.isNaN(opaqueRegionRight) ? width - tileWidth : Math.min(width - tileWidth, opaqueRegionRight);
                        opaqueRegionBottom = Double.isNaN(opaqueRegionBottom) ? height - tileHeight : Math.min(height - tileHeight, opaqueRegionBottom);
                        opaqueRegionLeft = Double.isNaN(opaqueRegionLeft) ? 0.0d : Math.min(0.0d, opaqueRegionLeft);
                    }
                }
            }
        }
        opaqueRegionTop = Double.isNaN(opaqueRegionTop) ? 0.0d : Math.min(0.0d, opaqueRegionTop);
        opaqueRegionRight = Double.isNaN(opaqueRegionRight) ? 0.0d : Math.min(0.0d, opaqueRegionRight);
        opaqueRegionBottom = Double.isNaN(opaqueRegionBottom) ? 0.0d : Math.min(0.0d, opaqueRegionBottom);
        opaqueRegionLeft = Double.isNaN(opaqueRegionLeft) ? 0.0d : Math.min(0.0d, opaqueRegionLeft);
        trbl[0] = opaqueRegionTop;
        trbl[1] = opaqueRegionRight;
        trbl[2] = opaqueRegionBottom;
        trbl[3] = opaqueRegionLeft;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        Background that = (Background) o2;
        return this.hash == that.hash && this.fills.equals(that.fills) && this.images.equals(that.images);
    }

    public int hashCode() {
        return this.hash;
    }
}
