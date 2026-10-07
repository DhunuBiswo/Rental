package com.example.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rental.entity.ZipFile;


public interface ZipFileRepository extends JpaRepository<ZipFile, Long> {

}
