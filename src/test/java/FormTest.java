import org.junit.Test;


/**
 * Created by fangjiang on 2018/4/2.
 */
public class FormTest {
//    @Test
//    public void testClassLoader(){
//        URL resourceName =
//                this.getClass().getClassLoader().getResource("abc");
//        System.out.println(resourceName);
//    }
//
//    @Test
//    public void testGetCurrentDate() throws ParseException {
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat =
//                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date2 = simpleDateFormat.parse(simpleDateFormat.format(date));
//        System.out.println(simpleDateFormat.parse(simpleDateFormat.format(date)));
//    }
//
//    @Test
//    public void testMoreArgs(){
//        int index = TestEnum.Black.getIndex();
//        System.out.println(index);
//    }
//
//    public void demo1(String ...args){
//        System.out.println(args.length);
//        for (String arg:args){
//            System.out.println(arg);
//        }
//
//    }
//
//    //测试检测资源
//    @Test
//    public void testCheckData(){
//
//    }

    public static  void main(String[] args){

      StringBuilder sb = new StringBuilder();
      sb.append("123456");
      sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
    }

    @Test
    public void demo1(){
        String testStr = "orgName=人事部门";
        if(testStr.contains("orgName")){
            System.out.println(testStr.indexOf("="));
        }
    }

    @Test
    public void demo2(){
        String testStr = "${salary>=5000}";
        int arg0 = 6000;
        if(testStr.startsWith(ELConst.START_EXPRESSION)&&testStr.endsWith(ELConst.END_EXPRESSION)){
           testStr = testStr.replace(ELConst.START_EXPRESSION,"").replace(ELConst.END_EXPRESSION,"");

            if(testStr.contains(ELConst.GREATER_EQUAL)){
                boolean flag =  arg0 >= Integer.parseInt(testStr.split(ELConst.GREATER_EQUAL)[1]);
                System.out.println(flag);
            }
           else if(testStr.contains(ELConst.GREATER_THAN)){
              boolean flag =  arg0 > Integer.parseInt(testStr.split(ELConst.GREATER_THAN)[1]);
                System.out.println(flag);
            }
            else if(testStr.contains(ELConst.LESS_EQUAL)){
                boolean flag =  arg0 <= Integer.parseInt(testStr.split(ELConst.LESS_EQUAL)[1]);
                System.out.println(flag);
            }
            else if(testStr.contains(ELConst.LESS_THAN)){
                boolean flag =  arg0 < Integer.parseInt(testStr.split(ELConst.LESS_THAN)[1]);
                System.out.println(flag);
            }

            else if(testStr.contains(ELConst.EQUAL)){
                boolean flag =  arg0 == Integer.parseInt(testStr.split(ELConst.EQUAL)[1]);
                System.out.println(flag);
            }
            else if(testStr.contains(ELConst.NOT_EQUAL)){
                boolean flag =  arg0 != Integer.parseInt(testStr.split(ELConst.NOT_EQUAL)[1]);
                System.out.println(flag);
            }
        }else {
            System.out.println("EL表达式格式有问题，请检查流程定义文件中表达式");
        }
    }
}
