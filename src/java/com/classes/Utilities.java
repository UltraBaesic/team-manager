/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.classes;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Stephen
 */
public class Utilities {

    public Utilities() {

    }

    public static LinkedHashMap<String, String> sortHashMapStringStringByValues(HashMap<String, String> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, String> sortedMap
                = new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next().trim();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next().trim();
                String comp1 = passedMap.get(key);
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    public static LinkedHashMap<Integer, String> SortHashMapIntStringByValues(HashMap<Integer, String> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, String> sortedMap = new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next().trim();
            Iterator<Integer> keyIt = mapKeys.iterator();
            while (keyIt.hasNext()) {
                int key = keyIt.next();
                String comp1 = passedMap.get(key);
                String comp2 = val;
                if (comp1.trim().equals(comp2.trim())) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    public static ArrayList<Integer> SortHashMapIntStringReturnArrayListInt(HashMap<Integer, String> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        ArrayList<Integer> sortedList = new ArrayList<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next().trim();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                int key = keyIt.next();
                String comp1 = passedMap.get(key);
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedList.add(key);
                    break;
                }
            }
        }
        return sortedList;
    }

    public static LinkedHashMap<String, Object> sortHashMapByStringKeys(HashMap<String, Object> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        Collections.sort(mapKeys);

        LinkedHashMap<String, Object> sortedMap
                = new LinkedHashMap<>();

        Iterator<String> keyIt = mapKeys.iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next().trim();
            Object val = passedMap.get(key);
            sortedMap.put(key, val);
        }
        return sortedMap;
    }

    public static LinkedHashMap<String, Object> sortHashMapByDateKeysReadDate(HashMap<String, Object> passedMap, String order) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        if(order.equalsIgnoreCase("asc")) {
            Collections.sort(mapKeys);
        } else if(order.equalsIgnoreCase("desc")){            
            Collections.sort(mapKeys, Collections.reverseOrder());
        }

        LinkedHashMap<String, Object> sortedMap
                = new LinkedHashMap<>();

        Iterator<String> keyIt = mapKeys.iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next().trim();
            Object val = passedMap.get(key);
            String sortedDate = DateUtil.readDate(key);
            sortedMap.put(sortedDate, val);
        }
        return sortedMap;
    }

    public static LinkedHashMap<Integer, Object> sortHashMapByIntegerKeys(HashMap<Integer, Object> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, Object> sortedMap
                = new LinkedHashMap<>();

        Iterator<Integer> keyIt = mapKeys.iterator();
        while (keyIt.hasNext()) {
            int key = keyIt.next();
            Object val = passedMap.get(key);
            sortedMap.put(key, val);
        }
        return sortedMap;
    }

    public static java.sql.Date CurrentDate() throws ParseException {
        Calendar currentdate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        String Placeholder = formatter.format(currentdate.getTime());
        java.util.Date datenow = formatter.parse(Placeholder);
        java.sql.Date CurrentDate = new Date(datenow.getTime());
        return CurrentDate;
    }

    public static java.sql.Time CurrentTime() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Time(today.getTime());
    }

    public static java.sql.Date getSqlDateFromString(String StringDate) {
        Date date;
        try {
            date = Date.valueOf(StringDate);
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    public static int RandomNumber(int max, int min) {
        Random rand = new Random();
        int itID = rand.nextInt((max - min) + 1) + min;
        return itID;
    }

    public static String GenerateAlphaNumericCode(int LengthOfCode) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < LengthOfCode; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }

    public static ArrayList<Integer> removeDuplicatesIntegerArrayList(ArrayList<Integer> arraylist) {
        ArrayList<Integer> result = new ArrayList<Integer>(new LinkedHashSet<Integer>(arraylist));
        return result;
    }

    public static ArrayList<String> removeDuplicatesStringArrayList(ArrayList<String> arraylist) {
        ArrayList<String> result = new ArrayList<String>(new LinkedHashSet<String>(arraylist));
        return result;
    }

    public static String getTextBeforeCharacter(String text, String character) {
        String res = text.substring(0, text.indexOf(character));
        return res;
    }

    public static String getTextAfterCharacter(String text, String character) {
        String res = text.substring(text.indexOf(character) + 1, text.length());
        return res;
    }
}
