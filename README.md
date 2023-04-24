Lab6
The project uses Document and DocumentBuilder to read the arxml file, and extract its contents into a NodeList. Each container inside the root is a node in the DOM. The list of nodes can be sorted using a sort function which implements Comparator.

After sorting the nodes, a new document is created with the root. Afterwards, the sorted nodes are appended to the DOM and the file is written with the new modified name.

How to run:
Compile the program with javac Main.java

Run the program and specify the arxml file name as an argument: java Main lab.arxml

Test cases
The program includes exception handling for empty files and invalid arxml files. All 3 cases can be tested by running tests.bat.
