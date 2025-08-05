package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dO.class */
public class dO extends JPanel implements KeyListener, Serializable {

    /* renamed from: a, reason: collision with root package name */
    JProgressBar f11352a = new JProgressBar(0, 100);

    /* renamed from: b, reason: collision with root package name */
    JLabel f11353b = new JLabel();

    /* renamed from: g, reason: collision with root package name */
    private boolean f11354g = false;

    /* renamed from: c, reason: collision with root package name */
    Image f11355c = null;

    /* renamed from: d, reason: collision with root package name */
    MouseInputAdapter f11356d = new dP(this);

    /* renamed from: e, reason: collision with root package name */
    JButton f11357e = new JButton();

    /* renamed from: f, reason: collision with root package name */
    int f11358f = 550;

    public dO() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        this.f11352a.setIndeterminate(true);
        this.f11352a.setStringPainted(true);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.gray), BorderFactory.createEmptyBorder(10, 10, 0, 10)));
        jPanel.setLayout(new BorderLayout(10, 10));
        jPanel.add("North", this.f11352a);
        jPanel.add(BorderLayout.CENTER, this.f11353b);
        jPanel.add("East", this.f11357e);
        this.f11357e.setVisible(false);
        add(jPanel, new GridBagConstraints());
        this.f11353b.setMinimumSize(eJ.a(this.f11358f, 20));
        this.f11353b.setPreferredSize(eJ.a(this.f11358f, 20));
    }

    public void a(int i2) {
        this.f11358f = i2;
        this.f11353b.setMinimumSize(eJ.a(i2, 20));
        this.f11353b.setPreferredSize(eJ.a(i2, 20));
    }

    public void a(String str, ActionListener actionListener) {
        this.f11357e.setVisible(true);
        this.f11357e.setText(str);
        this.f11357e.addActionListener(actionListener);
        validate();
    }

    public void a() {
        this.f11357e.setVisible(false);
    }

    public void a(boolean z2) {
        this.f11352a.setVisible(z2);
        if (z2) {
            this.f11353b.setMinimumSize(eJ.a(this.f11358f, 20));
            this.f11353b.setPreferredSize(eJ.a(this.f11358f, 20));
            this.f11353b.setFont(UIManager.getFont("Label.font"));
        } else {
            this.f11353b.setMinimumSize(eJ.a(this.f11358f, 40));
            this.f11353b.setPreferredSize(eJ.a(this.f11358f, 40));
            this.f11353b.setFont(new Font("Arial Unicode MS", 1, eJ.a(26)));
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f11354g) {
            graphics.drawImage(c(), 0, 0, this);
        }
        super.paint(graphics);
    }

    private Image c() {
        if (this.f11355c == null || this.f11355c.getWidth(this) != getWidth() || this.f11355c.getHeight(this) != getHeight()) {
            this.f11355c = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight(), 3);
            Graphics2D graphics2D = (Graphics2D) this.f11355c.getGraphics();
            graphics2D.setColor(new Color(64, 64, 64, 96));
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
        }
        return this.f11355c;
    }

    public void a(double d2) {
        if (d2 < 0.0d || d2 > 100.0d) {
            this.f11352a.setValue(0);
            this.f11352a.setIndeterminate(true);
        } else {
            this.f11352a.setIndeterminate(false);
            this.f11352a.setValue((int) (d2 * 100.0d));
        }
    }

    public void a(String str) {
        this.f11353b.setText(str);
    }

    public void b(boolean z2) {
        this.f11354g = z2;
        if (z2) {
            addMouseListener(this.f11356d);
            addMouseMotionListener(this.f11356d);
            addKeyListener(this);
        } else {
            removeMouseListener(this.f11356d);
            removeMouseMotionListener(this.f11356d);
            removeKeyListener(this);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setVisible(boolean z2) {
        super.setVisible(z2);
        d();
    }

    private void d() {
        int iA = eJ.a(110);
        if (isShowing() && getWidth() > 0 && getWidth() < eJ.a(this.f11358f) + iA) {
            int width = getWidth() - iA;
            this.f11353b.setMinimumSize(eJ.a(width, 20));
            this.f11353b.setPreferredSize(eJ.a(width, 20));
            doLayout();
            return;
        }
        if (this.f11353b.getWidth() != this.f11358f) {
            this.f11353b.setMinimumSize(eJ.a(this.f11358f, 20));
            this.f11353b.setPreferredSize(eJ.a(this.f11358f, 20));
            doLayout();
        }
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        keyEvent.consume();
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        keyEvent.consume();
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        keyEvent.consume();
    }

    public void b() {
        this.f11352a.setValue(0);
        this.f11352a.setIndeterminate(true);
    }
}
