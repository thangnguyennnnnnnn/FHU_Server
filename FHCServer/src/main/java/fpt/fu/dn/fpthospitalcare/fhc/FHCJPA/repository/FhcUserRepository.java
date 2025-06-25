package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.dto.UserDto;
import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity.FhcUser;
import jakarta.transaction.Transactional;

@Repository
public interface FhcUserRepository extends JpaRepository<FhcUser, String> {

	@Query("SELECT new fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.dto.UserDto(u.id, u.username, r.roleId, CONCAT(i.firstName, ' ', i.middleName, ' ', i.lastName), i.avatarUrl) " +
		       "FROM FhcUser u " +
		       "JOIN u.roles r " +
		       "LEFT JOIN u.information i " +
		       "WHERE u.username = :username AND u.password = :password AND u.actived = '1'")
	UserDto findUserInfo(@Param("username") String username, @Param("password") String password);

    @Modifying
    @Transactional
    @Query("UPDATE FhcUser u SET u.isActive = 'Y', u.lastLogin = :lastLogin WHERE u.username = :username")
    int activateUser(@Param("lastLogin") String lastLogin, @Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO FHC_USER (ID, USERNAME, PASSWORD, CREATED_AT, IS_ACTIVE, LAST_LOGIN, ACTIVED) " +
                   "VALUES (:id, :username, :password, :createAt, :isActive, :lastLogin, :actived)", nativeQuery = true)
    int insertUser(
        @Param("id") String id,
        @Param("username") String username,
        @Param("password") String password,
        @Param("createAt") String createAt,
        @Param("isActive") String isActive,
        @Param("lastLogin") String lastLogin,
        @Param("actived") String actived
    );

}