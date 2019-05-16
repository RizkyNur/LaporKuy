package com.example.tarkijowow.rpl;

import android.content.Context;

public class ReportActivityData {
    private Context context;
    private String report_spinner, report_detail;
    private static ReportActivityData ourInstance;

    public static ReportActivityData getInstance(Context c) {
        if(ourInstance == null){
            ourInstance = new ReportActivityData(c.getApplicationContext());
        }
        return ourInstance;
    }

    private ReportActivityData(Context c) {
        context = c;
    }

    public String getReport_spinner() {
        return report_spinner;
    }

    public void setReport_spinner(String report_spinner) {
        this.report_spinner = report_spinner;
    }

    public String getReport_detail() {
        return report_detail;
    }

    public void setReport_detail(String report_detail) {
        this.report_detail = report_detail;
    }
}
