package javax.swing.colorchooser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/colorchooser/DiagramComponent.class */
final class DiagramComponent extends JComponent implements MouseListener, MouseMotionListener {
    private final ColorPanel panel;
    private final boolean diagram;
    private final Insets insets = new Insets(0, 0, 0, 0);
    private int width;
    private int height;
    private int[] array;
    private BufferedImage image;

    DiagramComponent(ColorPanel colorPanel, boolean z2) {
        this.panel = colorPanel;
        this.diagram = z2;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override // javax.swing.JComponent
    protected void paintComponent(Graphics graphics) {
        getInsets(this.insets);
        this.width = (getWidth() - this.insets.left) - this.insets.right;
        this.height = (getHeight() - this.insets.top) - this.insets.bottom;
        if ((this.image != null && this.width == this.image.getWidth() && this.height == this.image.getHeight()) ? false : true) {
            int i2 = this.width * this.height;
            if (this.array == null || this.array.length < i2) {
                this.array = new int[i2];
            }
            this.image = new BufferedImage(this.width, this.height, 1);
        }
        float f2 = 1.0f / (this.width - 1);
        float f3 = 1.0f / (this.height - 1);
        int i3 = 0;
        float f4 = 0.0f;
        int i4 = 0;
        while (i4 < this.height) {
            if (this.diagram) {
                float f5 = 0.0f;
                int i5 = 0;
                while (i5 < this.width) {
                    this.array[i3] = this.panel.getColor(f5, f4);
                    i5++;
                    f5 += f2;
                    i3++;
                }
            } else {
                int color = this.panel.getColor(f4);
                int i6 = 0;
                while (i6 < this.width) {
                    this.array[i3] = color;
                    i6++;
                    i3++;
                }
            }
            i4++;
            f4 += f3;
        }
        this.image.setRGB(0, 0, this.width, this.height, this.array, 0, this.width);
        graphics.drawImage(this.image, this.insets.left, this.insets.top, this.width, this.height, this);
        if (isEnabled()) {
            this.width--;
            this.height--;
            graphics.setXORMode(Color.WHITE);
            graphics.setColor(Color.BLACK);
            if (this.diagram) {
                int value = getValue(this.panel.getValueX(), this.insets.left, this.width);
                int value2 = getValue(this.panel.getValueY(), this.insets.top, this.height);
                graphics.drawLine(value - 8, value2, value + 8, value2);
                graphics.drawLine(value, value2 - 8, value, value2 + 8);
            } else {
                int value3 = getValue(this.panel.getValueZ(), this.insets.top, this.height);
                graphics.drawLine(this.insets.left, value3, this.insets.left + this.width, value3);
            }
            graphics.setPaintMode();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        mouseDragged(mouseEvent);
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        if (isEnabled()) {
            float value = getValue(mouseEvent.getY(), this.insets.top, this.height);
            if (this.diagram) {
                this.panel.setValue(getValue(mouseEvent.getX(), this.insets.left, this.width), value);
            } else {
                this.panel.setValue(value);
            }
        }
    }

    private static int getValue(float f2, int i2, int i3) {
        return i2 + ((int) (f2 * i3));
    }

    private static float getValue(int i2, int i3, int i4) {
        if (i3 < i2) {
            int i5 = i2 - i3;
            if (i5 < i4) {
                return i5 / i4;
            }
            return 1.0f;
        }
        return 0.0f;
    }
}
