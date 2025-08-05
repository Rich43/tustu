package com.sun.xml.internal.ws.org.objectweb.asm;

import com.sun.org.apache.bcel.internal.Constants;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/MethodWriter.class */
class MethodWriter implements MethodVisitor {
    static final int ACC_CONSTRUCTOR = 262144;
    static final int SAME_FRAME = 0;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
    static final int RESERVED = 128;
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
    static final int CHOP_FRAME = 248;
    static final int SAME_FRAME_EXTENDED = 251;
    static final int APPEND_FRAME = 252;
    static final int FULL_FRAME = 255;
    private static final int FRAMES = 0;
    private static final int MAXS = 1;
    private static final int NOTHING = 2;
    MethodWriter next;
    final ClassWriter cw;
    private int access;
    private final int name;
    private final int desc;
    private final String descriptor;
    String signature;
    int classReaderOffset;
    int classReaderLength;
    int exceptionCount;
    int[] exceptions;
    private ByteVector annd;
    private AnnotationWriter anns;
    private AnnotationWriter ianns;
    private AnnotationWriter[] panns;
    private AnnotationWriter[] ipanns;
    private int synthetics;
    private Attribute attrs;
    private ByteVector code = new ByteVector();
    private int maxStack;
    private int maxLocals;
    private int frameCount;
    private ByteVector stackMap;
    private int previousFrameOffset;
    private int[] previousFrame;
    private int frameIndex;
    private int[] frame;
    private int handlerCount;
    private Handler firstHandler;
    private Handler lastHandler;
    private int localVarCount;
    private ByteVector localVar;
    private int localVarTypeCount;
    private ByteVector localVarType;
    private int lineNumberCount;
    private ByteVector lineNumber;
    private Attribute cattrs;
    private boolean resize;
    private int subroutines;
    private final int compute;
    private Label labels;
    private Label previousBlock;
    private Label currentBlock;
    private int stackSize;
    private int maxStackSize;

