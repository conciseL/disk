import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author LHZ
 * @Date 2021/11/8 13:43
 */
public class HardDiskInfo {

    //系统最初磁盘数
    public static int SIZE;

    //磁盘数据全局变量
    public static Vector<Vector<Object>> VECTORS;

    //初始化长度
    public static int NUM = 7;

    //磁盘与cid绑定的全局list
    public static List<DetailInfo> list = new ArrayList<>(HardDiskInfo.NUM);

    //初始化，获取磁盘数量
    public static int getNum() {
        File[] parts = File.listRoots();
        //磁盘数量
        int size = parts.length;
        return size;
    }

    //检查U是否有U盘插入
    void checkU(Form form) {
        //JTable
        JTable jt = form.getJt();
        //获取表头
        Vector<String> columnNames = this.getColumnNames();
        //获取U盘信息
        HardDiskInfo.VECTORS = getRowData();
        DefaultTableModel dtm = new DefaultTableModel(VECTORS, columnNames);
        jt = new JTable(dtm) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        form.setJt(jt);
        //内容刷新
        form.getJsp().setViewportView(jt);

        //绑定list的信息
        int len = HardDiskInfo.VECTORS.size();
        System.out.println("此时的u盘数量:" + len);
        if (len > 0) {
            //获取U盘盘符
            String[] str = HardDiskInfo.getDiskPathStr();

            if(str.length > 0) {


                //必须先绑定一个，后续动态增加的时候可以在此基础判断
                if (len == 1) {
                    //vector里的第四项存的是sn
                    HardDiskInfo.list.get(0).setSn((String) HardDiskInfo.VECTORS.get(0).get(3));
                    HardDiskInfo.list.get(0).setDisk(str[0]);
                }


                if (len > 1) {
                    //未绑定盘
                    String disk = "";
                    //已经绑定的盘
                    String[] judge = new String[len - 1];
                    for (int i = 0; i < len - 1; i++) {
                        judge[i] = HardDiskInfo.list.get(i).getDisk();
                    }
                    //比较绑定盘与新增加的差异，未新增加的盘符加绑定
                    disk = FileUtil.compare(judge, str).get(0);

                    //绑定
                    HardDiskInfo.list.get(len - 1).setSn((String) HardDiskInfo.VECTORS.get(len - 1).get(3));
                    HardDiskInfo.list.get(len - 1).setDisk(disk);
                }

                //list
                for (int i = 0; i < HardDiskInfo.NUM; i++) {
                    System.out.println(HardDiskInfo.list.get(i));
                }

                System.out.println("此时的List长度：" + HardDiskInfo.list.size());

            }
        }

    }

    //获取U盘信息
    public Vector<Vector<Object>> getRowData() {
        String commond = "reg query HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\services\\USBSTOR\\Enum";
        Vector<Vector<Object>> data = null;
        try {
            //获取注册表信息
            Process ps = null;
            ps = Runtime.getRuntime().exec(commond);
            ps.getOutputStream().close();
            InputStreamReader i = new InputStreamReader(ps.getInputStream());
            String line;
            BufferedReader ir = new BufferedReader(i);
            int count = 0;
            data = new Vector<Vector<Object>>();
            //将信息分离出来
            while ((line = ir.readLine()) != null) {                if (line.contains("USB\\VID")) {
                    count++;
                    for (String s : line.split("    ")) {
                        if (s.contains("USB\\VID")) {
                            Vector<Object> v = new Vector<Object>();
                            for (String ss : s.split("\\\\")) {
                                if (ss.contains("VID")) {
                                    for (String sss : ss.split("&")) {
                                        v.add(sss);
                                    }
                                } else if (ss.contains("USB")) {
                                    v.add(ss + count);
                                } else {
                                    v.add(ss);
                                }
                            }
                            data.add(v);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    //设置表头信息
    public Vector<String> getColumnNames() {
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("USB");
        columnNames.add("VID");
        columnNames.add("PID");
        columnNames.add("SN");
        return columnNames;
    }

    //获取盘符
    public static String[] getDiskPathStr() {
        File[] parts = File.listRoots();
        int len = parts.length;
        //多出原有系统的盘符的数量
        int count = len - HardDiskInfo.SIZE;
        System.out.println("多出系统盘的盘符数量：" + count);
        String[] str = new String[count];
        for (int i = 0; i < count; i++) {
            str[i] = parts[i + HardDiskInfo.SIZE].getPath();
        }
        return str;
    }

}
