/**
 * Created by fangjiang on 2018/4/12.
 */
var contextPath = '';
function setContextPath(rootPath) {
    contextPath = rootPath;
}

//用户一般操作
function operator(value,rowData,rowIndex) {
    return  "<a href='#' sourceGroupName='"+rowData.name+"' sourceGroupId='"+
        rowData.id+"'  onclick='delSource(this)'>删除</a>"
}

function find() {
    var obj = {};
    obj.condition = $('#sourceGroupName').val();
    findByPage(contextPath+'sourceGroup/findSourceGroupByPage','sourceGroupInfo',obj,function () {
    });
}

function delSource(data) {
    var sourceGroupName = $(data).attr('sourceGroupName');
    var sourceGroupId = $(data).attr('sourceGroupId');
    confirm('提示','您确认是否删除资源名【'+sourceGroupName+'】,删除之后永远不能恢复！',function (r) {
        if(r){
            doData(contextPath+'sourceGroup/deleteSourceGroup',[sourceGroupId],function (data) {
                if(data=="success"){
                    showMessage('提示','资源名为【'+sourceGroupName+'】已成功！');
                    delayRefresh();
                }else {
                    alert('错误提示','资源名为【'+sourceGroupName+'】删除失败','error');
                }
            });
        }
    });
}

function reloadPage() {
    window.location.reload();
}


$(function () {
    find();

    $('#serarchSourceGroup').click(function () {
        find();
    });

    $('#addSourceGroup').click(function () {
        clearForm('sourceGroupForm');
        openEasyWin('btnAddSave','btnAddClose','addSourceGroupWin','资源组新增');
    });

    $('#editSourceGroup').click(function () {
        var rows = $('#sourceGroupInfo').datagrid('getSelections');
        if(rows.length>1){
            alert('一次只能编辑一条数据');
            return;
        }
        if(rows.length==0){
            alert('请选择一条数据进行编辑');
            return;
        }
        //初始化数据
        var row = rows[0];
        $('#sourceGroupForm').find(':input').each(function (index,element) {
            var name = $(this).attr('name');
            if(name=='id'){
                $(this).val(row.id);
            }
            else if(name=='name'){
                $(this).val(row.name);
            }
        });
        openEasyWin('btnAddSave','btnAddClose','addSourceGroupWin','资源组编辑');
    });

    $('#delAllSourceGroup').click(function () {
        var rows = $('#sourceGroupInfo').datagrid('getSelections');
        confirm('提示','你确认删除这【'+rows.length+'】条数据吗？删除之后不可以恢复。',function (r) {
            if(r){
                var id =[];
                for(var key in rows){
                    id.push(rows[key].id);
                }
                doData(contextPath+'sourceGroup/deleteSourceGroup',id,function (data) {
                    if(data=="success"){
                        showMessage('提示','已成功删除所有选中的数据');
                        delayRefresh();
                    }else {
                        alert('错误提示','资源删除失败,请联系管理员！','error');
                    }
                });
            }
        });

    });

    $('#btnAddSave').click(function () {
        var obj=serializeArrayToObject('sourceGroupForm');
        doData(contextPath+'sourceGroup/saveOrUpdate',obj,function (data) {
            if(data=="success"){
                if(obj.id!=''){
                    showMessage('提示','成功编辑一条数据.');
                    $('#addSourceGroupWin').window('close');
                    //两秒之后刷新页面
                    setTimeout('reloadPage()',2000);
                }else {
                    showMessage('提示','成功新增一条数据.');
                    $('#addSourceGroupWin').window('close');
                    setTimeout('reloadPage()',2000);
                }

            }else {
                alert('错误提示','操作失败,请联系管理员','error')
            }
        });
    });

    $('#btnAddClose').click(function () {
        $('#addSourceGroupWin').window('close');
    });
});