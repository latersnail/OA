/**
 * Created by fangjiang on 2018/4/1.
 */
var contextPath = '';
function setContextPath(rootPath) {
    contextPath = rootPath;
}
//用户一般操作
function operator(value,rowData,rowIndex) {
    return  "<a href='#' processName='"+rowData.processName+"' recvDocId='"+
        rowData.id+"' onclick='delRecvDoc(this)'>删除</a>|" +
        "<a href='#' processName='"+rowData.processName+"' recvDocId='"+
        rowData.id+"' onclick='gotoNext(this)'>办理</a>"
}

function find() {
 var obj = new Object();
 obj.condition = $("#recvDocName").val();
 findByPage(contextPath+'recvdoc/getDraftDoc','draftDoc',obj,function (){});
}

//删除草稿
function delRecvDoc($this) {
    var that = $($this);
    var name = that.attr('processName');
    var id = that.attr('recvDocId');
    $.messager.confirm('删除操作','您正在删除【'+name+'】,删除之后进入草稿列表',function (r) {
        if(r){
            var obj = [id];
            doData(contextPath+'recvdoc/abandonDoc',obj,function(data){
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

function showDialog(url, w, h){
    //新版本火狐谷歌不支持showModalDialog
    //showModalDialog(url, self, ‘dialogWidth=‘+w+‘px;dialogHeight=‘+h+‘px‘);
    window.open(url, self, 'modal=yes,Width='+w+'px,Height='+h+'px');
}

//搜索
function serarchRecvDoc() {
    find();
}

//批量删除
function delAllRecvDoc() {
  var rows = $('#draftDoc').datagrid('getSelections');
  if(rows.length==0){
      alert('请选择需要删除的数据');
      return false;
  }
  confirm('删除确认','你已经选中【'+rows.length+'】条数据,删除之后可在回收站还原.',function (r) {
     if(r){
         var ids =[];
         for(var key in rows){
             ids.push(rows[key].id);
         }
         doData(contextPath+'recvdoc/abandonDoc',ids,function (data) {
             if(data=="success"){
                 showMessage('提示','成功删除【'+ids.length+'】条数据');
                 delayRefresh();
             }else {
                 alert('错误提示','删除失败,请联系管理员','error');
             }
         });
     }
  });

}

//新建流程
function createRecvDoc() {
    showDialog(contextPath+'recvdoc/input?type=add',800,700);
}

/**************************************************************************
 *datagrid数据处理 共用   start
 **************************************************************************/
//格式化用户数据 姓名
function formatUser(value,rowData,rowIndex) {
    if(rowData.user!=null){
        return rowData.user.name;
    }
}

function formatUser2(value,rowData,rowIndex) {
    var user = rowData.user;
    if(user!=null){
        return user.id;
    }
}

//获取任务执行者 需要请求服务
function formatTakeActor(value,rowData,rowIndex){
    var name = '';
    if(rowData.takeActor){
        var obj = {};
        obj.id = rowData.takeActor;
        $.ajax({
            url: contextPath+'user/getUserInfo',
            type:'method',
            dataType:'json',
            async:false,//采用同步  待优化
            data:JSON.stringify(obj),
            success:function (data) {
                name = data.name;
            }
        });
    }
    return name;
}

//获取表单数据
function formatForm(value,rowData,rowIndex) {
    var form = rowData.form;
    if(form!=null){
        return form.formName;
    }
}

//格式日期
function formatDate(value,rowData,rowIndex) {
    return Date.prototype.getLongDate(value);
}
/***************************end********************************/

//办理流程 进入办理页面 移除西面板(西面板为选择流程面板,流程草稿进入无需选择流程)
// 根据流程类型加载中心面板所需的表单模板
function gotoNext($this) {
    var rows = $("#draftDoc").datagrid('getSelections');
    if(rows.length>1){
        alert('提示','不允许办理多个任务','info');
    }
    else if(rows.length==0){
        alert('提示','请选择一个任务进行办理','info');
    }
    showDialog(contextPath+'recvdoc/input?type=edit&id='+rows[0].id,800,700);
}

$(function () {
    find();
});