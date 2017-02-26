package tr.name.fatihdogan.books.callback;

public interface TwoObjectListener<T1, T2> {

    void onResponse(T1 object, T2 object2);

}
