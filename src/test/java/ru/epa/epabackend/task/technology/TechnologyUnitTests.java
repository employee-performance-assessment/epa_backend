package ru.epa.epabackend.task.technology;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
import ru.epa.epabackend.dto.technology.TechnologyResponseDto;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.model.Technology;
import ru.epa.epabackend.repository.TechnologyRepository;
import ru.epa.epabackend.service.impl.TechnologyServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TechnologyUnitTests {
    private static final long ID_1 = 1L;
    @Mock
    private TechnologyRepository technologyRepository;
    @Mock
    private TechnologyMapper technologyMapper;
    @InjectMocks
    private TechnologyServiceImpl technologyService;
    private Technology technology = new Technology();
    private TechnologyResponseDto technologyResponseDto = new TechnologyResponseDto();
    private TechnologyRequestDto technologyRequestDto = new TechnologyRequestDto();

    @BeforeEach
    public void unit() {
        technology = Technology.builder()
                .id(ID_1)
                .name("technology")
                .build();
        technologyResponseDto = TechnologyResponseDto.builder()
                .id(ID_1)
                .name("technologyResponseDto")
                .build();
        technologyRequestDto = TechnologyRequestDto.builder()
                .name("technologyRequestDto")
                .build();
    }

    @Test
    @DisplayName("Создание технологии с вызовом репозитория")
    void shouldCreateTechnologyWhenCallRepository() {
        when(technologyRepository.save(technology)).thenReturn(technology);
        when(technologyMapper.mapToEntity(technologyRequestDto)).thenReturn(technology);
        Technology technology = technologyService.create(technologyRequestDto);
        int expectedId = 1;
        assertNotNull(technology);
        assertEquals(expectedId, technology.getId());
        verify(technologyRepository, times(1)).save(this.technology);
    }

    @Test
    @DisplayName("Обновление технологии с вызовом репозитория")
    void shouldUpdateTechnologyWhenCallRepository() {
        when(technologyRepository.findById(ID_1)).thenReturn(Optional.ofNullable(technology));
        when(technologyRepository.save(technology)).thenReturn(technology);
        Technology technology = technologyService.update(technologyRequestDto, ID_1);
        int expectedId = 1;
        assertNotNull(technology);
        assertEquals(expectedId, technology.getId());
        verify(technologyRepository, times(1)).save(this.technology);
    }

    @Test
    @DisplayName("Поиск технологии по Id с исключением Not Found Exception")
    void shouldFindByIdTechnologyWhenThrowNotFoundException() throws ValidationException {
        when(technologyRepository.findById(ID_1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> technologyService.findById(ID_1));
    }

    @Test
    @DisplayName("Поиск технологии по Id с вызовом репозитория")
    void shouldFindByIdTechnologyWhenCallRepository() {
        when(technologyRepository.findById(technology.getId())).thenReturn(Optional.ofNullable(technology));
        Technology technology = technologyService.findById(this.technology.getId());
        int expectedId = 1;
        assertNotNull(technology);
        assertEquals(expectedId, technology.getId());
        verify(technologyRepository, times(1)).findById(technology.getId());
    }

    @Test
    @DisplayName("Список всех технологий с вызовом репозитория")
    void shouldFindAllTechnologyWhenCallRepository() {
        when(technologyRepository.findAll()).thenReturn(List.of(technology));
        List<Technology> targetTechnology = technologyService.findAll();
        assertNotNull(targetTechnology);
        assertEquals(1, targetTechnology.size());
        verify(technologyRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Удаление технологии")
    void shouldDeleteTechnologyWhen() {
        technologyService.delete(ID_1);
        verify(technologyRepository, times(1)).deleteById(ID_1);
    }
}
