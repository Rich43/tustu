package de.muntjak.tinylookandfeel.util;

import de.muntjak.tinylookandfeel.Theme;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.swing.plaf.FontUIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/util/ColoredFont.class */
public class ColoredFont {
    private SBReference sbReference;
    private FontUIResource font;
    private boolean isPlainFont;
    private boolean isBoldFont;

    public ColoredFont(ColoredFont coloredFont) {
        this.font = new FontUIResource(coloredFont.font);
        if (coloredFont.sbReference != null) {
            this.sbReference = new SBReference(coloredFont.sbReference);
        }
        this.isPlainFont = coloredFont.isPlainFont;
        this.isBoldFont = coloredFont.isBoldFont;
    }

    public ColoredFont(String str, int i2, int i3) {
        this.font = new FontUIResource(Theme.getPlatformFont(str), i2, i3);
    }

    public ColoredFont() {
        this.font = new FontUIResource(Theme.getPlatformFont("Tahoma"), 0, 11);
        this.isPlainFont = true;
    }

    public ColoredFont(SBReference sBReference) {
        this("Tahoma", 0, 11, sBReference);
        this.isPlainFont = true;
    }

    private ColoredFont(String str, int i2, int i3, SBReference sBReference) {
        this.font = new FontUIResource(Theme.getPlatformFont(str), i2, i3);
        this.sbReference = sBReference;
    }

    public void update(String str, int i2, int i3) {
        this.font = new FontUIResource(Theme.getPlatformFont(str), i2, i3);
    }

    public void update(SBReference sBReference) {
        this.font = new FontUIResource(Theme.getPlatformFont("Tahoma"), 0, 11);
        this.sbReference = sBReference;
        this.isPlainFont = true;
    }

    public void update(ColoredFont coloredFont) {
        this.font = new FontUIResource(coloredFont.font);
        this.isPlainFont = coloredFont.isPlainFont;
        this.isBoldFont = coloredFont.isBoldFont;
        if (coloredFont.sbReference != null) {
            this.sbReference.setBrightness(coloredFont.sbReference.getBrightness());
            this.sbReference.setLocked(coloredFont.sbReference.isLocked());
            this.sbReference.setReference(coloredFont.sbReference.getReference());
            this.sbReference.setSaturation(coloredFont.sbReference.getSaturation());
            this.sbReference.setColor(coloredFont.sbReference.getColor());
        }
    }

    public void update(ColoredFont coloredFont, Vector vector) {
        this.font = new FontUIResource(coloredFont.font);
        this.isPlainFont = coloredFont.isPlainFont;
        this.isBoldFont = coloredFont.isBoldFont;
        if (coloredFont.sbReference != null) {
            this.sbReference.update(coloredFont.sbReference, vector);
        }
    }

    public void setPlainFont(boolean z2) {
        this.isPlainFont = z2;
        if (z2) {
            this.isBoldFont = false;
        }
    }

    public void setBoldFont(boolean z2) {
        this.isBoldFont = z2;
        if (z2) {
            this.isPlainFont = false;
        }
    }

    public boolean isPlainFont() {
        return this.isPlainFont;
    }

    public boolean isBoldFont() {
        return this.isBoldFont;
    }

    public void setFont(String str, int i2, int i3) {
        this.font = new FontUIResource(str, i2, i3);
    }

    public void setFont(Font font) {
        this.font = new FontUIResource(font);
    }

    public void setFont(FontUIResource fontUIResource) {
        this.font = fontUIResource;
    }

    public FontUIResource getFont() {
        return this.isPlainFont ? Theme.plainFont.font : this.isBoldFont ? Theme.boldFont.font : this.font;
    }

    public SBReference getSBReference() {
        return this.sbReference;
    }

    public void setSBReference(SBReference sBReference) {
        this.sbReference = sBReference;
    }

    public void save(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF(this.font.getFamily());
        dataOutputStream.writeBoolean(this.font.isBold());
        dataOutputStream.writeInt(this.font.getSize());
        dataOutputStream.writeBoolean(this.isPlainFont);
        dataOutputStream.writeBoolean(this.isBoldFont);
    }

    public void load(DataInputStream dataInputStream) throws IOException {
        this.font = new FontUIResource(Theme.getPlatformFont(dataInputStream.readUTF()), dataInputStream.readBoolean() ? 1 : 0, dataInputStream.readInt());
        this.isPlainFont = dataInputStream.readBoolean();
        this.isBoldFont = dataInputStream.readBoolean();
    }

    public static void loadDummyData(DataInputStream dataInputStream) throws IOException {
        dataInputStream.readUTF();
        dataInputStream.readBoolean();
        dataInputStream.readInt();
        dataInputStream.readBoolean();
        dataInputStream.readBoolean();
    }

    public String toString() {
        return new StringBuffer().append("ColoredFont[ref=").append(this.sbReference == null ? FXMLLoader.NULL_KEYWORD : this.sbReference.toString()).append(",font=").append((Object) this.font).append("]").toString();
    }
}
