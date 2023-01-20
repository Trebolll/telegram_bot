package ru.nsk.tgbot.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tg_data")
public class User {

    @Id
    private long id; //BigInt
    private String name; //Text
    private int msg_numb; //Integer

}
