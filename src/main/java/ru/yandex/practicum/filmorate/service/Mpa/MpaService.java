package ru.yandex.practicum.filmorate.service.Mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaStorage;

    public Collection<MpaDto> getAllMpa() {
        return mpaStorage.getAllMpa()
                .stream()
                .map(MpaMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public MpaDto getMpa(Short id) {
        Mpa mpa = mpaStorage.getMpa(id);
        return MpaMapper.mapToDto(mpa);
    }
}

