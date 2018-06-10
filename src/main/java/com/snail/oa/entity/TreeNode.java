package com.snail.oa.entity;

import java.util.List;

/**
 * Created by fangjiang on 2018/4/1.
 */
public class TreeNode {
    private String id;
    private TreeNode parentNode;
    private List<TreeNode> children;
    private String text;
    private String type = "radio";//
    private Attributes attributes;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public TreeNode(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public TreeNode() {
    }
}
