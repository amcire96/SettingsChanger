package me.amcire.settingschanger;

import java.util.Calendar;

/**
 * Created by Eric on 8/28/2015.
 */
    public class DaysUtil {
        public static int getValue(String day){
            switch (day){
                case "M":
                    return 1;
                case "Tu":
                    return 2;
                case "W":
                    return 3;
                case "Th":
                    return 4;
                case "F":
                    return 5;
                case "Sa":
                    return 6;
                case "Su":
                    return 0;
            }
            return -1;
        }

        public static int convertToCalDay(String day){
            switch (day){
                case "Su":
                    return Calendar.SUNDAY;
                case "M":
                    return Calendar.MONDAY;
                case "Tu":
                    return Calendar.TUESDAY;
                case "W":
                    return Calendar.WEDNESDAY;
                case "Th":
                    return Calendar.THURSDAY;
                case "F":
                    return Calendar.FRIDAY;
                case "Sa":
                    return Calendar.SATURDAY;

            }
            return -1;
        }

        public static int convertCalendarDay(int day){
            switch(day){
                case Calendar.SUNDAY:
                    return 0;
                case Calendar.MONDAY:
                    return 1;
                case Calendar.TUESDAY:
                    return 2;
                case Calendar.WEDNESDAY:
                    return 3;
                case Calendar.THURSDAY:
                    return 4;
                case Calendar.FRIDAY:
                    return 5;
                case Calendar.SATURDAY:
                    return 6;

            }
            return -1;
        }


        public static String getDate(int day){
            switch(day){
                case 0:
                    return "Su";
                case 1:
                    return "M";
                case 2:
                    return "Tu";
                case 3:
                    return "W";
                case 4:
                    return "Th";
                case 5:
                    return "F";
                case 6:
                    return "Sa";

            }
            return "";
        }
    }


