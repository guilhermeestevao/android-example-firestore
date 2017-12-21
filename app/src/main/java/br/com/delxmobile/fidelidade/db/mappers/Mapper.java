package br.com.delxmobile.fidelidade.db.mappers;



public interface Mapper<From, To> {
    To map(From from);
}