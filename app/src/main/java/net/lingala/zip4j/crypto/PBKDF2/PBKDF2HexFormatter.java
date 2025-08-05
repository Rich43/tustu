package net.lingala.zip4j.crypto.PBKDF2;

import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/crypto/PBKDF2/PBKDF2HexFormatter.class */
class PBKDF2HexFormatter {
    PBKDF2HexFormatter() {
    }

    public boolean fromString(PBKDF2Parameters p2, String s2) throws NumberFormatException {
        String[] p123;
        if (p2 == null || s2 == null || (p123 = s2.split(CallSiteDescriptor.TOKEN_DELIMITER)) == null || p123.length != 3) {
            return true;
        }
        byte[] salt = BinTools.hex2bin(p123[0]);
        int iterationCount = Integer.parseInt(p123[1]);
        byte[] bDK = BinTools.hex2bin(p123[2]);
        p2.setSalt(salt);
        p2.setIterationCount(iterationCount);
        p2.setDerivedKey(bDK);
        return false;
    }

    public String toString(PBKDF2Parameters p2) {
        String s2 = new StringBuffer(String.valueOf(BinTools.bin2hex(p2.getSalt()))).append(CallSiteDescriptor.TOKEN_DELIMITER).append(String.valueOf(p2.getIterationCount())).append(CallSiteDescriptor.TOKEN_DELIMITER).append(BinTools.bin2hex(p2.getDerivedKey())).toString();
        return s2;
    }
}
