package javax.swing.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/colorchooser/DefaultPreviewPanel.class */
class DefaultPreviewPanel extends JPanel {
    private String sampleText;
    private int squareSize = 25;
    private int squareGap = 5;
    private int innerGap = 5;
    private int textGap = 5;
    private Font font = new Font(Font.DIALOG, 0, 12);
    private int swatchWidth = 50;
    private Color oldColor = null;

    DefaultPreviewPanel() {
    }

    private JColorChooser getColorChooser() {
        return (JColorChooser) SwingUtilities.getAncestorOfClass(JColorChooser.class, this);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        JColorChooser colorChooser = getColorChooser();
        if (colorChooser == null) {
            colorChooser = this;
        }
        FontMetrics fontMetrics = colorChooser.getFontMetrics(getFont());
        fontMetrics.getAscent();
        int height = fontMetrics.getHeight();
        int iStringWidth = SwingUtilities2.stringWidth(colorChooser, fontMetrics, getSampleText());
        return new Dimension((this.squareSize * 3) + (this.squareGap * 2) + this.swatchWidth + iStringWidth + (this.textGap * 3), (height * 3) + (this.textGap * 3));
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        if (this.oldColor == null) {
            this.oldColor = getForeground();
        }
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        if (getComponentOrientation().isLeftToRight()) {
            int iPaintSquares = paintSquares(graphics, 0);
            paintSwatch(graphics, iPaintSquares + paintText(graphics, iPaintSquares));
        } else {
            int iPaintSwatch = paintSwatch(graphics, 0);
            paintSquares(graphics, iPaintSwatch + paintText(graphics, iPaintSwatch));
        }
    }

    private int paintSwatch(Graphics graphics, int i2) {
        graphics.setColor(this.oldColor);
        graphics.fillRect(i2, 0, this.swatchWidth, this.squareSize + (this.squareGap / 2));
        graphics.setColor(getForeground());
        graphics.fillRect(i2, this.squareSize + (this.squareGap / 2), this.swatchWidth, this.squareSize + (this.squareGap / 2));
        return i2 + this.swatchWidth;
    }

    private int paintText(Graphics graphics, int i2) {
        graphics.setFont(getFont());
        JColorChooser colorChooser = getColorChooser();
        if (colorChooser == null) {
            colorChooser = this;
        }
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(colorChooser, graphics);
        int ascent = fontMetrics.getAscent();
        int height = fontMetrics.getHeight();
        int iStringWidth = SwingUtilities2.stringWidth(colorChooser, fontMetrics, getSampleText());
        int i3 = i2 + this.textGap;
        Color foreground = getForeground();
        graphics.setColor(foreground);
        SwingUtilities2.drawString(colorChooser, graphics, getSampleText(), i3 + (this.textGap / 2), ascent + 2);
        graphics.fillRect(i3, height + this.textGap, iStringWidth + this.textGap, height + 2);
        graphics.setColor(Color.black);
        SwingUtilities2.drawString(colorChooser, graphics, getSampleText(), i3 + (this.textGap / 2), height + ascent + this.textGap + 2);
        graphics.setColor(Color.white);
        graphics.fillRect(i3, (height + this.textGap) * 2, iStringWidth + this.textGap, height + 2);
        graphics.setColor(foreground);
        SwingUtilities2.drawString(colorChooser, graphics, getSampleText(), i3 + (this.textGap / 2), ((height + this.textGap) * 2) + ascent + 2);
        return iStringWidth + (this.textGap * 3);
    }

    private int paintSquares(Graphics graphics, int i2) {
        Color foreground = getForeground();
        graphics.setColor(Color.white);
        graphics.fillRect(i2, 0, this.squareSize, this.squareSize);
        graphics.setColor(foreground);
        graphics.fillRect(i2 + this.innerGap, this.innerGap, this.squareSize - (this.innerGap * 2), this.squareSize - (this.innerGap * 2));
        graphics.setColor(Color.white);
        graphics.fillRect(i2 + (this.innerGap * 2), this.innerGap * 2, this.squareSize - (this.innerGap * 4), this.squareSize - (this.innerGap * 4));
        graphics.setColor(foreground);
        graphics.fillRect(i2, this.squareSize + this.squareGap, this.squareSize, this.squareSize);
        graphics.translate(this.squareSize + this.squareGap, 0);
        graphics.setColor(Color.black);
        graphics.fillRect(i2, 0, this.squareSize, this.squareSize);
        graphics.setColor(foreground);
        graphics.fillRect(i2 + this.innerGap, this.innerGap, this.squareSize - (this.innerGap * 2), this.squareSize - (this.innerGap * 2));
        graphics.setColor(Color.white);
        graphics.fillRect(i2 + (this.innerGap * 2), this.innerGap * 2, this.squareSize - (this.innerGap * 4), this.squareSize - (this.innerGap * 4));
        graphics.translate(-(this.squareSize + this.squareGap), 0);
        graphics.translate(this.squareSize + this.squareGap, this.squareSize + this.squareGap);
        graphics.setColor(Color.white);
        graphics.fillRect(i2, 0, this.squareSize, this.squareSize);
        graphics.setColor(foreground);
        graphics.fillRect(i2 + this.innerGap, this.innerGap, this.squareSize - (this.innerGap * 2), this.squareSize - (this.innerGap * 2));
        graphics.translate(-(this.squareSize + this.squareGap), -(this.squareSize + this.squareGap));
        graphics.translate((this.squareSize + this.squareGap) * 2, 0);
        graphics.setColor(Color.white);
        graphics.fillRect(i2, 0, this.squareSize, this.squareSize);
        graphics.setColor(foreground);
        graphics.fillRect(i2 + this.innerGap, this.innerGap, this.squareSize - (this.innerGap * 2), this.squareSize - (this.innerGap * 2));
        graphics.setColor(Color.black);
        graphics.fillRect(i2 + (this.innerGap * 2), this.innerGap * 2, this.squareSize - (this.innerGap * 4), this.squareSize - (this.innerGap * 4));
        graphics.translate(-((this.squareSize + this.squareGap) * 2), 0);
        graphics.translate((this.squareSize + this.squareGap) * 2, this.squareSize + this.squareGap);
        graphics.setColor(Color.black);
        graphics.fillRect(i2, 0, this.squareSize, this.squareSize);
        graphics.setColor(foreground);
        graphics.fillRect(i2 + this.innerGap, this.innerGap, this.squareSize - (this.innerGap * 2), this.squareSize - (this.innerGap * 2));
        graphics.translate(-((this.squareSize + this.squareGap) * 2), -(this.squareSize + this.squareGap));
        return (this.squareSize * 3) + (this.squareGap * 2);
    }

    private String getSampleText() {
        if (this.sampleText == null) {
            this.sampleText = UIManager.getString("ColorChooser.sampleText", getLocale());
        }
        return this.sampleText;
    }
}
