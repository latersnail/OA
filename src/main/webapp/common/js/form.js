/**
 * Created by fangjiang on 2018/4/1.
 */
var contextPath = '';
var type = '';
function setContextPath(rootPath) {
    contextPath = rootPath;
}
//用户一般操作
function operator(value,rowData,rowIndex) {
    return  "<a href='#' formName='"+rowData.formName+"' formId='"+
        rowData.id+"' onclick='delForm(this)'>删除</a>"
}
//日期格式化
function formatDate(value,rowData,rowIndex) {
   return Date.prototype.getLongDate(value);
}

function find() {
    var obj = new Object();
    obj.condition = $("#keyword").val();
    findByPage(contextPath+'form/findFormByPage','formInfo',obj,function (){});
}

function serarchForm() {
    find()
}

function addForm() {
    type = 'add';
 $("#commonForm").form('clear');
 $('#btnSave').show();
 $('#btnClose').show();
 $('#addFormWin').panel({title:'表单新增'})
 $('#btnSave').val('保存');
 $('#addFormWin').window('open');
}

function editForm() {
    type = 'edit';
    var rows = $("#formInfo").datagrid('getSelections');
    if(rows.length>1){
        alert('一次只能编辑一条数据')
        return;
    }
    if(rows.length==0){
        alert('请选择一条数据进行编辑')
        return;
    }
    //编辑初始化数据
    $('#addFormWin').attr('title','表单编辑');
    $('#btnSave').show();
    $('#btnClose').show();
    $('#addFormWin').panel({title:'表单编辑'})
    $('#btnSave').val('保存')
    $('#formId').val(rows[0].id);
    $('#formName').val(rows[0].formName);
    $('#path').val(rows[0].path);
    $('#addFormWin').window('open');
}

function doSubmit() {
    var obj = serializeArrayToObject('commonForm');
    if ($("#commonForm").form('enableValidation').form('validate') == false)
        return;
    doData(contextPath+'form/saveOrUpdateForm',obj,function (data) {
        if(type=="add"&&data=="success"){
            showMessage('提示','成功添加一条表单数据');
            $('#addFormWin').window('close');
            delayRefresh();
        }
        else if(type=="add"&&data=='fail'){
            alert('提示','添加数据失败','error');
        }
        else if(type=="edit"&&data=='success'){
            showMessage('提示','成功修改一条数据');
            $('#addFormWin').window('close');
            delayRefresh();
        }
        else if(type=="edit"&&data=='fail'){
            alert('提示','修改数据失败','info');
        }
    })
}

//关闭窗口
function winClose() {
    $('#addFormWin').window('close');
}

//表单查看
function viewForm() {
    var rows = $('#formInfo').datagrid('getSelections');
    if(rows.length>1){
        alert('一次只能查看一个表单');
        return;
    }
    if(rows.length==0){
        alert('请选择需要查看的表单');
        return;
    }
    showDialog(contextPath+rows[0].path,600,500);
}
function delForm($this) {
    var that = $($this);
    var formName = that.attr('formName');
    var id = that.attr('formId');
    confirm('删除操作','您正在删除表单为【'+formName+'】,删除之后将永久不能恢复',function (r) {
        if(r){
            doData(contextPath+'form/deleteForm',[id],function (data) {
                if(data=="success"){
                    window.showMessage('提示','已成功删除【'+formName+'】');
                    delayRefresh();
                } else {
                    alert('提示','删除失败','error');
                }
            });
        }
    });
}

//检验资源
function checkData() {
   var path = $("#path").val();
   var obj = new Object();
   obj.path =path;
   if(path!=null&&typeof path !="undefined"&&$.trim(path)!=""){
        doData(contextPath+'form/checkSourcePath',{path:path},function (data) {
            alert('提示',data,'info');
        });
   }else{
            alert('提示','资源路径不能为空','info');
   }
}

function delAllForm() {
    var rows = $("#formInfo").datagrid('getSelections');
    confirm('删除操作','你确认删除【'+rows.length+'】条数据?删除之后不可以恢复！',function (r) {
       if(r){
           var ids = [];
           for(var key in rows){
               ids.push(rows[key].id);
           }
           doData(contextPath+'form/deleteForm',ids,function (data) {
               if(data=="success"){
                   showMessage('提示','成功删除'+rows.length+'条数据');
                   delayRefresh();
               }else {
                   alert('提示','删除失败','error')
               }
           })
       }
    });
}

function showDialog(url, w, h){
    //新版本火狐谷歌不支持showModalDialog
    //showModalDialog(url, self, ‘dialogWidth=‘+w+‘px;dialogHeight=‘+h+‘px‘);
    window.open(url, self, 'modal=yes,Width='+w+'px,Height='+h+'px');
}
$(function () {
   find();
});