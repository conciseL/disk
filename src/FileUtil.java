import javax.swing.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LHZ
 * @Date 2021/11/8 16:59
 */
public class FileUtil {

    public static void main(String[] args) throws IOException {
        File f = new File("H:\\\\");
        String[] str = getFileName(f);
        System.out.println("文件长度:"+str.length);
    }


    /**
     * 获取指定文件夹下的文件名称
     *
     * @return
     */
    public static String[] getFileName(File file) {
        //文件列表
        File[] files = file.listFiles();
        System.out.println("文件数量：" + files.length);
        String[] str = new String[files.length];
        int i = 0;
        for (File f : files) {
            String name = f.getName();
            if (!name.equals("System Volume Information")) {
                System.out.println("文件名：" + name);
                str[i] = name;
                i = i + 1;
            } else {
                System.out.println("跳过系统卷标信息");
            }
        }
        String[] res = new String[i];
        for (int j = 0; j < res.length; j++) {
            res[j] = str[j];
        }
        return res;
    }

    /**
     * 比较两个列表，输出不同
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    public static <T> List<T> compare(T[] t1, T[] t2) {
        List<T> list1 = Arrays.asList(t1);
        List<T> list2 = Arrays.asList(t2);
        List<T> list3 = new ArrayList<>();//用来保存最后结果的集合
        for (T t : t2) {
            if (!list1.contains(t)) {
                list3.add(t);
            }
        }
        for (T t:t1) {
            if (!list2.contains(t)) {
                list3.add(t);
            }
        }
        return list3;
    }

}
