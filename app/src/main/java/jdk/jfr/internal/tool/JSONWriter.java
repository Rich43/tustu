package jdk.jfr.internal.tool;

import java.io.PrintWriter;
import java.util.List;
import javafx.fxml.FXMLLoader;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedFrame;
import jdk.jfr.consumer.RecordedObject;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/JSONWriter.class */
final class JSONWriter extends EventPrintWriter {
    private boolean first;

    public JSONWriter(PrintWriter printWriter) {
        super(printWriter);
        this.first = true;
    }

    @Override // jdk.jfr.internal.tool.EventPrintWriter
    protected void printBegin() {
        printObjectBegin();
        printDataStructureName("recording");
        printObjectBegin();
        printDataStructureName("events");
        printArrayBegin();
    }

    @Override // jdk.jfr.internal.tool.EventPrintWriter
    protected void print(List<RecordedEvent> list) {
        for (RecordedEvent recordedEvent : list) {
            printNewDataStructure(this.first, true, null);
            printEvent(recordedEvent);
            flush(false);
            this.first = false;
        }
    }

    @Override // jdk.jfr.internal.tool.EventPrintWriter
    protected void printEnd() {
        printArrayEnd();
        printObjectEnd();
        printObjectEnd();
    }

    private void printEvent(RecordedEvent recordedEvent) {
        printObjectBegin();
        printValue(true, false, "type", recordedEvent.getEventType().getName());
        printNewDataStructure(false, false, "values");
        printObjectBegin();
        boolean z2 = true;
        for (ValueDescriptor valueDescriptor : recordedEvent.getFields()) {
            printValueDescriptor(z2, false, valueDescriptor, getValue(recordedEvent, valueDescriptor));
            z2 = false;
        }
        printObjectEnd();
        printObjectEnd();
    }

    void printValue(boolean z2, boolean z3, String str, Object obj) {
        printNewDataStructure(z2, z3, str);
        if (!printIfNull(obj)) {
            if (obj instanceof Boolean) {
                printAsString(obj);
                return;
            }
            if (obj instanceof Double) {
                Double d2 = (Double) obj;
                if (Double.isNaN(d2.doubleValue()) || Double.isInfinite(d2.doubleValue())) {
                    printNull();
                    return;
                } else {
                    printAsString(obj);
                    return;
                }
            }
            if (obj instanceof Float) {
                Float f2 = (Float) obj;
                if (Float.isNaN(f2.floatValue()) || Float.isInfinite(f2.floatValue())) {
                    printNull();
                    return;
                } else {
                    printAsString(obj);
                    return;
                }
            }
            if (obj instanceof Number) {
                printAsString(obj);
                return;
            }
            print(PdfOps.DOUBLE_QUOTE__TOKEN);
            printEscaped(String.valueOf(obj));
            print(PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }

    public void printObject(RecordedObject recordedObject) {
        printObjectBegin();
        boolean z2 = true;
        for (ValueDescriptor valueDescriptor : recordedObject.getFields()) {
            printValueDescriptor(z2, false, valueDescriptor, getValue(recordedObject, valueDescriptor));
            z2 = false;
        }
        printObjectEnd();
    }

    private void printArray(ValueDescriptor valueDescriptor, Object[] objArr) {
        printArrayBegin();
        boolean z2 = true;
        int i2 = 0;
        for (Object obj : objArr) {
            if (!(obj instanceof RecordedFrame) || i2 < getStackDepth()) {
                printValueDescriptor(z2, true, valueDescriptor, obj);
            }
            i2++;
            z2 = false;
        }
        printArrayEnd();
    }

    private void printValueDescriptor(boolean z2, boolean z3, ValueDescriptor valueDescriptor, Object obj) {
        if (valueDescriptor.isArray() && !z3) {
            printNewDataStructure(z2, z3, valueDescriptor.getName());
            if (!printIfNull(obj)) {
                printArray(valueDescriptor, (Object[]) obj);
                return;
            }
            return;
        }
        if (!valueDescriptor.getFields().isEmpty()) {
            printNewDataStructure(z2, z3, valueDescriptor.getName());
            if (!printIfNull(obj)) {
                printObject((RecordedObject) obj);
                return;
            }
            return;
        }
        printValue(z2, z3, valueDescriptor.getName(), obj);
    }

    private void printNewDataStructure(boolean z2, boolean z3, String str) {
        if (!z2) {
            print(", ");
            if (!z3) {
                println();
            }
        }
        if (!z3) {
            printDataStructureName(str);
        }
    }

    private boolean printIfNull(Object obj) {
        if (obj == null) {
            printNull();
            return true;
        }
        return false;
    }

    private void printNull() {
        print(FXMLLoader.NULL_KEYWORD);
    }

    private void printDataStructureName(String str) {
        printIndent();
        print(PdfOps.DOUBLE_QUOTE__TOKEN);
        printEscaped(str);
        print("\": ");
    }

    private void printObjectEnd() {
        retract();
        println();
        printIndent();
        print("}");
    }

    private void printObjectBegin() {
        println(VectorFormat.DEFAULT_PREFIX);
        indent();
    }

    private void printArrayEnd() {
        print("]");
    }

    private void printArrayBegin() {
        print("[");
    }

    private void printEscaped(String str) {
        for (int i2 = 0; i2 < str.length(); i2++) {
            printEscaped(str.charAt(i2));
        }
    }

    private void printEscaped(char c2) {
        if (c2 == '\b') {
            print("\\b");
            return;
        }
        if (c2 == '\n') {
            print("\\n");
            return;
        }
        if (c2 == '\t') {
            print("\\t");
            return;
        }
        if (c2 == '\f') {
            print("\\f");
            return;
        }
        if (c2 == '\r') {
            print("\\r");
            return;
        }
        if (c2 == '\"') {
            print("\\\"");
            return;
        }
        if (c2 == '\\') {
            print("\\\\");
            return;
        }
        if (c2 == '/') {
            print("\\/");
        } else if (c2 > 127 || c2 < ' ') {
            print("\\u");
            print(Integer.toHexString(0 + c2).substring(1));
        } else {
            print(c2);
        }
    }
}
