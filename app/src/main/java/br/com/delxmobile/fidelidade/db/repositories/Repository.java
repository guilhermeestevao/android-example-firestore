package br.com.delxmobile.fidelidade.db.repositories;

import java.util.ArrayList;
import br.com.delxmobile.fidelidade.db.sql.SqlSpecification;

/**
 * Created by Guilherme on 26/09/2017.
 */
public interface Repository<T> {
    T add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

    void remove(SqlSpecification specification);

    T findById(long id);

    T findByOid(String oid);

    ArrayList<T> query(SqlSpecification specification);


}
