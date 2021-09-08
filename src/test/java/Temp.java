import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Temp {
        public static void main(String[] args) throws IOException {
//
//            File file = new File("N^ome*&deTs#t@.mp4");
//
//            String name = file.getName();
//            String newName = name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
//            file.renameTo(new File(newName));
//
//            System.out.println("FILE NAME: " + name);
//            System.out.println("FILE NAME DEPOIS DO REGEX: " + newName);


            // your date/time, in -03:00 offset
            String strDate = "2021-09-08T17:30:00.000-03:00";
            OffsetDateTime odt = OffsetDateTime.parse(strDate);

            // formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            // convert to UTC
            String utc = formatter.format(odt.withOffsetSameInstant(ZoneOffset.UTC));
            System.out.println(utc); // 2017-06-20T00:50:51.000Z
        }
 }
