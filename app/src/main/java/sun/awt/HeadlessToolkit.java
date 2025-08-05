package sun.awt;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.JobAttributes;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.PageAttributes;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.PrintJob;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.AWTEventListener;
import java.awt.im.InputMethodHighlight;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.peer.ButtonPeer;
import java.awt.peer.CanvasPeer;
import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.CheckboxPeer;
import java.awt.peer.ChoicePeer;
import java.awt.peer.DesktopPeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.FileDialogPeer;
import java.awt.peer.FontPeer;
import java.awt.peer.FramePeer;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.awt.peer.LabelPeer;
import java.awt.peer.ListPeer;
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.PopupMenuPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.ScrollPanePeer;
import java.awt.peer.ScrollbarPeer;
import java.awt.peer.SystemTrayPeer;
import java.awt.peer.TextAreaPeer;
import java.awt.peer.TextFieldPeer;
import java.awt.peer.TrayIconPeer;
import java.awt.peer.WindowPeer;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import sun.awt.datatransfer.DataTransferer;

/* loaded from: rt.jar:sun/awt/HeadlessToolkit.class */
public class HeadlessToolkit extends Toolkit implements ComponentFactory, KeyboardFocusManagerPeerProvider {
    private static final KeyboardFocusManagerPeer kfmPeer = new KeyboardFocusManagerPeer() { // from class: sun.awt.HeadlessToolkit.1
        @Override // java.awt.peer.KeyboardFocusManagerPeer
        public void setCurrentFocusedWindow(Window window) {
        }

        @Override // java.awt.peer.KeyboardFocusManagerPeer
        public Window getCurrentFocusedWindow() {
            return null;
        }

        @Override // java.awt.peer.KeyboardFocusManagerPeer
        public void setCurrentFocusOwner(Component component) {
        }

        @Override // java.awt.peer.KeyboardFocusManagerPeer
        public Component getCurrentFocusOwner() {
            return null;
        }

        @Override // java.awt.peer.KeyboardFocusManagerPeer
        public void clearGlobalFocusOwner(Window window) {
        }
    };
    private Toolkit tk;
    private ComponentFactory componentFactory;

    /* JADX WARN: Multi-variable type inference failed */
    public HeadlessToolkit(Toolkit toolkit) {
        this.tk = toolkit;
        if (toolkit instanceof ComponentFactory) {
            this.componentFactory = (ComponentFactory) toolkit;
        }
    }

    public Toolkit getUnderlyingToolkit() {
        return this.tk;
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public CanvasPeer createCanvas(Canvas canvas) {
        return (CanvasPeer) createComponent(canvas);
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public PanelPeer createPanel(Panel panel) {
        return (PanelPeer) createComponent(panel);
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public WindowPeer createWindow(Window window) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public FramePeer createFrame(Frame frame) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public DialogPeer createDialog(Dialog dialog) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public ButtonPeer createButton(Button button) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public TextFieldPeer createTextField(TextField textField) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public ChoicePeer createChoice(Choice choice) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public LabelPeer createLabel(Label label) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public ListPeer createList(List list) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public CheckboxPeer createCheckbox(Checkbox checkbox) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public ScrollbarPeer createScrollbar(Scrollbar scrollbar) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public ScrollPanePeer createScrollPane(ScrollPane scrollPane) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public TextAreaPeer createTextArea(TextArea textArea) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public FileDialogPeer createFileDialog(FileDialog fileDialog) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public MenuBarPeer createMenuBar(MenuBar menuBar) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public MenuPeer createMenu(Menu menu) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public PopupMenuPeer createPopupMenu(PopupMenu popupMenu) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public MenuItemPeer createMenuItem(MenuItem menuItem) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem checkboxMenuItem) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException {
        throw new InvalidDnDOperationException("Headless environment");
    }

    @Override // sun.awt.ComponentFactory
    public RobotPeer createRobot(Robot robot, GraphicsDevice graphicsDevice) throws AWTException, HeadlessException {
        throw new HeadlessException();
    }

    @Override // sun.awt.KeyboardFocusManagerPeerProvider
    public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer() {
        return kfmPeer;
    }

    public TrayIconPeer createTrayIcon(TrayIcon trayIcon) throws HeadlessException {
        throw new HeadlessException();
    }

    public SystemTrayPeer createSystemTray(SystemTray systemTray) throws HeadlessException {
        throw new HeadlessException();
    }

    public boolean isTraySupported() {
        return false;
    }

    public GlobalCursorManager getGlobalCursorManager() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    protected void loadSystemColors(int[] iArr) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public ColorModel getColorModel() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public int getScreenResolution() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public Map mapInputMethodHighlight(InputMethodHighlight inputMethodHighlight) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public int getMenuShortcutKeyMask() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public boolean getLockingKeyState(int i2) throws UnsupportedOperationException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public void setLockingKeyState(int i2, boolean z2) throws UnsupportedOperationException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public Cursor createCustomCursor(Image image, Point point, String str) throws IndexOutOfBoundsException, HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public Dimension getBestCursorSize(int i2, int i3) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public int getMaximumCursorColors() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> cls, DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
        return null;
    }

    public int getScreenHeight() throws HeadlessException {
        throw new HeadlessException();
    }

    public int getScreenWidth() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public Dimension getScreenSize() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public Insets getScreenInsets(GraphicsConfiguration graphicsConfiguration) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public void setDynamicLayout(boolean z2) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    protected boolean isDynamicLayoutSet() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public boolean isDynamicLayoutActive() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public Clipboard getSystemClipboard() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public PrintJob getPrintJob(Frame frame, String str, JobAttributes jobAttributes, PageAttributes pageAttributes) {
        if (frame != null) {
            throw new HeadlessException();
        }
        throw new NullPointerException("frame must not be null");
    }

    @Override // java.awt.Toolkit
    public PrintJob getPrintJob(Frame frame, String str, Properties properties) {
        if (frame != null) {
            throw new HeadlessException();
        }
        throw new NullPointerException("frame must not be null");
    }

    @Override // java.awt.Toolkit
    public void sync() {
    }

    @Override // java.awt.Toolkit
    public void beep() {
        System.out.write(7);
    }

    @Override // java.awt.Toolkit
    public EventQueue getSystemEventQueueImpl() {
        return SunToolkit.getSystemEventQueueImplPP();
    }

    @Override // java.awt.Toolkit
    public int checkImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return this.tk.checkImage(image, i2, i3, imageObserver);
    }

