package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.View;
import org.slf4j.Marker;
import sun.security.pkcs11.wrapper.Constants;
import sun.swing.MenuItemCheckIconFactory;
import sun.swing.MenuItemLayoutHelper;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuItemUI.class */
public class BasicMenuItemUI extends MenuItemUI {
    protected Color selectionBackground;
    protected Color selectionForeground;
    protected Color disabledForeground;
    protected Color acceleratorForeground;
    protected Color acceleratorSelectionForeground;
    protected String acceleratorDelimiter;
    protected int defaultTextIconGap;
    protected Font acceleratorFont;
    protected MouseInputListener mouseInputListener;
    protected MenuDragMouseListener menuDragMouseListener;
    protected MenuKeyListener menuKeyListener;
    protected PropertyChangeListener propertyChangeListener;
    Handler handler;
    protected boolean oldBorderPainted;
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;
    protected JMenuItem menuItem = null;
    protected Icon arrowIcon = null;
    protected Icon checkIcon = null;

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("doClick"));
        BasicLookAndFeel.installAudioActionMap(lazyActionMap);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicMenuItemUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.menuItem = (JMenuItem) jComponent;
        installDefaults();
        installComponents(this.menuItem);
        installListeners();
        installKeyboardActions();
    }

    protected void installDefaults() {
        String propertyPrefix = getPropertyPrefix();
        this.acceleratorFont = UIManager.getFont("MenuItem.acceleratorFont");
        if (this.acceleratorFont == null) {
            this.acceleratorFont = UIManager.getFont("MenuItem.font");
        }
        Object obj = UIManager.get(getPropertyPrefix() + ".opaque");
        if (obj != null) {
            LookAndFeel.installProperty(this.menuItem, "opaque", obj);
        } else {
            LookAndFeel.installProperty(this.menuItem, "opaque", Boolean.TRUE);
        }
        if (this.menuItem.getMargin() == null || (this.menuItem.getMargin() instanceof UIResource)) {
            this.menuItem.setMargin(UIManager.getInsets(propertyPrefix + ".margin"));
        }
        LookAndFeel.installProperty(this.menuItem, "iconTextGap", 4);
        this.defaultTextIconGap = this.menuItem.getIconTextGap();
        LookAndFeel.installBorder(this.menuItem, propertyPrefix + ".border");
        this.oldBorderPainted = this.menuItem.isBorderPainted();
        LookAndFeel.installProperty(this.menuItem, AbstractButton.BORDER_PAINTED_CHANGED_PROPERTY, Boolean.valueOf(UIManager.getBoolean(propertyPrefix + ".borderPainted")));
        LookAndFeel.installColorsAndFont(this.menuItem, propertyPrefix + ".background", propertyPrefix + ".foreground", propertyPrefix + ".font");
        if (this.selectionBackground == null || (this.selectionBackground instanceof UIResource)) {
            this.selectionBackground = UIManager.getColor(propertyPrefix + ".selectionBackground");
        }
        if (this.selectionForeground == null || (this.selectionForeground instanceof UIResource)) {
            this.selectionForeground = UIManager.getColor(propertyPrefix + ".selectionForeground");
        }
        if (this.disabledForeground == null || (this.disabledForeground instanceof UIResource)) {
            this.disabledForeground = UIManager.getColor(propertyPrefix + ".disabledForeground");
        }
        if (this.acceleratorForeground == null || (this.acceleratorForeground instanceof UIResource)) {
            this.acceleratorForeground = UIManager.getColor(propertyPrefix + ".acceleratorForeground");
        }
        if (this.acceleratorSelectionForeground == null || (this.acceleratorSelectionForeground instanceof UIResource)) {
            this.acceleratorSelectionForeground = UIManager.getColor(propertyPrefix + ".acceleratorSelectionForeground");
        }
        this.acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");
        if (this.acceleratorDelimiter == null) {
            this.acceleratorDelimiter = Marker.ANY_NON_NULL_MARKER;
        }
        if (this.arrowIcon == null || (this.arrowIcon instanceof UIResource)) {
            this.arrowIcon = UIManager.getIcon(propertyPrefix + ".arrowIcon");
        }
        updateCheckIcon();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCheckIcon() {
        MenuItemCheckIconFactory menuItemCheckIconFactory;
        String propertyPrefix = getPropertyPrefix();
        if (this.checkIcon == null || (this.checkIcon instanceof UIResource)) {
            this.checkIcon = UIManager.getIcon(propertyPrefix + ".checkIcon");
            if (MenuItemLayoutHelper.isColumnLayout(BasicGraphicsUtils.isLeftToRight(this.menuItem), this.menuItem) && (menuItemCheckIconFactory = (MenuItemCheckIconFactory) UIManager.get(propertyPrefix + ".checkIconFactory")) != null && MenuItemLayoutHelper.useCheckAndArrow(this.menuItem) && menuItemCheckIconFactory.isCompatible(this.checkIcon, propertyPrefix)) {
                this.checkIcon = menuItemCheckIconFactory.getIcon(this.menuItem);
            }
        }
    }

    protected void installComponents(JMenuItem jMenuItem) {
        BasicHTML.updateRenderer(jMenuItem, jMenuItem.getText());
    }

    protected String getPropertyPrefix() {
        return "MenuItem";
    }

    protected void installListeners() {
        MouseInputListener mouseInputListenerCreateMouseInputListener = createMouseInputListener(this.menuItem);
        this.mouseInputListener = mouseInputListenerCreateMouseInputListener;
        if (mouseInputListenerCreateMouseInputListener != null) {
            this.menuItem.addMouseListener(this.mouseInputListener);
            this.menuItem.addMouseMotionListener(this.mouseInputListener);
        }
        MenuDragMouseListener menuDragMouseListenerCreateMenuDragMouseListener = createMenuDragMouseListener(this.menuItem);
        this.menuDragMouseListener = menuDragMouseListenerCreateMenuDragMouseListener;
        if (menuDragMouseListenerCreateMenuDragMouseListener != null) {
            this.menuItem.addMenuDragMouseListener(this.menuDragMouseListener);
        }
        MenuKeyListener menuKeyListenerCreateMenuKeyListener = createMenuKeyListener(this.menuItem);
        this.menuKeyListener = menuKeyListenerCreateMenuKeyListener;
        if (menuKeyListenerCreateMenuKeyListener != null) {
            this.menuItem.addMenuKeyListener(this.menuKeyListener);
        }
        PropertyChangeListener propertyChangeListenerCreatePropertyChangeListener = createPropertyChangeListener(this.menuItem);
        this.propertyChangeListener = propertyChangeListenerCreatePropertyChangeListener;
        if (propertyChangeListenerCreatePropertyChangeListener != null) {
            this.menuItem.addPropertyChangeListener(this.propertyChangeListener);
        }
    }

    protected void installKeyboardActions() {
        installLazyActionMap();
        updateAcceleratorBinding();
    }

    void installLazyActionMap() {
        LazyActionMap.installLazyActionMap(this.menuItem, BasicMenuItemUI.class, getPropertyPrefix() + ".actionMap");
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        this.menuItem = (JMenuItem) jComponent;
        uninstallDefaults();
        uninstallComponents(this.menuItem);
        uninstallListeners();
        uninstallKeyboardActions();
        MenuItemLayoutHelper.clearUsedParentClientProperties(this.menuItem);
        this.menuItem = null;
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.menuItem);
        LookAndFeel.installProperty(this.menuItem, AbstractButton.BORDER_PAINTED_CHANGED_PROPERTY, Boolean.valueOf(this.oldBorderPainted));
        if (this.menuItem.getMargin() instanceof UIResource) {
            this.menuItem.setMargin(null);
        }
        if (this.arrowIcon instanceof UIResource) {
            this.arrowIcon = null;
        }
        if (this.checkIcon instanceof UIResource) {
            this.checkIcon = null;
        }
    }

    protected void uninstallComponents(JMenuItem jMenuItem) {
        BasicHTML.updateRenderer(jMenuItem, "");
    }

    protected void uninstallListeners() {
        if (this.mouseInputListener != null) {
            this.menuItem.removeMouseListener(this.mouseInputListener);
            this.menuItem.removeMouseMotionListener(this.mouseInputListener);
        }
        if (this.menuDragMouseListener != null) {
            this.menuItem.removeMenuDragMouseListener(this.menuDragMouseListener);
        }
        if (this.menuKeyListener != null) {
            this.menuItem.removeMenuKeyListener(this.menuKeyListener);
        }
        if (this.propertyChangeListener != null) {
            this.menuItem.removePropertyChangeListener(this.propertyChangeListener);
        }
        this.mouseInputListener = null;
        this.menuDragMouseListener = null;
        this.menuKeyListener = null;
        this.propertyChangeListener = null;
        this.handler = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.menuItem, null);
        SwingUtilities.replaceUIInputMap(this.menuItem, 2, null);
    }

    protected MouseInputListener createMouseInputListener(JComponent jComponent) {
        return getHandler();
    }

    protected MenuDragMouseListener createMenuDragMouseListener(JComponent jComponent) {
        return getHandler();
    }

    protected MenuKeyListener createMenuKeyListener(JComponent jComponent) {
        return null;
    }

    protected PropertyChangeListener createPropertyChangeListener(JComponent jComponent) {
        return getHandler();
    }

    Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    InputMap createInputMap(int i2) {
        if (i2 == 2) {
            return new ComponentInputMapUIResource(this.menuItem);
        }
        return null;
    }

    void updateAcceleratorBinding() {
        KeyStroke accelerator = this.menuItem.getAccelerator();
        InputMap uIInputMap = SwingUtilities.getUIInputMap(this.menuItem, 2);
        if (uIInputMap != null) {
            uIInputMap.clear();
        }
        if (accelerator != null) {
            if (uIInputMap == null) {
                uIInputMap = createInputMap(2);
                SwingUtilities.replaceUIInputMap(this.menuItem, 2, uIInputMap);
            }
            uIInputMap.put(accelerator, "doClick");
            int modifiers = accelerator.getModifiers();
            if ((modifiers & 512) != 0 && (modifiers & 8192) != 0) {
                uIInputMap.put(KeyStroke.getKeyStroke(accelerator.getKeyCode(), modifiers & (-8193) & (-33), accelerator.isOnKeyRelease()), "doClick");
            } else if ((modifiers & 512) != 0 && (modifiers & 8192) == 0) {
                uIInputMap.put(KeyStroke.getKeyStroke(accelerator.getKeyCode(), modifiers | 8192, accelerator.isOnKeyRelease()), "doClick");
            } else if ((modifiers & 8192) != 0) {
                int i2 = (modifiers & (-8193) & (-33)) | 512;
                uIInputMap.put(KeyStroke.getKeyStroke(accelerator.getKeyCode(), i2, accelerator.isOnKeyRelease()), "doClick");
                uIInputMap.put(KeyStroke.getKeyStroke(accelerator.getKeyCode(), i2 | 8192, accelerator.isOnKeyRelease()), "doClick");
            }
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension preferredSize = null;
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            preferredSize = getPreferredSize(jComponent);
            preferredSize.width = (int) (preferredSize.width - (view.getPreferredSpan(0) - view.getMinimumSpan(0)));
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return getPreferredMenuItemSize(jComponent, this.checkIcon, this.arrowIcon, this.defaultTextIconGap);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension preferredSize = null;
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            preferredSize = getPreferredSize(jComponent);
            preferredSize.width = (int) (preferredSize.width + (view.getMaximumSpan(0) - view.getPreferredSpan(0)));
        }
        return preferredSize;
    }

    protected Dimension getPreferredMenuItemSize(JComponent jComponent, Icon icon, Icon icon2, int i2) {
        JMenuItem jMenuItem = (JMenuItem) jComponent;
        MenuItemLayoutHelper menuItemLayoutHelper = new MenuItemLayoutHelper(jMenuItem, icon, icon2, MenuItemLayoutHelper.createMaxRect(), i2, this.acceleratorDelimiter, BasicGraphicsUtils.isLeftToRight(jMenuItem), jMenuItem.getFont(), this.acceleratorFont, MenuItemLayoutHelper.useCheckAndArrow(this.menuItem), getPropertyPrefix());
        Dimension dimension = new Dimension();
        dimension.width = menuItemLayoutHelper.getLeadingGap();
        MenuItemLayoutHelper.addMaxWidth(menuItemLayoutHelper.getCheckSize(), menuItemLayoutHelper.getAfterCheckIconGap(), dimension);
        if (!menuItemLayoutHelper.isTopLevelMenu() && menuItemLayoutHelper.getMinTextOffset() > 0 && dimension.width < menuItemLayoutHelper.getMinTextOffset()) {
            dimension.width = menuItemLayoutHelper.getMinTextOffset();
        }
        MenuItemLayoutHelper.addMaxWidth(menuItemLayoutHelper.getLabelSize(), menuItemLayoutHelper.getGap(), dimension);
        MenuItemLayoutHelper.addMaxWidth(menuItemLayoutHelper.getAccSize(), menuItemLayoutHelper.getGap(), dimension);
        MenuItemLayoutHelper.addMaxWidth(menuItemLayoutHelper.getArrowSize(), menuItemLayoutHelper.getGap(), dimension);
        dimension.height = MenuItemLayoutHelper.max(menuItemLayoutHelper.getCheckSize().getHeight(), menuItemLayoutHelper.getLabelSize().getHeight(), menuItemLayoutHelper.getAccSize().getHeight(), menuItemLayoutHelper.getArrowSize().getHeight());
        Insets insets = menuItemLayoutHelper.getMenuItem().getInsets();
        if (insets != null) {
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
        }
        if (dimension.width % 2 == 0) {
            dimension.width++;
        }
        if (dimension.height % 2 == 0 && Boolean.TRUE != UIManager.get(getPropertyPrefix() + ".evenHeight")) {
            dimension.height++;
        }
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        paint(graphics, jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        paintMenuItem(graphics, jComponent, this.checkIcon, this.arrowIcon, this.selectionBackground, this.selectionForeground, this.defaultTextIconGap);
    }

    protected void paintMenuItem(Graphics graphics, JComponent jComponent, Icon icon, Icon icon2, Color color, Color color2, int i2) {
        Font font = graphics.getFont();
        Color color3 = graphics.getColor();
        JMenuItem jMenuItem = (JMenuItem) jComponent;
        graphics.setFont(jMenuItem.getFont());
        Rectangle rectangle = new Rectangle(0, 0, jMenuItem.getWidth(), jMenuItem.getHeight());
        applyInsets(rectangle, jMenuItem.getInsets());
        MenuItemLayoutHelper menuItemLayoutHelper = new MenuItemLayoutHelper(jMenuItem, icon, icon2, rectangle, i2, this.acceleratorDelimiter, BasicGraphicsUtils.isLeftToRight(jMenuItem), jMenuItem.getFont(), this.acceleratorFont, MenuItemLayoutHelper.useCheckAndArrow(this.menuItem), getPropertyPrefix());
        MenuItemLayoutHelper.LayoutResult layoutResultLayoutMenuItem = menuItemLayoutHelper.layoutMenuItem();
        paintBackground(graphics, jMenuItem, color);
        paintCheckIcon(graphics, menuItemLayoutHelper, layoutResultLayoutMenuItem, color3, color2);
        paintIcon(graphics, menuItemLayoutHelper, layoutResultLayoutMenuItem, color3);
        paintText(graphics, menuItemLayoutHelper, layoutResultLayoutMenuItem);
        paintAccText(graphics, menuItemLayoutHelper, layoutResultLayoutMenuItem);
        paintArrowIcon(graphics, menuItemLayoutHelper, layoutResultLayoutMenuItem, color2);
        graphics.setColor(color3);
        graphics.setFont(font);
    }

    private void paintIcon(Graphics graphics, MenuItemLayoutHelper menuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult, Color color) {
        Icon icon;
        if (menuItemLayoutHelper.getIcon() != null) {
            ButtonModel model = menuItemLayoutHelper.getMenuItem().getModel();
            if (!model.isEnabled()) {
                icon = menuItemLayoutHelper.getMenuItem().getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                icon = menuItemLayoutHelper.getMenuItem().getPressedIcon();
                if (icon == null) {
                    icon = menuItemLayoutHelper.getMenuItem().getIcon();
                }
            } else {
                icon = menuItemLayoutHelper.getMenuItem().getIcon();
            }
            if (icon != null) {
                icon.paintIcon(menuItemLayoutHelper.getMenuItem(), graphics, layoutResult.getIconRect().f12372x, layoutResult.getIconRect().f12373y);
                graphics.setColor(color);
            }
        }
    }

    private void paintCheckIcon(Graphics graphics, MenuItemLayoutHelper menuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult, Color color, Color color2) {
        if (menuItemLayoutHelper.getCheckIcon() != null) {
            ButtonModel model = menuItemLayoutHelper.getMenuItem().getModel();
            if (model.isArmed() || ((menuItemLayoutHelper.getMenuItem() instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color2);
            } else {
                graphics.setColor(color);
            }
            if (menuItemLayoutHelper.useCheckAndArrow()) {
                menuItemLayoutHelper.getCheckIcon().paintIcon(menuItemLayoutHelper.getMenuItem(), graphics, layoutResult.getCheckRect().f12372x, layoutResult.getCheckRect().f12373y);
            }
            graphics.setColor(color);
        }
    }

    private void paintAccText(Graphics graphics, MenuItemLayoutHelper menuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult) {
        if (!menuItemLayoutHelper.getAccText().equals("")) {
            ButtonModel model = menuItemLayoutHelper.getMenuItem().getModel();
            graphics.setFont(menuItemLayoutHelper.getAccFontMetrics().getFont());
            if (!model.isEnabled()) {
                if (this.disabledForeground != null) {
                    graphics.setColor(this.disabledForeground);
                    SwingUtilities2.drawString(menuItemLayoutHelper.getMenuItem(), graphics, menuItemLayoutHelper.getAccText(), layoutResult.getAccRect().f12372x, layoutResult.getAccRect().f12373y + menuItemLayoutHelper.getAccFontMetrics().getAscent());
                    return;
                } else {
                    graphics.setColor(menuItemLayoutHelper.getMenuItem().getBackground().brighter());
                    SwingUtilities2.drawString(menuItemLayoutHelper.getMenuItem(), graphics, menuItemLayoutHelper.getAccText(), layoutResult.getAccRect().f12372x, layoutResult.getAccRect().f12373y + menuItemLayoutHelper.getAccFontMetrics().getAscent());
                    graphics.setColor(menuItemLayoutHelper.getMenuItem().getBackground().darker());
                    SwingUtilities2.drawString(menuItemLayoutHelper.getMenuItem(), graphics, menuItemLayoutHelper.getAccText(), layoutResult.getAccRect().f12372x - 1, (layoutResult.getAccRect().f12373y + menuItemLayoutHelper.getFontMetrics().getAscent()) - 1);
                    return;
                }
            }
            if (model.isArmed() || ((menuItemLayoutHelper.getMenuItem() instanceof JMenu) && model.isSelected())) {
                graphics.setColor(this.acceleratorSelectionForeground);
            } else {
                graphics.setColor(this.acceleratorForeground);
            }
            SwingUtilities2.drawString(menuItemLayoutHelper.getMenuItem(), graphics, menuItemLayoutHelper.getAccText(), layoutResult.getAccRect().f12372x, layoutResult.getAccRect().f12373y + menuItemLayoutHelper.getAccFontMetrics().getAscent());
        }
    }

    private void paintText(Graphics graphics, MenuItemLayoutHelper menuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult) {
        if (!menuItemLayoutHelper.getText().equals("")) {
            if (menuItemLayoutHelper.getHtmlView() != null) {
                menuItemLayoutHelper.getHtmlView().paint(graphics, layoutResult.getTextRect());
            } else {
                paintText(graphics, menuItemLayoutHelper.getMenuItem(), layoutResult.getTextRect(), menuItemLayoutHelper.getText());
            }
        }
    }

    private void paintArrowIcon(Graphics graphics, MenuItemLayoutHelper menuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult layoutResult, Color color) {
        if (menuItemLayoutHelper.getArrowIcon() != null) {
            ButtonModel model = menuItemLayoutHelper.getMenuItem().getModel();
            if (model.isArmed() || ((menuItemLayoutHelper.getMenuItem() instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color);
            }
            if (menuItemLayoutHelper.useCheckAndArrow()) {
                menuItemLayoutHelper.getArrowIcon().paintIcon(menuItemLayoutHelper.getMenuItem(), graphics, layoutResult.getArrowRect().f12372x, layoutResult.getArrowRect().f12373y);
            }
        }
    }

    private void applyInsets(Rectangle rectangle, Insets insets) {
        if (insets != null) {
            rectangle.f12372x += insets.left;
            rectangle.f12373y += insets.top;
            rectangle.width -= insets.right + rectangle.f12372x;
            rectangle.height -= insets.bottom + rectangle.f12373y;
        }
    }

    protected void paintBackground(Graphics graphics, JMenuItem jMenuItem, Color color) {
        ButtonModel model = jMenuItem.getModel();
        Color color2 = graphics.getColor();
        int width = jMenuItem.getWidth();
        int height = jMenuItem.getHeight();
        if (jMenuItem.isOpaque()) {
            if (model.isArmed() || ((jMenuItem instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color);
                graphics.fillRect(0, 0, width, height);
            } else {
                graphics.setColor(jMenuItem.getBackground());
                graphics.fillRect(0, 0, width, height);
            }
            graphics.setColor(color2);
            return;
        }
        if (model.isArmed() || ((jMenuItem instanceof JMenu) && model.isSelected())) {
            graphics.setColor(color);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(color2);
        }
    }

    protected void paintText(Graphics graphics, JMenuItem jMenuItem, Rectangle rectangle, String str) {
        ButtonModel model = jMenuItem.getModel();
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jMenuItem, graphics);
        int displayedMnemonicIndex = jMenuItem.getDisplayedMnemonicIndex();
        if (!model.isEnabled()) {
            if (UIManager.get("MenuItem.disabledForeground") instanceof Color) {
                graphics.setColor(UIManager.getColor("MenuItem.disabledForeground"));
                SwingUtilities2.drawStringUnderlineCharAt(jMenuItem, graphics, str, displayedMnemonicIndex, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
                return;
            } else {
                graphics.setColor(jMenuItem.getBackground().brighter());
                SwingUtilities2.drawStringUnderlineCharAt(jMenuItem, graphics, str, displayedMnemonicIndex, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
                graphics.setColor(jMenuItem.getBackground().darker());
                SwingUtilities2.drawStringUnderlineCharAt(jMenuItem, graphics, str, displayedMnemonicIndex, rectangle.f12372x - 1, (rectangle.f12373y + fontMetrics.getAscent()) - 1);
                return;
            }
        }
        if (model.isArmed() || ((jMenuItem instanceof JMenu) && model.isSelected())) {
            graphics.setColor(this.selectionForeground);
        }
        SwingUtilities2.drawStringUnderlineCharAt(jMenuItem, graphics, str, displayedMnemonicIndex, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
    }

    public MenuElement[] getPath() {
        MenuElement[] menuElementArr;
        MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
        int length = selectedPath.length;
        if (length == 0) {
            return new MenuElement[0];
        }
        Container parent = this.menuItem.getParent();
        if (selectedPath[length - 1].getComponent() == parent) {
            menuElementArr = new MenuElement[length + 1];
            System.arraycopy(selectedPath, 0, menuElementArr, 0, length);
            menuElementArr[length] = this.menuItem;
        } else {
            int length2 = selectedPath.length - 1;
            while (length2 >= 0 && selectedPath[length2].getComponent() != parent) {
                length2--;
            }
            menuElementArr = new MenuElement[length2 + 2];
            System.arraycopy(selectedPath, 0, menuElementArr, 0, length2 + 1);
            menuElementArr[length2 + 1] = this.menuItem;
        }
        return menuElementArr;
    }

    void printMenuElementArray(MenuElement[] menuElementArr, boolean z2) {
        System.out.println("Path is(");
        int length = menuElementArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            for (int i3 = 0; i3 <= i2; i3++) {
                System.out.print(Constants.INDENT);
            }
            MenuElement menuElement = menuElementArr[i2];
            if (menuElement instanceof JMenuItem) {
                System.out.println(((JMenuItem) menuElement).getText() + ", ");
            } else if (menuElement == null) {
                System.out.println("NULL , ");
            } else {
                System.out.println("" + ((Object) menuElement) + ", ");
            }
        }
        System.out.println(")");
        if (z2) {
            Thread.dumpStack();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuItemUI$MouseInputHandler.class */
    protected class MouseInputHandler implements MouseInputListener {
        protected MouseInputHandler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            BasicMenuItemUI.this.getHandler().mouseClicked(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            BasicMenuItemUI.this.getHandler().mousePressed(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicMenuItemUI.this.getHandler().mouseReleased(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            BasicMenuItemUI.this.getHandler().mouseEntered(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            BasicMenuItemUI.this.getHandler().mouseExited(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            BasicMenuItemUI.this.getHandler().mouseDragged(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicMenuItemUI.this.getHandler().mouseMoved(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuItemUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String CLICK = "doClick";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JMenuItem jMenuItem = (JMenuItem) actionEvent.getSource();
            MenuSelectionManager.defaultManager().clearSelectedPath();
            jMenuItem.doClick();
        }
    }

    protected void doClick(MenuSelectionManager menuSelectionManager) {
        if (!isInternalFrameSystemMenu()) {
            BasicLookAndFeel.playSound(this.menuItem, getPropertyPrefix() + ".commandSound");
        }
        if (menuSelectionManager == null) {
            menuSelectionManager = MenuSelectionManager.defaultManager();
        }
        menuSelectionManager.clearSelectedPath();
        this.menuItem.doClick(0);
    }

    private boolean isInternalFrameSystemMenu() {
        String actionCommand = this.menuItem.getActionCommand();
        if (actionCommand == "Close" || actionCommand == "Minimize" || actionCommand == "Restore" || actionCommand == "Maximize") {
            return true;
        }
        return false;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuItemUI$Handler.class */
    class Handler implements MenuDragMouseListener, MouseInputListener, PropertyChangeListener {
        Handler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (!BasicMenuItemUI.this.menuItem.isEnabled()) {
                return;
            }
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            Point point = mouseEvent.getPoint();
            if (point.f12370x >= 0 && point.f12370x < BasicMenuItemUI.this.menuItem.getWidth() && point.f12371y >= 0 && point.f12371y < BasicMenuItemUI.this.menuItem.getHeight()) {
                BasicMenuItemUI.this.doClick(menuSelectionManagerDefaultManager);
            } else {
                menuSelectionManagerDefaultManager.processMouseEvent(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            if ((mouseEvent.getModifiers() & 28) != 0) {
                MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
            } else {
                menuSelectionManagerDefaultManager.setSelectedPath(BasicMenuItemUI.this.getPath());
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            if ((mouseEvent.getModifiers() & 28) != 0) {
                MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
                return;
            }
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (selectedPath.length > 1 && selectedPath[selectedPath.length - 1] == BasicMenuItemUI.this.menuItem) {
                MenuElement[] menuElementArr = new MenuElement[selectedPath.length - 1];
                int length = selectedPath.length - 1;
                for (int i2 = 0; i2 < length; i2++) {
                    menuElementArr[i2] = selectedPath[i2];
                }
                menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseEntered(MenuDragMouseEvent menuDragMouseEvent) {
            menuDragMouseEvent.getMenuSelectionManager().setSelectedPath(menuDragMouseEvent.getPath());
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseDragged(MenuDragMouseEvent menuDragMouseEvent) {
            menuDragMouseEvent.getMenuSelectionManager().setSelectedPath(menuDragMouseEvent.getPath());
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseExited(MenuDragMouseEvent menuDragMouseEvent) {
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseReleased(MenuDragMouseEvent menuDragMouseEvent) {
            if (!BasicMenuItemUI.this.menuItem.isEnabled()) {
                return;
            }
            MenuSelectionManager menuSelectionManager = menuDragMouseEvent.getMenuSelectionManager();
            menuDragMouseEvent.getPath();
            Point point = menuDragMouseEvent.getPoint();
            if (point.f12370x >= 0 && point.f12370x < BasicMenuItemUI.this.menuItem.getWidth() && point.f12371y >= 0 && point.f12371y < BasicMenuItemUI.this.menuItem.getHeight()) {
                BasicMenuItemUI.this.doClick(menuSelectionManager);
            } else {
                menuSelectionManager.clearSelectedPath();
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == "labelFor" || propertyName == "displayedMnemonic" || propertyName == "accelerator") {
                BasicMenuItemUI.this.updateAcceleratorBinding();
                return;
            }
            if (propertyName == "text" || "font" == propertyName || "foreground" == propertyName) {
                JMenuItem jMenuItem = (JMenuItem) propertyChangeEvent.getSource();
                BasicHTML.updateRenderer(jMenuItem, jMenuItem.getText());
            } else if (propertyName == "iconTextGap") {
                BasicMenuItemUI.this.defaultTextIconGap = ((Number) propertyChangeEvent.getNewValue()).intValue();
            } else if (propertyName == AbstractButton.HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY) {
                BasicMenuItemUI.this.updateCheckIcon();
            }
        }
    }
}
