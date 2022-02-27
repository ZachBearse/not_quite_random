package com.zachbearse.notquiterandom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FightersViewModel extends AndroidViewModel {

    private final List<Fighter> allFighters = new ArrayList<>();
    private final Map<String, List<Fighter>> randomGroups = new HashMap<>();

    public FightersViewModel(@NonNull Application application) {
        super(application);
        allFighters.addAll(JsonParser.getFighterSet(application));
    }

    List<Fighter> getAllFighters(){
        return allFighters;
    }

    Set<String> getRandomGroups() {
        return randomGroups.keySet();
    }

    List<Fighter> getGroup(String groupName) {
        return randomGroups.get(groupName);
    }

    void addGroup(String title, List<Fighter> group){
        randomGroups.put(title, group);
    }
}
