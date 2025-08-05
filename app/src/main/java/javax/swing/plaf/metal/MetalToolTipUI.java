package javax.swing.plaf.metal;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.text.View;
import sun.swing.SwingUtilities2;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalToolTipUI.class */
public class MetalToolTipUI extends BasicToolTipUI {
    static MetalToolTipUI sharedInstance = new MetalToolTipUI();
    private Font smallFont;
    private JToolTip tip;
    public static final int padSpaceBetweenStrings = 12;
    private String acceleratorDelimiter;

    public static ComponentUI createUI(JComponent jComponent) {
        return sharedInstance;
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        this.tip = (JToolTip) jComponent;
        Font font = jComponent.getFont();
        this.smallFont = new Font(font.getName(), font.getStyle(), font.getSize() - 2);
        this.acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");
        if (this.acceleratorDelimiter == null) {
            this.acceleratorDelimiter = LanguageTag.SEP;
        }
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        this.tip = null;
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        int ascent;
        JToolTip jToolTip = (JToolTip) jComponent;
        Font font = jComponent.getFont();
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics, font);
        Dimension size = jComponent.getSize();
        graphics.setColor(jComponent.getForeground());
        String tipText = jToolTip.getTipText();
        if (tipText == null) {
            tipText = "";
        }
        String acceleratorString = getAcceleratorString(jToolTip);
        int iCalcAccelSpacing = calcAccelSpacing(jComponent, SwingUtilities2.getFontMetrics(jComponent, graphics, this.smallFont), acceleratorString);
        Insets insets = jToolTip.getInsets();
        Rectangle rectangle = new Rectangle(insets.left + 3, insets.top, ((size.width - (insets.left + insets.right)) - 6) - iCalcAccelSpacing, size.height - (insets.top + insets.bottom));
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            view.paint(graphics, rectangle);
            ascent = BasicHTML.getHTMLBaseline(view, rectangle.width, rectangle.height);
        } else {
            graphics.setFont(font);
            SwingUtilities2.drawString(jToolTip, graphics, tipText, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
            ascent = fontMetrics.getAscent();
        }
        if (!acceleratorString.equals("")) {
            graphics.setFont(this.smallFont);
            graphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            SwingUtilities2.drawString(jToolTip, graphics, acceleratorString, ((((jToolTip.getWidth() - 1) - insets.right) - iCalcAccelSpacing) + 12) - 3, rectangle.f12373y + ascent);
        }
    }

    private int calcAccelSpacing(JComponent jComponent, FontMetrics fontMetrics, String str) {
        if (str.equals("")) {
            return 0;
        }
        return 12 + SwingUtilities2.stringWidth(jComponent, fontMetrics, str);
    }

    @Override // javax.swing.plaf.basic.BasicToolTipUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension preferredSize = super.getPreferredSize(jComponent);
        String acceleratorString = getAcceleratorString((JToolTip) jComponent);
        if (!acceleratorString.equals("")) {
            preferredSize.width += calcAccelSpacing(jComponent, jComponent.getFontMetrics(this.smallFont), acceleratorString);
        }
        return preferredSize;
    }

    protected boolean isAcceleratorHidden() {
        Boolean bool = (Boolean) UIManager.get("ToolTip.hideAccelerator");
        return bool != null && bool.booleanValue();
    }

    private String getAcceleratorString(JToolTip jToolTip) {
        this.tip = jToolTip;
        String acceleratorString = getAcceleratorString();
        this.tip = null;
        return acceleratorString;
    }

    public String getAcceleratorString() {
        KeyStroke[] keyStrokeArrKeys;
        if (this.tip == null || isAcceleratorHidden()) {
            return "";
        }
        JComponent component = this.tip.getComponent();
        if (!(component instanceof AbstractButton) || (keyStrokeArrKeys = component.getInputMap(2).keys()) == null) {
            return "";
        }
        String str = "";
        if (0 < keyStrokeArrKeys.length) {
            str = KeyEvent.getKeyModifiersText(keyStrokeArrKeys[0].getModifiers()) + this.acceleratorDelimiter + KeyEvent.getKeyText(keyStrokeArrKeys[0].getKeyCode());
        }
        return str;
    }
}
