package com.efiAnalytics.ui;

import bH.C0995c;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JTextArea;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aP.class */
public class aP extends JTextArea {

    /* renamed from: b, reason: collision with root package name */
    private int f10754b = 16;

    /* renamed from: c, reason: collision with root package name */
    private int[] f10755c = null;

    /* renamed from: a, reason: collision with root package name */
    Dimension f10756a = new Dimension(10, 10);

    /* renamed from: d, reason: collision with root package name */
    private Insets f10757d = new Insets(5, 5, 5, 5);

    /* renamed from: e, reason: collision with root package name */
    private ArrayList f10758e = new ArrayList();

    public aP() {
        setAutoscrolls(true);
        setFont(new Font("Monospaced", 0, 12));
        setColumns(77);
    }

    @Override // javax.swing.JComponent
    public String getToolTipText() {
        return a();
    }

    public String a() {
        int[] iArrB = b();
        String str = "";
        if (iArrB.length <= 4) {
            str = (str + "Big endianess: " + C0995c.b(iArrB, 0, iArrB.length, true, false)) + ", Little endianess: " + C0995c.b(iArrB, 0, iArrB.length, false, false);
        } else if (iArrB.length > 4) {
            str = str + "Select 4 or less bytes for endianess values";
        }
        return str;
    }

    protected int[] b() {
        String selectedText = getSelectedText();
        if (selectedText == null || selectedText.equals("")) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(selectedText, " ");
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(stringTokenizer.nextToken());
        }
        int[] iArr = new int[arrayList.size()];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = Integer.parseInt(bH.W.b((String) arrayList.get(i2), "0x", ""), this.f10754b);
        }
        return iArr;
    }

    public String c() {
        return new String(C0995c.a(b()));
    }

    private String f() {
        int i2 = 0;
        long jNanoTime = System.nanoTime();
        StringBuffer stringBuffer = new StringBuffer();
        this.f10758e.clear();
        while (this.f10755c != null && this.f10755c.length > i2) {
            StringBuffer stringBuffer2 = new StringBuffer();
            for (int i3 = 0; i3 < e() && i2 < this.f10755c.length; i3++) {
                stringBuffer2.append(c(C0995c.a((byte) this.f10755c[i2])));
                stringBuffer2.append(' ');
                i2++;
            }
            this.f10758e.add(stringBuffer2.toString());
            stringBuffer2.append('\n');
            stringBuffer.append(stringBuffer2.toString());
        }
        bH.C.c("Time to format:" + ((System.nanoTime() - jNanoTime) / 1000000) + "ms.");
        return stringBuffer.toString();
    }

    public int d() {
        return this.f10754b;
    }

    public void a(int i2) {
        int iE = e();
        this.f10754b = i2;
        b(iE);
        setText(f());
    }

    public int e() {
        return getColumns() / g();
    }

    public void b(int i2) {
        super.setColumns(i2 * g());
        setText(f());
        h();
    }

    @Override // javax.swing.text.JTextComponent
    public void setText(String str) {
        int caretPosition = getCaretPosition();
        super.setText(str);
        if (caretPosition > str.length()) {
            caretPosition = str.length();
        }
        setCaretPosition(caretPosition);
    }

    public void a(int[] iArr) {
        this.f10755c = iArr;
        setText(f());
        h();
    }

    private int g() {
        switch (this.f10754b) {
            case 2:
                return 9;
            case 10:
                return 4;
            case 16:
                return 3;
            default:
                return 4;
        }
    }

    private void h() {
        if (this.f10755c == null) {
            this.f10756a = new Dimension(10, 10);
            return;
        }
        FontMetrics fontMetrics = getFontMetrics(getFont());
        this.f10756a = new Dimension((fontMetrics.charWidth('0') * e() * g()) + this.f10757d.left + this.f10757d.right, (fontMetrics.getHeight() * (1 + (this.f10755c.length / e()))) + this.f10757d.top + this.f10757d.bottom);
    }

    private String c(int i2) {
        switch (this.f10754b) {
            case 2:
                return bH.W.a(Integer.toBinaryString(i2), '0', 8);
            case 10:
                return bH.W.a(Integer.toString(i2), '0', 3);
            case 16:
                return bH.W.a(Integer.toHexString(i2), '0', 2);
            default:
                return bH.W.a(Integer.toHexString(i2), '0', 2);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return this.f10756a;
    }

    @Override // javax.swing.JTextArea, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }
}
