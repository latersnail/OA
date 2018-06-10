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
    findByPage(contextPath+'recvdoc/processDoc','processDoc',obj,function (){});
}

/**************************************************************************
 *datagrid数据处理 共用   start
 **************************************************************************/
//格式化用户数据 姓名
function formatUser(value,rowData,rowIndex) {
    var name = '';
    if(rowData.userId!=null){
        var obj = {};
        obj.userId = rowData.userId;
        $.ajax({
            url: contextPath+'user/getUserById',
            type:'post',
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

//获取任务执行者 需要请求服务
function formatTakeActor(value,rowData,rowIndex){
    var name = '';
    if(rowData.takeActor){
        var obj = {};
        obj.userId = rowData.takeActor;
        $.ajax({
            url: contextPath+'user/getUserById',
            type:'post',
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
    return '<a href="#" onclick="signDocView(this)" recvDocId="'+rowData.taskId+'" processName="'+rowData.processName+'">签收</a>';
}


//查询
function serarchRecvDoc() {
    find();
}

//查看数据
function signDocView($this) {
    var id = '';
    if($this){
        $this = $($this);
        id = $this.attr('recvDocId');
    }else {
        var rows = $("#processDoc").datagrid('getSelections');
        if(rows.length>1){
            alert('一次只能查看一条数据')
            return;
        }
        if(rows.length==0){
            alert('请选择一条数据进行查看')
            return;
        }
        id = rows[0].id;
    }
    showDialog(contextPath+'recvdoc/input?type=view&id='+id,700,800);
}

$(function () {
    find();
    $('#serarchRecvDoc').click(function () {
        find();
    });
});