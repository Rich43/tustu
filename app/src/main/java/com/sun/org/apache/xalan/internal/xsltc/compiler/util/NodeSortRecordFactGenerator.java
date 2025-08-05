package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/NodeSortRecordFactGenerator.class */
public final class NodeSortRecordFactGenerator extends ClassGenerator {
    public NodeSortRecordFactGenerator(String className, String superClassName, String fileName, int accessFlags, String[] interfaces, Stylesheet stylesheet) {
        super(className, superClassName, fileName, accessFlags, interfaces, stylesheet);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator
    public boolean isExternal() {
        return true;
    }
}
