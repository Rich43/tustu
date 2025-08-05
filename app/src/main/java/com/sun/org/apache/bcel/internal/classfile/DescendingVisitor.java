package com.sun.org.apache.bcel.internal.classfile;

import java.util.Stack;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/DescendingVisitor.class */
public class DescendingVisitor implements Visitor {
    private JavaClass clazz;
    private Visitor visitor;
    private Stack stack = new Stack();

    public Object predecessor() {
        return predecessor(0);
    }

    public Object predecessor(int level) {
        int size = this.stack.size();
        if (size < 2 || level < 0) {
            return null;
        }
        return this.stack.elementAt(size - (level + 2));
    }

    public Object current() {
        return this.stack.peek();
    }

    public DescendingVisitor(JavaClass clazz, Visitor visitor) {
        this.clazz = clazz;
        this.visitor = visitor;
    }

    public void visit() {
        this.clazz.accept(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitJavaClass(JavaClass clazz) {
        this.stack.push(clazz);
        clazz.accept(this.visitor);
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            field.accept(this);
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            method.accept(this);
        }
        Attribute[] attributes = clazz.getAttributes();
        for (Attribute attribute : attributes) {
            attribute.accept(this);
        }
        clazz.getConstantPool().accept(this);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitField(Field field) {
        this.stack.push(field);
        field.accept(this.visitor);
        Attribute[] attributes = field.getAttributes();
        for (Attribute attribute : attributes) {
            attribute.accept(this);
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantValue(ConstantValue cv) {
        this.stack.push(cv);
        cv.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitMethod(Method method) {
        this.stack.push(method);
        method.accept(this.visitor);
        Attribute[] attributes = method.getAttributes();
        for (Attribute attribute : attributes) {
            attribute.accept(this);
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitExceptionTable(ExceptionTable table) {
        this.stack.push(table);
        table.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitCode(Code code) {
        this.stack.push(code);
        code.accept(this.visitor);
        CodeException[] table = code.getExceptionTable();
        for (CodeException codeException : table) {
            codeException.accept(this);
        }
        Attribute[] attributes = code.getAttributes();
        for (Attribute attribute : attributes) {
            attribute.accept(this);
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitCodeException(CodeException ce) {
        this.stack.push(ce);
        ce.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitLineNumberTable(LineNumberTable table) {
        this.stack.push(table);
        table.accept(this.visitor);
        LineNumber[] numbers = table.getLineNumberTable();
        for (LineNumber lineNumber : numbers) {
            lineNumber.accept(this);
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitLineNumber(LineNumber number) {
        this.stack.push(number);
        number.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitLocalVariableTable(LocalVariableTable table) {
        this.stack.push(table);
        table.accept(this.visitor);
        LocalVariable[] vars = table.getLocalVariableTable();
        for (LocalVariable localVariable : vars) {
            localVariable.accept(this);
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitLocalVariableTypeTable(LocalVariableTypeTable obj) {
        this.stack.push(obj);
        obj.accept(this.visitor);
        LocalVariable[] vars = obj.getLocalVariableTypeTable();
        for (LocalVariable localVariable : vars) {
            localVariable.accept(this);
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitStackMap(StackMap table) {
        this.stack.push(table);
        table.accept(this.visitor);
        StackMapEntry[] vars = table.getStackMap();
        for (StackMapEntry stackMapEntry : vars) {
            stackMapEntry.accept(this);
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitStackMapEntry(StackMapEntry var) {
        this.stack.push(var);
        var.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitLocalVariable(LocalVariable var) {
        this.stack.push(var);
        var.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantPool(ConstantPool cp) {
        this.stack.push(cp);
        cp.accept(this.visitor);
        Constant[] constants = cp.getConstantPool();
        for (int i2 = 1; i2 < constants.length; i2++) {
            if (constants[i2] != null) {
                constants[i2].accept(this);
            }
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantClass(ConstantClass constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantDouble(ConstantDouble constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantFieldref(ConstantFieldref constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantFloat(ConstantFloat constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantInteger(ConstantInteger constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantLong(ConstantLong constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantMethodref(ConstantMethodref constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantNameAndType(ConstantNameAndType constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantString(ConstantString constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitConstantUtf8(ConstantUtf8 constant) {
        this.stack.push(constant);
        constant.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitInnerClasses(InnerClasses ic) {
        this.stack.push(ic);
        ic.accept(this.visitor);
        InnerClass[] ics = ic.getInnerClasses();
        for (InnerClass innerClass : ics) {
            innerClass.accept(this);
        }
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitInnerClass(InnerClass inner) {
        this.stack.push(inner);
        inner.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitDeprecated(Deprecated attribute) {
        this.stack.push(attribute);
        attribute.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitSignature(Signature attribute) {
        this.stack.push(attribute);
        attribute.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitSourceFile(SourceFile attribute) {
        this.stack.push(attribute);
        attribute.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitSynthetic(Synthetic attribute) {
        this.stack.push(attribute);
        attribute.accept(this.visitor);
        this.stack.pop();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitUnknown(Unknown attribute) {
        this.stack.push(attribute);
        attribute.accept(this.visitor);
        this.stack.pop();
    }
}
