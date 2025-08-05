package ao;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: ao.H, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/H.class */
public class C0590H extends JPanel implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    JSlider f5084a = new JSlider();

    /* renamed from: b, reason: collision with root package name */
    JLabel f5085b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    List f5086c = new ArrayList();

    public C0590H() {
        this.f5084a.setOrientation(0);
        this.f5084a.setMinimum(-75);
        this.f5084a.setMaximum(75);
        this.f5084a.setPaintLabels(true);
        this.f5084a.setMinorTickSpacing(5);
        this.f5084a.setPaintTicks(true);
        this.f5084a.addChangeListener(this);
        setLayout(new BorderLayout(5, 5));
        add(BorderLayout.CENTER, this.f5084a);
        add("East", this.f5085b);
    }

    public void a(ChangeListener changeListener) {
        this.f5086c.add(changeListener);
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        this.f5085b.setText(Integer.toString(this.f5084a.getValue()));
        Iterator it = this.f5086c.iterator();
        while (it.hasNext()) {
            ((ChangeListener) it.next()).stateChanged(changeEvent);
        }
    }

    public void a(int i2) {
        this.f5084a.setValue(i2);
    }
}
