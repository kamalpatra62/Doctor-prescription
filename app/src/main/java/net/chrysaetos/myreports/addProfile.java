package net.chrysaetos.myreports;



public class addProfile {

    public static class users {

        public String name;
        public String phnumber;
        public String email;
        public String gender;
        public String dob;
        public String booldGroup;
        public String weight;
        public String imageGo;
        private String usrKey;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public  String test;

        public users() {
        }

        public users(String name, String phnumber, String email, String gender, String dob, String booldGroup, String weight,String imageGo) {
            this.name = name;
            this.phnumber = phnumber;
            this.email = email;
            this.gender = gender;
            this.dob = dob;
            this.booldGroup = booldGroup;
            this.weight = weight;
            this.imageGo = imageGo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhnumber() {
            return phnumber;
        }

        public void setPhnumber(String phnumber) {
            this.phnumber = phnumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getBooldGroup() {
            return booldGroup;
        }

        public void setBooldGroup(String booldGroup) {
            this.booldGroup = booldGroup;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getImageGo() {
            return imageGo;
        }

        public void setImageGo(String imageGo) {
            this.imageGo = imageGo;
        }

        public String getUsrKey() {
            return usrKey;
        }

        public void setUsrKey(String usrKey) {
            this.usrKey = usrKey;
        }
    }


}