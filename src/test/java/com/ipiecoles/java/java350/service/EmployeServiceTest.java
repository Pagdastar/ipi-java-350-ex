package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this.getClass());
    }

    @Test
    void testEmbaucheEmployeTechnicienPleinTempsBts() throws EmployeException {
        //Given
        String nom = "Pavich'";
        String prenom = "Guillemette";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("T00001")).thenReturn(null);
        when(employeRepository.save(any())).thenAnswer(returnsFirstArg());

        //When
        Employe employe;
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository).save(employeArgumentCaptor.capture());
        employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
        Assertions.assertThat(employe.getPerformance()).isEqualTo(1);

        // Salaire de base * coefficient
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
    }

        @Test
        public void testEmbaucheEmployeManagerMiTempsMaster() throws EmployeException {
            //Given
            String nom = "Pavich'";
            String prenom = "Guillemette";
            Poste poste = Poste.MANAGER;
            NiveauEtude niveauEtude = NiveauEtude.MASTER;
            Double tempsPartiel = 0.5;
            when(employeRepository.findLastMatricule()).thenReturn("00345");
            when(employeRepository.findByMatricule("M00346")).thenReturn(null);
            when(employeRepository.save(any())).thenAnswer(returnsFirstArg());

            //When
            Employe employe;
            employe = employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

            //Then
            Assertions.assertThat(employe.getNom()).isEqualTo(nom);
            Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
            Assertions.assertThat(employe.getMatricule()).isEqualTo("M00346");
            Assertions.assertThat(employe.getPerformance()).isEqualTo(1);

            // 1521.22 * 1.4 * 0.5
            Assertions.assertThat(employe.getSalaire()).isEqualTo(1064.85);
            Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
    }
        @Test
        public void testEmbaucheSalarieLastMatricule99999() {
            //Given
            String nom = "Doe";
            String prenom = "John";
            Poste poste = Poste.MANAGER;
            NiveauEtude niveauEtude = NiveauEtude.MASTER;
            Double tempsPartiel = 0.5;
            when(employeRepository.findLastMatricule()).thenReturn("99999");
            //When
            try {
                employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
                Assertions.fail("Aurait du lancer une exception");
            }
            //Then
            catch (EmployeException e) {
                Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
            }
        }
        @Test
        public void testEmbaucheExistingEmploye() throws EmployeException {
            //Given
            String nom = "Doe";
            String prenom = "John";
            Poste poste = Poste.MANAGER;
            NiveauEtude niveauEtude = NiveauEtude.MASTER;
            Double tempsPartiel = 0.5;
            when(employeRepository.findLastMatricule()).thenReturn("00001");
            when(employeRepository.findByMatricule("M00002")).thenReturn(new Employe());

            //When
            try {
                employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
                Assertions.fail("Aurait du lancer une exception");
            }
            //Then
            catch (EntityExistsException e) {
                Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule M00002 existe déjà en BDD");
            }
        }
}