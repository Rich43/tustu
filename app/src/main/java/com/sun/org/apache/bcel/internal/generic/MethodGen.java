package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.Attribute;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.bcel.internal.classfile.CodeException;
import com.sun.org.apache.bcel.internal.classfile.ExceptionTable;
import com.sun.org.apache.bcel.internal.classfile.LineNumber;
import com.sun.org.apache.bcel.internal.classfile.LineNumberTable;
import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
import com.sun.org.apache.bcel.internal.classfile.LocalVariableTable;
import com.sun.org.apache.bcel.internal.classfile.LocalVariableTypeTable;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/MethodGen.class */
public class MethodGen extends FieldGenOrMethodGen {
    private String class_name;
    private Type[] arg_types;
    private String[] arg_names;
    private int max_locals;
    private int max_stack;
    private InstructionList il;
    private boolean strip_attributes;
    private ArrayList variable_vec;
    private ArrayList type_vec;
    private ArrayList line_number_vec;
    private ArrayList exception_vec;
    private ArrayList throws_vec;
    private ArrayList code_attrs_vec;
    private ArrayList observers;

    public MethodGen(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
        this.variable_vec = new ArrayList();
        this.type_vec = new ArrayList();
        this.line_number_vec = new ArrayList();
        this.exception_vec = new ArrayList();
        this.throws_vec = new ArrayList();
        this.code_attrs_vec = new ArrayList();
        setAccessFlags(access_flags);
        setType(return_type);
        setArgumentTypes(arg_types);
        setArgumentNames(arg_names);
        setName(method_name);
        setClassName(class_name);
        setInstructionList(il);
        setConstantPool(cp);
        boolean abstract_ = isAbstract() || isNative();
        InstructionHandle start = null;
        InstructionHandle end = null;
        if (!abstract_) {
            start = il.getStart();
            end = il.getEnd();
            if (!isStatic() && class_name != null) {
                addLocalVariable("this", new ObjectType(class_name), start, end);
            }
        }
        if (arg_types != null) {
            int size = arg_types.length;
            for (Type type : arg_types) {
                if (Type.VOID == type) {
                    throw new ClassGenException("'void' is an illegal argument type for a method");
                }
            }
            if (arg_names != null) {
                if (size != arg_names.length) {
                    throw new ClassGenException("Mismatch in argument array lengths: " + size + " vs. " + arg_names.length);
                }
            } else {
                arg_names = new String[size];
                for (int i2 = 0; i2 < size; i2++) {
                    arg_names[i2] = Constants.ELEMNAME_ARG_STRING + i2;
                }
                setArgumentNames(arg_names);
            }
            if (!abstract_) {
                for (int i3 = 0; i3 < size; i3++) {
                    addLocalVariable(arg_names[i3], arg_types[i3], start, end);
                }
            }
        }
    }

