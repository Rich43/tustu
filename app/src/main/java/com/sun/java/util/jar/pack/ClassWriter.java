package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Attribute;
import com.sun.java.util.jar.pack.ConstantPool;
import com.sun.java.util.jar.pack.Package;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/ClassWriter.class */
class ClassWriter {
    int verbose;
    Package pkg;
    Package.Class cls;
    DataOutputStream out;
    ConstantPool.Index cpIndex;
    ConstantPool.Index bsmIndex;
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    DataOutputStream bufOut = new DataOutputStream(this.buf);
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ClassWriter.class.desiredAssertionStatus();
    }

    ClassWriter(Package.Class r8, OutputStream outputStream) throws IOException {
        this.pkg = r8.getPackage();
        this.cls = r8;
        this.verbose = this.pkg.verbose;
        this.out = new DataOutputStream(new BufferedOutputStream(outputStream));
        this.cpIndex = ConstantPool.makeIndex(r8.toString(), r8.getCPMap());
        this.cpIndex.flattenSigs = true;
        if (r8.hasBootstrapMethods()) {
            this.bsmIndex = ConstantPool.makeIndex(this.cpIndex.debugName + ".BootstrapMethods", r8.getBootstrapMethodMap());
        }
        if (this.verbose > 1) {
            Utils.log.fine("local CP=" + (this.verbose > 2 ? this.cpIndex.dumpString() : this.cpIndex.toString()));
        }
    }

    private void writeShort(int i2) throws IOException {
        this.out.writeShort(i2);
    }

    private void writeInt(int i2) throws IOException {
        this.out.writeInt(i2);
    }

    private void writeRef(ConstantPool.Entry entry) throws IOException {
        writeRef(entry, this.cpIndex);
    }

    private void writeRef(ConstantPool.Entry entry, ConstantPool.Index index) throws IOException {
        writeShort(entry == null ? 0 : index.indexOf(entry));
    }

    void write() throws IOException {
        boolean z2 = false;
        try {
            if (this.verbose > 1) {
                Utils.log.fine("...writing " + ((Object) this.cls));
            }
            writeMagicNumbers();
            writeConstantPool();
            writeHeader();
            writeMembers(false);
            writeMembers(true);
            writeAttributes(0, this.cls);
            this.out.flush();
            z2 = true;
            if (1 == 0) {
                Utils.log.warning("Error on output of " + ((Object) this.cls));
            }
        } catch (Throwable th) {
            if (!z2) {
                Utils.log.warning("Error on output of " + ((Object) this.cls));
            }
            throw th;
        }
    }

    void writeMagicNumbers() throws IOException {
        writeInt(this.cls.magic);
        writeShort(this.cls.version.minor);
        writeShort(this.cls.version.major);
    }

    void writeConstantPool() throws IOException {
        ConstantPool.Entry[] entryArr = this.cls.cpMap;
        writeShort(entryArr.length);
        int i2 = 0;
        while (i2 < entryArr.length) {
            ConstantPool.Entry entry = entryArr[i2];
            if (!$assertionsDisabled) {
                if ((entry == null) != (i2 == 0 || (entryArr[i2 - 1] != null && entryArr[i2 - 1].isDoubleWord()))) {
                    throw new AssertionError();
                }
            }
            if (entry != null) {
                byte tag = entry.getTag();
                if (this.verbose > 2) {
                    Utils.log.fine("   CP[" + i2 + "] = " + ((Object) entry));
                }
                this.out.write(tag);
                switch (tag) {
                    case 1:
                        this.out.writeUTF(entry.stringValue());
                        break;
                    case 2:
                    case 14:
                    default:
                        throw new IOException("Bad constant pool tag " + ((int) tag));
                    case 3:
                        this.out.writeInt(((ConstantPool.NumberEntry) entry).numberValue().intValue());
                        break;
                    case 4:
                        this.out.writeInt(Float.floatToRawIntBits(((ConstantPool.NumberEntry) entry).numberValue().floatValue()));
                        break;
                    case 5:
                        this.out.writeLong(((ConstantPool.NumberEntry) entry).numberValue().longValue());
                        break;
                    case 6:
                        this.out.writeLong(Double.doubleToRawLongBits(((ConstantPool.NumberEntry) entry).numberValue().doubleValue()));
                        break;
                    case 7:
                    case 8:
                    case 16:
                        writeRef(entry.getRef(0));
                        break;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        writeRef(entry.getRef(0));
                        writeRef(entry.getRef(1));
                        break;
                    case 13:
                        throw new AssertionError((Object) "CP should have Signatures remapped to Utf8");
                    case 15:
                        ConstantPool.MethodHandleEntry methodHandleEntry = (ConstantPool.MethodHandleEntry) entry;
                        this.out.writeByte(methodHandleEntry.refKind);
                        writeRef(methodHandleEntry.getRef(0));
                        break;
                    case 17:
                        throw new AssertionError((Object) "CP should have BootstrapMethods moved to side-table");
                    case 18:
                        writeRef(entry.getRef(0), this.bsmIndex);
                        writeRef(entry.getRef(1));
                        break;
                }
            }
            i2++;
        }
    }

    void writeHeader() throws IOException {
        writeShort(this.cls.flags);
        writeRef(this.cls.thisClass);
        writeRef(this.cls.superClass);
        writeShort(this.cls.interfaces.length);
        for (int i2 = 0; i2 < this.cls.interfaces.length; i2++) {
            writeRef(this.cls.interfaces[i2]);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void writeMembers(boolean z2) throws IOException {
        List methods;
        if (!z2) {
            methods = this.cls.getFields();
        } else {
            methods = this.cls.getMethods();
        }
        writeShort(methods.size());
        Iterator<Package.Class.Method> it = methods.iterator();
        while (it.hasNext()) {
            writeMember(it.next(), z2);
        }
    }

    void writeMember(Package.Class.Member member, boolean z2) throws IOException {
        if (this.verbose > 2) {
            Utils.log.fine("writeMember " + ((Object) member));
        }
        writeShort(member.flags);
        writeRef(member.getDescriptor().nameRef);
        writeRef(member.getDescriptor().typeRef);
        writeAttributes(!z2 ? 1 : 2, member);
    }

    private void reorderBSMandICS(Attribute.Holder holder) {
        Attribute attribute;
        Attribute attribute2 = holder.getAttribute(Package.attrBootstrapMethodsEmpty);
        if (attribute2 == null || (attribute = holder.getAttribute(Package.attrInnerClassesEmpty)) == null) {
            return;
        }
        int iIndexOf = holder.attributes.indexOf(attribute2);
        int iIndexOf2 = holder.attributes.indexOf(attribute);
        if (iIndexOf > iIndexOf2) {
            holder.attributes.remove(attribute2);
            holder.attributes.add(iIndexOf2, attribute2);
        }
    }

    void writeAttributes(int i2, Attribute.Holder holder) throws IOException {
        if (holder.attributes == null) {
            writeShort(0);
            return;
        }
        if (holder instanceof Package.Class) {
            reorderBSMandICS(holder);
        }
        writeShort(holder.attributes.size());
        for (Attribute attribute : holder.attributes) {
            attribute.finishRefs(this.cpIndex);
            writeRef(attribute.getNameRef());
            if (attribute.layout() == Package.attrCodeEmpty || attribute.layout() == Package.attrBootstrapMethodsEmpty || attribute.layout() == Package.attrInnerClassesEmpty) {
                DataOutputStream dataOutputStream = this.out;
                if (!$assertionsDisabled && this.out == this.bufOut) {
                    throw new AssertionError();
                }
                this.buf.reset();
                this.out = this.bufOut;
                if ("Code".equals(attribute.name())) {
                    writeCode(((Package.Class.Method) holder).code);
                } else if ("BootstrapMethods".equals(attribute.name())) {
                    if (!$assertionsDisabled && holder != this.cls) {
                        throw new AssertionError();
                    }
                    writeBootstrapMethods(this.cls);
                } else if ("InnerClasses".equals(attribute.name())) {
                    if (!$assertionsDisabled && holder != this.cls) {
                        throw new AssertionError();
                    }
                    writeInnerClasses(this.cls);
                } else {
                    throw new AssertionError();
                }
                this.out = dataOutputStream;
                if (this.verbose > 2) {
                    Utils.log.fine("Attribute " + attribute.name() + " [" + this.buf.size() + "]");
                }
                writeInt(this.buf.size());
                this.buf.writeTo(this.out);
            } else {
                if (this.verbose > 2) {
                    Utils.log.fine("Attribute " + attribute.name() + " [" + attribute.size() + "]");
                }
                writeInt(attribute.size());
                this.out.write(attribute.bytes());
            }
        }
    }

    void writeCode(Code code) throws IOException {
        code.finishRefs(this.cpIndex);
        writeShort(code.max_stack);
        writeShort(code.max_locals);
        writeInt(code.bytes.length);
        this.out.write(code.bytes);
        int handlerCount = code.getHandlerCount();
        writeShort(handlerCount);
        for (int i2 = 0; i2 < handlerCount; i2++) {
            writeShort(code.handler_start[i2]);
            writeShort(code.handler_end[i2]);
            writeShort(code.handler_catch[i2]);
            writeRef(code.handler_class[i2]);
        }
        writeAttributes(3, code);
    }

    void writeBootstrapMethods(Package.Class r4) throws IOException {
        List<ConstantPool.BootstrapMethodEntry> bootstrapMethods = r4.getBootstrapMethods();
        writeShort(bootstrapMethods.size());
        for (ConstantPool.BootstrapMethodEntry bootstrapMethodEntry : bootstrapMethods) {
            writeRef(bootstrapMethodEntry.bsmRef);
            writeShort(bootstrapMethodEntry.argRefs.length);
            for (ConstantPool.Entry entry : bootstrapMethodEntry.argRefs) {
                writeRef(entry);
            }
        }
    }

    void writeInnerClasses(Package.Class r4) throws IOException {
        List<Package.InnerClass> innerClasses = r4.getInnerClasses();
        writeShort(innerClasses.size());
        for (Package.InnerClass innerClass : innerClasses) {
            writeRef(innerClass.thisClass);
            writeRef(innerClass.outerClass);
            writeRef(innerClass.name);
            writeShort(innerClass.flags);
        }
    }
}
