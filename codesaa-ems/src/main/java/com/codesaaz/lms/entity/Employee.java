package com.codesaaz.lms.entity;

import com.codesaaz.lms.util.enums.EmployeeStatus;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "employee")
@NoArgsConstructor
public class Employee {

    @Id
    @Column(name = "employee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "joining_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime joiningDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor")
    private Employee supervisor;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "total_leave", nullable = false)
    private Integer totalLeave;

    @Column(name = "leave_remaining", nullable = false)
    private Integer leaveRemaining;

    @Column(name = "leave_consumed", nullable = true)
    private Integer leaveConsumed;

    @Column(name = "cnic", nullable = false)
    private Integer cnic;

    public Integer getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(Integer totalLeave) {
        this.totalLeave = totalLeave;
    }

    public Integer getLeaveRemaining() {
        return leaveRemaining;
    }

    public void setLeaveRemaining(Integer leaveRemaining) {
        this.leaveRemaining = leaveRemaining;
    }

    public Integer getLeaveConsumed() {
        return leaveConsumed;
    }

    public void setLeaveConsumed(Integer leaveConsumed) {
        this.leaveConsumed = leaveConsumed;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Integer getCnic() {
        return cnic;
    }

    public void setCnic(Integer cnic) {
        this.cnic = cnic;
    }

    public LocalDateTime getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDateTime joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId) &&
                Objects.equals(firstName, employee.firstName) &&
                Objects.equals(middleName, employee.middleName) &&
                Objects.equals(lastName, employee.lastName) &&
                Objects.equals(email, employee.email) &&
                Objects.equals(username, employee.username) &&
                Objects.equals(password, employee.password) &&
                Objects.equals(role, employee.role) &&
                Objects.equals(phoneNumber, employee.phoneNumber) &&
                Objects.equals(createdAt, employee.createdAt) &&
                status == employee.status &&
                Objects.equals(supervisor, employee.supervisor) &&
                Objects.equals(totalLeave, employee.totalLeave) &&
                Objects.equals(leaveRemaining, employee.leaveRemaining) &&
                Objects.equals(leaveConsumed, employee.leaveConsumed) &&
                Objects.equals(group, employee.group) &&
                Objects.equals(joiningDate, employee.joiningDate) &&
                Objects.equals(cnic, employee.cnic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, middleName, lastName, email, username, password, role, phoneNumber, createdAt, status, supervisor, group, leaveConsumed, leaveRemaining, totalLeave, joiningDate, cnic);
    }

    public Employee(Employee employee) {
        this.employeeId = employee.getEmployeeId();
        this.firstName = employee.getFirstName();
        this.middleName = employee.getMiddleName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.username = employee.getUsername();
        this.password = employee.getPassword();
        this.role = employee.getRole();
        this.phoneNumber = employee.getPhoneNumber();
        this.createdAt = employee.getCreatedAt();
        this.status = employee.getStatus();
        this.supervisor = employee.getSupervisor();
        this.group = employee.getGroup();
        this.totalLeave = employee.getTotalLeave();
        this.leaveRemaining = employee.getLeaveRemaining();
        this.leaveConsumed = employee.getLeaveConsumed();
        this.cnic = employee.getCnic();
        this.joiningDate = employee.getJoiningDate();
    }

}
