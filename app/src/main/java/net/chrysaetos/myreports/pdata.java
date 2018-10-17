package net.chrysaetos.myreports;

public  class pdata {
    private String personalInfo;
    private  String personalValue;



    public pdata(String personalInfo, String personalValue) {

        this.personalInfo = personalInfo;
        this.personalValue = personalValue;
    }

//    public static void  setName(String name)
//    {
//        name = name;
//    }
    public String getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    public String getPersonalValue() {
        return personalValue;
    }

    public void setPersonalValue(String personalValue) {
        this.personalValue = personalValue;
    }


}
