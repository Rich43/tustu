package bB;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:bB/j.class */
class j implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    long f6551a = 0;

    /* renamed from: b, reason: collision with root package name */
    StringBuilder f6552b = new StringBuilder();

    /* renamed from: c, reason: collision with root package name */
    long f6553c = 2000;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ h f6554d;

    j(h hVar) {
        this.f6554d = hVar;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        if (System.currentTimeMillis() - this.f6551a > this.f6553c && this.f6552b.length() > 0) {
            this.f6552b.delete(0, this.f6552b.length());
        }
        this.f6551a = System.currentTimeMillis();
        if (keyEvent.getKeyChar() != '\b' || this.f6552b.length() <= 0) {
            this.f6552b.append(keyEvent.getKeyChar());
        } else {
            this.f6552b.setLength(this.f6552b.length() - 1);
        }
        String lowerCase = this.f6552b.toString().toLowerCase();
        if (lowerCase.length() > 0) {
            boolean z2 = false;
            int i2 = 0;
            while (true) {
                if (i2 >= this.f6554d.f6548b.getRowCount()) {
                    break;
                }
                if (this.f6554d.f6548b.getValueAt(i2, 0).toString().toLowerCase().startsWith(lowerCase)) {
                    this.f6554d.getSelectionModel().setSelectionInterval(i2, i2);
                    this.f6554d.scrollRectToVisible(new Rectangle(this.f6554d.getCellRect(i2, 0, true)));
                    z2 = true;
                    break;
                }
                i2++;
            }
            if (z2) {
                return;
            }
            for (int i3 = 0; i3 < this.f6554d.f6548b.getRowCount(); i3++) {
                if (this.f6554d.f6548b.getValueAt(i3, 0).toString().toLowerCase().contains(lowerCase)) {
                    this.f6554d.getSelectionModel().setSelectionInterval(i3, i3);
                    this.f6554d.scrollRectToVisible(new Rectangle(this.f6554d.getCellRect(i3, 0, true)));
                    return;
                }
            }
        }
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
    }
}
