package pl.mefiu.bank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@SuppressWarnings("ALL")
@Entity
@Table(name = "employee")
public final class Employee {

    public enum EmployeeRole {
        ADMIN, ORDINARY
    }

    public Employee(EmployeeRole employeeRole, String name, String accessCode, Date expiresAt) {
        setEmployeeRole(employeeRole);
        setName(name);
        setAccessCode(accessCode);
        setCreatedAt(new Date());
        setExpiresAt(expiresAt);
    }

    protected Employee() {

    }

    public Long getId() {
        return this.id;
    }

    public EmployeeRole getEmployeeRole() {
        if(employeeRole == employeeRole.ADMIN) {
            return employeeRole.ADMIN;
        }
        return employeeRole.ORDINARY;
    }

    public void setEmployeeRole(EmployeeRole employeeRole) {
        this.employeeRole = employeeRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if((name != null) && (name.isEmpty())) {
            throw new IllegalArgumentException("name cannot be empty!");
        }
        this.name = name;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }

    private void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return new Date(expiresAt.getTime());
    }

    public void setExpiresAt(Date expiresAt) {
        if(expiresAt != null) {
            if(expiresAt.before(createdAt)) {
                throw new IllegalArgumentException("bad date!");
            }
        }
        this.expiresAt = expiresAt;
    }

    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "employee_role", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Size(min = 1)
    @Column(name = "access_code", nullable = false, unique = true)
    private String accessCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at", nullable = false, unique = false)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "expires_at", nullable = false, unique = false)
    private Date expiresAt;

}
