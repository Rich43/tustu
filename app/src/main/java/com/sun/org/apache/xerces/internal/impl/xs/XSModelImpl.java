package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMap4Types;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSAttributeGroupDefinition;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition;
import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItemList;
import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSObject;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSModelImpl.class */
public final class XSModelImpl extends AbstractList implements XSModel, XSNamespaceItemList {
    private static final short MAX_COMP_IDX = 16;
    private static final boolean[] GLOBAL_COMP = {false, true, true, true, false, true, true, false, false, false, false, true, false, false, false, true, true};
    private final int fGrammarCount;
    private final String[] fNamespaces;
    private final SchemaGrammar[] fGrammarList;
    private final SymbolHash fGrammarMap;
    private final SymbolHash fSubGroupMap;
    private final XSNamedMap[] fGlobalComponents;
    private final XSNamedMap[][] fNSComponents;
    private final StringList fNamespacesList;
    private XSObjectList fAnnotations;
    private final boolean fHasIDC;

    public XSModelImpl(SchemaGrammar[] grammars) {
        this(grammars, (short) 1);
    }

    public XSModelImpl(SchemaGrammar[] grammars, short s4sVersion) {
        this.fAnnotations = null;
        int len = grammars.length;
        int initialSize = Math.max(len + 1, 5);
        String[] namespaces = new String[initialSize];
        SchemaGrammar[] grammarList = new SchemaGrammar[initialSize];
        boolean hasS4S = false;
        for (int i2 = 0; i2 < len; i2++) {
            SchemaGrammar sg = grammars[i2];
            String tns = sg.getTargetNamespace();
            namespaces[i2] = tns;
            grammarList[i2] = sg;
            if (tns == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
                hasS4S = true;
            }
        }
        if (!hasS4S) {
            namespaces[len] = SchemaSymbols.URI_SCHEMAFORSCHEMA;
            len++;
            grammarList[len] = SchemaGrammar.getS4SGrammar(s4sVersion);
        }
        for (int i3 = 0; i3 < len; i3++) {
            SchemaGrammar sg1 = grammarList[i3];
            Vector gs = sg1.getImportedGrammars();
            for (int j2 = gs == null ? -1 : gs.size() - 1; j2 >= 0; j2--) {
                SchemaGrammar sg2 = (SchemaGrammar) gs.elementAt(j2);
                int k2 = 0;
                while (k2 < len && sg2 != grammarList[k2]) {
                    k2++;
                }
                if (k2 == len) {
                    if (len == grammarList.length) {
                        String[] newSA = new String[len * 2];
                        System.arraycopy(namespaces, 0, newSA, 0, len);
                        namespaces = newSA;
                        SchemaGrammar[] newGA = new SchemaGrammar[len * 2];
                        System.arraycopy(grammarList, 0, newGA, 0, len);
                        grammarList = newGA;
                    }
                    namespaces[len] = sg2.getTargetNamespace();
                    grammarList[len] = sg2;
                    len++;
                }
            }
        }
        this.fNamespaces = namespaces;
        this.fGrammarList = grammarList;
        boolean hasIDC = false;
        this.fGrammarMap = new SymbolHash(len * 2);
        for (int i4 = 0; i4 < len; i4++) {
            this.fGrammarMap.put(null2EmptyString(this.fNamespaces[i4]), this.fGrammarList[i4]);
            if (this.fGrammarList[i4].hasIDConstraints()) {
                hasIDC = true;
            }
        }
        this.fHasIDC = hasIDC;
        this.fGrammarCount = len;
        this.fGlobalComponents = new XSNamedMap[17];
        this.fNSComponents = new XSNamedMap[len][17];
        this.fNamespacesList = new StringListImpl(this.fNamespaces, this.fGrammarCount);
        this.fSubGroupMap = buildSubGroups();
    }

