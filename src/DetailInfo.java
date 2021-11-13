/**
 * @author LHZ
 * @Date 2021/11/9 10:47
 */
public class DetailInfo {

    /**
     * u盘盘符
     */
    private String disk;

    /**
     * U盘SN
     */
    private String sn;


    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "DetailInfo{" +
                "disk='" + disk + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }
}
