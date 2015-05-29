package pl.mefiu.bank;

import pl.mefiu.bank.Employee.EmployeeRole;

import java.util.Date;
import java.util.List;

public final class EmployeeRepository extends Repository<Employee> implements IEmployeeRepository {

    @Override
    public Employee employeeByName(final String name) {
        if((name == null) || (name.isEmpty())) {
            throw new IllegalArgumentException("name cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = name;
        return read("from Employee employee where employee.name = ?", params).get(0);
    }

    @Override
    public List<Employee> employeesByRole(final EmployeeRole employeeRole) {
        if(employeeRole == null) {
            throw new IllegalArgumentException("employeeRole cannot be null!");
        }
        Object[] params = new Object[1];
        params[0] = employeeRole;
        return read("from Employee employee where employee.employeeRole = ?", params);
    }

    @Override
    public List<Employee> employeesByCreationDate(final Date createdAt) {
        if(createdAt == null) {
            throw new IllegalArgumentException("createdAt cannot be null!");
        }
        Object[] params = new Object[1];
        params[0] = createdAt;
        return read("from Employee employee where employee.createdAt = ?", params);
    }

    @Override
    public List<Employee> employeesByExpirationDate(final Date expiresAt) {
        if(expiresAt == null) {
            throw new IllegalArgumentException("expiresAt cannot be null!");
        }
        Object[] params = new Object[1];
        params[0] = expiresAt;
        return read("from Employee employee where employee.expiresAt = ?", params);
    }

    @Override
    public Employee employeeByAccessCode(final String accessCode) {
        if((accessCode == null) || (accessCode.isEmpty())) {
            throw new IllegalArgumentException("accessCode cannot be empty/null!");
        }
        Object[] params = new Object[1];
        params[0] = accessCode;
        return read("from Employee employee where employee.accessCode = ?", params).get(0);
    }

    @Override
    public List<Employee> fetchAll() {
        return read("from Employee", null);
    }

}
