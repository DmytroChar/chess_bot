package com.procore.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
