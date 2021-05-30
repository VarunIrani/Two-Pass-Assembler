package macro_processor.pass_one;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class FileIO {
	private final HashMap<Integer, String> program = new HashMap<>();
	public static String fileName;

	public void getProgram() {
		int lineNo = 1;
		String l;
		FileReader fileReader;
		BufferedReader reader;
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter name of file: ");
		FileIO.fileName = scanner.next();

		try {
			fileReader = new FileReader(FileIO.fileName);
			reader = new BufferedReader(fileReader);
			while ((l = reader.readLine()) != null) {
				program.put(lineNo, l);
				lineNo += 1;
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

	public HashMap<Integer, String> returnProgram() {
		return this.program;
	}
}
