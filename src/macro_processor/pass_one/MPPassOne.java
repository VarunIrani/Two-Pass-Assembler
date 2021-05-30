package macro_processor.pass_one;

import java.io.IOException;

public class MPPassOne {
	public static void main(String[] args) throws IOException {
		FileIO fileIO = new FileIO();
		fileIO.getProgram();
		FirstPass pass1 = new FirstPass(fileIO.returnProgram());
		pass1.process();
		pass1.createOutputFile();
	}
}
