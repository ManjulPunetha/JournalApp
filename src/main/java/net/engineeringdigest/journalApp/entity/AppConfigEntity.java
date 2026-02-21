package net.engineeringdigest.journalApp.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "APP_CONFIG")
public class AppConfigEntity {
    @Id
    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;
}
