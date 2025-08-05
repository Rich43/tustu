package com.sun.javafx.scene.paint;

import java.util.LinkedList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/paint/GradientUtils.class */
public class GradientUtils {
    public static String lengthToString(double value, boolean proportional) {
        if (proportional) {
            return (value * 100.0d) + FXMLLoader.RESOURCE_KEY_PREFIX;
        }
        return value + "px";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/paint/GradientUtils$Point.class */
    public static class Point {
        public static final Point MIN = new Point(0.0d, true);
        public static final Point MAX = new Point(1.0d, true);
        public double value;
        public boolean proportional;

        public String toString() {
            return "value = " + this.value + ", proportional = " + this.proportional;
        }

        public Point(double value, boolean proportional) {
            this.value = value;
            this.proportional = proportional;
        }

        public Point() {
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/paint/GradientUtils$Parser.class */
    public static class Parser {
        private String[] tokens;
        private boolean proportional;
        private boolean proportionalSet = false;
        private int index = 0;

        /* loaded from: jfxrt.jar:com/sun/javafx/scene/paint/GradientUtils$Parser$Delimiter.class */
        private interface Delimiter {
            boolean isDelimiter(char c2);
        }

        private String[] splitString(String string, Delimiter delimiter, boolean canRepeat) {
            List<String> tokenList = new LinkedList<>();
            StringBuilder token = new StringBuilder();
            int i2 = 0;
            char[] input = string.toCharArray();
            while (i2 < input.length) {
                char currentChar = input[i2];
                if (delimiter.isDelimiter(currentChar)) {
                    if (!canRepeat || token.length() > 0) {
                        tokenList.add(token.toString());
                    }
                    token.setLength(0);
                } else if (currentChar == '(') {
                    while (i2 < input.length) {
                        token.append(input[i2]);
                        if (input[i2] == ')') {
                            break;
                        }
                        i2++;
                    }
                } else {
                    token.append(input[i2]);
                }
                i2++;
            }
            if (!canRepeat || token.length() > 0) {
                tokenList.add(token.toString());
            }
            return (String[]) tokenList.toArray(new String[tokenList.size()]);
        }

        public Parser(String content) {
            this.tokens = splitString(content, value -> {
                return value == ',';
            }, false);
        }

        public int getSize() {
            return this.tokens.length;
        }

        public void shift() {
            this.index++;
        }

        public String getCurrentToken() {
            String currentToken = this.tokens[this.index].trim();
            if (currentToken.isEmpty()) {
                throw new IllegalArgumentException("Invalid gradient specification: found empty token.");
            }
            return currentToken;
        }

        public String[] splitCurrentToken() {
            return getCurrentToken().split("\\s");
        }

        public static void checkNumberOfArguments(String[] tokens, int count) {
            if (tokens.length < count + 1) {
                throw new IllegalArgumentException("Invalid gradient specification: parameter '" + tokens[0] + "' needs " + count + " argument(s).");
            }
        }

        public static double parseAngle(String value) {
            double angle;
            if (value.endsWith("deg")) {
                angle = Double.parseDouble(value.substring(0, value.length() - 3));
            } else if (value.endsWith("grad")) {
                double angle2 = Double.parseDouble(value.substring(0, value.length() - 4));
                angle = (angle2 * 9.0d) / 10.0d;
            } else if (value.endsWith("rad")) {
                double angle3 = Double.parseDouble(value.substring(0, value.length() - 3));
                angle = (angle3 * 180.0d) / 3.141592653589793d;
            } else if (value.endsWith("turn")) {
                double angle4 = Double.parseDouble(value.substring(0, value.length() - 4));
                angle = angle4 * 360.0d;
            } else {
                throw new IllegalArgumentException("Invalid gradient specification:angle must end in deg, rad, grad, or turn");
            }
            return angle;
        }

        public static double parsePercentage(String value) {
            if (value.endsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                double percentage = Double.parseDouble(value.substring(0, value.length() - 1)) / 100.0d;
                return percentage;
            }
            throw new IllegalArgumentException("Invalid gradient specification: focus-distance must be specified as percentage");
        }

        public Point parsePoint(String value) {
            Point p2 = new Point();
            if (value.endsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                p2.proportional = true;
                value = value.substring(0, value.length() - 1);
            } else if (value.endsWith("px")) {
                value = value.substring(0, value.length() - 2);
            }
            p2.value = Double.parseDouble(value);
            if (p2.proportional) {
                p2.value /= 100.0d;
            }
            if (this.proportionalSet && this.proportional != p2.proportional) {
                throw new IllegalArgumentException("Invalid gradient specification:cannot mix proportional and non-proportional values");
            }
            this.proportionalSet = true;
            this.proportional = p2.proportional;
            return p2;
        }

        public Stop[] parseStops(boolean proportional, double length) {
            int stopsCount = this.tokens.length - this.index;
            Color[] colors = new Color[stopsCount];
            double[] offsets = new double[stopsCount];
            Stop[] stops = new Stop[stopsCount];
            for (int i2 = 0; i2 < stopsCount; i2++) {
                String stopString = this.tokens[i2 + this.index].trim();
                String[] stopTokens = splitString(stopString, value -> {
                    return Character.isWhitespace(value);
                }, true);
                if (stopTokens.length == 0) {
                    throw new IllegalArgumentException("Invalid gradient specification, empty stop found");
                }
                String currentToken = stopTokens[0];
                double offset = -1.0d;
                Color c2 = Color.web(currentToken);
                if (stopTokens.length == 2) {
                    String o2 = stopTokens[1];
                    if (o2.endsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                        offset = Double.parseDouble(o2.substring(0, o2.length() - 1)) / 100.0d;
                    } else if (!proportional) {
                        if (o2.endsWith("px")) {
                            o2 = o2.substring(0, o2.length() - 2);
                        }
                        offset = Double.parseDouble(o2) / length;
                    } else {
                        throw new IllegalArgumentException("Invalid gradient specification, non-proportional stops not permited in proportional gradient: " + o2);
                    }
                } else if (stopTokens.length > 2) {
                    throw new IllegalArgumentException("Invalid gradient specification, unexpected content in stop specification: " + stopTokens[2]);
                }
                colors[i2] = c2;
                offsets[i2] = offset;
            }
            if (offsets[0] < 0.0d) {
                offsets[0] = 0.0d;
            }
            if (offsets[offsets.length - 1] < 0.0d) {
                offsets[offsets.length - 1] = 1.0d;
            }
            double max = offsets[0];
            for (int i3 = 1; i3 < offsets.length; i3++) {
                if (offsets[i3] < max && offsets[i3] > 0.0d) {
                    offsets[i3] = max;
                } else {
                    max = offsets[i3];
                }
            }
            int firstIndex = -1;
            for (int i4 = 1; i4 < offsets.length; i4++) {
                double offset2 = offsets[i4];
                if (offset2 < 0.0d && firstIndex < 0) {
                    firstIndex = i4;
                } else if (offset2 >= 0.0d && firstIndex > 0) {
                    int n2 = (i4 - firstIndex) + 1;
                    double part = (offsets[i4] - offsets[firstIndex - 1]) / n2;
                    for (int j2 = 0; j2 < n2 - 1; j2++) {
                        offsets[firstIndex + j2] = offsets[firstIndex - 1] + (part * (j2 + 1));
                    }
                }
            }
            for (int i5 = 0; i5 < stops.length; i5++) {
                Stop stop = new Stop(offsets[i5], colors[i5]);
                stops[i5] = stop;
            }
            return stops;
        }
    }
}
