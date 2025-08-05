package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InputMapUIResource;
import javax.swing.plaf.LabelUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.text.View;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicLabelUI.class */
public class BasicLabelUI extends LabelUI implements PropertyChangeListener {
    protected static BasicLabelUI labelUI = new BasicLabelUI();
    private static final Object BASIC_LABEL_UI_KEY = new Object();
    private Rectangle paintIconR = new Rectangle();
    private Rectangle paintTextR = new Rectangle();

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions(BasicRootPaneUI.Actions.PRESS));
        lazyActionMap.put(new Actions(BasicRootPaneUI.Actions.RELEASE));
    }

    protected String layoutCL(JLabel jLabel, FontMetrics fontMetrics, String str, Icon icon, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
        return SwingUtilities.layoutCompoundLabel(jLabel, fontMetrics, str, icon, jLabel.getVerticalAlignment(), jLabel.getHorizontalAlignment(), jLabel.getVerticalTextPosition(), jLabel.getHorizontalTextPosition(), rectangle, rectangle2, rectangle3, jLabel.getIconTextGap());
    }

    protected void paintEnabledText(JLabel jLabel, Graphics graphics, String str, int i2, int i3) {
        int displayedMnemonicIndex = jLabel.getDisplayedMnemonicIndex();
        graphics.setColor(jLabel.getForeground());
        SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2, i3);
    }

    protected void paintDisabledText(JLabel jLabel, Graphics graphics, String str, int i2, int i3) {
        int displayedMnemonicIndex = jLabel.getDisplayedMnemonicIndex();
        Color background = jLabel.getBackground();
        graphics.setColor(background.brighter());
        SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2 + 1, i3 + 1);
        graphics.setColor(background.darker());
        SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2, i3);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        JLabel jLabel = (JLabel) jComponent;
        String text = jLabel.getText();
        Icon icon = jLabel.isEnabled() ? jLabel.getIcon() : jLabel.getDisabledIcon();
        if (icon == null && text == null) {
            return;
        }
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jLabel, graphics);
        String strLayout = layout(jLabel, fontMetrics, jComponent.getWidth(), jComponent.getHeight());
        if (icon != null) {
            icon.paintIcon(jComponent, graphics, this.paintIconR.f12372x, this.paintIconR.f12373y);
        }
        if (text != null) {
            View view = (View) jComponent.getClientProperty("html");
            if (view != null) {
                view.paint(graphics, this.paintTextR);
                return;
            }
            int i2 = this.paintTextR.f12372x;
            int ascent = this.paintTextR.f12373y + fontMetrics.getAscent();
            if (jLabel.isEnabled()) {
                paintEnabledText(jLabel, graphics, strLayout, i2, ascent);
            } else {
                paintDisabledText(jLabel, graphics, strLayout, i2, ascent);
            }
        }
    }

    private String layout(JLabel jLabel, FontMetrics fontMetrics, int i2, int i3) {
        Insets insets = jLabel.getInsets(null);
        String text = jLabel.getText();
        Icon icon = jLabel.isEnabled() ? jLabel.getIcon() : jLabel.getDisabledIcon();
        Rectangle rectangle = new Rectangle();
        rectangle.f12372x = insets.left;
        rectangle.f12373y = insets.top;
        rectangle.width = i2 - (insets.left + insets.right);
        rectangle.height = i3 - (insets.top + insets.bottom);
        Rectangle rectangle2 = this.paintIconR;
        Rectangle rectangle3 = this.paintIconR;
        Rectangle rectangle4 = this.paintIconR;
        this.paintIconR.height = 0;
        rectangle4.width = 0;
        rectangle3.f12373y = 0;
        rectangle2.f12372x = 0;
        Rectangle rectangle5 = this.paintTextR;
        Rectangle rectangle6 = this.paintTextR;
        Rectangle rectangle7 = this.paintTextR;
        this.paintTextR.height = 0;
        rectangle7.width = 0;
        rectangle6.f12373y = 0;
        rectangle5.f12372x = 0;
        return layoutCL(jLabel, fontMetrics, text, icon, rectangle, this.paintIconR, this.paintTextR);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        JLabel jLabel = (JLabel) jComponent;
        String text = jLabel.getText();
        Icon icon = jLabel.isEnabled() ? jLabel.getIcon() : jLabel.getDisabledIcon();
        Insets insets = jLabel.getInsets(null);
        Font font = jLabel.getFont();
        int i2 = insets.left + insets.right;
        int i3 = insets.top + insets.bottom;
        if (icon == null && (text == null || (text != null && font == null))) {
            return new Dimension(i2, i3);
        }
        if (text == null || (icon != null && font == null)) {
            return new Dimension(icon.getIconWidth() + i2, icon.getIconHeight() + i3);
        }
        FontMetrics fontMetrics = jLabel.getFontMetrics(font);
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle();
        Rectangle rectangle3 = new Rectangle();
        rectangle.height = 0;
        rectangle.width = 0;
        rectangle.f12373y = 0;
        rectangle.f12372x = 0;
        rectangle2.height = 0;
        rectangle2.width = 0;
        rectangle2.f12373y = 0;
        rectangle2.f12372x = 0;
        rectangle3.f12372x = i2;
        rectangle3.f12373y = i3;
        rectangle3.height = Short.MAX_VALUE;
        rectangle3.width = Short.MAX_VALUE;
        layoutCL(jLabel, fontMetrics, text, icon, rectangle3, rectangle, rectangle2);
        Dimension dimension = new Dimension(Math.max(rectangle.f12372x + rectangle.width, rectangle2.f12372x + rectangle2.width) - Math.min(rectangle.f12372x, rectangle2.f12372x), Math.max(rectangle.f12373y + rectangle.height, rectangle2.f12373y + rectangle2.height) - Math.min(rectangle.f12373y, rectangle2.f12373y));
        dimension.width += i2;
        dimension.height += i3;
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(jComponent);
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            preferredSize.width = (int) (preferredSize.width - (view.getPreferredSpan(0) - view.getMinimumSpan(0)));
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(jComponent);
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            preferredSize.width = (int) (preferredSize.width + (view.getMaximumSpan(0) - view.getPreferredSpan(0)));
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        JLabel jLabel = (JLabel) jComponent;
        String text = jLabel.getText();
        if (text == null || "".equals(text) || jLabel.getFont() == null) {
            return -1;
        }
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        layout(jLabel, fontMetrics, i2, i3);
        return BasicHTML.getBaseline(jLabel, this.paintTextR.f12373y, fontMetrics.getAscent(), this.paintTextR.width, this.paintTextR.height);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        if (jComponent.getClientProperty("html") != null) {
            return Component.BaselineResizeBehavior.OTHER;
        }
        switch (((JLabel) jComponent).getVerticalAlignment()) {
            case 0:
                return Component.BaselineResizeBehavior.CENTER_OFFSET;
            case 1:
                return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
            case 2:
            default:
                return Component.BaselineResizeBehavior.OTHER;
            case 3:
                return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        installDefaults((JLabel) jComponent);
        installComponents((JLabel) jComponent);
        installListeners((JLabel) jComponent);
        installKeyboardActions((JLabel) jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults((JLabel) jComponent);
        uninstallComponents((JLabel) jComponent);
        uninstallListeners((JLabel) jComponent);
        uninstallKeyboardActions((JLabel) jComponent);
    }

    protected void installDefaults(JLabel jLabel) {
        LookAndFeel.installColorsAndFont(jLabel, "Label.background", "Label.foreground", "Label.font");
        LookAndFeel.installProperty(jLabel, "opaque", Boolean.FALSE);
    }

    protected void installListeners(JLabel jLabel) {
        jLabel.addPropertyChangeListener(this);
    }

    protected void installComponents(JLabel jLabel) {
        BasicHTML.updateRenderer(jLabel, jLabel.getText());
        jLabel.setInheritsPopupMenu(true);
    }

    protected void installKeyboardActions(JLabel jLabel) {
        int displayedMnemonic = jLabel.getDisplayedMnemonic();
        Component labelFor = jLabel.getLabelFor();
        if (displayedMnemonic != 0 && labelFor != null) {
            LazyActionMap.installLazyActionMap(jLabel, BasicLabelUI.class, "Label.actionMap");
            InputMap uIInputMap = SwingUtilities.getUIInputMap(jLabel, 2);
            if (uIInputMap == null) {
                uIInputMap = new ComponentInputMapUIResource(jLabel);
                SwingUtilities.replaceUIInputMap(jLabel, 2, uIInputMap);
            }
            uIInputMap.clear();
            uIInputMap.put(KeyStroke.getKeyStroke(displayedMnemonic, BasicLookAndFeel.getFocusAcceleratorKeyMask(), false), BasicRootPaneUI.Actions.PRESS);
            uIInputMap.put(KeyStroke.getKeyStroke(displayedMnemonic, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask()), false), BasicRootPaneUI.Actions.PRESS);
            return;
        }
        InputMap uIInputMap2 = SwingUtilities.getUIInputMap(jLabel, 2);
        if (uIInputMap2 != null) {
            uIInputMap2.clear();
        }
    }

    protected void uninstallDefaults(JLabel jLabel) {
    }

    protected void uninstallListeners(JLabel jLabel) {
        jLabel.removePropertyChangeListener(this);
    }

    protected void uninstallComponents(JLabel jLabel) {
        BasicHTML.updateRenderer(jLabel, "");
    }

    protected void uninstallKeyboardActions(JLabel jLabel) {
        SwingUtilities.replaceUIInputMap(jLabel, 0, null);
        SwingUtilities.replaceUIInputMap(jLabel, 2, null);
        SwingUtilities.replaceUIActionMap(jLabel, null);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        if (System.getSecurityManager() != null) {
            AppContext appContext = AppContext.getAppContext();
            BasicLabelUI basicLabelUI = (BasicLabelUI) appContext.get(BASIC_LABEL_UI_KEY);
            if (basicLabelUI == null) {
                basicLabelUI = new BasicLabelUI();
                appContext.put(BASIC_LABEL_UI_KEY, basicLabelUI);
            }
            return basicLabelUI;
        }
        return labelUI;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName == "text" || "font" == propertyName || "foreground" == propertyName) {
            JLabel jLabel = (JLabel) propertyChangeEvent.getSource();
            BasicHTML.updateRenderer(jLabel, jLabel.getText());
        } else if (propertyName == "labelFor" || propertyName == "displayedMnemonic") {
            installKeyboardActions((JLabel) propertyChangeEvent.getSource());
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicLabelUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String PRESS = "press";
        private static final String RELEASE = "release";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JLabel jLabel = (JLabel) actionEvent.getSource();
            String name = getName();
            if (name == "press") {
                doPress(jLabel);
            } else if (name == "release") {
                doRelease(jLabel, actionEvent.getActionCommand() != null);
            }
        }

        private void doPress(JLabel jLabel) {
            Component labelFor = jLabel.getLabelFor();
            if (labelFor != null && labelFor.isEnabled()) {
                InputMap uIInputMap = SwingUtilities.getUIInputMap(jLabel, 0);
                if (uIInputMap == null) {
                    uIInputMap = new InputMapUIResource();
                    SwingUtilities.replaceUIInputMap(jLabel, 0, uIInputMap);
                }
                int displayedMnemonic = jLabel.getDisplayedMnemonic();
                putOnRelease(uIInputMap, displayedMnemonic, BasicLookAndFeel.getFocusAcceleratorKeyMask());
                putOnRelease(uIInputMap, displayedMnemonic, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask()));
                putOnRelease(uIInputMap, displayedMnemonic, 0);
                putOnRelease(uIInputMap, 18, 0);
                jLabel.requestFocus();
            }
        }

        private void doRelease(JLabel jLabel, boolean z2) {
            Component labelFor = jLabel.getLabelFor();
            if (labelFor != null && labelFor.isEnabled()) {
                if (jLabel.hasFocus()) {
                    InputMap uIInputMap = SwingUtilities.getUIInputMap(jLabel, 0);
                    if (uIInputMap != null) {
                        int displayedMnemonic = jLabel.getDisplayedMnemonic();
                        removeOnRelease(uIInputMap, displayedMnemonic, BasicLookAndFeel.getFocusAcceleratorKeyMask());
                        removeOnRelease(uIInputMap, displayedMnemonic, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask()));
                        removeOnRelease(uIInputMap, displayedMnemonic, 0);
                        removeOnRelease(uIInputMap, 18, 0);
                    }
                    InputMap uIInputMap2 = SwingUtilities.getUIInputMap(jLabel, 2);
                    if (uIInputMap2 == null) {
                        uIInputMap2 = new InputMapUIResource();
                        SwingUtilities.replaceUIInputMap(jLabel, 2, uIInputMap2);
                    }
                    int displayedMnemonic2 = jLabel.getDisplayedMnemonic();
                    if (z2) {
                        putOnRelease(uIInputMap2, 18, 0);
                    } else {
                        putOnRelease(uIInputMap2, displayedMnemonic2, BasicLookAndFeel.getFocusAcceleratorKeyMask());
                        putOnRelease(uIInputMap2, displayedMnemonic2, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask()));
                        putOnRelease(uIInputMap2, displayedMnemonic2, 0);
                    }
                    if ((labelFor instanceof Container) && ((Container) labelFor).isFocusCycleRoot()) {
                        labelFor.requestFocus();
                        return;
                    } else {
                        SwingUtilities2.compositeRequestFocus(labelFor);
                        return;
                    }
                }
                InputMap uIInputMap3 = SwingUtilities.getUIInputMap(jLabel, 2);
                int displayedMnemonic3 = jLabel.getDisplayedMnemonic();
                if (uIInputMap3 != null) {
                    if (z2) {
                        removeOnRelease(uIInputMap3, displayedMnemonic3, BasicLookAndFeel.getFocusAcceleratorKeyMask());
                        removeOnRelease(uIInputMap3, displayedMnemonic3, SwingUtilities2.setAltGraphMask(BasicLookAndFeel.getFocusAcceleratorKeyMask()));
                        removeOnRelease(uIInputMap3, displayedMnemonic3, 0);
                        return;
                    }
                    removeOnRelease(uIInputMap3, 18, 0);
                }
            }
        }

        private void putOnRelease(InputMap inputMap, int i2, int i3) {
            inputMap.put(KeyStroke.getKeyStroke(i2, i3, true), "release");
        }

        private void removeOnRelease(InputMap inputMap, int i2, int i3) {
            inputMap.remove(KeyStroke.getKeyStroke(i2, i3, true));
        }
    }
}
