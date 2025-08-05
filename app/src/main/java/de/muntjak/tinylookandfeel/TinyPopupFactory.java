package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyPopupMenuBorder;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyPopupFactory.class */
public class TinyPopupFactory extends PopupFactory {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_DIALOGS = false;
    public static final String SHADOW_POPUP_KEY = "SHADOW_POPUP_KEY";
    public static final String VERTICAL_IMAGE_KEY = "VERTICAL_IMAGE_KEY";
    public static final String HORIZONTAL_IMAGE_KEY = "HORIZONTAL_IMAGE_KEY";
    public static final String COMPONENT_ORIENTATION_KEY = "COMPONENT_ORIENTATION_KEY";
    private static WindowListener activationListener;
    private PopupFactory storedFactory;
    private static final Vector DIALOGS = new Vector();
    private static final Stack SHADOW_POPUP_CACHE = new Stack();

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyPopupFactory$ShadowPopup.class */
    private static class ShadowPopup extends Popup {
        private static final Rectangle RECT = new Rectangle();
        private static final Point POINT = new Point();
        private static final Dimension SIZE = new Dimension();
        private Component owner;
        private Popup delegate;
        private JPopupMenu contents;

        /* renamed from: x, reason: collision with root package name */
        private int f12128x;

        /* renamed from: y, reason: collision with root package name */
        private int f12129y;
        private BufferedImage vertImg;
        private BufferedImage horzImg;

        private ShadowPopup(Component component, Popup popup, JPopupMenu jPopupMenu, int i2, int i3) {
            init(component, popup, jPopupMenu, i2, i3);
        }

        static ShadowPopup getInstance(Component component, Popup popup, JPopupMenu jPopupMenu, int i2, int i3) {
            if (TinyPopupFactory.SHADOW_POPUP_CACHE.empty()) {
                return new ShadowPopup(component, popup, jPopupMenu, i2, i3);
            }
            ShadowPopup shadowPopup = (ShadowPopup) TinyPopupFactory.SHADOW_POPUP_CACHE.pop();
            shadowPopup.init(component, popup, jPopupMenu, i2, i3);
            return shadowPopup;
        }

        private void init(Component component, Popup popup, JPopupMenu jPopupMenu, int i2, int i3) {
            this.owner = component;
            this.delegate = popup;
            this.contents = jPopupMenu;
            this.f12128x = i2;
            this.f12129y = i3;
            ComponentOrientation componentOrientation = ComponentOrientation.LEFT_TO_RIGHT;
            if (component != null) {
                componentOrientation = component.getComponentOrientation();
            }
            jPopupMenu.putClientProperty(TinyPopupFactory.SHADOW_POPUP_KEY, Boolean.TRUE);
            jPopupMenu.putClientProperty(TinyPopupFactory.COMPONENT_ORIENTATION_KEY, componentOrientation);
        }

        @Override // javax.swing.Popup
        public void show() {
            makeSnapshot();
            this.delegate.show();
        }

        @Override // javax.swing.Popup
        public void hide() {
            this.delegate.hide();
            uninstall();
        }

