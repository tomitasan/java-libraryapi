import java.io.File;
import java.io.IOException;

public class Temp {
        public static void main(String[] args) throws IOException {

            File file = new File("N^ome*&deTs#t@.mp4");

            String name = file.getName();
            String newName = name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            file.renameTo(new File(newName));

            System.out.println("FILE NAME: " + name);
            System.out.println("FILE NAME DEPOIS DO REGEX: " + newName);
        }
    }
