package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/TransformInputOutput.class */
public abstract class TransformInputOutput {
    private static URI currentJavaWorkingDirectory = new File(System.getProperty("user.dir")).toURI();

    public abstract void parse(InputStream inputStream, OutputStream outputStream) throws Exception;

    public void parse(String[] args) throws Exception {
        InputStream in;
        OutputStream out;
        if (args.length == 0) {
            in = new BufferedInputStream(System.in);
            out = new BufferedOutputStream(System.out);
        } else if (args.length == 1) {
            in = new BufferedInputStream(new FileInputStream(args[0]));
            out = new BufferedOutputStream(System.out);
        } else if (args.length == 2) {
            in = new BufferedInputStream(new FileInputStream(args[0]));
            out = new BufferedOutputStream(new FileOutputStream(args[1]));
        } else {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.optinalFileNotSpecified"));
        }
        parse(in, out);
    }

    public void parse(InputStream in, OutputStream out, String workingDirectory) throws Exception {
        throw new UnsupportedOperationException();
    }

    protected static EntityResolver createRelativePathResolver(final String workingDirectory) {
        return new EntityResolver() { // from class: com.sun.xml.internal.fastinfoset.tools.TransformInputOutput.1
            @Override // org.xml.sax.EntityResolver
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                if (systemId != null && systemId.startsWith("file:/")) {
                    URI workingDirectoryURI = new File(workingDirectory).toURI();
                    try {
                        URI workingFile = TransformInputOutput.convertToNewWorkingDirectory(TransformInputOutput.currentJavaWorkingDirectory, workingDirectoryURI, new File(new URI(systemId)).toURI());
                        return new InputSource(workingFile.toString());
                    } catch (URISyntaxException e2) {
                        return null;
                    }
                }
                return null;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static URI convertToNewWorkingDirectory(URI oldwd, URI newwd, URI file) throws URISyntaxException, IOException {
        String oldwdStr = oldwd.toString();
        String newwdStr = newwd.toString();
        String fileStr = file.toString();
        if (fileStr.startsWith(oldwdStr)) {
            String cmpStr = fileStr.substring(oldwdStr.length());
            if (cmpStr.indexOf(47) == -1) {
                return new URI(newwdStr + '/' + cmpStr);
            }
        }
        String[] oldwdSplit = oldwdStr.split("/");
        String[] newwdSplit = newwdStr.split("/");
        String[] fileSplit = fileStr.split("/");
        int diff = 0;
        while (diff < oldwdSplit.length && diff < fileSplit.length && oldwdSplit[diff].equals(fileSplit[diff])) {
            diff++;
        }
        int diffNew = 0;
        while (diffNew < newwdSplit.length && diffNew < fileSplit.length && newwdSplit[diffNew].equals(fileSplit[diffNew])) {
            diffNew++;
        }
        if (diffNew > diff) {
            return file;
        }
        int elemsToSub = oldwdSplit.length - diff;
        StringBuffer resultStr = new StringBuffer(100);
        for (int i2 = 0; i2 < newwdSplit.length - elemsToSub; i2++) {
            resultStr.append(newwdSplit[i2]);
            resultStr.append('/');
        }
        for (int i3 = diff; i3 < fileSplit.length; i3++) {
            resultStr.append(fileSplit[i3]);
            if (i3 < fileSplit.length - 1) {
                resultStr.append('/');
            }
        }
        return new URI(resultStr.toString());
    }
}
