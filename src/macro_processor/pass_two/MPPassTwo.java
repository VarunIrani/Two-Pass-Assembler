package macro_processor.pass_two;

import java.io.IOException;

public class MPPassTwo {
	public static void main(String[] args) throws IOException {
		FileIO fileIO = new FileIO();
		fileIO.getPass1Data();
		SecondPass pass2 = new SecondPass(
				fileIO.returnProgram(),
				fileIO.returnMNT(),
				fileIO.returnMDT(),
				fileIO.returnALATables()
		);
		pass2.process();
		pass2.createOutputFile();
	}
}
