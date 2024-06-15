package ru.nightidk.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Pair<L,R> {
    private L key;
    private R value;
}