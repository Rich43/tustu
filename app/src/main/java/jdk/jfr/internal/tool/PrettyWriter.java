package jdk.jfr.internal.tool;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import javafx.fxml.FXMLLoader;
import jdk.jfr.AnnotationElement;
import jdk.jfr.DataAmount;
import jdk.jfr.Frequency;
import jdk.jfr.MemoryAddress;
import jdk.jfr.Percentage;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.consumer.RecordedClass;
import jdk.jfr.consumer.RecordedClassLoader;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedFrame;
import jdk.jfr.consumer.RecordedMethod;
import jdk.jfr.consumer.RecordedObject;
import jdk.jfr.consumer.RecordedStackTrace;
import jdk.jfr.consumer.RecordedThread;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/PrettyWriter.class */
public final class PrettyWriter extends EventPrintWriter {
    private static final String TYPE_OLD_OBJECT = "jdk.types.OldObject";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final Long ZERO = 0L;
    private boolean showIds;
    private RecordedEvent currentEvent;

    @Override // jdk.jfr.internal.tool.EventPrintWriter
    public /* bridge */ /* synthetic */ void setStackDepth(int i2) {
        super.setStackDepth(i2);
    }

    public PrettyWriter(PrintWriter printWriter) {
        super(printWriter);
    }

    @Override // jdk.jfr.internal.tool.EventPrintWriter
    protected void print(List<RecordedEvent> list) {
        Iterator<RecordedEvent> it = list.iterator();
        while (it.hasNext()) {
            print(it.next());
            flush(false);
        }
    }

    public void printType(Type type) {
        if (this.showIds) {
            print("// id: ");
            println(String.valueOf(type.getId()));
        }
        int length = type.getName().length() + 10;
        String name = type.getName();
        int iLastIndexOf = name.lastIndexOf(".");
        if (iLastIndexOf != -1) {
            println("@Name(\"" + name + "\")");
        }
        printAnnotations(length, type.getAnnotationElements());
        print("class " + name.substring(iLastIndexOf + 1));
        String superType = type.getSuperType();
        if (superType != null) {
            print(" extends " + superType);
        }
        println(" {");
        indent();
        boolean z2 = true;
        Iterator<ValueDescriptor> it = type.getFields().iterator();
        while (it.hasNext()) {
            printField(length, it.next(), z2);
            z2 = false;
        }
        retract();
        println("}");
        println();
    }

    private void printField(int i2, ValueDescriptor valueDescriptor, boolean z2) {
        if (!z2) {
            println();
        }
        printAnnotations(i2, valueDescriptor.getAnnotationElements());
        printIndent();
        if (Type.SUPER_TYPE_SETTING.equals(PrivateAccess.getInstance().getType(valueDescriptor).getSuperType())) {
            print("static ");
        }
        print(makeSimpleType(valueDescriptor.getTypeName()));
        if (valueDescriptor.isArray()) {
            print("[]");
        }
        print(" ");
        print(valueDescriptor.getName());
        print(";");
        printCommentRef(i2, valueDescriptor.getTypeId());
    }

    private void printCommentRef(int i2, long j2) {
        if (this.showIds) {
            int column = getColumn();
            if (column > i2) {
                print(Constants.INDENT);
            } else {
                while (column < i2) {
                    print(" ");
                    column++;
                }
            }
            println(" // id=" + j2);
            return;
        }
        println();
    }

    private void printAnnotations(int i2, List<AnnotationElement> list) {
        for (AnnotationElement annotationElement : list) {
            printIndent();
            print("@");
            print(makeSimpleType(annotationElement.getTypeName()));
            if (!annotationElement.getValueDescriptors().isEmpty()) {
                printAnnotation(annotationElement);
                printCommentRef(i2, annotationElement.getTypeId());
            } else {
                println();
            }
        }
    }

    private void printAnnotation(AnnotationElement annotationElement) {
        StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
        List<ValueDescriptor> valueDescriptors = annotationElement.getValueDescriptors();
        for (ValueDescriptor valueDescriptor : valueDescriptors) {
            Object value = annotationElement.getValue(valueDescriptor.getName());
            if (valueDescriptors.size() == 1 && valueDescriptor.getName().equals("value")) {
                stringJoiner.add(textify(value));
            } else {
                stringJoiner.add(valueDescriptor.getName() + "=" + textify(value));
            }
        }
        print(stringJoiner.toString());
    }

    private String textify(Object obj) {
        if (obj.getClass().isArray()) {
            Object[] objArr = (Object[]) obj;
            if (objArr.length == 1) {
                return quoteIfNeeded(objArr[0]);
            }
            StringJoiner stringJoiner = new StringJoiner(", ", VectorFormat.DEFAULT_PREFIX, "}");
            for (Object obj2 : objArr) {
                stringJoiner.add(quoteIfNeeded(obj2));
            }
            return stringJoiner.toString();
        }
        return quoteIfNeeded(obj);
    }

