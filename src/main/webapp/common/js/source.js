/**
 * Created by fangjiang on 2018/3/27.
 */
var contextPath = '';
function setContextPath(rootPath) {
    contextPath = rootPath;
}

//用户一般操作
function operator(value,rowData,rowIndex) {
    return  "<a href='#' sourceName='"+rowData.name+"' sourceId='"+
        rowData.id+"'  onclick='delSource(this)'>删除</a>"
}

function find() {
    var obj = {};
    obj.condition = $('#sourceName').val();
    findByPage(contextPath+'source/findSourceByPage','sourceInfo',obj,function () {
    });
}

function delSource(data) {
    var sourceName = $(data).attr('sourceName');
    var sourceId = $(data).attr('sourceId');
    confirm('提示','您确认是否删除资源名【'+sourceName+'】,删除之后永远不能恢复！',function (r) {
        if(r){
            doData(contextPath+'source/deleteSource',[sourceId],function (data) {
                if(data=="success"){
                    showMessage('提示','资源名为【'+sourceName+'】已成功！')
                    delayRefresh();
                }else {
                    alert('错误提示','资源名为【'+sourceName+'】删除失败','error');
                }
            });
        }
    });
}

function formatSourceCode(value,rowData,index) {
    var obj ={};
    obj.id = rowData.sourceCode;
    var sourceGroupName = '';
    $.ajax({
       url: contextPath+'sourceGroup/findSourceGroupById',
       method: 'post',
       dataType: 'json',
       data: JSON.stringify(obj),
       async: false,
       success: function (data) {
          sourceGroupName = data.name;
          $('#sourceGroupName').val(sourceGroupName);
       }
    });
    return sourceGroupName;
}

function setSourceGroup(){
    openEasyWin('btnSourceSave','btnSourceClose','chooseSourceGroup','资源组设置');
}

function checkSource() {
   var obj = {};
   obj.sourcePath = $('#sourcePath').val();
   doData(contextPath+'sourceGroup/checkSource',obj,function (data) {
       if(data=='success'){
           alert('资源路径合法');
           $('#isChecked').val('true');
       }else {
           alert('错误提示','资源路径找不到','error');
           $('#isChecked').val('false');
       }
   });
}

$(function () {

    find();

    //初始化easyui tree
    initTree('chooseSourceGroupTree',contextPath+'sourceGroup/getGroupTree',true,function (node) {

    });

    $('#serarchSource').click(function () {
        find();
    });

    $('#addSource').click(function () {
        clearForm('sourceForm');
        openEasyWin('btnAddSave','btnAddClose','addSourceWin','资源新增');
    });

    $('#editSource').click(function () {
        var rows = $('#sourceInfo').datagrid('getSelections');
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
        $('#sourceForm').find(':input').each(function (index,element) {
            var name = $(this).attr('name');
            if(name=='id'){
                $(this).val(row.id);
            }
            else if(name=='name'){
                $(this).val(row.name);
            }
            else if(name=='sourcePath'){
                $(this).val(row.sourcePath);
            }
            else if(name=='sourceCode'){
                $(this).val(row.sourceCode);
            }
            else if(name=='sourceCodeName'){
                $(this).val( $('#sourceGroupName').val());
            }
        });
        openEasyWin('btnAddSave','btnAddClose','addSourceWin','资源编辑');
    });

    $('#delAllSource').click(function () {
        var rows = $('#sourceInfo').datagrid('getSelections');
        confirm('提示','你确认删除这【'+rows.length+'】条数据吗？删除之后不可以恢复。',function (r) {
            if(r){
                var id =[];
                for(var key in rows){
                    id.push(rows[key].id);
                }
                doData(contextPath+'source/deleteSource',id,function (data) {
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

    $('#setSourceGroup').click(function () {
        var rows = $('#sourceInfo').datagrid('getSelections');
        if(rows.length>1){
            alert('一次只能设置一条数据');
            return false;
        }
        if(rows.length==0){
            alert('请选择一条数据进行设置');
            return false;
        }
        openEasyWin('btnSourceSave','btnSourceClose','chooseSourceGroup','资源组设置');
    });

    $('#btnAddSave').click(function () {
        var obj=serializeArrayToObject('sourceForm');
            if($.trim(obj.name)==""){
                alert('资源名称不能空');
                return;
            }
            else if($.trim(obj.sourceCodeName)==''){
                alert('资源分组不能空');
                return;
            }
            else if($.trim(obj.sourcePath)==''){
                alert('资源路径不能为空');
                return;
            }
        var isChecked = $('#isChecked').val();
        if(isChecked=='false'){
            alert('资源路径不合法,请重新输入资源路径');
            return;
        }
        $.messager.progress({title:'保存数据',msg:'正在保存数据'});
        doData(contextPath+'source/saveOrUpdateSource',obj,function (data) {
            if(data=="success"){
                $.messager.progress('close');
                if(obj.id!=''){
                    showMessage('提示','成功编辑一条数据.');
                    $('#addSourceWin').window('close');
                    $('#isChecked').val('false');
                    delayRefresh();
                }else {
                    showMessage('提示','成功新增一条数据.');
                    $('#addSourceWin').window('close');
                    delayRefresh();
                }
            }else {
                $.messager.progress('close');
                alert('错误提示','操作失败,请联系管理员','error')
            }
        });
    });

    $('#btnAddClose').click(function () {
        $('#addSourceWin').window('close');
    });

    $('#btnSourceSave').click(function () {
        var node = getSelectTreeNode('chooseSourceGroupTree',true);
        $('#sourceCode').val(node.id);
        $('#sourceCodeName').val(node.text);
        $('#chooseSourceGroup').window('close');
    });

    $('#btnSourceClose').click(function () {
        $('#chooseSourceGroup').window('close');
    });
});
