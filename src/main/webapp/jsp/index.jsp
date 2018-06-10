<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/include/init.jsp"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>OA业务管理系统</title>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=cssRoot%>index.css">
</head>
<body class="easyui-layout">

<div data-options="region:'north',border:false"
     style="height:100px; line-height:100px;background-image: url(<%=basePath%>common/images/banner.jpg);background-repeat: no-repeat;background-size: 100% 100px;overflow: hidden;">
    <div class="banner_font">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OA 办公自动化系统</div>
    <!-- 顶部状态栏 -->
    <div style="float:right;padding-top:15px;padding-right:20px; text-align:right;display: inline-block">
        <div class="user" style="font-size: 18px;font-weight: 800;">
            <span style="color: white;">欢迎您 ${user.name}</span>
            <a href="javascript:void(0)" id="btnUpdatePwd">更改密码</a>
            <a href="javascript:void(0)" id="btnLogout">安全退出系统</a>
            <a href="<%=basePath%>user/create">测试</a>
        </div>
    </div>
</div>
<!--头部-->
<div data-options="region:'west',split:false,collapsible:true,title:'导航菜单'"
     style="width:200px;padding:0;" id="dMenu">
    <div id="dMyMenu" class="easyui-accordion"  style="width:196px;">

    </div>
</div>

<div data-options="region:'south',border:false,height:30" style="padding:6px 0;text-align: center;">
    Copy Right &copy 2018 江西理工大学(南昌校区) 毕业设计项目 版权所有</div>
<div data-options="region:'center'">
    <div class="easyui-tabs" id="tWork"
         data-options="fit:true,border:false,plain:true">
        <div title="个人桌面">
            <iframe src="<%=basePath%>jsp/home.jsp" width="100%" height="99%;" frameborder="0"> </iframe>
        </div>
    </div>
</div>
<!--组织结构选择-->
<div id="updatePwd" class="easyui-window" title="密码修改" data-options="modal:true,maximizable:false,closed:true,iconCls:'icon-filter',footer:'#updatePwdFooter'" style="width:500px;height:300px;padding:10px;">
    <span>请输入旧密码:</span>&nbsp;<input type="password" name="oldPassowrd" id="oldPassowrd" class="form-control" style="width: 300px;"/><br/>
    <span>请输入新密码:</span>&nbsp;<input type="password" name="passowrd" id="password" class="form-control" style="width: 300px;"/><br/>
    <span>再次输入新密码:</span><input type="password" name="repwd" id="repwd" class="form-control" style="width: 300px;"/>
</div>
<div id="updatePwdFooter" style="padding:5px;text-align: center;">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" style="width:90px;height:30px;" id="btnSave">保存</a>
    <a href="#" class="easyui-linkbutton"  style="width:90px;height:30px;" id="btnClose">取消</a>
</div>
</body>
<script type="text/javascript">
    function addTab(data) {
        var $this = $(data);
        var url = $this.attr('url');
        var title = $this.attr('sourceName');
        if ($('#tWork').tabs('exists', title)) {
            $('#tWork').tabs('select', title);
        } else {
            var content = '<iframe scrolling="auto" frameborder="0"  src="'+ url + '" style="width:100%;height:98%;"></iframe>';

            $('#tWork').tabs('add', {
                title : title,
                content : content,
                closable : true,
                fit:true
            });
        }
    }

    /**
    *@description 加载用户资源
    *@author  fangjiang
    *@date 2018/4/15 17:33
    */

    function initSource() {
        $.messager.progress({title:'正在加载',msg:'正在加载资源'});
       doData('<%=basePath%>authority/initSource',{},function (data) {
          if(data){
              for(var i=0;i<data.length;i++){
                var content = '';
                var sources = data[i].sourceList;
                for(var j=0;j<sources.length;j++){
                    var source = sources[j];
                    content += '<a href="#" onclick="addTab(this)" url="<%=basePath%>'+source.sourcePath+'" sourceName="'+source.name+'"><li class="li_bian" id="'+source.id+'">'+source.name+'</li></a>';
                }
                content += '';
                $('#dMyMenu').accordion('add',{
                    title: data[i].name,
                    content: content,
                    selected: false
                });
              }
          }
       });
       window.onload = function () {
         $('#dMyMenu').children('div').each(function () {
             $(this).css('margin',0);
         });
           $.messager.progress('close');
       }
    }

    $(function() {

        initSource();
        $("#btnLogout").click(function () {
            confirm('退出提示','你确定要退出系统吗?',function (r) {
              if(r){
                  simpleAjax("<%=basePath%>user/logout",function () {
                      window.location.href='<%=basePath%>login.jsp';
                  });
              }
            });
        });

        $('#btnUpdatePwd').click(function () {
            openEasyWin('btnSave','btnClose','updatePwd','密码修改');
        });

        $('#btnSave').click(function () {
            var oldPassword = $('#oldPassowrd').val();
            var password = $('#password').val();
            var repwd = $('#repwd').val();
            if($.trim(password)==''){
                alert('密码不能为空!');
                return;
            }
            if(password!=repwd){
                alert('两次输入的密码不一致');
                $('#password').val('');
                $('#repwd').val('');
                return;
            }
            //当前操作不科学，需要优化。 user为session中的
            if(oldPassword!="${user.password}"){
                alert('原密码输入错误，请重新输入');
                return;
            }
            var obj = {};
            obj.password = password;
            doData('<%=basePath%>user/updatePwd',obj,function (data) {
                if(data=="success"){
                    $('#updatePwd').window('close');
                    alert('密码更改成功!');
                    doData("<%=basePath%>user/logout",null,function () {
                        location.href="login.jsp";
                    });
                }else {
                    alert('错误提示',"密码更改失败,请联系管理员",'error')
                }
            });

        });

        $('#btnClose').click(function () {
            $('#updatePwd').window('close');
        });
    });

    function showTab(title,url) {
        if ($('#tWork').tabs('exists', title)) {
            $('#tWork').tabs('select', title);
        } else {
            var content = '<iframe scrolling="auto" frameborder="0"  src="'+ url + '" style="width:100%;height:98%;"></iframe>';

            $('#tWork').tabs('add', {
                title : title,
                content : content,
                closable : true,
                fit:true
            });
        }
    }
</script>

</html>
