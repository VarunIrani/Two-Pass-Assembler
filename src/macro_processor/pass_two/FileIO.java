package macro_processor.pass_two;

import macro_processor.ALAEntry;
import macro_processor.MDTEntry;
import macro_processor.MNTEntry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class FileIO {
	private final HashMap<Integer, String> program = new HashMap<>();
	private final HashMap<Integer, MNTEntry> macroNameTable = new HashMap<>();
	private final HashMap<Integer, MDTEntry> macroDefinitionTable = new HashMap<>();
	private final HashMap<Integer, HashMap<Integer, ALAEntry>> allALA = new HashMap<>();
	public static String fileName;
	int lineNo = 1;
	BufferedReader reader;
	String l;
	FileReader fileReader;


	public void getPass1Data() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter name of original source file: ");
		FileIO.fileName = scanner.next();
		try {
//		Read output code of Pass 1
			readOutputPass1();
//		Read MNT of Pass 1
			readMNT();
//		Read MDT of Pass 1
			readMDT();
//		Read All ALAs
			readAllALA();
		} catch (FileNotFoundException e) {
			System.out.println(
					"Error: " + FileIO.fileName + " not found. File is either missing or not present in default directory.");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		scanner.close();
	}

	private void readOutputPass1() throws IOException {
		String fileNameWithoutExtension = FileIO.fileName.split("\\.", 2)[0];
		fileReader = new FileReader(fileNameWithoutExtension + "_output_1.txt");
		reader = new BufferedReader(fileReader);
		while ((l = reader.readLine()) != null) {
			program.put(lineNo, l);
			lineNo += 1;
		}
		fileReader.close();
		reader.close();
	}

	private void readMNT() throws IOException {
		String fileNameWithoutExtension = FileIO.fileName.split("\\.", 2)[0];
		fileReader = new FileReader(fileNameWithoutExtension + "_mnt_1.txt");
		reader = new BufferedReader(fileReader);
		while ((l = reader.readLine()) != null) {
			String[] splitMNTEntry = l.split("\\s", 3);
			int index = Integer.parseInt(splitMNTEntry[0]);
			String macroName = splitMNTEntry[1];
			int mdtIndex = Integer.parseInt(splitMNTEntry[2]);
			macroNameTable.put(index, new MNTEntry(macroName, mdtIndex));
		}
		fileReader.close();
		reader.close();
	}

	private void readMDT() throws IOException {
		String fileNameWithoutExtension = FileIO.fileName.split("\\.", 2)[0];
		fileReader = new FileReader(fileNameWithoutExtension + "_mdt_1.txt");
		reader = new BufferedReader(fileReader);
		while ((l = reader.readLine()) != null) {
			String[] splitMDTEntry = l.split("\\s", 2);
			int index = Integer.parseInt(splitMDTEntry[0]);
			String definition = splitMDTEntry[1];
			macroDefinitionTable.put(index, new MDTEntry(definition));
		}
		fileReader.close();
		reader.close();
	}


	private void readAllALA() throws IOException {
		String fileNameWithoutExtension = FileIO.fileName.split("\\.", 2)[0];
		for (int i = 0; i < macroNameTable.size(); i++) {
			fileReader = new FileReader(fileNameWithoutExtension + "_ala_" + i +  "_1.txt");
			reader = new BufferedReader(fileReader);
			HashMap<Integer ,ALAEntry> macroALA = new HashMap<>();
			while ((l = reader.readLine()) != null) {
				String[] splitALAEntry = l.split("\\s", 3);
				int index = Integer.parseInt(splitALAEntry[0]);
				String formalParameter = splitALAEntry[1];
				String actualParameter = splitALAEntry[2];
				macroALA.put(index, new ALAEntry(formalParameter, actualParameter));
			}
			allALA.put(i, macroALA);
			fileReader.close();
			reader.close();
		}
	}


	public HashMap<Integer, String> returnProgram() {
		return program;
	}

	public HashMap<Integer, MNTEntry> returnMNT() {
		return macroNameTable;
	}

	public HashMap<Integer, MDTEntry> returnMDT() {
		return macroDefinitionTable;
	}

	public HashMap<Integer, HashMap<Integer, ALAEntry>> returnALATables() {
		return allALA;
	}
}
