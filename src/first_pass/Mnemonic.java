package first_pass;

import java.util.HashMap;

enum MnemonicClass {
	AD("AD"), DL("DL"), IS("IS"), AREG("AREG"), BREG("BREG"), CREG("CREG"), DREG("DREG");

	public String value;

	MnemonicClass(String value) {
		this.value = value;
	}
}

public class Mnemonic {
	HashMap<String, Opcode> MachineOpcodeTable = new HashMap<>();
	HashMap<String, String> RegisterMap = new HashMap<>();

	Mnemonic() {
		// AD
		Opcode AD = new Opcode(MnemonicClass.AD, "01");
		MachineOpcodeTable.put("start", AD);
		AD = new Opcode(MnemonicClass.AD, "02");
		MachineOpcodeTable.put("end", AD);
		AD = new Opcode(MnemonicClass.AD, "03");
		MachineOpcodeTable.put("origin", AD);
		AD = new Opcode(MnemonicClass.AD, "04");
		MachineOpcodeTable.put("equ", AD);
		AD = new Opcode(MnemonicClass.AD, "05");
		MachineOpcodeTable.put("ltorg", AD);
		// DL
		Opcode DL = new Opcode(MnemonicClass.DL, "01");
		MachineOpcodeTable.put("ds", DL);
		DL = new Opcode(MnemonicClass.DL, "02");
		MachineOpcodeTable.put("dc", DL);
		// IS
		Opcode IS = new Opcode(MnemonicClass.IS, "00");
		MachineOpcodeTable.put("stop", IS);
		IS = new Opcode(MnemonicClass.IS, "01");
		MachineOpcodeTable.put("add", IS);
		IS = new Opcode(MnemonicClass.IS, "02");
		MachineOpcodeTable.put("sub", IS);
		IS = new Opcode(MnemonicClass.IS, "03");
		MachineOpcodeTable.put("mult", IS);
		IS = new Opcode(MnemonicClass.IS, "04");
		MachineOpcodeTable.put("mover", IS);
		IS = new Opcode(MnemonicClass.IS, "05");
		MachineOpcodeTable.put("movem", IS);
		IS = new Opcode(MnemonicClass.IS, "06");
		MachineOpcodeTable.put("comp", IS);
		IS = new Opcode(MnemonicClass.IS, "07");
		MachineOpcodeTable.put("bc", IS);
		IS = new Opcode(MnemonicClass.IS, "08");
		MachineOpcodeTable.put("div", IS);
		IS = new Opcode(MnemonicClass.IS, "09");
		MachineOpcodeTable.put("read", IS);
		IS = new Opcode(MnemonicClass.IS, "10");
		MachineOpcodeTable.put("print", IS);
		// Registers
		RegisterMap.put(MnemonicClass.AREG.value, "01");
		RegisterMap.put(MnemonicClass.BREG.value, "02");
		RegisterMap.put(MnemonicClass.CREG.value, "03");
		RegisterMap.put(MnemonicClass.DREG.value, "04");
	}

	public Opcode getMnemonic(String key) {
		return MachineOpcodeTable.getOrDefault(key, null);
	}

	public boolean checkOpcode(String key) {
		return MachineOpcodeTable.containsKey(key);
	}

	public boolean checkRegister(String key) {
		return RegisterMap.containsKey(key);
	}

	public String getRegister(String key) {
		return RegisterMap.getOrDefault(key, null);
	}
}
