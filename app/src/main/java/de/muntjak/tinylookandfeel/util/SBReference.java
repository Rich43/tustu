package de.muntjak.tinylookandfeel.util;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.controlpanel.ControlPanel;
import de.muntjak.tinylookandfeel.controlpanel.SBControl;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.plaf.ColorUIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/util/SBReference.class */
public class SBReference {
    public static final int ABS_COLOR = 1;
    public static final int MAIN_COLOR = 2;
    public static final int BACK_COLOR = 3;
    public static final int DIS_COLOR = 4;
    public static final int FRAME_COLOR = 5;
    public static final int SUB1_COLOR = 6;
    public static final int SUB2_COLOR = 7;
    public static final int SUB3_COLOR = 8;
    public static final int SUB4_COLOR = 9;
    public static final int SUB5_COLOR = 10;
    public static final int SUB6_COLOR = 11;
    public static final int SUB7_COLOR = 12;
    public static final int SUB8_COLOR = 13;
    private static final Vector instances = new Vector();
    protected ColorUIResource color;
    protected ColorUIResource referenceColor;
    protected int sat;
    protected int bri;
    protected int ref;
    protected boolean locked;
    protected ColorIcon icon;
    protected static ColorIcon absoluteIcon;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/util/SBReference$ColorIcon.class */
    class ColorIcon implements Icon {
        private boolean paintGradients;
        private final SBReference this$0;

        ColorIcon(SBReference sBReference, boolean z2) {
            this.this$0 = sBReference;
            this.paintGradients = z2;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 16;
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 16;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            Color color = graphics.getColor();
            graphics.setColor(Color.GRAY);
            graphics.drawRect(i2, i3, getIconWidth(), getIconHeight());
            if (this.paintGradients) {
                float f2 = 0.0f;
                for (int i4 = 0; i4 < 15; i4++) {
                    graphics.setColor(Color.getHSBColor(f2, 0.5f, 1.0f));
                    graphics.drawLine(i2 + 1 + i4, i3 + 1, i2 + 1 + i4, (i3 + getIconHeight()) - 1);
                    f2 = (float) (f2 + 0.0625d);
                }
            } else {
                graphics.setColor(this.this$0.color);
                graphics.fillRect(i2 + 1, i3 + 1, getIconWidth() - 1, getIconHeight() - 1);
            }
            if ((component instanceof AbstractButton) && ((AbstractButton) component).isSelected()) {
                graphics.setColor(Color.WHITE);
                drawArrow(graphics, i2 + 1, i3 + 1);
                graphics.setColor(Color.BLACK);
                drawArrow(graphics, i2, i3);
            }
            graphics.setColor(color);
        }

        private void drawArrow(Graphics graphics, int i2, int i3) {
            graphics.drawLine(i2 + 3, i3 + 5, i2 + 3, i3 + 7);
            graphics.drawLine(i2 + 4, i3 + 6, i2 + 4, i3 + 8);
            graphics.drawLine(i2 + 5, i3 + 7, i2 + 5, i3 + 9);
            graphics.drawLine(i2 + 6, i3 + 6, i2 + 6, i3 + 8);
            graphics.drawLine(i2 + 7, i3 + 5, i2 + 7, i3 + 7);
            graphics.drawLine(i2 + 8, i3 + 4, i2 + 8, i3 + 6);
            graphics.drawLine(i2 + 9, i3 + 3, i2 + 9, i3 + 5);
        }
    }

    public SBReference() {
        this.sat = 0;
        this.bri = 0;
        this.color = new ColorUIResource(Color.BLACK);
        this.ref = 1;
        instances.add(this);
    }

    public SBReference(boolean z2) {
        this.sat = 0;
        this.bri = 0;
        this.color = new ColorUIResource(Color.BLACK);
        this.ref = 1;
    }

    public SBReference(Color color, int i2, int i3, int i4) {
        this.sat = 0;
        this.bri = 0;
        this.color = new ColorUIResource(color);
        this.sat = i2;
        this.bri = i3;
        this.ref = i4;
        instances.add(this);
    }

    public SBReference(Color color, int i2, int i3, int i4, boolean z2) {
        this.sat = 0;
        this.bri = 0;
        this.color = new ColorUIResource(color);
        this.sat = i2;
        this.bri = i3;
        this.ref = i4;
        this.locked = z2;
    }

    public SBReference(SBReference sBReference) {
        this.sat = 0;
        this.bri = 0;
        this.color = new ColorUIResource(sBReference.color);
        this.sat = sBReference.sat;
        this.bri = sBReference.bri;
        this.ref = sBReference.ref;
        this.locked = sBReference.locked;
    }

    public SBReference copy() {
        SBReference sBReference = new SBReference(this);
        if (!isAbsoluteColor()) {
            sBReference.referenceColor = getReferenceColor();
        }
        return sBReference;
    }

    public static int getNumReferences(int i2) {
        int i3 = 0;
        Iterator it = instances.iterator();
        while (it.hasNext()) {
            if (i2 == ((SBReference) it.next()).ref) {
                i3++;
            }
        }
        return i3;
    }

