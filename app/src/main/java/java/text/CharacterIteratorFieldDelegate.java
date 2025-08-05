package java.text;

import java.text.Format;
import java.util.ArrayList;

/* loaded from: rt.jar:java/text/CharacterIteratorFieldDelegate.class */
class CharacterIteratorFieldDelegate implements Format.FieldDelegate {
    private ArrayList<AttributedString> attributedStrings = new ArrayList<>();
    private int size;

    CharacterIteratorFieldDelegate() {
    }

    @Override // java.text.Format.FieldDelegate
    public void formatted(Format.Field field, Object obj, int i2, int i3, StringBuffer stringBuffer) {
        if (i2 != i3) {
            if (i2 < this.size) {
                int i4 = this.size;
                int size = this.attributedStrings.size() - 1;
                while (i2 < i4) {
                    int i5 = size;
                    size--;
                    AttributedString attributedString = this.attributedStrings.get(i5);
                    int length = i4 - attributedString.length();
                    int iMax = Math.max(0, i2 - length);
                    attributedString.addAttribute(field, obj, iMax, Math.min(i3 - i2, attributedString.length() - iMax) + iMax);
                    i4 = length;
                }
            }
            if (this.size < i2) {
                this.attributedStrings.add(new AttributedString(stringBuffer.substring(this.size, i2)));
                this.size = i2;
            }
            if (this.size < i3) {
                AttributedString attributedString2 = new AttributedString(stringBuffer.substring(Math.max(i2, this.size), i3));
                attributedString2.addAttribute(field, obj);
                this.attributedStrings.add(attributedString2);
                this.size = i3;
            }
        }
    }

    @Override // java.text.Format.FieldDelegate
    public void formatted(int i2, Format.Field field, Object obj, int i3, int i4, StringBuffer stringBuffer) {
        formatted(field, obj, i3, i4, stringBuffer);
    }

    public AttributedCharacterIterator getIterator(String str) {
        if (str.length() > this.size) {
            this.attributedStrings.add(new AttributedString(str.substring(this.size)));
            this.size = str.length();
        }
        int size = this.attributedStrings.size();
        AttributedCharacterIterator[] attributedCharacterIteratorArr = new AttributedCharacterIterator[size];
        for (int i2 = 0; i2 < size; i2++) {
            attributedCharacterIteratorArr[i2] = this.attributedStrings.get(i2).getIterator();
        }
        return new AttributedString(attributedCharacterIteratorArr).getIterator();
    }
}
