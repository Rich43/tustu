package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Attribute;
import com.sun.java.util.jar.pack.ConstantPool;
import com.sun.java.util.jar.pack.Fixups;
import com.sun.java.util.jar.pack.Package;
import java.util.Arrays;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Code.class */
class Code extends Attribute.Holder {

    /* renamed from: m, reason: collision with root package name */
    Package.Class.Method f11843m;
    private static final ConstantPool.Entry[] noRefs;
    int max_stack;
    int max_locals;
    ConstantPool.Entry[] handler_class = noRefs;
    int[] handler_start = Constants.noInts;
    int[] handler_end = Constants.noInts;
    int[] handler_catch = Constants.noInts;
    byte[] bytes;
    Fixups fixups;
    Object insnMap;
    static final boolean shrinkMaps = true;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Code.class.desiredAssertionStatus();
        noRefs = ConstantPool.noRefs;
    }

    public Code(Package.Class.Method method) {
        this.f11843m = method;
    }

    public Package.Class.Method getMethod() {
        return this.f11843m;
    }

    public Package.Class thisClass() {
        return this.f11843m.thisClass();
    }

    public Package getPackage() {
        return this.f11843m.thisClass().getPackage();
    }

    @Override // com.sun.java.util.jar.pack.Attribute.Holder
    public ConstantPool.Entry[] getCPMap() {
        return this.f11843m.getCPMap();
    }

    int getLength() {
        return this.bytes.length;
    }

    int getMaxStack() {
        return this.max_stack;
    }

    void setMaxStack(int i2) {
        this.max_stack = i2;
    }

    int getMaxNALocals() {
        return this.max_locals - this.f11843m.getArgumentSize();
    }

    void setMaxNALocals(int i2) {
        this.max_locals = this.f11843m.getArgumentSize() + i2;
    }

    int getHandlerCount() {
        if (!$assertionsDisabled && this.handler_class.length != this.handler_start.length) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.handler_class.length != this.handler_end.length) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || this.handler_class.length == this.handler_catch.length) {
            return this.handler_class.length;
        }
        throw new AssertionError();
    }

    void setHandlerCount(int i2) {
        if (i2 > 0) {
            this.handler_class = new ConstantPool.Entry[i2];
            this.handler_start = new int[i2];
            this.handler_end = new int[i2];
            this.handler_catch = new int[i2];
        }
    }

    void setBytes(byte[] bArr) {
        this.bytes = bArr;
        if (this.fixups != null) {
            this.fixups.setBytes(bArr);
        }
    }

    void setInstructionMap(int[] iArr, int i2) {
        this.insnMap = allocateInstructionMap(iArr, i2);
    }

    void setInstructionMap(int[] iArr) {
        setInstructionMap(iArr, iArr.length);
    }

    int[] getInstructionMap() {
        return expandInstructionMap(getInsnMap());
    }

    void addFixups(Collection<Fixups.Fixup> collection) {
        if (this.fixups == null) {
            this.fixups = new Fixups(this.bytes);
        }
        if (!$assertionsDisabled && this.fixups.getBytes() != this.bytes) {
            throw new AssertionError();
        }
        this.fixups.addAll(collection);
    }

    @Override // com.sun.java.util.jar.pack.Attribute.Holder
    public void trimToSize() {
        if (this.fixups != null) {
            this.fixups.trimToSize();
            if (this.fixups.size() == 0) {
                this.fixups = null;
            }
        }
        super.trimToSize();
    }

    @Override // com.sun.java.util.jar.pack.Attribute.Holder
    protected void visitRefs(int i2, Collection<ConstantPool.Entry> collection) {
        int i3 = getPackage().verbose;
        if (i3 > 2) {
            System.out.println("Reference scan " + ((Object) this));
        }
        collection.addAll(Arrays.asList(this.handler_class));
        if (this.fixups != null) {
            this.fixups.visitRefs(collection);
        } else {
            ConstantPool.Entry[] cPMap = getCPMap();
            Instruction instructionInstructionAt = instructionAt(0);
            while (true) {
                Instruction instruction = instructionInstructionAt;
                if (instruction == null) {
                    break;
                }
                if (i3 > 4) {
                    System.out.println(instruction);
                }
                int cPIndex = instruction.getCPIndex();
                if (cPIndex >= 0) {
                    collection.add(cPMap[cPIndex]);
                }
                instructionInstructionAt = instruction.next();
            }
        }
        super.visitRefs(i2, collection);
    }

    private Object allocateInstructionMap(int[] iArr, int i2) {
        int length = getLength();
        if (length <= 255) {
            byte[] bArr = new byte[i2 + 1];
            for (int i3 = 0; i3 < i2; i3++) {
                bArr[i3] = (byte) (iArr[i3] - 128);
            }
            bArr[i2] = (byte) (length - 128);
            return bArr;
        }
        if (length < 65535) {
            short[] sArr = new short[i2 + 1];
            for (int i4 = 0; i4 < i2; i4++) {
                sArr[i4] = (short) (iArr[i4] + Short.MIN_VALUE);
            }
            sArr[i2] = (short) (length + Short.MIN_VALUE);
            return sArr;
        }
        int[] iArrCopyOf = Arrays.copyOf(iArr, i2 + 1);
        iArrCopyOf[i2] = length;
        return iArrCopyOf;
    }

    private int[] expandInstructionMap(Object obj) {
        int[] iArrCopyOfRange;
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            iArrCopyOfRange = new int[bArr.length - 1];
            for (int i2 = 0; i2 < iArrCopyOfRange.length; i2++) {
                iArrCopyOfRange[i2] = bArr[i2] - Byte.MIN_VALUE;
            }
        } else if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            iArrCopyOfRange = new int[sArr.length - 1];
            for (int i3 = 0; i3 < iArrCopyOfRange.length; i3++) {
                iArrCopyOfRange[i3] = sArr[i3] - (-128);
            }
        } else {
            int[] iArr = (int[]) obj;
            iArrCopyOfRange = Arrays.copyOfRange(iArr, 0, iArr.length - 1);
        }
        return iArrCopyOfRange;
    }

    Object getInsnMap() {
        if (this.insnMap != null) {
            return this.insnMap;
        }
        int[] iArr = new int[getLength()];
        int i2 = 0;
        Instruction instructionInstructionAt = instructionAt(0);
        while (true) {
            Instruction instruction = instructionInstructionAt;
            if (instruction != null) {
                int i3 = i2;
                i2++;
                iArr[i3] = instruction.getPC();
                instructionInstructionAt = instruction.next();
            } else {
                this.insnMap = allocateInstructionMap(iArr, i2);
                return this.insnMap;
            }
        }
    }

    public int encodeBCI(int i2) {
        int length;
        int iBinarySearch;
        if (i2 <= 0 || i2 > getLength()) {
            return i2;
        }
        Object insnMap = getInsnMap();
        if (insnMap instanceof byte[]) {
            byte[] bArr = (byte[]) insnMap;
            length = bArr.length;
            iBinarySearch = Arrays.binarySearch(bArr, (byte) (i2 - 128));
        } else if (insnMap instanceof short[]) {
            short[] sArr = (short[]) insnMap;
            length = sArr.length;
            iBinarySearch = Arrays.binarySearch(sArr, (short) (i2 + Short.MIN_VALUE));
        } else {
            int[] iArr = (int[]) insnMap;
            length = iArr.length;
            iBinarySearch = Arrays.binarySearch(iArr, i2);
        }
        if (!$assertionsDisabled && iBinarySearch == -1) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && iBinarySearch == 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && iBinarySearch == length) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || iBinarySearch != (-length) - 1) {
            return iBinarySearch >= 0 ? iBinarySearch : (length + i2) - ((-iBinarySearch) - 1);
        }
        throw new AssertionError();
    }

    public int decodeBCI(int i2) {
        int length;
        int iBinarySearch;
        if (i2 <= 0 || i2 > getLength()) {
            return i2;
        }
        Object insnMap = getInsnMap();
        if (insnMap instanceof byte[]) {
            byte[] bArr = (byte[]) insnMap;
            length = bArr.length;
            if (i2 < length) {
                return bArr[i2] - Byte.MIN_VALUE;
            }
            iBinarySearch = Arrays.binarySearch(bArr, (byte) (i2 - 128));
            if (iBinarySearch < 0) {
                iBinarySearch = (-iBinarySearch) - 1;
            }
            while (bArr[iBinarySearch - 1] - (iBinarySearch - 1) > (i2 - length) - 128) {
                iBinarySearch--;
            }
        } else if (insnMap instanceof short[]) {
            short[] sArr = (short[]) insnMap;
            length = sArr.length;
            if (i2 < length) {
                return sArr[i2] - Short.MIN_VALUE;
            }
            iBinarySearch = Arrays.binarySearch(sArr, (short) (i2 + Short.MIN_VALUE));
            if (iBinarySearch < 0) {
                iBinarySearch = (-iBinarySearch) - 1;
            }
            while (sArr[iBinarySearch - 1] - (iBinarySearch - 1) > (i2 - length) + Short.MIN_VALUE) {
                iBinarySearch--;
            }
        } else {
            int[] iArr = (int[]) insnMap;
            length = iArr.length;
            if (i2 < length) {
                return iArr[i2];
            }
            iBinarySearch = Arrays.binarySearch(iArr, i2);
            if (iBinarySearch < 0) {
                iBinarySearch = (-iBinarySearch) - 1;
            }
            while (iArr[iBinarySearch - 1] - (iBinarySearch - 1) > i2 - length) {
                iBinarySearch--;
            }
        }
        return (i2 - length) + iBinarySearch;
    }

    public void finishRefs(ConstantPool.Index index) {
        if (this.fixups != null) {
            this.fixups.finishRefs(index);
            this.fixups = null;
        }
    }

    Instruction instructionAt(int i2) {
        return Instruction.at(this.bytes, i2);
    }

    static boolean flagsRequireCode(int i2) {
        return (i2 & 1280) == 0;
    }

    public String toString() {
        return ((Object) this.f11843m) + ".Code";
    }

    public int getInt(int i2) {
        return Instruction.getInt(this.bytes, i2);
    }

    public int getShort(int i2) {
        return Instruction.getShort(this.bytes, i2);
    }

    public int getByte(int i2) {
        return Instruction.getByte(this.bytes, i2);
    }

    void setInt(int i2, int i3) {
        Instruction.setInt(this.bytes, i2, i3);
    }

    void setShort(int i2, int i3) {
        Instruction.setShort(this.bytes, i2, i3);
    }

    void setByte(int i2, int i3) {
        Instruction.setByte(this.bytes, i2, i3);
    }
}
