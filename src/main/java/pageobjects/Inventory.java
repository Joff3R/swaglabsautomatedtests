package pageobjects;

import com.codeborne.selenide.SelenideElement;
import org.assertj.core.api.AssertionsForInterfaceTypes;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static helper.UrlHelper.CART;
import static helper.UrlHelper.INVENTORY;
import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static util.FormatUtil.extractProductAmount;
import static util.FormatUtil.getInnerTextsFromLocatorList;
import static util.RandomUtil.generateRandomNumber;

public class Inventory {

    private static final String ADD_TO_CART = "#add-to-cart-";
    private static final String REMOVE = "#remove-";
    private static final String SAUCE_LABS = "sauce-labs-";
    private final SelenideElement backpackItemName = $(byText("Sauce Labs Backpack"));
    private final SelenideElement shoppingCartIconWithItemNumber = $(".shopping_cart_badge");
    private final SelenideElement shoppingCartIcon = $(".shopping_cart_link");
    private final SelenideElement sortingByNameAsc = $("option[value=az]");
    private final SelenideElement sortingByNameDesc = $("option[value=za]");
    private final SelenideElement sortingByPriceAsc = $("option[value=lohi]");
    private final SelenideElement sortingByPriceDesc = $("option[value=hilo]");
    private final SelenideElement backpackAddButton = $(ADD_TO_CART + SAUCE_LABS + "backpack");
    private final SelenideElement bikeLightAddButton = $(ADD_TO_CART + SAUCE_LABS + "bike-light");
    private final SelenideElement boltTshirtAddButton = $(ADD_TO_CART + SAUCE_LABS + "bolt-t-shirt");
    private final SelenideElement fleeceJacketAddButton = $(ADD_TO_CART + SAUCE_LABS + "fleece-jacket");
    private final SelenideElement onesieAddButton = $(ADD_TO_CART + SAUCE_LABS + "onesie");
    private final SelenideElement testTshirtAddButton = $(byXpath("//button[@id='add-to-cart-test.allthethings()-t-shirt-(red)']"));
    private final List<SelenideElement> addButtonList = Arrays.asList(
            backpackAddButton,
            bikeLightAddButton,
            boltTshirtAddButton,
            fleeceJacketAddButton,
            onesieAddButton,
            testTshirtAddButton);
    private final SelenideElement backpackRemoveButton = $(REMOVE + SAUCE_LABS + "backpack");
    private final SelenideElement bikeLightRemoveButton = $(REMOVE + SAUCE_LABS + "bike-light");
    private final SelenideElement boltTshirtRemoveButton = $(REMOVE + SAUCE_LABS + "bolt-t-shirt");
    private final SelenideElement fleeceJacketRemoveButton = $(REMOVE + SAUCE_LABS + "fleece-jacket");
    private final SelenideElement onesieRemoveButton = $(REMOVE + SAUCE_LABS + "onesie");
    private final SelenideElement testTshirtRemoveButton = $(byXpath("//button[@id='remove-test.allthethings()-t-shirt-(red)']"));
    private final List<SelenideElement> removeButtonList = Arrays.asList(
            backpackRemoveButton,
            bikeLightRemoveButton,
            boltTshirtRemoveButton,
            fleeceJacketRemoveButton,
            onesieRemoveButton,
            testTshirtRemoveButton);
    private final List<SelenideElement> inventoryItemNames = $$(".inventory_item_name");
    private final List<SelenideElement> inventoryItemPrices = $$(".inventory_item_price");

    public void clickBackpackAddButton() {
        backpackAddButton.click();
    }

    public void clickSortByNameAscButton() {
        sortingByNameAsc.click();
    }

    public void clickSortByNameDescButton() {
        sortingByNameDesc.click();
    }

    public void clickSortByPriceAscButton() {
        sortingByPriceAsc.click();
    }

    public void clickSortByPriceDescButton() {
        sortingByPriceDesc.click();
    }

    public void clickBackpackItemName() {
        backpackItemName.click();
    }

    public void backpackAddButtonShouldNotBeVisible() {
        backpackAddButton.shouldNotBe(visible);
    }

    public void shoppingCartIconWithItemNumberShouldNotBeVisible() {
        shoppingCartIconWithItemNumber.shouldNotBe(visible);
    }

    public void addAllProductsToCart() {
        addButtonList.forEach(SelenideElement::click);
    }

    public void allAddButtonsShouldNotBeVisible() {
        addButtonList.forEach(e -> e.shouldNotBe(visible));
    }

    public void allAddButtonsShouldBeVisible() {
        addButtonList.forEach(e -> e.shouldBe(visible));
    }

    public void allRemoveButtonsShouldBeVisible() {
        removeButtonList.forEach(e -> e.shouldBe(visible));
    }

    public void shoppingCartIconWithItemNumberShouldDisplayNumberOfAddedProducts(int products) {
        String productsNumber = String.valueOf(products);
        String shoppingCartBadgeNumber = shoppingCartIconWithItemNumber.innerText();
        assertThat(shoppingCartBadgeNumber).isEqualTo(productsNumber);
    }

    public void shoppingCartIsEmpty() {
        removeButtonList.stream()
                .filter(SelenideElement::exists)
                .forEach(SelenideElement::click);
    }

    public void itemsShouldBeSortedByNameAsc() {
        AssertionsForInterfaceTypes
                .assertThat(getInnerTextsFromLocatorList(inventoryItemNames))
                .isSorted();
    }

    public void itemsShouldBeSortedByNameDesc() {
        AssertionsForInterfaceTypes
                .assertThat(getInnerTextsFromLocatorList(inventoryItemNames))
                .isSortedAccordingTo(reverseOrder());
    }

    public void itemsShouldBeSortedByPriceAsc() {
        AssertionsForInterfaceTypes
                .assertThat(extractProductAmount(getInnerTextsFromLocatorList(inventoryItemPrices)))
                .isSorted();
    }

    public void itemsShouldBeSortedByPriceDesc() {
        AssertionsForInterfaceTypes
                .assertThat(extractProductAmount(getInnerTextsFromLocatorList(inventoryItemPrices)))
                .isSortedAccordingTo(reverseOrder());
    }

    public void itemPreviewPageIsOpened() {
        openRandomItemPreview();
        assertThat(url()).isNotEqualTo(INVENTORY);
    }

    public void cartPageIsOpened() {
        openCartPage();
        assertThat(url()).isEqualTo(CART);
    }

    public float sumInventoryPrices() {
        return extractProductAmount(getInnerTextsFromLocatorList(inventoryItemPrices))
                .stream()
                .reduce(0.0f, Float::sum);
    }

    private void openCartPage() {
        shoppingCartIcon.click();
    }

    private void openRandomItemPreview() {
        int itemNumber = generateRandomNumber(0, 5);
        inventoryItemNames.get(itemNumber).click();
    }
}
