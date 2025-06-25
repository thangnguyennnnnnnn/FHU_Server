package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FHC_STAFF")
public class FhcStaff {
	
	@Id
    @Column(name = "STAFF_ID")
    private String staffId;

    @Column(name = "FACULTY_ID")
    private String facultyId;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    @Column(name = "POSITION_ID")
    private String positionId;

    @Column(name = "BUILDING_ID")
    private String buildingId;

    @Column(name = "FLOOR_ID")
    private String floorId;

    @Column(name = "ROOM_ID")
    private String roomId;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "CREATE_AT")
    private String createAt;

    @Column(name = "UPDATE_AT")
    private String updateAt;

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}
    
}
