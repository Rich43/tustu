package com.sun.xml.internal.fastinfoset.stax.events;

import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/EntityReferenceEvent.class */
public class EntityReferenceEvent extends EventBase implements EntityReference {
    private EntityDeclaration _entityDeclaration;
    private String _entityName;

    public EntityReferenceEvent() {
        init();
    }

    public EntityReferenceEvent(String entityName, EntityDeclaration entityDeclaration) {
        init();
        this._entityName = entityName;
        this._entityDeclaration = entityDeclaration;
    }

    @Override // javax.xml.stream.events.EntityReference
    public String getName() {
        return this._entityName;
    }

    @Override // javax.xml.stream.events.EntityReference
    public EntityDeclaration getDeclaration() {
        return this._entityDeclaration;
    }

    public void setName(String name) {
        this._entityName = name;
    }

    public void setDeclaration(EntityDeclaration declaration) {
        this._entityDeclaration = declaration;
    }

    public String toString() {
        String text = this._entityDeclaration.getReplacementText();
        if (text == null) {
            text = "";
        }
        return "&" + getName() + ";='" + text + PdfOps.SINGLE_QUOTE_TOKEN;
    }

    protected void init() {
        setEventType(9);
    }
}
