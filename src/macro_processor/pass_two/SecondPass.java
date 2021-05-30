package macro_processor.pass_two;

import macro_processor.ALAEntry;
import macro_processor.MDTEntry;
import macro_processor.MNTEntry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SecondPass {

	private final HashMap<Integer, String> program;
	private final HashMap<Integer, MNTEntry> macroNameTable;
	private final HashMap<Integer, MDTEntry> macroDefinitionTable;
	private final HashMap<Integer, HashMap<Integer, ALAEntry>> allALA;

	final StringBuilder outputCode = new StringBuilder();
	int currentLineNumber = 1;

	public SecondPass(HashMap<Integer, String> program, HashMap<Integer, MNTEntry> macroNameTable, HashMap<Integer, MDTEntry> macroDefinitionTable, HashMap<Integer, HashMap<Integer, ALAEntry>> allALA) {
		this.program = program;
		this.macroNameTable = macroNameTable;
		this.macroDefinitionTable = macroDefinitionTable;
		this.allALA = allALA;
	}

	private String getExpandedCode(String line, int mntIndex) {
		StringBuilder expanded = new StringBuilder();
		int mdtIndex = macroNameTable.get(mntIndex).getMdtIndex();

		String passedParams = line.split("\\s", 2)[1];
		String noSpaceParams = passedParams.replaceAll("\\s", "");
		String[] actualParams = noSpaceParams.split(",");

		for (int i = 0; i < actualParams.length; i++) {
			HashMap<Integer, ALAEntry> macroALA = allALA.get(mntIndex);
			String param = actualParams[i];
			if (param.contains("="))
				param = param.split("=", 2)[1];
			macroALA.replace(i, new ALAEntry(macroALA.get(i).getFormalParameter(), param));
		}

		while (!macroDefinitionTable.get(mdtIndex).getDefinition().equalsIgnoreCase("MEND")) {
			String defLine = macroDefinitionTable.get(mdtIndex).getDefinition();
			HashMap<Integer, ALAEntry> macroALA = allALA.get(mntIndex);
			if (defLine.contains(",")) {
				String[] args = defLine
						.split("\\s", 2)[1]
						.split(",");

				for (String arg : args) {
					Integer j = Integer.parseInt(arg.replace(" ", ""));
					defLine = defLine.replace(j.toString(), macroALA.get(j).getActualParameter());
				}

			}
			expanded.append(defLine).append("\n");
			mdtIndex++;
		}
		expanded.replace(expanded.length() - 1, expanded.length(), "");
		return expanded.toString();
	}

	public void process() {
		while (!program.get(currentLineNumber).equalsIgnoreCase("END")) {
			// Read next source line
			String line = program.get(currentLineNumber);
			int mntIndex;
			if ((mntIndex = lineHasMacroCall(line)) != -1) {
				String expandedCodeWithArgs = getExpandedCode(line, mntIndex);

				outputCode.append(expandedCodeWithArgs).append("\n");
			} else {
				outputCode.append(line).append("\n");
			}
			currentLineNumber++;
		}
	}

	private int lineHasMacroCall(String line) {
		for (Map.Entry<Integer, MNTEntry> entry : macroNameTable.entrySet()) {
			int mntIndex = entry.getKey();
			String macroName = entry.getValue().getMacroName();
			if (line.contains(macroName))
				return mntIndex;
		}
		return -1;
	}

	public void createOutputFile() throws IOException {
		String fileName = FileIO.fileName.split("[.]")[0];
		FileWriter writer = new FileWriter(fileName + "_output_2.txt");
		System.out.println(outputCode);
		writer.write(outputCode.toString());
		writer.close();
		writer = new FileWriter(fileName + "_mnt_2.txt");
		for (Map.Entry<Integer, MNTEntry> entry : macroNameTable.entrySet()) {
			int index = entry.getKey();
			MNTEntry value = entry.getValue();
			writer.write(index + " " + value.toString() + "\n");
		}
		writer.close();
		writer = new FileWriter(fileName + "_mdt_2.txt");
		for (Map.Entry<Integer, MDTEntry> entry : macroDefinitionTable.entrySet()) {
			int index = entry.getKey();
			MDTEntry value = entry.getValue();
			writer.write(index + " " + value.toString() + "\n");
		}
		writer.close();
		for (Map.Entry<Integer, HashMap<Integer, ALAEntry>> entry : allALA.entrySet()) {
			int fileIndex = entry.getKey();
			HashMap<Integer, ALAEntry> alaAtIndex = entry.getValue();
			writer = new FileWriter(fileName + "_ala_" + fileIndex + "_2.txt");
			for (Map.Entry<Integer, ALAEntry> ala : alaAtIndex.entrySet()) {
				int index = ala.getKey();
				ALAEntry value = ala.getValue();
				writer.write(index + " " + value.toString() + "\n");
			}
			writer.close();
		}
	}
}
