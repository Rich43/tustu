package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/InstructionHandle.class */
public class InstructionHandle implements Serializable {
    InstructionHandle next;
    InstructionHandle prev;
    Instruction instruction;
    protected int i_position = -1;
    private HashSet targeters;
    private HashMap attributes;
    private static InstructionHandle ih_list = null;

    public final InstructionHandle getNext() {
        return this.next;
    }

    public final InstructionHandle getPrev() {
        return this.prev;
    }

    public final Instruction getInstruction() {
        return this.instruction;
    }

    public void setInstruction(Instruction i2) {
        if (i2 == null) {
            throw new ClassGenException("Assigning null to handle");
        }
        if (getClass() != BranchHandle.class && (i2 instanceof BranchInstruction)) {
            throw new ClassGenException("Assigning branch instruction " + ((Object) i2) + " to plain handle");
        }
        if (this.instruction != null) {
            this.instruction.dispose();
        }
        this.instruction = i2;
    }

    public Instruction swapInstruction(Instruction i2) {
        Instruction oldInstruction = this.instruction;
        this.instruction = i2;
        return oldInstruction;
    }

    protected InstructionHandle(Instruction i2) {
        setInstruction(i2);
    }

    static final InstructionHandle getInstructionHandle(Instruction i2) {
        if (ih_list == null) {
            return new InstructionHandle(i2);
        }
        InstructionHandle ih = ih_list;
        ih_list = ih.next;
        ih.setInstruction(i2);
        return ih;
    }

    protected int updatePosition(int offset, int max_offset) {
        this.i_position += offset;
        return 0;
    }

    public int getPosition() {
        return this.i_position;
    }

    void setPosition(int pos) {
        this.i_position = pos;
    }

    protected void addHandle() {
        this.next = ih_list;
        ih_list = this;
    }

    void dispose() {
        this.prev = null;
        this.next = null;
        this.instruction.dispose();
        this.instruction = null;
        this.i_position = -1;
        this.attributes = null;
        removeAllTargeters();
        addHandle();
    }

    public void removeAllTargeters() {
        if (this.targeters != null) {
            this.targeters.clear();
        }
    }

    public void removeTargeter(InstructionTargeter t2) {
        this.targeters.remove(t2);
    }

    public void addTargeter(InstructionTargeter t2) {
        if (this.targeters == null) {
            this.targeters = new HashSet();
        }
        this.targeters.add(t2);
    }

    public boolean hasTargeters() {
        return this.targeters != null && this.targeters.size() > 0;
    }

    public InstructionTargeter[] getTargeters() {
        if (!hasTargeters()) {
            return null;
        }
        InstructionTargeter[] t2 = new InstructionTargeter[this.targeters.size()];
        this.targeters.toArray(t2);
        return t2;
    }

    public String toString(boolean verbose) {
        return Utility.format(this.i_position, 4, false, ' ') + ": " + this.instruction.toString(verbose);
    }

    public String toString() {
        return toString(true);
    }

    public void addAttribute(Object key, Object attr) {
        if (this.attributes == null) {
            this.attributes = new HashMap(3);
        }
        this.attributes.put(key, attr);
    }

    public void removeAttribute(Object key) {
        if (this.attributes != null) {
            this.attributes.remove(key);
        }
    }

    public Object getAttribute(Object key) {
        if (this.attributes != null) {
            return this.attributes.get(key);
        }
        return null;
    }

    public Collection getAttributes() {
        return this.attributes.values();
    }

    public void accept(Visitor v2) {
        this.instruction.accept(v2);
    }
}
