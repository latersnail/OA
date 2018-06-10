/**
 * Created by fangjiang on 2018/4/3.
 */
// contextPath js 执行上下环境
var contextPath = '';
function setContextPath(rootPath) {
   contextPath = rootPath;
}


//表单提交
function doSubmit() {
    $.messager.progress({title:'保存表单数据',text:'正在保存....'});
    //序列化表单数据。
    var obj = getSubWin().serializeForm("templateForm");
    var node = getSelectTreeNode();
    if(node) obj.pdi = node.id;
    var formId = $('#formId').val();
    var processName = $('#processName').val();
    var takeActor = $('#takeActor').val();

    if($.trim(formId)!='')
    obj.formId = formId;

    if($.trim(processName))
    obj.processName = processName;

    if($.trim(takeActor))
    obj.takeActor = takeActor;

    obj.id = $('#recvdocId').val();

    doData(contextPath+'recvdoc/saveRecvDoc',obj,function (data) {
       if(data=='success'){
           $.messager.progress('close');
           alert('表单信息保存成功');
           delayClose();
           getParWin().location.reload();
       }else {
           $.messager.progress('close');
           alert('错误提示','表单信息保存失败','error');
       }
    });


}

//填写审批意见
function leaderOptions() {
    $('#btnSaveOption').show();
    $('#btnCloseOption').show();
    $('#leaderWin').panel({title:'填写意见'});
    $('#leaderWin').window('open');
}

function getSelectTreeNode() {
    var treeDom = $("#processTree");
    //防止左边流程列表移除之后该方法异常 在此捕获一下 异常时返回null 异常信息无用不打出。
    try{
        var nodes = treeDom.tree('getChecked');
        if(nodes){
          return nodes[0];
        }else {
            return null;
        }
    }catch(e){
        return null;
    }
}

//获取服务器时间
function getServerTime() {
    doData(contextPath+'form/getServerTime',{},function (data) {
        $('#serverTime').val(data);
    });
}

//得到子窗口的windows对象
function getSubWin() {
   return window.frames[0];
}

//得到父窗口的window对象
function getParWin() {
   return window.opener;
}

//执行下一步
function gotoNext(workflowType) {
    var type = $('#type').val();
    var isLastUserTask = $('#isLastUserTask').val();
    if(type =='add'){
        $("#choosePersonTree").html('确认提交将转入你的待办.');
        openEasyWin('btnSave','btnClose','choosePerson','流程启动');
    }
    else if(isLastUserTask=="false"){
        var obj = getSubWin().serializeForm("templateForm");
        var isFirstUserTask = $('#isFirstUserTask').val();
        //如果是第一个用户任务节点 则个人是不需要审批任务。
        if(isFirstUserTask=='false'){
            if(obj.suggestion.indexOf($('#userName').val())==-1){
                alert('当前流程你尚未审批,请先审批在提交');
                return;
            }
        }
        //请求数据 如果是签收则不需要提供人员选择
        var obj = {};
        obj.pid = $('#pid').val();
        obj.taskId = $('#taskId').val();
        $.ajax({
            url:contextPath+'recvdoc/getExecutionPerson',
            method: 'post',
            dataType: 'json',
            data: JSON.stringify(obj),
            async: true,
            success: function (data) {
                //如果为复选框则下一步为签收 当前流程为设置签收人
                //加载下一步处理人员树
                $("#choosePersonTree").tree({
                    data:data,
                    animate: true,
                    checkbox: true,
                    onlyLeafCheck: true,
                    onClick: function(node){
                        if(node.type=='radio'){
                            treeClick2(node,'click');
                        }else {
                            treeClick3(node,'click');
                        }
                    },
                    onCheck: function (node,checked) {
                        if(node.type=='radio'){
                            treeClick2(node,'check',checked);
                        }else {
                            treeClick3(node,'check');
                        }

                    }
                });
            }
        });
        //留下100毫秒的时间用于渲染
        window.setTimeout("openEasyWin('btnSave','btnClose','choosePerson','选择下一步处理人')",100);
    }
    else if(isLastUserTask=="true"){
        $("#choosePersonTree").html('确认提交将结束流程。');
        openEasyWin('btnSave','btnClose','choosePerson','流程办结');
    }

}

