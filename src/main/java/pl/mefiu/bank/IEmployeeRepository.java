package pl.mefiu.bank;

import pl.mefiu.bank.Employee.EmployeeRole;

import java.util.Date;
import java.util.List;

public interface IEmployeeRepository {

    Employee employeeByName(final String name);

    List<Employee> employeesByRole(final EmployeeRole employeeRole);

    List<Employee> employeesByCreationDate(final Date createdAt);

    List<Employee> employeesByExpirationDate(final Date expiresAt);

    Employee employeeByAccessCode(final String accessCode);

    List<Employee> fetchAll();

}
