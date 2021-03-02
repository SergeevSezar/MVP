package com.example.myemployeers.screens.employee;

import com.example.myemployeers.pojo.Employee;

import java.util.List;

public interface EmployeesListView {
    //тут объявляем все методы которые должен знать Presenter.
    void showData(List<Employee> employees);
    void showError();
}
