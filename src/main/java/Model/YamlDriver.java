package Model;

import java.io.*;
import java.net.URL;

import org.yaml.snakeyaml.Yaml;
import org.apache.commons.io.FilenameUtils;

public class YamlDriver {

    private static Configuration conf;

    public static Configuration getConfiguration() {
        return conf;
    }

    public static void setConfiguration(File database) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream in;

        in = new FileInputStream("configurations/"+FilenameUtils.removeExtension(database.getName())+".yml");

        conf = yaml.loadAs(in, Configuration.class);
    }
}