    private SymbolHash buildSubGroups_Org() {
        SubstitutionGroupHandler sgHandler = new SubstitutionGroupHandler(null);
        for (int i2 = 0; i2 < this.fGrammarCount; i2++) {
            sgHandler.addSubstitutionGroup(this.fGrammarList[i2].getSubstitutionGroups());
        }
        XSNamedMap elements = getComponents((short) 2);
        int len = elements.getLength();
        SymbolHash subGroupMap = new SymbolHash(len * 2);
        for (int i3 = 0; i3 < len; i3++) {
            XSElementDecl head = (XSElementDecl) elements.item(i3);
            XSElementDeclaration[] subGroup = sgHandler.getSubstitutionGroup(head);
            subGroupMap.put(head, subGroup.length > 0 ? new XSObjectListImpl(subGroup, subGroup.length) : XSObjectListImpl.EMPTY_LIST);
        }
        return subGroupMap;
    }

    private SymbolHash buildSubGroups() {
        SubstitutionGroupHandler sgHandler = new SubstitutionGroupHandler(null);
        for (int i2 = 0; i2 < this.fGrammarCount; i2++) {
            sgHandler.addSubstitutionGroup(this.fGrammarList[i2].getSubstitutionGroups());
        }
        XSObjectListImpl elements = getGlobalElements();
        int len = elements.getLength();
        SymbolHash subGroupMap = new SymbolHash(len * 2);
        for (int i3 = 0; i3 < len; i3++) {
            XSElementDecl head = (XSElementDecl) elements.item(i3);
            XSElementDeclaration[] subGroup = sgHandler.getSubstitutionGroup(head);
            subGroupMap.put(head, subGroup.length > 0 ? new XSObjectListImpl(subGroup, subGroup.length) : XSObjectListImpl.EMPTY_LIST);
        }
        return subGroupMap;
    }

