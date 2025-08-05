package sun.applet;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* compiled from: AppletViewer.java */
/* loaded from: rt.jar:sun/applet/TextFrame.class */
final class TextFrame extends Frame {
    private static AppletMessageHandler amh = new AppletMessageHandler("textframe");

    TextFrame(int i2, int i3, String str, String str2) {
        setTitle(str);
        TextArea textArea = new TextArea(20, 60);
        textArea.setText(str2);
        textArea.setEditable(false);
        add(BorderLayout.CENTER, textArea);
        Panel panel = new Panel();
        add("South", panel);
        Button button = new Button(amh.getMessage("button.dismiss", "Dismiss"));
        panel.add(button);
        button.addActionListener(new ActionListener() { // from class: sun.applet.TextFrame.1ActionEventListener
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                TextFrame.this.dispose();
            }
        });
        pack();
        move(i2, i3);
        setVisible(true);
        addWindowListener(new WindowAdapter() { // from class: sun.applet.TextFrame.1
            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosing(WindowEvent windowEvent) {
                TextFrame.this.dispose();
            }
        });
    }
}