        private void makeSnapshot() {
            JLayeredPane layeredPane;
            JLayeredPane layeredPane2;
            SIZE.setSize(this.contents.getPreferredSize());
            if (SIZE.width < 5 || SIZE.height < 5) {
                return;
            }
            Object clientProperty = this.contents.getClientProperty(TinyPopupFactory.COMPONENT_ORIENTATION_KEY);
            if (clientProperty == null ? true : ((ComponentOrientation) clientProperty).isLeftToRight()) {
                RECT.setBounds((this.f12128x + SIZE.width) - 5, this.f12129y, 5, SIZE.height);
                this.vertImg = TinyLookAndFeel.ROBOT.createScreenCapture(RECT);
                this.contents.putClientProperty(TinyPopupFactory.VERTICAL_IMAGE_KEY, this.vertImg);
                RECT.setBounds(this.f12128x, (this.f12129y + SIZE.height) - 5, SIZE.width, 5);
                this.horzImg = TinyLookAndFeel.ROBOT.createScreenCapture(RECT);
                this.contents.putClientProperty(TinyPopupFactory.HORIZONTAL_IMAGE_KEY, this.horzImg);
                JRootPane rootPane = SwingUtilities.getRootPane(this.owner);
                if (rootPane == null || (layeredPane2 = rootPane.getLayeredPane()) == null) {
                    return;
                }
                int width = layeredPane2.getWidth();
                int height = layeredPane2.getHeight();
                POINT.f12370x = this.f12128x;
                POINT.f12371y = this.f12129y;
                SwingUtilities.convertPointFromScreen(POINT, layeredPane2);
                RECT.f12372x = POINT.f12370x;
                RECT.f12373y = (POINT.f12371y + SIZE.height) - 5;
                RECT.width = SIZE.width;
                RECT.height = 5;
                if (RECT.f12372x + RECT.width > width) {
                    RECT.width = width - RECT.f12372x;
                }
                if (RECT.f12373y + RECT.height > height) {
                    RECT.height = height - RECT.f12373y;
                }
                Graphics2D graphics2DCreateGraphics = this.horzImg.createGraphics();
                if (!RECT.isEmpty()) {
                    graphics2DCreateGraphics.translate(-RECT.f12372x, -RECT.f12373y);
                    graphics2DCreateGraphics.setClip(RECT);
                    if (layeredPane2 instanceof JComponent) {
                        boolean zIsDoubleBuffered = layeredPane2.isDoubleBuffered();
                        layeredPane2.setDoubleBuffered(false);
                        layeredPane2.paintAll(graphics2DCreateGraphics);
                        layeredPane2.setDoubleBuffered(zIsDoubleBuffered);
                    } else {
                        layeredPane2.paintAll(graphics2DCreateGraphics);
                    }
                    graphics2DCreateGraphics.translate(RECT.f12372x, RECT.f12373y);
                }
                Iterator it = TinyPopupFactory.DIALOGS.iterator();
                while (it.hasNext()) {
                    Window window = (Window) it.next();
                    int width2 = window.getWidth();
                    int height2 = window.getHeight();
                    POINT.f12370x = this.f12128x;
                    POINT.f12371y = this.f12129y;
                    SwingUtilities.convertPointFromScreen(POINT, window);
                    RECT.f12372x = POINT.f12370x;
                    RECT.f12373y = (POINT.f12371y + SIZE.height) - 5;
                    RECT.width = SIZE.width;
                    RECT.height = 5;
                    if (RECT.f12372x + RECT.width > width2) {
                        RECT.width = width2 - RECT.f12372x;
                    }
                    if (RECT.f12373y + RECT.height > height2) {
                        RECT.height = height2 - RECT.f12373y;
                    }
                    if (!RECT.isEmpty()) {
                        graphics2DCreateGraphics.translate(-RECT.f12372x, -RECT.f12373y);
                        graphics2DCreateGraphics.setClip(RECT);
                        window.paintAll(graphics2DCreateGraphics);
                        graphics2DCreateGraphics.translate(RECT.f12372x, RECT.f12373y);
                    }
                }
                graphics2DCreateGraphics.dispose();
                POINT.f12370x = this.f12128x;
                POINT.f12371y = this.f12129y;
                SwingUtilities.convertPointFromScreen(POINT, layeredPane2);
                RECT.f12372x = (POINT.f12370x + SIZE.width) - 5;
                RECT.f12373y = POINT.f12371y;
                RECT.width = 5;
                RECT.height = SIZE.height;
                if (RECT.f12372x + RECT.width > width) {
                    RECT.width = width - RECT.f12372x;
                }
                if (RECT.f12373y + RECT.height > height) {
                    RECT.height = height - RECT.f12373y;
                }
                Graphics2D graphics2DCreateGraphics2 = this.vertImg.createGraphics();
                if (!RECT.isEmpty()) {
                    graphics2DCreateGraphics2.translate(-RECT.f12372x, -RECT.f12373y);
                    graphics2DCreateGraphics2.setClip(RECT);
                    if (layeredPane2 instanceof JComponent) {
                        boolean zIsDoubleBuffered2 = layeredPane2.isDoubleBuffered();
                        layeredPane2.setDoubleBuffered(false);
                        layeredPane2.paintAll(graphics2DCreateGraphics2);
                        layeredPane2.setDoubleBuffered(zIsDoubleBuffered2);
                    } else {
                        layeredPane2.paintAll(graphics2DCreateGraphics2);
                    }
                    graphics2DCreateGraphics2.translate(RECT.f12372x, RECT.f12373y);
                }
                Iterator it2 = TinyPopupFactory.DIALOGS.iterator();
                while (it2.hasNext()) {
                    Window window2 = (Window) it2.next();
                    int width3 = window2.getWidth();
                    int height3 = window2.getHeight();
                    POINT.f12370x = this.f12128x;
                    POINT.f12371y = this.f12129y;
                    SwingUtilities.convertPointFromScreen(POINT, window2);
                    RECT.f12372x = (POINT.f12370x + SIZE.width) - 5;
                    RECT.f12373y = POINT.f12371y;
                    RECT.width = 5;
                    RECT.height = SIZE.height;
                    if (RECT.f12372x + RECT.width > width3) {
                        RECT.width = width3 - RECT.f12372x;
                    }
                    if (RECT.f12373y + RECT.height > height3) {
                        RECT.height = height3 - RECT.f12373y;
                    }
                    if (!RECT.isEmpty()) {
                        graphics2DCreateGraphics2.translate(-RECT.f12372x, -RECT.f12373y);
                        graphics2DCreateGraphics2.setClip(RECT);
                        window2.paintAll(graphics2DCreateGraphics2);
                        graphics2DCreateGraphics2.translate(RECT.f12372x, RECT.f12373y);
                    }
                }
                graphics2DCreateGraphics2.dispose();
                return;
            }
            RECT.setBounds(this.f12128x, this.f12129y, 5, SIZE.height);
            this.vertImg = TinyLookAndFeel.ROBOT.createScreenCapture(RECT);
            this.contents.putClientProperty(TinyPopupFactory.VERTICAL_IMAGE_KEY, this.vertImg);
            RECT.setBounds(this.f12128x, (this.f12129y + SIZE.height) - 5, SIZE.width, 5);
            this.horzImg = TinyLookAndFeel.ROBOT.createScreenCapture(RECT);
            this.contents.putClientProperty(TinyPopupFactory.HORIZONTAL_IMAGE_KEY, this.horzImg);
            JRootPane rootPane2 = SwingUtilities.getRootPane(this.owner);
            if (rootPane2 == null || (layeredPane = rootPane2.getLayeredPane()) == null) {
                return;
            }
            int width4 = layeredPane.getWidth();
            int height4 = layeredPane.getHeight();
            POINT.f12370x = this.f12128x;
            POINT.f12371y = this.f12129y;
            SwingUtilities.convertPointFromScreen(POINT, layeredPane);
            RECT.f12372x = POINT.f12370x;
            RECT.f12373y = (POINT.f12371y + SIZE.height) - 5;
            RECT.width = SIZE.width;
            RECT.height = 5;
            if (RECT.f12372x + RECT.width > width4) {
                RECT.width = width4 - RECT.f12372x;
            }
            if (RECT.f12373y + RECT.height > height4) {
                RECT.height = height4 - RECT.f12373y;
            }
            Graphics2D graphics2DCreateGraphics3 = this.horzImg.createGraphics();
            if (!RECT.isEmpty()) {
                graphics2DCreateGraphics3.translate(-RECT.f12372x, -RECT.f12373y);
                graphics2DCreateGraphics3.setClip(RECT);
                if (layeredPane instanceof JComponent) {
                    boolean zIsDoubleBuffered3 = layeredPane.isDoubleBuffered();
                    layeredPane.setDoubleBuffered(false);
                    layeredPane.paintAll(graphics2DCreateGraphics3);
                    layeredPane.setDoubleBuffered(zIsDoubleBuffered3);
                } else {
                    layeredPane.paintAll(graphics2DCreateGraphics3);
                }
                graphics2DCreateGraphics3.translate(RECT.f12372x, RECT.f12373y);
            }
            Iterator it3 = TinyPopupFactory.DIALOGS.iterator();
            while (it3.hasNext()) {
                Window window3 = (Window) it3.next();
                int width5 = window3.getWidth();
                int height5 = window3.getHeight();
                POINT.f12370x = this.f12128x;
                POINT.f12371y = this.f12129y;
                SwingUtilities.convertPointFromScreen(POINT, window3);
                RECT.f12372x = POINT.f12370x;
                RECT.f12373y = (POINT.f12371y + SIZE.height) - 5;
                RECT.width = SIZE.width;
                RECT.height = 5;
                if (RECT.f12372x + RECT.width > width5) {
                    RECT.width = width5 - RECT.f12372x;
                }
                if (RECT.f12373y + RECT.height > height5) {
                    RECT.height = height5 - RECT.f12373y;
                }
                if (!RECT.isEmpty()) {
                    graphics2DCreateGraphics3.translate(-RECT.f12372x, -RECT.f12373y);
                    graphics2DCreateGraphics3.setClip(RECT);
                    window3.paintAll(graphics2DCreateGraphics3);
                    graphics2DCreateGraphics3.translate(RECT.f12372x, RECT.f12373y);
                }
            }
            graphics2DCreateGraphics3.dispose();
            POINT.f12370x = this.f12128x;
            POINT.f12371y = this.f12129y;
            SwingUtilities.convertPointFromScreen(POINT, layeredPane);
            RECT.f12372x = POINT.f12370x;
            RECT.f12373y = POINT.f12371y;
            RECT.width = 5;
            RECT.height = SIZE.height;
            if (RECT.f12372x + RECT.width > width4) {
                RECT.width = width4 - RECT.f12372x;
            }
            if (RECT.f12373y + RECT.height > height4) {
                RECT.height = height4 - RECT.f12373y;
            }
            Graphics2D graphics2DCreateGraphics4 = this.vertImg.createGraphics();
            if (!RECT.isEmpty()) {
                graphics2DCreateGraphics4.translate(-RECT.f12372x, -RECT.f12373y);
                graphics2DCreateGraphics4.setClip(RECT);
                if (layeredPane instanceof JComponent) {
                    boolean zIsDoubleBuffered4 = layeredPane.isDoubleBuffered();
                    layeredPane.setDoubleBuffered(false);
                    layeredPane.paintAll(graphics2DCreateGraphics4);
                    layeredPane.setDoubleBuffered(zIsDoubleBuffered4);
                } else {
                    layeredPane.paintAll(graphics2DCreateGraphics4);
                }
                graphics2DCreateGraphics4.translate(RECT.f12372x, RECT.f12373y);
            }
            Iterator it4 = TinyPopupFactory.DIALOGS.iterator();
            while (it4.hasNext()) {
                Window window4 = (Window) it4.next();
                int width6 = window4.getWidth();
                int height6 = window4.getHeight();
                POINT.f12370x = this.f12128x;
                POINT.f12371y = this.f12129y;
                SwingUtilities.convertPointFromScreen(POINT, window4);
                RECT.f12372x = POINT.f12370x;
                RECT.f12373y = POINT.f12371y;
                RECT.width = 5;
                RECT.height = SIZE.height;
                if (RECT.f12372x + RECT.width > width6) {
                    RECT.width = width6 - RECT.f12372x;
                }
                if (RECT.f12373y + RECT.height > height6) {
                    RECT.height = height6 - RECT.f12373y;
                }
                if (!RECT.isEmpty()) {
                    graphics2DCreateGraphics4.translate(-RECT.f12372x, -RECT.f12373y);
                    graphics2DCreateGraphics4.setClip(RECT);
                    window4.paintAll(graphics2DCreateGraphics4);
                    graphics2DCreateGraphics4.translate(RECT.f12372x, RECT.f12373y);
                }
            }
            graphics2DCreateGraphics4.dispose();
        }

