/**
 * Created by fangjiang on 2018/3/27.
 */
var contextPath = '';
function setContextPath(rootPath) {
    contextPath = rootPath;
}

//用户一般操作
function operator(value,rowData,rowIndex) {
    return  "<a href='#' orgName='"+rowData.name+"' orgId='"+
        rowData.id+"' type='delOrg' onclick='delOrg(this)'>删除</a>"
}

function find() {
    var obj = new Object();
    obj.condition = $('#name').val();
    findByPage(contextPath+'organization/findOrganizationByPage','orgInfo',obj,function () {
    });
}

function showDialog(url, w, h){
    //新版本火狐谷歌不支持showModalDialog
    //showModalDialog(url, self, ‘dialogWidth=‘+w+‘px;dialogHeight=‘+h+‘px‘);
    window.open(url, self, 'modal=yes,Width='+w+'px,Height='+h+'px');
}

function delOrg($this){
    var id =$($this).attr('orgId');
    var name = $($this).attr('orgName');
    $.messager.confirm('删除操作','您正在删除【'+name+'】,删除之后将永久不能回复',function (r) {
        if(r){
            var obj = [id];
            doData(contextPath+'organization/deleteOrganization',obj,function(data){
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

    // $("#addOrg").click(function(){
    //   showDialog(contextPath+'organization/input?type=add&id=""',300,300);
    // });
    //
    // $("#editOrg").click(function () {
    //     var rows = $('#orgInfo').datagrid('getSelections');
    //     if (rows==null || rows.length==0)
    //     {
    //         alert("请选中需要修改的组织！");
    //         return;
    //     }
    //     if(rows.length>1){
    //         alert("一次只能编辑一条数据");
    //         return;
    //     }
    //     showDialog(contextPath+'organization/input?type=edit&id='+rows[0].id,300,300);
    // });

    $("#delOrg").click(function () {
        var rows = $('#orgInfo').datagrid('getSelections');
        if (rows==null || rows.length==0)
        {
            alert("提示","请选中需要删除的组织！","info");
            return;
        }
        confirm('确认提示','您正在删除【'+rows.length+'】条数据，删除之后永久不能恢复',function (r) {
           if(r){
               var ids = new Array();
               for(var key in rows){
                   ids.push(rows[key].id);
               }
               doData(contextPath+"organization/deleteOrganization",ids,function (data) {
                   if(data=="success"){
                       $.messager.show({
                           title: '提示',
                           msg: '已成功删除【'+rows.length+'】条数据'
                       });
                      delayRefresh()
                   }else{
                       $.messager.alert('提示','删除失败,错误代码'+data,'error');
                   }
               });
           }
        });

    });

    $("#serarchOrg").click(function () {
       find();
    });

    $('#addOrg').click(function () {
        clearForm('orgForm');
        openEasyWin('btnSave','btnClose','orgWin','组织机构新增');
    });

    $('#editOrg').click(function () {
        var rows = $('#orgInfo').datagrid('getSelections');
        if(rows.length==0){
            alert('请选择一条组织机构数据进行编辑');
            return;
        }
        if(rows.length>1){
            alert('一次只能编辑一条组织机构数据');
            return;
        }
        var row = rows[0];
        $('#orgForm').find(':input').each(function () {
            var name = $(this).attr('name');
            if(name=='id'){
                $(this).val(row.id);
            }
            else if(name=='name'){
                $(this).val(row.name);
            }
        });
       openEasyWin('btnSave','btnClose','orgWin','组织机构编辑');
    });

    $('#btnSave').click(function () {
        var obj = serializeArrayToObject('orgForm');
        for(var key in obj){
            if(key=='name'&&$.trim(obj[key])==''){
                alert('组织机构名称不能为空');
                return;
            }
        }
        var orgId = obj.id;
        doData(contextPath+'organization/saveOrUpdate',obj,function (data) {
            if(data=="success"){
                $('#orgWin').window('close');
                if($.trim(orgId)==''){
                    showMessage('提示','成功新增一条组织机构信息');
                }else {
                    showMessage('提示','成功编辑一条组织机构信息');
                }
                delayRefresh();
            }else {
                if($.trim(orgId)==''){
                    alert('错误提示','组织机构信息新增失败，请联系管理员！','error');
                }else {
                    alert('错误提示','组织机构信息编辑失败，请联系管理员！','error');
                }
            }
        });
    });

    $('#btnClose').click(function () {
        $('#orgWin').window('close');
    });

});
