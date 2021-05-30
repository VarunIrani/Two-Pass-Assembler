package second_pass;

import java.util.ArrayList;
import java.util.HashMap;

enum MnemonicClass {
	AD("AD"), DL("DL"), IS("IS");

	public String value;

	MnemonicClass(String value) {
		this.value = value;
	}
}

enum MemoryOperandType {
	Literal("L"), Symbol("S"), Constant("C");

	public String value;

	MemoryOperandType(String value) {
		this.value = value;
	}
}

public class Utils {
	public static boolean checkIfMemoryOperand(String operand) {
		return (operand.contains("C") || operand.contains("L") || operand.contains("S"));
	}

	public static String evaluateInstruction(String instruction) {
		String machineCode = "";
		String mnemonicClass = instruction.split("[,]")[0].replace("(", "");
		int mnemonicCode = Integer.parseInt(instruction.split("[,]")[1].replace(")", ""));
		if (mnemonicClass.equalsIgnoreCase(MnemonicClass.AD.value)) {
			if (mnemonicCode < 5)
				machineCode = "-";
			else
				machineCode = "00";
		} else if (mnemonicClass.equalsIgnoreCase(MnemonicClass.DL.value)) {
			if (mnemonicCode < 2)
				machineCode = "-";
			else
				machineCode = "00";
		} else if (mnemonicClass.equalsIgnoreCase(MnemonicClass.IS.value)) {
			machineCode = Integer.toString(mnemonicCode);
		}
		return machineCode;
	}

	public static String evaluateRegisterOperand(String register) {
		return register.split("[,]")[1].replace(")", "");
	}

	public static String evaluateMemoryOperand(String memoryOperand, HashMap<Integer, Literal> literalTable,
			HashMap<String, ArrayList<Integer>> symbolTable) {
		// System.out.println(memoryOperand);
		String machineCode = "";
		String mnemonicClass = memoryOperand.split("[,]")[0].replace("(", "");
		Integer index = Integer.parseInt(memoryOperand.split("[,]")[1].replace(")", ""));
		if (mnemonicClass.equalsIgnoreCase(MemoryOperandType.Literal.value)) {
			machineCode = Integer.toString(literalTable.get(index).getAddress());
		} else if (mnemonicClass.equalsIgnoreCase(MemoryOperandType.Symbol.value)) {
			for (ArrayList<Integer> value : symbolTable.values()) {
				if (value.get(0).equals(index)) {
					machineCode = Integer.toString(value.get(1));
					break;
				}
			}
		} else if (mnemonicClass.equalsIgnoreCase(MemoryOperandType.Constant.value)) {
			machineCode = Integer.toString(index);
		}
		return machineCode;
	}
}
