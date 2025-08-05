package com.sun.corba.se.impl.naming.namingutil;

import com.sun.corba.se.impl.logging.NamingSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.io.StringWriter;
import org.omg.CORBA.DATA_CONVERSION;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/namingutil/Utility.class */
class Utility {
    private static NamingSystemException wrapper = NamingSystemException.get(CORBALogDomains.NAMING);

    Utility() {
    }

    static String cleanEscapes(String str) {
        StringWriter stringWriter = new StringWriter();
        int i2 = 0;
        while (i2 < str.length()) {
            char cCharAt = str.charAt(i2);
            if (cCharAt != '%') {
                stringWriter.write(cCharAt);
            } else {
                int i3 = i2 + 1;
                int iHexOf = hexOf(str.charAt(i3));
                i2 = i3 + 1;
                stringWriter.write((char) ((iHexOf * 16) + hexOf(str.charAt(i2))));
            }
            i2++;
        }
        return stringWriter.toString();
    }

    static int hexOf(char c2) {
        int i2 = c2 - '0';
        if (i2 >= 0 && i2 <= 9) {
            return i2;
        }
        int i3 = (c2 - 'a') + 10;
        if (i3 >= 10 && i3 <= 15) {
            return i3;
        }
        int i4 = (c2 - 'A') + 10;
        if (i4 >= 10 && i4 <= 15) {
            return i4;
        }
        throw new DATA_CONVERSION();
    }

    static void validateGIOPVersion(IIOPEndpointInfo iIOPEndpointInfo) {
        if (iIOPEndpointInfo.getMajor() > 1 || iIOPEndpointInfo.getMinor() > 2) {
            throw wrapper.insBadAddress();
        }
    }
}
