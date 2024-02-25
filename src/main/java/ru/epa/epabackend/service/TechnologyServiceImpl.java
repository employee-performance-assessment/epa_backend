package ru.epa.epabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.TechnologyDto;
import ru.epa.epabackend.exception.NotFoundException;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.model.Technology;
import ru.epa.epabackend.repository.TechnologyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService{
    private final TechnologyRepository technologyRepository;

    @Transactional
    public TechnologyDto createTechnology(Technology technology){
        return TechnologyMapper.toTechnologyDto(technologyRepository.save(technology));
    }

    @Transactional
    public TechnologyDto updateTechnology(TechnologyDto technologyDto, Long technologyId){
        Technology technology = technologyRepository.findById(technologyId)
                .orElseThrow(() -> new NotFoundException
                        (String.format("Технологии с id %d не существует", technologyId)));
        return TechnologyMapper.toTechnologyDto(technology);
    }

    @Transactional
    public List<TechnologyDto> getAllTechnologys(){
        return TechnologyMapper.toTechnologyDtoList(technologyRepository.findAll());
    }

    @Transactional
    public Technology getTechnologyById(Long technologyId){
        return technologyRepository.findById(technologyId)
                .orElseThrow(() -> new NotFoundException
                        (String.format("Технологии с id %d не существует", technologyId)));
    }

    @Transactional
    public TechnologyDto getTechnologyDtoById(Long technologyId){
        return TechnologyMapper.toTechnologyDto(getTechnologyById(technologyId));
    }

    @Transactional
    public void deleteTechnologyById(Long technologyId){
        technologyRepository.deleteById(technologyId);
    }


}
