package com.sun.webkit.dom;

import org.w3c.dom.Comment;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CommentImpl.class */
public class CommentImpl extends CharacterDataImpl implements Comment {
    CommentImpl(long peer) {
        super(peer);
    }

    static Comment getImpl(long peer) {
        return (Comment) create(peer);
    }
}
