package model;

public enum RawMaterial {
	RAW1("RAW1"), RAW2("RAW2"), RAW3("RAW3");
	
	private String rw;
	
	public String getRw() {
		return rw;
	}

	RawMaterial(String rw) {
		this.rw = rw;
	}
}
