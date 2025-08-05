package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyPopupMenuBorder;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.fxml.FXMLLoader;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
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
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import org.slf4j.Marker;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuItemUI.class */
public class TinyMenuItemUI extends MenuItemUI {
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_ICON_GAP = 4;
    private static final int DEFAULT_ACC_GAP = 8;
    private static final int DEFAULT_ARROW_GAP = 12;
    private static final int CHECK_WIDTH = 14;
    private static final int ARROW_WIDTH = 16;
    protected Color selectionBackground;
    protected Color selectionForeground;
    protected Color disabledForeground;
    protected Color acceleratorForeground;
    protected Color acceleratorSelectionForeground;
    private String acceleratorDelimiter;
    protected Font acceleratorFont;
    protected MouseInputListener mouseInputListener;
    protected MenuDragMouseListener menuDragMouseListener;
    protected MenuKeyListener menuKeyListener;
    private PropertyChangeListener propertyChangeListener;
    protected boolean oldBorderPainted;
    InputMap windowInputMap;
    public static final String MAX_TEXT_WIDTH = "TinyMenuItemUI.maxTextWidth";
    public static final String MAX_ICON_WIDTH = "TinyMenuItemUI.maxIconWidth";
    public static final String MAX_LABEL_WIDTH = "TinyMenuItemUI.maxLabelWidth";
    public static final String MAX_ACC_WIDTH = "TinyMenuItemUI.maxAccWidth";
    protected static Rectangle zeroRect = new Rectangle(0, 0, 0, 0);
    protected static Rectangle iconRect = new Rectangle();
    protected static Rectangle textRect = new Rectangle();
    protected static Rectangle acceleratorRect = new Rectangle();
    protected static Rectangle checkIconRect = new Rectangle();
    protected static Rectangle arrowIconRect = new Rectangle();
    protected static Rectangle viewRect = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
    protected static Rectangle rect = new Rectangle();
    protected JMenuItem menuItem = null;
    protected Icon arrowIcon = null;
    protected Icon checkIcon = null;

    /* renamed from: de.muntjak.tinylookandfeel.TinyMenuItemUI$1, reason: invalid class name */
    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuItemUI$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuItemUI$ClickAction.class */
    private static class ClickAction extends AbstractAction {
        private ClickAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JMenuItem jMenuItem = (JMenuItem) actionEvent.getSource();
            MenuSelectionManager.defaultManager().clearSelectedPath();
            jMenuItem.doClick();
        }

