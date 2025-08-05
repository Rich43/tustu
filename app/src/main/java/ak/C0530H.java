package ak;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ak.H, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/H.class */
public class C0530H extends C0546f {

    /* renamed from: a, reason: collision with root package name */
    Properties f4593a;

    public C0530H() {
        super(",", false);
        this.f4593a = null;
        this.f4593a = new Properties();
        this.f4837t = true;
        try {
            this.f4593a.load(new FileInputStream("FieldMaps/EMtron.properties"));
        } catch (IOException e2) {
            Logger.getLogger(C0530H.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        ArrayList arrayList = new ArrayList();
        Iterator itB = super.b();
        while (itB.hasNext()) {
            C0543c c0543c = (C0543c) itB.next();
            String property = this.f4593a.getProperty(c0543c.a());
            if (property != null) {
                c0543c.a(property);
            }
            arrayList.add(c0543c);
        }
        return arrayList.iterator();
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00e0, code lost:
    
        if (r6.f4839u == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00e3, code lost:
    
        r6.f4822f = h(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ec, code lost:
    
        r0 = d(r8, r6.f4822f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00fa, code lost:
    
        if (r13 < 4) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0109, code lost:
    
        if (d(r12, r6.f4822f) != r0) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0116, code lost:
    
        if (b(r12, r6.f4822f) == false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0119, code lost:
    
        r0 = r13 - 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0121, code lost:
    
        if (r0 == null) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0124, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x012c, code lost:
    
        r18 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x012e, code lost:
    
        java.util.logging.Logger.getLogger(ak.C0546f.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0145, code lost:
    
        if (r13 < 3) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0154, code lost:
    
        if (d(r11, r6.f4822f) != r0) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0161, code lost:
    
        if (b(r11, r6.f4822f) == false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0164, code lost:
    
        r0 = r13 - 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x016c, code lost:
    
        if (r0 == null) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x016f, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0177, code lost:
    
        r18 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0179, code lost:
    
        java.util.logging.Logger.getLogger(ak.C0546f.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0199, code lost:
    
        if (d(r10, r6.f4822f) != r0) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01a6, code lost:
    
        if (b(r10, r6.f4822f) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01a9, code lost:
    
        r0 = r13 - 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01b1, code lost:
    
        if (r0 == null) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01b4, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01bc, code lost:
    
        r18 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01be, code lost:
    
        java.util.logging.Logger.getLogger(ak.C0546f.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01d2, code lost:
    
        r0 = r13 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01da, code lost:
    
        if (r0 == null) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01dd, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01e5, code lost:
    
        r18 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01e7, code lost:
    
        java.util.logging.Logger.getLogger(ak.C0546f.class.getName()).log(java.util.logging.Level.SEVERE, (java.lang.String) null, (java.lang.Throwable) r18);
     */
    @Override // ak.C0546f
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int b(java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 602
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ak.C0530H.b(java.lang.String):int");
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f2002R;
    }
}
