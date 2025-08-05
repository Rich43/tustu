package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.StringTokenizer;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/UseAttributeSets.class */
final class UseAttributeSets extends Instruction {
    private static final String ATTR_SET_NOT_FOUND = "";
    private final Vector _sets = new Vector(2);

    public UseAttributeSets(String setNames, Parser parser) {
        setParser(parser);
        addAttributeSets(setNames);
    }

    public void addAttributeSets(String setNames) {
        if (setNames != null && !setNames.equals("")) {
            StringTokenizer tokens = new StringTokenizer(setNames);
            while (tokens.hasMoreTokens()) {
                QName qname = getParser().getQNameIgnoreDefaultNs(tokens.nextToken());
                this._sets.add(qname);
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        SymbolTable symbolTable = getParser().getSymbolTable();
        for (int i2 = 0; i2 < this._sets.size(); i2++) {
            QName name = (QName) this._sets.elementAt(i2);
            AttributeSet attrs = symbolTable.lookupAttributeSet(name);
            if (attrs != null) {
                String methodName = attrs.getMethodName();
                il.append(classGen.loadTranslet());
                il.append(methodGen.loadDOM());
                il.append(methodGen.loadIterator());
                il.append(methodGen.loadHandler());
                il.append(methodGen.loadCurrentNode());
                int method = cpg.addMethodref(classGen.getClassName(), methodName, Constants.ATTR_SET_SIG);
                il.append(new INVOKESPECIAL(method));
            } else {
                Parser parser = getParser();
                String atrs = name.toString();
                reportError(this, parser, ErrorMsg.ATTRIBSET_UNDEF_ERR, atrs);
            }
        }
    }
}
