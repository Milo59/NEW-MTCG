package org.example.application.battles.repository;


import org.example.application.battles.model.Log;

public interface BattlesRepository {
    void insert(Log log)throws Exception;
}
