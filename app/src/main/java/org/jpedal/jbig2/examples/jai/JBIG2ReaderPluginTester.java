package org.jpedal.jbig2.examples.jai;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/examples/jai/JBIG2ReaderPluginTester.class */
public class JBIG2ReaderPluginTester extends JFrame {
    static final String appTitle = "JBIG2 Reader Plug-in Tester";
    static final int FORMAT_NAME = 0;
    static final int INPUT = 1;
    static final int MIME_TYPE = 2;
    static final int SUFFIX = 3;
    BufferedImage biImage;
    int dstOffX;
    int dstOffY;
    int height;
    int width;
    int srcX;
    int srcY;
    int srcWidth;
    int srcHeight;
    int xSS;
    int ySS;
    JLabel lblStatus;
    int method;
    PicPanel pp;
    JScrollPane jsp;

    public JBIG2ReaderPluginTester(String title) {
        super(title);
        this.srcHeight = 1;
        this.xSS = 1;
        this.ySS = 1;
        this.method = 0;
        setDefaultCloseOperation(3);
        final JFileChooser fcOpen = new JFileChooser();
        fcOpen.setCurrentDirectory(new File(System.getProperty("user.dir")));
        JMenuBar mb = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem miOpen = new JMenuItem("Open...");
        ActionListener openl = new ActionListener() { // from class: org.jpedal.jbig2.examples.jai.JBIG2ReaderPluginTester.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) throws IllegalArgumentException {
                fcOpen.setSelectedFile(null);
                if (fcOpen.showOpenDialog(JBIG2ReaderPluginTester.this) != 0 || !JBIG2ReaderPluginTester.this.doOpen(fcOpen.getSelectedFile())) {
                    return;
                }
                JBIG2ReaderPluginTester.this.lblStatus.setText("Width: " + JBIG2ReaderPluginTester.this.width + ", Height: " + JBIG2ReaderPluginTester.this.height + ", File: " + fcOpen.getSelectedFile().getAbsolutePath());
                JBIG2ReaderPluginTester.this.pp.setBufferedImage(JBIG2ReaderPluginTester.this.biImage);
                JBIG2ReaderPluginTester.this.jsp.getHorizontalScrollBar().setValue(0);
                JBIG2ReaderPluginTester.this.jsp.getVerticalScrollBar().setValue(0);
            }
        };
        miOpen.addActionListener(openl);
        menuFile.add(miOpen);
        JMenuItem miConfigure = new JMenuItem("Configure...");
        ActionListener cfgl = new ActionListener() { // from class: org.jpedal.jbig2.examples.jai.JBIG2ReaderPluginTester.2
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) {
                CfgDialog cfgdlg = new CfgDialog(JBIG2ReaderPluginTester.this, JBIG2ReaderPluginTester.this.dstOffX, JBIG2ReaderPluginTester.this.dstOffY, JBIG2ReaderPluginTester.this.method, JBIG2ReaderPluginTester.this.srcX, JBIG2ReaderPluginTester.this.srcY, JBIG2ReaderPluginTester.this.srcWidth, JBIG2ReaderPluginTester.this.srcHeight, JBIG2ReaderPluginTester.this.xSS, JBIG2ReaderPluginTester.this.ySS);
                cfgdlg.setVisible(true);
                if (cfgdlg.isCanceled()) {
                    return;
                }
                JBIG2ReaderPluginTester.this.dstOffX = cfgdlg.getDstOffX();
                JBIG2ReaderPluginTester.this.dstOffY = cfgdlg.getDstOffY();
                JBIG2ReaderPluginTester.this.method = cfgdlg.getMethod();
                JBIG2ReaderPluginTester.this.srcX = cfgdlg.getSrcX();
                JBIG2ReaderPluginTester.this.srcY = cfgdlg.getSrcY();
                JBIG2ReaderPluginTester.this.srcWidth = cfgdlg.getSrcWidth();
                JBIG2ReaderPluginTester.this.srcHeight = cfgdlg.getSrcHeight();
                JBIG2ReaderPluginTester.this.xSS = cfgdlg.getXSS();
                JBIG2ReaderPluginTester.this.ySS = cfgdlg.getYSS();
            }
        };
        miConfigure.addActionListener(cfgl);
        menuFile.add(miConfigure);
        menuFile.addSeparator();
        JMenuItem miExit = new JMenuItem(ToolWindow.QUIT);
        ActionListener exitl = new ActionListener() { // from class: org.jpedal.jbig2.examples.jai.JBIG2ReaderPluginTester.3
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) {
                System.exit(0);
            }
        };
        miExit.addActionListener(exitl);
        menuFile.add(miExit);
        mb.add(menuFile);
        setJMenuBar(mb);
        this.pp = new PicPanel(null);
        Container contentPane = getContentPane();
        JScrollPane jScrollPane = new JScrollPane(this.pp);
        this.jsp = jScrollPane;
        contentPane.add(jScrollPane);
        this.lblStatus = new JLabel(" ");
        this.lblStatus.setBorder(BorderFactory.createEtchedBorder());
        getContentPane().add(this.lblStatus, "South");
        pack();
        setVisible(true);
    }

    boolean doOpen(File file) throws HeadlessException {
        Iterator iter;
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File does not exist!", appTitle, 0);
            return false;
        }
        try {
            String path = file.getAbsolutePath().toLowerCase();
            if (!path.endsWith(".jbig2") && !path.endsWith(".jb2")) {
                JOptionPane.showMessageDialog(this, "Incorrect file extension!", appTitle, 0);
                return false;
            }
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            if (this.method == 0) {
                iter = ImageIO.getImageReadersByFormatName("jbig2");
            } else if (this.method == 2) {
                iter = ImageIO.getImageReadersByMIMEType("image/x-jbig2");
            } else if (this.method == 3) {
                iter = ImageIO.getImageReadersBySuffix("jbig2");
            } else {
                iter = ImageIO.getImageReaders(iis);
            }
            if (!iter.hasNext()) {
                JOptionPane.showMessageDialog(this, "Unable to obtain reader!", appTitle, 0);
                return false;
            }
            ImageReader reader = iter.next();
            reader.setInput(iis, true);
            ImageReadParam irp = reader.getDefaultReadParam();
            if (this.dstOffX != 0 || this.dstOffY != 0) {
                irp.setDestinationOffset(new Point(this.dstOffX, this.dstOffY));
            }
            if (this.srcWidth != 0) {
                irp.setSourceRegion(new Rectangle(this.srcX, this.srcY, this.srcWidth, this.srcHeight));
            }
            if (this.xSS != 1 || this.ySS != 1) {
                irp.setSourceSubsampling(this.xSS, this.ySS, 0, 0);
            }
            this.biImage = reader.read(0, irp);
            this.width = reader.getWidth(0);
            this.height = reader.getHeight(0);
            reader.dispose();
            return true;
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(this, e2.getMessage(), appTitle, 0);
            return false;
        }
    }

    public static void main(String[] args) {
        new JBIG2ReaderPluginTester(appTitle);
    }
}