    public static void printReferences(int i2) {
        Iterator it = instances.iterator();
        while (it.hasNext()) {
            SBReference sBReference = (SBReference) it.next();
            if (i2 == sBReference.ref) {
                System.out.println(sBReference);
            }
        }
    }

    public void update(Color color, int i2, int i3, int i4) {
        this.color = new ColorUIResource(color);
        this.sat = i2;
        this.bri = i3;
        this.ref = i4;
    }

    public void update(SBReference sBReference) {
        if (!sBReference.isAbsoluteColor() && sBReference.referenceColor != null) {
            update(sBReference, sBReference.referenceColor);
            return;
        }
        this.color = new ColorUIResource(sBReference.color);
        this.sat = sBReference.sat;
        this.bri = sBReference.bri;
        this.ref = sBReference.ref;
    }

    public void update(SBReference sBReference, Vector vector) {
        if (!sBReference.isAbsoluteColor()) {
            update(sBReference, (ColorUIResource) vector.get(sBReference.ref - 2));
            return;
        }
        this.color = new ColorUIResource(sBReference.color);
        this.sat = sBReference.sat;
        this.bri = sBReference.bri;
        this.ref = sBReference.ref;
    }

    private void update(SBReference sBReference, ColorUIResource colorUIResource) {
        if (sBReference.getReferenceColor().equals(colorUIResource)) {
            this.color = new ColorUIResource(sBReference.color);
            this.sat = sBReference.sat;
            this.bri = sBReference.bri;
            this.ref = sBReference.ref;
            return;
        }
        int refForColor = getRefForColor(colorUIResource);
        if (refForColor != -1) {
            this.ref = refForColor;
            this.sat = sBReference.sat;
            this.bri = sBReference.bri;
            return;
        }
        int emptyReferenceColor = getEmptyReferenceColor();
        if (emptyReferenceColor == -1) {
            this.color = new ColorUIResource(sBReference.getColor());
            this.sat = 0;
            this.bri = 0;
            this.ref = 1;
            return;
        }
        this.ref = emptyReferenceColor;
        SBControl sBControlFromRef = ControlPanel.instance.getSBControlFromRef(this.ref);
        sBControlFromRef.getSBReference().setReference(1);
        sBControlFromRef.getSBReference().setSaturation(0);
        sBControlFromRef.getSBReference().setBrightness(0);
        sBControlFromRef.getSBReference().setColor(colorUIResource);
        sBControlFromRef.update();
        this.sat = sBReference.sat;
        this.bri = sBReference.bri;
    }

    private int getEmptyReferenceColor() {
        for (int i2 = 6; i2 <= 13; i2++) {
            if (getNumReferences(i2) == 0) {
                return i2;
            }
        }
        return -1;
    }

    private int getRefForColor(ColorUIResource colorUIResource) {
        if (Theme.mainColor.getColor().equals(colorUIResource)) {
            return 2;
        }
        if (Theme.backColor.getColor().equals(colorUIResource)) {
            return 3;
        }
        if (Theme.disColor.getColor().equals(colorUIResource)) {
            return 4;
        }
        if (Theme.frameColor.getColor().equals(colorUIResource)) {
            return 5;
        }
        if (Theme.sub1Color.getColor().equals(colorUIResource)) {
            return 6;
        }
        if (Theme.sub2Color.getColor().equals(colorUIResource)) {
            return 7;
        }
        if (Theme.sub3Color.getColor().equals(colorUIResource)) {
            return 8;
        }
        if (Theme.sub4Color.getColor().equals(colorUIResource)) {
            return 9;
        }
        if (Theme.sub5Color.getColor().equals(colorUIResource)) {
            return 10;
        }
        if (Theme.sub6Color.getColor().equals(colorUIResource)) {
            return 11;
        }
        if (Theme.sub7Color.getColor().equals(colorUIResource)) {
            return 12;
        }
        return Theme.sub8Color.getColor().equals(colorUIResource) ? 13 : -1;
    }

    public void update(Color color) {
        this.color = new ColorUIResource(color);
        this.sat = 0;
        this.bri = 0;
        this.ref = 1;
    }

    public void reset() {
        this.sat = 0;
        this.bri = 0;
    }

    public ColorUIResource getColor() {
        return this.color;
    }

    public int getSaturation() {
        return this.sat;
    }

    public int getBrightness() {
        return this.bri;
    }

    public int getReference() {
        return this.ref;
    }

    public ColorUIResource getReferenceColor() {
        return getReferencedColor(this.ref);
    }

    public static ColorUIResource getReferencedColor(int i2) {
        switch (i2) {
            case 2:
                return Theme.mainColor.getColor();
            case 3:
                return Theme.backColor.getColor();
            case 4:
                return Theme.disColor.getColor();
            case 5:
                return Theme.frameColor.getColor();
            case 6:
                return Theme.sub1Color.getColor();
            case 7:
                return Theme.sub2Color.getColor();
            case 8:
                return Theme.sub3Color.getColor();
            case 9:
                return Theme.sub4Color.getColor();
            case 10:
                return Theme.sub5Color.getColor();
            case 11:
                return Theme.sub6Color.getColor();
            case 12:
                return Theme.sub7Color.getColor();
            case 13:
                return Theme.sub8Color.getColor();
            default:
                return null;
        }
    }

