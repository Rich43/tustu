package sun.security.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:sun/security/util/ManifestDigester.class */
public class ManifestDigester {
    public static final String MF_MAIN_ATTRS = "Manifest-Main-Attributes";
    private byte[] rawBytes;
    private HashMap<String, Entry> entries = new HashMap<>();

    /* loaded from: rt.jar:sun/security/util/ManifestDigester$Position.class */
    static class Position {
        int endOfFirstLine;
        int endOfSection;
        int startOfNext;

        Position() {
        }
    }

    private boolean findSection(int i2, Position position) {
        boolean z2;
        int i3 = i2;
        int length = this.rawBytes.length;
        int i4 = i2 - 1;
        boolean z3 = true;
        position.endOfFirstLine = Integer.MIN_VALUE;
        while (i3 < length) {
            switch (this.rawBytes[i3]) {
                case 10:
                    break;
                case 13:
                    if (position.endOfFirstLine == Integer.MIN_VALUE) {
                        position.endOfFirstLine = i3 - 1;
                    }
                    if (i3 < length - 1 && this.rawBytes[i3 + 1] == 10) {
                        i3++;
                        break;
                    }
                    break;
                default:
                    z2 = false;
                    continue;
                    z3 = z2;
                    i3++;
            }
            if (position.endOfFirstLine == Integer.MIN_VALUE) {
                position.endOfFirstLine = i3 - 1;
            }
            if (z3 || i3 == length - 1) {
                position.endOfSection = z3 ? i4 : i3;
                position.startOfNext = i3 + 1;
                return true;
            }
            i4 = i3;
            z2 = true;
            z3 = z2;
            i3++;
        }
        return false;
    }

    public ManifestDigester(byte[] bArr) {
        this.rawBytes = bArr;
        new ByteArrayOutputStream();
        Position position = new Position();
        if (!findSection(0, position)) {
            return;
        }
        this.entries.put(MF_MAIN_ATTRS, new Entry().addSection(new Section(0, position.endOfSection + 1, position.startOfNext, this.rawBytes)));
        int i2 = position.startOfNext;
        while (true) {
            int i3 = i2;
            if (findSection(i3, position)) {
                int i4 = (position.endOfFirstLine - i3) + 1;
                int i5 = (position.endOfSection - i3) + 1;
                int i6 = position.startOfNext - i3;
                if (i4 > 6 && isNameAttr(bArr, i3)) {
                    StringBuilder sb = new StringBuilder(i5);
                    try {
                        sb.append(new String(bArr, i3 + 6, i4 - 6, InternalZipConstants.CHARSET_UTF8));
                        int i7 = i3 + i4;
                        if (i7 - i3 < i5) {
                            if (bArr[i7] == 13) {
                                i7 += 2;
                            } else {
                                i7++;
                            }
                        }
                        while (i7 - i3 < i5) {
                            int i8 = i7;
                            i7++;
                            if (bArr[i8] != 32) {
                                break;
                            }
                            while (i7 - i3 < i5) {
                                int i9 = i7;
                                i7++;
                                if (bArr[i9] == 10) {
                                    break;
                                }
                            }
                            if (bArr[i7 - 1] != 10) {
                                return;
                            } else {
                                sb.append(new String(bArr, i7, bArr[i7 - 2] == 13 ? (i7 - i7) - 2 : (i7 - i7) - 1, InternalZipConstants.CHARSET_UTF8));
                            }
                        }
                        Entry entry = this.entries.get(sb.toString());
                        if (entry == null) {
                            this.entries.put(sb.toString(), new Entry().addSection(new Section(i3, i5, i6, this.rawBytes)));
                        } else {
                            entry.addSection(new Section(i3, i5, i6, this.rawBytes));
                        }
                    } catch (UnsupportedEncodingException e2) {
                        throw new IllegalStateException("UTF8 not available on platform");
                    }
                }
                i2 = position.startOfNext;
            } else {
                return;
            }
        }
    }

    private boolean isNameAttr(byte[] bArr, int i2) {
        return (bArr[i2] == 78 || bArr[i2] == 110) && (bArr[i2 + 1] == 97 || bArr[i2 + 1] == 65) && ((bArr[i2 + 2] == 109 || bArr[i2 + 2] == 77) && ((bArr[i2 + 3] == 101 || bArr[i2 + 3] == 69) && bArr[i2 + 4] == 58 && bArr[i2 + 5] == 32));
    }

    /* loaded from: rt.jar:sun/security/util/ManifestDigester$Entry.class */
    public static class Entry {
        private List<Section> sections = new ArrayList();
        boolean oldStyle;

        /* JADX INFO: Access modifiers changed from: private */
        public Entry addSection(Section section) {
            this.sections.add(section);
            return this;
        }

        public byte[] digest(MessageDigest messageDigest) {
            messageDigest.reset();
            for (Section section : this.sections) {
                if (!this.oldStyle) {
                    messageDigest.update(section.rawBytes, section.offset, section.lengthWithBlankLine);
                } else {
                    Section.doOldStyle(messageDigest, section.rawBytes, section.offset, section.lengthWithBlankLine);
                }
            }
            return messageDigest.digest();
        }

        public byte[] digestWorkaround(MessageDigest messageDigest) {
            messageDigest.reset();
            for (Section section : this.sections) {
                messageDigest.update(section.rawBytes, section.offset, section.length);
            }
            return messageDigest.digest();
        }
    }

    /* loaded from: rt.jar:sun/security/util/ManifestDigester$Section.class */
    private static class Section {
        int offset;
        int length;
        int lengthWithBlankLine;
        byte[] rawBytes;

        public Section(int i2, int i3, int i4, byte[] bArr) {
            this.offset = i2;
            this.length = i3;
            this.lengthWithBlankLine = i4;
            this.rawBytes = bArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void doOldStyle(MessageDigest messageDigest, byte[] bArr, int i2, int i3) {
            int i4 = i2;
            int i5 = i2;
            int i6 = i2 + i3;
            byte b2 = -1;
            while (i4 < i6) {
                if (bArr[i4] == 13 && b2 == 32) {
                    messageDigest.update(bArr, i5, (i4 - i5) - 1);
                    i5 = i4;
                }
                b2 = bArr[i4];
                i4++;
            }
            messageDigest.update(bArr, i5, i4 - i5);
        }
    }

    public Entry get(String str, boolean z2) {
        Entry entry = this.entries.get(str);
        if (entry != null) {
            entry.oldStyle = z2;
        }
        return entry;
    }

    public byte[] manifestDigest(MessageDigest messageDigest) {
        messageDigest.reset();
        messageDigest.update(this.rawBytes, 0, this.rawBytes.length);
        return messageDigest.digest();
    }
}
