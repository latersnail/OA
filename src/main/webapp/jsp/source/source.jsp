<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/4/11
  Time: 22:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>资源列表</title>
    <%@include file="../../common/include/init.jsp"%>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
    <script type="text/javascript" src="<%=jsRoot%>tree.js"></script>
</head>
<body>
<table class="easyui-datagird" title="资源列表" style="height: auto" id="sourceInfo"
       data-options="rownumbers:true,pagination:true,collapsible:true,striped:true,fit:true,toolbar:'#toolbar'">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'name',width:200">资源名称</th>
        <th data-options="field:'sourcePath',width:200">资源路径</th>
        <th data-options="field:'sourceCodeName',width:200,formatter:formatSourceCode">资源组</th>
        <th data-options="field:'sourceCode',hidden:true"></th>
        <th data-options="field: 'ids',width:200,formatter:operator">操作</th>
    </tr>
    </thead>
</table>
<div id="toolbar" style="" class="input-group col-md-10">
    <input class="form-control" placeholder="请输入你要查找的资源名称" id="sourceName" style="width:250px;height: 30px"/>
    <span>
     <button class="btn btn-info btn-sm" type="button" id="serarchSource" >搜索</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="addSource">新增</button>
        <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="editSource">编辑</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="delAllSource">批量删除</button>
 </span>
</div>

<!--资源组设置-->
<div id="chooseSourceGroup" class="easyui-window" title="设置资源组" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#chooseGroupfooter'"
     style="width:500px;height:300px;padding:10px;">
    <ul id="chooseSourceGroupTree" class="easyui-tree"></ul>
</div>

<div id="chooseGroupfooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnSourceSave">保存</a>
    <a href="#" class="easyui-linkbutton" data-options="" style="width:90px;height:30px;" id="btnSourceClose">取消</a>
</div>

<!-- 资源新增和编辑-->
<div id="addSourceWin" class="easyui-window" title="设置资源组" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#addSourceWinfooter'"
     style="width:500px;height:300px;padding:10px;">
    <form id="sourceForm">
        <input type="hidden" name="id" />
        <input type="hidden" name="sourceCode" id="sourceCode"/>
        <span>资源名称:</span><input type="text" name="name" /><br/>
        <span>资源分组:</span><input type="text" name="sourceCodeName" id="sourceCodeName" readonly="readonly" />
        <button class="btn btn-sm btn-primary" type="button" onclick="setSourceGroup()">设置分组</button><br/>
        <span>资源路径:</span><input type="text" name="sourcePath" id="sourcePath"/>
        <button class="btn btn-sm btn-primary" type="button" onclick="checkSource()">检测资源</button>
    </form>
</div>
<div id="addSourceWinfooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnAddSave">保存</a>
    <a href="#" class="easyui-linkbutton" data-options="" style="width:90px;height:30px;" id="btnAddClose">取消</a>
</div>
<input type="hidden" id="sourceGroupName" name="sourceGroupName"/>
<input type="hidden" id="isChecked" name="isChecked"/>
</body>
<script>
    $(function () {
        setContextPath("<%=basePath%>");
        window.onload = function () {
            $('#sourceInfo').datagrid('getPager').pagination({
                beforePageText: '第',
                afterPageText: '页   共{pages}页',
                displayMsg: '第{from}到{to}条，共{total}条'
            });
        }
    });
//    window.onload = function () {
//        $('#chooseForm').window('vcenter');
//    };
</script>
<script type="text/javascript" src="<%=jsRoot%>source.js"></script>
</html>