    public MethodGen(Method m2, String class_name, ConstantPoolGen cp) throws ClassFormatException {
        InstructionHandle end;
        this(m2.getAccessFlags(), Type.getReturnType(m2.getSignature()), Type.getArgumentTypes(m2.getSignature()), null, m2.getName(), class_name, (m2.getAccessFlags() & 1280) == 0 ? new InstructionList(m2.getCode().getCode()) : null, cp);
        Attribute[] attributes = m2.getAttributes();
        for (Attribute a2 : attributes) {
            if (a2 instanceof Code) {
                Code c2 = (Code) a2;
                setMaxStack(c2.getMaxStack());
                setMaxLocals(c2.getMaxLocals());
                CodeException[] ces = c2.getExceptionTable();
                if (ces != null) {
                    for (CodeException ce : ces) {
                        int type = ce.getCatchType();
                        ObjectType c_type = null;
                        if (type > 0) {
                            String cen = m2.getConstantPool().getConstantString(type, (byte) 7);
                            c_type = new ObjectType(cen);
                        }
                        int end_pc = ce.getEndPC();
                        int length = m2.getCode().getCode().length;
                        if (length == end_pc) {
                            end = this.il.getEnd();
                        } else {
                            end = this.il.findHandle(end_pc).getPrev();
                        }
                        addExceptionHandler(this.il.findHandle(ce.getStartPC()), end, this.il.findHandle(ce.getHandlerPC()), c_type);
                    }
                }
                Attribute[] c_attributes = c2.getAttributes();
                for (Attribute a3 : c_attributes) {
                    if (a3 instanceof LineNumberTable) {
                        LineNumber[] ln = ((LineNumberTable) a3).getLineNumberTable();
                        for (LineNumber l2 : ln) {
                            addLineNumber(this.il.findHandle(l2.getStartPC()), l2.getLineNumber());
                        }
                    } else if (a3 instanceof LocalVariableTable) {
                        LocalVariable[] lv = ((LocalVariableTable) a3).getLocalVariableTable();
                        removeLocalVariables();
                        for (LocalVariable l3 : lv) {
                            InstructionHandle start = this.il.findHandle(l3.getStartPC());
                            InstructionHandle end2 = this.il.findHandle(l3.getStartPC() + l3.getLength());
                            start = null == start ? this.il.getStart() : start;
                            if (null == end2) {
                                end2 = this.il.getEnd();
                            }
                            addLocalVariable(l3.getName(), Type.getType(l3.getSignature()), l3.getIndex(), start, end2);
                        }
                    } else if (a3 instanceof LocalVariableTypeTable) {
                        LocalVariable[] lv2 = ((LocalVariableTypeTable) a3).getLocalVariableTypeTable();
                        removeLocalVariableTypes();
                        for (LocalVariable l4 : lv2) {
                            InstructionHandle start2 = this.il.findHandle(l4.getStartPC());
                            InstructionHandle end3 = this.il.findHandle(l4.getStartPC() + l4.getLength());
                            start2 = null == start2 ? this.il.getStart() : start2;
                            if (null == end3) {
                                end3 = this.il.getEnd();
                            }
                            addLocalVariableType(l4.getName(), Type.getType(l4.getSignature()), l4.getIndex(), start2, end3);
                        }
                    } else {
                        addCodeAttribute(a3);
                    }
                }
            } else if (a2 instanceof ExceptionTable) {
                String[] names = ((ExceptionTable) a2).getExceptionNames();
                for (String str : names) {
                    addException(str);
                }
            } else {
                addAttribute(a2);
            }
        }
    }

    public LocalVariableGen addLocalVariable(String name, Type type, int slot, InstructionHandle start, InstructionHandle end) {
        byte t2 = type.getType();
        if (t2 != 16) {
            int add = type.getSize();
            if (slot + add > this.max_locals) {
                this.max_locals = slot + add;
            }
            LocalVariableGen l2 = new LocalVariableGen(slot, name, type, start, end);
            int i2 = this.variable_vec.indexOf(l2);
            if (i2 >= 0) {
                this.variable_vec.set(i2, l2);
            } else {
                this.variable_vec.add(l2);
            }
            return l2;
        }
        throw new IllegalArgumentException("Can not use " + ((Object) type) + " as type for local variable");
    }

    public LocalVariableGen addLocalVariable(String name, Type type, InstructionHandle start, InstructionHandle end) {
        return addLocalVariable(name, type, this.max_locals, start, end);
    }

    public void removeLocalVariable(LocalVariableGen l2) {
        this.variable_vec.remove(l2);
    }

    public void removeLocalVariables() {
        this.variable_vec.clear();
    }

    private static final void sort(LocalVariableGen[] vars, int l2, int r2) {
        int i2 = l2;
        int j2 = r2;
        int m2 = vars[(l2 + r2) / 2].getIndex();
        while (true) {
            if (vars[i2].getIndex() < m2) {
                i2++;
            } else {
                while (m2 < vars[j2].getIndex()) {
                    j2--;
                }
                if (i2 <= j2) {
                    LocalVariableGen h2 = vars[i2];
                    vars[i2] = vars[j2];
                    vars[j2] = h2;
                    i2++;
                    j2--;
                }
                if (i2 > j2) {
                    break;
                }
            }
        }
        if (l2 < j2) {
            sort(vars, l2, j2);
        }
        if (i2 < r2) {
            sort(vars, i2, r2);
        }
    }

    public LocalVariableGen[] getLocalVariables() {
        int size = this.variable_vec.size();
        LocalVariableGen[] lg = new LocalVariableGen[size];
        this.variable_vec.toArray(lg);
        for (int i2 = 0; i2 < size; i2++) {
            if (lg[i2].getStart() == null) {
                lg[i2].setStart(this.il.getStart());
            }
            if (lg[i2].getEnd() == null) {
                lg[i2].setEnd(this.il.getEnd());
            }
        }
        if (size > 1) {
            sort(lg, 0, size - 1);
        }
        return lg;
    }

