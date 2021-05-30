package macro_processor.pass_one;

import macro_processor.ALAEntry;
import macro_processor.MDTEntry;
import macro_processor.MNTEntry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirstPass {

	int mdtCounter = 0;
	int mntCounter = 0;

	public FirstPass(HashMap<Integer, String> program) {
		this.program = program;
	}

	final HashMap<Integer, MNTEntry> macroNameTable = new HashMap<>();
	final HashMap<Integer, MDTEntry> macroDefinitionTable = new HashMap<>();
	final HashMap<Integer, HashMap<Integer, ALAEntry>> allALA = new HashMap<>();

	final StringBuilder outputCode = new StringBuilder();
	int currentLineNumber = 1;
	private final HashMap<Integer, String> program;

	private void mdtIndexSubstitution(int ALAIndex) {
		int i = currentLineNumber + 1;
		HashMap<Integer, ALAEntry> macroALA = allALA.get(ALAIndex);
		while (!program.get(i).equalsIgnoreCase("MEND")) {
			String line = program.get(i);
			if (line.contains("\t"))
				line = line.replace("\t", "");
			for (Map.Entry<Integer, ALAEntry> entry : macroALA.entrySet()) {
				Integer index = entry.getKey();
				String formal = entry.getValue().getFormalParameter();
				if (line.contains(formal)) {
					line = line.replace(formal, index.toString());
				}
			}
			macroDefinitionTable.put(mdtCounter, new MDTEntry(line));
			mdtCounter += 1;
			currentLineNumber += 1;
			i++;
		}
		macroDefinitionTable.put(mdtCounter, new MDTEntry("MEND"));
		mdtCounter += 1;
		currentLineNumber += 1;
	}

	private void prepareALA(String formalParams) {
		HashMap<Integer, ALAEntry> macroALA = new HashMap<>();
		String noSpaceParams = formalParams.replaceAll("\\s", "");
		String[] params = noSpaceParams.split(",");
		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			if (param.contains("=")) {
				macroALA.put(i, new ALAEntry(param.split("=")[0], param.split("=")[1]));
			} else {
				macroALA.put(i, new ALAEntry(param, ""));
			}
		}
		allALA.put(allALA.size(), macroALA);
	}

	public void process() {
		// ? END Pseudo Opcode
		while (!program.get(currentLineNumber).equalsIgnoreCase("END")) {
			// Read next source line
			String line = program.get(currentLineNumber);
			// ? MACRO Pseudo Opcode
			if (line.equalsIgnoreCase("MACRO")) {
				currentLineNumber += 1;
				// Get MACRO Prototype
				String[] macroPrototype = program.get(currentLineNumber).split("\\s", 2);
				String macroName = macroPrototype[0];
				// Make a new MNT entry
				macroNameTable.put(mntCounter, new MNTEntry(macroName, mdtCounter));
				mntCounter += 1;
				// Prepare ALA
				prepareALA(macroPrototype[1]);
				// Create MDT entries and substitute index for arguments
				mdtIndexSubstitution(allALA.size() - 1);
			} else {
				// Copy source line to output
				outputCode.append(line).append("\n");
			}
			currentLineNumber += 1;
		}
		outputCode.append("END");
	}

	public void createOutputFile() throws IOException {
		String fileName = FileIO.fileName.split("[.]")[0];
		FileWriter writer = new FileWriter(fileName + "_output_1.txt");
		System.out.println(outputCode);
		writer.write(outputCode.toString());
		writer.close();
		writer = new FileWriter(fileName + "_mnt_1.txt");
		for (Map.Entry<Integer, MNTEntry> entry : macroNameTable.entrySet()) {
			int index = entry.getKey();
			MNTEntry value = entry.getValue();
			writer.write(index + " " + value.toString() + "\n");
		}
		writer.close();
		writer = new FileWriter(fileName + "_mdt_1.txt");
		for (Map.Entry<Integer, MDTEntry> entry : macroDefinitionTable.entrySet()) {
			int index = entry.getKey();
			MDTEntry value = entry.getValue();
			writer.write(index + " " + value.toString() + "\n");
		}
		writer.close();
		for (Map.Entry<Integer, HashMap<Integer, ALAEntry>> entry : allALA.entrySet()) {
			int fileIndex = entry.getKey();
			HashMap<Integer, ALAEntry> alaAtIndex = entry.getValue();
			writer = new FileWriter(fileName + "_ala_" + fileIndex + "_1.txt");
			for (Map.Entry<Integer, ALAEntry> ala : alaAtIndex.entrySet()) {
				int index = ala.getKey();
				ALAEntry value = ala.getValue();
				writer.write(index + " " + value.toString() + "\n");
			}
			writer.close();
		}
	}
}
