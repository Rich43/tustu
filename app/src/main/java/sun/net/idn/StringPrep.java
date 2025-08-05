package sun.net.idn;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.ParseException;
import sun.text.Normalizer;
import sun.text.normalizer.CharTrie;
import sun.text.normalizer.NormalizerImpl;
import sun.text.normalizer.Trie;
import sun.text.normalizer.UCharacter;
import sun.text.normalizer.UCharacterIterator;
import sun.text.normalizer.UTF16;
import sun.text.normalizer.VersionInfo;

/* loaded from: rt.jar:sun/net/idn/StringPrep.class */
public final class StringPrep {
    public static final int DEFAULT = 0;
    public static final int ALLOW_UNASSIGNED = 1;
    private static final int UNASSIGNED = 0;
    private static final int MAP = 1;
    private static final int PROHIBITED = 2;
    private static final int DELETE = 3;
    private static final int TYPE_LIMIT = 4;
    private static final int NORMALIZATION_ON = 1;
    private static final int CHECK_BIDI_ON = 2;
    private static final int TYPE_THRESHOLD = 65520;
    private static final int MAX_INDEX_VALUE = 16319;
    private static final int MAX_INDEX_TOP_LENGTH = 3;
    private static final int INDEX_TRIE_SIZE = 0;
    private static final int INDEX_MAPPING_DATA_SIZE = 1;
    private static final int NORM_CORRECTNS_LAST_UNI_VERSION = 2;
    private static final int ONE_UCHAR_MAPPING_INDEX_START = 3;
    private static final int TWO_UCHARS_MAPPING_INDEX_START = 4;
    private static final int THREE_UCHARS_MAPPING_INDEX_START = 5;
    private static final int FOUR_UCHARS_MAPPING_INDEX_START = 6;
    private static final int OPTIONS = 7;
    private static final int INDEX_TOP = 16;
    private static final int DATA_BUFFER_SIZE = 25000;
    private StringPrepTrieImpl sprepTrieImpl;
    private int[] indexes;
    private char[] mappingData;
    private byte[] formatVersion;
    private VersionInfo sprepUniVer;
    private VersionInfo normCorrVer;
    private boolean doNFKC;
    private boolean checkBiDi;

    /* loaded from: rt.jar:sun/net/idn/StringPrep$StringPrepTrieImpl.class */
    private static final class StringPrepTrieImpl implements Trie.DataManipulate {
        private CharTrie sprepTrie;

        private StringPrepTrieImpl() {
            this.sprepTrie = null;
        }

        @Override // sun.text.normalizer.Trie.DataManipulate
        public int getFoldingOffset(int i2) {
            return i2;
        }
    }

    private char getCodePointValue(int i2) {
        return this.sprepTrieImpl.sprepTrie.getCodePointValue(i2);
    }

    private static VersionInfo getVersionInfo(int i2) {
        return VersionInfo.getInstance((i2 >> 24) & 255, (i2 >> 16) & 255, (i2 >> 8) & 255, i2 & 255);
    }

    private static VersionInfo getVersionInfo(byte[] bArr) {
        if (bArr.length != 4) {
            return null;
        }
        return VersionInfo.getInstance(bArr[0], bArr[1], bArr[2], bArr[3]);
    }

