package com.example.adventure_book.entity;

import com.example.adventure_book.type.ConsequenceType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Consequence {
    @Enumerated(EnumType.STRING)
    private ConsequenceType type;

    private Integer amount;

    private String text;
}
