package macro_processor;

public class MNTEntry {
	private final String macroName;
	private final int mdtIndex;

	public MNTEntry(String macroName, int mdtIndex) {
		this.macroName = macroName;
		this.mdtIndex = mdtIndex;
	}

	public String getMacroName() {
		return macroName;
	}

	public int getMdtIndex() {
		return mdtIndex;
	}

	@Override
	public String toString() {
		return macroName + " " + mdtIndex;
	}
}
