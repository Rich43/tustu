package javafx.scene.layout;

import com.sun.org.apache.xalan.internal.templates.Constants;
import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;

/* loaded from: jfxrt.jar:javafx/scene/layout/BorderStroke.class */
public class BorderStroke {
    public static final BorderWidths THIN = new BorderWidths(1.0d);
    public static final BorderWidths MEDIUM = new BorderWidths(3.0d);
    public static final BorderWidths THICK = new BorderWidths(5.0d);
    public static final BorderWidths DEFAULT_WIDTHS = THIN;
    final Paint topStroke;
    final Paint rightStroke;
    final Paint bottomStroke;
    final Paint leftStroke;
    final BorderStrokeStyle topStyle;
    final BorderStrokeStyle rightStyle;
    final BorderStrokeStyle bottomStyle;
    final BorderStrokeStyle leftStyle;
    final BorderWidths widths;
    final Insets insets;
    final Insets innerEdge;
    final Insets outerEdge;
    private final CornerRadii radii;
    private final boolean strokeUniform;
    private final int hash;

    public final Paint getTopStroke() {
        return this.topStroke;
    }

    public final Paint getRightStroke() {
        return this.rightStroke;
    }

    public final Paint getBottomStroke() {
        return this.bottomStroke;
    }

    public final Paint getLeftStroke() {
        return this.leftStroke;
    }

    public final BorderStrokeStyle getTopStyle() {
        return this.topStyle;
    }

    public final BorderStrokeStyle getRightStyle() {
        return this.rightStyle;
    }

    public final BorderStrokeStyle getBottomStyle() {
        return this.bottomStyle;
    }

    public final BorderStrokeStyle getLeftStyle() {
        return this.leftStyle;
    }

    public final BorderWidths getWidths() {
        return this.widths;
    }

    public final Insets getInsets() {
        return this.insets;
    }

    public final CornerRadii getRadii() {
        return this.radii;
    }

    public final boolean isStrokeUniform() {
        return this.strokeUniform;
    }

    public BorderStroke(@NamedArg("stroke") Paint stroke, @NamedArg(Constants.ATTRNAME_STYLE) BorderStrokeStyle style, @NamedArg("radii") CornerRadii radii, @NamedArg("widths") BorderWidths widths) {
        Paint paint = stroke == null ? Color.BLACK : stroke;
        this.bottomStroke = paint;
        this.rightStroke = paint;
        this.topStroke = paint;
        this.leftStroke = paint;
        BorderStrokeStyle borderStrokeStyle = style == null ? BorderStrokeStyle.NONE : style;
        this.leftStyle = borderStrokeStyle;
        this.bottomStyle = borderStrokeStyle;
        this.rightStyle = borderStrokeStyle;
        this.topStyle = borderStrokeStyle;
        this.radii = radii == null ? CornerRadii.EMPTY : radii;
        this.widths = widths == null ? DEFAULT_WIDTHS : widths;
        this.insets = Insets.EMPTY;
        this.strokeUniform = this.widths.left == this.widths.top && this.widths.left == this.widths.right && this.widths.left == this.widths.bottom;
        this.innerEdge = new Insets(computeInside(this.topStyle.getType(), this.widths.getTop()), computeInside(this.rightStyle.getType(), this.widths.getRight()), computeInside(this.bottomStyle.getType(), this.widths.getBottom()), computeInside(this.leftStyle.getType(), this.widths.getLeft()));
        this.outerEdge = new Insets(Math.max(0.0d, computeOutside(this.topStyle.getType(), this.widths.getTop())), Math.max(0.0d, computeOutside(this.rightStyle.getType(), this.widths.getRight())), Math.max(0.0d, computeOutside(this.bottomStyle.getType(), this.widths.getBottom())), Math.max(0.0d, computeOutside(this.leftStyle.getType(), this.widths.getLeft())));
        this.hash = preComputeHash();
    }

    public BorderStroke(@NamedArg("stroke") Paint stroke, @NamedArg(Constants.ATTRNAME_STYLE) BorderStrokeStyle style, @NamedArg("radii") CornerRadii radii, @NamedArg("widths") BorderWidths widths, @NamedArg("insets") Insets insets) {
        this(stroke, stroke, stroke, stroke, style, style, style, style, radii, widths, insets);
    }

