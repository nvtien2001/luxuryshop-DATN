package com.luxuryshop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.luxuryshop.entities.ChiMucGioHang;
import com.luxuryshop.entities.GioHang;
import com.luxuryshop.entities.NguoiDung;
import com.luxuryshop.entities.SanPham;
import com.luxuryshop.service.ChiMucGioHangService;
import com.luxuryshop.service.GioHangService;
import com.luxuryshop.service.NguoiDungService;
import com.luxuryshop.service.SanPhamService;

@Controller
@SessionAttributes("loggedInUser")
public class CartController {
	
	@Autowired
	private SanPhamService sanPhamService;
	@Autowired
	private NguoiDungService nguoiDungService;
	@Autowired
	private GioHangService gioHangService;
	@Autowired
	private ChiMucGioHangService chiMucGioHangService;
	
	@ModelAttribute("loggedInUser")
	public NguoiDung loggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return nguoiDungService.findByEmail(auth.getName());
	}
	
	public NguoiDung getSessionUser(HttpServletRequest request) {
		return (NguoiDung) request.getSession().getAttribute("loggedInUser");
	}
	
	@GetMapping("/cart")
	public String cartPage(HttpServletRequest res,Model model) {
		NguoiDung currentUser = getSessionUser(res);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Map<Long,String> quanity = new HashMap<>();
		List<SanPham> listsp = new ArrayList<>();
		if(auth == null || auth.getPrincipal() == "anonymousUser")     //Lay tu cookie
		{
			Cookie[] cl = res.getCookies();
			Set<Long> idList = new HashSet<>();
			for (Cookie cookie : cl) {
				if (cookie.getName().matches("[0-9]+")) {
					idList.add(Long.parseLong(cookie.getName()));
					quanity.put(Long.parseLong(cookie.getName()), cookie.getValue());
				}

			}
			listsp = sanPhamService.getAllSanPhamByList(idList);
		}else     //Lay tu database
		{
			GioHang g = gioHangService.getGioHangByNguoiDung(currentUser);
			if(g != null)
			{
				List<ChiMucGioHang> listchimuc = chiMucGioHangService.getChiMucGioHangByGioHang(g);
				for(ChiMucGioHang c: listchimuc)
				{
					listsp.add(c.getSanPham());
					quanity.put(c.getSanPham().getId(), Integer.toString(c.getSo_luong()));
				}
			}
		}

		model.addAttribute("checkEmpty",listsp.size());
		model.addAttribute("cart",listsp);
		model.addAttribute("quanity",quanity);
		
		
		return "client/cart";
	}

}
