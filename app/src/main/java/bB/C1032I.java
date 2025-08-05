package bb;

import com.efiAnalytics.ui.bV;
import java.io.File;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import r.C1798a;
import s.C1818g;

/* renamed from: bb.I, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/I.class */
class C1032I extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ File f7731a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ JDialog f7732b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1028E f7733c;

    C1032I(C1028E c1028e, File file, JDialog jDialog) {
        this.f7733c = c1028e;
        this.f7731a = file;
        this.f7732b = jDialog;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            ae.k kVarA = ae.l.a(this.f7731a);
            if (kVarA == null || !kVarA.a()) {
                bV.d(C1818g.b("The selected file does not appear to be a firmware file."), this.f7732b);
                this.f7733c.f7721f.setSelected(false);
                this.f7733c.f7720e.setText("");
                this.f7733c.f7720e.setEnabled(false);
            } else {
                this.f7733c.f7721f.setSelected(true);
                this.f7733c.f7720e.setText(this.f7731a.getAbsolutePath());
                this.f7733c.f7720e.setEnabled(true);
                this.f7733c.a(kVarA);
                C1798a.a().b(C1798a.f13297E, this.f7731a.getParentFile().getAbsolutePath());
            }
        } catch (Exception e2) {
            bV.d(C1818g.b("Error") + ":\n" + e2.getLocalizedMessage(), this.f7732b);
            this.f7733c.f7721f.setSelected(false);
            this.f7733c.f7720e.setText("");
            this.f7733c.f7720e.setEnabled(false);
            e2.printStackTrace();
        } catch (ae.x e3) {
            bV.d(C1818g.b(e3.getMessage()), this.f7732b);
            this.f7733c.f7721f.setSelected(false);
            this.f7733c.f7720e.setText("");
            this.f7733c.f7720e.setEnabled(false);
        } catch (IOException e4) {
            bV.d(C1818g.b("Error Reading File.") + "\n" + e4.getLocalizedMessage(), this.f7732b);
            this.f7733c.f7721f.setSelected(false);
            this.f7733c.f7720e.setText("");
            this.f7733c.f7720e.setEnabled(false);
        } catch (NullPointerException e5) {
            bV.d(C1818g.b("Unhandled Error"), this.f7732b);
            this.f7733c.f7721f.setSelected(false);
            this.f7733c.f7720e.setText("");
            this.f7733c.f7720e.setEnabled(false);
            e5.printStackTrace();
        } finally {
            SwingUtilities.invokeLater(new RunnableC1033J(this));
        }
    }
}
