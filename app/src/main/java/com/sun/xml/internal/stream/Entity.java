package com.sun.xml.internal.stream;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.xml.internal.stream.util.BufferAllocator;
import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/* loaded from: rt.jar:com/sun/xml/internal/stream/Entity.class */
public abstract class Entity {
    public String name;
    public boolean inExternalSubset;

    public abstract boolean isExternal();

    public abstract boolean isUnparsed();

    public Entity() {
        clear();
    }

    public Entity(String name, boolean inExternalSubset) {
        this.name = name;
        this.inExternalSubset = inExternalSubset;
    }

    public boolean isEntityDeclInExternalSubset() {
        return this.inExternalSubset;
    }

    public void clear() {
        this.name = null;
        this.inExternalSubset = false;
    }

    public void setValues(Entity entity) {
        this.name = entity.name;
        this.inExternalSubset = entity.inExternalSubset;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/Entity$InternalEntity.class */
    public static class InternalEntity extends Entity {
        public String text;

        public InternalEntity() {
            clear();
        }

        public InternalEntity(String name, String text, boolean inExternalSubset) {
            super(name, inExternalSubset);
            this.text = text;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public final boolean isExternal() {
            return false;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public final boolean isUnparsed() {
            return false;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public void clear() {
            super.clear();
            this.text = null;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public void setValues(Entity entity) {
            super.setValues(entity);
            this.text = null;
        }

        public void setValues(InternalEntity entity) {
            super.setValues((Entity) entity);
            this.text = entity.text;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/Entity$ExternalEntity.class */
    public static class ExternalEntity extends Entity {
        public XMLResourceIdentifier entityLocation;
        public String notation;

        public ExternalEntity() {
            clear();
        }

        public ExternalEntity(String name, XMLResourceIdentifier entityLocation, String notation, boolean inExternalSubset) {
            super(name, inExternalSubset);
            this.entityLocation = entityLocation;
            this.notation = notation;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public final boolean isExternal() {
            return true;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public final boolean isUnparsed() {
            return this.notation != null;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public void clear() {
            super.clear();
            this.entityLocation = null;
            this.notation = null;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public void setValues(Entity entity) {
            super.setValues(entity);
            this.entityLocation = null;
            this.notation = null;
        }

        public void setValues(ExternalEntity entity) {
            super.setValues((Entity) entity);
            this.entityLocation = entity.entityLocation;
            this.notation = entity.notation;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/Entity$ScannedEntity.class */
    public static class ScannedEntity extends Entity {
        public static final int DEFAULT_BUFFER_SIZE = 8192;
        public static final int DEFAULT_XMLDECL_BUFFER_SIZE = 28;
        public static final int DEFAULT_INTERNAL_BUFFER_SIZE = 1024;
        public InputStream stream;
        public Reader reader;
        public XMLResourceIdentifier entityLocation;
        public String encoding;
        public boolean literal;
        public boolean isExternal;
        public String version;
        public char[] ch;
        public int position;
        public int count;
        public int fTotalCountTillLastLoad;
        public int fLastCount;
        public int baseCharOffset;
        public int startPosition;
        public boolean mayReadChunks;
        public boolean isGE;
        public int fBufferSize = 8192;
        public int lineNumber = 1;
        public int columnNumber = 1;
        boolean declaredEncoding = false;
        boolean externallySpecifiedEncoding = false;
        public String xmlVersion = "1.0";
        public boolean xmlDeclChunkRead = false;

        public String getEncodingName() {
            return this.encoding;
        }

        public String getEntityVersion() {
            return this.version;
        }

        public void setEntityVersion(String version) {
            this.version = version;
        }

        public Reader getEntityReader() {
            return this.reader;
        }

        public InputStream getEntityInputStream() {
            return this.stream;
        }

        public ScannedEntity(boolean isGE, String name, XMLResourceIdentifier entityLocation, InputStream stream, Reader reader, String encoding, boolean literal, boolean mayReadChunks, boolean isExternal) {
            this.ch = null;
            this.isGE = false;
            this.isGE = isGE;
            this.name = name;
            this.entityLocation = entityLocation;
            this.stream = stream;
            this.reader = reader;
            this.encoding = encoding;
            this.literal = literal;
            this.mayReadChunks = mayReadChunks;
            this.isExternal = isExternal;
            int size = isExternal ? this.fBufferSize : 1024;
            BufferAllocator ba2 = ThreadLocalBufferAllocator.getBufferAllocator();
            this.ch = ba2.getCharBuffer(size);
            if (this.ch == null) {
                this.ch = new char[size];
            }
        }

        public void close() throws IOException {
            BufferAllocator ba2 = ThreadLocalBufferAllocator.getBufferAllocator();
            ba2.returnCharBuffer(this.ch);
            this.ch = null;
            this.reader.close();
        }

        public boolean isEncodingExternallySpecified() {
            return this.externallySpecifiedEncoding;
        }

        public void setEncodingExternallySpecified(boolean value) {
            this.externallySpecifiedEncoding = value;
        }

        public boolean isDeclaredEncoding() {
            return this.declaredEncoding;
        }

        public void setDeclaredEncoding(boolean value) {
            this.declaredEncoding = value;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public final boolean isExternal() {
            return this.isExternal;
        }

        @Override // com.sun.xml.internal.stream.Entity
        public final boolean isUnparsed() {
            return false;
        }

        public String toString() {
            StringBuffer str = new StringBuffer();
            str.append("name=\"" + this.name + '\"');
            str.append(",ch=" + new String(this.ch));
            str.append(",position=" + this.position);
            str.append(",count=" + this.count);
            return str.toString();
        }
    }
}
