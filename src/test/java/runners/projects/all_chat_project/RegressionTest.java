package runners.projects.all_chat_project;

import io.cucumber.testng.CucumberOptions;
import runners.base.BaseRunnerTest;

@CucumberOptions(
        features = "classpath:features/all_chat_project",
        glue = {"stepdefinitions.all_chat_project", "hooks"},
        tags = "@regression"
)
public class RegressionTest extends BaseRunnerTest {}
