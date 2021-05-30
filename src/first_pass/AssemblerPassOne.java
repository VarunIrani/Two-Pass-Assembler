package first_pass;

import java.io.IOException;

public class AssemblerPassOne {
	public static void main(String[] args) throws IOException {
		FileIO fileIO = new FileIO();
		fileIO.getProgram();
		FirstPass pass1 = new FirstPass();
		pass1.process(fileIO.returnProgram());
		pass1.getIntermediateCode();
		pass1.print();
	}
}
