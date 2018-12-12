package com.samuel.main.education;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/**
 * 定向生分数
 * 
 * @author Administrator
 *
 */
public class DingXiang extends DataSupport implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** 初中学校名称 */
    private String schoolName = "";
    /** 录取学校名称 */
    private String recruitSchoolName = "";
    /** 分配名额 */
    private int allocationNum = 0;
    /** 录取名额 */
    private int recruitNum = 0;
    /** 最低分 */
    private int minScore = 0;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getRecruitSchoolName() {
        return recruitSchoolName;
    }

    public void setRecruitSchoolName(String recruitSchoolName) {
        this.recruitSchoolName = recruitSchoolName;
    }

    public int getAllocationNum() {
        return allocationNum;
    }

    public void setAllocationNum(int allocationNum) {
        this.allocationNum = allocationNum;
    }

    public int getRecruitNum() {
        return recruitNum;
    }

    public void setRecruitNum(int recruitNum) {
        this.recruitNum = recruitNum;
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }
}
