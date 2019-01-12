/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellbook;

import filewriter.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

/**
 *
 * @author rewil
 */
public class FXMLDocumentController implements Initializable {
    
    // <editor-fold>
    Writer ioSpells = new Writer("Spellbook/Spell List");
    Writer ioBooks = new Writer("Spellbook/Spell Books");
    
    @FXML ListView spellList;
    @FXML ListView spellBook;
    
    @FXML TextField nameField;
    @FXML TextField schoolField;
    @FXML TextField levelField;
    @FXML TextField ritualField;
    @FXML TextField castingTimeField;
    @FXML TextField rangeField;
    @FXML TextField componentsField;
    @FXML TextField durationField;
    @FXML TextField tagSearch;
    @FXML TextField levelSearch;
    @FXML TextField nameSearch;
    
    @FXML TextArea descriptionArea;
    @FXML TextArea effectArea;
    @FXML TextArea upcastArea;
    @FXML TextArea tagsArea;
    
    ArrayList<TextInputControl> fields = new ArrayList<>();
    
    @FXML ComboBox bookPicker;
    // </editor-fold>
    
  //~~~~~~~~General~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void saveSpell() {
        
        int level = 0;
        try {
            level = Integer.parseInt(levelField.getText());
        } catch(Exception e) {}
        boolean ritual = Boolean.getBoolean(ritualField.getText());
        ArrayList<String> tags = new ArrayList<>();
        
        char[] tagChars = tagsArea.getText().toCharArray();
        String temp = "";
        for(int i = 0; i < tagChars.length; ++i) {
            if(tagChars[i] != ',') {
                temp += tagChars[i];
            } else {
                tags.add(temp.toLowerCase());
                temp = "";
            }
        }
        tags.add(temp);
        
        Spell newSpell = new Spell(nameField.getText(), schoolField.getText(), level,ritual, 
                castingTimeField.getText(), rangeField.getText(), componentsField.getText(), 
                durationField.getText(), descriptionArea.getText(), effectArea.getText(), upcastArea.getText(), tags.toArray(new String[0]));
        
