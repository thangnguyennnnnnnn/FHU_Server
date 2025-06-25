package fpt.fu.dn.fpthospitalcare.fhc.model;

public class ReturnModel {
	public int errorCode = 0;
	
	public String errorMessage = "";
	
	public Object returnObject;
	
	//public String sessionAuthor;

	//public String getSessionAuthor() {
	//	return sessionAuthor;
	//}

	//public void setSessionAuthor(String sessionAuthor) {
	//	this.sessionAuthor = sessionAuthor;
	//}
	
	public ReturnModel() {
	}

	public ReturnModel(Object returnObject) {
		this.returnObject = returnObject;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	@Override
	public String toString() {
		return "AbstractModel [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", returnObject="
				+ returnObject + "]";
	}
}
