package org.example.application.packages.repository;

import org.example.application.card.model.Card;
import org.example.application.packages.model.Packages;

import java.util.List;

public interface PackageRepository {

    boolean save(List<Card> packageList) throws Exception ;

    //

}