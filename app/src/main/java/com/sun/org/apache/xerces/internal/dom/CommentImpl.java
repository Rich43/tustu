package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xpath.internal.compiler.PsuedoNames;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/CommentImpl.class */
public class CommentImpl extends CharacterDataImpl implements CharacterData, Comment {
    static final long serialVersionUID = -2685736833408134044L;

    public CommentImpl(CoreDocumentImpl ownerDoc, String data) {
        super(ownerDoc, data);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public short getNodeType() {
        return (short) 8;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNodeName() {
        return PsuedoNames.PSEUDONAME_COMMENT;
    }
}
