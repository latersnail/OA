/**
 * Created by fangjiang on 2018/3/24.
 */
/********************所有的JSON数据的序列化和反序列化统一使用json2.js文件******************************************/
/**
*@description 处理常规的ajax请求
*@author  fangjiang
*@date 2018/3/24 13:29
*/

/*将$(form).serializeArray()转对象*/
function serializeArrayToObject(formId) {
    var obj = {};
    $('#'+formId).find(":input").each(function (index,element) {
        var tagName = this.tagName;
        var $this = $(this);
        var name = $this.attr('name');
        var value = '';
        if(tagName=='TEXTAREA'){
           value = $this.val().split('\n').join('$');
        }else {
            value = $this.val();
        }
        if(name==null||$.trim(name)==''){
            console.error('表单中出现没有name属性的input or textarea or select元素');
        }
        eval('obj.'+name+'= "'+value+'"');
    });
    return obj;
}
//清空表单中所有需要提交的数据
function clearForm(formId) {
    $('#'+formId).find(":input").each(function (index,element) {
        var $element = $(element);
        if($element.attr('type')=='radio'){

        }else {
            $(element).val('');
        }

    });
}

function doData(url,params,callback) {
    if(params)params=JSON.stringify(params);
    else params = {};
    $.ajax({
        type: 'post',
        url: url,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: params,
        success: function (data) {
            if (callback) callback(data);
        },
        error: function (arg0, arg1, arg2) {
            switch (arg0.status) {
                case 200:
                    alert("当前操作可能未授权操作，服务器未响应，请联系管理员进行处理，问题发生地址：" + url);

                    location.href = "../login.jsp";
                    break;
                case 404:
                    alert("当前操作的资源不存在，请联系管理员！");
                    break;
                case 500:
                    alert("程序内容处理错误：500,内部符号" + url);
                    break;
                default:
                    alert("数据处理错误,错误代码：" + arg0.status);
                    break;
            }
        }
    });
}

/**********************************************
 * datagrid的Ajax的数据分页查找
 * page:页码，从1开始
 * rows:每页行数
 * datagridId:datagrid的ID
 * params:参数对象
 * type:操作类别
 *********************************************/
function findData(url,page, rows,datagridId,params,callback) {
    $('#'+datagridId).datagrid("loading");
    $('#'+datagridId).datagrid('getPager').pagination({
        beforePageText: '第',
        afterPageText: '页   共{pages}页',
        displayMsg: '第{from}到{to}条，共{total}条'
    });
    params.pageNumber=page;
    params.rowsCount=rows;
    $.ajax({
        type : 'post',
        url : url,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data :JSON.stringify(params),
        success: function (obj) {
            $('#'+datagridId).datagrid("options").pageNumber=page;
            $('#'+datagridId).datagrid("options").pageSize=rows;
            $('#'+datagridId).datagrid('loadData',obj);
            $('#'+datagridId).datagrid("loaded");
            if(callback){
                callback(rows);//成功后回调操作
            }
        },
        error : function(arg0,arg1,arg2) {
            $('#'+datagridId).datagrid("loaded");
            switch(arg0.status)
            {
                case 200:
                    alert("当前操作可能未授权操作，服务器未响应，请联系管理员进行处理，问题发生地址：" + url);
                    top.location.href = "../login.jsp";
                    break;
                case 404:
                    alert("当前操作的资源不存在，请联系管理员！");
                    break;
                case 500:
                    alert("程序内容处理错误：500,内部符号"+url);
                    break;
                default:
                    alert("数据处理错误,错误代码："+arg0.status);
                    break;
            }
        }
    });
}

/**
*@description 查找分页
*@author  fangjiang
*@date 2018/3/26 13:35
*/

function findByPage(url,id,con,callback)
{
    setPager(url,id,con,callback);
    findData(url, 1, 10, id, con,callback);
}


function setPager(url,id,con,callback)
{
    var pager = $('#'+id).datagrid().datagrid('getPager'); // get the pager of datagrid
    pager.pagination({
        pageSize : 10,//每页显示的记录条数，默认为10
        pageList : [ 10, 20, 30,40,50 ],//可以设置每页记录条数的列表
        onSelectPage : function(pageNumber, pageSize) {

            findData(url,pageNumber, pageSize,id,con,callback);
        },
        onBeforeRefresh : function(pageNumber, pageSize) {
            findData(url,pageNumber, pageSize,id,con,callback);
        }
    });
}

