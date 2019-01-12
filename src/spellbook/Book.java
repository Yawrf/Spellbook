/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellbook;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author rewil
 */
public class Book implements Serializable{
    
    private ArrayList<String> spells = new ArrayList<>();
    
    public Book(){}
    
    public Book(ArrayList<String> spells) {
        this.spells = spells;
    }
    
    public void addSpell(String s) {
        spells.add(s);
    }
    
    public void removeSpell(String s) {
        spells.remove(s);
    }
    
    public ArrayList<String> listSpells() {
        return spells;
    }
    
}
