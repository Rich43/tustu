package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.DLOAD;
import com.sun.org.apache.bcel.internal.generic.DSTORE;
import com.sun.org.apache.bcel.internal.generic.FLOAD;
import com.sun.org.apache.bcel.internal.generic.FSTORE;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.ICONST;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.IfInstruction;
import com.sun.org.apache.bcel.internal.generic.IndexedInstruction;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.InstructionTargeter;
import com.sun.org.apache.bcel.internal.generic.LLOAD;
import com.sun.org.apache.bcel.internal.generic.LSTORE;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction;
import com.sun.org.apache.bcel.internal.generic.MethodGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.bcel.internal.generic.RET;
import com.sun.org.apache.bcel.internal.generic.Select;
import com.sun.org.apache.bcel.internal.generic.TargetLostException;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/MethodGenerator.class */
public class MethodGenerator extends MethodGen implements Constants {
    protected static final int INVALID_INDEX = -1;
    private static final String START_ELEMENT_SIG = "(Ljava/lang/String;)V";
    private static final String END_ELEMENT_SIG = "(Ljava/lang/String;)V";
    private InstructionList _mapTypeSub;
    private static final int DOM_INDEX = 1;
    private static final int ITERATOR_INDEX = 2;
    private static final int HANDLER_INDEX = 3;
    private static final int MAX_METHOD_SIZE = 65535;
    private static final int MAX_BRANCH_TARGET_OFFSET = 32767;
    private static final int MIN_BRANCH_TARGET_OFFSET = -32768;
    private static final int TARGET_METHOD_SIZE = 60000;
    private static final int MINIMUM_OUTLINEABLE_CHUNK_SIZE = 1000;
    private Instruction _iloadCurrent;
    private Instruction _istoreCurrent;
    private final Instruction _astoreHandler;
    private final Instruction _aloadHandler;
    private final Instruction _astoreIterator;
    private final Instruction _aloadIterator;
    private final Instruction _aloadDom;
    private final Instruction _astoreDom;
    private final Instruction _startElement;
    private final Instruction _endElement;
    private final Instruction _startDocument;
    private final Instruction _endDocument;
    private final Instruction _attribute;
    private final Instruction _uniqueAttribute;
    private final Instruction _namespace;
    private final Instruction _setStartNode;
    private final Instruction _reset;
    private final Instruction _nextNode;
    private SlotAllocator _slotAllocator;
    private boolean _allocatorInit;
    private LocalVariableRegistry _localVariableRegistry;
    private Map<Pattern, InstructionList> _preCompiled;
    private int m_totalChunks;
    private int m_openChunks;

