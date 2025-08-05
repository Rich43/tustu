package javax.xml.bind.annotation.adapters;

import javax.xml.bind.DatatypeConverter;

/* loaded from: rt.jar:javax/xml/bind/annotation/adapters/HexBinaryAdapter.class */
public final class HexBinaryAdapter extends XmlAdapter<String, byte[]> {
    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public byte[] unmarshal(String s2) {
        if (s2 == null) {
            return null;
        }
        return DatatypeConverter.parseHexBinary(s2);
    }

    @Override // javax.xml.bind.annotation.adapters.XmlAdapter
    public String marshal(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return DatatypeConverter.printHexBinary(bytes);
    }
}
