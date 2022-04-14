package com.bijay.ems.data;

import com.bijay.ems.model.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {
    Employee findEmployeeByPhone(String phone);
    Employee findEmployeeByEmail(String email);
}
