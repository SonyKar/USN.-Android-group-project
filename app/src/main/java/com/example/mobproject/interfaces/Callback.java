package com.example.mobproject.interfaces;

import java.util.ArrayList;

public abstract class Callback<T> {
    public abstract void OnFinish(ArrayList<T> arrayList);
}
