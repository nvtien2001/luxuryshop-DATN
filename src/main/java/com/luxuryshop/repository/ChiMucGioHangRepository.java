package com.luxuryshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxuryshop.entities.ChiMucGioHang;
import com.luxuryshop.entities.GioHang;
import com.luxuryshop.entities.SanPham;

public interface ChiMucGioHangRepository extends JpaRepository<ChiMucGioHang, Long>{
	
	ChiMucGioHang findBySanPhamAndGioHang(SanPham sp,GioHang g);
	
	List<ChiMucGioHang> findByGioHang(GioHang g);
}
