package javax.xml.bind;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/bind/DatatypeConverterInterface.class */
public interface DatatypeConverterInterface {
    String parseString(String str);

    BigInteger parseInteger(String str);

    int parseInt(String str);

    long parseLong(String str);

    short parseShort(String str);

    BigDecimal parseDecimal(String str);

    float parseFloat(String str);

    double parseDouble(String str);

    boolean parseBoolean(String str);

    byte parseByte(String str);

    QName parseQName(String str, NamespaceContext namespaceContext);

    Calendar parseDateTime(String str);

    byte[] parseBase64Binary(String str);

    byte[] parseHexBinary(String str);

    long parseUnsignedInt(String str);

    int parseUnsignedShort(String str);

    Calendar parseTime(String str);

    Calendar parseDate(String str);

    String parseAnySimpleType(String str);

    String printString(String str);

    String printInteger(BigInteger bigInteger);

    String printInt(int i2);

    String printLong(long j2);

    String printShort(short s2);

    String printDecimal(BigDecimal bigDecimal);

    String printFloat(float f2);

    String printDouble(double d2);

    String printBoolean(boolean z2);

    String printByte(byte b2);

    String printQName(QName qName, NamespaceContext namespaceContext);

    String printDateTime(Calendar calendar);

    String printBase64Binary(byte[] bArr);

    String printHexBinary(byte[] bArr);

    String printUnsignedInt(long j2);

    String printUnsignedShort(int i2);

    String printTime(Calendar calendar);

    String printDate(Calendar calendar);

    String printAnySimpleType(String str);
}
