package com.cms.cdl.repo;

import com.cms.cdl.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
//    @Query(value = "select max(emp_id) from employee", nativeQuery = true)
//    public Long findByEmpId();

    Optional<Employee> findByEmailId(String emailId);

    @Query(value = "SELECT * FROM employee" +
            "                   WHERE EXTRACT(MONTH FROM dob) = EXTRACT(MONTH FROM CURRENT_DATE)" +
            "                   AND EXTRACT(DAY FROM dob) BETWEEN EXTRACT(DAY FROM CURRENT_DATE)" +
            "                   AND EXTRACT(DAY FROM DATE_TRUNC('MONTH', CURRENT_DATE) + INTERVAL '1 MONTH' - INTERVAL '1 DAY')" +
            "                   ORDER BY EXTRACT(DAY FROM dob) ASC", nativeQuery = true)
    List<Employee> findBirthdays();

    @Query(value = "SELECT * FROM employee " +
            "WHERE EXTRACT(MONTH FROM doj) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(DAY FROM doj) = EXTRACT(DAY FROM CURRENT_DATE) " +
            "AND AGE(CURRENT_DATE, doj) >= INTERVAL '1 year'", nativeQuery = true)
    List<Employee> findWorkAnniversary();

    @Query("SELECT e FROM Employee e WHERE e.userId IN :userIds AND e.status = true")
    List<Employee> findEmployeesByUserIdsAndStatus(@Param("userIds") List<Long> userIds);

    @Query(value = "SELECT * FROM employee WHERE hiring_hr = :hiringHr", nativeQuery = true)
    List<Employee> findByHiringHr(@Param("hiringHr") String hiringHr);

//    @Query(value = "SELECT * from employee where location_id = ?1", nativeQuery = true)
//    List<Employee> findByLocationBasedEmployees(Long locationId);

    @Query(value = "SELECT * from employee WHERE report_to = :empCode", nativeQuery = true)
    List<Employee> findByReportTo(@Param("empCode") String empCode);

    @Query(value = "select * from EMPLOYEE WHERE emp_code = :empCode", nativeQuery = true)
    Employee findByEmpCode(String empCode);

}