    private String quoteIfNeeded(Object obj) {
        if (obj instanceof String) {
            return PdfOps.DOUBLE_QUOTE__TOKEN + obj + PdfOps.DOUBLE_QUOTE__TOKEN;
        }
        return String.valueOf(obj);
    }

    private String makeSimpleType(String str) {
        return str.substring(str.lastIndexOf(".") + 1);
    }

    public void print(RecordedEvent recordedEvent) {
        this.currentEvent = recordedEvent;
        print(recordedEvent.getEventType().getName(), " ");
        println(VectorFormat.DEFAULT_PREFIX);
        indent();
        for (ValueDescriptor valueDescriptor : recordedEvent.getFields()) {
            String name = valueDescriptor.getName();
            if (!isZeroDuration(recordedEvent, name) && !isLateField(name)) {
                printFieldValue(recordedEvent, valueDescriptor);
            }
        }
        if (recordedEvent.getThread() != null) {
            printIndent();
            print("eventThread = ");
            printThread(recordedEvent.getThread(), "");
        }
        if (recordedEvent.getStackTrace() != null) {
            printIndent();
            print("stackTrace = ");
            printStackTrace(recordedEvent.getStackTrace());
        }
        retract();
        printIndent();
        println("}");
        println();
    }

    private boolean isZeroDuration(RecordedEvent recordedEvent, String str) {
        return str.equals("duration") && ZERO.equals(recordedEvent.getValue("duration"));
    }

    private void printStackTrace(RecordedStackTrace recordedStackTrace) {
        println("[");
        List<RecordedFrame> frames = recordedStackTrace.getFrames();
        indent();
        int i2 = 0;
        while (i2 < frames.size() && i2 < getStackDepth()) {
            RecordedFrame recordedFrame = frames.get(i2);
            if (recordedFrame.isJavaFrame()) {
                printIndent();
                printValue(recordedFrame, null, "");
                println();
                i2++;
            }
        }
        if (recordedStackTrace.isTruncated() || i2 == getStackDepth()) {
            printIndent();
            println("...");
        }
        retract();
        printIndent();
        println("]");
    }

    public void print(RecordedObject recordedObject, String str) {
        println(VectorFormat.DEFAULT_PREFIX);
        indent();
        Iterator<ValueDescriptor> it = recordedObject.getFields().iterator();
        while (it.hasNext()) {
            printFieldValue(recordedObject, it.next());
        }
        retract();
        printIndent();
        println("}" + str);
    }

    private void printFieldValue(RecordedObject recordedObject, ValueDescriptor valueDescriptor) {
        printIndent();
        print(valueDescriptor.getName(), " = ");
        printValue(getValue(recordedObject, valueDescriptor), valueDescriptor, "");
    }

    private void printArray(Object[] objArr) {
        println("[");
        indent();
        for (int i2 = 0; i2 < objArr.length; i2++) {
            printIndent();
            printValue(objArr[i2], null, i2 + 1 < objArr.length ? ", " : "");
        }
        retract();
        printIndent();
        println("]");
    }

    private void printValue(Object obj, ValueDescriptor valueDescriptor, String str) {
        if (obj == null) {
            println("N/A" + str);
            return;
        }
        if (obj instanceof RecordedObject) {
            if (obj instanceof RecordedThread) {
                printThread((RecordedThread) obj, str);
                return;
            }
            if (obj instanceof RecordedClass) {
                printClass((RecordedClass) obj, str);
                return;
            }
            if (obj instanceof RecordedClassLoader) {
                printClassLoader((RecordedClassLoader) obj, str);
                return;
            }
            if ((obj instanceof RecordedFrame) && ((RecordedFrame) obj).isJavaFrame()) {
                printJavaFrame((RecordedFrame) obj, str);
                return;
            }
            if (obj instanceof RecordedMethod) {
                println(formatMethod((RecordedMethod) obj));
                return;
            } else if (valueDescriptor.getTypeName().equals(TYPE_OLD_OBJECT)) {
                printOldObject((RecordedObject) obj);
                return;
            } else {
                print((RecordedObject) obj, str);
                return;
            }
        }
        if (obj.getClass().isArray()) {
            printArray((Object[]) obj);
            return;
        }
        if (obj instanceof Double) {
            Double d2 = (Double) obj;
            if (Double.isNaN(d2.doubleValue()) || d2.doubleValue() == Double.NEGATIVE_INFINITY) {
                println("N/A");
                return;
            }
        }
        if (obj instanceof Float) {
            Float f2 = (Float) obj;
            if (Float.isNaN(f2.floatValue()) || f2.floatValue() == Float.NEGATIVE_INFINITY) {
                println("N/A");
                return;
            }
        }
        if ((obj instanceof Long) && ((Long) obj).longValue() == Long.MIN_VALUE) {
            println("N/A");
            return;
        }
        if ((obj instanceof Integer) && ((Integer) obj).intValue() == Integer.MIN_VALUE) {
            println("N/A");
            return;
        }
        if (valueDescriptor.getContentType() != null && printFormatted(valueDescriptor, obj)) {
            return;
        }
        String strValueOf = String.valueOf(obj);
        if (obj instanceof String) {
            strValueOf = PdfOps.DOUBLE_QUOTE__TOKEN + strValueOf + PdfOps.DOUBLE_QUOTE__TOKEN;
        }
        println(strValueOf);
    }

