package com.example.myemployeers.screens.employee;

import android.widget.Toast;

import com.example.myemployeers.api.ApiFactory;
import com.example.myemployeers.api.ApiService;
import com.example.myemployeers.pojo.EmployerResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EnployeeListPresenter {

    private CompositeDisposable compositeDisposable;
    private EmployeesListView view;
    //этому объекту присвоим значение в конструкторе.

    public EnployeeListPresenter(EmployeesListView view) {
        this.view = view;
    }

    //добавим сюда один метод
    public void loadData() {
        //получаем доступ к apiService
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        compositeDisposable = new CompositeDisposable();

        //и теперь можем получать наши данные, метод getEmployees() возвращает тип Observable у этого объекта есть различные методы.
        //этот метод добавляется абсолютно всегда, он нужен для того чтобы показать в каком потоке все это делать,
        // все обращения в БД и скачивания из интернета делается в Schedulers.io().
        //затем нужно указать в каком потоке мы будем принимать данные(принимать данные будем в главном потоке),
        // эти две строчки стандартные, они указываются почто что всегда.
        // теперь необходимо указать что мы будем делать когда получим данные, вызываем метод subscribe(),
        // и внтру него будут два объекта анонимного класса Consumer и их методы будут вызываться в разных ситуациях.
        Disposable disposable = apiService.getResponse()
                //этот метод добавляется абсолютно всегда, он нужен для того чтобы показать в каком потоке все это делать,
                // все обращения в БД и скачивания из интернета делается в Schedulers.io().
                .subscribeOn(Schedulers.io())
                //затем нужно указать в каком потоке мы будем принимать данные(принимать данные будем в главном потоке),
                // эти две строчки стандартные, они указываются почто что всегда.
                .observeOn(AndroidSchedulers.mainThread())
                // теперь необходимо указать что мы будем делать когда получим данные, вызываем метод subscribe(),
                // и внтру него будут два объекта анонимного класса Consumer и их методы будут вызываться в разных ситуациях.
                .subscribe(new Consumer<EmployerResponse>() {
                    @Override
                    public void accept(EmployerResponse employerResponse) throws Exception {
                        //Presenter не должен знать как именно отображать данные, этим занимется view.
                        //Если загрузка прошла хорошо то вызываем метод активности showData(). Не забудьте добавить ссылку на Активность.
                        //у Presenter не должен быть доступ к активности, поэтому методы активности реализуем через интерфейс.
                        view.showData(employerResponse.getResponse());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showError();
                    }
                });
        //первый метод accept(EmployerResponse employerResponse) выполняется если данные были успешно загружены,
        // второй метод accept(Throwable throwable) выполняется если не успешно выпонили загрузку данных.
        //во время работы приложения пользователь закрыл ее но загрузка не остановилась и произощла утечка памяти,
        // чтобы этого избежать мы полученный объект можем привести к типу Disposable(выбрасываемый или одноразовый).
        //после того как получили объект типа Disposable и когда закрываем приложения мы можем переопределить метод onDestroy().
        //и в методе onDestroy() вызвать у объекта типа Disposable метод dispose(), не забудьте определить объект вне метода.
        //еще бывает так что объектов Disposable несколько и чтобы отдельно не вызывать каждого используется объект CompositeDisposable,
        //и после создания объекта Disposable мы добавляем его в CompositeDisposable, после в методе onDestroy(), вызываем метод dispose().
        compositeDisposable.add(disposable);
    }
    //и в конце у объекта compositeDisposable мы должны вызвать метод dispose().
    public void disposeDisposable() {
        //делаем проверку на null и вызываем метод dispose().
        if(compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
