/**
 * Created by fangjiang on 2018/4/13.
 */
var contextPath = '';

function setContextPath(rootPath) {
    contextPath = rootPath;
}

//初始化用户信息
function initPersonInfo() {
    doData(contextPath+'user/getUserInfo',{},function (data) {
        $('#personInfo').find(':input').each(function (index,element) {
            element = $(element);
            var name = element.attr('name');
            if(name=='name'){
                element.val(data.name);
            }
            else if(name=='loginName'){
                element.val(data.loginName);
            }
            else if(name=='sex'&&element.val()==data.sex){
                element.attr('checked','true');
            }
            else if(name=='telphone'){
                element.val(data.telphone);
            }
            else if(name=='orgName'){
                element.val(data.organization.name);
            }
            else if(name=='actorName'){
                element.val(data.actorList[0].actorName);
            }
            else if(name=='status'){
                element.val(data.status);
            }
        });
    });
}

// function doSubmit() {
//     var obj = serializeArrayToObject('personInfo');
//     for(var key in obj){
//         if($.trim(obj[key].name)==''){
//             alert('姓名不能为空');
//             return;
//         }
//         else if($.trim(obj[key].loginName)==''){
//             alert('登录名不能为空');
//             return;
//         }
//         else if($.trim(obj[key].telphone)==''){
//             alert('电话号码不能为空');
//             return;
//         }
//     }
//     doData(contextPath+'user/updateUserInfo',obj,function (data) {
//         if(data=='200'){
//             showMessage('提示','用户信息更改成功');
//             delayRefresh();
//         }else {
//             alert('错误提示','用户信息更改失败','error');
//         }
//     });
// }


$(function () {
    initPersonInfo();
    $('#doSubmit').click(function () {
        var obj = serializeArrayToObject('personInfo');
        for(var key in obj){
            if(key=='name'&&$.trim(key)==''){
                alert('姓名不能为空');
                return;
            }
            else if(key=='loginName'&&$.trim(key)==''){
                alert('登录名不能为空');
                return;
            }
            else if(key=='telphone'&&$.trim(key)==''){
                alert('电话号码不能为空');
                return;
            }
        }
        doData(contextPath+'user/updateUserInfo',obj,function (data) {
            if(data=='200'){
                showMessage('提示','用户信息更改成功');
                delayRefresh();
            }else {
                alert('错误提示','用户信息更改失败','error');
            }
        });
    });
});