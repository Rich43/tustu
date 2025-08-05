package org.apache.commons.math3.util;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/CompositeFormat.class */
public class CompositeFormat {
    private CompositeFormat() {
    }

    public static NumberFormat getDefaultNumberFormat() {
        return getDefaultNumberFormat(Locale.getDefault());
    }

    public static NumberFormat getDefaultNumberFormat(Locale locale) {
        NumberFormat nf = NumberFormat.getInstance(locale);
        nf.setMaximumFractionDigits(10);
        return nf;
    }

    public static void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
        parseNextCharacter(source, pos);
        pos.setIndex(pos.getIndex() - 1);
    }

    public static char parseNextCharacter(String source, ParsePosition pos) {
        char c2;
        int index = pos.getIndex();
        int n2 = source.length();
        char ret = 0;
        if (index < n2) {
            do {
                int i2 = index;
                index++;
                c2 = source.charAt(i2);
                if (!Character.isWhitespace(c2)) {
                    break;
                }
            } while (index < n2);
            pos.setIndex(index);
            if (index < n2) {
                ret = c2;
            }
        }
        return ret;
    }

    private static Number parseNumber(String source, double value, ParsePosition pos) {
        Number ret = null;
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(value);
        sb.append(')');
        int n2 = sb.length();
        int startIndex = pos.getIndex();
        int endIndex = startIndex + n2;
        if (endIndex < source.length() && source.substring(startIndex, endIndex).compareTo(sb.toString()) == 0) {
            ret = Double.valueOf(value);
            pos.setIndex(endIndex);
        }
        return ret;
    }

    public static Number parseNumber(String source, NumberFormat format, ParsePosition pos) {
        int startIndex = pos.getIndex();
        Number number = format.parse(source, pos);
        int endIndex = pos.getIndex();
        if (startIndex == endIndex) {
            double[] special = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
            for (double d2 : special) {
                number = parseNumber(source, d2, pos);
                if (number != null) {
                    break;
                }
            }
        }
        return number;
    }

    public static boolean parseFixedstring(String source, String expected, ParsePosition pos) {
        int startIndex = pos.getIndex();
        int endIndex = startIndex + expected.length();
        if (startIndex >= source.length() || endIndex > source.length() || source.substring(startIndex, endIndex).compareTo(expected) != 0) {
            pos.setIndex(startIndex);
            pos.setErrorIndex(startIndex);
            return false;
        }
        pos.setIndex(endIndex);
        return true;
    }

    public static StringBuffer formatDouble(double value, NumberFormat format, StringBuffer toAppendTo, FieldPosition pos) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            toAppendTo.append('(');
            toAppendTo.append(value);
            toAppendTo.append(')');
        } else {
            format.format(value, toAppendTo, pos);
        }
        return toAppendTo;
    }
}
