package fpt.fu.dn.fpthospitalcare.fhc.model;

public class MenuObjectModel {
	private String menuItemId;
	
	private String menuItemName;
	
	private String menuItemIcon;
	
	private int menuRole;
	
	private String menuUrl;

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(String menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}

	public String getMenuItemIcon() {
		return menuItemIcon;
	}

	public void setMenuItemIcon(String menuItemIcon) {
		this.menuItemIcon = menuItemIcon;
	}

	public int getMenuRole() {
		return menuRole;
	}

	public void setMenuRole(int menuRole) {
		this.menuRole = menuRole;
	}
	
}
