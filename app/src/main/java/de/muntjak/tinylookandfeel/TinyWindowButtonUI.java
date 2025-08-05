package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyFrameBorder;
import de.muntjak.tinylookandfeel.borders.TinyInternalFrameBorder;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import de.muntjak.tinylookandfeel.util.DrawRoutines;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyWindowButtonUI.class */
public class TinyWindowButtonUI extends TinyButtonUI {
    private static final HashMap cache = new HashMap();
    protected static final Dimension frameExternalButtonSize = new Dimension(21, 21);
    protected static final Dimension frameInternalButtonSize = new Dimension(17, 17);
    protected static final Dimension framePaletteButtonSize = new Dimension(13, 13);
    private int type;
    public static final int CLOSE = 0;
    public static final int MAXIMIZE = 1;
    public static final int MINIMIZE = 2;
    public static final String EXTERNAL_FRAME_BUTTON_KEY = "externalFrameButton";
    public static final String DISABLED_WINDOW_BUTTON_KEY = "disabledWindowButton";

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyWindowButtonUI$ButtonKey.class */
    private static class ButtonKey {
        private Color background;
        private int size;
        private int type;
        private boolean rollover;
        private boolean pressed;
        private boolean frameSelected;
        private boolean frameMaximized;

        ButtonKey(Color color, int i2, int i3, boolean z2, boolean z3, boolean z4, boolean z5) {
            this.background = color;
            this.size = i2;
            this.type = i3 + 1;
            this.rollover = z2;
            this.pressed = z3;
            this.frameSelected = z4;
            this.frameMaximized = z5;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ButtonKey)) {
                return false;
            }
            ButtonKey buttonKey = (ButtonKey) obj;
            return this.size == buttonKey.size && this.type == buttonKey.type && this.rollover == buttonKey.rollover && this.pressed == buttonKey.pressed && this.frameSelected == buttonKey.frameSelected && this.frameMaximized == buttonKey.frameMaximized && this.background.equals(buttonKey.background);
        }

        public int hashCode() {
            return this.background.hashCode() * this.type * this.size * (this.rollover ? 2 : 1) * (this.pressed ? 8 : 4) * (this.frameSelected ? 32 : 16) * (this.frameMaximized ? 128 : 64);
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    public static ComponentUI createUI(JComponent jComponent) {
        throw new IllegalStateException("Must not be used this way.");
    }

    TinyWindowButtonUI(int i2) {
        this.type = i2;
    }

    @Override // de.muntjak.tinylookandfeel.TinyButtonUI, javax.swing.plaf.metal.MetalButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        abstractButton.setBorder(null);
        abstractButton.setFocusable(false);
    }

    @Override // de.muntjak.tinylookandfeel.TinyButtonUI, javax.swing.plaf.metal.MetalButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected void paintFocus(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
    }

    @Override // de.muntjak.tinylookandfeel.TinyButtonUI, javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        boolean zIsSelected = false;
        boolean zIsFrameMaximized = false;
        Container parent = jComponent.getParent();
        if (parent instanceof TinyInternalFrameTitlePane) {
            zIsSelected = ((TinyInternalFrameTitlePane) parent).isFrameSelected();
            zIsFrameMaximized = ((TinyInternalFrameTitlePane) parent).isFrameMaximized();
        } else if (parent instanceof TinyTitlePane) {
            zIsSelected = ((TinyTitlePane) parent).isSelected();
            zIsFrameMaximized = ((TinyTitlePane) parent).isFrameMaximized();
        }
        int width = abstractButton.getWidth();
        int height = abstractButton.getHeight();
        ColorUIResource color = !zIsSelected ? abstractButton.isEnabled() ? abstractButton.getModel().isPressed() ? this.type == 0 ? Theme.frameButtClosePressedColor.getColor() : Theme.frameButtPressedColor.getColor() : this.type == 0 ? Theme.frameButtCloseColor.getColor() : Theme.frameButtColor.getColor() : this.type == 0 ? Theme.frameButtCloseDisabledColor.getColor() : Theme.frameButtDisabledColor.getColor() : abstractButton.getModel().isPressed() ? (abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed()) ? this.type == 0 ? Theme.frameButtClosePressedColor.getColor() : Theme.frameButtPressedColor.getColor() : this.type == 0 ? Theme.frameButtCloseColor.getColor() : Theme.frameButtColor.getColor() : abstractButton.getModel().isRollover() ? this.type == 0 ? Theme.frameButtCloseRolloverColor.getColor() : Theme.frameButtRolloverColor.getColor() : this.type == 0 ? Theme.frameButtCloseColor.getColor() : Theme.frameButtColor.getColor();
        graphics.setColor(color);
        if (TinyLookAndFeel.controlPanelInstantiated) {
            drawXpButtonNoCache(graphics, abstractButton, color, width, height, zIsSelected, zIsFrameMaximized);
        } else {
            drawXpButton(graphics, abstractButton, color, width, height, zIsSelected, zIsFrameMaximized);
        }
    }

    private void drawXpButton(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, boolean z2, boolean z3) {
        ButtonKey buttonKey = new ButtonKey(color, i2, this.type, abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed(), abstractButton.getModel().isPressed(), z2, z3);
        Object obj = cache.get(buttonKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, 0, 0, abstractButton);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(i2, i3, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        graphics2.setColor(graphics.getColor());
        if (abstractButton.getClientProperty(EXTERNAL_FRAME_BUTTON_KEY) == Boolean.TRUE) {
            drawXpLargeButton(graphics2, abstractButton, color, i2, i3, z2);
        } else {
            graphics2.fillRect(1, 1, i2 - 2, i3 - 2);
            if (z2) {
                graphics2.setColor(TinyInternalFrameBorder.frameUpperColor);
                graphics2.drawLine(0, 0, i2 - 1, 0);
                graphics2.drawLine(0, 1, 0, 1);
                graphics2.drawLine(i2 - 1, 1, i2 - 1, 1);
                graphics2.setColor(TinyInternalFrameBorder.frameLowerColor);
                graphics2.drawLine(0, i3 - 1, i2 - 1, i3 - 1);
                graphics2.drawLine(0, i3 - 2, 0, i3 - 2);
                graphics2.drawLine(i2 - 1, i3 - 2, i2 - 1, i3 - 2);
            } else {
                graphics2.setColor(TinyInternalFrameBorder.disabledUpperColor);
                graphics2.drawLine(0, 0, i2 - 1, 0);
                graphics2.drawLine(0, 1, 0, 1);
                graphics2.drawLine(i2 - 1, 1, i2 - 1, 1);
                graphics2.setColor(TinyInternalFrameBorder.disabledLowerColor);
                graphics2.drawLine(0, i3 - 1, i2 - 1, i3 - 1);
                graphics2.drawLine(0, i3 - 2, 0, i3 - 2);
                graphics2.drawLine(i2 - 1, i3 - 2, i2 - 1, i3 - 2);
            }
            DrawRoutines.drawRoundedBorder(graphics2, !abstractButton.isEnabled() ? this.type == 0 ? Theme.frameButtCloseBorderDisabledColor.getColor() : Theme.frameButtBorderDisabledColor.getColor() : this.type == 0 ? Theme.frameButtCloseBorderColor.getColor() : Theme.frameButtBorderColor.getColor(), 0, 0, i2, i3);
            graphics2.setColor(!abstractButton.isEnabled() ? this.type == 0 ? Theme.frameButtCloseDisabledColor.getColor() : Theme.frameButtDisabledColor.getColor() : this.type == 0 ? Theme.frameButtCloseColor.getColor() : Theme.frameButtColor.getColor());
            graphics2.drawLine(2, 1, i2 - 3, 1);
            graphics2.drawLine(1, 2, 1, i3 - 3);
            graphics2.setColor(!abstractButton.isEnabled() ? this.type == 0 ? Theme.frameButtCloseDisabledColor.getColor() : Theme.frameButtDisabledColor.getColor() : this.type == 0 ? ColorRoutines.darken(Theme.frameButtCloseColor.getColor(), 20) : ColorRoutines.darken(Theme.frameButtColor.getColor(), 20));
            graphics2.drawLine(i2 - 2, 2, i2 - 2, i3 - 3);
            graphics2.drawLine(2, i3 - 2, i2 - 3, i3 - 2);
        }
        if (abstractButton.isEnabled()) {
            if (this.type == 0) {
                graphics2.setColor(Theme.frameSymbolCloseColor.getColor());
            } else {
                graphics2.setColor(Theme.frameSymbolColor.getColor());
            }
        } else if (this.type == 0) {
            graphics2.setColor(Theme.frameSymbolCloseDisabledColor.getColor());
        } else {
            graphics2.setColor(Theme.frameSymbolDisabledColor.getColor());
        }
        drawXpSymbol(graphics2, abstractButton, color, i2, i3, z2, z3);
        graphics2.dispose();
        graphics.drawImage(bufferedImage, 0, 0, abstractButton);
        cache.put(buttonKey, bufferedImage);
    }

    private void drawXpButtonNoCache(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, boolean z2, boolean z3) {
        if (abstractButton.getClientProperty(EXTERNAL_FRAME_BUTTON_KEY) == Boolean.TRUE) {
            drawXpLargeButton(graphics, abstractButton, color, i2, i3, z2);
        } else {
            graphics.fillRect(1, 1, i2 - 2, i3 - 2);
            if (z2) {
                graphics.setColor(TinyInternalFrameBorder.frameUpperColor);
                graphics.drawLine(0, 0, i2 - 1, 0);
                graphics.drawLine(0, 1, 0, 1);
                graphics.drawLine(i2 - 1, 1, i2 - 1, 1);
                graphics.setColor(TinyInternalFrameBorder.frameLowerColor);
                graphics.drawLine(0, i3 - 1, i2 - 1, i3 - 1);
                graphics.drawLine(0, i3 - 2, 0, i3 - 2);
                graphics.drawLine(i2 - 1, i3 - 2, i2 - 1, i3 - 2);
            } else {
                graphics.setColor(TinyInternalFrameBorder.disabledUpperColor);
                graphics.drawLine(0, 0, i2 - 1, 0);
                graphics.drawLine(0, 1, 0, 1);
                graphics.drawLine(i2 - 1, 1, i2 - 1, 1);
                graphics.setColor(TinyInternalFrameBorder.disabledLowerColor);
                graphics.drawLine(0, i3 - 1, i2 - 1, i3 - 1);
                graphics.drawLine(0, i3 - 2, 0, i3 - 2);
                graphics.drawLine(i2 - 1, i3 - 2, i2 - 1, i3 - 2);
            }
            DrawRoutines.drawWindowButtonBorder(graphics, !abstractButton.isEnabled() ? this.type == 0 ? Theme.frameButtCloseBorderDisabledColor.getColor() : Theme.frameButtBorderDisabledColor.getColor() : this.type == 0 ? Theme.frameButtCloseBorderColor.getColor() : Theme.frameButtBorderColor.getColor(), 0, 0, i2, i3);
            graphics.setColor(!abstractButton.isEnabled() ? this.type == 0 ? Theme.frameButtCloseDisabledColor.getColor() : Theme.frameButtDisabledColor.getColor() : this.type == 0 ? Theme.frameButtCloseColor.getColor() : Theme.frameButtColor.getColor());
            graphics.drawLine(2, 1, i2 - 3, 1);
            graphics.drawLine(1, 2, 1, i3 - 3);
            graphics.setColor(!abstractButton.isEnabled() ? this.type == 0 ? Theme.frameButtCloseDisabledColor.getColor() : Theme.frameButtDisabledColor.getColor() : this.type == 0 ? ColorRoutines.darken(Theme.frameButtCloseColor.getColor(), 20) : ColorRoutines.darken(Theme.frameButtColor.getColor(), 20));
            graphics.drawLine(i2 - 2, 2, i2 - 2, i3 - 3);
            graphics.drawLine(2, i3 - 2, i2 - 3, i3 - 2);
        }
        if (abstractButton.isEnabled()) {
            if (this.type == 0) {
                graphics.setColor(Theme.frameSymbolCloseColor.getColor());
            } else {
                graphics.setColor(Theme.frameSymbolColor.getColor());
            }
        } else if (this.type == 0) {
            graphics.setColor(Theme.frameSymbolCloseDisabledColor.getColor());
        } else {
            graphics.setColor(Theme.frameSymbolDisabledColor.getColor());
        }
        drawXpSymbol(graphics, abstractButton, color, i2, i3, z2, z3);
    }

    private void drawXpLargeButton(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, boolean z2) {
        graphics.drawLine(1, 2, 1, i3 - 2);
        graphics.drawLine(1, 1, i2 - 2, 1);
        graphics.drawLine(i2 - 2, i3 - 2, i2 - 2, i3 - 2);
        boolean z3 = abstractButton.getClientProperty(DISABLED_WINDOW_BUTTON_KEY) == Boolean.TRUE;
        if (z3) {
            graphics.setColor(TinyFrameBorder.buttonUpperDisabledColor);
        } else {
            graphics.setColor(TinyTitlePane.buttonUpperColor);
        }
        graphics.drawLine(0, 0, i2 - 1, 0);
        graphics.drawLine(0, 1, 0, 1);
        graphics.drawLine(i2 - 1, 1, i2 - 1, 1);
        if (z3) {
            graphics.setColor(TinyFrameBorder.buttonLowerDisabledColor);
        } else {
            graphics.setColor(TinyTitlePane.buttonLowerColor);
        }
        graphics.drawLine(0, i3 - 1, i2 - 1, i3 - 1);
        graphics.drawLine(0, i3 - 2, 0, i3 - 2);
        graphics.drawLine(i2 - 1, i3 - 2, i2 - 1, i3 - 2);
        if (z3) {
            graphics.setColor(ColorRoutines.darken(color, 14));
        } else {
            graphics.setColor(ColorRoutines.darken(color, 28));
        }
        graphics.drawLine(i2 - 2, 2, i2 - 2, i3 - 3);
        graphics.drawLine(2, i3 - 2, i2 - 3, i3 - 2);
        int value = Theme.frameButtSpreadLight.getValue();
        int value2 = Theme.frameButtSpreadDark.getValue();
        if (abstractButton.isEnabled()) {
            if (this.type == 0) {
                value = Theme.frameButtCloseSpreadLight.getValue();
                value2 = Theme.frameButtCloseSpreadDark.getValue();
            }
        } else if (this.type == 0) {
            value = Theme.frameButtCloseSpreadLightDisabled.getValue();
            value2 = Theme.frameButtCloseSpreadDarkDisabled.getValue();
        } else {
            value = Theme.frameButtSpreadLightDisabled.getValue();
            value2 = Theme.frameButtSpreadDarkDisabled.getValue();
        }
        float f2 = (10.0f * value) / (i3 - 5);
        float f3 = (10.0f * value2) / (i3 - 5);
        int i4 = i3 / 2;
        for (int i5 = 2; i5 < i3 - 2; i5++) {
            if (i5 < i4) {
                graphics.setColor(ColorRoutines.lighten(color, (int) ((i4 - i5) * f2)));
            } else if (i5 == i4) {
                graphics.setColor(color);
            } else {
                graphics.setColor(ColorRoutines.darken(color, (int) ((i5 - i4) * f3)));
            }
            graphics.drawLine(2, i5, i2 - 3, i5);
        }
        if (abstractButton.isEnabled()) {
            if (this.type == 0) {
                DrawRoutines.drawWindowButtonBorder(graphics, Theme.frameButtCloseBorderColor.getColor(), 0, 0, i2, i3);
                return;
            } else {
                DrawRoutines.drawWindowButtonBorder(graphics, Theme.frameButtBorderColor.getColor(), 0, 0, i2, i3);
                return;
            }
        }
        if (this.type == 0) {
            DrawRoutines.drawWindowButtonBorder(graphics, Theme.frameButtCloseBorderDisabledColor.getColor(), 0, 0, i2, i3);
        } else {
            DrawRoutines.drawWindowButtonBorder(graphics, Theme.frameButtBorderDisabledColor.getColor(), 0, 0, i2, i3);
        }
    }

    private void drawXpSymbol(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, boolean z2, boolean z3) {
        if (abstractButton.getClientProperty(EXTERNAL_FRAME_BUTTON_KEY) == Boolean.TRUE) {
            drawXpLargeSymbol(graphics, abstractButton, color, i2, i3, z2, z3);
        }
        if ((abstractButton.getParent() instanceof TinyInternalFrameTitlePane) && ((TinyInternalFrameTitlePane) abstractButton.getParent()).isPalette()) {
            drawXpSmallSymbol(graphics, abstractButton, color, i2, i3, z2, z3);
            return;
        }
        if (z2) {
            if (abstractButton.getModel().isPressed() && (abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed())) {
                if (this.type == 0) {
                    graphics.setColor(Theme.frameSymbolClosePressedColor.getColor());
                } else {
                    graphics.setColor(Theme.frameSymbolPressedColor.getColor());
                }
            } else if (this.type == 0) {
                graphics.setColor(Theme.frameSymbolCloseColor.getColor());
            } else {
                graphics.setColor(Theme.frameSymbolColor.getColor());
            }
        } else if (!abstractButton.isEnabled() || abstractButton.getModel().isPressed()) {
            if (this.type == 0) {
                if (abstractButton.getModel().isPressed()) {
                    graphics.setColor(Theme.frameSymbolClosePressedColor.getColor());
                } else {
                    graphics.setColor(Theme.frameSymbolCloseDisabledColor.getColor());
                }
            } else if (abstractButton.getModel().isPressed()) {
                graphics.setColor(Theme.frameSymbolPressedColor.getColor());
            } else {
                graphics.setColor(Theme.frameSymbolDisabledColor.getColor());
            }
        } else if (this.type == 0) {
            graphics.setColor(Theme.frameSymbolCloseColor.getColor());
        } else {
            graphics.setColor(Theme.frameSymbolColor.getColor());
        }
        switch (this.type) {
            case 0:
                graphics.drawLine(0 + 4, 0 + 3, 0 + 4, 0 + 3);
                graphics.drawLine(0 + 12, 0 + 3, 0 + 12, 0 + 3);
                graphics.drawLine(0 + 3, 0 + 4, 0 + 5, 0 + 4);
                graphics.drawLine(0 + 11, 0 + 4, 0 + 13, 0 + 4);
                graphics.drawLine(0 + 4, 0 + 5, 0 + 6, 0 + 5);
                graphics.drawLine(0 + 10, 0 + 5, 0 + 12, 0 + 5);
                graphics.drawLine(0 + 5, 0 + 6, 0 + 7, 0 + 6);
                graphics.drawLine(0 + 9, 0 + 6, 0 + 11, 0 + 6);
                graphics.drawLine(0 + 6, 0 + 7, 0 + 10, 0 + 7);
                graphics.drawLine(0 + 7, 0 + 8, 0 + 9, 0 + 8);
                graphics.drawLine(0 + 4, 0 + 13, 0 + 4, 0 + 13);
                graphics.drawLine(0 + 12, 0 + 13, 0 + 12, 0 + 13);
                graphics.drawLine(0 + 3, 0 + 12, 0 + 5, 0 + 12);
                graphics.drawLine(0 + 11, 0 + 12, 0 + 13, 0 + 12);
                graphics.drawLine(0 + 4, 0 + 11, 0 + 6, 0 + 11);
                graphics.drawLine(0 + 10, 0 + 11, 0 + 12, 0 + 11);
                graphics.drawLine(0 + 5, 0 + 10, 0 + 7, 0 + 10);
                graphics.drawLine(0 + 9, 0 + 10, 0 + 11, 0 + 10);
                graphics.drawLine(0 + 6, 0 + 9, 0 + 10, 0 + 9);
                break;
            case 1:
                if (!z3) {
                    graphics.fillRect(0 + 3, 0 + 3, 11, 2);
                    graphics.drawRect(0 + 3, 0 + 5, 10, 8);
                    break;
                } else {
                    graphics.fillRect(0 + 5, 0 + 3, 8, 2);
                    graphics.drawLine(0 + 12, 0 + 5, 0 + 12, 0 + 9);
                    graphics.drawLine(0 + 11, 0 + 9, 0 + 11, 0 + 9);
                    graphics.drawLine(0 + 3, 0 + 6, 0 + 9, 0 + 6);
                    graphics.drawRect(0 + 3, 0 + 7, 6, 5);
                    break;
                }
            case 2:
                graphics.fillRect(0 + 3, 0 + 11, 7, 3);
                break;
        }
    }

    private void drawXpSmallSymbol(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, boolean z2, boolean z3) {
        if (z2) {
            if (abstractButton.getModel().isPressed() && (abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed())) {
                if (this.type == 0) {
                    graphics.setColor(Theme.frameSymbolClosePressedColor.getColor());
                } else {
                    graphics.setColor(Theme.frameSymbolPressedColor.getColor());
                }
            } else if (this.type == 0) {
                graphics.setColor(Theme.frameSymbolCloseColor.getColor());
            } else {
                graphics.setColor(Theme.frameSymbolColor.getColor());
            }
        } else if (!abstractButton.isEnabled() || abstractButton.getModel().isPressed()) {
            if (this.type == 0) {
                if (abstractButton.getModel().isPressed()) {
                    graphics.setColor(Theme.frameSymbolClosePressedColor.getColor());
                } else {
                    graphics.setColor(Theme.frameSymbolCloseDisabledColor.getColor());
                }
            } else if (abstractButton.getModel().isPressed()) {
                graphics.setColor(Theme.frameSymbolPressedColor.getColor());
            } else {
                graphics.setColor(Theme.frameSymbolDisabledColor.getColor());
            }
        } else if (this.type == 0) {
            graphics.setColor(Theme.frameSymbolCloseColor.getColor());
        } else {
            graphics.setColor(Theme.frameSymbolColor.getColor());
        }
        switch (this.type) {
            case 0:
                graphics.drawLine(0 + 3, 0 + 2, 0 + 3, 0 + 2);
                graphics.drawLine(0 + 9, 0 + 2, 0 + 9, 0 + 2);
                graphics.drawLine(0 + 2, 0 + 3, 0 + 4, 0 + 3);
                graphics.drawLine(0 + 8, 0 + 3, 0 + 10, 0 + 3);
                graphics.drawLine(0 + 3, 0 + 4, 0 + 5, 0 + 4);
                graphics.drawLine(0 + 7, 0 + 4, 0 + 9, 0 + 4);
                graphics.drawLine(0 + 4, 0 + 5, 0 + 8, 0 + 5);
                graphics.drawLine(0 + 5, 0 + 6, 0 + 7, 0 + 6);
                graphics.drawLine(0 + 4, 0 + 7, 0 + 8, 0 + 7);
                graphics.drawLine(0 + 3, 0 + 8, 0 + 5, 0 + 8);
                graphics.drawLine(0 + 7, 0 + 8, 0 + 9, 0 + 8);
                graphics.drawLine(0 + 2, 0 + 9, 0 + 4, 0 + 9);
                graphics.drawLine(0 + 8, 0 + 9, 0 + 10, 0 + 9);
                graphics.drawLine(0 + 3, 0 + 10, 0 + 3, 0 + 10);
                graphics.drawLine(0 + 9, 0 + 10, 0 + 9, 0 + 10);
                break;
            case 1:
                if (!z3) {
                    graphics.drawLine(0 + 3, 0 + 3, 0 + 9, 0 + 3);
                    graphics.drawRect(0 + 3, 0 + 4, 6, 5);
                    break;
                } else {
                    graphics.drawRect(0 + 3, 0 + 6, 4, 3);
                    graphics.drawLine(0 + 3, 0 + 5, 0 + 7, 0 + 5);
                    graphics.fillRect(0 + 5, 0 + 2, 5, 0 + 2);
                    graphics.drawLine(0 + 9, 0 + 4, 0 + 9, 0 + 7);
                    break;
                }
            case 2:
                graphics.fillRect(0 + 3, 0 + 8, 5, 2);
                break;
        }
    }

    private void drawXpLargeSymbol(Graphics graphics, AbstractButton abstractButton, Color color, int i2, int i3, boolean z2, boolean z3) {
        if (z2) {
            if (abstractButton.getModel().isPressed() && (abstractButton.getModel().isRollover() || abstractButton.getModel().isArmed())) {
                if (this.type == 0) {
                    graphics.setColor(Theme.frameSymbolClosePressedColor.getColor());
                } else {
                    graphics.setColor(Theme.frameSymbolPressedColor.getColor());
                }
            } else if (this.type == 0) {
                graphics.setColor(Theme.frameSymbolCloseColor.getColor());
            } else {
                graphics.setColor(Theme.frameSymbolColor.getColor());
            }
        } else if (!abstractButton.isEnabled() || abstractButton.getModel().isPressed()) {
            if (this.type == 0) {
                if (abstractButton.getModel().isPressed()) {
                    graphics.setColor(Theme.frameSymbolClosePressedColor.getColor());
                } else {
                    graphics.setColor(Theme.frameSymbolCloseDisabledColor.getColor());
                }
            } else if (abstractButton.getModel().isPressed()) {
                graphics.setColor(Theme.frameSymbolPressedColor.getColor());
            } else {
                graphics.setColor(Theme.frameSymbolDisabledColor.getColor());
            }
        } else if (this.type == 0) {
            graphics.setColor(Theme.frameSymbolCloseColor.getColor());
        } else {
            graphics.setColor(Theme.frameSymbolColor.getColor());
        }
        switch (this.type) {
            case 0:
                graphics.drawLine(0 + 5, 0 + 5, 0 + 6, 0 + 5);
                graphics.drawLine(0 + 14, 0 + 5, 0 + 15, 0 + 5);
                graphics.drawLine(0 + 5, 0 + 6, 0 + 7, 0 + 6);
                graphics.drawLine(0 + 13, 0 + 6, 0 + 15, 0 + 6);
                graphics.drawLine(0 + 6, 0 + 7, 0 + 8, 0 + 7);
                graphics.drawLine(0 + 12, 0 + 7, 0 + 14, 0 + 7);
                graphics.drawLine(0 + 7, 0 + 8, 0 + 9, 0 + 8);
                graphics.drawLine(0 + 11, 0 + 8, 0 + 13, 0 + 8);
                graphics.drawLine(0 + 8, 0 + 9, 0 + 12, 0 + 9);
                graphics.drawLine(0 + 9, 0 + 10, 0 + 11, 0 + 10);
                graphics.drawLine(0 + 5, 0 + 15, 0 + 6, 0 + 15);
                graphics.drawLine(0 + 14, 0 + 15, 0 + 15, 0 + 15);
                graphics.drawLine(0 + 5, 0 + 14, 0 + 7, 0 + 14);
                graphics.drawLine(0 + 13, 0 + 14, 0 + 15, 0 + 14);
                graphics.drawLine(0 + 6, 0 + 13, 0 + 8, 0 + 13);
                graphics.drawLine(0 + 12, 0 + 13, 0 + 14, 0 + 13);
                graphics.drawLine(0 + 7, 0 + 12, 0 + 9, 0 + 12);
                graphics.drawLine(0 + 11, 0 + 12, 0 + 13, 0 + 12);
                graphics.drawLine(0 + 8, 0 + 11, 0 + 12, 0 + 11);
                break;
            case 1:
                if (!z3) {
                    graphics.fillRect(0 + 5, 0 + 6, 10, 2);
                    break;
                } else {
                    graphics.drawLine(0 + 8, 0 + 6, 0 + 13, 0 + 6);
                    graphics.drawLine(0 + 5, 0 + 10, 0 + 10, 0 + 10);
                    break;
                }
            case 2:
                graphics.fillRect(0 + 5, 0 + 14, 6, 2);
                break;
        }
        graphics.setColor(abstractButton.isEnabled() ? Theme.frameSymbolLightColor.getColor() : Theme.frameSymbolLightDisabledColor.getColor());
        switch (this.type) {
            case 1:
                if (!z3) {
                    graphics.drawLine(0 + 4, 0 + 6, 0 + 4, 0 + 15);
                    graphics.drawLine(0 + 4, 0 + 16, 0 + 15, 0 + 16);
                    graphics.drawLine(0 + 6, 0 + 8, 0 + 13, 0 + 8);
                    graphics.drawLine(0 + 14, 0 + 8, 0 + 14, 0 + 14);
                    break;
                } else {
                    graphics.drawLine(0 + 7, 0 + 6, 0 + 7, 0 + 8);
                    graphics.drawLine(0 + 9, 0 + 7, 0 + 13, 0 + 7);
                    graphics.drawLine(0 + 13, 0 + 8, 0 + 13, 0 + 10);
                    graphics.drawLine(0 + 12, 0 + 12, 0 + 14, 0 + 12);
                    graphics.drawLine(0 + 4, 0 + 10, 0 + 4, 0 + 16);
                    graphics.drawLine(0 + 5, 0 + 16, 0 + 11, 0 + 16);
                    graphics.drawLine(0 + 6, 0 + 11, 0 + 10, 0 + 11);
                    graphics.drawLine(0 + 10, 0 + 12, 0 + 10, 0 + 14);
                    break;
                }
            case 2:
                graphics.drawLine(0 + 4, 0 + 13, 0 + 4, 0 + 16);
                graphics.drawLine(0 + 5, 0 + 16, 0 + 11, 0 + 16);
                break;
        }
        graphics.setColor(this.type == 0 ? abstractButton.isEnabled() ? Theme.frameSymbolCloseDarkColor.getColor() : Theme.frameSymbolCloseDarkDisabledColor.getColor() : abstractButton.isEnabled() ? Theme.frameSymbolDarkColor.getColor() : Theme.frameSymbolDarkDisabledColor.getColor());
        switch (this.type) {
            case 0:
                graphics.drawLine(0 + 5, 0 + 4, 0 + 6, 0 + 4);
                graphics.drawLine(0 + 14, 0 + 4, 0 + 15, 0 + 4);
                graphics.drawLine(0 + 7, 0 + 5, 0 + 7, 0 + 5);
                graphics.drawLine(0 + 13, 0 + 5, 0 + 13, 0 + 5);
                graphics.drawLine(0 + 8, 0 + 6, 0 + 8, 0 + 6);
                graphics.drawLine(0 + 12, 0 + 6, 0 + 12, 0 + 6);
                graphics.drawLine(0 + 9, 0 + 7, 0 + 9, 0 + 7);
                graphics.drawLine(0 + 11, 0 + 7, 0 + 11, 0 + 7);
                graphics.drawLine(0 + 10, 0 + 8, 0 + 10, 0 + 8);
                graphics.drawLine(0 + 8, 0 + 10, 0 + 8, 0 + 10);
                graphics.drawLine(0 + 12, 0 + 10, 0 + 12, 0 + 10);
                graphics.drawLine(0 + 7, 0 + 11, 0 + 7, 0 + 11);
                graphics.drawLine(0 + 13, 0 + 11, 0 + 13, 0 + 11);
                graphics.drawLine(0 + 6, 0 + 12, 0 + 6, 0 + 12);
                graphics.drawLine(0 + 14, 0 + 12, 0 + 14, 0 + 12);
                graphics.drawLine(0 + 5, 0 + 13, 0 + 5, 0 + 13);
                graphics.drawLine(0 + 15, 0 + 13, 0 + 15, 0 + 13);
                graphics.drawLine(0 + 4, 0 + 14, 0 + 4, 0 + 14);
                graphics.drawLine(0 + 16, 0 + 14, 0 + 16, 0 + 14);
                break;
            case 1:
                if (!z3) {
                    graphics.drawLine(0 + 5, 0 + 5, 0 + 14, 0 + 5);
                    graphics.drawLine(0 + 15, 0 + 5, 0 + 15, 0 + 15);
                    graphics.drawLine(0 + 5, 0 + 15, 0 + 14, 0 + 15);
                    graphics.drawLine(0 + 5, 0 + 8, 0 + 5, 0 + 14);
                    break;
                } else {
                    graphics.drawLine(0 + 8, 0 + 5, 0 + 14, 0 + 5);
                    graphics.drawLine(0 + 14, 0 + 6, 0 + 14, 0 + 11);
                    graphics.drawLine(0 + 12, 0 + 11, 0 + 13, 0 + 11);
                    graphics.drawLine(0 + 8, 0 + 7, 0 + 8, 0 + 8);
                    graphics.drawLine(0 + 5, 0 + 9, 0 + 11, 0 + 9);
                    graphics.drawLine(0 + 11, 0 + 10, 0 + 11, 0 + 15);
                    graphics.drawLine(0 + 5, 0 + 15, 0 + 10, 0 + 15);
                    graphics.drawLine(0 + 5, 0 + 11, 0 + 5, 0 + 14);
                    break;
                }
            case 2:
                graphics.drawLine(0 + 5, 0 + 13, 0 + 10, 0 + 13);
                graphics.drawLine(0 + 11, 0 + 13, 0 + 11, 0 + 15);
                break;
        }
    }

    public static TinyWindowButtonUI createButtonUIForType(int i2) {
        return new TinyWindowButtonUI(i2);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return ((AbstractButton) jComponent).getClientProperty(EXTERNAL_FRAME_BUTTON_KEY) == Boolean.TRUE ? frameExternalButtonSize : ((jComponent.getParent() instanceof TinyInternalFrameTitlePane) && ((TinyInternalFrameTitlePane) jComponent.getParent()).isPalette()) ? framePaletteButtonSize : frameInternalButtonSize;
    }
}
