/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dsl.poiimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class UploadResult {
    
    private List result;
    private List result2;
    private Map failedMessage;

    public UploadResult(){ 
        this.result = new ArrayList<>();
        this.result2 = new ArrayList<>();
        this.failedMessage = new TreeMap<>();
    }

    /**
     * @return the result
     */
    public List getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List result) {
        this.result = result;
    }

    /**
     * @return the result2
     */
    public List getResult2() {
        return result2;
    }

    /**
     * @param result2 the result2 to set
     */
    public void setResult2(List result2) {
        this.result2 = result2;
    }

    /**
     * @return the failedMessage
     */
    public Map getFailedMessage() {

        return failedMessage;
    }

    /**
     * @param failedMessage the failedMessage to set
     */
    public void setFailedMessage(Map failedMessage) {
        this.failedMessage = failedMessage;
    }

}
