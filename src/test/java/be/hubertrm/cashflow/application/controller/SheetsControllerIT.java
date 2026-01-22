package be.hubertrm.cashflow.application.controller;

import be.hubertrm.cashflow.CashflowBaseIntegrationTest;
import be.hubertrm.cashflow.application.dto.RangeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;

import static java.util.Collections.emptyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SheetsControllerIT extends CashflowBaseIntegrationTest {

    public static final String SHEETS_PATH = API_PATH + "/sheets";
    private final static String SPREADSHEET_ID = "11ZaB0hObUJnnlXp4GQXuqga63ezjm5rUTTNlniIlo0o";


    @Autowired
    private JacksonTester<RangeDto> jsonRange;
    @Test
    void given_noEntityExists_when_getAll_then_emptyList() throws Exception {
        mvc.perform(get(SHEETS_PATH + "/" + SPREADSHEET_ID)
                        .param("range", "Main!A2:O274"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonRange.write(new RangeDto(emptyList())).getJson()));
    }

}