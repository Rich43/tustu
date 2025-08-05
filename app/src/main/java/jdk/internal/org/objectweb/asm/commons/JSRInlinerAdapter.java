package jdk.internal.org.objectweb.asm.commons;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/JSRInlinerAdapter.class */
public class JSRInlinerAdapter extends MethodNode implements Opcodes {
    private static final boolean LOGGING = false;
    private final Map<LabelNode, BitSet> subroutineHeads;
    private final BitSet mainSubroutine;
    final BitSet dualCitizens;

    public JSRInlinerAdapter(MethodVisitor methodVisitor, int i2, String str, String str2, String str3, String[] strArr) {
        this(Opcodes.ASM5, methodVisitor, i2, str, str2, str3, strArr);
        if (getClass() != JSRInlinerAdapter.class) {
            throw new IllegalStateException();
        }
    }

    protected JSRInlinerAdapter(int i2, MethodVisitor methodVisitor, int i3, String str, String str2, String str3, String[] strArr) {
        super(i2, i3, str, str2, str3, strArr);
        this.subroutineHeads = new HashMap();
        this.mainSubroutine = new BitSet();
        this.dualCitizens = new BitSet();
        this.mv = methodVisitor;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.MethodNode, jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i2, Label label) {
        super.visitJumpInsn(i2, label);
        LabelNode labelNode = ((JumpInsnNode) this.instructions.getLast()).label;
        if (i2 == 168 && !this.subroutineHeads.containsKey(labelNode)) {
            this.subroutineHeads.put(labelNode, new BitSet());
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.MethodNode, jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        if (!this.subroutineHeads.isEmpty()) {
            markSubroutines();
            emitCode();
        }
        if (this.mv != null) {
            accept(this.mv);
        }
    }

    private void markSubroutines() {
        BitSet bitSet = new BitSet();
        markSubroutineWalk(this.mainSubroutine, 0, bitSet);
        for (Map.Entry<LabelNode, BitSet> entry : this.subroutineHeads.entrySet()) {
            markSubroutineWalk(entry.getValue(), this.instructions.indexOf(entry.getKey()), bitSet);
        }
    }

    private void markSubroutineWalk(BitSet bitSet, int i2, BitSet bitSet2) {
        markSubroutineWalkDFS(bitSet, i2, bitSet2);
        boolean z2 = true;
        while (z2) {
            z2 = false;
            for (TryCatchBlockNode tryCatchBlockNode : this.tryCatchBlocks) {
                int iIndexOf = this.instructions.indexOf(tryCatchBlockNode.handler);
                if (!bitSet.get(iIndexOf)) {
                    int iIndexOf2 = this.instructions.indexOf(tryCatchBlockNode.start);
                    int iIndexOf3 = this.instructions.indexOf(tryCatchBlockNode.end);
                    int iNextSetBit = bitSet.nextSetBit(iIndexOf2);
                    if (iNextSetBit != -1 && iNextSetBit < iIndexOf3) {
                        markSubroutineWalkDFS(bitSet, iIndexOf, bitSet2);
                        z2 = true;
                    }
                }
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:101)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:103)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private void markSubroutineWalkDFS(java.util.BitSet r6, int r7, java.util.BitSet r8) {
        /*
            Method dump skipped, instructions count: 439
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.org.objectweb.asm.commons.JSRInlinerAdapter.markSubroutineWalkDFS(java.util.BitSet, int, java.util.BitSet):void");
    }

    private void emitCode() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new Instantiation(null, this.mainSubroutine));
        InsnList insnList = new InsnList();
        List<TryCatchBlockNode> arrayList = new ArrayList<>();
        List<LocalVariableNode> arrayList2 = new ArrayList<>();
        while (!linkedList.isEmpty()) {
            emitSubroutine((Instantiation) linkedList.removeFirst(), linkedList, insnList, arrayList, arrayList2);
        }
        this.instructions = insnList;
        this.tryCatchBlocks = arrayList;
        this.localVariables = arrayList2;
    }

    private void emitSubroutine(Instantiation instantiation, List<Instantiation> list, InsnList insnList, List<TryCatchBlockNode> list2, List<LocalVariableNode> list3) {
        LabelNode labelNode = null;
        int size = this.instructions.size();
        for (int i2 = 0; i2 < size; i2++) {
            AbstractInsnNode abstractInsnNode = this.instructions.get(i2);
            Instantiation instantiationFindOwner = instantiation.findOwner(i2);
            if (abstractInsnNode.getType() == 8) {
                LabelNode labelNodeRangeLabel = instantiation.rangeLabel((LabelNode) abstractInsnNode);
                if (labelNodeRangeLabel != labelNode) {
                    insnList.add(labelNodeRangeLabel);
                    labelNode = labelNodeRangeLabel;
                }
            } else if (instantiationFindOwner != instantiation) {
                continue;
            } else if (abstractInsnNode.getOpcode() == 169) {
                LabelNode labelNode2 = null;
                Instantiation instantiation2 = instantiation;
                while (true) {
                    Instantiation instantiation3 = instantiation2;
                    if (instantiation3 == null) {
                        break;
                    }
                    if (instantiation3.subroutine.get(i2)) {
                        labelNode2 = instantiation3.returnLabel;
                    }
                    instantiation2 = instantiation3.previous;
                }
                if (labelNode2 == null) {
                    throw new RuntimeException("Instruction #" + i2 + " is a RET not owned by any subroutine");
                }
                insnList.add(new JumpInsnNode(167, labelNode2));
            } else if (abstractInsnNode.getOpcode() == 168) {
                LabelNode labelNode3 = ((JumpInsnNode) abstractInsnNode).label;
                Instantiation instantiation4 = new Instantiation(instantiation, this.subroutineHeads.get(labelNode3));
                LabelNode labelNodeGotoLabel = instantiation4.gotoLabel(labelNode3);
                insnList.add(new InsnNode(1));
                insnList.add(new JumpInsnNode(167, labelNodeGotoLabel));
                insnList.add(instantiation4.returnLabel);
                list.add(instantiation4);
            } else {
                insnList.add(abstractInsnNode.clone(instantiation));
            }
        }
        for (TryCatchBlockNode tryCatchBlockNode : this.tryCatchBlocks) {
            LabelNode labelNodeRangeLabel2 = instantiation.rangeLabel(tryCatchBlockNode.start);
            LabelNode labelNodeRangeLabel3 = instantiation.rangeLabel(tryCatchBlockNode.end);
            if (labelNodeRangeLabel2 != labelNodeRangeLabel3) {
                LabelNode labelNodeGotoLabel2 = instantiation.gotoLabel(tryCatchBlockNode.handler);
                if (labelNodeRangeLabel2 == null || labelNodeRangeLabel3 == null || labelNodeGotoLabel2 == null) {
                    throw new RuntimeException("Internal error!");
                }
                list2.add(new TryCatchBlockNode(labelNodeRangeLabel2, labelNodeRangeLabel3, labelNodeGotoLabel2, tryCatchBlockNode.type));
            }
        }
        for (LocalVariableNode localVariableNode : this.localVariables) {
            LabelNode labelNodeRangeLabel4 = instantiation.rangeLabel(localVariableNode.start);
            LabelNode labelNodeRangeLabel5 = instantiation.rangeLabel(localVariableNode.end);
            if (labelNodeRangeLabel4 != labelNodeRangeLabel5) {
                list3.add(new LocalVariableNode(localVariableNode.name, localVariableNode.desc, localVariableNode.signature, labelNodeRangeLabel4, labelNodeRangeLabel5, localVariableNode.index));
            }
        }
    }

    private static void log(String str) {
        System.err.println(str);
    }

    /* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/JSRInlinerAdapter$Instantiation.class */
    private class Instantiation extends AbstractMap<LabelNode, LabelNode> {
        final Instantiation previous;
        public final BitSet subroutine;
        public final Map<LabelNode, LabelNode> rangeTable = new HashMap();
        public final LabelNode returnLabel;

        Instantiation(Instantiation instantiation, BitSet bitSet) {
            this.previous = instantiation;
            this.subroutine = bitSet;
            Instantiation instantiation2 = instantiation;
            while (true) {
                Instantiation instantiation3 = instantiation2;
                if (instantiation3 != null) {
                    if (instantiation3.subroutine != bitSet) {
                        instantiation2 = instantiation3.previous;
                    } else {
                        throw new RuntimeException("Recursive invocation of " + ((Object) bitSet));
                    }
                } else {
                    if (instantiation != null) {
                        this.returnLabel = new LabelNode();
                    } else {
                        this.returnLabel = null;
                    }
                    LabelNode labelNode = null;
                    int size = JSRInlinerAdapter.this.instructions.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        AbstractInsnNode abstractInsnNode = JSRInlinerAdapter.this.instructions.get(i2);
                        if (abstractInsnNode.getType() == 8) {
                            LabelNode labelNode2 = (LabelNode) abstractInsnNode;
                            labelNode = labelNode == null ? new LabelNode() : labelNode;
                            this.rangeTable.put(labelNode2, labelNode);
                        } else if (findOwner(i2) == this) {
                            labelNode = null;
                        }
                    }
                    return;
                }
            }
        }

        public Instantiation findOwner(int i2) {
            if (!this.subroutine.get(i2)) {
                return null;
            }
            if (!JSRInlinerAdapter.this.dualCitizens.get(i2)) {
                return this;
            }
            Instantiation instantiation = this;
            Instantiation instantiation2 = this.previous;
            while (true) {
                Instantiation instantiation3 = instantiation2;
                if (instantiation3 != null) {
                    if (instantiation3.subroutine.get(i2)) {
                        instantiation = instantiation3;
                    }
                    instantiation2 = instantiation3.previous;
                } else {
                    return instantiation;
                }
            }
        }

        public LabelNode gotoLabel(LabelNode labelNode) {
            return findOwner(JSRInlinerAdapter.this.instructions.indexOf(labelNode)).rangeTable.get(labelNode);
        }

        public LabelNode rangeLabel(LabelNode labelNode) {
            return this.rangeTable.get(labelNode);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<LabelNode, LabelNode>> entrySet() {
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public LabelNode get(Object obj) {
            return gotoLabel((LabelNode) obj);
        }
    }
}
