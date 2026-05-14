package runners.base;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-report.html", "json:target/cucumber-report.json"}
)
public abstract class BaseRunnerTest extends AbstractTestNGCucumberTests {}
