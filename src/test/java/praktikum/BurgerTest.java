package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static praktikum.TestConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {

    private Burger burger;

    @Mock
    private Bun bun;

    @Mock
    private Ingredient ingredient1;

    @Mock
    private Ingredient ingredient2;

    @Before
    public void setUp() {
        burger = new Burger();
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(bun);
        assertEquals("Булочка должна быть установлена", bun, burger.bun);
    }

    @Test
    public void testAddIngredientIncreasesSize() {
        int initialSize = burger.ingredients.size();
        burger.addIngredient(ingredient1);
        assertEquals("Количество ингредиентов должно увеличиться на 1",
                initialSize + 1, burger.ingredients.size());
    }

    @Test
    public void testAddedIngredientIsPresent() {
        burger.addIngredient(ingredient1);
        assertTrue("Добавленный ингредиент должен присутствовать",
                burger.ingredients.contains(ingredient1));
    }

    @Test
    public void testAddMultipleIngredients() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        assertEquals("Должно быть 2 ингредиента", 2, burger.ingredients.size());
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(ingredient1);
        burger.removeIngredient(0);
        assertTrue("Список должен быть пуст после удаления",
                burger.ingredients.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientInvalidIndex() {
        burger.removeIngredient(0);
    }

    @Test
    public void testMoveIngredient() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);
        burger.moveIngredient(0, 1);
        assertEquals("Ингредиент должен переместиться на вторую позицию",
                ingredient1, burger.ingredients.get(1));
        assertEquals("Второй ингредиент должен стать первым",
                ingredient2, burger.ingredients.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientInvalidIndex() {
        burger.moveIngredient(0, 1);
    }

    @Test
    public void testGetPrice() {
        // Используем WHITE_BUN
        when(bun.getPrice()).thenReturn(WHITE_BUN_PRICE);
        when(ingredient1.getPrice()).thenReturn(SOUR_CREAM_PRICE);
        when(ingredient2.getPrice()).thenReturn(DINOSAUR_PRICE);

        burger.setBuns(bun);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        // Ожидаемая цена: (200*2) + 200 + 200 = 800
        float expectedPrice = WHITE_BUN_PRICE * 2 + SOUR_CREAM_PRICE + DINOSAUR_PRICE;
        assertEquals("Цена должна быть рассчитана корректно",
                expectedPrice, burger.getPrice(), 0.0);
    }

    @Test
    public void testGetReceipt() {
        // Используем RED_BUN и CHILI_SAUCE
        when(bun.getName()).thenReturn(RED_BUN_NAME);
        when(bun.getPrice()).thenReturn(RED_BUN_PRICE);
        when(ingredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(ingredient1.getName()).thenReturn(CHILI_SAUCE_NAME);
        when(ingredient1.getPrice()).thenReturn(CHILI_SAUCE_PRICE);

        burger.setBuns(bun);
        burger.addIngredient(ingredient1);

        String expectedReceipt = String.format(RECEIPT_STRUCTURE,
                RED_BUN_NAME, "sauce", CHILI_SAUCE_NAME,
                RED_BUN_NAME, RED_BUN_PRICE * 2 + CHILI_SAUCE_PRICE);

        assertEquals("Чек должен быть корректным",
                expectedReceipt, burger.getReceipt());
    }

    @Test
    public void testGetPriceWithDifferentBun() {
        // Тестируем BLACK_BUN с SAUSAGE
        when(bun.getPrice()).thenReturn(BLACK_BUN_PRICE);
        when(ingredient1.getPrice()).thenReturn(SAUSAGE_PRICE);

        burger.setBuns(bun);
        burger.addIngredient(ingredient1);

        // (100*2) + 300 = 500
        float expectedPrice = BLACK_BUN_PRICE * 2 + SAUSAGE_PRICE;
        assertEquals("Цена должна быть рассчитана с другой булочкой",
                expectedPrice, burger.getPrice(), 0.0);
    }

    @Test
    public void testGetReceiptWithFilling() {
        // Тестируем BLACK_BUN с CUTLET
        when(bun.getName()).thenReturn(BLACK_BUN_NAME);
        when(bun.getPrice()).thenReturn(BLACK_BUN_PRICE);
        when(ingredient1.getType()).thenReturn(IngredientType.FILLING);
        when(ingredient1.getName()).thenReturn(CUTLET_NAME);
        when(ingredient1.getPrice()).thenReturn(CUTLET_PRICE);

        burger.setBuns(bun);
        burger.addIngredient(ingredient1);

        String expectedReceipt = String.format(RECEIPT_STRUCTURE,
                BLACK_BUN_NAME, "filling", CUTLET_NAME,
                BLACK_BUN_NAME, BLACK_BUN_PRICE * 2 + CUTLET_PRICE);

        assertEquals("Чек должен быть корректным с начинкой",
                expectedReceipt, burger.getReceipt());
    }
}