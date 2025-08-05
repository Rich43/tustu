package ao;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

/* loaded from: TunerStudioMS.jar:ao/cM.class */
class cM implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JMenu f5468a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bP f5469b;

    cM(bP bPVar, JMenu jMenu) {
        this.f5469b = bPVar;
        this.f5468a = jMenu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) itemEvent.getSource();
        if (!this.f5469b.c(jCheckBoxMenuItem.getName(), jCheckBoxMenuItem.getActionCommand(), itemEvent.getStateChange() == 1)) {
            jCheckBoxMenuItem.setState(false);
        }
        Map<TextAttribute, ?> attributes = this.f5468a.getFont().getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, Boolean.valueOf(!jCheckBoxMenuItem.getState()));
        this.f5468a.setFont(new Font(attributes));
    }
}
