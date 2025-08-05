package com.sun.javafx.webkit;

import com.sun.webkit.Pasteboard;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageFrame;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javax.imageio.ImageIO;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/PasteboardImpl.class */
final class PasteboardImpl implements Pasteboard {
    private final Clipboard clipboard = Clipboard.getSystemClipboard();

    PasteboardImpl() {
    }

    @Override // com.sun.webkit.Pasteboard
    public String getPlainText() {
        return this.clipboard.getString();
    }

    @Override // com.sun.webkit.Pasteboard
    public String getHtml() {
        return this.clipboard.getHtml();
    }

    @Override // com.sun.webkit.Pasteboard
    public void writePlainText(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        this.clipboard.setContent(content);
    }

    @Override // com.sun.webkit.Pasteboard
    public void writeSelection(boolean canSmartCopyOrDelete, String text, String html) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        content.putHtml(html);
        this.clipboard.setContent(content);
    }

    @Override // com.sun.webkit.Pasteboard
    public void writeImage(WCImageFrame frame) {
        WCImage img = frame.getFrame();
        Image fxImage = (img == null || img.isNull()) ? null : Image.impl_fromPlatformImage(img.getPlatformImage());
        if (fxImage != null) {
            ClipboardContent content = new ClipboardContent();
            content.putImage(fxImage);
            String fileExtension = img.getFileExtension();
            try {
                File imageDump = File.createTempFile("jfx", "." + fileExtension);
                imageDump.deleteOnExit();
                ImageIO.write(img.toBufferedImage(), fileExtension, imageDump);
                content.putFiles(Arrays.asList(imageDump));
            } catch (IOException | SecurityException e2) {
            }
            this.clipboard.setContent(content);
        }
    }

    @Override // com.sun.webkit.Pasteboard
    public void writeUrl(String url, String markup) {
        ClipboardContent content = new ClipboardContent();
        content.putString(url);
        content.putHtml(markup);
        content.putUrl(url);
        this.clipboard.setContent(content);
    }
}
