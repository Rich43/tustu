package org.icepdf.ri.common;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import org.icepdf.core.pobjects.Document;
import org.icepdf.ri.images.Images;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/AboutDialog.class */
public class AboutDialog extends JDialog implements ActionListener, WindowListener {
    private JButton ok;
    private Timer timer;
    private int whichTimer;
    private static final int WAIT_TIME = 3000;
    private static final String IMAGE = "icelogo.png";
    public static final int NO_BUTTONS = 0;
    public static final int OK = 2;
    public static final int NO_TIMER = 0;
    public static final int DISAPPEAR = 4;

    public AboutDialog(Frame frame, ResourceBundle messageBundle, boolean isModal, int buttons, int whichTimer) {
        int c2;
        super(frame, isModal);
        this.whichTimer = whichTimer;
        setTitle(messageBundle.getString("viewer.dialog.about.title"));
        setResizable(false);
        JPanel panelImage = new JPanel();
        ImageIcon icon = new ImageIcon(Images.get(IMAGE));
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder());
        panelImage.add(iconLabel);
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, 1));
        panel1.add(Box.createVerticalStrut(10));
        panel1.add(panelImage);
        panel1.add(Box.createVerticalStrut(30));
        JLabel label = new JLabel(Document.getLibraryVersion());
        label.setAlignmentX(0.5f);
        panel1.add(label);
        String text = messageBundle.getString("viewer.dialog.about.pageNumber.label");
        int i2 = 0;
        while (true) {
            c2 = i2;
            int c1 = text.indexOf("\n", c2);
            if (c1 <= -1) {
                break;
            }
            panel1.add(Box.createVerticalStrut(10));
            JLabel label2 = new JLabel(text.substring(c2, c1));
            label2.setAlignmentX(0.5f);
            panel1.add(label2);
            i2 = c1 + 1;
        }
        panel1.add(Box.createVerticalStrut(10));
        JLabel label3 = new JLabel(text.substring(c2, text.length()));
        label3.setAlignmentX(0.5f);
        panel1.add(label3);
        JPanel pane = new JPanel();
        pane.setBorder(new EmptyBorder(5, 15, 5, 15));
        pane.setLayout(new BorderLayout());
        pane.add(panel1);
        if (2 > 0) {
            JPanel panel2 = new JPanel();
            panel2.setLayout(new FlowLayout());
            if ((2 & 2) > 0) {
                this.ok = new JButton(messageBundle.getString("viewer.button.ok.label"));
                this.ok.addActionListener(this);
                if (0 > 0) {
                    this.ok.setEnabled(false);
                }
                panel2.add(this.ok);
            }
            pane.add(panel2, "South");
        }
        setContentPane(pane);
        pack();
        setLocationRelativeTo(frame);
        if (0 > 0) {
            this.timer = new Timer(3000, this);
            this.timer.start();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == this.timer) {
            this.timer.stop();
            if (this.whichTimer == 2) {
                this.ok.setEnabled(true);
                return;
            } else {
                if (this.whichTimer == 4) {
                    setVisible(false);
                    dispose();
                    return;
                }
                return;
            }
        }
        if (ev.getSource() == this.ok) {
            setVisible(false);
            dispose();
        }
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent ev) {
        if (this.ok.isEnabled()) {
            setVisible(false);
            dispose();
        }
    }

    @Override // java.awt.event.WindowListener
    public void windowActivated(WindowEvent ev) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(WindowEvent ev) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent ev) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeiconified(WindowEvent ev) {
    }

    @Override // java.awt.event.WindowListener
    public void windowIconified(WindowEvent ev) {
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent ev) {
    }
}
