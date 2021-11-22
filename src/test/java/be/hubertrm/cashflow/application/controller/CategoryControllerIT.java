package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.CashflowBaseIntegrationTest;
import be.hubertrm.cashflow.domain.core.model.Category;
import be.hubertrm.cashflow.domain.core.repository.CategoryRepository;
import be.hubertrm.cashflow.application.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoryControllerIT extends CashflowBaseIntegrationTest {

    public static final String CATEGORIES_PATH = API_PATH + "/categories";

    @Autowired
    private JacksonTester<CategoryDto> jsonCategory;
    @Autowired
    private JacksonTester<List<CategoryDto>> jsonCategoryList;
    @Autowired
    private CategoryRepository repository;

    @Test
    void given_noEntityExists_when_getAll_then_emptyList() throws Exception {
        mvc.perform(get(CATEGORIES_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonCategoryList.write(Collections.emptyList()).getJson()));
    }

    @Test
    @Sql("classpath:integrationTest/createCategory.sql")
    void given_multipleEntitiesExist_when_getAll_then_returnsAll() throws Exception {
        CategoryDto expected = new CategoryDto(1L, "name", LocalDate.of(2021, 1, 1));

        MockHttpServletResponse response = mvc.perform(get(CATEGORIES_PATH))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(jsonCategoryList.parse(response.getContentAsString()).getObject())
                .usingElementComparatorIgnoringFields("id").contains(expected);
    }

    @Test
    @Sql("classpath:integrationTest/createCategory.sql")
    void given_entityExists_when_getById_then_returnsEntity() throws Exception {
        Long id = 1L;
        CategoryDto expected = new CategoryDto(id, "name", LocalDate.of(2021, 1, 1));

        mvc.perform(get(CATEGORIES_PATH + "/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonCategory.write(expected).getJson()));
    }

    @Test
    void given_entityDoesNotExist_when_getById_then_NotFound() throws Exception {
        mvc.perform(get(CATEGORIES_PATH + "/" + 1L))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void given_entityToCreate_when_create_then_returnsEntityId() throws Exception {
        CategoryDto actual = new CategoryDto(null, "name", LocalDate.of(2021, 1, 1));

        mvc.perform(post(CATEGORIES_PATH)
                        .content(jsonCategory.write(actual).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @Sql("classpath:integrationTest/createCategory.sql")
    void given_entityExists_when_update_then_update() throws Exception {
        Long id = 1L;
        CategoryDto updated = new CategoryDto(id, "name updated", LocalDate.of(2021, 1, 1));
        Category expected = new Category(id, "name updated", LocalDate.of(2021, 1, 1));

        mvc.perform(put(CATEGORIES_PATH + "/" + id)
                        .content(jsonCategory.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id))
                .isPresent().contains(expected);
    }

    @Test
    @Sql("classpath:integrationTest/createCategory.sql")
    void given_entityExists_when_updateOnlyName_then_updateOnlyName() throws Exception {
        Long id = 1L;
        CategoryDto updated = new CategoryDto(id, "name updated", LocalDate.of(2021, 1, 1));
        Category expected = new Category(id, "name updated", LocalDate.of(2021, 1, 1));

        mvc.perform(put(CATEGORIES_PATH + "/" + id)
                        .content(jsonCategory.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id))
                .isPresent().contains(expected);
    }

    @Test
    void given_entityDoesNotExist_when_update_then_NotFound() throws Exception {
        CategoryDto updated = new CategoryDto(1L, "name updated", LocalDate.of(2021, 1, 1));

        mvc.perform(put(CATEGORIES_PATH + "/" + 1L)
                        .content(jsonCategory.write(updated).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        assertThat(repository.findById(1L)).isNotPresent();
    }

    @Test
    @Sql("classpath:integrationTest/createCategory.sql")
    void given_entityExists_when_deleted_then_doesNoLongerExist() throws Exception {
        Long id = 1L;
        mvc.perform(delete(CATEGORIES_PATH + "/" + id))
                .andExpect(status().is2xxSuccessful());

        assertThat(repository.findById(id)).isNotPresent();
    }
}