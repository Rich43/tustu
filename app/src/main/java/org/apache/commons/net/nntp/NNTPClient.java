package org.apache.commons.net.nntp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.io.DotTerminatedMessageReader;
import org.apache.commons.net.io.DotTerminatedMessageWriter;
import org.apache.commons.net.io.Util;
import sun.util.locale.LanguageTag;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/NNTPClient.class */
public class NNTPClient extends NNTP {
    private void __parseArticlePointer(String reply, ArticleInfo pointer) throws MalformedServerReplyException {
        String[] tokens = reply.split(" ");
        if (tokens.length >= 3) {
            try {
                int i2 = 1 + 1;
                pointer.articleNumber = Long.parseLong(tokens[1]);
                int i3 = i2 + 1;
                pointer.articleId = tokens[i2];
                return;
            } catch (NumberFormatException e2) {
            }
        }
        throw new MalformedServerReplyException("Could not parse article pointer.\nServer reply: " + reply);
    }

    private static void __parseGroupReply(String reply, NewsgroupInfo info) throws MalformedServerReplyException {
        String[] tokens = reply.split(" ");
        if (tokens.length >= 5) {
            try {
                int i2 = 1 + 1;
                info._setArticleCount(Long.parseLong(tokens[1]));
                int i3 = i2 + 1;
                info._setFirstArticle(Long.parseLong(tokens[i2]));
                int i4 = i3 + 1;
                info._setLastArticle(Long.parseLong(tokens[i3]));
                int i5 = i4 + 1;
                info._setNewsgroup(tokens[i4]);
                info._setPostingPermission(0);
                return;
            } catch (NumberFormatException e2) {
            }
        }
        throw new MalformedServerReplyException("Could not parse newsgroup info.\nServer reply: " + reply);
    }

    static NewsgroupInfo __parseNewsgroupListEntry(String entry) {
        String[] tokens = entry.split(" ");
        if (tokens.length < 4) {
            return null;
        }
        NewsgroupInfo result = new NewsgroupInfo();
        int i2 = 0 + 1;
        result._setNewsgroup(tokens[0]);
        try {
            int i3 = i2 + 1;
            long lastNum = Long.parseLong(tokens[i2]);
            int i4 = i3 + 1;
            long firstNum = Long.parseLong(tokens[i3]);
            result._setFirstArticle(firstNum);
            result._setLastArticle(lastNum);
            if (firstNum == 0 && lastNum == 0) {
                result._setArticleCount(0L);
            } else {
                result._setArticleCount((lastNum - firstNum) + 1);
            }
            int i5 = i4 + 1;
            switch (tokens[i4].charAt(0)) {
                case 'M':
                case 'm':
                    result._setPostingPermission(1);
                    break;
                case 'N':
                case 'n':
                    result._setPostingPermission(3);
                    break;
                case 'Y':
                case 'y':
                    result._setPostingPermission(2);
                    break;
                default:
                    result._setPostingPermission(0);
                    break;
            }
            return result;
        } catch (NumberFormatException e2) {
            return null;
        }
    }

    static Article __parseArticleEntry(String line) {
        Article article = new Article();
        article.setSubject(line);
        String[] parts = line.split("\t");
        if (parts.length > 6) {
            try {
                int i2 = 0 + 1;
                article.setArticleNumber(Long.parseLong(parts[0]));
                int i3 = i2 + 1;
                article.setSubject(parts[i2]);
                int i4 = i3 + 1;
                article.setFrom(parts[i3]);
                int i5 = i4 + 1;
                article.setDate(parts[i4]);
                int i6 = i5 + 1;
                article.setArticleId(parts[i5]);
                int i7 = i6 + 1;
                article.addReference(parts[i6]);
            } catch (NumberFormatException e2) {
            }
        }
        return article;
    }

    private NewsgroupInfo[] __readNewsgroupListing() throws IOException {
        BufferedReader reader = new DotTerminatedMessageReader(this._reader_);
        Vector<NewsgroupInfo> list = new Vector<>(2048);
        while (true) {
            try {
                String line = reader.readLine();
                if (line != null) {
                    NewsgroupInfo tmp = __parseNewsgroupListEntry(line);
                    if (tmp != null) {
                        list.addElement(tmp);
                    } else {
                        throw new MalformedServerReplyException(line);
                    }
                } else {
                    int size = list.size();
                    if (size < 1) {
                        return new NewsgroupInfo[0];
                    }
                    NewsgroupInfo[] info = new NewsgroupInfo[size];
                    list.copyInto(info);
                    return info;
                }
            } finally {
                reader.close();
            }
        }
    }

