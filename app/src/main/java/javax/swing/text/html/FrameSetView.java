package javax.swing.text.html;

import java.util.StringTokenizer;
import javax.swing.SizeRequirements;
import javax.swing.text.AttributeSet;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/FrameSetView.class */
class FrameSetView extends BoxView {
    String[] children;
    int[] percentChildren;
    int[] absoluteChildren;
    int[] relativeChildren;
    int percentTotals;
    int absoluteTotals;
    int relativeTotals;

    public FrameSetView(Element element, int i2) {
        super(element, i2);
        this.children = null;
    }

    private String[] parseRowColSpec(HTML.Attribute attribute) {
        AttributeSet attributes = getElement().getAttributes();
        String str = "*";
        if (attributes != null && attributes.getAttribute(attribute) != null) {
            str = (String) attributes.getAttribute(attribute);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int iCountTokens = stringTokenizer.countTokens();
        String[] strArr = new String[Math.max(iCountTokens, getViewCount())];
        int i2 = 0;
        while (i2 < iCountTokens) {
            strArr[i2] = stringTokenizer.nextToken().trim();
            if (strArr[i2].equals("100%")) {
                strArr[i2] = "*";
            }
            i2++;
        }
        while (i2 < strArr.length) {
            strArr[i2] = "*";
            i2++;
        }
        return strArr;
    }

    private void init() {
        if (getAxis() == 1) {
            this.children = parseRowColSpec(HTML.Attribute.ROWS);
        } else {
            this.children = parseRowColSpec(HTML.Attribute.COLS);
        }
        this.percentChildren = new int[this.children.length];
        this.relativeChildren = new int[this.children.length];
        this.absoluteChildren = new int[this.children.length];
        for (int i2 = 0; i2 < this.children.length; i2++) {
            this.percentChildren[i2] = -1;
            this.relativeChildren[i2] = -1;
            this.absoluteChildren[i2] = -1;
            if (this.children[i2].endsWith("*")) {
                if (this.children[i2].length() > 1) {
                    this.relativeChildren[i2] = Integer.parseInt(this.children[i2].substring(0, this.children[i2].length() - 1));
                    this.relativeTotals += this.relativeChildren[i2];
                } else {
                    this.relativeChildren[i2] = 1;
                    this.relativeTotals++;
                }
            } else if (this.children[i2].indexOf(37) != -1) {
                this.percentChildren[i2] = parseDigits(this.children[i2]);
                this.percentTotals += this.percentChildren[i2];
            } else {
                this.absoluteChildren[i2] = Integer.parseInt(this.children[i2]);
            }
        }
        if (this.percentTotals > 100) {
            for (int i3 = 0; i3 < this.percentChildren.length; i3++) {
                if (this.percentChildren[i3] > 0) {
                    this.percentChildren[i3] = (this.percentChildren[i3] * 100) / this.percentTotals;
                }
            }
            this.percentTotals = 100;
        }
    }

    @Override // javax.swing.text.BoxView
    protected void layoutMajorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
        if (this.children == null) {
            init();
        }
        SizeRequirements.calculateTiledPositions(i2, null, getChildRequests(i2, i3), iArr, iArr2);
    }

    protected SizeRequirements[] getChildRequests(int i2, int i3) {
        int[] iArr = new int[this.children.length];
        spread(i2, iArr);
        int viewCount = getViewCount();
        SizeRequirements[] sizeRequirementsArr = new SizeRequirements[viewCount];
        int i4 = 0;
        for (int i5 = 0; i5 < viewCount; i5++) {
            View view = getView(i5);
            if ((view instanceof FrameView) || (view instanceof FrameSetView)) {
                sizeRequirementsArr[i5] = new SizeRequirements((int) view.getMinimumSpan(i3), iArr[i4], (int) view.getMaximumSpan(i3), 0.5f);
                i4++;
            } else {
                sizeRequirementsArr[i5] = new SizeRequirements((int) view.getMinimumSpan(i3), (int) view.getPreferredSpan(i3), (int) view.getMaximumSpan(i3), view.getAlignment(i3));
            }
        }
        return sizeRequirementsArr;
    }

    private void spread(int i2, int[] iArr) {
        if (i2 == 0) {
            return;
        }
        int i3 = i2;
        for (int i4 = 0; i4 < iArr.length; i4++) {
            if (this.absoluteChildren[i4] > 0) {
                iArr[i4] = this.absoluteChildren[i4];
                i3 -= iArr[i4];
            }
        }
        int i5 = i3;
        for (int i6 = 0; i6 < iArr.length; i6++) {
            if (this.percentChildren[i6] > 0 && i5 > 0) {
                iArr[i6] = (this.percentChildren[i6] * i5) / 100;
                i3 -= iArr[i6];
            } else if (this.percentChildren[i6] > 0 && i5 <= 0) {
                iArr[i6] = i2 / iArr.length;
                i3 -= iArr[i6];
            }
        }
        if (i3 > 0 && this.relativeTotals > 0) {
            for (int i7 = 0; i7 < iArr.length; i7++) {
                if (this.relativeChildren[i7] > 0) {
                    iArr[i7] = (i3 * this.relativeChildren[i7]) / this.relativeTotals;
                }
            }
            return;
        }
        if (i3 > 0) {
            float f2 = i2 - i3;
            float[] fArr = new float[iArr.length];
            int i8 = i2;
            for (int i9 = 0; i9 < iArr.length; i9++) {
                fArr[i9] = (iArr[i9] / f2) * 100.0f;
                iArr[i9] = (int) ((i2 * fArr[i9]) / 100.0f);
                i8 -= iArr[i9];
            }
            int i10 = 0;
            while (i8 != 0) {
                if (i8 < 0) {
                    int i11 = i10;
                    i10++;
                    iArr[i11] = iArr[i11] - 1;
                    i8++;
                } else {
                    int i12 = i10;
                    i10++;
                    iArr[i12] = iArr[i12] + 1;
                    i8--;
                }
                if (i10 == iArr.length) {
                    i10 = 0;
                }
            }
        }
    }

    private int parseDigits(String str) {
        int iDigit = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (Character.isDigit(cCharAt)) {
                iDigit = (iDigit * 10) + Character.digit(cCharAt, 10);
            }
        }
        return iDigit;
    }
}
