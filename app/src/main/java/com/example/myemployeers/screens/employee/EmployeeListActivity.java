package com.example.myemployeers.screens.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myemployeers.R;
import com.example.myemployeers.adapters.EmployeeAdapter;
import com.example.myemployeers.api.ApiFactory;
import com.example.myemployeers.api.ApiService;
import com.example.myemployeers.pojo.Employee;
import com.example.myemployeers.pojo.EmployerResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

//расширяем акивность интерфейсом
public class EmployeeListActivity extends AppCompatActivity implements EmployeesListView {

    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter adapter;
    private EnployeeListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //присваиваем значение.
        presenter = new EnployeeListPresenter(this);
        //теперь активность отвечает только за отображения данных
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        adapter = new EmployeeAdapter();
        adapter.setEmployees(new ArrayList<Employee>());
        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEmployees.setAdapter(adapter);
        //мы создали RecyclerView и EmployeeAdapter, но пока нигде не говорим загружать данные,
        //для этого необходимо добавить ссылку на Presenter.
        //после того установки всех данных мы у Presenter вызываем его метод loadData().
        presenter.loadData();
    }

    //что бы отобразить данные наша активность должна содержать метод.
    public void showData(List<Employee> employees) {
        //в этом метода у adapter вызываем метод setEmployees().
        adapter.setEmployees(employees);
    }

    //добавим метод который выведет ошибку если не удалось получить данные.
    public void showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        //и в методе onDestroy() не забывать вызывать метод dispose(), чтобы освободить ресурсы.
        presenter.disposeDisposable();
        super.onDestroy();
    }
}