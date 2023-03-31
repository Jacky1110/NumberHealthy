package com.jotangi.NumberHealthy.api;

public class ApiConstant {

    // 正式機
    public static final String WATCH_API = "https://clinic.healthme.com.tw/health/";
    public static final String BOOK_API = "https://clinic.healthme.com.tw/medicalec/api/";
    public static final String MUG_SHOT_URL = "https://clinic.healthme.com.tw/medicalec/";
    public static final String IMAGE_URL = "https://clinic.healthme.com.tw/medicalhealth/";
    public static final String WEB_URL = "https://clinic.healthme.com.tw/medicalhealth/web/";
    public static final String PAY_URL = "https://clinic.healthme.com.tw/clinic/payindex.php?";

    public static final String HHQ_URL = "https://clinic.healthme.com.tw/medicalhealth/web/health_heart_query.php?id=";
    public static final String HBQ_URL = "https://clinic.healthme.com.tw/medicalhealth/web/health_bloodp_query.php?id=";
    public static final String HOQ_URL = "https://clinic.healthme.com.tw/medicalhealth/web/health_oxygen_query.php?id=";
    public static final String HRQ_URL = "https://clinic.healthme.com.tw/medicalhealth/web/health_respiratoryrate_query.php?id=";
    public static final String HTQ_URL = "https://clinic.healthme.com.tw/medicalhealth/web/health_temperature_query.php?id=";
    public static final String HSlQ_URL = "https://clinic.healthme.com.tw/medicalhealth/web/health_sleep_query.php?id=";
    public static final String HStQ_URL = "https://clinic.healthme.com.tw/medicalhealth/web/health_step_query.php?id=";



    // 測試機
//    public static final String WATCH_API = "https://tripspottest.jotangi.net/health/";
//    public static final String BOOK_API = "https://tripspottest.jotangi.net/medicalec/api/";
//    public static final String MUG_SHOT_URL = "https://tripspottest.jotangi.net/medicalec/";
//    public static final String IMAGE_URL = "https://tripspottest.jotangi.net/medicalhealth/";
//    public static final String WEB_URL = "https://medicalec.jotangi.net/medicalhealth/web/";
//    public static final String PAY_URL = "https://demo.jotangi.net/clinic/payindex.php?";

    public static final String ID_GO_BODY = "health_body_data.php?id=";
    public static final String ID_VESSEL_STIFFNESS = "health_vessel.php?id=";
    public static final String ID_EECP = "health_eecp.php?id=";

    // 健康快篩
    public static final String QUICK_SIEVE_URL = "https://scanreport.lohasgen.com/Viewer/ThirdParty/ReportViewer?id=";
    // 足壓
    public static final String ASIAFOOT_URL = "http://asiafoot.com/Spams/servlet/";
    public static final String ASIAFOOT_PDF_URL = "http://asiafoot.com/Spams/AsiaFootDatabase/FootPressure_PDF/";
    public static final String MAIN_FOOTSIZE = "footsize-";
    public static final String ITEM_P = "p-";
    public static final String ITEM_DE = "de-";
    public static final String ITEM_KI = "ki-";
    public static final String ITEM_KP = "kp-";
    public static final String ITEM_WP = "wp-";
    public static final String ITEM_TINEA = "tinea-";
    // 氣象
    public static final String WEATHER_URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-091?Authorization=CWB-493B9F0B-D2C9-48F8-9CC9-799D13228157&locationName=";
    public static final String ITEM_TIMEFROM = "&elementName=MinT,MaxT,T,Wx&timeFrom=";
    public static final String ITEM_TIMETO = "&timeTo=";


    public static final String STORE_URL = "https://play.google.com/store/apps/details?id=com.jotangi.NumberHealthy";


    // 足壓量測
    public static final String get_data_by_mobile = "getDataByMobile.php";


    // ========================== TASK ==========================
    public static final String TASK_fpm = "TASK_fpm";
    public static final String TASK_weather = "TASK_weather";
}