        ClickAction(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuItemUI$MenuDragMouseHandler.class */
    private class MenuDragMouseHandler implements MenuDragMouseListener {
        private final TinyMenuItemUI this$0;

        private MenuDragMouseHandler(TinyMenuItemUI tinyMenuItemUI) {
            this.this$0 = tinyMenuItemUI;
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseEntered(MenuDragMouseEvent menuDragMouseEvent) {
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
            MenuSelectionManager menuSelectionManager = menuDragMouseEvent.getMenuSelectionManager();
            menuDragMouseEvent.getPath();
            Point point = menuDragMouseEvent.getPoint();
            if (point.f12370x < 0 || point.f12370x >= this.this$0.menuItem.getWidth() || point.f12371y < 0 || point.f12371y >= this.this$0.menuItem.getHeight()) {
                menuSelectionManager.clearSelectedPath();
            } else {
                this.this$0.doClick(menuSelectionManager);
            }
        }

        MenuDragMouseHandler(TinyMenuItemUI tinyMenuItemUI, AnonymousClass1 anonymousClass1) {
            this(tinyMenuItemUI);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuItemUI$MouseInputHandler.class */
    protected class MouseInputHandler implements MouseInputListener {
        private final TinyMenuItemUI this$0;

        protected MouseInputHandler(TinyMenuItemUI tinyMenuItemUI) {
            this.this$0 = tinyMenuItemUI;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            Point point = mouseEvent.getPoint();
            if (point.f12370x < 0 || point.f12370x >= this.this$0.menuItem.getWidth() || point.f12371y < 0 || point.f12371y >= this.this$0.menuItem.getHeight()) {
                menuSelectionManagerDefaultManager.processMouseEvent(mouseEvent);
            } else {
                this.this$0.doClick(menuSelectionManagerDefaultManager);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            if ((mouseEvent.getModifiers() & 28) != 0) {
                MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
            } else {
                menuSelectionManagerDefaultManager.setSelectedPath(this.this$0.getPath());
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
            if (selectedPath.length > 1) {
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
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuItemUI$PropertyChangeHandler.class */
    private class PropertyChangeHandler implements PropertyChangeListener {
        private final TinyMenuItemUI this$0;

        private PropertyChangeHandler(TinyMenuItemUI tinyMenuItemUI) {
            this.this$0 = tinyMenuItemUI;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName.equals("labelFor") || propertyName.equals("displayedMnemonic") || propertyName.equals("accelerator")) {
                this.this$0.updateAcceleratorBinding();
            } else if (propertyName.equals("text") || "font".equals(propertyName) || "foreground".equals(propertyName)) {
                JMenuItem jMenuItem = (JMenuItem) propertyChangeEvent.getSource();
                BasicHTML.updateRenderer(jMenuItem, jMenuItem.getText());
            }
        }

        PropertyChangeHandler(TinyMenuItemUI tinyMenuItemUI, AnonymousClass1 anonymousClass1) {
            this(tinyMenuItemUI);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.menuItem = (JMenuItem) jComponent;
        installDefaults();
        installComponents(this.menuItem);
        installListeners();
        installKeyboardActions();
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
        PropertyChangeListener propertyChangeListenerCreatePropertyChangeListener = createPropertyChangeListener(this.menuItem);
        this.propertyChangeListener = propertyChangeListenerCreatePropertyChangeListener;
        if (propertyChangeListenerCreatePropertyChangeListener != null) {
            this.menuItem.addPropertyChangeListener(this.propertyChangeListener);
        }
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.menuItem, getActionMap());
        updateAcceleratorBinding();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        this.menuItem = (JMenuItem) jComponent;
        uninstallDefaults();
        uninstallComponents(this.menuItem);
        uninstallListeners();
        uninstallKeyboardActions();
        Container parent = this.menuItem.getParent();
        if (parent != null && (parent instanceof JComponent)) {
            JComponent jComponent2 = (JComponent) parent;
            jComponent2.putClientProperty(MAX_ACC_WIDTH, null);
            jComponent2.putClientProperty(MAX_TEXT_WIDTH, null);
            jComponent2.putClientProperty(MAX_ICON_WIDTH, null);
            jComponent2.putClientProperty(MAX_LABEL_WIDTH, null);
        }
        this.menuItem = null;
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.menuItem);
        this.menuItem.setBorderPainted(this.oldBorderPainted);
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
        if (this.propertyChangeListener != null) {
            this.menuItem.removePropertyChangeListener(this.propertyChangeListener);
        }
        this.mouseInputListener = null;
        this.menuDragMouseListener = null;
        this.menuKeyListener = null;
        this.propertyChangeListener = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.menuItem, null);
        if (this.windowInputMap != null) {
            SwingUtilities.replaceUIInputMap(this.menuItem, 2, null);
            this.windowInputMap = null;
        }
    }

    protected MouseInputListener createMouseInputListener(JComponent jComponent) {
        return new MouseInputHandler(this);
    }

    protected MenuDragMouseListener createMenuDragMouseListener(JComponent jComponent) {
        return new MenuDragMouseHandler(this, null);
    }

    protected PropertyChangeListener createPropertyChangeListener(JComponent jComponent) {
        return new PropertyChangeHandler(this, null);
    }

    ActionMap getActionMap() {
        String string = new StringBuffer().append(getPropertyPrefix()).append(".actionMap").toString();
        ActionMap actionMapCreateActionMap = (ActionMap) UIManager.get(string);
        if (actionMapCreateActionMap == null) {
            actionMapCreateActionMap = createActionMap();
            UIManager.getLookAndFeelDefaults().put(string, actionMapCreateActionMap);
        }
        return actionMapCreateActionMap;
    }

    ActionMap createActionMap() {
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        actionMapUIResource.put("doClick", new ClickAction(null));
        return actionMapUIResource;
    }

    InputMap createInputMap(int i2) {
        if (i2 == 2) {
            return new ComponentInputMapUIResource(this.menuItem);
        }
        return null;
    }

    void updateAcceleratorBinding() {
        KeyStroke accelerator = this.menuItem.getAccelerator();
        if (this.windowInputMap != null) {
            this.windowInputMap.clear();
        }
        if (accelerator != null) {
            if (this.windowInputMap == null) {
                this.windowInputMap = createInputMap(2);
                SwingUtilities.replaceUIInputMap(this.menuItem, 2, this.windowInputMap);
            }
            this.windowInputMap.put(accelerator, "doClick");
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
        return Theme.menuAllowTwoIcons.getValue() ? getPreferredMenuItemSizeTwoIcons(jComponent, this.checkIcon, this.arrowIcon) : getPreferredMenuItemSizeOneIcon(jComponent, this.checkIcon, this.arrowIcon);
    }

    protected void paintText(Graphics graphics, JMenuItem jMenuItem, Rectangle rectangle, String str) {
        ButtonModel model = jMenuItem.getModel();
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int displayedMnemonicIndex = jMenuItem.getDisplayedMnemonicIndex();
        if (!model.isEnabled()) {
            if (isTopLevelMenu()) {
                graphics.setColor(Theme.menuDisabledFgColor.getColor());
            } else {
                graphics.setColor(Theme.menuItemDisabledFgColor.getColor());
            }
            TinyUtils.drawStringUnderlineCharAt(jMenuItem, graphics, str, displayedMnemonicIndex, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
            return;
        }
        if (isTopLevelMenu()) {
            if (jMenuItem.getClientProperty("rollover") == Boolean.TRUE && Theme.menuRollover.getValue() && !model.isSelected()) {
                graphics.setColor(Theme.menuRolloverFgColor.getColor());
            } else if (jMenuItem.getForeground() instanceof ColorUIResource) {
                graphics.setColor(Theme.menuFontColor.getColor());
            } else {
                graphics.setColor(jMenuItem.getForeground());
            }
        } else if (model.isArmed() || ((jMenuItem instanceof JMenu) && model.isSelected())) {
            graphics.setColor(Theme.menuItemSelectedTextColor.getColor());
        } else {
            graphics.setColor(jMenuItem.getForeground());
        }
        TinyUtils.drawStringUnderlineCharAt(jMenuItem, graphics, str, displayedMnemonicIndex, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
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

    private void resetRects() {
        iconRect.setBounds(zeroRect);
        textRect.setBounds(zeroRect);
        acceleratorRect.setBounds(zeroRect);
        checkIconRect.setBounds(zeroRect);
        arrowIconRect.setBounds(zeroRect);
        viewRect.setBounds(0, 0, Short.MAX_VALUE, Short.MAX_VALUE);
        rect.setBounds(zeroRect);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        paint(graphics, jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (Theme.menuAllowTwoIcons.getValue()) {
            paintMenuItemTwoIcons(graphics, jComponent, this.checkIcon, this.arrowIcon, this.selectionBackground, this.selectionForeground, 4);
        } else {
            paintMenuItemOneIcon(graphics, jComponent, this.checkIcon, this.arrowIcon, this.selectionBackground, this.selectionForeground, 4);
        }
    }

    private boolean isTopLevelMenu() {
        return (this.menuItem instanceof JMenu) && ((JMenu) this.menuItem).isTopLevelMenu();
    }

    private boolean isTopLevelMenu(Component component) {
        return (component instanceof JMenu) && ((JMenu) component).isTopLevelMenu();
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

    protected void doClick(MenuSelectionManager menuSelectionManager) {
        if (menuSelectionManager == null) {
            menuSelectionManager = MenuSelectionManager.defaultManager();
        }
        menuSelectionManager.clearSelectedPath();
        this.menuItem.doClick(0);
    }

    private boolean isInternalFrameSystemMenu() {
        String actionCommand = this.menuItem.getActionCommand();
        return actionCommand == "Close" || actionCommand == "Minimize" || actionCommand == "Restore" || actionCommand == "Maximize";
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyMenuItemUI();
    }

    protected void installDefaults() {
        String propertyPrefix = getPropertyPrefix();
        this.acceleratorFont = UIManager.getFont(new StringBuffer().append(propertyPrefix).append(".acceleratorFont").toString());
        this.menuItem.setOpaque(true);
        if (this.menuItem.getMargin() == null || (this.menuItem.getMargin() instanceof UIResource)) {
            this.menuItem.setMargin(UIManager.getInsets(new StringBuffer().append(propertyPrefix).append(".margin").toString()));
        }
        LookAndFeel.installBorder(this.menuItem, new StringBuffer().append(propertyPrefix).append(".border").toString());
        this.oldBorderPainted = this.menuItem.isBorderPainted();
        this.menuItem.setBorderPainted(((Boolean) UIManager.get(new StringBuffer().append(propertyPrefix).append(".borderPainted").toString())).booleanValue());
        LookAndFeel.installColorsAndFont(this.menuItem, new StringBuffer().append(propertyPrefix).append(".background").toString(), new StringBuffer().append(propertyPrefix).append(".foreground").toString(), new StringBuffer().append(propertyPrefix).append(".font").toString());
        if (this.selectionBackground == null || (this.selectionBackground instanceof UIResource)) {
            this.selectionBackground = UIManager.getColor(new StringBuffer().append(propertyPrefix).append(".selectionBackground").toString());
        }
        if (this.selectionForeground == null || (this.selectionForeground instanceof UIResource)) {
            this.selectionForeground = UIManager.getColor(new StringBuffer().append(propertyPrefix).append(".selectionForeground").toString());
        }
        if (this.disabledForeground == null || (this.disabledForeground instanceof UIResource)) {
            this.disabledForeground = UIManager.getColor(new StringBuffer().append(propertyPrefix).append(".disabledForeground").toString());
        }
        if (this.acceleratorForeground == null || (this.acceleratorForeground instanceof UIResource)) {
            this.acceleratorForeground = UIManager.getColor(new StringBuffer().append(propertyPrefix).append(".acceleratorForeground").toString());
        }
        if (this.acceleratorSelectionForeground == null || (this.acceleratorSelectionForeground instanceof UIResource)) {
            this.acceleratorSelectionForeground = UIManager.getColor(new StringBuffer().append(propertyPrefix).append(".acceleratorSelectionForeground").toString());
        }
        this.acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");
        if (this.acceleratorDelimiter == null) {
            this.acceleratorDelimiter = Marker.ANY_NON_NULL_MARKER;
        }
        if (this.arrowIcon == null || (this.arrowIcon instanceof UIResource)) {
            this.arrowIcon = UIManager.getIcon(new StringBuffer().append(propertyPrefix).append(".arrowIcon").toString());
        }
        if (this.checkIcon == null || (this.checkIcon instanceof UIResource)) {
            this.checkIcon = UIManager.getIcon(new StringBuffer().append(propertyPrefix).append(".checkIcon").toString());
        }
    }

    private String getAcceleratorText(KeyStroke keyStroke) {
        if (keyStroke == null) {
            return "";
        }
        int modifiers = keyStroke.getModifiers();
        String string = modifiers > 0 ? new StringBuffer().append(KeyEvent.getKeyModifiersText(modifiers)).append(this.acceleratorDelimiter).toString() : "";
        int keyCode = keyStroke.getKeyCode();
        return keyCode != 0 ? new StringBuffer().append(string).append(KeyEvent.getKeyText(keyCode)).toString() : new StringBuffer().append(string).append(keyStroke.getKeyChar()).toString();
    }

    protected Dimension getPreferredMenuItemSizeTwoIcons(JComponent jComponent, Icon icon, Icon icon2) {
        JMenuItem jMenuItem = (JMenuItem) jComponent;
        String text = jMenuItem.getText();
        String acceleratorText = getAcceleratorText(jMenuItem.getAccelerator());
        boolean zIsTopLevelMenu = isTopLevelMenu(jComponent);
        Icon icon3 = jMenuItem.getIcon();
        FontMetrics fontMetrics = jMenuItem.getFontMetrics(jMenuItem.getFont());
        FontMetrics fontMetrics2 = jMenuItem.getFontMetrics(this.acceleratorFont);
        int horizontalAlignment = jMenuItem.getHorizontalAlignment();
        int horizontalTextPosition = jMenuItem.getHorizontalTextPosition();
        resetRects();
        layoutMenuItem(fontMetrics, text, fontMetrics2, acceleratorText, icon3, icon, icon2, jMenuItem.getVerticalAlignment(), horizontalAlignment, jMenuItem.getVerticalTextPosition(), horizontalTextPosition, viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, text == null ? 0 : 4, true);
        rect.setBounds(textRect);
        rect = SwingUtilities.computeUnion(iconRect.f12372x, iconRect.f12373y, iconRect.width, iconRect.height, rect);
        Insets insets = jMenuItem.getInsets();
        if (zIsTopLevelMenu) {
            rect.width += insets.left + insets.right;
            rect.height += insets.top + insets.bottom;
            rect.width += 5;
            return rect.getSize();
        }
        JComponent jComponent2 = (JComponent) jComponent.getParent();
        Integer num = (Integer) jComponent2.getClientProperty(MAX_TEXT_WIDTH);
        int iIntValue = num == null ? 0 : num.intValue();
        Integer num2 = (Integer) jComponent2.getClientProperty(MAX_LABEL_WIDTH);
        int iIntValue2 = num2 == null ? 0 : num2.intValue();
        Integer num3 = (Integer) jComponent2.getClientProperty(MAX_ICON_WIDTH);
        int iIntValue3 = num3 == null ? 0 : num3.intValue();
        Integer num4 = (Integer) jComponent2.getClientProperty(MAX_ACC_WIDTH);
        int iIntValue4 = num4 == null ? 0 : num4.intValue();
        if (horizontalTextPosition != 2 && horizontalTextPosition != 10 && horizontalTextPosition != 0) {
            if (textRect.width > iIntValue) {
                jComponent2.putClientProperty(MAX_TEXT_WIDTH, new Integer(textRect.width));
            } else if (iIntValue > 0 && iconRect.width < iIntValue) {
                rect.width += iIntValue - textRect.width;
            }
            if (horizontalAlignment == 2 || horizontalAlignment == 10) {
                if (iconRect.width > iIntValue3) {
                    jComponent2.putClientProperty(MAX_ICON_WIDTH, new Integer(iconRect.width));
                } else if (iconRect.width > 0) {
                    rect.width += iIntValue3 - iconRect.width;
                }
            }
        } else if (rect.width > iIntValue) {
            jComponent2.putClientProperty(MAX_TEXT_WIDTH, new Integer(rect.width));
        } else if (iIntValue > 0) {
            rect.width = iIntValue;
        }
        if (rect.width > iIntValue2) {
            jComponent2.putClientProperty(MAX_LABEL_WIDTH, new Integer(rect.width));
        } else if (iIntValue2 > 0) {
            rect.width = iIntValue2;
        }
        if (acceleratorRect.width > iIntValue4) {
            iIntValue4 = acceleratorRect.width;
            jComponent2.putClientProperty(MAX_ACC_WIDTH, new Integer(iIntValue4));
        }
        rect.width += Math.max(insets.left + insets.right, 11);
        rect.height += insets.top + insets.bottom;
        rect.width += 30;
        if (iIntValue4 > 0) {
            rect.width += iIntValue4 + 8;
        }
        return rect.getSize();
    }

    protected Dimension getPreferredMenuItemSizeOneIcon(JComponent jComponent, Icon icon, Icon icon2) {
        JMenuItem jMenuItem = (JMenuItem) jComponent;
        String text = jMenuItem.getText();
        String acceleratorText = getAcceleratorText(jMenuItem.getAccelerator());
        boolean zIsTopLevelMenu = isTopLevelMenu(jComponent);
        Icon icon3 = jMenuItem.getIcon();
        FontMetrics fontMetrics = jMenuItem.getFontMetrics(jMenuItem.getFont());
        FontMetrics fontMetrics2 = jMenuItem.getFontMetrics(this.acceleratorFont);
        int horizontalAlignment = jMenuItem.getHorizontalAlignment();
        int horizontalTextPosition = jMenuItem.getHorizontalTextPosition();
        if (icon3 == null && icon != null) {
            icon3 = icon;
            icon = null;
        }
        resetRects();
        layoutMenuItem(fontMetrics, text, fontMetrics2, acceleratorText, icon3, icon, icon2, jMenuItem.getVerticalAlignment(), horizontalAlignment, jMenuItem.getVerticalTextPosition(), horizontalTextPosition, viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, text == null ? 0 : 4, true);
        rect.setBounds(textRect);
        rect = SwingUtilities.computeUnion(iconRect.f12372x, iconRect.f12373y, iconRect.width, iconRect.height, rect);
        Insets insets = jMenuItem.getInsets();
        if (zIsTopLevelMenu) {
            rect.width += insets.left + insets.right;
            rect.height += insets.top + insets.bottom;
            rect.width += 5;
            return rect.getSize();
        }
        JComponent jComponent2 = (JComponent) jComponent.getParent();
        Integer num = (Integer) jComponent2.getClientProperty(MAX_TEXT_WIDTH);
        int iIntValue = num == null ? 0 : num.intValue();
        Integer num2 = (Integer) jComponent2.getClientProperty(MAX_LABEL_WIDTH);
        int iIntValue2 = num2 == null ? 0 : num2.intValue();
        Integer num3 = (Integer) jComponent2.getClientProperty(MAX_ICON_WIDTH);
        int iIntValue3 = num3 == null ? 0 : num3.intValue();
        Integer num4 = (Integer) jComponent2.getClientProperty(MAX_ACC_WIDTH);
        int iIntValue4 = num4 == null ? 0 : num4.intValue();
        if (horizontalTextPosition != 2 && horizontalTextPosition != 10 && horizontalTextPosition != 0) {
            if (textRect.width > iIntValue) {
                jComponent2.putClientProperty(MAX_TEXT_WIDTH, new Integer(textRect.width));
            } else if (iIntValue > 0 && iconRect.width < iIntValue) {
                rect.width += iIntValue - textRect.width;
            }
            if (horizontalAlignment == 2 || horizontalAlignment == 10) {
                if (iconRect.width > iIntValue3) {
                    jComponent2.putClientProperty(MAX_ICON_WIDTH, new Integer(iconRect.width));
                } else if (iconRect.width > 0) {
                    rect.width += iIntValue3 - iconRect.width;
                }
            }
        } else if (rect.width > iIntValue) {
            jComponent2.putClientProperty(MAX_TEXT_WIDTH, new Integer(rect.width));
        } else if (iIntValue > 0) {
            rect.width = iIntValue;
        }
        if (rect.width > iIntValue2) {
            jComponent2.putClientProperty(MAX_LABEL_WIDTH, new Integer(rect.width));
        } else if (iIntValue2 > 0) {
            rect.width = iIntValue2;
        }
        if (acceleratorRect.width > iIntValue4) {
            iIntValue4 = acceleratorRect.width;
            jComponent2.putClientProperty(MAX_ACC_WIDTH, new Integer(iIntValue4));
        }
        rect.width += Math.max(insets.left + insets.right, 11);
        rect.height += insets.top + insets.bottom;
        rect.width += 16;
        if (iIntValue4 > 0) {
            rect.width += iIntValue4 + 8;
        }
        return rect.getSize();
    }

    private String p(Rectangle rectangle) {
        return rectangle == null ? FXMLLoader.NULL_KEYWORD : new StringBuffer().append(rectangle.f12372x).append(", ").append(rectangle.f12373y).append(", ").append(rectangle.width).append(", ").append(rectangle.height).toString();
    }

    private String p(Insets insets) {
        return insets == null ? FXMLLoader.NULL_KEYWORD : new StringBuffer().append(insets.top).append(", ").append(insets.left).append(", ").append(insets.bottom).append(", ").append(insets.right).toString();
    }

    private String p(Dimension dimension) {
        return dimension == null ? FXMLLoader.NULL_KEYWORD : new StringBuffer().append(dimension.width).append(", ").append(dimension.height).toString();
    }

    private String getSwingConstantsString(int i2) {
        return i2 == 0 ? "CENTER" : i2 == 10 ? "LEADING" : i2 == 2 ? "LEFT" : i2 == 4 ? "RIGHT" : i2 == 11 ? "TRAILING" : "???";
    }

    private String getSwingConstantsString(int i2, int i3) {
        return new StringBuffer().append(getSwingConstantsString(i2)).append(", ").append(getSwingConstantsString(i3)).toString();
    }

    protected void paintMenuItemTwoIcons(Graphics graphics, JComponent jComponent, Icon icon, Icon icon2, Color color, Color color2, int i2) {
        Icon icon3;
        JMenuItem jMenuItem = (JMenuItem) jComponent;
        ButtonModel model = jMenuItem.getModel();
        JComponent jComponent2 = (JComponent) jMenuItem.getParent();
        Integer num = (Integer) jComponent2.getClientProperty(MAX_ACC_WIDTH);
        int iIntValue = num == null ? 0 : num.intValue();
        Integer num2 = (Integer) jComponent2.getClientProperty(MAX_ICON_WIDTH);
        int iIntValue2 = num2 == null ? 0 : num2.intValue();
        Integer num3 = (Integer) jComponent2.getClientProperty(MAX_LABEL_WIDTH);
        int iIntValue3 = num3 == null ? 0 : num3.intValue();
        Integer num4 = (Integer) jComponent2.getClientProperty(MAX_TEXT_WIDTH);
        int iIntValue4 = num4 == null ? 0 : num4.intValue();
        int width = jMenuItem.getWidth();
        int height = jMenuItem.getHeight();
        Insets insets = jComponent.getInsets();
        boolean zIsTopLevelMenu = isTopLevelMenu();
        boolean zIsLeftToRight = jComponent.getComponentOrientation().isLeftToRight();
        int horizontalAlignment = jMenuItem.getHorizontalAlignment();
        int horizontalTextPosition = jMenuItem.getHorizontalTextPosition();
        resetRects();
        viewRect.setBounds(0, 0, width, height);
        viewRect.f12372x += insets.left;
        viewRect.f12373y += insets.top;
        viewRect.width -= insets.right + insets.left;
        viewRect.height -= insets.bottom + insets.top;
        Font font = graphics.getFont();
        Font font2 = jComponent.getFont();
        graphics.setFont(font2);
        FontMetrics fontMetrics = graphics.getFontMetrics(font2);
        FontMetrics fontMetrics2 = graphics.getFontMetrics(this.acceleratorFont);
        String acceleratorText = getAcceleratorText(jMenuItem.getAccelerator());
        Icon icon4 = jMenuItem.getIcon();
        Icon icon5 = null;
        if ((jMenuItem instanceof JCheckBoxMenuItem) || (jMenuItem instanceof JRadioButtonMenuItem)) {
            icon5 = icon;
        }
        String strLayoutMenuItem = layoutMenuItem(fontMetrics, jMenuItem.getText(), fontMetrics2, acceleratorText, icon4, icon5, icon2, jMenuItem.getVerticalAlignment(), jMenuItem.getHorizontalAlignment(), jMenuItem.getVerticalTextPosition(), jMenuItem.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, jMenuItem.getText() == null ? 0 : i2, false);
        if (!zIsTopLevelMenu) {
            if (zIsLeftToRight) {
                checkIconRect.f12372x = insets.left;
                acceleratorRect.f12372x = insets.left + 14 + iIntValue3 + 8;
                if (horizontalTextPosition == 4 || horizontalTextPosition == 11) {
                    iconRect.f12372x = insets.left + 14;
                    textRect.f12372x = iconRect.f12372x + iIntValue2 + (iIntValue2 == 0 ? 0 : 4);
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i3 = insets.left + 14 + iIntValue3;
                        int i4 = i3 - (textRect.f12372x + textRect.width);
                        textRect.f12372x = i3 - textRect.width;
                        iconRect.f12372x = (textRect.f12372x - 4) - iconRect.width;
                    } else if (horizontalAlignment == 0) {
                        iconRect.f12372x = insets.left + 14 + ((iIntValue3 - ((iconRect.width + (iconRect.width == 0 ? 0 : 4)) + textRect.width)) / 2);
                        textRect.f12372x = iconRect.f12372x + iconRect.width + (iconRect.width == 0 ? 0 : 4);
                    }
                } else if (horizontalTextPosition == 2 || horizontalTextPosition == 10) {
                    textRect.f12372x = insets.left + 14 + iIntValue2 + (iIntValue2 == 0 ? 0 : 4);
                    iconRect.f12372x = textRect.f12372x + textRect.width + 4;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i5 = insets.left + 14 + iIntValue3;
                        int i6 = i5 - (textRect.f12372x + textRect.width);
                        iconRect.f12372x = i5 - iconRect.width;
                        textRect.f12372x = (iconRect.f12372x - 4) - textRect.width;
                    } else if (horizontalAlignment == 0) {
                        textRect.f12372x = insets.left + 14 + ((iIntValue3 - ((iconRect.width + (iconRect.width == 0 ? 0 : 4)) + textRect.width)) / 2);
                        iconRect.f12372x = textRect.f12372x + textRect.width + 4;
                    }
                } else if (horizontalTextPosition == 0) {
                    int i7 = textRect.width > iconRect.width ? textRect.width : iconRect.width;
                    int i8 = insets.left + 14 + iIntValue2 + (iIntValue2 == 0 ? 0 : 4);
                    int i9 = textRect.width > iconRect.width ? i8 - textRect.f12372x : i8 - iconRect.f12372x;
                    iconRect.f12372x += i9;
                    textRect.f12372x += i9;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i10 = ((insets.left + 14) + iIntValue3) - i7;
                        int i11 = textRect.width > iconRect.width ? i10 - textRect.f12372x : i10 - iconRect.f12372x;
                        iconRect.f12372x += i11;
                        textRect.f12372x += i11;
                    } else if (horizontalAlignment == 0) {
                        int i12 = insets.left + 14 + ((iIntValue3 - i7) / 2);
                        int i13 = textRect.width > iconRect.width ? i12 - textRect.f12372x : i12 - iconRect.f12372x;
                        iconRect.f12372x += i13;
                        textRect.f12372x += i13;
                    }
                }
            } else {
                checkIconRect.f12372x = ((viewRect.f12372x + viewRect.width) - 14) + 4;
                acceleratorRect.f12372x = ((insets.left + 16) + iIntValue) - acceleratorRect.width;
                if (horizontalTextPosition == 4 || horizontalTextPosition == 11) {
                    iconRect.f12372x = (checkIconRect.f12372x - 4) - iconRect.width;
                    textRect.f12372x = (((checkIconRect.f12372x - 4) - iIntValue2) - (iIntValue2 == 0 ? 0 : 4)) - textRect.width;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i14 = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8);
                        textRect.f12372x = i14;
                        iconRect.f12372x = i14 + textRect.width + 4;
                    } else if (horizontalAlignment == 0) {
                        textRect.f12372x = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8) + ((iIntValue3 - ((iconRect.width + (iconRect.width == 0 ? 0 : 4)) + textRect.width)) / 2);
                        iconRect.f12372x = textRect.f12372x + textRect.width + 4;
                    }
                } else if (horizontalTextPosition == 2 || horizontalTextPosition == 10) {
                    textRect.f12372x = (((checkIconRect.f12372x - 4) - textRect.width) - iIntValue2) - (iIntValue2 == 0 ? 0 : 4);
                    iconRect.f12372x = (textRect.f12372x - 4) - iconRect.width;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i15 = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8);
                        iconRect.f12372x = i15;
                        textRect.f12372x = i15 + iconRect.width + 4;
                    } else if (horizontalAlignment == 0) {
                        iconRect.f12372x = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8) + ((iIntValue3 - ((iconRect.width + (iconRect.width == 0 ? 0 : 4)) + textRect.width)) / 2);
                        textRect.f12372x = iconRect.f12372x + iconRect.width + 4;
                    }
                } else if (horizontalTextPosition == 0) {
                    int i16 = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8);
                    int i17 = textRect.width > iconRect.width ? textRect.width : iconRect.width;
                    int i18 = (i16 + iIntValue4) - i17;
                    int i19 = textRect.width > iconRect.width ? i18 - textRect.f12372x : i18 - iconRect.f12372x;
                    iconRect.f12372x += i19;
                    textRect.f12372x += i19;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i20 = textRect.width > iconRect.width ? i16 - textRect.f12372x : i16 - iconRect.f12372x;
                        iconRect.f12372x += i20;
                        textRect.f12372x += i20;
                    } else if (horizontalAlignment == 0) {
                        int i21 = i16 + ((iIntValue3 - i17) / 2);
                        int i22 = textRect.width > iconRect.width ? i21 - textRect.f12372x : i21 - iconRect.f12372x;
                        iconRect.f12372x += i22;
                        textRect.f12372x += i22;
                    }
                }
            }
        }
        paintBackground(graphics, jMenuItem, color, zIsLeftToRight);
        Color color3 = graphics.getColor();
        if (icon5 != null && !zIsTopLevelMenu) {
            if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color2);
            } else {
                graphics.setColor(color3);
            }
            icon5.paintIcon(jComponent, graphics, checkIconRect.f12372x, checkIconRect.f12373y);
            graphics.setColor(color3);
        }
        if (icon4 != null) {
            if (!model.isEnabled()) {
                icon3 = jMenuItem.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                icon3 = jMenuItem.getPressedIcon();
                if (icon3 == null) {
                    icon3 = jMenuItem.getIcon();
                }
            } else if (model.isSelected()) {
                icon3 = jMenuItem.getSelectedIcon();
                if (icon3 == null) {
                    icon3 = jMenuItem.getIcon();
                }
            } else {
                icon3 = model.isArmed() ? jMenuItem.getIcon() : jMenuItem.getIcon();
            }
            if (icon3 != null) {
                icon3.paintIcon(jComponent, graphics, iconRect.f12372x, iconRect.f12373y);
            }
        }
        if (strLayoutMenuItem != null) {
            View view = (View) jComponent.getClientProperty("html");
            graphics.setColor(Theme.menuItemFontColor.getColor());
            if (view != null) {
                view.paint(graphics, textRect);
            } else {
                paintText(graphics, jMenuItem, textRect, strLayoutMenuItem);
            }
        }
        if (!"".equals(acceleratorText)) {
            graphics.setFont(this.acceleratorFont);
            if (model.isEnabled()) {
                if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                    graphics.setColor(Theme.menuItemSelectedTextColor.getColor());
                } else {
                    graphics.setColor(Theme.menuItemFontColor.getColor());
                }
                BasicGraphicsUtils.drawString(graphics, acceleratorText, 0, acceleratorRect.f12372x, acceleratorRect.f12373y + fontMetrics2.getAscent());
            } else {
                graphics.setColor(Theme.menuItemDisabledFgColor.getColor());
                BasicGraphicsUtils.drawString(graphics, acceleratorText, 0, acceleratorRect.f12372x, acceleratorRect.f12373y + fontMetrics2.getAscent());
            }
        }
        if (icon2 != null) {
            if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color2);
            }
            if (!zIsTopLevelMenu) {
                icon2.paintIcon(jComponent, graphics, arrowIconRect.f12372x, arrowIconRect.f12373y);
            }
        }
        graphics.setColor(color3);
        graphics.setFont(font);
    }

    protected void paintMenuItemOneIcon(Graphics graphics, JComponent jComponent, Icon icon, Icon icon2, Color color, Color color2, int i2) {
        JMenuItem jMenuItem = (JMenuItem) jComponent;
        ButtonModel model = jMenuItem.getModel();
        JComponent jComponent2 = (JComponent) jMenuItem.getParent();
        Integer num = (Integer) jComponent2.getClientProperty(MAX_ACC_WIDTH);
        int iIntValue = num == null ? 0 : num.intValue();
        Integer num2 = (Integer) jComponent2.getClientProperty(MAX_ICON_WIDTH);
        int iIntValue2 = num2 == null ? 0 : num2.intValue();
        Integer num3 = (Integer) jComponent2.getClientProperty(MAX_LABEL_WIDTH);
        int iIntValue3 = num3 == null ? 0 : num3.intValue();
        Integer num4 = (Integer) jComponent2.getClientProperty(MAX_TEXT_WIDTH);
        int iIntValue4 = num4 == null ? 0 : num4.intValue();
        int width = jMenuItem.getWidth();
        int height = jMenuItem.getHeight();
        Insets insets = jComponent.getInsets();
        boolean zIsTopLevelMenu = isTopLevelMenu();
        boolean zIsLeftToRight = jComponent.getComponentOrientation().isLeftToRight();
        int horizontalAlignment = jMenuItem.getHorizontalAlignment();
        int horizontalTextPosition = jMenuItem.getHorizontalTextPosition();
        resetRects();
        viewRect.setBounds(0, 0, width, height);
        viewRect.f12372x += insets.left;
        viewRect.f12373y += insets.top;
        viewRect.width -= insets.right + insets.left;
        viewRect.height -= insets.bottom + insets.top;
        Font font = graphics.getFont();
        Font font2 = jComponent.getFont();
        graphics.setFont(font2);
        FontMetrics fontMetrics = graphics.getFontMetrics(font2);
        FontMetrics fontMetrics2 = graphics.getFontMetrics(this.acceleratorFont);
        String acceleratorText = getAcceleratorText(jMenuItem.getAccelerator());
        Icon icon3 = jMenuItem.getIcon();
        Icon icon4 = null;
        if (((jMenuItem instanceof JCheckBoxMenuItem) || (jMenuItem instanceof JRadioButtonMenuItem)) && icon3 == null) {
            icon4 = icon;
        }
        String strLayoutMenuItem = layoutMenuItem(fontMetrics, jMenuItem.getText(), fontMetrics2, acceleratorText, icon3, icon4, icon2, jMenuItem.getVerticalAlignment(), jMenuItem.getHorizontalAlignment(), jMenuItem.getVerticalTextPosition(), jMenuItem.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, jMenuItem.getText() == null ? 0 : i2, false);
        if (!zIsTopLevelMenu) {
            if (zIsLeftToRight) {
                checkIconRect.f12372x = insets.left;
                acceleratorRect.f12372x = insets.left + iIntValue3 + 8;
                if (horizontalTextPosition == 4 || horizontalTextPosition == 11) {
                    iconRect.f12372x = checkIconRect.f12372x;
                    textRect.f12372x = iconRect.f12372x + iIntValue2 + (iIntValue2 == 0 ? 0 : 4);
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i3 = insets.left + iIntValue3;
                        int i4 = i3 - (textRect.f12372x + textRect.width);
                        textRect.f12372x = i3 - textRect.width;
                        iconRect.f12372x = (textRect.f12372x - 4) - iconRect.width;
                    } else if (horizontalAlignment == 0) {
                        iconRect.f12372x = insets.left + ((iIntValue3 - ((iconRect.width + (iconRect.width == 0 ? 0 : 4)) + textRect.width)) / 2);
                        textRect.f12372x = iconRect.f12372x + iconRect.width + (iconRect.width == 0 ? 0 : 4);
                    }
                } else if (horizontalTextPosition == 2 || horizontalTextPosition == 10) {
                    textRect.f12372x = insets.left + iIntValue2 + (iIntValue2 == 0 ? 0 : 4);
                    iconRect.f12372x = textRect.f12372x + textRect.width + 4;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i5 = insets.left + iIntValue3;
                        int i6 = i5 - (textRect.f12372x + textRect.width);
                        iconRect.f12372x = i5 - iconRect.width;
                        textRect.f12372x = (iconRect.f12372x - 4) - textRect.width;
                    } else if (horizontalAlignment == 0) {
                        textRect.f12372x = insets.left + ((iIntValue3 - ((iconRect.width + (iconRect.width == 0 ? 0 : 4)) + textRect.width)) / 2);
                        iconRect.f12372x = textRect.f12372x + textRect.width + 4;
                    }
                } else if (horizontalTextPosition == 0) {
                    int i7 = textRect.width > iconRect.width ? textRect.width : iconRect.width;
                    int i8 = insets.left + iIntValue2 + (iIntValue2 == 0 ? 0 : 4);
                    int i9 = textRect.width > iconRect.width ? i8 - textRect.f12372x : i8 - iconRect.f12372x;
                    iconRect.f12372x += i9;
                    textRect.f12372x += i9;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i10 = (insets.left + iIntValue3) - i7;
                        int i11 = textRect.width > iconRect.width ? i10 - textRect.f12372x : i10 - iconRect.f12372x;
                        iconRect.f12372x += i11;
                        textRect.f12372x += i11;
                    } else if (horizontalAlignment == 0) {
                        int i12 = insets.left + ((iIntValue3 - i7) / 2);
                        int i13 = textRect.width > iconRect.width ? i12 - textRect.f12372x : i12 - iconRect.f12372x;
                        iconRect.f12372x += i13;
                        textRect.f12372x += i13;
                    }
                }
            } else {
                checkIconRect.f12372x = ((viewRect.f12372x + viewRect.width) - 14) + 4;
                acceleratorRect.f12372x = ((insets.left + 16) + iIntValue) - acceleratorRect.width;
                if (horizontalTextPosition == 4 || horizontalTextPosition == 11) {
                    iconRect.f12372x = (viewRect.f12372x + viewRect.width) - iconRect.width;
                    textRect.f12372x = (((checkIconRect.f12372x - 4) - iIntValue2) - (iIntValue2 == 0 ? 0 : 4)) - textRect.width;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i14 = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8);
                        textRect.f12372x = i14;
                        iconRect.f12372x = i14 + textRect.width + 4;
                    } else if (horizontalAlignment == 0) {
                        textRect.f12372x = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8) + ((iIntValue3 - ((iconRect.width + (iconRect.width == 0 ? 0 : 4)) + textRect.width)) / 2);
                        iconRect.f12372x = textRect.f12372x + textRect.width + 4;
                    }
                } else if (horizontalTextPosition == 2 || horizontalTextPosition == 10) {
                    textRect.f12372x = (((checkIconRect.f12372x - 4) - textRect.width) - iIntValue2) - (iIntValue2 == 0 ? 0 : 4);
                    iconRect.f12372x = (textRect.f12372x - 4) - iconRect.width;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i15 = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8);
                        iconRect.f12372x = i15;
                        textRect.f12372x = i15 + iconRect.width + 4;
                    } else if (horizontalAlignment == 0) {
                        iconRect.f12372x = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8) + ((iIntValue3 - ((iconRect.width + (iconRect.width == 0 ? 0 : 4)) + textRect.width)) / 2);
                        textRect.f12372x = iconRect.f12372x + iconRect.width + 4;
                    }
                } else if (horizontalTextPosition == 0) {
                    int i16 = insets.left + 16 + iIntValue + (iIntValue == 0 ? 0 : 8);
                    int i17 = textRect.width > iconRect.width ? textRect.width : iconRect.width;
                    int i18 = (i16 + iIntValue4) - i17;
                    int i19 = textRect.width > iconRect.width ? i18 - textRect.f12372x : i18 - iconRect.f12372x;
                    iconRect.f12372x += i19;
                    textRect.f12372x += i19;
                    if (horizontalAlignment == 4 || horizontalAlignment == 11) {
                        int i20 = textRect.width > iconRect.width ? i16 - textRect.f12372x : i16 - iconRect.f12372x;
                        iconRect.f12372x += i20;
                        textRect.f12372x += i20;
                    } else if (horizontalAlignment == 0) {
                        int i21 = i16 + ((iIntValue3 - i17) / 2);
                        int i22 = textRect.width > iconRect.width ? i21 - textRect.f12372x : i21 - iconRect.f12372x;
                        iconRect.f12372x += i22;
                        textRect.f12372x += i22;
                    }
                }
            }
        }
        paintBackground(graphics, jMenuItem, color, zIsLeftToRight);
        Color color3 = graphics.getColor();
        if (icon4 != null && !zIsTopLevelMenu) {
            if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color2);
            } else {
                graphics.setColor(color3);
            }
            icon4.paintIcon(jComponent, graphics, checkIconRect.f12372x, checkIconRect.f12373y);
            graphics.setColor(color3);
        }
        if (icon3 != null) {
            Icon icon5 = icon3;
            Icon selectedIcon = null;
            Icon rolloverIcon = null;
            if (model.isSelected()) {
                selectedIcon = jMenuItem.getSelectedIcon();
                if (selectedIcon != null) {
                    icon5 = selectedIcon;
                }
            }
            if (!model.isEnabled()) {
                if (model.isSelected()) {
                    rolloverIcon = jMenuItem.getDisabledSelectedIcon();
                    if (rolloverIcon == null) {
                        rolloverIcon = selectedIcon;
                    }
                }
                if (rolloverIcon == null) {
                    rolloverIcon = jMenuItem.getDisabledIcon();
                }
            } else if (model.isPressed() && model.isArmed()) {
                rolloverIcon = jMenuItem.getPressedIcon();
            } else if (model.isArmed()) {
                if (model.isSelected()) {
                    rolloverIcon = jMenuItem.getRolloverSelectedIcon();
                    if (rolloverIcon == null) {
                        rolloverIcon = selectedIcon;
                    }
                }
                if (rolloverIcon == null) {
                    rolloverIcon = jMenuItem.getRolloverIcon();
                }
            }
            if (rolloverIcon != null) {
                icon5 = rolloverIcon;
            }
            if (icon5 != null) {
                icon5.paintIcon(jComponent, graphics, iconRect.f12372x, iconRect.f12373y);
                if (((jMenuItem instanceof JCheckBoxMenuItem) || (jMenuItem instanceof JRadioButtonMenuItem)) && jMenuItem.getSelectedIcon() == null && model.isSelected()) {
                    paintSelected(graphics, jMenuItem);
                }
            }
        }
        if (strLayoutMenuItem != null) {
            View view = (View) jComponent.getClientProperty("html");
            graphics.setColor(Theme.menuItemFontColor.getColor());
            if (view != null) {
                view.paint(graphics, textRect);
            } else {
                paintText(graphics, jMenuItem, textRect, strLayoutMenuItem);
            }
        }
        if (!"".equals(acceleratorText)) {
            graphics.setFont(this.acceleratorFont);
            if (model.isEnabled()) {
                if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                    graphics.setColor(Theme.menuItemSelectedTextColor.getColor());
                } else {
                    graphics.setColor(Theme.menuItemFontColor.getColor());
                }
                BasicGraphicsUtils.drawString(graphics, acceleratorText, 0, acceleratorRect.f12372x, acceleratorRect.f12373y + fontMetrics2.getAscent());
            } else {
                graphics.setColor(Theme.menuItemDisabledFgColor.getColor());
                BasicGraphicsUtils.drawString(graphics, acceleratorText, 0, acceleratorRect.f12372x, acceleratorRect.f12373y + fontMetrics2.getAscent());
            }
        }
        if (icon2 != null) {
            if (model.isArmed() || ((jComponent instanceof JMenu) && model.isSelected())) {
                graphics.setColor(color2);
            }
            if (!zIsTopLevelMenu) {
                icon2.paintIcon(jComponent, graphics, arrowIconRect.f12372x, arrowIconRect.f12373y);
            }
        }
        graphics.setColor(color3);
        graphics.setFont(font);
    }

    private void paintSelected(Graphics graphics, JMenuItem jMenuItem) {
        ColorUIResource color;
        ColorUIResource color2;
        if (!jMenuItem.isEnabled()) {
            color = Theme.menuIconDisabledColor.getColor();
            color2 = Theme.menuPopupColor.getColor();
        } else if (jMenuItem.isArmed()) {
            color = Theme.menuIconRolloverColor.getColor();
            color2 = Theme.menuItemRolloverColor.getColor();
        } else {
            color = Theme.menuIconColor.getColor();
            color2 = Theme.menuPopupColor.getColor();
        }
        int i2 = iconRect.f12372x - 3;
        int i3 = iconRect.f12373y - 1;
        if (jMenuItem.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT) {
            i2 = (iconRect.f12372x + iconRect.width) - 4;
        }
        graphics.translate(i2, i3);
        graphics.setColor(color);
        if (jMenuItem instanceof JCheckBoxMenuItem) {
            graphics.drawLine(0, 2, 0, 4);
            graphics.drawLine(1, 3, 1, 5);
            graphics.drawLine(2, 4, 2, 6);
            graphics.drawLine(3, 3, 3, 5);
            graphics.drawLine(4, 2, 4, 4);
            graphics.drawLine(5, 1, 5, 3);
            graphics.drawLine(6, 0, 6, 2);
            graphics.setColor(color2);
            graphics.drawLine(3, 1, 4, 1);
            graphics.drawLine(3, 2, 3, 2);
            graphics.setColor(ColorRoutines.getAlphaColor(color2, 128));
            graphics.drawLine(3, 6, 3, 6);
            graphics.drawLine(4, 5, 4, 5);
            graphics.drawLine(5, 4, 5, 4);
            graphics.drawLine(6, 3, 6, 3);
        } else {
            graphics.setColor(ColorRoutines.getAlphaColor(color, 96));
            graphics.drawLine(1, 0, 1, 0);
            graphics.drawLine(5, 0, 5, 0);
            graphics.drawLine(0, 1, 0, 1);
            graphics.drawLine(6, 1, 6, 1);
            graphics.drawLine(0, 5, 0, 5);
            graphics.drawLine(6, 5, 6, 5);
            graphics.drawLine(1, 6, 1, 6);
            graphics.drawLine(5, 6, 5, 6);
            graphics.setColor(ColorRoutines.getAlphaColor(color, 184));
            graphics.drawLine(2, 0, 2, 0);
            graphics.drawLine(4, 0, 4, 0);
            graphics.drawLine(0, 2, 0, 2);
            graphics.drawLine(6, 2, 6, 2);
            graphics.drawLine(0, 4, 0, 4);
            graphics.drawLine(6, 4, 6, 4);
            graphics.drawLine(2, 6, 2, 6);
            graphics.drawLine(4, 6, 4, 6);
            graphics.setColor(ColorRoutines.getAlphaColor(color, 245));
            graphics.drawLine(3, 0, 3, 0);
            graphics.drawLine(3, 6, 3, 6);
            graphics.drawLine(0, 3, 0, 3);
            graphics.drawLine(6, 3, 6, 3);
            graphics.setColor(ColorRoutines.getAlphaColor(color, 159));
            graphics.drawLine(1, 1, 1, 1);
            graphics.drawLine(5, 1, 5, 1);
            graphics.drawLine(1, 5, 1, 5);
            graphics.drawLine(5, 5, 5, 5);
            graphics.setColor(ColorRoutines.getAlphaColor(color, 71));
            graphics.drawLine(2, 1, 2, 1);
            graphics.drawLine(4, 1, 4, 1);
            graphics.drawLine(1, 2, 1, 2);
            graphics.drawLine(5, 2, 5, 2);
            graphics.drawLine(1, 4, 1, 4);
            graphics.drawLine(5, 4, 5, 4);
            graphics.drawLine(2, 5, 2, 5);
            graphics.drawLine(4, 5, 4, 5);
            graphics.setColor(ColorRoutines.getAlphaColor(color, 112));
            graphics.drawLine(2, 2, 2, 2);
            graphics.drawLine(4, 2, 4, 2);
            graphics.drawLine(2, 4, 2, 4);
            graphics.drawLine(4, 4, 4, 4);
            graphics.setColor(ColorRoutines.getAlphaColor(color, 224));
            graphics.drawLine(3, 2, 3, 2);
            graphics.drawLine(3, 4, 3, 4);
            graphics.drawLine(2, 3, 2, 3);
            graphics.drawLine(4, 3, 4, 3);
            graphics.setColor(color);
            graphics.drawLine(3, 3, 3, 3);
            graphics.setColor(ColorRoutines.getAlphaColor(color2, 128));
            graphics.drawLine(3, 1, 3, 1);
            graphics.drawLine(5, 3, 5, 3);
            graphics.drawLine(3, 5, 3, 5);
        }
        graphics.translate(-i2, -i3);
    }

    protected void paintBackground(Graphics graphics, JMenuItem jMenuItem, Color color, boolean z2) {
        if (jMenuItem.isOpaque()) {
            ButtonModel model = jMenuItem.getModel();
            Color color2 = graphics.getColor();
            int width = jMenuItem.getWidth();
            int height = jMenuItem.getHeight();
            boolean z3 = model.isArmed() || ((jMenuItem instanceof JMenu) && model.isSelected());
            if (isTopLevelMenu(jMenuItem)) {
                Color background = jMenuItem.getParent().getBackground();
                if (background instanceof ColorUIResource) {
                    background = Theme.menuBarColor.getColor();
                }
                if (model.isSelected()) {
                    graphics.setColor(background);
                    graphics.fillRect(0, 0, width, height);
                    paintXpTopMenuBorder(graphics, 0, 0, width, height, true, z2, background);
                } else if (jMenuItem.getClientProperty("rollover") == Boolean.TRUE && Theme.menuRollover.getValue()) {
                    graphics.setColor(Theme.menuRolloverBgColor.getColor());
                    if (z2) {
                        graphics.fillRect(0, 0, width - 5, height);
                        graphics.setColor(background);
                        graphics.fillRect(width - 5, 0, 5, height);
                    } else {
                        graphics.fillRect(5, 0, width, height);
                        graphics.setColor(background);
                        graphics.fillRect(0, 0, 5, height);
                    }
                    paintXpTopMenuBorder(graphics, 0, 0, width, height, false, z2, background);
                } else {
                    if (jMenuItem.getBackground() instanceof ColorUIResource) {
                        graphics.setColor(background);
                    } else {
                        graphics.setColor(jMenuItem.getBackground());
                    }
                    graphics.fillRect(0, 0, width, height);
                }
            } else if (z3) {
                graphics.setColor(Theme.menuItemRolloverColor.getColor());
                graphics.fillRect(0, 0, width, height);
            } else {
                if (jMenuItem.getBackground() instanceof ColorUIResource) {
                    graphics.setColor(Theme.menuPopupColor.getColor());
                } else {
                    graphics.setColor(jMenuItem.getBackground());
                }
                graphics.fillRect(0, 0, width, height);
            }
            graphics.setColor(color2);
        }
    }

    private void paintXpTopMenuBorder(Graphics graphics, int i2, int i3, int i4, int i5, boolean z2, boolean z3, Color color) {
        graphics.setColor(Theme.menuBorderColor.getColor());
        if (!z2) {
            if (z3) {
                graphics.drawRect(i2, i3, (i4 - 5) - 1, i5 - 1);
                return;
            } else {
                graphics.drawRect(i2 + 5, i3, (i4 - 5) - 1, i5 - 1);
                return;
            }
        }
        if (z3) {
            graphics.drawLine(i2, i3, ((i2 + i4) - 5) - 1, i3);
            graphics.drawLine(i2, i3, i2, (i3 + i5) - 1);
            graphics.drawLine(((i2 + i4) - 5) - 1, i3, ((i2 + i4) - 5) - 1, (i3 + i5) - 1);
            paintTopMenuShadow(graphics, (i2 + i4) - 5, i3 + 1, 5, i5 - 1, z3);
            return;
        }
        graphics.drawLine(i2 + 5, i3, (i2 + i4) - 1, i3);
        graphics.drawLine(i2 + 5, i3, i2 + 5, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 1, i3, (i2 + i4) - 1, (i3 + i5) - 1);
        paintTopMenuShadow(graphics, i2, i3 + 1, 5, i5 - 1, z3);
    }

    private void paintTopMenuShadow(Graphics graphics, int i2, int i3, int i4, int i5, boolean z2) {
        if (z2) {
            Image image = TinyPopupMenuBorder.LEFT_TO_RIGHT_SHADOW_MASK;
            graphics.drawImage(image, i2, i3, i2 + 5, i3 + 4, 6, 0, 11, 4, null);
            graphics.drawImage(image, i2, i3 + 4, i2 + 5, i3 + i5, 6, 4, 11, 5, null);
        } else {
            Image image2 = TinyPopupMenuBorder.RIGHT_TO_LEFT_SHADOW_MASK;
            graphics.drawImage(image2, i2, i3, i2 + 5, i3 + 4, 0, 0, 5, 4, null);
            graphics.drawImage(image2, i2, i3 + 4, i2 + 5, i3 + i5, 0, 4, 5, 5, null);
        }
    }

    private String layoutMenuItem(FontMetrics fontMetrics, String str, FontMetrics fontMetrics2, String str2, Icon icon, Icon icon2, Icon icon3, int i2, int i3, int i4, int i5, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3, Rectangle rectangle4, Rectangle rectangle5, Rectangle rectangle6, int i6, boolean z2) {
        boolean zIsTopLevelMenu = isTopLevelMenu();
        boolean z3 = icon != null && icon.getIconWidth() > 0;
        boolean zIsLeftToRight = this.menuItem.getComponentOrientation().isLeftToRight();
        SwingUtilities.layoutCompoundLabel(this.menuItem, fontMetrics, str, icon, i2, i3, i4, i5, rectangle, rectangle2, rectangle3, z3 ? i6 : 0);
        if (!zIsTopLevelMenu) {
            if (zIsLeftToRight) {
                rectangle2.f12372x += 14;
                rectangle3.f12372x += 14;
            } else {
                rectangle2.f12372x -= 14;
                rectangle3.f12372x -= 14;
            }
        }
        if (str2 == null || str2.equals("")) {
            rectangle4.height = 0;
            rectangle4.width = 0;
        } else {
            rectangle4.width = SwingUtilities.computeStringWidth(fontMetrics2, str2);
            rectangle4.height = fontMetrics2.getHeight();
        }
        if (zIsTopLevelMenu) {
            rectangle5.height = 0;
            rectangle5.width = 0;
            rectangle6.height = 0;
            rectangle6.width = 0;
        } else {
            if (icon2 != null) {
                rectangle5.height = icon2.getIconHeight();
                rectangle5.width = icon2.getIconWidth();
            } else {
                rectangle5.height = 0;
                rectangle5.width = 0;
            }
            if (icon3 != null) {
                rectangle6.width = icon3.getIconWidth();
                rectangle6.height = icon3.getIconHeight();
            } else {
                rectangle6.height = 0;
                rectangle6.width = 0;
            }
        }
        Rectangle rectangleUnion = rectangle2.union(rectangle3);
        rectangle4.f12373y = (rectangleUnion.f12373y + (rectangleUnion.height / 2)) - (rectangle4.height / 2);
        if (!zIsTopLevelMenu) {
            rectangle6.f12373y = (rectangleUnion.f12373y + (rectangleUnion.height / 2)) - (rectangle6.height / 2);
            rectangle5.f12373y = (rectangleUnion.f12373y + (rectangleUnion.height / 2)) - (rectangle5.height / 2);
            if (zIsLeftToRight) {
                rectangle6.f12372x = (rectangle.f12372x + rectangle.width) - rectangle6.width;
            } else {
                rectangle6.f12372x = rectangle.f12372x;
            }
        }
        return str;
    }
}