    private BufferedReader __retrieve(int command, String articleId, ArticleInfo pointer) throws IOException {
        if (articleId != null) {
            if (!NNTPReply.isPositiveCompletion(sendCommand(command, articleId))) {
                return null;
            }
        } else if (!NNTPReply.isPositiveCompletion(sendCommand(command))) {
            return null;
        }
        if (pointer != null) {
            __parseArticlePointer(getReplyString(), pointer);
        }
        return new DotTerminatedMessageReader(this._reader_);
    }

    private BufferedReader __retrieve(int command, long articleNumber, ArticleInfo pointer) throws IOException {
        if (!NNTPReply.isPositiveCompletion(sendCommand(command, Long.toString(articleNumber)))) {
            return null;
        }
        if (pointer != null) {
            __parseArticlePointer(getReplyString(), pointer);
        }
        return new DotTerminatedMessageReader(this._reader_);
    }

    public BufferedReader retrieveArticle(String articleId, ArticleInfo pointer) throws IOException {
        return __retrieve(0, articleId, pointer);
    }

    public Reader retrieveArticle(String articleId) throws IOException {
        return retrieveArticle(articleId, (ArticleInfo) null);
    }

    public Reader retrieveArticle() throws IOException {
        return retrieveArticle((String) null);
    }

    public BufferedReader retrieveArticle(long articleNumber, ArticleInfo pointer) throws IOException {
        return __retrieve(0, articleNumber, pointer);
    }

    public BufferedReader retrieveArticle(long articleNumber) throws IOException {
        return retrieveArticle(articleNumber, (ArticleInfo) null);
    }

    public BufferedReader retrieveArticleHeader(String articleId, ArticleInfo pointer) throws IOException {
        return __retrieve(3, articleId, pointer);
    }

    public Reader retrieveArticleHeader(String articleId) throws IOException {
        return retrieveArticleHeader(articleId, (ArticleInfo) null);
    }

    public Reader retrieveArticleHeader() throws IOException {
        return retrieveArticleHeader((String) null);
    }

    public BufferedReader retrieveArticleHeader(long articleNumber, ArticleInfo pointer) throws IOException {
        return __retrieve(3, articleNumber, pointer);
    }

    public BufferedReader retrieveArticleHeader(long articleNumber) throws IOException {
        return retrieveArticleHeader(articleNumber, (ArticleInfo) null);
    }

    public BufferedReader retrieveArticleBody(String articleId, ArticleInfo pointer) throws IOException {
        return __retrieve(1, articleId, pointer);
    }

    public Reader retrieveArticleBody(String articleId) throws IOException {
        return retrieveArticleBody(articleId, (ArticleInfo) null);
    }

    public Reader retrieveArticleBody() throws IOException {
        return retrieveArticleBody((String) null);
    }

    public BufferedReader retrieveArticleBody(long articleNumber, ArticleInfo pointer) throws IOException {
        return __retrieve(1, articleNumber, pointer);
    }

    public BufferedReader retrieveArticleBody(long articleNumber) throws IOException {
        return retrieveArticleBody(articleNumber, (ArticleInfo) null);
    }

    public boolean selectNewsgroup(String newsgroup, NewsgroupInfo info) throws IOException {
        if (!NNTPReply.isPositiveCompletion(group(newsgroup))) {
            return false;
        }
        if (info != null) {
            __parseGroupReply(getReplyString(), info);
            return true;
        }
        return true;
    }

    public boolean selectNewsgroup(String newsgroup) throws IOException {
        return selectNewsgroup(newsgroup, null);
    }

    public String listHelp() throws IOException {
        if (!NNTPReply.isInformational(help())) {
            return null;
        }
        StringWriter help = new StringWriter();
        BufferedReader reader = new DotTerminatedMessageReader(this._reader_);
        Util.copyReader(reader, help);
        reader.close();
        help.close();
        return help.toString();
    }

