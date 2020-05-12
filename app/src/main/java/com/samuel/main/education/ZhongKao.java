package com.samuel.main.education;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/**
 * 中考成绩表单
 * 
 * @author Administrator
 *
 */
public class ZhongKao extends DataSupport implements Serializable {

    /**    */
    private static final long serialVersionUID = 1L;
    /** 初中学校名称 */
    private String schoolName = "";
    /** 中考年份 */
    private int year;
    /** 报考人数 */
    private int totalNum = 0;
    /** 一中本部录取人数 */
    private int yizhongbenbu = 0;
    /** 双十本部录取人数 */
    private int shuangshibenbu = 0;
    /** 外国语录取人数 */
    private int waiguoyu = 0;
    /** 一双外录取人数(可能没有学校的明细, 只有合计) */
    private int top3 = 0;
    /** 一中海沧录取人数 */
    private int yizhonghaicang = 0;
    /** 普高录取人数 */
    private int toHighSchool = 0;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getYizhongbenbu() {
        return yizhongbenbu;
    }

    public void setYizhongbenbu(int yizhongbenbu) {
        this.yizhongbenbu = yizhongbenbu;
    }

    public int getShuangshibenbu() {
        return shuangshibenbu;
    }

    public void setShuangshibenbu(int shuangshibenbu) {
        this.shuangshibenbu = shuangshibenbu;
    }

    public int getWaiguoyu() {
        return waiguoyu;
    }

    public void setWaiguoyu(int waiguoyu) {
        this.waiguoyu = waiguoyu;
    }

    public int getTop3() {
        return top3;
    }

    public void setTop3(int top3) {
        this.top3 = top3;
    }

    public int getYizhonghaicang() {
        return yizhonghaicang;
    }

    public void setYizhonghaicang(int yizhonghaicang) {
        this.yizhonghaicang = yizhonghaicang;
    }

    public int getToHighSchool() {
        return toHighSchool;
    }

    public void setToHighSchool(int toHighSchool) {
        this.toHighSchool = toHighSchool;
    }

}
