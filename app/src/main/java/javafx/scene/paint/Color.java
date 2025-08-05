package javafx.scene.paint;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.animation.Interpolatable;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:javafx/scene/paint/Color.class */
public final class Color extends Paint implements Interpolatable<Color> {
    private static final double DARKER_BRIGHTER_FACTOR = 0.7d;
    private static final double SATURATE_DESATURATE_FACTOR = 0.7d;
    private static final int PARSE_COMPONENT = 0;
    private static final int PARSE_PERCENT = 1;
    private static final int PARSE_ANGLE = 2;
    private static final int PARSE_ALPHA = 3;
    public static final Color TRANSPARENT = new Color(0.0d, 0.0d, 0.0d, 0.0d);
    public static final Color ALICEBLUE = new Color(0.9411765f, 0.972549f, 1.0f);
    public static final Color ANTIQUEWHITE = new Color(0.98039216f, 0.92156863f, 0.84313726f);
    public static final Color AQUA = new Color(0.0f, 1.0f, 1.0f);
    public static final Color AQUAMARINE = new Color(0.49803922f, 1.0f, 0.83137256f);
    public static final Color AZURE = new Color(0.9411765f, 1.0f, 1.0f);
    public static final Color BEIGE = new Color(0.9607843f, 0.9607843f, 0.8627451f);
    public static final Color BISQUE = new Color(1.0f, 0.89411765f, 0.76862746f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);
    public static final Color BLANCHEDALMOND = new Color(1.0f, 0.92156863f, 0.8039216f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f);
    public static final Color BLUEVIOLET = new Color(0.5411765f, 0.16862746f, 0.8862745f);
    public static final Color BROWN = new Color(0.64705884f, 0.16470589f, 0.16470589f);
    public static final Color BURLYWOOD = new Color(0.87058824f, 0.72156864f, 0.5294118f);
    public static final Color CADETBLUE = new Color(0.37254903f, 0.61960787f, 0.627451f);
    public static final Color CHARTREUSE = new Color(0.49803922f, 1.0f, 0.0f);
    public static final Color CHOCOLATE = new Color(0.8235294f, 0.4117647f, 0.11764706f);
    public static final Color CORAL = new Color(1.0f, 0.49803922f, 0.3137255f);
    public static final Color CORNFLOWERBLUE = new Color(0.39215687f, 0.58431375f, 0.92941177f);
    public static final Color CORNSILK = new Color(1.0f, 0.972549f, 0.8627451f);
    public static final Color CRIMSON = new Color(0.8627451f, 0.078431375f, 0.23529412f);
    public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f);
    public static final Color DARKBLUE = new Color(0.0f, 0.0f, 0.54509807f);
    public static final Color DARKCYAN = new Color(0.0f, 0.54509807f, 0.54509807f);
    public static final Color DARKGOLDENROD = new Color(0.72156864f, 0.5254902f, 0.043137256f);
    public static final Color DARKGRAY = new Color(0.6627451f, 0.6627451f, 0.6627451f);
    public static final Color DARKGREEN = new Color(0.0f, 0.39215687f, 0.0f);
    public static final Color DARKGREY = DARKGRAY;
    public static final Color DARKKHAKI = new Color(0.7411765f, 0.7176471f, 0.41960785f);
    public static final Color DARKMAGENTA = new Color(0.54509807f, 0.0f, 0.54509807f);
    public static final Color DARKOLIVEGREEN = new Color(0.33333334f, 0.41960785f, 0.18431373f);
    public static final Color DARKORANGE = new Color(1.0f, 0.54901963f, 0.0f);
    public static final Color DARKORCHID = new Color(0.6f, 0.19607843f, 0.8f);
    public static final Color DARKRED = new Color(0.54509807f, 0.0f, 0.0f);
    public static final Color DARKSALMON = new Color(0.9137255f, 0.5882353f, 0.47843137f);
    public static final Color DARKSEAGREEN = new Color(0.56078434f, 0.7372549f, 0.56078434f);
    public static final Color DARKSLATEBLUE = new Color(0.28235295f, 0.23921569f, 0.54509807f);
    public static final Color DARKSLATEGRAY = new Color(0.18431373f, 0.30980393f, 0.30980393f);
    public static final Color DARKSLATEGREY = DARKSLATEGRAY;
    public static final Color DARKTURQUOISE = new Color(0.0f, 0.80784315f, 0.81960785f);
    public static final Color DARKVIOLET = new Color(0.5803922f, 0.0f, 0.827451f);
    public static final Color DEEPPINK = new Color(1.0f, 0.078431375f, 0.5764706f);
    public static final Color DEEPSKYBLUE = new Color(0.0f, 0.7490196f, 1.0f);
    public static final Color DIMGRAY = new Color(0.4117647f, 0.4117647f, 0.4117647f);
    public static final Color DIMGREY = DIMGRAY;
    public static final Color DODGERBLUE = new Color(0.11764706f, 0.5647059f, 1.0f);
    public static final Color FIREBRICK = new Color(0.69803923f, 0.13333334f, 0.13333334f);
    public static final Color FLORALWHITE = new Color(1.0f, 0.98039216f, 0.9411765f);
    public static final Color FORESTGREEN = new Color(0.13333334f, 0.54509807f, 0.13333334f);
    public static final Color FUCHSIA = new Color(1.0f, 0.0f, 1.0f);
    public static final Color GAINSBORO = new Color(0.8627451f, 0.8627451f, 0.8627451f);
    public static final Color GHOSTWHITE = new Color(0.972549f, 0.972549f, 1.0f);
    public static final Color GOLD = new Color(1.0f, 0.84313726f, 0.0f);
    public static final Color GOLDENROD = new Color(0.85490197f, 0.64705884f, 0.1254902f);
    public static final Color GRAY = new Color(0.5019608f, 0.5019608f, 0.5019608f);
    public static final Color GREEN = new Color(0.0f, 0.5019608f, 0.0f);
    public static final Color GREENYELLOW = new Color(0.6784314f, 1.0f, 0.18431373f);
    public static final Color GREY = GRAY;
    public static final Color HONEYDEW = new Color(0.9411765f, 1.0f, 0.9411765f);
    public static final Color HOTPINK = new Color(1.0f, 0.4117647f, 0.7058824f);
    public static final Color INDIANRED = new Color(0.8039216f, 0.36078432f, 0.36078432f);
    public static final Color INDIGO = new Color(0.29411766f, 0.0f, 0.50980395f);
    public static final Color IVORY = new Color(1.0f, 1.0f, 0.9411765f);
    public static final Color KHAKI = new Color(0.9411765f, 0.9019608f, 0.54901963f);
    public static final Color LAVENDER = new Color(0.9019608f, 0.9019608f, 0.98039216f);
    public static final Color LAVENDERBLUSH = new Color(1.0f, 0.9411765f, 0.9607843f);
    public static final Color LAWNGREEN = new Color(0.4862745f, 0.9882353f, 0.0f);
    public static final Color LEMONCHIFFON = new Color(1.0f, 0.98039216f, 0.8039216f);
    public static final Color LIGHTBLUE = new Color(0.6784314f, 0.84705883f, 0.9019608f);
    public static final Color LIGHTCORAL = new Color(0.9411765f, 0.5019608f, 0.5019608f);
    public static final Color LIGHTCYAN = new Color(0.8784314f, 1.0f, 1.0f);
    public static final Color LIGHTGOLDENRODYELLOW = new Color(0.98039216f, 0.98039216f, 0.8235294f);
    public static final Color LIGHTGRAY = new Color(0.827451f, 0.827451f, 0.827451f);
    public static final Color LIGHTGREEN = new Color(0.5647059f, 0.93333334f, 0.5647059f);
    public static final Color LIGHTGREY = LIGHTGRAY;
    public static final Color LIGHTPINK = new Color(1.0f, 0.7137255f, 0.75686276f);
    public static final Color LIGHTSALMON = new Color(1.0f, 0.627451f, 0.47843137f);
    public static final Color LIGHTSEAGREEN = new Color(0.1254902f, 0.69803923f, 0.6666667f);
    public static final Color LIGHTSKYBLUE = new Color(0.5294118f, 0.80784315f, 0.98039216f);
    public static final Color LIGHTSLATEGRAY = new Color(0.46666667f, 0.53333336f, 0.6f);
    public static final Color LIGHTSLATEGREY = LIGHTSLATEGRAY;
    public static final Color LIGHTSTEELBLUE = new Color(0.6901961f, 0.76862746f, 0.87058824f);
    public static final Color LIGHTYELLOW = new Color(1.0f, 1.0f, 0.8784314f);
    public static final Color LIME = new Color(0.0f, 1.0f, 0.0f);
    public static final Color LIMEGREEN = new Color(0.19607843f, 0.8039216f, 0.19607843f);
    public static final Color LINEN = new Color(0.98039216f, 0.9411765f, 0.9019608f);
    public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f);
    public static final Color MAROON = new Color(0.5019608f, 0.0f, 0.0f);
    public static final Color MEDIUMAQUAMARINE = new Color(0.4f, 0.8039216f, 0.6666667f);
    public static final Color MEDIUMBLUE = new Color(0.0f, 0.0f, 0.8039216f);
    public static final Color MEDIUMORCHID = new Color(0.7294118f, 0.33333334f, 0.827451f);
    public static final Color MEDIUMPURPLE = new Color(0.5764706f, 0.4392157f, 0.85882354f);
    public static final Color MEDIUMSEAGREEN = new Color(0.23529412f, 0.7019608f, 0.44313726f);
    public static final Color MEDIUMSLATEBLUE = new Color(0.48235294f, 0.40784314f, 0.93333334f);
    public static final Color MEDIUMSPRINGGREEN = new Color(0.0f, 0.98039216f, 0.6039216f);
    public static final Color MEDIUMTURQUOISE = new Color(0.28235295f, 0.81960785f, 0.8f);
    public static final Color MEDIUMVIOLETRED = new Color(0.78039217f, 0.08235294f, 0.52156866f);
    public static final Color MIDNIGHTBLUE = new Color(0.09803922f, 0.09803922f, 0.4392157f);
    public static final Color MINTCREAM = new Color(0.9607843f, 1.0f, 0.98039216f);
    public static final Color MISTYROSE = new Color(1.0f, 0.89411765f, 0.88235295f);
    public static final Color MOCCASIN = new Color(1.0f, 0.89411765f, 0.70980394f);
    public static final Color NAVAJOWHITE = new Color(1.0f, 0.87058824f, 0.6784314f);
    public static final Color NAVY = new Color(0.0f, 0.0f, 0.5019608f);
    public static final Color OLDLACE = new Color(0.99215686f, 0.9607843f, 0.9019608f);
    public static final Color OLIVE = new Color(0.5019608f, 0.5019608f, 0.0f);
    public static final Color OLIVEDRAB = new Color(0.41960785f, 0.5568628f, 0.13725491f);
    public static final Color ORANGE = new Color(1.0f, 0.64705884f, 0.0f);
    public static final Color ORANGERED = new Color(1.0f, 0.27058825f, 0.0f);
    public static final Color ORCHID = new Color(0.85490197f, 0.4392157f, 0.8392157f);
    public static final Color PALEGOLDENROD = new Color(0.93333334f, 0.9098039f, 0.6666667f);
    public static final Color PALEGREEN = new Color(0.59607846f, 0.9843137f, 0.59607846f);
    public static final Color PALETURQUOISE = new Color(0.6862745f, 0.93333334f, 0.93333334f);
    public static final Color PALEVIOLETRED = new Color(0.85882354f, 0.4392157f, 0.5764706f);
    public static final Color PAPAYAWHIP = new Color(1.0f, 0.9372549f, 0.8352941f);
    public static final Color PEACHPUFF = new Color(1.0f, 0.85490197f, 0.7254902f);
    public static final Color PERU = new Color(0.8039216f, 0.52156866f, 0.24705882f);
    public static final Color PINK = new Color(1.0f, 0.7529412f, 0.79607844f);
    public static final Color PLUM = new Color(0.8666667f, 0.627451f, 0.8666667f);
    public static final Color POWDERBLUE = new Color(0.6901961f, 0.8784314f, 0.9019608f);
    public static final Color PURPLE = new Color(0.5019608f, 0.0f, 0.5019608f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f);
    public static final Color ROSYBROWN = new Color(0.7372549f, 0.56078434f, 0.56078434f);
    public static final Color ROYALBLUE = new Color(0.25490198f, 0.4117647f, 0.88235295f);
    public static final Color SADDLEBROWN = new Color(0.54509807f, 0.27058825f, 0.07450981f);
    public static final Color SALMON = new Color(0.98039216f, 0.5019608f, 0.44705883f);
    public static final Color SANDYBROWN = new Color(0.95686275f, 0.6431373f, 0.3764706f);
    public static final Color SEAGREEN = new Color(0.18039216f, 0.54509807f, 0.34117648f);
    public static final Color SEASHELL = new Color(1.0f, 0.9607843f, 0.93333334f);
    public static final Color SIENNA = new Color(0.627451f, 0.32156864f, 0.1764706f);
    public static final Color SILVER = new Color(0.7529412f, 0.7529412f, 0.7529412f);
    public static final Color SKYBLUE = new Color(0.5294118f, 0.80784315f, 0.92156863f);
    public static final Color SLATEBLUE = new Color(0.41568628f, 0.3529412f, 0.8039216f);
    public static final Color SLATEGRAY = new Color(0.4392157f, 0.5019608f, 0.5647059f);
    public static final Color SLATEGREY = SLATEGRAY;
    public static final Color SNOW = new Color(1.0f, 0.98039216f, 0.98039216f);
    public static final Color SPRINGGREEN = new Color(0.0f, 1.0f, 0.49803922f);
    public static final Color STEELBLUE = new Color(0.27450982f, 0.50980395f, 0.7058824f);
    public static final Color TAN = new Color(0.8235294f, 0.7058824f, 0.54901963f);
    public static final Color TEAL = new Color(0.0f, 0.5019608f, 0.5019608f);
    public static final Color THISTLE = new Color(0.84705883f, 0.7490196f, 0.84705883f);
    public static final Color TOMATO = new Color(1.0f, 0.3882353f, 0.2784314f);
    public static final Color TURQUOISE = new Color(0.2509804f, 0.8784314f, 0.8156863f);
    public static final Color VIOLET = new Color(0.93333334f, 0.50980395f, 0.93333334f);
    public static final Color WHEAT = new Color(0.9607843f, 0.87058824f, 0.7019608f);
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f);
    public static final Color WHITESMOKE = new Color(0.9607843f, 0.9607843f, 0.9607843f);
    public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f);
    public static final Color YELLOWGREEN = new Color(0.6039216f, 0.8039216f, 0.19607843f);
    private float red;
    private float green;
    private float blue;
    private float opacity;
    private Object platformPaint;

    public static Color color(double red, double green, double blue, double opacity) {
        return new Color(red, green, blue, opacity);
    }

    public static Color color(double red, double green, double blue) {
        return new Color(red, green, blue, 1.0d);
    }

    public static Color rgb(int red, int green, int blue, double opacity) {
        checkRGB(red, green, blue);
        return new Color(red / 255.0d, green / 255.0d, blue / 255.0d, opacity);
    }

    public static Color rgb(int red, int green, int blue) {
        checkRGB(red, green, blue);
        return new Color(red / 255.0d, green / 255.0d, blue / 255.0d, 1.0d);
    }

    public static Color grayRgb(int gray) {
        return rgb(gray, gray, gray);
    }

    public static Color grayRgb(int gray, double opacity) {
        return rgb(gray, gray, gray, opacity);
    }

    public static Color gray(double gray, double opacity) {
        return new Color(gray, gray, gray, opacity);
    }

    public static Color gray(double gray) {
        return gray(gray, 1.0d);
    }

    private static void checkRGB(int red, int green, int blue) {
        if (red < 0 || red > 255) {
            throw new IllegalArgumentException("Color.rgb's red parameter (" + red + ") expects color values 0-255");
        }
        if (green < 0 || green > 255) {
            throw new IllegalArgumentException("Color.rgb's green parameter (" + green + ") expects color values 0-255");
        }
        if (blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Color.rgb's blue parameter (" + blue + ") expects color values 0-255");
        }
    }

    public static Color hsb(double hue, double saturation, double brightness, double opacity) {
        checkSB(saturation, brightness);
        double[] rgb = Utils.HSBtoRGB(hue, saturation, brightness);
        Color result = new Color(rgb[0], rgb[1], rgb[2], opacity);
        return result;
    }

    public static Color hsb(double hue, double saturation, double brightness) {
        return hsb(hue, saturation, brightness, 1.0d);
    }

    private static void checkSB(double saturation, double brightness) {
        if (saturation < 0.0d || saturation > 1.0d) {
            throw new IllegalArgumentException("Color.hsb's saturation parameter (" + saturation + ") expects values 0.0-1.0");
        }
        if (brightness < 0.0d || brightness > 1.0d) {
            throw new IllegalArgumentException("Color.hsb's brightness parameter (" + brightness + ") expects values 0.0-1.0");
        }
    }

    public static Color web(String colorString, double opacity) {
        if (colorString == null) {
            throw new NullPointerException("The color components or name must be specified");
        }
        if (colorString.isEmpty()) {
            throw new IllegalArgumentException("Invalid color specification");
        }
        String color = colorString.toLowerCase(Locale.ROOT);
        if (color.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
            color = color.substring(1);
        } else if (color.startsWith("0x")) {
            color = color.substring(2);
        } else if (color.startsWith("rgb")) {
            if (color.startsWith("(", 3)) {
                return parseRGBColor(color, 4, false, opacity);
            }
            if (color.startsWith("a(", 3)) {
                return parseRGBColor(color, 5, true, opacity);
            }
        } else if (color.startsWith("hsl")) {
            if (color.startsWith("(", 3)) {
                return parseHSLColor(color, 4, false, opacity);
            }
            if (color.startsWith("a(", 3)) {
                return parseHSLColor(color, 5, true, opacity);
            }
        } else {
            Color col = NamedColors.get(color);
            if (col != null) {
                if (opacity == 1.0d) {
                    return col;
                }
                return color(col.red, col.green, col.blue, opacity);
            }
        }
        int len = color.length();
        if (len == 3) {
            int r2 = Integer.parseInt(color.substring(0, 1), 16);
            int g2 = Integer.parseInt(color.substring(1, 2), 16);
            int b2 = Integer.parseInt(color.substring(2, 3), 16);
            return color(r2 / 15.0d, g2 / 15.0d, b2 / 15.0d, opacity);
        }
        if (len == 4) {
            int r3 = Integer.parseInt(color.substring(0, 1), 16);
            int g3 = Integer.parseInt(color.substring(1, 2), 16);
            int b3 = Integer.parseInt(color.substring(2, 3), 16);
            int a2 = Integer.parseInt(color.substring(3, 4), 16);
            return color(r3 / 15.0d, g3 / 15.0d, b3 / 15.0d, (opacity * a2) / 15.0d);
        }
        if (len == 6) {
            int r4 = Integer.parseInt(color.substring(0, 2), 16);
            int g4 = Integer.parseInt(color.substring(2, 4), 16);
            int b4 = Integer.parseInt(color.substring(4, 6), 16);
            return rgb(r4, g4, b4, opacity);
        }
        if (len == 8) {
            int r5 = Integer.parseInt(color.substring(0, 2), 16);
            int g5 = Integer.parseInt(color.substring(2, 4), 16);
            int b5 = Integer.parseInt(color.substring(4, 6), 16);
            int a3 = Integer.parseInt(color.substring(6, 8), 16);
            return rgb(r5, g5, b5, (opacity * a3) / 255.0d);
        }
        throw new IllegalArgumentException("Invalid color specification");
    }

    private static Color parseRGBColor(String color, int roff, boolean hasAlpha, double a2) {
        int iIndexOf;
        try {
            int rend = color.indexOf(44, roff);
            int gend = rend < 0 ? -1 : color.indexOf(44, rend + 1);
            if (gend < 0) {
                iIndexOf = -1;
            } else {
                iIndexOf = color.indexOf(hasAlpha ? 44 : 41, gend + 1);
            }
            int bend = iIndexOf;
            int aend = hasAlpha ? bend < 0 ? -1 : color.indexOf(41, bend + 1) : bend;
            if (aend >= 0) {
                double r2 = parseComponent(color, roff, rend, 0);
                double g2 = parseComponent(color, rend + 1, gend, 0);
                double b2 = parseComponent(color, gend + 1, bend, 0);
                if (hasAlpha) {
                    a2 *= parseComponent(color, bend + 1, aend, 3);
                }
                return new Color(r2, g2, b2, a2);
            }
        } catch (NumberFormatException e2) {
        }
        throw new IllegalArgumentException("Invalid color specification");
    }

    private static Color parseHSLColor(String color, int hoff, boolean hasAlpha, double a2) {
        int iIndexOf;
        try {
            int hend = color.indexOf(44, hoff);
            int send = hend < 0 ? -1 : color.indexOf(44, hend + 1);
            if (send < 0) {
                iIndexOf = -1;
            } else {
                iIndexOf = color.indexOf(hasAlpha ? 44 : 41, send + 1);
            }
            int lend = iIndexOf;
            int aend = hasAlpha ? lend < 0 ? -1 : color.indexOf(41, lend + 1) : lend;
            if (aend >= 0) {
                double h2 = parseComponent(color, hoff, hend, 2);
                double s2 = parseComponent(color, hend + 1, send, 1);
                double l2 = parseComponent(color, send + 1, lend, 1);
                if (hasAlpha) {
                    a2 *= parseComponent(color, lend + 1, aend, 3);
                }
                return hsb(h2, s2, l2, a2);
            }
        } catch (NumberFormatException e2) {
        }
        throw new IllegalArgumentException("Invalid color specification");
    }

    private static double parseComponent(String color, int off, int end, int type) throws NumberFormatException {
        double d2;
        String color2 = color.substring(off, end).trim();
        if (color2.endsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
            if (type > 1) {
                throw new IllegalArgumentException("Invalid color specification");
            }
            type = 1;
            color2 = color2.substring(0, color2.length() - 1).trim();
        } else if (type == 1) {
            throw new IllegalArgumentException("Invalid color specification");
        }
        if (type == 0) {
            d2 = Integer.parseInt(color2);
        } else {
            d2 = Double.parseDouble(color2);
        }
        double c2 = d2;
        switch (type) {
            case 0:
                if (c2 <= 0.0d) {
                    return 0.0d;
                }
                if (c2 >= 255.0d) {
                    return 1.0d;
                }
                return c2 / 255.0d;
            case 1:
                if (c2 <= 0.0d) {
                    return 0.0d;
                }
                if (c2 >= 100.0d) {
                    return 1.0d;
                }
                return c2 / 100.0d;
            case 2:
                return c2 < 0.0d ? (c2 % 360.0d) + 360.0d : c2 > 360.0d ? c2 % 360.0d : c2;
            case 3:
                if (c2 < 0.0d) {
                    return 0.0d;
                }
                if (c2 > 1.0d) {
                    return 1.0d;
                }
                return c2;
            default:
                throw new IllegalArgumentException("Invalid color specification");
        }
    }

    public static Color web(String colorString) {
        return web(colorString, 1.0d);
    }

    public static Color valueOf(String value) {
        if (value == null) {
            throw new NullPointerException("color must be specified");
        }
        return web(value);
    }

    private static int to32BitInteger(int red, int green, int blue, int alpha) {
        int i2 = red << 8;
        return ((((i2 | green) << 8) | blue) << 8) | alpha;
    }

    public double getHue() {
        return Utils.RGBtoHSB(this.red, this.green, this.blue)[0];
    }

    public double getSaturation() {
        return Utils.RGBtoHSB(this.red, this.green, this.blue)[1];
    }

    public double getBrightness() {
        return Utils.RGBtoHSB(this.red, this.green, this.blue)[2];
    }

    public Color deriveColor(double hueShift, double saturationFactor, double brightnessFactor, double opacityFactor) {
        double[] hsb = Utils.RGBtoHSB(this.red, this.green, this.blue);
        double b2 = hsb[2];
        if (b2 == 0.0d && brightnessFactor > 1.0d) {
            b2 = 0.05d;
        }
        double h2 = (((hsb[0] + hueShift) % 360.0d) + 360.0d) % 360.0d;
        double s2 = Math.max(Math.min(hsb[1] * saturationFactor, 1.0d), 0.0d);
        double b3 = Math.max(Math.min(b2 * brightnessFactor, 1.0d), 0.0d);
        double a2 = Math.max(Math.min(this.opacity * opacityFactor, 1.0d), 0.0d);
        return hsb(h2, s2, b3, a2);
    }

    public Color brighter() {
        return deriveColor(0.0d, 1.0d, 1.4285714285714286d, 1.0d);
    }

    public Color darker() {
        return deriveColor(0.0d, 1.0d, 0.7d, 1.0d);
    }

    public Color saturate() {
        return deriveColor(0.0d, 1.4285714285714286d, 1.0d, 1.0d);
    }

    public Color desaturate() {
        return deriveColor(0.0d, 0.7d, 1.0d, 1.0d);
    }

    public Color grayscale() {
        double gray = (0.21d * this.red) + (0.71d * this.green) + (0.07d * this.blue);
        return color(gray, gray, gray, this.opacity);
    }

    public Color invert() {
        return color(1.0d - this.red, 1.0d - this.green, 1.0d - this.blue, this.opacity);
    }

    /* loaded from: jfxrt.jar:javafx/scene/paint/Color$NamedColors.class */
    private static final class NamedColors {
        private static final Map<String, Color> namedColors = createNamedColors();

        private NamedColors() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Color get(String name) {
            return namedColors.get(name);
        }

        private static Map<String, Color> createNamedColors() {
            Map<String, Color> colors = new HashMap<>(256);
            colors.put("aliceblue", Color.ALICEBLUE);
            colors.put("antiquewhite", Color.ANTIQUEWHITE);
            colors.put("aqua", Color.AQUA);
            colors.put("aquamarine", Color.AQUAMARINE);
            colors.put("azure", Color.AZURE);
            colors.put("beige", Color.BEIGE);
            colors.put("bisque", Color.BISQUE);
            colors.put("black", Color.BLACK);
            colors.put("blanchedalmond", Color.BLANCHEDALMOND);
            colors.put("blue", Color.BLUE);
            colors.put("blueviolet", Color.BLUEVIOLET);
            colors.put("brown", Color.BROWN);
            colors.put("burlywood", Color.BURLYWOOD);
            colors.put("cadetblue", Color.CADETBLUE);
            colors.put("chartreuse", Color.CHARTREUSE);
            colors.put("chocolate", Color.CHOCOLATE);
            colors.put("coral", Color.CORAL);
            colors.put("cornflowerblue", Color.CORNFLOWERBLUE);
            colors.put("cornsilk", Color.CORNSILK);
            colors.put("crimson", Color.CRIMSON);
            colors.put("cyan", Color.CYAN);
            colors.put("darkblue", Color.DARKBLUE);
            colors.put("darkcyan", Color.DARKCYAN);
            colors.put("darkgoldenrod", Color.DARKGOLDENROD);
            colors.put("darkgray", Color.DARKGRAY);
            colors.put("darkgreen", Color.DARKGREEN);
            colors.put("darkgrey", Color.DARKGREY);
            colors.put("darkkhaki", Color.DARKKHAKI);
            colors.put("darkmagenta", Color.DARKMAGENTA);
            colors.put("darkolivegreen", Color.DARKOLIVEGREEN);
            colors.put("darkorange", Color.DARKORANGE);
            colors.put("darkorchid", Color.DARKORCHID);
            colors.put("darkred", Color.DARKRED);
            colors.put("darksalmon", Color.DARKSALMON);
            colors.put("darkseagreen", Color.DARKSEAGREEN);
            colors.put("darkslateblue", Color.DARKSLATEBLUE);
            colors.put("darkslategray", Color.DARKSLATEGRAY);
            colors.put("darkslategrey", Color.DARKSLATEGREY);
            colors.put("darkturquoise", Color.DARKTURQUOISE);
            colors.put("darkviolet", Color.DARKVIOLET);
            colors.put("deeppink", Color.DEEPPINK);
            colors.put("deepskyblue", Color.DEEPSKYBLUE);
            colors.put("dimgray", Color.DIMGRAY);
            colors.put("dimgrey", Color.DIMGREY);
            colors.put("dodgerblue", Color.DODGERBLUE);
            colors.put("firebrick", Color.FIREBRICK);
            colors.put("floralwhite", Color.FLORALWHITE);
            colors.put("forestgreen", Color.FORESTGREEN);
            colors.put("fuchsia", Color.FUCHSIA);
            colors.put("gainsboro", Color.GAINSBORO);
            colors.put("ghostwhite", Color.GHOSTWHITE);
            colors.put("gold", Color.GOLD);
            colors.put("goldenrod", Color.GOLDENROD);
            colors.put("gray", Color.GRAY);
            colors.put("green", Color.GREEN);
            colors.put("greenyellow", Color.GREENYELLOW);
            colors.put("grey", Color.GREY);
            colors.put("honeydew", Color.HONEYDEW);
            colors.put("hotpink", Color.HOTPINK);
            colors.put("indianred", Color.INDIANRED);
            colors.put("indigo", Color.INDIGO);
            colors.put("ivory", Color.IVORY);
            colors.put("khaki", Color.KHAKI);
            colors.put("lavender", Color.LAVENDER);
            colors.put("lavenderblush", Color.LAVENDERBLUSH);
            colors.put("lawngreen", Color.LAWNGREEN);
            colors.put("lemonchiffon", Color.LEMONCHIFFON);
            colors.put("lightblue", Color.LIGHTBLUE);
            colors.put("lightcoral", Color.LIGHTCORAL);
            colors.put("lightcyan", Color.LIGHTCYAN);
            colors.put("lightgoldenrodyellow", Color.LIGHTGOLDENRODYELLOW);
            colors.put("lightgray", Color.LIGHTGRAY);
            colors.put("lightgreen", Color.LIGHTGREEN);
            colors.put("lightgrey", Color.LIGHTGREY);
            colors.put("lightpink", Color.LIGHTPINK);
            colors.put("lightsalmon", Color.LIGHTSALMON);
            colors.put("lightseagreen", Color.LIGHTSEAGREEN);
            colors.put("lightskyblue", Color.LIGHTSKYBLUE);
            colors.put("lightslategray", Color.LIGHTSLATEGRAY);
            colors.put("lightslategrey", Color.LIGHTSLATEGREY);
            colors.put("lightsteelblue", Color.LIGHTSTEELBLUE);
            colors.put("lightyellow", Color.LIGHTYELLOW);
            colors.put("lime", Color.LIME);
            colors.put("limegreen", Color.LIMEGREEN);
            colors.put("linen", Color.LINEN);
            colors.put("magenta", Color.MAGENTA);
            colors.put("maroon", Color.MAROON);
            colors.put("mediumaquamarine", Color.MEDIUMAQUAMARINE);
            colors.put("mediumblue", Color.MEDIUMBLUE);
            colors.put("mediumorchid", Color.MEDIUMORCHID);
            colors.put("mediumpurple", Color.MEDIUMPURPLE);
            colors.put("mediumseagreen", Color.MEDIUMSEAGREEN);
            colors.put("mediumslateblue", Color.MEDIUMSLATEBLUE);
            colors.put("mediumspringgreen", Color.MEDIUMSPRINGGREEN);
            colors.put("mediumturquoise", Color.MEDIUMTURQUOISE);
            colors.put("mediumvioletred", Color.MEDIUMVIOLETRED);
            colors.put("midnightblue", Color.MIDNIGHTBLUE);
            colors.put("mintcream", Color.MINTCREAM);
            colors.put("mistyrose", Color.MISTYROSE);
            colors.put("moccasin", Color.MOCCASIN);
            colors.put("navajowhite", Color.NAVAJOWHITE);
            colors.put("navy", Color.NAVY);
            colors.put("oldlace", Color.OLDLACE);
            colors.put("olive", Color.OLIVE);
            colors.put("olivedrab", Color.OLIVEDRAB);
            colors.put("orange", Color.ORANGE);
            colors.put("orangered", Color.ORANGERED);
            colors.put("orchid", Color.ORCHID);
            colors.put("palegoldenrod", Color.PALEGOLDENROD);
            colors.put("palegreen", Color.PALEGREEN);
            colors.put("paleturquoise", Color.PALETURQUOISE);
            colors.put("palevioletred", Color.PALEVIOLETRED);
            colors.put("papayawhip", Color.PAPAYAWHIP);
            colors.put("peachpuff", Color.PEACHPUFF);
            colors.put("peru", Color.PERU);
            colors.put("pink", Color.PINK);
            colors.put("plum", Color.PLUM);
            colors.put("powderblue", Color.POWDERBLUE);
            colors.put("purple", Color.PURPLE);
            colors.put("red", Color.RED);
            colors.put("rosybrown", Color.ROSYBROWN);
            colors.put("royalblue", Color.ROYALBLUE);
            colors.put("saddlebrown", Color.SADDLEBROWN);
            colors.put("salmon", Color.SALMON);
            colors.put("sandybrown", Color.SANDYBROWN);
            colors.put("seagreen", Color.SEAGREEN);
            colors.put("seashell", Color.SEASHELL);
            colors.put("sienna", Color.SIENNA);
            colors.put("silver", Color.SILVER);
            colors.put("skyblue", Color.SKYBLUE);
            colors.put("slateblue", Color.SLATEBLUE);
            colors.put("slategray", Color.SLATEGRAY);
            colors.put("slategrey", Color.SLATEGREY);
            colors.put("snow", Color.SNOW);
            colors.put("springgreen", Color.SPRINGGREEN);
            colors.put("steelblue", Color.STEELBLUE);
            colors.put("tan", Color.TAN);
            colors.put("teal", Color.TEAL);
            colors.put("thistle", Color.THISTLE);
            colors.put("tomato", Color.TOMATO);
            colors.put("transparent", Color.TRANSPARENT);
            colors.put("turquoise", Color.TURQUOISE);
            colors.put("violet", Color.VIOLET);
            colors.put("wheat", Color.WHEAT);
            colors.put("white", Color.WHITE);
            colors.put("whitesmoke", Color.WHITESMOKE);
            colors.put("yellow", Color.YELLOW);
            colors.put("yellowgreen", Color.YELLOWGREEN);
            return colors;
        }
    }

    public final double getRed() {
        return this.red;
    }

    public final double getGreen() {
        return this.green;
    }

    public final double getBlue() {
        return this.blue;
    }

    public final double getOpacity() {
        return this.opacity;
    }

    @Override // javafx.scene.paint.Paint
    public final boolean isOpaque() {
        return this.opacity >= 1.0f;
    }

    public Color(@NamedArg("red") double red, @NamedArg("green") double green, @NamedArg("blue") double blue, @NamedArg(value = "opacity", defaultValue = "1") double opacity) {
        this.opacity = 1.0f;
        if (red < 0.0d || red > 1.0d) {
            throw new IllegalArgumentException("Color's red value (" + red + ") must be in the range 0.0-1.0");
        }
        if (green < 0.0d || green > 1.0d) {
            throw new IllegalArgumentException("Color's green value (" + green + ") must be in the range 0.0-1.0");
        }
        if (blue < 0.0d || blue > 1.0d) {
            throw new IllegalArgumentException("Color's blue value (" + blue + ") must be in the range 0.0-1.0");
        }
        if (opacity < 0.0d || opacity > 1.0d) {
            throw new IllegalArgumentException("Color's opacity value (" + opacity + ") must be in the range 0.0-1.0");
        }
        this.red = (float) red;
        this.green = (float) green;
        this.blue = (float) blue;
        this.opacity = (float) opacity;
    }

    private Color(float red, float green, float blue) {
        this.opacity = 1.0f;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override // javafx.scene.paint.Paint
    Object acc_getPlatformPaint() {
        if (this.platformPaint == null) {
            this.platformPaint = Toolkit.getToolkit().getPaint(this);
        }
        return this.platformPaint;
    }

    @Override // javafx.animation.Interpolatable
    public Color interpolate(Color endValue, double t2) {
        if (t2 <= 0.0d) {
            return this;
        }
        if (t2 >= 1.0d) {
            return endValue;
        }
        float ft = (float) t2;
        return new Color(this.red + ((endValue.red - this.red) * ft), this.green + ((endValue.green - this.green) * ft), this.blue + ((endValue.blue - this.blue) * ft), this.opacity + ((endValue.opacity - this.opacity) * ft));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Color) {
            Color other = (Color) obj;
            return this.red == other.red && this.green == other.green && this.blue == other.blue && this.opacity == other.opacity;
        }
        return false;
    }

    public int hashCode() {
        int r2 = (int) Math.round(this.red * 255.0d);
        int g2 = (int) Math.round(this.green * 255.0d);
        int b2 = (int) Math.round(this.blue * 255.0d);
        int a2 = (int) Math.round(this.opacity * 255.0d);
        return to32BitInteger(r2, g2, b2, a2);
    }

    public String toString() {
        int r2 = (int) Math.round(this.red * 255.0d);
        int g2 = (int) Math.round(this.green * 255.0d);
        int b2 = (int) Math.round(this.blue * 255.0d);
        int o2 = (int) Math.round(this.opacity * 255.0d);
        return String.format("0x%02x%02x%02x%02x", Integer.valueOf(r2), Integer.valueOf(g2), Integer.valueOf(b2), Integer.valueOf(o2));
    }
}
