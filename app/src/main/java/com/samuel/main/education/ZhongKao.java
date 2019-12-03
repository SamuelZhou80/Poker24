package com.samuel.main.education;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/**
 * �п��ɼ���
 * 
 * @author Administrator
 *
 */
public class ZhongKao extends DataSupport implements Serializable {

    /**    */
    private static final long serialVersionUID = 1L;
    /** ����ѧУ���� */
    private String schoolName = "";
    /** �п���� */
    private int year;
    /** �������� */
    private int totalNum = 0;
    /** һ�б���¼ȡ���� */
    private int yizhongbenbu = 0;
    /** ˫ʮ����¼ȡ���� */
    private int shuangshibenbu = 0;
    /** �����¼ȡ���� */
    private int waiguoyu = 0;
    /** һ˫��¼ȡ����(����û��ѧУ����ϸ, ֻ�кϼ�) */
    private int top3 = 0;
    /** һ�к���¼ȡ���� */
    private int yizhonghaicang = 0;
    /** �ո�¼ȡ���� */
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
