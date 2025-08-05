package jdk.jfr.internal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jdk.jfr.AnnotationElement;
import jdk.jfr.SettingDescriptor;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.MetadataDescriptor;
import jdk.jfr.internal.consumer.RecordingInput;
import sun.util.locale.LanguageTag;

/* loaded from: jfr.jar:jdk/jfr/internal/MetadataReader.class */
final class MetadataReader {
    private final DataInput input;
    private final List<String> pool;
    private final MetadataDescriptor descriptor;
    private final Map<Long, Type> types = new HashMap();

    public MetadataReader(DataInput dataInput) throws IOException {
        this.input = dataInput;
        int i2 = dataInput.readInt();
        ((RecordingInput) dataInput).require(i2, "Metadata string pool size %d exceeds available data");
        this.pool = new ArrayList(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            this.pool.add(dataInput.readUTF());
        }
        this.descriptor = new MetadataDescriptor();
        MetadataDescriptor.Element elementCreateElement = createElement();
        MetadataDescriptor.Element element = elementCreateElement.elements("metadata").get(0);
        declareTypes(element);
        defineTypes(element);
        annotateTypes(element);
        buildEvenTypes();
        MetadataDescriptor.Element element2 = elementCreateElement.elements("region").get(0);
        this.descriptor.gmtOffset = element2.attribute("gmtOffset", 1L);
        this.descriptor.locale = element2.attribute("locale", "");
        this.descriptor.root = elementCreateElement;
        if (Logger.shouldLog(LogTag.JFR_SYSTEM_PARSER, LogLevel.TRACE)) {
            ArrayList arrayList = new ArrayList(this.types.values());
            Collections.sort(arrayList, (type, type2) -> {
                return type.getName().compareTo(type2.getName());
            });
            Iterator<E> it = arrayList.iterator();
            while (it.hasNext()) {
                ((Type) it.next()).log("Found", LogTag.JFR_SYSTEM_PARSER, LogLevel.TRACE);
            }
        }
    }

    private String readString() throws IOException {
        return this.pool.get(readInt());
    }

    private int readInt() throws IOException {
        return this.input.readInt();
    }

    private MetadataDescriptor.Element createElement() throws IOException {
        MetadataDescriptor.Element element = new MetadataDescriptor.Element(readString());
        int i2 = readInt();
        for (int i3 = 0; i3 < i2; i3++) {
            element.addAttribute(readString(), readString());
        }
        int i4 = readInt();
        for (int i5 = 0; i5 < i4; i5++) {
            element.add(createElement());
        }
        return element;
    }

    private void annotateTypes(MetadataDescriptor.Element element) throws IOException {
        for (MetadataDescriptor.Element element2 : element.elements(Constants.ATTRNAME_CLASS)) {
            Type type = getType("id", element2);
            ArrayList arrayList = new ArrayList();
            Iterator<MetadataDescriptor.Element> it = element2.elements("annotation").iterator();
            while (it.hasNext()) {
                arrayList.add(makeAnnotation(it.next()));
            }
            arrayList.trimToSize();
            type.setAnnotations(arrayList);
            int i2 = 0;
            if (type instanceof PlatformEventType) {
                List<SettingDescriptor> allSettings = ((PlatformEventType) type).getAllSettings();
                for (MetadataDescriptor.Element element3 : element2.elements("setting")) {
                    ArrayList arrayList2 = new ArrayList();
                    Iterator<MetadataDescriptor.Element> it2 = element3.elements("annotation").iterator();
                    while (it2.hasNext()) {
                        arrayList2.add(makeAnnotation(it2.next()));
                    }
                    arrayList2.trimToSize();
                    PrivateAccess.getInstance().setAnnotations(allSettings.get(i2), arrayList2);
                    i2++;
                }
            }
            int i3 = 0;
            List<ValueDescriptor> fields = type.getFields();
            for (MetadataDescriptor.Element element4 : element2.elements("field")) {
                ArrayList arrayList3 = new ArrayList();
                Iterator<MetadataDescriptor.Element> it3 = element4.elements("annotation").iterator();
                while (it3.hasNext()) {
                    arrayList3.add(makeAnnotation(it3.next()));
                }
                arrayList3.trimToSize();
                PrivateAccess.getInstance().setAnnotations(fields.get(i3), arrayList3);
                i3++;
            }
        }
    }

