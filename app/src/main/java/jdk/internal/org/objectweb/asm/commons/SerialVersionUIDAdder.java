package jdk.internal.org.objectweb.asm.commons;

import com.sun.corba.se.impl.io.ObjectStreamClass;
import com.sun.org.apache.bcel.internal.Constants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/SerialVersionUIDAdder.class */
public class SerialVersionUIDAdder extends ClassVisitor {
    private boolean computeSVUID;
    private boolean hasSVUID;
    private int access;
    private String name;
    private String[] interfaces;
    private Collection<Item> svuidFields;
    private boolean hasStaticInitializer;
    private Collection<Item> svuidConstructors;
    private Collection<Item> svuidMethods;

    public SerialVersionUIDAdder(ClassVisitor classVisitor) {
        this(Opcodes.ASM5, classVisitor);
        if (getClass() != SerialVersionUIDAdder.class) {
            throw new IllegalStateException();
        }
    }

    protected SerialVersionUIDAdder(int i2, ClassVisitor classVisitor) {
        super(i2, classVisitor);
        this.svuidFields = new ArrayList();
        this.svuidConstructors = new ArrayList();
        this.svuidMethods = new ArrayList();
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        this.computeSVUID = (i3 & 512) == 0;
        if (this.computeSVUID) {
            this.name = str;
            this.access = i3;
            this.interfaces = new String[strArr.length];
            System.arraycopy(strArr, 0, this.interfaces, 0, strArr.length);
        }
        super.visit(i2, i3, str, str2, str3, strArr);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        if (this.computeSVUID) {
            if (Constants.STATIC_INITIALIZER_NAME.equals(str)) {
                this.hasStaticInitializer = true;
            }
            int i3 = i2 & ObjectStreamClass.METHOD_MASK;
            if ((i2 & 2) == 0) {
                if (Constants.CONSTRUCTOR_NAME.equals(str)) {
                    this.svuidConstructors.add(new Item(str, i3, str2));
                } else if (!Constants.STATIC_INITIALIZER_NAME.equals(str)) {
                    this.svuidMethods.add(new Item(str, i3, str2));
                }
            }
        }
        return super.visitMethod(i2, str, str2, str3, strArr);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int i2, String str, String str2, String str3, Object obj) {
        if (this.computeSVUID) {
            if ("serialVersionUID".equals(str)) {
                this.computeSVUID = false;
                this.hasSVUID = true;
            }
            if ((i2 & 2) == 0 || (i2 & 136) == 0) {
                this.svuidFields.add(new Item(str, i2 & 223, str2));
            }
        }
        return super.visitField(i2, str, str2, str3, obj);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String str, String str2, String str3, int i2) {
        if (this.name != null && this.name.equals(str)) {
            this.access = i2;
        }
        super.visitInnerClass(str, str2, str3, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        if (this.computeSVUID && !this.hasSVUID) {
            try {
                addSVUID(computeSVUID());
            } catch (Throwable th) {
                throw new RuntimeException("Error while computing SVUID for " + this.name, th);
            }
        }
        super.visitEnd();
    }

    public boolean hasSVUID() {
        return this.hasSVUID;
    }

    protected void addSVUID(long j2) {
        FieldVisitor fieldVisitorVisitField = super.visitField(24, "serialVersionUID", "J", null, Long.valueOf(j2));
        if (fieldVisitorVisitField != null) {
            fieldVisitorVisitField.visitEnd();
        }
    }

    protected long computeSVUID() throws IOException {
        DataOutputStream dataOutputStream = null;
        long j2 = 0;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(this.name.replace('/', '.'));
            dataOutputStream.writeInt(this.access & ObjectStreamClass.CLASS_MASK);
            Arrays.sort(this.interfaces);
            for (int i2 = 0; i2 < this.interfaces.length; i2++) {
                dataOutputStream.writeUTF(this.interfaces[i2].replace('/', '.'));
            }
            writeItems(this.svuidFields, dataOutputStream, false);
            if (this.hasStaticInitializer) {
                dataOutputStream.writeUTF(Constants.STATIC_INITIALIZER_NAME);
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            }
            writeItems(this.svuidConstructors, dataOutputStream, true);
            writeItems(this.svuidMethods, dataOutputStream, true);
            dataOutputStream.flush();
            for (int iMin = Math.min(computeSHAdigest(byteArrayOutputStream.toByteArray()).length, 8) - 1; iMin >= 0; iMin--) {
                j2 = (j2 << 8) | (r0[iMin] & 255);
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            return j2;
        } catch (Throwable th) {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            throw th;
        }
    }

    protected byte[] computeSHAdigest(byte[] bArr) {
        try {
            return MessageDigest.getInstance("SHA").digest(bArr);
        } catch (Exception e2) {
            throw new UnsupportedOperationException(e2.toString());
        }
    }

    private static void writeItems(Collection<Item> collection, DataOutput dataOutput, boolean z2) throws IOException {
        int size = collection.size();
        Item[] itemArr = (Item[]) collection.toArray(new Item[size]);
        Arrays.sort(itemArr);
        for (int i2 = 0; i2 < size; i2++) {
            dataOutput.writeUTF(itemArr[i2].name);
            dataOutput.writeInt(itemArr[i2].access);
            dataOutput.writeUTF(z2 ? itemArr[i2].desc.replace('/', '.') : itemArr[i2].desc);
        }
    }

    /* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/SerialVersionUIDAdder$Item.class */
    private static class Item implements Comparable<Item> {
        final String name;
        final int access;
        final String desc;

        Item(String str, int i2, String str2) {
            this.name = str;
            this.access = i2;
            this.desc = str2;
        }

        @Override // java.lang.Comparable
        public int compareTo(Item item) {
            int iCompareTo = this.name.compareTo(item.name);
            if (iCompareTo == 0) {
                iCompareTo = this.desc.compareTo(item.desc);
            }
            return iCompareTo;
        }

        public boolean equals(Object obj) {
            return (obj instanceof Item) && compareTo((Item) obj) == 0;
        }

        public int hashCode() {
            return (this.name + this.desc).hashCode();
        }
    }
}
