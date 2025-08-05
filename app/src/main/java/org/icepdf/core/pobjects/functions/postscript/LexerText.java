package org.icepdf.core.pobjects.functions.postscript;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/functions/postscript/LexerText.class */
public class LexerText {
    public static final String TEST_1 = "{1.000000 3 1 roll 1.000000 3 1 roll 1.000000 3 1 roll 5 -1 roll \n2 index -0.874500 mul 1.000000 add mul 1 index -0.098000 mul 1.000000 add mul 5 \n1 roll 4 -1 roll 2 index -0.796100 mul 1.000000 add mul 1 index -0.247100 \nmul 1.000000 add mul 4 1 roll 3 -1 roll 2 index -0.647100 mul 1.000000 \nadd mul 1 index -0.878400 mul 1.000000 add mul 3 1 roll pop pop }";
    public static final String TEST_2 = "{1.000000 2 1 roll 1.000000 2 1 roll 1.000000 2 1 roll 0 index 1.000000 \ncvr exch sub 2 1 roll 5 -1 roll 1.000000 cvr exch sub 5 1 \nroll 4 -1 roll 1.000000 cvr exch sub 4 1 roll 3 -1 roll 1.000000 \ncvr exch sub 3 1 roll 2 -1 roll 1.000000 cvr exch sub 2 1 \nroll pop }";
    public static final String TEST_3 = "{0 0 0 0 5 4 roll 0 index 3 -1 roll add 2 1 roll pop dup 1 gt {pop 1} if 4 1 roll dup 1 gt {pop 1} if 4 1 roll dup 1 gt {pop 1} if 4 1 roll dup 1 gt {pop 1} if 4 1 roll}";
    public static final String TEST_4 = "{dup 0 lt {pop 0 }{dup 1 gt {pop 1 } if } ifelse 0 index 1 exp 1 mul 0 add dup 0 lt {pop 0 }{dup 1 gt {pop 1 } if } ifelse 1 index 1 exp 0 mul 0 add dup 0 lt {pop 0 }{dup 1 gt {pop 1 } if } ifelse 2 index 1 exp 0 mul 0 add dup 0 lt {pop 0 }{dup 1 gt {pop 1 } if } ifelse 3 index 1 exp 0 mul 0 add dup 0 lt {pop 0 }{dup 1 gt {pop 1 } if } ifelse 5 4 roll pop }";

    public static void main(String[] args) {
        try {
            new LexerText().test8();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void test1() throws IOException {
        InputStream function_4 = new ByteArrayInputStream(TEST_1.getBytes());
        Lexer lex = new Lexer();
        lex.setInputStream(function_4);
        lex.parse(new float[]{1.0f, 1.0f});
        System.out.println("result: " + lex.getStack().toString());
    }

    public void test2() throws IOException {
        InputStream function_4 = new ByteArrayInputStream("{2 index 1.000000 cvr exch sub 4 1 roll 1 index 1.000000 cvr exch sub \n4 1 roll 0 index 1.000000 cvr exch sub 4 1 roll 1.000000 4 1 \nroll 7 -1 roll 1.000000 cvr exch sub 7 1 roll 6 -1 roll 1.000000 \ncvr exch sub 6 1 roll 5 -1 roll 1.000000 cvr exch sub 5 1 \nroll 4 -1 roll 1.000000 cvr exch sub 4 1 roll pop pop pop }".getBytes());
        Lexer lex = new Lexer();
        lex.setInputStream(function_4);
        lex.parse(new float[]{0.360779f, 0.094238274f, 0.00392151f});
        System.out.println("result: " + lex.getStack().toString());
    }

    public void test5() throws IOException {
        InputStream function_4 = new ByteArrayInputStream("{2 index 1.000000 cvr exch sub 4 1 roll 1 index 1.000000 cvr exch sub \n4 1 roll 0 index 1.000000 cvr exch sub 4 1 roll 1.000000 4 1 \nroll 7 -1 roll 1.000000 cvr exch sub 7 1 roll 6 -1 roll 1.000000 \ncvr exch sub 6 1 roll 5 -1 roll 1.000000 cvr exch sub 5 1 \nroll 4 -1 roll 1.000000 cvr exch sub 4 1 roll pop pop pop }".getBytes());
        Lexer lex = new Lexer();
        lex.setInputStream(function_4);
        lex.parse(new float[]{0.360779f, 0.094238274f, 0.00392151f});
        System.out.println("result: " + lex.getStack().toString());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void test6() throws IOException {
        InputStream function_4 = new ByteArrayInputStream(TEST_2.getBytes());
        Lexer lex = new Lexer();
        lex.setInputStream(function_4);
        lex.parse(new float[]{0.300003f});
        System.out.println("result: " + lex.getStack().toString());
        float[] range = {0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f};
        float[] y2 = new float[4];
        System.out.println();
        for (int i2 = 0; i2 < 4; i2++) {
            float value = ((Float) lex.getStack().elementAt(i2)).floatValue();
            y2[i2] = Math.min(Math.max(value, range[2 * i2]), range[(2 * i2) + 1]);
            System.out.print(y2[i2] + ", ");
        }
        System.out.println();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void test7() throws IOException {
        InputStream function_4 = new ByteArrayInputStream(TEST_3.getBytes());
        Lexer lex = new Lexer();
        lex.setInputStream(function_4);
        lex.parse(new float[]{1.0f});
        System.out.println("result: " + lex.getStack().toString());
        float[] range = {0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f};
        float[] y2 = new float[4];
        System.out.println();
        for (int i2 = 0; i2 < 4; i2++) {
            float value = ((Float) lex.getStack().elementAt(i2)).floatValue();
            y2[i2] = Math.min(Math.max(value, range[2 * i2]), range[(2 * i2) + 1]);
            System.out.print(y2[i2] + ", ");
        }
        System.out.println();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void test8() throws IOException {
        InputStream function_4 = new ByteArrayInputStream(TEST_4.getBytes());
        Lexer lex = new Lexer();
        lex.setInputStream(function_4);
        lex.parse(new float[]{1.0f});
        System.out.println("result: " + lex.getStack().toString());
        float[] range = {0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f};
        float[] y2 = new float[4];
        System.out.println();
        for (int i2 = 0; i2 < 4; i2++) {
            float value = ((Float) lex.getStack().elementAt(i2)).floatValue();
            y2[i2] = Math.min(Math.max(value, range[2 * i2]), range[(2 * i2) + 1]);
            System.out.print(y2[i2] + ", ");
        }
        System.out.println();
    }
}
