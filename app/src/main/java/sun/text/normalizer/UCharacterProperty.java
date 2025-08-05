package sun.text.normalizer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import sun.text.normalizer.CharTrie;
import sun.text.normalizer.RangeValueIterator;

/* loaded from: rt.jar:sun/text/normalizer/UCharacterProperty.class */
public final class UCharacterProperty {
    public CharTrie m_trie_;
    public char[] m_trieIndex_;
    public char[] m_trieData_;
    public int m_trieInitialValue_;
    public VersionInfo m_unicodeVersion_;
    public static final int SRC_PROPSVEC = 2;
    public static final int SRC_COUNT = 9;
    CharTrie m_additionalTrie_;
    int[] m_additionalVectors_;
    int m_additionalColumnsCount_;
    int m_maxBlockScriptValue_;
    int m_maxJTGValue_;
    private static UCharacterProperty INSTANCE_ = null;
    private static final String DATA_FILE_NAME_ = "/sun/text/resources/uprops.icu";
    private static final int DATA_BUFFER_SIZE_ = 25000;
    private static final int VALUE_SHIFT_ = 8;
    private static final int UNSIGNED_VALUE_MASK_AFTER_SHIFT_ = 255;
    private static final int LEAD_SURROGATE_SHIFT_ = 10;
    private static final int SURROGATE_OFFSET_ = -56613888;
    private static final int FIRST_NIBBLE_SHIFT_ = 4;
    private static final int LAST_NIBBLE_MASK_ = 15;
    private static final int AGE_SHIFT_ = 24;

    public void setIndexData(CharTrie.FriendAgent friendAgent) {
        this.m_trieIndex_ = friendAgent.getPrivateIndex();
        this.m_trieData_ = friendAgent.getPrivateData();
        this.m_trieInitialValue_ = friendAgent.getPrivateInitialValue();
    }

    public final int getProperty(int i2) {
        if (i2 < 55296 || (i2 > 56319 && i2 < 65536)) {
            try {
                return this.m_trieData_[(this.m_trieIndex_[i2 >> 5] << 2) + (i2 & 31)];
            } catch (ArrayIndexOutOfBoundsException e2) {
                return this.m_trieInitialValue_;
            }
        }
        if (i2 <= 56319) {
            return this.m_trieData_[(this.m_trieIndex_[320 + (i2 >> 5)] << 2) + (i2 & 31)];
        }
        if (i2 <= 1114111) {
            return this.m_trie_.getSurrogateValue(UTF16.getLeadSurrogate(i2), (char) (i2 & 1023));
        }
        return this.m_trieInitialValue_;
    }

    public static int getUnsignedValue(int i2) {
        return (i2 >> 8) & 255;
    }

    public int getAdditional(int i2, int i3) {
        if (i3 == -1) {
            return getProperty(i2);
        }
        if (i3 < 0 || i3 >= this.m_additionalColumnsCount_) {
            return 0;
        }
        return this.m_additionalVectors_[this.m_additionalTrie_.getCodePointValue(i2) + i3];
    }

    public VersionInfo getAge(int i2) {
        int additional = getAdditional(i2, 0) >> 24;
        return VersionInfo.getInstance((additional >> 4) & 15, additional & 15, 0, 0);
    }

    public static int getRawSupplementary(char c2, char c3) {
        return (c2 << '\n') + c3 + SURROGATE_OFFSET_;
    }

    public static UCharacterProperty getInstance() {
        if (INSTANCE_ == null) {
            try {
                INSTANCE_ = new UCharacterProperty();
            } catch (Exception e2) {
                throw new MissingResourceException(e2.getMessage(), "", "");
            }
        }
        return INSTANCE_;
    }

    public static boolean isRuleWhiteSpace(int i2) {
        return i2 >= 9 && i2 <= 8233 && (i2 <= 13 || i2 == 32 || i2 == 133 || i2 == 8206 || i2 == 8207 || i2 >= 8232);
    }

    private UCharacterProperty() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(ICUData.getRequiredStream(DATA_FILE_NAME_), DATA_BUFFER_SIZE_);
        new UCharacterPropertyReader(bufferedInputStream).read(this);
        bufferedInputStream.close();
        this.m_trie_.putIndexData(this);
    }

    public void upropsvec_addPropertyStarts(UnicodeSet unicodeSet) {
        if (this.m_additionalColumnsCount_ > 0) {
            TrieIterator trieIterator = new TrieIterator(this.m_additionalTrie_);
            RangeValueIterator.Element element = new RangeValueIterator.Element();
            while (trieIterator.next(element)) {
                unicodeSet.add(element.start);
            }
        }
    }
}
