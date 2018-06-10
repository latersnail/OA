/**
 * Created by fangjiang on 2018/3/30.
 */
var contextPath = '';

function setContextPath(rootPath) {
    this.contextPath = rootPath;
}

//用户一般操作
function operator(value,rowData,rowIndex) {
    return  "<a href='#' processName='"+rowData.name+"' processId='"+
        rowData.id+"' type='delProcess' onclick='delProcess(this)'>删除</a>"
}

function find() {
    var obj = new Object();
    obj.condition = $("#processName").val();
    findByPage(contextPath+'processInfo/getProcessDefinitionList','processInfo',obj,function () {
    });
}

//删除流程
function delProcess($this){
    var id = $($this).attr('processId');
    var processName = $($this).attr('processName');
    $.messager.confirm('删除操作','您正在删除【'+processName+'】,删除之后将永久不能回复',function (r) {
        if(r){
            var obj = [id];
            doData(contextPath+'processInfo/deleteDefinition',obj,function(data){
                if(data=="success"){
                    $.messager.show({
                        title: '提示',
                        msg: '已成功删除流程定义文件【'+processName+'】'
                    });
                   delayRefresh();
                }else{
                    $.messager.alert('提示','删除失败,错误代码'+data,'error');
                }
            });
        }
    });
}

function doUpload() {
    $.messager.progress({title: '文件上传',text:'流程定义文件正在上传...'});
    $('#processUpload').submit();

}
function callback(type) {
    $.messager.progress('close');
    if(type=="success"){
        showMessage('提示','文件上传成功');
        delayRefresh();
    }else {
        alert('错误提示','文件上传失败','error');
    }

}

//触发文件选择窗口
function addProcess() {
    $('#processFile').click();
}

$(function () {
    find();
    //按条件搜索
    $("#serarchProcess").click(function () {
        find();
    });
    //给流程挂接表单
    $("#setForm").click(function () {
        var rows = $("#processInfo").datagrid('getSelections');
        if(rows.length>1){
            alert('一次只能操作一条数据');
            return;
        }
        if(rows.length==0){
            alert('请选择一条数据进行挂接表单')
            return;
        }
        $("#btnSave").show();
        $("#btnClose").show();
        $("#chooseForm").panel({title:'设置流程表单'})
        $("#chooseForm").window('open');
    });

    //选择保存则设置流程表单
    $("#btnSave").click(function () {
        var rows = $("#processInfo").datagrid('getSelections');
        var nodes = $("#chooseFormTree").tree("getChecked");
        if(nodes.length>1){
            alert('一个流程只能挂接一张表单');
            return;
        }
        if(nodes.length==0){
            alert('尚未给当前流程设置表单');
            return;
        }
        var obj = new Object();
        obj.processId =rows[0].id;
        obj.formId = nodes[0].id;
        doData(contextPath+'recvdoc/setFormForProcess',obj,function (data) {
            if(data=="success"){
                showMessage('提示','成功给选择的流程设置表单');
                $("#chooseForm").window('close');
            }else{
                alert('错误提示','给流程设置表单失败','error');
            }
        });

    });

    //取消则关闭选择窗口
    $("#btnClose").click(function () {
        $("#chooseForm").window('close');
    });

    //流程图查看
    $('#processView').click(function () {
        var rows = $("#processInfo").datagrid('getSelections');
        if(rows.length==0){
            alert('请选择一条流程定义数据!');
            return;
        }
       if(rows.length>1){
            alert('一次只能产看一条流程定义数据的流程图!');
            return;
       }
       var pdi = rows[0].id;
        document.getElementById('processImage').src = contextPath+'processInfo/getDeploymentSource?type=image&processDefinitionId='+pdi;
        openEasyWin('viewImageClose',null,'viewImageWin','流程图');
    });
    $('#viewImageClose').click(function () {
        $('#viewImageWin').window('close');
    });
    //流程定义文件查看
    $('#processMessage').click(function () {
        var rows = $("#processInfo").datagrid('getSelections');
        if(rows.length==0){
            alert('请选择一条流程定义数据！');
            return;
        }
        if(rows.length>1){
            alert('一次只能产看一条流程定义数据的流程定义文件！');
            return;
        }
        var pdi = rows[0].id;
        $.ajax({
            type: 'get',
            url: contextPath+'processInfo/getDeploymentSource?type=xml&processDefinitionId='+pdi,
            success:function (data) {
                $('#processXml').text(data);
            },
            error: function (arg0) {
            }
        });
        openEasyWin('viewXmlClose',null,'viewXmlWin','流程定义文件');
    });
    $('#viewXmlClose').click(function () {
        $('#viewXmlWin').window('close');
    });

});
