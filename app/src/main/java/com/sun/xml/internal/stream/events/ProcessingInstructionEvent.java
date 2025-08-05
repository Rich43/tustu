package com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.events.ProcessingInstruction;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/ProcessingInstructionEvent.class */
public class ProcessingInstructionEvent extends DummyEvent implements ProcessingInstruction {
    private String fName;
    private String fContent;

    public ProcessingInstructionEvent() {
        init();
    }

    public ProcessingInstructionEvent(String targetName, String data) {
        this(targetName, data, null);
    }

    public ProcessingInstructionEvent(String targetName, String data, Location loc) {
        init();
        this.fName = targetName;
        this.fContent = data;
        setLocation(loc);
    }

    protected void init() {
        setEventType(3);
    }

    @Override // javax.xml.stream.events.ProcessingInstruction
    public String getTarget() {
        return this.fName;
    }

    public void setTarget(String targetName) {
        this.fName = targetName;
    }

    public void setData(String data) {
        this.fContent = data;
    }

    @Override // javax.xml.stream.events.ProcessingInstruction
    public String getData() {
        return this.fContent;
    }

    public String toString() {
        if (this.fContent != null && this.fName != null) {
            return "<?" + this.fName + " " + this.fContent + "?>";
        }
        if (this.fName != null) {
            return "<?" + this.fName + "?>";
        }
        if (this.fContent != null) {
            return "<?" + this.fContent + "?>";
        }
        return "<??>";
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write(toString());
    }
}
