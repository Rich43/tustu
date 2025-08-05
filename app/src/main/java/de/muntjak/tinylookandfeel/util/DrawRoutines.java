package de.muntjak.tinylookandfeel.util;

import de.muntjak.tinylookandfeel.Theme;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import javax.swing.ImageIcon;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/util/DrawRoutines.class */
public class DrawRoutines {
    static final int[][] checkA = {new int[]{53, 66, 78, 99, 115, 136, 144, 156, 165, 177, 189}, new int[]{66, 78, 99, 115, 136, 144, 156, 165, 177, 189, 202}, new int[]{78, 99, 0, 0, 0, 0, 0, 0, 0, 202, 210}, new int[]{99, 115, 0, 0, 0, 0, 0, 0, 0, 210, 214}, new int[]{115, 136, 0, 0, 0, 0, 0, 0, 0, 214, 226}, new int[]{136, 144, 0, 0, 0, 0, 0, 0, 0, 226, 230}, new int[]{144, 156, 0, 0, 0, 0, 0, 0, 0, 230, 239}, new int[]{156, 165, 0, 0, 0, 0, 0, 0, 0, 239, 243}, new int[]{165, 177, 0, 0, 0, 0, 0, 0, 0, 243, 247}, new int[]{177, 189, 202, 210, 214, 226, 230, 239, 243, 247, 251}, new int[]{189, 202, 210, 214, 226, 230, 239, 243, 247, 251, 255}};
    static final int[][] radioA = {new int[]{0, 0, 78, 99, 115, 136, 144, 156, 165, 0, 0}, new int[]{0, 78, 99, 115, 136, 144, 156, 165, 177, 189, 0}, new int[]{78, 99, 115, 136, 92, 48, 92, 177, 189, 202, 210}, new int[]{99, 115, 136, 0, 0, 0, 0, 0, 202, 210, 214}, new int[]{115, 136, 92, 0, 0, 0, 0, 0, 128, 214, 226}, new int[]{136, 144, 48, 0, 0, 0, 0, 0, 64, 226, 230}, new int[]{144, 156, 92, 0, 0, 0, 0, 0, 128, 230, 239}, new int[]{156, 165, 177, 0, 0, 0, 0, 0, 230, 239, 243}, new int[]{165, 177, 189, 202, 128, 64, 128, 230, 239, 243, 247}, new int[]{0, 189, 202, 210, 214, 226, 230, 239, 243, 247, 0}, new int[]{0, 0, 210, 214, 226, 230, 239, 243, 247, 0, 0}};
    static GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    public static void drawBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.setColor(color);
        graphics.drawRect(i2, i3, i4 - 1, i5 - 1);
    }

    public static void drawEditableComboBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.setColor(color);
        graphics.drawLine(i2, i3 + 3, i2, i5 - 4);
        graphics.drawLine(i2 + 3, i3, i4 - 1, i3);
        graphics.drawLine(i2 + 3, i5 - 1, i4 - 1, i5 - 1);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 216));
        graphics.drawLine(i2 + 2, i3, i2 + 2, i3);
        graphics.drawLine((i2 + i4) - 3, i3, (i2 + i4) - 3, i3);
        graphics.drawLine(i2, i3 + 2, i2, i3 + 2);
        graphics.drawLine(i2, (i3 + i5) - 3, i2, (i3 + i5) - 3);
        graphics.drawLine(i2 + 2, (i3 + i5) - 1, i2 + 2, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 3, (i3 + i5) - 1, (i2 + i4) - 3, (i3 + i5) - 1);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 130));
        graphics.drawLine(i2 + 1, i3, i2 + 1, i3);
        graphics.drawLine(i2, i3 + 1, i2, i3 + 1);
        graphics.drawLine(i2, (i3 + i5) - 2, i2, (i3 + i5) - 2);
        graphics.drawLine(i2 + 1, (i3 + i5) - 1, i2 + 1, (i3 + i5) - 1);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 24));
        graphics.drawLine(i2, i3, i2, i3);
        graphics.drawLine(i2, (i3 + i5) - 1, i2, (i3 + i5) - 1);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 112));
        graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + 1);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 104));
        graphics.drawLine(i2 + 1, (i3 + i5) - 2, i2 + 1, (i3 + i5) - 2);
    }

    public static void drawRoundedBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.setColor(color);
        graphics.drawLine(i2 + 3, i3, (i2 + i4) - 4, i3);
        graphics.drawLine(i2 + 3, (i3 + i5) - 1, (i2 + i4) - 4, (i3 + i5) - 1);
        graphics.drawLine(i2, i3 + 3, i2, (i3 + i5) - 4);
        graphics.drawLine((i2 + i4) - 1, i3 + 3, (i2 + i4) - 1, (i3 + i5) - 4);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 216));
        graphics.drawLine(i2 + 2, i3, i2 + 2, i3);
        graphics.drawLine((i2 + i4) - 3, i3, (i2 + i4) - 3, i3);
        graphics.drawLine(i2, i3 + 2, i2, i3 + 2);
        graphics.drawLine(i2, (i3 + i5) - 3, i2, (i3 + i5) - 3);
        graphics.drawLine(i2 + 2, (i3 + i5) - 1, i2 + 2, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 3, (i3 + i5) - 1, (i2 + i4) - 3, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 1, i3 + 2, (i2 + i4) - 1, i3 + 2);
        graphics.drawLine((i2 + i4) - 1, (i3 + i5) - 3, (i2 + i4) - 1, (i3 + i5) - 3);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 130));
        graphics.drawLine(i2 + 1, i3, i2 + 1, i3);
        graphics.drawLine((i2 + i4) - 2, i3, (i2 + i4) - 2, i3);
        graphics.drawLine(i2, i3 + 1, i2, i3 + 1);
        graphics.drawLine(i2, (i3 + i5) - 2, i2, (i3 + i5) - 2);
        graphics.drawLine(i2 + 1, (i3 + i5) - 1, i2 + 1, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 2, (i3 + i5) - 1, (i2 + i4) - 2, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 1, i3 + 1, (i2 + i4) - 1, i3 + 1);
        graphics.drawLine((i2 + i4) - 1, (i3 + i5) - 2, (i2 + i4) - 1, (i3 + i5) - 2);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 24));
        graphics.drawLine(i2, i3, i2, i3);
        graphics.drawLine((i2 + i4) - 1, i3, (i2 + i4) - 1, i3);
        graphics.drawLine(i2, (i3 + i5) - 1, i2, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 1, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 112));
        graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + 1);
        graphics.drawLine((i2 + i4) - 2, i3 + 1, (i2 + i4) - 2, i3 + 1);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 104));
        graphics.drawLine(i2 + 1, (i3 + i5) - 2, i2 + 1, (i3 + i5) - 2);
        graphics.drawLine((i2 + i4) - 2, (i3 + i5) - 2, (i2 + i4) - 2, (i3 + i5) - 2);
    }

    public static void drawWindowButtonBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.setColor(color);
        graphics.drawLine(i2 + 2, i3, (i2 + i4) - 3, i3);
        graphics.drawLine(i2 + 2, (i3 + i5) - 1, (i2 + i4) - 3, (i3 + i5) - 1);
        graphics.drawLine(i2, i3 + 2, i2, (i3 + i5) - 3);
        graphics.drawLine((i2 + i4) - 1, i3 + 2, (i2 + i4) - 1, (i3 + i5) - 3);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        graphics.drawLine(i2 + 1, i3, i2 + 1, i3);
        graphics.drawLine((i2 + i4) - 2, i3, (i2 + i4) - 2, i3);
        graphics.drawLine(i2, i3 + 1, i2, i3 + 1);
        graphics.drawLine(i2, (i3 + i5) - 2, i2, (i3 + i5) - 2);
        graphics.drawLine(i2 + 1, (i3 + i5) - 1, i2 + 1, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 2, (i3 + i5) - 1, (i2 + i4) - 2, (i3 + i5) - 1);
        graphics.drawLine((i2 + i4) - 1, i3 + 1, (i2 + i4) - 1, i3 + 1);
        graphics.drawLine((i2 + i4) - 1, (i3 + i5) - 2, (i2 + i4) - 1, (i3 + i5) - 2);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 216));
        graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + 1);
        graphics.drawLine((i2 + i4) - 2, i3 + 1, (i2 + i4) - 2, i3 + 1);
        graphics.drawLine(i2 + 1, (i3 + i5) - 2, i2 + 1, (i3 + i5) - 2);
        graphics.drawLine((i2 + i4) - 2, (i3 + i5) - 2, (i2 + i4) - 2, (i3 + i5) - 2);
    }

    public static synchronized void drawProgressBarBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.setColor(color);
        graphics.drawLine(i2 + 1, i3, (i2 + i4) - 2, i3);
        graphics.drawLine(i2 + 1, (i3 + i5) - 1, (i2 + i4) - 2, (i3 + i5) - 1);
        graphics.drawLine(i2, i3 + 1, i2, (i3 + i5) - 2);
        graphics.drawLine((i2 + i4) - 1, i3 + 1, (i2 + i4) - 1, (i3 + i5) - 2);
        graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + 1);
        graphics.drawLine((i2 + i4) - 2, i3 + 1, (i2 + i4) - 2, i3 + 1);
        graphics.drawLine(i2 + 1, (i3 + i5) - 2, i2 + 1, (i3 + i5) - 2);
        graphics.drawLine((i2 + i4) - 2, (i3 + i5) - 2, (i2 + i4) - 2, (i3 + i5) - 2);
    }

    public static void drawRolloverBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.setColor(ColorRoutines.darken(color, 10));
        graphics.drawLine(i2 + 2, (i3 + i5) - 2, (i2 + i4) - 3, (i3 + i5) - 2);
        graphics.setColor(color);
        graphics.drawLine(i2 + 1, (i3 + i5) - 3, (i2 + i4) - 2, (i3 + i5) - 3);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 144));
        graphics.drawLine(i2 + 2, i3 + 2, (i2 + i4) - 3, i3 + 2);
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 64));
        graphics.drawLine(i2 + 2, i3 + 1, (i2 + i4) - 3, i3 + 1);
        if (i5 <= 6) {
            return;
        }
        int i6 = 191 / (i5 - 5);
        int i7 = 64 + i6;
        for (int i8 = i3 + 2; i8 < (i3 + i5) - 3; i8++) {
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), i7));
            graphics.drawLine(i2 + 1, i8, i2 + 1, i8);
            graphics.drawLine((i2 + i4) - 2, i8, (i2 + i4) - 2, i8);
            i7 += i6;
        }
        int i9 = 111 / (i5 - 6);
        int i10 = 144 + i9;
        for (int i11 = i3 + 3; i11 < (i3 + i5) - 3; i11++) {
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), i10));
            graphics.drawLine(i2 + 2, i11, i2 + 2, i11);
            graphics.drawLine((i2 + i4) - 3, i11, (i2 + i4) - 3, i11);
            i10 += i9;
        }
    }

    public static void drawRolloverCheckBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.translate(i2, i3);
        for (int i6 = 0; i6 < 11; i6++) {
            for (int i7 = 0; i7 < 11; i7++) {
                if (checkA[i6][i7] > 0) {
                    graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), checkA[i6][i7]));
                    graphics.drawLine(i7 + 1, i6 + 1, i7 + 1, i6 + 1);
                }
            }
        }
        graphics.translate(-i2, -i3);
    }

    public static void drawSelectedXpTabBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5, int i6) {
        Color adjustedColor = ColorRoutines.getAdjustedColor(Theme.tabRolloverColor.getColor(), 20, -30);
        graphics.setColor(adjustedColor);
        Color average = ColorRoutines.getAverage(Theme.backColor.getColor(), adjustedColor);
        switch (i6) {
            case 1:
            default:
                int i7 = i4 - 1;
                graphics.drawLine(i2 + 2, i3, (i2 + i7) - 3, i3);
                graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + 1);
                graphics.drawLine((i2 + i7) - 2, i3 + 1, (i2 + i7) - 2, i3 + 1);
                graphics.drawLine(i2, i3 + 2, i2, i3 + 2);
                graphics.drawLine((i2 + i7) - 1, i3 + 2, (i2 + i7) - 1, i3 + 2);
                graphics.setColor(Theme.tabRolloverColor.getColor());
                graphics.drawLine(i2 + 2, i3 + 1, (i2 + i7) - 3, i3 + 1);
                graphics.drawLine(i2 + 1, i3 + 2, (i2 + i7) - 2, i3 + 2);
                graphics.setColor(average);
                graphics.drawLine(i2 + 1, i3, i2 + 1, i3);
                graphics.drawLine(i2, i3 + 1, i2, i3 + 1);
                graphics.drawLine((i2 + i7) - 2, i3, (i2 + i7) - 2, i3);
                graphics.drawLine((i2 + i7) - 1, i3 + 1, (i2 + i7) - 1, i3 + 1);
                graphics.setColor(color);
                graphics.drawLine(i2, i3 + 3, i2, (i3 + i5) - 1);
                graphics.drawLine((i2 + i7) - 1, i3 + 3, (i2 + i7) - 1, (i3 + i5) - 1);
                break;
            case 2:
                int i8 = i5 - 1;
                graphics.drawLine(i2, i3 + 2, i2, (i3 + i8) - 3);
                graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + 1);
                graphics.drawLine(i2 + 1, (i3 + i8) - 2, i2 + 1, (i3 + i8) - 2);
                graphics.drawLine(i2 + 2, i3, i2 + 2, i3);
                graphics.drawLine(i2 + 2, (i3 + i8) - 1, i2 + 2, (i3 + i8) - 1);
                graphics.setColor(Theme.tabRolloverColor.getColor());
                graphics.drawLine(i2 + 1, i3 + 2, i2 + 1, (i3 + i8) - 3);
                graphics.drawLine(i2 + 2, i3 + 1, i2 + 2, (i3 + i8) - 2);
                graphics.setColor(average);
                graphics.drawLine(i2, i3 + 1, i2, i3 + 1);
                graphics.drawLine(i2 + 1, i3, i2 + 1, i3);
                graphics.drawLine(i2, (i3 + i8) - 2, i2, (i3 + i8) - 2);
                graphics.drawLine(i2 + 1, (i3 + i8) - 1, i2 + 1, (i3 + i8) - 1);
                graphics.setColor(color);
                graphics.drawLine(i2 + 3, i3, (i2 + i4) - 1, i3);
                graphics.drawLine(i2 + 3, (i3 + i8) - 1, (i2 + i4) - 1, (i3 + i8) - 1);
                break;
            case 3:
                int i9 = i4 - 1;
                int i10 = i3 - 2;
                graphics.drawLine(i2 + 2, (i10 + i5) - 1, (i2 + i9) - 3, (i10 + i5) - 1);
                graphics.drawLine(i2 + 1, (i10 + i5) - 2, i2 + 1, (i10 + i5) - 2);
                graphics.drawLine((i2 + i9) - 2, (i10 + i5) - 2, (i2 + i9) - 2, (i10 + i5) - 2);
                graphics.drawLine(i2, (i10 + i5) - 3, i2, (i10 + i5) - 3);
                graphics.drawLine((i2 + i9) - 1, (i10 + i5) - 3, (i2 + i9) - 1, (i10 + i5) - 3);
                graphics.setColor(Theme.tabRolloverColor.getColor());
                graphics.drawLine(i2 + 2, (i10 + i5) - 2, (i2 + i9) - 3, (i10 + i5) - 2);
                graphics.drawLine(i2 + 1, (i10 + i5) - 3, (i2 + i9) - 2, (i10 + i5) - 3);
                graphics.setColor(color);
                graphics.drawLine(i2, i10, i2, (i10 + i5) - 4);
                graphics.drawLine((i2 + i9) - 1, i10, (i2 + i9) - 1, (i10 + i5) - 4);
                graphics.setColor(average);
                graphics.drawLine(i2 + 1, (i10 + i5) - 1, i2 + 1, (i10 + i5) - 1);
                graphics.drawLine(i2, (i10 + i5) - 2, i2, (i10 + i5) - 2);
                graphics.drawLine((i2 + i9) - 2, (i10 + i5) - 1, (i2 + i9) - 2, (i10 + i5) - 1);
                graphics.drawLine((i2 + i9) - 1, (i10 + i5) - 2, (i2 + i9) - 1, (i10 + i5) - 2);
                break;
            case 4:
                int i11 = i5 - 1;
                int i12 = i2 - 2;
                graphics.drawLine((i12 + i4) - 1, i3 + 2, (i12 + i4) - 1, (i3 + i11) - 3);
                graphics.drawLine((i12 + i4) - 2, i3 + 1, (i12 + i4) - 2, i3 + 1);
                graphics.drawLine((i12 + i4) - 2, (i3 + i11) - 2, (i12 + i4) - 2, (i3 + i11) - 2);
                graphics.drawLine((i12 + i4) - 3, i3, (i12 + i4) - 3, i3);
                graphics.drawLine((i12 + i4) - 3, (i3 + i11) - 1, (i12 + i4) - 3, (i3 + i11) - 1);
                graphics.setColor(Theme.tabRolloverColor.getColor());
                graphics.drawLine((i12 + i4) - 2, i3 + 2, (i12 + i4) - 2, (i3 + i11) - 3);
                graphics.drawLine((i12 + i4) - 3, i3 + 1, (i12 + i4) - 3, (i3 + i11) - 2);
                graphics.setColor(average);
                graphics.drawLine((i12 + i4) - 1, i3 + 1, (i12 + i4) - 1, i3 + 1);
                graphics.drawLine((i12 + i4) - 2, i3, (i12 + i4) - 2, i3);
                graphics.drawLine((i12 + i4) - 1, (i3 + i11) - 2, (i12 + i4) - 1, (i3 + i11) - 2);
                graphics.drawLine((i12 + i4) - 2, (i3 + i11) - 1, (i12 + i4) - 2, (i3 + i11) - 1);
                graphics.setColor(color);
                graphics.drawLine(i12, i3, (i12 + i4) - 4, i3);
                graphics.drawLine(i12, (i3 + i11) - 1, (i12 + i4) - 4, (i3 + i11) - 1);
                break;
        }
    }

    public static void drawXpTabBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5, int i6) {
        graphics.setColor(color);
        switch (i6) {
            case 1:
            default:
                int i7 = i4 - 1;
                graphics.drawLine(i2 + 2, i3, (i2 + i7) - 3, i3);
                graphics.drawLine(i2, i3 + 2, i2, (i3 + i5) - 1);
                graphics.drawLine((i2 + i7) - 1, i3 + 2, (i2 + i7) - 1, (i3 + i5) - 1);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 56));
                graphics.drawLine(i2, i3, i2, i3);
                graphics.drawLine((i2 + i7) - 1, i3, (i2 + i7) - 1, i3);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 183));
                graphics.drawLine(i2 + 1, i3, i2 + 1, i3);
                graphics.drawLine((i2 + i7) - 2, i3, (i2 + i7) - 2, i3);
                graphics.drawLine(i2, i3 + 1, i2, i3 + 1);
                graphics.drawLine((i2 + i7) - 1, i3 + 1, (i2 + i7) - 1, i3 + 1);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 76));
                graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + 1);
                graphics.drawLine((i2 + i7) - 2, i3 + 1, (i2 + i7) - 2, i3 + 1);
                break;
            case 2:
                int i8 = i5 - 1;
                graphics.drawLine(i2 + 2, i3, (i2 + i4) - 1, i3);
                graphics.drawLine(i2 + 2, (i3 + i8) - 1, (i2 + i4) - 1, (i3 + i8) - 1);
                graphics.drawLine(i2, i3 + 2, i2, (i3 + i8) - 3);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 56));
                graphics.drawLine(i2, i3, i2, i3);
                graphics.drawLine(i2, (i3 + i8) - 1, i2, (i3 + i8) - 1);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 183));
                graphics.drawLine(i2 + 1, i3, i2 + 1, i3);
                graphics.drawLine(i2, i3 + 1, i2, i3 + 1);
                graphics.drawLine(i2, (i3 + i8) - 2, i2, (i3 + i8) - 2);
                graphics.drawLine(i2 + 1, (i3 + i8) - 1, i2 + 1, (i3 + i8) - 1);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 76));
                graphics.drawLine(i2 + 1, i3 + 1, i2 + 1, i3 + 1);
                graphics.drawLine(i2 + 1, (i3 + i8) - 2, i2 + 1, (i3 + i8) - 2);
                break;
            case 3:
                int i9 = i4 - 1;
                int i10 = i3 - 2;
                graphics.drawLine(i2 + 2, (i10 + i5) - 1, (i2 + i9) - 3, (i10 + i5) - 1);
                graphics.drawLine(i2, i10, i2, (i10 + i5) - 3);
                graphics.drawLine((i2 + i9) - 1, i10, (i2 + i9) - 1, (i10 + i5) - 3);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 56));
                graphics.drawLine(i2, (i10 + i5) - 1, i2, (i10 + i5) - 1);
                graphics.drawLine((i2 + i9) - 1, (i10 + i5) - 1, (i2 + i9) - 1, (i10 + i5) - 1);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 183));
                graphics.drawLine(i2, (i10 + i5) - 2, i2, (i10 + i5) - 2);
                graphics.drawLine(i2 + 1, (i10 + i5) - 1, i2 + 1, (i10 + i5) - 1);
                graphics.drawLine((i2 + i9) - 2, (i10 + i5) - 1, (i2 + i9) - 2, (i10 + i5) - 1);
                graphics.drawLine((i2 + i9) - 1, (i10 + i5) - 2, (i2 + i9) - 1, (i10 + i5) - 2);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 76));
                graphics.drawLine(i2 + 1, (i10 + i5) - 2, i2 + 1, (i10 + i5) - 2);
                graphics.drawLine((i2 + i9) - 2, (i10 + i5) - 2, (i2 + i9) - 2, (i10 + i5) - 2);
                break;
            case 4:
                int i11 = i5 - 1;
                int i12 = i2 - 2;
                graphics.drawLine(i12, i3, (i12 + i4) - 3, i3);
                graphics.drawLine(i12, (i3 + i11) - 1, (i12 + i4) - 3, (i3 + i11) - 1);
                graphics.drawLine((i12 + i4) - 1, i3 + 2, (i12 + i4) - 1, (i3 + i11) - 3);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 56));
                graphics.drawLine((i12 + i4) - 1, i3, (i12 + i4) - 1, i3);
                graphics.drawLine((i12 + i4) - 1, (i3 + i11) - 1, (i12 + i4) - 1, (i3 + i11) - 1);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 183));
                graphics.drawLine((i12 + i4) - 2, i3, (i12 + i4) - 2, i3);
                graphics.drawLine((i12 + i4) - 2, (i3 + i11) - 1, (i12 + i4) - 2, (i3 + i11) - 1);
                graphics.drawLine((i12 + i4) - 1, i3 + 1, (i12 + i4) - 1, i3 + 1);
                graphics.drawLine((i12 + i4) - 1, (i3 + i11) - 2, (i12 + i4) - 1, (i3 + i11) - 2);
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 76));
                graphics.drawLine((i12 + i4) - 2, i3 + 1, (i12 + i4) - 2, i3 + 1);
                graphics.drawLine((i12 + i4) - 2, (i3 + i11) - 2, (i12 + i4) - 2, (i3 + i11) - 2);
                break;
        }
    }

    public static void drawXpRadioRolloverBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.translate(i2, i3);
        for (int i6 = 0; i6 < 11; i6++) {
            for (int i7 = 0; i7 < 11; i7++) {
                if (radioA[i6][i7] > 0) {
                    graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), radioA[i6][i7]));
                    graphics.drawLine(i7 + 1, i6 + 1, i7 + 1, i6 + 1);
                }
            }
        }
        graphics.translate(-i2, -i3);
    }

    public static void drawXpRadioBorder(Graphics graphics, Color color, int i2, int i3, int i4, int i5) {
        graphics.setColor(color);
        graphics.drawLine(i2 + 6, i3, i2 + 6, i3);
        graphics.drawLine(i2 + 3, i3 + 1, i2 + 3, i3 + 1);
        graphics.drawLine(i2 + 9, i3 + 1, i2 + 9, i3 + 1);
        graphics.drawLine(i2 + 1, i3 + 3, i2 + 1, i3 + 3);
        graphics.drawLine(i2 + 11, i3 + 3, i2 + 11, i3 + 3);
        graphics.drawLine(i2, i3 + 6, i2, i3 + 6);
        graphics.drawLine(i2 + 12, i3 + 6, i2 + 12, i3 + 6);
        graphics.drawLine(i2 + 1, i3 + 9, i2 + 1, i3 + 9);
        graphics.drawLine(i2 + 11, i3 + 9, i2 + 11, i3 + 9);
        graphics.drawLine(i2 + 3, i3 + 11, i2 + 3, i3 + 11);
        graphics.drawLine(i2 + 9, i3 + 11, i2 + 9, i3 + 11);
        graphics.drawLine(i2 + 6, i3 + 12, i2 + 6, i3 + 12);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 168));
        graphics.drawLine(i2 + 5, i3, i2 + 5, i3);
        graphics.drawLine(i2 + 7, i3, i2 + 7, i3);
        graphics.drawLine(i2 + 4, i3 + 1, i2 + 4, i3 + 1);
        graphics.drawLine(i2 + 8, i3 + 1, i2 + 8, i3 + 1);
        graphics.drawLine(i2 + 2, i3 + 2, i2 + 2, i3 + 2);
        graphics.drawLine(i2 + 10, i3 + 2, i2 + 10, i3 + 2);
        graphics.drawLine(i2 + 1, i3 + 4, i2 + 1, i3 + 4);
        graphics.drawLine(i2 + 11, i3 + 4, i2 + 11, i3 + 4);
        graphics.drawLine(i2, i3 + 5, i2, i3 + 5);
        graphics.drawLine(i2 + 12, i3 + 5, i2 + 12, i3 + 5);
        graphics.drawLine(i2, i3 + 7, i2, i3 + 7);
        graphics.drawLine(i2 + 12, i3 + 7, i2 + 12, i3 + 7);
        graphics.drawLine(i2 + 1, i3 + 8, i2 + 1, i3 + 8);
        graphics.drawLine(i2 + 11, i3 + 8, i2 + 11, i3 + 8);
        graphics.drawLine(i2 + 2, i3 + 10, i2 + 2, i3 + 10);
        graphics.drawLine(i2 + 10, i3 + 10, i2 + 10, i3 + 10);
        graphics.drawLine(i2 + 4, i3 + 11, i2 + 4, i3 + 11);
        graphics.drawLine(i2 + 8, i3 + 11, i2 + 8, i3 + 11);
        graphics.drawLine(i2 + 5, i3 + 12, i2 + 5, i3 + 12);
        graphics.drawLine(i2 + 7, i3 + 12, i2 + 7, i3 + 12);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 64));
        graphics.drawLine(i2 + 4, i3, i2 + 4, i3);
        graphics.drawLine(i2 + 8, i3, i2 + 8, i3);
        graphics.drawLine(i2 + 2, i3 + 1, i2 + 2, i3 + 1);
        graphics.drawLine(i2 + 2, i3 + 3, i2 + 2, i3 + 3);
        graphics.drawLine(i2 + 10, i3 + 1, i2 + 10, i3 + 1);
        graphics.drawLine(i2 + 10, i3 + 3, i2 + 10, i3 + 3);
        graphics.drawLine(i2 + 5, i3 + 1, i2 + 5, i3 + 1);
        graphics.drawLine(i2 + 7, i3 + 1, i2 + 7, i3 + 1);
        graphics.drawLine(i2 + 1, i3 + 2, i2 + 1, i3 + 2);
        graphics.drawLine(i2 + 1, i3 + 5, i2 + 1, i3 + 5);
        graphics.drawLine(i2 + 1, i3 + 7, i2 + 1, i3 + 7);
        graphics.drawLine(i2 + 11, i3 + 2, i2 + 11, i3 + 2);
        graphics.drawLine(i2 + 3, i3 + 2, i2 + 3, i3 + 2);
        graphics.drawLine(i2 + 9, i3 + 2, i2 + 9, i3 + 2);
        graphics.drawLine(i2, i3 + 4, i2, i3 + 4);
        graphics.drawLine(i2 + 12, i3 + 4, i2 + 12, i3 + 4);
        graphics.drawLine(i2, i3 + 8, i2, i3 + 8);
        graphics.drawLine(i2 + 12, i3 + 8, i2 + 12, i3 + 8);
        graphics.drawLine(i2 + 2, i3 + 9, i2 + 2, i3 + 9);
        graphics.drawLine(i2 + 10, i3 + 9, i2 + 10, i3 + 9);
        graphics.drawLine(i2 + 1, i3 + 10, i2 + 1, i3 + 10);
        graphics.drawLine(i2 + 11, i3 + 5, i2 + 11, i3 + 5);
        graphics.drawLine(i2 + 11, i3 + 7, i2 + 11, i3 + 7);
        graphics.drawLine(i2 + 11, i3 + 10, i2 + 11, i3 + 10);
        graphics.drawLine(i2 + 3, i3 + 10, i2 + 3, i3 + 10);
        graphics.drawLine(i2 + 9, i3 + 10, i2 + 9, i3 + 10);
        graphics.drawLine(i2 + 2, i3 + 11, i2 + 2, i3 + 11);
        graphics.drawLine(i2 + 10, i3 + 11, i2 + 10, i3 + 11);
        graphics.drawLine(i2 + 5, i3 + 11, i2 + 5, i3 + 11);
        graphics.drawLine(i2 + 7, i3 + 11, i2 + 7, i3 + 11);
        graphics.drawLine(i2 + 4, i3 + 12, i2 + 4, i3 + 12);
        graphics.drawLine(i2 + 8, i3 + 12, i2 + 8, i3 + 12);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 16));
        graphics.drawLine(i2 + 3, i3, i2 + 3, i3);
        graphics.drawLine(i2 + 9, i3, i2 + 9, i3);
        graphics.drawLine(i2, i3 + 3, i2, i3 + 3);
        graphics.drawLine(i2 + 12, i3 + 3, i2 + 12, i3 + 3);
        graphics.drawLine(i2, i3 + 9, i2, i3 + 9);
        graphics.drawLine(i2 + 12, i3 + 9, i2 + 12, i3 + 9);
        graphics.drawLine(i2 + 3, i3 + 12, i2 + 3, i3 + 12);
        graphics.drawLine(i2 + 9, i3 + 12, i2 + 9, i3 + 12);
    }

    public static ImageIcon colorizeIcon(Image image, HSBReference hSBReference) {
        ColorRoutines colorRoutines = new ColorRoutines(hSBReference);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bufferedImageCreateCompatibleImage = graphicsConfiguration.createCompatibleImage(width, height, 3);
        int[] iArr = new int[width * height];
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, width, height, iArr, 0, width);
        try {
            pixelGrabber.grabPixels();
        } catch (InterruptedException e2) {
            System.err.println("PixelGrabber interrupted waiting for pixels");
        }
        if ((pixelGrabber.getStatus() & 128) != 0) {
            System.err.println("Image fetch aborted or errored.");
        } else {
            for (int i2 = 0; i2 < height; i2++) {
                for (int i3 = 0; i3 < width; i3++) {
                    bufferedImageCreateCompatibleImage.setRGB(i3, i2, colorizePixel(iArr[(i2 * width) + i3], colorRoutines));
                }
            }
        }
        return new ImageIcon(bufferedImageCreateCompatibleImage);
    }

    private static int colorizePixel(int i2, ColorRoutines colorRoutines) {
        int i3 = (i2 >> 24) & 255;
        return i3 == 0 ? i2 : colorRoutines.colorize((i2 >> 16) & 255, (i2 >> 8) & 255, i2 & 255, i3);
    }
}
