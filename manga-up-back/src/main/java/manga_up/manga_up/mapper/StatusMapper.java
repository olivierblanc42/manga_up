package manga_up.manga_up.mapper;

import manga_up.manga_up.dto.status.StatusDto;
import manga_up.manga_up.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    private static final Logger LOGGER= LoggerFactory.getLogger(StatusMapper.class);

    public StatusDto toEntity(Status status) {
        LOGGER.info("Converting status to entity");
        return new StatusDto(
               status.getId(),
               status.getLabel());
    }


    public Status toModel(StatusDto statusDto) {
        LOGGER.info("Converting status to model");
        Status status = new Status();
        status.setId(statusDto.getId());
        status.setLabel(statusDto.getLabel());
        return status;
    }


}