        private void uninstall() {
            this.contents.putClientProperty(TinyPopupFactory.SHADOW_POPUP_KEY, null);
            this.contents.putClientProperty(TinyPopupFactory.COMPONENT_ORIENTATION_KEY, null);
            this.contents.putClientProperty(TinyPopupFactory.VERTICAL_IMAGE_KEY, null);
            this.contents.putClientProperty(TinyPopupFactory.HORIZONTAL_IMAGE_KEY, null);
            this.contents = null;
            this.delegate = null;
            this.vertImg = null;
            this.horzImg = null;
            TinyPopupFactory.SHADOW_POPUP_CACHE.push(this);
        }
    }

    private TinyPopupFactory(PopupFactory popupFactory) {
        this.storedFactory = popupFactory;
    }

    @Override // javax.swing.PopupFactory
    public Popup getPopup(Component component, Component component2, int i2, int i3) throws IllegalArgumentException {
        Popup popup = super.getPopup(component, component2, i2, i3);
        return (component2 instanceof JPopupMenu) && (((JComponent) component2).getBorder() instanceof TinyPopupMenuBorder) ? ShadowPopup.getInstance(component, popup, (JPopupMenu) component2, i2, i3) : popup;
    }

    public static void addDialog(JDialog jDialog) {
        if (TinyUtils.isOSMac() || jDialog.isModal()) {
            return;
        }
        DIALOGS.add(jDialog);
        if (activationListener == null) {
            activationListener = new WindowAdapter() { // from class: de.muntjak.tinylookandfeel.TinyPopupFactory.1
                @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                public void windowActivated(WindowEvent windowEvent) {
                    Window window = windowEvent.getWindow();
                    TinyPopupFactory.DIALOGS.remove(window);
                    TinyPopupFactory.DIALOGS.add(window);
                }

                @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                public void windowClosed(WindowEvent windowEvent) {
                    Window window = windowEvent.getWindow();
                    window.removeWindowListener(TinyPopupFactory.activationListener);
                    TinyPopupFactory.DIALOGS.remove(window);
                }
            };
        }
        jDialog.addWindowListener(activationListener);
    }

