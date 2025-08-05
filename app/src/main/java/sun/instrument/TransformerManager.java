package sun.instrument;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/* loaded from: rt.jar:sun/instrument/TransformerManager.class */
public class TransformerManager {
    private TransformerInfo[] mTransformerList = new TransformerInfo[0];
    private boolean mIsRetransformable;

    /* loaded from: rt.jar:sun/instrument/TransformerManager$TransformerInfo.class */
    private class TransformerInfo {
        final ClassFileTransformer mTransformer;
        String mPrefix = null;

        TransformerInfo(ClassFileTransformer classFileTransformer) {
            this.mTransformer = classFileTransformer;
        }

        ClassFileTransformer transformer() {
            return this.mTransformer;
        }

        String getPrefix() {
            return this.mPrefix;
        }

        void setPrefix(String str) {
            this.mPrefix = str;
        }
    }

    TransformerManager(boolean z2) {
        this.mIsRetransformable = z2;
    }

    boolean isRetransformable() {
        return this.mIsRetransformable;
    }

    public synchronized void addTransformer(ClassFileTransformer classFileTransformer) {
        TransformerInfo[] transformerInfoArr = this.mTransformerList;
        TransformerInfo[] transformerInfoArr2 = new TransformerInfo[transformerInfoArr.length + 1];
        System.arraycopy(transformerInfoArr, 0, transformerInfoArr2, 0, transformerInfoArr.length);
        transformerInfoArr2[transformerInfoArr.length] = new TransformerInfo(classFileTransformer);
        this.mTransformerList = transformerInfoArr2;
    }

    public synchronized boolean removeTransformer(ClassFileTransformer classFileTransformer) {
        boolean z2 = false;
        TransformerInfo[] transformerInfoArr = this.mTransformerList;
        int length = transformerInfoArr.length;
        int i2 = length - 1;
        int i3 = 0;
        int i4 = length - 1;
        while (true) {
            if (i4 < 0) {
                break;
            }
            if (transformerInfoArr[i4].transformer() != classFileTransformer) {
                i4--;
            } else {
                z2 = true;
                i3 = i4;
                break;
            }
        }
        if (z2) {
            TransformerInfo[] transformerInfoArr2 = new TransformerInfo[i2];
            if (i3 > 0) {
                System.arraycopy(transformerInfoArr, 0, transformerInfoArr2, 0, i3);
            }
            if (i3 < i2) {
                System.arraycopy(transformerInfoArr, i3 + 1, transformerInfoArr2, i3, i2 - i3);
            }
            this.mTransformerList = transformerInfoArr2;
        }
        return z2;
    }

    synchronized boolean includesTransformer(ClassFileTransformer classFileTransformer) {
        for (TransformerInfo transformerInfo : this.mTransformerList) {
            if (transformerInfo.transformer() == classFileTransformer) {
                return true;
            }
        }
        return false;
    }

    private TransformerInfo[] getSnapshotTransformerList() {
        return this.mTransformerList;
    }

    public byte[] transform(ClassLoader classLoader, String str, Class<?> cls, ProtectionDomain protectionDomain, byte[] bArr) {
        byte[] bArr2;
        boolean z2 = false;
        byte[] bArr3 = bArr;
        for (TransformerInfo transformerInfo : getSnapshotTransformerList()) {
            byte[] bArrTransform = null;
            try {
                bArrTransform = transformerInfo.transformer().transform(classLoader, str, cls, protectionDomain, bArr3);
            } catch (Throwable th) {
            }
            if (bArrTransform != null) {
                z2 = true;
                bArr3 = bArrTransform;
            }
        }
        if (z2) {
            bArr2 = bArr3;
        } else {
            bArr2 = null;
        }
        return bArr2;
    }

    int getTransformerCount() {
        return getSnapshotTransformerList().length;
    }

    boolean setNativeMethodPrefix(ClassFileTransformer classFileTransformer, String str) {
        for (TransformerInfo transformerInfo : getSnapshotTransformerList()) {
            if (transformerInfo.transformer() == classFileTransformer) {
                transformerInfo.setPrefix(str);
                return true;
            }
        }
        return false;
    }

    String[] getNativeMethodPrefixes() {
        TransformerInfo[] snapshotTransformerList = getSnapshotTransformerList();
        String[] strArr = new String[snapshotTransformerList.length];
        for (int i2 = 0; i2 < snapshotTransformerList.length; i2++) {
            strArr[i2] = snapshotTransformerList[i2].getPrefix();
        }
        return strArr;
    }
}
