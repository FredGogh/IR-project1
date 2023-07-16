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
        left:100px;
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
        -webkit-border-radius: 30;
        -moz-border-radius: 30;
        border-radius: 30px;

        position:absolute;
        left:10px;
        top:7px;
        height:30px;
        width:60px;
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
        -webkit-border-radius: 20;
        -moz-border-radius: 20;
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
</head>
<body>
<div class="header">
    <form action="check.jsp" method="post">
        <h1> 模糊查询 </h1>
        <div class="search-field">
            <select size='1' name="choose" class="ui">
                <option value="content" selected="selected">正文</option>
                <option value="title">标题</option>
                <option value="region">地区</option>
                <option value='author'>作者</option>
                <option value='abstract'>摘要</option>
                <option value='time'>时间</option>
                <option value='institution'>机构</option>
            </select>
            <input type="text" class="business" name="content" >
            <a href="#" class="c">
                <input type="image" src="image3.jpg" class="search-button" />
            </a>
        </div>
        <input type="hidden" name="option" value="fuzzyindex">
        <div style="position:absolute;top:60%;left:25%;font-size:1.5em;">
            <a href="index1.jsp" class="ch">普通查询</a>
            <a href="wildcardindex.jsp" class="ch">通配符查询</a>
            <a href="boolindex.jsp" class="ch">布尔查询</a>
            <a href="spanindex.jsp" class="ch">跨域查询</a>
            <a href="multiindex.jsp" class="ch">多域查询</a>
            <a href="weight_multiindex.jsp" class="ch">加权多域查询</a>
        </div>
        <%

        %>
    </form>
</div>

</body>
</html>
>