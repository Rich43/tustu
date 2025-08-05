package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Attribute;
import com.sun.java.util.jar.pack.ConstantPool;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Package.class */
class Package {
    int verbose;
    final int magic = -889270259;
    int default_modtime;
    int default_options;
    Version defaultClassVersion;
    final Version minClassVersion;
    final Version maxClassVersion;
    final Version packageVersion;
    Version observedHighestClassVersion;
    ConstantPool.IndexGroup cp;
    public static final Attribute.Layout attrCodeEmpty;
    public static final Attribute.Layout attrBootstrapMethodsEmpty;
    public static final Attribute.Layout attrInnerClassesEmpty;
    public static final Attribute.Layout attrSourceFileSpecial;
    public static final Map<Attribute.Layout, Attribute> attrDefs;
    ArrayList<Class> classes;
    ArrayList<File> files;
    List<InnerClass> allInnerClasses;
    Map<ConstantPool.ClassEntry, InnerClass> allInnerClassesByThis;
    private static final int SLASH_MIN = 46;
    private static final int SLASH_MAX = 47;
    private static final int DOLLAR_MIN = 0;
    private static final int DOLLAR_MAX = 45;
    static final List<Object> noObjects;
    static final List<Class.Field> noFields;
    static final List<Class.Method> noMethods;
    static final List<InnerClass> noInnerClasses;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Package.class.desiredAssertionStatus();
        HashMap map = new HashMap(3);
        attrCodeEmpty = Attribute.define(map, 2, "Code", "").layout();
        attrBootstrapMethodsEmpty = Attribute.define(map, 0, "BootstrapMethods", "").layout();
        attrInnerClassesEmpty = Attribute.define(map, 0, "InnerClasses", "").layout();
        attrSourceFileSpecial = Attribute.define(map, 0, "SourceFile", "RUNH").layout();
        attrDefs = Collections.unmodifiableMap(map);
        if (!$assertionsDisabled && lastIndexOf(0, 45, "x$$y$", 4) != 2) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && lastIndexOf(46, 47, "x//y/", 4) != 2) {
            throw new AssertionError();
        }
        noObjects = Arrays.asList(new Object[0]);
        noFields = Arrays.asList(new Class.Field[0]);
        noMethods = Arrays.asList(new Class.Method[0]);
        noInnerClasses = Arrays.asList(new InnerClass[0]);
    }

    public Package() {
        PropMap propMapCurrentPropMap = Utils.currentPropMap();
        if (propMapCurrentPropMap != null) {
            this.verbose = propMapCurrentPropMap.getInteger("com.sun.java.util.jar.pack.verbose");
        }
        this.magic = Constants.JAVA_PACKAGE_MAGIC;
        this.default_modtime = 0;
        this.default_options = 0;
        this.defaultClassVersion = null;
        this.observedHighestClassVersion = null;
        this.cp = new ConstantPool.IndexGroup();
        this.classes = new ArrayList<>();
        this.files = new ArrayList<>();
        this.allInnerClasses = new ArrayList();
        this.minClassVersion = Constants.JAVA_MIN_CLASS_VERSION;
        this.maxClassVersion = Constants.JAVA_MAX_CLASS_VERSION;
        this.packageVersion = null;
    }

    public Package(Version version, Version version2, Version version3) {
        PropMap propMapCurrentPropMap = Utils.currentPropMap();
        if (propMapCurrentPropMap != null) {
            this.verbose = propMapCurrentPropMap.getInteger("com.sun.java.util.jar.pack.verbose");
        }
        this.magic = Constants.JAVA_PACKAGE_MAGIC;
        this.default_modtime = 0;
        this.default_options = 0;
        this.defaultClassVersion = null;
        this.observedHighestClassVersion = null;
        this.cp = new ConstantPool.IndexGroup();
        this.classes = new ArrayList<>();
        this.files = new ArrayList<>();
        this.allInnerClasses = new ArrayList();
        this.minClassVersion = version == null ? Constants.JAVA_MIN_CLASS_VERSION : version;
        this.maxClassVersion = version2 == null ? Constants.JAVA_MAX_CLASS_VERSION : version2;
        this.packageVersion = version3;
    }

    public void reset() {
        this.cp = new ConstantPool.IndexGroup();
        this.classes.clear();
        this.files.clear();
        BandStructure.nextSeqForDebug = 0;
        this.observedHighestClassVersion = null;
    }

    Version getDefaultClassVersion() {
        return this.defaultClassVersion;
    }

    private void setHighestClassVersion() {
        if (this.observedHighestClassVersion != null) {
            return;
        }
        Version version = Constants.JAVA_MIN_CLASS_VERSION;
        Iterator<Class> it = this.classes.iterator();
        while (it.hasNext()) {
            Version version2 = it.next().getVersion();
            if (version.lessThan(version2)) {
                version = version2;
            }
        }
        this.observedHighestClassVersion = version;
    }

    Version getHighestClassVersion() {
        setHighestClassVersion();
        return this.observedHighestClassVersion;
    }

    public List<Class> getClasses() {
        return this.classes;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Package$Class.class */
    public final class Class extends Attribute.Holder implements Comparable<Class> {
        File file;
        int magic;
        Version version;
        ConstantPool.Entry[] cpMap;
        ConstantPool.ClassEntry thisClass;
        ConstantPool.ClassEntry superClass;
        ConstantPool.ClassEntry[] interfaces;
        ArrayList<Field> fields;
        ArrayList<Method> methods;
        ArrayList<InnerClass> innerClasses;
        ArrayList<ConstantPool.BootstrapMethodEntry> bootstrapMethods;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Package.class.desiredAssertionStatus();
        }

        public Package getPackage() {
            return Package.this;
        }

        Class(int i2, ConstantPool.ClassEntry classEntry, ConstantPool.ClassEntry classEntry2, ConstantPool.ClassEntry[] classEntryArr) {
            this.magic = Constants.JAVA_MAGIC;
            this.version = Package.this.defaultClassVersion;
            this.flags = i2;
            this.thisClass = classEntry;
            this.superClass = classEntry2;
            this.interfaces = classEntryArr;
            boolean zAdd = Package.this.classes.add(this);
            if (!$assertionsDisabled && !zAdd) {
                throw new AssertionError();
            }
        }

        Class(String str) {
            initFile(Package.this.newStub(str));
        }

        List<Field> getFields() {
            return this.fields == null ? Package.noFields : this.fields;
        }

        List<Method> getMethods() {
            return this.methods == null ? Package.noMethods : this.methods;
        }

        public String getName() {
            return this.thisClass.stringValue();
        }

        Version getVersion() {
            return this.version;
        }

        @Override // java.lang.Comparable
        public int compareTo(Class r4) {
            return getName().compareTo(r4.getName());
        }

        String getObviousSourceFile() {
            return Package.getObviousSourceFile(getName());
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void transformSourceFile(boolean z2) {
            Attribute attribute = getAttribute(Package.attrSourceFileSpecial);
            if (attribute == null) {
                return;
            }
            String obviousSourceFile = getObviousSourceFile();
            ArrayList arrayList = new ArrayList(1);
            attribute.visitRefs(this, 1, arrayList);
            ConstantPool.Utf8Entry utf8Entry = (ConstantPool.Utf8Entry) arrayList.get(0);
            Attribute attributeAddContent = attribute;
            if (utf8Entry == null) {
                if (z2) {
                    attributeAddContent = Attribute.find(0, "SourceFile", PdfOps.H_TOKEN).addContent(new byte[2]);
                } else {
                    byte[] bArr = new byte[2];
                    attributeAddContent = Package.attrSourceFileSpecial.addContent(bArr, Fixups.addRefWithBytes(null, bArr, Package.getRefString(obviousSourceFile)));
                }
            } else if (obviousSourceFile.equals(utf8Entry.stringValue())) {
                if (z2) {
                    attributeAddContent = Package.attrSourceFileSpecial.addContent(new byte[2]);
                } else if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            if (attributeAddContent != attribute) {
                if (Package.this.verbose > 2) {
                    Utils.log.fine("recoding obvious SourceFile=" + obviousSourceFile);
                }
                List<Attribute> arrayList2 = new ArrayList<>(getAttributes());
                arrayList2.set(arrayList2.indexOf(attribute), attributeAddContent);
                setAttributes(arrayList2);
            }
        }

        void minimizeSourceFile() {
            transformSourceFile(true);
        }

        void expandSourceFile() {
            transformSourceFile(false);
        }

        @Override // com.sun.java.util.jar.pack.Attribute.Holder
        protected ConstantPool.Entry[] getCPMap() {
            return this.cpMap;
        }

        protected void setCPMap(ConstantPool.Entry[] entryArr) {
            this.cpMap = entryArr;
        }

        boolean hasBootstrapMethods() {
            return (this.bootstrapMethods == null || this.bootstrapMethods.isEmpty()) ? false : true;
        }

        List<ConstantPool.BootstrapMethodEntry> getBootstrapMethods() {
            return this.bootstrapMethods;
        }

        ConstantPool.BootstrapMethodEntry[] getBootstrapMethodMap() {
            if (hasBootstrapMethods()) {
                return (ConstantPool.BootstrapMethodEntry[]) this.bootstrapMethods.toArray(new ConstantPool.BootstrapMethodEntry[this.bootstrapMethods.size()]);
            }
            return null;
        }

        void setBootstrapMethods(Collection<ConstantPool.BootstrapMethodEntry> collection) {
            if (!$assertionsDisabled && this.bootstrapMethods != null) {
                throw new AssertionError();
            }
            this.bootstrapMethods = new ArrayList<>(collection);
        }

        boolean hasInnerClasses() {
            return this.innerClasses != null;
        }

        List<InnerClass> getInnerClasses() {
            return this.innerClasses;
        }

        public void setInnerClasses(Collection<InnerClass> collection) {
            this.innerClasses = collection == null ? null : new ArrayList<>(collection);
            Attribute attribute = getAttribute(Package.attrInnerClassesEmpty);
            if (this.innerClasses != null && attribute == null) {
                addAttribute(Package.attrInnerClassesEmpty.canonicalInstance());
            } else if (this.innerClasses == null && attribute != null) {
                removeAttribute(attribute);
            }
        }

        public List<InnerClass> computeGloballyImpliedICs() {
            InnerClass globalInnerClass;
            HashSet<ConstantPool.Entry> hashSet = new HashSet();
            ArrayList<InnerClass> arrayList = this.innerClasses;
            this.innerClasses = null;
            visitRefs(0, hashSet);
            this.innerClasses = arrayList;
            ConstantPool.completeReferencesIn(hashSet, true);
            HashSet hashSet2 = new HashSet();
            for (ConstantPool.Entry entry : hashSet) {
                if (entry instanceof ConstantPool.ClassEntry) {
                    while (entry != null && (globalInnerClass = Package.this.getGlobalInnerClass(entry)) != null && hashSet2.add(entry)) {
                        entry = globalInnerClass.outerClass;
                    }
                }
            }
            ArrayList arrayList2 = new ArrayList();
            for (InnerClass innerClass : Package.this.allInnerClasses) {
                if (hashSet2.contains(innerClass.thisClass) || innerClass.outerClass == this.thisClass) {
                    if (Package.this.verbose > 1) {
                        Utils.log.fine("Relevant IC: " + ((Object) innerClass));
                    }
                    arrayList2.add(innerClass);
                }
            }
            return arrayList2;
        }

        private List<InnerClass> computeICdiff() {
            List<InnerClass> listComputeGloballyImpliedICs = computeGloballyImpliedICs();
            List<InnerClass> innerClasses = getInnerClasses();
            if (innerClasses == null) {
                innerClasses = Collections.emptyList();
            }
            if (innerClasses.isEmpty()) {
                return listComputeGloballyImpliedICs;
            }
            if (listComputeGloballyImpliedICs.isEmpty()) {
                return innerClasses;
            }
            HashSet hashSet = new HashSet(innerClasses);
            hashSet.retainAll(new HashSet(listComputeGloballyImpliedICs));
            listComputeGloballyImpliedICs.addAll(innerClasses);
            listComputeGloballyImpliedICs.removeAll(hashSet);
            return listComputeGloballyImpliedICs;
        }

        void minimizeLocalICs() {
            List<InnerClass> listEmptyList;
            List<InnerClass> listComputeICdiff = computeICdiff();
            ArrayList<InnerClass> arrayList = this.innerClasses;
            if (listComputeICdiff.isEmpty()) {
                listEmptyList = null;
                if (arrayList != null && arrayList.isEmpty() && Package.this.verbose > 0) {
                    Utils.log.info("Warning: Dropping empty InnerClasses attribute from " + ((Object) this));
                }
            } else {
                listEmptyList = arrayList == null ? Collections.emptyList() : listComputeICdiff;
            }
            setInnerClasses(listEmptyList);
            if (Package.this.verbose > 1 && listEmptyList != null) {
                Utils.log.fine("keeping local ICs in " + ((Object) this) + ": " + ((Object) listEmptyList));
            }
        }

        int expandLocalICs() {
            List<InnerClass> listComputeICdiff;
            int i2;
            ArrayList<InnerClass> arrayList = this.innerClasses;
            if (arrayList == null) {
                List<InnerClass> listComputeGloballyImpliedICs = computeGloballyImpliedICs();
                if (listComputeGloballyImpliedICs.isEmpty()) {
                    listComputeICdiff = null;
                    i2 = 0;
                } else {
                    listComputeICdiff = listComputeGloballyImpliedICs;
                    i2 = 1;
                }
            } else if (arrayList.isEmpty()) {
                listComputeICdiff = null;
                i2 = 0;
            } else {
                listComputeICdiff = computeICdiff();
                i2 = listComputeICdiff.containsAll(arrayList) ? 1 : -1;
            }
            setInnerClasses(listComputeICdiff);
            return i2;
        }

        /* loaded from: rt.jar:com/sun/java/util/jar/pack/Package$Class$Member.class */
        public abstract class Member extends Attribute.Holder implements Comparable<Member> {
            ConstantPool.DescriptorEntry descriptor;

            protected Member(int i2, ConstantPool.DescriptorEntry descriptorEntry) {
                this.flags = i2;
                this.descriptor = descriptorEntry;
            }

            public Class thisClass() {
                return Class.this;
            }

            public ConstantPool.DescriptorEntry getDescriptor() {
                return this.descriptor;
            }

            public String getName() {
                return this.descriptor.nameRef.stringValue();
            }

            public String getType() {
                return this.descriptor.typeRef.stringValue();
            }

            @Override // com.sun.java.util.jar.pack.Attribute.Holder
            protected ConstantPool.Entry[] getCPMap() {
                return Class.this.cpMap;
            }

            @Override // com.sun.java.util.jar.pack.Attribute.Holder
            protected void visitRefs(int i2, Collection<ConstantPool.Entry> collection) {
                if (Package.this.verbose > 2) {
                    Utils.log.fine("visitRefs " + ((Object) this));
                }
                if (i2 == 0) {
                    collection.add(this.descriptor.nameRef);
                    collection.add(this.descriptor.typeRef);
                } else {
                    collection.add(this.descriptor);
                }
                super.visitRefs(i2, collection);
            }

            public String toString() {
                return ((Object) Class.this) + "." + this.descriptor.prettyString();
            }
        }

        /* loaded from: rt.jar:com/sun/java/util/jar/pack/Package$Class$Field.class */
        public class Field extends Member {
            int order;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !Package.class.desiredAssertionStatus();
            }

            public Field(int i2, ConstantPool.DescriptorEntry descriptorEntry) {
                super(i2, descriptorEntry);
                if (!$assertionsDisabled && descriptorEntry.isMethod()) {
                    throw new AssertionError();
                }
                if (Class.this.fields == null) {
                    Class.this.fields = new ArrayList<>();
                }
                boolean zAdd = Class.this.fields.add(this);
                if (!$assertionsDisabled && !zAdd) {
                    throw new AssertionError();
                }
                this.order = Class.this.fields.size();
            }

            public byte getLiteralTag() {
                return this.descriptor.getLiteralTag();
            }

            @Override // java.lang.Comparable
            public int compareTo(Member member) {
                return this.order - ((Field) member).order;
            }
        }

        /* loaded from: rt.jar:com/sun/java/util/jar/pack/Package$Class$Method.class */
        public class Method extends Member {
            Code code;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !Package.class.desiredAssertionStatus();
            }

            public Method(int i2, ConstantPool.DescriptorEntry descriptorEntry) {
                super(i2, descriptorEntry);
                if (!$assertionsDisabled && !descriptorEntry.isMethod()) {
                    throw new AssertionError();
                }
                if (Class.this.methods == null) {
                    Class.this.methods = new ArrayList<>();
                }
                boolean zAdd = Class.this.methods.add(this);
                if (!$assertionsDisabled && !zAdd) {
                    throw new AssertionError();
                }
            }

            @Override // com.sun.java.util.jar.pack.Attribute.Holder
            public void trimToSize() {
                super.trimToSize();
                if (this.code != null) {
                    this.code.trimToSize();
                }
            }

            public int getArgumentSize() {
                return (Modifier.isStatic(this.flags) ? 0 : 1) + this.descriptor.typeRef.computeSize(true);
            }

            @Override // java.lang.Comparable
            public int compareTo(Member member) {
                return getDescriptor().compareTo(((Method) member).getDescriptor());
            }

            @Override // com.sun.java.util.jar.pack.Attribute.Holder
            public void strip(String str) {
                if ("Code".equals(str)) {
                    this.code = null;
                }
                if (this.code != null) {
                    this.code.strip(str);
                }
                super.strip(str);
            }

            @Override // com.sun.java.util.jar.pack.Package.Class.Member, com.sun.java.util.jar.pack.Attribute.Holder
            protected void visitRefs(int i2, Collection<ConstantPool.Entry> collection) {
                super.visitRefs(i2, collection);
                if (this.code != null) {
                    if (i2 == 0) {
                        collection.add(Package.getRefString("Code"));
                    }
                    this.code.visitRefs(i2, collection);
                }
            }
        }

        @Override // com.sun.java.util.jar.pack.Attribute.Holder
        public void trimToSize() {
            super.trimToSize();
            int i2 = 0;
            while (i2 <= 1) {
                ArrayList arrayList = i2 == 0 ? this.fields : this.methods;
                if (arrayList != null) {
                    arrayList.trimToSize();
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ((Member) it.next()).trimToSize();
                    }
                }
                i2++;
            }
            if (this.innerClasses != null) {
                this.innerClasses.trimToSize();
            }
        }

        @Override // com.sun.java.util.jar.pack.Attribute.Holder
        public void strip(String str) {
            if ("InnerClass".equals(str)) {
                this.innerClasses = null;
            }
            int i2 = 0;
            while (i2 <= 1) {
                ArrayList arrayList = i2 == 0 ? this.fields : this.methods;
                if (arrayList != null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ((Member) it.next()).strip(str);
                    }
                }
                i2++;
            }
            super.strip(str);
        }

        @Override // com.sun.java.util.jar.pack.Attribute.Holder
        protected void visitRefs(int i2, Collection<ConstantPool.Entry> collection) {
            if (Package.this.verbose > 2) {
                Utils.log.fine("visitRefs " + ((Object) this));
            }
            collection.add(this.thisClass);
            collection.add(this.superClass);
            collection.addAll(Arrays.asList(this.interfaces));
            int i3 = 0;
            while (i3 <= 1) {
                ArrayList arrayList = i3 == 0 ? this.fields : this.methods;
                if (arrayList != null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        Member member = (Member) it.next();
                        boolean z2 = false;
                        try {
                            member.visitRefs(i2, collection);
                            z2 = true;
                            if (1 == 0) {
                                Utils.log.warning("Error scanning " + ((Object) member));
                            }
                        } catch (Throwable th) {
                            if (!z2) {
                                Utils.log.warning("Error scanning " + ((Object) member));
                            }
                            throw th;
                        }
                    }
                }
                i3++;
            }
            visitInnerClassRefs(i2, collection);
            super.visitRefs(i2, collection);
        }

        protected void visitInnerClassRefs(int i2, Collection<ConstantPool.Entry> collection) {
            Package.visitInnerClassRefs(this.innerClasses, i2, collection);
        }

        void finishReading() {
            trimToSize();
            maybeChooseFileName();
        }

        public void initFile(File file) {
            if (!$assertionsDisabled && this.file != null) {
                throw new AssertionError();
            }
            if (file == null) {
                file = Package.this.newStub(canonicalFileName());
            }
            this.file = file;
            if (!$assertionsDisabled && !file.isClassStub()) {
                throw new AssertionError();
            }
            file.stubClass = this;
            maybeChooseFileName();
        }

        public void maybeChooseFileName() {
            if (this.thisClass == null) {
                return;
            }
            String strCanonicalFileName = canonicalFileName();
            if (this.file.nameString.equals("")) {
                this.file.nameString = strCanonicalFileName;
            }
            if (this.file.nameString.equals(strCanonicalFileName)) {
                this.file.name = Package.getRefString("");
            } else if (this.file.name == null) {
                this.file.name = Package.getRefString(this.file.nameString);
            }
        }

        public String canonicalFileName() {
            if (this.thisClass == null) {
                return null;
            }
            return this.thisClass.stringValue() + ".class";
        }

        public java.io.File getFileName(java.io.File file) {
            String strStringValue = this.file.name.stringValue();
            if (strStringValue.equals("")) {
                strStringValue = canonicalFileName();
            }
            return new java.io.File(file, strStringValue.replace('/', java.io.File.separatorChar));
        }

        public java.io.File getFileName() {
            return getFileName(null);
        }

        public String toString() {
            return this.thisClass.stringValue();
        }
    }

    void addClass(Class r4) {
        if (!$assertionsDisabled && r4.getPackage() != this) {
            throw new AssertionError();
        }
        boolean zAdd = this.classes.add(r4);
        if (!$assertionsDisabled && !zAdd) {
            throw new AssertionError();
        }
        if (r4.file == null) {
            r4.initFile(null);
        }
        addFile(r4.file);
    }

    public List<File> getFiles() {
        return this.files;
    }

    public List<File> getClassStubs() {
        ArrayList arrayList = new ArrayList(this.classes.size());
        Iterator<Class> it = this.classes.iterator();
        while (it.hasNext()) {
            Class next = it.next();
            if (!$assertionsDisabled && !next.file.isClassStub()) {
                throw new AssertionError();
            }
            arrayList.add(next.file);
        }
        return arrayList;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Package$File.class */
    public final class File implements Comparable<File> {
        String nameString;
        ConstantPool.Utf8Entry name;
        Class stubClass;
        static final /* synthetic */ boolean $assertionsDisabled;
        int modtime = 0;
        int options = 0;
        ArrayList<byte[]> prepend = new ArrayList<>();
        ByteArrayOutputStream append = new ByteArrayOutputStream();

        static {
            $assertionsDisabled = !Package.class.desiredAssertionStatus();
        }

        File(ConstantPool.Utf8Entry utf8Entry) {
            this.name = utf8Entry;
            this.nameString = utf8Entry.stringValue();
        }

        File(String str) {
            this.name = Package.getRefString(Package.fixupFileName(str));
            this.nameString = this.name.stringValue();
        }

        public boolean isDirectory() {
            return this.nameString.endsWith("/");
        }

        public boolean isClassStub() {
            return (this.options & 2) != 0;
        }

        public Class getStubClass() {
            if (!$assertionsDisabled && !isClassStub()) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || this.stubClass != null) {
                return this.stubClass;
            }
            throw new AssertionError();
        }

        public boolean isTrivialClassStub() {
            return isClassStub() && this.name.stringValue().equals("") && (this.modtime == 0 || this.modtime == Package.this.default_modtime) && (this.options & (-3)) == 0;
        }

        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != File.class) {
                return false;
            }
            return ((File) obj).nameString.equals(this.nameString);
        }

        public int hashCode() {
            return this.nameString.hashCode();
        }

        @Override // java.lang.Comparable
        public int compareTo(File file) {
            return this.nameString.compareTo(file.nameString);
        }

        public String toString() {
            return this.nameString + VectorFormat.DEFAULT_PREFIX + (isClassStub() ? "*" : "") + (BandStructure.testBit(this.options, 1) ? "@" : "") + (this.modtime == 0 ? "" : PdfOps.M_TOKEN + this.modtime) + (getFileLength() == 0 ? "" : "[" + getFileLength() + "]") + "}";
        }

        public java.io.File getFileName() {
            return getFileName(null);
        }

        public java.io.File getFileName(java.io.File file) {
            return new java.io.File(file, this.nameString.replace('/', java.io.File.separatorChar));
        }

        public void addBytes(byte[] bArr) {
            addBytes(bArr, 0, bArr.length);
        }

        public void addBytes(byte[] bArr, int i2, int i3) {
            if (((this.append.size() | i3) << 2) < 0) {
                this.prepend.add(this.append.toByteArray());
                this.append.reset();
            }
            this.append.write(bArr, i2, i3);
        }

        public long getFileLength() {
            long length = 0;
            if (this.prepend == null || this.append == null) {
                return 0L;
            }
            while (this.prepend.iterator().hasNext()) {
                length += r0.next().length;
            }
            return length + this.append.size();
        }

        public void writeTo(OutputStream outputStream) throws IOException {
            if (this.prepend == null || this.append == null) {
                return;
            }
            Iterator<byte[]> it = this.prepend.iterator();
            while (it.hasNext()) {
                outputStream.write(it.next());
            }
            this.append.writeTo(outputStream);
        }

        public void readFrom(InputStream inputStream) throws IOException {
            byte[] bArr = new byte[65536];
            while (true) {
                int i2 = inputStream.read(bArr);
                if (i2 > 0) {
                    addBytes(bArr, 0, i2);
                } else {
                    return;
                }
            }
        }

        public InputStream getInputStream() {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.append.toByteArray());
            if (this.prepend.isEmpty()) {
                return byteArrayInputStream;
            }
            ArrayList arrayList = new ArrayList(this.prepend.size() + 1);
            Iterator<byte[]> it = this.prepend.iterator();
            while (it.hasNext()) {
                arrayList.add(new ByteArrayInputStream(it.next()));
            }
            arrayList.add(byteArrayInputStream);
            return new SequenceInputStream(Collections.enumeration(arrayList));
        }

        protected void visitRefs(int i2, Collection<ConstantPool.Entry> collection) {
            if (!$assertionsDisabled && this.name == null) {
                throw new AssertionError();
            }
            collection.add(this.name);
        }
    }

    File newStub(String str) {
        File file = new File(str);
        file.options |= 2;
        file.prepend = null;
        file.append = null;
        return file;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String fixupFileName(String str) {
        String strReplace = str.replace(java.io.File.separatorChar, '/');
        if (strReplace.startsWith("/")) {
            throw new IllegalArgumentException("absolute file name " + strReplace);
        }
        return strReplace;
    }

    void addFile(File file) {
        boolean zAdd = this.files.add(file);
        if (!$assertionsDisabled && !zAdd) {
            throw new AssertionError();
        }
    }

    public List<InnerClass> getAllInnerClasses() {
        return this.allInnerClasses;
    }

    public void setAllInnerClasses(Collection<InnerClass> collection) {
        if (!$assertionsDisabled && collection == this.allInnerClasses) {
            throw new AssertionError();
        }
        this.allInnerClasses.clear();
        this.allInnerClasses.addAll(collection);
        this.allInnerClassesByThis = new HashMap(this.allInnerClasses.size());
        for (InnerClass innerClass : this.allInnerClasses) {
            InnerClass innerClassPut = this.allInnerClassesByThis.put(innerClass.thisClass, innerClass);
            if (!$assertionsDisabled && innerClassPut != null) {
                throw new AssertionError();
            }
        }
    }

    public InnerClass getGlobalInnerClass(ConstantPool.Entry entry) {
        if ($assertionsDisabled || (entry instanceof ConstantPool.ClassEntry)) {
            return this.allInnerClassesByThis.get(entry);
        }
        throw new AssertionError();
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Package$InnerClass.class */
    static class InnerClass implements Comparable<InnerClass> {
        final ConstantPool.ClassEntry thisClass;
        final ConstantPool.ClassEntry outerClass;
        final ConstantPool.Utf8Entry name;
        final int flags;
        final boolean predictable = computePredictable();

        InnerClass(ConstantPool.ClassEntry classEntry, ConstantPool.ClassEntry classEntry2, ConstantPool.Utf8Entry utf8Entry, int i2) {
            this.thisClass = classEntry;
            this.outerClass = classEntry2;
            this.name = utf8Entry;
            this.flags = i2;
        }

        private boolean computePredictable() {
            String[] innerClassName = Package.parseInnerClassName(this.thisClass.stringValue());
            if (innerClassName == null) {
                return false;
            }
            return innerClassName[2] == (this.name == null ? null : this.name.stringValue()) && innerClassName[0] == (this.outerClass == null ? null : this.outerClass.stringValue());
        }

        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != InnerClass.class) {
                return false;
            }
            InnerClass innerClass = (InnerClass) obj;
            return eq(this.thisClass, innerClass.thisClass) && eq(this.outerClass, innerClass.outerClass) && eq(this.name, innerClass.name) && this.flags == innerClass.flags;
        }

        private static boolean eq(Object obj, Object obj2) {
            return obj == null ? obj2 == null : obj.equals(obj2);
        }

        public int hashCode() {
            return this.thisClass.hashCode();
        }

        @Override // java.lang.Comparable
        public int compareTo(InnerClass innerClass) {
            return this.thisClass.compareTo(innerClass.thisClass);
        }

        protected void visitRefs(int i2, Collection<ConstantPool.Entry> collection) {
            collection.add(this.thisClass);
            if (i2 == 0 || !this.predictable) {
                collection.add(this.outerClass);
                collection.add(this.name);
            }
        }

        public String toString() {
            return this.thisClass.stringValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void visitInnerClassRefs(Collection<InnerClass> collection, int i2, Collection<ConstantPool.Entry> collection2) {
        if (collection == null) {
            return;
        }
        if (i2 == 0) {
            collection2.add(getRefString("InnerClasses"));
        }
        if (collection.size() > 0) {
            Iterator<InnerClass> it = collection.iterator();
            while (it.hasNext()) {
                it.next().visitRefs(i2, collection2);
            }
        }
    }

    static String[] parseInnerClassName(String str) {
        int i2;
        String strSubstring;
        String strIntern;
        String strIntern2;
        int length = str.length();
        int iLastIndexOf = lastIndexOf(46, 47, str, str.length()) + 1;
        int iLastIndexOf2 = lastIndexOf(0, 45, str, str.length());
        if (iLastIndexOf2 < iLastIndexOf) {
            return null;
        }
        if (isDigitString(str, iLastIndexOf2 + 1, length)) {
            strSubstring = str.substring(iLastIndexOf2 + 1, length);
            strIntern = null;
            i2 = iLastIndexOf2;
        } else {
            int iLastIndexOf3 = lastIndexOf(0, 45, str, iLastIndexOf2 - 1);
            i2 = iLastIndexOf3;
            if (iLastIndexOf3 > iLastIndexOf && isDigitString(str, i2 + 1, iLastIndexOf2)) {
                strSubstring = str.substring(i2 + 1, iLastIndexOf2);
                strIntern = str.substring(iLastIndexOf2 + 1, length).intern();
            } else {
                i2 = iLastIndexOf2;
                strSubstring = null;
                strIntern = str.substring(iLastIndexOf2 + 1, length).intern();
            }
        }
        if (strSubstring == null) {
            strIntern2 = str.substring(0, i2).intern();
        } else {
            strIntern2 = null;
        }
        return new String[]{strIntern2, strSubstring, strIntern};
    }

    private static int lastIndexOf(int i2, int i3, String str, int i4) {
        int i5 = i4;
        while (true) {
            i5--;
            if (i5 >= 0) {
                char cCharAt = str.charAt(i5);
                if (cCharAt >= i2 && cCharAt <= i3) {
                    return i5;
                }
            } else {
                return -1;
            }
        }
    }

    private static boolean isDigitString(String str, int i2, int i3) {
        if (i2 == i3) {
            return false;
        }
        for (int i4 = i2; i4 < i3; i4++) {
            char cCharAt = str.charAt(i4);
            if (cCharAt < '0' || cCharAt > '9') {
                return false;
            }
        }
        return true;
    }

    static String getObviousSourceFile(String str) {
        String strSubstring = str.substring(lastIndexOf(46, 47, str, str.length()) + 1);
        int length = strSubstring.length();
        do {
            int iLastIndexOf = lastIndexOf(0, 45, strSubstring, length - 1);
            if (iLastIndexOf < 0) {
                break;
            }
            length = iLastIndexOf;
        } while (length != 0);
        return strSubstring.substring(0, length) + ".java";
    }

    static ConstantPool.Utf8Entry getRefString(String str) {
        return ConstantPool.getUtf8Entry(str);
    }

    static ConstantPool.LiteralEntry getRefLiteral(Comparable<?> comparable) {
        return ConstantPool.getLiteralEntry(comparable);
    }

    void stripAttributeKind(String str) {
        if (this.verbose > 0) {
            Utils.log.info("Stripping " + str.toLowerCase() + " data and attributes...");
        }
        switch (str) {
            case "Debug":
                strip("SourceFile");
                strip("LineNumberTable");
                strip("LocalVariableTable");
                strip("LocalVariableTypeTable");
                break;
            case "Compile":
                strip("Deprecated");
                strip("Synthetic");
                break;
            case "Exceptions":
                strip("Exceptions");
                break;
            case "Constant":
                stripConstantFields();
                break;
        }
    }

    public void trimToSize() {
        this.classes.trimToSize();
        Iterator<Class> it = this.classes.iterator();
        while (it.hasNext()) {
            it.next().trimToSize();
        }
        this.files.trimToSize();
    }

    public void strip(String str) {
        Iterator<Class> it = this.classes.iterator();
        while (it.hasNext()) {
            it.next().strip(str);
        }
    }

    public void stripConstantFields() {
        Iterator<Class> it = this.classes.iterator();
        while (it.hasNext()) {
            Iterator<Class.Field> it2 = it.next().fields.iterator();
            while (it2.hasNext()) {
                Class.Field next = it2.next();
                if (Modifier.isFinal(next.flags) && Modifier.isStatic(next.flags) && next.getAttribute("ConstantValue") != null && !next.getName().startsWith("serial") && this.verbose > 2) {
                    Utils.log.fine(">> Strip " + ((Object) this) + " ConstantValue");
                    it2.remove();
                }
            }
        }
    }

    protected void visitRefs(int i2, Collection<ConstantPool.Entry> collection) {
        Iterator<Class> it = this.classes.iterator();
        while (it.hasNext()) {
            it.next().visitRefs(i2, collection);
        }
        if (i2 != 0) {
            Iterator<File> it2 = this.files.iterator();
            while (it2.hasNext()) {
                it2.next().visitRefs(i2, collection);
            }
            visitInnerClassRefs(this.allInnerClasses, i2, collection);
        }
    }

    void reorderFiles(boolean z2, boolean z3) {
        if (!z2) {
            Collections.sort(this.classes);
        }
        List<File> classStubs = getClassStubs();
        Iterator<File> it = this.files.iterator();
        while (it.hasNext()) {
            File next = it.next();
            if (next.isClassStub() || (z3 && next.isDirectory())) {
                it.remove();
            }
        }
        Collections.sort(this.files, new Comparator<File>() { // from class: com.sun.java.util.jar.pack.Package.1
            @Override // java.util.Comparator
            public int compare(File file, File file2) {
                String str = file.nameString;
                String str2 = file2.nameString;
                if (str.equals(str2)) {
                    return 0;
                }
                if (JarFile.MANIFEST_NAME.equals(str)) {
                    return -1;
                }
                if (JarFile.MANIFEST_NAME.equals(str2)) {
                    return 1;
                }
                String strSubstring = str.substring(1 + str.lastIndexOf(47));
                String strSubstring2 = str2.substring(1 + str2.lastIndexOf(47));
                int iCompareTo = strSubstring.substring(1 + strSubstring.lastIndexOf(46)).compareTo(strSubstring2.substring(1 + strSubstring2.lastIndexOf(46)));
                return iCompareTo != 0 ? iCompareTo : str.compareTo(str2);
            }
        });
        this.files.addAll(classStubs);
    }

    void trimStubs() {
        ListIterator<File> listIterator = this.files.listIterator(this.files.size());
        while (true) {
            if (!listIterator.hasPrevious()) {
                break;
            }
            File filePrevious = listIterator.previous();
            if (!filePrevious.isTrivialClassStub()) {
                if (this.verbose > 1) {
                    Utils.log.fine("Keeping last non-trivial " + ((Object) filePrevious));
                }
            } else {
                if (this.verbose > 2) {
                    Utils.log.fine("Removing trivial " + ((Object) filePrevious));
                }
                listIterator.remove();
            }
        }
        if (this.verbose > 0) {
            Utils.log.info("Transmitting " + this.files.size() + " files, including per-file data for " + getClassStubs().size() + " classes out of " + this.classes.size());
        }
    }

    void buildGlobalConstantPool(Set<ConstantPool.Entry> set) {
        if (this.verbose > 1) {
            Utils.log.fine("Checking for unused CP entries");
        }
        set.add(getRefString(""));
        visitRefs(1, set);
        ConstantPool.completeReferencesIn(set, false);
        if (this.verbose > 1) {
            Utils.log.fine("Sorting CP entries");
        }
        ConstantPool.Index[] indexArrPartitionByTag = ConstantPool.partitionByTag(ConstantPool.makeIndex("unsorted", set));
        for (int i2 = 0; i2 < ConstantPool.TAGS_IN_ORDER.length; i2++) {
            byte b2 = ConstantPool.TAGS_IN_ORDER[i2];
            ConstantPool.Index index = indexArrPartitionByTag[b2];
            if (index != null) {
                ConstantPool.sort(index);
                this.cp.initIndexByTag(b2, index);
                indexArrPartitionByTag[b2] = null;
            }
        }
        for (ConstantPool.Index index2 : indexArrPartitionByTag) {
            if (!$assertionsDisabled && index2 != null) {
                throw new AssertionError();
            }
        }
        for (int i3 = 0; i3 < ConstantPool.TAGS_IN_ORDER.length; i3++) {
            ConstantPool.Index indexByTag = this.cp.getIndexByTag(ConstantPool.TAGS_IN_ORDER[i3]);
            if (!$assertionsDisabled && !indexByTag.assertIsSorted()) {
                throw new AssertionError();
            }
            if (this.verbose > 2) {
                Utils.log.fine(indexByTag.dumpString());
            }
        }
    }

    void ensureAllClassFiles() {
        HashSet hashSet = new HashSet(this.files);
        Iterator<Class> it = this.classes.iterator();
        while (it.hasNext()) {
            Class next = it.next();
            if (!hashSet.contains(next.file)) {
                this.files.add(next.file);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Package$Version.class */
    protected static final class Version {
        public final short major;
        public final short minor;

        private Version(short s2, short s3) {
            this.major = s2;
            this.minor = s3;
        }

        public String toString() {
            return ((int) this.major) + "." + ((int) this.minor);
        }

        public boolean equals(Object obj) {
            return (obj instanceof Version) && this.major == ((Version) obj).major && this.minor == ((Version) obj).minor;
        }

        public int intValue() {
            return (this.major << 16) + this.minor;
        }

        public int hashCode() {
            return (this.major << 16) + 7 + this.minor;
        }

        public static Version of(int i2, int i3) {
            return new Version((short) i2, (short) i3);
        }

        public static Version of(byte[] bArr) {
            return new Version((short) (((bArr[2] & 255) << 8) | (bArr[3] & 255)), (short) (((bArr[0] & 255) << 8) | (bArr[1] & 255)));
        }

        public static Version of(int i2) {
            return new Version((short) (i2 >>> 16), (short) i2);
        }

        public static Version makeVersion(PropMap propMap, String str) {
            int integer = propMap.getInteger("com.sun.java.util.jar.pack." + str + ".minver", -1);
            int integer2 = propMap.getInteger("com.sun.java.util.jar.pack." + str + ".majver", -1);
            if (integer < 0 || integer2 < 0) {
                return null;
            }
            return of(integer2, integer);
        }

        public byte[] asBytes() {
            return new byte[]{(byte) (this.minor >> 8), (byte) this.minor, (byte) (this.major >> 8), (byte) this.major};
        }

        public int compareTo(Version version) {
            return intValue() - version.intValue();
        }

        public boolean lessThan(Version version) {
            return compareTo(version) < 0;
        }

        public boolean greaterThan(Version version) {
            return compareTo(version) > 0;
        }
    }
}
