<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/4/13
  Time: 18:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/include/init.jsp"%>
<html>
<head>
    <title>个人信息表</title>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
    <style>
        body{
            overflow-y: hidden;
            overflow-x: hidden;
        }
        .inputBorder{
            border: none;
            background: white;
        }
        .personInfo-body{
            padding-top: 20px;
            border: 2px solid blue;
            height: 370px;
        }
        table{
            position: relative;
            left: 10%;
        }
        td{
            padding-top: 15px;
        }
        .personInfo-footer{
            position: relative;
            top: 40px;
            left: 30%;
        }
        .personInfo-head{
            background: white;
            position: relative;
            left: 20px;
            top: 16px;
            width: 90px;
        }
    </style>
</head>
<body>
<div>
    <div class="personInfo-head">
    <span>个人基本信息</span>
    </div>
    <div >
     <form id="personInfo" class="personInfo-body">
         <table>
            <tr>
                <td><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;姓名:</span></td>
                <td>&nbsp;&nbsp;<input  type="text" name="name" value=""/></td>
            </tr>
             <tr>
                 <td><span>登录名称:</span></td>
                 <td>&nbsp;&nbsp;<input  type="text" name="loginName"/></td>
             </tr>
             <tr>
                 <td><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;性别:</span></td>
                 <td>&nbsp;&nbsp;<input  type="radio" name="sex" value="男"/>男
                     <input  type="radio" name="sex" value="女"/>女</td>
             </tr>
             <tr>
                 <td><span>电话号码:</span></td>
                 <td>&nbsp;&nbsp;<input  type="text" name="telphone"/></td>
             </tr>
             <tr>
                 <td><span>部门名称:</span></td>
                 <td>&nbsp;&nbsp;<input class="inputBorder" type="text" name="orgName" disabled="disabled"></td>
             </tr>
             <tr>
                 <td><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;角色:</span></td>
                 <td>&nbsp;&nbsp;<input class="inputBorder" type="text" name="actorName" disabled="disabled"/></td>
             </tr>
             <tr>
                 <td><span>当前状态:</span></td>
                 <td>&nbsp;&nbsp;<input class="inputBorder" type="text" name="status" disabled="disabled"/></td>
             </tr>
         </table>
         <input type="hidden" name="id" value="${user.id}"/>
         <div class="personInfo-footer">
             <button class="btn btn-info btn-sm"  type="button" id="doSubmit">提交</button>
             <button class="btn btn-primary btn-sm" type="reset" style="margin-left: 10px;" id="reset" >重置</button>
         </div>
     </form>
    </div>
</div>
</body>
<script>
    $(function () {
        setContextPath('<%=basePath%>');
    });

</script>
<script type="text/javascript" src="<%=jsRoot%>personInfo.js"></script>
</html>
