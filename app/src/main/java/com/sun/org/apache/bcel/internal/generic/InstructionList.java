package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/InstructionList.class */
public class InstructionList implements Serializable {
    private InstructionHandle start = null;
    private InstructionHandle end = null;
    private int length = 0;
    private int[] byte_positions;
    private ArrayList observers;

    public InstructionList() {
    }

    public InstructionList(Instruction i2) {
        append(i2);
    }

    public InstructionList(BranchInstruction i2) {
        append(i2);
    }

    public InstructionList(CompoundInstruction c2) {
        append(c2.getInstructionList());
    }

    public boolean isEmpty() {
        return this.start == null;
    }

    public static InstructionHandle findHandle(InstructionHandle[] ihs, int[] pos, int count, int target) {
        int l2 = 0;
        int r2 = count - 1;
        do {
            int i2 = (l2 + r2) / 2;
            int j2 = pos[i2];
            if (j2 == target) {
                return ihs[i2];
            }
            if (target < j2) {
                r2 = i2 - 1;
            } else {
                l2 = i2 + 1;
            }
        } while (l2 <= r2);
        return null;
    }

    public InstructionHandle findHandle(int pos) {
        InstructionHandle[] ihs = getInstructionHandles();
        return findHandle(ihs, this.byte_positions, this.length, pos);
    }

