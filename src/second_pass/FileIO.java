package second_pass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public class FileIO {
	private TreeMap<Integer, IntermediateCode> program;
	public static String fileName;

	public void getProgram() {
		String l;

		IntermediateCode programLine;

		FileReader fileReader;
		BufferedReader reader;
		Scanner scanner = new Scanner(System.in), tokenizer;

		program = new TreeMap<>();

		System.out.print("Enter name of intermediate code file: ");
		FileIO.fileName = scanner.next();

		try {
			fileReader = new FileReader(FileIO.fileName);
			reader = new BufferedReader(fileReader);

			while ((l = reader.readLine()) != null) {
				tokenizer = new Scanner(l);
				programLine = new IntermediateCode();
				if (tokenizer.hasNextInt()) {
					programLine.setLineCounter(tokenizer.nextInt());
					if (tokenizer.hasNext()) {
						programLine.setInstruction(tokenizer.next());
						if (tokenizer.hasNext()) {
							programLine.setRegisterOperand(tokenizer.next());
							if (tokenizer.hasNext()) {
								programLine.setMemoryOperand(tokenizer.next());
							}
						}
					}
				}
				program.put(programLine.getLineCounter(), programLine);

			}
		} catch (FileNotFoundException e) {
			System.out.println(
					"Error: " + FileIO.fileName + " not found. File is either missing or not present in default directory.");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		scanner.close();

	}

	public TreeMap<Integer, IntermediateCode> returnProgram() {
		return this.program;
	}
}
