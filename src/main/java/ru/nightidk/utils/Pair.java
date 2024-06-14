package ru.nightidk.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pair<L,R> {
    private L key;
    private R value;
    public Pair(L key, R value){
        this.key = key;
        this.value = value;
    }

}