    public static void closeDialogs() {
        Iterator it = DIALOGS.iterator();
        while (it.hasNext()) {
            ((JDialog) it.next()).dispose();
        }
    }

    private static void printDialogs() {
        System.out.println();
        String string = "";
        Iterator it = DIALOGS.iterator();
        while (it.hasNext()) {
            System.out.println(new StringBuffer().append(string).append(((JDialog) it.next()).getTitle()).toString());
            string = new StringBuffer().append(string).append(Constants.INDENT).toString();
        }
    }

    public static void install() {
        if (isPopupShadowEnabled()) {
            PopupFactory sharedInstance = PopupFactory.getSharedInstance();
            if (sharedInstance instanceof TinyPopupFactory) {
                return;
            }
            if (!DIALOGS.isEmpty()) {
                DIALOGS.clear();
            }
            PopupFactory.setSharedInstance(new TinyPopupFactory(sharedInstance));
        }
    }

    public static boolean isPopupShadowEnabled() {
        return (TinyUtils.isOSMac() || !Theme.menuPopupShadow.getValue() || TinyLookAndFeel.ROBOT == null) ? false : true;
    }

    public static void uninstall() {
        while (!SHADOW_POPUP_CACHE.empty()) {
            SHADOW_POPUP_CACHE.pop();
        }
        while (!DIALOGS.isEmpty()) {
            Window window = (Window) DIALOGS.get(0);
            window.removeWindowListener(activationListener);
            DIALOGS.remove(window);
        }
        PopupFactory sharedInstance = PopupFactory.getSharedInstance();
        if (sharedInstance instanceof TinyPopupFactory) {
            PopupFactory.setSharedInstance(((TinyPopupFactory) sharedInstance).storedFactory);
        }
    }
}
