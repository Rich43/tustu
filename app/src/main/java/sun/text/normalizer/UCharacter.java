package sun.text.normalizer;

import java.io.IOException;
import java.util.MissingResourceException;

/* loaded from: rt.jar:sun/text/normalizer/UCharacter.class */
public final class UCharacter {
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 1114111;
    public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
    private static final UCharacterProperty PROPERTY_;
    private static final char[] PROPERTY_TRIE_INDEX_;
    private static final char[] PROPERTY_TRIE_DATA_;
    private static final int PROPERTY_INITIAL_VALUE_;
    private static final UBiDiProps gBdp;
    private static final int NUMERIC_TYPE_SHIFT_ = 5;
    private static final int NUMERIC_TYPE_MASK_ = 224;

    /* loaded from: rt.jar:sun/text/normalizer/UCharacter$NumericType.class */
    public interface NumericType {
        public static final int DECIMAL = 1;
    }

    public static int digit(int i2, int i3) {
        int europeanDigit;
        int property = getProperty(i2);
        if (getNumericType(property) == 1) {
            europeanDigit = UCharacterProperty.getUnsignedValue(property);
        } else {
            europeanDigit = getEuropeanDigit(i2);
        }
        if (0 > europeanDigit || europeanDigit >= i3) {
            return -1;
        }
        return europeanDigit;
    }

    public static int getDirection(int i2) {
        return gBdp.getClass(i2);
    }

    public static int getCodePoint(char c2, char c3) {
        if (UTF16.isLeadSurrogate(c2) && UTF16.isTrailSurrogate(c3)) {
            return UCharacterProperty.getRawSupplementary(c2, c3);
        }
        throw new IllegalArgumentException("Illegal surrogate characters");
    }

    public static VersionInfo getAge(int i2) {
        if (i2 < 0 || i2 > 1114111) {
            throw new IllegalArgumentException("Codepoint out of bounds");
        }
        return PROPERTY_.getAge(i2);
    }

    static {
        UBiDiProps dummy;
        try {
            PROPERTY_ = UCharacterProperty.getInstance();
            PROPERTY_TRIE_INDEX_ = PROPERTY_.m_trieIndex_;
            PROPERTY_TRIE_DATA_ = PROPERTY_.m_trieData_;
            PROPERTY_INITIAL_VALUE_ = PROPERTY_.m_trieInitialValue_;
            try {
                dummy = UBiDiProps.getSingleton();
            } catch (IOException e2) {
                dummy = UBiDiProps.getDummy();
            }
            gBdp = dummy;
        } catch (Exception e3) {
            throw new MissingResourceException(e3.getMessage(), "", "");
        }
    }

    private static int getEuropeanDigit(int i2) {
        if ((i2 > 122 && i2 < 65313) || i2 < 65) {
            return -1;
        }
        if ((i2 > 90 && i2 < 97) || i2 > 65370) {
            return -1;
        }
        if (i2 > 65338 && i2 < 65345) {
            return -1;
        }
        if (i2 <= 122) {
            return (i2 + 10) - (i2 <= 90 ? 65 : 97);
        }
        if (i2 <= 65338) {
            return (i2 + 10) - 65313;
        }
        return (i2 + 10) - 65345;
    }

    private static int getNumericType(int i2) {
        return (i2 & 224) >> 5;
    }

    private static final int getProperty(int i2) {
        if (i2 < 55296 || (i2 > 56319 && i2 < 65536)) {
            try {
                return PROPERTY_TRIE_DATA_[(PROPERTY_TRIE_INDEX_[i2 >> 5] << 2) + (i2 & 31)];
            } catch (ArrayIndexOutOfBoundsException e2) {
                return PROPERTY_INITIAL_VALUE_;
            }
        }
        if (i2 <= 56319) {
            return PROPERTY_TRIE_DATA_[(PROPERTY_TRIE_INDEX_[320 + (i2 >> 5)] << 2) + (i2 & 31)];
        }
        if (i2 <= 1114111) {
            return PROPERTY_.m_trie_.getSurrogateValue(UTF16.getLeadSurrogate(i2), (char) (i2 & 1023));
        }
        return PROPERTY_INITIAL_VALUE_;
    }
}
