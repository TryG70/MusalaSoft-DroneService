package com.musalasoftdroneservice.serviceImpl;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.LoadedDroneDetails;
import com.musalasoftdroneservice.dto.MedicationDto;
import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.entity.Medication;
import com.musalasoftdroneservice.enums.DroneModel;
import com.musalasoftdroneservice.enums.DroneState;
import com.musalasoftdroneservice.exception.DroneAlreadyRegisteredException;
import com.musalasoftdroneservice.exception.DroneNotFoundException;
import com.musalasoftdroneservice.exception.MedicationNotFoundException;
import com.musalasoftdroneservice.mapper.DroneMapper;
import com.musalasoftdroneservice.mapper.MedicationMapper;
import com.musalasoftdroneservice.repository.DroneRepository;
import com.musalasoftdroneservice.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DroneServiceImplTest {

    @Mock
    DroneRepository droneRepository;


    @Mock
    MedicationRepository medicationRepository;


    @InjectMocks
    @Spy
    DroneServiceImpl droneServiceImpl;


    @Mock
    DroneMapper droneMapper;


    @Mock
    MedicationMapper medicationMapper;

    private Drone drone;

    private Drone savedDrone;

    private Medication medication;

    private Medication savedMedication;


    @BeforeEach
    void setUp() {

        LocalDateTime time = LocalDateTime.of(2022, Month.DECEMBER, 2, 7, 0, 0, 0);

        drone = Drone.builder().model(DroneModel.Lightweight).droneState(DroneState.IDLE).weightLimit(10.0d).serialNumber("TR676").batteryCapacity(69).medications(new ArrayList<>()).build();

        savedDrone = new Drone();
        savedDrone.setId(123L);
        savedDrone.setCreatedAt(time);
        savedDrone.setUpdatedAt(time);
        savedDrone.setSerialNumber("TR676");
        savedDrone.setModel(DroneModel.Lightweight);
        savedDrone.setBatteryCapacity(69);
        savedDrone.setWeightLimit(10.0d);
        savedDrone.setDroneState(DroneState.IDLE);
        savedDrone.setMedications(new ArrayList<>());



        medication = Medication.builder().code("GHGFJ78").weight(0.1d).name("Doxycycline").image("droneLN.jpeg").build();

        savedMedication = new Medication();
        savedMedication.setId(45L);
        savedMedication.setCreatedAt(time);
        savedMedication.setUpdatedAt(time);
        savedMedication.setName("Doxycycline");
        savedMedication.setCode("GHGFJ78");
        savedMedication.setWeight(0.1d);
        savedMedication.setImage("droneLN.jpeg");

        when(droneRepository.save(drone)).thenReturn(savedDrone);
        when(medicationRepository.save(medication)).thenReturn(savedMedication);
        when(medicationRepository.findByCode(medication.getCode())).thenReturn(Optional.ofNullable(savedMedication));

    }


    @Test
    void registerDrone() {

        DroneDto droneDto = DroneDto.builder()
                .model("Lightweight")
                .droneState(DroneState.IDLE)
                .weightLimit(10.0d)
                .serialNumber("TR676")
                .batteryCapacity(69)
                .build();


        when(droneRepository.findBySerialNumber(droneDto.getSerialNumber())).thenReturn(Optional.empty());
        when(droneMapper.droneToDroneDtoMapper(savedDrone)).thenReturn(droneDto);


        var actual = droneServiceImpl.registerDrone(droneDto);


        verify(droneRepository, times(1)).findBySerialNumber(droneDto.getSerialNumber());
        verify(droneRepository, times(1)).save(drone);

        assertEquals(savedDrone.getModel(), DroneModel.valueOf(actual.getDto().getModel()));
        assertEquals(savedDrone.getDroneState(), actual.getDto().getDroneState());
        assertEquals(savedDrone.getWeightLimit(), actual.getDto().getWeightLimit());
        assertEquals(savedDrone.getSerialNumber(), actual.getDto().getSerialNumber());
        assertEquals(savedDrone.getBatteryCapacity(), actual.getDto().getBatteryCapacity());

    }


    @Test
    void failedRegisterDrone() {

        DroneDto droneDto = DroneDto.builder()
                .model("Lightweight")
                .droneState(DroneState.IDLE)
                .weightLimit(10.0d)
                .serialNumber("TR676")
                .batteryCapacity(69)
                .build();

        when(droneRepository.findBySerialNumber(drone.getSerialNumber())).thenReturn(Optional.ofNullable(savedDrone));

        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.registerDrone(droneDto));
        verify(droneRepository, times(1)).findBySerialNumber(droneDto.getSerialNumber());
    }


    @Test
    void loadDrone() {

        when(droneRepository.findBySerialNumber("TR676")).thenReturn(Optional.of(savedDrone));

        LoadedDroneDetails loadedDroneDetails = LoadedDroneDetails.builder()
                .serialNumber(savedDrone.getSerialNumber())
                .medicationList(savedDrone.getMedications())
                .build();


        var actual = droneServiceImpl.loadDrone(drone.getSerialNumber(), medication.getCode());


        verify(droneRepository, times(1)).findBySerialNumber(drone.getSerialNumber());
        verify(droneRepository, times(2)).save(savedDrone);


        assertEquals(actual.getDto().getSerialNumber(), loadedDroneDetails.getSerialNumber());
        assertEquals(actual.getDto().getMedicationList(), loadedDroneDetails.getMedicationList());
    }


    @Test
    void getDroneMedicationItems() {

        MedicationDto medicationDto = medicationMapper.medicationToMedicationDtoMapper(medication);
        List<MedicationDto> medicationDtoList = new ArrayList<>();
        medicationDtoList.add(medicationDto);

        savedDrone.getMedications().add(medication);
        when(droneRepository.findBySerialNumber("TR676")).thenReturn(Optional.of(savedDrone));


        var actual = droneServiceImpl.getDroneMedicationItems("TR676");

        verify(droneRepository, times(1)).findBySerialNumber(drone.getSerialNumber());
        assertEquals(actual.getDto(), medicationDtoList);
    }


    @Test
    void getAvailableDrones() {

        List<Drone> droneList = new ArrayList<>();
        droneList.add(savedDrone);
        when(droneRepository.findAllByDroneStateAndBatteryCapacityGreaterThan(DroneState.IDLE, 25)).thenReturn(Optional.of(droneList));

        List<DroneDto> droneDtoList = droneList.stream().map(droneMapper::droneToDroneDtoMapper).toList();

        var actual = droneServiceImpl.getAvailableDrones();

        verify(droneRepository).findAllByDroneStateAndBatteryCapacityGreaterThan(DroneState.IDLE, 25);
        verify(droneRepository, Mockito.atLeastOnce()).findAllByDroneStateAndBatteryCapacityGreaterThan(DroneState.IDLE, 25);
        assertIterableEquals(droneDtoList, actual.getDto());
    }


    @Test
    void getDroneBatteryLevel() {
        when(droneRepository.getBatteryLevel("TR676")).thenReturn(Optional.of(savedDrone.getBatteryCapacity()));

        var actual = droneServiceImpl.getDroneBatteryLevel("TR676");

        verify(droneRepository).getBatteryLevel("TR676");
        verify(droneRepository, Mockito.atLeastOnce()).getBatteryLevel("TR676");
        assertEquals(savedDrone.getBatteryCapacity(), actual.getDto());
    }


    @Test
    void failedGetDroneBatteryLevel() {

        when(droneRepository.getBatteryLevel("TR676")).thenReturn(Optional.of(savedDrone.getBatteryCapacity()));

        assertThrows(DroneNotFoundException.class, () -> droneServiceImpl.getDroneBatteryLevel("PY895"));
        verify(droneRepository, times(1)).getBatteryLevel("PY895");
    }


    @Test
    void offloadDrone() {

        savedDrone.getMedications().add(medication);
        when(droneRepository.findBySerialNumber("TR676")).thenReturn(Optional.ofNullable(savedDrone));

        var actual =droneServiceImpl.offloadDrone("TR676");

        verify(droneRepository, times(1)).findBySerialNumber("TR676");
        verify(droneRepository, times(1)).save(savedDrone);
        assertEquals("Drone unloaded successfully", actual.getMessage());
    }


    @Test
    void findDroneBySerialNumber() {

        when(droneRepository.findBySerialNumber("TR676")).thenReturn(Optional.ofNullable(savedDrone));

        var actual = droneServiceImpl.findDroneBySerialNumber("TR676");

        verify(droneRepository, Mockito.atLeastOnce()).findBySerialNumber("TR676");
        assertEquals(savedDrone.getId(), actual.getId());
        assertEquals(savedDrone.getCreatedAt(), actual.getCreatedAt());
        assertEquals(savedDrone.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(savedDrone.getSerialNumber(), actual.getSerialNumber());
        assertEquals(savedDrone.getDroneState(), actual.getDroneState());
        assertEquals(savedDrone.getModel(), actual.getModel());
        assertEquals(savedDrone.getWeightLimit(), actual.getWeightLimit());
        assertEquals(savedDrone.getBatteryCapacity(), actual.getBatteryCapacity());
        assertEquals(savedDrone.getMedications(), actual.getMedications());
    }


    @Test
    void failedFindDroneBySerialNumber() {

        assertThrows(DroneNotFoundException.class, () -> droneServiceImpl.findDroneBySerialNumber("YUB68"));
        verify(droneRepository, times(1)).findBySerialNumber("YUB68");
    }


    @Test
    void findMedicationByCode() {

        var actual = droneServiceImpl.findMedicationByCode("GHGFJ78");

        verify(medicationRepository, Mockito.atLeastOnce()).findByCode("GHGFJ78");
        assertEquals(savedMedication.getId(), actual.getId());
        assertEquals(savedMedication.getCreatedAt(), actual.getCreatedAt());
        assertEquals(savedMedication.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(savedMedication.getName(), actual.getName());
        assertEquals(savedMedication.getWeight(), actual.getWeight());
        assertEquals(savedMedication.getImage(), actual.getImage());
        assertEquals(savedMedication.getCode(), actual.getCode());
    }


    @Test
    void failedFindMedicationByCode() {

        assertThrows(MedicationNotFoundException.class, () -> droneServiceImpl.findMedicationByCode("FED68"));
        verify(medicationRepository, times(1)).findByCode("FED68");
    }
}