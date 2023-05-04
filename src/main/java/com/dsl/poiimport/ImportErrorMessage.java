/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dsl.poiimport;

public class ImportErrorMessage {

    private Integer no;
    private String msg;

    public ImportErrorMessage() {
        this.no = 0;
        this.msg = "";
    }

    public ImportErrorMessage(Integer no, String msg) {
        this.no = no;
        this.msg = msg;
    }

    /**
     * @return the no
     */
    public Integer getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(Integer no) {
        this.no = no;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