    public InstructionList(byte[] code) throws Exception {
        InstructionHandle ih;
        ByteSequence bytes = new ByteSequence(code);
        InstructionHandle[] ihs = new InstructionHandle[code.length];
        int[] pos = new int[code.length];
        int count = 0;
        while (bytes.available() > 0) {
            try {
                int off = bytes.getIndex();
                pos[count] = off;
                Instruction i2 = Instruction.readInstruction(bytes);
                if (i2 instanceof BranchInstruction) {
                    ih = append((BranchInstruction) i2);
                } else {
                    ih = append(i2);
                }
                ih.setPosition(off);
                ihs[count] = ih;
                count++;
            } catch (IOException e2) {
                throw new ClassGenException(e2.toString());
            }
        }
        this.byte_positions = new int[count];
        System.arraycopy(pos, 0, this.byte_positions, 0, count);
        for (int i3 = 0; i3 < count; i3++) {
            if (ihs[i3] instanceof BranchHandle) {
                BranchInstruction bi2 = (BranchInstruction) ihs[i3].instruction;
                int target = bi2.position + bi2.getIndex();
                InstructionHandle ih2 = findHandle(ihs, pos, count, target);
                if (ih2 == null) {
                    throw new ClassGenException("Couldn't find target for branch: " + ((Object) bi2));
                }
                bi2.setTarget(ih2);
                if (bi2 instanceof Select) {
                    Select s2 = (Select) bi2;
                    int[] indices = s2.getIndices();
                    for (int j2 = 0; j2 < indices.length; j2++) {
                        int target2 = bi2.position + indices[j2];
                        InstructionHandle ih3 = findHandle(ihs, pos, count, target2);
                        if (ih3 == null) {
                            throw new ClassGenException("Couldn't find target for switch: " + ((Object) bi2));
                        }
                        s2.setTarget(j2, ih3);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    public InstructionHandle append(InstructionHandle ih, InstructionList il) {
        if (il == null) {
            throw new ClassGenException("Appending null InstructionList");
        }
        if (il.isEmpty()) {
            return ih;
        }
        InstructionHandle next = ih.next;
        InstructionHandle ret = il.start;
        ih.next = il.start;
        il.start.prev = ih;
        il.end.next = next;
        if (next != null) {
            next.prev = il.end;
        } else {
            this.end = il.end;
        }
        this.length += il.length;
        il.clear();
        return ret;
    }

    public InstructionHandle append(Instruction i2, InstructionList il) {
        InstructionHandle ih = findInstruction2(i2);
        if (ih == null) {
            throw new ClassGenException("Instruction " + ((Object) i2) + " is not contained in this list.");
        }
        return append(ih, il);
    }

    public InstructionHandle append(InstructionList il) {
        if (il == null) {
            throw new ClassGenException("Appending null InstructionList");
        }
        if (il.isEmpty()) {
            return null;
        }
        if (isEmpty()) {
            this.start = il.start;
            this.end = il.end;
            this.length = il.length;
            il.clear();
            return this.start;
        }
        return append(this.end, il);
    }

    private void append(InstructionHandle ih) {
        if (isEmpty()) {
            this.end = ih;
            this.start = ih;
            ih.prev = null;
            ih.next = null;
        } else {
            this.end.next = ih;
            ih.prev = this.end;
            ih.next = null;
            this.end = ih;
        }
        this.length++;
    }

    public InstructionHandle append(Instruction i2) {
        InstructionHandle ih = InstructionHandle.getInstructionHandle(i2);
        append(ih);
        return ih;
    }

    public BranchHandle append(BranchInstruction i2) {
        BranchHandle ih = BranchHandle.getBranchHandle(i2);
        append(ih);
        return ih;
    }

    public InstructionHandle append(Instruction i2, Instruction j2) {
        return append(i2, new InstructionList(j2));
    }

    public InstructionHandle append(Instruction i2, CompoundInstruction c2) {
        return append(i2, c2.getInstructionList());
    }

    public InstructionHandle append(CompoundInstruction c2) {
        return append(c2.getInstructionList());
    }

    public InstructionHandle append(InstructionHandle ih, CompoundInstruction c2) {
        return append(ih, c2.getInstructionList());
    }

    public InstructionHandle append(InstructionHandle ih, Instruction i2) {
        return append(ih, new InstructionList(i2));
    }

    public BranchHandle append(InstructionHandle ih, BranchInstruction i2) {
        BranchHandle bh2 = BranchHandle.getBranchHandle(i2);
        InstructionList il = new InstructionList();
        il.append(bh2);
        append(ih, il);
        return bh2;
    }

    public InstructionHandle insert(InstructionHandle ih, InstructionList il) {
        if (il == null) {
            throw new ClassGenException("Inserting null InstructionList");
        }
        if (il.isEmpty()) {
            return ih;
        }
        InstructionHandle prev = ih.prev;
        InstructionHandle ret = il.start;
        ih.prev = il.end;
        il.end.next = ih;
        il.start.prev = prev;
        if (prev != null) {
            prev.next = il.start;
        } else {
            this.start = il.start;
        }
        this.length += il.length;
        il.clear();
        return ret;
    }

    public InstructionHandle insert(InstructionList il) {
        if (isEmpty()) {
            append(il);
            return this.start;
        }
        return insert(this.start, il);
    }

    private void insert(InstructionHandle ih) {
        if (isEmpty()) {
            this.end = ih;
            this.start = ih;
            ih.prev = null;
            ih.next = null;
        } else {
            this.start.prev = ih;
            ih.next = this.start;
            ih.prev = null;
            this.start = ih;
        }
        this.length++;
    }

    public InstructionHandle insert(Instruction i2, InstructionList il) {
        InstructionHandle ih = findInstruction1(i2);
        if (ih == null) {
            throw new ClassGenException("Instruction " + ((Object) i2) + " is not contained in this list.");
        }
        return insert(ih, il);
    }

    public InstructionHandle insert(Instruction i2) {
        InstructionHandle ih = InstructionHandle.getInstructionHandle(i2);
        insert(ih);
        return ih;
    }

    public BranchHandle insert(BranchInstruction i2) {
        BranchHandle ih = BranchHandle.getBranchHandle(i2);
        insert(ih);
        return ih;
    }

    public InstructionHandle insert(Instruction i2, Instruction j2) {
        return insert(i2, new InstructionList(j2));
    }

    public InstructionHandle insert(Instruction i2, CompoundInstruction c2) {
        return insert(i2, c2.getInstructionList());
    }

    public InstructionHandle insert(CompoundInstruction c2) {
        return insert(c2.getInstructionList());
    }

    public InstructionHandle insert(InstructionHandle ih, Instruction i2) {
        return insert(ih, new InstructionList(i2));
    }

    public InstructionHandle insert(InstructionHandle ih, CompoundInstruction c2) {
        return insert(ih, c2.getInstructionList());
    }

    public BranchHandle insert(InstructionHandle ih, BranchInstruction i2) {
        BranchHandle bh2 = BranchHandle.getBranchHandle(i2);
        InstructionList il = new InstructionList();
        il.append(bh2);
        insert(ih, il);
        return bh2;
    }

    public void move(InstructionHandle start, InstructionHandle end, InstructionHandle target) {
        if (start == null || end == null) {
            throw new ClassGenException("Invalid null handle: From " + ((Object) start) + " to " + ((Object) end));
        }
        if (target == start || target == end) {
            throw new ClassGenException("Invalid range: From " + ((Object) start) + " to " + ((Object) end) + " contains target " + ((Object) target));
        }
        InstructionHandle instructionHandle = start;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih != end.next) {
                if (ih == null) {
                    throw new ClassGenException("Invalid range: From " + ((Object) start) + " to " + ((Object) end));
                }
                if (ih != target) {
                    instructionHandle = ih.next;
                } else {
                    throw new ClassGenException("Invalid range: From " + ((Object) start) + " to " + ((Object) end) + " contains target " + ((Object) target));
                }
            } else {
                InstructionHandle prev = start.prev;
                InstructionHandle next = end.next;
                if (prev != null) {
                    prev.next = next;
                } else {
                    this.start = next;
                }
                if (next != null) {
                    next.prev = prev;
                } else {
                    this.end = prev;
                }
                end.next = null;
                start.prev = null;
                if (target == null) {
                    end.next = this.start;
                    this.start = start;
                    return;
                }
                InstructionHandle next2 = target.next;
                target.next = start;
                start.prev = target;
                end.next = next2;
                if (next2 != null) {
                    next2.prev = end;
                    return;
                }
                return;
            }
        }
    }

    public void move(InstructionHandle ih, InstructionHandle target) {
        move(ih, ih, target);
    }

    private void remove(InstructionHandle prev, InstructionHandle next) throws TargetLostException {
        InstructionHandle first;
        InstructionHandle last;
        if (prev == null && next == null) {
            InstructionHandle instructionHandle = this.start;
            last = instructionHandle;
            first = instructionHandle;
            this.end = null;
            this.start = null;
        } else {
            if (prev == null) {
                first = this.start;
                this.start = next;
            } else {
                first = prev.next;
                prev.next = next;
            }
            if (next == null) {
                last = this.end;
                this.end = prev;
            } else {
                last = next.prev;
                next.prev = prev;
            }
        }
        first.prev = null;
        last.next = null;
        ArrayList target_vec = new ArrayList();
        InstructionHandle instructionHandle2 = first;
        while (true) {
            InstructionHandle ih = instructionHandle2;
            if (ih == null) {
                break;
            }
            ih.getInstruction().dispose();
            instructionHandle2 = ih.next;
        }
        StringBuffer buf = new StringBuffer("{ ");
        InstructionHandle instructionHandle3 = first;
        while (true) {
            InstructionHandle ih2 = instructionHandle3;
            if (ih2 == null) {
                break;
            }
            InstructionHandle next2 = ih2.next;
            this.length--;
            if (ih2.hasTargeters()) {
                target_vec.add(ih2);
                buf.append(ih2.toString(true) + " ");
                ih2.prev = null;
                ih2.next = null;
            } else {
                ih2.dispose();
            }
            instructionHandle3 = next2;
        }
        buf.append("}");
        if (!target_vec.isEmpty()) {
            InstructionHandle[] targeted = new InstructionHandle[target_vec.size()];
            target_vec.toArray(targeted);
            throw new TargetLostException(targeted, buf.toString());
        }
    }

    public void delete(InstructionHandle ih) throws TargetLostException {
        remove(ih.prev, ih.next);
    }

    public void delete(Instruction i2) throws TargetLostException {
        InstructionHandle ih = findInstruction1(i2);
        if (ih == null) {
            throw new ClassGenException("Instruction " + ((Object) i2) + " is not contained in this list.");
        }
        delete(ih);
    }

    public void delete(InstructionHandle from, InstructionHandle to) throws TargetLostException {
        remove(from.prev, to.next);
    }

    public void delete(Instruction from, Instruction to) throws TargetLostException {
        InstructionHandle from_ih = findInstruction1(from);
        if (from_ih == null) {
            throw new ClassGenException("Instruction " + ((Object) from) + " is not contained in this list.");
        }
        InstructionHandle to_ih = findInstruction2(to);
        if (to_ih == null) {
            throw new ClassGenException("Instruction " + ((Object) to) + " is not contained in this list.");
        }
        delete(from_ih, to_ih);
    }

    private InstructionHandle findInstruction1(Instruction i2) {
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih != null) {
                if (ih.instruction != i2) {
                    instructionHandle = ih.next;
                } else {
                    return ih;
                }
            } else {
                return null;
            }
        }
    }

    private InstructionHandle findInstruction2(Instruction i2) {
        InstructionHandle instructionHandle = this.end;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih != null) {
                if (ih.instruction != i2) {
                    instructionHandle = ih.prev;
                } else {
                    return ih;
                }
            } else {
                return null;
            }
        }
    }

    public boolean contains(InstructionHandle i2) {
        if (i2 == null) {
            return false;
        }
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih != null) {
                if (ih != i2) {
                    instructionHandle = ih.next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public boolean contains(Instruction i2) {
        return findInstruction1(i2) != null;
    }

    public void setPositions() {
        setPositions(false);
    }

    public void setPositions(boolean check) {
        int max_additional_bytes = 0;
        int additional_bytes = 0;
        int index = 0;
        int count = 0;
        int[] pos = new int[this.length];
        if (check) {
            InstructionHandle instructionHandle = this.start;
            while (true) {
                InstructionHandle ih = instructionHandle;
                if (ih == null) {
                    break;
                }
                Instruction i2 = ih.instruction;
                if (i2 instanceof BranchInstruction) {
                    Instruction inst = ((BranchInstruction) i2).getTarget().instruction;
                    if (!contains(inst)) {
                        throw new ClassGenException("Branch target of " + Constants.OPCODE_NAMES[i2.opcode] + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) inst) + " not in instruction list");
                    }
                    if (i2 instanceof Select) {
                        InstructionHandle[] targets = ((Select) i2).getTargets();
                        for (InstructionHandle instructionHandle2 : targets) {
                            inst = instructionHandle2.instruction;
                            if (!contains(inst)) {
                                throw new ClassGenException("Branch target of " + Constants.OPCODE_NAMES[i2.opcode] + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) inst) + " not in instruction list");
                            }
                        }
                    }
                    if (!(ih instanceof BranchHandle)) {
                        throw new ClassGenException("Branch instruction " + Constants.OPCODE_NAMES[i2.opcode] + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) inst) + " not contained in BranchHandle.");
                    }
                }
                instructionHandle = ih.next;
            }
        }
        InstructionHandle instructionHandle3 = this.start;
        while (true) {
            InstructionHandle ih2 = instructionHandle3;
            if (ih2 != null) {
                Instruction i3 = ih2.instruction;
                ih2.setPosition(index);
                int i4 = count;
                count++;
                pos[i4] = index;
                switch (i3.getOpcode()) {
                    case 167:
                    case 168:
                        max_additional_bytes += 2;
                        break;
                    case 170:
                    case 171:
                        max_additional_bytes += 3;
                        break;
                }
                index += i3.getLength();
                instructionHandle3 = ih2.next;
            } else {
                InstructionHandle instructionHandle4 = this.start;
                while (true) {
                    InstructionHandle ih3 = instructionHandle4;
                    if (ih3 != null) {
                        additional_bytes += ih3.updatePosition(additional_bytes, max_additional_bytes);
                        instructionHandle4 = ih3.next;
                    } else {
                        int count2 = 0;
                        int index2 = 0;
                        InstructionHandle instructionHandle5 = this.start;
                        while (true) {
                            InstructionHandle ih4 = instructionHandle5;
                            if (ih4 != null) {
                                Instruction i5 = ih4.instruction;
                                ih4.setPosition(index2);
                                int i6 = count2;
                                count2++;
                                pos[i6] = index2;
                                index2 += i5.getLength();
                                instructionHandle5 = ih4.next;
                            } else {
                                this.byte_positions = new int[count2];
                                System.arraycopy(pos, 0, this.byte_positions, 0, count2);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public byte[] getByteCode() {
        setPositions();
        ByteArrayOutputStream b2 = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b2);
        try {
            for (InstructionHandle ih = this.start; ih != null; ih = ih.next) {
                Instruction i2 = ih.instruction;
                i2.dump(out);
            }
            return b2.toByteArray();
        } catch (IOException e2) {
            System.err.println(e2);
            return null;
        }
    }

    public Instruction[] getInstructions() {
        ByteSequence bytes = new ByteSequence(getByteCode());
        ArrayList instructions = new ArrayList();
        while (bytes.available() > 0) {
            try {
                instructions.add(Instruction.readInstruction(bytes));
            } catch (IOException e2) {
                throw new ClassGenException(e2.toString());
            }
        }
        Instruction[] result = new Instruction[instructions.size()];
        instructions.toArray(result);
        return result;
    }

    public String toString() {
        return toString(true);
    }

    public String toString(boolean verbose) {
        StringBuffer buf = new StringBuffer();
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih != null) {
                buf.append(ih.toString(verbose) + "\n");
                instructionHandle = ih.next;
            } else {
                return buf.toString();
            }
        }
    }

    public Iterator iterator() {
        return new Iterator() { // from class: com.sun.org.apache.bcel.internal.generic.InstructionList.1
            private InstructionHandle ih;

            {
                this.ih = InstructionList.this.start;
            }

            @Override // java.util.Iterator
            public Object next() {
                InstructionHandle i2 = this.ih;
                this.ih = this.ih.next;
                return i2;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.ih != null;
            }
        };
    }

    public InstructionHandle[] getInstructionHandles() {
        InstructionHandle[] ihs = new InstructionHandle[this.length];
        InstructionHandle ih = this.start;
        for (int i2 = 0; i2 < this.length; i2++) {
            ihs[i2] = ih;
            ih = ih.next;
        }
        return ihs;
    }

    public int[] getInstructionPositions() {
        return this.byte_positions;
    }

    public InstructionList copy() {
        HashMap map = new HashMap();
        InstructionList il = new InstructionList();
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih == null) {
                break;
            }
            Instruction c2 = ih.instruction.copy();
            if (c2 instanceof BranchInstruction) {
                map.put(ih, il.append((BranchInstruction) c2));
            } else {
                map.put(ih, il.append(c2));
            }
            instructionHandle = ih.next;
        }
        InstructionHandle ih2 = this.start;
        InstructionHandle instructionHandle2 = il.start;
        while (true) {
            InstructionHandle ch = instructionHandle2;
            if (ih2 != null) {
                Instruction i2 = ih2.instruction;
                Instruction c3 = ch.instruction;
                if (i2 instanceof BranchInstruction) {
                    BranchInstruction bi2 = (BranchInstruction) i2;
                    BranchInstruction bc2 = (BranchInstruction) c3;
                    InstructionHandle itarget = bi2.getTarget();
                    bc2.setTarget((InstructionHandle) map.get(itarget));
                    if (bi2 instanceof Select) {
                        InstructionHandle[] itargets = ((Select) bi2).getTargets();
                        InstructionHandle[] ctargets = ((Select) bc2).getTargets();
                        for (int j2 = 0; j2 < itargets.length; j2++) {
                            ctargets[j2] = (InstructionHandle) map.get(itargets[j2]);
                        }
                    }
                }
                ih2 = ih2.next;
                instructionHandle2 = ch.next;
            } else {
                return il;
            }
        }
    }

    public void replaceConstantPool(ConstantPoolGen old_cp, ConstantPoolGen new_cp) {
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih != null) {
                Instruction i2 = ih.instruction;
                if (i2 instanceof CPInstruction) {
                    CPInstruction ci = (CPInstruction) i2;
                    Constant c2 = old_cp.getConstant(ci.getIndex());
                    ci.setIndex(new_cp.addConstant(c2, old_cp));
                }
                instructionHandle = ih.next;
            } else {
                return;
            }
        }
    }

    private void clear() {
        this.end = null;
        this.start = null;
        this.length = 0;
    }

    public void dispose() {
        InstructionHandle instructionHandle = this.end;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih != null) {
                ih.dispose();
                instructionHandle = ih.prev;
            } else {
                clear();
                return;
            }
        }
    }

    public InstructionHandle getStart() {
        return this.start;
    }

    public InstructionHandle getEnd() {
        return this.end;
    }

    public int getLength() {
        return this.length;
    }

    public int size() {
        return this.length;
    }

    public void redirectBranches(InstructionHandle old_target, InstructionHandle new_target) {
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle ih = instructionHandle;
            if (ih != null) {
                Instruction i2 = ih.getInstruction();
                if (i2 instanceof BranchInstruction) {
                    BranchInstruction b2 = (BranchInstruction) i2;
                    InstructionHandle target = b2.getTarget();
                    if (target == old_target) {
                        b2.setTarget(new_target);
                    }
                    if (b2 instanceof Select) {
                        InstructionHandle[] targets = ((Select) b2).getTargets();
                        for (int j2 = 0; j2 < targets.length; j2++) {
                            if (targets[j2] == old_target) {
                                ((Select) b2).setTarget(j2, new_target);
                            }
                        }
                    }
                }
                instructionHandle = ih.next;
            } else {
                return;
            }
        }
    }

    public void redirectLocalVariables(LocalVariableGen[] lg, InstructionHandle old_target, InstructionHandle new_target) {
        for (int i2 = 0; i2 < lg.length; i2++) {
            InstructionHandle start = lg[i2].getStart();
            InstructionHandle end = lg[i2].getEnd();
            if (start == old_target) {
                lg[i2].setStart(new_target);
            }
            if (end == old_target) {
                lg[i2].setEnd(new_target);
            }
        }
    }

    public void redirectExceptionHandlers(CodeExceptionGen[] exceptions, InstructionHandle old_target, InstructionHandle new_target) {
        for (int i2 = 0; i2 < exceptions.length; i2++) {
            if (exceptions[i2].getStartPC() == old_target) {
                exceptions[i2].setStartPC(new_target);
            }
            if (exceptions[i2].getEndPC() == old_target) {
                exceptions[i2].setEndPC(new_target);
            }
            if (exceptions[i2].getHandlerPC() == old_target) {
                exceptions[i2].setHandlerPC(new_target);
            }
        }
    }

    public void addObserver(InstructionListObserver o2) {
        if (this.observers == null) {
            this.observers = new ArrayList();
        }
        this.observers.add(o2);
    }

    public void removeObserver(InstructionListObserver o2) {
        if (this.observers != null) {
            this.observers.remove(o2);
        }
    }

    public void update() {
        if (this.observers != null) {
            Iterator e2 = this.observers.iterator();
            while (e2.hasNext()) {
                ((InstructionListObserver) e2.next()).notify(this);
            }
        }
    }
}
