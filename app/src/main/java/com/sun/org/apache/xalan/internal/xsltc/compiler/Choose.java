package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Choose.class */
final class Choose extends Instruction {
    Choose() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("Choose");
        indent(indent + 4);
        displayContents(indent + 4);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        Vector whenElements = new Vector();
        Otherwise otherwise = null;
        Iterator<SyntaxTreeNode> elements = elements();
        getLineNumber();
        while (elements.hasNext()) {
            SyntaxTreeNode element = elements.next();
            if (element instanceof When) {
                whenElements.addElement(element);
            } else if (element instanceof Otherwise) {
                if (otherwise == null) {
                    otherwise = (Otherwise) element;
                } else {
                    ErrorMsg error = new ErrorMsg(ErrorMsg.MULTIPLE_OTHERWISE_ERR, (SyntaxTreeNode) this);
                    getParser().reportError(3, error);
                }
            } else if (element instanceof Text) {
                ((Text) element).ignore();
            } else {
                ErrorMsg error2 = new ErrorMsg(ErrorMsg.WHEN_ELEMENT_ERR, (SyntaxTreeNode) this);
                getParser().reportError(3, error2);
            }
        }
        if (whenElements.size() == 0) {
            ErrorMsg error3 = new ErrorMsg(ErrorMsg.MISSING_WHEN_ERR, (SyntaxTreeNode) this);
            getParser().reportError(3, error3);
            return;
        }
        InstructionList il = methodGen.getInstructionList();
        BranchHandle nextElement = null;
        Vector exitHandles = new Vector();
        InstructionHandle exit = null;
        Enumeration whens = whenElements.elements();
        while (whens.hasMoreElements()) {
            When when = (When) whens.nextElement2();
            Expression test = when.getTest();
            il.getEnd();
            if (nextElement != null) {
                nextElement.setTarget(il.append(NOP));
            }
            test.translateDesynthesized(classGen, methodGen);
            if (test instanceof FunctionCall) {
                FunctionCall call = (FunctionCall) test;
                try {
                    Type type = call.typeCheck(getParser().getSymbolTable());
                    if (type != Type.Boolean) {
                        test._falseList.add(il.append((BranchInstruction) new IFEQ(null)));
                    }
                } catch (TypeCheckError e2) {
                }
            }
            InstructionHandle truec = il.getEnd();
            if (!when.ignore()) {
                when.translateContents(classGen, methodGen);
            }
            exitHandles.addElement(il.append((BranchInstruction) new GOTO(null)));
            if (whens.hasMoreElements() || otherwise != null) {
                nextElement = il.append((BranchInstruction) new GOTO(null));
                test.backPatchFalseList(nextElement);
            } else {
                InstructionHandle instructionHandleAppend = il.append(NOP);
                exit = instructionHandleAppend;
                test.backPatchFalseList(instructionHandleAppend);
            }
            test.backPatchTrueList(truec.getNext());
        }
        if (otherwise != null) {
            nextElement.setTarget(il.append(NOP));
            otherwise.translateContents(classGen, methodGen);
            exit = il.append(NOP);
        }
        Enumeration exitGotos = exitHandles.elements();
        while (exitGotos.hasMoreElements()) {
            BranchHandle gotoExit = (BranchHandle) exitGotos.nextElement2();
            gotoExit.setTarget(exit);
        }
    }
}
