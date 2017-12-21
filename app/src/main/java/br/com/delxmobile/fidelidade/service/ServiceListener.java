package br.com.delxmobile.fidelidade.service;

/**
 * Created by Guilherme on 21/12/2017.
 */

public interface ServiceListener<T> {

    void onComplete(T object);
    void onError(String cause);
}
