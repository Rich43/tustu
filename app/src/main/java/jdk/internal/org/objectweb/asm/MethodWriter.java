package jdk.internal.org.objectweb.asm;

import com.sun.org.apache.bcel.internal.Constants;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/MethodWriter.class */
class MethodWriter extends MethodVisitor {
    static final int ACC_CONSTRUCTOR = 524288;
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
    private AnnotationWriter tanns;
    private AnnotationWriter itanns;
    private AnnotationWriter[] panns;
    private AnnotationWriter[] ipanns;
    private int synthetics;
    private Attribute attrs;
    private ByteVector code;
    private int maxStack;
    private int maxLocals;
    private int currentLocals;
    private int frameCount;
    private ByteVector stackMap;
    private int previousFrameOffset;
    private int[] previousFrame;
    private int[] frame;
    private int handlerCount;
    private Handler firstHandler;
    private Handler lastHandler;
    private int methodParametersCount;
    private ByteVector methodParameters;
    private int localVarCount;
    private ByteVector localVar;
    private int localVarTypeCount;
    private ByteVector localVarType;
    private int lineNumberCount;
    private ByteVector lineNumber;
    private int lastCodeOffset;
    private AnnotationWriter ctanns;
    private AnnotationWriter ictanns;
    private Attribute cattrs;
    private boolean resize;
    private int subroutines;
    private final int compute;
    private Label labels;
    private Label previousBlock;
    private Label currentBlock;
    private int stackSize;
    private int maxStackSize;

