package ru.skypro.lessons.springboot.weblibrary.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.weblibrary.model.Position;
import ru.skypro.lessons.springboot.weblibrary.repository.PositionRepository;

@Service
@AllArgsConstructor
public class PositionServiceImpl implements PositionService{
    private final PositionRepository positionRepository;

    @Override
    public void addPosition(Position position) {
        positionRepository.save(position);
    }
}
