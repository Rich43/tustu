package com.sun.xml.internal.fastinfoset.stax.events;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/EventBase.class */
public abstract class EventBase implements XMLEvent {
    protected int _eventType;
    protected Location _location = null;

    public EventBase() {
    }

    public EventBase(int eventType) {
        this._eventType = eventType;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public int getEventType() {
        return this._eventType;
    }

    protected void setEventType(int eventType) {
        this._eventType = eventType;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isStartElement() {
        return this._eventType == 1;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isEndElement() {
        return this._eventType == 2;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isEntityReference() {
        return this._eventType == 9;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isProcessingInstruction() {
        return this._eventType == 3;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isStartDocument() {
        return this._eventType == 7;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isEndDocument() {
        return this._eventType == 8;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public Location getLocation() {
        return this._location;
    }

    public void setLocation(Location loc) {
        this._location = loc;
    }

    public String getSystemId() {
        if (this._location == null) {
            return "";
        }
        return this._location.getSystemId();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.xml.stream.events.XMLEvent
    public Characters asCharacters() {
        if (isCharacters()) {
            return (Characters) this;
        }
        throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.charactersCast", new Object[]{getEventTypeString()}));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.xml.stream.events.XMLEvent
    public EndElement asEndElement() {
        if (isEndElement()) {
            return (EndElement) this;
        }
        throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.endElementCase", new Object[]{getEventTypeString()}));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.xml.stream.events.XMLEvent
    public StartElement asStartElement() {
        if (isStartElement()) {
            return (StartElement) this;
        }
        throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.startElementCase", new Object[]{getEventTypeString()}));
    }

    @Override // javax.xml.stream.events.XMLEvent
    public QName getSchemaType() {
        return null;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isAttribute() {
        return this._eventType == 10;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isCharacters() {
        return this._eventType == 4;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isNamespace() {
        return this._eventType == 13;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
    }

    private String getEventTypeString() {
        switch (this._eventType) {
            case 1:
                return "StartElementEvent";
            case 2:
                return "EndElementEvent";
            case 3:
                return "ProcessingInstructionEvent";
            case 4:
                return "CharacterEvent";
            case 5:
                return "CommentEvent";
            case 6:
            default:
                return "UNKNOWN_EVENT_TYPE";
            case 7:
                return "StartDocumentEvent";
            case 8:
                return "EndDocumentEvent";
            case 9:
                return "EntityReferenceEvent";
            case 10:
                return "AttributeBase";
            case 11:
                return "DTDEvent";
            case 12:
                return "CDATA";
        }
    }
}