    MethodWriter(ClassWriter classWriter, int i2, String str, String str2, String str3, String[] strArr, boolean z2, boolean z3) {
        super(Opcodes.ASM5);
        this.code = new ByteVector();
        if (classWriter.firstMethod == null) {
            classWriter.firstMethod = this;
        } else {
            classWriter.lastMethod.mv = this;
        }
        classWriter.lastMethod = this;
        this.cw = classWriter;
        this.access = i2;
        if (Constants.CONSTRUCTOR_NAME.equals(str)) {
            this.access |= 524288;
        }
        this.name = classWriter.newUTF8(str);
        this.desc = classWriter.newUTF8(str2);
        this.descriptor = str2;
        this.signature = str3;
        if (strArr != null && strArr.length > 0) {
            this.exceptionCount = strArr.length;
            this.exceptions = new int[this.exceptionCount];
            for (int i3 = 0; i3 < this.exceptionCount; i3++) {
                this.exceptions[i3] = classWriter.newClass(strArr[i3]);
            }
        }
        this.compute = z3 ? 0 : z2 ? 1 : 2;
        if (z2 || z3) {
            int argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
            argumentsAndReturnSizes = (i2 & 8) != 0 ? argumentsAndReturnSizes - 1 : argumentsAndReturnSizes;
            this.maxLocals = argumentsAndReturnSizes;
            this.currentLocals = argumentsAndReturnSizes;
            this.labels = new Label();
            this.labels.status |= 8;
            visitLabel(this.labels);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitParameter(String str, int i2) {
        if (this.methodParameters == null) {
            this.methodParameters = new ByteVector();
        }
        this.methodParametersCount++;
        this.methodParameters.putShort(str == null ? 0 : this.cw.newUTF8(str)).putShort(i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        this.annd = new ByteVector();
        return new AnnotationWriter(this.cw, false, this.annd, null, 0);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.cw.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, 2);
        if (z2) {
            annotationWriter.next = this.anns;
            this.anns = annotationWriter;
        } else {
            annotationWriter.next = this.ianns;
            this.ianns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget(i2, typePath, byteVector);
        byteVector.putShort(this.cw.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (z2) {
            annotationWriter.next = this.tanns;
            this.tanns = annotationWriter;
        } else {
            annotationWriter.next = this.itanns;
            this.itanns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int i2, String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(str)) {
            this.synthetics = Math.max(this.synthetics, i2 + 1);
            return new AnnotationWriter(this.cw, false, byteVector, null, 0);
        }
        byteVector.putShort(this.cw.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, 2);
        if (z2) {
            if (this.panns == null) {
                this.panns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            annotationWriter.next = this.panns[i2];
            this.panns[i2] = annotationWriter;
        } else {
            if (this.ipanns == null) {
                this.ipanns = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            annotationWriter.next = this.ipanns[i2];
            this.ipanns[i2] = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.next = this.cattrs;
            this.cattrs = attribute;
        } else {
            attribute.next = this.attrs;
            this.attrs = attribute;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitCode() {
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        int i5;
        if (this.compute == 0) {
            return;
        }
        if (i2 == -1) {
            if (this.previousFrame == null) {
                visitImplicitFirstFrame();
            }
            this.currentLocals = i3;
            int iStartFrame = startFrame(this.code.length, i3, i4);
            for (int i6 = 0; i6 < i3; i6++) {
                if (objArr[i6] instanceof String) {
                    int i7 = iStartFrame;
                    iStartFrame++;
                    this.frame[i7] = 24117248 | this.cw.addType((String) objArr[i6]);
                } else if (objArr[i6] instanceof Integer) {
                    int i8 = iStartFrame;
                    iStartFrame++;
                    this.frame[i8] = ((Integer) objArr[i6]).intValue();
                } else {
                    int i9 = iStartFrame;
                    iStartFrame++;
                    this.frame[i9] = 25165824 | this.cw.addUninitializedType("", ((Label) objArr[i6]).position);
                }
            }
            for (int i10 = 0; i10 < i4; i10++) {
                if (objArr2[i10] instanceof String) {
                    int i11 = iStartFrame;
                    iStartFrame++;
                    this.frame[i11] = 24117248 | this.cw.addType((String) objArr2[i10]);
                } else if (objArr2[i10] instanceof Integer) {
                    int i12 = iStartFrame;
                    iStartFrame++;
                    this.frame[i12] = ((Integer) objArr2[i10]).intValue();
                } else {
                    int i13 = iStartFrame;
                    iStartFrame++;
                    this.frame[i13] = 25165824 | this.cw.addUninitializedType("", ((Label) objArr2[i10]).position);
                }
            }
            endFrame();
        } else {
            if (this.stackMap == null) {
                this.stackMap = new ByteVector();
                i5 = this.code.length;
            } else {
                i5 = (this.code.length - this.previousFrameOffset) - 1;
                if (i5 < 0) {
                    if (i2 == 3) {
                        return;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
            switch (i2) {
                case 0:
                    this.currentLocals = i3;
                    this.stackMap.putByte(255).putShort(i5).putShort(i3);
                    for (int i14 = 0; i14 < i3; i14++) {
                        writeFrameType(objArr[i14]);
                    }
                    this.stackMap.putShort(i4);
                    for (int i15 = 0; i15 < i4; i15++) {
                        writeFrameType(objArr2[i15]);
                    }
                    break;
                case 1:
                    this.currentLocals += i3;
                    this.stackMap.putByte(251 + i3).putShort(i5);
                    for (int i16 = 0; i16 < i3; i16++) {
                        writeFrameType(objArr[i16]);
                    }
                    break;
                case 2:
                    this.currentLocals -= i3;
                    this.stackMap.putByte(251 - i3).putShort(i5);
                    break;
                case 3:
                    if (i5 < 64) {
                        this.stackMap.putByte(i5);
                        break;
                    } else {
                        this.stackMap.putByte(251).putShort(i5);
                        break;
                    }
                case 4:
                    if (i5 < 64) {
                        this.stackMap.putByte(64 + i5);
                    } else {
                        this.stackMap.putByte(247).putShort(i5);
                    }
                    writeFrameType(objArr2[0]);
                    break;
            }
            this.previousFrameOffset = this.code.length;
            this.frameCount++;
        }
        this.maxStack = Math.max(this.maxStack, i4);
        this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i2) {
        this.lastCodeOffset = this.code.length;
        this.code.putByte(i2);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(i2, 0, null, null);
            } else {
                int i3 = this.stackSize + Frame.SIZE[i2];
                if (i3 > this.maxStackSize) {
                    this.maxStackSize = i3;
                }
                this.stackSize = i3;
            }
            if ((i2 >= 172 && i2 <= 177) || i2 == 191) {
                noSuccessor();
            }
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i2, int i3) {
        this.lastCodeOffset = this.code.length;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(i2, i3, null, null);
            } else if (i2 != 188) {
                int i4 = this.stackSize + 1;
                if (i4 > this.maxStackSize) {
                    this.maxStackSize = i4;
                }
                this.stackSize = i4;
            }
        }
        if (i2 == 17) {
            this.code.put12(i2, i3);
        } else {
            this.code.put11(i2, i3);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i2, int i3) {
        int i4;
        int i5;
        this.lastCodeOffset = this.code.length;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(i2, i3, null, null);
            } else if (i2 == 169) {
                this.currentBlock.status |= 256;
                this.currentBlock.inputStackTop = this.stackSize;
                noSuccessor();
            } else {
                int i6 = this.stackSize + Frame.SIZE[i2];
                if (i6 > this.maxStackSize) {
                    this.maxStackSize = i6;
                }
                this.stackSize = i6;
            }
        }
        if (this.compute != 2) {
            if (i2 == 22 || i2 == 24 || i2 == 55 || i2 == 57) {
                i5 = i3 + 2;
            } else {
                i5 = i3 + 1;
            }
            if (i5 > this.maxLocals) {
                this.maxLocals = i5;
            }
        }
        if (i3 < 4 && i2 != 169) {
            if (i2 < 54) {
                i4 = 26 + ((i2 - 21) << 2) + i3;
            } else {
                i4 = 59 + ((i2 - 54) << 2) + i3;
            }
            this.code.putByte(i4);
        } else if (i3 >= 256) {
            this.code.putByte(196).put12(i2, i3);
        } else {
            this.code.put11(i2, i3);
        }
        if (i2 >= 54 && this.compute == 0 && this.handlerCount > 0) {
            visitLabel(new Label());
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i2, String str) {
        this.lastCodeOffset = this.code.length;
        Item itemNewClassItem = this.cw.newClassItem(str);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(i2, this.code.length, this.cw, itemNewClassItem);
            } else if (i2 == 187) {
                int i3 = this.stackSize + 1;
                if (i3 > this.maxStackSize) {
                    this.maxStackSize = i3;
                }
                this.stackSize = i3;
            }
        }
        this.code.put12(i2, itemNewClassItem.index);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        int i3;
        this.lastCodeOffset = this.code.length;
        Item itemNewFieldItem = this.cw.newFieldItem(str, str2, str3);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(i2, 0, this.cw, itemNewFieldItem);
            } else {
                char cCharAt = str3.charAt(0);
                switch (i2) {
                    case 178:
                        i3 = this.stackSize + ((cCharAt == 'D' || cCharAt == 'J') ? 2 : 1);
                        break;
                    case 179:
                        i3 = this.stackSize + ((cCharAt == 'D' || cCharAt == 'J') ? -2 : -1);
                        break;
                    case 180:
                        i3 = this.stackSize + ((cCharAt == 'D' || cCharAt == 'J') ? 1 : 0);
                        break;
                    default:
                        i3 = this.stackSize + ((cCharAt == 'D' || cCharAt == 'J') ? -3 : -2);
                        break;
                }
                if (i3 > this.maxStackSize) {
                    this.maxStackSize = i3;
                }
                this.stackSize = i3;
            }
        }
        this.code.put12(i2, itemNewFieldItem.index);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        int i3;
        this.lastCodeOffset = this.code.length;
        Item itemNewMethodItem = this.cw.newMethodItem(str, str2, str3, z2);
        int argumentsAndReturnSizes = itemNewMethodItem.intVal;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(i2, 0, this.cw, itemNewMethodItem);
            } else {
                if (argumentsAndReturnSizes == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
                    itemNewMethodItem.intVal = argumentsAndReturnSizes;
                }
                if (i2 == 184) {
                    i3 = (this.stackSize - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3) + 1;
                } else {
                    i3 = (this.stackSize - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3);
                }
                if (i3 > this.maxStackSize) {
                    this.maxStackSize = i3;
                }
                this.stackSize = i3;
            }
        }
        if (i2 == 185) {
            if (argumentsAndReturnSizes == 0) {
                argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
                itemNewMethodItem.intVal = argumentsAndReturnSizes;
            }
            this.code.put12(185, itemNewMethodItem.index).put11(argumentsAndReturnSizes >> 2, 0);
            return;
        }
        this.code.put12(i2, itemNewMethodItem.index);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        this.lastCodeOffset = this.code.length;
        Item itemNewInvokeDynamicItem = this.cw.newInvokeDynamicItem(str, str2, handle, objArr);
        int argumentsAndReturnSizes = itemNewInvokeDynamicItem.intVal;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(186, 0, this.cw, itemNewInvokeDynamicItem);
            } else {
                if (argumentsAndReturnSizes == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str2);
                    itemNewInvokeDynamicItem.intVal = argumentsAndReturnSizes;
                }
                int i2 = (this.stackSize - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3) + 1;
                if (i2 > this.maxStackSize) {
                    this.maxStackSize = i2;
                }
                this.stackSize = i2;
            }
        }
        this.code.put12(186, itemNewInvokeDynamicItem.index);
        this.code.putShort(0);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i2, Label label) {
        this.lastCodeOffset = this.code.length;
        Label label2 = null;
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(i2, 0, null, null);
                label.getFirst().status |= 16;
                addSuccessor(0, label);
                if (i2 != 167) {
                    label2 = new Label();
                }
            } else if (i2 == 168) {
                if ((label.status & 512) == 0) {
                    label.status |= 512;
                    this.subroutines++;
                }
                this.currentBlock.status |= 128;
                addSuccessor(this.stackSize + 1, label);
                label2 = new Label();
            } else {
                this.stackSize += Frame.SIZE[i2];
                addSuccessor(this.stackSize, label);
            }
        }
        if ((label.status & 2) != 0 && label.position - this.code.length < -32768) {
            if (i2 == 167) {
                this.code.putByte(200);
            } else if (i2 == 168) {
                this.code.putByte(201);
            } else {
                if (label2 != null) {
                    label2.status |= 16;
                }
                this.code.putByte(i2 <= 166 ? ((i2 + 1) ^ 1) - 1 : i2 ^ 1);
                this.code.putShort(8);
                this.code.putByte(200);
            }
            label.put(this, this.code, this.code.length - 1, true);
        } else {
            this.code.putByte(i2);
            label.put(this, this.code, this.code.length - 1, false);
        }
        if (this.currentBlock != null) {
            if (label2 != null) {
                visitLabel(label2);
            }
            if (i2 == 167) {
                noSuccessor();
            }
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
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

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        int i2;
        this.lastCodeOffset = this.code.length;
        Item itemNewConstItem = this.cw.newConstItem(obj);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(18, 0, this.cw, itemNewConstItem);
            } else {
                if (itemNewConstItem.type == 5 || itemNewConstItem.type == 6) {
                    i2 = this.stackSize + 2;
                } else {
                    i2 = this.stackSize + 1;
                }
                if (i2 > this.maxStackSize) {
                    this.maxStackSize = i2;
                }
                this.stackSize = i2;
            }
        }
        int i3 = itemNewConstItem.index;
        if (itemNewConstItem.type == 5 || itemNewConstItem.type == 6) {
            this.code.put12(20, i3);
        } else if (i3 >= 256) {
            this.code.put12(19, i3);
        } else {
            this.code.put11(18, i3);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i2, int i3) {
        int i4;
        this.lastCodeOffset = this.code.length;
        if (this.currentBlock != null && this.compute == 0) {
            this.currentBlock.frame.execute(132, i2, null, null);
        }
        if (this.compute != 2 && (i4 = i2 + 1) > this.maxLocals) {
            this.maxLocals = i4;
        }
        if (i2 > 255 || i3 > 127 || i3 < -128) {
            this.code.putByte(196).put12(132, i2).putShort(i3);
        } else {
            this.code.putByte(132).put11(i2, i3);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        this.lastCodeOffset = this.code.length;
        int i4 = this.code.length;
        this.code.putByte(170);
        this.code.putByteArray(null, 0, (4 - (this.code.length % 4)) % 4);
        label.put(this, this.code, i4, true);
        this.code.putInt(i2).putInt(i3);
        for (Label label2 : labelArr) {
            label2.put(this, this.code, i4, true);
        }
        visitSwitchInsn(label, labelArr);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        this.lastCodeOffset = this.code.length;
        int i2 = this.code.length;
        this.code.putByte(171);
        this.code.putByteArray(null, 0, (4 - (this.code.length % 4)) % 4);
        label.put(this, this.code, i2, true);
        this.code.putInt(labelArr.length);
        for (int i3 = 0; i3 < labelArr.length; i3++) {
            this.code.putInt(iArr[i3]);
            labelArr[i3].put(this, this.code, i2, true);
        }
        visitSwitchInsn(label, labelArr);
    }

    private void visitSwitchInsn(Label label, Label[] labelArr) {
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(171, 0, null, null);
                addSuccessor(0, label);
                label.getFirst().status |= 16;
                for (int i2 = 0; i2 < labelArr.length; i2++) {
                    addSuccessor(0, labelArr[i2]);
                    labelArr[i2].getFirst().status |= 16;
                }
            } else {
                this.stackSize--;
                addSuccessor(this.stackSize, label);
                for (Label label2 : labelArr) {
                    addSuccessor(this.stackSize, label2);
                }
            }
            noSuccessor();
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i2) {
        this.lastCodeOffset = this.code.length;
        Item itemNewClassItem = this.cw.newClassItem(str);
        if (this.currentBlock != null) {
            if (this.compute == 0) {
                this.currentBlock.frame.execute(197, i2, this.cw, itemNewClassItem);
            } else {
                this.stackSize += 1 - i2;
            }
        }
        this.code.put12(197, itemNewClassItem.index).putByte(i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget((i2 & (-16776961)) | (this.lastCodeOffset << 8), typePath, byteVector);
        byteVector.putShort(this.cw.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (z2) {
            annotationWriter.next = this.ctanns;
            this.ctanns = annotationWriter;
        } else {
            annotationWriter.next = this.ictanns;
            this.ictanns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.handlerCount++;
        Handler handler = new Handler();
        handler.start = label;
        handler.end = label2;
        handler.handler = label3;
        handler.desc = str;
        handler.type = str != null ? this.cw.newClass(str) : 0;
        if (this.lastHandler == null) {
            this.firstHandler = handler;
        } else {
            this.lastHandler.next = handler;
        }
        this.lastHandler = handler;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget(i2, typePath, byteVector);
        byteVector.putShort(this.cw.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (z2) {
            annotationWriter.next = this.ctanns;
            this.ctanns = annotationWriter;
        } else {
            annotationWriter.next = this.ictanns;
            this.ictanns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        if (str3 != null) {
            if (this.localVarType == null) {
                this.localVarType = new ByteVector();
            }
            this.localVarTypeCount++;
            this.localVarType.putShort(label.position).putShort(label2.position - label.position).putShort(this.cw.newUTF8(str)).putShort(this.cw.newUTF8(str3)).putShort(i2);
        }
        if (this.localVar == null) {
            this.localVar = new ByteVector();
        }
        this.localVarCount++;
        this.localVar.putShort(label.position).putShort(label2.position - label.position).putShort(this.cw.newUTF8(str)).putShort(this.cw.newUTF8(str2)).putShort(i2);
        if (this.compute != 2) {
            char cCharAt = str2.charAt(0);
            int i3 = i2 + ((cCharAt == 'J' || cCharAt == 'D') ? 2 : 1);
            if (i3 > this.maxLocals) {
                this.maxLocals = i3;
            }
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        byteVector.putByte(i2 >>> 24).putShort(labelArr.length);
        for (int i3 = 0; i3 < labelArr.length; i3++) {
            byteVector.putShort(labelArr[i3].position).putShort(labelArr2[i3].position - labelArr[i3].position).putShort(iArr[i3]);
        }
        if (typePath == null) {
            byteVector.putByte(0);
        } else {
            byteVector.putByteArray(typePath.f12860b, typePath.offset, (typePath.f12860b[typePath.offset] * 2) + 1);
        }
        byteVector.putShort(this.cw.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (z2) {
            annotationWriter.next = this.ctanns;
            this.ctanns = annotationWriter;
        } else {
            annotationWriter.next = this.ictanns;
            this.ictanns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int i2, Label label) {
        if (this.lineNumber == null) {
            this.lineNumber = new ByteVector();
        }
        this.lineNumberCount++;
        this.lineNumber.putShort(label.position);
        this.lineNumber.putShort(i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i2, int i3) {
        if (this.resize) {
            resizeInstructions();
        }
        if (this.compute != 0) {
            if (this.compute == 1) {
                Handler handler = this.firstHandler;
                while (true) {
                    Handler handler2 = handler;
                    if (handler2 == null) {
                        break;
                    }
                    Label label = handler2.handler;
                    Label label2 = handler2.end;
                    for (Label label3 = handler2.start; label3 != label2; label3 = label3.successor) {
                        Edge edge = new Edge();
                        edge.info = Integer.MAX_VALUE;
                        edge.successor = label;
                        if ((label3.status & 128) == 0) {
                            edge.next = label3.successors;
                            label3.successors = edge;
                        } else {
                            edge.next = label3.successors.next.next;
                            label3.successors.next.next = edge;
                        }
                    }
                    handler = handler2.next;
                }
                if (this.subroutines > 0) {
                    int i4 = 0;
                    this.labels.visitSubroutine(null, 1L, this.subroutines);
                    Label label4 = this.labels;
                    while (true) {
                        Label label5 = label4;
                        if (label5 == null) {
                            break;
                        }
                        if ((label5.status & 128) != 0) {
                            Label label6 = label5.successors.next.successor;
                            if ((label6.status & 1024) == 0) {
                                i4++;
                                label6.visitSubroutine(null, ((i4 / 32) << 32) | (1 << (i4 % 32)), this.subroutines);
                            }
                        }
                        label4 = label5.successor;
                    }
                    Label label7 = this.labels;
                    while (true) {
                        Label label8 = label7;
                        if (label8 == null) {
                            break;
                        }
                        if ((label8.status & 128) != 0) {
                            Label label9 = this.labels;
                            while (true) {
                                Label label10 = label9;
                                if (label10 == null) {
                                    break;
                                }
                                label10.status &= -2049;
                                label9 = label10.successor;
                            }
                            label8.successors.next.successor.visitSubroutine(label8, 0L, this.subroutines);
                        }
                        label7 = label8.successor;
                    }
                }
                int i5 = 0;
                Label label11 = this.labels;
                while (label11 != null) {
                    Label label12 = label11;
                    label11 = label11.next;
                    int i6 = label12.inputStackTop;
                    int i7 = i6 + label12.outputStackMax;
                    if (i7 > i5) {
                        i5 = i7;
                    }
                    Edge edge2 = label12.successors;
                    if ((label12.status & 128) != 0) {
                        edge2 = edge2.next;
                    }
                    while (edge2 != null) {
                        Label label13 = edge2.successor;
                        if ((label13.status & 8) == 0) {
                            label13.inputStackTop = edge2.info == Integer.MAX_VALUE ? 1 : i6 + edge2.info;
                            label13.status |= 8;
                            label13.next = label11;
                            label11 = label13;
                        }
                        edge2 = edge2.next;
                    }
                }
                this.maxStack = Math.max(i2, i5);
                return;
            }
            this.maxStack = i2;
            this.maxLocals = i3;
            return;
        }
        Handler handler3 = this.firstHandler;
        while (true) {
            Handler handler4 = handler3;
            if (handler4 == null) {
                break;
            }
            Label first = handler4.handler.getFirst();
            Label first2 = handler4.end.getFirst();
            int iAddType = 24117248 | this.cw.addType(handler4.desc == null ? "java/lang/Throwable" : handler4.desc);
            first.status |= 16;
            for (Label first3 = handler4.start.getFirst(); first3 != first2; first3 = first3.successor) {
                Edge edge3 = new Edge();
                edge3.info = iAddType;
                edge3.successor = first;
                edge3.next = first3.successors;
                first3.successors = edge3;
            }
            handler3 = handler4.next;
        }
        Frame frame = this.labels.frame;
        frame.initInputFrame(this.cw, this.access, Type.getArgumentTypes(this.descriptor), this.maxLocals);
        visitFrame(frame);
        int iMax = 0;
        Label label14 = this.labels;
        while (label14 != null) {
            Label label15 = label14;
            label14 = label14.next;
            label15.next = null;
            Frame frame2 = label15.frame;
            if ((label15.status & 16) != 0) {
                label15.status |= 32;
            }
            label15.status |= 64;
            int length = frame2.inputStack.length + label15.outputStackMax;
            if (length > iMax) {
                iMax = length;
            }
            Edge edge4 = label15.successors;
            while (true) {
                Edge edge5 = edge4;
                if (edge5 != null) {
                    Label first4 = edge5.successor.getFirst();
                    if (frame2.merge(this.cw, first4.frame, edge5.info) && first4.next == null) {
                        first4.next = label14;
                        label14 = first4;
                    }
                    edge4 = edge5.next;
                }
            }
        }
        Label label16 = this.labels;
        while (true) {
            Label label17 = label16;
            if (label17 == null) {
                break;
            }
            Frame frame3 = label17.frame;
            if ((label17.status & 32) != 0) {
                visitFrame(frame3);
            }
            if ((label17.status & 64) == 0) {
                Label label18 = label17.successor;
                int i8 = label17.position;
                int i9 = (label18 == null ? this.code.length : label18.position) - 1;
                if (i9 >= i8) {
                    iMax = Math.max(iMax, 1);
                    for (int i10 = i8; i10 < i9; i10++) {
                        this.code.data[i10] = 0;
                    }
                    this.code.data[i9] = -65;
                    this.frame[startFrame(i8, 0, 1)] = 24117248 | this.cw.addType("java/lang/Throwable");
                    endFrame();
                    this.firstHandler = Handler.remove(this.firstHandler, label17, label18);
                }
            }
            label16 = label17.successor;
        }
        this.handlerCount = 0;
        for (Handler handler5 = this.firstHandler; handler5 != null; handler5 = handler5.next) {
            this.handlerCount++;
        }
        this.maxStack = iMax;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
    }

    private void addSuccessor(int i2, Label label) {
        Edge edge = new Edge();
        edge.info = i2;
        edge.successor = label;
        edge.next = this.currentBlock.successors;
        this.currentBlock.successors = edge;
    }

    private void noSuccessor() {
        if (this.compute == 0) {
            Label label = new Label();
            label.frame = new Frame();
            label.frame.owner = label;
            label.resolve(this, this.code.length, this.code.data);
            this.previousBlock.successor = label;
            this.previousBlock = label;
        } else {
            this.currentBlock.outputStackMax = this.maxStackSize;
        }
        this.currentBlock = null;
    }

    private void visitFrame(Frame frame) {
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int[] iArr = frame.inputLocals;
        int[] iArr2 = frame.inputStack;
        int i5 = 0;
        while (i5 < iArr.length) {
            int i6 = iArr[i5];
            if (i6 == 16777216) {
                i2++;
            } else {
                i3 += i2 + 1;
                i2 = 0;
            }
            if (i6 == 16777220 || i6 == 16777219) {
                i5++;
            }
            i5++;
        }
        int i7 = 0;
        while (i7 < iArr2.length) {
            int i8 = iArr2[i7];
            i4++;
            if (i8 == 16777220 || i8 == 16777219) {
                i7++;
            }
            i7++;
        }
        int iStartFrame = startFrame(frame.owner.position, i3, i4);
        int i9 = 0;
        while (i3 > 0) {
            int i10 = iArr[i9];
            int i11 = iStartFrame;
            iStartFrame++;
            this.frame[i11] = i10;
            if (i10 == 16777220 || i10 == 16777219) {
                i9++;
            }
            i9++;
            i3--;
        }
        int i12 = 0;
        while (i12 < iArr2.length) {
            int i13 = iArr2[i12];
            int i14 = iStartFrame;
            iStartFrame++;
            this.frame[i14] = i13;
            if (i13 == 16777220 || i13 == 16777219) {
                i12++;
            }
            i12++;
        }
        endFrame();
    }

    private void visitImplicitFirstFrame() {
        int iStartFrame = startFrame(0, this.descriptor.length() + 1, 0);
        if ((this.access & 8) == 0) {
            if ((this.access & 524288) == 0) {
                iStartFrame++;
                this.frame[iStartFrame] = 24117248 | this.cw.addType(this.cw.thisName);
            } else {
                iStartFrame++;
                this.frame[iStartFrame] = 6;
            }
        }
        int i2 = 1;
        while (true) {
            int i3 = i2;
            int i4 = i2;
            i2++;
            switch (this.descriptor.charAt(i4)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z':
                    int i5 = iStartFrame;
                    iStartFrame++;
                    this.frame[i5] = 1;
                    break;
                case 'D':
                    int i6 = iStartFrame;
                    iStartFrame++;
                    this.frame[i6] = 3;
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    this.frame[1] = iStartFrame - 3;
                    endFrame();
                    return;
                case 'F':
                    int i7 = iStartFrame;
                    iStartFrame++;
                    this.frame[i7] = 2;
                    break;
                case 'J':
                    int i8 = iStartFrame;
                    iStartFrame++;
                    this.frame[i8] = 4;
                    break;
                case 'L':
                    while (this.descriptor.charAt(i2) != ';') {
                        i2++;
                    }
                    int i9 = iStartFrame;
                    iStartFrame++;
                    int i10 = i2;
                    i2++;
                    this.frame[i9] = 24117248 | this.cw.addType(this.descriptor.substring(i3 + 1, i10));
                    break;
                case '[':
                    while (this.descriptor.charAt(i2) == '[') {
                        i2++;
                    }
                    if (this.descriptor.charAt(i2) == 'L') {
                        do {
                            i2++;
                        } while (this.descriptor.charAt(i2) != ';');
                    }
                    int i11 = iStartFrame;
                    iStartFrame++;
                    i2++;
                    this.frame[i11] = 24117248 | this.cw.addType(this.descriptor.substring(i3, i2));
                    break;
            }
        }
    }

    private int startFrame(int i2, int i3, int i4) {
        int i5 = 3 + i3 + i4;
        if (this.frame == null || this.frame.length < i5) {
            this.frame = new int[i5];
        }
        this.frame[0] = i2;
        this.frame[1] = i3;
        this.frame[2] = i4;
        return 3;
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v26 */
    /* JADX WARN: Type inference failed for: r0v27 */
    /* JADX WARN: Type inference failed for: r0v28 */
    /* JADX WARN: Type inference failed for: r0v29 */
    /* JADX WARN: Type inference failed for: r0v78 */
    /* JADX WARN: Type inference failed for: r0v79 */
    /* JADX WARN: Type inference failed for: r0v80 */
    private void writeFrame() {
        int i2;
        int i3 = this.frame[1];
        int i4 = this.frame[2];
        if ((this.cw.version & 65535) < 50) {
            this.stackMap.putShort(this.frame[0]).putShort(i3);
            writeFrameTypes(3, 3 + i3);
            this.stackMap.putShort(i4);
            writeFrameTypes(3 + i3, 3 + i3 + i4);
        }
        int i5 = this.previousFrame[1];
        boolean z2 = 255;
        int i6 = 0;
        if (this.frameCount == 0) {
            i2 = this.frame[0];
        } else {
            i2 = (this.frame[0] - this.previousFrame[0]) - 1;
        }
        if (i4 == 0) {
            i6 = i3 - i5;
            switch (i6) {
                case -3:
                case -2:
                case -1:
                    z2 = 248;
                    i5 = i3;
                    break;
                case 0:
                    z2 = i2 < 64 ? 0 : 251;
                    break;
                case 1:
                case 2:
                case 3:
                    z2 = 252;
                    break;
            }
        } else if (i3 == i5 && i4 == 1) {
            z2 = i2 < 63 ? 64 : 247;
        }
        if (z2 != 255) {
            int i7 = 3;
            int i8 = 0;
            while (true) {
                if (i8 < i5) {
                    if (this.frame[i7] != this.previousFrame[i7]) {
                        z2 = 255;
                    } else {
                        i7++;
                        i8++;
                    }
                }
            }
        }
        switch (z2) {
            case false:
                this.stackMap.putByte(i2);
                break;
            case true:
                this.stackMap.putByte(64 + i2);
                writeFrameTypes(3 + i3, 4 + i3);
                break;
            case true:
                this.stackMap.putByte(247).putShort(i2);
                writeFrameTypes(3 + i3, 4 + i3);
                break;
            case true:
                this.stackMap.putByte(251 + i6).putShort(i2);
                break;
            case true:
                this.stackMap.putByte(251).putShort(i2);
                break;
            case true:
                this.stackMap.putByte(251 + i6).putShort(i2);
                writeFrameTypes(3 + i5, 3 + i3);
                break;
            default:
                this.stackMap.putByte(255).putShort(i2).putShort(i3);
                writeFrameTypes(3, 3 + i3);
                this.stackMap.putShort(i4);
                writeFrameTypes(3 + i3, 3 + i3 + i4);
                break;
        }
    }

    private void writeFrameTypes(int i2, int i3) {
        for (int i4 = i2; i4 < i3; i4++) {
            int i5 = this.frame[i4];
            int i6 = i5 & (-268435456);
            if (i6 == 0) {
                int i7 = i5 & 1048575;
                switch (i5 & 267386880) {
                    case 24117248:
                        this.stackMap.putByte(7).putShort(this.cw.newClass(this.cw.typeTable[i7].strVal1));
                        break;
                    case 25165824:
                        this.stackMap.putByte(8).putShort(this.cw.typeTable[i7].intVal);
                        break;
                    default:
                        this.stackMap.putByte(i7);
                        break;
                }
            } else {
                StringBuilder sb = new StringBuilder();
                int i8 = i6 >> 28;
                while (true) {
                    int i9 = i8;
                    i8--;
                    if (i9 > 0) {
                        sb.append('[');
                    } else {
                        if ((i5 & 267386880) == 24117248) {
                            sb.append('L');
                            sb.append(this.cw.typeTable[i5 & 1048575].strVal1);
                            sb.append(';');
                        } else {
                            switch (i5 & 15) {
                                case 1:
                                    sb.append('I');
                                    break;
                                case 2:
                                    sb.append('F');
                                    break;
                                case 3:
                                    sb.append('D');
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                default:
                                    sb.append('J');
                                    break;
                                case 9:
                                    sb.append('Z');
                                    break;
                                case 10:
                                    sb.append('B');
                                    break;
                                case 11:
                                    sb.append('C');
                                    break;
                                case 12:
                                    sb.append('S');
                                    break;
                            }
                        }
                        this.stackMap.putByte(7).putShort(this.cw.newClass(sb.toString()));
                    }
                }
            }
        }
    }

    private void writeFrameType(Object obj) {
        if (obj instanceof String) {
            this.stackMap.putByte(7).putShort(this.cw.newClass((String) obj));
        } else if (obj instanceof Integer) {
            this.stackMap.putByte(((Integer) obj).intValue());
        } else {
            this.stackMap.putByte(8).putShort(((Label) obj).position);
        }
    }

    final int getSize() {
        if (this.classReaderOffset != 0) {
            return 6 + this.classReaderLength;
        }
        int size = 8;
        if (this.code.length > 0) {
            if (this.code.length > 65536) {
                throw new RuntimeException("Method code too large!");
            }
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
                this.cw.newUTF8((this.cw.version & 65535) >= 50 ? "StackMapTable" : "StackMap");
                size += 8 + this.stackMap.length;
            }
            if (this.ctanns != null) {
                this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
                size += 8 + this.ctanns.getSize();
            }
            if (this.ictanns != null) {
                this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
                size += 8 + this.ictanns.getSize();
            }
            if (this.cattrs != null) {
                size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
        }
        if (this.exceptionCount > 0) {
            this.cw.newUTF8("Exceptions");
            size += 8 + (2 * this.exceptionCount);
        }
        if ((this.access & 4096) != 0 && ((this.cw.version & 65535) < 49 || (this.access & 262144) != 0)) {
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
        if (this.methodParameters != null) {
            this.cw.newUTF8("MethodParameters");
            size += 7 + this.methodParameters.length;
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
        if (this.tanns != null) {
            this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
            size += 8 + this.tanns.getSize();
        }
        if (this.itanns != null) {
            this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
            size += 8 + this.itanns.getSize();
        }
        if (this.panns != null) {
            this.cw.newUTF8("RuntimeVisibleParameterAnnotations");
            size += 7 + (2 * (this.panns.length - this.synthetics));
            for (int length = this.panns.length - 1; length >= this.synthetics; length--) {
                size += this.panns[length] == null ? 0 : this.panns[length].getSize();
            }
        }
        if (this.ipanns != null) {
            this.cw.newUTF8("RuntimeInvisibleParameterAnnotations");
            size += 7 + (2 * (this.ipanns.length - this.synthetics));
            for (int length2 = this.ipanns.length - 1; length2 >= this.synthetics; length2--) {
                size += this.ipanns[length2] == null ? 0 : this.ipanns[length2].getSize();
            }
        }
        if (this.attrs != null) {
            size += this.attrs.getSize(this.cw, null, 0, -1, -1);
        }
        return size;
    }

    final void put(ByteVector byteVector) {
        byteVector.putShort(this.access & ((917504 | ((this.access & 262144) / 64)) ^ (-1))).putShort(this.name).putShort(this.desc);
        if (this.classReaderOffset != 0) {
            byteVector.putByteArray(this.cw.cr.f12859b, this.classReaderOffset, this.classReaderLength);
            return;
        }
        int count = 0;
        if (this.code.length > 0) {
            count = 0 + 1;
        }
        if (this.exceptionCount > 0) {
            count++;
        }
        if ((this.access & 4096) != 0 && ((this.cw.version & 65535) < 49 || (this.access & 262144) != 0)) {
            count++;
        }
        if ((this.access & 131072) != 0) {
            count++;
        }
        if (this.signature != null) {
            count++;
        }
        if (this.methodParameters != null) {
            count++;
        }
        if (this.annd != null) {
            count++;
        }
        if (this.anns != null) {
            count++;
        }
        if (this.ianns != null) {
            count++;
        }
        if (this.tanns != null) {
            count++;
        }
        if (this.itanns != null) {
            count++;
        }
        if (this.panns != null) {
            count++;
        }
        if (this.ipanns != null) {
            count++;
        }
        if (this.attrs != null) {
            count += this.attrs.getCount();
        }
        byteVector.putShort(count);
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
            if (this.ctanns != null) {
                size += 8 + this.ctanns.getSize();
            }
            if (this.ictanns != null) {
                size += 8 + this.ictanns.getSize();
            }
            if (this.cattrs != null) {
                size += this.cattrs.getSize(this.cw, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
            byteVector.putShort(this.cw.newUTF8("Code")).putInt(size);
            byteVector.putShort(this.maxStack).putShort(this.maxLocals);
            byteVector.putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
            byteVector.putShort(this.handlerCount);
            if (this.handlerCount > 0) {
                Handler handler = this.firstHandler;
                while (true) {
                    Handler handler2 = handler;
                    if (handler2 == null) {
                        break;
                    }
                    byteVector.putShort(handler2.start.position).putShort(handler2.end.position).putShort(handler2.handler.position).putShort(handler2.type);
                    handler = handler2.next;
                }
            }
            int count2 = 0;
            if (this.localVar != null) {
                count2 = 0 + 1;
            }
            if (this.localVarType != null) {
                count2++;
            }
            if (this.lineNumber != null) {
                count2++;
            }
            if (this.stackMap != null) {
                count2++;
            }
            if (this.ctanns != null) {
                count2++;
            }
            if (this.ictanns != null) {
                count2++;
            }
            if (this.cattrs != null) {
                count2 += this.cattrs.getCount();
            }
            byteVector.putShort(count2);
            if (this.localVar != null) {
                byteVector.putShort(this.cw.newUTF8("LocalVariableTable"));
                byteVector.putInt(this.localVar.length + 2).putShort(this.localVarCount);
                byteVector.putByteArray(this.localVar.data, 0, this.localVar.length);
            }
            if (this.localVarType != null) {
                byteVector.putShort(this.cw.newUTF8("LocalVariableTypeTable"));
                byteVector.putInt(this.localVarType.length + 2).putShort(this.localVarTypeCount);
                byteVector.putByteArray(this.localVarType.data, 0, this.localVarType.length);
            }
            if (this.lineNumber != null) {
                byteVector.putShort(this.cw.newUTF8("LineNumberTable"));
                byteVector.putInt(this.lineNumber.length + 2).putShort(this.lineNumberCount);
                byteVector.putByteArray(this.lineNumber.data, 0, this.lineNumber.length);
            }
            if (this.stackMap != null) {
                byteVector.putShort(this.cw.newUTF8((this.cw.version & 65535) >= 50 ? "StackMapTable" : "StackMap"));
                byteVector.putInt(this.stackMap.length + 2).putShort(this.frameCount);
                byteVector.putByteArray(this.stackMap.data, 0, this.stackMap.length);
            }
            if (this.ctanns != null) {
                byteVector.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
                this.ctanns.put(byteVector);
            }
            if (this.ictanns != null) {
                byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
                this.ictanns.put(byteVector);
            }
            if (this.cattrs != null) {
                this.cattrs.put(this.cw, this.code.data, this.code.length, this.maxLocals, this.maxStack, byteVector);
            }
        }
        if (this.exceptionCount > 0) {
            byteVector.putShort(this.cw.newUTF8("Exceptions")).putInt((2 * this.exceptionCount) + 2);
            byteVector.putShort(this.exceptionCount);
            for (int i2 = 0; i2 < this.exceptionCount; i2++) {
                byteVector.putShort(this.exceptions[i2]);
            }
        }
        if ((this.access & 4096) != 0 && ((this.cw.version & 65535) < 49 || (this.access & 262144) != 0)) {
            byteVector.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.access & 131072) != 0) {
            byteVector.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
        }
        if (this.signature != null) {
            byteVector.putShort(this.cw.newUTF8(com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE)).putInt(2).putShort(this.cw.newUTF8(this.signature));
        }
        if (this.methodParameters != null) {
            byteVector.putShort(this.cw.newUTF8("MethodParameters"));
            byteVector.putInt(this.methodParameters.length + 1).putByte(this.methodParametersCount);
            byteVector.putByteArray(this.methodParameters.data, 0, this.methodParameters.length);
        }
        if (this.annd != null) {
            byteVector.putShort(this.cw.newUTF8("AnnotationDefault"));
            byteVector.putInt(this.annd.length);
            byteVector.putByteArray(this.annd.data, 0, this.annd.length);
        }
        if (this.anns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(byteVector);
        }
        if (this.ianns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(byteVector);
        }
        if (this.tanns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.tanns.put(byteVector);
        }
        if (this.itanns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.itanns.put(byteVector);
        }
        if (this.panns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.put(this.panns, this.synthetics, byteVector);
        }
        if (this.ipanns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.put(this.ipanns, this.synthetics, byteVector);
        }
        if (this.attrs != null) {
            this.attrs.put(this.cw, null, 0, -1, -1, byteVector);
        }
    }

    private void resizeInstructions() {
        int unsignedShort;
        int unsignedShort2;
        byte[] bArr = this.code.data;
        int[] iArr = new int[0];
        int[] iArr2 = new int[0];
        boolean[] zArr = new boolean[this.code.length];
        int i2 = 3;
        do {
            if (i2 == 3) {
                i2 = 2;
            }
            int i3 = 0;
            while (i3 < bArr.length) {
                int i4 = bArr[i3] & 255;
                int i5 = 0;
                switch (ClassWriter.TYPE[i4]) {
                    case 0:
                    case 4:
                        i3++;
                        break;
                    case 1:
                    case 3:
                    case 11:
                        i3 += 2;
                        break;
                    case 2:
                    case 5:
                    case 6:
                    case 12:
                    case 13:
                        i3 += 3;
                        break;
                    case 7:
                    case 8:
                        i3 += 5;
                        break;
                    case 9:
                        if (i4 > 201) {
                            i4 = i4 < 218 ? i4 - 49 : i4 - 20;
                            unsignedShort2 = i3 + readUnsignedShort(bArr, i3 + 1);
                        } else {
                            unsignedShort2 = i3 + readShort(bArr, i3 + 1);
                        }
                        int newOffset = getNewOffset(iArr, iArr2, i3, unsignedShort2);
                        if ((newOffset < -32768 || newOffset > 32767) && !zArr[i3]) {
                            if (i4 == 167 || i4 == 168) {
                                i5 = 2;
                            } else {
                                i5 = 5;
                            }
                            zArr[i3] = true;
                        }
                        i3 += 3;
                        break;
                    case 10:
                        i3 += 5;
                        break;
                    case 14:
                        if (i2 == 1) {
                            i5 = -(getNewOffset(iArr, iArr2, 0, i3) & 3);
                        } else if (!zArr[i3]) {
                            i5 = i3 & 3;
                            zArr[i3] = true;
                        }
                        int i6 = (i3 + 4) - (i3 & 3);
                        i3 = i6 + (4 * ((readInt(bArr, i6 + 8) - readInt(bArr, i6 + 4)) + 1)) + 12;
                        break;
                    case 15:
                        if (i2 == 1) {
                            i5 = -(getNewOffset(iArr, iArr2, 0, i3) & 3);
                        } else if (!zArr[i3]) {
                            i5 = i3 & 3;
                            zArr[i3] = true;
                        }
                        int i7 = (i3 + 4) - (i3 & 3);
                        i3 = i7 + (8 * readInt(bArr, i7 + 4)) + 8;
                        break;
                    case 16:
                    default:
                        i3 += 4;
                        break;
                    case 17:
                        if ((bArr[i3 + 1] & 255) == 132) {
                            i3 += 6;
                            break;
                        } else {
                            i3 += 4;
                            break;
                        }
                }
                if (i5 != 0) {
                    int[] iArr3 = new int[iArr.length + 1];
                    int[] iArr4 = new int[iArr2.length + 1];
                    System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                    System.arraycopy(iArr2, 0, iArr4, 0, iArr2.length);
                    iArr3[iArr.length] = i3;
                    iArr4[iArr2.length] = i5;
                    iArr = iArr3;
                    iArr2 = iArr4;
                    if (i5 > 0) {
                        i2 = 3;
                    }
                }
            }
            if (i2 < 3) {
                i2--;
            }
        } while (i2 != 0);
        ByteVector byteVector = new ByteVector(this.code.length);
        int i8 = 0;
        while (i8 < this.code.length) {
            int i9 = bArr[i8] & 255;
            switch (ClassWriter.TYPE[i9]) {
                case 0:
                case 4:
                    byteVector.putByte(i9);
                    i8++;
                    break;
                case 1:
                case 3:
                case 11:
                    byteVector.putByteArray(bArr, i8, 2);
                    i8 += 2;
                    break;
                case 2:
                case 5:
                case 6:
                case 12:
                case 13:
                    byteVector.putByteArray(bArr, i8, 3);
                    i8 += 3;
                    break;
                case 7:
                case 8:
                    byteVector.putByteArray(bArr, i8, 5);
                    i8 += 5;
                    break;
                case 9:
                    if (i9 > 201) {
                        i9 = i9 < 218 ? i9 - 49 : i9 - 20;
                        unsignedShort = i8 + readUnsignedShort(bArr, i8 + 1);
                    } else {
                        unsignedShort = i8 + readShort(bArr, i8 + 1);
                    }
                    int newOffset2 = getNewOffset(iArr, iArr2, i8, unsignedShort);
                    if (zArr[i8]) {
                        if (i9 == 167) {
                            byteVector.putByte(200);
                        } else if (i9 == 168) {
                            byteVector.putByte(201);
                        } else {
                            byteVector.putByte(i9 <= 166 ? ((i9 + 1) ^ 1) - 1 : i9 ^ 1);
                            byteVector.putShort(8);
                            byteVector.putByte(200);
                            newOffset2 -= 3;
                        }
                        byteVector.putInt(newOffset2);
                    } else {
                        byteVector.putByte(i9);
                        byteVector.putShort(newOffset2);
                    }
                    i8 += 3;
                    break;
                case 10:
                    int newOffset3 = getNewOffset(iArr, iArr2, i8, i8 + readInt(bArr, i8 + 1));
                    byteVector.putByte(i9);
                    byteVector.putInt(newOffset3);
                    i8 += 5;
                    break;
                case 14:
                    int i10 = i8;
                    int i11 = (i8 + 4) - (i10 & 3);
                    byteVector.putByte(170);
                    byteVector.putByteArray(null, 0, (4 - (byteVector.length % 4)) % 4);
                    int i12 = i11 + 4;
                    byteVector.putInt(getNewOffset(iArr, iArr2, i10, i10 + readInt(bArr, i11)));
                    int i13 = readInt(bArr, i12);
                    int i14 = i12 + 4;
                    byteVector.putInt(i13);
                    i8 = i14 + 4;
                    byteVector.putInt(readInt(bArr, i8 - 4));
                    for (int i15 = (readInt(bArr, i14) - i13) + 1; i15 > 0; i15--) {
                        int i16 = i10 + readInt(bArr, i8);
                        i8 += 4;
                        byteVector.putInt(getNewOffset(iArr, iArr2, i10, i16));
                    }
                    break;
                case 15:
                    int i17 = i8;
                    int i18 = (i8 + 4) - (i17 & 3);
                    byteVector.putByte(171);
                    byteVector.putByteArray(null, 0, (4 - (byteVector.length % 4)) % 4);
                    int i19 = i18 + 4;
                    byteVector.putInt(getNewOffset(iArr, iArr2, i17, i17 + readInt(bArr, i18)));
                    int i20 = readInt(bArr, i19);
                    i8 = i19 + 4;
                    byteVector.putInt(i20);
                    while (i20 > 0) {
                        byteVector.putInt(readInt(bArr, i8));
                        int i21 = i8 + 4;
                        int i22 = i17 + readInt(bArr, i21);
                        i8 = i21 + 4;
                        byteVector.putInt(getNewOffset(iArr, iArr2, i17, i22));
                        i20--;
                    }
                    break;
                case 16:
                default:
                    byteVector.putByteArray(bArr, i8, 4);
                    i8 += 4;
                    break;
                case 17:
                    if ((bArr[i8 + 1] & 255) == 132) {
                        byteVector.putByteArray(bArr, i8, 6);
                        i8 += 6;
                        break;
                    } else {
                        byteVector.putByteArray(bArr, i8, 4);
                        i8 += 4;
                        break;
                    }
            }
        }
        if (this.compute == 0) {
            Label label = this.labels;
            while (true) {
                Label label2 = label;
                if (label2 != null) {
                    int i23 = label2.position - 3;
                    if (i23 >= 0 && zArr[i23]) {
                        label2.status |= 16;
                    }
                    getNewOffset(iArr, iArr2, label2);
                    label = label2.successor;
                } else {
                    for (int i24 = 0; i24 < this.cw.typeTable.length; i24++) {
                        Item item = this.cw.typeTable[i24];
                        if (item != null && item.type == 31) {
                            item.intVal = getNewOffset(iArr, iArr2, 0, item.intVal);
                        }
                    }
                }
            }
        } else if (this.frameCount > 0) {
            this.cw.invalidFrames = true;
        }
        Handler handler = this.firstHandler;
        while (true) {
            Handler handler2 = handler;
            if (handler2 != null) {
                getNewOffset(iArr, iArr2, handler2.start);
                getNewOffset(iArr, iArr2, handler2.end);
                getNewOffset(iArr, iArr2, handler2.handler);
                handler = handler2.next;
            } else {
                int i25 = 0;
                while (i25 < 2) {
                    ByteVector byteVector2 = i25 == 0 ? this.localVar : this.localVarType;
                    if (byteVector2 != null) {
                        byte[] bArr2 = byteVector2.data;
                        for (int i26 = 0; i26 < byteVector2.length; i26 += 10) {
                            int unsignedShort3 = readUnsignedShort(bArr2, i26);
                            int newOffset4 = getNewOffset(iArr, iArr2, 0, unsignedShort3);
                            writeShort(bArr2, i26, newOffset4);
                            writeShort(bArr2, i26 + 2, getNewOffset(iArr, iArr2, 0, unsignedShort3 + readUnsignedShort(bArr2, i26 + 2)) - newOffset4);
                        }
                    }
                    i25++;
                }
                if (this.lineNumber != null) {
                    byte[] bArr3 = this.lineNumber.data;
                    for (int i27 = 0; i27 < this.lineNumber.length; i27 += 4) {
                        writeShort(bArr3, i27, getNewOffset(iArr, iArr2, 0, readUnsignedShort(bArr3, i27)));
                    }
                }
                Attribute attribute = this.cattrs;
                while (true) {
                    Attribute attribute2 = attribute;
                    if (attribute2 != null) {
                        Label[] labels = attribute2.getLabels();
                        if (labels != null) {
                            for (int length = labels.length - 1; length >= 0; length--) {
                                getNewOffset(iArr, iArr2, labels[length]);
                            }
                        }
                        attribute = attribute2.next;
                    } else {
                        this.code = byteVector;
                        return;
                    }
                }
            }
        }
    }

    static int readUnsignedShort(byte[] bArr, int i2) {
        return ((bArr[i2] & 255) << 8) | (bArr[i2 + 1] & 255);
    }

    static short readShort(byte[] bArr, int i2) {
        return (short) (((bArr[i2] & 255) << 8) | (bArr[i2 + 1] & 255));
    }

    static int readInt(byte[] bArr, int i2) {
        return ((bArr[i2] & 255) << 24) | ((bArr[i2 + 1] & 255) << 16) | ((bArr[i2 + 2] & 255) << 8) | (bArr[i2 + 3] & 255);
    }

    static void writeShort(byte[] bArr, int i2, int i3) {
        bArr[i2] = (byte) (i3 >>> 8);
        bArr[i2 + 1] = (byte) i3;
    }

    static int getNewOffset(int[] iArr, int[] iArr2, int i2, int i3) {
        int i4 = i3 - i2;
        for (int i5 = 0; i5 < iArr.length; i5++) {
            if (i2 < iArr[i5] && iArr[i5] <= i3) {
                i4 += iArr2[i5];
            } else if (i3 < iArr[i5] && iArr[i5] <= i2) {
                i4 -= iArr2[i5];
            }
        }
        return i4;
    }

    static void getNewOffset(int[] iArr, int[] iArr2, Label label) {
        if ((label.status & 4) == 0) {
            label.position = getNewOffset(iArr, iArr2, 0, label.position);
            label.status |= 4;
        }
    }
}