    private XSObjectListImpl getGlobalElements() {
        SymbolHash[] tables = new SymbolHash[this.fGrammarCount];
        int length = 0;
        for (int i2 = 0; i2 < this.fGrammarCount; i2++) {
            tables[i2] = this.fGrammarList[i2].fAllGlobalElemDecls;
            length += tables[i2].getLength();
        }
        if (length == 0) {
            return XSObjectListImpl.EMPTY_LIST;
        }
        XSObject[] components = new XSObject[length];
        int start = 0;
        for (int i3 = 0; i3 < this.fGrammarCount; i3++) {
            tables[i3].getValues(components, start);
            start += tables[i3].getLength();
        }
        return new XSObjectListImpl(components, length);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public StringList getNamespaces() {
        return this.fNamespacesList;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public XSNamespaceItemList getNamespaceItems() {
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public synchronized XSNamedMap getComponents(short objectType) {
        if (objectType <= 0 || objectType > 16 || !GLOBAL_COMP[objectType]) {
            return XSNamedMapImpl.EMPTY_MAP;
        }
        SymbolHash[] tables = new SymbolHash[this.fGrammarCount];
        if (this.fGlobalComponents[objectType] == null) {
            for (int i2 = 0; i2 < this.fGrammarCount; i2++) {
                switch (objectType) {
                    case 1:
                        tables[i2] = this.fGrammarList[i2].fGlobalAttrDecls;
                        break;
                    case 2:
                        tables[i2] = this.fGrammarList[i2].fGlobalElemDecls;
                        break;
                    case 3:
                    case 15:
                    case 16:
                        tables[i2] = this.fGrammarList[i2].fGlobalTypeDecls;
                        break;
                    case 5:
                        tables[i2] = this.fGrammarList[i2].fGlobalAttrGrpDecls;
                        break;
                    case 6:
                        tables[i2] = this.fGrammarList[i2].fGlobalGroupDecls;
                        break;
                    case 11:
                        tables[i2] = this.fGrammarList[i2].fGlobalNotationDecls;
                        break;
                }
            }
            if (objectType == 15 || objectType == 16) {
                this.fGlobalComponents[objectType] = new XSNamedMap4Types(this.fNamespaces, tables, this.fGrammarCount, objectType);
            } else {
                this.fGlobalComponents[objectType] = new XSNamedMapImpl(this.fNamespaces, tables, this.fGrammarCount);
            }
        }
        return this.fGlobalComponents[objectType];
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public synchronized XSNamedMap getComponentsByNamespace(short objectType, String namespace) {
        if (objectType <= 0 || objectType > 16 || !GLOBAL_COMP[objectType]) {
            return XSNamedMapImpl.EMPTY_MAP;
        }
        int i2 = 0;
        if (namespace != null) {
            while (i2 < this.fGrammarCount && !namespace.equals(this.fNamespaces[i2])) {
                i2++;
            }
        } else {
            while (i2 < this.fGrammarCount && this.fNamespaces[i2] != null) {
                i2++;
            }
        }
        if (i2 == this.fGrammarCount) {
            return XSNamedMapImpl.EMPTY_MAP;
        }
        if (this.fNSComponents[i2][objectType] == null) {
            SymbolHash table = null;
            switch (objectType) {
                case 1:
                    table = this.fGrammarList[i2].fGlobalAttrDecls;
                    break;
                case 2:
                    table = this.fGrammarList[i2].fGlobalElemDecls;
                    break;
                case 3:
                case 15:
                case 16:
                    table = this.fGrammarList[i2].fGlobalTypeDecls;
                    break;
                case 5:
                    table = this.fGrammarList[i2].fGlobalAttrGrpDecls;
                    break;
                case 6:
                    table = this.fGrammarList[i2].fGlobalGroupDecls;
                    break;
                case 11:
                    table = this.fGrammarList[i2].fGlobalNotationDecls;
                    break;
            }
            if (objectType == 15 || objectType == 16) {
                this.fNSComponents[i2][objectType] = new XSNamedMap4Types(namespace, table, objectType);
            } else {
                this.fNSComponents[i2][objectType] = new XSNamedMapImpl(namespace, table);
            }
        }
        return this.fNSComponents[i2][objectType];
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public XSTypeDefinition getTypeDefinition(String name, String namespace) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return (XSTypeDefinition) sg.fGlobalTypeDecls.get(name);
    }

    public XSTypeDefinition getTypeDefinition(String name, String namespace, String loc) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return sg.getGlobalTypeDecl(name, loc);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public XSAttributeDeclaration getAttributeDeclaration(String name, String namespace) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return (XSAttributeDeclaration) sg.fGlobalAttrDecls.get(name);
    }

    public XSAttributeDeclaration getAttributeDeclaration(String name, String namespace, String loc) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return sg.getGlobalAttributeDecl(name, loc);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public XSElementDeclaration getElementDeclaration(String name, String namespace) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return (XSElementDeclaration) sg.fGlobalElemDecls.get(name);
    }

    public XSElementDeclaration getElementDeclaration(String name, String namespace, String loc) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return sg.getGlobalElementDecl(name, loc);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public XSAttributeGroupDefinition getAttributeGroup(String name, String namespace) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return (XSAttributeGroupDefinition) sg.fGlobalAttrGrpDecls.get(name);
    }

    public XSAttributeGroupDefinition getAttributeGroup(String name, String namespace, String loc) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return sg.getGlobalAttributeGroupDecl(name, loc);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public XSModelGroupDefinition getModelGroupDefinition(String name, String namespace) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return (XSModelGroupDefinition) sg.fGlobalGroupDecls.get(name);
    }

