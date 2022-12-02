package com.musalasoftdroneservice.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musalasoftdroneservice.dto.DroneDto;
import com.musalasoftdroneservice.dto.MedicationDto;
import com.musalasoftdroneservice.entity.Drone;
import com.musalasoftdroneservice.entity.Medication;
import com.musalasoftdroneservice.enums.DroneModel;
import com.musalasoftdroneservice.enums.DroneState;
import com.musalasoftdroneservice.exception.DroneAlreadyRegisteredException;
import com.musalasoftdroneservice.exception.DroneLowBatteryException;
import com.musalasoftdroneservice.exception.DroneNotFoundException;
import com.musalasoftdroneservice.exception.MedicationNotFoundException;
import com.musalasoftdroneservice.exception.NoDronesAvailableException;
import com.musalasoftdroneservice.mapper.DroneMapper;
import com.musalasoftdroneservice.mapper.MedicationMapper;
import com.musalasoftdroneservice.reponse.APIResponse;
import com.musalasoftdroneservice.repository.DroneRepository;
import com.musalasoftdroneservice.repository.MedicationRepository;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DroneServiceImpl.class})
@ExtendWith(SpringExtension.class)
class DroneServiceImplTest {
    @MockBean
    private DroneMapper droneMapper;

    @MockBean
    private DroneRepository droneRepository;

    @Autowired
    private DroneServiceImpl droneServiceImpl;

    @MockBean
    private MedicationMapper medicationMapper;

    @MockBean
    private MedicationRepository medicationRepository;

    private Drone drone;

    private Medication medication;

    private List<Medication> medicationList;

    private LocalDateTime time;


    @BeforeEach
    void setUp() {

        medicationList = new ArrayList<>();

        time = LocalDateTime.of(2022, Month.DECEMBER, 2, 7, 0, 0, 0);

        drone = mock(Drone.class);
        drone.setBatteryCapacity(1);
        drone.setCreatedAt(time);
        drone.setDroneState(DroneState.IDLE);
        drone.setId(123L);
        drone.setMedications(new ArrayList<>());
        drone.setModel(DroneModel.Lightweight);
        drone.setSerialNumber("42");
        drone.setUpdatedAt(time);
        drone.setWeightLimit(10.0d);


        medication = new Medication();
        medication.setCode("Medication items found");
        medication.setCreatedAt(time);
        medication.setId(123L);
        medication.setImage("Medication items found");
        medication.setName("Medication items found");
        medication.setUpdatedAt(time);
        medication.setWeight(10.0d);

    }


