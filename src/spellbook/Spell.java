/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellbook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author rewil
 */
public class Spell implements Serializable{
    
    private String name = "";
    private String school = "";
    private int level = 0;
    private boolean ritual = false;
    private String castingTime = "";
    private String range = "";
    private String components = "";
    private String duration = "";
    private String description = "";
    private String effect = "";
    private String upcast = "";
    private ArrayList<String> tags = new ArrayList<>();
    
    public Spell(String name, String school, int level, boolean ritual, String castingTime, String range, String components, String duration, String description, String effect, String upcast, String[] tags) {
        this.name = name;
        this.school = school;
        this.level = level;
        this.ritual = ritual;
        this.castingTime = castingTime;
        this.range = range;
        this.components = components;
        this.duration = duration;
        this.description = description;
        this.effect = effect;
        this.upcast = upcast;
        this.tags.addAll(Arrays.asList(tags));
    }

    public void addTag(String tag) {
        tags.add(tag);
    }
    
    public ArrayList<String> getTags() {
        return tags;
    }
    
    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public int getLevel() {
        return level;
    }

    public boolean getRitual() {
        return ritual;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public String getRange() {
        return range;
    }

    public String getComponents() {
        return components;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getEffect() {
        return effect;
    }

    public String getUpcast() {
        return upcast;
    }
    
    
    
}
