package com.zachbearse.notquiterandom;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FightersViewModel extends AndroidViewModel {

    private final String GROUP_NAMES_KEY = "com.zachbearse.notquiterandom.groupnameskey";
    private final List<Fighter> allFighters = new ArrayList<>();
    private final Map<String, List<Fighter>> randomGroups = new HashMap<>();
    private SharedPreferences sharedPreferences;

    public FightersViewModel(@NonNull Application application) {
        super(application);
        allFighters.addAll(JsonParser.getFighterSet(application));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        Set<String> groupNames = sharedPreferences.getStringSet(GROUP_NAMES_KEY, new HashSet<>());
        if (!groupNames.isEmpty()){
            for(String groupName : groupNames){
                List<String> fighterNumbers = new ArrayList<>(sharedPreferences.getStringSet(groupName, new HashSet<>()));
                List<Fighter> fightersInGroup = new ArrayList<>();
                for(String number : fighterNumbers){
                    for(Fighter fighter : allFighters){
                        if (fighter.getNumber().equals(number)){
                            fightersInGroup.add(fighter);
                            break;
                        }
                    }
                }
                fightersInGroup.sort(new Comparator<Fighter>() {
                    @Override
                    public int compare(Fighter fighter1, Fighter fighter2) {
                        return fighter1.getNumber().compareTo(fighter2.getNumber());
                    }
                });
                randomGroups.put(groupName, fightersInGroup);
            }
        }
    }

    List<Fighter> getAllFighters() {
        return allFighters;
    }

    Set<String> getRandomGroups() {
        return randomGroups.keySet();
    }

    List<Fighter> getGroup(String groupName) {
        return randomGroups.get(groupName);
    }

    void addGroup(String title, List<Fighter> group) {
        randomGroups.put(title, group);
    }

    void saveGroups() {
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        Set<String> groupNames = randomGroups.keySet();
        preferencesEditor.putStringSet(GROUP_NAMES_KEY, groupNames);
        for(String groupName : groupNames){
            List<Fighter> fightersInGroup = randomGroups.get(groupName);
            Set<String> fighterNumbers = new HashSet<>();
            if (fightersInGroup != null) {
                for(Fighter fighter: fightersInGroup){
                    fighterNumbers.add(fighter.getNumber());
                }
            }
            preferencesEditor.putStringSet(groupName, fighterNumbers);
        }
        preferencesEditor.apply();
    }

    void removeGroup(String groupName) {
        randomGroups.remove(groupName);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.remove(groupName);
        preferencesEditor.apply();
    }
}