        ioSpells.writeObject(newSpell, nameField.getText());
        clearSpell();
        resetSpellList();
    }
    
    public void deleteSpell() {
        ioSpells.deleteFile(nameField.getText());
        for(TextInputControl t : fields) {
            t.setText("");
        }
        resetSpellList();
        resetSpellBook();
    }
    
    public void viewSpell(Spell s) {
        clearSpell();
        
        nameField.setText(s.getName());
        schoolField.setText(s.getSchool());
        levelField.setText(s.getLevel()+"");
        ritualField.setText(s.getRitual()+"");
        castingTimeField.setText(s.getCastingTime());
        rangeField.setText(s.getRange());
        componentsField.setText(s.getComponents());
        durationField.setText(s.getDuration());
        descriptionArea.setText(s.getDescription());
        effectArea.setText(s.getEffect());
        upcastArea.setText(s.getUpcast());
        
        String temp = "";
        for(String t : s.getTags()) {
            temp += t + ",";
        }
        temp = temp.substring(0, temp.length() - 1);
        tagsArea.setText(temp);
    }
    
    public void addToBook() {
        bookSpells.add(nameField.getText());
        resetSpellBook();
        saveSpell();
        resetSpellList();
    }
    
    public void removeFromBook() {
        bookSpells.remove(nameField.getText());
        resetSpellBook();
        clearSpell();
    }
    
    public void clearSpell() {
        for(TextInputControl t : fields) {
            t.setText("");
        }
    }
    
  //~~~~~~~~Spell List~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void resetSpellList() {
        ObservableList<String> noList = FXCollections.observableArrayList(new ArrayList<>());
        spellList.setItems(noList);
        ObservableList<String> spellsList = FXCollections.observableArrayList(ioSpells.listFiles());
        spellList.setItems(spellsList);
    }
    
    public void resetSpellList(ArrayList<String> list) {
        ObservableList<String> noList = FXCollections.observableArrayList(new ArrayList<>());
        spellList.setItems(noList);
        ObservableList<String> spellsList = FXCollections.observableArrayList(list);
        spellList.setItems(spellsList);
    }
    
    public void selectListSpell() {
        if(spellList.getSelectionModel().getSelectedItem() != null) {
            Spell selected = (Spell) ioSpells.readObject((String) spellList.getSelectionModel().getSelectedItem());
            viewSpell(selected);
            spellList.getSelectionModel().clearSelection();
        }
    }
    
    private ArrayList<String> searchTags = new ArrayList<>();
    private Integer searchLevel = null;
    private String searchName = null;
    
    public void searchTag() {
        if(!tagSearch.getText().equals("")) {
            char[] tagChars = tagSearch.getText().toCharArray();
            String temp = "";
            for(int i = 0; i < tagChars.length; ++i) {
                if(tagChars[i] != ',') {
                    temp += tagChars[i];
                } else {
                    searchTags.add(temp.toLowerCase());
                    temp = "";
                }
            }
            searchTags.add(temp);
        } else {
            searchTags = new ArrayList<>();
        }
        narrowList();
    }
    
    public void limitLevel() {
        if(!levelSearch.getText().equals("")) {
            try {
                searchLevel = Integer.parseInt(levelSearch.getText());
            } catch(Exception e) {}
        } else {
            searchLevel = null;
        }
        narrowList();
    }
    
    public void searchName() {
        if(!nameSearch.getText().equals("")) {
            searchName = nameSearch.getText().toLowerCase();
        } else {
            searchName = null;
        }
        narrowList();
    }
    
    public void clearLimits() {
        searchTags = new ArrayList<>();
        searchLevel = null;
        searchName = null;
        tagSearch.setText((""));
        levelSearch.setText("");
        nameSearch.setText("");
        resetSpellList();
    }
    
    public void narrowList() {
        ArrayList<String> spells = new ArrayList<>();
        if(searchLevel != null) {
            for(String s : ioSpells.listFiles()) {
                Spell spell = (Spell)ioSpells.readObject(s);
                if(spell.getLevel() == searchLevel) {
                    spells.add(s);
                }
            }
            if(searchTags.size() > 0) {
                for(int i = spells.size() - 1; i >= 0; --i) {
                    String s = spells.get(i);
                    Spell spell = (Spell)ioSpells.readObject(s);
                    boolean tag = true;
                    for(String t : searchTags) {
                        if(!spell.getTags().contains(t)) {
                            tag = false;
                        }
                    }
                    if(!tag) {
                        spells.remove(s);
                    }
                }
            }
            if(searchName != null) {
                for(int i = spells.size() - 1; i >= 0; --i) {
                    if(!spells.get(i).toLowerCase().contains(searchName)) {
                        spells.remove(i);
                    }
                }
            }
            resetSpellList(spells);
        } else if(searchTags.size() > 0) {
            for(String s : ioSpells.listFiles()) {
                Spell spell = (Spell)ioSpells.readObject(s);
                boolean tag = true;
                for(String t : searchTags) {
                    if(!spell.getTags().contains(t)) {
                        tag = false;
                    }
                }
                if(tag) {
                    spells.add(s);
                }
            }
            if(searchName != null) {
                for(int i = spells.size() - 1; i >= 0; --i) {
                    if(!spells.get(i).toLowerCase().contains(searchName)) {
                        spells.remove(i);
                    }
                }
            }
            resetSpellList(spells);
        } else if(searchName != null) {
            for(String s : ioSpells.listFiles()) {
                if(s.toLowerCase().contains(searchName)) {
                    spells.add(s);
                }
            }
            resetSpellList(spells);
        } else {
            resetSpellList();
        }
    }
    
  //~~~~~~~~Spell Book~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    private ArrayList<String> bookSpells = new ArrayList<>();
    
    public void resetSpellBook() {
        ObservableList<String> noList = FXCollections.observableArrayList(new ArrayList<>());
        spellBook.setItems(noList);
        sortBook();
        ObservableList<String> spellsList = FXCollections.observableArrayList(bookSpells);
        spellBook.setItems(spellsList);
    }
    
    public void sortBook() {
        if(bookSpells.size() > 0) {
            for(int i = bookSpells.size() - 1; i >= 0; --i) {
                if(bookSpells.get(i).startsWith("--")) {
                    bookSpells.remove(i);
                }
            }
            ArrayList<String> sortedSpells = new ArrayList<>();
            ArrayList<String> temp = new ArrayList<>();
            int max = 0;

            //Set Highest Level
            for(String s : bookSpells) {
                if(ioSpells.listFiles().contains(s)) {
                    Spell spell = (Spell) ioSpells.readObject(s);
                    if(spell.getLevel() > max) {
                        max = spell.getLevel();
                    }
                }
            }
            max++;

            //Identify Unlisted Spells
            for(String s : bookSpells) {
                if(!ioSpells.listFiles().contains(s)) {
                    temp.add(s);
                }
            }
            if(temp.size() > 0) {
                sortedSpells.add("-- Spells not in Spell List");
                sortedSpells.addAll(temp);
            }
            temp.clear();

            //Group rest by level
            for(int i = 0; i < max; ++i) {
                for(String s : bookSpells) {
                    if(ioSpells.listFiles().contains(s)) {
                        Spell spell = (Spell) ioSpells.readObject(s);
                        if(spell.getLevel() == i) {
                            temp.add(s);
                        }
                    }
                }
                if(temp.size() > 0) {
                    sortedSpells.add("-- Level " + i + " spells");
                    sortedSpells.addAll(temp);
                    temp.clear();
                }
            }

            //Reassign sortedSpells back into bookSpells
            bookSpells = sortedSpells;
        }
    }
    
    public void selectBookSpell() {
        String spell = (String) spellBook.getSelectionModel().getSelectedItem();
        if(spell != null && !spell.startsWith("--")) {
            if(ioSpells.listFiles().contains(spell)) {
                Spell selected = (Spell) ioSpells.readObject(spell);
                viewSpell(selected);
                spellBook.getSelectionModel().clearSelection();
            } else {
                nameField.setText(spell);
                descriptionArea.setText("Not in Spell List");
            }
        }
    }
    
    public void loadBook() {
        if(ioBooks.listFiles().contains((String)bookPicker.getValue())){
            Book book = (Book) ioBooks.readObject((String) bookPicker.getValue());
            bookSpells = book.listSpells();
            resetSpellBook();
        }
    }
    
    public void saveBook() {
        ioBooks.writeObject(new Book(bookSpells), (String)bookPicker.getValue());
    }
    
    public void deleteBook() {
        bookSpells = new ArrayList<>();
        ioBooks.deleteFile((String) bookPicker.getValue());
        bookPicker.setValue("");
        resetSpellBook();
    }
    
    public void updateBookList() {
        ArrayList<String> files = ioBooks.listFiles();
        ObservableList<String> options = FXCollections.observableArrayList(files);
        bookPicker.setItems(options);
    }
    
    public void clearBook() {
        bookSpells = new ArrayList<>();
        resetSpellBook();
    }
    
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //<editor-fold>
        fields.add(nameField);
        fields.add(schoolField);
        fields.add(levelField);
        fields.add(ritualField);
        fields.add(castingTimeField);
        fields.add(rangeField);
        fields.add(componentsField);
        fields.add(durationField);
        fields.add(descriptionArea);
        fields.add(effectArea);
        fields.add(upcastArea);
        fields.add(tagsArea);
        //</editor-fold>
        
        //<editor-fold>
        levelField.setTooltip(new Tooltip("Defaults to 0 \nUse 0 for Cantrips"));
        ritualField.setTooltip(new Tooltip("Put only \"True\" or \"False\"\nCapitalization is not important\nDefaults to False"));
        effectArea.setTooltip(new Tooltip("tl;dr version of Description"));
        upcastArea.setTooltip(new Tooltip("Describe upcast possibilities here"));
        tagsArea.setTooltip(new Tooltip("Add each tag separated by a comma. Do not include a space \nEx. \"tag1,tag2\"\nAll tags will be saved lowercase"));
        tagSearch.setTooltip(new Tooltip("Enter tags separated by a comma. Do not include a space \nEx. \"tag1,tag2\"\nClear field and hit Search By Tag to clear search"));
        levelSearch.setTooltip(new Tooltip("Enter level number by itself, do not search multiples.\nClear field and hit Limit By Level to clear limit"));
        //</editor-fold>
        
        resetSpellList();
    }    
    
}
