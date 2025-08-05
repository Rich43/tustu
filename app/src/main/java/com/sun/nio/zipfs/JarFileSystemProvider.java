package com.sun.nio.zipfs;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.icepdf.core.util.PdfOps;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/JarFileSystemProvider.class */
public class JarFileSystemProvider extends ZipFileSystemProvider {
    @Override // com.sun.nio.zipfs.ZipFileSystemProvider, java.nio.file.spi.FileSystemProvider
    public String getScheme() {
        return "jar";
    }

    @Override // com.sun.nio.zipfs.ZipFileSystemProvider
    protected Path uriToPath(URI uri) {
        String scheme = uri.getScheme();
        if (scheme == null || !scheme.equalsIgnoreCase(getScheme())) {
            throw new IllegalArgumentException("URI scheme is not '" + getScheme() + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        try {
            String string = uri.toString();
            int iIndexOf = string.indexOf("!/");
            URI uri2 = new URI(string.substring(4, iIndexOf == -1 ? string.length() : iIndexOf));
            return Paths.get(new URI(DeploymentDescriptorParser.ATTR_FILE, uri2.getHost(), uri2.getPath(), null)).toAbsolutePath();
        } catch (URISyntaxException e2) {
            throw new AssertionError(e2);
        }
    }

    @Override // com.sun.nio.zipfs.ZipFileSystemProvider, java.nio.file.spi.FileSystemProvider
    public Path getPath(URI uri) {
        String string;
        int iIndexOf;
        FileSystem fileSystem = getFileSystem(uri);
        String fragment = uri.getFragment();
        if (fragment == null && (iIndexOf = (string = uri.toString()).indexOf("!/")) != -1) {
            fragment = string.substring(iIndexOf + 2);
        }
        if (fragment != null) {
            return fileSystem.getPath(fragment, new String[0]);
        }
        throw new IllegalArgumentException("URI: " + ((Object) uri) + " does not contain path fragment ex. jar:///c:/foo.zip!/BAR");
    }
}
