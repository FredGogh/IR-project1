<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.io.*" %>
<%
String filenamen=request.getParameter("filename");
String filename=filenamen.replace(".xml",".pdf");
System.out.println(filename);
String basepath="E:/Personal_File/Third_1/Info_search/file/"+filename;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<base href="<%=basepath %>">
</head>
<body>
<%	
out.clear();
out=pageContext.pushBody();
response.setContentType("application/pdf");
File file=new File(basepath);
DataOutputStream temps=new DataOutputStream(response.getOutputStream());
DataInputStream in=new DataInputStream(new FileInputStream(basepath));
byte[] b=new byte[888888];
while((in.read(b))!=-1){
	temps.write(b);
	temps.flush();
}      
%>



</body>
</html>