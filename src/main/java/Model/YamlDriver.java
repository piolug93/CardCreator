package Model;

import java.io.*;
import java.net.URL;

import org.yaml.snakeyaml.Yaml;
import org.apache.commons.io.FilenameUtils;

public class YamlDriver {

    public static Configuration getConfiguration(File database) throws FileNotFoundException{
        Yaml yaml = new Yaml();
        String inResources = getConfURL(database.getName());
        InputStream in;

        if(inResources != null)
            in = new FileInputStream(getConfURL(FilenameUtils.getBaseName(database.getName())));
        else
            in = new FileInputStream(FilenameUtils.removeExtension(database.getPath())+".yml");

        return yaml.loadAs(in, Configuration.class);
    }

    private static String getConfURL(String name) {
        URL u = YamlDriver.class.getClassLoader().getResource("configurations/"+name+".yml");
        if(u != null)
            return u.getPath();
        else
            return null;
    }
}
