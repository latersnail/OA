<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/3/27
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@include file="../../common/include/init.jsp"%>
<script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
<html>
<head>
    <title>角色</title>
</head>
<body>
<table class="easyui-datagird" title="角色信息管理" style="height: auto" id="actorInfo"
       data-options="rownumbers:true,pagination:true,collapsible:true,striped:true,fit:true,toolbar:'#toolbar'">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'actorName',width:200">角色名称</th>
        <th data-options="field: 'ids',width:200,formatter:operator" >操作</th>
    </tr>
    </thead>
</table>
<div id="toolbar" style="" class="input-group col-md-7">
    <input class="form-control" placeholder="请输入你要查找的角色名称" id="actorName" style="width:250px;height: 30px"/>
    <span>
     <button class="btn btn-info btn-sm" type="button" id="serarchActor" >搜索</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="addActor">添加</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="delAll" onclick="delAll()">批量删除</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="editActor">编辑</button>
     <button class="btn btn-primary btn-sm" style="margin-left: 10px;" id="setSource">资源设置</button>
 </span>
</div>
<!--角色编辑与新增窗口-->
<div id="actorWin" class="easyui-window" title="角色新增" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#actorWinfooter'"
     style="width:500px;height:300px;padding:10px;">
    <form id="actorForm">
        <input type="hidden" name="id" id="actorId"/>
        <span>角色名称：</span><input type="text" name="actorName" placeholder="请输入角色名称"/>
    </form>
</div>
<div id="actorWinfooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnSave">保存</a>
    <a href="#" class="easyui-linkbutton"  style="width:90px;height:30px;" id="btnClose">取消</a>
</div>
</body>
<script>
    $(function () {
        //设置js的执行上下文环境
        setContextPath("<%=basePath%>");
        window.onload = function () {
            $('#actorInfo').datagrid('getPager').pagination({
                beforePageText: '第',
                afterPageText: '页   共{pages}页',
                displayMsg: '第{from}到{to}条，共{total}条'
            });
        }
    });
  /*
    function editActor(){
        var rows = $('#actorInfo').datagrid('getSelections');
        if (rows==null || rows.length==0)
        {
            alert("请选中需要修改的的角色！");
            return;
        }
        if(rows.length>1){
            alert("一次只能编辑一条数据");
            return;
        }
        showDialog('actor/input?id='+rows[0].id,300,300);
    }

    function addActor() {
        showDialog('actor/input',300,300);
    }
*/
    function delAll() {
        var rows = $('#actorInfo').datagrid('getSelections');
        if (rows==null || rows.length==0)
        {
            alert("请选中需要删除的角色！");
            return;
        }
       confirm('角色删除确认','你正在删除【'+rows.length+'】条数据，删除之后永久不能恢复',function (r) {
          if(r){
              var ids = [];
              for(var i=0;i<rows.length;i++){
                  ids.push(rows[i].id);
              }
              doData("<%=basePath%>actor/deleteActor",ids,function (data) {
                  if(data=="success"){
                      $.messager.show({
                          title: '提示',
                          msg: '已成功删除【'+rows.length+'】条数据'
                      });
                      delayRefresh();
                  }else{
                      $.messager.alert('提示','删除失败,错误代码'+data,'error');
                  }
              });
          }
       });

    }

    function showDialog(url, w, h){
        //新版本火狐谷歌不支持showModalDialog
        //showModalDialog(url, self, ‘dialogWidth=‘+w+‘px;dialogHeight=‘+h+‘px‘);
        window.open(url, self, 'modal=yes,Width='+w+'px,Height='+h+'px');
    }
</script>
<!--导入角色管理的js处理文件-->
<script type="text/javascript" src="<%=jsRoot%>actor.js"></script>
</html>
</body>
</html>
