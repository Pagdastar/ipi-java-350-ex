package com.ipiecoles.java.java350;

import com.ipiecoles.java.java350.model.Employe;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class EmployeTest {
    @Test
    public void getNombreAnneeAncienneteNow(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnee).isEqualTo(0);
    }

    @Test
    public void getNombreAnneeAncienneteNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnee).isEqualTo(0);
    }

    @Test
    public void getNombreAnneeNMoinsDeux(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(2));

        //When
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnee).isEqualTo(2);
    }

    @Test
    public void getNombreAnneeNPlusDeux(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));

        //When
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnnee).isEqualTo(0);
    }


}