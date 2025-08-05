package javax.swing.colorchooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/* compiled from: DefaultSwatchChooserPanel.java */
/* loaded from: rt.jar:javax/swing/colorchooser/SwatchPanel.class */
class SwatchPanel extends JPanel {
    protected Color[] colors;
    protected Dimension swatchSize;
    protected Dimension numSwatches;
    protected Dimension gap;
    private int selRow;
    private int selCol;

    static /* synthetic */ int access$010(SwatchPanel swatchPanel) {
        int i2 = swatchPanel.selRow;
        swatchPanel.selRow = i2 - 1;
        return i2;
    }

    static /* synthetic */ int access$008(SwatchPanel swatchPanel) {
        int i2 = swatchPanel.selRow;
        swatchPanel.selRow = i2 + 1;
        return i2;
    }

    static /* synthetic */ int access$110(SwatchPanel swatchPanel) {
        int i2 = swatchPanel.selCol;
        swatchPanel.selCol = i2 - 1;
        return i2;
    }

    static /* synthetic */ int access$108(SwatchPanel swatchPanel) {
        int i2 = swatchPanel.selCol;
        swatchPanel.selCol = i2 + 1;
        return i2;
    }

    public SwatchPanel() {
        initValues();
        initColors();
        setToolTipText("");
        setOpaque(true);
        setBackground(Color.white);
        setFocusable(true);
        setInheritsPopupMenu(true);
        addFocusListener(new FocusAdapter() { // from class: javax.swing.colorchooser.SwatchPanel.1
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusGained(FocusEvent focusEvent) {
                SwatchPanel.this.repaint();
            }

            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusLost(FocusEvent focusEvent) {
                SwatchPanel.this.repaint();
            }
        });
        addKeyListener(new KeyAdapter() { // from class: javax.swing.colorchooser.SwatchPanel.2
            @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case 35:
                        SwatchPanel.this.selCol = SwatchPanel.this.numSwatches.width - 1;
                        SwatchPanel.this.selRow = SwatchPanel.this.numSwatches.height - 1;
                        SwatchPanel.this.repaint();
                        break;
                    case 36:
                        SwatchPanel.this.selCol = 0;
                        SwatchPanel.this.selRow = 0;
                        SwatchPanel.this.repaint();
                        break;
                    case 37:
                        if (SwatchPanel.this.selCol <= 0 || !SwatchPanel.this.getComponentOrientation().isLeftToRight()) {
                            if (SwatchPanel.this.selCol < SwatchPanel.this.numSwatches.width - 1 && !SwatchPanel.this.getComponentOrientation().isLeftToRight()) {
                                SwatchPanel.access$108(SwatchPanel.this);
                                SwatchPanel.this.repaint();
                                break;
                            }
                        } else {
                            SwatchPanel.access$110(SwatchPanel.this);
                            SwatchPanel.this.repaint();
                            break;
                        }
                        break;
                    case 38:
                        if (SwatchPanel.this.selRow > 0) {
                            SwatchPanel.access$010(SwatchPanel.this);
                            SwatchPanel.this.repaint();
                            break;
                        }
                        break;
                    case 39:
                        if (SwatchPanel.this.selCol >= SwatchPanel.this.numSwatches.width - 1 || !SwatchPanel.this.getComponentOrientation().isLeftToRight()) {
                            if (SwatchPanel.this.selCol > 0 && !SwatchPanel.this.getComponentOrientation().isLeftToRight()) {
                                SwatchPanel.access$110(SwatchPanel.this);
                                SwatchPanel.this.repaint();
                                break;
                            }
                        } else {
                            SwatchPanel.access$108(SwatchPanel.this);
                            SwatchPanel.this.repaint();
                            break;
                        }
                        break;
                    case 40:
                        if (SwatchPanel.this.selRow < SwatchPanel.this.numSwatches.height - 1) {
                            SwatchPanel.access$008(SwatchPanel.this);
                            SwatchPanel.this.repaint();
                            break;
                        }
                        break;
                }
            }
        });
    }

    public Color getSelectedColor() {
        return getColorForCell(this.selCol, this.selRow);
    }

    protected void initValues() {
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        int i2;
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        for (int i3 = 0; i3 < this.numSwatches.height; i3++) {
            int i4 = i3 * (this.swatchSize.height + this.gap.height);
            for (int i5 = 0; i5 < this.numSwatches.width; i5++) {
                Color colorForCell = getColorForCell(i5, i3);
                graphics.setColor(colorForCell);
                if (!getComponentOrientation().isLeftToRight()) {
                    i2 = ((this.numSwatches.width - i5) - 1) * (this.swatchSize.width + this.gap.width);
                } else {
                    i2 = i5 * (this.swatchSize.width + this.gap.width);
                }
                graphics.fillRect(i2, i4, this.swatchSize.width, this.swatchSize.height);
                graphics.setColor(Color.black);
                graphics.drawLine((i2 + this.swatchSize.width) - 1, i4, (i2 + this.swatchSize.width) - 1, (i4 + this.swatchSize.height) - 1);
                graphics.drawLine(i2, (i4 + this.swatchSize.height) - 1, (i2 + this.swatchSize.width) - 1, (i4 + this.swatchSize.height) - 1);
                if (this.selRow == i3 && this.selCol == i5 && isFocusOwner()) {
                    graphics.setColor(new Color(colorForCell.getRed() < 125 ? 255 : 0, colorForCell.getGreen() < 125 ? 255 : 0, colorForCell.getBlue() < 125 ? 255 : 0));
                    graphics.drawLine(i2, i4, (i2 + this.swatchSize.width) - 1, i4);
                    graphics.drawLine(i2, i4, i2, (i4 + this.swatchSize.height) - 1);
                    graphics.drawLine((i2 + this.swatchSize.width) - 1, i4, (i2 + this.swatchSize.width) - 1, (i4 + this.swatchSize.height) - 1);
                    graphics.drawLine(i2, (i4 + this.swatchSize.height) - 1, (i2 + this.swatchSize.width) - 1, (i4 + this.swatchSize.height) - 1);
                    graphics.drawLine(i2, i4, (i2 + this.swatchSize.width) - 1, (i4 + this.swatchSize.height) - 1);
                    graphics.drawLine(i2, (i4 + this.swatchSize.height) - 1, (i2 + this.swatchSize.width) - 1, i4);
                }
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension((this.numSwatches.width * (this.swatchSize.width + this.gap.width)) - 1, (this.numSwatches.height * (this.swatchSize.height + this.gap.height)) - 1);
    }

    protected void initColors() {
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        Color colorForLocation = getColorForLocation(mouseEvent.getX(), mouseEvent.getY());
        return colorForLocation.getRed() + ", " + colorForLocation.getGreen() + ", " + colorForLocation.getBlue();
    }

    public void setSelectedColorFromLocation(int i2, int i3) {
        if (!getComponentOrientation().isLeftToRight()) {
            this.selCol = (this.numSwatches.width - (i2 / (this.swatchSize.width + this.gap.width))) - 1;
        } else {
            this.selCol = i2 / (this.swatchSize.width + this.gap.width);
        }
        this.selRow = i3 / (this.swatchSize.height + this.gap.height);
        repaint();
    }

    public Color getColorForLocation(int i2, int i3) {
        int i4;
        if (!getComponentOrientation().isLeftToRight()) {
            i4 = (this.numSwatches.width - (i2 / (this.swatchSize.width + this.gap.width))) - 1;
        } else {
            i4 = i2 / (this.swatchSize.width + this.gap.width);
        }
        return getColorForCell(i4, i3 / (this.swatchSize.height + this.gap.height));
    }

    private Color getColorForCell(int i2, int i3) {
        return this.colors[(i3 * this.numSwatches.width) + i2];
    }
}
