package com.sun.javafx.util;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.stage.StageHelper;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/util/Utils.class */
public class Utils {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Utils.class.desiredAssertionStatus();
    }

    public static float clamp(float min, float value, float max) {
        return value < min ? min : value > max ? max : value;
    }

    public static int clamp(int min, int value, int max) {
        return value < min ? min : value > max ? max : value;
    }

    public static double clamp(double min, double value, double max) {
        return value < min ? min : value > max ? max : value;
    }

    public static double clampMin(double value, double min) {
        return value < min ? min : value;
    }

    public static int clampMax(int value, int max) {
        return value > max ? max : value;
    }

    public static double nearest(double less, double value, double more) {
        double lessDiff = value - less;
        double moreDiff = more - value;
        return lessDiff < moreDiff ? less : more;
    }

    public static String stripQuotes(String str) {
        if (str != null && str.length() != 0) {
            int beginIndex = 0;
            char openQuote = str.charAt(0);
            if (openQuote == '\"' || openQuote == '\'') {
                beginIndex = 0 + 1;
            }
            int endIndex = str.length();
            char closeQuote = str.charAt(endIndex - 1);
            if (closeQuote == '\"' || closeQuote == '\'') {
                endIndex--;
            }
            return endIndex - beginIndex < 0 ? str : str.substring(beginIndex, endIndex);
        }
        return str;
    }

    public static String[] split(String str, String separator) {
        if (str == null || str.length() == 0) {
            return new String[0];
        }
        if (separator == null || separator.length() == 0) {
            return new String[0];
        }
        if (separator.length() > str.length()) {
            return new String[0];
        }
        List<String> result = new ArrayList<>();
        int iIndexOf = str.indexOf(separator);
        while (true) {
            int index = iIndexOf;
            if (index < 0) {
                break;
            }
            String newStr = str.substring(0, index);
            if (newStr != null && newStr.length() > 0) {
                result.add(newStr);
            }
            str = str.substring(index + separator.length());
            iIndexOf = str.indexOf(separator);
        }
        if (str != null && str.length() > 0) {
            result.add(str);
        }
        return (String[]) result.toArray(new String[0]);
    }

    public static boolean contains(String src, String s2) {
        return (src == null || src.length() == 0 || s2 == null || s2.length() == 0 || s2.length() > src.length() || src.indexOf(s2) <= -1) ? false : true;
    }

    public static double calculateBrightness(Color color) {
        return (0.3d * color.getRed()) + (0.59d * color.getGreen()) + (0.11d * color.getBlue());
    }

    public static Color deriveColor(Color c2, double brightness) {
        double baseBrightness = calculateBrightness(c2);
        double calcBrightness = brightness;
        if (brightness > 0.0d) {
            if (baseBrightness > 0.85d) {
                calcBrightness *= 1.6d;
            } else if (baseBrightness <= 0.6d) {
                calcBrightness = baseBrightness > 0.5d ? calcBrightness * 0.9d : baseBrightness > 0.4d ? calcBrightness * 0.8d : baseBrightness > 0.3d ? calcBrightness * 0.7d : calcBrightness * 0.6d;
            }
        } else if (baseBrightness < 0.2d) {
            calcBrightness *= 0.6d;
        }
        if (calcBrightness < -1.0d) {
            calcBrightness = -1.0d;
        } else if (calcBrightness > 1.0d) {
            calcBrightness = 1.0d;
        }
        double[] hsb = RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue());
        if (calcBrightness > 0.0d) {
            hsb[1] = hsb[1] * (1.0d - calcBrightness);
            hsb[2] = hsb[2] + ((1.0d - hsb[2]) * calcBrightness);
        } else {
            hsb[2] = hsb[2] * (calcBrightness + 1.0d);
        }
        if (hsb[1] < 0.0d) {
            hsb[1] = 0.0d;
        } else if (hsb[1] > 1.0d) {
            hsb[1] = 1.0d;
        }
        if (hsb[2] < 0.0d) {
            hsb[2] = 0.0d;
        } else if (hsb[2] > 1.0d) {
            hsb[2] = 1.0d;
        }
        Color.hsb((int) hsb[0], hsb[1], hsb[2], c2.getOpacity());
        return Color.hsb((int) hsb[0], hsb[1], hsb[2], c2.getOpacity());
    }

    private static Color interpolateLinear(double position, Color color1, Color color2) {
        Color c1Linear = convertSRGBtoLinearRGB(color1);
        Color c2Linear = convertSRGBtoLinearRGB(color2);
        return convertLinearRGBtoSRGB(Color.color(c1Linear.getRed() + ((c2Linear.getRed() - c1Linear.getRed()) * position), c1Linear.getGreen() + ((c2Linear.getGreen() - c1Linear.getGreen()) * position), c1Linear.getBlue() + ((c2Linear.getBlue() - c1Linear.getBlue()) * position), c1Linear.getOpacity() + ((c2Linear.getOpacity() - c1Linear.getOpacity()) * position)));
    }

    private static Color ladder(double position, Stop[] stops) {
        Stop prevStop = null;
        for (Stop stop : stops) {
            if (position <= stop.getOffset()) {
                if (prevStop == null) {
                    return stop.getColor();
                }
                return interpolateLinear((position - prevStop.getOffset()) / (stop.getOffset() - prevStop.getOffset()), prevStop.getColor(), stop.getColor());
            }
            prevStop = stop;
        }
        return prevStop.getColor();
    }

    public static Color ladder(Color color, Stop[] stops) {
        return ladder(calculateBrightness(color), stops);
    }

    public static double[] HSBtoRGB(double hue, double saturation, double brightness) {
        double normalizedHue = ((hue % 360.0d) + 360.0d) % 360.0d;
        double hue2 = normalizedHue / 360.0d;
        double r2 = 0.0d;
        double g2 = 0.0d;
        double b2 = 0.0d;
        if (saturation == 0.0d) {
            b2 = brightness;
            g2 = brightness;
            r2 = brightness;
        } else {
            double h2 = (hue2 - Math.floor(hue2)) * 6.0d;
            double f2 = h2 - Math.floor(h2);
            double p2 = brightness * (1.0d - saturation);
            double q2 = brightness * (1.0d - (saturation * f2));
            double t2 = brightness * (1.0d - (saturation * (1.0d - f2)));
            switch ((int) h2) {
                case 0:
                    r2 = brightness;
                    g2 = t2;
                    b2 = p2;
                    break;
                case 1:
                    r2 = q2;
                    g2 = brightness;
                    b2 = p2;
                    break;
                case 2:
                    r2 = p2;
                    g2 = brightness;
                    b2 = t2;
                    break;
                case 3:
                    r2 = p2;
                    g2 = q2;
                    b2 = brightness;
                    break;
                case 4:
                    r2 = t2;
                    g2 = p2;
                    b2 = brightness;
                    break;
                case 5:
                    r2 = brightness;
                    g2 = p2;
                    b2 = q2;
                    break;
            }
        }
        return new double[]{r2, g2, b2};
    }

    public static double[] RGBtoHSB(double r2, double g2, double b2) {
        double saturation;
        double hue;
        double hue2;
        double[] hsbvals = new double[3];
        double cmax = r2 > g2 ? r2 : g2;
        if (b2 > cmax) {
            cmax = b2;
        }
        double cmin = r2 < g2 ? r2 : g2;
        if (b2 < cmin) {
            cmin = b2;
        }
        double brightness = cmax;
        if (cmax != 0.0d) {
            saturation = (cmax - cmin) / cmax;
        } else {
            saturation = 0.0d;
        }
        if (saturation == 0.0d) {
            hue2 = 0.0d;
        } else {
            double redc = (cmax - r2) / (cmax - cmin);
            double greenc = (cmax - g2) / (cmax - cmin);
            double bluec = (cmax - b2) / (cmax - cmin);
            if (r2 == cmax) {
                hue = bluec - greenc;
            } else if (g2 == cmax) {
                hue = (2.0d + redc) - bluec;
            } else {
                hue = (4.0d + greenc) - redc;
            }
            hue2 = hue / 6.0d;
            if (hue2 < 0.0d) {
                hue2 += 1.0d;
            }
        }
        hsbvals[0] = hue2 * 360.0d;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public static Color convertSRGBtoLinearRGB(Color color) {
        double[] colors = new double[3];
        colors[0] = color.getRed();
        colors[1] = color.getGreen();
        colors[2] = color.getBlue();
        for (int i2 = 0; i2 < colors.length; i2++) {
            if (colors[i2] <= 0.04045d) {
                colors[i2] = colors[i2] / 12.92d;
            } else {
                colors[i2] = Math.pow((colors[i2] + 0.055d) / 1.055d, 2.4d);
            }
        }
        return Color.color(colors[0], colors[1], colors[2], color.getOpacity());
    }

    public static Color convertLinearRGBtoSRGB(Color color) {
        double[] colors = new double[3];
        colors[0] = color.getRed();
        colors[1] = color.getGreen();
        colors[2] = color.getBlue();
        for (int i2 = 0; i2 < colors.length; i2++) {
            if (colors[i2] <= 0.0031308d) {
                colors[i2] = colors[i2] * 12.92d;
            } else {
                colors[i2] = (1.055d * Math.pow(colors[i2], 0.4166666666666667d)) - 0.055d;
            }
        }
        return Color.color(colors[0], colors[1], colors[2], color.getOpacity());
    }

    public static double sum(double[] values) {
        double sum = 0.0d;
        for (double v2 : values) {
            sum += v2;
        }
        return sum / values.length;
    }

    public static Point2D pointRelativeTo(Node parent, Node node, HPos hpos, VPos vpos, double dx, double dy, boolean reposition) {
        double nodeWidth = node.getLayoutBounds().getWidth();
        double nodeHeight = node.getLayoutBounds().getHeight();
        return pointRelativeTo(parent, nodeWidth, nodeHeight, hpos, vpos, dx, dy, reposition);
    }

    public static Point2D pointRelativeTo(Node parent, double anchorWidth, double anchorHeight, HPos hpos, VPos vpos, double dx, double dy, boolean reposition) {
        Bounds parentBounds = getBounds(parent);
        Scene scene = parent.getScene();
        NodeOrientation orientation = parent.getEffectiveNodeOrientation();
        if (orientation == NodeOrientation.RIGHT_TO_LEFT) {
            if (hpos == HPos.LEFT) {
                hpos = HPos.RIGHT;
            } else if (hpos == HPos.RIGHT) {
                hpos = HPos.LEFT;
            }
            dx *= -1.0d;
        }
        double layoutX = positionX(parentBounds, anchorWidth, hpos) + dx;
        double layoutY = positionY(parentBounds, anchorHeight, vpos) + dy;
        if (orientation == NodeOrientation.RIGHT_TO_LEFT && hpos == HPos.CENTER) {
            layoutX = scene.getWindow() instanceof Stage ? (layoutX + parentBounds.getWidth()) - anchorWidth : (layoutX - parentBounds.getWidth()) - anchorWidth;
        }
        if (reposition) {
            return pointRelativeTo(parent, anchorWidth, anchorHeight, layoutX, layoutY, hpos, vpos);
        }
        return new Point2D(layoutX, layoutY);
    }

    public static Point2D pointRelativeTo(Object parent, double width, double height, double screenX, double screenY, HPos hpos, VPos vpos) {
        Rectangle2D visualBounds;
        double finalScreenX = screenX;
        double finalScreenY = screenY;
        Bounds parentBounds = getBounds(parent);
        Screen currentScreen = getScreen(parent);
        if (hasFullScreenStage(currentScreen)) {
            visualBounds = currentScreen.getBounds();
        } else {
            visualBounds = currentScreen.getVisualBounds();
        }
        Rectangle2D screenBounds = visualBounds;
        if (hpos != null) {
            if (finalScreenX + width > screenBounds.getMaxX()) {
                finalScreenX = positionX(parentBounds, width, getHPosOpposite(hpos, vpos));
            }
            if (finalScreenX < screenBounds.getMinX()) {
                finalScreenX = positionX(parentBounds, width, getHPosOpposite(hpos, vpos));
            }
        }
        if (vpos != null) {
            if (finalScreenY + height > screenBounds.getMaxY()) {
                finalScreenY = positionY(parentBounds, height, getVPosOpposite(hpos, vpos));
            }
            if (finalScreenY < screenBounds.getMinY()) {
                finalScreenY = positionY(parentBounds, height, getVPosOpposite(hpos, vpos));
            }
        }
        if (finalScreenX + width > screenBounds.getMaxX()) {
            finalScreenX -= (finalScreenX + width) - screenBounds.getMaxX();
        }
        if (finalScreenX < screenBounds.getMinX()) {
            finalScreenX = screenBounds.getMinX();
        }
        if (finalScreenY + height > screenBounds.getMaxY()) {
            finalScreenY -= (finalScreenY + height) - screenBounds.getMaxY();
        }
        if (finalScreenY < screenBounds.getMinY()) {
            finalScreenY = screenBounds.getMinY();
        }
        return new Point2D(finalScreenX, finalScreenY);
    }

    private static double positionX(Bounds parentBounds, double width, HPos hpos) {
        if (hpos == HPos.CENTER) {
            return parentBounds.getMinX();
        }
        if (hpos == HPos.RIGHT) {
            return parentBounds.getMaxX();
        }
        if (hpos == HPos.LEFT) {
            return parentBounds.getMinX() - width;
        }
        return 0.0d;
    }

    private static double positionY(Bounds parentBounds, double height, VPos vpos) {
        if (vpos == VPos.BOTTOM) {
            return parentBounds.getMaxY();
        }
        if (vpos == VPos.CENTER) {
            return parentBounds.getMinY();
        }
        if (vpos == VPos.TOP) {
            return parentBounds.getMinY() - height;
        }
        return 0.0d;
    }

    private static Bounds getBounds(Object obj) {
        if (obj instanceof Node) {
            Node n2 = (Node) obj;
            Bounds b2 = n2.localToScreen(n2.getLayoutBounds());
            return b2 != null ? b2 : new BoundingBox(0.0d, 0.0d, 0.0d, 0.0d);
        }
        if (obj instanceof Window) {
            Window window = (Window) obj;
            return new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        }
        return new BoundingBox(0.0d, 0.0d, 0.0d, 0.0d);
    }

    private static HPos getHPosOpposite(HPos hpos, VPos vpos) {
        if (vpos == VPos.CENTER) {
            if (hpos == HPos.LEFT) {
                return HPos.RIGHT;
            }
            if (hpos == HPos.RIGHT) {
                return HPos.LEFT;
            }
            if (hpos == HPos.CENTER) {
                return HPos.CENTER;
            }
            return HPos.CENTER;
        }
        return HPos.CENTER;
    }

    private static VPos getVPosOpposite(HPos hpos, VPos vpos) {
        if (hpos == HPos.CENTER) {
            if (vpos == VPos.BASELINE) {
                return VPos.BASELINE;
            }
            if (vpos == VPos.BOTTOM) {
                return VPos.TOP;
            }
            if (vpos == VPos.CENTER) {
                return VPos.CENTER;
            }
            if (vpos == VPos.TOP) {
                return VPos.BOTTOM;
            }
            return VPos.CENTER;
        }
        return VPos.CENTER;
    }

    public static boolean hasFullScreenStage(Screen screen) {
        List<Stage> allStages = StageHelper.getStages();
        for (Stage stage : allStages) {
            if (stage.isFullScreen() && getScreen(stage) == screen) {
                return true;
            }
        }
        return false;
    }

    public static boolean isQVGAScreen() {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        return (bounds.getWidth() == 320.0d && bounds.getHeight() == 240.0d) || (bounds.getWidth() == 240.0d && bounds.getHeight() == 320.0d);
    }

    public static Screen getScreen(Object obj) {
        Bounds parentBounds = getBounds(obj);
        Rectangle2D rect = new Rectangle2D(parentBounds.getMinX(), parentBounds.getMinY(), parentBounds.getWidth(), parentBounds.getHeight());
        return getScreenForRectangle(rect);
    }

    public static Screen getScreenForRectangle(Rectangle2D rect) {
        List<Screen> screens = Screen.getScreens();
        double rectX0 = rect.getMinX();
        double rectX1 = rect.getMaxX();
        double rectY0 = rect.getMinY();
        double rectY1 = rect.getMaxY();
        Screen selectedScreen = null;
        double maxIntersection = 0.0d;
        for (Screen screen : screens) {
            Rectangle2D screenBounds = screen.getBounds();
            double intersection = getIntersectionLength(rectX0, rectX1, screenBounds.getMinX(), screenBounds.getMaxX()) * getIntersectionLength(rectY0, rectY1, screenBounds.getMinY(), screenBounds.getMaxY());
            if (maxIntersection < intersection) {
                maxIntersection = intersection;
                selectedScreen = screen;
            }
        }
        if (selectedScreen != null) {
            return selectedScreen;
        }
        Screen selectedScreen2 = Screen.getPrimary();
        double minDistance = Double.MAX_VALUE;
        for (Screen screen2 : screens) {
            Rectangle2D screenBounds2 = screen2.getBounds();
            double dx = getOuterDistance(rectX0, rectX1, screenBounds2.getMinX(), screenBounds2.getMaxX());
            double dy = getOuterDistance(rectY0, rectY1, screenBounds2.getMinY(), screenBounds2.getMaxY());
            double distance = (dx * dx) + (dy * dy);
            if (minDistance > distance) {
                minDistance = distance;
                selectedScreen2 = screen2;
            }
        }
        return selectedScreen2;
    }

    public static Screen getScreenForPoint(double x2, double y2) {
        List<Screen> screens = Screen.getScreens();
        for (Screen screen : screens) {
            Rectangle2D screenBounds = screen.getBounds();
            if (x2 >= screenBounds.getMinX() && x2 < screenBounds.getMaxX() && y2 >= screenBounds.getMinY() && y2 < screenBounds.getMaxY()) {
                return screen;
            }
        }
        Screen selectedScreen = Screen.getPrimary();
        double minDistance = Double.MAX_VALUE;
        for (Screen screen2 : screens) {
            Rectangle2D screenBounds2 = screen2.getBounds();
            double dx = getOuterDistance(screenBounds2.getMinX(), screenBounds2.getMaxX(), x2);
            double dy = getOuterDistance(screenBounds2.getMinY(), screenBounds2.getMaxY(), y2);
            double distance = (dx * dx) + (dy * dy);
            if (minDistance >= distance) {
                minDistance = distance;
                selectedScreen = screen2;
            }
        }
        return selectedScreen;
    }

    private static double getIntersectionLength(double a0, double a1, double b0, double b1) {
        return a0 <= b0 ? getIntersectionLengthImpl(b0, b1, a1) : getIntersectionLengthImpl(a0, a1, b1);
    }

    private static double getIntersectionLengthImpl(double v0, double v1, double v2) {
        if (v2 <= v0) {
            return 0.0d;
        }
        return v2 <= v1 ? v2 - v0 : v1 - v0;
    }

    private static double getOuterDistance(double a0, double a1, double b0, double b1) {
        if (a1 <= b0) {
            return b0 - a1;
        }
        if (b1 <= a0) {
            return b1 - a0;
        }
        return 0.0d;
    }

    private static double getOuterDistance(double v0, double v1, double v2) {
        if (v2 <= v0) {
            return v0 - v2;
        }
        if (v2 >= v1) {
            return v2 - v1;
        }
        return 0.0d;
    }

    public static boolean assertionEnabled() {
        boolean assertsEnabled = false;
        if (!$assertionsDisabled) {
            assertsEnabled = true;
            if (1 == 0) {
                throw new AssertionError();
            }
        }
        return assertsEnabled;
    }

    public static boolean isWindows() {
        return PlatformUtil.isWindows();
    }

    public static boolean isMac() {
        return PlatformUtil.isMac();
    }

    public static boolean isUnix() {
        return PlatformUtil.isUnix();
    }

    public static String convertUnicode(String src) {
        int code;
        int unicodeConversionBp = -1;
        char[] buf = src.toCharArray();
        int buflen = buf.length;
        int bp2 = -1;
        char[] dst = new char[buflen];
        int dstIndex = 0;
        while (bp2 < buflen - 1) {
            bp2++;
            char ch = buf[bp2];
            if (ch == '\\' && unicodeConversionBp != bp2) {
                bp2++;
                if (buf[bp2] == 'u') {
                    do {
                        bp2++;
                        ch = buf[bp2];
                    } while (ch == 'u');
                    int limit = bp2 + 3;
                    if (limit < buflen) {
                        int result = Character.digit(ch, 16);
                        if (result >= 0 && ch > 127) {
                            ch = "0123456789abcdef".charAt(result);
                        }
                        int d2 = result;
                        int i2 = d2;
                        while (true) {
                            code = i2;
                            if (bp2 >= limit || d2 < 0) {
                                break;
                            }
                            bp2++;
                            ch = buf[bp2];
                            int result1 = Character.digit(ch, 16);
                            if (result1 >= 0 && ch > 127) {
                                ch = "0123456789abcdef".charAt(result1);
                            }
                            d2 = result1;
                            i2 = (code << 4) + d2;
                        }
                        if (d2 >= 0) {
                            ch = (char) code;
                            unicodeConversionBp = bp2;
                        }
                    }
                } else {
                    bp2--;
                    ch = '\\';
                }
            }
            int i3 = dstIndex;
            dstIndex++;
            dst[i3] = ch;
        }
        return new String(dst, 0, dstIndex);
    }
}
