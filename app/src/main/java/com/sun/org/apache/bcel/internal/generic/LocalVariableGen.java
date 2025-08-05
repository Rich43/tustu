package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
import java.io.Serializable;
import java.util.Objects;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LocalVariableGen.class */
public class LocalVariableGen implements InstructionTargeter, NamedAndTyped, Cloneable, Serializable {
    private final int index;
    private String name;
    private Type type;
    private InstructionHandle start;
    private InstructionHandle end;

    public LocalVariableGen(int index, String name, Type type, InstructionHandle start, InstructionHandle end) {
        if (index < 0 || index > 65535) {
            throw new ClassGenException("Invalid index index: " + index);
        }
        this.name = name;
        this.type = type;
        this.index = index;
        setStart(start);
        setEnd(end);
    }

    public LocalVariable getLocalVariable(ConstantPoolGen cp) {
        int start_pc = this.start.getPosition();
        int length = this.end.getPosition() - start_pc;
        if (length > 0) {
            length += this.end.getInstruction().getLength();
        }
        int name_index = cp.addUtf8(this.name);
        int signature_index = cp.addUtf8(this.type.getSignature());
        return new LocalVariable(start_pc, length, name_index, signature_index, this.index, cp.getConstantPool());
    }

    public int getIndex() {
        return this.index;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.NamedAndTyped
    public void setName(String name) {
        this.name = name;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.NamedAndTyped
    public String getName() {
        return this.name;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.NamedAndTyped
    public void setType(Type type) {
        this.type = type;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.NamedAndTyped
    public Type getType() {
        return this.type;
    }

    public InstructionHandle getStart() {
        return this.start;
    }

    public InstructionHandle getEnd() {
        return this.end;
    }

    void notifyTargetChanging() {
        BranchInstruction.notifyTargetChanging(this.start, this);
        if (this.end != this.start) {
            BranchInstruction.notifyTargetChanging(this.end, this);
        }
    }

    void notifyTargetChanged() {
        BranchInstruction.notifyTargetChanged(this.start, this);
        if (this.end != this.start) {
            BranchInstruction.notifyTargetChanged(this.end, this);
        }
    }

    public final void setStart(InstructionHandle start) {
        notifyTargetChanging();
        this.start = start;
        notifyTargetChanged();
    }

    public final void setEnd(InstructionHandle end) {
        notifyTargetChanging();
        this.end = end;
        notifyTargetChanged();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionTargeter
    public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
        boolean targeted = false;
        if (this.start == old_ih) {
            targeted = true;
            setStart(new_ih);
        }
        if (this.end == old_ih) {
            targeted = true;
            setEnd(new_ih);
        }
        if (!targeted) {
            throw new ClassGenException("Not targeting " + ((Object) old_ih) + ", but {" + ((Object) this.start) + ", " + ((Object) this.end) + "}");
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle ih) {
        return this.start == ih || this.end == ih;
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof LocalVariableGen)) {
            return false;
        }
        LocalVariableGen l2 = (LocalVariableGen) o2;
        return l2.index == this.index && l2.start == this.start && l2.end == this.end;
    }

    public int hashCode() {
        int hash = (59 * 7) + this.index;
        return (59 * ((59 * hash) + Objects.hashCode(this.start))) + Objects.hashCode(this.end);
    }

    public String toString() {
        return "LocalVariableGen(" + this.name + ", " + ((Object) this.type) + ", " + ((Object) this.start) + ", " + ((Object) this.end) + ")";
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            System.err.println(e2);
            return null;
        }
    }
}
