package sun.nio.fs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.attribute.UserDefinedFileAttributeView;

/* loaded from: rt.jar:sun/nio/fs/AbstractUserDefinedFileAttributeView.class */
abstract class AbstractUserDefinedFileAttributeView implements UserDefinedFileAttributeView, DynamicFileAttributeView {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractUserDefinedFileAttributeView.class.desiredAssertionStatus();
    }

    protected AbstractUserDefinedFileAttributeView() {
    }

    protected void checkAccess(String str, boolean z2, boolean z3) {
        if (!$assertionsDisabled && !z2 && !z3) {
            throw new AssertionError();
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (z2) {
                securityManager.checkRead(str);
            }
            if (z3) {
                securityManager.checkWrite(str);
            }
            securityManager.checkPermission(new RuntimePermission("accessUserDefinedAttributes"));
        }
    }

    @Override // java.nio.file.attribute.UserDefinedFileAttributeView, java.nio.file.attribute.AttributeView
    public final String name() {
        return "user";
    }

    @Override // sun.nio.fs.DynamicFileAttributeView
    public final void setAttribute(String str, Object obj) throws IOException {
        ByteBuffer byteBufferWrap;
        if (obj instanceof byte[]) {
            byteBufferWrap = ByteBuffer.wrap((byte[]) obj);
        } else {
            byteBufferWrap = (ByteBuffer) obj;
        }
        write(str, byteBufferWrap);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x004f, code lost:
    
        r0 = new java.util.HashMap();
        r0 = r6.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0066, code lost:
    
        if (r0.hasNext() == false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0069, code lost:
    
        r0 = r0.next();
        r0 = size(r0);
        r0 = new byte[r0];
        r0 = read(r0, java.nio.ByteBuffer.wrap(r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0094, code lost:
    
        if (r0 != r0) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0097, code lost:
    
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x009c, code lost:
    
        r0 = java.util.Arrays.copyOf(r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00a3, code lost:
    
        r0.put(r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00b4, code lost:
    
        return r0;
     */
    @Override // sun.nio.fs.DynamicFileAttributeView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.Map<java.lang.String, java.lang.Object> readAttributes(java.lang.String[] r5) throws java.io.IOException {
        /*
            r4 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r6 = r0
            r0 = r5
            r7 = r0
            r0 = r7
            int r0 = r0.length
            r8 = r0
            r0 = 0
            r9 = r0
        L11:
            r0 = r9
            r1 = r8
            if (r0 >= r1) goto L4f
            r0 = r7
            r1 = r9
            r0 = r0[r1]
            r10 = r0
            r0 = r10
            java.lang.String r1 = "*"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L30
            r0 = r4
            java.util.List r0 = r0.list()
            r6 = r0
            goto L4f
        L30:
            r0 = r10
            int r0 = r0.length()
            if (r0 != 0) goto L40
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            r1.<init>()
            throw r0
        L40:
            r0 = r6
            r1 = r10
            boolean r0 = r0.add(r1)
            int r9 = r9 + 1
            goto L11
        L4f:
            java.util.HashMap r0 = new java.util.HashMap
            r1 = r0
            r1.<init>()
            r7 = r0
            r0 = r6
            java.util.Iterator r0 = r0.iterator()
            r8 = r0
        L5f:
            r0 = r8
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto Lb3
            r0 = r8
            java.lang.Object r0 = r0.next()
            java.lang.String r0 = (java.lang.String) r0
            r9 = r0
            r0 = r4
            r1 = r9
            int r0 = r0.size(r1)
            r10 = r0
            r0 = r10
            byte[] r0 = new byte[r0]
            r11 = r0
            r0 = r4
            r1 = r9
            r2 = r11
            java.nio.ByteBuffer r2 = java.nio.ByteBuffer.wrap(r2)
            int r0 = r0.read(r1, r2)
            r12 = r0
            r0 = r12
            r1 = r10
            if (r0 != r1) goto L9c
            r0 = r11
            goto La3
        L9c:
            r0 = r11
            r1 = r12
            byte[] r0 = java.util.Arrays.copyOf(r0, r1)
        La3:
            r13 = r0
            r0 = r7
            r1 = r9
            r2 = r13
            java.lang.Object r0 = r0.put(r1, r2)
            goto L5f
        Lb3:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.fs.AbstractUserDefinedFileAttributeView.readAttributes(java.lang.String[]):java.util.Map");
    }
}
