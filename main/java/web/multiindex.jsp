<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style>
    body{
        margin: 0;
        padding:0;
    }
    .header{
        height:100vh;
        background-image:url(background3.jpg);
        background-size: cover;
        background-position: center;
        display:flex;
        justify-content:center;
        align-items: center;
        font-family: sans-serif;
    }
    h1{

        position: absolute;
        top:30%;
        right:900px;
        color:#fff;
        margin-bottom:60px;
        font-size:45px;
        letter-spacing:2px;
        text-align:center;
        animation: nty 10s;

    }
    .search-field{
        position:absolute;
        background-color:white;
        width:500px;
        top:50%;
        left:35%;
        border-radius:25px;
        height:40px;
        padding:3px;
    }

    .business{
        position:absolute;
        left:20px;
        border:none;
        background:none;
        outline:none;
        width:300px;
        transition:.4s;
        line-height:40px;
        font-size:20px;
        color:black;
    }

    .search-button{
        width:30px;


    }
    .ui{
        position:absolute;
        left:550px;
        top:350px;
        height:100px;
        width:100px;
    }
    .ch{
        background: #eb94d0;
        /* 创建渐变 */
        background-image: -webkit-linear-gradient(top, #eb94d0, #2079b0);
        background-image: -moz-linear-gradient(top, #eb94d0, #2079b0);
        background-image: -ms-linear-gradient(top, #eb94d0, #2079b0);
        background-image: -o-linear-gradient(top, #eb94d0, #2079b0);
        background-image: linear-gradient(to bottom, #eb94d0, #2079b0);
        /* 给按钮添加圆角 */
        -webkit-border-radius: 20px;
        -moz-border-radius: 20px;
        border-radius: 20px;
        text-shadow: 3px 2px 1px #9daef5;
        -webkit-box-shadow: 6px 5px 24px #666666;
        -moz-box-shadow: 6px 5px 24px #666666;
        box-shadow: 6px 5px 24px #666666;
        font-family: Arial;
        color: #fafafa;
        font-size: 20px;
        padding: 19px;
        text-decoration: none;
        /*font-weight:bold;
        color:red;
        text-decoration:none;*/
    }
    .ch:hover {
        background: #2079b0;
        background-image: -webkit-linear-gradient(top, #2079b0, #eb94d0);
        background-image: -moz-linear-gradient(top, #2079b0, #eb94d0);
        background-image: -ms-linear-gradient(top, #2079b0, #eb94d0);
        background-image: -o-linear-gradient(top, #2079b0, #eb94d0);
        background-image: linear-gradient(to bottom, #2079b0, #eb94d0);
        text-decoration: none;
    }
    .c{
        border-style:solid;
        border-width:1px;
        border-color:grey;
        float:right;
        width:41px;
        height:41px;
        border-radius:28px;
        display:flex;
        justify-content:center;
        align-items:center;
        transition:.4s;
    }
    /*动画*/
    @keyframes nty{
        from{color:transparent}
        to{color:#fff}
    }
    @keyframes nty1{
        from{width:100px}
        to{width:400px}
    }
</style>
<html>
<head>
    <meta charset="UTF-8">
    <title>搜索引擎</title>
    <script type="text/javascript">
        function checkboxlib(){
            var content= document.getElementsByName("contents")[0].value;
            var checkboxvalue=new Array();
            var checkboxtext=new Array();
            var checkboxStr=document.getElementsByName("choice");
            var option="multiindex";
            for(var i=0;i<checkboxStr.length;i++){
                if(checkboxStr[i].checked){
                    checkboxvalue.push(checkboxStr[i].value);
                    checkboxtext.push(checkboxStr[i].nextSibling.nodeValue);
                }
            }
            checkboxvalue=checkboxvalue.toString();
            checkboxtext=checkboxtext.toString();
            window.location='check.jsp?checkboxvalue='+checkboxvalue+"&option="+option+"&content="+content;

        }
    </script>
</head>
<body>
<div class="header">
    <form action="check.jsp" method="post">
        <h1> 多域查询 </h1>
        <form id="checkboxform" method="post" action="">
            <div class="ui" style="color:grey;" font-weight:bolder >
                <input type="checkbox" name="choice" value="content">正文</input><br>
                <input type="checkbox" name="choice" value="title">标题</input><br>
                <input type="checkbox" name="choice" value="region" >地区</input><br>
                <input type="checkbox" name="choice" value='author'>作者</input><br>
                <input type="checkbox" name="choice" value='abstract'>摘要</input><br>
                <input type="checkbox" name="choice" value='time'>时间</input><br>
                <input type="checkbox" name="choice" value='institution'>院校</input><br>
            </div>
        </form>

        <div class="search-field">


            <input type="text" class="business" name="contents">
            <a href="#" class="c">
                <input type="image" onclick="checkboxlib()" src="image3.jpg" class="search-button" />
            </a>
        </div>
        <input type="hidden" name="option" value="multiindex">
        <div style="position:absolute;top:60%;left:25%;font-size:1.5em;">
            <a href="index1.jsp" class="ch">普通查询</a>
            <a href="wildcardindex.jsp" class="ch">通配符查询</a>
            <a href="boolindex.jsp" class="ch">布尔查询</a>
            <a href="spanindex.jsp" class="ch">跨域查询</a>
            <a href="fuzzyindex.jsp" class="ch">模糊查询</a>
            <a href="weight_multiindex.jsp" class="ch">加权多域查询</a>
        </div>
        <%

        %>
    </form>
</div>

</body>
</html>
>