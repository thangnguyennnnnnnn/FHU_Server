package fpt.fu.dn.fpthospitalcare.fhc.model;

public class HomeModel {
	String a;
	String b;
	public HomeModel() {
		
	}
	
	public HomeModel(String a, String b) {
		this.a = a;
		this.b = b;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "HomeModel [a=" + a + ", b=" + b + "] ";
	}
}
