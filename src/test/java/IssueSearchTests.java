import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;


public class IssueSearchTests {

    private static final String BASE_URL = "https://github.com";
    private static final String REPOSITORY_NAME = "eroshenkoam/allure-example";
    private static final String ISSUE_NAME = "с днем археолога!";

    private WebSteps steps = new WebSteps();

    @Test
    void IssueSearchTestSelenideOnly(){
            SelenideLogger.addListener("allure", new AllureSelenide());

            open(BASE_URL);

            $(".header-search-input").click();
            $(".header-search-input").sendKeys(REPOSITORY_NAME);
            $(".header-search-input").submit();

            $(By.linkText(REPOSITORY_NAME)).click();

            $(withText("Issues")).click();

            $(withText(ISSUE_NAME)).should(Condition.visible);
    }

    @Test
    void IssueSearchTestWithLambdaSteps(){
        step("Открываем главную страницу " + BASE_URL, (s) -> {
            s.parameter("URL", BASE_URL);
            open(BASE_URL);
        });

        step("Ищем репозиторий " + REPOSITORY_NAME, (s) -> {
            s.parameter("repository", REPOSITORY_NAME);
            $(".header-search-input").click();
            $(".header-search-input").sendKeys(REPOSITORY_NAME);
            $(".header-search-input").submit();
        });

        step("Переходим в репозиторий " + REPOSITORY_NAME, (s) -> {
            s.parameter("repository", REPOSITORY_NAME);
            $(By.linkText(REPOSITORY_NAME)).click();
        });

        step("Открываем таб Issues в репозитории", () -> {
            $(withText("Issues")).click();
        });

        step("Проверяем, что Issue с названием \"" + ISSUE_NAME + "\" существует", (s) -> {
            s.parameter("name", ISSUE_NAME);
            $(withText(ISSUE_NAME)).should(Condition.visible);
        });
    }

    @Test
    void IssueSearchTestWithAnnotatedSteps(){
        steps.openMainPage(BASE_URL);
        steps.searchForRepositoryByName(REPOSITORY_NAME);
        steps.goToRepository(REPOSITORY_NAME);
        steps.openIssuesTab();
        steps.shouldSeeIssueWithName(ISSUE_NAME);
    }



}
