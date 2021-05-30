package first_pass;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FirstPass {
	HashMap<Integer, ProgramLine> program;
	HashMap<String, ArrayList<Integer>> symbolTable = new HashMap<>();
	HashMap<Integer, Literal> literalTable = new HashMap<>();
	ArrayList<Integer> poolTable = new ArrayList<>();

	Integer locationCounter = 0;
	Integer symbolIndex = 0;
	Integer instructionSize = 1;
	StringBuffer intermediateCode = new StringBuffer();

	Mnemonic mnemonic = new Mnemonic();
	Integer address, offset;


	void process(HashMap<Integer, ProgramLine> program) {
		this.program = program;
		normalize();
		for (ProgramLine statement : program.values()) {
			System.out.println(statement.get("label") + "\t" + statement.get("opcode") + "\t" + statement.get("first operand")
					+ "\t" + statement.get("second operand"));
		}
	}

	void normalize() {
		for (ProgramLine statement : program.values()) {
			// For statements without label
			if (mnemonic.checkOpcode(statement.get("label").toLowerCase())) {
				statement.set("second operand", statement.get("first operand"));
				statement.set("first operand", statement.get("opcode"));
				statement.set("opcode", statement.get("label"));
				statement.set("label", "");
			}
			// Set opcode only if it is one
			if (mnemonic.checkOpcode(statement.get("opcode").toLowerCase()))
				statement.set("opcode", statement.get("opcode").toLowerCase());
			if (mnemonic.checkRegister(statement.get("first operand").toLowerCase()))
				statement.set("first operand", statement.get("first operand").toLowerCase());
			if (mnemonic.checkRegister(statement.get("second operand").toLowerCase()))
				statement.set("second operand", statement.get("second operand").toLowerCase());
		}
	}

	void processLiterals(String opcode) {
		if (opcode.equalsIgnoreCase("END")) {
			intermediateCode.append(locationCounter).append(" ").append(mnemonic.getMnemonic(opcode).getIntermediateCode()).append("\n");
		}
		for (int i = poolTable.get(poolTable.size() - 1); i < literalTable.size(); i++) {
			System.out.println(locationCounter);

			Literal literal = literalTable.get(i);
			if (opcode.equalsIgnoreCase("LTORG")) {
				intermediateCode.append(locationCounter).append(" ").append(mnemonic.getMnemonic(opcode).getIntermediateCode()).append("(C,").append(Integer.parseInt(literal.getLiteral())).append(")").append("\n");
			}
			if (literal == null)
				return;
			literal.setAddress(locationCounter);
			locationCounter += instructionSize;
		}
		if (poolTable.get(poolTable.size() - 1) < literalTable.size())
			poolTable.add(literalTable.size());
	}

	public void getIntermediateCode() throws IOException {
		poolTable.add(0);
		for (ProgramLine statement : program.values()) {
			if (!statement.get("opcode").equalsIgnoreCase("END")) {
				// If label is present then
				if (statement.get("label").length() != 0) {
					ArrayList<Integer> tempSym = new ArrayList<>();
					if (symbolTable.containsKey(statement.get("label"))) {
						tempSym.add(symbolTable.get(statement.get("label")).get(0));
						tempSym.add(locationCounter);
						symbolTable.replace(statement.get("label"), tempSym);
					} else {
						tempSym.add(symbolIndex);
						tempSym.add(locationCounter);
						symbolTable.put(statement.get("label"), tempSym);
						symbolIndex += 1;
					}
				}
				if (statement.get("opcode").equalsIgnoreCase("LTORG")) {
					// LTORG
					processLiterals(statement.get("opcode"));
					System.out.println(locationCounter);
				} else if (statement.get("opcode").equalsIgnoreCase("START")) {
					// START
					intermediateCode.append("-1 ").append(mnemonic.getMnemonic(statement.get("opcode")).getIntermediateCode()).append("(C,").append(statement.get("first operand")).append(")").append("\n");
					locationCounter = Integer.parseInt(statement.get("first operand"));
				} else if (statement.get("opcode").equalsIgnoreCase("ORIGIN")) {
					// ORIGIN
					setEQUAddressAndOffset(statement);
					intermediateCode.append(locationCounter).append(" ").append(mnemonic.getMnemonic(statement.get("opcode")).getIntermediateCode()).append("(C,").append(address + offset).append(")").append("\n");
					locationCounter = address + offset;

				} else if (statement.get("opcode").equalsIgnoreCase("EQU")) {
					// EQU
					String symbolToBeAssigned = statement.get("label");
					setEQUAddressAndOffset(statement);
					ArrayList<Integer> tempSym = new ArrayList<>();
					if (symbolTable.containsKey(symbolToBeAssigned)) {
						tempSym.add(symbolTable.get(symbolToBeAssigned).get(0));
						tempSym.add(address + offset);
						symbolTable.replace(symbolToBeAssigned, tempSym);
					} else {
						tempSym.add(symbolIndex);
						tempSym.add(address + offset);
						symbolTable.put(symbolToBeAssigned, tempSym);
						symbolIndex += 1;
					}
					intermediateCode.append(locationCounter).append(" ").append(mnemonic.getMnemonic(statement.get("opcode")).getIntermediateCode()).append("(C,").append(address + offset).append(")").append("\n");
					locationCounter += instructionSize;

				} else if (statement.get("opcode").equalsIgnoreCase("DS")) {
					// DS
					intermediateCode.append(locationCounter).append(" ").append(mnemonic.getMnemonic(statement.get("opcode")).getIntermediateCode()).append("\n");
					String symbolToBeAssigned = statement.get("label");
					ArrayList<Integer> tempSym = new ArrayList<>();
					if (symbolTable.containsKey(symbolToBeAssigned)) {
						tempSym.add(symbolTable.get(symbolToBeAssigned).get(0));
						tempSym.add(locationCounter);
						symbolTable.replace(symbolToBeAssigned, tempSym);
					} else {
						tempSym.add(symbolIndex);
						tempSym.add(locationCounter);
						symbolTable.put(symbolToBeAssigned, tempSym);
						symbolIndex += 1;
					}
					locationCounter += Integer.parseInt(statement.get("first operand"));
				} else if (statement.get("opcode").equalsIgnoreCase("DC")) {
					// DC
					intermediateCode.append(locationCounter).append(" ").append(mnemonic.getMnemonic(statement.get("opcode")).getIntermediateCode());
					String firstOperand = statement.get("first operand");
					if (firstOperand.contains("'")) {
						literalTable.put(literalTable.size(), new Literal(firstOperand, -1));
						intermediateCode.append("(L,").append(literalTable.size() - 1).append(")").append("\n");
					} else {
						intermediateCode.append("(C,").append(firstOperand).append(")").append("\n");
					}
					locationCounter += instructionSize;
				} else {
					// Imperative Statements
					intermediateCode.append(locationCounter).append(" ").append(mnemonic.getMnemonic(statement.get("opcode")).getIntermediateCode());
					String firstOperand = statement.get("first operand");
					String secondOperand = statement.get("second operand");
					if (firstOperand.length() != 0) {
						if (mnemonic.checkRegister(firstOperand)) {
							intermediateCode.append("(RG,").append(mnemonic.getRegister(firstOperand)).append(") ");
						} else {
							addToSymbolTable(firstOperand);
						}
					}
					if (secondOperand.length() != 0) {
						if (secondOperand.contains("'")) {
							literalTable.put(literalTable.size(), new Literal(secondOperand, -1));
							intermediateCode.append("(L,").append(literalTable.size() - 1).append(") ");
						} else {
							if (secondOperand.matches("[a-zA-Z]")) {
								addToSymbolTable(secondOperand);
							} else {
								intermediateCode.append("(C,").append(secondOperand).append(")");
							}
						}
					}
					intermediateCode.append("\n");
					locationCounter += instructionSize;
				}
			} else {
				processLiterals(statement.get("opcode"));
			}
		}
		writeFiles();
	}

	private void setEQUAddressAndOffset(ProgramLine statement) {
		if (statement.get("first operand").contains("+")) {
			String firstOperandSymbol = statement.get("first operand").split("[+]")[0];
			address = symbolTable.get(firstOperandSymbol).get(1);
			offset = Integer.parseInt(statement.get("first operand").split("[+]")[1]);
		} else {
			address = Integer.parseInt(statement.get("first operand"));
			offset = 0;
		}
	}

	private void addToSymbolTable(String operand) {
		ArrayList<Integer> tempSym = new ArrayList<>();
		if (!symbolTable.containsKey(operand)) {
			tempSym.add(symbolIndex);
			symbolTable.put(operand, tempSym);
			symbolIndex += 1;
		}
		intermediateCode.append("(S,").append(symbolTable.get(operand).get(0)).append(") ");
	}

	private void writeFiles() throws IOException {
		String fileName = FileIO.fileName.split("[.]")[0];
		FileWriter writer = new FileWriter(fileName + "_inter.txt");
		writer.write(intermediateCode.toString());
		writer.close();

		writer = new FileWriter(fileName + "_pool.txt");
		for (Integer poolTableEntry : poolTable) {
			writer.write(poolTableEntry + "\n");
		}
		writer.close();

		writer = new FileWriter(fileName + "_lit.txt");
		for (Literal literal : literalTable.values()) {
			writer.write("'" + literal.getLiteral() + "'" + " " + literal.getAddress() + "\n");
		}
		writer.close();

		writer = new FileWriter(fileName + "_sym.txt");
		for (String symbol : symbolTable.keySet()) {
			Integer address = symbolTable.get(symbol).get(1);
			writer.write(symbolTable.get(symbol).get(0) + " " + symbol + " " + address + "\n");
		}
		writer.close();
	}

	public void print() {
		Literal temp_lit;
		System.out.println("\nSymbol Table:\n");
		for (String symbol : symbolTable.keySet()) {
			System.out.println(symbol + " " + symbolTable.get(symbol).get(1));
		}
		System.out.println("\nLiteral Table:\n");
		for (int i : literalTable.keySet()) {
			temp_lit = literalTable.get(i);
			System.out.println("'" + temp_lit.getLiteral() + "'" + " " + temp_lit.getAddress());
		}
		System.out.println("\nPool Table:\n");
		for (Integer poolTableEntry : poolTable) {
			System.out.println("#" + poolTableEntry);
		}
	}

}
