package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/NamespaceAlias.class */
final class NamespaceAlias extends TopLevelElement {
    private String sPrefix;
    private String rPrefix;

    NamespaceAlias() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        this.sPrefix = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_STYLESHEET_PREFIX);
        this.rPrefix = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_RESULT_PREFIX);
        parser.getSymbolTable().addPrefixAlias(this.sPrefix, this.rPrefix);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }
}
