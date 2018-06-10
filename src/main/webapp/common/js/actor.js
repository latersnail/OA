/**
 * Created by fangjiang on 2018/3/27.
 */
var contextPath = '';
function setContextPath(rootPath) {
    contextPath = rootPath;
}

//用户一般操作
function operator(value,rowData,rowIndex) {
    return  "<a href='#' actorName='"+rowData.actorName+"' actorId='"+
        rowData.id+"' type='delActor' onclick='delActor(this)'>删除</a>"
}

function find() {
    var obj = new Object();
    obj.condition = $('#actorName').val();
    findByPage(contextPath+'actor/findAcotrByPage','actorInfo',obj,function () {
    });
}
function delActor($this) {
    var id =$($this).attr('actorId');
    var name = $($this).attr('actorName');
    $.messager.confirm('删除操作','您正在删除角色为【'+name+'】,删除之后将永久不能回复',function (r) {
        if(r){
            var obj = [id];
            doData(contextPath+'actor/deleteActor',obj,function(data){
                if(data=="success"){
                    $.messager.show({
                        title: '提示',
                        msg: '已成功删除【'+name+'】'
                    });
                    delayRefresh();
                }else{
                    $.messager.alert('提示','删除失败,错误代码'+data,'error');
                }
            });
        }
    });
}

$(function () {
    find();
    $('#serarchActor').click(function () {
        find();
    });

    // $("#addActor").on('onclick',function () {
    //     showDialog('<%=basePath%>jsp/actor/actor-input.jsp',300,300);
    //     find();
    // });
    $('#addActor').click(function () {
        clearForm('actorForm');
        openEasyWin('btnClose','btnSave','actorWin','角色新增');
    });

    $('#editActor').click(function () {
        var rows = $('#actorInfo').datagrid('getSelections');
        if(rows.length==0){
            alert('请选择一条角色数据进行编辑');
            return;
        }
        if(rows.length>1){
            alert('一次只能编辑一条角色数据');
            return;
        }
        var row = rows[0];
        $('#actorForm').find(':input').each(function () {
            var name = $(this).attr('name');
           if(name=='id'){
               $(this).val(row.id);
           }
           else if(name=='actorName'){
               $(this).val(row.actorName);
           }
        });
        openEasyWin('btnClose','btnSave','actorWin','角色新增');
    });

    $('#btnSave').click(function () {
        var obj = serializeArrayToObject('actorForm');
        for(var key in obj){
            if(key=='actorName'&&$.trim(obj[key])==''){
                alert('角色名称不能为空');
                return;
            }
        }
        var actorId = obj.id;
        doData(contextPath+'actor/saveOrUpdate',obj,function (data) {
            if(data=="success"){
                $('#actorWin').window('close');
                if($.trim(actorId)==''){
                    showMessage('提示','成功新增一条角色信息');
                }else {
                    showMessage('提示','成功编辑一条角色信息');
                }
                delayRefresh();
            }else {
                if($.trim(actorId)==''){
                 alert('错误提示','角色信息新增失败，请联系管理员！','error');
                }else {
                    alert('错误提示','角色信息编辑失败，请联系管理员！','error');
                }
            }
        });
    });

    $('#btnClose').click(function () {
        $('#actorWin').window('close');
    });


});