    public StringPrep(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, DATA_BUFFER_SIZE);
        StringPrepDataReader stringPrepDataReader = new StringPrepDataReader(bufferedInputStream);
        this.indexes = stringPrepDataReader.readIndexes(16);
        byte[] bArr = new byte[this.indexes[0]];
        this.mappingData = new char[this.indexes[1] / 2];
        stringPrepDataReader.read(bArr, this.mappingData);
        this.sprepTrieImpl = new StringPrepTrieImpl();
        this.sprepTrieImpl.sprepTrie = new CharTrie(new ByteArrayInputStream(bArr), this.sprepTrieImpl);
        this.formatVersion = stringPrepDataReader.getDataFormatVersion();
        this.doNFKC = (this.indexes[7] & 1) > 0;
        this.checkBiDi = (this.indexes[7] & 2) > 0;
        this.sprepUniVer = getVersionInfo(stringPrepDataReader.getUnicodeVersion());
        this.normCorrVer = getVersionInfo(this.indexes[2]);
        VersionInfo unicodeVersion = NormalizerImpl.getUnicodeVersion();
        if (unicodeVersion.compareTo(this.sprepUniVer) < 0 && unicodeVersion.compareTo(this.normCorrVer) < 0 && (this.indexes[7] & 1) > 0) {
            throw new IOException("Normalization Correction version not supported");
        }
        bufferedInputStream.close();
    }

    /* loaded from: rt.jar:sun/net/idn/StringPrep$Values.class */
    private static final class Values {
        boolean isIndex;
        int value;
        int type;

        private Values() {
        }

        public void reset() {
            this.isIndex = false;
            this.value = 0;
            this.type = -1;
        }
    }

    private static final void getValues(char c2, Values values) {
        values.reset();
        if (c2 == 0) {
            values.type = 4;
            return;
        }
        if (c2 >= TYPE_THRESHOLD) {
            values.type = c2 - TYPE_THRESHOLD;
            return;
        }
        values.type = 1;
        if ((c2 & 2) > 0) {
            values.isIndex = true;
            values.value = c2 >> 2;
        } else {
            values.isIndex = false;
            values.value = (c2 << 16) >> 16;
            values.value >>= 2;
        }
        if ((c2 >> 2) == MAX_INDEX_VALUE) {
            values.type = 3;
            values.isIndex = false;
            values.value = 0;
        }
    }

    private StringBuffer map(UCharacterIterator uCharacterIterator, int i2) throws ParseException {
        char c2;
        Values values = new Values();
        StringBuffer stringBuffer = new StringBuffer();
        boolean z2 = (i2 & 1) > 0;
        while (true) {
            int iNextCodePoint = uCharacterIterator.nextCodePoint();
            int i3 = iNextCodePoint;
            if (iNextCodePoint != -1) {
                getValues(getCodePointValue(i3), values);
                if (values.type == 0 && !z2) {
                    throw new ParseException("An unassigned code point was found in the input " + uCharacterIterator.getText(), uCharacterIterator.getIndex());
                }
                if (values.type == 1) {
                    if (values.isIndex) {
                        int i4 = values.value;
                        if (i4 >= this.indexes[3] && i4 < this.indexes[4]) {
                            c2 = 1;
                        } else if (i4 >= this.indexes[4] && i4 < this.indexes[5]) {
                            c2 = 2;
                        } else if (i4 >= this.indexes[5] && i4 < this.indexes[6]) {
                            c2 = 3;
                        } else {
                            i4++;
                            c2 = this.mappingData[i4];
                        }
                        stringBuffer.append(this.mappingData, i4, (int) c2);
                    } else {
                        i3 -= values.value;
                        UTF16.append(stringBuffer, i3);
                    }
                } else if (values.type != 3) {
                    UTF16.append(stringBuffer, i3);
                }
            } else {
                return stringBuffer;
            }
        }
    }

    private StringBuffer normalize(StringBuffer stringBuffer) {
        return new StringBuffer(Normalizer.normalize(stringBuffer.toString(), Normalizer.Form.NFKC, 262432));
    }

    public StringBuffer prepare(UCharacterIterator uCharacterIterator, int i2) throws ParseException {
        StringBuffer map = map(uCharacterIterator, i2);
        StringBuffer stringBufferNormalize = map;
        if (this.doNFKC) {
            stringBufferNormalize = normalize(map);
        }
        UCharacterIterator uCharacterIterator2 = UCharacterIterator.getInstance(stringBufferNormalize);
        Values values = new Values();
        int direction = 19;
        int i3 = 19;
        int index = -1;
        int index2 = -1;
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            int iNextCodePoint = uCharacterIterator2.nextCodePoint();
            if (iNextCodePoint != -1) {
                getValues(getCodePointValue(iNextCodePoint), values);
                if (values.type == 2) {
                    throw new ParseException("A prohibited code point was found in the input" + uCharacterIterator2.getText(), values.value);
                }
                direction = UCharacter.getDirection(iNextCodePoint);
                if (i3 == 19) {
                    i3 = direction;
                }
                if (direction == 0) {
                    z3 = true;
                    index2 = uCharacterIterator2.getIndex() - 1;
                }
                if (direction == 1 || direction == 13) {
                    z2 = true;
                    index = uCharacterIterator2.getIndex() - 1;
                }
            } else {
                if (this.checkBiDi) {
                    if (z3 && z2) {
                        throw new ParseException("The input does not conform to the rules for BiDi code points." + uCharacterIterator2.getText(), index > index2 ? index : index2);
                    }
                    if (z2 && ((i3 != 1 && i3 != 13) || (direction != 1 && direction != 13))) {
                        throw new ParseException("The input does not conform to the rules for BiDi code points." + uCharacterIterator2.getText(), index > index2 ? index : index2);
                    }
                }
                return stringBufferNormalize;
            }
        }
    }
}