    @Override // java.awt.Toolkit
    public boolean prepareImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return this.tk.prepareImage(image, i2, i3, imageObserver);
    }

    @Override // java.awt.Toolkit
    public Image getImage(String str) {
        return this.tk.getImage(str);
    }

    @Override // java.awt.Toolkit
    public Image getImage(URL url) {
        return this.tk.getImage(url);
    }

    @Override // java.awt.Toolkit
    public Image createImage(String str) {
        return this.tk.createImage(str);
    }

    @Override // java.awt.Toolkit
    public Image createImage(URL url) {
        return this.tk.createImage(url);
    }

    @Override // java.awt.Toolkit
    public Image createImage(byte[] bArr, int i2, int i3) {
        return this.tk.createImage(bArr, i2, i3);
    }

    @Override // java.awt.Toolkit
    public Image createImage(ImageProducer imageProducer) {
        return this.tk.createImage(imageProducer);
    }

    @Override // java.awt.Toolkit
    public Image createImage(byte[] bArr) {
        return this.tk.createImage(bArr);
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public FontPeer getFontPeer(String str, int i2) {
        if (this.componentFactory != null) {
            return this.componentFactory.getFontPeer(str, i2);
        }
        return null;
    }

    @Override // sun.awt.ComponentFactory
    public DataTransferer getDataTransferer() {
        return null;
    }

    @Override // java.awt.Toolkit
    public FontMetrics getFontMetrics(Font font) {
        return this.tk.getFontMetrics(font);
    }

    @Override // java.awt.Toolkit
    public String[] getFontList() {
        return this.tk.getFontList();
    }

    @Override // java.awt.Toolkit
    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.tk.addPropertyChangeListener(str, propertyChangeListener);
    }

    @Override // java.awt.Toolkit
    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.tk.removePropertyChangeListener(str, propertyChangeListener);
    }

    @Override // java.awt.Toolkit
    public boolean isModalityTypeSupported(Dialog.ModalityType modalityType) {
        return false;
    }

    @Override // java.awt.Toolkit
    public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType modalExclusionType) {
        return false;
    }

    @Override // java.awt.Toolkit
    public boolean isAlwaysOnTopSupported() {
        return false;
    }

    @Override // java.awt.Toolkit
    public void addAWTEventListener(AWTEventListener aWTEventListener, long j2) {
        this.tk.addAWTEventListener(aWTEventListener, j2);
    }

    @Override // java.awt.Toolkit
    public void removeAWTEventListener(AWTEventListener aWTEventListener) {
        this.tk.removeAWTEventListener(aWTEventListener);
    }

    @Override // java.awt.Toolkit
    public AWTEventListener[] getAWTEventListeners() {
        return this.tk.getAWTEventListeners();
    }

    @Override // java.awt.Toolkit
    public AWTEventListener[] getAWTEventListeners(long j2) {
        return this.tk.getAWTEventListeners(j2);
    }

    public boolean isDesktopSupported() {
        return false;
    }

    @Override // java.awt.Toolkit
    public DesktopPeer createDesktopPeer(Desktop desktop) throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.Toolkit
    public boolean areExtraMouseButtonsEnabled() throws HeadlessException {
        throw new HeadlessException();
    }
}
