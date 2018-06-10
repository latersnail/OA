/**
 * Created by fangjiang on 2018/4/11.
 */
var contextPath = '';
function setContextPath(rootPath) {
    contextPath = rootPath;
}

function showDialog(url, w, h){
    //新版本火狐谷歌不支持showModalDialog
    //showModalDialog(url, self, ‘dialogWidth=‘+w+‘px;dialogHeight=‘+h+‘px‘);
    window.open(url, window, 'modal=yes,Width='+w+'px,Height='+h+'px');
}

function find() {
    var obj = new Object();
    obj.condition = $("#recvDocName").val();
    findByPage(contextPath+'recvdoc/signDoc','signDocInfo',obj,function (){});
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
    var formName = '';
    if(rowData.formId){
        var obj = {};
        obj.formId = rowData.formId;
        $.ajax({
            url: contextPath+'form/getFormById',
            type:'method',
            dataType:'json',
            async:false,//采用同步  待优化
            data:JSON.stringify(obj),
            success:function (data) {
                formName = data.formName;
            }
        });
    }
    return formName;
}

//格式日期
function formatDate(value,rowData,rowIndex) {
    return Date.prototype.getLongDate(value);
}

function operation(value,rowData,rowIndex) {
   return '<a href="#" onclick="signDoc(this)" taskId="'+rowData.taskId+'" processName="'+rowData.processName+'">签收</a>';
}

//签收任务
function signDoc($this) {
    var obj = {};
    obj.type = 'sign';
  if($this){
      $this = $($this);
      var taskId = $this.attr('taskId');
      var processName = $this.attr('processName');
      obj.taskId = taskId;
  }else{
      var rows = $("#signDocInfo").datagrid('getSelections');
      if(rows.length>1){
          alert('一次只能签收一条数据')
          return;
      }
      if(rows.length==0){
          alert('请选择一条数据进行签收')
          return;
      }
      obj.taskId = rows[0].taskId;
  }
  doData(contextPath+'recvdoc/signDoc',obj,function (data) {
      if(data=='success'){
         showMessage('提示','当前任务已经进入了你的待办');
         window.location.reload();
      }else {
          alert('错误提示','任务签收失败,请联系管理员','error');
      }
  });
}

//查询
function serarchRecvDoc() {
    find();
}

//查看数据
function signDocView() {
    var rows = $("#signDocInfo").datagrid('getSelections');
    if(rows.length>1){
        alert('一次只能查看一条数据')
        return;
    }
    if(rows.length==0){
        alert('请选择一条数据进行查看')
        return;
    }
    showDialog(contextPath+'recvdoc/viewDoc?type=view&id='+rows[0].id,700,800);
}

$(function () {
   find();
    $('#serarchRecvDoc').click(function () {
        find();
    });
});