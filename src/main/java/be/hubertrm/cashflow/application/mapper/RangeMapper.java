package be.hubertrm.cashflow.application.mapper;

import be.hubertrm.cashflow.application.dto.RangeDto;

import java.util.List;
import java.util.stream.Collectors;

public class RangeMapper {

    public static RangeDto toRangeDto(List<List<Object>> range) {
        return new RangeDto(range.stream()
                .map((List<Object> rows) -> new RangeDto.RowDto(rows.stream()
                        .map(RangeDto.CellDto::from)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList()));
    }
}
