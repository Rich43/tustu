package com.oracle.webservices.internal.api.message;

import com.sun.xml.internal.ws.encoding.ContentTypeImpl;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/message/ContentType.class */
public interface ContentType {
    String getContentType();

    String getSOAPActionHeader();

    String getAcceptHeader();

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/ContentType$Builder.class */
    public static class Builder {
        private String contentType;
        private String soapAction;
        private String accept;
        private String charset;

        public Builder contentType(String s2) {
            this.contentType = s2;
            return this;
        }

        public Builder soapAction(String s2) {
            this.soapAction = s2;
            return this;
        }

        public Builder accept(String s2) {
            this.accept = s2;
            return this;
        }

        public Builder charset(String s2) {
            this.charset = s2;
            return this;
        }

        public ContentType build() {
            return new ContentTypeImpl(this.contentType, this.soapAction, this.accept, this.charset);
        }
    }
}
