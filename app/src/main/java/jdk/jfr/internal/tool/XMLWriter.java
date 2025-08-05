package jdk.jfr.internal.tool;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import jdk.jfr.EventType;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedFrame;
import jdk.jfr.consumer.RecordedObject;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/XMLWriter.class */
final class XMLWriter extends EventPrintWriter {
    public XMLWriter(PrintWriter printWriter) {
        super(printWriter);
    }

    @Override // jdk.jfr.internal.tool.EventPrintWriter
    protected void printBegin() {
        println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        println("<recording xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
        indent();
        printIndent();
        println("<events>");
        indent();
    }

    @Override // jdk.jfr.internal.tool.EventPrintWriter
    protected void printEnd() {
        retract();
        printIndent();
        println("</events>");
        retract();
        println("</recording>");
    }

    @Override // jdk.jfr.internal.tool.EventPrintWriter
    protected void print(List<RecordedEvent> list) {
        Iterator<RecordedEvent> it = list.iterator();
        while (it.hasNext()) {
            printEvent(it.next());
        }
    }

    private void printEvent(RecordedEvent recordedEvent) {
        EventType eventType = recordedEvent.getEventType();
        printIndent();
        print("<event");
        printAttribute("type", eventType.getName());
        print(">");
        println();
        indent();
        for (ValueDescriptor valueDescriptor : recordedEvent.getFields()) {
            printValueDescriptor(valueDescriptor, getValue(recordedEvent, valueDescriptor), -1);
        }
        retract();
        printIndent();
        println("</event>");
        println();
    }

    private void printAttribute(String str, String str2) {
        print(" ");
        print(str);
        print("=\"");
        printEscaped(str2);
        print(PdfOps.DOUBLE_QUOTE__TOKEN);
    }

    public void printObject(RecordedObject recordedObject) {
        println();
        indent();
        for (ValueDescriptor valueDescriptor : recordedObject.getFields()) {
            printValueDescriptor(valueDescriptor, getValue(recordedObject, valueDescriptor), -1);
        }
        retract();
    }

    private void printArray(ValueDescriptor valueDescriptor, Object[] objArr) {
        println();
        indent();
        int i2 = 0;
        for (int i3 = 0; i3 < objArr.length; i3++) {
            if (!(objArr[i3] instanceof RecordedFrame) || i2 < getStackDepth()) {
                printValueDescriptor(valueDescriptor, objArr[i3], i3);
            }
            i2++;
        }
        retract();
    }

    private void printValueDescriptor(ValueDescriptor valueDescriptor, Object obj, int i2) {
        boolean z2 = i2 != -1;
        String name = z2 ? null : valueDescriptor.getName();
        if (valueDescriptor.isArray() && !z2) {
            if (printBeginElement(ControllerParameter.PARAM_CLASS_ARRAY, name, obj, i2)) {
                printArray(valueDescriptor, (Object[]) obj);
                printIndent();
                printEndElement(ControllerParameter.PARAM_CLASS_ARRAY);
                return;
            }
            return;
        }
        if (!valueDescriptor.getFields().isEmpty()) {
            if (printBeginElement("struct", name, obj, i2)) {
                printObject((RecordedObject) obj);
                printIndent();
                printEndElement("struct");
                return;
            }
            return;
        }
        if (printBeginElement("value", name, obj, i2)) {
            printEscaped(String.valueOf(obj));
            printEndElement("value");
        }
    }

    private boolean printBeginElement(String str, String str2, Object obj, int i2) {
        printIndent();
        print("<", str);
        if (str2 != null) {
            printAttribute("name", str2);
        }
        if (i2 != -1) {
            printAttribute("index", Integer.toString(i2));
        }
        if (obj == null) {
            printAttribute("xsi:nil", "true");
            println("/>");
            return false;
        }
        if (obj.getClass().isArray()) {
            printAttribute("size", Integer.toString(((Object[]) obj).length));
        }
        print(">");
        return true;
    }

    private void printEndElement(String str) {
        print("</");
        print(str);
        println(">");
    }

    private void printEscaped(String str) {
        for (int i2 = 0; i2 < str.length(); i2++) {
            printEscaped(str.charAt(i2));
        }
    }

    private void printEscaped(char c2) {
        if (c2 == '\"') {
            print(SerializerConstants.ENTITY_QUOT);
            return;
        }
        if (c2 == '&') {
            print(SerializerConstants.ENTITY_AMP);
            return;
        }
        if (c2 == '\'') {
            print("&apos;");
            return;
        }
        if (c2 == '<') {
            print(SerializerConstants.ENTITY_LT);
            return;
        }
        if (c2 == '>') {
            print(SerializerConstants.ENTITY_GT);
        } else {
            if (c2 > 127) {
                print("&#");
                print((int) c2);
                print(';');
                return;
            }
            print(c2);
        }
    }
}
