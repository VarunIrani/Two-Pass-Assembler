package macro_processor;

public class ALAEntry {
	private final String formalParameter;
	private final String actualParameter;

	public ALAEntry(String formalParameter, String actualParameter) {
		this.formalParameter = formalParameter;
		this.actualParameter = actualParameter;
	}

	public String getFormalParameter() {
		return formalParameter;
	}

	public String getActualParameter() {
		return actualParameter;
	}

	@Override
	public String toString() {
		return formalParameter + " " + actualParameter;
	}
}
