package jdk.jfr.internal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.management.JMX;
import jdk.jfr.AnnotationElement;
import jdk.jfr.SettingDescriptor;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.MetadataDescriptor;

/* loaded from: jfr.jar:jdk/jfr/internal/MetadataWriter.class */
final class MetadataWriter {
    private final MetadataDescriptor.Element metadata = new MetadataDescriptor.Element("metadata");
    private final MetadataDescriptor.Element root = new MetadataDescriptor.Element("root");

    public MetadataWriter(MetadataDescriptor metadataDescriptor) {
        metadataDescriptor.getTypes().forEach(type -> {
            makeTypeElement(this.metadata, type);
        });
        this.root.add(this.metadata);
        MetadataDescriptor.Element element = new MetadataDescriptor.Element("region");
        element.addAttribute("locale", metadataDescriptor.locale);
        element.addAttribute("gmtOffset", Long.valueOf(metadataDescriptor.gmtOffset));
        this.root.add(element);
    }

    public void writeBinary(DataOutput dataOutput) throws IOException {
        HashSet hashSet = new HashSet(1000);
        buildStringPool(this.root, hashSet);
        LinkedHashMap linkedHashMap = new LinkedHashMap(hashSet.size());
        int i2 = 0;
        writeInt(dataOutput, hashSet.size());
        for (String str : hashSet) {
            linkedHashMap.put(str, Integer.valueOf(i2));
            writeString(dataOutput, str);
            i2++;
        }
        write(dataOutput, this.root, linkedHashMap);
    }

    private void writeString(DataOutput dataOutput, String str) throws IOException {
        if (str == null) {
            dataOutput.writeByte(0);
            return;
        }
        dataOutput.writeByte(4);
        int length = str.length();
        writeInt(dataOutput, length);
        for (int i2 = 0; i2 < length; i2++) {
            writeInt(dataOutput, str.charAt(i2));
        }
    }

    private void writeInt(DataOutput dataOutput, int i2) throws IOException {
        long j2 = i2 & 4294967295L;
        if (j2 < 128) {
            dataOutput.write((byte) j2);
            return;
        }
        dataOutput.write((byte) (j2 | 128));
        long j3 = j2 >> 7;
        if (j3 < 128) {
            dataOutput.write((byte) j3);
            return;
        }
        dataOutput.write((byte) (j3 | 128));
        long j4 = j3 >> 7;
        if (j4 < 128) {
            dataOutput.write((byte) j4);
            return;
        }
        dataOutput.write((byte) (j4 | 128));
        long j5 = j4 >> 7;
        if (j5 < 128) {
            dataOutput.write((byte) j5);
        } else {
            dataOutput.write((byte) (j5 >> 7));
        }
    }

    private void buildStringPool(MetadataDescriptor.Element element, Set<String> set) {
        set.add(element.name);
        for (MetadataDescriptor.Attribute attribute : element.attributes) {
            set.add(attribute.name);
            set.add(attribute.value);
        }
        Iterator<MetadataDescriptor.Element> it = element.elements.iterator();
        while (it.hasNext()) {
            buildStringPool(it.next(), set);
        }
    }

    private void write(DataOutput dataOutput, MetadataDescriptor.Element element, HashMap<String, Integer> map) throws IOException {
        writeInt(dataOutput, map.get(element.name).intValue());
        writeInt(dataOutput, element.attributes.size());
        for (MetadataDescriptor.Attribute attribute : element.attributes) {
            writeInt(dataOutput, map.get(attribute.name).intValue());
            writeInt(dataOutput, map.get(attribute.value).intValue());
        }
        writeInt(dataOutput, element.elements.size());
        Iterator<MetadataDescriptor.Element> it = element.elements.iterator();
        while (it.hasNext()) {
            write(dataOutput, it.next(), map);
        }
    }

    private void makeTypeElement(MetadataDescriptor.Element element, Type type) {
        MetadataDescriptor.Element elementNewChild = element.newChild(Constants.ATTRNAME_CLASS);
        elementNewChild.addAttribute("name", type.getName());
        String superType = type.getSuperType();
        if (superType != null) {
            elementNewChild.addAttribute("superType", superType);
        }
        if (type.isSimpleType()) {
            elementNewChild.addAttribute("simpleType", true);
        }
        elementNewChild.addAttribute("id", Long.valueOf(type.getId()));
        if (type instanceof PlatformEventType) {
            Iterator<SettingDescriptor> it = ((PlatformEventType) type).getSettings().iterator();
            while (it.hasNext()) {
                makeSettingElement(elementNewChild, it.next());
            }
        }
        Iterator<ValueDescriptor> it2 = type.getFields().iterator();
        while (it2.hasNext()) {
            makeFieldElement(elementNewChild, it2.next());
        }
        Iterator<AnnotationElement> it3 = type.getAnnotationElements().iterator();
        while (it3.hasNext()) {
            makeAnnotation(elementNewChild, it3.next());
        }
    }

    private void makeSettingElement(MetadataDescriptor.Element element, SettingDescriptor settingDescriptor) {
        MetadataDescriptor.Element elementNewChild = element.newChild("setting");
        elementNewChild.addAttribute("name", settingDescriptor.getName());
        elementNewChild.addAttribute(Constants.ATTRNAME_CLASS, Long.valueOf(settingDescriptor.getTypeId()));
        elementNewChild.addAttribute(JMX.DEFAULT_VALUE_FIELD, settingDescriptor.getDefaultValue());
        Iterator<AnnotationElement> it = settingDescriptor.getAnnotationElements().iterator();
        while (it.hasNext()) {
            makeAnnotation(elementNewChild, it.next());
        }
    }

    private void makeFieldElement(MetadataDescriptor.Element element, ValueDescriptor valueDescriptor) {
        MetadataDescriptor.Element elementNewChild = element.newChild("field");
        elementNewChild.addAttribute("name", valueDescriptor.getName());
        elementNewChild.addAttribute(Constants.ATTRNAME_CLASS, Long.valueOf(valueDescriptor.getTypeId()));
        if (valueDescriptor.isArray()) {
            elementNewChild.addAttribute("dimension", 1);
        }
        if (PrivateAccess.getInstance().isConstantPool(valueDescriptor)) {
            elementNewChild.addAttribute("constantPool", true);
        }
        Iterator<AnnotationElement> it = valueDescriptor.getAnnotationElements().iterator();
        while (it.hasNext()) {
            makeAnnotation(elementNewChild, it.next());
        }
    }

    private void makeAnnotation(MetadataDescriptor.Element element, AnnotationElement annotationElement) {
        MetadataDescriptor.Element elementNewChild = element.newChild("annotation");
        elementNewChild.addAttribute(Constants.ATTRNAME_CLASS, Long.valueOf(annotationElement.getTypeId()));
        List<Object> values = annotationElement.getValues();
        int i2 = 0;
        for (ValueDescriptor valueDescriptor : annotationElement.getValueDescriptors()) {
            int i3 = i2;
            i2++;
            Object obj = values.get(i3);
            if (valueDescriptor.isArray()) {
                elementNewChild.addArrayAttribute(elementNewChild, valueDescriptor.getName(), obj);
            } else {
                elementNewChild.addAttribute(valueDescriptor.getName(), obj);
            }
        }
    }
}
