package aP;

import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/jx.class */
class jx implements G.aG {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ jv f3816a;

    jx(jv jvVar) {
        this.f3816a = jvVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00bd  */
    @Override // G.aG
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean a(java.lang.String r6, G.bS r7) {
        /*
            Method dump skipped, instructions count: 554
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aP.jx.a(java.lang.String, G.bS):boolean");
    }

    @Override // G.aG
    public void a(String str) {
    }

    private boolean a(G.R r2) throws HeadlessException {
        JFrame jFrameC = cZ.a().c();
        String str = C1818g.b("The loaded Project was used last used with a different ECU.") + "\n\n" + C1818g.b("To assist in maintaining calibration restore points and data logs") + " \n" + C1818g.b("a separate project should be used for each ECU you work with.") + "\n\n" + C1818g.b("It is recommended that you use a Project that was created for this car or create a new Project.") + "\n";
        String[] strArr = {C1818g.b("Open another Project"), C1818g.b("Create a new Project"), C1818g.b("Connect anyway")};
        int iShowOptionDialog = JOptionPane.showOptionDialog(jFrameC, str, C1818g.b("Different ECU Detected!"), 1, 2, null, strArr, strArr[0]);
        if (iShowOptionDialog == 0) {
            SwingUtilities.invokeLater(new jy(this, jFrameC));
            return false;
        }
        if (iShowOptionDialog == 1) {
            SwingUtilities.invokeLater(new jz(this, jFrameC));
            return false;
        }
        r2.h().f();
        return true;
    }

    private boolean a(B.i iVar, String str, String str2) {
        JFrame jFrameC = cZ.a().c();
        String str3 = C1818g.b("The loaded Project was used last used with a different ECU.") + "\n\n" + C1818g.b("To assist in maintaining calibration restore points and data logs") + " \n" + C1818g.b("a separate project is used for each ECU you work with.") + "\n\n" + C1818g.b("It is recommended that you use a Project that was created for this ECU.") + "\n";
        String[] strArr = {C1818g.b("Open Project for this ECU"), C1818g.b("Assign this project to this ECU")};
        if (JOptionPane.showOptionDialog(jFrameC, str3, C1818g.b("Different ECU Detected!"), 0, 2, null, strArr, strArr[0]) != 0) {
            return true;
        }
        SwingUtilities.invokeLater(new jA(this, iVar));
        return false;
    }
}
