package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.bcel.internal.generic.AllocationInstruction;
import com.sun.org.apache.bcel.internal.generic.ArrayInstruction;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.CPInstruction;
import com.sun.org.apache.bcel.internal.generic.CodeExceptionGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction;
import com.sun.org.apache.bcel.internal.generic.EmptyVisitor;
import com.sun.org.apache.bcel.internal.generic.FieldInstruction;
import com.sun.org.apache.bcel.internal.generic.IINC;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InvokeInstruction;
import com.sun.org.apache.bcel.internal.generic.LDC;
import com.sun.org.apache.bcel.internal.generic.LDC2_W;
import com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction;
import com.sun.org.apache.bcel.internal.generic.MULTIANEWARRAY;
import com.sun.org.apache.bcel.internal.generic.MethodGen;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import com.sun.org.apache.bcel.internal.generic.RET;
import com.sun.org.apache.bcel.internal.generic.ReturnInstruction;
import com.sun.org.apache.bcel.internal.generic.Select;
import com.sun.org.apache.bcel.internal.generic.Type;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/BCELFactory.class */
class BCELFactory extends EmptyVisitor {
    private MethodGen _mg;
    private PrintWriter _out;
    private ConstantPoolGen _cp;
    private HashMap branch_map = new HashMap();
    private ArrayList branches = new ArrayList();

    BCELFactory(MethodGen mg, PrintWriter out) {
        this._mg = mg;
        this._cp = mg.getConstantPool();
        this._out = out;
    }

    public void start() {
        if (!this._mg.isAbstract() && !this._mg.isNative()) {
            InstructionHandle start = this._mg.getInstructionList().getStart();
            while (true) {
                InstructionHandle ih = start;
                if (ih != null) {
                    Instruction i2 = ih.getInstruction();
                    if (i2 instanceof BranchInstruction) {
                        this.branch_map.put(i2, ih);
                    }
                    if (ih.hasTargeters()) {
                        if (i2 instanceof BranchInstruction) {
                            this._out.println("    InstructionHandle ih_" + ih.getPosition() + ";");
                        } else {
                            this._out.print("    InstructionHandle ih_" + ih.getPosition() + " = ");
                        }
                    } else {
                        this._out.print("    ");
                    }
                    if (!visitInstruction(i2)) {
                        i2.accept(this);
                    }
                    start = ih.getNext();
                } else {
                    updateBranchTargets();
                    updateExceptionHandlers();
                    return;
                }
            }
        }
    }

