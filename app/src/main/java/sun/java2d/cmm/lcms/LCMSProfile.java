package sun.java2d.cmm.lcms;

import java.util.Arrays;
import java.util.HashMap;
import sun.java2d.cmm.Profile;

/* loaded from: rt.jar:sun/java2d/cmm/lcms/LCMSProfile.class */
final class LCMSProfile extends Profile {
    private final TagCache tagCache;
    private final Object disposerReferent;

    LCMSProfile(long j2, Object obj) {
        super(j2);
        this.disposerReferent = obj;
        this.tagCache = new TagCache(this);
    }

    final long getLcmsPtr() {
        return getNativePtr();
    }

    TagData getTag(int i2) {
        return this.tagCache.getTag(i2);
    }

    void clearTagCache() {
        this.tagCache.clear();
    }

    /* loaded from: rt.jar:sun/java2d/cmm/lcms/LCMSProfile$TagCache.class */
    static class TagCache {
        final LCMSProfile profile;
        private HashMap<Integer, TagData> tags = new HashMap<>();

        TagCache(LCMSProfile lCMSProfile) {
            this.profile = lCMSProfile;
        }

        TagData getTag(int i2) {
            byte[] tagNative;
            TagData tagData = this.tags.get(Integer.valueOf(i2));
            if (tagData == null && (tagNative = LCMS.getTagNative(this.profile.getNativePtr(), i2)) != null) {
                tagData = new TagData(i2, tagNative);
                this.tags.put(Integer.valueOf(i2), tagData);
            }
            return tagData;
        }

        void clear() {
            this.tags.clear();
        }
    }

    /* loaded from: rt.jar:sun/java2d/cmm/lcms/LCMSProfile$TagData.class */
    static class TagData {
        private int signature;
        private byte[] data;

        TagData(int i2, byte[] bArr) {
            this.signature = i2;
            this.data = bArr;
        }

        int getSize() {
            return this.data.length;
        }

        byte[] getData() {
            return Arrays.copyOf(this.data, this.data.length);
        }

        void copyDataTo(byte[] bArr) {
            System.arraycopy(this.data, 0, bArr, 0, this.data.length);
        }

        int getSignature() {
            return this.signature;
        }
    }
}