function simpleAjax(url,callback) {
   $.ajax({
       type: 'post',
       url: url,
       success:function (data) {
          callback(data);
       }
   })
}
/**
*@description 以下简化easyUI的使用方式
*@author  fangjiang
*@date 2018/4/1 19:43
*/
$.extend($.messager.defaults,{
    ok: '确定',
    cancel: '取消'
});
// alert icon的取值  error,question,info,warning
window.alert = function (title,msg,icon,callback) {
    //添加判断  兼容之前单使用浏览器的alert 以避免造成大量的修改 同时也使用默认方式接收单参数
    if(arguments.length==1){
        msg = title;
        title = '提示';
        icon = 'info';
    }
    return $.messager.alert(title,msg,icon);
};

//confirm
window.confirm = function (title,msg,callback) {
  return  $.messager.confirm(title,msg,callback);
};

//show
window.showMessage = function (title,msg) {
 return   $.messager.show({
           title:title,
           msg:msg
         });
};

/*************************************************
 * 将数值型的日期转换成长日期格式，
 * 例如：1495977369000；YYYY-MM-DD  hh:mm:ss
 * 返回：字符串值
 *************************************************/
Date.prototype.getLongDate=function(value){
    var d=new Date(value);
    return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getMinutes()+":"+d.getMilliseconds();
};
//短日期格式
Date.prototype.getShortDate=function(value){
    var d=new Date(value);
    return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
};

/********************START***************************
 *对easyui窗口进行拓展
 *************************************************/
//让window居中
var easyuiPanelOnOpen = function (left, top) {
    var iframeWidth = $(this).parent().parent().width();

    var iframeHeight = $(this).parent().parent().height();

    var windowWidth = $(this).parent().width();
    var windowHeight = $(this).parent().height();

    var setWidth = (iframeWidth - windowWidth) / 2;
    var setHeight = (iframeHeight - windowHeight) / 2;
    $(this).parent().css({/* 修正面板位置 */
        left: setWidth,
        top: setHeight
    });

    if (iframeHeight < windowHeight)
    {
        $(this).parent().css({/* 修正面板位置 */
            left: setWidth,
            top: 0
        });
    }
    $(".window-shadow").hide();
};
$.fn.window.defaults.onOpen = easyuiPanelOnOpen;

var easyuiPanelOnResize = function (left, top) {


    var iframeWidth = $(this).parent().parent().width();

    var iframeHeight = $(this).parent().parent().height();

    var windowWidth = $(this).parent().width();
    var windowHeight = $(this).parent().height();


    var setWidth = (iframeWidth - windowWidth) / 2;
    var setHeight = (iframeHeight - windowHeight) / 2;
    $(this).parent().css({/* 修正面板位置 */
        left: setWidth-6,
        top: setHeight-6
    });

    if (iframeHeight < windowHeight) {
        $(this).parent().css({/* 修正面板位置 */
            left: setWidth,
            top: 0
        });
    }
    $(".window-shadow").hide();
    //$(".window-mask").hide().width(1).height(3000).show();
};
$.fn.window.defaults.onResize = easyuiPanelOnResize;
/********************************END*************************************************/

/**
*@description
 * @param btnId1  按钮1
 * @param btnId2 按钮2
 * @param mainWindId 窗口ID
 * @param title 窗口名称
 * @param options 窗口其他属性
*@author  fangjiang
*@date 2018/4/7 13:43
*/

//打开easyUI的窗口常用操作
function openEasyWin(btnId1,btnId2,mainWinId,title,options) {
   if(btnId1) $('#'+btnId1).show();
   if(btnId2)$('#'+btnId2).show();
    if(options){
        options.title = title;
        $('#'+mainWinId).panel(options);
    }else {
        $('#'+mainWinId).panel({title:title});
    }
    $('#'+mainWinId).window("open");
}

/***********************************************
 *对easyui窗口进行拓展    end
 *************************************************/

//延迟刷新页面
function delayRefresh(timeout) {
    if(!timeout){
        timeout = 2000; //default 2000
    }
    setTimeout("reloadPage()",timeout);
}

function reloadPage() {
    window.location.reload();
}

//增加String的replaceAll方法
String.prototype.replaceAll  = function(s1,s2){
    return this.replace(new RegExp(s1,"gm"),s2);
};