    public BorderStroke(@NamedArg("topStroke") Paint topStroke, @NamedArg("rightStroke") Paint rightStroke, @NamedArg("bottomStroke") Paint bottomStroke, @NamedArg("leftStroke") Paint leftStroke, @NamedArg("topStyle") BorderStrokeStyle topStyle, @NamedArg("rightStyle") BorderStrokeStyle rightStyle, @NamedArg("bottomStyle") BorderStrokeStyle bottomStyle, @NamedArg("leftStyle") BorderStrokeStyle leftStyle, @NamedArg("radii") CornerRadii radii, @NamedArg("widths") BorderWidths widths, @NamedArg("insets") Insets insets) {
        this.topStroke = topStroke == null ? Color.BLACK : topStroke;
        this.rightStroke = rightStroke == null ? this.topStroke : rightStroke;
        this.bottomStroke = bottomStroke == null ? this.topStroke : bottomStroke;
        this.leftStroke = leftStroke == null ? this.rightStroke : leftStroke;
        this.topStyle = topStyle == null ? BorderStrokeStyle.NONE : topStyle;
        this.rightStyle = rightStyle == null ? this.topStyle : rightStyle;
        this.bottomStyle = bottomStyle == null ? this.topStyle : bottomStyle;
        this.leftStyle = leftStyle == null ? this.rightStyle : leftStyle;
        this.radii = radii == null ? CornerRadii.EMPTY : radii;
        this.widths = widths == null ? DEFAULT_WIDTHS : widths;
        this.insets = insets == null ? Insets.EMPTY : insets;
        boolean colorsSame = this.leftStroke.equals(this.topStroke) && this.leftStroke.equals(this.rightStroke) && this.leftStroke.equals(this.bottomStroke);
        boolean widthsSame = this.widths.left == this.widths.top && this.widths.left == this.widths.right && this.widths.left == this.widths.bottom;
        boolean stylesSame = this.leftStyle.equals(this.topStyle) && this.leftStyle.equals(this.rightStyle) && this.leftStyle.equals(this.bottomStyle);
        this.strokeUniform = colorsSame && widthsSame && stylesSame;
        this.innerEdge = new Insets(this.insets.getTop() + computeInside(this.topStyle.getType(), this.widths.getTop()), this.insets.getRight() + computeInside(this.rightStyle.getType(), this.widths.getRight()), this.insets.getBottom() + computeInside(this.bottomStyle.getType(), this.widths.getBottom()), this.insets.getLeft() + computeInside(this.leftStyle.getType(), this.widths.getLeft()));
        this.outerEdge = new Insets(Math.max(0.0d, computeOutside(this.topStyle.getType(), this.widths.getTop()) - this.insets.getTop()), Math.max(0.0d, computeOutside(this.rightStyle.getType(), this.widths.getRight()) - this.insets.getRight()), Math.max(0.0d, computeOutside(this.bottomStyle.getType(), this.widths.getBottom()) - this.insets.getBottom()), Math.max(0.0d, computeOutside(this.leftStyle.getType(), this.widths.getLeft()) - this.insets.getLeft()));
        this.hash = preComputeHash();
    }

    private int preComputeHash() {
        int result = this.topStroke.hashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.rightStroke.hashCode())) + this.bottomStroke.hashCode())) + this.leftStroke.hashCode())) + this.topStyle.hashCode())) + this.rightStyle.hashCode())) + this.bottomStyle.hashCode())) + this.leftStyle.hashCode())) + this.widths.hashCode())) + this.radii.hashCode())) + this.insets.hashCode();
    }

    private double computeInside(StrokeType type, double width) {
        if (type == StrokeType.OUTSIDE) {
            return 0.0d;
        }
        if (type == StrokeType.CENTERED) {
            return width / 2.0d;
        }
        if (type == StrokeType.INSIDE) {
            return width;
        }
        throw new AssertionError((Object) "Unexpected Stroke Type");
    }

    private double computeOutside(StrokeType type, double width) {
        if (type == StrokeType.OUTSIDE) {
            return width;
        }
        if (type == StrokeType.CENTERED) {
            return width / 2.0d;
        }
        if (type == StrokeType.INSIDE) {
            return 0.0d;
        }
        throw new AssertionError((Object) "Unexpected Stroke Type");
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        BorderStroke that = (BorderStroke) o2;
        return this.hash == that.hash && this.bottomStroke.equals(that.bottomStroke) && this.bottomStyle.equals(that.bottomStyle) && this.leftStroke.equals(that.leftStroke) && this.leftStyle.equals(that.leftStyle) && this.radii.equals(that.radii) && this.rightStroke.equals(that.rightStroke) && this.rightStyle.equals(that.rightStyle) && this.topStroke.equals(that.topStroke) && this.topStyle.equals(that.topStyle) && this.widths.equals(that.widths) && this.insets.equals(that.insets);
    }

    public int hashCode() {
        return this.hash;
    }
}
