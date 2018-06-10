/**
 * Created by fangjiang on 2018/4/2.
 */
public enum TestEnum {
    Red("红色",1),Black("黑色",2);
    private String name;
    private int index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private TestEnum(String name, int index){
     this.name = name;
     this.index = index;
   }
}
