package com.efiAnalytics.ui;

import com.sun.javafx.application.PlatformImpl;
import java.awt.BorderLayout;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eD.class */
public class eD extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    private Stage f11474a;

    /* renamed from: b, reason: collision with root package name */
    private WebView f11475b;

    /* renamed from: c, reason: collision with root package name */
    private JFXPanel f11476c;

    /* renamed from: d, reason: collision with root package name */
    private JButton f11477d;

    /* renamed from: e, reason: collision with root package name */
    private WebEngine f11478e;

    public eD() {
        a();
    }

    private void a() {
        this.f11476c = new JFXPanel();
        b();
        setLayout(new BorderLayout());
        add(this.f11476c, BorderLayout.CENTER);
        this.f11477d = new JButton();
        this.f11477d.addActionListener(new eE(this));
        this.f11477d.setText("Reload");
        this.f11476c.addComponentListener(new eG(this));
    }

    private void b() {
        PlatformImpl.startup(new eI(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        int width = getParent().getWidth();
        int height = getParent().getHeight();
        this.f11475b.setMinSize(width, height);
        this.f11475b.setPrefSize(width, height);
    }
}
