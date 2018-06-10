package com.snail.oa.activiti;

/**
*@description 常见的EL表达式格式的常用运算符
*@author  fangjiang
*@date 2018/4/23 15:18
*/


public interface ELConst {
    String START_EXPRESSION = "${";
    String END_EXPRESSION = "}";
    String GREATER_THAN = ">";
    String GREATER_EQUAL = ">=";
    String LESS_THAN = "<";
    String LESS_EQUAL = "<=";
    String NOT_EQUAL = "!=";
    String EQUAL = "==";
    String[] SIGNAL_ARRAY = {">",">=","<","<=","!=","=="};
}
