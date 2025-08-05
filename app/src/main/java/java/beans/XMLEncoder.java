package java.beans;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/beans/XMLEncoder.class */
public class XMLEncoder extends Encoder implements AutoCloseable {
    private final CharsetEncoder encoder;
    private final String charset;
    private final boolean declaration;
    private OutputStreamWriter out;
    private Object owner;
    private int indentation;
    private boolean internal;
    private Map<Object, ValueData> valueToExpression;
    private Map<Object, List<Statement>> targetToStatementList;
    private boolean preambleWritten;
    private NameGenerator nameGenerator;

    /* loaded from: rt.jar:java/beans/XMLEncoder$ValueData.class */
    private class ValueData {
        public int refs;
        public boolean marked;
        public String name;
        public Expression exp;

        private ValueData() {
            this.refs = 0;
            this.marked = false;
            this.name = null;
            this.exp = null;
        }
    }

    public XMLEncoder(OutputStream outputStream) {
        this(outputStream, "UTF-8", true, 0);
    }

    public XMLEncoder(OutputStream outputStream, String str, boolean z2, int i2) {
        this.indentation = 0;
        this.internal = false;
        this.preambleWritten = false;
        if (outputStream == null) {
            throw new IllegalArgumentException("the output stream cannot be null");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("the indentation must be >= 0");
        }
        Charset charsetForName = Charset.forName(str);
        this.encoder = charsetForName.newEncoder();
        this.charset = str;
        this.declaration = z2;
        this.indentation = i2;
        this.out = new OutputStreamWriter(outputStream, charsetForName.newEncoder());
        this.valueToExpression = new IdentityHashMap();
        this.targetToStatementList = new IdentityHashMap();
        this.nameGenerator = new NameGenerator();
    }

    public void setOwner(Object obj) {
        this.owner = obj;
        writeExpression(new Expression(this, "getOwner", new Object[0]));
    }

    public Object getOwner() {
        return this.owner;
    }

    @Override // java.beans.Encoder
    public void writeObject(Object obj) {
        if (this.internal) {
            super.writeObject(obj);
        } else {
            writeStatement(new Statement(this, "writeObject", new Object[]{obj}));
        }
    }

