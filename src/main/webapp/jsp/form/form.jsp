<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/4/1
  Time: 16:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>表单管理</title>
    <%@include file="../../common/include/init.jsp"%>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
</head>
<body>
<table class="easyui-datagird" title="表单列表" style="height: auto;margin-bottom: 50px;" id="formInfo"
       data-options="rownumbers:true,pagination:true,collapsible:true,striped:true,fit:true,toolbar:'#toolbar'">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'formName',width:200">表单名称</th>
        <th data-options="field:'path',width:200">表单路径</th>
        <th data-options="field:'createTime',width:200,formatter:formatDate">创建时间</th>
        <th data-options="field: 'ids',width:200,formatter:operator" >操作</th>
    </tr>
    </thead>
</table>
<div id="toolbar" style="" class="input-group col-md-8">
    <input class="form-control" placeholder="请输入你要查找的表单名称" id="keyword" style="width:250px;height: 30px"/>
    <span>
     <button class="btn btn-info btn-sm" type="button" id="serarchForm" onclick="serarchForm()">搜索</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="addForm" onclick="addForm()">添加</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="delForm" onclick="delAllForm()">批量删除</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="editForm" onclick="editForm()">编辑</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="viewForm" onclick="viewForm()">查看表单</button>
 </span>
</div>
<!--表单添加与编辑-->
<div id="addFormWin" class="easyui-window" title="表单添加" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#addFormfooter'" style="width:400px;height:300px;padding:20px;">
   <form id="commonForm" action="<%=basePath%>form/saveOrUpdateForm" method="post">
       <input type="hidden" name="id" id="formId" value=""/>
       <table cellpadding="5">
           <tr>
               <th style="font-size: 14px">表单名称:</th>
               <th><input type="text" name="formName" id="formName" value=""/></th>
           </tr>
           <tr>
               <th style="font-size: 14px">表单资源路径</th>
               <th><input type="text" name="path" id="path" value=""/> </th>
               <th> <button type="button" class="btn btn-primary btn-sm" style="margin-left: 10px;"  onclick="checkData()">检验资源</button></th>
           </tr>
       </table>
   </form>
</div>
<div id="addFormfooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnSave" onclick="doSubmit()">保存</a>
    <a href="#" class="easyui-linkbutton" data-options="" style="width:90px;height:30px;" id="btnClose" onclick="winClose()">取消</a>
</div>
</body>
<script>
    $(function () {
        setContextPath("<%=basePath%>");
        window.onload = function () {
            $('#formInfo').datagrid('getPager').pagination({
                beforePageText: '第',
                afterPageText: '页   共{pages}页',
                displayMsg: '第{from}到{to}条，共{total}条'
            });
        }
    })
</script>
<script type="text/javascript" src="<%=jsRoot%>form.js"></script>
</html>
