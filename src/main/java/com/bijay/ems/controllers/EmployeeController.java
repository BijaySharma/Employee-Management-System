package com.bijay.ems.controllers;

import com.bijay.ems.model.Employee;
import com.bijay.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/")
    public String getAllEmployees(Model model,
                                  @RequestParam(name="page", defaultValue = "0") String page,
                                  @RequestParam(name="sortBy", defaultValue = "id") String sortBy
                                ){
        Page<Employee> employeePage = null;
        try {
            employeePage = employeeService.getEmployeesByPages(Integer.parseInt(page), sortBy);
        }catch (NumberFormatException | NullPointerException e){
            return "notFound404";
        }
        List<Employee> employees = employeePage.getContent();
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("currentPage", page);
        model.addAttribute("employees", employees);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        return "index";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute("employee") Employee employee, Model model){

        List<String> mistakes = employeeService.validateNewEmployee(employee, false);
        if(!mistakes.isEmpty()){
            model.addAttribute("mistakes", mistakes);
            return "addEmployee";
        }

        employeeService.registerEmployee(employee);
        return "redirect:/employees/" + employee.getId();
    }

    @GetMapping("/addEmployee")
    public String addEmployee(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "addEmployee";
    }

    @GetMapping("/employees/{empId}")
    public String viewEmployee(@PathVariable("empId") String empId, Model model){
        Long id;

        try{
           id = Long.parseLong(empId);
        }catch (NumberFormatException nfe){
            model.addAttribute("error", "Invalid Employee ID");
            return "viewEmployee";
        }

        Employee employee = employeeService.getEmployeeById(id);
        if(employee == null){
            return "notfound404";
        }

        model.addAttribute("emp", employee);
        return "viewEmployee";
    }

    @GetMapping("/delete/{empId}")
    public String deleteEmployeeConfirmation(@PathVariable("empId") String empId, Model model){
        Long id;
        try{
            id = Long.parseLong(empId);
        }catch (NumberFormatException | NullPointerException e){
            return "index";
        }
        Employee e = employeeService.getEmployeeById(id);
        if(e == null){
            return "notfound404";
        }
        model.addAttribute("emp", e);
        return "confirmDelete";
    }

    @PostMapping("/delete/{empId}")
    public String deleteEmployee(@PathVariable("empId") String empId, Model model){

        Long id;
        try{
            id = Long.parseLong(empId);
        }catch (NumberFormatException | NullPointerException e){
            return "index";
        }

        Employee employee = employeeService.getEmployeeById(id);

        model.addAttribute("emp", employee);

        boolean success = employeeService.deleteEmployeeById(employee.getId());

        if(success){
            model.addAttribute("success", "true");
        }else{
            model.addAttribute("error", "Something went wrong.");
        }
        return "deleted";
    }

    @GetMapping("/updateEmployee/{empId}")
    public String showUpdateEmployee(@PathVariable("empId") String empId, Model model){
        Long id;

        try{
            id = Long.parseLong(empId);
        }catch (NumberFormatException nfe){
            model.addAttribute("error", "Invalid Employee ID");
            return "viewEmployee";
        }

        Employee employee = employeeService.getEmployeeById(id);
        if(employee == null){
            return "notfound404";
        }

        model.addAttribute("employee", employee);
        return "editEmployee";
    }

    @PostMapping("/updateEmployee/{empId}")
    public String updateEmployee(@ModelAttribute("employee") Employee employee, @PathVariable("empId") String empId, Model model){
        List<String> mistakes = employeeService.validateNewEmployee(employee, true);

        if(!mistakes.isEmpty()){
            model.addAttribute("mistakes", mistakes);
            mistakes.stream().forEach(System.out::println);
            return "redirect:/updateEmployee/" + empId;
        }
        try{
            employee.setId(Long.parseLong(empId));
        }catch (NumberFormatException | NullPointerException e){
            return "index";
        }
        employeeService.updateEmployee(employee);
        return "redirect:/employees/" + empId;
    }
}