    private LocalVariableGen[] getLocalVariableTypes() {
        int size = this.type_vec.size();
        LocalVariableGen[] lg = new LocalVariableGen[size];
        this.type_vec.toArray(lg);
        for (int i2 = 0; i2 < size; i2++) {
            if (lg[i2].getStart() == null) {
                lg[i2].setStart(this.il.getStart());
            }
            if (lg[i2].getEnd() == null) {
                lg[i2].setEnd(this.il.getEnd());
            }
        }
        if (size > 1) {
            sort(lg, 0, size - 1);
        }
        return lg;
    }

    public LocalVariableTable getLocalVariableTable(ConstantPoolGen cp) {
        LocalVariableGen[] lg = getLocalVariables();
        int size = lg.length;
        LocalVariable[] lv = new LocalVariable[size];
        for (int i2 = 0; i2 < size; i2++) {
            lv[i2] = lg[i2].getLocalVariable(cp);
        }
        return new LocalVariableTable(cp.addUtf8("LocalVariableTable"), 2 + (lv.length * 10), lv, cp.getConstantPool());
    }

    public LocalVariableTypeTable getLocalVariableTypeTable(ConstantPoolGen cp) {
        LocalVariableGen[] lg = getLocalVariableTypes();
        int size = lg.length;
        LocalVariable[] lv = new LocalVariable[size];
        for (int i2 = 0; i2 < size; i2++) {
            lv[i2] = lg[i2].getLocalVariable(cp);
        }
        return new LocalVariableTypeTable(cp.addUtf8("LocalVariableTypeTable"), 2 + (lv.length * 10), lv, cp.getConstantPool());
    }

    private LocalVariableGen addLocalVariableType(String name, Type type, int slot, InstructionHandle start, InstructionHandle end) {
        byte t2 = type.getType();
        if (t2 != 16) {
            int add = type.getSize();
            if (slot + add > this.max_locals) {
                this.max_locals = slot + add;
            }
            LocalVariableGen l2 = new LocalVariableGen(slot, name, type, start, end);
            int i2 = this.type_vec.indexOf(l2);
            if (i2 >= 0) {
                this.type_vec.set(i2, l2);
            } else {
                this.type_vec.add(l2);
            }
            return l2;
        }
        throw new IllegalArgumentException("Can not use " + ((Object) type) + " as type for local variable");
    }

    private void removeLocalVariableTypes() {
        this.type_vec.clear();
    }

    public LineNumberGen addLineNumber(InstructionHandle ih, int src_line) {
        LineNumberGen l2 = new LineNumberGen(ih, src_line);
        this.line_number_vec.add(l2);
        return l2;
    }

    public void removeLineNumber(LineNumberGen l2) {
        this.line_number_vec.remove(l2);
    }

    public void removeLineNumbers() {
        this.line_number_vec.clear();
    }

    public LineNumberGen[] getLineNumbers() {
        LineNumberGen[] lg = new LineNumberGen[this.line_number_vec.size()];
        this.line_number_vec.toArray(lg);
        return lg;
    }

    public LineNumberTable getLineNumberTable(ConstantPoolGen cp) {
        int size = this.line_number_vec.size();
        LineNumber[] ln = new LineNumber[size];
        for (int i2 = 0; i2 < size; i2++) {
            try {
                ln[i2] = ((LineNumberGen) this.line_number_vec.get(i2)).getLineNumber();
            } catch (ArrayIndexOutOfBoundsException e2) {
            }
        }
        return new LineNumberTable(cp.addUtf8("LineNumberTable"), 2 + (ln.length * 4), ln, cp.getConstantPool());
    }

    public CodeExceptionGen addExceptionHandler(InstructionHandle start_pc, InstructionHandle end_pc, InstructionHandle handler_pc, ObjectType catch_type) {
        if (start_pc == null || end_pc == null || handler_pc == null) {
            throw new ClassGenException("Exception handler target is null instruction");
        }
        CodeExceptionGen c2 = new CodeExceptionGen(start_pc, end_pc, handler_pc, catch_type);
        this.exception_vec.add(c2);
        return c2;
    }

