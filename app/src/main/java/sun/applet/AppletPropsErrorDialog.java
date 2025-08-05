package sun.applet;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;

/* compiled from: AppletProps.java */
/* loaded from: rt.jar:sun/applet/AppletPropsErrorDialog.class */
class AppletPropsErrorDialog extends Dialog {
    public AppletPropsErrorDialog(Frame frame, String str, String str2, String str3) {
        super(frame, str, true);
        Panel panel = new Panel();
        add(BorderLayout.CENTER, new Label(str2));
        panel.add(new Button(str3));
        add("South", panel);
        pack();
        Dimension size = size();
        Rectangle rectangleBounds = frame.bounds();
        move(rectangleBounds.f12372x + ((rectangleBounds.width - size.width) / 2), rectangleBounds.f12373y + ((rectangleBounds.height - size.height) / 2));
    }

    @Override // java.awt.Component
    public boolean action(Event event, Object obj) {
        hide();
        dispose();
        return true;
    }
}
