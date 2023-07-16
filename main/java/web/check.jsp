<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ page import ="search.*" %>
<%@ page import ="java.util.ArrayList" %>
<%@ page import ="java.util.List" %>
<%@ page import="java.sql.Time" %>
<%@ page import="utils.*" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>
<style>
	.topse{
		height:80px;
		width:100%;
		left: 0px;
		top:0;
		padding:0px;
		background:lightskyblue;
		outline:none;
		position:fixed;
	}
	.link{
		color:blue;
		font-size:1.0em;
		text-decoration:none;
	}
	.link:hover{
		color:purple;
	}
	.framese{
		position:absolute;
		width:500px;
		height:30px;
		left:150px;
		top:8px;
		border-style:solid;
		border-radius:60px;
		border-width:thin;
		padding-left:15px;
		font-size:1.2em;
		color:lightgrey;
		outline:none;
	}
	.framese:hover{
		border-bottom-style:outset;
		border-bottom-width:4px;
		border-bottom-color:lightgrey;
		cursor:pointer ;
		color:grey;
	}
	.searchbutton{
		position:absolute;
		top:10px;
		left:630px;
		height:30px;
		width:30px;
		border-radius:0 50% 50% 0;
	}
	.zhaiyao{
		height:30px;
		width:1050px;
		font-size:0.8em;
		text-align:left;
		padding:2px;
		overflow:hidden;
		text-overflow:ellipsis;
		display:-webkit-box;
		-webkit-line-clamp:2;
		line-clamp:2;
		-webkit-box-orient:vertical;
	}
	.result{
		height:20px;
		position:absolute;
		top:45px;
		left:160px;
		text-decoration:none;
		color:black;
		font-size:8px;
		padding:1px;
	}
</style>
<body>


<%
	String choose=request.getParameter("choose");
	System.out.println("choose"+choose);
	String option=request.getParameter("option");

	//获取搜索内容
	String content=request.getParameter("content");
	String checkbox="";
	String content_1="";
	String content_2="";
	String weight="";
	//获取查询方式
	//String bool=request.getParameter("bool");
	//System.out.println(option);
	//获得结果
	System.out.println("content: "+content);
	List<List<String>> output=new ArrayList();
		/*if(bool!=null){
			System.out.println(1);*/
	long start_time = System.currentTimeMillis();
	System.out.println(option);
	if(option.equals("boolindex")){
		output=BooleanSearch.BooleanSearch(content);
	}
	else if(option.equals("wildcardindex")){
		output=WildCardSearch.WildCardSearch(choose,content);
	}
	else if(option.equals("normalindex")){
		System.out.println("choose"+choose);
		output=NormalSearch.NormalSearch(choose,content);
	}
	else if(option.equals("fuzzyindex")){
		output=FuzzySearch.FuzzySearch(choose,content);
	}
	else if(option.equals("multiindex")){
		checkbox = request.getParameter("checkboxvalue");
		String[] checkboxes = checkbox.split(",");
		System.out.println(content);
		output=MultiSearch.MultiSearch(checkboxes,content);
	}
	else if (option.equals("weight_multiindex")) {
		checkbox = request.getParameter("checkboxvalue");
		System.out.println(checkbox);
		String[] checkboxes = checkbox.split(",");
		weight = request.getParameter("weight");
		System.out.println(weight);
		String[] weights_string = weight.split(",");
		ArrayList<Float> weights_array = new ArrayList<>();
		for (int i = 0;i<weights_string.length;i++){
			weights_array.add(Float.valueOf(weights_string[i]));
		}
		Float[] weights = (Float[]) weights_array.toArray(new Float[weights_array.size()]);
		output=MultiSearch.WeightedMultiSearch(checkboxes,weights,content);
	}
	else if (option.equals("spanindex")){
		content_1=request.getParameter("content1");
		content_2=request.getParameter("content2");
		output=SpanSearch.SpanSearch(choose,content_1,content_2,5,false);
	}
	long end_time = System.currentTimeMillis();
	long consumetime = end_time - start_time;
%>




<%
	//分页
	String pageNow=request.getParameter("pageNow");
	int pagesize =10;
	int lineCount=0;
	int pageCount=0;
	int pagenow=1;
	if(pageNow !=null){
		pagenow=Integer.parseInt(pageNow);
	}
%>

<%
	if(!output.isEmpty()){
		lineCount=output.get(0).size();
		pageCount=lineCount % pagesize ==0 ? lineCount / pagesize :lineCount/pagesize +1;
		int st=(pagenow-1)*10;
		int en=pagenow*10>lineCount?lineCount:pagenow*10;
%>
<form action="check.jsp" method="post">
	<div class="topse">
		<input type="image" src="image1.jpg" style="left:55px;width:80px;height:80px;position:absolute"/>
		<input type="text" class="framese" name="content" value="<%=content%>"/>
		<input type="image" src="image3.jpg" class="searchbutton" />
		<input type="hidden" name="option" value="<%=option %>"/>
		<input type="hidden" name="choose" value="<%=choose %>"/>
		<div class="result" >
		<%="查询到<font color='red'>" +output.get(0).size()+"</font>条结果"%>&nbsp&nbsp&nbsp&nbsp
		<%="查询耗时:<font color='red'>"+consumetime+"ms </font>" %><br>
		</div>

	</div>



</form>



<div style="margin-top:85px;margin-left:150px">
	<%
		for(int j=st;st<=j&&j<en;j++){
	%>

	<div style="margin-top:5px;height:100px">
		<div style="height:20px;width:1050px;text-align:left;white-space:nowrap;overflow:hidden;text-overflow:ellipsis">
			<a href="show.jsp?filename=<%=output.get(0).get(j)%>" target="_blank" class="link"><%= output.get(1).get(j) %> <br></a>
		</div>
		<div style="height:15px;width:1050px;margin-top:3px;font-size:0.8em;text-align:left;padding:2px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis">
			<%= output.get(2).get(j)%>	<br>
		</div>
		<div class="zhaiyao">
			<%= output.get(8).get(j)%> <br>
		</div>
	</div>
	<%		}
	%>
	<div style="margin-top:30px;margin-bottom:40px;margin-left:30%;font-size:0.9em">
		<%
			for(int i=1;i<pagenow;i++){

		%>

		<a href="check.jsp?pageNow=<%=i%>&content=<%=content%>&option=<%=option %>&choose=<%=choose%>&checkboxvalue=<%=checkbox%>&content1=<%=content_1%>&content2=<%=content_2%>&weight=<%=weight%>" class="link"> <%=i %> &nbsp</a>
		<% } %>
		<%=pagenow %> &nbsp
		<%for(int i=pagenow+1;i<pageCount;i++){%>
		<a href="check.jsp?pageNow=<%=i%>&content=<%=content%>&option=<%=option %>&choose=<%=choose%>&checkboxvalue=<%=checkbox%>&content1=<%=content_1%>&content2=<%=content_2%>&weight=<%=weight%>" class="link"> <%=i %> &nbsp</a>
<%}%>
		<a href="index1.jsp" class="link">主页</a>
		<%  }
		else{%>
		<%="暂无结果"%><br>
<%--		<%="您要搜的是不是："+Spellcheck.suggest(content)[0]%>--%>
		<a href="index1.jsp">主页</a>
		<%} %>
	</div>
</div>
</body>
</html>