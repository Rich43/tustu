package com.sun.xml.internal.ws.api.model;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/ParameterBinding.class */
public final class ParameterBinding {
    public static final ParameterBinding BODY = new ParameterBinding(Kind.BODY, null);
    public static final ParameterBinding HEADER = new ParameterBinding(Kind.HEADER, null);
    public static final ParameterBinding UNBOUND = new ParameterBinding(Kind.UNBOUND, null);
    public final Kind kind;
    private String mimeType;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/ParameterBinding$Kind.class */
    public enum Kind {
        BODY,
        HEADER,
        UNBOUND,
        ATTACHMENT
    }

    public static ParameterBinding createAttachment(String mimeType) {
        return new ParameterBinding(Kind.ATTACHMENT, mimeType);
    }

    private ParameterBinding(Kind kind, String mimeType) {
        this.kind = kind;
        this.mimeType = mimeType;
    }

    public String toString() {
        return this.kind.toString();
    }

    public String getMimeType() {
        if (!isAttachment()) {
            throw new IllegalStateException();
        }
        return this.mimeType;
    }

    public boolean isBody() {
        return this == BODY;
    }

    public boolean isHeader() {
        return this == HEADER;
    }

    public boolean isUnbound() {
        return this == UNBOUND;
    }

    public boolean isAttachment() {
        return this.kind == Kind.ATTACHMENT;
    }
}