    public String[] listOverviewFmt() throws IOException {
        if (!NNTPReply.isPositiveCompletion(sendCommand("LIST", "OVERVIEW.FMT"))) {
            return null;
        }
        BufferedReader reader = new DotTerminatedMessageReader(this._reader_);
        ArrayList<String> list = new ArrayList<>();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                list.add(line);
            } else {
                reader.close();
                return (String[]) list.toArray(new String[list.size()]);
            }
        }
    }

    public boolean selectArticle(String articleId, ArticleInfo pointer) throws IOException {
        if (articleId != null) {
            if (!NNTPReply.isPositiveCompletion(stat(articleId))) {
                return false;
            }
        } else if (!NNTPReply.isPositiveCompletion(stat())) {
            return false;
        }
        if (pointer != null) {
            __parseArticlePointer(getReplyString(), pointer);
            return true;
        }
        return true;
    }

    public boolean selectArticle(String articleId) throws IOException {
        return selectArticle(articleId, (ArticleInfo) null);
    }

    public boolean selectArticle(ArticleInfo pointer) throws IOException {
        return selectArticle((String) null, pointer);
    }

    public boolean selectArticle(long articleNumber, ArticleInfo pointer) throws IOException {
        if (!NNTPReply.isPositiveCompletion(stat(articleNumber))) {
            return false;
        }
        if (pointer != null) {
            __parseArticlePointer(getReplyString(), pointer);
            return true;
        }
        return true;
    }

    public boolean selectArticle(long articleNumber) throws IOException {
        return selectArticle(articleNumber, (ArticleInfo) null);
    }

    public boolean selectPreviousArticle(ArticleInfo pointer) throws IOException {
        if (!NNTPReply.isPositiveCompletion(last())) {
            return false;
        }
        if (pointer != null) {
            __parseArticlePointer(getReplyString(), pointer);
            return true;
        }
        return true;
    }

    public boolean selectPreviousArticle() throws IOException {
        return selectPreviousArticle((ArticleInfo) null);
    }

    public boolean selectNextArticle(ArticleInfo pointer) throws IOException {
        if (!NNTPReply.isPositiveCompletion(next())) {
            return false;
        }
        if (pointer != null) {
            __parseArticlePointer(getReplyString(), pointer);
            return true;
        }
        return true;
    }

    public boolean selectNextArticle() throws IOException {
        return selectNextArticle((ArticleInfo) null);
    }

    public NewsgroupInfo[] listNewsgroups() throws IOException {
        if (!NNTPReply.isPositiveCompletion(list())) {
            return null;
        }
        return __readNewsgroupListing();
    }

    public Iterable<String> iterateNewsgroupListing() throws IOException {
        if (NNTPReply.isPositiveCompletion(list())) {
            return new ReplyIterator(this._reader_);
        }
        throw new IOException("LIST command failed: " + getReplyString());
    }

    public Iterable<NewsgroupInfo> iterateNewsgroups() throws IOException {
        return new NewsgroupIterator(iterateNewsgroupListing());
    }

    public NewsgroupInfo[] listNewsgroups(String wildmat) throws IOException {
        if (!NNTPReply.isPositiveCompletion(listActive(wildmat))) {
            return null;
        }
        return __readNewsgroupListing();
    }

    public Iterable<String> iterateNewsgroupListing(String wildmat) throws IOException {
        if (NNTPReply.isPositiveCompletion(listActive(wildmat))) {
            return new ReplyIterator(this._reader_);
        }
        throw new IOException("LIST ACTIVE " + wildmat + " command failed: " + getReplyString());
    }

    public Iterable<NewsgroupInfo> iterateNewsgroups(String wildmat) throws IOException {
        return new NewsgroupIterator(iterateNewsgroupListing(wildmat));
    }

    public NewsgroupInfo[] listNewNewsgroups(NewGroupsOrNewsQuery query) throws IOException {
        if (!NNTPReply.isPositiveCompletion(newgroups(query.getDate(), query.getTime(), query.isGMT(), query.getDistributions()))) {
            return null;
        }
        return __readNewsgroupListing();
    }

    public Iterable<String> iterateNewNewsgroupListing(NewGroupsOrNewsQuery query) throws IOException {
        if (NNTPReply.isPositiveCompletion(newgroups(query.getDate(), query.getTime(), query.isGMT(), query.getDistributions()))) {
            return new ReplyIterator(this._reader_);
        }
        throw new IOException("NEWGROUPS command failed: " + getReplyString());
    }

    public Iterable<NewsgroupInfo> iterateNewNewsgroups(NewGroupsOrNewsQuery query) throws IOException {
        return new NewsgroupIterator(iterateNewNewsgroupListing(query));
    }

    public String[] listNewNews(NewGroupsOrNewsQuery query) throws IOException {
        if (!NNTPReply.isPositiveCompletion(newnews(query.getNewsgroups(), query.getDate(), query.getTime(), query.isGMT(), query.getDistributions()))) {
            return null;
        }
        Vector<String> list = new Vector<>();
        BufferedReader reader = new DotTerminatedMessageReader(this._reader_);
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                list.addElement(line);
            } finally {
                reader.close();
            }
        }
        int size = list.size();
        if (size < 1) {
            return new String[0];
        }
        String[] result = new String[size];
        list.copyInto(result);
        return result;
    }

    public Iterable<String> iterateNewNews(NewGroupsOrNewsQuery query) throws IOException {
        if (NNTPReply.isPositiveCompletion(newnews(query.getNewsgroups(), query.getDate(), query.getTime(), query.isGMT(), query.getDistributions()))) {
            return new ReplyIterator(this._reader_);
        }
        throw new IOException("NEWNEWS command failed: " + getReplyString());
    }

    public boolean completePendingCommand() throws IOException {
        return NNTPReply.isPositiveCompletion(getReply());
    }

    public Writer postArticle() throws IOException {
        if (!NNTPReply.isPositiveIntermediate(post())) {
            return null;
        }
        return new DotTerminatedMessageWriter(this._writer_);
    }

    public Writer forwardArticle(String articleId) throws IOException {
        if (!NNTPReply.isPositiveIntermediate(ihave(articleId))) {
            return null;
        }
        return new DotTerminatedMessageWriter(this._writer_);
    }

    public boolean logout() throws IOException {
        return NNTPReply.isPositiveCompletion(quit());
    }

    public boolean authenticate(String username, String password) throws IOException {
        int replyCode = authinfoUser(username);
        if (replyCode == 381) {
            int replyCode2 = authinfoPass(password);
            if (replyCode2 == 281) {
                this._isAllowedToPost = true;
                return true;
            }
            return false;
        }
        return false;
    }

    private BufferedReader __retrieveArticleInfo(String articleRange) throws IOException {
        if (!NNTPReply.isPositiveCompletion(xover(articleRange))) {
            return null;
        }
        return new DotTerminatedMessageReader(this._reader_);
    }

    public BufferedReader retrieveArticleInfo(long articleNumber) throws IOException {
        return __retrieveArticleInfo(Long.toString(articleNumber));
    }

    public BufferedReader retrieveArticleInfo(long lowArticleNumber, long highArticleNumber) throws IOException {
        return __retrieveArticleInfo(lowArticleNumber + LanguageTag.SEP + highArticleNumber);
    }

    public Iterable<Article> iterateArticleInfo(long lowArticleNumber, long highArticleNumber) throws IOException {
        BufferedReader info = retrieveArticleInfo(lowArticleNumber, highArticleNumber);
        if (info == null) {
            throw new IOException("XOVER command failed: " + getReplyString());
        }
        return new ArticleIterator(new ReplyIterator(info, false));
    }

    private BufferedReader __retrieveHeader(String header, String articleRange) throws IOException {
        if (!NNTPReply.isPositiveCompletion(xhdr(header, articleRange))) {
            return null;
        }
        return new DotTerminatedMessageReader(this._reader_);
    }

    public BufferedReader retrieveHeader(String header, long articleNumber) throws IOException {
        return __retrieveHeader(header, Long.toString(articleNumber));
    }

    public BufferedReader retrieveHeader(String header, long lowArticleNumber, long highArticleNumber) throws IOException {
        return __retrieveHeader(header, lowArticleNumber + LanguageTag.SEP + highArticleNumber);
    }

    @Deprecated
    public Reader retrieveHeader(String header, int lowArticleNumber, int highArticleNumber) throws IOException {
        return retrieveHeader(header, lowArticleNumber, highArticleNumber);
    }

    @Deprecated
    public Reader retrieveArticleInfo(int lowArticleNumber, int highArticleNumber) throws IOException {
        return retrieveArticleInfo(lowArticleNumber, highArticleNumber);
    }

    @Deprecated
    public Reader retrieveHeader(String a2, int b2) throws IOException {
        return retrieveHeader(a2, b2);
    }

    @Deprecated
    public boolean selectArticle(int a2, ArticlePointer ap2) throws IOException {
        ArticleInfo ai2 = __ap2ai(ap2);
        boolean b2 = selectArticle(a2, ai2);
        __ai2ap(ai2, ap2);
        return b2;
    }

    @Deprecated
    public Reader retrieveArticleInfo(int lowArticleNumber) throws IOException {
        return retrieveArticleInfo(lowArticleNumber);
    }

    @Deprecated
    public boolean selectArticle(int a2) throws IOException {
        return selectArticle(a2);
    }

    @Deprecated
    public Reader retrieveArticleHeader(int a2) throws IOException {
        return retrieveArticleHeader(a2);
    }

    @Deprecated
    public Reader retrieveArticleHeader(int a2, ArticlePointer ap2) throws IOException {
        ArticleInfo ai2 = __ap2ai(ap2);
        Reader rdr = retrieveArticleHeader(a2, ai2);
        __ai2ap(ai2, ap2);
        return rdr;
    }

    @Deprecated
    public Reader retrieveArticleBody(int a2) throws IOException {
        return retrieveArticleBody(a2);
    }

    @Deprecated
    public Reader retrieveArticle(int articleNumber, ArticlePointer pointer) throws IOException {
        ArticleInfo ai2 = __ap2ai(pointer);
        Reader rdr = retrieveArticle(articleNumber, ai2);
        __ai2ap(ai2, pointer);
        return rdr;
    }

    @Deprecated
    public Reader retrieveArticle(int articleNumber) throws IOException {
        return retrieveArticle(articleNumber);
    }

    @Deprecated
    public Reader retrieveArticleBody(int a2, ArticlePointer ap2) throws IOException {
        ArticleInfo ai2 = __ap2ai(ap2);
        Reader rdr = retrieveArticleBody(a2, ai2);
        __ai2ap(ai2, ap2);
        return rdr;
    }

    @Deprecated
    public Reader retrieveArticle(String articleId, ArticlePointer pointer) throws IOException {
        ArticleInfo ai2 = __ap2ai(pointer);
        Reader rdr = retrieveArticle(articleId, ai2);
        __ai2ap(ai2, pointer);
        return rdr;
    }

    @Deprecated
    public Reader retrieveArticleBody(String articleId, ArticlePointer pointer) throws IOException {
        ArticleInfo ai2 = __ap2ai(pointer);
        Reader rdr = retrieveArticleBody(articleId, ai2);
        __ai2ap(ai2, pointer);
        return rdr;
    }

    @Deprecated
    public Reader retrieveArticleHeader(String articleId, ArticlePointer pointer) throws IOException {
        ArticleInfo ai2 = __ap2ai(pointer);
        Reader rdr = retrieveArticleHeader(articleId, ai2);
        __ai2ap(ai2, pointer);
        return rdr;
    }

    @Deprecated
    public boolean selectArticle(String articleId, ArticlePointer pointer) throws IOException {
        ArticleInfo ai2 = __ap2ai(pointer);
        boolean b2 = selectArticle(articleId, ai2);
        __ai2ap(ai2, pointer);
        return b2;
    }

    @Deprecated
    public boolean selectArticle(ArticlePointer pointer) throws IOException {
        ArticleInfo ai2 = __ap2ai(pointer);
        boolean b2 = selectArticle(ai2);
        __ai2ap(ai2, pointer);
        return b2;
    }

    @Deprecated
    public boolean selectNextArticle(ArticlePointer pointer) throws IOException {
        ArticleInfo ai2 = __ap2ai(pointer);
        boolean b2 = selectNextArticle(ai2);
        __ai2ap(ai2, pointer);
        return b2;
    }

    @Deprecated
    public boolean selectPreviousArticle(ArticlePointer pointer) throws IOException {
        ArticleInfo ai2 = __ap2ai(pointer);
        boolean b2 = selectPreviousArticle(ai2);
        __ai2ap(ai2, pointer);
        return b2;
    }

    private ArticleInfo __ap2ai(ArticlePointer ap2) {
        if (ap2 == null) {
            return null;
        }
        ArticleInfo ai2 = new ArticleInfo();
        return ai2;
    }

    private void __ai2ap(ArticleInfo ai2, ArticlePointer ap2) {
        if (ap2 != null) {
            ap2.articleId = ai2.articleId;
            ap2.articleNumber = (int) ai2.articleNumber;
        }
    }
}
