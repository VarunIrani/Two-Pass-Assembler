package second_pass;

public class MachineCode {
	private String opcode, registerOperand, memoryOperand;

	public MachineCode() {
		opcode = registerOperand = "00";
		memoryOperand = "000";
	}

	@Override
	public String toString() {
		return String.format("%s %s %s\n", opcode, registerOperand, memoryOperand);
	}

	public String returnNoMachineCode() {
		return "-\n";
	}

	public MachineCode(String opcode, String registerOperand, String memoryOperand) {
		this.setOpcode(opcode);
		this.setRegisterOperand(registerOperand);
		this.setMemoryOperand(memoryOperand);
	}

	public String getMemoryOperand() {
		return memoryOperand;
	}

	public void setMemoryOperand(String memoryOperand) {
		this.memoryOperand = memoryOperand;
	}

	public String getRegisterOperand() {
		return registerOperand;
	}

	public void setRegisterOperand(String registerOperand) {
		this.registerOperand = registerOperand;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

}
