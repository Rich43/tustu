package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.ProcessingInstruction;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/ProcessingInstructionImpl.class */
public class ProcessingInstructionImpl extends CharacterDataImpl implements ProcessingInstruction {
    static final long serialVersionUID = 7554435174099981510L;
    protected String target;

    public ProcessingInstructionImpl(CoreDocumentImpl ownerDoc, String target, String data) {
        super(ownerDoc, data);
        this.target = target;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 7;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.target;
    }

    @Override // org.w3c.dom.ProcessingInstruction
    public String getTarget() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.target;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CharacterDataImpl, org.w3c.dom.ProcessingInstruction
    public String getData() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.data;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.CharacterDataImpl, org.w3c.dom.ProcessingInstruction
    public void setData(String data) {
        setNodeValue(data);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getBaseURI() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.ownerNode.getBaseURI();
    }
}
