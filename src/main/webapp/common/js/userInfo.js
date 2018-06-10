/**
 * Created by fangjiang on 2018/3/25.
 */
//js执行的上下文环境
var contextPath = '';
var doActorType = 'default';
function setContextPath(rootPath) {
    contextPath = rootPath;
}
//用户一般操作
function operator(value,rowData,rowIndex) {
   return  "<a href='#' userName='"+rowData.name+"' userId='"+
       rowData.id+"' type='delUser' onclick='delUser(this)'>删除</a>"
}

//对用户进行角色分配
function formatActor(value,rowData,rowIndex) {
    if(value==null||value=='')return "未分配【<a href='#' userName='"+rowData.name+"' userId='"+
        rowData.id+"' type='doActor'>分配</a>】"
    return value+"【<a href='#' userName='"+rowData.name+"' userId='"+
        rowData.id+"' type='doActor'>重新分配</a>】";
}

//处理用户状态
function formatStatus(value,rowData,rowIndex)
{
    if (value=="正常") return "<select userID='"+rowData.id+"' oldState='"+value+"'><option value='正常' selected='selected'>正常</option><option value='锁定'>锁定</option><option value='注销'>注销</option></select>";
    if (value=="锁定") return "<select userID='"+rowData.id+"' oldState='"+value+"'><option value='正常'>正常</option><option value='锁定' selected='selected'>锁定</option><option value='注销'>注销</option></select>";
    if (value=="注销") return "<select userID='"+rowData.id+"' oldState='"+value+"'><option value='正常'>正常</option><option value='锁定'>锁定</option><option value='注销' selected='selected'>注销</option></select>";
    return "<select userID='"+rowData.id+"' oldState='"+value+"'><option value='正常'>正常</option><option value='锁定'>锁定</option><option value='注销'>注销</option><option value='"+value+"' selected='selected'>"+value+"</option></select>";
}

//获取登录用户名搜索用户信息
function find(){
    var obj = new Object();
    obj.condition = $('#loginName').val();
    findByPage(contextPath+'user/findUserByPage','userInfo',obj);
}

//打开easyUI的窗口常用操作
function openWin(btnId1,btnId2,mainWinId,title) {
    $('#'+btnId1).show();
    $('#'+btnId2).show();
    $('#'+mainWinId).panel({title:title});
    $('#'+mainWinId).window("open");
}

