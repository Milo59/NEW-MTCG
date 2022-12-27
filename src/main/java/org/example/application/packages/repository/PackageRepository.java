package org.example.application.packages.repository;

import org.example.application.card.model.Card;

import java.util.List;

public interface PackageRepository {

    boolean save(List<Card> packageList) throws Exception ;

    List<Card> acquirePackages(Long userId) throws Exception ;

    boolean checkPackage()throws Exception ;

}