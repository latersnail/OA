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
        rowData.id+"' onclick='recover(this)'>还原</a>"
}

function find() {
 var obj = new Object();
 obj.condition = $("#recvDocName").val();
 findByPage(contextPath+'recvdoc/getAbandonDoc','abandonDoc',obj,function (){});
}

//删除草稿
function delRecvDoc($this) {
    var that = $($this);
    var name = that.attr('processName');
    var id = that.attr('recvDocId');
    $.messager.confirm('删除操作','您正在删除【'+name+'】,删除之后进入草稿列表',function (r) {
        if(r){
            var obj = [id];
            doData(contextPath+'recvdoc/deleteRecvdoc',obj,function(data){
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


//搜索
function serarchRecvDoc() {
    find();
}

//批量删除
function delAllRecvDoc() {
  var rows = $('#abandonDoc').datagrid('getSelections');
  if(rows.length==0){
      alert('请选择需要删除的数据');
      return false;
  }
  confirm('删除确认','你已经选中【'+rows.length+'】条数据,删除之后永久不能恢复',function (r) {
     if(r){
         var ids =[];
         for(var key in rows){
             ids.push(rows.id);
         }
         doData(contextPath+'recvdoc/deleteRecvdoc',ids,function (data) {
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

//还原文件
function recover($this) {
  if($this){
      var that = $($this);
      var name = that.attr('processName');
      var id = that.attr('recvDocId');
      confirm('还原确认','你正在还原'+name+'文件',function (r) {
          if(r){
              doData(contextPath+'recvdoc/recoverDoc',[id],function (data) {
                 if(data=="success"){
                     showMessage('提示','你已经成功还原'+name+'文件');
                     delayRefresh();
                 } else {
                     alert('错误提示','还原失败,请联系管理员','error');
                 }
              });
          }
      });
  }else {
      var rows = $('#abandonDoc').datagrid('getSelections');
      if(rows.length==0){
          alert('请选择一条数据进行还原');
          return false;
      }
      var ids =[];
      for(var key in rows){
          ids.push(rows[key].id);
      }
      doData(contextPath+'recvdoc/recoverDoc',ids,function (data) {
          if(data=="success"){
              showMessage('提示','成功还原【'+ids.length+'】条数据');
              delayRefresh();
          }else {
              alert('错误提示','还原失败,请联系管理员','error');
          }
      });
  }
}

$(function () {
    find();
});