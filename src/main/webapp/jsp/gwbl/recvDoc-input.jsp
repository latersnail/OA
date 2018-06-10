<%--suppress JSDuplicatedDeclaration --%>
<%--suppress ALL --%>
<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/4/1
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<html>
<head>
    <title>流程办理界面</title>
    <%@include file="../../common/include/init.jsp"%>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
    <script type="text/javascript" src="<%=jsRoot%>json2.js"></script>
    <style>
        *{
            margin: 0;
            padding: 0;
            text-decoration: none;
            outline: none;
            border: none;
            list-style: none;
            font-weight: normal;
            font-family: "微软雅黑";
            font-size: 12px;
            color: #333;
        }
    </style>
</head>
<body class="easyui-layout">
<!--顶部-->
<div data-options="region:'north',border:false" style="height: 60px;" id="topToolbar">
    <div class="input-group col-md-5" style="float: left;right: -500px;">
        <button class="btn btn-primary btn-sm" type="button" style="margin-left: 10px;"  onclick="leaderOptions();" id="leaderOptions">填写意见</button>
        <button class="btn btn-info btn-sm" type="button" onclick="gotoNext('doDoc');" id="gotoNext">提交</button>
        <button class="btn btn-primary btn-sm" type="button" style="margin-left: 10px;"  id="doSubmit" onclick="doSubmit()">保存</button>
        <button class="btn btn-primary btn-sm" type="button"  style="margin-left: 10px;"  onclick="window.close()">取消</button>
    </div>
</div>
<!--左边  西-->
<div data-options="region:'west',border:false,split:false,collapsible:true,title:'流程选择导航栏'" style="width:150px;padding:1px;">
 <ul class="easyui-tree" data-options="" id="processTree"></ul>
</div>
<!--底部-->
<div data-options="region:'south',border:false,height:30" style="padding: 6px 0;text-align: center">
    Copy Right &copy 2018 江西理工大学(南昌校区) 毕业设计项目 版权所有
</div>
<!--中部-->
<div data-options="region:'center'">
 <div class="easyui-tabs" id="tWork" data-options="border:false,plain:false,fit:true">
 </div>
</div>
<!--选择下一步处理人 根据角色列出所有的选择人员-->
<div id="choosePerson" class="easyui-window" title="选择下一步处理人" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#choosefooter'" style="width:500px;height:300px;padding:10px;">
    <ul class="easyui-tree" id="choosePersonTree"></ul>
</div>
<div id="choosefooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnSave">保存</a>
    <a href="#" class="easyui-linkbutton" data-options="" style="width:90px;height:30px;" id="btnClose">取消</a>
</div>
<!--领导审批意见窗口-->
<div id="leaderWin" class="easyui-window" title="填写意见" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#leaderFooter'" style="width:500px;height:300px;padding:10px;">
    <textarea id="leaderOption" style="width: 400px;height: 200px;border: 1px solid black;">
    </textarea>
    <input type="hidden" id="serverTime"/>
    <input type="hidden" id="userName" value="${sessionScope.user.name}"/>
</div>
<div id="leaderFooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnSaveOption">保存</a>
    <a href="#" class="easyui-linkbutton"  style="width:90px;height:30px;" id="btnCloseOption">取消</a>
</div>
<!--流程监控页面-->
<div style="display: none" id="processMonitor">
    <div><img src="" id="processMonitorImage"/></div>
