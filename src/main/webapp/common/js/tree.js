/**
*@description 一般使用树的操作 简化easyui tree的使用(抽取共用的部分)
*@param treeId
*@param url
*@param single
*@param callback
*@param unCheck 取消是否回调函数
*@author  fangjiang
*@date 2018/4/6 20:37
*/
//方法入口处
function initTree(treeId,url,single,callback,unCheck) {
    $("#"+treeId).tree({
        url:url,
        method: 'post',
        animate: true,
        checkbox: true,
        onlyLeafCheck: true,
        onClick: function(node){
            treeClick(treeId,node,'click',single,callback);
        },
        onCheck: function (node,checked) {
            if(unCheck&&!checked){
                callback(node);
            }else {
              treeClick(treeId,node,'check',single,callback,checked);
            }
        }
    });
}

/**
*@description 对initTree进行的拓展
*@author  fangjiang
*@date 2018/4/15 10:27
*/

function initTreeExtend(options) {
    var defaults ={
        treeId: '',
        url: null,
        single: true,
        animate: true,
        method: 'post',
        checkbox: true,
        onlyLeafCheck: false,
        callback: false,
        unCheck: false,
        onClick: function(node){
            treeClick(this.treeId,node,'click',this.single,this.callback);
        },
        onCheck: function (node,checked) {
            if(!checked&&this.unCheck){
                    this.callback(node);
            }else{
              treeClick(this.treeId,node,'check',this.single,this.callback,checked);
            }
        }
    };
    //合并参数
    $.extend(true,defaults,options);
    $("#"+defaults.treeId).tree(defaults);
}

/**
*@description 点击与勾选的联动
 * @param treeId 树ID
 * @param node 被点解的节点
 * @param type 被点击的类型
 * @param checked 是取消还勾选(取消暂时没有处理 选择不存在取消 只有在勾选复选框有取消的概念)
 * @param single 是要达到单选效果还是复选的效果
 * @param callback 回调函数(节点被点击之后响应的事件)
*@author  fangjiang
*@date 2018/4/6 20:39
*/

function treeClick(treeId,node,type,single,callback,checked) {
    var treeDom = $('#'+treeId);
    var nodes = treeDom.tree('getChecked');
    if(type == 'click'){
        if(single){
            //取消所有之前勾选的数据
            for(var i=0;i<nodes.length;i++){
                if(nodes[i].id==node.id) continue;
                treeDom.tree('uncheck',nodes[i].target);
            }
        }
        treeDom.tree('check',node.target);
        if(callback){
            callback(node);
        }
    }
    else if(type == 'check'&&checked==true){
        if(single){
            for(var i=0;i<nodes.length;i++){
                if(nodes[i].id==node.id) continue;
                treeDom.tree('uncheck',nodes[i].target);
            }
        }
        if(callback){
            callback(node);
        }
    }
}

/**
*@description 获取当前所有被选择的节点
 * @param treeId
 * @param single 单个还是所有
*@author  fangjiang
*@date 2018/4/6 21:02
*/
function getSelectTreeNode(treeId,single) {
        var treeDom = $("#"+treeId);
        var nodes = treeDom.tree('getChecked');
        if(single){
            return nodes[0];
        }
       return nodes;
}



