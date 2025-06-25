package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity.RoomSocket;

@Repository
public interface SocketRepository extends JpaRepository<RoomSocket, String> {
	@Query("SELECT r.roomId FROM RoomSocket r WHERE r.userId = :userId")
	String getRoomId(@Param("userId") String userId);

}
