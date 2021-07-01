package mate.academy.spring.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import mate.academy.spring.dto.request.ConcertSessionRequestDto;
import mate.academy.spring.dto.response.ConcertSessionResponseDto;
import mate.academy.spring.model.ConcertSession;
import mate.academy.spring.service.ConcertSessionService;
import mate.academy.spring.service.mapper.ConcertSessionMapper;
import mate.academy.spring.util.DateTimePatternUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concert-sessions")
public class ConcertSessionController {
    public static final String DATE_PATTERN = DateTimePatternUtil.DATE_PATTERN;
    private final ConcertSessionService concertSessionService;
    private final ConcertSessionMapper concertSessionMapper;

    public ConcertSessionController(ConcertSessionService concertSessionService,
                                    ConcertSessionMapper concertSessionMapper) {
        this.concertSessionService = concertSessionService;
        this.concertSessionMapper = concertSessionMapper;
    }

    @PostMapping
    public ConcertSessionResponseDto add(@RequestBody @Valid ConcertSessionRequestDto requestDto) {
        ConcertSession concertSession = concertSessionMapper.mapToModel(requestDto);
        concertSessionService.add(concertSession);
        return concertSessionMapper.mapToDto(concertSession);
    }

    @GetMapping("/available")
    public List<ConcertSessionResponseDto> getAll(@RequestParam Long concertId,
                                                @RequestParam
                                                @DateTimeFormat(pattern = DATE_PATTERN)
                                                        LocalDate date) {
        return concertSessionService.findAvailableSessions(concertId, date)
                .stream()
                .map(concertSessionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ConcertSessionResponseDto update(@PathVariable Long id,
                                          @RequestBody @Valid ConcertSessionRequestDto requestDto) {
        ConcertSession concertSession = concertSessionMapper.mapToModel(requestDto);
        concertSession.setId(id);
        concertSessionService.update(concertSession);
        return concertSessionMapper.mapToDto(concertSession);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        concertSessionService.delete(id);
    }
}
