package com.luxuryshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxuryshop.entities.VaiTro;

public interface VaiTroRepository extends JpaRepository<VaiTro, Long> {

	VaiTro findByTenVaiTro(String tenVaiTro);
}
