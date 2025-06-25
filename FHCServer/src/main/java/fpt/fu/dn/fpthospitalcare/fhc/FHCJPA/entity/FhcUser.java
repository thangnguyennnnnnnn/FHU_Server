package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FHC_USER")
public class FhcUser {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ACTIVED")
    private String actived;

    @Column(name = "UPDATE_AT")
    private String updatedAt;

	@Column(name = "CREATED_AT")
    private String createAt;
    
    @Column(name = "LAST_LOGIN")
    private String lastLogin;
    
    @Column(name = "IS_ACTIVE")
    private String isActive;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FhcUserInformation information;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FhcUserRole> roles;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getActived() {
		return actived;
	}

	public void setActived(String actived) {
		this.actived = actived;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public FhcUserInformation getInformation() {
		return information;
	}

	public void setInformation(FhcUserInformation information) {
		this.information = information;
	}

	public List<FhcUserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<FhcUserRole> roles) {
		this.roles = roles;
	}

	  
    public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}
