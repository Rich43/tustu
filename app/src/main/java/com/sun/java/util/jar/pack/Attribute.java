package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.ConstantPool;
import com.sun.java.util.jar.pack.Package;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Attribute.class */
class Attribute implements Comparable<Attribute> {
    Layout def;
    byte[] bytes;
    Object fixups;
    private static final Map<List<Attribute>, List<Attribute>> canonLists;
    private static final Map<Layout, Attribute> attributes;
    private static final Map<Layout, Attribute> standardDefs;
    static final byte EK_INT = 1;
    static final byte EK_BCI = 2;
    static final byte EK_BCO = 3;
    static final byte EK_FLAG = 4;
    static final byte EK_REPL = 5;
    static final byte EK_REF = 6;
    static final byte EK_UN = 7;
    static final byte EK_CASE = 8;
    static final byte EK_CALL = 9;
    static final byte EK_CBLE = 10;
    static final byte EF_SIGN = 1;
    static final byte EF_DELTA = 2;
    static final byte EF_NULL = 4;
    static final byte EF_BACK = 8;
    static final int NO_BAND_INDEX = -1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Attribute.class.desiredAssertionStatus();
        canonLists = new HashMap();
        attributes = new HashMap();
        standardDefs = new HashMap();
        Map<Layout, Attribute> map = standardDefs;
        define(map, 0, com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE, "RSH");
        define(map, 0, "Synthetic", "");
        define(map, 0, "Deprecated", "");
        define(map, 0, "SourceFile", "RUH");
        define(map, 0, "EnclosingMethod", "RCHRDNH");
        define(map, 0, "InnerClasses", "NH[RCHRCNHRUNHFH]");
        define(map, 0, "BootstrapMethods", "NH[RMHNH[KLH]]");
        define(map, 1, com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE, "RSH");
        define(map, 1, "Synthetic", "");
        define(map, 1, "Deprecated", "");
        define(map, 1, "ConstantValue", "KQH");
        define(map, 2, com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE, "RSH");
        define(map, 2, "Synthetic", "");
        define(map, 2, "Deprecated", "");
        define(map, 2, "Exceptions", "NH[RCH]");
        define(map, 2, "MethodParameters", "NB[RUNHFH]");
        define(map, 3, "StackMapTable", "[NH[(1)]][TB(64-127)[(2)](247)[(1)(2)](248-251)[(1)](252)[(1)(2)](253)[(1)(2)(2)](254)[(1)(2)(2)(2)](255)[(1)NH[(2)]NH[(2)]]()[]][H][TB(7)[RCH](8)[PH]()[]]");
        define(map, 3, "LineNumberTable", "NH[PHH]");
        define(map, 3, "LocalVariableTable", "NH[PHOHRUHRSHH]");
        define(map, 3, "LocalVariableTypeTable", "NH[PHOHRUHRSHH]");
        String[] strArr = {normalizeLayoutString("\n  # parameter_annotations :=\n  [ NB[(1)] ]     # forward call to annotations"), normalizeLayoutString("\n  # annotations :=\n  [ NH[(1)] ]     # forward call to annotation\n  "), normalizeLayoutString("\n  # annotation :=\n  [RSH\n    NH[RUH (1)]   # forward call to value\n    ]"), normalizeLayoutString("\n  # value :=\n  [TB # Callable 2 encodes one tagged value.\n    (\\B,\\C,\\I,\\S,\\Z)[KIH]\n    (\\D)[KDH]\n    (\\F)[KFH]\n    (\\J)[KJH]\n    (\\c)[RSH]\n    (\\e)[RSH RUH]\n    (\\s)[RUH]\n    (\\[)[NH[(0)]] # backward self-call to value\n    (\\@)[RSH NH[RUH (0)]] # backward self-call to value\n    ()[] ]")};
        String[] strArr2 = {normalizeLayoutString("\n # type-annotations :=\n  [ NH[(1)(2)(3)] ]     # forward call to type-annotations"), normalizeLayoutString("\n  # type-annotation :=\n  [TB\n    (0-1) [B] # {CLASS, METHOD}_TYPE_PARAMETER\n    (16) [FH] # CLASS_EXTENDS\n    (17-18) [BB] # {CLASS, METHOD}_TYPE_PARAMETER_BOUND\n    (19-21) [] # FIELD, METHOD_RETURN, METHOD_RECEIVER\n    (22) [B] # METHOD_FORMAL_PARAMETER\n    (23) [H] # THROWS\n    (64-65) [NH[PHOHH]] # LOCAL_VARIABLE, RESOURCE_VARIABLE\n    (66) [H] # EXCEPTION_PARAMETER\n    (67-70) [PH] # INSTANCEOF, NEW, {CONSTRUCTOR, METHOD}_REFERENCE_RECEIVER\n    (71-75) [PHB] # CAST, {CONSTRUCTOR,METHOD}_INVOCATION_TYPE_ARGUMENT, {CONSTRUCTOR, METHOD}_REFERENCE_TYPE_ARGUMENT\n    ()[] ]"), normalizeLayoutString("\n # type-path\n [ NB[BB] ]")};
        Map<Layout, Attribute> map2 = standardDefs;
        String str = strArr[3];
        String str2 = strArr[1] + strArr[2] + strArr[3];
        String str3 = strArr[0] + str2;
        String str4 = strArr2[0] + strArr2[1] + strArr2[2] + strArr[2] + strArr[3];
        for (int i2 = 0; i2 < 4; i2++) {
            if (i2 != 3) {
                define(map2, i2, "RuntimeVisibleAnnotations", str2);
                define(map2, i2, "RuntimeInvisibleAnnotations", str2);
                if (i2 == 2) {
                    define(map2, i2, "RuntimeVisibleParameterAnnotations", str3);
                    define(map2, i2, "RuntimeInvisibleParameterAnnotations", str3);
                    define(map2, i2, "AnnotationDefault", str);
                }
            }
            define(map2, i2, "RuntimeVisibleTypeAnnotations", str4);
            define(map2, i2, "RuntimeInvisibleTypeAnnotations", str4);
        }
        if (!$assertionsDisabled && !expandCaseDashNotation("1-5").equals("1,2,3,4,5")) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !expandCaseDashNotation("-2--1").equals("-2,-1")) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !expandCaseDashNotation("-2-1").equals("-2,-1,0,1")) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !expandCaseDashNotation("-1-0").equals("-1,0")) {
            throw new AssertionError();
        }
    }

    public String name() {
        return this.def.name();
    }

    public Layout layout() {
        return this.def;
    }

    public byte[] bytes() {
        return this.bytes;
    }

    public int size() {
        return this.bytes.length;
    }

    public ConstantPool.Entry getNameRef() {
        return this.def.getNameRef();
    }

    private Attribute(Attribute attribute) {
        this.def = attribute.def;
        this.bytes = attribute.bytes;
        this.fixups = attribute.fixups;
    }

    public Attribute(Layout layout, byte[] bArr, Object obj) {
        this.def = layout;
        this.bytes = bArr;
        this.fixups = obj;
        Fixups.setBytes(obj, bArr);
    }

    public Attribute(Layout layout, byte[] bArr) {
        this(layout, bArr, null);
    }

    public Attribute addContent(byte[] bArr, Object obj) {
        if (!$assertionsDisabled && !isCanonical()) {
            throw new AssertionError();
        }
        if (bArr.length == 0 && obj == null) {
            return this;
        }
        Attribute attribute = new Attribute(this);
        attribute.bytes = bArr;
        attribute.fixups = obj;
        Fixups.setBytes(obj, bArr);
        return attribute;
    }

    public Attribute addContent(byte[] bArr) {
        return addContent(bArr, null);
    }

    public void finishRefs(ConstantPool.Index index) {
        if (this.fixups != null) {
            Fixups.finishRefs(this.fixups, this.bytes, index);
            this.fixups = null;
        }
    }

    public boolean isCanonical() {
        return this == this.def.canon;
    }

    @Override // java.lang.Comparable
    public int compareTo(Attribute attribute) {
        return this.def.compareTo(attribute.def);
    }

    public static List<Attribute> getCanonList(List<Attribute> list) {
        List<Attribute> list2;
        synchronized (canonLists) {
            List<Attribute> listUnmodifiableList = canonLists.get(list);
            if (listUnmodifiableList == null) {
                ArrayList arrayList = new ArrayList(list.size());
                arrayList.addAll(list);
                listUnmodifiableList = Collections.unmodifiableList(arrayList);
                canonLists.put(list, listUnmodifiableList);
            }
            list2 = listUnmodifiableList;
        }
        return list2;
    }

    public static Attribute find(int i2, String str, String str2) {
        Attribute attribute;
        Layout layoutMakeKey = Layout.makeKey(i2, str, str2);
        synchronized (attributes) {
            Attribute attributeCanonicalInstance = attributes.get(layoutMakeKey);
            if (attributeCanonicalInstance == null) {
                attributeCanonicalInstance = new Layout(i2, str, str2).canonicalInstance();
                attributes.put(layoutMakeKey, attributeCanonicalInstance);
            }
            attribute = attributeCanonicalInstance;
        }
        return attribute;
    }

    public static Layout keyForLookup(int i2, String str) {
        return Layout.makeKey(i2, str);
    }

    public static Attribute lookup(Map<Layout, Attribute> map, int i2, String str) {
        if (map == null) {
            map = standardDefs;
        }
        return map.get(Layout.makeKey(i2, str));
    }

    public static Attribute define(Map<Layout, Attribute> map, int i2, String str, String str2) {
        Attribute attributeFind = find(i2, str, str2);
        map.put(Layout.makeKey(i2, str), attributeFind);
        return attributeFind;
    }

    public static String contextName(int i2) {
        switch (i2) {
            case 0:
                return com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_CLASS;
            case 1:
                return "field";
            case 2:
                return "method";
            case 3:
                return "code";
            default:
                return null;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Attribute$Holder.class */
    public static abstract class Holder {
        protected int flags;
        protected List<Attribute> attributes;
        static final List<Attribute> noAttributes;
        static final /* synthetic */ boolean $assertionsDisabled;

        protected abstract ConstantPool.Entry[] getCPMap();

        static {
            $assertionsDisabled = !Attribute.class.desiredAssertionStatus();
            noAttributes = Arrays.asList(new Attribute[0]);
        }

        public int attributeSize() {
            if (this.attributes == null) {
                return 0;
            }
            return this.attributes.size();
        }

        public void trimToSize() {
            if (this.attributes == null) {
                return;
            }
            if (this.attributes.isEmpty()) {
                this.attributes = null;
                return;
            }
            if (this.attributes instanceof ArrayList) {
                ArrayList arrayList = (ArrayList) this.attributes;
                arrayList.trimToSize();
                boolean z2 = true;
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Attribute attribute = (Attribute) it.next();
                    if (!attribute.isCanonical()) {
                        z2 = false;
                    }
                    if (attribute.fixups != null) {
                        if (!$assertionsDisabled && attribute.isCanonical()) {
                            throw new AssertionError();
                        }
                        attribute.fixups = Fixups.trimToSize(attribute.fixups);
                    }
                }
                if (z2) {
                    this.attributes = Attribute.getCanonList(arrayList);
                }
            }
        }

        public void addAttribute(Attribute attribute) {
            if (this.attributes == null) {
                this.attributes = new ArrayList(3);
            } else if (!(this.attributes instanceof ArrayList)) {
                this.attributes = new ArrayList(this.attributes);
            }
            this.attributes.add(attribute);
        }

        public Attribute removeAttribute(Attribute attribute) {
            if (this.attributes == null || !this.attributes.contains(attribute)) {
                return null;
            }
            if (!(this.attributes instanceof ArrayList)) {
                this.attributes = new ArrayList(this.attributes);
            }
            this.attributes.remove(attribute);
            return attribute;
        }

        public Attribute getAttribute(int i2) {
            return this.attributes.get(i2);
        }

        protected void visitRefs(int i2, Collection<ConstantPool.Entry> collection) {
            if (this.attributes == null) {
                return;
            }
            Iterator<Attribute> it = this.attributes.iterator();
            while (it.hasNext()) {
                it.next().visitRefs(this, i2, collection);
            }
        }

        public List<Attribute> getAttributes() {
            if (this.attributes == null) {
                return noAttributes;
            }
            return this.attributes;
        }

        public void setAttributes(List<Attribute> list) {
            if (list.isEmpty()) {
                this.attributes = null;
            } else {
                this.attributes = list;
            }
        }

        public Attribute getAttribute(String str) {
            if (this.attributes == null) {
                return null;
            }
            for (Attribute attribute : this.attributes) {
                if (attribute.name().equals(str)) {
                    return attribute;
                }
            }
            return null;
        }

        public Attribute getAttribute(Layout layout) {
            if (this.attributes == null) {
                return null;
            }
            for (Attribute attribute : this.attributes) {
                if (attribute.layout() == layout) {
                    return attribute;
                }
            }
            return null;
        }

        public Attribute removeAttribute(String str) {
            return removeAttribute(getAttribute(str));
        }

        public Attribute removeAttribute(Layout layout) {
            return removeAttribute(getAttribute(layout));
        }

        public void strip(String str) {
            removeAttribute(getAttribute(str));
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Attribute$ValueStream.class */
    public static abstract class ValueStream {
        public int getInt(int i2) {
            throw undef();
        }

        public void putInt(int i2, int i3) {
            throw undef();
        }

        public ConstantPool.Entry getRef(int i2) {
            throw undef();
        }

        public void putRef(int i2, ConstantPool.Entry entry) {
            throw undef();
        }

        public int decodeBCI(int i2) {
            throw undef();
        }

        public int encodeBCI(int i2) {
            throw undef();
        }

        public void noteBackCall(int i2) {
        }

        private RuntimeException undef() {
            return new UnsupportedOperationException("ValueStream method");
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Attribute$Layout.class */
    public static class Layout implements Comparable<Layout> {
        int ctype;
        String name;
        boolean hasRefs;
        String layout;
        int bandCount;
        Element[] elems;
        Attribute canon;
        private static final Element[] noElems;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Attribute.class.desiredAssertionStatus();
            noElems = new Element[0];
        }

        public int ctype() {
            return this.ctype;
        }

        public String name() {
            return this.name;
        }

        public String layout() {
            return this.layout;
        }

        public Attribute canonicalInstance() {
            return this.canon;
        }

        public ConstantPool.Entry getNameRef() {
            return ConstantPool.getUtf8Entry(name());
        }

        public boolean isEmpty() {
            return this.layout.isEmpty();
        }

        public Layout(int i2, String str, String str2) {
            this.ctype = i2;
            this.name = str.intern();
            this.layout = str2.intern();
            if (!$assertionsDisabled && i2 >= 4) {
                throw new AssertionError();
            }
            try {
                if (!str2.startsWith("[")) {
                    this.elems = Attribute.tokenizeLayout(this, -1, str2);
                } else {
                    String[] strArrSplitBodies = Attribute.splitBodies(str2);
                    Element[] elementArr = new Element[strArrSplitBodies.length];
                    this.elems = elementArr;
                    for (int i3 = 0; i3 < elementArr.length; i3++) {
                        Element element = new Element();
                        element.kind = (byte) 10;
                        element.removeBand();
                        element.bandIndex = -1;
                        element.layout = strArrSplitBodies[i3];
                        elementArr[i3] = element;
                    }
                    for (int i4 = 0; i4 < elementArr.length; i4++) {
                        elementArr[i4].body = Attribute.tokenizeLayout(this, i4, strArrSplitBodies[i4]);
                    }
                }
                this.canon = new Attribute(this, Constants.noBytes);
            } catch (StringIndexOutOfBoundsException e2) {
                throw new RuntimeException("Bad attribute layout: " + str2, e2);
            }
        }

        private Layout() {
        }

        static Layout makeKey(int i2, String str, String str2) {
            Layout layout = new Layout();
            layout.ctype = i2;
            layout.name = str.intern();
            layout.layout = str2.intern();
            if ($assertionsDisabled || i2 < 4) {
                return layout;
            }
            throw new AssertionError();
        }

        static Layout makeKey(int i2, String str) {
            return makeKey(i2, str, "");
        }

        public Attribute addContent(byte[] bArr, Object obj) {
            return this.canon.addContent(bArr, obj);
        }

        public Attribute addContent(byte[] bArr) {
            return this.canon.addContent(bArr, null);
        }

        public boolean equals(Object obj) {
            return obj != null && obj.getClass() == Layout.class && equals((Layout) obj);
        }

        public boolean equals(Layout layout) {
            return this.name.equals(layout.name) && this.layout.equals(layout.layout) && this.ctype == layout.ctype;
        }

        public int hashCode() {
            return ((((17 + this.name.hashCode()) * 37) + this.layout.hashCode()) * 37) + this.ctype;
        }

        @Override // java.lang.Comparable
        public int compareTo(Layout layout) {
            int iCompareTo = this.name.compareTo(layout.name);
            if (iCompareTo != 0) {
                return iCompareTo;
            }
            int iCompareTo2 = this.layout.compareTo(layout.layout);
            return iCompareTo2 != 0 ? iCompareTo2 : this.ctype - layout.ctype;
        }

        public String toString() {
            String str = Attribute.contextName(this.ctype) + "." + this.name + "[" + this.layout + "]";
            if (!$assertionsDisabled) {
                String strStringForDebug = stringForDebug();
                str = strStringForDebug;
                if (strStringForDebug == null) {
                    throw new AssertionError();
                }
            }
            return str;
        }

        private String stringForDebug() {
            return Attribute.contextName(this.ctype) + "." + this.name + ((Object) Arrays.asList(this.elems));
        }

        /* loaded from: rt.jar:com/sun/java/util/jar/pack/Attribute$Layout$Element.class */
        public class Element {
            String layout;
            byte flags;
            byte kind;
            byte len;
            byte refKind;
            int bandIndex;
            int value;
            Element[] body;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !Attribute.class.desiredAssertionStatus();
            }

            boolean flagTest(byte b2) {
                return (this.flags & b2) != 0;
            }

            Element() {
                int i2 = Layout.this.bandCount;
                Layout.this.bandCount = i2 + 1;
                this.bandIndex = i2;
            }

            void removeBand() {
                Layout.this.bandCount--;
                if (!$assertionsDisabled && this.bandIndex != Layout.this.bandCount) {
                    throw new AssertionError();
                }
                this.bandIndex = -1;
            }

            public boolean hasBand() {
                return this.bandIndex >= 0;
            }

            public String toString() {
                String str = this.layout;
                if (!$assertionsDisabled) {
                    String strStringForDebug = stringForDebug();
                    str = strStringForDebug;
                    if (strStringForDebug == null) {
                        throw new AssertionError();
                    }
                }
                return str;
            }

            private String stringForDebug() {
                Element[] elementArr = this.body;
                switch (this.kind) {
                    case 8:
                        if (flagTest((byte) 8)) {
                            elementArr = null;
                            break;
                        }
                        break;
                    case 9:
                        elementArr = null;
                        break;
                }
                return this.layout + (!hasBand() ? "" : FXMLLoader.CONTROLLER_METHOD_PREFIX + this.bandIndex) + "<" + (this.flags == 0 ? "" : "" + ((int) this.flags)) + ((int) this.kind) + ((int) this.len) + (this.refKind == 0 ? "" : "" + ((int) this.refKind)) + ">" + (this.value == 0 ? "" : "(" + this.value + ")") + (elementArr == null ? "" : "" + ((Object) Arrays.asList(elementArr)));
            }
        }

        public boolean hasCallables() {
            return this.elems.length > 0 && this.elems[0].kind == 10;
        }

        public Element[] getCallables() {
            if (hasCallables()) {
                return (Element[]) Arrays.copyOf(this.elems, this.elems.length);
            }
            return noElems;
        }

        public Element[] getEntryPoint() {
            if (hasCallables()) {
                return this.elems[0].body;
            }
            return (Element[]) Arrays.copyOf(this.elems, this.elems.length);
        }

        public void parse(Holder holder, byte[] bArr, int i2, int i3, ValueStream valueStream) {
            int using = Attribute.parseUsing(getEntryPoint(), holder, bArr, i2, i3, valueStream);
            if (using != i2 + i3) {
                throw new InternalError("layout parsed " + (using - i2) + " out of " + i3 + " bytes");
            }
        }

        public Object unparse(ValueStream valueStream, ByteArrayOutputStream byteArrayOutputStream) {
            Object[] objArr = {null};
            Attribute.unparseUsing(getEntryPoint(), objArr, valueStream, byteArrayOutputStream);
            return objArr[0];
        }

        public String layoutForClassVersion(Package.Version version) {
            if (version.lessThan(Constants.JAVA6_MAX_CLASS_VERSION)) {
                return Attribute.expandCaseDashNotation(this.layout);
            }
            return this.layout;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Attribute$FormatException.class */
    public static class FormatException extends IOException {
        private static final long serialVersionUID = -2542243830788066513L;
        private int ctype;
        private String name;
        String layout;

        public FormatException(String str, int i2, String str2, String str3) {
            super(Constants.ATTR_CONTEXT_NAME[i2] + " attribute \"" + str2 + PdfOps.DOUBLE_QUOTE__TOKEN + (str == null ? "" : ": " + str));
            this.ctype = i2;
            this.name = str2;
            this.layout = str3;
        }

        public FormatException(String str, int i2, String str2) {
            this(str, i2, str2, null);
        }
    }

    void visitRefs(Holder holder, int i2, final Collection<ConstantPool.Entry> collection) {
        if (i2 == 0) {
            collection.add(getNameRef());
        }
        if (this.bytes.length != 0 && this.def.hasRefs) {
            if (this.fixups != null) {
                Fixups.visitRefs(this.fixups, collection);
            } else {
                this.def.parse(holder, this.bytes, 0, this.bytes.length, new ValueStream() { // from class: com.sun.java.util.jar.pack.Attribute.1
                    @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                    public void putInt(int i3, int i4) {
                    }

                    @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                    public void putRef(int i3, ConstantPool.Entry entry) {
                        collection.add(entry);
                    }

                    @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                    public int encodeBCI(int i3) {
                        return i3;
                    }
                });
            }
        }
    }

    public void parse(Holder holder, byte[] bArr, int i2, int i3, ValueStream valueStream) {
        this.def.parse(holder, bArr, i2, i3, valueStream);
    }

    public Object unparse(ValueStream valueStream, ByteArrayOutputStream byteArrayOutputStream) {
        return this.def.unparse(valueStream, byteArrayOutputStream);
    }

    public String toString() {
        return ((Object) this.def) + VectorFormat.DEFAULT_PREFIX + (this.bytes == null ? -1 : size()) + "}" + (this.fixups == null ? "" : this.fixups.toString());
    }

    public static String normalizeLayoutString(String str) {
        char cCharAt;
        StringBuilder sb = new StringBuilder();
        int iMin = 0;
        int length = str.length();
        while (iMin < length) {
            int i2 = iMin;
            iMin++;
            char cCharAt2 = str.charAt(i2);
            if (cCharAt2 > ' ') {
                if (cCharAt2 == '#') {
                    int iIndexOf = str.indexOf(10, iMin);
                    int iIndexOf2 = str.indexOf(13, iMin);
                    if (iIndexOf < 0) {
                        iIndexOf = length;
                    }
                    if (iIndexOf2 < 0) {
                        iIndexOf2 = length;
                    }
                    iMin = Math.min(iIndexOf, iIndexOf2);
                } else if (cCharAt2 == '\\') {
                    iMin++;
                    sb.append((int) str.charAt(iMin));
                } else if (cCharAt2 == '0' && str.startsWith("0x", iMin - 1)) {
                    int i3 = iMin - 1;
                    int i4 = i3 + 2;
                    while (i4 < length && (((cCharAt = str.charAt(i4)) >= '0' && cCharAt <= '9') || (cCharAt >= 'a' && cCharAt <= 'f'))) {
                        i4++;
                    }
                    if (i4 > i3) {
                        sb.append((Object) Integer.decode(str.substring(i3, i4)));
                        iMin = i4;
                    } else {
                        sb.append(cCharAt2);
                    }
                } else {
                    sb.append(cCharAt2);
                }
            }
        }
        return sb.toString();
    }

    static Layout.Element[] tokenizeLayout(Layout layout, int i2, String str) throws NumberFormatException {
        ArrayList arrayList = new ArrayList(str.length());
        tokenizeLayout(layout, i2, str, arrayList);
        Layout.Element[] elementArr = new Layout.Element[arrayList.size()];
        arrayList.toArray(elementArr);
        return elementArr;
    }

    static void tokenizeLayout(Layout layout, int i2, String str, List<Layout.Element> list) throws NumberFormatException {
        byte b2;
        int i3;
        int intAfter;
        int intBefore;
        boolean z2 = false;
        int length = str.length();
        int i4 = 0;
        while (i4 < length) {
            int i5 = i4;
            layout.getClass();
            Layout.Element element = layout.new Element();
            int i6 = i4;
            int i7 = i4 + 1;
            switch (str.charAt(i6)) {
                case '(':
                    b2 = 9;
                    element.removeBand();
                    int iIndexOf = str.indexOf(41, i7);
                    i4 = iIndexOf + 1;
                    String strSubstring = str.substring(i5 + 1, iIndexOf);
                    int i8 = Integer.parseInt(strSubstring);
                    int i9 = i2 + i8;
                    if (!(i8 + "").equals(strSubstring) || layout.elems == null || i9 < 0 || i9 >= layout.elems.length) {
                        i4 = -i4;
                    } else {
                        Layout.Element element2 = layout.elems[i9];
                        if (!$assertionsDisabled && element2.kind != 10) {
                            throw new AssertionError();
                        }
                        element.value = i9;
                        element.body = new Layout.Element[]{element2};
                        if (i8 <= 0) {
                            element.flags = (byte) (element.flags | 8);
                            element2.flags = (byte) (element2.flags | 8);
                            break;
                        }
                    }
                    break;
                case ')':
                case '*':
                case '+':
                case ',':
                case '-':
                case '.':
                case '/':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case ':':
                case ';':
                case '<':
                case '=':
                case '>':
                case '?':
                case '@':
                case 'A':
                case 'C':
                case 'D':
                case 'E':
                case 'G':
                case 'J':
                case 'L':
                case 'M':
                case 'Q':
                case 'U':
                default:
                    i4 = -i7;
                    continue;
                case 'B':
                case 'H':
                case 'I':
                case 'V':
                    b2 = 1;
                    i4 = tokenizeUInt(element, str, i7 - 1);
                    break;
                case 'F':
                    b2 = 4;
                    i4 = tokenizeUInt(element, str, i7);
                    break;
                case 'K':
                    b2 = 6;
                    i4 = i7 + 1;
                    switch (str.charAt(i7)) {
                        case 'D':
                            element.refKind = (byte) 6;
                            break;
                        case 'E':
                        case 'G':
                        case 'H':
                        case 'K':
                        case 'N':
                        case 'O':
                        case 'P':
                        case 'R':
                        default:
                            i4 = -i4;
                            continue;
                        case 'F':
                            element.refKind = (byte) 4;
                            break;
                        case 'I':
                            element.refKind = (byte) 3;
                            break;
                        case 'J':
                            element.refKind = (byte) 5;
                            break;
                        case 'L':
                            element.refKind = (byte) 51;
                            break;
                        case 'M':
                            element.refKind = (byte) 15;
                            break;
                        case 'Q':
                            element.refKind = (byte) 53;
                            break;
                        case 'S':
                            element.refKind = (byte) 8;
                            break;
                        case 'T':
                            element.refKind = (byte) 16;
                            break;
                    }
                case 'N':
                    b2 = 5;
                    int i10 = tokenizeUInt(element, str, i7);
                    int i11 = i10 + 1;
                    if (str.charAt(i10) != '[') {
                        i4 = -i11;
                    } else {
                        int iSkipBody = skipBody(str, i11);
                        i4 = iSkipBody + 1;
                        element.body = tokenizeLayout(layout, i2, str.substring(i11, iSkipBody));
                        break;
                    }
                case 'O':
                    b2 = 3;
                    element.flags = (byte) (element.flags | 2);
                    if (!z2) {
                        i4 = -i7;
                    } else {
                        i4 = tokenizeSInt(element, str, i7);
                        break;
                    }
                case 'P':
                    b2 = 2;
                    int i12 = i7 + 1;
                    if (str.charAt(i7) == 'O') {
                        element.flags = (byte) (element.flags | 2);
                        if (!z2) {
                            i4 = -i12;
                        } else {
                            i12++;
                        }
                    }
                    i4 = tokenizeUInt(element, str, i12 - 1);
                    break;
                case 'R':
                    b2 = 6;
                    i4 = i7 + 1;
                    switch (str.charAt(i7)) {
                        case 'B':
                            element.refKind = (byte) 17;
                            break;
                        case 'C':
                            element.refKind = (byte) 7;
                            break;
                        case 'D':
                            element.refKind = (byte) 12;
                            break;
                        case 'E':
                        case 'G':
                        case 'H':
                        case 'J':
                        case 'K':
                        case 'L':
                        case 'O':
                        case 'P':
                        case 'R':
                        case 'T':
                        case 'V':
                        case 'W':
                        case 'X':
                        default:
                            i4 = -i4;
                            continue;
                        case 'F':
                            element.refKind = (byte) 9;
                            break;
                        case 'I':
                            element.refKind = (byte) 11;
                            break;
                        case 'M':
                            element.refKind = (byte) 10;
                            break;
                        case 'N':
                            element.refKind = (byte) 52;
                            break;
                        case 'Q':
                            element.refKind = (byte) 50;
                            break;
                        case 'S':
                            element.refKind = (byte) 13;
                            break;
                        case 'U':
                            element.refKind = (byte) 1;
                            break;
                        case 'Y':
                            element.refKind = (byte) 18;
                            break;
                    }
                case 'S':
                    b2 = 1;
                    i4 = tokenizeSInt(element, str, i7 - 1);
                    break;
                case 'T':
                    b2 = 7;
                    i4 = tokenizeSInt(element, str, i7);
                    ArrayList arrayList = new ArrayList();
                    while (true) {
                        int i13 = i4;
                        int i14 = i4 + 1;
                        if (str.charAt(i13) != '(') {
                            i4 = -i14;
                        } else {
                            int iIndexOf2 = str.indexOf(41, i14);
                            int i15 = iIndexOf2 + 1;
                            String strSubstring2 = str.substring(i14, iIndexOf2);
                            int length2 = strSubstring2.length();
                            int iSkipBody2 = i15 + 1;
                            if (str.charAt(i15) != '[') {
                                i4 = -iSkipBody2;
                            } else {
                                if (str.charAt(iSkipBody2) == ']') {
                                    i3 = iSkipBody2;
                                } else {
                                    i3 = iSkipBody2;
                                    iSkipBody2 = skipBody(str, iSkipBody2);
                                }
                                int i16 = iSkipBody2;
                                i4 = iSkipBody2 + 1;
                                Layout.Element[] elementArr = tokenizeLayout(layout, i2, str.substring(i3, i16));
                                if (length2 == 0) {
                                    layout.getClass();
                                    Layout.Element element3 = layout.new Element();
                                    element3.body = elementArr;
                                    element3.kind = (byte) 8;
                                    element3.removeBand();
                                    arrayList.add(element3);
                                } else {
                                    boolean z3 = true;
                                    int i17 = 0;
                                    while (true) {
                                        int i18 = i17;
                                        int iIndexOf3 = strSubstring2.indexOf(44, i18);
                                        if (iIndexOf3 < 0) {
                                            iIndexOf3 = length2;
                                        }
                                        String strSubstring3 = strSubstring2.substring(i18, iIndexOf3);
                                        if (strSubstring3.length() == 0) {
                                            strSubstring3 = com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_EMPTY_STRING;
                                        }
                                        int iFindCaseDash = findCaseDash(strSubstring3, 0);
                                        if (iFindCaseDash >= 0) {
                                            intBefore = parseIntBefore(strSubstring3, iFindCaseDash);
                                            intAfter = parseIntAfter(strSubstring3, iFindCaseDash);
                                            if (intBefore >= intAfter) {
                                                i4 = -i4;
                                            }
                                        } else {
                                            int i19 = Integer.parseInt(strSubstring3);
                                            intAfter = i19;
                                            intBefore = i19;
                                        }
                                        while (true) {
                                            layout.getClass();
                                            Layout.Element element4 = layout.new Element();
                                            element4.body = elementArr;
                                            element4.kind = (byte) 8;
                                            element4.removeBand();
                                            if (!z3) {
                                                element4.flags = (byte) (element4.flags | 8);
                                            }
                                            z3 = false;
                                            element4.value = intBefore;
                                            arrayList.add(element4);
                                            if (intBefore != intAfter) {
                                                intBefore++;
                                            } else if (iIndexOf3 == length2) {
                                                break;
                                            } else {
                                                i17 = iIndexOf3 + 1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    element.body = new Layout.Element[arrayList.size()];
                    arrayList.toArray(element.body);
                    element.kind = (byte) 7;
                    int i20 = 0;
                    while (true) {
                        if (i20 >= element.body.length - 1) {
                            break;
                        } else {
                            Layout.Element element5 = element.body[i20];
                            if (matchCase(element, element5.value) == element5) {
                                i20++;
                            } else {
                                i4 = -i4;
                                break;
                            }
                        }
                    }
                    break;
            }
            if (b2 == 6) {
                int i21 = i4;
                int i22 = i4 + 1;
                if (str.charAt(i21) == 'N') {
                    element.flags = (byte) (element.flags | 4);
                    i22++;
                }
                i4 = tokenizeUInt(element, str, i22 - 1);
                layout.hasRefs = true;
            }
            z2 = b2 == 2;
            element.kind = b2;
            element.layout = str.substring(i5, i4);
            list.add(element);
        }
    }

    static String[] splitBodies(String str) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i2;
            int i4 = i2 + 1;
            if (str.charAt(i3) != '[') {
                str.charAt(-i4);
            }
            int iSkipBody = skipBody(str, i4);
            arrayList.add(str.substring(i4, iSkipBody));
            i2 = iSkipBody + 1;
        }
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        return strArr;
    }

    private static int skipBody(String str, int i2) {
        if (!$assertionsDisabled && str.charAt(i2 - 1) != '[') {
            throw new AssertionError();
        }
        if (str.charAt(i2) == ']') {
            return -i2;
        }
        int i3 = 1;
        while (i3 > 0) {
            int i4 = i2;
            i2++;
            switch (str.charAt(i4)) {
                case '[':
                    i3++;
                    break;
                case ']':
                    i3--;
                    break;
            }
        }
        int i5 = i2 - 1;
        if ($assertionsDisabled || str.charAt(i5) == ']') {
            return i5;
        }
        throw new AssertionError();
    }

    private static int tokenizeUInt(Layout.Element element, String str, int i2) {
        int i3 = i2 + 1;
        switch (str.charAt(i2)) {
            case 'B':
                element.len = (byte) 1;
                break;
            case 'H':
                element.len = (byte) 2;
                break;
            case 'I':
                element.len = (byte) 4;
                break;
            case 'V':
                element.len = (byte) 0;
                break;
            default:
                return -i3;
        }
        return i3;
    }

    private static int tokenizeSInt(Layout.Element element, String str, int i2) {
        if (str.charAt(i2) == 'S') {
            element.flags = (byte) (element.flags | 1);
            i2++;
        }
        return tokenizeUInt(element, str, i2);
    }

    private static boolean isDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    static int findCaseDash(String str, int i2) {
        if (i2 <= 0) {
            i2 = 1;
        }
        int length = str.length() - 2;
        while (true) {
            int iIndexOf = str.indexOf(45, i2);
            if (iIndexOf < 0 || iIndexOf > length) {
                return -1;
            }
            if (isDigit(str.charAt(iIndexOf - 1))) {
                char cCharAt = str.charAt(iIndexOf + 1);
                if (cCharAt == '-' && iIndexOf + 2 < str.length()) {
                    cCharAt = str.charAt(iIndexOf + 2);
                }
                if (isDigit(cCharAt)) {
                    return iIndexOf;
                }
            }
            i2 = iIndexOf + 1;
        }
    }

    static int parseIntBefore(String str, int i2) {
        int i3 = i2;
        while (i3 > 0 && isDigit(str.charAt(i3 - 1))) {
            i3--;
        }
        if (i3 == i2) {
            return Integer.parseInt(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_EMPTY_STRING);
        }
        if (i3 >= 1 && str.charAt(i3 - 1) == '-') {
            i3--;
        }
        if ($assertionsDisabled || i3 == 0 || !isDigit(str.charAt(i3 - 1))) {
            return Integer.parseInt(str.substring(i3, i2));
        }
        throw new AssertionError();
    }

    static int parseIntAfter(String str, int i2) {
        int i3 = i2 + 1;
        int i4 = i3;
        int length = str.length();
        if (i4 < length && str.charAt(i4) == '-') {
            i4++;
        }
        while (i4 < length && isDigit(str.charAt(i4))) {
            i4++;
        }
        return i3 == i4 ? Integer.parseInt(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_EMPTY_STRING) : Integer.parseInt(str.substring(i3, i4));
    }

    static String expandCaseDashNotation(String str) {
        int iFindCaseDash = findCaseDash(str, 0);
        if (iFindCaseDash < 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length() * 3);
        int i2 = 0;
        do {
            sb.append(str.substring(i2, iFindCaseDash));
            i2 = iFindCaseDash + 1;
            int intBefore = parseIntBefore(str, iFindCaseDash);
            int intAfter = parseIntAfter(str, iFindCaseDash);
            if (!$assertionsDisabled && intBefore >= intAfter) {
                throw new AssertionError();
            }
            sb.append(",");
            for (int i3 = intBefore + 1; i3 < intAfter; i3++) {
                sb.append(i3);
                sb.append(",");
            }
            iFindCaseDash = findCaseDash(str, i2);
        } while (iFindCaseDash >= 0);
        sb.append(str.substring(i2));
        return sb.toString();
    }

    static int parseUsing(Layout.Element[] elementArr, Holder holder, byte[] bArr, int i2, int i3, ValueStream valueStream) {
        ConstantPool.Entry signatureEntry;
        int i4;
        int i5 = 0;
        int i6 = 0;
        int i7 = i2 + i3;
        int[] iArr = {0};
        for (Layout.Element element : elementArr) {
            int i8 = element.bandIndex;
            switch (element.kind) {
                case 1:
                    i2 = parseInt(element, bArr, i2, iArr);
                    valueStream.putInt(i8, iArr[0]);
                    break;
                case 2:
                    i2 = parseInt(element, bArr, i2, iArr);
                    int i9 = iArr[0];
                    int iEncodeBCI = valueStream.encodeBCI(i9);
                    if (!element.flagTest((byte) 2)) {
                        i4 = iEncodeBCI;
                    } else {
                        i4 = iEncodeBCI - i6;
                    }
                    i5 = i9;
                    i6 = iEncodeBCI;
                    valueStream.putInt(i8, i4);
                    break;
                case 3:
                    if (!$assertionsDisabled && !element.flagTest((byte) 2)) {
                        throw new AssertionError();
                    }
                    i2 = parseInt(element, bArr, i2, iArr);
                    int i10 = i5 + iArr[0];
                    int iEncodeBCI2 = valueStream.encodeBCI(i10);
                    int i11 = iEncodeBCI2 - i6;
                    i5 = i10;
                    i6 = iEncodeBCI2;
                    valueStream.putInt(i8, i11);
                    break;
                case 4:
                    i2 = parseInt(element, bArr, i2, iArr);
                    valueStream.putInt(i8, iArr[0]);
                    break;
                case 5:
                    i2 = parseInt(element, bArr, i2, iArr);
                    int i12 = iArr[0];
                    valueStream.putInt(i8, i12);
                    for (int i13 = 0; i13 < i12; i13++) {
                        i2 = parseUsing(element.body, holder, bArr, i2, i7 - i2, valueStream);
                    }
                    break;
                case 6:
                    i2 = parseInt(element, bArr, i2, iArr);
                    int i14 = iArr[0];
                    if (i14 == 0) {
                        signatureEntry = null;
                    } else {
                        ConstantPool.Entry[] cPMap = holder.getCPMap();
                        signatureEntry = (i14 < 0 || i14 >= cPMap.length) ? null : cPMap[i14];
                        byte b2 = element.refKind;
                        if (signatureEntry != null && b2 == 13 && signatureEntry.getTag() == 1) {
                            signatureEntry = ConstantPool.getSignatureEntry(signatureEntry.stringValue());
                        }
                        String str = signatureEntry == null ? "invalid CP index" : "type=" + ConstantPool.tagName(signatureEntry.tag);
                        if (signatureEntry == null || !signatureEntry.tagMatches(b2)) {
                            throw new IllegalArgumentException("Bad constant, expected type=" + ConstantPool.tagName(b2) + " got " + str);
                        }
                    }
                    valueStream.putRef(i8, signatureEntry);
                    break;
                case 7:
                    int i15 = parseInt(element, bArr, i2, iArr);
                    int i16 = iArr[0];
                    valueStream.putInt(i8, i16);
                    i2 = parseUsing(matchCase(element, i16).body, holder, bArr, i15, i7 - i15, valueStream);
                    break;
                case 8:
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
                case 9:
                    if (!$assertionsDisabled && element.body.length != 1) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && element.body[0].kind != 10) {
                        throw new AssertionError();
                    }
                    if (element.flagTest((byte) 8)) {
                        valueStream.noteBackCall(element.value);
                    }
                    i2 = parseUsing(element.body[0].body, holder, bArr, i2, i7 - i2, valueStream);
                    break;
                    break;
            }
        }
        return i2;
    }

    static Layout.Element matchCase(Layout.Element element, int i2) {
        if (!$assertionsDisabled && element.kind != 7) {
            throw new AssertionError();
        }
        int length = element.body.length - 1;
        for (int i3 = 0; i3 < length; i3++) {
            Layout.Element element2 = element.body[i3];
            if (!$assertionsDisabled && element2.kind != 8) {
                throw new AssertionError();
            }
            if (i2 == element2.value) {
                return element2;
            }
        }
        return element.body[length];
    }

    private static int parseInt(Layout.Element element, byte[] bArr, int i2, int[] iArr) {
        int i3 = 0;
        int i4 = element.len * 8;
        int i5 = i4;
        while (true) {
            i5 -= 8;
            if (i5 < 0) {
                break;
            }
            int i6 = i2;
            i2++;
            i3 += (bArr[i6] & 255) << i5;
        }
        if (i4 < 32 && element.flagTest((byte) 1)) {
            int i7 = 32 - i4;
            i3 = (i3 << i7) >> i7;
        }
        iArr[0] = i3;
        return i2;
    }

    static void unparseUsing(Layout.Element[] elementArr, Object[] objArr, ValueStream valueStream, ByteArrayOutputStream byteArrayOutputStream) {
        int i2;
        int i3;
        int i4 = 0;
        int i5 = 0;
        for (Layout.Element element : elementArr) {
            int i6 = element.bandIndex;
            switch (element.kind) {
                case 1:
                    unparseInt(element, valueStream.getInt(i6), byteArrayOutputStream);
                    break;
                case 2:
                    int i7 = valueStream.getInt(i6);
                    if (!element.flagTest((byte) 2)) {
                        i3 = i7;
                    } else {
                        i3 = i5 + i7;
                    }
                    if (!$assertionsDisabled && i4 != valueStream.decodeBCI(i5)) {
                        throw new AssertionError();
                    }
                    int iDecodeBCI = valueStream.decodeBCI(i3);
                    unparseInt(element, iDecodeBCI, byteArrayOutputStream);
                    i4 = iDecodeBCI;
                    i5 = i3;
                    break;
                case 3:
                    int i8 = valueStream.getInt(i6);
                    if (!$assertionsDisabled && !element.flagTest((byte) 2)) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && i4 != valueStream.decodeBCI(i5)) {
                        throw new AssertionError();
                    }
                    int i9 = i5 + i8;
                    int iDecodeBCI2 = valueStream.decodeBCI(i9);
                    unparseInt(element, iDecodeBCI2 - i4, byteArrayOutputStream);
                    i4 = iDecodeBCI2;
                    i5 = i9;
                    break;
                case 4:
                    unparseInt(element, valueStream.getInt(i6), byteArrayOutputStream);
                    break;
                case 5:
                    int i10 = valueStream.getInt(i6);
                    unparseInt(element, i10, byteArrayOutputStream);
                    for (int i11 = 0; i11 < i10; i11++) {
                        unparseUsing(element.body, objArr, valueStream, byteArrayOutputStream);
                    }
                    break;
                case 6:
                    ConstantPool.Entry ref = valueStream.getRef(i6);
                    if (ref != null) {
                        objArr[0] = Fixups.addRefWithLoc(objArr[0], byteArrayOutputStream.size(), ref);
                        i2 = 0;
                    } else {
                        i2 = 0;
                    }
                    unparseInt(element, i2, byteArrayOutputStream);
                    break;
                case 7:
                    int i12 = valueStream.getInt(i6);
                    unparseInt(element, i12, byteArrayOutputStream);
                    unparseUsing(matchCase(element, i12).body, objArr, valueStream, byteArrayOutputStream);
                    break;
                case 8:
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
                case 9:
                    if (!$assertionsDisabled && element.body.length != 1) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && element.body[0].kind != 10) {
                        throw new AssertionError();
                    }
                    unparseUsing(element.body[0].body, objArr, valueStream, byteArrayOutputStream);
                    break;
                    break;
            }
        }
    }

    private static void unparseInt(Layout.Element element, int i2, ByteArrayOutputStream byteArrayOutputStream) {
        int i3;
        int i4 = element.len * 8;
        if (i4 == 0) {
            return;
        }
        if (i4 < 32) {
            int i5 = 32 - i4;
            if (element.flagTest((byte) 1)) {
                i3 = (i2 << i5) >> i5;
            } else {
                i3 = (i2 << i5) >>> i5;
            }
            if (i3 != i2) {
                throw new InternalError("cannot code in " + ((int) element.len) + " bytes: " + i2);
            }
        }
        int i6 = i4;
        while (true) {
            i6 -= 8;
            if (i6 >= 0) {
                byteArrayOutputStream.write((byte) (i2 >>> i6));
            } else {
                return;
            }
        }
    }
}
