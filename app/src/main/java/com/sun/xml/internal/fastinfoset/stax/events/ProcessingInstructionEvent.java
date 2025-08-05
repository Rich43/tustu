package com.sun.xml.internal.fastinfoset.stax.events;

import javax.xml.stream.events.ProcessingInstruction;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/ProcessingInstructionEvent.class */
public class ProcessingInstructionEvent extends EventBase implements ProcessingInstruction {
    private String targetName;
    private String _data;

    public ProcessingInstructionEvent() {
        init();
    }

    public ProcessingInstructionEvent(String targetName, String data) {
        this.targetName = targetName;
        this._data = data;
        init();
    }

    protected void init() {
        setEventType(3);
    }

    @Override // javax.xml.stream.events.ProcessingInstruction
    public String getTarget() {
        return this.targetName;
    }

    public void setTarget(String targetName) {
        this.targetName = targetName;
    }

    public void setData(String data) {
        this._data = data;
    }

    @Override // javax.xml.stream.events.ProcessingInstruction
    public String getData() {
        return this._data;
    }

    public String toString() {
        if (this._data != null && this.targetName != null) {
            return "<?" + this.targetName + " " + this._data + "?>";
        }
        if (this.targetName != null) {
            return "<?" + this.targetName + "?>";
        }
        if (this._data != null) {
            return "<?" + this._data + "?>";
        }
        return "<??>";
    }
}
