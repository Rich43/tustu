package sun.applet;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.jar.Pack200;
import javax.print.attribute.HashPrintRequestAttributeSet;
import org.icepdf.core.util.PdfOps;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.misc.Ref;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/applet/AppletViewer.class */
public class AppletViewer extends Frame implements AppletContext, Printable {
    AppletViewerPanel panel;
    Label label;
    PrintStream statusMsgStream;
    AppletViewerFactory factory;
    static AppletProps props;

    /* renamed from: c, reason: collision with root package name */
    static int f13536c;

    /* renamed from: x, reason: collision with root package name */
    private static int f13537x;

    /* renamed from: y, reason: collision with root package name */
    private static int f13538y;
    private static final int XDELTA = 30;
    private static final int YDELTA = 30;
    static String encoding;
    private static AppletMessageHandler amh;
    private static String defaultSaveFile = "Applet.ser";
    private static Map audioClips = new HashMap();
    private static Map imageRefs = new HashMap();
    static Vector appletPanels = new Vector();
    static Hashtable systemParam = new Hashtable();

    static {
        systemParam.put(Constants.ATTRNAME_CODEBASE, Constants.ATTRNAME_CODEBASE);
        systemParam.put("code", "code");
        systemParam.put("alt", "alt");
        systemParam.put(MetadataParser.WIDTH_TAG_NAME, MetadataParser.WIDTH_TAG_NAME);
        systemParam.put(MetadataParser.HEIGHT_TAG_NAME, MetadataParser.HEIGHT_TAG_NAME);
        systemParam.put("align", "align");
        systemParam.put("vspace", "vspace");
        systemParam.put("hspace", "hspace");
        f13537x = 0;
        f13538y = 0;
        encoding = null;
        amh = new AppletMessageHandler("appletviewer");
    }

