<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<link rel="stylesheet" href="Frontend/css/detailsp.css">
</head>
	<script type="text/javascript">	  
	$(document).ready(function() { 
	  	var priceConvert = accounting.formatMoney(${sp.getDonGia()})+' VND';
		document.getElementById("priceConvert").innerHTML= priceConvert;
		  
	  });
	</script>
<body>
	<div class="container">
		<div class="card">
			<div class="container-fliud">
				<div class="wrapper row">
					<div class="preview col-md-6">
						
						<div class="preview-pic tab-content">
						  <div class="tab-pane active" id="pic-1">
							  <img src="/luxuryshop/img/${sp.getId()}.png" width="450px" height="450px"/>
						  </div>
						</div>		
					</div>
					<div class="details col-md-6">
						<p style="display:none" id="spid">${sp.getId()}</p>
						<h2 class="product-title">${sp.getTenSanPham()}</h2>
						<h4 class="price" id ="blabla">Giá bán: <span id="priceConvert"></span></h4>
						<h4 class="price">Mô tả sản phẩm</h4>
						<c:if test = "${sp.getDvt().length() > 0}">
							<p class="product-description">Đơn vị tính: ${sp.getDvt()}</p>
						</c:if>
						<c:if test = "${sp.getKieuDang().length() > 0}">
							<p class="product-description">Kiểu dáng: ${sp.getKieuDang()}</p>
						</c:if>
						<c:if test = "${sp.getChatLieu().length() > 0}">
						<p class="product-description">Chất liệu chính: ${sp.getChatLieu()}</p>
						</c:if>
						<c:if test = "${sp.getHoanThien().length() > 0}">
							<p class="product-description">Hoàn thiện: ${sp.getHoanThien()}</p>
						</c:if>
						<c:if test = "${sp.getKichThuoc().length() > 0}">
							<p class="product-description">Kích thước: ${sp.getKichThuoc()}</p>
						</c:if>
						<c:if test = "${sp.getPhongCach().length() > 0}">
							<p class="product-description">Phong cách: ${sp.getPhongCach()}</p>
						</c:if>
						<p class="product-description">Hãng sản xuất: ${sp.hangSanXuat.tenHangSanXuat}</p>
						<p class="product-description"><span class="important">THÔNG TIN CHUNG:</span> ${sp.getThongTinChung()}</p>
						<p class="product-description"><span class="important">BẢO HÀNH:</span> ${sp.getThongTinBaoHanh()}</p>

						<div class="action">
							<button class="add-to-cart btn btn-warning" type="button">
							<span class="glyphicon glyphicon-shopping-cart pull-center"></span> Giỏ hàng</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<script src="<c:url value='/js/client/detailspAjax.js'/>" ></script>	 