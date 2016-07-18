package com.max.jumpingapp.types;

import com.max.jumpingapp.views.NoScoresException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by max on 4/11/2016.
 */
public class Score implements Comparable<Score>{
    private final int score;
    private final String name;

    public Score(int score){
        this(score, null);
    }
    public Score(String scoreAndName){
        String[] values = scoreAndName.split("\\|");
        this.name = values[0];
        this.score = Integer.valueOf(values[1]);
    }

    public Score(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public static ArrayList<Score> toArrayList(Set<String> stringSet) throws NoScoresException {
        if(null == stringSet){
            throw new NoScoresException();
        }
        Object[] objectArray = stringSet.toArray();
        ArrayList arrayList= new ArrayList<Score>();
        for(Object object : objectArray){
            arrayList.add(new Score(object.toString()));
        }
        return arrayList;
    }

    public Score update(Height height) {
        return new Score(height.maxOf(score));
    }

    @Override
    public String toString() {
        return "Score: " + score;
    }

    public String nameAndValue(){
        return this.name + " - " + this.score;
    }

    public boolean betterThan(int scoreNum) {
        return score > scoreNum;
    }

    public int toInt() {
        return score;
    }

    public String valueAsString() {
        return "" + score;
    }

    @Override
    public int compareTo(Score another) {
        if(this.toInt() > another.toInt()){
            return  -1;
        }
        if(this.toInt() < another.toInt()){
            return 1;
        }
        return 0;
    }

    public static Set<String> toSet(ArrayList<Score> scoreList){
        if(null == scoreList){
            return null;
        }
        Set<String> set = new TreeSet<>();
        for(int i=0; i<scoreList.size(); i++){
            set.add(scoreList.get(i).storeString());
        }
        return set;
    }

    private String storeString() {
        return this.name + "|"+this.score;
    }

    public static ArrayList firstTenOf(ArrayList<Score> scoreList) {
        for(int i=scoreList.size(); i>10; i--){
            scoreList.remove(i-1);
        }
        return scoreList;
    }

    public boolean isHighscore(ArrayList scoreList) {
        if(scoreList.size() <10){
            return true;
        }
        Score oldScore = getWeakestHighscore(scoreList);
        return this.betterThan(oldScore.score);
    }

    private Score getWeakestHighscore(ArrayList scoreList) {
        Collections.sort(scoreList);
        return (Score) scoreList.get(scoreList.size()-1);
    }

    public String getName() {
        return name;
    }

}
