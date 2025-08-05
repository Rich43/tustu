package sun.applet;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/applet/AppletProps.class */
class AppletProps extends Frame {
    TextField proxyHost;
    TextField proxyPort;
    Choice accessMode;
    private static AppletMessageHandler amh = new AppletMessageHandler("appletprops");

    AppletProps() {
        setTitle(amh.getMessage("title"));
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(0, 2));
        panel.add(new Label(amh.getMessage("label.http.server", "Http proxy server:")));
        TextField textField = new TextField();
        this.proxyHost = textField;
        panel.add(textField);
        panel.add(new Label(amh.getMessage("label.http.proxy")));
        TextField textField2 = new TextField();
        this.proxyPort = textField2;
        panel.add(textField2);
        panel.add(new Label(amh.getMessage("label.class")));
        Choice choice = new Choice();
        this.accessMode = choice;
        panel.add(choice);
        this.accessMode.addItem(amh.getMessage("choice.class.item.restricted"));
        this.accessMode.addItem(amh.getMessage("choice.class.item.unrestricted"));
        add(BorderLayout.CENTER, panel);
        Panel panel2 = new Panel();
        panel2.add(new Button(amh.getMessage("button.apply")));
        panel2.add(new Button(amh.getMessage("button.reset")));
        panel2.add(new Button(amh.getMessage("button.cancel")));
        add("South", panel2);
        move(200, 150);
        pack();
        reset();
    }

    void reset() {
        AppletSecurity appletSecurity = (AppletSecurity) System.getSecurityManager();
        if (appletSecurity != null) {
            appletSecurity.reset();
        }
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("http.proxyHost"));
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("http.proxyPort"));
        if (((Boolean) AccessController.doPrivileged(new GetBooleanAction("package.restrict.access.sun"))).booleanValue()) {
            this.accessMode.select(amh.getMessage("choice.class.item.restricted"));
        } else {
            this.accessMode.select(amh.getMessage("choice.class.item.unrestricted"));
        }
        if (str != null) {
            this.proxyHost.setText(str);
            this.proxyPort.setText(str2);
        } else {
            this.proxyHost.setText("");
            this.proxyPort.setText("");
        }
    }

    void apply() {
        String strTrim = this.proxyHost.getText().trim();
        String strTrim2 = this.proxyPort.getText().trim();
        final Properties properties = (Properties) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletProps.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return System.getProperties();
            }
        });
        if (strTrim.length() != 0) {
            int i2 = 0;
            try {
                i2 = Integer.parseInt(strTrim2);
            } catch (NumberFormatException e2) {
            }
            if (i2 <= 0) {
                this.proxyPort.selectAll();
                this.proxyPort.requestFocus();
                new AppletPropsErrorDialog(this, amh.getMessage("title.invalidproxy"), amh.getMessage("label.invalidproxy"), amh.getMessage("button.ok")).show();
                return;
            }
            properties.put("http.proxyHost", strTrim);
            properties.put("http.proxyPort", strTrim2);
        } else {
            properties.put("http.proxyHost", "");
        }
        if (amh.getMessage("choice.class.item.restricted").equals(this.accessMode.getSelectedItem())) {
            properties.put("package.restrict.access.sun", "true");
        } else {
            properties.put("package.restrict.access.sun", "false");
        }
        try {
            reset();
            AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: sun.applet.AppletProps.2
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    FileOutputStream fileOutputStream = new FileOutputStream(Main.theUserPropertiesFile);
                    Properties properties2 = new Properties();
                    for (int i3 = 0; i3 < Main.avDefaultUserProps.length; i3++) {
                        String str = Main.avDefaultUserProps[i3][0];
                        properties2.setProperty(str, properties.getProperty(str));
                    }
                    properties2.store(fileOutputStream, AppletProps.amh.getMessage("prop.store"));
                    fileOutputStream.close();
                    return null;
                }
            });
            hide();
        } catch (PrivilegedActionException e3) {
            System.out.println(amh.getMessage("apply.exception", e3.getException()));
            e3.printStackTrace();
            reset();
        }
    }

    @Override // java.awt.Component
    public boolean action(Event event, Object obj) {
        if (amh.getMessage("button.apply").equals(obj)) {
            apply();
            return true;
        }
        if (amh.getMessage("button.reset").equals(obj)) {
            reset();
            return true;
        }
        if (amh.getMessage("button.cancel").equals(obj)) {
            reset();
            hide();
            return true;
        }
        return false;
    }
}
