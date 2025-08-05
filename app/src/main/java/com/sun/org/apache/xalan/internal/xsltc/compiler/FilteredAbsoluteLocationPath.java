package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/FilteredAbsoluteLocationPath.class */
final class FilteredAbsoluteLocationPath extends Expression {
    private Expression _path;

    public FilteredAbsoluteLocationPath() {
        this._path = null;
    }

    public FilteredAbsoluteLocationPath(Expression path) {
        this._path = path;
        if (path != null) {
            this._path.setParent(this);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._path != null) {
            this._path.setParser(parser);
        }
    }

    public Expression getPath() {
        return this._path;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "FilteredAbsoluteLocationPath(" + (this._path != null ? this._path.toString() : FXMLLoader.NULL_KEYWORD) + ')';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._path != null) {
            Type ptype = this._path.typeCheck(stable);
            if (ptype instanceof NodeType) {
                this._path = new CastExpr(this._path, Type.NodeSet);
            }
        }
        Type type = Type.NodeSet;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._path != null) {
            int initDFI = cpg.addMethodref(Constants.DUP_FILTERED_ITERATOR, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)V");
            LocalVariableGen pathTemp = methodGen.addLocalVariable("filtered_absolute_location_path_tmp", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
            this._path.translate(classGen, methodGen);
            pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
            il.append(new NEW(cpg.addClass(Constants.DUP_FILTERED_ITERATOR)));
            il.append(DUP);
            pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
            il.append(new INVOKESPECIAL(initDFI));
            return;
        }
        int git = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(methodGen.loadDOM());
        il.append(new INVOKEINTERFACE(git, 1));
    }
}