    private List<Statement> statementList(Object obj) {
        List<Statement> arrayList = this.targetToStatementList.get(obj);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.targetToStatementList.put(obj, arrayList);
        }
        return arrayList;
    }

    private void mark(Object obj, boolean z2) {
        if (obj == null || obj == this) {
            return;
        }
        ValueData valueData = getValueData(obj);
        Expression expression = valueData.exp;
        if (obj.getClass() == String.class && expression == null) {
            return;
        }
        if (z2) {
            valueData.refs++;
        }
        if (valueData.marked) {
            return;
        }
        valueData.marked = true;
        Object target = expression.getTarget();
        mark(expression);
        if (!(target instanceof Class)) {
            statementList(target).add(expression);
            valueData.refs++;
        }
    }

    private void mark(Statement statement) {
        for (Object obj : statement.getArguments()) {
            mark(obj, true);
        }
        mark(statement.getTarget(), statement instanceof Expression);
    }

    @Override // java.beans.Encoder
    public void writeStatement(Statement statement) {
        boolean z2 = this.internal;
        this.internal = true;
        try {
            super.writeStatement(statement);
            mark(statement);
            Object target = statement.getTarget();
            if (target instanceof Field) {
                String methodName = statement.getMethodName();
                Object[] arguments = statement.getArguments();
                if (methodName != null && arguments != null) {
                    if (methodName.equals("get") && arguments.length == 1) {
                        target = arguments[0];
                    } else if (methodName.equals("set") && arguments.length == 2) {
                        target = arguments[0];
                    }
                }
            }
            statementList(target).add(statement);
        } catch (Exception e2) {
            getExceptionListener().exceptionThrown(new Exception("XMLEncoder: discarding statement " + ((Object) statement), e2));
        }
        this.internal = z2;
    }

    @Override // java.beans.Encoder
    public void writeExpression(Expression expression) {
        boolean z2 = this.internal;
        this.internal = true;
        Object value = getValue(expression);
        if (get(value) == null || ((value instanceof String) && !z2)) {
            getValueData(value).exp = expression;
            super.writeExpression(expression);
        }
        this.internal = z2;
    }

    public void flush() {
        if (!this.preambleWritten) {
            if (this.declaration) {
                writeln("<?xml version=" + quote("1.0") + " encoding=" + quote(this.charset) + "?>");
            }
            writeln("<java version=" + quote(System.getProperty("java.version")) + " class=" + quote(XMLDecoder.class.getName()) + ">");
            this.preambleWritten = true;
        }
        this.indentation++;
        List<Statement> listStatementList = statementList(this);
        while (!listStatementList.isEmpty()) {
            Statement statementRemove = listStatementList.remove(0);
            if ("writeObject".equals(statementRemove.getMethodName())) {
                outputValue(statementRemove.getArguments()[0], this, true);
            } else {
                outputStatement(statementRemove, this, false);
            }
        }
        this.indentation--;
        Statement missedStatement = getMissedStatement();
        while (true) {
            Statement statement = missedStatement;
            if (statement != null) {
                outputStatement(statement, this, false);
                missedStatement = getMissedStatement();
            } else {
                try {
                    break;
                } catch (IOException e2) {
                    getExceptionListener().exceptionThrown(e2);
                }
            }
        }
        this.out.flush();
        clear();
    }

    @Override // java.beans.Encoder
    void clear() {
        super.clear();
        this.nameGenerator.clear();
        this.valueToExpression.clear();
        this.targetToStatementList.clear();
    }

    Statement getMissedStatement() {
        for (List<Statement> list : this.targetToStatementList.values()) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                if (Statement.class == list.get(i2).getClass()) {
                    return list.remove(i2);
                }
            }
        }
        return null;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        flush();
        writeln("</java>");
        try {
            this.out.close();
        } catch (IOException e2) {
            getExceptionListener().exceptionThrown(e2);
        }
    }

    private String quote(String str) {
        return PdfOps.DOUBLE_QUOTE__TOKEN + str + PdfOps.DOUBLE_QUOTE__TOKEN;
    }

    private ValueData getValueData(Object obj) {
        ValueData valueData = this.valueToExpression.get(obj);
        if (valueData == null) {
            valueData = new ValueData();
            this.valueToExpression.put(obj, valueData);
        }
        return valueData;
    }

    private static boolean isValidCharCode(int i2) {
        return (32 <= i2 && i2 <= 55295) || 10 == i2 || 9 == i2 || 13 == i2 || (57344 <= i2 && i2 <= 65533) || (65536 <= i2 && i2 <= 1114111);
    }

    private void writeln(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < this.indentation; i2++) {
                sb.append(' ');
            }
            sb.append(str);
            sb.append('\n');
            this.out.write(sb.toString());
        } catch (IOException e2) {
            getExceptionListener().exceptionThrown(e2);
        }
    }

    private void outputValue(Object obj, Object obj2, boolean z2) {
        if (obj == null) {
            writeln("<null/>");
            return;
        }
        if (obj instanceof Class) {
            writeln("<class>" + ((Class) obj).getName() + "</class>");
            return;
        }
        ValueData valueData = getValueData(obj);
        if (valueData.exp != null) {
            Object target = valueData.exp.getTarget();
            String methodName = valueData.exp.getMethodName();
            if (target == null || methodName == null) {
                throw new NullPointerException((target == null ? "target" : "methodName") + " should not be null");
            }
            if (z2 && (target instanceof Field) && methodName.equals("get")) {
                Field field = (Field) target;
                if (Modifier.isStatic(field.getModifiers())) {
                    writeln("<object class=" + quote(field.getDeclaringClass().getName()) + " field=" + quote(field.getName()) + "/>");
                    return;
                }
            }
            Class<Character> clsPrimitiveTypeFor = primitiveTypeFor(obj.getClass());
            if (clsPrimitiveTypeFor != null && target == obj.getClass() && methodName.equals("new")) {
                String name = clsPrimitiveTypeFor.getName();
                if (clsPrimitiveTypeFor == Character.TYPE) {
                    char cCharValue = ((Character) obj).charValue();
                    if (!isValidCharCode(cCharValue)) {
                        writeln(createString(cCharValue));
                        return;
                    } else {
                        obj = quoteCharCode(cCharValue);
                        if (obj == null) {
                            obj = Character.valueOf(cCharValue);
                        }
                    }
                }
                writeln("<" + name + ">" + obj + "</" + name + ">");
                return;
            }
        } else if (obj instanceof String) {
            writeln(createString((String) obj));
            return;
        }
        if (valueData.name != null) {
            if (z2) {
                writeln("<object idref=" + quote(valueData.name) + "/>");
                return;
            } else {
                outputXML("void", " idref=" + quote(valueData.name), obj, new Object[0]);
                return;
            }
        }
        if (valueData.exp != null) {
            outputStatement(valueData.exp, obj2, z2);
        }
    }

    private static String quoteCharCode(int i2) {
        switch (i2) {
            case 13:
                return "&#13;";
            case 34:
                return SerializerConstants.ENTITY_QUOT;
            case 38:
                return SerializerConstants.ENTITY_AMP;
            case 39:
                return "&apos;";
            case 60:
                return SerializerConstants.ENTITY_LT;
            case 62:
                return SerializerConstants.ENTITY_GT;
            default:
                return null;
        }
    }

    private static String createString(int i2) {
        return "<char code=\"#" + Integer.toString(i2, 16) + "\"/>";
    }

    private String createString(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("<string>");
        int i2 = 0;
        while (i2 < str.length()) {
            int iCodePointAt = str.codePointAt(i2);
            int iCharCount = Character.charCount(iCodePointAt);
            if (isValidCharCode(iCodePointAt) && this.encoder.canEncode(str.substring(i2, i2 + iCharCount))) {
                String strQuoteCharCode = quoteCharCode(iCodePointAt);
                if (strQuoteCharCode != null) {
                    sb.append(strQuoteCharCode);
                } else {
                    sb.appendCodePoint(iCodePointAt);
                }
                i2 += iCharCount;
            } else {
                sb.append(createString(str.charAt(i2)));
                i2++;
            }
        }
        sb.append("</string>");
        return sb.toString();
    }

    private void outputStatement(Statement statement, Object obj, boolean z2) {
        Object target = statement.getTarget();
        String methodName = statement.getMethodName();
        if (target == null || methodName == null) {
            throw new NullPointerException((target == null ? "target" : "methodName") + " should not be null");
        }
        Object[] arguments = statement.getArguments();
        boolean z3 = statement.getClass() == Expression.class;
        Object value = z3 ? getValue((Expression) statement) : null;
        String str = (z3 && z2) ? "object" : "void";
        String str2 = "";
        ValueData valueData = getValueData(value);
        if (target != obj) {
            if (target == Array.class && methodName.equals("newInstance")) {
                str = ControllerParameter.PARAM_CLASS_ARRAY;
                str2 = (str2 + " class=" + quote(((Class) arguments[0]).getName())) + " length=" + quote(arguments[1].toString());
                arguments = new Object[0];
            } else if (target.getClass() == Class.class) {
                str2 = str2 + " class=" + quote(((Class) target).getName());
            } else {
                valueData.refs = 2;
                if (valueData.name == null) {
                    getValueData(target).refs++;
                    List<Statement> listStatementList = statementList(target);
                    if (!listStatementList.contains(statement)) {
                        listStatementList.add(statement);
                    }
                    outputValue(target, obj, false);
                }
                if (z3) {
                    outputValue(value, obj, z2);
                    return;
                }
                return;
            }
        }
        if (z3 && valueData.refs > 1) {
            String strInstanceName = this.nameGenerator.instanceName(value);
            valueData.name = strInstanceName;
            str2 = str2 + " id=" + quote(strInstanceName);
        }
        if ((!z3 && methodName.equals("set") && arguments.length == 2 && (arguments[0] instanceof Integer)) || (z3 && methodName.equals("get") && arguments.length == 1 && (arguments[0] instanceof Integer))) {
            str2 = str2 + " index=" + quote(arguments[0].toString());
            arguments = arguments.length == 1 ? new Object[0] : new Object[]{arguments[1]};
        } else if ((!z3 && methodName.startsWith("set") && arguments.length == 1) || (z3 && methodName.startsWith("get") && arguments.length == 0)) {
            if (3 < methodName.length()) {
                str2 = str2 + " property=" + quote(Introspector.decapitalize(methodName.substring(3)));
            }
        } else if (!methodName.equals("new") && !methodName.equals("newInstance")) {
            str2 = str2 + " method=" + quote(methodName);
        }
        outputXML(str, str2, value, arguments);
    }

    private void outputXML(String str, String str2, Object obj, Object... objArr) {
        List<Statement> listStatementList = statementList(obj);
        if (objArr.length == 0 && listStatementList.size() == 0) {
            writeln("<" + str + str2 + "/>");
            return;
        }
        writeln("<" + str + str2 + ">");
        this.indentation++;
        for (Object obj2 : objArr) {
            outputValue(obj2, null, true);
        }
        while (!listStatementList.isEmpty()) {
            outputStatement(listStatementList.remove(0), obj, false);
        }
        this.indentation--;
        writeln("</" + str + ">");
    }

    static Class primitiveTypeFor(Class cls) {
        if (cls == Boolean.class) {
            return Boolean.TYPE;
        }
        if (cls == Byte.class) {
            return Byte.TYPE;
        }
        if (cls == Character.class) {
            return Character.TYPE;
        }
        if (cls == Short.class) {
            return Short.TYPE;
        }
        if (cls == Integer.class) {
            return Integer.TYPE;
        }
        if (cls == Long.class) {
            return Long.TYPE;
        }
        if (cls == Float.class) {
            return Float.TYPE;
        }
        if (cls == Double.class) {
            return Double.TYPE;
        }
        if (cls == Void.class) {
            return Void.TYPE;
        }
        return null;
    }
}