    private void printOldObject(RecordedObject recordedObject) {
        println(" [");
        indent();
        printIndent();
        try {
            printReferenceChain(recordedObject);
        } catch (IllegalArgumentException e2) {
        }
        retract();
        printIndent();
        println("]");
    }

    private void printReferenceChain(RecordedObject recordedObject) {
        printObject(recordedObject, this.currentEvent.getLong("arrayElements"));
        Object value = recordedObject.getValue("referrer");
        while (true) {
            RecordedObject recordedObject2 = (RecordedObject) value;
            if (recordedObject2 != null) {
                if (recordedObject2.getLong(SchemaSymbols.ATTVAL_SKIP) > 0) {
                    printIndent();
                    println("...");
                }
                String string = "";
                long j2 = Long.MIN_VALUE;
                RecordedObject recordedObject3 = (RecordedObject) recordedObject2.getValue(ControllerParameter.PARAM_CLASS_ARRAY);
                if (recordedObject3 != null) {
                    long j3 = recordedObject3.getLong("index");
                    j2 = recordedObject3.getLong("size");
                    string = "[" + j3 + "]";
                }
                RecordedObject recordedObject4 = (RecordedObject) recordedObject2.getValue("field");
                if (recordedObject4 != null) {
                    string = recordedObject4.getString("name");
                }
                printIndent();
                print(string);
                print(" : ");
                RecordedObject recordedObject5 = (RecordedObject) recordedObject2.getValue("object");
                if (recordedObject5 != null) {
                    printObject(recordedObject5, j2);
                }
                value = recordedObject5.getValue("referrer");
            } else {
                return;
            }
        }
    }

    void printObject(RecordedObject recordedObject, long j2) {
        RecordedClass recordedClass = recordedObject.getClass("type");
        if (recordedClass != null) {
            String name = recordedClass.getName();
            if (name != null && name.startsWith("[")) {
                name = decodeDescriptors(name, j2 > 0 ? Long.toString(j2) : "").get(0);
            }
            print(name);
            String string = recordedObject.getString("description");
            if (string != null) {
                print(" ");
                print(string);
            }
        }
        println();
    }

    private void printClassLoader(RecordedClassLoader recordedClassLoader, String str) {
        RecordedClass type = recordedClassLoader.getType();
        print(type == null ? FXMLLoader.NULL_KEYWORD : type.getName());
        if (type != null) {
            print(" (");
            print("id = ");
            print(String.valueOf(recordedClassLoader.getId()));
            println(")");
        }
    }

    private void printJavaFrame(RecordedFrame recordedFrame, String str) {
        print(formatMethod(recordedFrame.getMethod()));
        int lineNumber = recordedFrame.getLineNumber();
        if (lineNumber >= 0) {
            print(" line: " + lineNumber);
        }
        print(str);
    }

    private String formatMethod(RecordedMethod recordedMethod) {
        StringBuilder sb = new StringBuilder();
        sb.append(recordedMethod.getType().getName());
        sb.append(".");
        sb.append(recordedMethod.getName());
        sb.append("(");
        StringJoiner stringJoiner = new StringJoiner(", ");
        String strReplace = recordedMethod.getDescriptor().replace("/", ".");
        for (String str : decodeDescriptors(strReplace.substring(1, strReplace.lastIndexOf(")")), "")) {
            stringJoiner.add(str.substring(str.lastIndexOf(46) + 1));
        }
        sb.append((Object) stringJoiner);
        sb.append(")");
        return sb.toString();
    }

    private void printClass(RecordedClass recordedClass, String str) {
        RecordedClassLoader classLoader = recordedClass.getClassLoader();
        String name = FXMLLoader.NULL_KEYWORD;
        if (classLoader != null) {
            if (classLoader.getName() != null) {
                name = classLoader.getName();
            } else {
                name = classLoader.getType().getName();
            }
        }
        String name2 = recordedClass.getName();
        if (name2.startsWith("[")) {
            name2 = decodeDescriptors(name2, "").get(0);
        }
        println(name2 + " (classLoader = " + name + ")" + str);
    }

