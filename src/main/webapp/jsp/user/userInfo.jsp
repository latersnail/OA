<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/3/25
  Time: 21:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/include/init.jsp"%>
<script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
<!--引入对 easyui tree 的封装-->
<script type="text/javascript" src="<%=jsRoot%>tree.js"></script>
<html>
<head>
    <title>用户管理</title>
</head>
<body>
 <table class="easyui-datagird" title="用户信息管理" style="height: auto" id="userInfo"
  data-options="rownumbers:true,pagination:true,collapsible:true,striped:true,fit:true,toolbar:'#toolbar'">
   <thead>
   <tr>
       <th data-options="field:'id',checkbox:true"></th>
       <th data-options="field:'name',width:100">姓名</th>
       <th data-options="field:'loginName',width:100">登录名称</th>
       <th data-options="field:'sex',width:100">性别</th>
       <th data-options="field:'telphone',width:120">电话号码</th>
       <th data-options="field:'actorName',width:120,formatter:formatActor">角色</th>
       <th data-options="field: 'orgName',width:100">组织结构</th>
       <th data-options="field: 'status',width:120,formatter:formatStatus">用户状态</th>
       <th data-options="field: 'actorId',hidden:true"></th>
       <th data-options="field: 'orgId',hidden:true"></th>
       <th data-options="field: 'ids',width:120,formatter:operator" >操作</th>
   </tr>
   </thead>
 </table>
<div id="toolbar" style="" class="input-group col-md-7">
 <input class="form-control" placeholder="请输入你要查找的用户名" id="loginName" style="width:250px;height: 30px"/>
 <span>
     <button class="btn btn-info btn-sm" type="button" id="serarchUser" >搜索</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="addUser">添加</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="delAll">批量删除</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="editUser">编辑</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="setActor">角色分配</button>
 </span>
</div>
<!--用户添加窗口-->
 <div id="userWind" class="easyui-window" title="用户添加" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#userFooter'" style="width:500px;height:300px;padding:10px;">
     <form id="userForm">
         <input type="hidden" name="id"/>
         <span>用户姓名:</span> <input type="text" name="name"><br/>
         <span>登录名:</span><input type="text" name="loginName"><br/>
         <span>登录密码:</span><input type="password" name="password" id="password"><br/>
         <span>再次输入密码:</span><input type="password" name="repassword" id="repassword"/><br/>
         <span>性别:</span><input type="radio" name="sex" value="男">男<input type="radio" name="sex" value="女"/>女 <br/>
         <span>电话号码:</span><input type="text" name="telphone"><br/>
         <span>角色:</span><input type="text" name="actorId" id="actorId" readonly="readonly" actorId="">
         <button type="button" class="btn btn-primary btn-sm" style="margin-left: 20px;height: 25px;" id="openActorWin">角色设置</button><br/>
         <span>组织结构:</span><input type="text" name="orgId" id="orgId" readonly="readonly"  orgId="">
         <button type="button" class="btn btn-primary btn-sm" style="margin-left: 20px;height: 25px;" id="openOrgWin">设置机构</button>
     </form>
 </div>
 <div id="userFooter" style="padding:5px;text-align: center;">
     <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnUserSave">保存</a>
     <a href="#" class="easyui-linkbutton"  style="width:90px;height:30px;" id="btnUserClose">取消</a>
 </div>
<!--角色选择-->
 <div id="actorWin" class="easyui-window" title="用户添加" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#actorFooter'" style="width:500px;height:300px;padding:10px;">
   <ul class="easyui-tree" id="actorTree"></ul>
 </div>
 <div id="actorFooter" style="padding:5px;text-align: center;">
     <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnActorSave">保存</a>
     <a href="#" class="easyui-linkbutton"  style="width:90px;height:30px;" id="btnActorClose">取消</a>
 </div>
<!--组织结构选择-->
 <div id="orgWin" class="easyui-window" title="用户添加" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#orgFooter'" style="width:500px;height:300px;padding:10px;">
     <ul class="easyui-tree" id="orgTree"></ul>
 </div>
 <div id="orgFooter" style="padding:5px;text-align: center;">
     <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnOrgSave">保存</a>
     <a href="#" class="easyui-linkbutton"  style="width:90px;height:30px;" id="btnOrgClose">取消</a>
 </div>
 <!--中间数据-->
<input type="hidden" id="userId" value=""/>
</body>
<script>
    $(function () {
        //设置js的执行上下文环境
        setContextPath("<%=basePath%>");
        window.onload = function () {
            $('#userInfo').datagrid('getPager').pagination({
                beforePageText: '第',
                afterPageText: '页   共{pages}页',
                displayMsg: '第{from}到{to}条，共{total}条'
            });
        }
    });
</script>
<!--导入用户管理的js处理文件-->
<script type="text/javascript" src="<%=jsRoot%>userInfo.js"></script>
</html>
