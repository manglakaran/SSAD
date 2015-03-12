package com.example.akhil.loginparse;

/**
 * Created by Manu on 15-10-2014.
 */
    public class contact implements Comparable<contact>{
        String name = null;
        String number = null;
        boolean selected = false;

        public contact(String name, String number, boolean selected){
            super();
            this.name = name;
            this.number = number;
            this.selected = selected;
        }

        @Override
        public int compareTo(contact another) {
            return 0;
        }
}
