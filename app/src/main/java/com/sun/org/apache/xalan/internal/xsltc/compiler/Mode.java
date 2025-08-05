package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.DUP;
import com.sun.org.apache.bcel.internal.generic.GOTO_W;
import com.sun.org.apache.bcel.internal.generic.IFLT;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.SWITCH;
import com.sun.org.apache.bcel.internal.generic.TargetLostException;
import com.sun.org.apache.bcel.internal.generic.Type;
import com.sun.org.apache.bcel.internal.util.InstructionFinder;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NamedMethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Mode.class */
final class Mode implements Constants {
    private final QName _name;
    private final Stylesheet _stylesheet;
    private final String _methodName;
    private TestSeq[] _testSeq;
    private int _currentIndex;
    private Vector _childNodeGroup = null;
    private TestSeq _childNodeTestSeq = null;
    private Vector _attribNodeGroup = null;
    private TestSeq _attribNodeTestSeq = null;
    private Vector _idxGroup = null;
    private TestSeq _idxTestSeq = null;
    private Map<Template, Object> _neededTemplates = new HashMap();
    private Map<Template, Mode> _namedTemplates = new HashMap();
    private Map<Template, InstructionHandle> _templateIHs = new HashMap();
    private Map<Template, InstructionList> _templateILs = new HashMap();
    private LocationPathPattern _rootPattern = null;
    private Map<Integer, Integer> _importLevels = null;
    private Map<String, Key> _keys = null;
    private Vector _templates = new Vector();
    private Vector[] _patternGroups = new Vector[32];

    public Mode(QName name, Stylesheet stylesheet, String suffix) {
        this._name = name;
        this._stylesheet = stylesheet;
        this._methodName = Constants.APPLY_TEMPLATES + suffix;
    }

    public String functionName() {
        return this._methodName;
    }

    public String functionName(int min, int max) {
        if (this._importLevels == null) {
            this._importLevels = new HashMap();
        }
        this._importLevels.put(Integer.valueOf(max), Integer.valueOf(min));
        return this._methodName + '_' + max;
    }

    private String getClassName() {
        return this._stylesheet.getClassName();
    }

    public Stylesheet getStylesheet() {
        return this._stylesheet;
    }

    public void addTemplate(Template template) {
        this._templates.addElement(template);
    }

    private Vector quicksort(Vector templates, int p2, int r2) {
        if (p2 < r2) {
            int q2 = partition(templates, p2, r2);
            quicksort(templates, p2, q2);
            quicksort(templates, q2 + 1, r2);
        }
        return templates;
    }

    private int partition(Vector templates, int p2, int r2) {
        Template x2 = (Template) templates.elementAt(p2);
        int i2 = p2 - 1;
        int j2 = r2 + 1;
        while (true) {
            j2--;
            if (x2.compareTo((Template) templates.elementAt(j2)) <= 0) {
                do {
                    i2++;
                } while (x2.compareTo((Template) templates.elementAt(i2)) < 0);
                if (i2 < j2) {
                    templates.set(j2, templates.set(i2, templates.elementAt(j2)));
                } else {
                    return j2;
                }
            }
        }
    }

    public void processPatterns(Map<String, Key> keys) {
        this._keys = keys;
        this._templates = quicksort(this._templates, 0, this._templates.size() - 1);
        Enumeration templates = this._templates.elements();
        while (templates.hasMoreElements()) {
            Template template = (Template) templates.nextElement2();
            if (template.isNamed() && !template.disabled()) {
                this._namedTemplates.put(template, this);
            }
            Pattern pattern = template.getPattern();
            if (pattern != null) {
                flattenAlternative(pattern, template, keys);
            }
        }
        prepareTestSequences();
    }

    private void flattenAlternative(Pattern pattern, Template template, Map<String, Key> keys) {
        if (pattern instanceof IdKeyPattern) {
            IdKeyPattern idkey = (IdKeyPattern) pattern;
            idkey.setTemplate(template);
            if (this._idxGroup == null) {
                this._idxGroup = new Vector();
            }
            this._idxGroup.add(pattern);
            return;
        }
        if (pattern instanceof AlternativePattern) {
            AlternativePattern alt = (AlternativePattern) pattern;
            flattenAlternative(alt.getLeft(), template, keys);
            flattenAlternative(alt.getRight(), template, keys);
        } else if (pattern instanceof LocationPathPattern) {
            LocationPathPattern lpp = (LocationPathPattern) pattern;
            lpp.setTemplate(template);
            addPatternToGroup(lpp);
        }
    }

