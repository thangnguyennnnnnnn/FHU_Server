package fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity.FhcUser;
import fpt.fu.dn.fpthospitalcare.fhc.FHCJPA.entity.FhcUserInformation;
import jakarta.transaction.Transactional;

@Repository
public interface FhcUserInformationRepository extends JpaRepository<FhcUserInformation, String> {

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO FHC_USER_INFORMATION "
				+ "(USER_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDATE, GENDER, NATIONALITY, PHONE_NUMBER, EMAIL, ADDRESS, URL_AVATA) "
				+ "VALUES(:userId, :firstName, :middleName, :lastName, :birthDate, :gender, :nationality, :phoneNumber, :email, :address, :avatarUrl); ", nativeQuery = true)
	int insertFhcUserInformation(@Param("userId") String userId ,
			@Param("firstName") String firstName ,
			@Param("middleName") String middleName ,
			@Param("lastName") String lastName ,
			@Param("birthDate") String birthDate ,
			@Param("gender") String gender ,
			@Param("nationality") String nationality ,
			@Param("phoneNumber") String phoneNumber ,
			@Param("email") String email ,
			@Param("address") String address ,
			@Param("avatarUrl") String avatarUrl );
}
