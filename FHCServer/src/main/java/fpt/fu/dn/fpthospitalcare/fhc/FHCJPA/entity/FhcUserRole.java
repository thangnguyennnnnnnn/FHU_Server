package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FHC_USER_ROLE")
public class FhcUserRole {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private FhcUser user;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_AT")
    private String createdAt;

    @Column(name = "IS_ACTIVE")
    private String isActive;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public FhcUser getUser() {
		return user;
	}

	public void setUser(FhcUser user) {
		this.user = user;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
