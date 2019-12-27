package com.niek125.projectproducer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataHeader {
    private String text;
    private Type type;
    private String value;
}
