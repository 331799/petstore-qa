package com.petstore.qa;

import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

@Log4j2
public class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext testContext) {
        log.info("Suite <" + testContext.getSuite().getName() + "> started");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext testContext) {
        log.info("Suite <" + testContext.getSuite().getName() + "> ended");
    }

    @BeforeTest(alwaysRun = true)
    public void beforeTest(ITestContext testContext) {
        log.info("Test <<" + testContext.getCurrentXmlTest().getName() + ">> started");
        log.info(Thread.currentThread().getName());
    }

    @AfterTest(alwaysRun = true)
    public void afterTest(ITestContext testContext) {
        log.info("Test <<" + testContext.getCurrentXmlTest().getName() + ">> finished");
    }
}
