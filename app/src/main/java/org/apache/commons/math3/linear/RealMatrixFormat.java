package org.apache.commons.math3.linear;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.util.CompositeFormat;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RealMatrixFormat.class */
public class RealMatrixFormat {
    private static final String DEFAULT_PREFIX = "{";
    private static final String DEFAULT_SUFFIX = "}";
    private static final String DEFAULT_ROW_PREFIX = "{";
    private static final String DEFAULT_ROW_SUFFIX = "}";
    private static final String DEFAULT_ROW_SEPARATOR = ",";
    private static final String DEFAULT_COLUMN_SEPARATOR = ",";
    private final String prefix;
    private final String suffix;
    private final String rowPrefix;
    private final String rowSuffix;
    private final String rowSeparator;
    private final String columnSeparator;
    private final NumberFormat format;

    public RealMatrixFormat() {
        this(VectorFormat.DEFAULT_PREFIX, "}", VectorFormat.DEFAULT_PREFIX, "}", ",", ",", CompositeFormat.getDefaultNumberFormat());
    }

    public RealMatrixFormat(NumberFormat format) {
        this(VectorFormat.DEFAULT_PREFIX, "}", VectorFormat.DEFAULT_PREFIX, "}", ",", ",", format);
    }

    public RealMatrixFormat(String prefix, String suffix, String rowPrefix, String rowSuffix, String rowSeparator, String columnSeparator) {
        this(prefix, suffix, rowPrefix, rowSuffix, rowSeparator, columnSeparator, CompositeFormat.getDefaultNumberFormat());
    }

    public RealMatrixFormat(String prefix, String suffix, String rowPrefix, String rowSuffix, String rowSeparator, String columnSeparator, NumberFormat format) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.rowPrefix = rowPrefix;
        this.rowSuffix = rowSuffix;
        this.rowSeparator = rowSeparator;
        this.columnSeparator = columnSeparator;
        this.format = format;
        this.format.setGroupingUsed(false);
    }

    public static Locale[] getAvailableLocales() {
        return NumberFormat.getAvailableLocales();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public String getRowPrefix() {
        return this.rowPrefix;
    }

    public String getRowSuffix() {
        return this.rowSuffix;
    }

    public String getRowSeparator() {
        return this.rowSeparator;
    }

    public String getColumnSeparator() {
        return this.columnSeparator;
    }

    public NumberFormat getFormat() {
        return this.format;
    }

    public static RealMatrixFormat getInstance() {
        return getInstance(Locale.getDefault());
    }

    public static RealMatrixFormat getInstance(Locale locale) {
        return new RealMatrixFormat(CompositeFormat.getDefaultNumberFormat(locale));
    }

    public String format(RealMatrix m2) {
        return format(m2, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public StringBuffer format(RealMatrix matrix, StringBuffer toAppendTo, FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        toAppendTo.append(this.prefix);
        int rows = matrix.getRowDimension();
        for (int i2 = 0; i2 < rows; i2++) {
            toAppendTo.append(this.rowPrefix);
            for (int j2 = 0; j2 < matrix.getColumnDimension(); j2++) {
                if (j2 > 0) {
                    toAppendTo.append(this.columnSeparator);
                }
                CompositeFormat.formatDouble(matrix.getEntry(i2, j2), this.format, toAppendTo, pos);
            }
            toAppendTo.append(this.rowSuffix);
            if (i2 < rows - 1) {
                toAppendTo.append(this.rowSeparator);
            }
        }
        toAppendTo.append(this.suffix);
        return toAppendTo;
    }

    public RealMatrix parse(String source) {
        ParsePosition parsePosition = new ParsePosition(0);
        RealMatrix result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw new MathParseException(source, parsePosition.getErrorIndex(), Array2DRowRealMatrix.class);
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v36, types: [double[], double[][]] */
    public RealMatrix parse(String source, ParsePosition pos) {
        int initialIndex = pos.getIndex();
        String trimmedPrefix = this.prefix.trim();
        String trimmedSuffix = this.suffix.trim();
        String trimmedRowPrefix = this.rowPrefix.trim();
        String trimmedRowSuffix = this.rowSuffix.trim();
        String trimmedColumnSeparator = this.columnSeparator.trim();
        String trimmedRowSeparator = this.rowSeparator.trim();
        CompositeFormat.parseAndIgnoreWhitespace(source, pos);
        if (!CompositeFormat.parseFixedstring(source, trimmedPrefix, pos)) {
            return null;
        }
        List<List<Number>> matrix = new ArrayList<>();
        List<Number> rowComponents = new ArrayList<>();
        boolean loop = true;
        while (loop) {
            if (!rowComponents.isEmpty()) {
                CompositeFormat.parseAndIgnoreWhitespace(source, pos);
                if (!CompositeFormat.parseFixedstring(source, trimmedColumnSeparator, pos)) {
                    if (trimmedRowSuffix.length() != 0 && !CompositeFormat.parseFixedstring(source, trimmedRowSuffix, pos)) {
                        return null;
                    }
                    CompositeFormat.parseAndIgnoreWhitespace(source, pos);
                    if (CompositeFormat.parseFixedstring(source, trimmedRowSeparator, pos)) {
                        matrix.add(rowComponents);
                        rowComponents = new ArrayList<>();
                    } else {
                        loop = false;
                    }
                }
            } else {
                CompositeFormat.parseAndIgnoreWhitespace(source, pos);
                if (trimmedRowPrefix.length() != 0 && !CompositeFormat.parseFixedstring(source, trimmedRowPrefix, pos)) {
                    return null;
                }
            }
            if (loop) {
                CompositeFormat.parseAndIgnoreWhitespace(source, pos);
                Number component = CompositeFormat.parseNumber(source, this.format, pos);
                if (component != null) {
                    rowComponents.add(component);
                } else if (rowComponents.isEmpty()) {
                    loop = false;
                } else {
                    pos.setIndex(initialIndex);
                    return null;
                }
            } else {
                continue;
            }
        }
        if (!rowComponents.isEmpty()) {
            matrix.add(rowComponents);
        }
        CompositeFormat.parseAndIgnoreWhitespace(source, pos);
        if (!CompositeFormat.parseFixedstring(source, trimmedSuffix, pos)) {
            return null;
        }
        if (matrix.isEmpty()) {
            pos.setIndex(initialIndex);
            return null;
        }
        ?? r0 = new double[matrix.size()];
        int row = 0;
        for (List<Number> rowList : matrix) {
            r0[row] = new double[rowList.size()];
            for (int i2 = 0; i2 < rowList.size(); i2++) {
                r0[row][i2] = rowList.get(i2).doubleValue();
            }
            row++;
        }
        return MatrixUtils.createRealMatrix(r0);
    }
}
