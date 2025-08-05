package org.apache.commons.net.nntp;

import java.io.PrintStream;
import java.util.ArrayList;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/Article.class */
public class Article implements Threadable {
    private String subject;
    private String date;
    private String articleId;
    private String simplifiedSubject;
    private String from;
    private ArrayList<String> references;
    public Article kid;
    public Article next;
    private boolean isReply = false;
    private long articleNumber = -1;

    public void addReference(String msgId) {
        if (msgId == null || msgId.length() == 0) {
            return;
        }
        if (this.references == null) {
            this.references = new ArrayList<>();
        }
        this.isReply = true;
        String[] arr$ = msgId.split(" ");
        for (String s2 : arr$) {
            this.references.add(s2);
        }
    }

    public String[] getReferences() {
        if (this.references == null) {
            return new String[0];
        }
        return (String[]) this.references.toArray(new String[this.references.size()]);
    }

    private void simplifySubject() {
        int start = 0;
        String subject = getSubject();
        int len = subject.length();
        boolean done = false;
        while (!done) {
            done = true;
            while (start < len && subject.charAt(start) == ' ') {
                start++;
            }
            if (start < len - 2 && ((subject.charAt(start) == 'r' || subject.charAt(start) == 'R') && (subject.charAt(start + 1) == 'e' || subject.charAt(start + 1) == 'E'))) {
                if (subject.charAt(start + 2) == ':') {
                    start += 3;
                    done = false;
                } else if (start < len - 2 && (subject.charAt(start + 2) == '[' || subject.charAt(start + 2) == '(')) {
                    int i2 = start + 3;
                    while (i2 < len && subject.charAt(i2) >= '0' && subject.charAt(i2) <= '9') {
                        i2++;
                    }
                    if (i2 < len - 1 && ((subject.charAt(i2) == ']' || subject.charAt(i2) == ')') && subject.charAt(i2 + 1) == ':')) {
                        start = i2 + 2;
                        done = false;
                    }
                }
            }
            if ("(no subject)".equals(this.simplifiedSubject)) {
                this.simplifiedSubject = "";
            }
            int end = len;
            while (end > start && subject.charAt(end - 1) < ' ') {
                end--;
            }
            if (start == 0 && end == len) {
                this.simplifiedSubject = subject;
            } else {
                this.simplifiedSubject = subject.substring(start, end);
            }
        }
    }

    public static void printThread(Article article) {
        printThread(article, 0, System.out);
    }

    public static void printThread(Article article, PrintStream ps) {
        printThread(article, 0, ps);
    }

    public static void printThread(Article article, int depth) {
        printThread(article, depth, System.out);
    }

    public static void printThread(Article article, int depth, PrintStream ps) {
        for (int i2 = 0; i2 < depth; i2++) {
            ps.print("==>");
        }
        ps.println(article.getSubject() + "\t" + article.getFrom() + "\t" + article.getArticleId());
        if (article.kid != null) {
            printThread(article.kid, depth + 1);
        }
        if (article.next != null) {
            printThread(article.next, depth);
        }
    }

    public String getArticleId() {
        return this.articleId;
    }

    public long getArticleNumberLong() {
        return this.articleNumber;
    }

    public String getDate() {
        return this.date;
    }

    public String getFrom() {
        return this.from;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setArticleId(String string) {
        this.articleId = string;
    }

    public void setArticleNumber(long l2) {
        this.articleNumber = l2;
    }

    public void setDate(String string) {
        this.date = string;
    }

    public void setFrom(String string) {
        this.from = string;
    }

    public void setSubject(String string) {
        this.subject = string;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public boolean isDummy() {
        return this.articleNumber == -1;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public String messageThreadId() {
        return this.articleId;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public String[] messageThreadReferences() {
        return getReferences();
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public String simplifiedSubject() {
        if (this.simplifiedSubject == null) {
            simplifySubject();
        }
        return this.simplifiedSubject;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public boolean subjectIsReply() {
        return this.isReply;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public void setChild(Threadable child) {
        this.kid = (Article) child;
        flushSubjectCache();
    }

    private void flushSubjectCache() {
        this.simplifiedSubject = null;
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public void setNext(Threadable next) {
        this.next = (Article) next;
        flushSubjectCache();
    }

    @Override // org.apache.commons.net.nntp.Threadable
    public Threadable makeDummy() {
        return new Article();
    }

    public String toString() {
        return this.articleNumber + " " + this.articleId + " " + this.subject;
    }

    @Deprecated
    public int getArticleNumber() {
        return (int) this.articleNumber;
    }

    @Deprecated
    public void setArticleNumber(int a2) {
        this.articleNumber = a2;
    }

    @Deprecated
    public void addHeaderField(String name, String val) {
    }
}
