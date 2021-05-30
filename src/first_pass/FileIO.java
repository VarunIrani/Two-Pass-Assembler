package first_pass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class FileIO {
	private HashMap<Integer, ProgramLine> program;
	public static String fileName;

	public void getProgram() {
		int lineNo = 1;
		String l;

		ProgramLine programLine;

		FileReader fileReader;
		BufferedReader reader;
		Scanner scanner = new Scanner(System.in), tokenizer;

		program = new HashMap<>();
		System.out.println(System.getProperty("user.dir"));
		System.out.print("Enter name of file: ");
		FileIO.fileName = scanner.next();

		try {
			fileReader = new FileReader(FileIO.fileName);
			reader = new BufferedReader(fileReader);

			while ((l = reader.readLine()) != null) {
				tokenizer = new Scanner(l);
				programLine = new ProgramLine();
				if (tokenizer.hasNext()) {
					programLine.set("label", tokenizer.next());
					if (tokenizer.hasNext()) {
						programLine.set("opcode", tokenizer.next().replace(",", ""));
						if (tokenizer.hasNext()) {
							programLine.set("first operand", tokenizer.next().replace(",", ""));
							if (tokenizer.hasNext()) {
								programLine.set("second operand", tokenizer.next());
							}
						}
					}

				}
				program.put(lineNo, programLine);
				lineNo++;
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

	public HashMap<Integer, ProgramLine> returnProgram() {
		return this.program;
	}
}
