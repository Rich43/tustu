package jdk.internal.org.objectweb.asm.tree.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LookupSwitchInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.TableSwitchInsnNode;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/Analyzer.class */
public class Analyzer<V extends Value> implements Opcodes {
    private final Interpreter<V> interpreter;

    /* renamed from: n, reason: collision with root package name */
    private int f12862n;
    private InsnList insns;
    private List<TryCatchBlockNode>[] handlers;
    private Frame<V>[] frames;
    private Subroutine[] subroutines;
    private boolean[] queued;
    private int[] queue;
    private int top;

    public Analyzer(Interpreter<V> interpreter) {
        this.interpreter = interpreter;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Frame<V>[] analyze(String str, MethodNode methodNode) throws IndexOutOfBoundsException, AnalyzerException {
        Type objectType;
        if ((methodNode.access & 1280) != 0) {
            this.frames = new Frame[0];
            return this.frames;
        }
        this.f12862n = methodNode.instructions.size();
        this.insns = methodNode.instructions;
        this.handlers = new List[this.f12862n];
        this.frames = new Frame[this.f12862n];
        this.subroutines = new Subroutine[this.f12862n];
        this.queued = new boolean[this.f12862n];
        this.queue = new int[this.f12862n];
        this.top = 0;
        for (int i2 = 0; i2 < methodNode.tryCatchBlocks.size(); i2++) {
            TryCatchBlockNode tryCatchBlockNode = methodNode.tryCatchBlocks.get(i2);
            int iIndexOf = this.insns.indexOf(tryCatchBlockNode.start);
            int iIndexOf2 = this.insns.indexOf(tryCatchBlockNode.end);
            for (int i3 = iIndexOf; i3 < iIndexOf2; i3++) {
                List<TryCatchBlockNode> arrayList = this.handlers[i3];
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    this.handlers[i3] = arrayList;
                }
                arrayList.add(tryCatchBlockNode);
            }
        }
        Subroutine subroutine = new Subroutine(null, methodNode.maxLocals, null);
        List<AbstractInsnNode> arrayList2 = new ArrayList<>();
        HashMap map = new HashMap();
        findSubroutine(0, subroutine, arrayList2);
        while (!arrayList2.isEmpty()) {
            JumpInsnNode jumpInsnNode = (JumpInsnNode) arrayList2.remove(0);
            Subroutine subroutine2 = (Subroutine) map.get(jumpInsnNode.label);
            if (subroutine2 == null) {
                Subroutine subroutine3 = new Subroutine(jumpInsnNode.label, methodNode.maxLocals, jumpInsnNode);
                map.put(jumpInsnNode.label, subroutine3);
                findSubroutine(this.insns.indexOf(jumpInsnNode.label), subroutine3, arrayList2);
            } else {
                subroutine2.callers.add(jumpInsnNode);
            }
        }
        for (int i4 = 0; i4 < this.f12862n; i4++) {
            if (this.subroutines[i4] != null && this.subroutines[i4].start == null) {
                this.subroutines[i4] = null;
            }
        }
        Frame<V> frameNewFrame = newFrame(methodNode.maxLocals, methodNode.maxStack);
        Frame<V> frameNewFrame2 = newFrame(methodNode.maxLocals, methodNode.maxStack);
        frameNewFrame.setReturn(this.interpreter.newValue(Type.getReturnType(methodNode.desc)));
        Type[] argumentTypes = Type.getArgumentTypes(methodNode.desc);
        int i5 = 0;
        if ((methodNode.access & 8) == 0) {
            i5 = 0 + 1;
            frameNewFrame.setLocal(0, this.interpreter.newValue(Type.getObjectType(str)));
        }
        for (int i6 = 0; i6 < argumentTypes.length; i6++) {
            int i7 = i5;
            i5++;
            frameNewFrame.setLocal(i7, this.interpreter.newValue(argumentTypes[i6]));
            if (argumentTypes[i6].getSize() == 2) {
                i5++;
                frameNewFrame.setLocal(i5, this.interpreter.newValue(null));
            }
        }
        while (i5 < methodNode.maxLocals) {
            int i8 = i5;
            i5++;
            frameNewFrame.setLocal(i8, this.interpreter.newValue(null));
        }
        merge(0, frameNewFrame, null);
        init(str, methodNode);
        while (this.top > 0) {
            int[] iArr = this.queue;
            int i9 = this.top - 1;
            this.top = i9;
            int i10 = iArr[i9];
            Frame<V> frame = this.frames[i10];
            Subroutine subroutineCopy = this.subroutines[i10];
            this.queued[i10] = false;
            AbstractInsnNode abstractInsnNode = null;
            try {
                abstractInsnNode = methodNode.instructions.get(i10);
                int opcode = abstractInsnNode.getOpcode();
                int type = abstractInsnNode.getType();
                if (type == 8 || type == 15 || type == 14) {
                    merge(i10 + 1, frame, subroutineCopy);
                    newControlFlowEdge(i10, i10 + 1);
                } else {
                    frameNewFrame.init(frame).execute(abstractInsnNode, this.interpreter);
                    subroutineCopy = subroutineCopy == null ? null : subroutineCopy.copy();
                    if (abstractInsnNode instanceof JumpInsnNode) {
                        JumpInsnNode jumpInsnNode2 = (JumpInsnNode) abstractInsnNode;
                        if (opcode != 167 && opcode != 168) {
                            merge(i10 + 1, frameNewFrame, subroutineCopy);
                            newControlFlowEdge(i10, i10 + 1);
                        }
                        int iIndexOf3 = this.insns.indexOf(jumpInsnNode2.label);
                        if (opcode == 168) {
                            merge(iIndexOf3, frameNewFrame, new Subroutine(jumpInsnNode2.label, methodNode.maxLocals, jumpInsnNode2));
                        } else {
                            merge(iIndexOf3, frameNewFrame, subroutineCopy);
                        }
                        newControlFlowEdge(i10, iIndexOf3);
                    } else if (abstractInsnNode instanceof LookupSwitchInsnNode) {
                        LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode) abstractInsnNode;
                        int iIndexOf4 = this.insns.indexOf(lookupSwitchInsnNode.dflt);
                        merge(iIndexOf4, frameNewFrame, subroutineCopy);
                        newControlFlowEdge(i10, iIndexOf4);
                        for (int i11 = 0; i11 < lookupSwitchInsnNode.labels.size(); i11++) {
                            int iIndexOf5 = this.insns.indexOf(lookupSwitchInsnNode.labels.get(i11));
                            merge(iIndexOf5, frameNewFrame, subroutineCopy);
                            newControlFlowEdge(i10, iIndexOf5);
                        }
                    } else if (abstractInsnNode instanceof TableSwitchInsnNode) {
                        TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode) abstractInsnNode;
                        int iIndexOf6 = this.insns.indexOf(tableSwitchInsnNode.dflt);
                        merge(iIndexOf6, frameNewFrame, subroutineCopy);
                        newControlFlowEdge(i10, iIndexOf6);
                        for (int i12 = 0; i12 < tableSwitchInsnNode.labels.size(); i12++) {
                            int iIndexOf7 = this.insns.indexOf(tableSwitchInsnNode.labels.get(i12));
                            merge(iIndexOf7, frameNewFrame, subroutineCopy);
                            newControlFlowEdge(i10, iIndexOf7);
                        }
                    } else if (opcode == 169) {
                        if (subroutineCopy == null) {
                            throw new AnalyzerException(abstractInsnNode, "RET instruction outside of a sub routine");
                        }
                        for (int i13 = 0; i13 < subroutineCopy.callers.size(); i13++) {
                            int iIndexOf8 = this.insns.indexOf(subroutineCopy.callers.get(i13));
                            if (this.frames[iIndexOf8] != null) {
                                merge(iIndexOf8 + 1, this.frames[iIndexOf8], frameNewFrame, this.subroutines[iIndexOf8], subroutineCopy.access);
                                newControlFlowEdge(i10, iIndexOf8 + 1);
                            }
                        }
                    } else if (opcode != 191 && (opcode < 172 || opcode > 177)) {
                        if (subroutineCopy != null) {
                            if (abstractInsnNode instanceof VarInsnNode) {
                                int i14 = ((VarInsnNode) abstractInsnNode).var;
                                subroutineCopy.access[i14] = true;
                                if (opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57) {
                                    subroutineCopy.access[i14 + 1] = true;
                                }
                            } else if (abstractInsnNode instanceof IincInsnNode) {
                                subroutineCopy.access[((IincInsnNode) abstractInsnNode).var] = true;
                            }
                        }
                        merge(i10 + 1, frameNewFrame, subroutineCopy);
                        newControlFlowEdge(i10, i10 + 1);
                    }
                }
                List<TryCatchBlockNode> list = this.handlers[i10];
                if (list != null) {
                    for (int i15 = 0; i15 < list.size(); i15++) {
                        TryCatchBlockNode tryCatchBlockNode2 = list.get(i15);
                        if (tryCatchBlockNode2.type == null) {
                            objectType = Type.getObjectType("java/lang/Throwable");
                        } else {
                            objectType = Type.getObjectType(tryCatchBlockNode2.type);
                        }
                        int iIndexOf9 = this.insns.indexOf(tryCatchBlockNode2.handler);
                        if (newControlFlowExceptionEdge(i10, tryCatchBlockNode2)) {
                            frameNewFrame2.init(frame);
                            frameNewFrame2.clearStack();
                            frameNewFrame2.push(this.interpreter.newValue(objectType));
                            merge(iIndexOf9, frameNewFrame2, subroutineCopy);
                        }
                    }
                }
            } catch (AnalyzerException e2) {
                throw new AnalyzerException(e2.node, "Error at instruction " + i10 + ": " + e2.getMessage(), e2);
            } catch (Exception e3) {
                throw new AnalyzerException(abstractInsnNode, "Error at instruction " + i10 + ": " + e3.getMessage(), e3);
            }
        }
        return this.frames;
    }

    private void findSubroutine(int i2, Subroutine subroutine, List<AbstractInsnNode> list) throws AnalyzerException {
        while (i2 >= 0 && i2 < this.f12862n) {
            if (this.subroutines[i2] != null) {
                return;
            }
            this.subroutines[i2] = subroutine.copy();
            AbstractInsnNode abstractInsnNode = this.insns.get(i2);
            if (abstractInsnNode instanceof JumpInsnNode) {
                if (abstractInsnNode.getOpcode() == 168) {
                    list.add(abstractInsnNode);
                } else {
                    findSubroutine(this.insns.indexOf(((JumpInsnNode) abstractInsnNode).label), subroutine, list);
                }
            } else if (abstractInsnNode instanceof TableSwitchInsnNode) {
                TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode) abstractInsnNode;
                findSubroutine(this.insns.indexOf(tableSwitchInsnNode.dflt), subroutine, list);
                for (int size = tableSwitchInsnNode.labels.size() - 1; size >= 0; size--) {
                    findSubroutine(this.insns.indexOf(tableSwitchInsnNode.labels.get(size)), subroutine, list);
                }
            } else if (abstractInsnNode instanceof LookupSwitchInsnNode) {
                LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode) abstractInsnNode;
                findSubroutine(this.insns.indexOf(lookupSwitchInsnNode.dflt), subroutine, list);
                for (int size2 = lookupSwitchInsnNode.labels.size() - 1; size2 >= 0; size2--) {
                    findSubroutine(this.insns.indexOf(lookupSwitchInsnNode.labels.get(size2)), subroutine, list);
                }
            }
            List<TryCatchBlockNode> list2 = this.handlers[i2];
            if (list2 != null) {
                for (int i3 = 0; i3 < list2.size(); i3++) {
                    findSubroutine(this.insns.indexOf(list2.get(i3).handler), subroutine, list);
                }
            }
            switch (abstractInsnNode.getOpcode()) {
                case 167:
                case 169:
                case 170:
                case 171:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 191:
                    return;
                case 168:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 185:
                case 186:
                case 187:
                case 188:
                case 189:
                case 190:
                default:
                    i2++;
            }
        }
        throw new AnalyzerException(null, "Execution can fall off end of the code");
    }

    public Frame<V>[] getFrames() {
        return this.frames;
    }

    public List<TryCatchBlockNode> getHandlers(int i2) {
        return this.handlers[i2];
    }

    protected void init(String str, MethodNode methodNode) throws AnalyzerException {
    }

    protected Frame<V> newFrame(int i2, int i3) {
        return new Frame<>(i2, i3);
    }

    protected Frame<V> newFrame(Frame<? extends V> frame) {
        return new Frame<>(frame);
    }

    protected void newControlFlowEdge(int i2, int i3) {
    }

    protected boolean newControlFlowExceptionEdge(int i2, int i3) {
        return true;
    }

    protected boolean newControlFlowExceptionEdge(int i2, TryCatchBlockNode tryCatchBlockNode) {
        return newControlFlowExceptionEdge(i2, this.insns.indexOf(tryCatchBlockNode.handler));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void merge(int i2, Frame<V> frame, Subroutine subroutine) throws AnalyzerException {
        boolean zMerge;
        Frame<V> frame2 = this.frames[i2];
        Subroutine subroutine2 = this.subroutines[i2];
        if (frame2 == null) {
            this.frames[i2] = newFrame(frame);
            zMerge = true;
        } else {
            zMerge = frame2.merge(frame, this.interpreter);
        }
        if (subroutine2 == null) {
            if (subroutine != null) {
                this.subroutines[i2] = subroutine.copy();
                zMerge = true;
            }
        } else if (subroutine != null) {
            zMerge |= subroutine2.merge(subroutine);
        }
        if (zMerge && !this.queued[i2]) {
            this.queued[i2] = true;
            int[] iArr = this.queue;
            int i3 = this.top;
            this.top = i3 + 1;
            iArr[i3] = i2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void merge(int i2, Frame<V> frame, Frame<V> frame2, Subroutine subroutine, boolean[] zArr) throws AnalyzerException {
        boolean zMerge;
        Frame<V> frame3 = this.frames[i2];
        Subroutine subroutine2 = this.subroutines[i2];
        frame2.merge(frame, zArr);
        if (frame3 == null) {
            this.frames[i2] = newFrame(frame2);
            zMerge = true;
        } else {
            zMerge = frame3.merge(frame2, this.interpreter);
        }
        if (subroutine2 != null && subroutine != null) {
            zMerge |= subroutine2.merge(subroutine);
        }
        if (zMerge && !this.queued[i2]) {
            this.queued[i2] = true;
            int[] iArr = this.queue;
            int i3 = this.top;
            this.top = i3 + 1;
            iArr[i3] = i2;
        }
    }
}
