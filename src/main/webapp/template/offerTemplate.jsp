<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/4/12
  Time: 10:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/include/init.jsp"%>
<html>
<head>
    <title>入职申请单</title>
    <script type="text/javascript" src="<%=jsRoot%>ajaxUtil.js"></script>
    <!--引入My97DatePicker日历控件-->
    <script type="text/javascript" src="<%=pluginRoot%>My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<div>
    <div>
        <span style="position:relative;left:30%;font-size: 18px;">入职申请单</span>
    </div>
    <div>
        <div style="position: relative;left: 20%;">
            <form id="templateForm" action="<%=basePath%>recvdoc/saveRecvDoc">
                <span >申请人：</span><br/>
                <input type="text" name="userName" value=""/><br/>
                <span>申请职位：</span><br/>
                <input type="text" name="quarters"/><br>
                <span>期望薪资：</span><br/>
                <input type="text" name="salary"/><br>
                <span>领导审批:</span><br/>
                <textarea name="suggestion" id="suggestion" cols="60" rows="8"></textarea>
            </form>
        </div>
    </div>
</div>
<div>

</div>
</body>
<script>
    $(function () {
        setContextPath("<%=basePath%>");
    });
    //初始化表单信息
    window.onload = function () {
        window.top.initForm(function (formData,type,isFirstUserTask) {
            console.log(formData);
            //根据type值确定是否需要冻结Input
            var isDisabled = true;
            if(type=='add'||isFirstUserTask=='true'){
                isDisabled = false;
            }
            $('#templateForm').find(':input').each(function (index,element) {
                var name = $(this).attr('name');
                if(isDisabled){
                    $(this).attr('disabled','true');
                }
                if(formData.user&&name=="userName"){
                    $(this).val(formData.user.name);
                }
                else if(formData.quarters&&name=="quarters"){
                    $(this).val(formData.quarters);
                }
                else if(formData.salary&&name=="salary"){
                    $(this).val(formData.salary);
                }
                else if(name=="suggestion"){
                    if(formData.suggestion)
                    $(this).val(replaceAll(formData.suggestion));
                    if(!isDisabled){
                        $(this).attr('disabled','disabled');

                    }else {
                        $(this).removeAttr('disabled');
                        $(this).attr('readonly','readonly');
                    }
                }
                //由于进入了办理流程所有属性除了领导审批之外都不能改动
            });
        });
    };

    function replaceAll(str) {
      return  str.split('$').join('\r\n');
    }
</script>
<script type="text/javascript" src="<%=jsRoot%>template.js"></script>
</html>
