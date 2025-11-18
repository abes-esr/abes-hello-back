package fr.abes.helloabes.web;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class CustomTestExecutionListener implements TestExecutionListener, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(CustomTestExecutionListener.class);

    public void beforeTestClass(TestContext testContext) {
        logger.info(() -> ("beforeTestClass : " + testContext.getTestClass()));
    }

    public void prepareTestInstance(TestContext testContext) {
        logger.info(() -> ("prepareTestInstance : " + testContext.getTestClass()));
    }

    public void beforeTestMethod(TestContext testContext) {
        logger.info(() -> ("beforeTestMethod : " + testContext.getTestMethod()));
    }

    public void afterTestMethod(TestContext testContext) {
        logger.info(() -> ("afterTestMethod : " + testContext.getTestMethod()));
    }

    public void afterTestClass(TestContext testContext) {
        logger.info(() -> ("afterTestClass : " + testContext.getTestClass()));
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