    List<String> decodeDescriptors(String str, String str2) {
        String strSubstring;
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < str.length()) {
            String str3 = "";
            while (str.charAt(i2) == '[') {
                str3 = str3 + "[" + str2 + "]";
                str2 = "";
                i2++;
            }
            switch (str.charAt(i2)) {
                case 'B':
                    strSubstring = SchemaSymbols.ATTVAL_BYTE;
                    break;
                case 'C':
                    strSubstring = "char";
                    break;
                case 'D':
                    strSubstring = SchemaSymbols.ATTVAL_DOUBLE;
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    strSubstring = "<unknown-descriptor-type>";
                    break;
                case 'F':
                    strSubstring = SchemaSymbols.ATTVAL_FLOAT;
                    break;
                case 'I':
                    strSubstring = "int";
                    break;
                case 'J':
                    strSubstring = SchemaSymbols.ATTVAL_LONG;
                    break;
                case 'L':
                    int iIndexOf = str.indexOf(59, i2);
                    strSubstring = str.substring(i2 + 1, iIndexOf);
                    i2 = iIndexOf;
                    break;
                case 'S':
                    strSubstring = SchemaSymbols.ATTVAL_SHORT;
                    break;
                case 'Z':
                    strSubstring = "boolean";
                    break;
            }
            arrayList.add(strSubstring + str3);
            i2++;
        }
        return arrayList;
    }

    private void printThread(RecordedThread recordedThread, String str) {
        if (recordedThread.getJavaThreadId() > 0) {
            println(PdfOps.DOUBLE_QUOTE__TOKEN + recordedThread.getJavaName() + "\" (javaThreadId = " + recordedThread.getJavaThreadId() + ")" + str);
        } else {
            println(PdfOps.DOUBLE_QUOTE__TOKEN + recordedThread.getOSName() + "\" (osThreadId = " + recordedThread.getOSThreadId() + ")" + str);
        }
    }

    private boolean printFormatted(ValueDescriptor valueDescriptor, Object obj) {
        if (obj instanceof Duration) {
            Duration duration = (Duration) obj;
            if (duration.getSeconds() == Long.MIN_VALUE && duration.getNano() == 0) {
                println("N/A");
                return true;
            }
            double nano = (duration.getNano() / 1.0E9d) + ((int) (duration.getSeconds() % 60));
            if (nano < 1.0d) {
                if (nano < 0.001d) {
                    println(String.format("%.3f", Double.valueOf(nano * 1000000.0d)) + " us");
                    return true;
                }
                println(String.format("%.3f", Double.valueOf(nano * 1000.0d)) + " ms");
                return true;
            }
            if (nano < 1000.0d) {
                println(String.format("%.3f", Double.valueOf(nano)) + " s");
                return true;
            }
            println(String.format("%.0f", Double.valueOf(nano)) + " s");
            return true;
        }
        if (obj instanceof OffsetDateTime) {
            OffsetDateTime offsetDateTime = (OffsetDateTime) obj;
            if (offsetDateTime.equals(OffsetDateTime.MIN)) {
                println("N/A");
                return true;
            }
            println(TIME_FORMAT.format(offsetDateTime));
            return true;
        }
        if (((Percentage) valueDescriptor.getAnnotation(Percentage.class)) != null && (obj instanceof Number)) {
            println(String.format("%.2f", Double.valueOf(((Number) obj).doubleValue() * 100.0d)) + FXMLLoader.RESOURCE_KEY_PREFIX);
            return true;
        }
        DataAmount dataAmount = (DataAmount) valueDescriptor.getAnnotation(DataAmount.class);
        if (dataAmount != null && (obj instanceof Number)) {
            long jLongValue = ((Number) obj).longValue();
            if (valueDescriptor.getAnnotation(Frequency.class) != null) {
                if (dataAmount.value().equals(DataAmount.BYTES)) {
                    println(Utils.formatBytesPerSecond(jLongValue));
                    return true;
                }
                if (dataAmount.value().equals(DataAmount.BITS)) {
                    println(Utils.formatBitsPerSecond(jLongValue));
                    return true;
                }
            } else {
                if (dataAmount.value().equals(DataAmount.BYTES)) {
                    println(Utils.formatBytes(jLongValue));
                    return true;
                }
                if (dataAmount.value().equals(DataAmount.BITS)) {
                    println(Utils.formatBits(jLongValue));
                    return true;
                }
            }
        }
        if (((MemoryAddress) valueDescriptor.getAnnotation(MemoryAddress.class)) != null && (obj instanceof Number)) {
            println(String.format("0x%08X", Long.valueOf(((Number) obj).longValue())));
            return true;
        }
        if (((Frequency) valueDescriptor.getAnnotation(Frequency.class)) != null && (obj instanceof Number)) {
            println(obj + " Hz");
            return true;
        }
        return false;
    }

    public void setShowIds(boolean z2) {
        this.showIds = z2;
    }
}
