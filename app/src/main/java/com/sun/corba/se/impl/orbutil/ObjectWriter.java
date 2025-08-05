package com.sun.corba.se.impl.orbutil;

import java.util.Arrays;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectWriter.class */
public abstract class ObjectWriter {
    protected StringBuffer result = new StringBuffer();

    public abstract void startObject(Object obj);

    public abstract void startElement();

    public abstract void endElement();

    public abstract void endObject(String str);

    public abstract void endObject();

    public static ObjectWriter make(boolean z2, int i2, int i3) {
        if (z2) {
            return new IndentingObjectWriter(i2, i3);
        }
        return new SimpleObjectWriter();
    }

    public String toString() {
        return this.result.toString();
    }

    public void append(boolean z2) {
        this.result.append(z2);
    }

    public void append(char c2) {
        this.result.append(c2);
    }

    public void append(short s2) {
        this.result.append((int) s2);
    }

    public void append(int i2) {
        this.result.append(i2);
    }

    public void append(long j2) {
        this.result.append(j2);
    }

    public void append(float f2) {
        this.result.append(f2);
    }

    public void append(double d2) {
        this.result.append(d2);
    }

    public void append(String str) {
        this.result.append(str);
    }

    protected ObjectWriter() {
    }

    protected void appendObjectHeader(Object obj) {
        this.result.append(obj.getClass().getName());
        this.result.append("<");
        this.result.append(System.identityHashCode(obj));
        this.result.append(">");
        Class<?> componentType = obj.getClass().getComponentType();
        if (componentType != null) {
            this.result.append("[");
            if (componentType == Boolean.TYPE) {
                this.result.append(((boolean[]) obj).length);
                this.result.append("]");
            } else if (componentType == Byte.TYPE) {
                this.result.append(((byte[]) obj).length);
                this.result.append("]");
            } else if (componentType == Short.TYPE) {
                this.result.append(((short[]) obj).length);
                this.result.append("]");
            } else if (componentType == Integer.TYPE) {
                this.result.append(((int[]) obj).length);
                this.result.append("]");
            } else if (componentType == Long.TYPE) {
                this.result.append(((long[]) obj).length);
                this.result.append("]");
            } else if (componentType == Character.TYPE) {
                this.result.append(((char[]) obj).length);
                this.result.append("]");
            } else if (componentType == Float.TYPE) {
                this.result.append(((float[]) obj).length);
                this.result.append("]");
            } else if (componentType == Double.TYPE) {
                this.result.append(((double[]) obj).length);
                this.result.append("]");
            } else {
                this.result.append(((Object[]) obj).length);
                this.result.append("]");
            }
        }
        this.result.append("(");
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectWriter$IndentingObjectWriter.class */
    private static class IndentingObjectWriter extends ObjectWriter {
        private int level;
        private int increment;

        public IndentingObjectWriter(int i2, int i3) {
            this.level = i2;
            this.increment = i3;
            startLine();
        }

        private void startLine() {
            char[] cArr = new char[this.level * this.increment];
            Arrays.fill(cArr, ' ');
            this.result.append(cArr);
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void startObject(Object obj) {
            appendObjectHeader(obj);
            this.level++;
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void startElement() {
            this.result.append("\n");
            startLine();
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void endElement() {
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void endObject(String str) {
            this.level--;
            this.result.append(str);
            this.result.append(")");
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void endObject() {
            this.level--;
            this.result.append("\n");
            startLine();
            this.result.append(")");
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectWriter$SimpleObjectWriter.class */
    private static class SimpleObjectWriter extends ObjectWriter {
        private SimpleObjectWriter() {
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void startObject(Object obj) {
            appendObjectHeader(obj);
            this.result.append(" ");
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void startElement() {
            this.result.append(" ");
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void endObject(String str) {
            this.result.append(str);
            this.result.append(")");
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void endElement() {
        }

        @Override // com.sun.corba.se.impl.orbutil.ObjectWriter
        public void endObject() {
            this.result.append(")");
        }
    }
}
