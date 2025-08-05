package sun.tools.jar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:sun/tools/jar/CommandLine.class */
public class CommandLine {
    public static String[] parse(String[] strArr) throws IOException {
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            if (str.length() > 1 && str.charAt(0) == '@') {
                String strSubstring = str.substring(1);
                if (strSubstring.charAt(0) == '@') {
                    arrayList.add(strSubstring);
                } else {
                    loadCmdFile(strSubstring, arrayList);
                }
            } else {
                arrayList.add(str);
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private static void loadCmdFile(String str, List<String> list) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(str));
        StreamTokenizer streamTokenizer = new StreamTokenizer(bufferedReader);
        streamTokenizer.resetSyntax();
        streamTokenizer.wordChars(32, 255);
        streamTokenizer.whitespaceChars(0, 32);
        streamTokenizer.commentChar(35);
        streamTokenizer.quoteChar(34);
        streamTokenizer.quoteChar(39);
        while (streamTokenizer.nextToken() != -1) {
            list.add(streamTokenizer.sval);
        }
        bufferedReader.close();
    }
}