function delUser($this) {
    var id =$($this).attr('userId');
    var name = $($this).attr('userName');
    $.messager.confirm('删除操作','您正在删除【'+name+'】,删除之后将永久不能回复',function (r) {
        if(r){
            doData(contextPath+'user/deleteUser',[id],function(data){
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

$(function(){
    find();
    //搜索
    $('#serarchUser').click(function () {
       find();
    });
   //批量删除用户
  $('#delAll').click(function () {
     var rows = $('#userInfo').datagrid('getSelections');
     if(rows.length==0){
         alert('请选择一条用户数据进行删除。');
         return;
     }
     confirm('批量删除提示','您正在删除【'+rows.length+'】条数据',function (r) {
         if(r){
             var ids = [];
             for(var i=0;i<rows.length;i++){
                 ids.push(rows[i].id);
             }
             doData(contextPath+'user/deleteUser',ids,function (data) {
                 if(data=="success"){
                     showMessage('提示','你成功删除了'+ids.length+'条数据');
                     delayRefresh();
                 }else {
                     alert('错误提示','删除用户失败，请联系管理员！','error');
                 }
             });
         }
     });

  });
  // 新增用户
    $('#addUser').click(function () {
        doActorType = 'default';
        clearForm('userForm');
        openWin('btnUserSave','btnUserClose','userWind','用户添加');
    });
    $('#btnUserSave').click(function () {
        var obj = serializeArrayToObject('userForm');
        var type = 'edit';
        var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
        if(obj.id==null||typeof obj.id =='undefined'||$.trim(obj.id)==''){
          type = 'insert';
        }
        //对数据进行检查
        var sex = $('input[name=sex]:checked').val();
        if(typeof sex=='undefined'||sex==''||sex==null){
            alert('请选择性别');
            return;
        }
        for(var key in obj){
            if(obj.hasOwnProperty(key)){
                if(key=='name'&&obj[key]==""){
                    alert('姓名不能为空');
                    return;
                }
                else if(key=='loginName'&&obj[key]==""){
                    alert('登录名不能为空');
                    return;
                }
                else if(key=="telphone"&&obj[key]==""){
                    alert('电话号码不能为空');
                    return;
                }
                else if(key=="telphone"&&!myreg.test(obj[key])){
                    alert('手机格式不正确！');
                    return;
                }
                else if(key=="orgId"&&obj[key]==''){
                    alert('结构单位不能为空');
                    return;
                }
                else if(key=="actorId"&&obj[key]==""){
                    alert('角色不能为空');
                    return;
                }
                else if(key=="password"&&obj[key]==""){
                    alert("登录密码不能为空");
                    return;
                }
            }
        }
        if($('#password').val()!=$('#repassword').val()){
            alert('两次输入的密码不相等,请重新输入');
            $('#password').val('');
            $('#repassword').val('');
            return;
        }
        //对结果数据再次处理
        obj.sex = sex;
        obj.orgId = $('#orgId').attr('orgId');
        obj.actorId = $('#actorId').attr('actorId');
        obj.status = '正常';
        obj.address = '默认';
        doData(contextPath+'user/saveOrUpdate',obj,function (data) {
            if(data=='success'){
                if(type =='insert'){
                    showMessage('提示','成功新增一名用户');
                }else {
                    showMessage('提示','成功编辑一名用户');
                }
                $('#userWind').window('close');
                find();
            }else {
                if(type =='insert'){
                    alert('错误提示','添加用户失败','error');
                }else {
                    alert('错误提示','编辑用户失败','error');
                }
            }
        });
    });
    $('#btnUserClose').click(function () {
        $('#userWind').window('close');
    });

    //用户添加中的角色设置
    $('#openActorWin').click(function () {
        openEasyWin('btnActorSave','btnActorClose','actorWin','角色设置');
        initTree('actorTree',contextPath+'actor/getActorTree',true,false);
    });

    $('#setActor').click(function () {
        var rows = $('#userInfo').datagrid('getSelections');
        if(rows.length==0){
            alert('请选择一条用户数据进行角色设置');
            return;
        }
        if(rows.length>1){
            alert('一次只能设置一条用户数据的角色');
            return ;
        }
        doActorType = 'click';
        openEasyWin('btnActorSave','btnActorClose','actorWin','角色设置');
        initTree('actorTree',contextPath+'actor/getActorTree',true,false);
    });

    //角色内容保存
   $('#btnActorSave').click(function () {
       debugger
       var node = getSelectTreeNode('actorTree',true);
       if(!node){
           alert('请选择一个角色');
           return;
       }
       if(doActorType=='default') {
           $('#actorId').val(node.text);
           $('#actorId').attr('actorId', node.id);
       }
       else if(doActorType=='link'){
           var obj = {};
           obj.actorId = node.id;
           obj.id = $('a[type=doActor]').attr('userId');
         //   doData(contextPath+'user/setActor',obj,function (data) {
         //     if(data=="success"){
         //        showMessage('提示','角色设置成功');
         //        delayRefresh();
         //     }else {
         //         alert('错误提示','角色设置失败,请联系管理员！','error');
         //     }
         // });
       }
       else if(doActorType=='click'){
           var rows = $("#userInfo").datagrid('getSelections');
           if(rows[0].actorId==node.id){
               $('#actorWin').window('close');
               return;
           }
           var obj = {};
           obj.actorId = node.id;
           obj.id = rows[0].id;
           doData(contextPath+'user/setActor',obj,function (data) {
               if(data=="success"){
                   showMessage('提示','角色设置成功');
                   delayRefresh();
               }else {
                   alert('错误提示','角色设置失败,请联系管理员！','error');
               }
           });
       }
       $('#actorWin').window('close');
   });

   $('#btnActorClose').click(function () {
       $('#actorWin').window('close');
   });

   //用户添加组织结构
    $('#openOrgWin').click(function () {
        openEasyWin('btnOrgSave','btnOrgClose','orgWin','机构选择',{draggable:true});
        initTree('orgTree',contextPath+'organization/getOrgTree',true,false);
    });

    $('#btnOrgSave').click(function () {
        var node = getSelectTreeNode('orgTree',true);
        if(!node){
            alert('请选择一个组织结构');
            return;
        }
        $('#orgId').val(node.text);
        $('#orgId').attr('orgId',node.id);
        $('#orgWin').window('close');
    });

    $('#btnOrgClose').click(function () {
        $('#orgWin').window('close');
    });

    //编辑
    $('#editUser').click(function () {
        var rows = $("#userInfo").datagrid('getSelections');
        if(rows.length>1){
            alert('提示','不允许编辑多个用户','info');
            return;
        }
        else if(rows.length==0){
            alert('提示','请选择一个用户进行编辑','info');
            return;
        }
        doActorType = 'default';
        var row = rows[0];
        $('#userForm input').each(function () {
            var $this = $(this);
            if($this.attr('type')=='text'&&$this.attr('name')=='name'){
                $this.val(row.name);
            }
            else if($this.attr('type')=='text'&&$this.attr('name')=='loginName'){
                $this.val(row.loginName);
            }
            else if($this.attr('type')=='radio'&&$this.val()==row.sex){
                $this.attr('checked','checked');
            }
            else if($this.attr('type')=='text'&&$this.attr('name')=='actorId'){
                $this.val(row.actorName);
                $this.attr('actorId',row.actorId);
            }
            else if($this.attr('type')=='text'&&$this.attr('name')=='orgId'){
                $this.val(row.orgName);
                $this.attr('orgId',row.orgId);
            }
            else if($this.attr('type')=='text'&&$this.attr('name')=='telphone'){
                $this.val(row.telphone);
            }
            else if($this.attr('type')=='hidden'&&$this.attr('name')=='id'){
                $this.val(row.id);
            }
        });
        openWin('btnUserSave','btnUserClose','userWind','用户编辑');
    });

    //角色分配 链接点击
    $(document).on('click','a[type=doActor]',function () {
        doActorType = "link";
        openEasyWin('btnActorSave','btnActorClose','actorWin','角色选择',{draggable:true});
        initTree('actorTree',contextPath+'actor/getActorTree',true,false);
    });

    //操作用户状态
    $(document).on('change','select',function(){
        var that = $(this);
      confirm('用户状态改变提示','用户状态只有在【正常】情况下才能登陆,【注销】状态的用户无法登陆,确认继续操作吗？',function (r) {
          if(r){
              if(that.attr('oldState')==that.val())return; //状态没有发生改变。
              var obj = {};
              obj.id = that.attr("userID");
              obj.status = that.val();
              doData(contextPath+'user/changeStatus',obj,function (data) {
                 if(data=='success'){
                     showMessage('提示','用户状态已经被改变');
                     delayRefresh();
                 }else {
                     alert('错误提示','用户状态改变失败','error');
                 }
              });
          }else {
             window.location.reload();
          }
      });
    });
});

