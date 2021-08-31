package com.bikash.filetransferapp.model;

public interface Comparable
{
    boolean comparisonSupported();

    String getComparableName();

    long getComparableDate();

    long getComparableSize();
}