    private void addPatternToGroup(LocationPathPattern lpp) {
        if (lpp instanceof IdKeyPattern) {
            addPattern(-1, lpp);
            return;
        }
        StepPattern kernel = lpp.getKernelPattern();
        if (kernel != null) {
            addPattern(kernel.getNodeType(), lpp);
        } else if (this._rootPattern == null || lpp.noSmallerThan(this._rootPattern)) {
            this._rootPattern = lpp;
        }
    }

    private void addPattern(int kernelType, LocationPathPattern pattern) {
        Vector vector;
        Vector patterns;
        Vector vector2;
        Vector vector3;
        int oldLength = this._patternGroups.length;
        if (kernelType >= oldLength) {
            Vector[] newGroups = new Vector[kernelType * 2];
            System.arraycopy(this._patternGroups, 0, newGroups, 0, oldLength);
            this._patternGroups = newGroups;
        }
        if (kernelType == -1) {
            if (pattern.getAxis() == 2) {
                if (this._attribNodeGroup == null) {
                    Vector vector4 = new Vector(2);
                    vector3 = vector4;
                    this._attribNodeGroup = vector4;
                } else {
                    vector3 = this._attribNodeGroup;
                }
                patterns = vector3;
            } else {
                if (this._childNodeGroup == null) {
                    Vector vector5 = new Vector(2);
                    vector2 = vector5;
                    this._childNodeGroup = vector5;
                } else {
                    vector2 = this._childNodeGroup;
                }
                patterns = vector2;
            }
        } else {
            if (this._patternGroups[kernelType] == null) {
                Vector[] vectorArr = this._patternGroups;
                Vector vector6 = new Vector(2);
                vector = vector6;
                vectorArr[kernelType] = vector6;
            } else {
                vector = this._patternGroups[kernelType];
            }
            patterns = vector;
        }
        if (patterns.size() == 0) {
            patterns.addElement(pattern);
            return;
        }
        boolean inserted = false;
        int i2 = 0;
        while (true) {
            if (i2 >= patterns.size()) {
                break;
            }
            LocationPathPattern lppToCompare = (LocationPathPattern) patterns.elementAt(i2);
            if (!pattern.noSmallerThan(lppToCompare)) {
                i2++;
            } else {
                inserted = true;
                patterns.insertElementAt(pattern, i2);
                break;
            }
        }
        if (!inserted) {
            patterns.addElement(pattern);
        }
    }

    private void completeTestSequences(int nodeType, Vector patterns) {
        if (patterns != null) {
            if (this._patternGroups[nodeType] == null) {
                this._patternGroups[nodeType] = patterns;
                return;
            }
            int m2 = patterns.size();
            for (int j2 = 0; j2 < m2; j2++) {
                addPattern(nodeType, (LocationPathPattern) patterns.elementAt(j2));
            }
        }
    }

    private void prepareTestSequences() {
        Vector starGroup = this._patternGroups[1];
        Vector atStarGroup = this._patternGroups[2];
        completeTestSequences(3, this._childNodeGroup);
        completeTestSequences(1, this._childNodeGroup);
        completeTestSequences(7, this._childNodeGroup);
        completeTestSequences(8, this._childNodeGroup);
        completeTestSequences(2, this._attribNodeGroup);
        Vector names = this._stylesheet.getXSLTC().getNamesIndex();
        if (starGroup != null || atStarGroup != null || this._childNodeGroup != null || this._attribNodeGroup != null) {
            int n2 = this._patternGroups.length;
            for (int i2 = 14; i2 < n2; i2++) {
                if (this._patternGroups[i2] != null) {
                    String name = (String) names.elementAt(i2 - 14);
                    if (isAttributeName(name)) {
                        completeTestSequences(i2, atStarGroup);
                        completeTestSequences(i2, this._attribNodeGroup);
                    } else {
                        completeTestSequences(i2, starGroup);
                        completeTestSequences(i2, this._childNodeGroup);
                    }
                }
            }
        }
        this._testSeq = new TestSeq[14 + names.size()];
        int n3 = this._patternGroups.length;
        for (int i3 = 0; i3 < n3; i3++) {
            Vector patterns = this._patternGroups[i3];
            if (patterns != null) {
                TestSeq testSeq = new TestSeq(patterns, i3, this);
                testSeq.reduce();
                this._testSeq[i3] = testSeq;
                testSeq.findTemplates(this._neededTemplates);
            }
        }
        if (this._childNodeGroup != null && this._childNodeGroup.size() > 0) {
            this._childNodeTestSeq = new TestSeq(this._childNodeGroup, -1, this);
            this._childNodeTestSeq.reduce();
            this._childNodeTestSeq.findTemplates(this._neededTemplates);
        }
        if (this._idxGroup != null && this._idxGroup.size() > 0) {
            this._idxTestSeq = new TestSeq(this._idxGroup, this);
            this._idxTestSeq.reduce();
            this._idxTestSeq.findTemplates(this._neededTemplates);
        }
        if (this._rootPattern != null) {
            this._neededTemplates.put(this._rootPattern.getTemplate(), this);
        }
    }

