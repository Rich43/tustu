package jdk.jfr.internal;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import jdk.jfr.EventType;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: jfr.jar:jdk/jfr/internal/MetadataDescriptor.class */
public final class MetadataDescriptor {
    static final String ATTRIBUTE_ID = "id";
    static final String ATTRIBUTE_SIMPLE_TYPE = "simpleType";
    static final String ATTRIBUTE_GMT_OFFSET = "gmtOffset";
    static final String ATTRIBUTE_LOCALE = "locale";
    static final String ELEMENT_TYPE = "class";
    static final String ELEMENT_SETTING = "setting";
    static final String ELEMENT_ANNOTATION = "annotation";
    static final String ELEMENT_FIELD = "field";
    static final String ATTRIBUTE_SUPER_TYPE = "superType";
    static final String ATTRIBUTE_TYPE_ID = "class";
    static final String ATTRIBUTE_DIMENSION = "dimension";
    static final String ATTRIBUTE_NAME = "name";
    static final String ATTRIBUTE_CONSTANT_POOL = "constantPool";
    static final String ATTRIBUTE_DEFAULT_VALUE = "defaultValue";
    final List<EventType> eventTypes = new ArrayList();
    final Collection<Type> types = new ArrayList();
    long gmtOffset;
    String locale;
    Element root;

    /* loaded from: jfr.jar:jdk/jfr/internal/MetadataDescriptor$Attribute.class */
    static final class Attribute {
        final String name;
        final String value;

        private Attribute(String str, String str2) {
            this.name = str;
            this.value = str2;
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/MetadataDescriptor$Element.class */
    static final class Element {
        final String name;
        final List<Element> elements = new ArrayList();
        final List<Attribute> attributes = new ArrayList();

        Element(String str) {
            this.name = str;
        }

        long longValue(String str) {
            String strAttribute = attribute(str);
            if (strAttribute != null) {
                return Long.parseLong(strAttribute);
            }
            throw new IllegalArgumentException(str);
        }

        String attribute(String str) {
            for (Attribute attribute : this.attributes) {
                if (attribute.name.equals(str)) {
                    return attribute.value;
                }
            }
            return null;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            try {
                MetadataDescriptor.prettyPrintXML(sb, "", this);
            } catch (IOException e2) {
            }
            return sb.toString();
        }

        long attribute(String str, long j2) {
            String strAttribute = attribute(str);
            if (strAttribute == null) {
                return j2;
            }
            return Long.parseLong(strAttribute);
        }

        String attribute(String str, String str2) {
            String strAttribute = attribute(str);
            if (strAttribute == null) {
                return str2;
            }
            return strAttribute;
        }

        List<Element> elements(String... strArr) {
            ArrayList arrayList = new ArrayList();
            for (String str : strArr) {
                for (Element element : this.elements) {
                    if (element.name.equals(str)) {
                        arrayList.add(element);
                    }
                }
            }
            return arrayList;
        }

        void add(Element element) {
            this.elements.add(element);
        }

        void addAttribute(String str, Object obj) {
            this.attributes.add(new Attribute(str, String.valueOf(obj)));
        }

        Element newChild(String str) {
            Element element = new Element(str);
            this.elements.add(element);
            return element;
        }

        public void addArrayAttribute(Element element, String str, Object obj) {
            String name;
            name = obj.getClass().getComponentType().getName();
            switch (name) {
                case "int":
                    int[] iArr = (int[]) obj;
                    for (int i2 = 0; i2 < iArr.length; i2++) {
                        addAttribute(str + LanguageTag.SEP + i2, Integer.valueOf(iArr[i2]));
                    }
                    return;
                case "long":
                    long[] jArr = (long[]) obj;
                    for (int i3 = 0; i3 < jArr.length; i3++) {
                        addAttribute(str + LanguageTag.SEP + i3, Long.valueOf(jArr[i3]));
                    }
                    return;
                case "float":
                    float[] fArr = (float[]) obj;
                    for (int i4 = 0; i4 < fArr.length; i4++) {
                        addAttribute(str + LanguageTag.SEP + i4, Float.valueOf(fArr[i4]));
                    }
                    return;
                case "double":
                    double[] dArr = (double[]) obj;
                    for (int i5 = 0; i5 < dArr.length; i5++) {
                        addAttribute(str + LanguageTag.SEP + i5, Double.valueOf(dArr[i5]));
                    }
                    return;
                case "short":
                    short[] sArr = (short[]) obj;
                    for (int i6 = 0; i6 < sArr.length; i6++) {
                        addAttribute(str + LanguageTag.SEP + i6, Short.valueOf(sArr[i6]));
                    }
                    return;
                case "char":
                    char[] cArr = (char[]) obj;
                    for (int i7 = 0; i7 < cArr.length; i7++) {
                        addAttribute(str + LanguageTag.SEP + i7, Character.valueOf(cArr[i7]));
                    }
                    return;
                case "byte":
                    byte[] bArr = (byte[]) obj;
                    for (int i8 = 0; i8 < bArr.length; i8++) {
                        addAttribute(str + LanguageTag.SEP + i8, Byte.valueOf(bArr[i8]));
                    }
                    return;
                case "boolean":
                    boolean[] zArr = (boolean[]) obj;
                    for (int i9 = 0; i9 < zArr.length; i9++) {
                        addAttribute(str + LanguageTag.SEP + i9, Boolean.valueOf(zArr[i9]));
                    }
                    return;
                case "java.lang.String":
                    String[] strArr = (String[]) obj;
                    for (int i10 = 0; i10 < strArr.length; i10++) {
                        addAttribute(str + LanguageTag.SEP + i10, strArr[i10]);
                    }
                    return;
                default:
                    throw new InternalError("Array type of " + name + " is not supported");
            }
        }
    }

    MetadataDescriptor() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void prettyPrintXML(Appendable appendable, String str, Element element) throws IOException {
        appendable.append(str + "<" + element.name);
        for (Attribute attribute : element.attributes) {
            appendable.append(" ").append(attribute.name).append("=\"").append(attribute.value).append(PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (element.elements.size() == 0) {
            appendable.append("/");
        }
        appendable.append(">\n");
        Iterator<Element> it = element.elements.iterator();
        while (it.hasNext()) {
            prettyPrintXML(appendable, str + Constants.INDENT, it.next());
        }
        if (element.elements.size() != 0) {
            appendable.append(str).append("</").append(element.name).append(">\n");
        }
    }

    public Collection<Type> getTypes() {
        return this.types;
    }

    public List<EventType> getEventTypes() {
        return this.eventTypes;
    }

    public int getGMTOffset() {
        return (int) this.gmtOffset;
    }

    public String getLocale() {
        return this.locale;
    }

    public static MetadataDescriptor read(DataInput dataInput) throws IOException {
        return new MetadataReader(dataInput).getDescriptor();
    }

    static void write(List<Type> list, DataOutput dataOutput) throws IOException {
        MetadataDescriptor metadataDescriptor = new MetadataDescriptor();
        metadataDescriptor.locale = Locale.getDefault().toString();
        metadataDescriptor.gmtOffset = TimeZone.getDefault().getRawOffset();
        metadataDescriptor.types.addAll(list);
        new MetadataWriter(metadataDescriptor).writeBinary(dataOutput);
    }

    public String toString() {
        return this.root.toString();
    }
}
