<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/4/14
  Time: 14:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/include/init.jsp"%>
<html>
<head>
    <title>权限管理</title>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
    <script type="text/javascript" src="<%=jsRoot%>tree.js"></script>
    <style>
        body{
            overflow: hidden;
            <%--background: url("<%=basePath%>common/images/bg.png") no-repeat;--%>
            <%--background-size: 100% 100%;--%>
            border: 1px solid black;
        }
       .source-content{
           display: inline-block;
           position: relative;
           left: 550px;
           height: 320px;
           width: 300px;
           border: 1px solid #00bbee;
           overflow: scroll;
       }
        .actor-content{
            display: inline-block;
            position: absolute;
            height: 320px;
            width: 300px;
            border: 1px solid #00bbee;
            left: 50px;
            overflow: scroll;
        }
        .footer{
            position: relative;
            left: 32%;
        }
        .middle-content{
            display: inline-block;
            position: absolute;
            left: 350px;
            top: 150px;
            width: 150px;
            height: 70px;
            background: url("<%=basePath%>common/images/arrow.gif");
            background-size: cover;
            text-align: center;
        }
    </style>
</head>
<body>
<div style="">
    <span style="display: block;position: relative;top: 0;margin-bottom: 10px;left: 35%;">角色权限管理</span>
    <div class="actor-content">
        <div>角色选择</div>
        <!--角色树-->
        <div id="chooseActor">
            <ul id="chooseActorTree" class="easyui-tree"></ul>
        </div>
    </div>
    <div class="middle-content">
    </div>
    <div class="source-content">
        <div>资源选择</div>
        <!--资源树-->
        <div id="chooseSource">
            <ul id="chooseSourceTree" class="easyui-tree"></ul>
        </div>
    </div>
    <div class="footer"><button type="button" class="btn btn-info btn-sm" onclick="doSubmit()">保存</button>
        <button class="btn btn-info btn-sm" style="margin-left: 10px;" onclick="uncheckAll();">重置资源</button>
        <button class="btn btn-info btn-sm" style="margin-left: 10px;" onclick="window.location.reload()">刷新</button>
    </div>
</div>
</body>
<script>
    $(function () {
      setContextPath('<%=basePath%>');
    });
</script>
<script type="text/javascript" src="<%=jsRoot%>actorSourceInfo.js"></script>
</html>
