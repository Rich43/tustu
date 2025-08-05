package ao;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;

/* renamed from: ao.an, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/an.class */
class C0623an extends JMenuItem {

    /* renamed from: b, reason: collision with root package name */
    private Color f5225b;

    /* renamed from: c, reason: collision with root package name */
    private int f5226c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0620ak f5227a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0623an(C0620ak c0620ak, Color color, int i2) {
        super(GoToActionDialog.EMPTY_DESTINATION);
        this.f5227a = c0620ak;
        this.f5225b = color;
        this.f5226c = i2;
        super.setBackground(color);
        super.setForeground(color);
        super.setRolloverEnabled(false);
    }

    @Override // javax.swing.AbstractButton
    public void addActionListener(ActionListener actionListener) {
        super.addActionListener(actionListener);
    }

    public Color a() {
        return this.f5225b;
    }

    public int b() {
        return this.f5226c;
    }
}
