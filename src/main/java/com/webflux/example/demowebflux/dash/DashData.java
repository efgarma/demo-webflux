package com.webflux.example.demowebflux.dash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashData {

    private String id;
    private double value;
    private Date date;
}
