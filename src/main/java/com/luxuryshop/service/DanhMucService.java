package com.luxuryshop.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.luxuryshop.entities.DanhMuc;

public interface DanhMucService {

	Page<DanhMuc> getAllDanhMucForPageable(int page, int size);

	List<DanhMuc> getAllDanhMuc();

	DanhMuc getDanhMucById(long id);

	DanhMuc save(DanhMuc d);

	DanhMuc update(DanhMuc d);

	void deleteById(long id);
}
