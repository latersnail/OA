<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/3/27
  Time: 22:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/include/init.jsp"%>
<html>
<head>
    <title>添加组织结构</title>
    <!--引入弹出窗口的共有样式-->
    <link type="text/css" rel="stylesheet" href="<%=cssRoot%>openWin.css"/>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
</head>
<body>
    <div class="openWin-container">
        <div class="openWin-title">
            <span>组织结构添加</span>
        </div>
        <div class="input-group openWin-input">
            <div>
                <span>组织结构名称：</span><br/>
                <input placeholder="组织机构名称" id="name" style="width: 200px;"/>
            </div>

            <div class="operation">
                <button class="btn-close btn btn-sm btn-primary" onclick="window.close()">取消</button>
                <button class="btn-confirm btn btn-sm btn-primary"  onclick="doSubmit()">确认</button>
            </div>
        </div>
    </div>
</body>
<script>
    function doSubmit() {
        var obj = new Object();
        obj.name = $("#name").val();
        if($.trim(obj.name)==''||obj.name==null){
            $.messager.show({
                title: '提示',
                msg: '角色名称不能为空'
            });
            return;
        }
        doData("<%=basePath%>organization/insertOrganization",obj,function (data) {
            if(data=="success"){
                $.messager.show({
                    title: '提示',
                    msg: '已成功添加一条数据'
                });
                window.opener.find();
                window.close();
            }else{
                $.messager.alert('提示','添加失败,错误代码'+data,'error');
            }
        })
    }
</script>
</html>
