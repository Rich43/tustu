package bf;

import G.Q;
import javax.swing.tree.DefaultMutableTreeNode;
import s.C1818g;

/* renamed from: bf.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bf/j.class */
public class C1117j extends DefaultMutableTreeNode {

    /* renamed from: a, reason: collision with root package name */
    private Q f8057a;

    public C1117j(Q q2) {
        super(q2);
        this.f8057a = q2;
    }

    public C1117j(String str) {
        super(C1818g.b(str));
    }

    @Override // javax.swing.tree.DefaultMutableTreeNode
    public String toString() {
        return a() != null ? a().aJ() : super.toString();
    }

    public Q a() {
        return this.f8057a;
    }
}
