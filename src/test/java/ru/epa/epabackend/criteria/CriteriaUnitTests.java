package ru.epa.epabackend.criteria;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.criteria.RequestCriteriaDto;
import ru.epa.epabackend.mapper.CriteriaMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.repository.CriteriaRepository;
import ru.epa.epabackend.service.impl.CriteriaServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CriteriaUnitTests {
    private static final long ID_1 = 1L;
    @Mock
    private CriteriaRepository criteriaRepository;
    @Mock
    private CriteriaMapper criteriaMapper;
    @InjectMocks
    private CriteriaServiceImpl criteriaService;
    private Criteria criteria;
    private List<Criteria> criteriaList;
    private List<RequestCriteriaDto> requestCriteriaDtoList;

    @BeforeEach
    public void unit() {
        criteria = Criteria.builder()
                .id(ID_1)
                .name("criteria")
                .build();
        criteriaList = new ArrayList<>();
        requestCriteriaDtoList = new ArrayList<>();
        criteriaList.add(criteria);
    }

    @Test
    @DisplayName("Сохранение списка критериев оценок с вызовом репозитория")
    void shouldCreateCriteriaWhenCallRepository() {
        when(criteriaMapper.mapListToEntity(requestCriteriaDtoList)).thenReturn(criteriaList);
        when(criteriaRepository.saveAll(criteriaMapper.mapListToEntity(requestCriteriaDtoList))).thenReturn(criteriaList);
        List<Criteria> criteriaListResult = criteriaService.create(requestCriteriaDtoList);
        int expectedSize = 1;
        assertNotNull(criteriaListResult);
        assertEquals(expectedSize, criteriaListResult.size());
        verify(criteriaRepository,times(1)).saveAll(criteriaList);
    }

    @Test
    @DisplayName("Поиск критерия по Id с исключением Not Found Exception")
    void shouldFindByIdCriteriaWhenThrowNotFoundException() throws ValidationException {
        when(criteriaRepository.findById(ID_1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> criteriaService.findById(ID_1));
    }

    @Test
    @DisplayName("Поиск критерия по Id с вызовом репозитория")
    void shouldFindByIdCriteriaWhenCallRepository() {
        when(criteriaRepository.findById(criteria.getId())).thenReturn(Optional.ofNullable(criteria));
        Criteria criteriaResult = criteriaService.findById(this.criteria.getId());
        long expectedId = 1L;
        assertEquals(expectedId, criteriaResult.getId());
        verify(criteriaRepository, times(1)).findById(criteriaResult.getId());
    }
}

