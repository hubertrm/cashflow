package be.hubertrm.cashflow;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Slf4j
public abstract class CashflowBaseIntegrationTest {

    public static final String API_PATH = "/api/v1";

    @Autowired
    protected MockMvc mvc;

    @BeforeEach
    public void setup(TestInfo testInfo) {
        log.info("=====================================================");
        log.info("Begin Test " + testInfo.getDisplayName());
        log.info("=====================================================");
    }

    @AfterEach
    public void teardown(TestInfo testInfo) {
        log.info("=====================================================");
        log.info("End Test " + testInfo.getDisplayName());
        log.info("=====================================================");
    }
}
