package com.luxuryshop.service.impl;

import com.luxuryshop.entities.ChiTietDonHang;
import com.luxuryshop.repository.ChiTietDonHangRepository;
import com.luxuryshop.service.ChiTietDonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietDonHangServiceImpl implements ChiTietDonHangService{
	
	@Autowired
	private ChiTietDonHangRepository repo;

	@Override
	public List<ChiTietDonHang> save(List<ChiTietDonHang> list) {
		return repo.saveAll(list);
	}
}