    private void compileNamedTemplate(Template template, ClassGenerator classGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = new InstructionList();
        String methodName = Util.escape(template.getName().toString());
        int numParams = 0;
        if (template.isSimpleNamedTemplate()) {
            Vector parameters = template.getParameters();
            numParams = parameters.size();
        }
        Type[] types = new Type[4 + numParams];
        String[] names = new String[4 + numParams];
        types[0] = Util.getJCRefType(Constants.DOM_INTF_SIG);
        types[1] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        types[2] = Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
        types[3] = Type.INT;
        names[0] = Constants.DOCUMENT_PNAME;
        names[1] = Constants.ITERATOR_PNAME;
        names[2] = Constants.TRANSLET_OUTPUT_PNAME;
        names[3] = "node";
        for (int i2 = 4; i2 < 4 + numParams; i2++) {
            types[i2] = Util.getJCRefType(Constants.OBJECT_SIG);
            names[i2] = com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PARAMVARIABLE_STRING + String.valueOf(i2 - 4);
        }
        NamedMethodGenerator methodGen = new NamedMethodGenerator(1, Type.VOID, types, names, methodName, getClassName(), il, cpg);
        il.append(template.compile(classGen, methodGen));
        il.append(RETURN);
        classGen.addMethod(methodGen);
    }

    private void compileTemplates(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next) {
        Set<Template> templates = this._namedTemplates.keySet();
        Iterator<Template> it = templates.iterator();
        while (it.hasNext()) {
            compileNamedTemplate(it.next(), classGen);
        }
        Set<Template> templates2 = this._neededTemplates.keySet();
        for (Template template : templates2) {
            if (template.hasContents()) {
                InstructionList til = template.compile(classGen, methodGen);
                til.append((BranchInstruction) new GOTO_W(next));
                this._templateILs.put(template, til);
                this._templateIHs.put(template, til.getStart());
            } else {
                this._templateIHs.put(template, next);
            }
        }
    }

    private void appendTemplateCode(InstructionList body) {
        for (Template template : this._neededTemplates.keySet()) {
            InstructionList iList = this._templateILs.get(template);
            if (iList != null) {
                body.append(iList);
            }
        }
    }

    private void appendTestSequences(InstructionList body) {
        InstructionList il;
        int n2 = this._testSeq.length;
        for (int i2 = 0; i2 < n2; i2++) {
            TestSeq testSeq = this._testSeq[i2];
            if (testSeq != null && (il = testSeq.getInstructionList()) != null) {
                body.append(il);
            }
        }
    }

