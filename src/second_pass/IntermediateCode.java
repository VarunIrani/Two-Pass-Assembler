package second_pass;

public class IntermediateCode {
	private String instruction, registerOperand, memoryOperand;
	private Integer lineCounter;

	public String getInstruction() {
		return instruction;
	}

	public Integer getLineCounter() {
		return lineCounter;
	}

	public void setLineCounter(Integer lineCounter) {
		this.lineCounter = lineCounter;
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

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public IntermediateCode(String instruction, String registerOperand, String memoryOperand) {
		this.instruction = instruction;
		this.registerOperand = registerOperand;
		this.memoryOperand = memoryOperand;
	}

	public IntermediateCode() {
		lineCounter = -1;
		instruction = registerOperand = memoryOperand = "";
	}

	@Override
	public String toString() {
		return String.format("%d\t%s\t%s\t%s", lineCounter, instruction, registerOperand, memoryOperand);
	}
}
