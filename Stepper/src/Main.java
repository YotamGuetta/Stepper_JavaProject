import static java.lang.System.*;

public class Main {
    public static int l = 1;
    public static int m = 1;
    public static void main(String[] args) {
        class b {
            int z;
            int y;
            b(int i){
                z=i;
                y=m;
                m++;
            }

        }
        class a extends b{
             int x;

             b z;
            a(){
                super(l);
                x=l;
                l++;
            }
        }

        String[] s = {"a", "b"};
        b[] a ={new a(),new a(),new a(),new a()};
        for (b str: a) {
            out.println(str.z+", "+ str.y);
        }
    }

}
