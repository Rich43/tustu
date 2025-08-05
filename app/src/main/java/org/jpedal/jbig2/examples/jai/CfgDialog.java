package org.jpedal.jbig2.examples.jai;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

/* compiled from: JBIG2ReaderPluginTester.java */
/* loaded from: icepdf-core.jar:org/jpedal/jbig2/examples/jai/CfgDialog.class */
class CfgDialog extends JDialog {
    private static final int MAX_DSTOFFX = 9999;
    private static final int MAX_DSTOFFY = 9999;
    private static final int MAX_XSS = 9999;
    private static final int MAX_YSS = 9999;
    private static final int MAX_SRCX = 9999;
    private static final int MAX_SRCY = 9999;
    private static final int MAX_SRCWIDTH = 9999;
    private static final int MAX_SRCHEIGHT = 9999;
    private boolean canceled;
    private int dstOffX;
    private int dstOffY;
    private int srcHeight;
    private int srcWidth;
    private int srcX;
    private int srcY;
    private int xSS;
    private int ySS;
    private int method;

    CfgDialog(JFrame f2, int dstOffX, int dstOffY, int method, int srcX, int srcY, int srcWidth, int srcHeight, int xSS, int ySS) {
        super((Frame) f2, "Configure", true);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(5, 1));
        JPanel pnl = new JPanel();
        Border bd2 = BorderFactory.createEtchedBorder(1);
        pnl.setBorder(BorderFactory.createTitledBorder(bd2, "Destination Offset"));
        pnl.add(new JLabel("X"));
        final JSpinner spnDstOffX = new JSpinner(new SpinnerNumberModel(dstOffX, 0, 9999, 1));
        pnl.add(spnDstOffX);
        pnl.add(new JLabel(Constants._TAG_Y));
        final JSpinner spnDstOffY = new JSpinner(new SpinnerNumberModel(dstOffY, 0, 9999, 1));
        pnl.add(spnDstOffY);
        jPanel.add(pnl);
        JPanel pnl2 = new JPanel();
        Border bd3 = BorderFactory.createEtchedBorder(1);
        pnl2.setBorder(BorderFactory.createTitledBorder(bd3, "Method"));
        final JRadioButton rbChoice1 = new JRadioButton("Format name");
        if (method == 0) {
            rbChoice1.setSelected(true);
        }
        pnl2.add(rbChoice1);
        final JRadioButton rbChoice2 = new JRadioButton("Input");
        if (method == 1) {
            rbChoice2.setSelected(true);
        }
        pnl2.add(rbChoice2);
        final JRadioButton rbChoice3 = new JRadioButton("MIME type");
        if (method == 2) {
            rbChoice3.setSelected(true);
        }
        pnl2.add(rbChoice3);
        JRadioButton rbChoice4 = new JRadioButton("Suffix");
        if (method == 3) {
            rbChoice4.setSelected(true);
        }
        pnl2.add(rbChoice4);
        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(rbChoice1);
        bg2.add(rbChoice2);
        bg2.add(rbChoice3);
        bg2.add(rbChoice4);
        jPanel.add(pnl2);
        JPanel pnl3 = new JPanel();
        Border bd4 = BorderFactory.createEtchedBorder(1);
        pnl3.setBorder(BorderFactory.createTitledBorder(bd4, "Source Region"));
        pnl3.add(new JLabel("Src X"));
        final JSpinner spnSrcX = new JSpinner(new SpinnerNumberModel(srcX, 0, 9999, 1));
        pnl3.add(spnSrcX);
        pnl3.add(new JLabel("Src Y"));
        final JSpinner spnSrcY = new JSpinner(new SpinnerNumberModel(srcY, 0, 9999, 1));
        pnl3.add(spnSrcY);
        pnl3.add(new JLabel("Src Width"));
        final JSpinner spnSrcWidth = new JSpinner(new SpinnerNumberModel(srcWidth, 0, 9999, 1));
        pnl3.add(spnSrcWidth);
        pnl3.add(new JLabel("Src Height"));
        final JSpinner spnSrcHeight = new JSpinner(new SpinnerNumberModel(srcHeight, 1, 9999, 1));
        pnl3.add(spnSrcHeight);
        jPanel.add(pnl3);
        JPanel pnl4 = new JPanel();
        Border bd5 = BorderFactory.createEtchedBorder(1);
        pnl4.setBorder(BorderFactory.createTitledBorder(bd5, "Source Subsampling"));
        pnl4.add(new JLabel("X Subsampling"));
        final JSpinner spnXSS = new JSpinner(new SpinnerNumberModel(xSS, 1, 9999, 1));
        pnl4.add(spnXSS);
        pnl4.add(new JLabel("Y Subsampling"));
        final JSpinner spnYSS = new JSpinner(new SpinnerNumberModel(ySS, 1, 9999, 1));
        pnl4.add(spnYSS);
        jPanel.add(pnl4);
        JPanel pnl5 = new JPanel();
        pnl5.setLayout(new FlowLayout(2));
        JButton btn = new JButton("OK");
        pnl5.add(btn);
        btn.addActionListener(new ActionListener() { // from class: org.jpedal.jbig2.examples.jai.CfgDialog.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) {
                CfgDialog.this.canceled = false;
                if (rbChoice1.isSelected()) {
                    CfgDialog.this.method = 0;
                } else if (rbChoice2.isSelected()) {
                    CfgDialog.this.method = 1;
                } else if (rbChoice3.isSelected()) {
                    CfgDialog.this.method = 2;
                } else {
                    CfgDialog.this.method = 3;
                }
                CfgDialog.this.dstOffX = ((Integer) spnDstOffX.getValue()).intValue();
                CfgDialog.this.dstOffY = ((Integer) spnDstOffY.getValue()).intValue();
                CfgDialog.this.xSS = ((Integer) spnXSS.getValue()).intValue();
                CfgDialog.this.ySS = ((Integer) spnYSS.getValue()).intValue();
                CfgDialog.this.srcX = ((Integer) spnSrcX.getValue()).intValue();
                CfgDialog.this.srcY = ((Integer) spnSrcY.getValue()).intValue();
                CfgDialog.this.srcWidth = ((Integer) spnSrcWidth.getValue()).intValue();
                CfgDialog.this.srcHeight = ((Integer) spnSrcHeight.getValue()).intValue();
                CfgDialog.this.dispose();
            }
        });
        JButton btn2 = new JButton("Cancel");
        pnl5.add(btn2);
        btn2.addActionListener(new ActionListener() { // from class: org.jpedal.jbig2.examples.jai.CfgDialog.2
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) {
                CfgDialog.this.canceled = true;
                CfgDialog.this.dispose();
            }
        });
        jPanel.add(pnl5);
        getContentPane().add(jPanel);
        pack();
    }

    int getDstOffX() {
        return this.dstOffX;
    }

    int getDstOffY() {
        return this.dstOffY;
    }

    int getMethod() {
        return this.method;
    }

    int getSrcHeight() {
        return this.srcHeight;
    }

    int getSrcWidth() {
        return this.srcWidth;
    }

    int getSrcX() {
        return this.srcX;
    }

    int getSrcY() {
        return this.srcY;
    }

    int getXSS() {
        return this.xSS;
    }

    int getYSS() {
        return this.ySS;
    }

    boolean isCanceled() {
        return this.canceled;
    }
}
