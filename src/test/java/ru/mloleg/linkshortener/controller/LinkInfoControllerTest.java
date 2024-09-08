package ru.mloleg.linkshortener.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.mloleg.linkshortener.AbstractTest;
import ru.mloleg.linkshortener.dto.*;
import ru.mloleg.linkshortener.dto.common.CommonRequest;
import ru.mloleg.linkshortener.dto.common.CommonResponse;
import ru.mloleg.linkshortener.dto.common.ValidationError;
import ru.mloleg.linkshortener.model.LinkInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LinkInfoControllerTest extends AbstractTest {

    @Test
    @Transactional
    public void when_postCreateShortLink_expect_success() throws Exception {
        CreateShortLinkRequest request = CreateShortLinkRequest.builder()
                .link(LINK)
                .endTime(END_TIME)
                .description(DESCRIPTION)
                .active(true)
                .build();

        CommonRequest<CreateShortLinkRequest> commonRequest = new CommonRequest<>(request);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isOk())
                .andReturn();

        CommonResponse<LinkInfoResponse> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        LinkInfoResponse linkInfoResponse = response.body();

        assertNotNull(linkInfoResponse);
        assertNotNull(linkInfoResponse.id());
        assertEquals(linkInfoResponse.link(), request.link());
        assertTrue(linkInfoResponse.endTime().isEqual(request.endTime()));
        assertEquals(linkInfoResponse.description(), request.description());
        assertEquals(linkInfoResponse.active(), request.active());
        assertEquals(request.link(), linkInfoResponse.link());

        Mockito.verify(linkInfoRepository, Mockito.times(1)).save(any());
    }

    @ParameterizedTest
    @MethodSource("postCreateShortLinkInvalidRequestSource")
    void when_postCreateShortLink_withInvalidRequest_expect_validationError(
            CreateShortLinkRequest createRequest, String validationErrorMessage) throws Exception {

        CommonRequest<CreateShortLinkRequest> request = new CommonRequest<>();
        request.setBody(createRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        CommonResponse<ValidationError> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals("Validation error", response.errorMessage());
        assertTrue(response.validationErrorList().stream()
                .anyMatch(e -> e.message().equals(validationErrorMessage)));
    }

    @Test
    @Transactional
    void when_filter_expect_success() throws Exception {
        String shortLink = "aaaaaa";

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setEndTime(END_TIME);
        linkInfo.setLink(LINK);
        linkInfo.setShortLink(shortLink);
        linkInfo.setDescription(DESCRIPTION);
        linkInfo.setActive(true);

        linkInfoRepository.save(linkInfo);

        FilterLinkInfoRequest filterRequest = FilterLinkInfoRequest.builder()
                .linkPart("google")
                .endTimeFrom(LocalDateTime.now())
                .endTimeTo(LocalDateTime.now().plusDays(7))
                .active(true)
                .descriptionPart("test")
                .pageableRequest(PageableRequest.builder().number(1).size(10).sorts(List.of()).build())
                .build();

        CommonRequest<FilterLinkInfoRequest> commonRequest = new CommonRequest<>(filterRequest);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/link-infos/filter")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isOk())
                .andReturn();

        CommonResponse<List<LinkInfoResponse>> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(1, response.body().size());
        assertEquals(shortLink, response.body().get(0).shortLink());
        assertEquals(LINK, response.body().get(0).link());
        assertEquals(DESCRIPTION, response.body().get(0).description());
        assertEquals(true, response.body().get(0).active());

        Mockito.verify(linkInfoRepository)
                .findByFilter(eq(filterRequest.linkPart()), eq(filterRequest.endTimeFrom()),
                        eq(filterRequest.endTimeTo()), eq(filterRequest.descriptionPart()),
                        eq(filterRequest.active()), any(Pageable.class));
    }

    @Test
    @Transactional
    void when_patchUpdate_expect_success() throws Exception {
        String shortLink = "aaaaaa";

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setEndTime(END_TIME);
        linkInfo.setLink(LINK);
        linkInfo.setShortLink(shortLink);
        linkInfo.setDescription(DESCRIPTION);
        linkInfo.setActive(true);

        linkInfoRepository.save(linkInfo);

        UpdateLinkInfoRequest filterRequest = UpdateLinkInfoRequest.builder()
                .id(linkInfo.getId())
                .link(LINK + "test")
                .endTime(END_TIME.plusDays(1))
                .description(DESCRIPTION + "test")
                .active(false)
                .build();

        CommonRequest<UpdateLinkInfoRequest> commonRequest = new CommonRequest<>(filterRequest);

        MvcResult mvcResult = mockMvc.perform(patch("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commonRequest)))
                .andExpect(status().isOk())
                .andReturn();

        CommonResponse<LinkInfoResponse> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(linkInfo.getId(), response.body().id());
        assertEquals(LINK + "test", response.body().link());
        assertEquals(END_TIME.plusDays(1), response.body().endTime());
        assertEquals(DESCRIPTION + "test", response.body().description());
        assertEquals(false, response.body().active());

        Mockito.verify(linkInfoRepository).findById(eq(linkInfo.getId()));
        Mockito.verify(linkInfoRepository).save(any(LinkInfo.class));
    }

    @ParameterizedTest
    @MethodSource("patchUpdateLinkInfoRequestSource")
    void when_patchUpdate_withInvalidRequest_expect_validationError(
            UpdateLinkInfoRequest updateRequest, String validationErrorMessage) throws Exception {

        CommonRequest<UpdateLinkInfoRequest> request = new CommonRequest<>();
        request.setBody(updateRequest);

        MvcResult mvcResult = mockMvc.perform(patch("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        CommonResponse<ValidationError> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals("Validation error", response.errorMessage());
        assertTrue(response.validationErrorList().stream()
                .anyMatch(e -> e.message().equals(validationErrorMessage)));
    }

    @Test
    @Transactional
    void when_deleteById_expect_success() throws Exception {
        String shortLink = "aaaaaa";

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setEndTime(END_TIME);
        linkInfo.setLink(LINK);
        linkInfo.setShortLink(shortLink);
        linkInfo.setDescription(DESCRIPTION);
        linkInfo.setActive(true);

        linkInfoRepository.save(linkInfo);

        IdRequest idRequest = new IdRequest(linkInfo.getId());

        CommonRequest<IdRequest> request = new CommonRequest<>();
        request.setBody(idRequest);

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        CommonResponse<LinkInfoResponse> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals(linkInfo.getId(), response.body().id());

        Mockito.verify(linkInfoRepository).deleteById(eq(linkInfo.getId()));
    }

    @Test
    void when_deleteById_withInvalidId_expect_validationError() throws Exception {
        IdRequest idRequest = new IdRequest(null);

        CommonRequest<IdRequest> request = new CommonRequest<>();
        request.setBody(idRequest);

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/link-infos")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        CommonResponse<ValidationError> response = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>(){});

        assertEquals("Validation error", response.errorMessage());
        assertTrue(response.validationErrorList().stream()
                .anyMatch(e -> e.message().equals("UUID should not be empty")));
    }

    public static Stream<Arguments> postCreateShortLinkInvalidRequestSource() {
        return Stream.of(
                Arguments.of(new CreateShortLinkRequest(null, END_TIME, DESCRIPTION, true), "Link must not be null"),
                Arguments.of(new CreateShortLinkRequest("", END_TIME, DESCRIPTION, true), "Link must not be null"),
                Arguments.of(new CreateShortLinkRequest("123456789", END_TIME, DESCRIPTION, true), "Link length should be between 10 and 4096"),
                Arguments.of(new CreateShortLinkRequest("wrong_url_pattern", END_TIME, DESCRIPTION, true), "Link does not match URL pattern"),
                Arguments.of(new CreateShortLinkRequest(LINK, LocalDateTime.now().minusDays(1), DESCRIPTION, true), "Expiration date should be in future"),
                Arguments.of(new CreateShortLinkRequest(LINK, END_TIME, "", true), "Description should not be empty"),
                Arguments.of(new CreateShortLinkRequest(LINK, END_TIME, null, true), "Description should not be empty"),
                Arguments.of(new CreateShortLinkRequest(LINK, END_TIME, DESCRIPTION, null), "Active flag should not be empty")
        );
    }

    public static Stream<Arguments> patchUpdateLinkInfoRequestSource() {
        return Stream.of(
                Arguments.of(new UpdateLinkInfoRequest(null, LINK, END_TIME, DESCRIPTION, true), "ID must not be null"),
                Arguments.of(new UpdateLinkInfoRequest(ID, "", END_TIME, DESCRIPTION, true), "Link must not be null"),
                Arguments.of(new UpdateLinkInfoRequest(ID, "123456789", END_TIME, DESCRIPTION, true), "Link length should be between 10 and 4096"),
                Arguments.of(new UpdateLinkInfoRequest(ID, "wrong_url_pattern", END_TIME, DESCRIPTION, true), "Link does not match URL pattern"),
                Arguments.of(new UpdateLinkInfoRequest(ID, LINK, LocalDateTime.now().minusDays(1), DESCRIPTION, true), "Expiration date should be in future"),
                Arguments.of(new UpdateLinkInfoRequest(ID, LINK, END_TIME, "", true), "Description should not be empty"),
                Arguments.of(new UpdateLinkInfoRequest(ID, LINK, END_TIME, null, true), "Description should not be empty"),
                Arguments.of(new UpdateLinkInfoRequest(ID, LINK, END_TIME, DESCRIPTION, null), "Active flag should not be empty")
        );
    }
}