    public MethodGenerator(int access_flags, com.sun.org.apache.bcel.internal.generic.Type return_type, com.sun.org.apache.bcel.internal.generic.Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cpg) {
        super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cpg);
        this._allocatorInit = false;
        this._preCompiled = new HashMap();
        this.m_totalChunks = 0;
        this.m_openChunks = 0;
        this._astoreHandler = new ASTORE(3);
        this._aloadHandler = new ALOAD(3);
        this._astoreIterator = new ASTORE(2);
        this._aloadIterator = new ALOAD(2);
        this._aloadDom = new ALOAD(1);
        this._astoreDom = new ASTORE(1);
        int startElement = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "startElement", "(Ljava/lang/String;)V");
        this._startElement = new INVOKEINTERFACE(startElement, 2);
        int endElement = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "endElement", "(Ljava/lang/String;)V");
        this._endElement = new INVOKEINTERFACE(endElement, 2);
        int attribute = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, Constants.ADD_ATTRIBUTE, "(Ljava/lang/String;Ljava/lang/String;)V");
        this._attribute = new INVOKEINTERFACE(attribute, 3);
        int uniqueAttribute = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "addUniqueAttribute", "(Ljava/lang/String;Ljava/lang/String;I)V");
        this._uniqueAttribute = new INVOKEINTERFACE(uniqueAttribute, 4);
        int namespace = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "namespaceAfterStartElement", "(Ljava/lang/String;Ljava/lang/String;)V");
        this._namespace = new INVOKEINTERFACE(namespace, 3);
        int index = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "startDocument", "()V");
        this._startDocument = new INVOKEINTERFACE(index, 1);
        int index2 = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "endDocument", "()V");
        this._endDocument = new INVOKEINTERFACE(index2, 1);
        int index3 = cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.SET_START_NODE, "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        this._setStartNode = new INVOKEINTERFACE(index3, 2);
        int index4 = cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.RESET, "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        this._reset = new INVOKEINTERFACE(index4, 1);
        int index5 = cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.NEXT, "()I");
        this._nextNode = new INVOKEINTERFACE(index5, 1);
        this._slotAllocator = new SlotAllocator();
        this._slotAllocator.initialize(getLocalVariableRegistry().getLocals(false));
        this._allocatorInit = true;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.MethodGen
    public LocalVariableGen addLocalVariable(String name, com.sun.org.apache.bcel.internal.generic.Type type, InstructionHandle start, InstructionHandle end) {
        LocalVariableGen lvg;
        if (this._allocatorInit) {
            lvg = addLocalVariable2(name, type, start);
        } else {
            lvg = super.addLocalVariable(name, type, start, end);
            getLocalVariableRegistry().registerLocalVariable(lvg);
        }
        return lvg;
    }

    public LocalVariableGen addLocalVariable2(String name, com.sun.org.apache.bcel.internal.generic.Type type, InstructionHandle start) {
        LocalVariableGen lvg = super.addLocalVariable(name, type, this._slotAllocator.allocateSlot(type), start, null);
        getLocalVariableRegistry().registerLocalVariable(lvg);
        return lvg;
    }

    private LocalVariableRegistry getLocalVariableRegistry() {
        if (this._localVariableRegistry == null) {
            this._localVariableRegistry = new LocalVariableRegistry();
        }
        return this._localVariableRegistry;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/MethodGenerator$LocalVariableRegistry.class */
    protected class LocalVariableRegistry {
        protected ArrayList _variables = new ArrayList();
        protected HashMap _nameToLVGMap = new HashMap();

        protected LocalVariableRegistry() {
        }

        protected void registerLocalVariable(LocalVariableGen lvg) {
            int slot = lvg.getIndex();
            int registrySize = this._variables.size();
            if (slot >= registrySize) {
                for (int i2 = registrySize; i2 < slot; i2++) {
                    this._variables.add(null);
                }
                this._variables.add(lvg);
            } else {
                Object localsInSlot = this._variables.get(slot);
                if (localsInSlot != null) {
                    if (localsInSlot instanceof LocalVariableGen) {
                        ArrayList listOfLocalsInSlot = new ArrayList();
                        listOfLocalsInSlot.add(localsInSlot);
                        listOfLocalsInSlot.add(lvg);
                        this._variables.set(slot, listOfLocalsInSlot);
                    } else {
                        ((ArrayList) localsInSlot).add(lvg);
                    }
                } else {
                    this._variables.set(slot, lvg);
                }
            }
            registerByName(lvg);
        }

        protected LocalVariableGen lookupRegisteredLocalVariable(int slot, int offset) {
            Object localsInSlot = this._variables != null ? this._variables.get(slot) : null;
            if (localsInSlot != null) {
                if (localsInSlot instanceof LocalVariableGen) {
                    LocalVariableGen lvg = (LocalVariableGen) localsInSlot;
                    if (MethodGenerator.this.offsetInLocalVariableGenRange(lvg, offset)) {
                        return lvg;
                    }
                    return null;
                }
                ArrayList listOfLocalsInSlot = (ArrayList) localsInSlot;
                int size = listOfLocalsInSlot.size();
                for (int i2 = 0; i2 < size; i2++) {
                    LocalVariableGen lvg2 = (LocalVariableGen) listOfLocalsInSlot.get(i2);
                    if (MethodGenerator.this.offsetInLocalVariableGenRange(lvg2, offset)) {
                        return lvg2;
                    }
                }
                return null;
            }
            return null;
        }

        protected void registerByName(LocalVariableGen lvg) {
            ArrayList sameNameList;
            Object duplicateNameEntry = this._nameToLVGMap.get(lvg.getName());
            if (duplicateNameEntry == null) {
                this._nameToLVGMap.put(lvg.getName(), lvg);
                return;
            }
            if (duplicateNameEntry instanceof ArrayList) {
                sameNameList = (ArrayList) duplicateNameEntry;
                sameNameList.add(lvg);
            } else {
                sameNameList = new ArrayList();
                sameNameList.add(duplicateNameEntry);
                sameNameList.add(lvg);
            }
            this._nameToLVGMap.put(lvg.getName(), sameNameList);
        }

        protected void removeByNameTracking(LocalVariableGen lvg) {
            Object duplicateNameEntry = this._nameToLVGMap.get(lvg.getName());
            if (duplicateNameEntry instanceof ArrayList) {
                ArrayList sameNameList = (ArrayList) duplicateNameEntry;
                for (int i2 = 0; i2 < sameNameList.size(); i2++) {
                    if (sameNameList.get(i2) == lvg) {
                        sameNameList.remove(i2);
                        return;
                    }
                }
                return;
            }
            this._nameToLVGMap.remove(lvg);
        }

        protected LocalVariableGen lookUpByName(String name) {
            LocalVariableGen lvg = null;
            Object duplicateNameEntry = this._nameToLVGMap.get(name);
            if (duplicateNameEntry instanceof ArrayList) {
                ArrayList sameNameList = (ArrayList) duplicateNameEntry;
                for (int i2 = 0; i2 < sameNameList.size(); i2++) {
                    lvg = (LocalVariableGen) sameNameList.get(i2);
                    if (lvg.getName() == name) {
                        break;
                    }
                }
            } else {
                lvg = (LocalVariableGen) duplicateNameEntry;
            }
            return lvg;
        }

        protected LocalVariableGen[] getLocals(boolean includeRemoved) {
            ArrayList allVarsEverDeclared = new ArrayList();
            if (includeRemoved) {
                int slotCount = allVarsEverDeclared.size();
                for (int i2 = 0; i2 < slotCount; i2++) {
                    Object slotEntries = this._variables.get(i2);
                    if (slotEntries != null) {
                        if (slotEntries instanceof ArrayList) {
                            ArrayList slotList = (ArrayList) slotEntries;
                            for (int j2 = 0; j2 < slotList.size(); j2++) {
                                allVarsEverDeclared.add(slotList.get(i2));
                            }
                        } else {
                            allVarsEverDeclared.add(slotEntries);
                        }
                    }
                }
            } else {
                for (Map.Entry nameVarsPair : this._nameToLVGMap.entrySet()) {
                    Object vars = nameVarsPair.getValue();
                    if (vars != null) {
                        if (vars instanceof ArrayList) {
                            ArrayList varsList = (ArrayList) vars;
                            for (int i3 = 0; i3 < varsList.size(); i3++) {
                                allVarsEverDeclared.add(varsList.get(i3));
                            }
                        } else {
                            allVarsEverDeclared.add(vars);
                        }
                    }
                }
            }
            LocalVariableGen[] locals = new LocalVariableGen[allVarsEverDeclared.size()];
            allVarsEverDeclared.toArray(locals);
            return locals;
        }
    }

    boolean offsetInLocalVariableGenRange(LocalVariableGen lvg, int offset) {
        InstructionHandle lvgStart = lvg.getStart();
        InstructionHandle lvgEnd = lvg.getEnd();
        if (lvgStart == null) {
            lvgStart = getInstructionList().getStart();
        }
        if (lvgEnd == null) {
            lvgEnd = getInstructionList().getEnd();
        }
        return lvgStart.getPosition() <= offset && lvgEnd.getPosition() + lvgEnd.getInstruction().getLength() >= offset;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.MethodGen
    public void removeLocalVariable(LocalVariableGen lvg) {
        this._slotAllocator.releaseSlot(lvg);
        getLocalVariableRegistry().removeByNameTracking(lvg);
        super.removeLocalVariable(lvg);
    }

    public Instruction loadDOM() {
        return this._aloadDom;
    }

    public Instruction storeDOM() {
        return this._astoreDom;
    }

    public Instruction storeHandler() {
        return this._astoreHandler;
    }

    public Instruction loadHandler() {
        return this._aloadHandler;
    }

    public Instruction storeIterator() {
        return this._astoreIterator;
    }

    public Instruction loadIterator() {
        return this._aloadIterator;
    }

    public final Instruction setStartNode() {
        return this._setStartNode;
    }

    public final Instruction reset() {
        return this._reset;
    }

    public final Instruction nextNode() {
        return this._nextNode;
    }

    public final Instruction startElement() {
        return this._startElement;
    }

    public final Instruction endElement() {
        return this._endElement;
    }

    public final Instruction startDocument() {
        return this._startDocument;
    }

    public final Instruction endDocument() {
        return this._endDocument;
    }

    public final Instruction attribute() {
        return this._attribute;
    }

    public final Instruction uniqueAttribute() {
        return this._uniqueAttribute;
    }

    public final Instruction namespace() {
        return this._namespace;
    }

    public Instruction loadCurrentNode() {
        if (this._iloadCurrent == null) {
            int idx = getLocalIndex(Keywords.FUNC_CURRENT_STRING);
            if (idx > 0) {
                this._iloadCurrent = new ILOAD(idx);
            } else {
                this._iloadCurrent = new ICONST(0);
            }
        }
        return this._iloadCurrent;
    }

    public Instruction storeCurrentNode() {
        if (this._istoreCurrent != null) {
            return this._istoreCurrent;
        }
        ISTORE istore = new ISTORE(getLocalIndex(Keywords.FUNC_CURRENT_STRING));
        this._istoreCurrent = istore;
        return istore;
    }

    public Instruction loadContextNode() {
        return loadCurrentNode();
    }

    public Instruction storeContextNode() {
        return storeCurrentNode();
    }

    public int getLocalIndex(String name) {
        return getLocalVariable(name).getIndex();
    }

    public LocalVariableGen getLocalVariable(String name) {
        return getLocalVariableRegistry().lookUpByName(name);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.MethodGen
    public void setMaxLocals() {
        int maxLocals = super.getMaxLocals();
        LocalVariableGen[] localVars = super.getLocalVariables();
        if (localVars != null && localVars.length > maxLocals) {
            maxLocals = localVars.length;
        }
        if (maxLocals < 5) {
            maxLocals = 5;
        }
        super.setMaxLocals(maxLocals);
    }

    public void addInstructionList(Pattern pattern, InstructionList ilist) {
        this._preCompiled.put(pattern, ilist);
    }

    public InstructionList getInstructionList(Pattern pattern) {
        return this._preCompiled.get(pattern);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/MethodGenerator$Chunk.class */
    private class Chunk implements Comparable {
        private InstructionHandle m_start;
        private InstructionHandle m_end;
        private int m_size;

        Chunk(InstructionHandle start, InstructionHandle end) {
            this.m_start = start;
            this.m_end = end;
            this.m_size = end.getPosition() - start.getPosition();
        }

        boolean isAdjacentTo(Chunk neighbour) {
            return getChunkEnd().getNext() == neighbour.getChunkStart();
        }

        InstructionHandle getChunkStart() {
            return this.m_start;
        }

        InstructionHandle getChunkEnd() {
            return this.m_end;
        }

        int getChunkSize() {
            return this.m_size;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object comparand) {
            return getChunkSize() - ((Chunk) comparand).getChunkSize();
        }
    }

    private ArrayList getCandidateChunks(ClassGenerator classGen, int totalMethodSize) {
        InstructionHandle currentHandle;
        int childChunkCount;
        Iterator instructions = getInstructionList().iterator();
        ArrayList candidateChunks = new ArrayList();
        ArrayList currLevelChunks = new ArrayList();
        Stack subChunkStack = new Stack();
        boolean openChunkAtCurrLevel = false;
        boolean firstInstruction = true;
        if (this.m_openChunks != 0) {
            String msg = new ErrorMsg(ErrorMsg.OUTLINE_ERR_UNBALANCED_MARKERS).toString();
            throw new InternalError(msg);
        }
        do {
            currentHandle = instructions.hasNext() ? (InstructionHandle) instructions.next() : null;
            Instruction inst = currentHandle != null ? currentHandle.getInstruction() : null;
            if (firstInstruction) {
                openChunkAtCurrLevel = true;
                currLevelChunks.add(currentHandle);
                firstInstruction = false;
            }
            if (inst instanceof OutlineableChunkStart) {
                if (openChunkAtCurrLevel) {
                    subChunkStack.push(currLevelChunks);
                    currLevelChunks = new ArrayList();
                }
                openChunkAtCurrLevel = true;
                currLevelChunks.add(currentHandle);
            } else if (currentHandle == null || (inst instanceof OutlineableChunkEnd)) {
                ArrayList nestedSubChunks = null;
                if (!openChunkAtCurrLevel) {
                    nestedSubChunks = currLevelChunks;
                    currLevelChunks = (ArrayList) subChunkStack.pop();
                }
                InstructionHandle chunkStart = (InstructionHandle) currLevelChunks.get(currLevelChunks.size() - 1);
                int chunkEndPosition = currentHandle != null ? currentHandle.getPosition() : totalMethodSize;
                int chunkSize = chunkEndPosition - chunkStart.getPosition();
                if (chunkSize <= TARGET_METHOD_SIZE) {
                    currLevelChunks.add(currentHandle);
                } else {
                    if (!openChunkAtCurrLevel && (childChunkCount = nestedSubChunks.size() / 2) > 0) {
                        Chunk[] childChunks = new Chunk[childChunkCount];
                        for (int i2 = 0; i2 < childChunkCount; i2++) {
                            InstructionHandle start = (InstructionHandle) nestedSubChunks.get(i2 * 2);
                            InstructionHandle end = (InstructionHandle) nestedSubChunks.get((i2 * 2) + 1);
                            childChunks[i2] = new Chunk(start, end);
                        }
                        ArrayList mergedChildChunks = mergeAdjacentChunks(childChunks);
                        for (int i3 = 0; i3 < mergedChildChunks.size(); i3++) {
                            Chunk mergedChunk = (Chunk) mergedChildChunks.get(i3);
                            int mergedSize = mergedChunk.getChunkSize();
                            if (mergedSize >= 1000 && mergedSize <= TARGET_METHOD_SIZE) {
                                candidateChunks.add(mergedChunk);
                            }
                        }
                    }
                    currLevelChunks.remove(currLevelChunks.size() - 1);
                }
                openChunkAtCurrLevel = (currLevelChunks.size() & 1) == 1;
            }
        } while (currentHandle != null);
        return candidateChunks;
    }

    private ArrayList mergeAdjacentChunks(Chunk[] chunks) {
        int[] adjacencyRunStart = new int[chunks.length];
        int[] adjacencyRunLength = new int[chunks.length];
        boolean[] chunkWasMerged = new boolean[chunks.length];
        int maximumRunOfChunks = 0;
        int numAdjacentRuns = 0;
        ArrayList mergedChunks = new ArrayList();
        int startOfCurrentRun = 0;
        for (int i2 = 1; i2 < chunks.length; i2++) {
            if (!chunks[i2 - 1].isAdjacentTo(chunks[i2])) {
                int lengthOfRun = i2 - startOfCurrentRun;
                if (maximumRunOfChunks < lengthOfRun) {
                    maximumRunOfChunks = lengthOfRun;
                }
                if (lengthOfRun > 1) {
                    adjacencyRunLength[numAdjacentRuns] = lengthOfRun;
                    adjacencyRunStart[numAdjacentRuns] = startOfCurrentRun;
                    numAdjacentRuns++;
                }
                startOfCurrentRun = i2;
            }
        }
        if (chunks.length - startOfCurrentRun > 1) {
            int lengthOfRun2 = chunks.length - startOfCurrentRun;
            if (maximumRunOfChunks < lengthOfRun2) {
                maximumRunOfChunks = lengthOfRun2;
            }
            adjacencyRunLength[numAdjacentRuns] = chunks.length - startOfCurrentRun;
            adjacencyRunStart[numAdjacentRuns] = startOfCurrentRun;
            numAdjacentRuns++;
        }
        for (int numToMerge = maximumRunOfChunks; numToMerge > 1; numToMerge--) {
            for (int run = 0; run < numAdjacentRuns; run++) {
                int runStart = adjacencyRunStart[run];
                int runEnd = (runStart + adjacencyRunLength[run]) - 1;
                boolean foundChunksToMerge = false;
                for (int mergeStart = runStart; (mergeStart + numToMerge) - 1 <= runEnd && !foundChunksToMerge; mergeStart++) {
                    int mergeEnd = (mergeStart + numToMerge) - 1;
                    int mergeSize = 0;
                    for (int j2 = mergeStart; j2 <= mergeEnd; j2++) {
                        mergeSize += chunks[j2].getChunkSize();
                    }
                    if (mergeSize <= TARGET_METHOD_SIZE) {
                        foundChunksToMerge = true;
                        for (int j3 = mergeStart; j3 <= mergeEnd; j3++) {
                            chunkWasMerged[j3] = true;
                        }
                        mergedChunks.add(new Chunk(chunks[mergeStart].getChunkStart(), chunks[mergeEnd].getChunkEnd()));
                        adjacencyRunLength[run] = adjacencyRunStart[run] - mergeStart;
                        int trailingRunLength = runEnd - mergeEnd;
                        if (trailingRunLength >= 2) {
                            adjacencyRunStart[numAdjacentRuns] = mergeEnd + 1;
                            adjacencyRunLength[numAdjacentRuns] = trailingRunLength;
                            numAdjacentRuns++;
                        }
                    }
                }
            }
        }
        for (int i3 = 0; i3 < chunks.length; i3++) {
            if (!chunkWasMerged[i3]) {
                mergedChunks.add(chunks[i3]);
            }
        }
        return mergedChunks;
    }

    public Method[] outlineChunks(ClassGenerator classGen, int originalMethodSize) {
        ArrayList methodsOutlined = new ArrayList();
        int currentMethodSize = originalMethodSize;
        int outlinedCount = 0;
        String originalMethodName = getName();
        if (originalMethodName.equals(com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME)) {
            originalMethodName = "$lt$init$gt$";
        } else if (originalMethodName.equals(com.sun.org.apache.bcel.internal.Constants.STATIC_INITIALIZER_NAME)) {
            originalMethodName = "$lt$clinit$gt$";
        }
        do {
            ArrayList candidateChunks = getCandidateChunks(classGen, currentMethodSize);
            Collections.sort(candidateChunks);
            boolean moreMethodsOutlined = false;
            for (int i2 = candidateChunks.size() - 1; i2 >= 0 && currentMethodSize > TARGET_METHOD_SIZE; i2--) {
                Chunk chunkToOutline = (Chunk) candidateChunks.get(i2);
                methodsOutlined.add(outline(chunkToOutline.getChunkStart(), chunkToOutline.getChunkEnd(), originalMethodName + "$outline$" + outlinedCount, classGen));
                outlinedCount++;
                moreMethodsOutlined = true;
                InstructionList il = getInstructionList();
                InstructionHandle lastInst = il.getEnd();
                il.setPositions();
                currentMethodSize = lastInst.getPosition() + lastInst.getInstruction().getLength();
            }
            if (!moreMethodsOutlined) {
                break;
            }
        } while (currentMethodSize > TARGET_METHOD_SIZE);
        if (currentMethodSize > 65535) {
            String msg = new ErrorMsg(ErrorMsg.OUTLINE_ERR_METHOD_TOO_BIG).toString();
            throw new InternalError(msg);
        }
        Method[] methodsArr = new Method[methodsOutlined.size() + 1];
        methodsOutlined.toArray(methodsArr);
        methodsArr[methodsOutlined.size()] = getThisMethod();
        return methodsArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Method outline(InstructionHandle first, InstructionHandle last, String outlinedMethodName, ClassGenerator classGen) {
        InstructionHandle outlinedMethodRef;
        int newLocalVarIndex;
        Object newLVG;
        if (getExceptionHandlers().length != 0) {
            String msg = new ErrorMsg(ErrorMsg.OUTLINE_ERR_TRY_CATCH).toString();
            throw new InternalError(msg);
        }
        int outlineChunkStartOffset = first.getPosition();
        int outlineChunkEndOffset = last.getPosition() + last.getInstruction().getLength();
        ConstantPoolGen cpg = getConstantPool();
        InstructionList instructionList = new InstructionList();
        XSLTC xsltc = classGen.getParser().getXSLTC();
        String argTypeName = xsltc.getHelperClassName();
        com.sun.org.apache.bcel.internal.generic.Type[] argTypes = {new ObjectType(argTypeName).toJCType()};
        String[] argNames = {"copyLocals"};
        int methodAttributes = 18;
        boolean isStaticMethod = (getAccessFlags() & 8) != 0;
        if (isStaticMethod) {
            methodAttributes = 18 | 8;
        }
        MethodGenerator outlinedMethodGen = new MethodGenerator(methodAttributes, com.sun.org.apache.bcel.internal.generic.Type.VOID, argTypes, argNames, outlinedMethodName, getClassName(), instructionList, cpg);
        ClassGenerator copyAreaCG = new ClassGenerator(argTypeName, Constants.OBJECT_CLASS, argTypeName + ".java", 49, null, classGen.getStylesheet()) { // from class: com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator.1
            @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator
            public boolean isExternal() {
                return true;
            }
        };
        ConstantPoolGen copyAreaCPG = copyAreaCG.getConstantPool();
        copyAreaCG.addEmptyConstructor(1);
        int copyAreaFieldCount = 0;
        InstructionHandle limit = last.getNext();
        InstructionList oldMethCopyInIL = new InstructionList();
        InstructionList oldMethCopyOutIL = new InstructionList();
        InstructionList newMethCopyInIL = new InstructionList();
        InstructionList newMethCopyOutIL = new InstructionList();
        InstructionHandle outlinedMethodCallSetup = oldMethCopyInIL.append(new NEW(cpg.addClass(argTypeName)));
        oldMethCopyInIL.append(InstructionConstants.DUP);
        oldMethCopyInIL.append(InstructionConstants.DUP);
        oldMethCopyInIL.append(new INVOKESPECIAL(cpg.addMethodref(argTypeName, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V")));
        if (isStaticMethod) {
            outlinedMethodRef = oldMethCopyOutIL.append(new INVOKESTATIC(cpg.addMethodref(classGen.getClassName(), outlinedMethodName, outlinedMethodGen.getSignature())));
        } else {
            oldMethCopyOutIL.append(InstructionConstants.THIS);
            oldMethCopyOutIL.append(InstructionConstants.SWAP);
            outlinedMethodRef = oldMethCopyOutIL.append(new INVOKEVIRTUAL(cpg.addMethodref(classGen.getClassName(), outlinedMethodName, outlinedMethodGen.getSignature())));
        }
        boolean chunkStartTargetMappingsPending = false;
        InstructionHandle pendingTargetMappingHandle = null;
        InstructionHandle lastCopyHandle = null;
        HashMap targetMap = new HashMap();
        HashMap localVarMap = new HashMap();
        HashMap revisedLocalVarStart = new HashMap();
        HashMap revisedLocalVarEnd = new HashMap();
        InstructionHandle next = first;
        while (true) {
            InstructionHandle ih = next;
            if (ih == limit) {
                break;
            }
            Instruction inst = ih.getInstruction();
            if (inst instanceof MarkerInstruction) {
                if (ih.hasTargeters()) {
                    if (inst instanceof OutlineableChunkEnd) {
                        targetMap.put(ih, lastCopyHandle);
                    } else if (!chunkStartTargetMappingsPending) {
                        chunkStartTargetMappingsPending = true;
                        pendingTargetMappingHandle = ih;
                    }
                }
            } else {
                Instruction instructionCopy = inst.copy();
                if (instructionCopy instanceof BranchInstruction) {
                    lastCopyHandle = instructionList.append((BranchInstruction) instructionCopy);
                } else {
                    lastCopyHandle = instructionList.append(instructionCopy);
                }
                if ((instructionCopy instanceof LocalVariableInstruction) || (instructionCopy instanceof RET)) {
                    int oldLocalVarIndex = ((IndexedInstruction) instructionCopy).getIndex();
                    LocalVariableGen oldLVG = getLocalVariableRegistry().lookupRegisteredLocalVariable(oldLocalVarIndex, ih.getPosition());
                    if (localVarMap.get(oldLVG) == null) {
                        boolean copyInLocalValue = offsetInLocalVariableGenRange(oldLVG, outlineChunkStartOffset != 0 ? outlineChunkStartOffset - 1 : 0);
                        boolean copyOutLocalValue = offsetInLocalVariableGenRange(oldLVG, outlineChunkEndOffset + 1);
                        if (copyInLocalValue || copyOutLocalValue) {
                            String varName = oldLVG.getName();
                            com.sun.org.apache.bcel.internal.generic.Type varType = oldLVG.getType();
                            LocalVariableGen newLVG2 = outlinedMethodGen.addLocalVariable(varName, varType, null, null);
                            int newLocalVarIndex2 = newLVG2.getIndex();
                            String varSignature = varType.getSignature();
                            localVarMap.put(oldLVG, newLVG2);
                            copyAreaFieldCount++;
                            String copyAreaFieldName = "field" + copyAreaFieldCount;
                            copyAreaCG.addField(new Field(1, copyAreaCPG.addUtf8(copyAreaFieldName), copyAreaCPG.addUtf8(varSignature), null, copyAreaCPG.getConstantPool()));
                            int fieldRef = cpg.addFieldref(argTypeName, copyAreaFieldName, varSignature);
                            if (copyInLocalValue) {
                                oldMethCopyInIL.append(InstructionConstants.DUP);
                                InstructionHandle copyInLoad = oldMethCopyInIL.append(loadLocal(oldLocalVarIndex, varType));
                                oldMethCopyInIL.append(new PUTFIELD(fieldRef));
                                if (!copyOutLocalValue) {
                                    revisedLocalVarEnd.put(oldLVG, copyInLoad);
                                }
                                newMethCopyInIL.append(InstructionConstants.ALOAD_1);
                                newMethCopyInIL.append(new GETFIELD(fieldRef));
                                newMethCopyInIL.append(storeLocal(newLocalVarIndex2, varType));
                            }
                            if (copyOutLocalValue) {
                                newMethCopyOutIL.append(InstructionConstants.ALOAD_1);
                                newMethCopyOutIL.append(loadLocal(newLocalVarIndex2, varType));
                                newMethCopyOutIL.append(new PUTFIELD(fieldRef));
                                oldMethCopyOutIL.append(InstructionConstants.DUP);
                                oldMethCopyOutIL.append(new GETFIELD(fieldRef));
                                InstructionHandle copyOutStore = oldMethCopyOutIL.append(storeLocal(oldLocalVarIndex, varType));
                                if (!copyInLocalValue) {
                                    revisedLocalVarStart.put(oldLVG, copyOutStore);
                                }
                            }
                        }
                    }
                }
                if (ih.hasTargeters()) {
                    targetMap.put(ih, lastCopyHandle);
                }
                if (chunkStartTargetMappingsPending) {
                    do {
                        targetMap.put(pendingTargetMappingHandle, lastCopyHandle);
                        pendingTargetMappingHandle = pendingTargetMappingHandle.getNext();
                    } while (pendingTargetMappingHandle != ih);
                    chunkStartTargetMappingsPending = false;
                }
            }
            next = ih.getNext();
        }
        InstructionHandle ih2 = first;
        InstructionHandle ch = instructionList.getStart();
        while (ch != null) {
            Instruction i2 = ih2.getInstruction();
            Cloneable instruction = ch.getInstruction();
            if (i2 instanceof BranchInstruction) {
                BranchInstruction bc2 = (BranchInstruction) instruction;
                BranchInstruction bi2 = (BranchInstruction) i2;
                InstructionHandle itarget = bi2.getTarget();
                InstructionHandle newTarget = (InstructionHandle) targetMap.get(itarget);
                bc2.setTarget(newTarget);
                if (bi2 instanceof Select) {
                    InstructionHandle[] itargets = ((Select) bi2).getTargets();
                    InstructionHandle[] ctargets = ((Select) bc2).getTargets();
                    for (int j2 = 0; j2 < itargets.length; j2++) {
                        ctargets[j2] = (InstructionHandle) targetMap.get(itargets[j2]);
                    }
                }
            } else if ((i2 instanceof LocalVariableInstruction) || (i2 instanceof RET)) {
                IndexedInstruction lvi = (IndexedInstruction) instruction;
                LocalVariableGen oldLVG2 = getLocalVariableRegistry().lookupRegisteredLocalVariable(lvi.getIndex(), ih2.getPosition());
                LocalVariableGen newLVG3 = (LocalVariableGen) localVarMap.get(oldLVG2);
                if (newLVG3 == null) {
                    String varName2 = oldLVG2.getName();
                    LocalVariableGen newLVG4 = outlinedMethodGen.addLocalVariable(varName2, oldLVG2.getType(), null, null);
                    newLocalVarIndex = newLVG4.getIndex();
                    localVarMap.put(oldLVG2, newLVG4);
                    revisedLocalVarStart.put(oldLVG2, outlinedMethodRef);
                    revisedLocalVarEnd.put(oldLVG2, outlinedMethodRef);
                } else {
                    newLocalVarIndex = newLVG3.getIndex();
                }
                lvi.setIndex(newLocalVarIndex);
            }
            if (ih2.hasTargeters()) {
                for (InstructionTargeter targeter : ih2.getTargeters()) {
                    if ((targeter instanceof LocalVariableGen) && ((LocalVariableGen) targeter).getEnd() == ih2 && (newLVG = localVarMap.get(targeter)) != null) {
                        outlinedMethodGen.removeLocalVariable((LocalVariableGen) newLVG);
                    }
                }
            }
            if (!(i2 instanceof MarkerInstruction)) {
                ch = ch.getNext();
            }
            ih2 = ih2.getNext();
        }
        oldMethCopyOutIL.append(InstructionConstants.POP);
        for (Map.Entry lvgRangeStartPair : revisedLocalVarStart.entrySet()) {
            LocalVariableGen lvg = (LocalVariableGen) lvgRangeStartPair.getKey();
            InstructionHandle startInst = (InstructionHandle) lvgRangeStartPair.getValue();
            lvg.setStart(startInst);
        }
        for (Map.Entry lvgRangeEndPair : revisedLocalVarEnd.entrySet()) {
            LocalVariableGen lvg2 = (LocalVariableGen) lvgRangeEndPair.getKey();
            InstructionHandle endInst = (InstructionHandle) lvgRangeEndPair.getValue();
            lvg2.setEnd(endInst);
        }
        xsltc.dumpClass(copyAreaCG.getJavaClass());
        InstructionList oldMethodIL = getInstructionList();
        oldMethodIL.insert(first, oldMethCopyInIL);
        oldMethodIL.insert(first, oldMethCopyOutIL);
        instructionList.insert(newMethCopyInIL);
        instructionList.append(newMethCopyOutIL);
        instructionList.append(InstructionConstants.RETURN);
        try {
            oldMethodIL.delete(first, last);
        } catch (TargetLostException e2) {
            InstructionHandle[] targets = e2.getTargets();
            for (InstructionHandle lostTarget : targets) {
                InstructionTargeter[] targeters = lostTarget.getTargeters();
                for (int j3 = 0; j3 < targeters.length; j3++) {
                    if (targeters[j3] instanceof LocalVariableGen) {
                        LocalVariableGen lvgTargeter = (LocalVariableGen) targeters[j3];
                        if (lvgTargeter.getStart() == lostTarget) {
                            lvgTargeter.setStart(outlinedMethodRef);
                        }
                        if (lvgTargeter.getEnd() == lostTarget) {
                            lvgTargeter.setEnd(outlinedMethodRef);
                        }
                    } else {
                        targeters[j3].updateTarget(lostTarget, outlinedMethodCallSetup);
                    }
                }
            }
        }
        String[] exceptions = getExceptions();
        for (String str : exceptions) {
            outlinedMethodGen.addException(str);
        }
        return outlinedMethodGen.getThisMethod();
    }

    private static Instruction loadLocal(int index, com.sun.org.apache.bcel.internal.generic.Type type) {
        if (type == com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN) {
            return new ILOAD(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.INT) {
            return new ILOAD(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.SHORT) {
            return new ILOAD(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.LONG) {
            return new LLOAD(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.BYTE) {
            return new ILOAD(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.CHAR) {
            return new ILOAD(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.FLOAT) {
            return new FLOAD(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.DOUBLE) {
            return new DLOAD(index);
        }
        return new ALOAD(index);
    }

    private static Instruction storeLocal(int index, com.sun.org.apache.bcel.internal.generic.Type type) {
        if (type == com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN) {
            return new ISTORE(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.INT) {
            return new ISTORE(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.SHORT) {
            return new ISTORE(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.LONG) {
            return new LSTORE(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.BYTE) {
            return new ISTORE(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.CHAR) {
            return new ISTORE(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.FLOAT) {
            return new FSTORE(index);
        }
        if (type == com.sun.org.apache.bcel.internal.generic.Type.DOUBLE) {
            return new DSTORE(index);
        }
        return new ASTORE(index);
    }

    public void markChunkStart() {
        getInstructionList().append(OutlineableChunkStart.OUTLINEABLECHUNKSTART);
        this.m_totalChunks++;
        this.m_openChunks++;
    }

    public void markChunkEnd() {
        getInstructionList().append(OutlineableChunkEnd.OUTLINEABLECHUNKEND);
        this.m_openChunks--;
        if (this.m_openChunks < 0) {
            String msg = new ErrorMsg(ErrorMsg.OUTLINE_ERR_UNBALANCED_MARKERS).toString();
            throw new InternalError(msg);
        }
    }

    Method[] getGeneratedMethods(ClassGenerator classGen) {
        Method[] generatedMethods;
        InstructionList il = getInstructionList();
        InstructionHandle last = il.getEnd();
        il.setPositions();
        int instructionListSize = last.getPosition() + last.getInstruction().getLength();
        if (instructionListSize > 32767) {
            boolean ilChanged = widenConditionalBranchTargetOffsets();
            if (ilChanged) {
                il.setPositions();
                InstructionHandle last2 = il.getEnd();
                instructionListSize = last2.getPosition() + last2.getInstruction().getLength();
            }
        }
        if (instructionListSize > 65535) {
            generatedMethods = outlineChunks(classGen, instructionListSize);
        } else {
            generatedMethods = new Method[]{getThisMethod()};
        }
        return generatedMethods;
    }

    protected Method getThisMethod() {
        stripAttributes(true);
        setMaxLocals();
        setMaxStack();
        removeNOPs();
        return getMethod();
    }

    boolean widenConditionalBranchTargetOffsets() {
        boolean ilChanged = false;
        int maxOffsetChange = 0;
        InstructionList il = getInstructionList();
        InstructionHandle start = il.getStart();
        while (true) {
            InstructionHandle ih = start;
            if (ih != null) {
                switch (ih.getInstruction().getOpcode()) {
                    case 153:
                    case 154:
                    case 155:
                    case 156:
                    case 157:
                    case 158:
                    case 159:
                    case 160:
                    case 161:
                    case 162:
                    case 163:
                    case 164:
                    case 165:
                    case 166:
                    case 198:
                    case 199:
                        maxOffsetChange += 5;
                        break;
                    case 167:
                    case 168:
                        maxOffsetChange += 2;
                        break;
                    case 170:
                    case 171:
                        maxOffsetChange += 3;
                        break;
                }
                start = ih.getNext();
            } else {
                InstructionHandle start2 = il.getStart();
                while (true) {
                    InstructionHandle ih2 = start2;
                    if (ih2 != null) {
                        Instruction inst = ih2.getInstruction();
                        if (inst instanceof IfInstruction) {
                            IfInstruction oldIfInst = (IfInstruction) inst;
                            BranchHandle oldIfHandle = (BranchHandle) ih2;
                            InstructionHandle target = oldIfInst.getTarget();
                            int relativeTargetOffset = target.getPosition() - oldIfHandle.getPosition();
                            if (relativeTargetOffset - maxOffsetChange < -32768 || relativeTargetOffset + maxOffsetChange > 32767) {
                                InstructionHandle nextHandle = oldIfHandle.getNext();
                                IfInstruction invertedIfInst = oldIfInst.negate();
                                BranchHandle invertedIfHandle = il.append((InstructionHandle) oldIfHandle, (BranchInstruction) invertedIfInst);
                                BranchHandle gotoHandle = il.append((InstructionHandle) invertedIfHandle, (BranchInstruction) new GOTO(target));
                                if (nextHandle == null) {
                                    nextHandle = il.append(gotoHandle, NOP);
                                }
                                invertedIfHandle.updateTarget(target, nextHandle);
                                if (oldIfHandle.hasTargeters()) {
                                    InstructionTargeter[] targeters = oldIfHandle.getTargeters();
                                    for (InstructionTargeter targeter : targeters) {
                                        if (targeter instanceof LocalVariableGen) {
                                            LocalVariableGen lvg = (LocalVariableGen) targeter;
                                            if (lvg.getStart() == oldIfHandle) {
                                                lvg.setStart(invertedIfHandle);
                                            } else if (lvg.getEnd() == oldIfHandle) {
                                                lvg.setEnd(gotoHandle);
                                            }
                                        } else {
                                            targeter.updateTarget(oldIfHandle, invertedIfHandle);
                                        }
                                    }
                                }
                                try {
                                    il.delete(oldIfHandle);
                                    ih2 = gotoHandle;
                                    ilChanged = true;
                                } catch (TargetLostException tle) {
                                    String msg = new ErrorMsg(ErrorMsg.OUTLINE_ERR_DELETED_TARGET, tle.getMessage()).toString();
                                    throw new InternalError(msg);
                                }
                            }
                        }
                        start2 = ih2.getNext();
                    } else {
                        return ilChanged;
                    }
                }
            }
        }
    }
}