    MethodWriter(ClassWriter cw, int access, String name, String desc, String signature, String[] exceptions, boolean computeMaxs, boolean computeFrames) {
        if (cw.firstMethod == null) {
            cw.firstMethod = this;
        } else {
            cw.lastMethod.next = this;
        }
        cw.lastMethod = this;
        this.cw = cw;
        this.access = access;
        this.name = cw.newUTF8(name);
        this.desc = cw.newUTF8(desc);
        this.descriptor = desc;
        this.signature = signature;
        if (exceptions != null && exceptions.length > 0) {
            this.exceptionCount = exceptions.length;
            this.exceptions = new int[this.exceptionCount];
            for (int i2 = 0; i2 < this.exceptionCount; i2++) {
                this.exceptions[i2] = cw.newClass(exceptions[i2]);
            }
        }
        this.compute = computeFrames ? 0 : computeMaxs ? 1 : 2;
        if (computeMaxs || computeFrames) {
            if (computeFrames && Constants.CONSTRUCTOR_NAME.equals(name)) {
                this.access |= 262144;
            }
            int size = getArgumentsAndReturnSizes(this.descriptor) >> 2;
            this.maxLocals = (access & 8) != 0 ? size - 1 : size;
            this.labels = new Label();
            this.labels.status |= 8;
            visitLabel(this.labels);
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        this.annd = new ByteVector();
        return new AnnotationWriter(this.cw, false, this.annd, null, 0);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        ByteVector bv2 = new ByteVector();
        bv2.putShort(this.cw.newUTF8(desc)).putShort(0);
        AnnotationWriter aw2 = new AnnotationWriter(this.cw, true, bv2, bv2, 2);
        if (visible) {
            aw2.next = this.anns;
            this.anns = aw2;
        } else {
            aw2.next = this.ianns;
            this.ianns = aw2;
        }
        return aw2;
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        ByteVector bv2 = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(desc)) {
            this.synthetics = Math.max(this.synthetics, parameter + 1);
            return new AnnotationWriter(this.cw, false, bv2, null, 0);
        }
        bv2.putShort(this.cw.newUTF8(desc)).putShort(0);
        AnnotationWriter aw2 = new AnnotationWriter(this.cw, true, bv2, bv2, 2);
        if (visible) {
            if (this.panns == null) {
                this.panns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            aw2.next = this.panns[parameter];
            this.panns[parameter] = aw2;
        } else {
            if (this.ipanns == null) {
                this.ipanns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            aw2.next = this.ipanns[parameter];
            this.ipanns[parameter] = aw2;
        }
        return aw2;
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attr) {
        if (attr.isCodeAttribute()) {
            attr.next = this.cattrs;
            this.cattrs = attr;
        } else {
            attr.next = this.attrs;
            this.attrs = attr;
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitCode() {
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        int delta;
        if (this.compute == 0) {
            return;
        }
        if (type == -1) {
            startFrame(this.code.length, nLocal, nStack);
            for (int i2 = 0; i2 < nLocal; i2++) {
                if (local[i2] instanceof String) {
                    int[] iArr = this.frame;
                    int i3 = this.frameIndex;
                    this.frameIndex = i3 + 1;
                    iArr[i3] = 24117248 | this.cw.addType((String) local[i2]);
                } else if (local[i2] instanceof Integer) {
                    int[] iArr2 = this.frame;
                    int i4 = this.frameIndex;
                    this.frameIndex = i4 + 1;
                    iArr2[i4] = ((Integer) local[i2]).intValue();
                } else {
                    int[] iArr3 = this.frame;
                    int i5 = this.frameIndex;
                    this.frameIndex = i5 + 1;
                    iArr3[i5] = 25165824 | this.cw.addUninitializedType("", ((Label) local[i2]).position);
                }
            }
            for (int i6 = 0; i6 < nStack; i6++) {
                if (stack[i6] instanceof String) {
                    int[] iArr4 = this.frame;
                    int i7 = this.frameIndex;
                    this.frameIndex = i7 + 1;
                    iArr4[i7] = 24117248 | this.cw.addType((String) stack[i6]);
                } else if (stack[i6] instanceof Integer) {
                    int[] iArr5 = this.frame;
                    int i8 = this.frameIndex;
                    this.frameIndex = i8 + 1;
                    iArr5[i8] = ((Integer) stack[i6]).intValue();
                } else {
                    int[] iArr6 = this.frame;
                    int i9 = this.frameIndex;
                    this.frameIndex = i9 + 1;
                    iArr6[i9] = 25165824 | this.cw.addUninitializedType("", ((Label) stack[i6]).position);
                }
            }
            endFrame();
            return;
        }
        if (this.stackMap == null) {
            this.stackMap = new ByteVector();
            delta = this.code.length;
        } else {
            delta = (this.code.length - this.previousFrameOffset) - 1;
        }
        switch (type) {
            case 0:
                this.stackMap.putByte(255).putShort(delta).putShort(nLocal);
                for (int i10 = 0; i10 < nLocal; i10++) {
                    writeFrameType(local[i10]);
                }
                this.stackMap.putShort(nStack);
                for (int i11 = 0; i11 < nStack; i11++) {
                    writeFrameType(stack[i11]);
                }
                break;
            case 1:
                this.stackMap.putByte(251 + nLocal).putShort(delta);
                for (int i12 = 0; i12 < nLocal; i12++) {
                    writeFrameType(local[i12]);
                }
                break;
            case 2:
                this.stackMap.putByte(251 - nLocal).putShort(delta);
                break;
            case 3:
                if (delta < 64) {
                    this.stackMap.putByte(delta);
                    break;
                } else {
                    this.stackMap.putByte(251).putShort(delta);
                    break;
                }
            case 4:
                if (delta < 64) {
                    this.stackMap.putByte(64 + delta);
                } else {
                    this.stackMap.putByte(247).putShort(delta);
                }
                writeFrameType(stack[0]);
                break;
        }
        this.previousFrameOffset = this.code.length;
        this.frameCount++;
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitInsn(int opcode) {
        this.code.putByte(opcode);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, 0, null, null);
            } else {
                int size = this.stackSize + Frame.SIZE[opcode];
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
            if ((opcode >= 172 && opcode <= 177) || opcode == 191) {
                noSuccessor();
            }
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int opcode, int operand) {
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, operand, null, null);
            } else if (opcode != 188) {
                int size = this.stackSize + 1;
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        if (opcode == 17) {
            this.code.put12(opcode, operand);
        } else {
            this.code.put11(opcode, operand);
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int opcode, int var) {
        int opt;
        int n2;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, var, null, null);
            } else if (opcode == 169) {
                this.currentBlock.status |= 256;
                this.currentBlock.inputStackTop = this.stackSize;
                noSuccessor();
            } else {
                int size = this.stackSize + Frame.SIZE[opcode];
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        if (this.compute != 2) {
            if (opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57) {
                n2 = var + 2;
            } else {
                n2 = var + 1;
            }
            if (n2 > this.maxLocals) {
                this.maxLocals = n2;
            }
        }
        if (var < 4 && opcode != 169) {
            if (opcode < 54) {
                opt = 26 + ((opcode - 21) << 2) + var;
            } else {
                opt = 59 + ((opcode - 54) << 2) + var;
            }
            this.code.putByte(opt);
        } else if (var >= 256) {
            this.code.putByte(196).put12(opcode, var);
        } else {
            this.code.put11(opcode, var);
        }
        if (opcode >= 54 && this.compute == 0 && this.handlerCount > 0) {
            visitLabel(new Label());
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int opcode, String type) {
        Item i2 = this.cw.newClassItem(type);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, this.code.length, this.cw, i2);
            } else if (opcode == 187) {
                int size = this.stackSize + 1;
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        this.code.put12(opcode, i2.index);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        int size;
        Item i2 = this.cw.newFieldItem(owner, name, desc);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, 0, this.cw, i2);
            } else {
                char c2 = desc.charAt(0);
                switch (opcode) {
                    case 178:
                        size = this.stackSize + ((c2 == 'D' || c2 == 'J') ? 2 : 1);
                        break;
                    case 179:
                        size = this.stackSize + ((c2 == 'D' || c2 == 'J') ? -2 : -1);
                        break;
                    case 180:
                        size = this.stackSize + ((c2 == 'D' || c2 == 'J') ? 1 : 0);
                        break;
                    default:
                        size = this.stackSize + ((c2 == 'D' || c2 == 'J') ? -3 : -2);
                        break;
                }
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        this.code.put12(opcode, i2.index);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        int size;
        boolean itf = opcode == 185;
        Item i2 = this.cw.newMethodItem(owner, name, desc, itf);
        int argSize = i2.intVal;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, 0, this.cw, i2);
            } else {
                if (argSize == 0) {
                    argSize = getArgumentsAndReturnSizes(desc);
                    i2.intVal = argSize;
                }
                if (opcode == 184) {
                    size = (this.stackSize - (argSize >> 2)) + (argSize & 3) + 1;
                } else {
                    size = (this.stackSize - (argSize >> 2)) + (argSize & 3);
                }
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        if (itf) {
            if (argSize == 0) {
                argSize = getArgumentsAndReturnSizes(desc);
                i2.intVal = argSize;
            }
            this.code.put12(185, i2.index).put11(argSize >> 2, 0);
            return;
        }
        this.code.put12(opcode, i2.index);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int opcode, Label label) {
        Label nextInsn = null;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(opcode, 0, null, null);
                label.getFirst().status |= 16;
                addSuccessor(0, label);
                if (opcode != 167) {
                    nextInsn = new Label();
                }
            } else if (opcode == 168) {
                if ((label.status & 512) == 0) {
                    label.status |= 512;
                    this.subroutines++;
                }
                this.currentBlock.status |= 128;
                addSuccessor(this.stackSize + 1, label);
                nextInsn = new Label();
            } else {
                this.stackSize += Frame.SIZE[opcode];
                addSuccessor(this.stackSize, label);
            }
        }
        if ((label.status & 2) != 0 && label.position - this.code.length < -32768) {
            if (opcode == 167) {
                this.code.putByte(200);
            } else if (opcode == 168) {
                this.code.putByte(201);
            } else {
                if (nextInsn != null) {
                    nextInsn.status |= 16;
                }
                this.code.putByte(opcode <= 166 ? ((opcode + 1) ^ 1) - 1 : opcode ^ 1);
                this.code.putShort(8);
                this.code.putByte(200);
            }
            label.put(this, this.code, this.code.length - 1, true);
        } else {
            this.code.putByte(opcode);
            label.put(this, this.code, this.code.length - 1, false);
        }
        if (this.currentBlock != null) {
            if (nextInsn != null) {
                visitLabel(nextInsn);
            }
            if (opcode == 167) {
                noSuccessor();
            }
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        this.resize |= label.resolve(this, this.code.length, this.code.data);
        if ((label.status & 1) != 0) {
            return;
        }
        if (this.compute == 0) {
            if (this.currentBlock != null) {
                if (label.position == this.currentBlock.position) {
                    this.currentBlock.status |= label.status & 16;
                    label.frame = this.currentBlock.frame;
                    return;
                }
                addSuccessor(0, label);
            }
            this.currentBlock = label;
            if (label.frame == null) {
                label.frame = new Frame();
                label.frame.owner = label;
            }
            if (this.previousBlock != null) {
                if (label.position == this.previousBlock.position) {
                    this.previousBlock.status |= label.status & 16;
                    label.frame = this.previousBlock.frame;
                    this.currentBlock = this.previousBlock;
                    return;
                }
                this.previousBlock.successor = label;
            }
            this.previousBlock = label;
            return;
        }
        if (this.compute == 1) {
            if (this.currentBlock != null) {
                this.currentBlock.outputStackMax = this.maxStackSize;
                addSuccessor(this.stackSize, label);
            }
            this.currentBlock = label;
            this.stackSize = 0;
            this.maxStackSize = 0;
            if (this.previousBlock != null) {
                this.previousBlock.successor = label;
            }
            this.previousBlock = label;
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object cst) {
        int size;
        Item i2 = this.cw.newConstItem(cst);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(18, 0, this.cw, i2);
            } else {
                if (i2.type == 5 || i2.type == 6) {
                    size = this.stackSize + 2;
                } else {
                    size = this.stackSize + 1;
                }
                if (size > this.maxStackSize) {
                    this.maxStackSize = size;
                }
                this.stackSize = size;
            }
        }
        int index = i2.index;
        if (i2.type == 5 || i2.type == 6) {
            this.code.put12(20, index);
        } else if (index >= 256) {
            this.code.put12(19, index);
        } else {
            this.code.put11(18, index);
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int var, int increment) {
        int n2;
        if (this.currentBlock != null && this.compute == 0) {
            this.currentBlock.frame.execute(132, var, null, null);
        }
        if (this.compute != 2 && (n2 = var + 1) > this.maxLocals) {
            this.maxLocals = n2;
        }
        if (var > 255 || increment > 127 || increment < -128) {
            this.code.putByte(196).put12(132, var).putShort(increment);
        } else {
            this.code.putByte(132).put11(var, increment);
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        int source = this.code.length;
        this.code.putByte(170);
        this.code.length += (4 - (this.code.length % 4)) % 4;
        dflt.put(this, this.code, source, true);
        this.code.putInt(min).putInt(max);
        for (Label label : labels) {
            label.put(this, this.code, source, true);
        }
        visitSwitchInsn(dflt, labels);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        int source = this.code.length;
        this.code.putByte(171);
        this.code.length += (4 - (this.code.length % 4)) % 4;
        dflt.put(this, this.code, source, true);
        this.code.putInt(labels.length);
        for (int i2 = 0; i2 < labels.length; i2++) {
            this.code.putInt(keys[i2]);
            labels[i2].put(this, this.code, source, true);
        }
        visitSwitchInsn(dflt, labels);
    }

    private void visitSwitchInsn(Label dflt, Label[] labels) {
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(171, 0, null, null);
                addSuccessor(0, dflt);
                dflt.getFirst().status |= 16;
                for (int i2 = 0; i2 < labels.length; i2++) {
                    addSuccessor(0, labels[i2]);
                    labels[i2].getFirst().status |= 16;
                }
            } else {
                this.stackSize--;
                addSuccessor(this.stackSize, dflt);
                for (Label label : labels) {
                    addSuccessor(this.stackSize, label);
                }
            }
            noSuccessor();
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String desc, int dims) {
        Item i2 = this.cw.newClassItem(desc);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(197, dims, this.cw, i2);
            } else {
                this.stackSize += 1 - dims;
            }
        }
        this.code.put12(197, i2.index).putByte(dims);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.handlerCount++;
        Handler h2 = new Handler();
        h2.start = start;
        h2.end = end;
        h2.handler = handler;
        h2.desc = type;
        h2.type = type != null ? this.cw.newClass(type) : 0;
        if (this.lastHandler == null) {
            this.firstHandler = h2;
        } else {
            this.lastHandler.next = h2;
        }
        this.lastHandler = h2;
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        if (signature != null) {
            if (this.localVarType == null) {
                this.localVarType = new ByteVector();
            }
            this.localVarTypeCount++;
            this.localVarType.putShort(start.position).putShort(end.position - start.position).putShort(this.cw.newUTF8(name)).putShort(this.cw.newUTF8(signature)).putShort(index);
        }
        if (this.localVar == null) {
            this.localVar = new ByteVector();
        }
        this.localVarCount++;
        this.localVar.putShort(start.position).putShort(end.position - start.position).putShort(this.cw.newUTF8(name)).putShort(this.cw.newUTF8(desc)).putShort(index);
        if (this.compute != 2) {
            char c2 = desc.charAt(0);
            int n2 = index + ((c2 == 'J' || c2 == 'D') ? 2 : 1);
            if (n2 > this.maxLocals) {
                this.maxLocals = n2;
            }
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int line, Label start) {
        if (this.lineNumber == null) {
            this.lineNumber = new ByteVector();
        }
        this.lineNumberCount++;
        this.lineNumber.putShort(start.position);
        this.lineNumber.putShort(line);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int maxStack, int maxLocals) {
        if (this.compute != 0) {
            if (this.compute == 1) {
                Handler handler = this.firstHandler;
                while (true) {
                    Handler handler2 = handler;
                    if (handler2 == null) {
                        break;
                    }
                    Label h2 = handler2.handler;
                    Label e2 = handler2.end;
                    for (Label l2 = handler2.start; l2 != e2; l2 = l2.successor) {
                        Edge b2 = new Edge();
                        b2.info = Integer.MAX_VALUE;
                        b2.successor = h2;
                        if ((l2.status & 128) == 0) {
                            b2.next = l2.successors;
                            l2.successors = b2;
                        } else {
                            b2.next = l2.successors.next.next;
                            l2.successors.next.next = b2;
                        }
                    }
                    handler = handler2.next;
                }
                if (this.subroutines > 0) {
                    int id = 0;
                    this.labels.visitSubroutine(null, 1L, this.subroutines);
                    Label label = this.labels;
                    while (true) {
                        Label l3 = label;
                        if (l3 == null) {
                            break;
                        }
                        if ((l3.status & 128) != 0) {
                            Label subroutine = l3.successors.next.successor;
                            if ((subroutine.status & 1024) == 0) {
                                id++;
                                subroutine.visitSubroutine(null, ((id / 32) << 32) | (1 << (id % 32)), this.subroutines);
                            }
                        }
                        label = l3.successor;
                    }
                    Label label2 = this.labels;
                    while (true) {
                        Label l4 = label2;
                        if (l4 == null) {
                            break;
                        }
                        if ((l4.status & 128) != 0) {
                            Label label3 = this.labels;
                            while (true) {
                                Label L2 = label3;
                                if (L2 == null) {
                                    break;
                                }
                                L2.status &= -1025;
                                label3 = L2.successor;
                            }
                            l4.successors.next.successor.visitSubroutine(l4, 0L, this.subroutines);
                        }
                        label2 = l4.successor;
                    }
                }
                int max = 0;
                Label stack = this.labels;
                while (stack != null) {
                    Label l5 = stack;
                    stack = stack.next;
                    int start = l5.inputStackTop;
                    int blockMax = start + l5.outputStackMax;
                    if (blockMax > max) {
                        max = blockMax;
                    }
                    Edge b3 = l5.successors;
                    if ((l5.status & 128) != 0) {
                        b3 = b3.next;
                    }
                    while (b3 != null) {
                        Label l6 = b3.successor;
                        if ((l6.status & 8) == 0) {
                            l6.inputStackTop = b3.info == Integer.MAX_VALUE ? 1 : start + b3.info;
                            l6.status |= 8;
                            l6.next = stack;
                            stack = l6;
                        }
                        b3 = b3.next;
                    }
                }
                this.maxStack = max;
                return;
            }
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
            return;
        }
        Handler handler3 = this.firstHandler;
        while (true) {
            Handler handler4 = handler3;
            if (handler4 == null) {
                break;
            }
            Label h3 = handler4.handler.getFirst();
            Label e3 = handler4.end.getFirst();
            String t2 = handler4.desc == null ? "java/lang/Throwable" : handler4.desc;
            int kind = 24117248 | this.cw.addType(t2);
            h3.status |= 16;
            for (Label l7 = handler4.start.getFirst(); l7 != e3; l7 = l7.successor) {
                Edge b4 = new Edge();
                b4.info = kind;
                b4.successor = h3;
                b4.next = l7.successors;
                l7.successors = b4;
            }
            handler3 = handler4.next;
        }
        Frame f2 = this.labels.frame;
        Type[] args = Type.getArgumentTypes(this.descriptor);
        f2.initInputFrame(this.cw, this.access, args, this.maxLocals);
        visitFrame(f2);
        int max2 = 0;
        Label changed = this.labels;
        while (changed != null) {
            Label l8 = changed;
            changed = changed.next;
            l8.next = null;
            Frame f3 = l8.frame;
            if ((l8.status & 16) != 0) {
                l8.status |= 32;
            }
            l8.status |= 64;
            int blockMax2 = f3.inputStack.length + l8.outputStackMax;
            if (blockMax2 > max2) {
                max2 = blockMax2;
            }
            Edge edge = l8.successors;
            while (true) {
                Edge e4 = edge;
                if (e4 != null) {
                    Label n2 = e4.successor.getFirst();
                    boolean change = f3.merge(this.cw, n2.frame, e4.info);
                    if (change && n2.next == null) {
                        n2.next = changed;
                        changed = n2;
                    }
                    edge = e4.next;
                }
            }
        }
        this.maxStack = max2;
        Label label4 = this.labels;
        while (true) {
            Label l9 = label4;
            if (l9 != null) {
                Frame f4 = l9.frame;
                if ((l9.status & 32) != 0) {
                    visitFrame(f4);
                }
                if ((l9.status & 64) == 0) {
                    Label k2 = l9.successor;
                    int start2 = l9.position;
                    int end = (k2 == null ? this.code.length : k2.position) - 1;
                    if (end >= start2) {
                        for (int i2 = start2; i2 < end; i2++) {
                            this.code.data[i2] = 0;
                        }
                        this.code.data[end] = -65;
                        startFrame(start2, 0, 1);
                        int[] iArr = this.frame;
                        int i3 = this.frameIndex;
                        this.frameIndex = i3 + 1;
                        iArr[i3] = 24117248 | this.cw.addType("java/lang/Throwable");
                        endFrame();
                    }
                }
                label4 = l9.successor;
            } else {
                return;
            }
        }
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
    }

    static int getArgumentsAndReturnSizes(String desc) {
        int i2;
        char car;
        int n2 = 1;
        int c2 = 1;
        while (true) {
            int i3 = c2;
            c2++;
            char car2 = desc.charAt(i3);
            if (car2 == ')') {
                break;
            }
            if (car2 == 'L') {
                do {
                    i2 = c2;
                    c2++;
                } while (desc.charAt(i2) != ';');
                n2++;
            } else if (car2 == '[') {
                while (true) {
                    car = desc.charAt(c2);
                    if (car != '[') {
                        break;
                    }
                    c2++;
                }
                if (car == 'D' || car == 'J') {
                    n2--;
                }
            } else if (car2 == 'D' || car2 == 'J') {
                n2 += 2;
            } else {
                n2++;
            }
        }
        char car3 = desc.charAt(c2);
        return (n2 << 2) | (car3 == 'V' ? 0 : (car3 == 'D' || car3 == 'J') ? 2 : 1);
    }

    private void addSuccessor(int info, Label successor) {
        Edge b2 = new Edge();
        b2.info = info;
        b2.successor = successor;
        b2.next = this.currentBlock.successors;
        this.currentBlock.successors = b2;
    }

    private void noSuccessor() {
        if (this.compute == 0) {
            Label l2 = new Label();
            l2.frame = new Frame();
            l2.frame.owner = l2;
            l2.resolve(this, this.code.length, this.code.data);
            this.previousBlock.successor = l2;
            this.previousBlock = l2;
        } else {
            this.currentBlock.outputStackMax = this.maxStackSize;
        }
        this.currentBlock = null;
    }

    private void visitFrame(Frame f2) {
        int nTop = 0;
        int nLocal = 0;
        int nStack = 0;
        int[] locals = f2.inputLocals;
        int[] stacks = f2.inputStack;
        int i2 = 0;
        while (i2 < locals.length) {
            int t2 = locals[i2];
            if (t2 == 16777216) {
                nTop++;
            } else {
                nLocal += nTop + 1;
                nTop = 0;
            }
            if (t2 == 16777220 || t2 == 16777219) {
                i2++;
            }
            i2++;
        }
        int i3 = 0;
        while (i3 < stacks.length) {
            int t3 = stacks[i3];
            nStack++;
            if (t3 == 16777220 || t3 == 16777219) {
                i3++;
            }
            i3++;
        }
        startFrame(f2.owner.position, nLocal, nStack);
        int i4 = 0;
        while (nLocal > 0) {
            int t4 = locals[i4];
            int[] iArr = this.frame;
            int i5 = this.frameIndex;
            this.frameIndex = i5 + 1;
            iArr[i5] = t4;
            if (t4 == 16777220 || t4 == 16777219) {
                i4++;
            }
            i4++;
            nLocal--;
        }
        int i6 = 0;
        while (i6 < stacks.length) {
            int t5 = stacks[i6];
            int[] iArr2 = this.frame;
            int i7 = this.frameIndex;
            this.frameIndex = i7 + 1;
            iArr2[i7] = t5;
            if (t5 == 16777220 || t5 == 16777219) {
                i6++;
            }
            i6++;
        }
        endFrame();
    }

    private void startFrame(int offset, int nLocal, int nStack) {
        int n2 = 3 + nLocal + nStack;
        if (this.frame == null || this.frame.length < n2) {
            this.frame = new int[n2];
        }
        this.frame[0] = offset;
        this.frame[1] = nLocal;
        this.frame[2] = nStack;
        this.frameIndex = 3;
    }

    private void endFrame() {
        if (this.previousFrame != null) {
            if (this.stackMap == null) {
                this.stackMap = new ByteVector();
            }
            writeFrame();
            this.frameCount++;
        }
        this.previousFrame = this.frame;
        this.frame = null;
    }

    private void writeFrame() {
        int delta;
        int clocalsSize = this.frame[1];
        int cstackSize = this.frame[2];
        if ((this.cw.version & 65535) < 50) {
            this.stackMap.putShort(this.frame[0]).putShort(clocalsSize);
            writeFrameTypes(3, 3 + clocalsSize);
            this.stackMap.putShort(cstackSize);
            writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
        }
        int localsSize = this.previousFrame[1];
        int type = 255;
        int k2 = 0;
        if (this.frameCount == 0) {
            delta = this.frame[0];
        } else {
            delta = (this.frame[0] - this.previousFrame[0]) - 1;
        }
        if (cstackSize == 0) {
            k2 = clocalsSize - localsSize;
            switch (k2) {
                case -3:
                case -2:
                case -1:
                    type = 248;
                    localsSize = clocalsSize;
                    break;
                case 0:
                    type = delta < 64 ? 0 : 251;
                    break;
                case 1:
                case 2:
                case 3:
                    type = 252;
                    break;
            }
        } else if (clocalsSize == localsSize && cstackSize == 1) {
            type = delta < 63 ? 64 : 247;
        }
        if (type != 255) {
            int l2 = 3;
            int j2 = 0;
            while (true) {
                if (j2 < localsSize) {
                    if (this.frame[l2] != this.previousFrame[l2]) {
                        type = 255;
                    } else {
                        l2++;
                        j2++;
                    }
                }
            }
        }
        switch (type) {
            case 0:
                this.stackMap.putByte(delta);
                break;
            case 64:
                this.stackMap.putByte(64 + delta);
                writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
                break;
            case 247:
                this.stackMap.putByte(247).putShort(delta);
                writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
                break;
            case 248:
                this.stackMap.putByte(251 + k2).putShort(delta);
                break;
            case 251:
                this.stackMap.putByte(251).putShort(delta);
                break;
            case 252:
                this.stackMap.putByte(251 + k2).putShort(delta);
                writeFrameTypes(3 + localsSize, 3 + clocalsSize);
                break;
            default:
                this.stackMap.putByte(255).putShort(delta).putShort(clocalsSize);
                writeFrameTypes(3, 3 + clocalsSize);
                this.stackMap.putShort(cstackSize);
                writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
                break;
        }
    }

    private void writeFrameTypes(int start, int end) {
        for (int i2 = start; i2 < end; i2++) {
            int t2 = this.frame[i2];
            int d2 = t2 & (-268435456);
            if (d2 == 0) {
                int v2 = t2 & 1048575;
                switch (t2 & 267386880) {
                    case 24117248:
                        this.stackMap.putByte(7).putShort(this.cw.newClass(this.cw.typeTable[v2].strVal1));
                        break;
                    case 25165824:
                        this.stackMap.putByte(8).putShort(this.cw.typeTable[v2].intVal);
                        break;
                    default:
                        this.stackMap.putByte(v2);
                        break;
                }
            } else {
                StringBuffer buf = new StringBuffer();
                int d3 = d2 >> 28;
                while (true) {
                    int i3 = d3;
                    d3--;
                    if (i3 > 0) {
                        buf.append('[');
                    } else {
                        if ((t2 & 267386880) == 24117248) {
                            buf.append('L');
                            buf.append(this.cw.typeTable[t2 & 1048575].strVal1);
                            buf.append(';');
                        } else {
                            switch (t2 & 15) {
                                case 1:
                                    buf.append('I');
                                    break;
                                case 2:
                                    buf.append('F');
                                    break;
                                case 3:
                                    buf.append('D');
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                default:
                                    buf.append('J');
                                    break;
                                case 9:
                                    buf.append('Z');
                                    break;
                                case 10:
                                    buf.append('B');
                                    break;
                                case 11:
                                    buf.append('C');
                                    break;
                                case 12:
                                    buf.append('S');
                                    break;
                            }
                        }
                        this.stackMap.putByte(7).putShort(this.cw.newClass(buf.toString()));
                    }
                }
            }
        }
    }

    private void writeFrameType(Object type) {
        if (type instanceof String) {
            this.stackMap.putByte(7).putShort(this.cw.newClass((String) type));
        } else if (type instanceof Integer) {
            this.stackMap.putByte(((Integer) type).intValue());
        } else {
            this.stackMap.putByte(8).putShort(((Label) type).position);
        }
    }

    final int getSize() {
        if (this.classReaderOffset != 0) {
            return 6 + this.classReaderLength;
        }
        if (this.resize) {
            resizeInstructions();
        }
        int size = 8;
        if (this.code.length > 0) {
            this.cw.newUTF8("Code");
            size = 8 + 18 + this.code.length + (8 * this.handlerCount);
            if (this.localVar != null) {
                this.cw.newUTF8("LocalVariableTable");
                size += 8 + this.localVar.length;
            }
            if (this.localVarType != null) {
                this.cw.newUTF8("LocalVariableTypeTable");
                size += 8 + this.localVarType.length;
            }
            if (this.lineNumber != null) {
                this.cw.newUTF8("LineNumberTable");
                size += 8 + this.lineNumber.length;
            }
            if (this.stackMap != null) {
                boolean zip = (this.cw.version & 65535) >= 50;
                this.cw.newUTF8(zip ? "StackMapTable" : "StackMap");
                size += 8 + this.stackMap.length;
            }
            if (this.cattrs != null) {
                size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
        }
        if (this.exceptionCount > 0) {
            this.cw.newUTF8("Exceptions");
            size += 8 + (2 * this.exceptionCount);
        }
        if ((this.access & 4096) != 0 && (this.cw.version & 65535) < 49) {
            this.cw.newUTF8("Synthetic");
            size += 6;
        }
        if ((this.access & 131072) != 0) {
            this.cw.newUTF8("Deprecated");
            size += 6;
        }
        if (this.signature != null) {
            this.cw.newUTF8(com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE);
            this.cw.newUTF8(this.signature);
            size += 8;
        }
        if (this.annd != null) {
            this.cw.newUTF8("AnnotationDefault");
            size += 6 + this.annd.length;
        }
        if (this.anns != null) {
            this.cw.newUTF8("RuntimeVisibleAnnotations");
            size += 8 + this.anns.getSize();
        }
        if (this.ianns != null) {
            this.cw.newUTF8("RuntimeInvisibleAnnotations");
            size += 8 + this.ianns.getSize();
        }
        if (this.panns != null) {
            this.cw.newUTF8("RuntimeVisibleParameterAnnotations");
            size += 7 + (2 * (this.panns.length - this.synthetics));
            for (int i2 = this.panns.length - 1; i2 >= this.synthetics; i2--) {
                size += this.panns[i2] == null ? 0 : this.panns[i2].getSize();
            }
        }
        if (this.ipanns != null) {
            this.cw.newUTF8("RuntimeInvisibleParameterAnnotations");
            size += 7 + (2 * (this.ipanns.length - this.synthetics));
            for (int i3 = this.ipanns.length - 1; i3 >= this.synthetics; i3--) {
                size += this.ipanns[i3] == null ? 0 : this.ipanns[i3].getSize();
            }
        }
        if (this.attrs != null) {
            size += this.attrs.getSize(this.cw, null, 0, -1, -1);
        }
        return size;
    }

    final void put(ByteVector out) {
        out.putShort(this.access).putShort(this.name).putShort(this.desc);
        if (this.classReaderOffset != 0) {
            out.putByteArray(this.cw.cr.f12090b, this.classReaderOffset, this.classReaderLength);
            return;
        }
        int attributeCount = 0;
        if (this.code.length > 0) {
            attributeCount = 0 + 1;
        }
        if (this.exceptionCount > 0) {
            attributeCount++;
        }
        if ((this.access & 4096) != 0 && (this.cw.version & 65535) < 49) {
            attributeCount++;
        }
        if ((this.access & 131072) != 0) {
            attributeCount++;
        }
        if (this.signature != null) {
            attributeCount++;
        }
        if (this.annd != null) {
            attributeCount++;
        }
        if (this.anns != null) {
            attributeCount++;
        }
        if (this.ianns != null) {
            attributeCount++;
        }
        if (this.panns != null) {
            attributeCount++;
        }
        if (this.ipanns != null) {
            attributeCount++;
        }
        if (this.attrs != null) {
            attributeCount += this.attrs.getCount();
        }
        out.putShort(attributeCount);
        if (this.code.length > 0) {
            int size = 12 + this.code.length + (8 * this.handlerCount);
            if (this.localVar != null) {
                size += 8 + this.localVar.length;
            }
            if (this.localVarType != null) {
                size += 8 + this.localVarType.length;
            }
            if (this.lineNumber != null) {
                size += 8 + this.lineNumber.length;
            }
            if (this.stackMap != null) {
                size += 8 + this.stackMap.length;
            }
            if (this.cattrs != null) {
                size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
            out.putShort(this.cw.newUTF8("Code")).putInt(size);
            out.putShort(this.maxStack).putShort(this.maxLocals);
            out.putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
            out.putShort(this.handlerCount);
            if (this.handlerCount > 0) {
                Handler handler = this.firstHandler;
                while (true) {
                    Handler h2 = handler;
                    if (h2 == null) {
                        break;
                    }
                    out.putShort(h2.start.position).putShort(h2.end.position).putShort(h2.handler.position).putShort(h2.type);
                    handler = h2.next;
                }
            }
            int attributeCount2 = 0;
            if (this.localVar != null) {
                attributeCount2 = 0 + 1;
            }
            if (this.localVarType != null) {
                attributeCount2++;
            }
            if (this.lineNumber != null) {
                attributeCount2++;
            }
            if (this.stackMap != null) {
                attributeCount2++;
            }
            if (this.cattrs != null) {
                attributeCount2 += this.cattrs.getCount();
            }
            out.putShort(attributeCount2);
            if (this.localVar != null) {
                out.putShort(this.cw.newUTF8("LocalVariableTable"));
                out.putInt(this.localVar.length + 2).putShort(this.localVarCount);
                out.putByteArray(this.localVar.data, 0, this.localVar.length);
            }
            if (this.localVarType != null) {
                out.putShort(this.cw.newUTF8("LocalVariableTypeTable"));
                out.putInt(this.localVarType.length + 2).putShort(this.localVarTypeCount);
                out.putByteArray(this.localVarType.data, 0, this.localVarType.length);
            }
            if (this.lineNumber != null) {
                out.putShort(this.cw.newUTF8("LineNumberTable"));
                out.putInt(this.lineNumber.length + 2).putShort(this.lineNumberCount);
                out.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
            }
            if (this.stackMap != null) {
                boolean zip = (this.cw.version & 65535) >= 50;
                out.putShort(this.cw.newUTF8(zip ? "StackMapTable" : "StackMap"));
                out.putInt(this.stackMap.length + 2).putShort(this.frameCount);
                out.putByteArray(this.stackMap.data, 0, this.stackMap.length);
            }
            if (this.cattrs != null) {
                this.cattrs.put(this.cw, this.code.data, this.code.length, this.maxLocals, this.maxStack, out);
            }
        }
        if (this.exceptionCount > 0) {
            out.putShort(this.cw.newUTF8("Exceptions")).putInt((2 * this.exceptionCount) + 2);
            out.putShort(this.exceptionCount);
            for (int i2 = 0; i2 < this.exceptionCount; i2++) {
                out.putShort(this.exceptions[i2]);
            }
        }
        if ((this.access & 4096) != 0 && (this.cw.version & 65535) < 49) {
            out.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.access & 131072) != 0) {
            out.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
        }
        if (this.signature != null) {
            out.putShort(this.cw.newUTF8(com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE)).putInt(2).putShort(this.cw.newUTF8(this.signature));
        }
        if (this.annd != null) {
            out.putShort(this.cw.newUTF8("AnnotationDefault"));
            out.putInt(this.annd.length);
            out.putByteArray(this.annd.data, 0, this.annd.length);
        }
        if (this.anns != null) {
            out.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(out);
        }
        if (this.ianns != null) {
            out.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(out);
        }
        if (this.panns != null) {
            out.putShort(this.cw.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.put(this.panns, this.synthetics, out);
        }
        if (this.ipanns != null) {
            out.putShort(this.cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.put(this.ipanns, this.synthetics, out);
        }
        if (this.attrs != null) {
            this.attrs.put(this.cw, null, 0, -1, -1, out);
        }
    }

    private void resizeInstructions() {
        int label;
        int label2;
        byte[] b2 = this.code.data;
        int[] allIndexes = new int[0];
        int[] allSizes = new int[0];
        boolean[] resize = new boolean[this.code.length];
        int state = 3;
        do {
            if (state == 3) {
                state = 2;
            }
            int u2 = 0;
            while (u2 < b2.length) {
                int opcode = b2[u2] & 255;
                int insert = 0;
                switch (ClassWriter.TYPE[opcode]) {
                    case 0:
                    case 4:
                        u2++;
                        break;
                    case 1:
                    case 3:
                    case 10:
                        u2 += 2;
                        break;
                    case 2:
                    case 5:
                    case 6:
                    case 11:
                    case 12:
                        u2 += 3;
                        break;
                    case 7:
                        u2 += 5;
                        break;
                    case 8:
                        if (opcode > 201) {
                            opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                            label2 = u2 + readUnsignedShort(b2, u2 + 1);
                        } else {
                            label2 = u2 + readShort(b2, u2 + 1);
                        }
                        int newOffset = getNewOffset(allIndexes, allSizes, u2, label2);
                        if ((newOffset < -32768 || newOffset > 32767) && !resize[u2]) {
                            if (opcode == 167 || opcode == 168) {
                                insert = 2;
                            } else {
                                insert = 5;
                            }
                            resize[u2] = true;
                        }
                        u2 += 3;
                        break;
                    case 9:
                        u2 += 5;
                        break;
                    case 13:
                        if (state == 1) {
                            insert = -(getNewOffset(allIndexes, allSizes, 0, u2) & 3);
                        } else if (!resize[u2]) {
                            insert = u2 & 3;
                            resize[u2] = true;
                        }
                        int u3 = (u2 + 4) - (u2 & 3);
                        u2 = u3 + (4 * ((readInt(b2, u3 + 8) - readInt(b2, u3 + 4)) + 1)) + 12;
                        break;
                    case 14:
                        if (state == 1) {
                            insert = -(getNewOffset(allIndexes, allSizes, 0, u2) & 3);
                        } else if (!resize[u2]) {
                            insert = u2 & 3;
                            resize[u2] = true;
                        }
                        int u4 = (u2 + 4) - (u2 & 3);
                        u2 = u4 + (8 * readInt(b2, u4 + 4)) + 8;
                        break;
                    case 15:
                    default:
                        u2 += 4;
                        break;
                    case 16:
                        if ((b2[u2 + 1] & 255) == 132) {
                            u2 += 6;
                            break;
                        } else {
                            u2 += 4;
                            break;
                        }
                }
                if (insert != 0) {
                    int[] newIndexes = new int[allIndexes.length + 1];
                    int[] newSizes = new int[allSizes.length + 1];
                    System.arraycopy(allIndexes, 0, newIndexes, 0, allIndexes.length);
                    System.arraycopy(allSizes, 0, newSizes, 0, allSizes.length);
                    newIndexes[allIndexes.length] = u2;
                    newSizes[allSizes.length] = insert;
                    allIndexes = newIndexes;
                    allSizes = newSizes;
                    if (insert > 0) {
                        state = 3;
                    }
                }
            }
            if (state < 3) {
                state--;
            }
        } while (state != 0);
        ByteVector newCode = new ByteVector(this.code.length);
        int u5 = 0;
        while (u5 < this.code.length) {
            int opcode2 = b2[u5] & 255;
            switch (ClassWriter.TYPE[opcode2]) {
                case 0:
                case 4:
                    newCode.putByte(opcode2);
                    u5++;
                    break;
                case 1:
                case 3:
                case 10:
                    newCode.putByteArray(b2, u5, 2);
                    u5 += 2;
                    break;
                case 2:
                case 5:
                case 6:
                case 11:
                case 12:
                    newCode.putByteArray(b2, u5, 3);
                    u5 += 3;
                    break;
                case 7:
                    newCode.putByteArray(b2, u5, 5);
                    u5 += 5;
                    break;
                case 8:
                    if (opcode2 > 201) {
                        opcode2 = opcode2 < 218 ? opcode2 - 49 : opcode2 - 20;
                        label = u5 + readUnsignedShort(b2, u5 + 1);
                    } else {
                        label = u5 + readShort(b2, u5 + 1);
                    }
                    int newOffset2 = getNewOffset(allIndexes, allSizes, u5, label);
                    if (resize[u5]) {
                        if (opcode2 == 167) {
                            newCode.putByte(200);
                        } else if (opcode2 == 168) {
                            newCode.putByte(201);
                        } else {
                            newCode.putByte(opcode2 <= 166 ? ((opcode2 + 1) ^ 1) - 1 : opcode2 ^ 1);
                            newCode.putShort(8);
                            newCode.putByte(200);
                            newOffset2 -= 3;
                        }
                        newCode.putInt(newOffset2);
                    } else {
                        newCode.putByte(opcode2);
                        newCode.putShort(newOffset2);
                    }
                    u5 += 3;
                    break;
                case 9:
                    int newOffset3 = getNewOffset(allIndexes, allSizes, u5, u5 + readInt(b2, u5 + 1));
                    newCode.putByte(opcode2);
                    newCode.putInt(newOffset3);
                    u5 += 5;
                    break;
                case 13:
                    int v2 = u5;
                    int u6 = (u5 + 4) - (v2 & 3);
                    newCode.putByte(170);
                    newCode.length += (4 - (newCode.length % 4)) % 4;
                    int u7 = u6 + 4;
                    newCode.putInt(getNewOffset(allIndexes, allSizes, v2, v2 + readInt(b2, u6)));
                    int j2 = readInt(b2, u7);
                    int u8 = u7 + 4;
                    newCode.putInt(j2);
                    u5 = u8 + 4;
                    newCode.putInt(readInt(b2, u5 - 4));
                    for (int j3 = (readInt(b2, u8) - j2) + 1; j3 > 0; j3--) {
                        int label3 = v2 + readInt(b2, u5);
                        u5 += 4;
                        newCode.putInt(getNewOffset(allIndexes, allSizes, v2, label3));
                    }
                    break;
                case 14:
                    int v3 = u5;
                    int u9 = (u5 + 4) - (v3 & 3);
                    newCode.putByte(171);
                    newCode.length += (4 - (newCode.length % 4)) % 4;
                    int u10 = u9 + 4;
                    newCode.putInt(getNewOffset(allIndexes, allSizes, v3, v3 + readInt(b2, u9)));
                    int j4 = readInt(b2, u10);
                    u5 = u10 + 4;
                    newCode.putInt(j4);
                    while (j4 > 0) {
                        newCode.putInt(readInt(b2, u5));
                        int u11 = u5 + 4;
                        int label4 = v3 + readInt(b2, u11);
                        u5 = u11 + 4;
                        newCode.putInt(getNewOffset(allIndexes, allSizes, v3, label4));
                        j4--;
                    }
                    break;
                case 15:
                default:
                    newCode.putByteArray(b2, u5, 4);
                    u5 += 4;
                    break;
                case 16:
                    if ((b2[u5 + 1] & 255) == 132) {
                        newCode.putByteArray(b2, u5, 6);
                        u5 += 6;
                        break;
                    } else {
                        newCode.putByteArray(b2, u5, 4);
                        u5 += 4;
                        break;
                    }
            }
        }
        if (this.frameCount > 0) {
            if (this.compute == 0) {
                this.frameCount = 0;
                this.stackMap = null;
                this.previousFrame = null;
                this.frame = null;
                Frame f2 = new Frame();
                f2.owner = this.labels;
                Type[] args = Type.getArgumentTypes(this.descriptor);
                f2.initInputFrame(this.cw, this.access, args, this.maxLocals);
                visitFrame(f2);
                Label label5 = this.labels;
                while (true) {
                    Label l2 = label5;
                    if (l2 != null) {
                        int u12 = l2.position - 3;
                        if ((l2.status & 32) != 0 || (u12 >= 0 && resize[u12])) {
                            getNewOffset(allIndexes, allSizes, l2);
                            visitFrame(l2.frame);
                        }
                        label5 = l2.successor;
                    }
                }
            } else {
                this.cw.invalidFrames = true;
            }
        }
        Handler handler = this.firstHandler;
        while (true) {
            Handler h2 = handler;
            if (h2 != null) {
                getNewOffset(allIndexes, allSizes, h2.start);
                getNewOffset(allIndexes, allSizes, h2.end);
                getNewOffset(allIndexes, allSizes, h2.handler);
                handler = h2.next;
            } else {
                int i2 = 0;
                while (i2 < 2) {
                    ByteVector bv2 = i2 == 0 ? this.localVar : this.localVarType;
                    if (bv2 != null) {
                        byte[] b3 = bv2.data;
                        for (int u13 = 0; u13 < bv2.length; u13 += 10) {
                            int label6 = readUnsignedShort(b3, u13);
                            int newOffset4 = getNewOffset(allIndexes, allSizes, 0, label6);
                            writeShort(b3, u13, newOffset4);
                            writeShort(b3, u13 + 2, getNewOffset(allIndexes, allSizes, 0, label6 + readUnsignedShort(b3, u13 + 2)) - newOffset4);
                        }
                    }
                    i2++;
                }
                if (this.lineNumber != null) {
                    byte[] b4 = this.lineNumber.data;
                    for (int u14 = 0; u14 < this.lineNumber.length; u14 += 4) {
                        writeShort(b4, u14, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b4, u14)));
                    }
                }
                Attribute attribute = this.cattrs;
                while (true) {
                    Attribute attr = attribute;
                    if (attr != null) {
                        Label[] labels = attr.getLabels();
                        if (labels != null) {
                            for (int i3 = labels.length - 1; i3 >= 0; i3--) {
                                getNewOffset(allIndexes, allSizes, labels[i3]);
                            }
                        }
                        attribute = attr.next;
                    } else {
                        this.code = newCode;
                        return;
                    }
                }
            }
        }
    }

    static int readUnsignedShort(byte[] b2, int index) {
        return ((b2[index] & 255) << 8) | (b2[index + 1] & 255);
    }

    static short readShort(byte[] b2, int index) {
        return (short) (((b2[index] & 255) << 8) | (b2[index + 1] & 255));
    }

    static int readInt(byte[] b2, int index) {
        return ((b2[index] & 255) << 24) | ((b2[index + 1] & 255) << 16) | ((b2[index + 2] & 255) << 8) | (b2[index + 3] & 255);
    }

    static void writeShort(byte[] b2, int index, int s2) {
        b2[index] = (byte) (s2 >>> 8);
        b2[index + 1] = (byte) s2;
    }

    static int getNewOffset(int[] indexes, int[] sizes, int begin, int end) {
        int offset = end - begin;
        for (int i2 = 0; i2 < indexes.length; i2++) {
            if (begin < indexes[i2] && indexes[i2] <= end) {
                offset += sizes[i2];
            } else if (end < indexes[i2] && indexes[i2] <= begin) {
                offset -= sizes[i2];
            }
        }
        return offset;
    }

    static void getNewOffset(int[] indexes, int[] sizes, Label label) {
        if ((label.status & 4) == 0) {
            label.position = getNewOffset(indexes, sizes, 0, label.position);
            label.status |= 4;
        }
    }
}
