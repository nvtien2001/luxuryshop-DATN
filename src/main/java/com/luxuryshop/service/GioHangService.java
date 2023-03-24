package com.luxuryshop.service;

import com.luxuryshop.entities.GioHang;
import com.luxuryshop.entities.NguoiDung;

public interface GioHangService {
	
	GioHang getGioHangByNguoiDung(NguoiDung n);
	
	GioHang save(GioHang g);
}
