package com.sun.webkit;

import com.sun.javafx.scene.control.skin.CustomColorDialog;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

/* loaded from: jfxrt.jar:com/sun/webkit/ColorChooser.class */
public final class ColorChooser {
    private static final double COLOR_DOUBLE_TO_UCHAR_FACTOR = 255.0d;
    private CustomColorDialog colorChooserDialog;
    private final long pdata;

    private native void twkSetSelectedColor(long j2, int i2, int i3, int i4);

    private ColorChooser(WebPage webPage, Color color, long data) {
        this.pdata = data;
        WebPageClient<WebView> client = webPage.getPageClient();
        this.colorChooserDialog = new CustomColorDialog(client.getContainer().getScene().getWindow());
        this.colorChooserDialog.setSaveBtnToOk();
        this.colorChooserDialog.setShowUseBtn(false);
        this.colorChooserDialog.setShowOpacitySlider(false);
        this.colorChooserDialog.setOnSave(() -> {
            twkSetSelectedColor(this.pdata, (int) Math.round(this.colorChooserDialog.getCustomColor().getRed() * COLOR_DOUBLE_TO_UCHAR_FACTOR), (int) Math.round(this.colorChooserDialog.getCustomColor().getGreen() * COLOR_DOUBLE_TO_UCHAR_FACTOR), (int) Math.round(this.colorChooserDialog.getCustomColor().getBlue() * COLOR_DOUBLE_TO_UCHAR_FACTOR));
        });
        this.colorChooserDialog.setCurrentColor(color);
        this.colorChooserDialog.show();
    }

    private static ColorChooser fwkCreateAndShowColorChooser(WebPage webPage, int r2, int g2, int b2, long pdata) {
        return new ColorChooser(webPage, Color.rgb(r2, g2, b2), pdata);
    }

    private void fwkShowColorChooser(int r2, int g2, int b2) {
        this.colorChooserDialog.setCurrentColor(Color.rgb(r2, g2, b2));
        this.colorChooserDialog.show();
    }

    private void fwkHideColorChooser() {
        this.colorChooserDialog.hide();
    }
}