    public static void compileGetChildren(ClassGenerator classGen, MethodGenerator methodGen, int node) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int git = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_CHILDREN, "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(methodGen.loadDOM());
        il.append(new ILOAD(node));
        il.append(new INVOKEINTERFACE(git, 2));
    }

    private InstructionList compileDefaultRecursion(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = new InstructionList();
        String applyTemplatesSig = classGen.getApplyTemplatesSig();
        int git = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_CHILDREN, "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        int applyTemplates = cpg.addMethodref(getClassName(), functionName(), applyTemplatesSig);
        il.append(classGen.loadTranslet());
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadDOM());
        il.append(new ILOAD(this._currentIndex));
        il.append(new INVOKEINTERFACE(git, 2));
        il.append(methodGen.loadHandler());
        il.append(new INVOKEVIRTUAL(applyTemplates));
        il.append((BranchInstruction) new GOTO_W(next));
        return il;
    }

    private InstructionList compileDefaultText(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = new InstructionList();
        int chars = cpg.addInterfaceMethodref(Constants.DOM_INTF, "characters", Constants.CHARACTERS_SIG);
        il.append(methodGen.loadDOM());
        il.append(new ILOAD(this._currentIndex));
        il.append(methodGen.loadHandler());
        il.append(new INVOKEINTERFACE(chars, 3));
        il.append((BranchInstruction) new GOTO_W(next));
        return il;
    }

    private InstructionList compileNamespaces(ClassGenerator classGen, MethodGenerator methodGen, boolean[] isNamespace, boolean[] isAttribute, boolean attrFlag, InstructionHandle defaultTarget) {
        XSLTC xsltc = classGen.getParser().getXSLTC();
        ConstantPoolGen cpg = classGen.getConstantPool();
        Vector namespaces = xsltc.getNamespaceIndex();
        Vector names = xsltc.getNamesIndex();
        int namespaceCount = namespaces.size() + 1;
        int namesCount = names.size();
        InstructionList il = new InstructionList();
        int[] types = new int[namespaceCount];
        InstructionHandle[] targets = new InstructionHandle[types.length];
        if (namespaceCount > 0) {
            boolean compiled = false;
            for (int i2 = 0; i2 < namespaceCount; i2++) {
                targets[i2] = defaultTarget;
                types[i2] = i2;
            }
            for (int i3 = 14; i3 < 14 + namesCount; i3++) {
                if (isNamespace[i3] && isAttribute[i3] == attrFlag) {
                    String name = (String) names.elementAt(i3 - 14);
                    String namespace = name.substring(0, name.lastIndexOf(58));
                    int type = xsltc.registerNamespace(namespace);
                    if (i3 < this._testSeq.length && this._testSeq[i3] != null) {
                        targets[type] = this._testSeq[i3].compile(classGen, methodGen, defaultTarget);
                        compiled = true;
                    }
                }
            }
            if (!compiled) {
                return null;
            }
            int getNS = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNamespaceType", Constants.GET_PARENT_SIG);
            il.append(methodGen.loadDOM());
            il.append(new ILOAD(this._currentIndex));
            il.append(new INVOKEINTERFACE(getNS, 2));
            il.append(new SWITCH(types, targets, defaultTarget));
            return il;
        }
        return null;
    }

    public void compileApplyTemplates(ClassGenerator classGen) {
        XSLTC xsltc = classGen.getParser().getXSLTC();
        ConstantPoolGen cpg = classGen.getConstantPool();
        Vector names = xsltc.getNamesIndex();
        Type[] argTypes = {Util.getJCRefType(Constants.DOM_INTF_SIG), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;")};
        String[] argNames = {Constants.DOCUMENT_PNAME, Constants.ITERATOR_PNAME, Constants.TRANSLET_OUTPUT_PNAME};
        InstructionList mainIL = new InstructionList();
        MethodGenerator methodGen = new MethodGenerator(17, Type.VOID, argTypes, argNames, functionName(), getClassName(), mainIL, classGen.getConstantPool());
        methodGen.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
        mainIL.append(NOP);
        LocalVariableGen current = methodGen.addLocalVariable2(Keywords.FUNC_CURRENT_STRING, Type.INT, null);
        this._currentIndex = current.getIndex();
        InstructionList body = new InstructionList();
        body.append(NOP);
        InstructionList ilLoop = new InstructionList();
        ilLoop.append(methodGen.loadIterator());
        ilLoop.append(methodGen.nextNode());
        ilLoop.append(DUP);
        ilLoop.append(new ISTORE(this._currentIndex));
        BranchHandle ifeq = ilLoop.append((BranchInstruction) new IFLT(null));
        BranchHandle loop = ilLoop.append((BranchInstruction) new GOTO_W(null));
        ifeq.setTarget(ilLoop.append(RETURN));
        InstructionHandle ihLoop = ilLoop.getStart();
        current.setStart(mainIL.append((BranchInstruction) new GOTO_W(ihLoop)));
        current.setEnd(loop);
        InstructionList ilRecurse = compileDefaultRecursion(classGen, methodGen, ihLoop);
        InstructionHandle ihRecurse = ilRecurse.getStart();
        InstructionList ilText = compileDefaultText(classGen, methodGen, ihLoop);
        InstructionHandle ihText = ilText.getStart();
        int[] types = new int[14 + names.size()];
        for (int i2 = 0; i2 < types.length; i2++) {
            types[i2] = i2;
        }
        boolean[] isAttribute = new boolean[types.length];
        boolean[] isNamespace = new boolean[types.length];
        for (int i3 = 0; i3 < names.size(); i3++) {
            String name = (String) names.elementAt(i3);
            isAttribute[i3 + 14] = isAttributeName(name);
            isNamespace[i3 + 14] = isNamespaceName(name);
        }
        compileTemplates(classGen, methodGen, ihLoop);
        TestSeq elemTest = this._testSeq[1];
        InstructionHandle ihElem = ihRecurse;
        if (elemTest != null) {
            ihElem = elemTest.compile(classGen, methodGen, ihRecurse);
        }
        TestSeq attrTest = this._testSeq[2];
        InstructionHandle ihAttr = ihText;
        if (attrTest != null) {
            ihAttr = attrTest.compile(classGen, methodGen, ihAttr);
        }
        InstructionList ilKey = null;
        if (this._idxTestSeq != null) {
            loop.setTarget(this._idxTestSeq.compile(classGen, methodGen, body.getStart()));
            ilKey = this._idxTestSeq.getInstructionList();
        } else {
            loop.setTarget(body.getStart());
        }
        if (this._childNodeTestSeq != null) {
            double nodePrio = this._childNodeTestSeq.getPriority();
            int nodePos = this._childNodeTestSeq.getPosition();
            double elemPrio = -1.7976931348623157E308d;
            int elemPos = Integer.MIN_VALUE;
            if (elemTest != null) {
                elemPrio = elemTest.getPriority();
                elemPos = elemTest.getPosition();
            }
            if (elemPrio == Double.NaN || elemPrio < nodePrio || (elemPrio == nodePrio && elemPos < nodePos)) {
                ihElem = this._childNodeTestSeq.compile(classGen, methodGen, ihLoop);
            }
            TestSeq textTest = this._testSeq[3];
            double textPrio = -1.7976931348623157E308d;
            int textPos = Integer.MIN_VALUE;
            if (textTest != null) {
                textPrio = textTest.getPriority();
                textPos = textTest.getPosition();
            }
            if (textPrio == Double.NaN || textPrio < nodePrio || (textPrio == nodePrio && textPos < nodePos)) {
                ihText = this._childNodeTestSeq.compile(classGen, methodGen, ihLoop);
                this._testSeq[3] = this._childNodeTestSeq;
            }
        }
        InstructionHandle elemNamespaceHandle = ihElem;
        InstructionList nsElem = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, false, ihElem);
        if (nsElem != null) {
            elemNamespaceHandle = nsElem.getStart();
        }
        InstructionHandle attrNamespaceHandle = ihAttr;
        InstructionList nsAttr = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, true, ihAttr);
        if (nsAttr != null) {
            attrNamespaceHandle = nsAttr.getStart();
        }
        InstructionHandle[] targets = new InstructionHandle[types.length];
        for (int i4 = 14; i4 < targets.length; i4++) {
            TestSeq testSeq = this._testSeq[i4];
            if (isNamespace[i4]) {
                if (isAttribute[i4]) {
                    targets[i4] = attrNamespaceHandle;
                } else {
                    targets[i4] = elemNamespaceHandle;
                }
            } else if (testSeq != null) {
                if (isAttribute[i4]) {
                    targets[i4] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
                } else {
                    targets[i4] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
                }
            } else {
                targets[i4] = ihLoop;
            }
        }
        targets[0] = this._rootPattern != null ? getTemplateInstructionHandle(this._rootPattern.getTemplate()) : ihRecurse;
        targets[9] = this._rootPattern != null ? getTemplateInstructionHandle(this._rootPattern.getTemplate()) : ihRecurse;
        targets[3] = this._testSeq[3] != null ? this._testSeq[3].compile(classGen, methodGen, ihText) : ihText;
        targets[13] = ihLoop;
        targets[1] = elemNamespaceHandle;
        targets[2] = attrNamespaceHandle;
        InstructionHandle ihPI = ihLoop;
        if (this._childNodeTestSeq != null) {
            ihPI = ihElem;
        }
        if (this._testSeq[7] != null) {
            targets[7] = this._testSeq[7].compile(classGen, methodGen, ihPI);
        } else {
            targets[7] = ihPI;
        }
        InstructionHandle ihComment = ihLoop;
        if (this._childNodeTestSeq != null) {
            ihComment = ihElem;
        }
        targets[8] = this._testSeq[8] != null ? this._testSeq[8].compile(classGen, methodGen, ihComment) : ihComment;
        targets[4] = ihLoop;
        targets[11] = ihLoop;
        targets[10] = ihLoop;
        targets[6] = ihLoop;
        targets[5] = ihLoop;
        targets[12] = ihLoop;
        for (int i5 = 14; i5 < targets.length; i5++) {
            TestSeq testSeq2 = this._testSeq[i5];
            if (testSeq2 == null || isNamespace[i5]) {
                if (isAttribute[i5]) {
                    targets[i5] = attrNamespaceHandle;
                } else {
                    targets[i5] = elemNamespaceHandle;
                }
            } else if (isAttribute[i5]) {
                targets[i5] = testSeq2.compile(classGen, methodGen, attrNamespaceHandle);
            } else {
                targets[i5] = testSeq2.compile(classGen, methodGen, elemNamespaceHandle);
            }
        }
        if (ilKey != null) {
            body.insert(ilKey);
        }
        int getType = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getExpandedTypeID", Constants.GET_PARENT_SIG);
        body.append(methodGen.loadDOM());
        body.append(new ILOAD(this._currentIndex));
        body.append(new INVOKEINTERFACE(getType, 2));
        body.append(new SWITCH(types, targets, ihLoop));
        appendTestSequences(body);
        appendTemplateCode(body);
        if (nsElem != null) {
            body.append(nsElem);
        }
        if (nsAttr != null) {
            body.append(nsAttr);
        }
        body.append(ilRecurse);
        body.append(ilText);
        mainIL.append(body);
        mainIL.append(ilLoop);
        peepHoleOptimization(methodGen);
        classGen.addMethod(methodGen);
        if (this._importLevels != null) {
            for (Map.Entry<Integer, Integer> entry : this._importLevels.entrySet()) {
                compileApplyImports(classGen, entry.getValue().intValue(), entry.getKey().intValue());
            }
        }
    }

    private void compileTemplateCalls(ClassGenerator classGen, MethodGenerator methodGen, InstructionHandle next, int min, int max) {
        for (Template template : this._neededTemplates.keySet()) {
            int prec = template.getImportPrecedence();
            if (prec >= min && prec < max) {
                if (template.hasContents()) {
                    InstructionList til = template.compile(classGen, methodGen);
                    til.append((BranchInstruction) new GOTO_W(next));
                    this._templateILs.put(template, til);
                    this._templateIHs.put(template, til.getStart());
                } else {
                    this._templateIHs.put(template, next);
                }
            }
        }
    }

    public void compileApplyImports(ClassGenerator classGen, int min, int max) {
        XSLTC xsltc = classGen.getParser().getXSLTC();
        ConstantPoolGen cpg = classGen.getConstantPool();
        Vector names = xsltc.getNamesIndex();
        this._namedTemplates = new HashMap();
        this._neededTemplates = new HashMap();
        this._templateIHs = new HashMap();
        this._templateILs = new HashMap();
        this._patternGroups = new Vector[32];
        this._rootPattern = null;
        Vector oldTemplates = this._templates;
        this._templates = new Vector();
        Enumeration templates = oldTemplates.elements();
        while (templates.hasMoreElements()) {
            Template template = (Template) templates.nextElement2();
            int prec = template.getImportPrecedence();
            if (prec >= min && prec < max) {
                addTemplate(template);
            }
        }
        processPatterns(this._keys);
        Type[] argTypes = {Util.getJCRefType(Constants.DOM_INTF_SIG), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;"), Type.INT};
        String[] argNames = {Constants.DOCUMENT_PNAME, Constants.ITERATOR_PNAME, Constants.TRANSLET_OUTPUT_PNAME, "node"};
        InstructionList mainIL = new InstructionList();
        MethodGenerator methodGen = new MethodGenerator(17, Type.VOID, argTypes, argNames, functionName() + '_' + max, getClassName(), mainIL, classGen.getConstantPool());
        methodGen.addException("com.sun.org.apache.xalan.internal.xsltc.TransletException");
        LocalVariableGen current = methodGen.addLocalVariable2(Keywords.FUNC_CURRENT_STRING, Type.INT, null);
        this._currentIndex = current.getIndex();
        mainIL.append(new ILOAD(methodGen.getLocalIndex("node")));
        current.setStart(mainIL.append(new ISTORE(this._currentIndex)));
        InstructionList body = new InstructionList();
        body.append(NOP);
        InstructionList ilLoop = new InstructionList();
        ilLoop.append(RETURN);
        InstructionHandle ihLoop = ilLoop.getStart();
        InstructionList ilRecurse = compileDefaultRecursion(classGen, methodGen, ihLoop);
        InstructionHandle ihRecurse = ilRecurse.getStart();
        InstructionList ilText = compileDefaultText(classGen, methodGen, ihLoop);
        InstructionHandle ihText = ilText.getStart();
        int[] types = new int[14 + names.size()];
        for (int i2 = 0; i2 < types.length; i2++) {
            types[i2] = i2;
        }
        boolean[] isAttribute = new boolean[types.length];
        boolean[] isNamespace = new boolean[types.length];
        for (int i3 = 0; i3 < names.size(); i3++) {
            String name = (String) names.elementAt(i3);
            isAttribute[i3 + 14] = isAttributeName(name);
            isNamespace[i3 + 14] = isNamespaceName(name);
        }
        compileTemplateCalls(classGen, methodGen, ihLoop, min, max);
        TestSeq elemTest = this._testSeq[1];
        InstructionHandle ihElem = ihRecurse;
        if (elemTest != null) {
            ihElem = elemTest.compile(classGen, methodGen, ihLoop);
        }
        TestSeq attrTest = this._testSeq[2];
        InstructionHandle ihAttr = ihLoop;
        if (attrTest != null) {
            ihAttr = attrTest.compile(classGen, methodGen, ihAttr);
        }
        InstructionList ilKey = null;
        if (this._idxTestSeq != null) {
            ilKey = this._idxTestSeq.getInstructionList();
        }
        if (this._childNodeTestSeq != null) {
            double nodePrio = this._childNodeTestSeq.getPriority();
            int nodePos = this._childNodeTestSeq.getPosition();
            double elemPrio = -1.7976931348623157E308d;
            int elemPos = Integer.MIN_VALUE;
            if (elemTest != null) {
                elemPrio = elemTest.getPriority();
                elemPos = elemTest.getPosition();
            }
            if (elemPrio == Double.NaN || elemPrio < nodePrio || (elemPrio == nodePrio && elemPos < nodePos)) {
                ihElem = this._childNodeTestSeq.compile(classGen, methodGen, ihLoop);
            }
            TestSeq textTest = this._testSeq[3];
            double textPrio = -1.7976931348623157E308d;
            int textPos = Integer.MIN_VALUE;
            if (textTest != null) {
                textPrio = textTest.getPriority();
                textPos = textTest.getPosition();
            }
            if (textPrio == Double.NaN || textPrio < nodePrio || (textPrio == nodePrio && textPos < nodePos)) {
                ihText = this._childNodeTestSeq.compile(classGen, methodGen, ihLoop);
                this._testSeq[3] = this._childNodeTestSeq;
            }
        }
        InstructionHandle elemNamespaceHandle = ihElem;
        InstructionList nsElem = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, false, ihElem);
        if (nsElem != null) {
            elemNamespaceHandle = nsElem.getStart();
        }
        InstructionList nsAttr = compileNamespaces(classGen, methodGen, isNamespace, isAttribute, true, ihAttr);
        InstructionHandle attrNamespaceHandle = ihAttr;
        if (nsAttr != null) {
            attrNamespaceHandle = nsAttr.getStart();
        }
        InstructionHandle[] targets = new InstructionHandle[types.length];
        for (int i4 = 14; i4 < targets.length; i4++) {
            TestSeq testSeq = this._testSeq[i4];
            if (isNamespace[i4]) {
                if (isAttribute[i4]) {
                    targets[i4] = attrNamespaceHandle;
                } else {
                    targets[i4] = elemNamespaceHandle;
                }
            } else if (testSeq != null) {
                if (isAttribute[i4]) {
                    targets[i4] = testSeq.compile(classGen, methodGen, attrNamespaceHandle);
                } else {
                    targets[i4] = testSeq.compile(classGen, methodGen, elemNamespaceHandle);
                }
            } else {
                targets[i4] = ihLoop;
            }
        }
        targets[0] = this._rootPattern != null ? getTemplateInstructionHandle(this._rootPattern.getTemplate()) : ihRecurse;
        targets[9] = this._rootPattern != null ? getTemplateInstructionHandle(this._rootPattern.getTemplate()) : ihRecurse;
        targets[3] = this._testSeq[3] != null ? this._testSeq[3].compile(classGen, methodGen, ihText) : ihText;
        targets[13] = ihLoop;
        targets[1] = elemNamespaceHandle;
        targets[2] = attrNamespaceHandle;
        InstructionHandle ihPI = ihLoop;
        if (this._childNodeTestSeq != null) {
            ihPI = ihElem;
        }
        if (this._testSeq[7] != null) {
            targets[7] = this._testSeq[7].compile(classGen, methodGen, ihPI);
        } else {
            targets[7] = ihPI;
        }
        InstructionHandle ihComment = ihLoop;
        if (this._childNodeTestSeq != null) {
            ihComment = ihElem;
        }
        targets[8] = this._testSeq[8] != null ? this._testSeq[8].compile(classGen, methodGen, ihComment) : ihComment;
        targets[4] = ihLoop;
        targets[11] = ihLoop;
        targets[10] = ihLoop;
        targets[6] = ihLoop;
        targets[5] = ihLoop;
        targets[12] = ihLoop;
        for (int i5 = 14; i5 < targets.length; i5++) {
            TestSeq testSeq2 = this._testSeq[i5];
            if (testSeq2 == null || isNamespace[i5]) {
                if (isAttribute[i5]) {
                    targets[i5] = attrNamespaceHandle;
                } else {
                    targets[i5] = elemNamespaceHandle;
                }
            } else if (isAttribute[i5]) {
                targets[i5] = testSeq2.compile(classGen, methodGen, attrNamespaceHandle);
            } else {
                targets[i5] = testSeq2.compile(classGen, methodGen, elemNamespaceHandle);
            }
        }
        if (ilKey != null) {
            body.insert(ilKey);
        }
        int getType = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getExpandedTypeID", Constants.GET_PARENT_SIG);
        body.append(methodGen.loadDOM());
        body.append(new ILOAD(this._currentIndex));
        body.append(new INVOKEINTERFACE(getType, 2));
        body.append(new SWITCH(types, targets, ihLoop));
        appendTestSequences(body);
        appendTemplateCode(body);
        if (nsElem != null) {
            body.append(nsElem);
        }
        if (nsAttr != null) {
            body.append(nsAttr);
        }
        body.append(ilRecurse);
        body.append(ilText);
        mainIL.append(body);
        current.setEnd(body.getEnd());
        mainIL.append(ilLoop);
        peepHoleOptimization(methodGen);
        classGen.addMethod(methodGen);
        this._templates = oldTemplates;
    }

    private void peepHoleOptimization(MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        InstructionFinder find = new InstructionFinder(il);
        Iterator iter = find.search("loadinstruction pop");
        while (iter.hasNext()) {
            InstructionHandle[] match = (InstructionHandle[]) iter.next();
            try {
                if (!match[0].hasTargeters() && !match[1].hasTargeters()) {
                    il.delete(match[0], match[1]);
                }
            } catch (TargetLostException e2) {
            }
        }
        Iterator iter2 = find.search("iload iload swap istore");
        while (iter2.hasNext()) {
            InstructionHandle[] match2 = (InstructionHandle[]) iter2.next();
            try {
                ILOAD iload1 = (ILOAD) match2[0].getInstruction();
                ILOAD iload2 = (ILOAD) match2[1].getInstruction();
                ISTORE istore = (ISTORE) match2[3].getInstruction();
                if (!match2[1].hasTargeters() && !match2[2].hasTargeters() && !match2[3].hasTargeters() && iload1.getIndex() == iload2.getIndex() && iload2.getIndex() == istore.getIndex()) {
                    il.delete(match2[1], match2[3]);
                }
            } catch (TargetLostException e3) {
            }
        }
        Iterator iter3 = find.search("loadinstruction loadinstruction swap");
        while (iter3.hasNext()) {
            InstructionHandle[] match3 = (InstructionHandle[]) iter3.next();
            try {
                if (!match3[0].hasTargeters() && !match3[1].hasTargeters() && !match3[2].hasTargeters()) {
                    com.sun.org.apache.bcel.internal.generic.Instruction load_m = match3[1].getInstruction();
                    il.insert(match3[0], load_m);
                    il.delete(match3[1], match3[2]);
                }
            } catch (TargetLostException e4) {
            }
        }
        Iterator iter4 = find.search("aload aload");
        while (iter4.hasNext()) {
            InstructionHandle[] match4 = (InstructionHandle[]) iter4.next();
            try {
                if (!match4[1].hasTargeters()) {
                    ALOAD aload1 = (ALOAD) match4[0].getInstruction();
                    ALOAD aload2 = (ALOAD) match4[1].getInstruction();
                    if (aload1.getIndex() == aload2.getIndex()) {
                        il.insert(match4[1], new DUP());
                        il.delete(match4[1]);
                    }
                }
            } catch (TargetLostException e5) {
            }
        }
    }

    public InstructionHandle getTemplateInstructionHandle(Template template) {
        return this._templateIHs.get(template);
    }

    private static boolean isAttributeName(String qname) {
        int col = qname.lastIndexOf(58) + 1;
        return qname.charAt(col) == '@';
    }

    private static boolean isNamespaceName(String qname) {
        int col = qname.lastIndexOf(58);
        return col > -1 && qname.charAt(qname.length() - 1) == '*';
    }
}