    /* loaded from: rt.jar:sun/applet/AppletViewer$UserActionListener.class */
    private final class UserActionListener implements ActionListener {
        private UserActionListener() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            AppletViewer.this.processUserAction(actionEvent);
        }
    }

    public AppletViewer(int i2, int i3, URL url, Hashtable hashtable, PrintStream printStream, AppletViewerFactory appletViewerFactory) {
        this.factory = appletViewerFactory;
        this.statusMsgStream = printStream;
        setTitle(amh.getMessage("tool.title", hashtable.get("code")));
        MenuBar baseMenuBar = appletViewerFactory.getBaseMenuBar();
        Menu menu = new Menu(amh.getMessage("menu.applet"));
        addMenuItem(menu, "menuitem.restart");
        addMenuItem(menu, "menuitem.reload");
        addMenuItem(menu, "menuitem.stop");
        addMenuItem(menu, "menuitem.save");
        addMenuItem(menu, "menuitem.start");
        addMenuItem(menu, "menuitem.clone");
        menu.add(new MenuItem(LanguageTag.SEP));
        addMenuItem(menu, "menuitem.tag");
        addMenuItem(menu, "menuitem.info");
        addMenuItem(menu, "menuitem.edit").disable();
        addMenuItem(menu, "menuitem.encoding");
        menu.add(new MenuItem(LanguageTag.SEP));
        addMenuItem(menu, "menuitem.print");
        menu.add(new MenuItem(LanguageTag.SEP));
        addMenuItem(menu, "menuitem.props");
        menu.add(new MenuItem(LanguageTag.SEP));
        addMenuItem(menu, "menuitem.close");
        if (appletViewerFactory.isStandalone()) {
            addMenuItem(menu, "menuitem.quit");
        }
        baseMenuBar.add(menu);
        setMenuBar(baseMenuBar);
        AppletViewerPanel appletViewerPanel = new AppletViewerPanel(url, hashtable);
        this.panel = appletViewerPanel;
        add(BorderLayout.CENTER, appletViewerPanel);
        Label label = new Label(amh.getMessage("label.hello"));
        this.label = label;
        add("South", label);
        this.panel.init();
        appletPanels.addElement(this.panel);
        pack();
        move(i2, i3);
        setVisible(true);
        addWindowListener(new WindowAdapter() { // from class: sun.applet.AppletViewer.1
            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosing(WindowEvent windowEvent) {
                AppletViewer.this.appletClose();
            }

            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowIconified(WindowEvent windowEvent) {
                AppletViewer.this.appletStop();
            }

            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowDeiconified(WindowEvent windowEvent) {
                AppletViewer.this.appletStart();
            }
        });
        this.panel.addAppletListener(new AppletListener(this) { // from class: sun.applet.AppletViewer.1AppletEventListener
            final Frame frame;

            {
                this.frame = this;
            }

            @Override // sun.applet.AppletListener
            public void appletStateChanged(AppletEvent appletEvent) {
                AppletPanel appletPanel = (AppletPanel) appletEvent.getSource();
                switch (appletEvent.getID()) {
                    case AppletPanel.APPLET_RESIZE /* 51234 */:
                        if (appletPanel != null) {
                            AppletViewer.this.resize(AppletViewer.this.preferredSize());
                            AppletViewer.this.validate();
                            break;
                        }
                        break;
                    case AppletPanel.APPLET_LOADING_COMPLETED /* 51236 */:
                        Applet applet = appletPanel.getApplet();
                        if (applet != null) {
                            AppletPanel.changeFrameAppContext(this.frame, SunToolkit.targetToAppContext(applet));
                            break;
                        } else {
                            AppletPanel.changeFrameAppContext(this.frame, AppContext.getAppContext());
                            break;
                        }
                }
            }
        });
        showStatus(amh.getMessage("status.start"));
        initEventQueue();
    }

    public MenuItem addMenuItem(Menu menu, String str) {
        MenuItem menuItem = new MenuItem(amh.getMessage(str));
        menuItem.addActionListener(new UserActionListener());
        return menu.add(menuItem);
    }

    private void initEventQueue() {
        String property = System.getProperty("appletviewer.send.event");
        if (property == null) {
            this.panel.sendEvent(1);
            this.panel.sendEvent(2);
            this.panel.sendEvent(3);
            return;
        }
        String[] strArrSplitSeparator = splitSeparator(",", property);
        for (int i2 = 0; i2 < strArrSplitSeparator.length; i2++) {
            System.out.println("Adding event to queue: " + strArrSplitSeparator[i2]);
            if (strArrSplitSeparator[i2].equals("dispose")) {
                this.panel.sendEvent(0);
            } else if (strArrSplitSeparator[i2].equals("load")) {
                this.panel.sendEvent(1);
            } else if (strArrSplitSeparator[i2].equals("init")) {
                this.panel.sendEvent(2);
            } else if (strArrSplitSeparator[i2].equals("start")) {
                this.panel.sendEvent(3);
            } else if (strArrSplitSeparator[i2].equals("stop")) {
                this.panel.sendEvent(4);
            } else if (strArrSplitSeparator[i2].equals("destroy")) {
                this.panel.sendEvent(5);
            } else if (strArrSplitSeparator[i2].equals("quit")) {
                this.panel.sendEvent(6);
            } else if (strArrSplitSeparator[i2].equals(Pack200.Packer.ERROR)) {
                this.panel.sendEvent(7);
            } else {
                System.out.println("Unrecognized event name: " + strArrSplitSeparator[i2]);
            }
        }
        while (!this.panel.emptyEventQueue()) {
        }
        appletSystemExit();
    }

    private String[] splitSeparator(String str, String str2) {
        Vector vector = new Vector();
        int i2 = 0;
        while (true) {
            int iIndexOf = str2.indexOf(str, i2);
            if (iIndexOf != -1) {
                vector.addElement(str2.substring(i2, iIndexOf));
                i2 = iIndexOf + 1;
            } else {
                vector.addElement(str2.substring(i2));
                String[] strArr = new String[vector.size()];
                vector.copyInto(strArr);
                return strArr;
            }
        }
    }

    @Override // java.applet.AppletContext
    public AudioClip getAudioClip(URL url) {
        AudioClip audioClip;
        checkConnect(url);
        synchronized (audioClips) {
            AudioClip audioClip2 = (AudioClip) audioClips.get(url);
            if (audioClip2 == null) {
                Map map = audioClips;
                AppletAudioClip appletAudioClip = new AppletAudioClip(url);
                audioClip2 = appletAudioClip;
                map.put(url, appletAudioClip);
            }
            audioClip = audioClip2;
        }
        return audioClip;
    }

    @Override // java.applet.AppletContext
    public Image getImage(URL url) {
        return getCachedImage(url);
    }

    static Image getCachedImage(URL url) {
        return (Image) getCachedImageRef(url).get();
    }

    static Ref getCachedImageRef(URL url) {
        AppletImageRef appletImageRef;
        synchronized (imageRefs) {
            AppletImageRef appletImageRef2 = (AppletImageRef) imageRefs.get(url);
            if (appletImageRef2 == null) {
                appletImageRef2 = new AppletImageRef(url);
                imageRefs.put(url, appletImageRef2);
            }
            appletImageRef = appletImageRef2;
        }
        return appletImageRef;
    }

    static void flushImageCache() {
        imageRefs.clear();
    }

    @Override // java.applet.AppletContext
    public Applet getApplet(String str) {
        String lowerCase = str.toLowerCase();
        SocketPermission socketPermission = new SocketPermission(this.panel.getCodeBase().getHost(), SecurityConstants.SOCKET_CONNECT_ACTION);
        Enumeration enumerationElements = appletPanels.elements();
        while (enumerationElements.hasMoreElements()) {
            AppletPanel appletPanel = (AppletPanel) enumerationElements.nextElement2();
            String parameter = appletPanel.getParameter("name");
            if (parameter != null) {
                parameter = parameter.toLowerCase();
            }
            if (lowerCase.equals(parameter) && appletPanel.getDocumentBase().equals(this.panel.getDocumentBase()) && socketPermission.implies(new SocketPermission(appletPanel.getCodeBase().getHost(), SecurityConstants.SOCKET_CONNECT_ACTION))) {
                return appletPanel.applet;
            }
        }
        return null;
    }

    @Override // java.applet.AppletContext
    public Enumeration getApplets() {
        Vector vector = new Vector();
        SocketPermission socketPermission = new SocketPermission(this.panel.getCodeBase().getHost(), SecurityConstants.SOCKET_CONNECT_ACTION);
        Enumeration enumerationElements = appletPanels.elements();
        while (enumerationElements.hasMoreElements()) {
            AppletPanel appletPanel = (AppletPanel) enumerationElements.nextElement2();
            if (appletPanel.getDocumentBase().equals(this.panel.getDocumentBase()) && socketPermission.implies(new SocketPermission(appletPanel.getCodeBase().getHost(), SecurityConstants.SOCKET_CONNECT_ACTION))) {
                vector.addElement(appletPanel.applet);
            }
        }
        return vector.elements();
    }

    @Override // java.applet.AppletContext
    public void showDocument(URL url) {
    }

    @Override // java.applet.AppletContext
    public void showDocument(URL url, String str) {
    }

    @Override // java.applet.AppletContext
    public void showStatus(String str) {
        this.label.setText(str);
    }

    @Override // java.applet.AppletContext
    public void setStream(String str, InputStream inputStream) throws IOException {
    }

    @Override // java.applet.AppletContext
    public InputStream getStream(String str) {
        return null;
    }

    @Override // java.applet.AppletContext
    public Iterator getStreamKeys() {
        return null;
    }

    public static void printTag(PrintStream printStream, Hashtable hashtable) {
        printStream.print("<applet");
        String str = (String) hashtable.get(Constants.ATTRNAME_CODEBASE);
        if (str != null) {
            printStream.print(" codebase=\"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        String str2 = (String) hashtable.get("code");
        if (str2 == null) {
            str2 = "applet.class";
        }
        printStream.print(" code=\"" + str2 + PdfOps.DOUBLE_QUOTE__TOKEN);
        String str3 = (String) hashtable.get(MetadataParser.WIDTH_TAG_NAME);
        if (str3 == null) {
            str3 = "150";
        }
        printStream.print(" width=" + str3);
        String str4 = (String) hashtable.get(MetadataParser.HEIGHT_TAG_NAME);
        if (str4 == null) {
            str4 = "100";
        }
        printStream.print(" height=" + str4);
        String str5 = (String) hashtable.get("name");
        if (str5 != null) {
            printStream.print(" name=\"" + str5 + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        printStream.println(">");
        String[] strArr = new String[hashtable.size()];
        int i2 = 0;
        Enumeration enumerationKeys = hashtable.keys();
        while (enumerationKeys.hasMoreElements()) {
            String str6 = (String) enumerationKeys.nextElement2();
            int i3 = 0;
            while (i3 < i2 && strArr[i3].compareTo(str6) < 0) {
                i3++;
            }
            System.arraycopy(strArr, i3, strArr, i3 + 1, i2 - i3);
            strArr[i3] = str6;
            i2++;
        }
        for (int i4 = 0; i4 < i2; i4++) {
            String str7 = strArr[i4];
            if (systemParam.get(str7) == null) {
                printStream.println("<param name=" + str7 + " value=\"" + hashtable.get(str7) + "\">");
            }
        }
        printStream.println("</applet>");
    }

    public void updateAtts() {
        Dimension size = this.panel.size();
        Insets insets = this.panel.insets();
        this.panel.atts.put(MetadataParser.WIDTH_TAG_NAME, Integer.toString(size.width - (insets.left + insets.right)));
        this.panel.atts.put(MetadataParser.HEIGHT_TAG_NAME, Integer.toString(size.height - (insets.top + insets.bottom)));
    }

    void appletRestart() {
        this.panel.sendEvent(4);
        this.panel.sendEvent(5);
        this.panel.sendEvent(2);
        this.panel.sendEvent(3);
    }

    void appletReload() {
        this.panel.sendEvent(4);
        this.panel.sendEvent(5);
        this.panel.sendEvent(0);
        AppletPanel.flushClassLoader(this.panel.getClassLoaderCacheKey());
        try {
            this.panel.joinAppletThread();
            this.panel.release();
            this.panel.createAppletThread();
            this.panel.sendEvent(1);
            this.panel.sendEvent(2);
            this.panel.sendEvent(3);
        } catch (InterruptedException e2) {
        }
    }

    void appletSave() {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletViewer.2
            /* JADX WARN: Failed to calculate best type for var: r11v1 ??
            java.lang.NullPointerException
             */
            /* JADX WARN: Failed to calculate best type for var: r12v0 ??
            java.lang.NullPointerException
             */
            /* JADX WARN: Failed to calculate best type for var: r13v0 ??
            java.lang.NullPointerException
             */
            /* JADX WARN: Failed to calculate best type for var: r14v0 ??
            java.lang.NullPointerException
             */
            /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
            	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
            	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
            	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
            	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
            	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
            	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
            	at java.base/java.util.ArrayList.forEach(Unknown Source)
            	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
             */
            /* JADX WARN: Not initialized variable reg: 11, insn: 0x0193: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r11 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:66:0x0193 */
            /* JADX WARN: Not initialized variable reg: 12, insn: 0x0198: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:68:0x0198 */
            /* JADX WARN: Not initialized variable reg: 13, insn: 0x013c: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:44:0x013c */
            /* JADX WARN: Not initialized variable reg: 14, insn: 0x0141: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:46:0x0141 */
            /* JADX WARN: Type inference failed for: r11v1, types: [java.io.FileOutputStream] */
            /* JADX WARN: Type inference failed for: r12v0, types: [java.lang.Throwable] */
            /* JADX WARN: Type inference failed for: r13v0, types: [java.io.BufferedOutputStream] */
            /* JADX WARN: Type inference failed for: r14v0, types: [java.lang.Throwable] */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ?? r11;
                ?? r12;
                ?? r13;
                ?? r14;
                AppletViewer.this.panel.sendEvent(4);
                FileDialog fileDialog = new FileDialog(AppletViewer.this, AppletViewer.amh.getMessage("appletsave.filedialogtitle"), 1);
                fileDialog.setDirectory(System.getProperty("user.dir"));
                fileDialog.setFile(AppletViewer.defaultSaveFile);
                fileDialog.show();
                String file = fileDialog.getFile();
                if (file == null) {
                    AppletViewer.this.panel.sendEvent(3);
                    return null;
                }
                File file2 = new File(fileDialog.getDirectory(), file);
                try {
                    try {
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file2);
                            Throwable th = null;
                            try {
                                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                                Throwable th2 = null;
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
                                Throwable th3 = null;
                                try {
                                    try {
                                        AppletViewer.this.showStatus(AppletViewer.amh.getMessage("appletsave.err1", AppletViewer.this.panel.applet.toString(), file2.toString()));
                                        objectOutputStream.writeObject(AppletViewer.this.panel.applet);
                                        if (objectOutputStream != null) {
                                            if (0 != 0) {
                                                try {
                                                    objectOutputStream.close();
                                                } catch (Throwable th4) {
                                                    th3.addSuppressed(th4);
                                                }
                                            } else {
                                                objectOutputStream.close();
                                            }
                                        }
                                        if (bufferedOutputStream != null) {
                                            if (0 != 0) {
                                                try {
                                                    bufferedOutputStream.close();
                                                } catch (Throwable th5) {
                                                    th2.addSuppressed(th5);
                                                }
                                            } else {
                                                bufferedOutputStream.close();
                                            }
                                        }
                                        if (fileOutputStream != null) {
                                            if (0 != 0) {
                                                try {
                                                    fileOutputStream.close();
                                                } catch (Throwable th6) {
                                                    th.addSuppressed(th6);
                                                }
                                            } else {
                                                fileOutputStream.close();
                                            }
                                        }
                                        AppletViewer.this.panel.sendEvent(3);
                                        return null;
                                    } catch (Throwable th7) {
                                        if (objectOutputStream != null) {
                                            if (th3 != null) {
                                                try {
                                                    objectOutputStream.close();
                                                } catch (Throwable th8) {
                                                    th3.addSuppressed(th8);
                                                }
                                            } else {
                                                objectOutputStream.close();
                                            }
                                        }
                                        throw th7;
                                    }
                                } catch (Throwable th9) {
                                    th3 = th9;
                                    throw th9;
                                }
                            } catch (Throwable th10) {
                                if (r13 != 0) {
                                    if (r14 != 0) {
                                        try {
                                            r13.close();
                                        } catch (Throwable th11) {
                                            r14.addSuppressed(th11);
                                        }
                                    } else {
                                        r13.close();
                                    }
                                }
                                throw th10;
                            }
                        } catch (IOException e2) {
                            System.err.println(AppletViewer.amh.getMessage("appletsave.err2", e2));
                            AppletViewer.this.panel.sendEvent(3);
                            return null;
                        }
                    } catch (Throwable th12) {
                        if (r11 != 0) {
                            if (r12 != 0) {
                                try {
                                    r11.close();
                                } catch (Throwable th13) {
                                    r12.addSuppressed(th13);
                                }
                            } else {
                                r11.close();
                            }
                        }
                        throw th12;
                    }
                } catch (Throwable th14) {
                    AppletViewer.this.panel.sendEvent(3);
                    throw th14;
                }
            }
        });
    }

    void appletClone() {
        Point pointLocation = location();
        updateAtts();
        this.factory.createAppletViewer(pointLocation.f12370x + 30, pointLocation.f12371y + 30, this.panel.documentURL, (Hashtable) this.panel.atts.clone());
    }

    void appletTag() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        updateAtts();
        printTag(new PrintStream(byteArrayOutputStream), this.panel.atts);
        showStatus(amh.getMessage("applettag"));
        Point pointLocation = location();
        new TextFrame(pointLocation.f12370x + 30, pointLocation.f12371y + 30, amh.getMessage("applettag.textframe"), byteArrayOutputStream.toString());
    }

    void appletInfo() {
        String appletInfo = this.panel.applet.getAppletInfo();
        if (appletInfo == null) {
            appletInfo = amh.getMessage("appletinfo.applet");
        }
        String str = appletInfo + "\n\n";
        String[][] parameterInfo = this.panel.applet.getParameterInfo();
        if (parameterInfo != null) {
            for (int i2 = 0; i2 < parameterInfo.length; i2++) {
                str = str + parameterInfo[i2][0] + " -- " + parameterInfo[i2][1] + " -- " + parameterInfo[i2][2] + "\n";
            }
        } else {
            str = str + amh.getMessage("appletinfo.param");
        }
        Point pointLocation = location();
        new TextFrame(pointLocation.f12370x + 30, pointLocation.f12371y + 30, amh.getMessage("appletinfo.textframe"), str);
    }

    void appletCharacterEncoding() {
        showStatus(amh.getMessage("appletencoding", encoding));
    }

    void appletEdit() {
    }

    void appletPrint() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        if (printerJob != null) {
            HashPrintRequestAttributeSet hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
            if (printerJob.printDialog(hashPrintRequestAttributeSet)) {
                printerJob.setPrintable(this);
                try {
                    printerJob.print(hashPrintRequestAttributeSet);
                    this.statusMsgStream.println(amh.getMessage("appletprint.finish"));
                    return;
                } catch (PrinterException e2) {
                    this.statusMsgStream.println(amh.getMessage("appletprint.fail"));
                    return;
                }
            }
            this.statusMsgStream.println(amh.getMessage("appletprint.cancel"));
            return;
        }
        this.statusMsgStream.println(amh.getMessage("appletprint.fail"));
    }

    @Override // java.awt.print.Printable
    public int print(Graphics graphics, PageFormat pageFormat, int i2) {
        if (i2 > 0) {
            return 1;
        }
        ((Graphics2D) graphics).translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        this.panel.applet.printAll(graphics);
        return 0;
    }

    public static synchronized void networkProperties() {
        if (props == null) {
            props = new AppletProps();
        }
        props.addNotify();
        props.setVisible(true);
    }

    void appletStart() {
        this.panel.sendEvent(3);
    }

    void appletStop() {
        this.panel.sendEvent(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appletShutdown(AppletPanel appletPanel) {
        appletPanel.sendEvent(4);
        appletPanel.sendEvent(5);
        appletPanel.sendEvent(0);
        appletPanel.sendEvent(6);
    }

    void appletClose() {
        final AppletViewerPanel appletViewerPanel = this.panel;
        new Thread(new Runnable() { // from class: sun.applet.AppletViewer.3
            @Override // java.lang.Runnable
            public void run() {
                AppletViewer.this.appletShutdown(appletViewerPanel);
                AppletViewer.appletPanels.removeElement(appletViewerPanel);
                AppletViewer.this.dispose();
                if (AppletViewer.countApplets() == 0) {
                    AppletViewer.this.appletSystemExit();
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appletSystemExit() {
        if (this.factory.isStandalone()) {
            System.exit(0);
        }
    }

    protected void appletQuit() {
        new Thread(new Runnable() { // from class: sun.applet.AppletViewer.4
            @Override // java.lang.Runnable
            public void run() {
                Enumeration enumerationElements = AppletViewer.appletPanels.elements();
                while (enumerationElements.hasMoreElements()) {
                    AppletViewer.this.appletShutdown((AppletPanel) enumerationElements.nextElement2());
                }
                AppletViewer.this.appletSystemExit();
            }
        }).start();
    }

    public void processUserAction(ActionEvent actionEvent) {
        String label = ((MenuItem) actionEvent.getSource()).getLabel();
        if (amh.getMessage("menuitem.restart").equals(label)) {
            appletRestart();
            return;
        }
        if (amh.getMessage("menuitem.reload").equals(label)) {
            appletReload();
            return;
        }
        if (amh.getMessage("menuitem.clone").equals(label)) {
            appletClone();
            return;
        }
        if (amh.getMessage("menuitem.stop").equals(label)) {
            appletStop();
            return;
        }
        if (amh.getMessage("menuitem.save").equals(label)) {
            appletSave();
            return;
        }
        if (amh.getMessage("menuitem.start").equals(label)) {
            appletStart();
            return;
        }
        if (amh.getMessage("menuitem.tag").equals(label)) {
            appletTag();
            return;
        }
        if (amh.getMessage("menuitem.info").equals(label)) {
            appletInfo();
            return;
        }
        if (amh.getMessage("menuitem.encoding").equals(label)) {
            appletCharacterEncoding();
            return;
        }
        if (amh.getMessage("menuitem.edit").equals(label)) {
            appletEdit();
            return;
        }
        if (amh.getMessage("menuitem.print").equals(label)) {
            appletPrint();
            return;
        }
        if (amh.getMessage("menuitem.props").equals(label)) {
            networkProperties();
            return;
        }
        if (amh.getMessage("menuitem.close").equals(label)) {
            appletClose();
        } else if (this.factory.isStandalone() && amh.getMessage("menuitem.quit").equals(label)) {
            appletQuit();
        }
    }

    public static int countApplets() {
        return appletPanels.size();
    }

    public static void skipSpace(Reader reader) throws IOException {
        while (f13536c >= 0) {
            if (f13536c == 32 || f13536c == 9 || f13536c == 10 || f13536c == 13) {
                f13536c = reader.read();
            } else {
                return;
            }
        }
    }

    public static String scanIdentifier(Reader reader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            if ((f13536c >= 97 && f13536c <= 122) || ((f13536c >= 65 && f13536c <= 90) || ((f13536c >= 48 && f13536c <= 57) || f13536c == 95))) {
                stringBuffer.append((char) f13536c);
                f13536c = reader.read();
            } else {
                return stringBuffer.toString();
            }
        }
    }

    public static Hashtable scanTag(Reader reader) throws IOException {
        Hashtable hashtable = new Hashtable();
        skipSpace(reader);
        while (f13536c >= 0 && f13536c != 62) {
            String strScanIdentifier = scanIdentifier(reader);
            String string = "";
            skipSpace(reader);
            if (f13536c == 61) {
                int i2 = -1;
                f13536c = reader.read();
                skipSpace(reader);
                if (f13536c == 39 || f13536c == 34) {
                    i2 = f13536c;
                    f13536c = reader.read();
                }
                StringBuffer stringBuffer = new StringBuffer();
                while (f13536c > 0 && ((i2 < 0 && f13536c != 32 && f13536c != 9 && f13536c != 10 && f13536c != 13 && f13536c != 62) || (i2 >= 0 && f13536c != i2))) {
                    stringBuffer.append((char) f13536c);
                    f13536c = reader.read();
                }
                if (f13536c == i2) {
                    f13536c = reader.read();
                }
                skipSpace(reader);
                string = stringBuffer.toString();
            }
            if (!string.equals("")) {
                hashtable.put(strScanIdentifier.toLowerCase(Locale.ENGLISH), string);
            }
            while (f13536c != 62 && f13536c >= 0 && ((f13536c < 97 || f13536c > 122) && ((f13536c < 65 || f13536c > 90) && ((f13536c < 48 || f13536c > 57) && f13536c != 95)))) {
                f13536c = reader.read();
            }
        }
        return hashtable;
    }

    private static Reader makeReader(InputStream inputStream) {
        if (encoding != null) {
            try {
                return new BufferedReader(new InputStreamReader(inputStream, encoding));
            } catch (IOException e2) {
            }
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        encoding = inputStreamReader.getEncoding();
        return new BufferedReader(inputStreamReader);
    }

    public static void parse(URL url, String str) throws IOException {
        encoding = str;
        parse(url, System.out, new StdAppletViewerFactory());
    }

    public static void parse(URL url) throws IOException, HeadlessException {
        parse(url, System.out, new StdAppletViewerFactory());
    }

    public static void parse(URL url, PrintStream printStream, AppletViewerFactory appletViewerFactory) throws IOException, HeadlessException {
        boolean z2 = false;
        String message = amh.getMessage("parse.warning.requiresname");
        String message2 = amh.getMessage("parse.warning.paramoutside");
        String message3 = amh.getMessage("parse.warning.applet.requirescode");
        String message4 = amh.getMessage("parse.warning.applet.requiresheight");
        String message5 = amh.getMessage("parse.warning.applet.requireswidth");
        String message6 = amh.getMessage("parse.warning.object.requirescode");
        String message7 = amh.getMessage("parse.warning.object.requiresheight");
        String message8 = amh.getMessage("parse.warning.object.requireswidth");
        String message9 = amh.getMessage("parse.warning.embed.requirescode");
        String message10 = amh.getMessage("parse.warning.embed.requiresheight");
        String message11 = amh.getMessage("parse.warning.embed.requireswidth");
        String message12 = amh.getMessage("parse.warning.appnotLongersupported");
        URLConnection uRLConnectionOpenConnection = url.openConnection();
        Reader readerMakeReader = makeReader(uRLConnectionOpenConnection.getInputStream());
        URL url2 = uRLConnectionOpenConnection.getURL();
        int i2 = 1;
        Hashtable hashtableScanTag = null;
        while (true) {
            f13536c = readerMakeReader.read();
            if (f13536c != -1) {
                if (f13536c == 60) {
                    f13536c = readerMakeReader.read();
                    if (f13536c == 47) {
                        f13536c = readerMakeReader.read();
                        String strScanIdentifier = scanIdentifier(readerMakeReader);
                        if (strScanIdentifier.equalsIgnoreCase("applet") || strScanIdentifier.equalsIgnoreCase("object") || strScanIdentifier.equalsIgnoreCase("embed")) {
                            if (z2 && hashtableScanTag.get("code") == null && hashtableScanTag.get("object") == null) {
                                printStream.println(message6);
                                hashtableScanTag = null;
                            }
                            if (hashtableScanTag != null) {
                                appletViewerFactory.createAppletViewer(f13537x, f13538y, url2, hashtableScanTag);
                                f13537x += 30;
                                f13538y += 30;
                                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                                if (f13537x > screenSize.width - 300 || f13538y > screenSize.height - 300) {
                                    f13537x = 0;
                                    f13538y = 2 * i2 * 30;
                                    i2++;
                                }
                            }
                            hashtableScanTag = null;
                            z2 = false;
                        }
                    } else {
                        String strScanIdentifier2 = scanIdentifier(readerMakeReader);
                        if (strScanIdentifier2.equalsIgnoreCase(Constants.ELEMNAME_PARAMVARIABLE_STRING)) {
                            Hashtable hashtableScanTag2 = scanTag(readerMakeReader);
                            String str = (String) hashtableScanTag2.get("name");
                            if (str == null) {
                                printStream.println(message);
                            } else {
                                String str2 = (String) hashtableScanTag2.get("value");
                                if (str2 == null) {
                                    printStream.println(message);
                                } else if (hashtableScanTag != null) {
                                    hashtableScanTag.put(str.toLowerCase(), str2);
                                } else {
                                    printStream.println(message2);
                                }
                            }
                        } else if (strScanIdentifier2.equalsIgnoreCase("applet")) {
                            hashtableScanTag = scanTag(readerMakeReader);
                            if (hashtableScanTag.get("code") == null && hashtableScanTag.get("object") == null) {
                                printStream.println(message3);
                                hashtableScanTag = null;
                            } else if (hashtableScanTag.get(MetadataParser.WIDTH_TAG_NAME) == null) {
                                printStream.println(message5);
                                hashtableScanTag = null;
                            } else if (hashtableScanTag.get(MetadataParser.HEIGHT_TAG_NAME) == null) {
                                printStream.println(message4);
                                hashtableScanTag = null;
                            }
                        } else if (strScanIdentifier2.equalsIgnoreCase("object")) {
                            z2 = true;
                            hashtableScanTag = scanTag(readerMakeReader);
                            if (hashtableScanTag.get(Constants.ATTRNAME_CODEBASE) != null) {
                                hashtableScanTag.remove(Constants.ATTRNAME_CODEBASE);
                            }
                            if (hashtableScanTag.get(MetadataParser.WIDTH_TAG_NAME) == null) {
                                printStream.println(message8);
                                hashtableScanTag = null;
                            } else if (hashtableScanTag.get(MetadataParser.HEIGHT_TAG_NAME) == null) {
                                printStream.println(message7);
                                hashtableScanTag = null;
                            }
                        } else if (strScanIdentifier2.equalsIgnoreCase("embed")) {
                            hashtableScanTag = scanTag(readerMakeReader);
                            if (hashtableScanTag.get("code") == null && hashtableScanTag.get("object") == null) {
                                printStream.println(message9);
                                hashtableScanTag = null;
                            } else if (hashtableScanTag.get(MetadataParser.WIDTH_TAG_NAME) == null) {
                                printStream.println(message11);
                                hashtableScanTag = null;
                            } else if (hashtableScanTag.get(MetadataParser.HEIGHT_TAG_NAME) == null) {
                                printStream.println(message10);
                                hashtableScanTag = null;
                            }
                        } else if (strScanIdentifier2.equalsIgnoreCase("app")) {
                            printStream.println(message12);
                            Hashtable hashtableScanTag3 = scanTag(readerMakeReader);
                            String str3 = (String) hashtableScanTag3.get(Constants.ATTRNAME_CLASS);
                            if (str3 != null) {
                                hashtableScanTag3.remove(Constants.ATTRNAME_CLASS);
                                hashtableScanTag3.put("code", str3 + ".class");
                            }
                            String str4 = (String) hashtableScanTag3.get("src");
                            if (str4 != null) {
                                hashtableScanTag3.remove("src");
                                hashtableScanTag3.put(Constants.ATTRNAME_CODEBASE, str4);
                            }
                            if (hashtableScanTag3.get(MetadataParser.WIDTH_TAG_NAME) == null) {
                                hashtableScanTag3.put(MetadataParser.WIDTH_TAG_NAME, "100");
                            }
                            if (hashtableScanTag3.get(MetadataParser.HEIGHT_TAG_NAME) == null) {
                                hashtableScanTag3.put(MetadataParser.HEIGHT_TAG_NAME, "100");
                            }
                            printTag(printStream, hashtableScanTag3);
                            printStream.println();
                        }
                    }
                }
            } else {
                readerMakeReader.close();
                return;
            }
        }
    }

    @Deprecated
    public static void main(String[] strArr) {
        Main.main(strArr);
    }

    private static void checkConnect(URL url) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                Permission permission = url.openConnection().getPermission();
                if (permission != null) {
                    securityManager.checkPermission(permission);
                } else {
                    securityManager.checkConnect(url.getHost(), url.getPort());
                }
            } catch (IOException e2) {
                securityManager.checkConnect(url.getHost(), url.getPort());
            }
        }
    }
}
