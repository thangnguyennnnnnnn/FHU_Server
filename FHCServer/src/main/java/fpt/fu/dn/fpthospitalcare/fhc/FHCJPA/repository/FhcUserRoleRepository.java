package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity.FhcUserRole;
import jakarta.transaction.Transactional;

@Repository
public interface FhcUserRoleRepository extends JpaRepository<FhcUserRole, String> {

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO FHC_USER_ROLE "
				+ "(USER_ID, ROLE_ID, CREATED_BY, CREATED_AT, IS_ACTIVE) "
				+ "VALUES(:userId, :roleId, :createdBy, :createdAt, :isActive); ", nativeQuery = true)
	int insertFhcUserRole(
			@Param("userId") String userId ,
			@Param("roleId") String roleId ,
			@Param("createdBy") String createdBy ,
			@Param("createdAt") String createdAt ,
			@Param("isActive") String isActive
			);
}
