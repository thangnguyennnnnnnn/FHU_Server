package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity.FhcStaff;

@Repository
public interface FhcStaffRepository extends JpaRepository<FhcStaff, String> {
	
	@Query("SELECT s.isActive FROM FhcStaff s WHERE s.staffId = :userId")
	String getStaffActived(@Param("userId") String userId);

}
