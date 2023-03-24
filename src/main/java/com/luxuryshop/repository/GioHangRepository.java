package com.luxuryshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxuryshop.entities.GioHang;
import com.luxuryshop.entities.NguoiDung;

public interface GioHangRepository extends JpaRepository<GioHang, Long>{
	
	GioHang findByNguoiDung(NguoiDung n);
	
}
