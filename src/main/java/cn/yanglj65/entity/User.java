package cn.yanglj65.entity;

import javax.persistence.*;

@Table(name = "user_t")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "qq")
    private String QQ;
    @Column(name = "n_cookie")
    private String NCookie;
    @Column(name = "n_status")
    private String NStatus;
    @Column(name = "t_cookie")
    private String TCookie;
    @Column(name="t_status")
    private String TStatus;
    @Column(name="a_cookie")
    private String ACookie;
    @Column(name = "a_status")
    private String AStatus;

    public String getTCookie() {
        return TCookie;
    }

    public void setTCookie(String TCookie) {
        this.TCookie = TCookie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getNCookie() {
        return NCookie;
    }

    public void setNCookie(String NCookie) {
        this.NCookie = NCookie;
    }

    public String getNStatus() {
        return NStatus;
    }

    public void setNStatus(String NStatus) {
        this.NStatus = NStatus;
    }

    public String getTStatus() {
        return TStatus;
    }

    public void setTStatus(String TStatus) {
        this.TStatus = TStatus;
    }

    public String getACookie() {
        return ACookie;
    }

    public void setACookie(String ACookie) {
        this.ACookie = ACookie;
    }

    public String getAStatus() {
        return AStatus;
    }

    public void setAStatus(String AStatus) {
        this.AStatus = AStatus;
    }
}
