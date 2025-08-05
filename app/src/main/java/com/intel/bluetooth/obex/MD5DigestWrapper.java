package com.intel.bluetooth.obex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/MD5DigestWrapper.class */
class MD5DigestWrapper {
    private MessageDigest md5impl;

    MD5DigestWrapper() {
        try {
            this.md5impl = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2.getMessage());
        }
    }

    void update(byte[] input) {
        this.md5impl.update(input);
    }

    byte[] digest() {
        return this.md5impl.digest();
    }
}
