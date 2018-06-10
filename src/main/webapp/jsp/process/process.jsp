<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/3/30
  Time: 20:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>流程列表</title>
    <%@include file="../../common/include/init.jsp"%>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
</head>
<body>
<table class="easyui-datagird" title="流程列表" style="height: auto" id="processInfo"
       data-options="rownumbers:true,pagination:true,collapsible:true,striped:true,fit:true,toolbar:'#toolbar'">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'name',width:200">流程名称</th>
        <th data-options="field:'category',width:200">流程描述</th>
        <th data-options="field:'version',width:150">版本号</th>
        <th data-options="field:'resourceName',width:200">bpmn文件名</th>
        <th data-options="field:'diagramResourceName',width:200">流程图</th>
        <th data-options="field: 'ids',width:50,formatter:operator" >操作</th>
    </tr>
    </thead>
</table>
<div id="toolbar" style="" class="input-group col-md-10">
    <input class="form-control" placeholder="请输入你要查找的流程名称" id="processName" style="width:250px;height: 30px"/>
    <span>
     <button class="btn btn-info btn-sm" type="button" id="serarchProcess" >搜索</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="addProcess" onclick="addProcess()">部署</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="delProcess">批量删除</button>
      <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="setForm">挂接表单</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="processView">查看流程图</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="processMessage">查看流程文件信息</button>
 </span>
</div>
    <!--流程表单挂接窗口-->
    <div id="chooseForm" class="easyui-window" title="流程表单设置" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#choosefooter'"
         style="width:500px;height:300px;padding:10px;">
        <ul id="chooseFormTree" class="easyui-tree"></ul>
    </div>
    <div id="choosefooter" style="padding:5px;text-align: center;">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnSave">保存</a>
        <a href="#" class="easyui-linkbutton" data-options="" style="width:90px;height:30px;" id="btnClose">取消</a>
    </div>
<!--流程图查看窗口-->
<div id="viewImageWin" class="easyui-window" title="流程表单设置" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#viewImagefooter'"
     style="width:800px;height:400px;padding:10px;">
     <img src="" id="processImage" style="height: 390px;width: 790px;">
</div>
<div id="viewImagefooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:''" style="width:90px;height:30px;" id="viewImageClose">关闭</a>
</div>
<!--流程定义文件看窗口-->
<div id="viewXmlWin" class="easyui-window" title="流程表单设置" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#viewXmlfooter'"
     style="width:600px;height:400px;padding:10px;">
     <span id="processXml"></span>
</div>
<div id="viewXmlfooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:''" style="width:90px;height:30px;" id="viewXmlClose">关闭</a>
</div>
<iframe name="uploadTarget" id="uploadTarget" style="display: none;"></iframe>
<!--文件上传-->
<form  style="display: none;" enctype="multipart/form-data"
       action="<%=basePath%>processInfo/uploadProcess" target="uploadTarget"
       method="post" id="processUpload">
    <input id="processFile" type="file" name="file" onchange="doUpload()"
           accept=".zip" />
</form>
</body>
<script>
  $(function () {
      setContextPath("<%=basePath%>");
      $('#chooseFormTree').tree({
          url: '<%=basePath%>form/getFormTree',
          method: 'post',
          animate: true,
          checkbox: true,
          onlyLeafCheck: true
      });
      window.onload = function () {
          $('#processInfo').datagrid('getPager').pagination({
              beforePageText: '第',
              afterPageText: '页   共{pages}页',
              displayMsg: '第{from}到{to}条，共{total}条'
          });
      }
  });
//  window.onload = function () {
//      $('#chooseForm').window('vcenter');
//  };
</script>
<script type="text/javascript" src="<%=jsRoot%>process.js"></script>
</html>
