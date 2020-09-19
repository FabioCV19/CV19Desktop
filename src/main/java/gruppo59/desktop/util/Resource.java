package gruppo59.desktop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.stream.Stream;

public class Resource {

    private String path;

    public Resource(String path) {
        this.path = path;
    }

    public URL toURL() throws FileNotFoundException {
        URL url = getClass().getClassLoader().getResource(path);
        if (url == null)
            throw new FileNotFoundException(path);
        return url;
    }

    public File toFile() throws FileNotFoundException {
        return new File(toURL().getFile());
    }

    public InputStream toInputStream() throws FileNotFoundException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null)
            throw new FileNotFoundException(path);
        return stream;
    }

    public String readText() throws IOException {
        StringBuilder buf = new StringBuilder();
        try (Stream<String> stream = Files.lines(toFile().toPath())) {
            stream.forEach(buf::append);
        }
        return buf.toString();
    }
}
