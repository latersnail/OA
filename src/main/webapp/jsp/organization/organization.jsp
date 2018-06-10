<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/3/27
  Time: 22:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/include/init.jsp"%>
<html>
<head>
    <title>组织结构管理</title>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
</head>
<body>
<html>
<head>
    <title>组织机构管理</title>
</head>
<body>
<table class="easyui-datagird" title="组织机构管理" style="height: auto" id="orgInfo"
       data-options="rownumbers:true,pagination:true,collapsible:true,striped:true,fit:true,toolbar:'#toolbar'">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'name',width:200">组织名称</th>
        <th data-options="field: 'ids',width:200,formatter:operator" >操作</th>
    </tr>
    </thead>
</table>
<div id="toolbar" style="" class="input-group col-md-7">
    <input class="form-control" placeholder="请输入你要查找的组织名称" id="name" style="width:250px;height: 30px"/>
    <span>
     <button class="btn btn-info btn-sm" type="button" id="serarchOrg" >搜索</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="addOrg">添加</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="delOrg">批量删除</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="editOrg">编辑</button>
 </span>
</div>
<!--组织结构编辑与新增窗口-->
<div id="orgWin" class="easyui-window" title="组织机构" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#orgWinfooter'"
     style="width:500px;height:300px;padding:10px;">
    <form id="orgForm">
        <input type="hidden" name="id" id="orgId"/>
        <span>组织机构名称：</span><input type="text" name="name" placeholder="请输入组织机构名称"/>
    </form>
</div>
<div id="orgWinfooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnSave">保存</a>
    <a href="#" class="easyui-linkbutton"  style="width:90px;height:30px;" id="btnClose">取消</a>
</div>
</body>
<script>
    $(function () {
        //设置js的执行上下文环境
        setContextPath("<%=basePath%>");
    });
</script>
<!--导入组织管理的js处理文件-->
<script type="text/javascript" src="<%=jsRoot%>organization.js"></script>
</html>
</body>
</html>

