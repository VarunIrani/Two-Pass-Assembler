package second_pass;

public class AssemblerPassTwo {
	public static void main(String[] args) {
		FileIO fileIO = new FileIO();
		fileIO.getProgram();
		SecondPass secondPass = new SecondPass();
		secondPass.process(fileIO.returnProgram());
		secondPass.getMachineCode();
		secondPass.print();
	}
}
