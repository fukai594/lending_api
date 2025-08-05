package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Lending;

public interface LendingRepository extends JpaRepository<Lending, Integer>{

}
