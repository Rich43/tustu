package sun.util.locale.provider;

import java.io.IOException;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.Stack;

/* loaded from: rt.jar:sun/util/locale/provider/DictionaryBasedBreakIterator.class */
class DictionaryBasedBreakIterator extends RuleBasedBreakIterator {
    private BreakDictionary dictionary;
    private boolean[] categoryFlags;
    private int dictionaryCharCount;
    private int[] cachedBreakPositions;
    private int positionInCache;

    DictionaryBasedBreakIterator(String str, String str2) throws IOException {
        super(str);
        byte[] additionalData = super.getAdditionalData();
        if (additionalData != null) {
            prepareCategoryFlags(additionalData);
            super.setAdditionalData(null);
        }
        this.dictionary = new BreakDictionary(str2);
    }

    private void prepareCategoryFlags(byte[] bArr) {
        this.categoryFlags = new boolean[bArr.length];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            this.categoryFlags[i2] = bArr[i2] == 1;
        }
    }

    @Override // sun.util.locale.provider.RuleBasedBreakIterator, java.text.BreakIterator
    public void setText(CharacterIterator characterIterator) {
        super.setText(characterIterator);
        this.cachedBreakPositions = null;
        this.dictionaryCharCount = 0;
        this.positionInCache = 0;
    }

    @Override // sun.util.locale.provider.RuleBasedBreakIterator, java.text.BreakIterator
    public int first() {
        this.cachedBreakPositions = null;
        this.dictionaryCharCount = 0;
        this.positionInCache = 0;
        return super.first();
    }

    @Override // sun.util.locale.provider.RuleBasedBreakIterator, java.text.BreakIterator
    public int last() {
        this.cachedBreakPositions = null;
        this.dictionaryCharCount = 0;
        this.positionInCache = 0;
        return super.last();
    }

    @Override // sun.util.locale.provider.RuleBasedBreakIterator, java.text.BreakIterator
    public int previous() {
        CharacterIterator text = getText();
        if (this.cachedBreakPositions != null && this.positionInCache > 0) {
            this.positionInCache--;
            text.setIndex(this.cachedBreakPositions[this.positionInCache]);
            return this.cachedBreakPositions[this.positionInCache];
        }
        this.cachedBreakPositions = null;
        int iPrevious = super.previous();
        if (this.cachedBreakPositions != null) {
            this.positionInCache = this.cachedBreakPositions.length - 2;
        }
        return iPrevious;
    }

    @Override // sun.util.locale.provider.RuleBasedBreakIterator, java.text.BreakIterator
    public int preceding(int i2) {
        CharacterIterator text = getText();
        checkOffset(i2, text);
        if (this.cachedBreakPositions == null || i2 <= this.cachedBreakPositions[0] || i2 > this.cachedBreakPositions[this.cachedBreakPositions.length - 1]) {
            this.cachedBreakPositions = null;
            return super.preceding(i2);
        }
        this.positionInCache = 0;
        while (this.positionInCache < this.cachedBreakPositions.length && i2 > this.cachedBreakPositions[this.positionInCache]) {
            this.positionInCache++;
        }
        this.positionInCache--;
        text.setIndex(this.cachedBreakPositions[this.positionInCache]);
        return text.getIndex();
    }

    @Override // sun.util.locale.provider.RuleBasedBreakIterator, java.text.BreakIterator
    public int following(int i2) {
        CharacterIterator text = getText();
        checkOffset(i2, text);
        if (this.cachedBreakPositions == null || i2 < this.cachedBreakPositions[0] || i2 >= this.cachedBreakPositions[this.cachedBreakPositions.length - 1]) {
            this.cachedBreakPositions = null;
            return super.following(i2);
        }
        this.positionInCache = 0;
        while (this.positionInCache < this.cachedBreakPositions.length && i2 >= this.cachedBreakPositions[this.positionInCache]) {
            this.positionInCache++;
        }
        text.setIndex(this.cachedBreakPositions[this.positionInCache]);
        return text.getIndex();
    }

    @Override // sun.util.locale.provider.RuleBasedBreakIterator
    protected int handleNext() {
        CharacterIterator text = getText();
        if (this.cachedBreakPositions == null || this.positionInCache == this.cachedBreakPositions.length - 1) {
            int index = text.getIndex();
            this.dictionaryCharCount = 0;
            int iHandleNext = super.handleNext();
            if (this.dictionaryCharCount > 1 && iHandleNext - index > 1) {
                divideUpDictionaryRange(index, iHandleNext);
            } else {
                this.cachedBreakPositions = null;
                return iHandleNext;
            }
        }
        if (this.cachedBreakPositions != null) {
            this.positionInCache++;
            text.setIndex(this.cachedBreakPositions[this.positionInCache]);
            return this.cachedBreakPositions[this.positionInCache];
        }
        return -9999;
    }

    @Override // sun.util.locale.provider.RuleBasedBreakIterator
    protected int lookupCategory(int i2) {
        int iLookupCategory = super.lookupCategory(i2);
        if (iLookupCategory != -1 && this.categoryFlags[iLookupCategory]) {
            this.dictionaryCharCount++;
        }
        return iLookupCategory;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void divideUpDictionaryRange(int i2, int i3) {
        CharacterIterator text = getText();
        text.setIndex(i2);
        int iLookupCategory = lookupCategory(getCurrent());
        while (true) {
            int i4 = iLookupCategory;
            if (i4 != -1 && this.categoryFlags[i4]) {
                break;
            } else {
                iLookupCategory = lookupCategory(getNext());
            }
        }
        Stack stack = new Stack();
        Stack stack2 = new Stack();
        ArrayList arrayList = new ArrayList();
        short nextStateFromCharacter = 0;
        int index = text.getIndex();
        Stack stack3 = null;
        int current = getCurrent();
        while (true) {
            if (this.dictionary.getNextState(nextStateFromCharacter, 0) == -1) {
                stack2.push(Integer.valueOf(text.getIndex()));
            }
            nextStateFromCharacter = this.dictionary.getNextStateFromCharacter(nextStateFromCharacter, current);
            if (nextStateFromCharacter == -1) {
                stack.push(Integer.valueOf(text.getIndex()));
                break;
            }
            if (nextStateFromCharacter == 0 || text.getIndex() >= i3) {
                if (text.getIndex() > index) {
                    index = text.getIndex();
                    stack3 = (Stack) stack.clone();
                }
                while (!stack2.isEmpty() && arrayList.contains(stack2.peek())) {
                    stack2.pop();
                }
                if (stack2.isEmpty()) {
                    if (stack3 != null) {
                        stack = stack3;
                        if (index >= i3) {
                            break;
                        } else {
                            text.setIndex(index + 1);
                        }
                    } else {
                        if ((stack.size() == 0 || ((Integer) stack.peek()).intValue() != text.getIndex()) && text.getIndex() != i2) {
                            stack.push(new Integer(text.getIndex()));
                        }
                        getNext();
                        stack.push(new Integer(text.getIndex()));
                    }
                } else {
                    Integer num = (Integer) stack2.pop();
                    while (!stack.isEmpty() && num.intValue() < ((Integer) stack.peek()).intValue()) {
                        arrayList.add((Integer) stack.pop());
                    }
                    stack.push(num);
                    text.setIndex(((Integer) stack.peek()).intValue());
                }
                current = getCurrent();
                if (text.getIndex() >= i3) {
                    break;
                }
            } else {
                current = getNext();
            }
        }
        if (!stack.isEmpty()) {
            stack.pop();
        }
        stack.push(Integer.valueOf(i3));
        this.cachedBreakPositions = new int[stack.size() + 1];
        this.cachedBreakPositions[0] = i2;
        for (int i5 = 0; i5 < stack.size(); i5++) {
            this.cachedBreakPositions[i5 + 1] = ((Integer) stack.elementAt(i5)).intValue();
        }
        this.positionInCache = 0;
    }
}
