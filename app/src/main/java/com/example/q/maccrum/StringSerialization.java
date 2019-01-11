package com.example.q.maccrum;

import java.io.Serializable;

public class StringSerialization implements Serializable {
    public String text;

    public StringSerialization(String word){
        text = word;
    }
}
