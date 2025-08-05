package javafx.scene.paint;

import com.sun.javafx.scene.paint.GradientUtils;
import com.sun.javafx.tk.Toolkit;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.List;
import javafx.beans.NamedArg;
import javax.swing.JSplitPane;

/* loaded from: jfxrt.jar:javafx/scene/paint/LinearGradient.class */
public final class LinearGradient extends Paint {
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private boolean proportional;
    private CycleMethod cycleMethod;
    private List<Stop> stops;
    private final boolean opaque;
    private Object platformPaint;
    private int hash;

    public final double getStartX() {
        return this.startX;
    }

    public final double getStartY() {
        return this.startY;
    }

    public final double getEndX() {
        return this.endX;
    }

    public final double getEndY() {
        return this.endY;
    }

    public final boolean isProportional() {
        return this.proportional;
    }

    public final CycleMethod getCycleMethod() {
        return this.cycleMethod;
    }

    public final List<Stop> getStops() {
        return this.stops;
    }

    @Override // javafx.scene.paint.Paint
    public final boolean isOpaque() {
        return this.opaque;
    }

    public LinearGradient(@NamedArg("startX") double startX, @NamedArg("startY") double startY, @NamedArg(value = "endX", defaultValue = "1") double endX, @NamedArg(value = "endY", defaultValue = "1") double endY, @NamedArg(value = "proportional", defaultValue = "true") boolean proportional, @NamedArg("cycleMethod") CycleMethod cycleMethod, @NamedArg("stops") Stop... stops) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.proportional = proportional;
        this.cycleMethod = cycleMethod == null ? CycleMethod.NO_CYCLE : cycleMethod;
        this.stops = Stop.normalize(stops);
        this.opaque = determineOpacity();
    }

    public LinearGradient(@NamedArg("startX") double startX, @NamedArg("startY") double startY, @NamedArg(value = "endX", defaultValue = "1") double endX, @NamedArg(value = "endY", defaultValue = "1") double endY, @NamedArg(value = "proportional", defaultValue = "true") boolean proportional, @NamedArg("cycleMethod") CycleMethod cycleMethod, @NamedArg("stops") List<Stop> stops) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.proportional = proportional;
        this.cycleMethod = cycleMethod == null ? CycleMethod.NO_CYCLE : cycleMethod;
        this.stops = Stop.normalize(stops);
        this.opaque = determineOpacity();
    }

    private boolean determineOpacity() {
        int numStops = this.stops.size();
        for (int i2 = 0; i2 < numStops; i2++) {
            if (!this.stops.get(i2).getColor().isOpaque()) {
                return false;
            }
        }
        return true;
    }

    @Override // javafx.scene.paint.Paint
    Object acc_getPlatformPaint() {
        if (this.platformPaint == null) {
            this.platformPaint = Toolkit.getToolkit().getPaint((Paint) this);
        }
        return this.platformPaint;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof LinearGradient) {
            LinearGradient other = (LinearGradient) obj;
            return this.startX == other.startX && this.startY == other.startY && this.endX == other.endX && this.endY == other.endY && this.proportional == other.proportional && this.cycleMethod == other.cycleMethod && this.stops.equals(other.stops);
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long bits = (37 * 17) + Double.doubleToLongBits(this.startX);
            long bits2 = (37 * ((37 * ((37 * ((37 * ((37 * bits) + Double.doubleToLongBits(this.startY))) + Double.doubleToLongBits(this.endX))) + Double.doubleToLongBits(this.endY))) + (this.proportional ? 1231L : 1237L))) + this.cycleMethod.hashCode();
            for (Stop stop : this.stops) {
                bits2 = (37 * bits2) + stop.hashCode();
            }
            this.hash = (int) (bits2 ^ (bits2 >> 32));
        }
        return this.hash;
    }

    public String toString() {
        StringBuilder s2 = new StringBuilder("linear-gradient(from ").append(GradientUtils.lengthToString(this.startX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.startY, this.proportional)).append(" to ").append(GradientUtils.lengthToString(this.endX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.endY, this.proportional)).append(", ");
        switch (this.cycleMethod) {
            case REFLECT:
                s2.append("reflect").append(", ");
                break;
            case REPEAT:
                s2.append("repeat").append(", ");
                break;
        }
        for (Stop stop : this.stops) {
            s2.append((Object) stop).append(", ");
        }
        s2.delete(s2.length() - 2, s2.length());
        s2.append(")");
        return s2.toString();
    }

    public static LinearGradient valueOf(String value) {
        if (value == null) {
            throw new NullPointerException("gradient must be specified");
        }
        if (value.startsWith("linear-gradient(")) {
            if (!value.endsWith(")")) {
                throw new IllegalArgumentException("Invalid gradient specification, must end with \")\"");
            }
            value = value.substring("linear-gradient(".length(), value.length() - ")".length());
        }
        GradientUtils.Parser parser = new GradientUtils.Parser(value);
        if (parser.getSize() < 2) {
            throw new IllegalArgumentException("Invalid gradient specification");
        }
        GradientUtils.Point startX = GradientUtils.Point.MIN;
        GradientUtils.Point startY = GradientUtils.Point.MIN;
        GradientUtils.Point endX = GradientUtils.Point.MIN;
        GradientUtils.Point endY = GradientUtils.Point.MIN;
        String[] tokens = parser.splitCurrentToken();
        if (Constants.ATTRNAME_FROM.equals(tokens[0])) {
            GradientUtils.Parser.checkNumberOfArguments(tokens, 5);
            startX = parser.parsePoint(tokens[1]);
            startY = parser.parsePoint(tokens[2]);
            if (!"to".equals(tokens[3])) {
                throw new IllegalArgumentException("Invalid gradient specification, \"to\" expected");
            }
            endX = parser.parsePoint(tokens[4]);
            endY = parser.parsePoint(tokens[5]);
            parser.shift();
        } else if ("to".equals(tokens[0])) {
            int horizontalSet = 0;
            int verticalSet = 0;
            for (int i2 = 1; i2 < 3 && i2 < tokens.length; i2++) {
                if (JSplitPane.LEFT.equals(tokens[i2])) {
                    startX = GradientUtils.Point.MAX;
                    endX = GradientUtils.Point.MIN;
                    horizontalSet++;
                } else if (JSplitPane.RIGHT.equals(tokens[i2])) {
                    startX = GradientUtils.Point.MIN;
                    endX = GradientUtils.Point.MAX;
                    horizontalSet++;
                } else if (JSplitPane.TOP.equals(tokens[i2])) {
                    startY = GradientUtils.Point.MAX;
                    endY = GradientUtils.Point.MIN;
                    verticalSet++;
                } else if (JSplitPane.BOTTOM.equals(tokens[i2])) {
                    startY = GradientUtils.Point.MIN;
                    endY = GradientUtils.Point.MAX;
                    verticalSet++;
                } else {
                    throw new IllegalArgumentException("Invalid gradient specification, unknown value after 'to'");
                }
            }
            if (verticalSet > 1) {
                throw new IllegalArgumentException("Invalid gradient specification, vertical direction set twice after 'to'");
            }
            if (horizontalSet > 1) {
                throw new IllegalArgumentException("Invalid gradient specification, horizontal direction set twice after 'to'");
            }
            parser.shift();
        } else {
            startY = GradientUtils.Point.MIN;
            endY = GradientUtils.Point.MAX;
        }
        CycleMethod method = CycleMethod.NO_CYCLE;
        String currentToken = parser.getCurrentToken();
        if ("repeat".equals(currentToken)) {
            method = CycleMethod.REPEAT;
            parser.shift();
        } else if ("reflect".equals(currentToken)) {
            method = CycleMethod.REFLECT;
            parser.shift();
        }
        double dist = 0.0d;
        if (!startX.proportional) {
            double dx = endX.value - startX.value;
            double dy = endY.value - startY.value;
            dist = Math.sqrt((dx * dx) + (dy * dy));
        }
        Stop[] stops = parser.parseStops(startX.proportional, dist);
        return new LinearGradient(startX.value, startY.value, endX.value, endY.value, startX.proportional, method, stops);
    }
}
