package com.luxuryshop.api.admin;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.luxuryshop.entities.ChiMucGioHang;
import com.luxuryshop.entities.GioHang;
import com.luxuryshop.entities.NguoiDung;
import com.luxuryshop.entities.ResponseObject;
import com.luxuryshop.entities.SanPham;
import com.luxuryshop.service.ChiMucGioHangService;
import com.luxuryshop.service.GioHangService;
import com.luxuryshop.service.NguoiDungService;
import com.luxuryshop.service.SanPhamService;

@RestController
@RequestMapping("api/gio-hang")
@SessionAttributes("loggedInUser")
public class GioHangApi  {
	
	@Autowired
	private NguoiDungService nguoiDungService;
	@Autowired
	private GioHangService gioHangService;
	@Autowired
	private SanPhamService sanPhamService;
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
	
	@GetMapping("/addSanPham")
	public ResponseObject addToCart(@RequestParam String id,HttpServletRequest request,HttpServletResponse response) {
		ResponseObject ro = new ResponseObject();
		SanPham sp = sanPhamService.getSanPhamById(Long.parseLong(id));
		if(sp.getDonViKho() == 0)
		{
			ro.setStatus("false");
			return ro;
		}
		NguoiDung currentUser = getSessionUser(request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
		if(auth == null || auth.getPrincipal() == "anonymousUser" )    //Su dung cookie de luu
		{
			Cookie[] clientCookies = request.getCookies();
			boolean found = false;
			for (Cookie clientCookie : clientCookies) {
				if (clientCookie.getName().equals(id))     //Neu san pham da co trong cookie tang so luong them 1
				{
					clientCookie.setValue(Integer.toString(Integer.parseInt(clientCookie.getValue()) + 1));
					clientCookie.setPath("/luxuryshop");
					clientCookie.setMaxAge(60 * 60 * 24 * 7);
					response.addCookie(clientCookie);
					found = true;
					break;
				}
			}
			if(!found)   //Neu san pham ko co trong cookie,them vao cookie
			{
				Cookie c = new Cookie(id,"1");
				c.setPath("/luxuryshop");
				c.setMaxAge(60*60*24*7);
				response.addCookie(c);
			}
		}else {     //Su dung database de luu
			GioHang g = gioHangService.getGioHangByNguoiDung(currentUser);
			if(g==null)
			{
				g = new GioHang();
				g.setNguoiDung(currentUser);
				g = gioHangService.save(g);			
			}
			
			ChiMucGioHang c = chiMucGioHangService.getChiMucGioHangBySanPhamAndGioHang(sp,g);
			if(c== null)     //Neu khong tim chi muc gio hang, tao moi
			{
				System.out.println(g.getNguoiDung().getEmail());
				System.out.println(g.getId());
				c = new ChiMucGioHang();
				c.setGioHang(g);
				c.setSanPham(sp);
				c.setSo_luong(1);
			}else       //Neu san pham da co trong database tang so luong them 1
			{
				c.setSo_luong(c.getSo_luong()+1);
			}
			c = chiMucGioHangService.saveChiMucGiohang(c);
		}
		ro.setStatus("success");
		return ro;
	}
	
	@GetMapping("/changSanPhamQuanity")
	public ResponseObject changeQuanity(@RequestParam String id,@RequestParam String value,HttpServletRequest request,HttpServletResponse response) {
		NguoiDung currentUser = getSessionUser(request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ResponseObject ro = new ResponseObject();
		if(auth == null || auth.getPrincipal() == "anonymousUser" )    //Su dung cookie de luu
		{
			Cookie[] clientCookies = request.getCookies();
			for (Cookie clientCookie : clientCookies) {
				if (clientCookie.getName().equals(id)) {
					clientCookie.setValue(value);
					clientCookie.setPath("/luxuryshop");
					clientCookie.setMaxAge(60 * 60 * 24 * 7);
					response.addCookie(clientCookie);
					break;
				}
			}
		}else //Su dung database de luu
		{
			GioHang g = gioHangService.getGioHangByNguoiDung(currentUser);
			SanPham sp = sanPhamService.getSanPhamById(Long.parseLong(id));
			ChiMucGioHang c = chiMucGioHangService.getChiMucGioHangBySanPhamAndGioHang(sp,g);
			c.setSo_luong(Integer.parseInt(value));
			c = chiMucGioHangService.saveChiMucGiohang(c);
		}
		ro.setStatus("success");
		return ro;
	}
	
	@GetMapping("/deleteFromCart")
	public ResponseObject deleteSanPham(@RequestParam String id,HttpServletRequest request,HttpServletResponse response) {
		NguoiDung currentUser = getSessionUser(request);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
		ResponseObject ro = new ResponseObject();
		if(auth == null || auth.getPrincipal() == "anonymousUser")    //Su dung cookie de luu
		{
			Cookie[] clientCookies = request.getCookies();
			for (Cookie clientCookie : clientCookies) {
				if (clientCookie.getName().equals(id)) {
					clientCookie.setMaxAge(0);
					clientCookie.setPath("/luxuryshop");
					System.out.println(clientCookie.getMaxAge());
					response.addCookie(clientCookie);
					break;
				}
			}
		}else //Su dung database de luu
		{
			GioHang g = gioHangService.getGioHangByNguoiDung(currentUser);
			SanPham sp = sanPhamService.getSanPhamById(Long.parseLong(id));
			ChiMucGioHang c = chiMucGioHangService.getChiMucGioHangBySanPhamAndGioHang(sp,g);
			chiMucGioHangService.deleteChiMucGiohang(c);
		}
		
		ro.setStatus("success");
		return ro;
	}
}
