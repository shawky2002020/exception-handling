import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ARXMLSorter {

    public static void SortByShortName(String fileName, String outputName){
        List<Container> containers = readContainers(fileName);
        Collections.sort(containers, new Comparator<Container>() {
            @Override
            public int compare(Container c1, Container c2) {
                return c1.getShortName().compareTo(c2.getShortName());
            }
        });
        writeContainers(containers, outputName);
    }

    private static List<Container> readContainers(String fileName) {
        List<Container> containers = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("<CONTAINER ")) {
                    int idStart = line.indexOf("UUID=\"") + 6;
                    int idEnd = line.indexOf("\"", idStart);
                    String id = line.substring(idStart, idEnd);
                    String shortName = readShortName(scanner);
                    String longName = readLongName(scanner);
                    containers.add(new Container(id, shortName, longName));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return containers;
    }

    private static String readShortName(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.startsWith("<SHORT-NAME>")) {
                int start = "<SHORT-NAME>".length();
                int end = line.indexOf("</SHORT-NAME>");
                return line.substring(start, end);
            }
        }
        return "";
    }

    private static String readLongName(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.startsWith("<LONG-NAME>")) {
                int start = "<LONG-NAME>".length();
                int end = line.indexOf("</LONG-NAME>");
                return line.substring(start, end);
            }
        }
        return "";
    }

    private static void writeContainers(List<Container> containers, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<AUTOSAR>\n");
            for (Container container : containers) {
                writer.write("    <CONTAINER UUID=\"" + container.getId() + "\">\n");
                writer.write("        <SHORT-NAME>" + container.getShortName() + "</SHORT-NAME>\n");
                writer.write("        <LONG-NAME>" + container.getLongName() + "</LONG-NAME>\n");
                writer.write("    </CONTAINER>\n");
            }
            writer.write("</AUTOSAR>\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
