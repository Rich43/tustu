package javax.swing.plaf.synth;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthTextPaneUI.class */
public class SynthTextPaneUI extends SynthEditorPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthTextPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "TextPane";
    }

    @Override // javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        updateForeground(jComponent.getForeground());
        updateFont(jComponent.getFont());
    }

    @Override // javax.swing.plaf.synth.SynthEditorPaneUI, javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI
    protected void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        super.propertyChange(propertyChangeEvent);
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName.equals("foreground")) {
            updateForeground((Color) propertyChangeEvent.getNewValue());
            return;
        }
        if (propertyName.equals("font")) {
            updateFont((Font) propertyChangeEvent.getNewValue());
        } else if (propertyName.equals(Constants.DOCUMENT_PNAME)) {
            JTextComponent component = getComponent();
            updateForeground(component.getForeground());
            updateFont(component.getFont());
        }
    }

    private void updateForeground(Color color) {
        Style style = ((StyledDocument) getComponent().getDocument()).getStyle("default");
        if (style == null) {
            return;
        }
        if (color == null) {
            style.removeAttribute(StyleConstants.Foreground);
        } else {
            StyleConstants.setForeground(style, color);
        }
    }

    private void updateFont(Font font) {
        Style style = ((StyledDocument) getComponent().getDocument()).getStyle("default");
        if (style == null) {
            return;
        }
        if (font == null) {
            style.removeAttribute(StyleConstants.FontFamily);
            style.removeAttribute(StyleConstants.FontSize);
            style.removeAttribute(StyleConstants.Bold);
            style.removeAttribute(StyleConstants.Italic);
            return;
        }
        StyleConstants.setFontFamily(style, font.getName());
        StyleConstants.setFontSize(style, font.getSize());
        StyleConstants.setBold(style, font.isBold());
        StyleConstants.setItalic(style, font.isItalic());
    }

    @Override // javax.swing.plaf.synth.SynthEditorPaneUI
    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        synthContext.getPainter().paintTextPaneBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    @Override // javax.swing.plaf.synth.SynthEditorPaneUI, javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintTextPaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }
}
