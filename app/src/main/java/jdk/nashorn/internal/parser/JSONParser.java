package jdk.nashorn.internal.parser;

import java.util.ArrayList;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.SpillProperty;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import jdk.nashorn.internal.scripts.JD;
import jdk.nashorn.internal.scripts.JO;
import org.icepdf.core.util.PdfOps;

/* loaded from: nashorn.jar:jdk/nashorn/internal/parser/JSONParser.class */
public class JSONParser {
    private final String source;
    private final Global global;
    private final boolean dualFields;
    final int length;
    int pos = 0;
    private static final int EOF = -1;
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String NULL = "null";
    private static final int STATE_EMPTY = 0;
    private static final int STATE_ELEMENT_PARSED = 1;
    private static final int STATE_COMMA_PARSED = 2;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JSONParser.class.desiredAssertionStatus();
    }

    public JSONParser(String source, Global global, boolean dualFields) {
        this.source = source;
        this.global = global;
        this.length = source.length();
        this.dualFields = dualFields;
    }

    public static String quote(String value) {
        StringBuilder product = new StringBuilder();
        product.append(PdfOps.DOUBLE_QUOTE__TOKEN);
        for (char ch : value.toCharArray()) {
            switch (ch) {
                case '\b':
                    product.append("\\b");
                    break;
                case '\t':
                    product.append("\\t");
                    break;
                case '\n':
                    product.append("\\n");
                    break;
                case '\f':
                    product.append("\\f");
                    break;
                case '\r':
                    product.append("\\r");
                    break;
                case '\"':
                    product.append("\\\"");
                    break;
                case '\\':
                    product.append("\\\\");
                    break;
                default:
                    if (ch < ' ') {
                        product.append(Lexer.unicodeEscape(ch));
                        break;
                    } else {
                        product.append(ch);
                        break;
                    }
            }
        }
        product.append(PdfOps.DOUBLE_QUOTE__TOKEN);
        return product.toString();
    }

    public Object parse() {
        Object value = parseLiteral();
        skipWhiteSpace();
        if (this.pos < this.length) {
            throw expectedError(this.pos, "eof", toString(peek()));
        }
        return value;
    }

    private Object parseLiteral() {
        skipWhiteSpace();
        int c2 = peek();
        if (c2 == -1) {
            throw expectedError(this.pos, "json literal", "eof");
        }
        switch (c2) {
            case 34:
                return parseString();
            case 91:
                return parseArray();
            case 102:
                return parseKeyword("false", Boolean.FALSE);
            case 110:
                return parseKeyword("null", null);
            case 116:
                return parseKeyword("true", Boolean.TRUE);
            case 123:
                return parseObject();
            default:
                if (isDigit(c2) || c2 == 45) {
                    return parseNumber();
                }
                if (c2 == 46) {
                    throw numberError(this.pos);
                }
                throw expectedError(this.pos, "json literal", toString(c2));
        }
    }

    private Object parseObject() {
        PropertyMap propertyMap = this.dualFields ? JD.getInitialMap() : JO.getInitialMap();
        ArrayData arrayData = ArrayData.EMPTY_ARRAY;
        ArrayList<Object> values = new ArrayList<>();
        int state = 0;
        if (!$assertionsDisabled && peek() != 123) {
            throw new AssertionError();
        }
        this.pos++;
        while (this.pos < this.length) {
            skipWhiteSpace();
            int c2 = peek();
            switch (c2) {
                case 34:
                    if (state == 1) {
                        throw expectedError(this.pos - 1, ", or }", toString(c2));
                    }
                    String id = parseString();
                    expectColon();
                    Object value = parseLiteral();
                    int index = ArrayIndex.getArrayIndex(id);
                    if (ArrayIndex.isValidArrayIndex(index)) {
                        arrayData = addArrayElement(arrayData, index, value);
                    } else {
                        propertyMap = addObjectProperty(propertyMap, values, id, value);
                    }
                    state = 1;
                    break;
                case 44:
                    if (state != 1) {
                        throw error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    state = 2;
                    this.pos++;
                    break;
                case 125:
                    if (state == 2) {
                        throw error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    this.pos++;
                    return createObject(propertyMap, values, arrayData);
                default:
                    throw expectedError(this.pos, ", or }", toString(c2));
            }
        }
        throw expectedError(this.pos, ", or }", "eof");
    }

    private static ArrayData addArrayElement(ArrayData arrayData, int index, Object value) {
        long oldLength = arrayData.length();
        long longIndex = ArrayIndex.toLongIndex(index);
        ArrayData newArrayData = arrayData;
        if (longIndex >= oldLength) {
            newArrayData = newArrayData.ensure(longIndex);
            if (longIndex > oldLength) {
                newArrayData = newArrayData.delete(oldLength, longIndex - 1);
            }
        }
        return newArrayData.set(index, value, false);
    }

    private PropertyMap addObjectProperty(PropertyMap propertyMap, List<Object> values, String id, Object value) {
        Class<?> type;
        int flags;
        PropertyMap newMap;
        Property oldProperty = propertyMap.findProperty(id);
        if (this.dualFields) {
            type = getType(value);
            flags = 2048;
        } else {
            type = Object.class;
            flags = 0;
        }
        if (oldProperty != null) {
            values.set(oldProperty.getSlot(), value);
            newMap = propertyMap.replaceProperty(oldProperty, new SpillProperty(id, flags, oldProperty.getSlot(), type));
        } else {
            values.add(value);
            newMap = propertyMap.addProperty(new SpillProperty(id, flags, propertyMap.size(), type));
        }
        return newMap;
    }

    private Object createObject(PropertyMap propertyMap, List<Object> values, ArrayData arrayData) {
        long[] primitiveSpill = this.dualFields ? new long[values.size()] : null;
        Object[] objectSpill = new Object[values.size()];
        for (Property property : propertyMap.getProperties()) {
            if (!this.dualFields || property.getType() == Object.class) {
                objectSpill[property.getSlot()] = values.get(property.getSlot());
            } else {
                primitiveSpill[property.getSlot()] = ObjectClassGenerator.pack((Number) values.get(property.getSlot()));
            }
        }
        ScriptObject object = this.dualFields ? new JD(propertyMap, primitiveSpill, objectSpill) : new JO(propertyMap, null, objectSpill);
        object.setInitialProto(this.global.getObjectPrototype());
        object.setArray(arrayData);
        return object;
    }

    private static Class<?> getType(Object value) {
        if (value instanceof Integer) {
            return Integer.TYPE;
        }
        if (value instanceof Double) {
            return Double.TYPE;
        }
        return Object.class;
    }

    private void expectColon() {
        skipWhiteSpace();
        int n2 = next();
        if (n2 != 58) {
            throw expectedError(this.pos - 1, CallSiteDescriptor.TOKEN_DELIMITER, toString(n2));
        }
    }

    private Object parseArray() {
        ArrayData arrayData = ArrayData.EMPTY_ARRAY;
        int state = 0;
        if (!$assertionsDisabled && peek() != 91) {
            throw new AssertionError();
        }
        this.pos++;
        while (this.pos < this.length) {
            skipWhiteSpace();
            int c2 = peek();
            switch (c2) {
                case 44:
                    if (state != 1) {
                        throw error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    state = 2;
                    this.pos++;
                    break;
                case 93:
                    if (state == 2) {
                        throw error(AbstractParser.message("trailing.comma.in.json", new String[0]), this.pos);
                    }
                    this.pos++;
                    return this.global.wrapAsObject(arrayData);
                default:
                    if (state == 1) {
                        throw expectedError(this.pos, ", or ]", toString(c2));
                    }
                    long index = arrayData.length();
                    arrayData = arrayData.ensure(index).set((int) index, parseLiteral(), true);
                    state = 1;
                    break;
            }
        }
        throw expectedError(this.pos, ", or ]", "eof");
    }

    private String parseString() {
        int i2 = this.pos + 1;
        this.pos = i2;
        int start = i2;
        StringBuilder sb = null;
        while (this.pos < this.length) {
            int c2 = next();
            if (c2 <= 31) {
                throw syntaxError(this.pos, "String contains control character");
            }
            if (c2 == 92) {
                if (sb == null) {
                    sb = new StringBuilder((this.pos - start) + 16);
                }
                sb.append((CharSequence) this.source, start, this.pos - 1);
                sb.append(parseEscapeSequence());
                start = this.pos;
            } else if (c2 == 34) {
                if (sb != null) {
                    sb.append((CharSequence) this.source, start, this.pos - 1);
                    return sb.toString();
                }
                return this.source.substring(start, this.pos - 1);
            }
        }
        throw error(Lexer.message("missing.close.quote", new String[0]), this.pos, this.length);
    }

    private char parseEscapeSequence() {
        int c2 = next();
        switch (c2) {
            case 34:
                return '\"';
            case 47:
                return '/';
            case 92:
                return '\\';
            case 98:
                return '\b';
            case 102:
                return '\f';
            case 110:
                return '\n';
            case 114:
                return '\r';
            case 116:
                return '\t';
            case 117:
                return parseUnicodeEscape();
            default:
                throw error(Lexer.message("invalid.escape.char", new String[0]), this.pos - 1, this.length);
        }
    }

    private char parseUnicodeEscape() {
        return (char) ((parseHexDigit() << 12) | (parseHexDigit() << 8) | (parseHexDigit() << 4) | parseHexDigit());
    }

    private int parseHexDigit() {
        int c2 = next();
        if (c2 >= 48 && c2 <= 57) {
            return c2 - 48;
        }
        if (c2 >= 65 && c2 <= 70) {
            return (c2 + 10) - 65;
        }
        if (c2 >= 97 && c2 <= 102) {
            return (c2 + 10) - 97;
        }
        throw error(Lexer.message("invalid.hex", new String[0]), this.pos - 1, this.length);
    }

    private boolean isDigit(int c2) {
        return c2 >= 48 && c2 <= 57;
    }

    private void skipDigits() {
        while (this.pos < this.length) {
            int c2 = peek();
            if (isDigit(c2)) {
                this.pos++;
            } else {
                return;
            }
        }
    }

    private Number parseNumber() throws NumberFormatException {
        int start = this.pos;
        int c2 = next();
        if (c2 == 45) {
            c2 = next();
        }
        if (!isDigit(c2)) {
            throw numberError(start);
        }
        if (c2 != 48) {
            skipDigits();
        }
        if (peek() == 46) {
            this.pos++;
            if (!isDigit(next())) {
                throw numberError(this.pos - 1);
            }
            skipDigits();
        }
        int c3 = peek();
        if (c3 == 101 || c3 == 69) {
            this.pos++;
            int c4 = next();
            if (c4 == 45 || c4 == 43) {
                c4 = next();
            }
            if (!isDigit(c4)) {
                throw numberError(this.pos - 1);
            }
            skipDigits();
        }
        double d2 = Double.parseDouble(this.source.substring(start, this.pos));
        if (JSType.isRepresentableAsInt(d2)) {
            return Integer.valueOf((int) d2);
        }
        return Double.valueOf(d2);
    }

    private Object parseKeyword(String keyword, Object value) {
        if (!this.source.regionMatches(this.pos, keyword, 0, keyword.length())) {
            throw expectedError(this.pos, "json literal", "ident");
        }
        this.pos += keyword.length();
        return value;
    }

    private int peek() {
        if (this.pos >= this.length) {
            return -1;
        }
        return this.source.charAt(this.pos);
    }

    private int next() {
        int next = peek();
        this.pos++;
        return next;
    }

    private void skipWhiteSpace() {
        while (this.pos < this.length) {
            switch (peek()) {
                case 9:
                case 10:
                case 13:
                case 32:
                    this.pos++;
                default:
                    return;
            }
        }
    }

    private static String toString(int c2) {
        return c2 == -1 ? "eof" : String.valueOf((char) c2);
    }

    ParserException error(String message, int start, int length) throws ParserException {
        long token = Token.toDesc(TokenType.STRING, start, length);
        int pos = Token.descPosition(token);
        Source src = Source.sourceFor("<json>", this.source);
        int lineNum = src.getLine(pos);
        int columnNum = src.getColumn(pos);
        String formatted = ErrorManager.format(message, src, lineNum, columnNum, token);
        return new ParserException(JSErrorType.SYNTAX_ERROR, formatted, src, lineNum, columnNum, token);
    }

    private ParserException error(String message, int start) {
        return error(message, start, this.length);
    }

    private ParserException numberError(int start) {
        return error(Lexer.message("json.invalid.number", new String[0]), start);
    }

    private ParserException expectedError(int start, String expected, String found) {
        return error(AbstractParser.message("expected", expected, found), start);
    }

    private ParserException syntaxError(int start, String reason) {
        String message = ECMAErrors.getMessage("syntax.error.invalid.json", reason);
        return error(message, start);
    }
}
