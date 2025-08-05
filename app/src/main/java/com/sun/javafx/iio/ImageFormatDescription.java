package com.sun.javafx.iio;

import java.util.Arrays;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageFormatDescription.class */
public interface ImageFormatDescription {
    String getFormatName();

    List<String> getExtensions();

    List<Signature> getSignatures();

    /* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageFormatDescription$Signature.class */
    public static final class Signature {
        private final byte[] bytes;

        public Signature(byte... bytes) {
            this.bytes = bytes;
        }

        public int getLength() {
            return this.bytes.length;
        }

        public boolean matches(byte[] streamBytes) {
            if (streamBytes.length < this.bytes.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.bytes.length; i2++) {
                if (streamBytes[i2] != this.bytes[i2]) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            return Arrays.hashCode(this.bytes);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Signature)) {
                return false;
            }
            return Arrays.equals(this.bytes, ((Signature) other).bytes);
        }
    }
}
