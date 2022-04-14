package com.bijay.ems.service;

import com.bijay.ems.data.EmployeeRepository;
import com.bijay.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Employee getEmployeeById(Long id){
        Optional<Employee> data = employeeRepository.findById(id);
        if(data.isPresent()){
            return data.get();
        }else {
            return null;
        }
    }

    public List<String> validateNewEmployee(Employee employee, Boolean update){
        List<String> mistakes = new ArrayList<>();

        // check name
        if(employee.getName() == null || employee.getName().length() < 3)
            mistakes.add("Employee name must be atleast 3 characts long");

        // check department
        if(employee.getDepartment() == null || employee.getDepartment().length() < 5)
            mistakes.add("Employee name must be atleast 3 characts long");

        // check designation
        if(employee.getDesignation() == null || employee.getDesignation().length() < 5)
            mistakes.add("Employee Designation must be atleast 3 characts long");

        // check email
        if(employee.getEmail() == null || !employee.getEmail().matches("[\\w .]+@\\w+.[A-Z a-z]{2,10}"))
            mistakes.add("Please enter a valid email ID");

        // check phone
        if(employee.getPhone() == null || employee.getPhone().length() != 10)
            mistakes.add("Phone Number must contain 10 digits");
        try{
            Long.parseLong(employee.getPhone());
        }catch (NumberFormatException nfe){
            mistakes.add("Please enter a valid phone number");
        }

        if(!update){
             // check Unique
            Employee e = employeeRepository.findEmployeeByEmail(employee.getEmail());
            System.out.println(e);
            if(e != null){
                mistakes.add("Duplicate Entry! An Employee with same Email already Exists");
            }else {
                e = employeeRepository.findEmployeeByPhone(employee.getPhone());
                if (e != null) {
                    mistakes.add("Duplicate Entry! An Employee with same Phone already Exists");
                }
            }
        }

        return mistakes;
    }

    public Employee registerEmployee(Employee employee){
        employeeRepository.save(employee);
        return employee;
    }

    public boolean deleteEmployeeById(Long id){
        Employee employee = getEmployeeById(id);

        if(employee == null)
            return false;

        employeeRepository.deleteById(id);
        return true;
    }


    public Page<Employee> getEmployeesByPages(int page, String sortBy) {
        return employeeRepository.findAll(PageRequest.of(page, 4, Sort.by(sortBy)));
    }

    public boolean updateEmployee(Employee employee){
        employeeRepository.save(employee);
        return true;
    }
}
