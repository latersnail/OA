/**
 * Created by fangjiang on 2018/4/6.
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
    findByPage(contextPath+'recvdoc/todoDoc','todoDoc',obj,function (){});
}

//流程办理
function gotoNext() {
    var rows = $("#todoDoc").datagrid('getSelections');
    if(rows.length>1){
        alert('提示','不允许办理多个任务','info');
    }
    if(rows.length==0){
        alert('提示','请选择一个任务进行办理','info');
    }
    showDialog(contextPath+'recvdoc/input?type=edit&id='+rows[0].id+'&taskId='+rows[0].taskId,800,700);
}

/**************************************************************************
 *datagrid数据处理 共用   start
 **************************************************************************/
//格式化用户数据 姓名
function formatUser(value,rowData,rowIndex) {
  if(rowData.userId!=null){
      var name = '';
      if(rowData.takeActor){
          var obj = {};
          obj.userId = rowData.userId;
          $.ajax({
              url: contextPath+'user/getUserById',
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
}

//获取任务执行者 需要请求服务
function formatTakeActor(value,rowData,rowIndex){
    var name = '';
    if(rowData.takeActor){
        var obj = {};
        obj.userId = rowData.takeActor;
        $.ajax({
          url: contextPath+'user/getUserById',
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
/***************************************************************************
 * datagrid数据处理 共用   end
 **************************************************************************/

$(function () {
    find();
    $('#serarchRecvDoc').click(function () {
        find();
    });
});