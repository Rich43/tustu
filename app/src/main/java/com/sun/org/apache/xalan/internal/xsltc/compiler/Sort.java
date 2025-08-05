package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.NOP;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSortRecordFactGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSortRecordGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.ArrayList;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Sort.class */
final class Sort extends Instruction implements Closure {
    private Expression _select;
    private AttributeValue _order;
    private AttributeValue _caseOrder;
    private AttributeValue _dataType;
    private AttributeValue _lang;
    private String _className = null;
    private ArrayList<VariableRefBase> _closureVars = null;
    private boolean _needsSortRecordFactory = false;

    Sort() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public boolean inInnerClass() {
        return this._className != null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public Closure getParentClosure() {
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public String getInnerClassName() {
        return this._className;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public void addVariable(VariableRefBase variableRef) {
        if (this._closureVars == null) {
            this._closureVars = new ArrayList<>();
        }
        if (!this._closureVars.contains(variableRef)) {
            this._closureVars.add(variableRef);
            this._needsSortRecordFactory = true;
        }
    }

    private void setInnerClassName(String className) {
        this._className = className;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        SyntaxTreeNode parent = getParent();
        if (!(parent instanceof ApplyTemplates) && !(parent instanceof ForEach)) {
            reportError(this, parser, ErrorMsg.STRAY_SORT_ERR, null);
            return;
        }
        this._select = parser.parseExpression(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, "string(.)");
        String val = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ORDER);
        if (val.length() == 0) {
            val = com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_ORDER_ASCENDING;
        }
        this._order = AttributeValue.create(this, val, parser);
        String val2 = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_DATATYPE);
        if (val2.length() == 0) {
            try {
                Type type = this._select.typeCheck(parser.getSymbolTable());
                if (type instanceof IntType) {
                    val2 = "number";
                } else {
                    val2 = "text";
                }
            } catch (TypeCheckError e2) {
                val2 = "text";
            }
        }
        this._dataType = AttributeValue.create(this, val2, parser);
        this._lang = AttributeValue.create(this, getAttribute("lang"), parser);
        this._caseOrder = AttributeValue.create(this, getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_CASEORDER), parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type tselect = this._select.typeCheck(stable);
        if (!(tselect instanceof StringType)) {
            this._select = new CastExpr(this._select, Type.String);
        }
        this._order.typeCheck(stable);
        this._caseOrder.typeCheck(stable);
        this._dataType.typeCheck(stable);
        this._lang.typeCheck(stable);
        return Type.Void;
    }

    public void translateSortType(ClassGenerator classGen, MethodGenerator methodGen) {
        this._dataType.translate(classGen, methodGen);
    }

    public void translateSortOrder(ClassGenerator classGen, MethodGenerator methodGen) {
        this._order.translate(classGen, methodGen);
    }

    public void translateCaseOrder(ClassGenerator classGen, MethodGenerator methodGen) {
        this._caseOrder.translate(classGen, methodGen);
    }

    public void translateLang(ClassGenerator classGen, MethodGenerator methodGen) {
        this._lang.translate(classGen, methodGen);
    }

    public void translateSelect(ClassGenerator classGen, MethodGenerator methodGen) {
        this._select.translate(classGen, methodGen);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }

    public static void translateSortIterator(ClassGenerator classGen, MethodGenerator methodGen, Expression nodeSet, Vector<Sort> sortObjects) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int init = cpg.addMethodref(Constants.SORT_ITERATOR, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecordFactory;)V");
        LocalVariableGen nodesTemp = methodGen.addLocalVariable("sort_tmp1", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        LocalVariableGen sortRecordFactoryTemp = methodGen.addLocalVariable("sort_tmp2", Util.getJCRefType(Constants.NODE_SORT_FACTORY_SIG), null, null);
        if (nodeSet == null) {
            int children = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getAxisIterator", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(methodGen.loadDOM());
            il.append(new PUSH(cpg, 3));
            il.append(new INVOKEINTERFACE(children, 2));
        } else {
            nodeSet.translate(classGen, methodGen);
        }
        nodesTemp.setStart(il.append(new ASTORE(nodesTemp.getIndex())));
        compileSortRecordFactory(sortObjects, classGen, methodGen);
        sortRecordFactoryTemp.setStart(il.append(new ASTORE(sortRecordFactoryTemp.getIndex())));
        il.append(new NEW(cpg.addClass(Constants.SORT_ITERATOR)));
        il.append(DUP);
        nodesTemp.setEnd(il.append(new ALOAD(nodesTemp.getIndex())));
        sortRecordFactoryTemp.setEnd(il.append(new ALOAD(sortRecordFactoryTemp.getIndex())));
        il.append(new INVOKESPECIAL(init));
    }

    public static void compileSortRecordFactory(Vector<Sort> sortObjects, ClassGenerator classGen, MethodGenerator methodGen) {
        String sortRecordClass = compileSortRecord(sortObjects, classGen, methodGen);
        boolean needsSortRecordFactory = false;
        int nsorts = sortObjects.size();
        for (int i2 = 0; i2 < nsorts; i2++) {
            needsSortRecordFactory |= sortObjects.elementAt(i2)._needsSortRecordFactory;
        }
        String sortRecordFactoryClass = Constants.NODE_SORT_FACTORY;
        if (needsSortRecordFactory) {
            sortRecordFactoryClass = compileSortRecordFactory(sortObjects, classGen, methodGen, sortRecordClass);
        }
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        LocalVariableGen sortOrderTemp = methodGen.addLocalVariable("sort_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
        il.append(new PUSH(cpg, nsorts));
        il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
        for (int level = 0; level < nsorts; level++) {
            Sort sort = sortObjects.elementAt(level);
            il.append(DUP);
            il.append(new PUSH(cpg, level));
            sort.translateSortOrder(classGen, methodGen);
            il.append(AASTORE);
        }
        sortOrderTemp.setStart(il.append(new ASTORE(sortOrderTemp.getIndex())));
        LocalVariableGen sortTypeTemp = methodGen.addLocalVariable("sort_type_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
        il.append(new PUSH(cpg, nsorts));
        il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
        for (int level2 = 0; level2 < nsorts; level2++) {
            Sort sort2 = sortObjects.elementAt(level2);
            il.append(DUP);
            il.append(new PUSH(cpg, level2));
            sort2.translateSortType(classGen, methodGen);
            il.append(AASTORE);
        }
        sortTypeTemp.setStart(il.append(new ASTORE(sortTypeTemp.getIndex())));
        LocalVariableGen sortLangTemp = methodGen.addLocalVariable("sort_lang_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
        il.append(new PUSH(cpg, nsorts));
        il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
        for (int level3 = 0; level3 < nsorts; level3++) {
            Sort sort3 = sortObjects.elementAt(level3);
            il.append(DUP);
            il.append(new PUSH(cpg, level3));
            sort3.translateLang(classGen, methodGen);
            il.append(AASTORE);
        }
        sortLangTemp.setStart(il.append(new ASTORE(sortLangTemp.getIndex())));
        LocalVariableGen sortCaseOrderTemp = methodGen.addLocalVariable("sort_case_order_tmp", Util.getJCRefType("[Ljava/lang/String;"), null, null);
        il.append(new PUSH(cpg, nsorts));
        il.append(new ANEWARRAY(cpg.addClass("java.lang.String")));
        for (int level4 = 0; level4 < nsorts; level4++) {
            Sort sort4 = sortObjects.elementAt(level4);
            il.append(DUP);
            il.append(new PUSH(cpg, level4));
            sort4.translateCaseOrder(classGen, methodGen);
            il.append(AASTORE);
        }
        sortCaseOrderTemp.setStart(il.append(new ASTORE(sortCaseOrderTemp.getIndex())));
        il.append(new NEW(cpg.addClass(sortRecordFactoryClass)));
        il.append(DUP);
        il.append(methodGen.loadDOM());
        il.append(new PUSH(cpg, sortRecordClass));
        il.append(classGen.loadTranslet());
        sortOrderTemp.setEnd(il.append(new ALOAD(sortOrderTemp.getIndex())));
        sortTypeTemp.setEnd(il.append(new ALOAD(sortTypeTemp.getIndex())));
        sortLangTemp.setEnd(il.append(new ALOAD(sortLangTemp.getIndex())));
        sortCaseOrderTemp.setEnd(il.append(new ALOAD(sortCaseOrderTemp.getIndex())));
        il.append(new INVOKESPECIAL(cpg.addMethodref(sortRecordFactoryClass, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V")));
        ArrayList<VariableRefBase> dups = new ArrayList<>();
        for (int j2 = 0; j2 < nsorts; j2++) {
            Sort sort5 = sortObjects.get(j2);
            int length = sort5._closureVars == null ? 0 : sort5._closureVars.size();
            for (int i3 = 0; i3 < length; i3++) {
                VariableRefBase varRef = sort5._closureVars.get(i3);
                if (!dups.contains(varRef)) {
                    VariableBase var = varRef.getVariable();
                    il.append(DUP);
                    il.append(var.loadInstruction());
                    il.append(new PUTFIELD(cpg.addFieldref(sortRecordFactoryClass, var.getEscapedName(), var.getType().toSignature())));
                    dups.add(varRef);
                }
            }
        }
    }

    public static String compileSortRecordFactory(Vector<Sort> sortObjects, ClassGenerator classGen, MethodGenerator methodGen, String sortRecordClass) {
        XSLTC xsltc = sortObjects.firstElement().getXSLTC();
        String className = xsltc.getHelperClassName();
        NodeSortRecordFactGenerator sortRecordFactory = new NodeSortRecordFactGenerator(className, Constants.NODE_SORT_FACTORY, className + ".java", 49, new String[0], classGen.getStylesheet());
        ConstantPoolGen cpg = sortRecordFactory.getConstantPool();
        int nsorts = sortObjects.size();
        ArrayList<VariableRefBase> dups = new ArrayList<>();
        for (int j2 = 0; j2 < nsorts; j2++) {
            Sort sort = sortObjects.get(j2);
            int length = sort._closureVars == null ? 0 : sort._closureVars.size();
            for (int i2 = 0; i2 < length; i2++) {
                VariableRefBase varRef = sort._closureVars.get(i2);
                if (!dups.contains(varRef)) {
                    VariableBase var = varRef.getVariable();
                    sortRecordFactory.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
                    dups.add(varRef);
                }
            }
        }
        com.sun.org.apache.bcel.internal.generic.Type[] argTypes = {Util.getJCRefType(Constants.DOM_INTF_SIG), Util.getJCRefType(Constants.STRING_SIG), Util.getJCRefType(Constants.TRANSLET_INTF_SIG), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;"), Util.getJCRefType("[Ljava/lang/String;")};
        String[] argNames = {Constants.DOCUMENT_PNAME, "className", "translet", com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_ORDER, "type", "lang", "case_order"};
        InstructionList il = new InstructionList();
        MethodGenerator constructor = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, className, il, cpg);
        il.append(ALOAD_0);
        il.append(ALOAD_1);
        il.append(ALOAD_2);
        il.append(new ALOAD(3));
        il.append(new ALOAD(4));
        il.append(new ALOAD(5));
        il.append(new ALOAD(6));
        il.append(new ALOAD(7));
        il.append(new INVOKESPECIAL(cpg.addMethodref(Constants.NODE_SORT_FACTORY, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/Translet;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V")));
        il.append(RETURN);
        InstructionList il2 = new InstructionList();
        MethodGenerator makeNodeSortRecord = new MethodGenerator(1, Util.getJCRefType(Constants.NODE_SORT_RECORD_SIG), new com.sun.org.apache.bcel.internal.generic.Type[]{com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT}, new String[]{"node", Keywords.FUNC_LAST_STRING}, "makeNodeSortRecord", className, il2, cpg);
        il2.append(ALOAD_0);
        il2.append(ILOAD_1);
        il2.append(ILOAD_2);
        il2.append(new INVOKESPECIAL(cpg.addMethodref(Constants.NODE_SORT_FACTORY, "makeNodeSortRecord", "(II)Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecord;")));
        il2.append(DUP);
        il2.append(new CHECKCAST(cpg.addClass(sortRecordClass)));
        int ndups = dups.size();
        for (int i3 = 0; i3 < ndups; i3++) {
            VariableBase var2 = dups.get(i3).getVariable();
            Type varType = var2.getType();
            il2.append(DUP);
            il2.append(ALOAD_0);
            il2.append(new GETFIELD(cpg.addFieldref(className, var2.getEscapedName(), varType.toSignature())));
            il2.append(new PUTFIELD(cpg.addFieldref(sortRecordClass, var2.getEscapedName(), varType.toSignature())));
        }
        il2.append(POP);
        il2.append(ARETURN);
        constructor.setMaxLocals();
        constructor.setMaxStack();
        sortRecordFactory.addMethod(constructor);
        makeNodeSortRecord.setMaxLocals();
        makeNodeSortRecord.setMaxStack();
        sortRecordFactory.addMethod(makeNodeSortRecord);
        xsltc.dumpClass(sortRecordFactory.getJavaClass());
        return className;
    }

    private static String compileSortRecord(Vector<Sort> sortObjects, ClassGenerator classGen, MethodGenerator methodGen) {
        XSLTC xsltc = sortObjects.firstElement().getXSLTC();
        String className = xsltc.getHelperClassName();
        NodeSortRecordGenerator sortRecord = new NodeSortRecordGenerator(className, Constants.NODE_SORT_RECORD, "sort$0.java", 49, new String[0], classGen.getStylesheet());
        ConstantPoolGen cpg = sortRecord.getConstantPool();
        int nsorts = sortObjects.size();
        ArrayList<VariableRefBase> dups = new ArrayList<>();
        for (int j2 = 0; j2 < nsorts; j2++) {
            Sort sort = sortObjects.get(j2);
            sort.setInnerClassName(className);
            int length = sort._closureVars == null ? 0 : sort._closureVars.size();
            for (int i2 = 0; i2 < length; i2++) {
                VariableRefBase varRef = sort._closureVars.get(i2);
                if (!dups.contains(varRef)) {
                    VariableBase var = varRef.getVariable();
                    sortRecord.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
                    dups.add(varRef);
                }
            }
        }
        MethodGenerator init = compileInit(sortRecord, cpg, className);
        MethodGenerator extract = compileExtract(sortObjects, sortRecord, cpg, className);
        sortRecord.addMethod(init);
        sortRecord.addMethod(extract);
        xsltc.dumpClass(sortRecord.getJavaClass());
        return className;
    }

    private static MethodGenerator compileInit(NodeSortRecordGenerator sortRecord, ConstantPoolGen cpg, String className) {
        InstructionList il = new InstructionList();
        MethodGenerator init = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, null, null, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, className, il, cpg);
        il.append(ALOAD_0);
        il.append(new INVOKESPECIAL(cpg.addMethodref(Constants.NODE_SORT_RECORD, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V")));
        il.append(RETURN);
        return init;
    }

    private static MethodGenerator compileExtract(Vector<Sort> sortObjects, NodeSortRecordGenerator sortRecord, ConstantPoolGen cpg, String className) {
        InstructionList il = new InstructionList();
        CompareGenerator extractMethod = new CompareGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.STRING, new com.sun.org.apache.bcel.internal.generic.Type[]{Util.getJCRefType(Constants.DOM_INTF_SIG), com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT, Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;"), com.sun.org.apache.bcel.internal.generic.Type.INT}, new String[]{Constants.DOM_PNAME, Keywords.FUNC_CURRENT_STRING, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_LEVEL, "translet", Keywords.FUNC_LAST_STRING}, "extractValueFromDOM", className, il, cpg);
        int levels = sortObjects.size();
        int[] match = new int[levels];
        InstructionHandle[] target = new InstructionHandle[levels];
        InstructionHandle tblswitch = null;
        if (levels > 1) {
            il.append(new ILOAD(extractMethod.getLocalIndex(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_LEVEL)));
            tblswitch = il.append(new NOP());
        }
        for (int level = 0; level < levels; level++) {
            match[level] = level;
            Sort sort = sortObjects.elementAt(level);
            target[level] = il.append(NOP);
            sort.translateSelect(sortRecord, extractMethod);
            il.append(ARETURN);
        }
        if (levels > 1) {
            InstructionHandle defaultTarget = il.append(new PUSH(cpg, ""));
            il.insert(tblswitch, (BranchInstruction) new TABLESWITCH(match, target, defaultTarget));
            il.append(ARETURN);
        }
        return extractMethod;
    }
}
