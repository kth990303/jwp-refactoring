package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuFixture {
    private static final Long ID = 1L;
    private static final String NAME = "MENU_NAME";
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final Long MENU_GROUP_ID = 1L;

    private static final Long SEQ = 1L;
    private static final Long MENU_ID = 1L;
    private static final Long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu createMenu() {
        return createMenu(ID, NAME, PRICE, MENU_GROUP_ID, createMenuProducts());
    }

    public static Menu createMenu(BigDecimal price) {
        return createMenu(ID, NAME, price, MENU_GROUP_ID, createMenuProducts());
    }

    public static Menu createMenu(BigDecimal price, List<MenuProduct> menuProducts) {
        return createMenu(ID, NAME, price, MENU_GROUP_ID, menuProducts);
    }

    public static MenuProduct createMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(SEQ);
        menuProduct.setMenuId(MENU_ID);
        menuProduct.setProductId(PRODUCT_ID);
        menuProduct.setQuantity(QUANTITY);
        return menuProduct;
    }

    public static List<MenuProduct> createMenuProducts() {
        return Collections.singletonList(createMenuProduct());
    }
}