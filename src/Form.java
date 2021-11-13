import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * @author LHZ
 * @Date 2021/11/8 9:49
 */
public class Form {
    private JFrame mainFrame;
    //包裹JTable的滚动面板
    private JScrollPane jsp;
    //使用JTable显示信息
    private JTable jt;
    //表头信息
    private Vector<String> columnNames;

    public Form() {
        prepareGUI();
    }

    public JTable getJt() {
        return jt;
    }

    public void setJt(JTable jt) {
        this.jt = jt;
    }

    public JScrollPane getJsp() {
        return jsp;
    }



    private void prepareGUI() {
        mainFrame = new JFrame("Java SWING Examples");
        //设置窗口大小
        mainFrame.setSize(800, 400);
        //位于屏幕正中央
        mainFrame.setLocationRelativeTo(null);
        //退出监听
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        //网格布局
        mainFrame.setLayout(new GridLayout(3, 1));
        //创建面板
        JPanel jPanel=new JPanel();

        Vector<Vector<Object>> rowData = new Vector<>();
        //设置表头信息
        columnNames = this.getColumnNames();
        DefaultTableModel dtm = new DefaultTableModel(rowData,columnNames);
        jt = new JTable(dtm) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jsp = new JScrollPane(jt);
        mainFrame.add(jsp);
        //可视
        mainFrame.setVisible(true);
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

    public static void main(String[] args) {
        Form form = new Form();
        //先获取系统一开始的磁盘数
        int size = HardDiskInfo.getNum();
        System.out.println("系统一开始的磁盘数：" + size);
        HardDiskInfo.SIZE = size;
        //初始化List
        for (int i = 0; i < HardDiskInfo.NUM; i++) {
            DetailInfo detailInfo = new DetailInfo();
            HardDiskInfo.list.add(detailInfo);
        }

        HardDiskInfo hardDiskInfo = new HardDiskInfo();
        //充当线程A的角色
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    hardDiskInfo.checkU(form);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