function treeClick2(node,type,checked) {
    var treeDom = $("#choosePersonTree");
    var nodes = treeDom.tree('getChecked');
    if(type == 'click'){
        //取消所有之前勾选的数据
        for(var i=0;i<nodes.length;i++){
            if(nodes[i].id==node.id) continue;
            treeDom.tree('uncheck',nodes[i].target);
        }
        treeDom.tree('check',node.target);
       $('#takeActor').val(node.id);
    }
    else if(type == 'check'&&checked==true){
        for(var i=0;i<nodes.length;i++){
            if(nodes[i].id==node.id) continue;
            treeDom.tree('uncheck',nodes[i].target);
        }
        $('#takeActor').val(node.id);
    }
}

function treeClick3(node,type,checked) {
    var treeDom = $("#choosePersonTree");
    var nodes = treeDom.tree('getChecked');
    var ids = [];
    for(var i=0;i<nodes.length;i++){
        ids.push(nodes[i].id);
    }
    if(type == 'click'){
        treeDom.tree('check',node.target);
    }
    $('#takeActor').val(ids.join(','));
    $('#doType').val('sign');
}
function closeWin() {
   window.close();
}
function delayClose() {
    setTimeout('closeWin()',2000);
}

$(function () {
    getServerTime();
 //办理任务 则加载表单数据  获取iframe的window对象调用iframe初始化的方法
    //确定选择人员
    $("#btnSave").click(function () {
        $.messager.progress({title:'保存表单数据',text:'正在保存....'});
        var havePid = $('#pid').val();
        var obj = getSubWin().serializeForm("templateForm");
        if(havePid=='null'||typeof havePid =='undefined'||$.trim(havePid)==''){
            obj.pdi = getSelectTreeNode().id;
            obj.formId = $('#formId').val();
            obj.processName = $('#processName').val();
            doData(contextPath+'recvdoc/startProcess',obj,function (data) {
                if(data=="success"){
                    $("#choosePerson").window("close");
                    alert('流程启动成功，两秒之后窗口自动关闭！');
                    delayClose();
                }else {
                    alert('提示','流程启动失败','error');
                }
            });
        }else {
            var takeActor = $('#takeActor').val();
            var taskId = $('#taskId').val();
            var suggestion = obj.suggestion;
            var object = {};
            if($.trim(takeActor)!='')
                object.takeActor = takeActor;
            object.taskId = taskId;
            object.suggestion = suggestion;
            object.id = $('#recvdocId').val();
            object.type = 'complete';
            object.doType = $('#doType').val();
            $.messager.progress({title:'保存表单数据',text:'正在保存....'});
            doData(contextPath+'recvdoc/gotoNext',object,function (data) {
                if(data=="success"){
                    $("#choosePerson").window("close");
                     $.messager.progress('close');
                      alert("流程办理成功！");
                      delayClose();
                      //刷新页面数据
                      getParWin().location.reload();
                }else {
                    alert('错误提示','任务处理失败,请联系管理员','error')
                }
            });
        }
    });

    $("#btnClose").click(function () {
        $("#choosePerson").window("close");
    });

    $("#btnSaveOption").click(function () {
       var subDom = getSubWin().document;
        var oldOption =  subDom.getElementById('suggestion').value;
        var userName = $('#userName').val();
        if(oldOption.indexOf(userName)!=-1){
            alert('当前流程你已经审批了,可以提交下一步了');
            return;
        }
        if(oldOption!="")oldOption=oldOption+'\r\n';
        //在每条数据后面添加办理人姓名和办理时间
        var newOption = $('#leaderOption').val()+'\r\n     '+userName+
                        '   于'+Date.prototype.getLongDate($('#serverTime').val())+' 审批';
       subDom.getElementById('suggestion').value = oldOption + newOption;
       $('#leaderWin').window('close');
    });

    $('#btnCloseOption').click(function () {
        $("#leaderWin").window('close');
    });

    //每隔一分钟向服务器获取时间更新隐藏域数据
    window.setInterval('getServerTime',60000);

});
