package second_pass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class SecondPass {
	TreeMap<Integer, IntermediateCode> program;
	HashMap<String, ArrayList<Integer>> symbolTable = new HashMap<>();
	HashMap<Integer, Literal> literalTable = new HashMap<>();

	public void process(TreeMap<Integer, IntermediateCode> program) {
		this.program = program;
		this.normalize();
		for (IntermediateCode statement : program.values()) {
			System.out.println(statement);
		}
		this.readFiles();
	}

	private void normalize() {
		for (IntermediateCode statement : program.values()) {
			// Is there a register operand
			if (statement.getRegisterOperand().length() > 0) {
				// For statements without register operand
				if (Utils.checkIfMemoryOperand(statement.getRegisterOperand())) {
					statement.setMemoryOperand(statement.getRegisterOperand());
					statement.setRegisterOperand("");
				}
			}
		}
	}

	private void readFiles() {
		String l;
		String fileName = FileIO.fileName.split("[.]")[0].split("[_]")[0];
		Scanner tokenizer = new Scanner(System.in);
		try {
			// * Read Literal Table
			FileReader fileReader = new FileReader(fileName + "_lit.txt");
			BufferedReader reader = new BufferedReader(fileReader);
			int i = 0;
			while ((l = reader.readLine()) != null) {
				tokenizer = new Scanner(l);
				Literal literal = new Literal(tokenizer.next(), tokenizer.nextInt());
				literalTable.put(i, literal);
				i += 1;
			}
			reader.close();

			// * Read Symbol Table
			fileReader = new FileReader(fileName + "_sym.txt");
			reader = new BufferedReader(fileReader);
			while ((l = reader.readLine()) != null) {
				tokenizer = new Scanner(l);
				Integer index = tokenizer.nextInt();
				String symbol = tokenizer.next();
				ArrayList<Integer> list = new ArrayList<>();
				list.add(index);
				list.add(tokenizer.nextInt());
				symbolTable.put(symbol, list);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out
					.println("Error: " + fileName + " not found. File is either missing or not present in default directory.");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tokenizer.close();
	}

	public void getMachineCode() {
		MachineCode machineCode;
		String fileName = FileIO.fileName.split("[.]")[0].split("[_]")[0];
		FileWriter writer;
		try {
			writer = new FileWriter(fileName + "_machine.txt");
			for (IntermediateCode statement : program.values()) {
				machineCode = new MachineCode();
				machineCode.setOpcode(Utils.evaluateInstruction(statement.getInstruction()));
				if (statement.getRegisterOperand().length() > 0) {
					machineCode.setRegisterOperand(Utils.evaluateRegisterOperand(statement.getRegisterOperand()));
				}
				if (statement.getMemoryOperand().length() > 0) {
					machineCode
							.setMemoryOperand(Utils.evaluateMemoryOperand(statement.getMemoryOperand(), literalTable, symbolTable));
				}
				if (machineCode.getOpcode().contains("-")) {
					System.out.print(machineCode.returnNoMachineCode());
					writer.write(machineCode.returnNoMachineCode());
				} else {
					System.out.print(machineCode);
					writer.write(machineCode.toString());
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void print() {
		Literal temp_lit;
		System.out.println("\nSymbol Table:\n");
		for (String symbol : symbolTable.keySet()) {
			System.out.println(symbolTable.get(symbol).get(0) + " " + symbol + " " + symbolTable.get(symbol).get(1));
		}
		System.out.println("\nLiteral Table:\n");
		for (int i : literalTable.keySet()) {
			temp_lit = literalTable.get(i);
			System.out.println(i + " " + "'" + temp_lit.getLiteral() + "'" + " " + temp_lit.getAddress());
		}
	}

}
