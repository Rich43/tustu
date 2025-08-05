package com.sun.org.apache.xerces.internal.xinclude;

import java.util.Stack;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XPointerFramework.class */
public class XPointerFramework {
    XPointerSchema[] fXPointerSchema;
    String[] fSchemaPointerName;
    String[] fSchemaPointerURI;
    String fSchemaPointer;
    String fCurrentSchemaPointer;
    Stack fSchemaNotAvailable;
    int fCountSchemaName;
    int schemaLength;
    XPointerSchema fDefaultXPointerSchema;

    public XPointerFramework() {
        this(null);
    }

    public XPointerFramework(XPointerSchema[] xpointerschema) {
        this.fCountSchemaName = 0;
        this.schemaLength = 0;
        this.fXPointerSchema = xpointerschema;
        this.fSchemaNotAvailable = new Stack();
    }

    public void reset() {
        this.fXPointerSchema = null;
        this.fXPointerSchema = null;
        this.fCountSchemaName = 0;
        this.schemaLength = 0;
        this.fSchemaPointerName = null;
        this.fSchemaPointerURI = null;
        this.fDefaultXPointerSchema = null;
        this.fCurrentSchemaPointer = null;
    }

    public void setXPointerSchema(XPointerSchema[] xpointerschema) {
        this.fXPointerSchema = xpointerschema;
    }

    public void setSchemaPointer(String schemaPointer) {
        this.fSchemaPointer = schemaPointer;
    }

    public XPointerSchema getNextXPointerSchema() {
        int i2 = this.fCountSchemaName;
        if (this.fSchemaPointerName == null) {
            getSchemaNames();
        }
        if (this.fDefaultXPointerSchema == null) {
            getDefaultSchema();
        }
        if (this.fDefaultXPointerSchema.getXpointerSchemaName().equalsIgnoreCase(this.fSchemaPointerName[i2])) {
            this.fDefaultXPointerSchema.reset();
            this.fDefaultXPointerSchema.setXPointerSchemaPointer(this.fSchemaPointerURI[i2]);
            this.fCountSchemaName = i2 + 1;
            return getDefaultSchema();
        }
        if (this.fXPointerSchema == null) {
            this.fCountSchemaName = i2 + 1;
            return null;
        }
        int fschemalength = this.fXPointerSchema.length;
        while (this.fSchemaPointerName[i2] != null) {
            for (int j2 = 0; j2 < fschemalength; j2++) {
                if (this.fSchemaPointerName[i2].equalsIgnoreCase(this.fXPointerSchema[j2].getXpointerSchemaName())) {
                    this.fXPointerSchema[j2].setXPointerSchemaPointer(this.fSchemaPointerURI[i2]);
                    this.fCountSchemaName = i2 + 1;
                    return this.fXPointerSchema[j2];
                }
            }
            if (this.fSchemaNotAvailable == null) {
                this.fSchemaNotAvailable = new Stack();
            }
            this.fSchemaNotAvailable.push(this.fSchemaPointerName[i2]);
            i2++;
        }
        return null;
    }

    public XPointerSchema getDefaultSchema() {
        if (this.fDefaultXPointerSchema == null) {
            this.fDefaultXPointerSchema = new XPointerElementHandler();
        }
        return this.fDefaultXPointerSchema;
    }

    public void getSchemaNames() {
        int schemapointerURIindex = 0;
        int length = this.fSchemaPointer.length();
        this.fSchemaPointerName = new String[5];
        this.fSchemaPointerURI = new String[5];
        int index = this.fSchemaPointer.indexOf(40);
        if (index > 0) {
            int schemapointerindex = 0 + 1;
            int index2 = index + 1;
            this.fSchemaPointerName[0] = this.fSchemaPointer.substring(0, index).trim();
            int lastindex = index2;
            int count = 0 + 1;
            while (index2 < length) {
                char c2 = this.fSchemaPointer.charAt(index2);
                if (c2 == '(') {
                    count++;
                }
                if (c2 == ')') {
                    count--;
                }
                if (count == 0) {
                    String tempURI = this.fSchemaPointer.substring(lastindex, index2).trim();
                    int i2 = schemapointerURIindex;
                    schemapointerURIindex++;
                    this.fSchemaPointerURI[i2] = getEscapedURI(tempURI);
                    lastindex = index2;
                    int iIndexOf = this.fSchemaPointer.indexOf(40, lastindex);
                    index2 = iIndexOf;
                    if (iIndexOf != -1) {
                        int i3 = schemapointerindex;
                        schemapointerindex++;
                        this.fSchemaPointerName[i3] = this.fSchemaPointer.substring(lastindex + 1, index2).trim();
                        count++;
                        lastindex = index2 + 1;
                    } else {
                        index2 = lastindex;
                    }
                }
                index2++;
            }
            this.schemaLength = schemapointerURIindex - 1;
        }
    }

    public String getEscapedURI(String URI) {
        return URI;
    }

    public int getSchemaCount() {
        return this.schemaLength;
    }

    public int getCurrentPointer() {
        return this.fCountSchemaName;
    }
}