    private AnnotationElement makeAnnotation(MetadataDescriptor.Element element) throws IOException {
        Type type = getType(Constants.ATTRNAME_CLASS, element);
        ArrayList arrayList = new ArrayList();
        for (ValueDescriptor valueDescriptor : type.getFields()) {
            if (valueDescriptor.isArray()) {
                ArrayList arrayList2 = new ArrayList();
                int i2 = 0;
                while (true) {
                    String strAttribute = element.attribute(valueDescriptor.getName() + LanguageTag.SEP + i2);
                    if (strAttribute == null) {
                        break;
                    }
                    arrayList2.add(objectify(valueDescriptor.getTypeName(), strAttribute));
                    i2++;
                }
                Object objMakePrimitiveArray = Utils.makePrimitiveArray(valueDescriptor.getTypeName(), arrayList2);
                if (objMakePrimitiveArray == null) {
                    throw new IOException("Unsupported type " + ((Object) arrayList2) + " in array");
                }
                arrayList.add(objMakePrimitiveArray);
            } else {
                arrayList.add(objectify(valueDescriptor.getTypeName(), element.attribute(valueDescriptor.getName())));
            }
        }
        return PrivateAccess.getInstance().newAnnotation(type, arrayList, false);
    }

    private Object objectify(String str, String str2) throws IOException {
        try {
            switch (str) {
                case "int":
                    return Integer.valueOf(str2);
                case "long":
                    return Long.valueOf(str2);
                case "double":
                    return Double.valueOf(str2);
                case "float":
                    return Float.valueOf(str2);
                case "short":
                    return Short.valueOf(str2);
                case "char":
                    if (str2.length() != 1) {
                        throw new IOException("Unexpected size of char");
                    }
                    return Character.valueOf(str2.charAt(0));
                case "byte":
                    return Byte.valueOf(str2);
                case "boolean":
                    return Boolean.valueOf(str2);
                case "java.lang.String":
                    return str2;
                default:
                    throw new IOException("Unsupported type for annotation " + str);
            }
        } catch (IllegalArgumentException e2) {
            throw new IOException("Could not parse text representation of " + str);
        }
    }

    private Type getType(String str, MetadataDescriptor.Element element) {
        long jLongValue = element.longValue(str);
        Type type = this.types.get(Long.valueOf(jLongValue));
        if (type == null) {
            throw new IllegalStateException("Type '" + jLongValue + "' is not defined for " + element.attribute("type"));
        }
        return type;
    }

    private void buildEvenTypes() {
        for (Type type : this.descriptor.types) {
            if (type instanceof PlatformEventType) {
                this.descriptor.eventTypes.add(PrivateAccess.getInstance().newEventType((PlatformEventType) type));
            }
        }
    }

    private void defineTypes(MetadataDescriptor.Element element) {
        for (MetadataDescriptor.Element element2 : element.elements(Constants.ATTRNAME_CLASS)) {
            Type type = this.types.get(Long.valueOf(element2.attribute("id", -1L)));
            for (MetadataDescriptor.Element element3 : element2.elements("setting")) {
                ((PlatformEventType) type).add(PrivateAccess.getInstance().newSettingDescriptor(getType(Constants.ATTRNAME_CLASS, element3), element3.attribute("name"), element3.attribute("name"), new ArrayList(2)));
            }
            for (MetadataDescriptor.Element element4 : element2.elements("field")) {
                type.add(PrivateAccess.getInstance().newValueDescriptor(element4.attribute("name"), getType(Constants.ATTRNAME_CLASS, element4), new ArrayList(), (int) element4.attribute("dimension", 0L), element4.attribute("constantPool") != null, null));
            }
            type.trimFields();
        }
    }

    private void declareTypes(MetadataDescriptor.Element element) {
        Type type;
        for (MetadataDescriptor.Element element2 : element.elements(Constants.ATTRNAME_CLASS)) {
            String strAttribute = element2.attribute("name");
            String strAttribute2 = element2.attribute("superType");
            boolean z2 = element2.attribute("simpleType") != null;
            long jAttribute = element2.attribute("id", -1L);
            if (Type.SUPER_TYPE_EVENT.equals(strAttribute2)) {
                type = new PlatformEventType(strAttribute, jAttribute, false, false);
            } else {
                type = new Type(strAttribute, strAttribute2, jAttribute, false, Boolean.valueOf(z2));
            }
            Type type2 = type;
            this.types.put(Long.valueOf(jAttribute), type2);
            this.descriptor.types.add(type2);
        }
    }

    public MetadataDescriptor getDescriptor() {
        return this.descriptor;
    }
}