    private boolean visitInstruction(Instruction i2) {
        short opcode = i2.getOpcode();
        if (InstructionConstants.INSTRUCTIONS[opcode] != null && !(i2 instanceof ConstantPushInstruction) && !(i2 instanceof ReturnInstruction)) {
            this._out.println("il.append(InstructionConstants." + i2.getName().toUpperCase() + ");");
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitLocalVariableInstruction(LocalVariableInstruction i2) {
        short opcode = i2.getOpcode();
        Type type = i2.getType(this._cp);
        if (opcode == 132) {
            this._out.println("il.append(new IINC(" + i2.getIndex() + ", " + ((IINC) i2).getIncrement() + "));");
        } else {
            String kind = opcode < 54 ? "Load" : "Store";
            this._out.println("il.append(_factory.create" + kind + "(" + BCELifier.printType(type) + ", " + i2.getIndex() + "));");
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitArrayInstruction(ArrayInstruction i2) {
        short opcode = i2.getOpcode();
        Type type = i2.getType(this._cp);
        String kind = opcode < 79 ? "Load" : "Store";
        this._out.println("il.append(_factory.createArray" + kind + "(" + BCELifier.printType(type) + "));");
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitFieldInstruction(FieldInstruction i2) {
        short opcode = i2.getOpcode();
        String class_name = i2.getClassName(this._cp);
        String field_name = i2.getFieldName(this._cp);
        Type type = i2.getFieldType(this._cp);
        this._out.println("il.append(_factory.createFieldAccess(\"" + class_name + "\", \"" + field_name + "\", " + BCELifier.printType(type) + ", Constants." + Constants.OPCODE_NAMES[opcode].toUpperCase() + "));");
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitInvokeInstruction(InvokeInstruction i2) {
        short opcode = i2.getOpcode();
        String class_name = i2.getClassName(this._cp);
        String method_name = i2.getMethodName(this._cp);
        Type type = i2.getReturnType(this._cp);
        Type[] arg_types = i2.getArgumentTypes(this._cp);
        this._out.println("il.append(_factory.createInvoke(\"" + class_name + "\", \"" + method_name + "\", " + BCELifier.printType(type) + ", " + BCELifier.printArgumentTypes(arg_types) + ", Constants." + Constants.OPCODE_NAMES[opcode].toUpperCase() + "));");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitAllocationInstruction(AllocationInstruction allocationInstruction) throws ClassFormatException {
        Type type;
        if (allocationInstruction instanceof CPInstruction) {
            type = ((CPInstruction) allocationInstruction).getType(this._cp);
        } else {
            type = ((NEWARRAY) allocationInstruction).getType();
        }
        short opcode = ((Instruction) allocationInstruction).getOpcode();
        int dim = 1;
        switch (opcode) {
            case 187:
                this._out.println("il.append(_factory.createNew(\"" + ((ObjectType) type).getClassName() + "\"));");
                return;
            case 188:
            case 189:
                break;
            case 197:
                dim = ((MULTIANEWARRAY) allocationInstruction).getDimensions();
                break;
            default:
                throw new RuntimeException("Oops: " + ((int) opcode));
        }
        this._out.println("il.append(_factory.createNewArray(" + BCELifier.printType(type) + ", (short) " + dim + "));");
    }

    private void createConstant(Object value) {
        String embed = value.toString();
        if (value instanceof String) {
            embed = '\"' + Utility.convertString(value.toString()) + '\"';
        } else if (value instanceof Character) {
            embed = "(char)0x" + Integer.toHexString(((Character) value).charValue());
        }
        this._out.println("il.append(new PUSH(_cp, " + embed + "));");
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitLDC(LDC i2) {
        createConstant(i2.getValue(this._cp));
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitLDC2_W(LDC2_W i2) {
        createConstant(i2.getValue(this._cp));
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitConstantPushInstruction(ConstantPushInstruction i2) {
        createConstant(i2.getValue());
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitINSTANCEOF(INSTANCEOF i2) {
        Type type = i2.getType(this._cp);
        this._out.println("il.append(new INSTANCEOF(_cp.addClass(" + BCELifier.printType(type) + ")));");
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitCHECKCAST(CHECKCAST i2) {
        Type type = i2.getType(this._cp);
        this._out.println("il.append(_factory.createCheckCast(" + BCELifier.printType(type) + "));");
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitReturnInstruction(ReturnInstruction i2) {
        Type type = i2.getType(this._cp);
        this._out.println("il.append(_factory.createReturn(" + BCELifier.printType(type) + "));");
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitBranchInstruction(BranchInstruction bi2) {
        String target;
        BranchHandle bh2 = (BranchHandle) this.branch_map.get(bi2);
        int pos = bh2.getPosition();
        String name = bi2.getName() + "_" + pos;
        if (bi2 instanceof Select) {
            Select s2 = (Select) bi2;
            this.branches.add(bi2);
            StringBuffer args = new StringBuffer("new int[] { ");
            int[] matchs = s2.getMatchs();
            for (int i2 = 0; i2 < matchs.length; i2++) {
                args.append(matchs[i2]);
                if (i2 < matchs.length - 1) {
                    args.append(", ");
                }
            }
            args.append(" }");
            this._out.print("    Select " + name + " = new " + bi2.getName().toUpperCase() + "(" + ((Object) args) + ", new InstructionHandle[] { ");
            for (int i3 = 0; i3 < matchs.length; i3++) {
                this._out.print(FXMLLoader.NULL_KEYWORD);
                if (i3 < matchs.length - 1) {
                    this._out.print(", ");
                }
            }
            this._out.println(");");
        } else {
            int t_pos = bh2.getTarget().getPosition();
            if (pos > t_pos) {
                target = "ih_" + t_pos;
            } else {
                this.branches.add(bi2);
                target = FXMLLoader.NULL_KEYWORD;
            }
            this._out.println("    BranchInstruction " + name + " = _factory.createBranchInstruction(Constants." + bi2.getName().toUpperCase() + ", " + target + ");");
        }
        if (bh2.hasTargeters()) {
            this._out.println("    ih_" + pos + " = il.append(" + name + ");");
        } else {
            this._out.println("    il.append(" + name + ");");
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.EmptyVisitor, com.sun.org.apache.bcel.internal.generic.Visitor
    public void visitRET(RET i2) {
        this._out.println("il.append(new RET(" + i2.getIndex() + ")));");
    }

    private void updateBranchTargets() {
        Iterator i2 = this.branches.iterator();
        while (i2.hasNext()) {
            BranchInstruction bi2 = (BranchInstruction) i2.next();
            BranchHandle bh2 = (BranchHandle) this.branch_map.get(bi2);
            int pos = bh2.getPosition();
            String name = bi2.getName() + "_" + pos;
            int t_pos = bh2.getTarget().getPosition();
            this._out.println("    " + name + ".setTarget(ih_" + t_pos + ");");
            if (bi2 instanceof Select) {
                InstructionHandle[] ihs = ((Select) bi2).getTargets();
                for (int j2 = 0; j2 < ihs.length; j2++) {
                    int t_pos2 = ihs[j2].getPosition();
                    this._out.println("    " + name + ".setTarget(" + j2 + ", ih_" + t_pos2 + ");");
                }
            }
        }
    }

    private void updateExceptionHandlers() {
        CodeExceptionGen[] handlers = this._mg.getExceptionHandlers();
        for (CodeExceptionGen h2 : handlers) {
            String type = h2.getCatchType() == null ? FXMLLoader.NULL_KEYWORD : BCELifier.printType(h2.getCatchType());
            this._out.println("    method.addExceptionHandler(ih_" + h2.getStartPC().getPosition() + ", ih_" + h2.getEndPC().getPosition() + ", ih_" + h2.getHandlerPC().getPosition() + ", " + type + ");");
        }
    }
}
