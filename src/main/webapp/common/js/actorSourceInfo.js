/**
 * Created by fangjiang on 2018/4/14.
 */
var contextPath = '';
function setContextPath(rootPath) {
    contextPath = rootPath;
}

//对资源组数据遍历
function reloadTree(data) {
 uncheckAll();
 for(var i=0;i<data.length;i++){
    // var node = $('#chooseActorTree').tree('find',data[i].id);
    // if(node!=null){
    //  $('#chooseActorTree').tree('check',node.target);
    // }
     var sources = data[i].sourceList;
    for(var j=0;j<sources.length;j++){
        var node = $('#chooseSourceTree').tree('find',sources[j].id);
        if(node!=null){
           $('#chooseSourceTree').tree('update',{target:node.target,checkState:"checked"});
        }
    }
 }
}

//清除所有勾选
function uncheckAll() {
   var nodes = getSelectTreeNode('chooseSourceTree',false);
   if(!nodes)return;
   for(var i=0;i<nodes.length;i++){
       var node = nodes[i];
       $('#chooseSourceTree').tree('update',{target:node.target,checkState:"unchecked"});
   }
}

function doSubmit() {
    var actorNode = getSelectTreeNode('chooseActorTree',true);
    var actorId = actorNode.id;
    var sourceNodes = getSelectTreeNode('chooseSourceTree',false);
    var objs = [];
    for(var i=0;i<sourceNodes.length;i++){
        var obj ={};
        obj.actorId = actorId;
        obj.sourceId = sourceNodes[i].id;
        objs.push(obj);
    }
    doData(contextPath+'authority/saveOrUpdate',objs,function (data) {
        if(data=='success'){
            showMessage('提示','数据保存成功');
            delayRefresh();
        }else {
            alert('错误提示',"保存失败,请联系管理员",'error');
        }
    });
}

$(function () {

    //每次点击数的节点都将重新加载右边资源树
    initTree('chooseActorTree',contextPath+'actor/getActorTree',true,function (node) {
        var treeNode = getSelectTreeNode('chooseActorTree',true);
        if(treeNode){
            var obj ={};
            obj.actorId = node.id;
            //树加载完成后初始化数据
                doData(contextPath+'authority/getSourceByActorId',obj,function (data) {
                    reloadTree(data);
                });

        }else{
            uncheckAll();
        }
    },true);

    initTreeExtend({
        treeId: 'chooseSourceTree',
        url: contextPath+'sourceGroup/getSourceByGroup',
        single: false,
        unCheck: true
    });
});