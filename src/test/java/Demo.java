import org.junit.Test;

/**
 * Created by fangjiang on 2018/4/7.
 */
public class Demo {
    /**
    *@description 多态的测试
    *@author  fangjiang
    *@date 2018/4/7 21:27
    */

    @Test
    public void demo1(){
        Parent son = new Son();
        Son son1 = (Son) son;
        son1.test1();
        System.out.println(son instanceof Parent);
    }

    /**
    *@description 测试游标
    *@author  fangjiang
    *@date 2018/4/9 16:37
    */

    @Test
    public void demo2(){
        String str = "abcdef";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        System.out.println(stringBuilder.toString());
    }

}
class Parent{

}
class Son extends Parent{
  public void test1(){
      System.out.println("@@@@@@@@@@@@@@@@@@@@");
  }
}