    public XSModelGroupDefinition getModelGroupDefinition(String name, String namespace, String loc) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return sg.getGlobalGroupDecl(name, loc);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public XSNotationDeclaration getNotationDeclaration(String name, String namespace) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return (XSNotationDeclaration) sg.fGlobalNotationDecls.get(name);
    }

    public XSNotationDeclaration getNotationDeclaration(String name, String namespace, String loc) {
        SchemaGrammar sg = (SchemaGrammar) this.fGrammarMap.get(null2EmptyString(namespace));
        if (sg == null) {
            return null;
        }
        return sg.getGlobalNotationDecl(name, loc);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public synchronized XSObjectList getAnnotations() {
        if (this.fAnnotations != null) {
            return this.fAnnotations;
        }
        int totalAnnotations = 0;
        for (int i2 = 0; i2 < this.fGrammarCount; i2++) {
            totalAnnotations += this.fGrammarList[i2].fNumAnnotations;
        }
        if (totalAnnotations == 0) {
            this.fAnnotations = XSObjectListImpl.EMPTY_LIST;
            return this.fAnnotations;
        }
        XSAnnotationImpl[] annotations = new XSAnnotationImpl[totalAnnotations];
        int currPos = 0;
        for (int i3 = 0; i3 < this.fGrammarCount; i3++) {
            SchemaGrammar currGrammar = this.fGrammarList[i3];
            if (currGrammar.fNumAnnotations > 0) {
                System.arraycopy(currGrammar.fAnnotations, 0, annotations, currPos, currGrammar.fNumAnnotations);
                currPos += currGrammar.fNumAnnotations;
            }
        }
        this.fAnnotations = new XSObjectListImpl(annotations, annotations.length);
        return this.fAnnotations;
    }

    private static final String null2EmptyString(String str) {
        return str == null ? XMLSymbols.EMPTY_STRING : str;
    }

    public boolean hasIDConstraints() {
        return this.fHasIDC;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSModel
    public XSObjectList getSubstitutionGroup(XSElementDeclaration head) {
        return (XSObjectList) this.fSubGroupMap.get(head);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItemList
    public int getLength() {
        return this.fGrammarCount;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItemList
    public XSNamespaceItem item(int index) {
        if (index < 0 || index >= this.fGrammarCount) {
            return null;
        }
        return this.fGrammarList[index];
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int index) {
        if (index >= 0 && index < this.fGrammarCount) {
            return this.fGrammarList[index];
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return getLength();
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return listIterator0(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator listIterator() {
        return listIterator0(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator listIterator(int index) {
        if (index >= 0 && index < this.fGrammarCount) {
            return listIterator0(index);
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    private ListIterator listIterator0(int index) {
        return new XSNamespaceItemListIterator(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] a2 = new Object[this.fGrammarCount];
        toArray0(a2);
        return a2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray(Object[] a2) {
        if (a2.length < this.fGrammarCount) {
            Class arrayClass = a2.getClass();
            Class componentType = arrayClass.getComponentType();
            a2 = (Object[]) Array.newInstance((Class<?>) componentType, this.fGrammarCount);
        }
        toArray0(a2);
        if (a2.length > this.fGrammarCount) {
            a2[this.fGrammarCount] = null;
        }
        return a2;
    }

    private void toArray0(Object[] a2) {
        if (this.fGrammarCount > 0) {
            System.arraycopy(this.fGrammarList, 0, a2, 0, this.fGrammarCount);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSModelImpl$XSNamespaceItemListIterator.class */
    private final class XSNamespaceItemListIterator implements ListIterator {
        private int index;

        public XSNamespaceItemListIterator(int index) {
            this.index = index;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.index < XSModelImpl.this.fGrammarCount;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public Object next() {
            if (this.index < XSModelImpl.this.fGrammarCount) {
                SchemaGrammar[] schemaGrammarArr = XSModelImpl.this.fGrammarList;
                int i2 = this.index;
                this.index = i2 + 1;
                return schemaGrammarArr[i2];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.index > 0;
        }

        @Override // java.util.ListIterator
        public Object previous() {
            if (this.index > 0) {
                SchemaGrammar[] schemaGrammarArr = XSModelImpl.this.fGrammarList;
                int i2 = this.index - 1;
                this.index = i2;
                return schemaGrammarArr[i2];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.index;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.index - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void set(Object o2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void add(Object o2) {
            throw new UnsupportedOperationException();
        }
    }
}
