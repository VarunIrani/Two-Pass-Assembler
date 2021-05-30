package first_pass;

public class Opcode {
	private String mnemonic;
	private MnemonicClass mnemonicClass;

	public Opcode(MnemonicClass type, String mnemonic) {
		this.setType(type);
		this.setMnemonic(mnemonic);
	}

	/**
	 * @return the mnemonic
	 */
	public String getMnemonic() {
		return mnemonic;
	}

	/**
	 * @param mnemonic the mnemonic to set
	 */
	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	/**
	 * @return the type
	 */
	public String getMnemonicClass() {
		return mnemonicClass.value;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MnemonicClass type) {
		this.mnemonicClass = type;
	}

	public String getIntermediateCode() {
		return "(" + this.getMnemonicClass() + "," + this.getMnemonic() + ") ";
	}

}