</div>
<!--需要保存的数据 -->
<input type="hidden" name="formId" value="" id="formId"/>
<input type="hidden" name="pid" value="${recvDoc.pid}" id="pid"/>
<input type="hidden" name="userId" id="userId" value="">
<input type="hidden" name="processName" id="processName" value=""/>
<input type="hidden" name="type" id="type" value="${type}"/>
<input type="hidden" name="takeActor" id="takeActor" value="">
<input type="hidden" name="taskId" id="taskId" value="${recvDoc.taskId}"/>
<input type="hidden" name="recvdocId" id="recvdocId" value="${recvDoc.id}"/>
<input type="hidden" name="doType" id="doType" value="complete">
<input type="hidden" name="isFirstUserTask" id="isFirstUserTask" value="${isFirstUserTask}"/>
<input type="hidden" name="isLastUserTask" id="isLastUserTask" value="${isLastUserTask}"/>
</body>
<script>
    function addTab(title, url,formId) {
        var id = '';
        if(formId){
            id = formId;
        }
        if ($('#tWork').tabs('exists', title)) {
            $('#tWork').tabs('select', title);
        } else {
            var content = '<iframe id="'+id+'"scrolling="auto" frameborder="0"  src="'+ url + '" style="width:100%;height:98%;"></iframe>';

            $('#tWork').tabs('add', {
                title : title,
                content : content,
                closable : true,
                fit:true
            });
        }
    }

    /**
    *@description 关联选中节点和勾选节点的联动性(注：由于easyui不提供单选 这里在逻辑上用复选框实现单选的功能)
    *@param node 操作的节点对象
    *@param type 操作类型
    *@param checked 判断节点前的复选框是否被勾选
    *@author  fangjiang
    *@date 2018/4/4 17:09
    */

    function treeClick(node,type,checked) {
        var treeDom = $("#processTree");
        var nodes = treeDom.tree('getChecked');
       if(type == 'click'){
           //取消所有之前勾选的数据
           for(var i=0;i<nodes.length;i++){
               if(nodes[i].id==node.id) continue;
               treeDom.tree('uncheck',nodes[i].target);
           }
           treeDom.tree('check',node.target);
           updateTabForm(node.id);
           $("#processName").val(node.text);
       }
       else if(type == 'check'&&checked==true){
           for(var i=0;i<nodes.length;i++){
             if(nodes[i].id==node.id) continue;
             treeDom.tree('uncheck',nodes[i].target);
           }
           updateTabForm(node.id);
           $("#processName").val(node.text);
       }
    }

    /**
    *@description 更新选项卡中的表单
    *@param formId 流程ID
    *@author  fangjiang
    *@date 2018/4/4 17:40
    */
    function updateTabForm(processId) {
        var tabDom = $('#tWork');
        var tab = tabDom.tabs('getSelected');
        var index = tabDom.tabs('getTabIndex',tab)
        tabDom.tabs('close',index);
        var obj = {};
        obj.processId = processId;
        doData('<%=basePath%>recvdoc/getFormInfoByPid',obj,function (data) {
            if(data){
               addTab(data.formName,'<%=basePath%>'+data.path,data.id);
               $('#formId').val(data.id);
               $('#formPath').val(data.path);
            }
        });
    }

    /**
    *@description 提供子页面初始化表单数据  子页面加载完成后就调用
    *@author  fangjiang
    *@date 2018/4/10 14:02
    */

    function initForm(callback) {
        if(callback){
            callback(JSON.parse('${recvDocJson}'),'${type}','${isFirstUserTask}');
        }
    }


    $(function () {
        setContextPath('<%=basePath%>');
        if("${type}"=='add'){
            $('#leaderOptions').hide();
            //新建流程 一开始加载空白表单模板
            if("${recvDoc.id}"!=''){
                var obj = {};
                obj.formId = '${recvDoc.formId}';
                doData('<%=basePath%>form/getFormById',obj,function (data) {
                    addTab(data.formName,'<%=basePath%>'+data.path,'${recvDoc.id}');
                });
            }else {
                window.onload = function () {
                    addTab('新建流程','<%=basePath%>template/blankTemplate.jsp');
                }
            }
        }
        else if("${type}"=="view"){
            $('body').layout('remove','west');
            $('#topToolbar').find('button').each(function () {
                if(this.id!=''){
                    $(this).hide();
                }
            });
            window.onload = function () {
                var obj = {};
                obj.formId = '${recvDoc.formId}';
                doData('<%=basePath%>form/getFormById',obj,function (data) {
                    addTab(data.formName,'<%=basePath%>'+data.path,'${recvDoc.id}');
                });
            }
        }
        else{
            //去除左边的新建流程的流程树窗口
            $('body').layout('remove','west');
            if("${isFirstUserTask}"=='true'){
                $('#leaderOptions').hide();
            }
            window.onload = function () {
               var obj = {};
               obj.formId = '${recvDoc.formId}';
               doData('<%=basePath%>form/getFormById',obj,function (data) {
                  addTab(data.formName,'<%=basePath%>'+data.path,'${recvDoc.id}');
               });
              /*
                var pid = $('#pid').val();
                document.getElementById('processMonitorImage').src = 'recvdoc/processMonitor?pid='+pid;
                $('#tWork').tabs('add', {
                    title : '流程跟踪图',
                    content : '',
                    closable : $('#processMonitor').html(),
                    selected: false,
                    fit:true
                });
                */
           }
        }

        //加载流程树
        $("#processTree").tree({
            url:'<%=basePath%>processInfo/getProcessTree',
            method: 'post',
            animate: true,
            checkbox: true,
            onlyLeafCheck: true,
            onClick: function(node){
                treeClick(node,'click');
            },
            onCheck: function (node,checked) {
                treeClick(node,'check',checked);
            }
        });

    });
</script>
<script src="<%=jsRoot%>recvDoc-input.js"></script>
</html>
