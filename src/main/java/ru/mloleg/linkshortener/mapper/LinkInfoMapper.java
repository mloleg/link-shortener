package ru.mloleg.linkshortener.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.mloleg.linkshortener.dto.CreateShortLinkRequest;
import ru.mloleg.linkshortener.dto.LinkInfoResponse;
import ru.mloleg.linkshortener.dto.UpdateShortLinkRequest;
import ru.mloleg.linkshortener.model.LinkInfo;

@Component
@Mapper(componentModel = "spring")
public interface LinkInfoMapper {

    LinkInfo toLinkInfo(CreateShortLinkRequest request);

    LinkInfo toLinkInfo(UpdateShortLinkRequest request);

    LinkInfoResponse toResponse(LinkInfo linkInfo);
}
