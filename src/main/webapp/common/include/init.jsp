<%--
  Created by IntelliJ IDEA.
  User: fangjiang
  Date: 2018/3/25
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<!--引入easyUI bootstrap插件相关文件 引入该文件后无需再引入rootPath.jsp文件-->
<%
    String basePath = request.getScheme()+"://"+
            request.getServerName()+":"+request.getServerPort()+"/"+
            request.getContextPath()+"/";
    String jsRoot = basePath+"common/js/";
    String cssRoot = basePath+"common/css/";
    String pluginRoot = basePath+ "common/plugin/";
%>

<!--整个工程的样式-->
<link type="text/css" rel="stylesheet" href="<%=cssRoot%>project.css"/>
<!--引入easyui主题样式-->
<link type="text/css" rel="stylesheet" href="<%=pluginRoot%>easyui/themes/bootstrap/easyui.css"/>
<!--引入图标样式文件-->
<link type="text/css" rel="stylesheet" href="<%=pluginRoot%>easyui/themes/icon.css"/>
<!--引入bootstrap核心样式文件-->
<link type="text/css" rel="stylesheet" href="<%=pluginRoot%>bootstrap-3.3.7/css/bootstrap.css"/>
<!--引入bootstrap主题样式文件-->
<link type="text/css" rel="stylesheet" href="<%=pluginRoot%>bootstrap-3.3.7/css/bootstrap-theme.css/>"
<!--引入jquery.min.js脚本库-->
<script type="text/javascript" src="<%=pluginRoot%>jquery.min.js"></script>
<!--引入easyUI所依赖的脚本文件-->
<script type="text/javascript" src="<%=pluginRoot%>easyui/jquery.easyui.min.js"></script>
<!--引入bootstrap的js文件-->
<script type="text/javascript" src="<%=pluginRoot%>bootstrap-3.3.7/js/bootstrap.js"></script>
<!--引入jQuery-form-->
<script type="text/javascript" src="<%=jsRoot%>jquery.form.js"></script>