    public void removeExceptionHandler(CodeExceptionGen c2) {
        this.exception_vec.remove(c2);
    }

    public void removeExceptionHandlers() {
        this.exception_vec.clear();
    }

    public CodeExceptionGen[] getExceptionHandlers() {
        CodeExceptionGen[] cg = new CodeExceptionGen[this.exception_vec.size()];
        this.exception_vec.toArray(cg);
        return cg;
    }

    private CodeException[] getCodeExceptions() {
        int size = this.exception_vec.size();
        CodeException[] c_exc = new CodeException[size];
        for (int i2 = 0; i2 < size; i2++) {
            try {
                CodeExceptionGen c2 = (CodeExceptionGen) this.exception_vec.get(i2);
                c_exc[i2] = c2.getCodeException(this.cp);
            } catch (ArrayIndexOutOfBoundsException e2) {
            }
        }
        return c_exc;
    }

    public void addException(String class_name) {
        this.throws_vec.add(class_name);
    }

    public void removeException(String c2) {
        this.throws_vec.remove(c2);
    }

    public void removeExceptions() {
        this.throws_vec.clear();
    }

    public String[] getExceptions() {
        String[] e2 = new String[this.throws_vec.size()];
        this.throws_vec.toArray(e2);
        return e2;
    }

    private ExceptionTable getExceptionTable(ConstantPoolGen cp) {
        int size = this.throws_vec.size();
        int[] ex = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            try {
                ex[i2] = cp.addClass((String) this.throws_vec.get(i2));
            } catch (ArrayIndexOutOfBoundsException e2) {
            }
        }
        return new ExceptionTable(cp.addUtf8("Exceptions"), 2 + (2 * size), ex, cp.getConstantPool());
    }

    public void addCodeAttribute(Attribute a2) {
        this.code_attrs_vec.add(a2);
    }

    public void removeCodeAttribute(Attribute a2) {
        this.code_attrs_vec.remove(a2);
    }

    public void removeCodeAttributes() {
        this.code_attrs_vec.clear();
    }

    public Attribute[] getCodeAttributes() {
        Attribute[] attributes = new Attribute[this.code_attrs_vec.size()];
        this.code_attrs_vec.toArray(attributes);
        return attributes;
    }

    public Method getMethod() {
        String signature = getSignature();
        int name_index = this.cp.addUtf8(this.name);
        int signature_index = this.cp.addUtf8(signature);
        byte[] byte_code = null;
        if (this.il != null) {
            byte_code = this.il.getByteCode();
        }
        LineNumberTable lnt = null;
        LocalVariableTable lvt = null;
        LocalVariableTypeTable lvtt = null;
        if (this.variable_vec.size() > 0 && !this.strip_attributes) {
            LocalVariableTable localVariableTable = getLocalVariableTable(this.cp);
            lvt = localVariableTable;
            addCodeAttribute(localVariableTable);
        }
        if (this.type_vec.size() > 0 && !this.strip_attributes) {
            LocalVariableTypeTable localVariableTypeTable = getLocalVariableTypeTable(this.cp);
            lvtt = localVariableTypeTable;
            addCodeAttribute(localVariableTypeTable);
        }
        if (this.line_number_vec.size() > 0 && !this.strip_attributes) {
            LineNumberTable lineNumberTable = getLineNumberTable(this.cp);
            lnt = lineNumberTable;
            addCodeAttribute(lineNumberTable);
        }
        Attribute[] code_attrs = getCodeAttributes();
        int attrs_len = 0;
        for (Attribute attribute : code_attrs) {
            attrs_len += attribute.getLength() + 6;
        }
        CodeException[] c_exc = getCodeExceptions();
        int exc_len = c_exc.length * 8;
        Code code = null;
        if (this.il != null && !isAbstract()) {
            Attribute[] attributes = getAttributes();
            for (Attribute a2 : attributes) {
                if (a2 instanceof Code) {
                    removeAttribute(a2);
                }
            }
            code = new Code(this.cp.addUtf8("Code"), 8 + byte_code.length + 2 + exc_len + 2 + attrs_len, this.max_stack, this.max_locals, byte_code, c_exc, code_attrs, this.cp.getConstantPool());
            addAttribute(code);
        }
        ExceptionTable et = null;
        if (this.throws_vec.size() > 0) {
            ExceptionTable exceptionTable = getExceptionTable(this.cp);
            et = exceptionTable;
            addAttribute(exceptionTable);
        }
        Method m2 = new Method(this.access_flags, name_index, signature_index, getAttributes(), this.cp.getConstantPool());
        if (lvt != null) {
            removeCodeAttribute(lvt);
        }
        if (lvtt != null) {
            removeCodeAttribute(lvtt);
        }
        if (lnt != null) {
            removeCodeAttribute(lnt);
        }
        if (code != null) {
            removeAttribute(code);
        }
        if (et != null) {
            removeAttribute(et);
        }
        return m2;
    }

    public void removeNOPs() {
        if (this.il != null) {
            InstructionHandle start = this.il.getStart();
            while (true) {
                InstructionHandle ih = start;
                if (ih != null) {
                    InstructionHandle next = ih.next;
                    if (next != null && (ih.getInstruction() instanceof NOP)) {
                        try {
                            this.il.delete(ih);
                        } catch (TargetLostException e2) {
                            InstructionHandle[] targets = e2.getTargets();
                            for (int i2 = 0; i2 < targets.length; i2++) {
                                InstructionTargeter[] targeters = targets[i2].getTargeters();
                                for (InstructionTargeter instructionTargeter : targeters) {
                                    instructionTargeter.updateTarget(targets[i2], next);
                                }
                            }
                        }
                    }
                    start = next;
                } else {
                    return;
                }
            }
        }
    }

    public void setMaxLocals(int m2) {
        this.max_locals = m2;
    }

    public int getMaxLocals() {
        return this.max_locals;
    }

    public void setMaxStack(int m2) {
        this.max_stack = m2;
    }

    public int getMaxStack() {
        return this.max_stack;
    }

    public String getClassName() {
        return this.class_name;
    }

    public void setClassName(String class_name) {
        this.class_name = class_name;
    }

    public void setReturnType(Type return_type) {
        setType(return_type);
    }

    public Type getReturnType() {
        return getType();
    }

    public void setArgumentTypes(Type[] arg_types) {
        this.arg_types = arg_types;
    }

    public Type[] getArgumentTypes() {
        return (Type[]) this.arg_types.clone();
    }

    public void setArgumentType(int i2, Type type) {
        this.arg_types[i2] = type;
    }

    public Type getArgumentType(int i2) {
        return this.arg_types[i2];
    }

    public void setArgumentNames(String[] arg_names) {
        this.arg_names = arg_names;
    }

    public String[] getArgumentNames() {
        return (String[]) this.arg_names.clone();
    }

    public void setArgumentName(int i2, String name) {
        this.arg_names[i2] = name;
    }

    public String getArgumentName(int i2) {
        return this.arg_names[i2];
    }

    public InstructionList getInstructionList() {
        return this.il;
    }

    public void setInstructionList(InstructionList il) {
        this.il = il;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.FieldGenOrMethodGen
    public String getSignature() {
        return Type.getMethodSignature(this.type, this.arg_types);
    }

    public void setMaxStack() {
        if (this.il != null) {
            this.max_stack = getMaxStack(this.cp, this.il, getExceptionHandlers());
        } else {
            this.max_stack = 0;
        }
    }

    public void setMaxLocals() {
        int index;
        if (this.il != null) {
            int max = isStatic() ? 0 : 1;
            if (this.arg_types != null) {
                for (int i2 = 0; i2 < this.arg_types.length; i2++) {
                    max += this.arg_types[i2].getSize();
                }
            }
            InstructionHandle start = this.il.getStart();
            while (true) {
                InstructionHandle ih = start;
                if (ih != null) {
                    Cloneable instruction = ih.getInstruction();
                    if (((instruction instanceof LocalVariableInstruction) || (instruction instanceof RET) || (instruction instanceof IINC)) && (index = ((IndexedInstruction) instruction).getIndex() + ((TypedInstruction) instruction).getType(this.cp).getSize()) > max) {
                        max = index;
                    }
                    start = ih.getNext();
                } else {
                    this.max_locals = max;
                    return;
                }
            }
        } else {
            this.max_locals = 0;
        }
    }

    public void stripAttributes(boolean flag) {
        this.strip_attributes = flag;
    }

    /* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/MethodGen$BranchTarget.class */
    static final class BranchTarget {
        InstructionHandle target;
        int stackDepth;

        BranchTarget(InstructionHandle target, int stackDepth) {
            this.target = target;
            this.stackDepth = stackDepth;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/MethodGen$BranchStack.class */
    static final class BranchStack {
        Stack branchTargets = new Stack();
        Hashtable visitedTargets = new Hashtable();

        BranchStack() {
        }

        public void push(InstructionHandle target, int stackDepth) {
            if (visited(target)) {
                return;
            }
            this.branchTargets.push(visit(target, stackDepth));
        }

        public BranchTarget pop() {
            if (!this.branchTargets.empty()) {
                BranchTarget bt2 = (BranchTarget) this.branchTargets.pop();
                return bt2;
            }
            return null;
        }

        private final BranchTarget visit(InstructionHandle target, int stackDepth) {
            BranchTarget bt2 = new BranchTarget(target, stackDepth);
            this.visitedTargets.put(target, bt2);
            return bt2;
        }

        private final boolean visited(InstructionHandle target) {
            return this.visitedTargets.get(target) != null;
        }
    }

    public static int getMaxStack(ConstantPoolGen cp, InstructionList il, CodeExceptionGen[] et) {
        BranchTarget bt2;
        BranchStack branchTargets = new BranchStack();
        for (CodeExceptionGen codeExceptionGen : et) {
            InstructionHandle handler_pc = codeExceptionGen.getHandlerPC();
            if (handler_pc != null) {
                branchTargets.push(handler_pc, 1);
            }
        }
        int stackDepth = 0;
        int maxStackDepth = 0;
        InstructionHandle ih = il.getStart();
        while (ih != null) {
            Instruction instruction = ih.getInstruction();
            short opcode = instruction.getOpcode();
            int delta = instruction.produceStack(cp) - instruction.consumeStack(cp);
            stackDepth += delta;
            if (stackDepth > maxStackDepth) {
                maxStackDepth = stackDepth;
            }
            if (instruction instanceof BranchInstruction) {
                BranchInstruction branch = (BranchInstruction) instruction;
                if (instruction instanceof Select) {
                    Select select = (Select) branch;
                    InstructionHandle[] targets = select.getTargets();
                    for (InstructionHandle instructionHandle : targets) {
                        branchTargets.push(instructionHandle, stackDepth);
                    }
                    ih = null;
                } else if (!(branch instanceof IfInstruction)) {
                    if (opcode == 168 || opcode == 201) {
                        branchTargets.push(ih.getNext(), stackDepth - 1);
                    }
                    ih = null;
                }
                branchTargets.push(branch.getTarget(), stackDepth);
            } else if (opcode == 191 || opcode == 169 || (opcode >= 172 && opcode <= 177)) {
                ih = null;
            }
            if (ih != null) {
                ih = ih.getNext();
            }
            if (ih == null && (bt2 = branchTargets.pop()) != null) {
                ih = bt2.target;
                stackDepth = bt2.stackDepth;
            }
        }
        return maxStackDepth;
    }

    public void addObserver(MethodObserver o2) {
        if (this.observers == null) {
            this.observers = new ArrayList();
        }
        this.observers.add(o2);
    }

    public void removeObserver(MethodObserver o2) {
        if (this.observers != null) {
            this.observers.remove(o2);
        }
    }

    public void update() {
        if (this.observers != null) {
            Iterator e2 = this.observers.iterator();
            while (e2.hasNext()) {
                ((MethodObserver) e2.next()).notify(this);
            }
        }
    }

    public final String toString() {
        String access = Utility.accessToString(this.access_flags);
        String signature = Type.getMethodSignature(this.type, this.arg_types);
        StringBuffer buf = new StringBuffer(Utility.methodSignatureToString(signature, this.name, access, true, getLocalVariableTable(this.cp)));
        if (this.throws_vec.size() > 0) {
            Iterator e2 = this.throws_vec.iterator();
            while (e2.hasNext()) {
                buf.append("\n\t\tthrows " + e2.next());
            }
        }
        return buf.toString();
    }

    public MethodGen copy(String class_name, ConstantPoolGen cp) {
        Method m2 = ((MethodGen) clone()).getMethod();
        MethodGen mg = new MethodGen(m2, class_name, this.cp);
        if (this.cp != cp) {
            mg.setConstantPool(cp);
            mg.getInstructionList().replaceConstantPool(this.cp, cp);
        }
        return mg;
    }
}
