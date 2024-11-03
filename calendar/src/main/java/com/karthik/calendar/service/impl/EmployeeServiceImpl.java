package com.karthik.calendar.service.impl;

import com.karthik.calendar.model.Employee;
import com.karthik.calendar.repo.EmployeeRepo;
import com.karthik.calendar.service.EmployeeService;
import com.karthik.calendar.service.helper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll().stream().map(Converter :: toEmployee).toList();
    }
}
