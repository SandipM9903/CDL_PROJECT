package com.cms.cdl.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@AttributeOverride(name = "id", column = @Column(name = "empId"))
public class Employee extends BaseEntity {
    @Column(unique = true)
    private String empCode;
    private Long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullNameAsAadhaar;
    private String age;
    private Long profileImgDocId;
    @Column(unique = true)
    private String emailId;
    private String password;
    private String roles;
    private String reportingManager;
    private String reportTo;
    private String reportingManagerEmailId;
    @Column(name = "doj")
    private LocalDate dateOfJoining;
    @Column(name = "dob")
    private LocalDate dateOfBirth;
    private String primaryContactNo;
    private String secondaryContactNo;
    private String emergencyContactNo;
    private String emergencyContactName;
    private String relationWithEmergencyContact;
    private String personalEmailId;
    private String passportNumber;
    private String aboutEmp;
    private String maritalStatus;
    private String gender;
    private String bloodGroup;
    private boolean status;
    private String probationPeriod;
    @Column(nullable = true)
    private LocalDate dateOfConfirmation;
    private boolean resignation;
    @Column(nullable = true)
    private LocalDate dateOfLeaving;
    private String noticePeriod;
    private String nomineeName;
    private boolean ptApplicability;
    private boolean mlwfApplicability;
    private String gmcApplicability;
    private String gtlApplicability;
    private String gpaApplicability;
    private String wcApplicability;
    private String hiringHr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mainDeptId", referencedColumnName = "mainDeptId")
    private MainDepartment mainDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subDeptId", referencedColumnName = "subDeptId")
    private SubDepartment subDepartment;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DependentDetails> dependentDetails;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "salaryAccDetailsId", referencedColumnName = "salaryAccDetailsId")
    private SalaryAccountDetails salaryAccountDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designationId", referencedColumnName = "designationId")
    private Designation designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgCode", referencedColumnName = "orgCode")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", referencedColumnName = "projectId")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buHeadId", referencedColumnName = "buHeadId")
    private BuHeads buHeads;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classificationId", referencedColumnName = "classificationId")
    private Classification classification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eeSubgroupId", referencedColumnName = "eeSubgroupId")
    private EESubGroup eeSubGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employmentStatusId", referencedColumnName = "employmentStatusId")
    private EmploymentStatus employmentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genTypeId", referencedColumnName = "genTypeId")
    private GenerationType generationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradeId", referencedColumnName = "gradeId")
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payrollTypeId", referencedColumnName = "payrollTypeId")
    private PayrollAreaType payrollAreaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgHierarchyId", referencedColumnName = "orgHierarchyId")
    private OrgHierarchy orgHierarchy;

}
