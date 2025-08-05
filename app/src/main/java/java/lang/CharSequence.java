package java.lang;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/* loaded from: rt.jar:java/lang/CharSequence.class */
public interface CharSequence {
    int length();

    char charAt(int i2);

    CharSequence subSequence(int i2, int i3);

    String toString();

    default IntStream chars() {
        return StreamSupport.intStream(() -> {
            return Spliterators.spliterator(new PrimitiveIterator.OfInt() { // from class: java.lang.CharSequence.1CharIterator
                int cur = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.cur < CharSequence.this.length();
                }

                @Override // java.util.PrimitiveIterator.OfInt
                public int nextInt() {
                    if (hasNext()) {
                        CharSequence charSequence = CharSequence.this;
                        int i2 = this.cur;
                        this.cur = i2 + 1;
                        return charSequence.charAt(i2);
                    }
                    throw new NoSuchElementException();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.PrimitiveIterator.OfInt, java.util.PrimitiveIterator
                public void forEachRemaining(IntConsumer intConsumer) {
                    while (this.cur < CharSequence.this.length()) {
                        intConsumer.accept(CharSequence.this.charAt(this.cur));
                        this.cur++;
                    }
                }
            }, length(), 16);
        }, 16464, false);
    }

    default IntStream codePoints() {
        return StreamSupport.intStream(() -> {
            return Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfInt() { // from class: java.lang.CharSequence.1CodePointIterator
                int cur = 0;

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.PrimitiveIterator.OfInt, java.util.PrimitiveIterator
                public void forEachRemaining(IntConsumer intConsumer) {
                    int length = CharSequence.this.length();
                    int i2 = this.cur;
                    while (i2 < length) {
                        try {
                            int i3 = i2;
                            i2++;
                            char cCharAt = CharSequence.this.charAt(i3);
                            if (!Character.isHighSurrogate(cCharAt) || i2 >= length) {
                                intConsumer.accept(cCharAt);
                            } else {
                                char cCharAt2 = CharSequence.this.charAt(i2);
                                if (Character.isLowSurrogate(cCharAt2)) {
                                    i2++;
                                    intConsumer.accept(Character.toCodePoint(cCharAt, cCharAt2));
                                } else {
                                    intConsumer.accept(cCharAt);
                                }
                            }
                        } finally {
                            this.cur = i2;
                        }
                    }
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.cur < CharSequence.this.length();
                }

                @Override // java.util.PrimitiveIterator.OfInt
                public int nextInt() {
                    int length = CharSequence.this.length();
                    if (this.cur >= length) {
                        throw new NoSuchElementException();
                    }
                    CharSequence charSequence = CharSequence.this;
                    int i2 = this.cur;
                    this.cur = i2 + 1;
                    char cCharAt = charSequence.charAt(i2);
                    if (Character.isHighSurrogate(cCharAt) && this.cur < length) {
                        char cCharAt2 = CharSequence.this.charAt(this.cur);
                        if (Character.isLowSurrogate(cCharAt2)) {
                            this.cur++;
                            return Character.toCodePoint(cCharAt, cCharAt2);
                        }
                    }
                    return cCharAt;
                }
            }, 16);
        }, 16, false);
    }
}
