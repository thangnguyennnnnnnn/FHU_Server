package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ROOM_SOCKET")
public class RoomSocket {
	
	@Id
	private String id;
	
	@Column(name = "ROOM_ID")
	private String roomId;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "CREATE_AT")
	private String createAt;

}
