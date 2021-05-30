package macro_processor;

public class MDTEntry {
	private final String definition;

	public MDTEntry(String definition) {
		this.definition = definition;
	}

	public String getDefinition() {
		return definition;
	}

	@Override
	public String toString() {
		return definition;
	}
}
