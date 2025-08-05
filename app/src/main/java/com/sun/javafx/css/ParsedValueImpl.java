package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/css/ParsedValueImpl.class */
public class ParsedValueImpl<V, T> extends ParsedValue<V, T> {
    private final boolean lookup;
    private final boolean containsLookups;
    private static int indent = 0;
    private int hash;
    private static final byte NULL_VALUE = 0;
    private static final byte VALUE = 1;
    private static final byte VALUE_ARRAY = 2;
    private static final byte ARRAY_OF_VALUE_ARRAY = 3;
    private static final byte STRING = 4;
    private static final byte COLOR = 5;
    private static final byte ENUM = 6;
    private static final byte BOOLEAN = 7;
    private static final byte URL = 8;
    private static final byte SIZE = 9;

    public final boolean isLookup() {
        return this.lookup;
    }

    public final boolean isContainsLookups() {
        return this.containsLookups;
    }

    private static boolean getContainsLookupsFlag(Object obj) {
        boolean containsLookupsFlag = false;
        if (obj instanceof Size) {
            containsLookupsFlag = false;
        } else if (obj instanceof ParsedValueImpl) {
            ParsedValueImpl value = (ParsedValueImpl) obj;
            containsLookupsFlag = value.lookup || value.containsLookups;
        } else if (obj instanceof ParsedValueImpl[]) {
            ParsedValueImpl[] values = (ParsedValueImpl[]) obj;
            for (int v2 = 0; v2 < values.length && !containsLookupsFlag; v2++) {
                if (values[v2] != null) {
                    containsLookupsFlag = containsLookupsFlag || values[v2].lookup || values[v2].containsLookups;
                }
            }
        } else if (obj instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] values2 = (ParsedValueImpl[][]) obj;
            for (int l2 = 0; l2 < values2.length && !containsLookupsFlag; l2++) {
                if (values2[l2] != null) {
                    for (int v3 = 0; v3 < values2[l2].length && !containsLookupsFlag; v3++) {
                        if (values2[l2][v3] != null) {
                            containsLookupsFlag = containsLookupsFlag || values2[l2][v3].lookup || values2[l2][v3].containsLookups;
                        }
                    }
                }
            }
        }
        return containsLookupsFlag;
    }

    public static boolean containsFontRelativeSize(ParsedValue parsedValue, boolean percentUnitsAreRelative) {
        boolean z2;
        boolean needsFont = false;
        Object obj = parsedValue.getValue();
        if (obj instanceof Size) {
            Size size = (Size) obj;
            if (size.getUnits() == SizeUnits.PERCENT) {
                z2 = percentUnitsAreRelative;
            } else {
                z2 = !size.isAbsolute();
            }
            needsFont = z2;
        } else if (obj instanceof ParsedValue) {
            ParsedValue value = (ParsedValueImpl) obj;
            needsFont = containsFontRelativeSize(value, percentUnitsAreRelative);
        } else if (obj instanceof ParsedValue[]) {
            ParsedValue[] values = (ParsedValue[]) obj;
            for (int v2 = 0; v2 < values.length && !needsFont; v2++) {
                if (values[v2] != null) {
                    needsFont = containsFontRelativeSize(values[v2], percentUnitsAreRelative);
                }
            }
        } else if (obj instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] values2 = (ParsedValueImpl[][]) obj;
            for (int l2 = 0; l2 < values2.length && !needsFont; l2++) {
                if (values2[l2] != null) {
                    for (int v3 = 0; v3 < values2[l2].length && !needsFont; v3++) {
                        if (values2[l2][v3] != null) {
                            needsFont = containsFontRelativeSize(values2[l2][v3], percentUnitsAreRelative);
                        }
                    }
                }
            }
        }
        return needsFont;
    }

    public ParsedValueImpl(V value, StyleConverter<V, T> converter, boolean lookup) {
        super(value, converter);
        this.hash = Integer.MIN_VALUE;
        this.lookup = lookup;
        this.containsLookups = lookup || getContainsLookupsFlag(value);
    }

    public ParsedValueImpl(V value, StyleConverter<V, T> type) {
        this(value, type, false);
    }

    @Override // javafx.css.ParsedValue
    public T convert(Font font) {
        return this.converter != null ? this.converter.convert(this, font) : this.value;
    }

    private static String spaces() {
        return new String(new char[indent]).replace((char) 0, ' ');
    }

    private static void indent() {
        indent += 2;
    }

    private static void outdent() {
        indent = Math.max(0, indent - 2);
    }

    public String toString() {
        String newline = System.lineSeparator();
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(spaces()).append(this.lookup ? "<Value lookup=\"true\">" : "<Value>").append(newline);
        indent();
        if (this.value != null) {
            appendValue(sbuf, this.value, "value");
        } else {
            appendValue(sbuf, FXMLLoader.NULL_KEYWORD, "value");
        }
        sbuf.append(spaces()).append("<converter>").append((Object) this.converter).append("</converter>").append(newline);
        outdent();
        sbuf.append(spaces()).append("</Value>");
        return sbuf.toString();
    }

    private void appendValue(StringBuilder sbuf, Object value, String tag) {
        String newline = System.lineSeparator();
        if (value instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] layers = (ParsedValueImpl[][]) value;
            sbuf.append(spaces()).append('<').append(tag).append(" layers=\"").append(layers.length).append("\">").append(newline);
            indent();
            for (ParsedValueImpl[] layer : layers) {
                sbuf.append(spaces()).append("<layer>").append(newline);
                indent();
                if (layer == null) {
                    sbuf.append(spaces()).append(FXMLLoader.NULL_KEYWORD).append(newline);
                } else {
                    for (ParsedValueImpl val : layer) {
                        if (val == null) {
                            sbuf.append(spaces()).append(FXMLLoader.NULL_KEYWORD).append(newline);
                        } else {
                            sbuf.append((Object) val);
                        }
                    }
                    outdent();
                    sbuf.append(spaces()).append("</layer>").append(newline);
                }
            }
            outdent();
            sbuf.append(spaces()).append("</").append(tag).append('>').append(newline);
            return;
        }
        if (!(value instanceof ParsedValueImpl[])) {
            if (value instanceof ParsedValueImpl) {
                sbuf.append(spaces()).append('<').append(tag).append('>').append(newline);
                indent();
                sbuf.append(value);
                outdent();
                sbuf.append(spaces()).append("</").append(tag).append('>').append(newline);
                return;
            }
            sbuf.append(spaces()).append('<').append(tag).append('>');
            sbuf.append(value);
            sbuf.append("</").append(tag).append('>').append(newline);
            return;
        }
        ParsedValueImpl[] values = (ParsedValueImpl[]) value;
        sbuf.append(spaces()).append('<').append(tag).append(" values=\"").append(values.length).append("\">").append(newline);
        indent();
        for (ParsedValueImpl val2 : values) {
            if (val2 == null) {
                sbuf.append(spaces()).append(FXMLLoader.NULL_KEYWORD).append(newline);
            } else {
                sbuf.append((Object) val2);
            }
        }
        outdent();
        sbuf.append(spaces()).append("</").append(tag).append('>').append(newline);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        ParsedValueImpl parsedValueImpl = (ParsedValueImpl) obj;
        if (this.hash != parsedValueImpl.hash) {
            return false;
        }
        if (this.value instanceof ParsedValueImpl[][]) {
            if (!(parsedValueImpl.value instanceof ParsedValueImpl[][])) {
                return false;
            }
            ParsedValueImpl[][] parsedValueImplArr = (ParsedValueImpl[][]) this.value;
            ParsedValueImpl[][] parsedValueImplArr2 = (ParsedValueImpl[][]) parsedValueImpl.value;
            if (parsedValueImplArr.length != parsedValueImplArr2.length) {
                return false;
            }
            for (int i2 = 0; i2 < parsedValueImplArr.length; i2++) {
                if (parsedValueImplArr[i2] != null || parsedValueImplArr2[i2] != null) {
                    if (parsedValueImplArr[i2] == null || parsedValueImplArr2[i2] == null || parsedValueImplArr[i2].length != parsedValueImplArr2[i2].length) {
                        return false;
                    }
                    for (int i3 = 0; i3 < parsedValueImplArr[i2].length; i3++) {
                        ParsedValueImpl parsedValueImpl2 = parsedValueImplArr[i2][i3];
                        ParsedValueImpl parsedValueImpl3 = parsedValueImplArr2[i2][i3];
                        if (parsedValueImpl2 != null) {
                            if (!parsedValueImpl2.equals(parsedValueImpl3)) {
                                return false;
                            }
                        } else if (parsedValueImpl3 != null) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        if (this.value instanceof ParsedValueImpl[]) {
            if (!(parsedValueImpl.value instanceof ParsedValueImpl[])) {
                return false;
            }
            ParsedValueImpl[] parsedValueImplArr3 = (ParsedValueImpl[]) this.value;
            ParsedValueImpl[] parsedValueImplArr4 = (ParsedValueImpl[]) parsedValueImpl.value;
            if (parsedValueImplArr3.length != parsedValueImplArr4.length) {
                return false;
            }
            for (int i4 = 0; i4 < parsedValueImplArr3.length; i4++) {
                ParsedValueImpl parsedValueImpl4 = parsedValueImplArr3[i4];
                ParsedValueImpl parsedValueImpl5 = parsedValueImplArr4[i4];
                if (parsedValueImpl4 != null) {
                    if (!parsedValueImpl4.equals(parsedValueImpl5)) {
                        return false;
                    }
                } else if (parsedValueImpl5 != null) {
                    return false;
                }
            }
            return true;
        }
        if ((this.value instanceof String) && (parsedValueImpl.value instanceof String)) {
            return this.value.toString().equalsIgnoreCase(parsedValueImpl.value.toString());
        }
        if (this.value != null) {
            return this.value.equals(parsedValueImpl.value);
        }
        return parsedValueImpl.value == null;
    }

    public int hashCode() {
        if (this.hash == Integer.MIN_VALUE) {
            this.hash = 17;
            if (this.value instanceof ParsedValueImpl[][]) {
                ParsedValueImpl[][] parsedValueImplArr = (ParsedValueImpl[][]) this.value;
                for (int i2 = 0; i2 < parsedValueImplArr.length; i2++) {
                    for (int i3 = 0; i3 < parsedValueImplArr[i2].length; i3++) {
                        ParsedValueImpl parsedValueImpl = parsedValueImplArr[i2][i3];
                        this.hash = (37 * this.hash) + ((parsedValueImpl == null || parsedValueImpl.value == null) ? 0 : parsedValueImpl.value.hashCode());
                    }
                }
            } else if (this.value instanceof ParsedValueImpl[]) {
                ParsedValueImpl[] parsedValueImplArr2 = (ParsedValueImpl[]) this.value;
                for (int i4 = 0; i4 < parsedValueImplArr2.length; i4++) {
                    if (parsedValueImplArr2[i4] != null && parsedValueImplArr2[i4].value != null) {
                        ParsedValueImpl parsedValueImpl2 = parsedValueImplArr2[i4];
                        this.hash = (37 * this.hash) + ((parsedValueImpl2 == null || parsedValueImpl2.value == null) ? 0 : parsedValueImpl2.value.hashCode());
                    }
                }
            } else {
                this.hash = (37 * this.hash) + (this.value != null ? this.value.hashCode() : 0);
            }
        }
        return this.hash;
    }

    public final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        dataOutputStream.writeBoolean(this.lookup);
        if (this.converter instanceof StyleConverterImpl) {
            dataOutputStream.writeBoolean(true);
            ((StyleConverterImpl) this.converter).writeBinary(dataOutputStream, stringStore);
        } else {
            dataOutputStream.writeBoolean(false);
            if (this.converter != null) {
                System.err.println("cannot writeBinary " + this.converter.getClass().getName());
            }
        }
        if (this.value instanceof ParsedValue) {
            dataOutputStream.writeByte(1);
            ParsedValue parsedValue = (ParsedValue) this.value;
            if (parsedValue instanceof ParsedValueImpl) {
                ((ParsedValueImpl) parsedValue).writeBinary(dataOutputStream, stringStore);
                return;
            } else {
                new ParsedValueImpl(parsedValue.getValue(), parsedValue.getConverter()).writeBinary(dataOutputStream, stringStore);
                return;
            }
        }
        if (this.value instanceof ParsedValue[]) {
            dataOutputStream.writeByte(2);
            ParsedValue[] parsedValueArr = (ParsedValue[]) this.value;
            if (parsedValueArr != null) {
                dataOutputStream.writeByte(1);
            } else {
                dataOutputStream.writeByte(0);
            }
            int length = parsedValueArr != null ? parsedValueArr.length : 0;
            dataOutputStream.writeInt(length);
            for (int i2 = 0; i2 < length; i2++) {
                if (parsedValueArr[i2] != null) {
                    dataOutputStream.writeByte(1);
                    ParsedValue parsedValue2 = parsedValueArr[i2];
                    if (parsedValue2 instanceof ParsedValueImpl) {
                        ((ParsedValueImpl) parsedValue2).writeBinary(dataOutputStream, stringStore);
                    } else {
                        new ParsedValueImpl(parsedValue2.getValue(), parsedValue2.getConverter()).writeBinary(dataOutputStream, stringStore);
                    }
                } else {
                    dataOutputStream.writeByte(0);
                }
            }
            return;
        }
        if (this.value instanceof ParsedValue[][]) {
            dataOutputStream.writeByte(3);
            ParsedValue[][] parsedValueArr2 = (ParsedValue[][]) this.value;
            if (parsedValueArr2 != null) {
                dataOutputStream.writeByte(1);
            } else {
                dataOutputStream.writeByte(0);
            }
            int length2 = parsedValueArr2 != null ? parsedValueArr2.length : 0;
            dataOutputStream.writeInt(length2);
            for (int i3 = 0; i3 < length2; i3++) {
                ParsedValue[] parsedValueArr3 = parsedValueArr2[i3];
                if (parsedValueArr3 != null) {
                    dataOutputStream.writeByte(1);
                } else {
                    dataOutputStream.writeByte(0);
                }
                int length3 = parsedValueArr3 != null ? parsedValueArr3.length : 0;
                dataOutputStream.writeInt(length3);
                for (int i4 = 0; i4 < length3; i4++) {
                    if (parsedValueArr3[i4] != null) {
                        dataOutputStream.writeByte(1);
                        ParsedValue parsedValue3 = parsedValueArr3[i4];
                        if (parsedValue3 instanceof ParsedValueImpl) {
                            ((ParsedValueImpl) parsedValue3).writeBinary(dataOutputStream, stringStore);
                        } else {
                            new ParsedValueImpl(parsedValue3.getValue(), parsedValue3.getConverter()).writeBinary(dataOutputStream, stringStore);
                        }
                    } else {
                        dataOutputStream.writeByte(0);
                    }
                }
            }
            return;
        }
        if (this.value instanceof Color) {
            Color color = (Color) this.value;
            dataOutputStream.writeByte(5);
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getRed()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getGreen()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getBlue()));
            dataOutputStream.writeLong(Double.doubleToLongBits(color.getOpacity()));
            return;
        }
        if (this.value instanceof Enum) {
            int iAddString = stringStore.addString(((Enum) this.value).name());
            dataOutputStream.writeByte(6);
            dataOutputStream.writeShort(iAddString);
            return;
        }
        if (this.value instanceof Boolean) {
            Boolean bool = (Boolean) this.value;
            dataOutputStream.writeByte(7);
            dataOutputStream.writeBoolean(bool.booleanValue());
            return;
        }
        if (this.value instanceof Size) {
            Size size = (Size) this.value;
            dataOutputStream.writeByte(9);
            dataOutputStream.writeLong(Double.doubleToLongBits(size.getValue()));
            dataOutputStream.writeShort(stringStore.addString(size.getUnits().name()));
            return;
        }
        if (this.value instanceof String) {
            dataOutputStream.writeByte(4);
            dataOutputStream.writeShort(stringStore.addString((String) this.value));
        } else if (this.value instanceof URL) {
            dataOutputStream.writeByte(8);
            dataOutputStream.writeShort(stringStore.addString(this.value.toString()));
        } else {
            if (this.value == null) {
                dataOutputStream.writeByte(0);
                return;
            }
            throw new InternalError("cannot writeBinary " + ((Object) this));
        }
    }

    public static ParsedValueImpl readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
        boolean lookup = is.readBoolean();
        boolean hasType = is.readBoolean();
        StyleConverter converter = hasType ? StyleConverterImpl.readBinary(is, strings) : null;
        int valType = is.readByte();
        if (valType == 1) {
            ParsedValueImpl value = readBinary(bssVersion, is, strings);
            return new ParsedValueImpl(value, converter, lookup);
        }
        if (valType == 2) {
            if (bssVersion >= 4) {
                is.readByte();
            }
            int nVals = is.readInt();
            ParsedValueImpl[] values = nVals > 0 ? new ParsedValueImpl[nVals] : null;
            for (int v2 = 0; v2 < nVals; v2++) {
                int vtype = is.readByte();
                if (vtype == 1) {
                    values[v2] = readBinary(bssVersion, is, strings);
                } else {
                    values[v2] = null;
                }
            }
            return new ParsedValueImpl(values, converter, lookup);
        }
        if (valType == 3) {
            if (bssVersion >= 4) {
                is.readByte();
            }
            int nLayers = is.readInt();
            ParsedValueImpl[][] layers = nLayers > 0 ? new ParsedValueImpl[nLayers][0] : (ParsedValueImpl[][]) null;
            for (int l2 = 0; l2 < nLayers; l2++) {
                if (bssVersion >= 4) {
                    is.readByte();
                }
                int nVals2 = is.readInt();
                layers[l2] = nVals2 > 0 ? new ParsedValueImpl[nVals2] : null;
                for (int v3 = 0; v3 < nVals2; v3++) {
                    int vtype2 = is.readByte();
                    if (vtype2 == 1) {
                        layers[l2][v3] = readBinary(bssVersion, is, strings);
                    } else {
                        layers[l2][v3] = null;
                    }
                }
            }
            return new ParsedValueImpl(layers, converter, lookup);
        }
        if (valType == 5) {
            double r2 = Double.longBitsToDouble(is.readLong());
            double g2 = Double.longBitsToDouble(is.readLong());
            double b2 = Double.longBitsToDouble(is.readLong());
            double a2 = Double.longBitsToDouble(is.readLong());
            return new ParsedValueImpl(Color.color(r2, g2, b2, a2), converter, lookup);
        }
        if (valType == 6) {
            int nameIndex = is.readShort();
            String ename = strings[nameIndex];
            if (bssVersion == 2) {
                int bad = is.readShort();
                if (bad >= strings.length) {
                    throw new IllegalArgumentException("bad version " + bssVersion);
                }
            }
            ParsedValueImpl value2 = new ParsedValueImpl(ename, converter, lookup);
            return value2;
        }
        if (valType == 7) {
            Boolean b3 = Boolean.valueOf(is.readBoolean());
            return new ParsedValueImpl(b3, converter, lookup);
        }
        if (valType == 9) {
            double val = Double.longBitsToDouble(is.readLong());
            SizeUnits units = SizeUnits.PX;
            String unitStr = strings[is.readShort()];
            try {
                units = (SizeUnits) Enum.valueOf(SizeUnits.class, unitStr);
            } catch (IllegalArgumentException iae) {
                System.err.println(iae.toString());
            } catch (NullPointerException npe) {
                System.err.println(npe.toString());
            }
            return new ParsedValueImpl(new Size(val, units), converter, lookup);
        }
        if (valType == 4) {
            String str = strings[is.readShort()];
            return new ParsedValueImpl(str, converter, lookup);
        }
        if (valType == 8) {
            String str2 = strings[is.readShort()];
            try {
                URL url = new URL(str2);
                return new ParsedValueImpl(url, converter, lookup);
            } catch (MalformedURLException malf) {
                throw new InternalError("Excpeption in Value.readBinary: " + ((Object) malf));
            }
        }
        if (valType == 0) {
            return new ParsedValueImpl(null, converter, lookup);
        }
        throw new InternalError("unknown type: " + valType);
    }
}
