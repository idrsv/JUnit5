package com.idrsv.junit.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class User {
     int ID;
     String name;
     String password;
}
