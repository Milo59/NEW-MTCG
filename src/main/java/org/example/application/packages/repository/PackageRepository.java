package org.example.application.packages.repository;

import org.example.application.packages.model.Packages;

import java.util.List;

public interface PackageRepository {

    boolean save(List<Packages> packageList) throws Exception ;

}