    @Test
    void testRegisterDrone() {

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getSerialNumber()).thenReturn("42");
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.registerDrone(droneDto));
        verify(droneRepository).findBySerialNumber((String) any());
        verify(droneDto, atLeast(1)).getSerialNumber();
    }


    @Test
    void testRegisterDrone2() {

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getSerialNumber()).thenThrow(new DroneAlreadyRegisteredException("An error occurred"));
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.registerDrone(droneDto));
        verify(droneDto).getSerialNumber();
    }


    @Test
    void testRegisterDrone4() {
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(Optional.empty());
        DroneDto droneDto = mock(DroneDto.class);
        when(droneDto.getModel()).thenThrow(new DroneAlreadyRegisteredException("An error occurred"));
        when(droneDto.getSerialNumber()).thenReturn("42");
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.registerDrone(droneDto));
        verify(droneRepository).findBySerialNumber((String) any());
        verify(droneDto).getModel();
        verify(droneDto, atLeast(1)).getSerialNumber();
    }


    @Test
    void testLoadDrone() {
        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        assertThrows(DroneLowBatteryException.class, () -> droneServiceImpl.loadDrone("42", "Code"));
        verify(droneRepository).findBySerialNumber((String) any());
    }


    @Test
    void testLoadDrone2() {

        when(drone.getBatteryCapacity()).thenReturn(1);
        doNothing().when(drone).setBatteryCapacity(anyInt());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        assertThrows(DroneLowBatteryException.class, () -> droneServiceImpl.loadDrone("42", "Code"));
        verify(droneRepository).findBySerialNumber((String) any());
        verify(drone).getBatteryCapacity();
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
    }


    @Test
    void testLoadDrone3() {
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(Optional.empty());

        when(drone.getBatteryCapacity()).thenReturn(1);
        doNothing().when(drone).setBatteryCapacity(anyInt());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());

        assertThrows(DroneNotFoundException.class, () -> droneServiceImpl.loadDrone("42", "Code"));
        verify(droneRepository).findBySerialNumber((String) any());
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
    }


    @Test
    void testGetDroneMedicationItems() {

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        APIResponse<List<MedicationDto>> actualDroneMedicationItems = droneServiceImpl.getDroneMedicationItems("42");
        assertEquals(medicationList, actualDroneMedicationItems.getDto());
        assertEquals("Medication items found", actualDroneMedicationItems.getMessage());
        verify(droneRepository).findBySerialNumber((String) any());
    }


    @Test
    void testGetDroneMedicationItems2() {

        when(drone.getMedications()).thenReturn(new ArrayList<>());
        doNothing().when(drone).setBatteryCapacity(anyInt());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        APIResponse<List<MedicationDto>> actualDroneMedicationItems = droneServiceImpl.getDroneMedicationItems("42");
        assertEquals(medicationList, actualDroneMedicationItems.getDto());
        assertEquals("Medication items found", actualDroneMedicationItems.getMessage());
        verify(droneRepository).findBySerialNumber((String) any());
        verify(drone).getMedications();
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
    }


    @Test
    void testGetDroneMedicationItems3() {


        ArrayList<Medication> medicationList = new ArrayList<>();
        medicationList.add(medication);

        when(drone.getMedications()).thenReturn(medicationList);
        doNothing().when(drone).setBatteryCapacity(anyInt());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        when(medicationMapper.medicationToMedicationDtoMapper((Medication) any())).thenReturn(mock(MedicationDto.class));
        APIResponse<List<MedicationDto>> actualDroneMedicationItems = droneServiceImpl.getDroneMedicationItems("42");
        assertEquals(1, actualDroneMedicationItems.getDto().size());
        assertEquals("Medication items found", actualDroneMedicationItems.getMessage());
        verify(droneRepository).findBySerialNumber((String) any());
        verify(drone).getMedications();
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
        verify(medicationMapper).medicationToMedicationDtoMapper((Medication) any());
    }


    @Test
    void testGetDroneMedicationItems4() {

        Medication medication1 = new Medication();
        medication1.setCode("Medication items found");
        medication1.setCreatedAt(time);
        medication1.setId(123L);
        medication1.setImage("Medication items found");
        medication1.setName("Medication items found");
        medication1.setUpdatedAt(time);
        medication1.setWeight(10.0d);

        ArrayList<Medication> medicationList = new ArrayList<>();
        medicationList.add(medication1);
        medicationList.add(medication);

        when(drone.getMedications()).thenReturn(medicationList);
        doNothing().when(drone).setBatteryCapacity(anyInt());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        when(medicationMapper.medicationToMedicationDtoMapper((Medication) any())).thenReturn(mock(MedicationDto.class));
        APIResponse<List<MedicationDto>> actualDroneMedicationItems = droneServiceImpl.getDroneMedicationItems("42");
        assertEquals(2, actualDroneMedicationItems.getDto().size());
        assertEquals("Medication items found", actualDroneMedicationItems.getMessage());
        verify(droneRepository).findBySerialNumber((String) any());
        verify(drone).getMedications();
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
        verify(medicationMapper, atLeast(1)).medicationToMedicationDtoMapper((Medication) any());
    }


    @Test
    void testGetDroneMedicationItems5() {
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(Optional.empty());


        ArrayList<Medication> medicationList = new ArrayList<>();
        medicationList.add(medication);

        when(drone.getMedications()).thenReturn(medicationList);
        doNothing().when(drone).setBatteryCapacity(anyInt());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());

        when(medicationMapper.medicationToMedicationDtoMapper((Medication) any())).thenReturn(mock(MedicationDto.class));
        assertThrows(DroneNotFoundException.class, () -> droneServiceImpl.getDroneMedicationItems("42"));
        verify(droneRepository).findBySerialNumber((String) any());
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
    }


    @Test
    void testGetDroneMedicationItems6() {

        ArrayList<Medication> medicationList = new ArrayList<>();
        medicationList.add(medication);

        when(drone.getMedications()).thenReturn(medicationList);
        doNothing().when(drone).setBatteryCapacity(anyInt());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        when(medicationMapper.medicationToMedicationDtoMapper((Medication) any()))
                .thenThrow(new DroneAlreadyRegisteredException("An error occurred"));
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.getDroneMedicationItems("42"));
        verify(droneRepository).findBySerialNumber((String) any());
        verify(drone).getMedications();
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
        verify(medicationMapper).medicationToMedicationDtoMapper((Medication) any());
    }


    @Test
    void testGetAvailableDrones() {
        ArrayList<Drone> droneList = new ArrayList<>();
        when(droneRepository.findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any()))
                .thenReturn(Optional.of(droneList));
        APIResponse<List<DroneDto>> actualAvailableDrones = droneServiceImpl.getAvailableDrones();
        assertEquals(droneList, actualAvailableDrones.getDto());
        assertEquals("Available Drones found", actualAvailableDrones.getMessage());
        verify(droneRepository).findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any());
    }


    @Test
    void testGetAvailableDrones2() {

        ArrayList<Drone> droneList = new ArrayList<>();
        droneList.add(drone);
        Optional<List<Drone>> ofResult = Optional.of(droneList);
        when(droneRepository.findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any()))
                .thenReturn(ofResult);
        when(droneMapper.droneToDroneDtoMapper((Drone) any())).thenReturn(mock(DroneDto.class));
        APIResponse<List<DroneDto>> actualAvailableDrones = droneServiceImpl.getAvailableDrones();
        assertEquals(1, actualAvailableDrones.getDto().size());
        assertEquals("Available Drones found", actualAvailableDrones.getMessage());
        verify(droneRepository).findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any());
        verify(droneMapper).droneToDroneDtoMapper((Drone) any());
    }


    @Test
    void testGetAvailableDrones3() {

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setCreatedAt(time);
        drone1.setDroneState(DroneState.IDLE);
        drone1.setId(123L);
        drone1.setMedications(new ArrayList<>());
        drone1.setModel(DroneModel.Lightweight);
        drone1.setSerialNumber("42");
        drone1.setUpdatedAt(time);
        drone1.setWeightLimit(0.25d);

        ArrayList<Drone> droneList = new ArrayList<>();
        droneList.add(drone1);
        droneList.add(drone);
        Optional<List<Drone>> ofResult = Optional.of(droneList);
        when(droneRepository.findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any()))
                .thenReturn(ofResult);
        when(droneMapper.droneToDroneDtoMapper((Drone) any())).thenReturn(mock(DroneDto.class));
        APIResponse<List<DroneDto>> actualAvailableDrones = droneServiceImpl.getAvailableDrones();
        assertEquals(2, actualAvailableDrones.getDto().size());
        assertEquals("Available Drones found", actualAvailableDrones.getMessage());
        verify(droneRepository).findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any());
        verify(droneMapper, atLeast(1)).droneToDroneDtoMapper((Drone) any());
    }


    @Test
    void testGetAvailableDrones4() {
        when(droneRepository.findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any()))
                .thenReturn(Optional.empty());
        when(droneMapper.droneToDroneDtoMapper((Drone) any())).thenReturn(mock(DroneDto.class));
        assertThrows(NoDronesAvailableException.class, () -> droneServiceImpl.getAvailableDrones());
        verify(droneRepository).findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any());
    }


    @Test
    void testGetAvailableDrones5() {

        ArrayList<Drone> droneList = new ArrayList<>();
        droneList.add(drone);
        Optional<List<Drone>> ofResult = Optional.of(droneList);
        when(droneRepository.findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any()))
                .thenReturn(ofResult);
        when(droneMapper.droneToDroneDtoMapper((Drone) any()))
                .thenThrow(new DroneAlreadyRegisteredException("An error occurred"));
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.getAvailableDrones());
        verify(droneRepository).findAllByDroneStateAndBatteryCapacityGreaterThan((DroneState) any(), (BigDecimal) any());
        verify(droneMapper).droneToDroneDtoMapper((Drone) any());
    }


    @Test
    void testGetDroneBatteryLevel() {
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        when(droneRepository.getBatteryLevel((String) any())).thenReturn(Optional.of(valueOfResult));
        APIResponse<BigDecimal> actualDroneBatteryLevel = droneServiceImpl.getDroneBatteryLevel("42");
        BigDecimal dto = actualDroneBatteryLevel.getDto();
        assertSame(valueOfResult, dto);
        assertEquals("Battery Level fetched", actualDroneBatteryLevel.getMessage());
        assertEquals("42", dto.toString());
        verify(droneRepository).getBatteryLevel((String) any());
    }


    @Test
    void testGetDroneBatteryLevel2() {
        when(droneRepository.getBatteryLevel((String) any())).thenReturn(Optional.empty());
        assertThrows(DroneNotFoundException.class, () -> droneServiceImpl.getDroneBatteryLevel("42"));
        verify(droneRepository).getBatteryLevel((String) any());
    }


    @Test
    void testGetDroneBatteryLevel3() {
        when(droneRepository.getBatteryLevel((String) any()))
                .thenThrow(new DroneAlreadyRegisteredException("An error occurred"));
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.getDroneBatteryLevel("42"));
        verify(droneRepository).getBatteryLevel((String) any());
    }


    @Test
    void testPeriodicBatteryHealthCheck() {
        APIResponse<?> actualPeriodicBatteryHealthCheckResult = droneServiceImpl
                .periodicBatteryHealthCheck(new ArrayList<>());
        assertNull(actualPeriodicBatteryHealthCheckResult.getDto());
        assertEquals("Drone battery health checked", actualPeriodicBatteryHealthCheckResult.getMessage());
    }


    @Test
    void testPeriodicBatteryHealthCheck2() {

        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setBatteryCapacity(anyInt());

        ArrayList<Drone> droneList = new ArrayList<>();
        droneList.add(drone);
        APIResponse<?> actualPeriodicBatteryHealthCheckResult = droneServiceImpl.periodicBatteryHealthCheck(droneList);
        assertNull(actualPeriodicBatteryHealthCheckResult.getDto());
        assertEquals("Drone battery health checked", actualPeriodicBatteryHealthCheckResult.getMessage());
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
    }


    @Test
    void testOffloadDrone() {
        Optional<Drone> ofResult = Optional.of(drone);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setCreatedAt(time);
        drone1.setDroneState(DroneState.IDLE);
        drone1.setId(123L);
        drone1.setMedications(new ArrayList<>());
        drone1.setModel(DroneModel.Lightweight);
        drone1.setSerialNumber("42");
        drone1.setUpdatedAt(time);
        drone1.setWeightLimit(10.0d);
        when(droneRepository.save((Drone) any())).thenReturn(drone1);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        APIResponse<?> actualOffloadDroneResult = droneServiceImpl.offloadDrone("42");
        assertNull(actualOffloadDroneResult.getDto());
        assertEquals("Drone unloaded successfully", actualOffloadDroneResult.getMessage());
        verify(droneRepository).save((Drone) any());
        verify(droneRepository).findBySerialNumber((String) any());
    }


    @Test
    void testOffloadDrone2() {
        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.save((Drone) any())).thenThrow(new DroneAlreadyRegisteredException("An error occurred"));
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.offloadDrone("42"));
        verify(droneRepository).save((Drone) any());
        verify(droneRepository).findBySerialNumber((String) any());
    }


    @Test
    void testOffloadDrone3() {

        when(drone.getMedications()).thenReturn(new ArrayList<>());
        doNothing().when(drone).setBatteryCapacity(anyInt());
        doNothing().when(drone).setDroneState((DroneState) any());
        doNothing().when(drone).setMedications((List<Medication>) any());
        doNothing().when(drone).setModel((DroneModel) any());
        doNothing().when(drone).setSerialNumber((String) any());
        doNothing().when(drone).setWeightLimit((Double) any());
        doNothing().when(drone).setCreatedAt((LocalDateTime) any());
        doNothing().when(drone).setId(anyLong());
        doNothing().when(drone).setUpdatedAt((LocalDateTime) any());

        Optional<Drone> ofResult = Optional.of(drone);

        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(1);
        drone1.setCreatedAt(time);
        drone1.setDroneState(DroneState.IDLE);
        drone1.setId(123L);
        drone1.setMedications(new ArrayList<>());
        drone1.setModel(DroneModel.Lightweight);
        drone1.setSerialNumber("42");
        drone1.setUpdatedAt(time);
        drone1.setWeightLimit(10.0d);
        when(droneRepository.save((Drone) any())).thenReturn(drone1);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        APIResponse<?> actualOffloadDroneResult = droneServiceImpl.offloadDrone("42");
        assertNull(actualOffloadDroneResult.getDto());
        assertEquals("Drone unloaded successfully", actualOffloadDroneResult.getMessage());
        verify(droneRepository).save((Drone) any());
        verify(droneRepository).findBySerialNumber((String) any());
        verify(drone, atLeast(1)).getMedications();
        verify(drone).setBatteryCapacity(anyInt());
        verify(drone, atLeast(1)).setDroneState((DroneState) any());
        verify(drone).setMedications((List<Medication>) any());
        verify(drone).setModel((DroneModel) any());
        verify(drone).setSerialNumber((String) any());
        verify(drone).setWeightLimit((Double) any());
        verify(drone).setCreatedAt((LocalDateTime) any());
        verify(drone).setId(anyLong());
        verify(drone).setUpdatedAt((LocalDateTime) any());
    }


    @Test
    void testFindDroneBySerialNumber() {

        Optional<Drone> ofResult = Optional.of(drone);
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(ofResult);
        assertSame(drone, droneServiceImpl.findDroneBySerialNumber("42"));
        verify(droneRepository).findBySerialNumber((String) any());
    }


    @Test
    void testFindDroneBySerialNumber2() {
        when(droneRepository.findBySerialNumber((String) any())).thenReturn(Optional.empty());
        assertThrows(DroneNotFoundException.class, () -> droneServiceImpl.findDroneBySerialNumber("42"));
        verify(droneRepository).findBySerialNumber((String) any());
    }


    @Test
    void testFindDroneBySerialNumber3() {
        when(droneRepository.findBySerialNumber((String) any()))
                .thenThrow(new DroneAlreadyRegisteredException("An error occurred"));
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.findDroneBySerialNumber("42"));
        verify(droneRepository).findBySerialNumber((String) any());
    }


    @Test
    void testFindMedicationByCode() {
        Optional<Medication> ofResult = Optional.of(medication);
        when(medicationRepository.findByCode((String) any())).thenReturn(ofResult);
        assertSame(medication, droneServiceImpl.findMedicationByCode("Code"));
        verify(medicationRepository).findByCode((String) any());
    }


    @Test
    void testFindMedicationByCode2() {
        when(medicationRepository.findByCode((String) any())).thenReturn(Optional.empty());
        assertThrows(MedicationNotFoundException.class, () -> droneServiceImpl.findMedicationByCode("Code"));
        verify(medicationRepository).findByCode((String) any());
    }


    @Test
    void testFindMedicationByCode3() {
        when(medicationRepository.findByCode((String) any()))
                .thenThrow(new DroneAlreadyRegisteredException("An error occurred"));
        assertThrows(DroneAlreadyRegisteredException.class, () -> droneServiceImpl.findMedicationByCode("Code"));
        verify(medicationRepository).findByCode((String) any());
    }
}

