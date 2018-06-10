<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/4/14
  Time: 9:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/include/init.jsp"%>

<html>
<head>
    <title>主页</title>
    <script src="<%=pluginRoot%>unslider/unslider.min.js"></script>
    <style>
        html, body { font-family: Segoe, "Segoe UI", "DejaVu Sans", "Trebuchet MS", Verdana, sans-serif;overflow:hidden;}

        ul, ol { padding: 0;}

        .banner-head{
          position: absolute;
          color: #66512c;
          font-size: 20px;
          font-weight: 800;
          font-family: "Adobe 仿宋 Std R";
          background-color: rgba(0,0,0,0.3);
          right: 0;
          width: 200px;
          top:0;
        }
        .fastOffice{
            background: rgba(223,240,250,0.3);
            display: inline-block;
            position: absolute;
            right: 0;
            top: 30px;
            width: 200px;
            height: 600px;
            border: 1px solid #C7EDCC;

        }
        .banner { position: relative; overflow: auto; text-align: center;top: 30px;}

        .banner li { list-style: none; }

        .banner ul li { float: left; }
        #b04 { width: 600px;position: relative;left: 10%;}

        #b04 .dots { position: absolute; left: 0; right: 0; bottom: 20px;}

        #b04 .dots li

        {

            display: inline-block;

            width: 10px;

            height: 10px;

            margin: 0 4px;

            text-indent: -999em;

            border: 2px solid #fff;

            border-radius: 6px;

            cursor: pointer;

            opacity: .4;

            -webkit-transition: background .5s, opacity .5s;

            -moz-transition: background .5s, opacity .5s;

            transition: background .5s, opacity .5s;

        }

        #b04 .dots li.active

        {

            background: #fff;

            opacity: 1;

        }

        #b04 .arrow { position: absolute; top: 200px;}

        #b04 #al { left: 15px;}

        #b04 #ar { right: 15px;}

    </style>
</head>
<body>
<div class="banner-head">
<div style="display: inline-block;float: right;margin-right: 70px;">公告栏</div>
</div>
<div>
    <div class="banner" id="b04">
        <ul>
            <li><img src="<%=basePath%>common/images/lunbo/01.jpg" alt="" width="640" height="390" ></li>

            <li><img src="<%=basePath%>common/images/lunbo/02.jpg" alt="" width="640" height="390" ></li>

            <li><img src="<%=basePath%>common/images/lunbo/03.jpg" alt="" width="640" height="390" ></li>

            <li><img src="<%=basePath%>common/images/lunbo/04.jpg" alt="" width="640" height="390" ></li>

            <li><img src="<%=basePath%>common/images/lunbo/05.jpg" alt="" width="640" height="390" ></li>

        </ul>

        <a href="javascript:void(0);" class="unslider-arrow04 prev"><img class="arrow" id="al" src="<%=basePath%>common/images/lunbo/arrowl.png" alt="prev" width="20" height="35"></a>

        <a href="javascript:void(0);" class="unslider-arrow04 next"><img class="arrow" id="ar" src="<%=basePath%>common/images/lunbo/arrowr.png" alt="next" width="20" height="37"></a>
    </div>
    <div class="fastOffice">

    </div>
</div>
</body>
<script>
    $(document).ready(function(e) {

        var unslider04 = $('#b04').unslider({

                dots: true

            }),

            data04 = unslider04.data('unslider');

        $('.unslider-arrow04').click(function() {

            var fn = this.className.split(' ')[1];
            data04[fn]();
        });

    });
</script>
</html>
