import java.io.File;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java App <arxml_file>");
            return;
        }

        String inputFile = args[0];

        try {
            validateAutosarFile(inputFile);
            String outputFile = getOutputFileName(inputFile);
            ARXMLSorter.SortByShortName(inputFile, outputFile);
            System.out.println("Reordered containers written to " + outputFile);
        } catch (NotVaildAutosarFileException e) {
            System.out.println("Error: " + inputFile + " is not a valid AUTOSAR file");
        }catch (EmptyAutosarFileException e) {
            System.out.println("Error: " + inputFile + " is empty");
        }
    }
    private static void validateAutosarFile(String fileName) throws NotVaildAutosarFileException, EmptyAutosarFileException {
        if (!fileName.endsWith(".arxml")) {
            throw new NotVaildAutosarFileException();
        }

        File file = new File(fileName);
        if (file.length() == 0) {
            throw new EmptyAutosarFileException();
        }
    }

    private static String getOutputFileName(String inputFile) {
        int index = inputFile.lastIndexOf('.');
        return inputFile.substring(0, index) + "_mod.arxml";
    }
}




    

    
