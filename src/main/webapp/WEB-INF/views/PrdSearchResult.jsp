<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
.prdTitle {
	width: 300px;
	display: inline-block;
	white-space: nowrap;
	overflow: hidden;
	margin: 0;
}

table {
	border-collapse: collapse;
	width: 480px;	
}

tr {
	border-bottom: 1px solid black;
}

td, th {
	padding: 8px;
}

div.prdArea {
	height: 700px;
	overflow: scroll;
	width: 500px;
	border: 2px solid black;
	padding: 3px;
}

div.flex {
	display: flex;
}
</style>
</head>
<body>
	<h1>PrdSearchResult.jsp</h1>

	<div class="flex">

		<div class="prdArea">
			<table>
				<thead>
					<tr>
						<th>쇼핑몰</th>
						<th>상품이름</th>
						<th><select onchange="prdSort(this.value, 'coopang')">
								<option value="prd_desc">상품가격↑</option>
								<option value="prd_asc">내림차순↓</option>
						</select></th>
					</tr>
				</thead>
				<tbody class="tbodyTag coopang">
					<c:forEach items="${prdList_coopang }" var="prdInfo">
						<tr>
							<td>${prdInfo.prdSite }</td>
							<td><a class="prdTitle" title="${prdInfo.name }"
								href="${prdInfo.url }">${prdInfo.name }</a></td>
							<td>${prdInfo.price }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="prdArea">
			<table>
				<thead>
					<tr>
						<th>쇼핑몰</th>
						<th>상품이름</th>
						<th><select onchange="prdSort(this.value, 'gmarket')">
								<option value="prd_desc">상품가격↑</option>
								<option value="prd_asc">내림차순↓</option>
						</select></th>
					</tr>
				</thead>
				<tbody class="tbodyTag gmarket">
					<c:forEach items="${prdList_gmarket }" var="prdInfo">
						<tr>
							<td>${prdInfo.prdSite }</td>
							<td><a class="prdTitle" title="${prdInfo.name }"
								href="${prdInfo.url }">${prdInfo.name }</a></td>
							<td class="a">${prdInfo.price }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="prdArea">
			<table>
				<thead>
					<tr>
						<th>쇼핑몰</th>
						<th>상품이름</th>
						<th><select onchange="prdSort(this.value, '11st')">
								<option value="prd_desc">상품가격↑</option>
								<option value="prd_asc">내림차순↓</option>
						</select></th>
					</tr>
				</thead>
				<tbody class="tbodyTag 11st">
					<c:forEach items="${prdList_11st }" var="prdInfo">
						<tr>
							<td>${prdInfo.prdSite }</td>
							<td><a class="prdTitle" title="${prdInfo.name }"
								href="${prdInfo.url }">${prdInfo.name }</a></td>
							<td class="a">${prdInfo.price }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
	
	function prdSort(sortOption, selectTable){
		let site = null;
		console.log(selectTable);
		switch(selectTable){
		case "coopang":
			site = document.querySelector("tbody.tbodyTag.coopang");
			break;
		case "gmarket":
			site = document.querySelector("tbody.tbodyTag.gmarket");
			break;
		case "11st":
			site = document.querySelector("tbody.tbodyTag.11st");
			break;
		}
		let prdList = site.querySelectorAll("tr");
		let prdList_arr = Array.from(prdList);
		console.log(prdList_arr);
		let prdSort = [];
		prdSort[0] = prdList_arr.shift();
		for(let prd_arr of prdList_arr){
			let idx = -1;
			let sortCheck = false;
			console.log(prd_arr.querySelector(".a").innerText);
			prd_arr_num = Number(prd_arr.querySelector(".a").innerText)
			for(let sortIdx in prdSort){
				let prd_sort = Number(prdSort[sortIdx].querySelector("td.a").innerText);
				switch(sortOption){
					case "prd_desc":
						sortCheck = prd_arr_num > prd_sort
						break;
					case "prd_asc":
						sortCheck = prd_arr_num < prd_sort
						break;
				}
				if(sortCheck){
					idx = sortIdx;
					break;
				}
			}
			if(idx > -1){
				prdSort.splice(idx,0,prd_arr)
			}else{
				prdSort.push(prd_arr);
			}
		}
		site.innerHTML = "";
		for(let prd_sort of prdSort){
			site.appendChild(prd_sort);			
		}
	}
</script>
</html>