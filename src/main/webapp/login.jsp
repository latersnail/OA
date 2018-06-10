<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <%@include file="/common/include/init.jsp"%>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=cssRoot%>login.css">
    <script>
        $(document).keyup(function(event){
            if (event.keyCode == "13") {//keyCode=13是回车键
                $('#btnLogin').click();
            }
        });
        function  refreshImg() {
            document.getElementById("imgCode").src="<%=basePath%>imageCode/showcode?timestamp="+(new Date()).getTime();
        }
        $(function () {
            if(window.parent.length>0)
                window.parent.location=location;
            $("#btnLogin").click(function () {
                if ($("#txtNumber").val()=="" || $("#txtPwd").val()=="" || $("#txtCode").val()=="")
                {
                    alert("账号、密码及验证码都不能为空！");
                    return;
                }

                /*验证验证码*/
              doData("<%=basePath%>imageCode/getCode",{},function (data) {
                    if ($("#txtCode").val()!=data.result)
                    {
                        refreshImg();
                        alert("验证码错误！");
                        return;
                    }
                    else
                    {
                        var user=new Object();
                        user.name=$("#txtNumber").val();
                        user.password=$("#txtPwd").val();
                        user.deviceType=0;//登录设备类型
                        doData("<%=basePath%>user/login",user,function (result) {
                            switch (parseInt(result))
                            {
                                case 0:
                                    alert("账号或密码有误！");
                                    break;
                                case -100:
                                    alert("当前账号已经锁定，无法使用，请联系管理员！");
                                    break;
                                case -200:
                                    alert("当前账号已经注销，无法使用，请联系管理员！");
                                    break;
                                case -300:
                                    alert("当前账号的状态不正常，不能使用，请联系管理员！");
                                    break;
                                case 1:
                                    location.href="/jsp/index.jsp";
                                    break;
                                default:
                                    alert("请采用PC端进行系统登录！");
                                    break;
                            }
                            refreshImg();
                            $("#txtNumber").val("");
                            $("#txtPwd").val("");
                        });
                    }
                })

            });

        });
    </script>
</head>

<body>
<div id="dl_bg"></div>
<div id="dl_box">
    <form action="" name="">
        <div class="dl_text dl_name">
            <input type="text" id="txtNumber" maxlength="20" name="ht_dl_name" placeholder="请输入用户名">
        </div>
        <div class="dl_text dl_pass">
            <input type="password" id="txtPwd" maxlength="20" name="ht_dl_pass" placeholder="请输入密码">
        </div>
        <div class="dl_yzm">
            <input type="text" maxlength="4" id="txtCode"  name="ht_dl_yzm" placeholder="请输入验证码">
            <div class="dl_yzmbf"><img id="imgCode" border=0 src="<%=basePath%>imageCode/showcode" width="100" height="40" style="cursor:pointer;" onclick="refreshImg()" alt="验证码" title="单击刷新验证码"></div>
        </div>
        <div class="dl_sub">
            <input type="button" id="btnLogin" name="ht_dl_sub" value="登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录">
        </div>
    </form>
</div>
<div id="dl_footer">© 2018 江西理工大学(南昌校区)毕业设计项目</div>
</body>
</html>
