package com.elmakers.test;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParseTest {
    public static void main(String[] args) {
        System.out.println("Reading duplicates.yml");

        try {
            InputStream in = ParseTest.class.getResourceAsStream("/duplicates.yml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Set<String> keys = new HashSet<String>();
            StringBuilder builder = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                builder.append(line);
                builder.append("\n");
                if (line.startsWith("#") || line.length() == 0 || line.startsWith(" ")) continue;
                int colonIndex = line.indexOf(':');
                if (colonIndex == -1) continue;
                line = line.substring(0, colonIndex);
                if (keys.contains(line)) {
                    System.out.println(" Found duplicate key: " + line);
                }
                keys.add(line);
            }
            reader.close();

            Yaml yaml = new Yaml();
            Map<?, ?> parsed = (Map<?, ?>)yaml.load(builder.toString());
            System.out.println("Loaded " + parsed.size() + " Yaml nodes and parsed " + keys.size() + " unique keys.");
            if (parsed == null) {
                System.out.println("Error reading file, null returned.");
            } else {
                for (String key : keys) {
                    //System.out.println(key + ": {}");
                    if (!parsed.containsKey(key)) {
                        System.out.println("*** Expected key '" + key + "' not found in loaded map.");
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("An error occurred reading files: " + ex.getClass().getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Done.\n");
    }
}
