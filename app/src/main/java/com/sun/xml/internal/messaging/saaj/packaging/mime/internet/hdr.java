package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

/* compiled from: InternetHeaders.java */
/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/hdr.class */
class hdr implements Header {
    String name;
    String line;

    hdr(String l2) {
        int i2 = l2.indexOf(58);
        if (i2 < 0) {
            this.name = l2.trim();
        } else {
            this.name = l2.substring(0, i2).trim();
        }
        this.line = l2;
    }

    hdr(String n2, String v2) {
        this.name = n2;
        this.line = n2 + ": " + v2;
    }

    @Override // com.sun.xml.internal.messaging.saaj.packaging.mime.Header
    public String getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.messaging.saaj.packaging.mime.Header
    public String getValue() {
        int j2;
        char c2;
        char c3;
        int i2 = this.line.indexOf(58);
        if (i2 < 0) {
            return this.line;
        }
        if (this.name.equalsIgnoreCase("Content-Description")) {
            j2 = i2 + 1;
            while (j2 < this.line.length() && ((c3 = this.line.charAt(j2)) == '\t' || c3 == '\r' || c3 == '\n')) {
                j2++;
            }
        } else {
            j2 = i2 + 1;
            while (j2 < this.line.length() && ((c2 = this.line.charAt(j2)) == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n')) {
                j2++;
            }
        }
        return this.line.substring(j2);
    }
}