    public String getReferenceString() {
        switch (this.ref) {
            case 2:
                return "Main Color";
            case 3:
                return "Back Color";
            case 4:
                return "Disabled Color";
            case 5:
                return "Frame Color";
            case 6:
                return "Sub1 Color";
            case 7:
                return "Sub2 Color";
            case 8:
                return "Sub3 Color";
            case 9:
                return "Sub4 Color";
            case 10:
                return "Sub5 Color";
            case 11:
                return "Sub6 Color";
            case 12:
                return "Sub7 Color";
            case 13:
                return "Sub8 Color";
            default:
                return "";
        }
    }

    public void setColor(Color color) {
        this.color = new ColorUIResource(color);
    }

    public void setSaturation(int i2) {
        this.sat = i2;
    }

    public void setBrightness(int i2) {
        this.bri = i2;
    }

    public void setReference(int i2) {
        this.ref = i2;
    }

    public void setColor(int i2, int i3) {
        if (isAbsoluteColor()) {
            return;
        }
        this.sat = i2;
        this.bri = i3;
        updateColor();
    }

    private void updateColor() {
        switch (this.ref) {
            case 2:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.mainColor.getColor(), this.sat, this.bri));
                break;
            case 3:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.backColor.getColor(), this.sat, this.bri));
                break;
            case 4:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.disColor.getColor(), this.sat, this.bri));
                break;
            case 5:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.frameColor.getColor(), this.sat, this.bri));
                break;
            case 6:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.sub1Color.getColor(), this.sat, this.bri));
                break;
            case 7:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.sub2Color.getColor(), this.sat, this.bri));
                break;
            case 8:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.sub3Color.getColor(), this.sat, this.bri));
                break;
            case 9:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.sub4Color.getColor(), this.sat, this.bri));
                break;
            case 10:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.sub5Color.getColor(), this.sat, this.bri));
                break;
            case 11:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.sub6Color.getColor(), this.sat, this.bri));
                break;
            case 12:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.sub7Color.getColor(), this.sat, this.bri));
                break;
            case 13:
                this.color = new ColorUIResource(ColorRoutines.getAdjustedColor(Theme.sub8Color.getColor(), this.sat, this.bri));
                break;
        }
    }

    public ColorUIResource update() {
        if (isAbsoluteColor()) {
            return this.color;
        }
        updateColor();
        return this.color;
    }

    public boolean isAbsoluteColor() {
        return this.ref == 1;
    }

    public boolean isReferenceColor() {
        return this.locked || equals(Theme.sub1Color) || equals(Theme.sub2Color) || equals(Theme.sub3Color) || equals(Theme.sub4Color) || equals(Theme.sub5Color) || equals(Theme.sub6Color) || equals(Theme.sub7Color) || equals(Theme.sub8Color);
    }

    public void setLocked(boolean z2) {
        this.locked = z2;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public String toString() {
        return new StringBuffer().append("SBReference[bri=").append(this.bri).append(",sat=").append(this.sat).append(",ref=").append(this.ref).append(",c=(").append(this.color.getRed()).append(",").append(this.color.getGreen()).append(",").append(this.color.getBlue()).append(")]").toString();
    }

    public Icon getIcon() {
        if (this.icon == null) {
            this.icon = new ColorIcon(this, false);
        }
        return this.icon;
    }

    public Icon getAbsoluteIcon() {
        if (absoluteIcon == null) {
            absoluteIcon = new ColorIcon(this, true);
        }
        return absoluteIcon;
    }

    public void save(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.color.getRGB());
        dataOutputStream.writeInt(this.sat);
        dataOutputStream.writeInt(this.bri);
        dataOutputStream.writeInt(this.ref);
        dataOutputStream.writeBoolean(this.locked);
    }

    public void load(DataInputStream dataInputStream) throws IOException {
        try {
            if (Theme.fileID >= 12852) {
                this.color = new ColorUIResource(dataInputStream.readInt());
            } else {
                this.color = new ColorUIResource(dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt());
            }
            this.sat = dataInputStream.readInt();
            this.bri = dataInputStream.readInt();
            this.ref = dataInputStream.readInt();
            this.locked = dataInputStream.readBoolean();
        } catch (Exception e2) {
            throw new IOException(new StringBuffer().append("SBReference.load() : ").append((Object) e2).toString());
        }
    }

    public static void loadDummyData(DataInputStream dataInputStream) throws IOException {
        try {
            if (Theme.fileID >= 12852) {
                dataInputStream.readInt();
            } else {
                dataInputStream.readInt();
                dataInputStream.readInt();
                dataInputStream.readInt();
            }
            dataInputStream.readInt();
            dataInputStream.readInt();
            dataInputStream.readInt();
            dataInputStream.readBoolean();
        } catch (Exception e2) {
            throw new IOException(new StringBuffer().append("SBReference.loadDummyData() : ").append(e2.getMessage()).toString());
        }
    }
}
